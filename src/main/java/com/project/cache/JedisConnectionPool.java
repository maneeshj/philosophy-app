package com.project.cache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisConnectionPool {
	private static JedisPool jedisPool;
	
	public JedisConnectionPool(){
		jedisPool = null;
	}
		
	public synchronized Jedis getJedisConnection(){
		try{
			if(jedisPool == null){
				JedisPoolConfig config = new JedisPoolConfig();
				jedisPool = new JedisPool(config, "localhost");
			}
			return jedisPool.getResource();
		}catch(JedisConnectionException e){
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	public synchronized void close() {
		if (jedisPool != null) {
			try{
				jedisPool.close();				
			}catch(JedisConnectionException e){
				System.out.println(e.getMessage());
				throw e;
			}			
		}
	}
}
