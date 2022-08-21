package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * @author : hung.nguyen23
 * @since : 8/22/22 Monday
 **/
@Component
public class JedisMaster {

    private static final int TIMEOUT = 30 * 1000;
    private static final int MAX_TOTAL = 300;
    private static final int MAX_IDLE = 20;
    @Autowired
    private ENVConfig envConfig;
    private JedisMaster() {}
    private JedisPool POOL_INSTANCE;

    private JedisPool getJedisPool() {
        if(POOL_INSTANCE != null) {
            return POOL_INSTANCE;
        }
        var redisEnv = envConfig.getStringProperty(Constant.KEY_CONFIG.REDIS);
        var uri = URI.create(redisEnv);
        POOL_INSTANCE = new JedisPool(getConfig(), uri, TIMEOUT);
        return POOL_INSTANCE;
    }

    private static JedisPoolConfig getConfig() {
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(MAX_TOTAL);
        poolConfig.setMaxIdle(MAX_IDLE);
        return poolConfig;
    }

    public String get(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String get(String key, int dbIndex) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(dbIndex);
            return jedis.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public Boolean exists(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public byte[] getByte(byte[] key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String set(String key, String value) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String set(String key, String value, int dbIndex) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(dbIndex);
            return jedis.set(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Long hset(String key, String field, String value) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.hset(key, field, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String hget(String key, String field) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> hvals(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.hvals(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String setWithoutExpire(String key, String value) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Boolean limitCall(String key, Long ccu, int expire) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            Long value = jedis.incr(key);
            if (value > ccu) {
                return false;
            }
            if (value == 1) {
                jedis.expire(key, expire);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Long lpush(String key, String value) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            Long result = jedis.lpush(key, value);
            jedis.expire(key, Constant.TIME.SECOND_OF_ONE_MONTH);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String rpop(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.rpop(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setWithExpire(String key, String value, int unixTime) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
            if (unixTime > 0) {
                jedis.expireAt(key, unixTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setWithExpireAfter(String key, String value, int expireSecond) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
            if (expireSecond > 0) {
                jedis.expire(key, expireSecond);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setWithExpireAfter(String key, String value, int expireSecond, int dbIndex) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireSecond > 0) {
                jedis.expire(key, expireSecond);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteByPattern(String pattern) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys(pattern);
            for (String key : keys) {
                jedis.del(key);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> getByPattern(String pattern) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(pattern);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Set<String> getByPattern(String pattern, int dbIndex) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(dbIndex);
            return jedis.keys(pattern);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void delete(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String key, int dbIndex) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(dbIndex);
            jedis.del(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String setByte(byte[] key, byte[] value) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setByteWithExpire(byte[] key, byte[] value, int expireSecond) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
            if (expireSecond > 0) {
                jedis.expire(key, expireSecond);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delByte(byte[] key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void publish(String channel, String message) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.publish(channel, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // Redis sorted sets : store score, member from smallest to greatest
    public void addSortedSet(String key, Double score, String member) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.zadd(key,score,member);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Long countMember(String key) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.zcard(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0L;
    }

    public Double getPointMember(String key, String member) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.zscore(key, member);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    // index : sorted from greatest to smallest
    // if member not exist -> return string "nil"
    public Long getIndexMember(String key, String member) {
        JedisPool pool = getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.zrevrank(key, member);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  null;
    }
}
