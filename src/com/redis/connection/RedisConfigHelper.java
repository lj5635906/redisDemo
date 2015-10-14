package com.redis.connection;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.redis.constant.ConstantRedis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 读取Redis配置文件
 * 
 * @author Roger
 */
public class RedisConfigHelper {
	/** 配置文件ResourceBundle */
	private static ResourceBundle bundle;
	/** ResourceBundle指向地址 */
	private static String bundleLocal;
	/** 连接池配置实例 */
	private static JedisPoolConfig config;

	/** 最大错误阀值 */
	public static final String REDIS_SENTINEL_MAXERRORCOUNT = "redis.sentinel.maxErrorCount";
	/** 错误迭代周期 */
	public static final String REDIS_SENTINEL_ERRORDELAY = "redis.sentinel.errorDelay";
	/** 链接端口 */
	public static final String REDIS_SENTINEL_HOSTANDPORTS = "redis.sentinel.hostandports";
	/** 主机名 */
	public static final String REDIS_SENTINEL_MASTERNAME = "redis.sentinel.masterName";

	/** sentine池累积错误叠加次数最大阀值(默认) */
	public static final int SENTINEL_DEFAULT_MAX_ERROR_COUNT = 10;
	/** sentine池累积错误叠加延迟周期：10秒（默认） */
	public static final int SENTINEL_DEFAULT_ERROR_DELAY = 10 * 1000;

	/**
	 * 获取化连接池配置
	 *
	 * @return JedisPoolConfig
	 * */
	public JedisPoolConfig getPoolConfig() {
		if (config == null) {
			config = new JedisPoolConfig();
			// 最大连接数
			config.setMaxTotal(Integer.valueOf(getResourceBundle().getString(
					"redis.pool.maxTotal")));
			// 最大空闲连接数
			config.setMaxIdle(Integer.valueOf(getResourceBundle().getString(
					"redis.pool.maxIdle")));
			// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
			// 默认-1
			config.setMaxWaitMillis(Long.valueOf(getResourceBundle().getString(
					"redis.pool.maxWaitMillis")));
			// 在获取连接的时候检查有效性, 默认false
			config.setTestOnBorrow(Boolean.valueOf(getResourceBundle()
					.getString("redis.pool.testOnBorrow")));
			// 在获取返回结果的时候检查有效性, 默认false
			config.setTestOnReturn(Boolean.valueOf(getResourceBundle()
					.getString("redis.pool.testOnReturn")));
		}
		return config;
	}

	/**
	 * 使用默认工程路径配置文件
	 * */
	private ResourceBundle getDefaultProjectLocalBundle() {
		try {
			// 使用默认工程路径配置文件
			System.out
					.println("use the default project local [redis.properties]!--:" + ConstantRedis.CONNECTION_BUNDLE_PROPERTIES_FIRST);
			ResourceBundle bundle = ResourceBundle
					.getBundle(ConstantRedis.CONNECTION_BUNDLE_PROPERTIES_FIRST);
			// 如果为空，则使用包内置配置文件
			if (bundle == null) {
				System.out
						.println("use the default inside propertise [redis.properties]!--:"+ConstantRedis.CONNECTION_BUNDLE_PROPERTIES);
				bundle = ResourceBundle
						.getBundle(ConstantRedis.CONNECTION_BUNDLE_PROPERTIES);
			}
			return bundle;
		} catch (MissingResourceException e) {
			// 如果未发现工程配置文件，则使用包内置配置文件
			System.out
					.println("Missing project properties, use inside propertise [redis.properties]!");
			return ResourceBundle
					.getBundle(ConstantRedis.CONNECTION_BUNDLE_PROPERTIES);
		}
	}

	/**
	 * 获取配置文件资源
	 *
	 * @return ResourceBundle
	 * */
	public ResourceBundle getResourceBundle() {
		// 使用设置配置路径
		if (bundle == null && bundleLocal != null && !bundleLocal.isEmpty()) {
			bundle = ResourceBundle.getBundle(bundleLocal);
			return bundle;
		}
		// 使用默认工程路径配置文件
		if (bundle == null) {
			bundle = getDefaultProjectLocalBundle();
			return bundle;
		}
		// 抛异常
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		return bundle;
	}

	/**
	 * 获取值
	 * */
	public String getBundleString(String key) {
		return getResourceBundle().getString(key);
	}

	/**
	 * 设置配置文件资源
	 *
	 * @return ResourceBundle
	 * */
	public void setBundleLocal(String local) {
		RedisConfigHelper.bundleLocal = local;
	}

	/**
	 * 设置配置文件资源
	 *
	 * @return ResourceBundle
	 * */
	public void setBundle(ResourceBundle bundle) {
		RedisConfigHelper.bundle = bundle;
	}

	/**
	 * 获取最大错误阀值
	 * */
	public int getMaxErrorCount() {
		if (getResourceBundle() == null
				|| !getResourceBundle().containsKey(
						REDIS_SENTINEL_MAXERRORCOUNT)) {
			return SENTINEL_DEFAULT_MAX_ERROR_COUNT;
		} else {
			int maxcount = Integer.valueOf(bundle
					.getString(REDIS_SENTINEL_MAXERRORCOUNT));
			if (maxcount < 0) {
				maxcount = 0;
			}
			return maxcount;
		}
	}

	/**
	 * 获取错误延展周期
	 * */
	public long getErrorDelay() {
		if (getResourceBundle() == null
				|| !getResourceBundle().containsKey(REDIS_SENTINEL_ERRORDELAY)) {
			return SENTINEL_DEFAULT_ERROR_DELAY;
		} else {
			int times = Integer.valueOf(bundle
					.getString(REDIS_SENTINEL_ERRORDELAY));
			if (times < 0) {
				times = 0;
			} else {
				times = times * 1000;
			}
			return times;
		}
	}

	/**
	 * 获取当前累积延时周期
	 * */
	public long getNowErrorDelay(int count) {
		return getErrorDelay() * count;
	}

	public String getSentinelMasterName() {
		return getBundleString(REDIS_SENTINEL_MASTERNAME);
	}

	public String getSentinelHostandports() {
		return getBundleString(REDIS_SENTINEL_HOSTANDPORTS);
	}
}