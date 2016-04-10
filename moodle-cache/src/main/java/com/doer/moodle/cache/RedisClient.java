package com.doer.moodle.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.doer.moodle.cache.core.RedisClientTemplate;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * redis操作
 * 
 * @author lixiongcheng
 *
 */
@Repository
public class RedisClient {
	// private static final Logger log = Logger.getLogger(RedisClient.class);

	@Autowired
	private RedisClientTemplate clientTemplate;

	/*********************************************************
	 * redis对key的操作
	 *********************************************************/
	/**
	 * redis key所对应值得类型
	 * 
	 * @param key
	 * @return
	 */
	public String type(String key) {
		notExsits(key);
		return clientTemplate.type(key);
	}

	/**
	 * 删除某个key
	 * 
	 * @param key
	 * @return
	 */
	public long del(String key) {
		notExsits(key);
		return clientTemplate.del(key);
	}

	/**
	 * key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		return clientTemplate.exists(key);
	}

	/**
	 * 查询key的生命周期</br>
	 * 不存在的key,返回-2,已过期的key/不过期的key,都返回-1</br>
	 * 对于设置过过期时间的key返回有效期的秒数
	 * 
	 * @param key
	 * @return
	 */
	public long ttl(String key) {
		notExsits(key);
		return clientTemplate.ttl(key);
	}

	/**
	 * key多少秒后过期
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int seconds) {
		notExsits(key);
		return clientTemplate.expire(key, seconds);
	}

	public long expireAt(String key, long seconds) {
		notExsits(key);
		return clientTemplate.expireAt(key, seconds);
	}

	public Set<String> keys(String key) {
		return clientTemplate.hkeys(key);
	}

	/***************************************************************************
	 * redis 对字符串操作
	 ***********************************************************************/
	/**
	 * 存储字符串，失败抛异常
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		clientTemplate.set(key, value);
	}

	/**
	 * 更新key，保持key的有效期
	 * 
	 * @param key
	 * @param value
	 */
	public void update(String key, String value) {
		notExsits(key);
		clientTemplate.setex(key, (int) ttl(key), value);
	}

	public void append(String key, String value) {
		clientTemplate.append(key, value);
	}

	/**
	 * 获取部分字符串
	 * 
	 * @param key
	 * @param startOffset
	 * @param endOffset
	 */
	public void getrang(String key, int startOffset, int endOffset) {
		notExsits(key);
		clientTemplate.getrange(key, startOffset, endOffset);
	}

	public String get(String key) {
		notExsits(key);
		return clientTemplate.get(key);
	}

	/**
	 * 获取旧值，设置新值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String getset(String key, String value) {
		notExsits(key);
		return clientTemplate.getSet(key, value);
	}

	public long incr(String key) {
		notExsits(key);
		return clientTemplate.incr(key);
	}

	public long incrBy(String key, long integer) {
		notExsits(key);
		return clientTemplate.incrBy(key, integer);
	}

	public long decr(String key) {
		notExsits(key);
		return clientTemplate.decr(key);
	}

	public long decrBy(String key, long integer) {
		notExsits(key);
		return clientTemplate.decrBy(key, integer);
	}

	/************************************************************
	 * redis 对链表(link)操作
	 ***********************************************************/

