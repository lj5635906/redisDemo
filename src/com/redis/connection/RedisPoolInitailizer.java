package com.redis.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
* 普通连接池
*
* @author Roger
*/
public class RedisPoolInitailizer extends RedisConfigHelper {

     /** 普通连接池 */
	 public static JedisPool pool;
	 
     public RedisPoolInitailizer() {
          getPool();
     }

     /**
     * 获取连接池
     *
     * @return JedisPool
     * */
     public JedisPool getPool() {
          if (pool == null) {
               initJedisPool();
          }
          return pool;
     }

     /**
     * 获取连接池资源
     *
     * @return Jedis
     * */
     public Jedis getPoolResource() {
          return getPool().getResource();
     }

     /**
     * 返回并释放链接池资源
     */
     public void returnPoolResource(Jedis jedis) {
          getPool().returnResourceObject(jedis);
     }

     /**
     * 初始化JedisPool
     */
     private void initJedisPool() {
          if (pool != null) {
               pool.close();
               pool.destroy();
          }
          // 获取服务器IP地址
          String ipStr = getResourceBundle().getString("redis.ip");
          // 服务器密码
          String password = getResourceBundle().getString("redis.password");
          // 获取服务器端口
          int portStr = Integer.valueOf(
                    getResourceBundle().getString("redis.port")).intValue();
          // 超时时间
          int timeout = Integer.valueOf(
        		  getResourceBundle().getString("redis.timeout")).intValue();
          // 初始化连接池
          pool = new JedisPool(getPoolConfig(), ipStr, portStr);
//          pool = new JedisPool(getPoolConfig(), ipStr, portStr, timeout, password);
     }

     /**
     * 从jedis连接池中获取获取jedis对象
     *
     * @return Jedis
     */
     public Jedis getJedis() {
          Jedis jedis = null;
          try {
               if (pool == null || pool.isClosed()) {
                    initJedisPool();
               }
               jedis = pool.getResource();
               return jedis;
          } catch (Exception e) {
               try {
                    e.printStackTrace();
                    if (jedis != null) {
                         jedis.close();
                    }
               } finally {
                    // 正确释放资源
                    if (jedis != null) {
                         jedis.close();
                    }
               }
          }
          return jedis;
     }

     /**
     * 销毁连接池
     */
     public void destroyAllPools() {
          if (pool != null) {
               pool.destroy();
          }
     }
}