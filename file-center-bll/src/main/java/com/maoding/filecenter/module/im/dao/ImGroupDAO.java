package com.maoding.filecenter.module.im.dao;

import com.maoding.filecenter.module.im.dto.GroupImgUpdateDTO;
import org.springframework.stereotype.Repository;

/**
 * Created by Wuwq on 2017/05/31.
 */
@Repository
public interface ImGroupDAO {
    int updateGroupImg(GroupImgUpdateDTO dto);
}
