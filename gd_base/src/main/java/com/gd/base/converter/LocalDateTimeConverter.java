package com.gd.base.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.gd.base.constant.Constans;
import com.gd.base.util.DateUtil;

import java.time.LocalDateTime;


public class LocalDateTimeConverter implements Converter<LocalDateTime> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param context
     * @return
     */
    @Override
    public LocalDateTime convertToJavaData(ReadConverterContext<?> context) {
        String strData = context.getReadCellData().getStringValue();
       return DateUtil.stringToLocalDateTime(strData, Constans.DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 这里是写的时候会调用 不用管
     *
     * @return
     * @param context
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDateTime> context) {
        return new WriteCellData<>(context.getValue());
    }

}
