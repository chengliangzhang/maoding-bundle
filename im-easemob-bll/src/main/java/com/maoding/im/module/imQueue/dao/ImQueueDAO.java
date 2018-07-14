package com.maoding.im.module.imQueue.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.im.module.imQueue.model.ImQueueDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImQueueDAO extends BaseDao<ImQueueDO> {
    int updateWithOptimisticLock(ImQueueDO o);
    List<ImQueueDO> listWaitingQueue(@Param("count") int count);
}
