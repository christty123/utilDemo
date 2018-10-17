package com.util.jspaer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

public class JasperUtil {

	enum ReportType {
		PDF, CSV
	}

	public static byte[] exportByJavaBeanConnection(String jasperTemplateName, Map reportParameter, List dataList,
			String reportType) throws Exception {
		byte[] result = null;
		if (reportType.equalsIgnoreCase(ReportType.PDF.name())) {
			result = exportPDF(jasperTemplateName, reportParameter, dataList);
		} else if (reportType.equalsIgnoreCase(ReportType.CSV.name())) {
			result = exportCSV(jasperTemplateName, reportParameter, dataList);
		}
		return result;
	}

	public static byte[] exportByDataBaseConnection(String jasperTemplateName, Map reportParameter, List dataList,
			String reportType) throws Exception {
		byte[] result = null;

		return result;
	}

	/**
	 * 
	 * @param jasperTemplateName
	 * @param reportParameter
	 * @param dataList
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public static byte[] exportPDF(String jasperPath, Map reportParameter, List dataList) throws Exception {
		File reFile = new File(jasperPath);
		String filePath = reFile.getPath();
		JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);
		JasperPrint jasperPrint = JasperFillManager.fillReport(filePath, reportParameter, dataSource);
		ByteArrayOutputStream outPut = new ByteArrayOutputStream();
		JRAbstractExporter exporter = exportedByFileType("pdf", jasperPrint, outPut);
		if (exporter == null) {
			return null;
		} else {
			exporter.exportReport();
			return outPut.toByteArray();
		}
	}

	/**
	 * 
	 * @param jasperTemplateName
	 * @param reportParameter
	 * @param dataList
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public static byte[] exportCSV(String jasperPath, Map reportParameter, List dataList) throws Exception {

		File reFile = new File(jasperPath);
		String filePath = reFile.getPath();
		JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);
		reportParameter.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);// Ignore
																			// pagination
																			// for
																			// csv
		JasperPrint jasperPrint = JasperFillManager.fillReport(filePath, reportParameter, dataSource);
		ByteArrayOutputStream outPut = new ByteArrayOutputStream();
		JRAbstractExporter exporter = exportedByFileType("csv", jasperPrint, outPut);
		if (exporter == null) {
			return null;
		} else {
			exporter.exportReport();
			return outPut.toByteArray();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JRAbstractExporter exportedByFileType(String fileType, JasperPrint jasperPrint,
			ByteArrayOutputStream outPut) {
		JRAbstractExporter exporter = null;
		switch (fileType) {
		case "pdf":
			exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outPut));
			break;
		case "csv":
			exporter = new JRCsvExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(outPut));
			break;
		default:
		}

		return exporter;
	}

	public static void saveCSVFilePath(String csvFilePath,List<String[]>list) throws Exception {
		if(null!=list){
			 CSVWriter wr = new CSVWriter(new FileWriter(csvFilePath),',','"',"\r");
			for (String[] content : list) {
				 wr.writeNext(content);
			}
			if(null!=wr){
				wr.close();
			}
		}
	}
	
	public static void writerContent(File file,String content){
		FileWriter wr=null;
		try {
			 wr=new FileWriter(file);
			 wr.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null!=wr){
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
