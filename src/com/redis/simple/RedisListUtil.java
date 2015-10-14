package com.redis.simple;

import java.util.List;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

import com.redis.simple.base.BaseRedis;

/**
* RedisListUtil
* @author Roger
*/
public  class RedisListUtil extends BaseRedis {

	private RedisListUtil(){
		
	}
	
     /**
     * 在前面加上一个或多个值的列表
     *
     * @param key
     *            key
     * @param strings
     *            values
     * @return
     */
     public static long lpush(String key, String... strings) {
          Jedis jedis = getJedis();
          Long data = jedis.lpush(key, strings);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在前面加上一个值列表，仅当列表中存在
     *
     * @param key
     *            key
     * @param strings
     *            values
     * @return
     */
     public static  long lpushx(String key, String... strings) {
          Jedis jedis = getJedis();
          Long data = jedis.lpushx(key, strings);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在结尾加上一个或多个值的列表
     *
     * @param key
     *            key
     * @param strings
     *            values
     * @return
     */
     public static  long rpush(String key, String... strings) {
          Jedis jedis = getJedis();
          Long data = jedis.rpush(key, strings);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在结尾加上一个或多个值的列表
     *
     * @param key
     *            key
     * @param strings
     *            values
     * @return
     */
     public static  long rpushx(String key, String... strings) {
          Jedis jedis = getJedis();
          Long data = jedis.rpushx(key, strings);
          returnJedis(jedis);
          return data;
     }

     /**
     * 从一个列表获取各种元素
     *
     * @param key
     *            key
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return List<String>
     */
     public static  List<String> lrange(String key, long start, long end) {
          Jedis jedis = getJedis();
          List<String> data = jedis.lrange(key, start, end);
          returnJedis(jedis);
          return data;
     }

     /**
     * 取出并获取列表中的最后一个元素
     *
     * @param key
     */
     public static  String rpop(String key) {
          Jedis jedis = getJedis();
          String data = jedis.rpop(key);
          returnJedis(jedis);
          return data;
     }

     /**
     * 取出并获取列表中的第一个元素，或阻塞，直到有可用
     *
     * @param timeout
     *            阻塞时间
     * @param key
     *            key
     * @return List<String>
     */
     public static  List<String> blpop(int timeout, String key) {
          Jedis jedis = getJedis();
          List<String> data = jedis.blpop(timeout, key);
          returnJedis(jedis);
          return data;
     }

     /**
     * 取出并获取列表中的最后一个元素，或阻塞，直到有可用
     *
     * @param timeout
     *            阻塞时间
     * @param key
     *            key
     * @return List<String>
     */
     public static  List<String> brpop(int timeout, String key) {
          Jedis jedis = getJedis();
          List<String> data = jedis.brpop(timeout, key);
          returnJedis(jedis);
          return data;
     }

     /**
     * 取出并获取列表中的最后一个元素，或阻塞，直到有可用
     *
     * @param source
     *            列表1
     * @param destination
     *            列表2
     * @param timeout
     *            执行其它命令时间
     * @return List<String>
     */
     public static  String brpoplpush(String source, String destination, int timeout) {
          Jedis jedis = getJedis();
          String data = jedis.brpoplpush(source, destination, timeout);
          returnJedis(jedis);
          return data;
     }

     /**
     * 从一个列表其索引获取对应的元素
     *
     * @param key
     * @param index
     * @return
     */
     public static  String lindex(String key, int index) {
          Jedis jedis = getJedis();
          String data = jedis.lindex(key, index);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在指定元素的左边边添加元素
     *
     * @param key
     *            key
     * @param pivot
     *            指定元素
     * @param value
     *            添加元素
     * @return
     */
     public static  Long linsertBefor(String key, String pivot, String value) {
          Jedis jedis = getJedis();
          long data = jedis.linsert(key, LIST_POSITION.BEFORE, pivot, value);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在指定元素的右边添加元素
     *
     * @param key
     *            key
     * @param pivot
     *            指定元素
     * @param value
     *            添加元素
     * @return 返回列表size
     */
     public static  Long linsertAfter(String key, String pivot, String value) {
          Jedis jedis = getJedis();
          long data = jedis.linsert(key, LIST_POSITION.AFTER, pivot, value);
          returnJedis(jedis);
          return data;
     }

     /**
     * 获取列表的长度
     *
     * @param key
     * @return
     */
     public static  long llen(String key) {
          Jedis jedis = getJedis();
          long data = jedis.llen(key);
          returnJedis(jedis);
          return data;
     }

     /**
     * 获取并取出列表中的第一个元素
     *
     * @param key
     * @return
     */
     public static  String lpop(String key) {
          Jedis jedis = getJedis();
          String data = jedis.lpop(key);
          returnJedis(jedis);
          return data;
     }

     /**
     * 从列表中删除元素
     *
     * @param key
     *            key
     * @param count
     *            要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
     * @param value
     *            要匹配的值
     * @return 删除后的List中的记录数
     */
     public static  long lrem(String key, long count, String value) {
          Jedis jedis = getJedis();
          Long data = jedis.lrem(key, count, value);
          returnJedis(jedis);
          return data;
     }

     /**
     * 在列表中的索引设置一个元素的值
     *
     * @param key
     *            key
     * @param index
     *            原有元素索引
     * @param value
     *            替换后的值
     * @return
     */
     public static  String lset(String key, long index, String value) {
          Jedis jedis = getJedis();
          String data = jedis.lset(key, index, value);
          returnJedis(jedis);
          return data;
     }

     /**
     * 修剪列表到指定的范围内
     *
     * @param key
     *            key
     * @param start
     *            保留开始元素的索引
     * @param stop
     *            保留结束元素的索引
     */
     public static  String ltrim(String key, long start, long end) {
          Jedis jedis = getJedis();
          String data = jedis.ltrim(key, start, end);
          returnJedis(jedis);
          return data;
     }

     public void set(String key,List<String> list){
    	 Jedis jedis = getJedis();
          returnJedis(jedis);
      }
     
}