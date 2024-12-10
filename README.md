# vttp5_day19l_pracws
        // How to Convert to Epoch Milliseconds:
        // Use the getTime() method on a Date object:
        // long epochMillis = createdAt.getTime();

        // How to Convert Back from Epoch Milliseconds:
        // Use the Date constructor:
        // Date dateFromMillis = new Date(epochMillis);

        // Where to Use Epoch Milliseconds:
        // In your service class, before storing dates in Redis, convert them to epoch milliseconds:
        // long createdAtEpoch = createdAt.getTime();
        // long updatedAtEpoch = updatedAt.getTime();
        // redisTemplate.opsForHash().put("todos", "createdAt", String.valueOf(createdAtEpoch));

        https://proactive-healing-production.up.railway.app/