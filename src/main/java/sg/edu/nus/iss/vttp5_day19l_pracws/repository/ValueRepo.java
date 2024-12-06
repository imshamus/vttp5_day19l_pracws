package sg.edu.nus.iss.vttp5_day19l_pracws.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.vttp5_day19l_pracws.constant.Constant;

@Repository
public class ValueRepo {
    
    // D15 - slide 20

    @Autowired
    @Qualifier(Constant.template01)
    RedisTemplate<String, String> template;

    // D15 - slide 24 - create/update a value 
    public void createValue(String key, String value) {
        template.opsForValue().set(key, value);

        // setIfPresent
        // setIfAbsent
    }

    // D15 - slide 25 -retrieve a value
    public String getValue(String key) {
        return template.opsForValue().get(key);
    }

    // D15 - slide 27 - delete
    public Boolean deleteValue(String key) {
        return template.delete(key);
    }

    // D15 - slide 26 - only works for key with integer value
    public void incrementValue(String key) {
        template.opsForValue().increment(key);
    }

    public void decrementValue(String key) {
        template.opsForValue().decrement(key);
    }

    public void incrementByValue(String key, Integer value) {
        template.opsForValue().increment(key, value);
    }

    public void decrementByValue(String key, Integer value) {
        template.opsForValue().decrement(key, value);
    }

    // D15 - slide 28
    public Boolean checkExists(String key) {
        return template.hasKey(key);
    }


    // Append to a value - Appends a value to an existing value stored at a key. (APPEND)
    public void append(String key, String value) {
        template.opsForValue().append(key, value);
    }

    
    // Get a substring of a value - Retrieves a substring of the string stored at a key. (GETRANGE)
    public String getSubstring(String key, long start, long end) {
        return template.opsForValue().get(key, start, end);
    }


    // Set a Value Only If It Doesn’t Exist - Ensures that the value is only set if the key does not already exist.  (SETNX)
    public Boolean setIfAbsent(String key, String value) {
        return template.opsForValue().setIfAbsent(key, value);
    }


    // Expire a Key - Sets a time-to-live (TTL) for a key. (EXPIRE)
    public Boolean setExpiration(String key, long timeout, TimeUnit unit) {
        return template.expire(key, timeout, unit);
    }

    
    
}
