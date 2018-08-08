package com.maoding.utils; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* StringUtils Tester. 
* 
* @author Zhangchengliang
* @since 08/08/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class UtilsTest {
    @Test
    public void testLeftAndRight() throws Exception {
        String s;
        s = StringUtils.left("abc,123,!@#",",");
        assert(s.equals("abc"));
        s = StringUtils.lastLeft("abc,123,!@#",",");
        assert(s.equals("abc,123"));
        s = StringUtils.right("abc,123,!@#",",");
        assert(s.equals("123,!@#"));
        s = StringUtils.lastRight("abc,123,!@#",",");
        assert(s.equals("!@#"));
    }

    @Test
    public void testIsSame() throws Exception {
        boolean b;
        b = StringUtils.isSame(null,null);
        assert (b);
        b = StringUtils.isSame("",null);
        assert (!b);
        b = StringUtils.isSame("sss","sss");
        assert (b);
        b = DigitUtils.isSame(new Integer("1"),new Float("1.0"));
        assert (b);
        b = DigitUtils.isSame(null,new Float("1.0"));
        assert (!b);
        b = DigitUtils.isSame(1,new Double("1.0"));
        assert (b);
    }
}
