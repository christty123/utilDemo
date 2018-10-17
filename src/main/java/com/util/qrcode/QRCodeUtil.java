package com.util.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.swetake.util.Qrcode;

/**
 * @author christ
 */
public class QRCodeUtil {
    
    private static final char ERROR_CORRECT = 'M';
    private static final char ENCODE_MODE = 'B';
    private static final String CHAR_SET = "utf-8";
    private static final String ICON_SUFFIX = "png";
    private static final int QRCODE_VERSION = 7; 
    private static final int IMAGE_SIZE = 140;
    private static final int PIX_OFF = 2;
    private static final int CONTENT_LENGTH = 800;

    
    /**
     * @Author Mianping_Wu
     * @Date Sep 1, 2015
     * @Param @param content
     * @Param @return
     * @Param @throws Exception
     * @return BufferedImage
     */
    public BufferedImage encoderQRCode(String content) throws Exception {

	Qrcode qrcodeHandler = new Qrcode();
	qrcodeHandler.setQrcodeErrorCorrect(ERROR_CORRECT);
	qrcodeHandler.setQrcodeEncodeMode(ENCODE_MODE);
	qrcodeHandler.setQrcodeVersion(QRCODE_VERSION);
	byte[] contentBytes = content.getBytes(CHAR_SET);
	BufferedImage bufImg = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
	Graphics2D gs = bufImg.createGraphics();
	gs.setBackground(Color.WHITE);
	gs.clearRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
	gs.setColor(Color.BLACK);
	
	if (contentBytes.length > 0 && contentBytes.length < CONTENT_LENGTH) {
	    
	    boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
	    for (int i = 0; i < codeOut.length; i++) {
		for (int j = 0; j < codeOut.length; j++) {
		    if (codeOut[j][i]) gs.fillRect(j * 3 + PIX_OFF, i * 3 + PIX_OFF, 3, 3);
		}
	    }
	} else {
	    
	    throw new Exception("QRCode content has acceed the limit length[" + CONTENT_LENGTH + " bytes]!");
	}
	gs.dispose();
	bufImg.flush();
	return bufImg;
    }
    
    
    
    /**
     * @author Junfeng_Yan
     * @since sep 2,2015
     * @param bi
     * @return
     * @throws IOException 
     */
    public static InputStream BufferedImage2InputStream(BufferedImage bi) throws IOException {
    	
    	if (null == bi) return null;
    		ByteArrayOutputStream bs = new ByteArrayOutputStream();  
    		ImageOutputStream imOut =ImageIO.createImageOutputStream(bs);
    		ImageIO.write(bi,"png", imOut);
    		return new ByteArrayInputStream(bs.toByteArray());
	}
    
    
    /**
     * @Author Mianping_Wu
     * @Date Sep 1, 2015
     * @Param @param content
     * @Param @param filePath
     * @Param @return
     * @return File
     */
    public static File createQRCode(String content) throws Exception {
	
	QRCodeUtil generator = new QRCodeUtil();
	BufferedImage bufImg = generator.encoderQRCode(content);
	File imageFile = new File(content + "." + ICON_SUFFIX);
	ImageIO.write(bufImg, ICON_SUFFIX, imageFile); 
	return imageFile;
	
    }

}
