package top.bultrail.markroad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
//        HashMap<Integer,ArrayList<String>> ha = new HashMap<>();
//        ArrayList<String> orDefault = ha.getOrDefault(1, new ArrayList<String>());
//        orDefault.add("");
//        ha.put(1, orDefault);
//
//
//        ListOperations<String, String> listOperations = redisTemplate.opsForList();
//        listOperations.leftPush("sensor", "dasdsd1");
//        listOperations.leftPush("sensor", "dasdsd2");
//        listOperations.leftPush("sensor", "dasdsd3");
//        listOperations.leftPush("sensor", "dasdsd4");
//        List<String> sensor = listOperations.range("sensor", 0, -1);
//        for (String ss : sensor) {
//            System.out.println(ss);
//
    }

}

