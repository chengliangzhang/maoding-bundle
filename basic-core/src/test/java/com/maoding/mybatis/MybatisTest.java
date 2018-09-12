package com.maoding.mybatis;

import com.maoding.core.base.BaseIdObject;
import com.maoding.core.base.CorePageDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/** 
* com.maoding.mybatis.provider Tester. 
* 
* @author ZhangChengliang
* @since 08/16/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})
public class MybatisTest {
    @Autowired
    private TestBaseViewDao testBaseViewDao;

    @Autowired
    private TestBaseEditDao testBaseEditDao;

    @Autowired
    private TestCoreEditDao testCoreEditDao;

    @Test
    public void testCoreEdit() throws Exception{
        TestCoreEntity parent;
        TestCoreEntity child;
        parent = testCoreEditDao.update(getParentRequest("parent"));
        assert (parent != null);
        child = testCoreEditDao.update(getChildRequest(parent,"child"));
        assert (child != null);
        parent = testCoreEditDao.update(getChangeRequest(parent,"parent2",null));
        assert (parent != null);
        testCoreEditDao.deleteById(getParentRequest("parent2"));
        testCoreEditDao.destroyById(getParentRequest("parent2"));
    }

    @Test
    public void testBaseEdit() throws Exception{
        TestBaseEntity entity;
        entity = testBaseEditDao.update(getParentRequest("parent"));
        assert (entity != null);
        entity = testBaseEditDao.update(getChangeRequest(entity,"parent2","parent2"));
        assert (entity != null);
        testBaseEditDao.deleteById(getParentRequest("parent"));
    }

    @Test
    public void testGet() throws Exception {
        TestDTO dto;
        dto = testBaseViewDao.selectById("000114aac76b47e183e27477ac42cd6d");
        assert (dto.getId() != null);
        dto = testBaseViewDao.getFirst(getQuery());
        assert (dto.getId() != null);

        TestBaseEntity entity;
        entity = testBaseEditDao.selectById("000114aac76b47e183e27477ac42cd6d");
        assert (entity.getId() != null);
    }

    @Test
    public void testList() throws Exception {
        List<TestDTO> dtoList;
        dtoList = testBaseViewDao.list(getQuery());
        assert (dtoList != null);

        CorePageDTO<TestDTO> dtoPage;
        dtoPage = testBaseViewDao.listPage(getQuery());
        assert (dtoPage != null);

        List<TestBaseEntity> entityList;
        entityList = testBaseEditDao.list(getQuery());
        assert (entityList != null);
    }

    @Test
    public void testCount() throws Exception {
        int count;
        count = testBaseViewDao.count(getQuery());
        assert (count > 0);
    }

    private TestQueryDTO getQuery(){
        TestQueryDTO query = new TestQueryDTO();
        query.setName("2222222");
        query.setFileName("1.txt");
        query.setPageIndex(1);
        query.setPageSize(5);
        return query;
    }

    private TestEditDTO getChangeRequest(BaseIdObject entity, String name, String path){
        TestEditDTO request = new TestEditDTO();
        request.setId(entity.getId());
        request.setName(name);
        request.setPath(path);
        request.setAccountId("editAccount");
        request.setProjectId("projectId");
        request.setIsSelected("1");
        return request;
    }

    private TestEditDTO getParentRequest(String name){
        TestEditDTO request = new TestEditDTO();
        request.setId("parent");
        request.setName(name);
        request.setPath(name);
        request.setAccountId("account");
        request.setProjectId("projectId");
        request.setIsSelected("1");
        return request;
    }

    private TestEditDTO getChildRequest(TestCoreEntity parent, String name){
        TestEditDTO request = new TestEditDTO();
        request.setId("child");
        request.setName(name);
        request.setPath(parent.getPath() + "/" + request.getName());
        request.setAccountId("account");
        request.setProjectId("projectId");
        request.setIsSelected("1");
        return request;
    }
}
