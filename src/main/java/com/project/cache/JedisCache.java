package com.project.cache;
import java.util.ArrayList;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class JedisCache {
	private Jedis jedis;
	private static final int expirationTime = 86400;
	
	public JedisCache(){
		jedis = new JedisConnectionPool().getJedisConnection();
		// ignore: testing
		//jedis.flushAll();
	}
	
	public void SetCache(String key, ArrayList<String> values){
		for(int i = 0; i < values.size(); i++){
			jedis.zadd(key, i, values.get(i));
		}
		// Key expires in 1 day(86400 seconds)
		jedis.expire(key, expirationTime);
	}
	
	public Set<String> GetCachedValues(String key){
		return jedis.zrange(key, 0, -1);
	}
	
	public void Print(String key){
		System.out.println(key + ": "+jedis.smembers(key));
	}
	
	public boolean ContainsKey(String key){
		return jedis.exists(key);
	}
}
