package utils;

import commons.exception.ServerException;
import redis.clients.jedis.Jedis;

import static commons.JedisFactory.getRedisConncetion;
import static commons.JedisFactory.returnConnection;

public class RedisStoreUtil {
	private static final String KEY_SEPARATOR = ":";

	public static void saveNodeProperty(String graphId, String objectId, String nodeProperty, String propValue) {

		Jedis jedis = getRedisConncetion();
		try {
			String redisKey = getNodePropertyKey(graphId, objectId, nodeProperty);
			jedis.set(redisKey, propValue);
		} catch (Exception e) {
			throw new ServerException(DialCodeEnum.ERR_CACHE_SAVE_PROPERTY_ERROR.name(), e.getMessage());
		} finally {
			returnConnection(jedis);
		}
	}

	public static Double getNodePropertyIncVal(String graphId, String objectId, String nodeProperty) {

		Jedis jedis = getRedisConncetion();
		try {
			String redisKey = getNodePropertyKey(graphId, objectId, nodeProperty);
			double inc = 1.0;
			double value = jedis.incrByFloat(redisKey, inc);
			return value;
		} catch (Exception e) {
			throw new ServerException(DialCodeEnum.ERR_CACHE_GET_PROPERTY_ERROR.name(), e.getMessage());
		} finally {
			returnConnection(jedis);
		}
	}

	private static String getNodePropertyKey(String graphId, String objectId, String propertyName) {
		return graphId + KEY_SEPARATOR + removeSpaces(objectId) + KEY_SEPARATOR + removeSpaces(propertyName);
	}

	private static String removeSpaces(String str) {
		return str.replaceAll("\\s", "");
	}

}
