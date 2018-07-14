package com.maoding.common.module.companyDisk.service;

import com.maoding.common.module.companyDisk.dao.CompanyDiskDAO;
import com.maoding.common.module.companyDisk.model.CompanyDiskDO;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.filecenter.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wuwq on 2017/06/08.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CompanyDiskServiceImplTest {
    @Autowired
    CompanyDiskDAO companyDiskDAO;

    @Autowired
    CompanyDiskService companyDiskService;

    @Test
    public void testRecalcSizeOnFileChanged() throws Exception {
        String companyId1 = "1";
        String companyId2 = "2";
        String companyId3 = "3";
        companyDiskDAO.deleteByPrimaryKey(companyId1);
        companyDiskDAO.deleteByPrimaryKey(companyId2);
        companyDiskDAO.deleteByPrimaryKey(companyId3);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        int loopTimes = 100;
        for (int i = 0; i < loopTimes; i++) {
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileAdded(companyId1, FileSizeSumType.DOCMGR, 2L);
            });
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileRemoved(companyId1, FileSizeSumType.DOCMGR, 1L);
            });


            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileAdded(companyId2, FileSizeSumType.OTHER, 2L);
            });
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileRemoved(companyId2, FileSizeSumType.OTHER, 1L);
            });


            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileAdded(companyId3, FileSizeSumType.DOCMGR, 2L);
            });
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileRemoved(companyId3, FileSizeSumType.DOCMGR, 1L);
            });
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileAdded(companyId3, FileSizeSumType.OTHER, 2L);
            });
            executor.execute(() -> {
                companyDiskService.recalcSizeOnFileRemoved(companyId3, FileSizeSumType.OTHER, 1L);
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        CompanyDiskDO cd1 = companyDiskDAO.selectByPrimaryKey(companyId1);
        Assert.assertEquals((long) cd1.getDocmgrSize(), loopTimes * 1L);

        CompanyDiskDO cd2 = companyDiskDAO.selectByPrimaryKey(companyId2);
        Assert.assertEquals((long) cd2.getOtherSize(), loopTimes * 1L);

        CompanyDiskDO cd3 = companyDiskDAO.selectByPrimaryKey(companyId3);
        Assert.assertEquals((long) cd3.getDocmgrSize(), loopTimes * 1L);
        Assert.assertEquals((long) cd3.getOtherSize(), loopTimes * 1L);
    }
}