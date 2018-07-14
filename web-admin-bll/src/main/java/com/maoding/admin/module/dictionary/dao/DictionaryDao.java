package com.maoding.admin.module.dictionary.dao;

import com.maoding.admin.module.dictionary.dto.DictionaryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryDao {

    List<DictionaryDTO> getDictionaryByCode(@Param("code") String code);
}
