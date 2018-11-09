/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sipin.cloud.order.service.feign.impl.MaterialServiceImpl;
import cn.sipin.cloud.order.service.service.RedisClusterServiceContract;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.sipin.sales.cloud.order.response.MaterialResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class SipinMaterialTest {

  @Autowired
  private RedisClusterServiceContract redisService;



  @Test
  public void hmgetTest() {

    HashMap<String,String> hashMap = new HashMap<>();
    hashMap.put("name2", "kdkkd");
    hashMap.put("name3", "dkskds");
    hashMap.put("name4", "kdk33");
    hashMap.put("name1", "kdkkd");

    redisService.hmset("hashredis01", hashMap);

    List<String> strings = new ArrayList<>();
    strings.add("kdkdk");
    strings.add("name2");
    strings.add("name4");
    strings.add("nam");
    strings.add("name3");

    List<Object> list = redisService.hmget("hashredis01", new ArrayList<Object>(strings));

    System.out.println("list size = " + list.size());

    if (list != null) {

    }
  }
}
