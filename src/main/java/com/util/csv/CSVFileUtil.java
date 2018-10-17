package com.util.csv;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

/**
 * @author christ
 * @desc common util for operating CSV file
 * @since 5/27/2015
 */
public class CSVFileUtil {
	
	
	/**
	 * @desc parse	 csv file into a list of object
	 * @param clazz	 the object class type you want to transfer to
	 * @param file	 the csv file you want to use
	 * @param columnOrder 	the mapping order between csv file column and Object field
	 * @return	 a list of model
	 * @throws Exception
	 */
	public static <T> List<T> parseCSVFile(Class<T> clazz, File file, String[] columnOrder) throws Exception {
		
		CsvToBean<T> csv = new CsvToBean<T>();
	    CSVReader csvReader = new CSVReader(new FileReader(file));
	    
	    ColumnPositionMappingStrategy<T> mappingStrategy = null;
	    if (columnOrder == null || columnOrder.length == 0) {
	    	mappingStrategy = getDefaultColumMapping(clazz);
	    } else {
	    	mappingStrategy = getSpecificColumMapping(clazz, columnOrder);
	    }
	    List<T> data = csv.parse(mappingStrategy, csvReader);
	    
	    return data;
	}
	
	
	private static <T> ColumnPositionMappingStrategy<T> getDefaultColumMapping(Class<T> clazz) throws Exception {
		
		List<String> columns = getDefaultColumnOrder(clazz);
		String[] cols = columns.toArray(new String[0]);
		ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<T>();
		strategy.setType(clazz);
		strategy.setColumnMapping(cols);
		
		return strategy;
	}
	
	
	private static <T> ColumnPositionMappingStrategy<T> getSpecificColumMapping(Class<T> clazz, String[] columnOrder) throws Exception {
		
		ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<T>();
		strategy.setType(clazz);
		strategy.setColumnMapping(columnOrder);
		
		return strategy;
	}
	
	
	/**
	 * @desc write	 a list of object into a CSV file
	 * @param file	 the csv file you want to write into
	 * @param clazz	 the object class type which you want to use
	 * @param data	 the list you want to write
	 * @param columnOrder 	the mapping order between csv file and Object
	 * @param isAppend 	if ture then add the new record from the tail of the csv file, else rewrite the csv file
	 * @throws Exception
	 */
	public static <T> void writeCSVFile(File file, Class<T> clazz, List<T> data, String[] columnOrder, Boolean isAppend) throws Exception {
		
		if (clazz == null || data == null || data.size() == 0) return;
		CSVWriter writer = null;
		if (isAppend) {
			 writer = new CSVWriter(new FileWriter(file, true));
		} else {
			writer = new CSVWriter(new FileWriter(file));
		}
	        
	    List<String[]> lines = getCSVRecords(clazz, data, columnOrder);
	    for(String[] line : lines) {
		    writer.writeNext(line);
	    }
	    writer.close();
	}
	
	
	private static <T> List<String[]> getCSVRecords(Class<T> clazz, List<T> data, String[] columnOrder) throws Exception {
		
		List<String> fieldNames = null;
		if (columnOrder == null || columnOrder.length == 0) {
			fieldNames = getDefaultColumnOrder(clazz);
		} else {
			fieldNames = Arrays.asList(columnOrder);
		}
		
		int length = fieldNames.size();
		List<String[]> lines = new ArrayList<String[]>();
		for(T obj : data) {
			
			String[] line = new String[length];
			for(int i = 0; i < length; i++) {
				
				Field field = clazz.getDeclaredField(fieldNames.get(i));
				field.setAccessible(true);
				Class<?> type = field.getType();
				
				if (type == String.class) {
					line[i] = (String)field.get(obj);
	            } else if (type == Integer.class) {  
	            	line[i] = String.valueOf(field.getInt(obj));
	            } else if (type == Double.class){
	            	line[i] = String.valueOf(field.getDouble(obj));
	            } else if (type == Float.class) {
	            	line[i] = String.valueOf(field.getFloat(obj));
	            }
			}
			
			lines.add(line);
		}

		return lines;
	}
	
	
	private static <T> List<String> getDefaultColumnOrder(Class<T> clazz) {
		
		Field[] fields = clazz.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for (Field field : fields) {
			fieldNames.add(field.getName());
		}
		Collections.sort(fieldNames);
		return fieldNames;
	}
}
