package com.maoding.utils; 

import com.maoding.core.base.BaseEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        assert (b);
        b = StringUtils.isSame("sss","sss");
        assert (b);
        b = DigitUtils.isSame(new Integer("1"),new Float("1.0"));
        assert (b);
        b = DigitUtils.isSame(null,new Float("1.0"));
        assert (!b);
        b = DigitUtils.isSame(1,new Double("1.0"));
        assert (b);
    }

    @Test
    public void testCreateFrom() throws Exception {
        TestClass1 src1 = getClass1();
        TestClass2 dst2 = BeanUtils.createFrom(src1,TestClass2.class);
        assert (isSame(src1,dst2));

        TestClass2 src2 = getClass2();
        Map<String,Object> dstMap = BeanUtils.createMapFrom(src2,String.class,Object.class);
        assert (isSame(src2,dstMap));

        List<Integer> srcList = getList();
        List<Float> dstList = BeanUtils.createListFrom(srcList,Float.class);
        assert (isSame(srcList,dstList));
    }

    private List<Integer> getList() throws Exception {
        List<Integer> src = new ArrayList<>();
        src.add(1);
        src.add(null);
        return src;
    }

    private TestClass1 getClass1() throws Exception {
        TestClass1 src = new TestClass1();
        src.setDigital(3);
        src.setObjectDigital(5L);
        src.setString("class1");
        src.setObject(new Father(1));
        src.setArray(new byte[]{1,2,3});
        src.setEntity(new BaseEntity());
        src.setList(new ArrayList<>());
        src.getList().add(1);
        return src;
    }

    private TestClass2 getClass2() throws Exception {
        TestClass2 src = new TestClass2();
        src.setDigital(3.2f);
        src.setObjectDigital(5.2);
        src.setString(new StringBuffer("class2"));
        src.setObject(new Child(1,"child"));
        src.setArray(new Byte[]{1,2,3,null});
        src.setEntity(new BaseEntity());
        src.setList(new ArrayList<>());
        src.getList().add(1.1f);
        return src;
    }

    private boolean isSame(TestClass2 src2, Map<String,Object> dstMap){
        return ((src2 == null) && (dstMap == null)) ||
            ((src2 != null) && (dstMap != null)
                && (DigitUtils.isSame(src2.getDigital(),dstMap.get("Digital")))
                && (DigitUtils.isSame(src2.getObjectDigital(),dstMap.get("ObjectDigital")))
            );
    }

    private boolean isSame(TestClass1 src1, TestClass2 dst2) {
        return ((src1 == null) && (dst2 == null)) ||
            ((src1 != null) && (dst2 != null)
                && (DigitUtils.isSame(src1.getDigital(),dst2.getDigital()))
                && (DigitUtils.isSame(src1.getObjectDigital(),dst2.getObjectDigital()))
            );
    }

    private boolean isSame(List<?> src, List<?> dst){
        boolean b = true;
        if ((src == null) != (dst == null)){
            b = false;
        } else {
            for (int i=0; i<src.size() && i<dst.size(); i++){
                if (!DigitUtils.isSame(src.get(i),dst.get(i))){
                    b = false;
                    break;
                }
            }
        }
        return b;
    }
}
