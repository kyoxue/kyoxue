package com.ilib.test;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ilib.testcommon.BaseTest;

public class RedisTest extends BaseTest{
	Logger log = LoggerFactory.getLogger(RedisTest.class);
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private StringRedisSerializer stringRedisSerializer;
	@Autowired
	private JdkSerializationRedisSerializer jdkSerializationRedisSerializer;
	private ValueOperations<String, String> opsForValue;
	private ValueOperations<String, Long> opsForValueLong;
	private HashOperations<String, Object, Object> opsForHash;
	private ListOperations<String, String> opsForList;
	private SetOperations<String, String> opsForSet;
	//private ZSetOperations<String, String> opsForZSet;
	@Before
	public void before(){
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setStringSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);//类似spring mvc的messageConvert，自动值转成String
		opsForValue = redisTemplate.opsForValue();
		opsForValueLong = redisTemplate.opsForValue();
        opsForHash = redisTemplate.opsForHash();
        opsForList = redisTemplate.opsForList();
        opsForSet = redisTemplate.opsForSet();
        //opsForZSet = redisTemplate.opsForZSet();

	}
	@Test
	public void testValue(){//简单K-V存储
//		String key = "test.value.key";
//		String keyLong = "test.value.key.long";
		/**========================================================================================**/
//		String value = new String("value1");
//		//添加或者覆盖vlaue缓存
//		opsForValue.set(key, value);
//		//添加或者覆盖vlaue缓存，缓存维持5秒
//		opsForValue.set(key, "value2", 5, TimeUnit.SECONDS);
		/**========================================================================================**/
//		//long类型相加，注意第一次放入缓存就用increment而不是set，后面都用increment来相加
//		Long longval = opsForValueLong.increment(keyLong, new Long(100L));
//		log.info("longval====================>"+longval);
		/**========================================================================================**/
//		String value2 = new String("value2");
//		//用来验证是否重复key
//		//false KEY重复，不做改动，如果要相同KEY改成现在的vlaue，则用opsForValue.set(key, value);覆盖。
//		//true Key不存在，新增缓存vlaue
//		boolean unexists = opsForValue.setIfAbsent(key, value);
//		if (unexists) {
//			log.info("插入value缓存成功！");
//		}else{
//			log.info("value缓存已经存在！");
//			opsForValue.set(key, value2);
//		}
		/**========================================================================================**/
//		String value3 = new String("value3");
//		//没有新建，有则返回，还是感觉用上面if判断靠谱
//		String getAndSetVal = opsForValue.getAndSet(key, value3);
//		log.info("getAndSetVal===============>"+getAndSetVal);
		/**========================================================================================**/
//		//取缓存的value
//		String val =opsForValue.get(key, 0, 6);//有点问题，值有乱码
//		String val = opsForValue.get(key);
//		log.info("get===============>"+val);
		/**========================================================================================**/
		//value的大小
//		Long size = opsForValue.size(key);//有点问题，值有乱码，大小和真实值不一致 
//		log.info("size===============>"+size);
		/**========================================================================================**/
//		//追加内容，注意第一次放入缓存就用append而不是set，后面都用append来追加
//		Integer integer = opsForValue.append(key, "追加内容");
//		log.info("append===============>"+integer);
//		String appendval = opsForValue.get(key);
//		log.info("append===============>"+appendval);
		/**========================================================================================**/
		
	}
	@After
	public void after(){
		
	}
}
