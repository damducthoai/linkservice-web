package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.entity.BadURL;
import com.butchjgo.linkservice.repository.BadURLRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestJPA {
    @Resource(name = "badURLRepository")
    BadURLRepository badURLRepository;

    @Test
    public void testInsert() {
        BadURL url = new BadURL("xin chao caca ban");
        badURLRepository.save(url);
        assert badURLRepository.findAll().size() > 0;
    }
}
