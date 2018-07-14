package com.maoding.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ExcelUtils
 * 类描述：Excel操作公共方法
 * 作    者：Zhangchengliang
 * 日    期：2017/7/19
 */
public class ExcelUtils {

	/** 日志对象 */
	private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

	/**
	 * 作用：关闭输入流
	 * 作者: Zhangchengliang
	 * 日期：2017/7/19
     */
	public static void closeInputSteam(InputStream in){
		if (in != null){
			try {
				in.close();
			} catch (IOException e) {
				log.error("关闭时产生错误", e);
			}
		}
	}

	/**
	 * 作用：关闭工作簿
	 * 作者: Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static void closeWorkbook(Workbook workbook){
		if (workbook != null){
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("关闭工作簿时产生错误", e);
			}
		}		
	}

	/**
	 * 作用：根据输入流获取工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static Workbook getWorkbook(InputStream in) {
		if (in == null) return null;

		Workbook wb = null;

		try {
			wb = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("Excel文件格式错误",e);
		} catch (IOException e) {
			log.error("Excel文件读取错误",e);
		}
		return wb;
	}

	/**
	 * 作用：根据文件名获取工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */	
	public static Workbook getWorkbook(String fileName){
		if (fileName == null) return null;

		InputStream in = null;
		Workbook wb = null;
		try {
			in = new FileInputStream((new File(fileName))); 
			wb = getWorkbook(in);
		} catch (FileNotFoundException e) {
			log.error("无法找到"+fileName, e);
		} catch (Exception e) {
			log.error("读取文件"+fileName+"错误",e);
			closeInputSteam(in);
		}
		return wb;
	}

	/**
	 * 作用：获取一个空的工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
     */
	public static Workbook getWorkbook(){
		return new HSSFWorkbook();
	}

	/**
	 * 作用：读取单元格内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
     */
	public static Object readFrom(Cell cell){
		if(cell==null) return null;

		switch (cell.getCellTypeEnum()) {
			case BLANK:
				return null;
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case ERROR:
				return cell.getErrorCellValue();
			case FORMULA:
				return cell.getCellFormula().replaceAll("\"", "");
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case STRING:
				return cell.getStringCellValue();
		}
		return "Unknown Cell Type:" + cell.getCellTypeEnum();
	}

	/**
	 * 作用：读取一行内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static Map<String,Object> readFrom(Row row, Map<Short,String> titleMap, Short startColumn, Short endColumn){
		if (row == null) return null;
		if ((startColumn == null) || (startColumn == -1)) startColumn = row.getFirstCellNum();
		if ((endColumn == null) || (endColumn == -1)) endColumn = row.getLastCellNum();
		
		//从startColumn处开始读取数据
		Map<String,Object> dataMap = null;
		for(Short i=startColumn; i<=endColumn; i++){
			String name = ((titleMap != null) && (titleMap.containsKey(i))) ? titleMap.get(i) : i.toString();
			Object data = readFrom(row.getCell(i));
			if (data != null) {
				if (dataMap == null) dataMap = new HashMap<>();
				dataMap.put(name, data);
			}
		}
		return dataMap;
	}

	/**
	 * 作用：读取一页内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static List<Map<String,Object>> readFrom(Sheet sheet, Integer titleRow, Integer startRow,Short startColumn,Short endColumn){
		if (sheet == null) return null;
		if ((titleRow == null) || (titleRow == -1)) titleRow = sheet.getFirstRowNum();

		//读取标题数据,从titleRow倒序读取第一个不为空的值，实现多标题行合并
		Map<Short,String> titleMap = new HashMap<>();
		if ((sheet.getFirstRowNum() <= titleRow) && (titleRow <= sheet.getLastRowNum())) {
			Row row = sheet.getRow(titleRow);
			if ((startColumn == null) || (startColumn == -1)) startColumn = row.getFirstCellNum();
			if ((endColumn == null) || (endColumn == -1)) endColumn = row.getLastCellNum();
			for(Short i=startColumn; i<=endColumn; i++){
				Object data = readFrom(row.getCell(i));
				if ((data == null) || StringUtils.isEmpty(data.toString())){
					for (Integer k=titleRow; k>sheet.getFirstRowNum(); k--){
						Row rowPrev = sheet.getRow(k);
						data = readFrom(rowPrev.getCell(i));
						if ((data != null) && !StringUtils.isEmpty(data.toString())) break;
					}
				}
				if ((data != null) && !StringUtils.isEmpty(data.toString())) {
					titleMap.put(i, data.toString());
				}
			}
		}
		
		//读取数据行
		List<Map<String,Object>> dataList = null;
		if ((startRow == null) || (startRow == -1)) startRow = titleRow + 1;
		Integer endRow = sheet.getLastRowNum();
		for (Integer i=startRow; i<=endRow; i++){
			Map<String,Object> r = readFrom(sheet.getRow(i),titleMap,startColumn,endColumn);
			if (r != null) {
				if (dataList == null) dataList = new ArrayList<>();
				dataList.add(readFrom(sheet.getRow(i), titleMap, startColumn, endColumn));
			}
		}
		return dataList;
	}

	/**
	 * 作用：读取工作簿内指定页内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static List<Map<String,Object>> readFrom(Workbook workbook,Integer sheetIndex,Integer titleRow,Integer startRow,Short startColumn,Short endColumn){
		if (workbook == null) return null;
		if ((sheetIndex == null) || (sheetIndex == -1)) sheetIndex = workbook.getActiveSheetIndex();
		
		return ((0<=sheetIndex) && (sheetIndex<workbook.getNumberOfSheets())) ?
				readFrom(workbook.getSheetAt(sheetIndex),titleRow,startRow,startColumn,endColumn) : null;
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,String sheetName,Integer titleRow,Integer startRow,Short startColumn,Short endColumn){
		if ((workbook == null) || (sheetName == null)) return null;
		return readFrom(workbook.getSheet(sheetName),titleRow,startRow,startColumn,endColumn);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,Integer sheetIndex,Integer titleRow){
		return readFrom(workbook,sheetIndex,titleRow,null,null,null);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,String sheetName,Integer titleRow){
		return readFrom(workbook,sheetName,titleRow,null,null,null);
	}
}
