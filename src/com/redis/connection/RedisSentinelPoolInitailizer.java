package com.redis.connection;

import java.util.HashSet;
import java.util.Set;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * JedisSentinelPool 该连接池用于应对Redis的Sentinel的主从切换机制
 * 
 * @author Roger
 */
public class RedisSentinelPoolInitailizer extends RedisConfigHelper {

	public RedisSentinelPoolInitailizer() {
		initJedisSentinelPool();
	}

	/** JedisSentinelPool */
	public static JedisSentinelPool mSentinelpool;

	/** sentine池累积错误叠加次数 */
	private static int ERROR_COUNT = 0;
	/** sentine池累积错误叠加次数 */
	private static boolean reinitFlag = true;
	/** sentine是否正在启动 */
	private static boolean isReIniting = false;

	/** 内部类，用于实现lzay机制 */
	private static class SingletonHolder {
		/** 单例变量 */
		private static RedisSentinelPoolInitailizer instance = new RedisSentinelPoolInitailizer();
	}

	public static RedisSentinelPoolInitailizer getInstantce() {
		return SingletonHolder.instance;
	}

	/**
	 * 初始化JedisSentinelPool
	 * 
	 * @discrib 该连接池用于应对Redis的Sentinel的主从切换机制， 能够正确在服务器宕机导致服务器切换时得到正确的服务器连接，
	 *          当服务器采用该部署策略的时候推荐使用该连接池进行操作
	 * */
	public JedisSentinelPool initJedisSentinelPool() {
		if (mSentinelpool != null) {
			mSentinelpool.close();
			mSentinelpool.destroy();
		}
		// 监听器列表
		Set<String> sentinels = new HashSet<String>();
		// hosts+ports
		String sentinelhostandports = getSentinelHostandports();
		if (sentinelhostandports == null || sentinelhostandports.isEmpty()) {
			throw new IllegalArgumentException(
					"redis.sentinel.hostandports is not difined! can't create sentinelpool!");
		}
		String[] hostandports = sentinelhostandports.split(",");
		for (int i = 0; i < hostandports.length; i++) {
			HostAndPort hp = getNewHostAndPort(hostandports[i]);
			if (hp == null) {
				continue;
			}
			sentinels.add(hp.toString());
		}
		// 中间的server-1M即为这里的masterName
		String masterName = getSentinelMasterName();

		// 初始化连接池
		try {
			// mSentinelpool = new JedisSentinelPool(masterName, sentinels);
			mSentinelpool = new JedisSentinelPool(masterName, sentinels,
					getPoolConfig());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("try to reInit sentinelpool...");
			startReconnection(masterName, sentinels);
		}

		return mSentinelpool;
	}

	/**
	 * 获取Jedis连接
	 * 
	 * @return Jedsi
	 */
	public Jedis getJedis() {
		if (null == mSentinelpool) {
			mSentinelpool = initJedisSentinelPool();
		}
		return mSentinelpool.getResource();
	}

	/**
	 * 释放连接
	 * 
	 * @param resource
	 *            Jedis
	 */
	public void returnResource(Jedis resource) {
		mSentinelpool.returnResourceObject(resource);
	}

	private void startReconnection(final String masterName,
			final Set<String> sentinels) {
		// 监测是否关闭
		if (reinitFlag == false) {
			throw new IllegalArgumentException(
					"reInit sentinelpool has bean closed...");
		}
		// 错误次数累计加一
		ERROR_COUNT++;
		// 判断是否超过阀值
		if (ERROR_COUNT > getMaxErrorCount()) {
			throw new IllegalArgumentException(
					"reinit sentinelpool failed ！ (error count:" + ERROR_COUNT
							+ " is biger than max-error-count:"
							+ getMaxErrorCount() + ")");
		}
		// 什么进程
		Thread initThr = new Thread() {
			@Override
			public void run() {
				try {
					isReIniting = true;
					long delay = getNowErrorDelay(ERROR_COUNT);
					System.out.println(" sentinelpool will re-init["
							+ ERROR_COUNT + "] in " + delay / 1000
							+ " secondes...");
					// 延迟一定时间周期再次链接
					Thread.sleep(delay);
					// 执行连接
					mSentinelpool = new JedisSentinelPool(masterName,
							sentinels, getPoolConfig());
					// 判断是否成功
					if (mSentinelpool != null) {
						isReIniting = false;
						System.out
								.println("init sentinelpool success ！ (total cost re-init:"
										+ ERROR_COUNT + ")");
					} else {
						isReIniting = false;
						startReconnection(masterName, sentinels);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					isReIniting = false;
					startReconnection(masterName, sentinels);
				}
			}
		};
		// 执行
		if (isReIniting == false) {
			initThr.start();
		} else {
			System.out.println("init sentinelpool is running...");
		}
	}

	/**
	 * 获取新的链接地址和端口类
	 * 
	 * @param sentinelhostandport
	 * @return HostAndPort
	 * */
	private HostAndPort getNewHostAndPort(String sentinelhostandport) {
		if (sentinelhostandport == null || sentinelhostandport.isEmpty()) {
			return null;
		}
		String[] arry = sentinelhostandport.split(":");
		String host = "";
		int port = 26379;
		host = arry[0];
		if (arry.length >= 2) {
			try {
				port = Integer.valueOf(arry[1]);
			} catch (Exception e) {
				e.printStackTrace();
				port = 26379;
				System.out.println("user default port:26379 !");
			}
		}
		HostAndPort hostport = new HostAndPort(host, port);
		return hostport;
	}

	public void closeReinitThread() {
		reinitFlag = false;
	}

	public void openReinitThread() {
		reinitFlag = true;
	}

	public void closeSentinelpool() {
		if (mSentinelpool != null) {
			mSentinelpool.destroy();
			mSentinelpool = null;
		}
	}
}