package com.asc.developer.designer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.context.ContextHolder;
import com.asc.developer.designer.exception.FileBuilderException;
import com.mixky.toolkit.IconTool;

public class RuntimeFileBuilder {
	private Log logger;
	private static RuntimeFileBuilder singleton;
	private String charset;

	private RuntimeFileBuilder(){
		logger = LogFactory.getLog(this.getClass());
		charset = "utf-8";
	}

	public static RuntimeFileBuilder instance() {
		if (singleton == null) {
			singleton = new RuntimeFileBuilder();
		}
		return singleton;
	}
	/**
	 * 生成定制工具类定义文件
	 * @throws FileBuilderException 
	 */
	public void buildDesignerClasses() throws FileBuilderException{
		try {
			folderFilesMerge("/app/framework/designer/class", "/runtime/js/asc.developer.classes.js", "js");
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new FileBuilderException("生成开发工具动作函数定义文件【runtime/js/asc.developer.classes.js】错误", e);
		}
	}
	/**
	 * 生成开发工具动作函数定义文件
	 * @throws FileBuilderException 
	 */
	public void buildDesignerActions() throws FileBuilderException{
		try {
			folderFilesMerge("/app/framework/action", "/runtime/js/asc.developer.actions.js", "js");
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new FileBuilderException("生成开发工具动作函数定义文件【runtime/js/asc.developer.actions.js】错误", e);
		}
	}
	
	/**
	 * 合并某个目录下的所有文件到某个文件中
	 * TODO: 发生异常时，有资源泄漏的bug，需修订
	 * 
	 * @param foldername
	 * @param filename
	 */
	private void folderFilesMerge(String foldername, String filename, String extname) throws Exception{
		foldername = ContextHolder.instance().getRealPath(foldername);
		filename = ContextHolder.instance().getRealPath(filename);
		int current;
		char[] buffer = new char[1024];
		FileInputStream fis = null;
		PrintStream ps = null;

		try {
			File filefolder = new File(foldername);
			File[] fileList = filefolder.listFiles();
			FileOutputStream fos = new FileOutputStream(filename);
			ps = new PrintStream(fos);
			OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
			
			for (int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory()){
					continue;
				}
				if(extname != null && !"".equals(extname)){
					if(!fileList[i].getName().endsWith("." + extname)){
						continue;
					}
				}
				fis = new FileInputStream(fileList[i]);
				InputStreamReader isReader = new InputStreamReader(fis, charset);
				// 输出注释头
				StringBuffer strbuffer = new StringBuffer();
				strbuffer.append("\r\n");
				strbuffer.append("//=================================================================\r\n");
				strbuffer.append("//\t文件名：" + fileList[i].getName() + "\r\n");
				strbuffer.append("//=================================================================\r\n");
		        ps.print(strbuffer.toString());
				// 输出注释头 结束
				while ((current = isReader.read(buffer, 0, 1024)) != -1) {
					osw.write(buffer, 0, current);
					osw.flush();
				}
				isReader.close();
			}
			osw.close();
			fos.close();
		} catch (Exception e) {
			logger.error("生成目录[" + foldername + "]下合并[" + filename + "]文件时发生错误");
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public void buildIconCssFiles() {
		String iconFolderPath = ContextHolder.instance().getRealPath("/resources/icon/");
		String iconFileName = ContextHolder.instance().getRealPath("/runtime/css/asc.icon.css");
		IconTool.buildIconCssFile(iconFolderPath, iconFileName);
	}
}
