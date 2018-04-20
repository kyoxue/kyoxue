package com.ilib.service.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisLinesCacheService<T> {
	@Autowired
	public RedisTemplate redisLineTemplate;

	public <T> ValueOperations<String, T> setCacheObject(String key, T value, Long timeout) {
		ValueOperations<String, T> operation = redisLineTemplate.opsForValue();
		if (null == timeout) {
			operation.set(key, value);
		} else {
			operation.set(key, value, timeout, TimeUnit.MINUTES);
		}
		return operation;
	}
	public boolean isClosed(){
		return redisLineTemplate.getConnectionFactory().getConnection().isClosed();
	}
	public <T> T getCacheObject(String key) {
		ValueOperations<String, T> operation = redisLineTemplate.opsForValue();
		return operation.get(key);
	}

	public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, Long timeout) {
		ListOperations listOperation = redisLineTemplate.opsForList();
		if (null != dataList) {
			int size = dataList.size();
			for (int i = 0; i < size; i++) {
				listOperation.rightPush(key, dataList.get(i));
			}
			redisLineTemplate.expire(key, timeout, TimeUnit.MINUTES);
		}
		return listOperation;
	}

	/**
	 * 获得缓存的list对象
	 * 
	 * @param key 缓存的键值
	 * @return 缓存键值对应的数据
	 */

	public <T> List<T> getCacheList(String key) {
		List<T> dataList = new ArrayList<T>();
		ListOperations<String, T> listOperation = redisLineTemplate.opsForList();
		Long size = listOperation.size(key);
		for (int i = 0; i < size; i++) {
			dataList.add(listOperation.index(key, i));
		}
		return dataList;
	}

	/**
	 * 缓存Set
	 * 
	 * @param key 缓存键值
	 * @param dataSet 缓存的数据
	 * @return 缓存数据的对象
	 */

	public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
		BoundSetOperations<String, T> setOperation = redisLineTemplate.boundSetOps(key);
		Iterator<T> it = dataSet.iterator();
		while (it.hasNext()) {
			setOperation.add(it.next());
		}
		return setOperation;
	}

	/**
	 * 获得缓存的set
	 * 
	 * @param key
	 * @param operation
	 * @return
	 */

	public Set<T> getCacheSet(String key) {
		BoundSetOperations<String, T> operation = redisLineTemplate.boundSetOps(key);
		return operation.members();
	}

	/**
	 * 缓存Map
	 * 
	 * @param key
	 * @param dataMap
	 * @return
	 */

	public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {
		HashOperations hashOperations = redisLineTemplate.opsForHash();
		if (null != dataMap) {
			for (Map.Entry<String, T> entry : dataMap.entrySet()) {
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}
		}
		return hashOperations;
	}

	/**
	 * 获得缓存的Map
	 * 
	 * @param key
	 * @param hashOperation
	 * @return
	 */

	public <T> Map<String, T> getCacheMap(String key) {
		Map<String, T> map = redisLineTemplate.opsForHash().entries(key);
		return map;
	}

	/**
	 * 缓存Map
	 * 
	 * @param key
	 * @param dataMap
	 * @return
	 */

	public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
		HashOperations hashOperations = redisLineTemplate.opsForHash();
		if (null != dataMap) {
			for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}
		}
		return hashOperations;
	}

	/**
	 * 获得缓存的Map
	 * 
	 * @param key
	 * @param hashOperation
	 * @return
	 */

	public <T> Map<Integer, T> getCacheIntegerMap(String key) {
		Map<Integer, T> map = redisLineTemplate.opsForHash().entries(key);
		return map;
	}

	public void removeKey(String key) {
		redisLineTemplate.delete(key);
	}
}
