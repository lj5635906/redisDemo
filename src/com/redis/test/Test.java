package com.redis.test;

import redis.clients.jedis.Jedis;

import com.redis.simple.RedisUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();

		Jedis jedis = new Jedis("192.168.20.217", 6379);
		for (int i = 10000; i < 20000; i++) {
//			jedis.set("str" + i, "value" + i);
			RedisUtil.set("str" + i, "value" + i);
		}
		jedis.close();
		
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}

}
