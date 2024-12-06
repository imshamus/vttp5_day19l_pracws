package sg.edu.nus.iss.vttp5_day19l_pracws.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.vttp5_day19l_pracws.constant.Constant;

@Repository
public class MapRepo {

    @Autowired
    @Qualifier(Constant.template02)
    RedisTemplate<String, String> redisTemplate;

    // D15 - Slide 36
    public void create(String redisKey, String hashKey, String hashValue)
    {
        // there is .put, .putall, . putifabsent
        redisTemplate.opsForHash().put(redisKey, hashKey, hashValue);
    }

    // D15 - Slide 37
    public Object get(String redisKey, String hashKey) // this is going to return a JSON obj (?), why does it return object?
    {
        return redisTemplate.opsForHash().get(redisKey, hashKey);

    }

   // D15 - Slide 38
    public Long delete(String redisKey, String hashKey)
    {
        return redisTemplate.opsForHash().delete(redisKey, hashKey);
    }

    // D15 - Slide 39
    public Boolean keyExists(String redisKey, String hashKey)
    {
        return redisTemplate.opsForHash().hasKey(redisKey, hashKey);
    }

    // D15 - Slide 40 (twisted)
    // <Object, Object> = <HashKeys, HashValues>
    public Map<Object, Object> getEntries(String redisKey)
    {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    // D15 - Slide 40
    public Set<Object> getKeys(String redisKey) // distinct keys thats why use set
    {
        return redisTemplate.opsForHash().keys(redisKey);
    }

    public List<Object> getValues(String redisKey) // values can be repeated so can use list
    {
        return redisTemplate.opsForHash().values(redisKey);
    }

    // D15 - Slide 41
    public Long size (String redisKey)
    {
        return redisTemplate.opsForHash().size(redisKey);
    }

    public void expire(String redisKey, Integer expireValue)
    {
        Duration expireDuration = Duration.ofSeconds(expireValue);

        redisTemplate.expire(redisKey, expireDuration);
    }
    
}
