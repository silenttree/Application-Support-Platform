package com.asc.developer.config;

import java.util.List;

import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.design.util.JsonFileUtil;
import com.asc.util.file.DirectoryUtil;
import com.google.gson.JsonObject;

/**
 * Application Developer/SerializeUtil.java<br>
 * 文件持久化工具类（完成对象与文件的保存和装载）
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class SerializeUtil {
	
	/**
	 * 查询实体对象文件列表
	 * @param rootPath 对象文件根路径
	 * @return
	 */
	public static List<String> listSerializedFiles(String rootPath) {
		List<String> jsonfiles = DirectoryUtil.listFilepaths(rootPath, ".json");
		return jsonfiles;
	}
	
	/**
	 * 从指定路径装载实体对象
	 * 
	 * @param filename 文件名称
	 * @param rootPath 根路径
	 * @param clazz 对象类型
	 * @return
	 */
	public static ApplicationConnection loadObject(String filename) {
		try {
			JsonObject jsonData = JsonFileUtil.readFile(filename, "UTF-8");
			ApplicationConnection conn = new ApplicationConnection();
			conn.fromJson(jsonData);
			return conn;
		} catch (DesignObjectException e) {
			e.printStackTrace();
			throw new IllegalStateException("装载实体对象失败，" + e.getMessage());
		}
	}
	
	/**
	 * 实体对象转换成jsonobject格式字符串保存到指定文件路径
	 * 
	 * @param filename
	 * @param object
	 * @param rootPath
	 */
	public static void saveObject(String filename, JsonObject jsonData, String rootPath) {
		String filepath = getFilepath(rootPath, filename);
		try {
			JsonFileUtil.writeFile(filepath, jsonData, "UTF-8");
		} catch (DesignObjectException e) {
			e.printStackTrace();
			throw new IllegalStateException("实体对象存储失败，" + e.getMessage());
		}
	}
	
	/**
	 * 删除指定路径的对象文件
	 * 
	 * @param filename
	 * @param rootPath
	 */
	public static void deleteObject(String filename, String rootPath) {
		String filepath = getFilepath(rootPath, filename);
		JsonFileUtil.deleteFile(filepath);
	}
	
	/**
	 * 生成实体对象文件名
	 * @param objId
	 * @return
	 */
	public static String getObjectFilename(String objId) {
		return objId + ".json";
	}
	
	/**
	 * 根据文件名和根路径计算实体对象文件路径
	 * 
	 * @param rootPath
	 * @param filename
	 * @return
	 */
	private static String getFilepath(String rootPath, String filename) {
		StringBuffer sb = new StringBuffer();
		sb.append(rootPath);
		if (!rootPath.endsWith("/") && !filename.startsWith("/")) {
			sb.append("/");
		}
		sb.append(filename);
		return sb.toString();
	}
	
}