package com.util.mail;

import java.io.File;
import java.io.FileReader;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

/**
 * 
 * @author christ
 * @time 2018-10-16
 */
public class MailSender {
	private final static String regexEmail = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	private final static String CONTENT_TYPE_PLAIN = "text/plain;charset=utf-8";
	private final static String CONTENT_TYPE_HTML = "text/html;charset=utf-8";
	private static final String CONFIG_FILE = "application.properties";
	private static Logger logger = Logger.getLogger(MailSender.class.getName());
	private MimeMessage mimeMsg;
	private Session session;
	private Multipart mp;
	private Properties props;

	/**
	 * Initialize MailSender with smtp server properties
	 * 
	 * @throws Exception
	 */
	private MailSender() throws Exception {

		loadConfig();
		session = Session.getDefaultInstance(props);
		mimeMsg = new MimeMessage(session);
		mp = new MimeMultipart("mixed");

	}

	/**
	 * Set email subject
	 * 
	 * @param mailSubject
	 * @return false when text format error
	 */
	private boolean setSubject(String mailSubject) {

		try {
			if (mailSubject == null || mailSubject.trim().length() == 0)
				return true;
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Set email body with default charset utf8
	 * 
	 * @param content
	 * @return false when text format error
	 */
	private boolean setBody(String content) {

		try {
			if (content == null || content.trim().length() == 0)
				return true;
			BodyPart bp = new MimeBodyPart();
			String contentType = getMessageContentType(content);
			bp.setContent(content, contentType);
			// bp.setContent(content, "text/plain;charset=utf-8");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * The direct email receivers
	 * 
	 * @param to
	 * @return false when email address format error
	 */
	private boolean setTo(String to) {

		if (to == null || to.trim().length() == 0)
			return true;

		if (!emailFormatValidate(to))
			return false;
		to = to.replace(";", ",");

		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, (Address[]) InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * The receivers who you want to copy to
	 * 
	 * @param copyto
	 * @return false when email address format error
	 */
	private boolean setCopyTo(String copyto) {

		if (copyto == null || copyto.trim().length() == 0)
			return true;

		if (!emailFormatValidate(copyto))
			return false;
		copyto = copyto.replace(";", ",");

		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * The receivers who you want to blind copy to
	 * 
	 * @param blindCopyTo
	 * @return false when email address format error
	 */
	private boolean setBlindCopyTo(String blindCopyTo) {

		if (blindCopyTo == null || blindCopyTo.trim().length() == 0)
			return true;

		if (!emailFormatValidate(blindCopyTo))
			return false;
		blindCopyTo = blindCopyTo.replace(";", ",");

		try {
			mimeMsg.setRecipients(Message.RecipientType.BCC, (Address[]) InternetAddress.parse(blindCopyTo));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Set attachments which you want to sent
	 * 
	 * @param files
	 * @return false when file attched failed
	 */
	private boolean setAttachments(File[] files) {

		if (files == null || files.length == 0)
			return true;

		try {
			for (File file : files) {
				MimeBodyPart attachment = new MimeBodyPart();
				attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
				String fileName = file.getName();
				attachment.setFileName(fileName.substring(fileName.lastIndexOf(File.separator) + 1));
				mp.addBodyPart(attachment);
			}
			return true;

		} catch (Exception e) {
			logger.error("MailSender.setAttachments Exception", e);
			return false;
		}

	}

	/**
	 * @author ChangPan
	 * @param arrayInputList
	 * @param mineTypeList
	 * @param fileNameList
	 * @return
	 * @throws Exception
	 */
	public boolean setAttachmentsByByteMineTypeList(List<byte[]> arrayInputList, List<String> mineTypeList,
			List<String> fileNameList) throws Exception {
		try {
			int i = 0;
			if (arrayInputList != null) {
				for (byte[] arrayInput : arrayInputList) {
					ByteArrayDataSource ds = new ByteArrayDataSource(arrayInput, mineTypeList.get(i));
					MimeBodyPart mimeFile = new MimeBodyPart();
					mimeFile.setDataHandler(new DataHandler(ds));
					mimeFile.setFileName(MimeUtility.encodeText(fileNameList.get(i), "utf-8", "Q"));// support
																									// Chinese
																									// Language
					mp.addBodyPart(mimeFile);
					i++;
				}
			}
			return true;
		} catch (Exception e) {
			logger.info(e.toString());
			return false;
		}
	}

	/**
	 * Email sending main function
	 * 
	 * @return false if some exception happened
	 */
	private boolean send() {

		try {
			String from = props.getProperty("mail.from");
			String user = props.getProperty("mail.user");
			String password = props.getProperty("mail.password");

			mimeMsg.setFrom(new InternetAddress(from));
			mimeMsg.setSentDate(new Date());
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();

			Transport transport = session.getTransport();
			transport.connect(user, password);

			transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());

			logger.info("send mail success.");
			transport.close();
			return true;
		} catch (MessagingException me) {
			/***
			 * SGG-3109 send mail time out ,need to update mail status is SENT
			 */
			logger.error(me.getMessage(), me);
			if (me.getNextException() instanceof SocketTimeoutException) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * send plain text
	 * 
	 * @param text
	 * @return
	 */
	private boolean send(String text) {

		try {
			String from = props.getProperty("mail.from");
			String user = props.getProperty("mail.user");
			String password = props.getProperty("mail.password");

			mimeMsg.setFrom(new InternetAddress(from));
			mimeMsg.setSentDate(new Date());
			mimeMsg.setText(text);
			mimeMsg.saveChanges();

			Transport transport = session.getTransport();
			transport.connect(user, password);
			transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());

			logger.info("send mail success.");
			transport.close();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Set all information which need to express
	 * 
	 * @param to
	 * @param copyto
	 * @param blindCopyTo
	 * @param subject
	 * @param content
	 * @param files
	 * @return false if some exception happened
	 * @throws Exception
	 */
	public static boolean sendEmail(String to, String copyto, String blindCopyTo, String subject, String content,
			File[] files) throws Exception {

		MailSender mailSender = new MailSender();

		if (!mailSender.setTo(to))
			return false;
		if (!mailSender.setCopyTo(copyto))
			return false;
		if (!mailSender.setBlindCopyTo(blindCopyTo))
			return false;
		if (!mailSender.setSubject(subject))
			return false;
		if (!mailSender.setBody(content))
			return false;
		if (!mailSender.setAttachments(files))
			return false;

		if (!mailSender.send())
			return false;

		return true;
	}

	/**
	 * @author ChangPan
	 * @param to
	 * @param copyto
	 * @param blindCopyTo
	 * @param subject
	 * @param content
	 * @param bytesList
	 * @param fileNameList
	 * @return
	 * @throws Exception
	 */
	public static boolean sendEmailWithBytesMineAttach(String to, String copyto, String blindCopyTo, String subject,
			String content, List<String> mineTypeList, List<byte[]> bytesList, List<String> fileNameList)
			throws Exception {

		MailSender mailSender = new MailSender();

		if (!mailSender.setTo(to))
			return false;
		if (!mailSender.setCopyTo(copyto))
			return false;
		if (!mailSender.setBlindCopyTo(blindCopyTo))
			return false;
		if (!mailSender.setSubject(subject))
			return false;
		if (!mailSender.setBody(content))
			return false;
		if (!mailSender.setAttachmentsByByteMineTypeList(bytesList, mineTypeList, fileNameList))
			return false;
		if (!mailSender.send())
			return false;

		return true;
	}

	/**
	 * send plain text email
	 * 
	 * @param to
	 * @param copyto
	 * @param blindCopyTo
	 * @param subject
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static boolean sendTextEmail(String to, String copyto, String blindCopyTo, String subject, String content)
			throws Exception {

		MailSender mailSender = new MailSender();

		if (!mailSender.setTo(to))
			return false;
		if (!mailSender.setCopyTo(copyto))
			return false;
		if (!mailSender.setBlindCopyTo(blindCopyTo))
			return false;
		if (!mailSender.setSubject(subject))
			return false;
		if (!mailSender.send(content))
			return false;

		return true;
	}

	/**
	 * Email address validating
	 * 
	 * @param emaillist
	 * @return false if format is invalid
	 */
	private boolean emailFormatValidate(String emaillist) {
		boolean tag = true;

		if (emaillist == null)
			return false;
		String[] list = emaillist.replace(",", ";").split(";");
		for (String mail : list) {

			tag = isPatternValid(mail);
			if (!tag)
				break;
		}

		return tag;
	}

	private boolean isPatternValid(String mail) {
		return mail != null && validateEmail(mail);
	}

	/**
	 * Load configuration about smtp server
	 * 
	 * @throws Exception
	 */
	private void loadConfig() throws Exception {
		String currentPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		String configPath = currentPath + File.separator + CONFIG_FILE;
		FileReader fr = null;
		try {
			fr = new FileReader(configPath);
			props = new Properties();
			props.load(fr);

		} catch (Exception e) {
			logger.info(e.toString());
		} finally {
			if (null != fr) {
				fr.close();
			}
		}
	}

	public static String getMessageContentType(String content) {
		if (null!=content&&!"".equals(content))
			return CONTENT_TYPE_PLAIN;
		if (content.indexOf("</p>") >= 0 || content.indexOf("</h") >= 0)
			return CONTENT_TYPE_HTML;
		return CONTENT_TYPE_PLAIN;
	}

	public static boolean validateEmail(String email) {
		if (email.matches(regexEmail)) {
			return true;
		} else {
			return false;
		}
	}

}
