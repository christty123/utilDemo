package com.util.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

public class PdfConvertor {
	
	private Logger logger = Logger.getLogger(PdfConvertor.class);

	public String createPresentationPdf(String pdfPath, List<String> photoUrls) {
		
//		if (CommUtil.nvl(pdfPath, null) == null) return null;
		if (photoUrls == null || photoUrls.size() == 0) return null; 
		
		logger.info("PdfConvertor.createPresentationPdf with pdfPath=" + pdfPath + ", photoUrls=" + photoUrls);
		Document document = new Document();
		FileOutputStream fos = null;
		
		// Initail pdfWriter with full direcotry name of pdf file
		try {
			
			fos = new FileOutputStream(pdfPath);
			PdfWriter.getInstance(document, fos);
			
		} catch (Exception e) {
			
			closeIOStream(fos);
			e.printStackTrace();
			return null;
			
		} 
		
		//Begin to wirte images to pdf file
		document.open();			
		for (String photoUrl : photoUrls) {
			
			Image image = null;
			try {
				
				image = Image.getInstance(photoUrl);
				float heigth = image.getHeight();  
                float width = image.getWidth();  
                int percent = getPercent(heigth, width);  
                image.setAlignment(Image.MIDDLE);  
                //compressing images as origin rate
                image.scalePercent(percent + 3);
				document.add(image);
				
			} catch (Exception e) {

				document.close();
				closeIOStream(fos);
				e.printStackTrace();
				return null;
			} 
		}

		document.close();
		closeIOStream(fos);
		return pdfPath;		
	}
	
	
	private void closeIOStream(OutputStream os) {
		
		try {
			if (os != null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getPercent(float h, float w) {  
		 
		int p1 = 0;  
		float p2 = 0.0f;  
		p2 = 530 / w * 100;  
		p1 = Math.round(p2);  
	     
	    return p1;  
	}  
	
}
