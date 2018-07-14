package com.maoding.constDefine.companyDisk;

import java.util.UnknownFormatConversionException;

/**
 * Created by Wuwq on 2017/06/08.
 * 文件大小统计类型
 */
public enum FileSizeSumType {
    /**
     * 协同
     **/
    CORP(1),
    /**
     * 文档库
     **/
    DOCMGR(2),
    /**
     * 其他
     **/
    OTHER(3);

    private Integer value = 0;

    FileSizeSumType(Integer value) {
        this.value = value;
    }

    public static FileSizeSumType valueOf(int value) {
        switch (value) {
            case 1:
                return CORP;
            case 2:
                return DOCMGR;
            case 3:
                return OTHER;
            default:
                throw new UnknownFormatConversionException("无效的枚举类型转换");
        }
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