	/**
	 * 链表左边推入数据
	 * 
	 * @param key
	 * @param values
	 * @return 推入数据后链表长度
	 */
	public long lpush(String key, String... values) {
		isNull(key);
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				throw new RuntimeException("第" + i + "个参数为null");
			}
		}
		long count = 0;
		for (String value : values) {
			clientTemplate.lpush(key, value);
			count++;
		}
		return count;
	}

	/**
	 * 链表右边推入数据</br>
	 * 两次循环确保数据有效性
	 * 
	 * @param key
	 * @param values
	 * @return 推入数据后链表长度
	 */
	public long rpush(String key, String... values) {
		isNull(key);
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				throw new RuntimeException("第" + i + "个参数为null");
			}
		}
		long count = 0;
		for (String value : values) {
			clientTemplate.rpush(key, value);
			count++;
		}
		return count;
	}

	/**
	 * 获取链表最左端值
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key) {
		notExsits(key);
		return clientTemplate.lpop(key);
	}

	/**
	 * 获取链表最右端值
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key) {
		notExsits(key);
		return clientTemplate.rpop(key);
	}

	/**
	 * 获取链表区间数据<br>
	 * 
	 * @param key
	 * @param start
	 *            左数从0开始,右数从-1开始
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end) {
		notExsits(key);
		return clientTemplate.lrange(key, start, end);
	}

	/**
	 * 从key链表中删除 value值<br>
	 * 注: 删除count的绝对值个value后结束 Count>0 从表头删除 Count<0 从表尾删除
	 * 
	 * @param key
	 * @param count
	 * @param value
	 */
	public void lrem(String key, long count, String value) {
		clientTemplate.lrem(key, count, value);
	}

	/**
	 * 剪切key对应的链接,切[start,stop]一段,并把该段重新赋给key
	 * 
	 * @param key
	 * @param start
	 *            左数从0开始,右数从-1开始
	 * @param end
	 * @return
	 */
	public String ltrim(String key, long start, long end) {
		return clientTemplate.ltrim(key, start, end);
	}

	/**
	 * 返回index索引上的值
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String lindex(String key, long index) {
		return clientTemplate.lindex(key, index);
	}

	public long llen(String key) {
		Long len = clientTemplate.llen(key);
		if (len == null) {
			return 0;
		}
		return len.longValue();
	}

	/**
	 * 在key链表中寻找’pivot’,并在search值之前|之后,.插入value<br>
	 * 注: 一旦找到一个search后,命令就结束了,因此不会插入多个value
	 * 
	 * @param key
	 * @param cmd
	 *            0:在pivot之前插入value,1:在pivot之后插入value
	 * @param pivot
	 *            链表中的值
	 * @param value
	 *            插入新值
	 */
	public void linsert(String key, int cmd, String pivot, String value) {
		notExsits(key);
		switch (cmd) {
		case 0:
			clientTemplate.linsert(key, LIST_POSITION.BEFORE, pivot, value);
			break;
		case 1:
			clientTemplate.linsert(key, LIST_POSITION.AFTER, pivot, value);
			break;
		default:
			clientTemplate.linsert(key, LIST_POSITION.BEFORE, pivot, value);
			break;
		}

	}

	public String brpop(String key, long timeout) {
		return null;
	}

	public String rpoplpush(String source, String desc) {
		return null;
	}

	/******************************************************************
	 * Set集合：无序性、唯一性
	 ******************************************************************/
	/**
	 * 添加集合元素
	 * 
	 * @param key
	 * @param member
	 */
	public long sadd(String key, String member) {
		Long sum = clientTemplate.sadd(key, member);
		if (sum == null) {
			return 0;
		}
		return sum.longValue();
	}

	/**
	 * 获取集合所有元素
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> smember(String key) {
		notExsits(key);
		return clientTemplate.smembers(key);
	}

	/**
	 * 删除集合元素
	 * 
	 * @param key
	 * @param member
	 * @return 删除的元素个数
	 */
	public long srem(String key, String member) {
		notExsits(key);
		Long sum = clientTemplate.srem(key, member);
		if (sum == null)
			return 0;
		return sum.longValue();
	}

	/**
	 * 返回并删除集合中key中1个随机元素<br>
	 * 体现了无序性
	 * 
	 * @param key
	 * @return 随机删除的元素
	 */
	public String spop(String key) {
		notExsits(key);
		return clientTemplate.spop(key);
	}

	/**
	 * 返回集合key中,随机的1个元素.
	 * 
	 * @param key
	 * @return
	 */
	public String srandmember(String key) {
		notExsits(key);
		return clientTemplate.srandmember(key);
	}

	/**
	 * 判断value是否在key集合中
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, String member) {
		notExsits(key);
		return clientTemplate.sismember(key, member).booleanValue();

	}

	/**
	 * 返回集合中元素的个数
	 * 
	 * @param key
	 * @return
	 */
	public long scard(String key) {
		notExsits(key);
		Long num = clientTemplate.scard(key);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/*******************************************************************
	 * 有序集合（通过权重score保证顺序）
	 ******************************************************************/

	/**
	 * 向有序集合中添加元素
	 * 
	 * @param key
	 * @param score
	 *            权重，按score自动排序
	 * @param member
	 */
	public void zadd(String key, double score, String member) {
		clientTemplate.zadd(key, score, member);
	}

	/**
	 * 按角标取值
	 * 
	 * @param key
	 * @param start
	 *            从0开始
	 * @param end
	 *            -1表示从最后开始，zrange(0,-1)即获取所有元素
	 * @return
	 */
	public Set<String> zrange(String key, int start, int end) {
		notExsits(key);
		return clientTemplate.zrange(key, start, end);
	}

	/**
	 * 按权重取值，分页功能
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 *            取值后偏移角标
	 * @param count
	 *            数量
	 * @return
	 */
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		notExsits(key);
		return clientTemplate.zrangeByScore(key, min, max, offset, count);
	}

	/**
	 * 有序集合元素数量
	 * 
	 * @param key
	 * @return
	 */
	public long zcard(String key) {
		notExsits(key);
		Long num = clientTemplate.zcard(key);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/**
	 * 统计权重在[min,max]之间的元素个数
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public long zcount(String key, double min, double max) {
		notExsits(key);
		Long num = clientTemplate.zcount(key, min, max);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/**
	 * 获取元素的权重or排名，正序
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public long zrank(String key, String member) {
		notExsits(key);
		Long score = clientTemplate.zrank(key, member);
		isNull(score);
		return score.longValue();
	}

	/**
	 * 获取元素的权重or排名，倒序
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public long zrevrank(String key, String member) {
		notExsits(key);
		Long score = clientTemplate.zrevrank(key, member);
		isNull(score);
		return score.longValue();
	}

	/**
	 * 按权重删除元素,[start,end]
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public long zremByScore(String key, double start, double end) {
		notExsits(key);
		Long num = clientTemplate.zremrangeByScore(key, start, end);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/**
	 * 按角标删除元素,[start,end]
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public long zremByRank(String key, int start, int end) {
		Long num = clientTemplate.zremrangeByRank(key, start, end);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/*******************************************************************
	 * Hash
	 ******************************************************************/
	/**
	 * 添加hash结构一个数据
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value) {
		clientTemplate.hset(key, field, value);
	}

	public void hmset(String key, Map<String, String> map) {
		clientTemplate.hmset(key, map);
	}

	/**
	 * 返回key中,所有域与其值
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {
		notExsits(key);
		return clientTemplate.hgetAll(key);
	}

	/**
	 * 返回key中field域的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		notExsits(key);
		return clientTemplate.hget(key, field);
	}

	public List<String> hmget(String key, String... fields) {
		notExsits(key);
		return clientTemplate.hmget(key, fields);
	}

	/**
	 * 删除key中 field域
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public long hdel(String key, String field) {
		notExsits(key);
		Long num = clientTemplate.hdel(key, field);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/**
	 * 返回key中元素的数量
	 * 
	 * @param key
	 * @return
	 */
	public long hlen(String key) {
		notExsits(key);
		Long num = clientTemplate.hlen(key);
		if (num == null)
			return 0;
		return num.longValue();
	}

	/**
	 * 判断key中有没有field域
	 */
	public boolean hexists(String key, String field) {
		notExsits(key);
		return clientTemplate.hexists(key, field).booleanValue();
	}

	/**
	 * 返回key中所有的field
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		notExsits(key);
		return clientTemplate.hkeys(key);
	}

	/**
	 * 返回key中所有的value
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvalues(String key) {
		notExsits(key);
		return clientTemplate.hvals(key);
	}
	

	public void isNull(Object obj) {
		if (obj == null) {
			throw new RuntimeException("parameter is null,please cheak it");
		}
	}

	public void notExsits(String key) {
		if (!exists(key)) {
			throw new RuntimeException("no key " + key + " exists,please cheak it");
		}
	}
}
