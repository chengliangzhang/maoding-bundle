package com.maoding.coreBuffer; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* com.maoding.coreBuffer Tester. 
* 
* @author ZhangChengliang
* @since 08/09/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class BufferFactoryTest { 
    @Test
    public void testCreateRedisBuffer() throws Exception {
        CoreBuffer buf = BufferFactory.getBuffer("redis");
        assert (buf != null);
    }
} 
