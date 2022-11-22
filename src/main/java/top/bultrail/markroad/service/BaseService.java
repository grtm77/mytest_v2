package top.bultrail.markroad.service;

import top.bultrail.markroad.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class BaseService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean doLogin(User user){

        String username = user.getUsername();
        String userpswd = user.getUserpswd();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String s = operations.get(username);
        if(s!=null && s.equals(userpswd)){
            return true;
        }else{
            return false;
        }
    }

    public void doRegister(User user){

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(user.getUsername(),user.getUserpswd());

    }

}
