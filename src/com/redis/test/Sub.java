package com.redis.test;

import com.redis.simple.RedisPubAndSub;
import com.redis.simple.RedisUtil;

public class Sub {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(RedisPubAndSub.publish("testChannel", "redis订阅频道测试2"));
		
		
		System.out.println(RedisUtil.get("test"));
		
	}

}
