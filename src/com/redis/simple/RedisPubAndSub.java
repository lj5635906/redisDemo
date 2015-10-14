package com.redis.simple;

import redis.clients.jedis.Jedis;
import com.redis.listener.PubSubListener;
import com.redis.simple.base.BaseRedis;

/**
 * redis发布和订阅
 * 
 * @author Roger
 */
public class RedisPubAndSub extends BaseRedis {

	private RedisPubAndSub() {
	}

	public static Jedis getJedis() {
		return BaseRedis.getJedis();
	}

	/**
	 * redis消息发布
	 * 
	 * @param channel
	 *            消息发布频道
	 * @param message
	 *            发布消息
	 * @return
	 */
	public static Long publish(String channel, String message) {
		Jedis jedis = getJedis();
		long data = jedis.publish(channel, message);
		returnJedis(jedis);
		return data;
	}

	/**
	 * redis订阅频道
	 * 
	 * @param channels
	 *            订阅频道
	 */
	public static void subscriber(PubSubListener pubSub,String... channels) {
		Jedis jedis = getJedis();
		jedis.subscribe(pubSub, channels);
	}

	/**
	 * redis订阅模式
	 * 
	 * @param patterns
	 *            订阅模式
	 */
	public static void psubscriber(PubSubListener pubSub,String... patterns) {
		Jedis jedis = getJedis();
		jedis.psubscribe(pubSub, patterns);
	}
}
