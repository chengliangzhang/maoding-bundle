package com.maoding.im.module.imAccount.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.im.module.imAccount.dto.ImAccountSyncDTO;
import com.maoding.im.module.imAccount.model.ImAccountDO;

import java.util.List;

public interface ImAccountDAO extends BaseDao<ImAccountDO> {
    int updateWithOptimisticLock(ImAccountDO o);

    List<ImAccountSyncDTO> selectAllAccountSynIm();
}
