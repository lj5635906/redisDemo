package com.redis.connection;

import redis.clients.jedis.JedisShardInfo;

public class JedisShard {

	public static void main(String[] args){
		

		JedisShardInfo shard = new JedisShardInfo("192.168.20.217", 6379);
		shard.setPassword("password1");
		shard.setTimeout(3000);
		
		
		System.out.println(shard.createResource().get("test"));
		
	}
	
	private void init(){
		
		
		
		JedisShardInfo shard = new JedisShardInfo("192.168.20.217", 6379);
		shard.setPassword("password1");
		shard.setTimeout(3000);
		
	}
	
	
}
