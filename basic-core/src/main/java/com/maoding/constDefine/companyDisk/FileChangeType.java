package com.maoding.constDefine.companyDisk;

import java.util.UnknownFormatConversionException;

/**
 * Created by Wuwq on 2017/06/08.
 */
public enum FileChangeType {
    ADDED(1), REMOVED(2);

    private Integer value = 0;

    FileChangeType(Integer value) {
        this.value = value;
    }

    public static FileChangeType valueOf(int value) {
        switch (value) {
            case 1:
                return ADDED;
            case 2:
                return REMOVED;
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
