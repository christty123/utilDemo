package com.util.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpByJsch {
	private static Logger	logger	= LoggerFactory.getLogger(SftpByJsch.class);
	private Channel			channel;
	private Session			session;
	private String			ftpHost;
	private String			ftpUserName;
	private String			ftpPassword;
	private int				ftpPort;
	private String			privateKey;
	private String			passphrase;
	private int				ftpTimeout;
	private int				connectTimeout = 80000;

	public SftpByJsch(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, String privateKey, String passphrase, int ftpTimeout) {
		super();
		this.ftpHost = ftpHost;
		this.ftpUserName = ftpUserName;
		this.ftpPassword = ftpPassword;
		this.ftpPort = ftpPort;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.ftpTimeout = ftpTimeout;
	}
	
	public SftpByJsch(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, 
			String privateKey, String passphrase, int ftpTimeout, int connectTimeout) {
		super();
		this.ftpHost = ftpHost;
		this.ftpUserName = ftpUserName;
		this.ftpPassword = ftpPassword;
		this.ftpPort = ftpPort;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.ftpTimeout = ftpTimeout;
		this.connectTimeout = connectTimeout;
	}

	private void init() throws Exception {
		JSch jsch = new JSch();
		// 设置密钥和密码
		if (privateKey != null && !"".equals(privateKey)) {
			if (passphrase != null && "".equals(passphrase)) {
				// 设置带口令的密钥
				jsch.addIdentity(privateKey, passphrase);
			} else {
				// 设置不带口令的密钥
				jsch.addIdentity(privateKey);
			}
		}

		if (ftpPort <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(ftpUserName, ftpHost);
		} else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}

		if (ftpPassword != null) {
			session.setPassword(ftpPassword);
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.setConfig(config);
		session.setTimeout(ftpTimeout);
		session.connect(connectTimeout);
		logger.info("Sftp session connected.");

		channel = session.openChannel("sftp");
		channel.connect(connectTimeout);
		if (channel.isConnected()) 
		     logger.info("channel is connected");
	}

	public void upload(String destPath, String destfilename, String copyfrom) throws Exception {
		try {
			init();
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			if (null != destPath && destPath.trim().length() > 0) {
				sftp.cd(destPath);
			}
			// 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			OutputStream outstream = sftp.put(destfilename);
			InputStream instream = new FileInputStream(new File(copyfrom));

			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}

			outstream.flush();
			outstream.close();
			instream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}
	}

	public void upload(String destPath, String destfilename, File sourceFile) throws Exception {
		try {
			init();
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			if (null != destPath && destPath.trim().length() > 0) {
				sftp.cd(destPath);
			}
			// 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			OutputStream outstream = sftp.put(destfilename, ChannelSftp.OVERWRITE);
			InputStream instream = new FileInputStream(sourceFile);

			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}

			outstream.flush();
			outstream.close();
			instream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}
	}

	public InputStream dowload(String remotePath, String remoteFileName) throws Exception {
		InputStream instream = null;
		try {
			init();
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			if (null != remotePath && remotePath.trim().length() > 0) {
				sftp.cd(remotePath);
			}
			// 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			instream = sftp.get(remoteFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}
		return instream;

	}

	public File dowload(String remotePath, String remoteFileName, String localFilePath) throws Exception {
		File file = null;
		try {
			init();
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			if (null != remotePath && remotePath.trim().length() > 0) {
				sftp.cd(remotePath);
			}
			// 列出服务器指定的文件列表
			Vector<LsEntry> v = sftp.ls("*.*");
			for (int i = 0; i < v.size(); i++) {

				System.out.println(v.get(i).getFilename());
			}
			sftp.get(remoteFileName, localFilePath);
			File dir = new File(localFilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = new File(localFilePath + File.separator + remoteFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}
		return file;

	}

	public List<File> dowloadByFileNameRegex(String remotePath, String remoteFileNameReg, String localFilePath) throws Exception {
		List<File> files = new ArrayList<File>();
		try {
			init();
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			if (null != remotePath && remotePath.trim().length() > 0) {
				sftp.cd(remotePath);
			}
			File dir = new File(localFilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 列出服务器指定的文件列表
			Vector<LsEntry> v = sftp.ls("*.*");
			for (int i = 0; i < v.size(); i++) {
				String fileName = v.get(i).getFilename();
				Pattern pattern = Pattern.compile(remoteFileNameReg);
				Matcher matcher = pattern.matcher(fileName);
				if (matcher.matches()) {
					sftp.get(fileName, localFilePath);
					File file = new File(localFilePath + File.separator + fileName);
					files.add(file);
				} else {
					continue;
				}

			}
			sftp.exit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}
		return files;

	}

	public void move(String fileName, String currentPath, String targetPath) throws Exception {

		try {
			init();
			
			if (!currentPath.endsWith("/") && !currentPath.endsWith("\\")) {
				currentPath = currentPath + "/";
			}

			if (!targetPath.endsWith("/") && !targetPath.endsWith("\\")) {
				targetPath = targetPath + "/";
			}

			ChannelSftp sftp = (ChannelSftp) channel;
			String currentFilePath = currentPath + fileName;
			String targeFilePath = targetPath + fileName;
			sftp.rename(currentFilePath, targeFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}

	}
	public void move(String fileName, String currentPath,String targetFileName, String targetPath) throws Exception {

		try {
			init();
			
			if (!currentPath.endsWith("/") && !currentPath.endsWith("\\")) {
				currentPath = currentPath + "/";
			}

			if (!targetPath.endsWith("/") && !targetPath.endsWith("\\")) {
				targetPath = targetPath + "/";
			}

			ChannelSftp sftp = (ChannelSftp) channel;
			String currentFilePath = currentPath + fileName;
			String targeFilePath = targetPath + targetFileName;
			sftp.rename(currentFilePath, targeFilePath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}

	}
	/**
	 * 根据Sam的要求先从本地上传文件到远端备份文件夹中，在删除远端原来的文件
	 * @param localFile
	 * @throws Exception
	 */
	public void move(File localFile, String currentPath, String targetPath, String ip, String user, String psw, int port, String privateKey, String passphrase) throws Exception {

		try {
//			init();
			
			if (!currentPath.endsWith("/") && !currentPath.endsWith("\\")) {
				currentPath = currentPath + "/";
			}

			if (!targetPath.endsWith("/") && !targetPath.endsWith("\\")) {
				targetPath = targetPath + "/";
			}
			//使用原有方法上传与删除方法
			upload(targetPath, localFile.getName(), localFile);
			delete(ip, user, psw, port, privateKey, passphrase, currentPath + localFile.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}

			logger.info("Sftp session disconnected.");
			logger.info("Sftp session disconnected.");
		}

	}

	public void delete(String ip, String user, String psw, int port, String privateKey, String passphrase, String remoteFilePath) throws Exception {

		Session session = null;
		Channel channel = null;

		JSch jsch = new JSch();

		// 设置密钥和密码
		if (privateKey != null && !"".equals(privateKey)) {
			if (passphrase != null && "".equals(passphrase)) {
				// 设置带口令的密钥
				jsch.addIdentity(privateKey, passphrase);
			} else {
				// 设置不带口令的密钥
				jsch.addIdentity(privateKey);
			}
		}

		if (port <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		} else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(user, ip, port);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}

		// 设置登陆主机的密码
		session.setPassword(psw);// 设置密码
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		// 设置登陆超时时间
		session.connect(30000);

		try {
			// 创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.rm(remoteFilePath);
		} catch (Exception e) {
//			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if(null!=session){
				session.disconnect();
			}
			if(null!=channel){
				channel.disconnect();
			}
		}
	}
	public static void main(String[] args) {
		File f=new File("d:/file/GHK51728.REPORTSCUST.20170503041925WU.139.txn");
		Pattern pattern = Pattern.compile("GHK\\d*\\.REPORTSCUST\\.\\d*.*\\.\\d*\\.txn");
		Matcher matcher = pattern.matcher(f.getName());
		System.out.println(f.getName());
		if (matcher.matches()) {
			System.out.println("ok");
		}else{
			System.out.println("no");
		}
		
	}
	
}
