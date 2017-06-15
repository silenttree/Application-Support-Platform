package com.asc.portal.direct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.asc.cas.server.CentralAuthenticationService;
import com.asc.common.direct.DirectList;
import com.asc.commons.context.ContextHolder;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.service.OrganizationService;
import com.asc.framework.cache.aop.CacheService;
import com.asc.portal.builder.RuntimeFileBuilder;
import com.asc.portal.exception.PortalDataServiceException;
import com.asc.portal.preferences.PreferencesManager;
import com.asc.portal.preferences.dao.Preferences;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mixky.toolkit.IconTool;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

/**
 * Application Portal/PortalAppDirect.java<br>
 * 应用门户direct方法定义
 * 
 * Mixky Co., Ltd. 2014<br>
 * 
 * @author Bill<br>
 */
public class PortalAppDirect {
	/**
	 * 清空缓存
	 * @param className
	 * @return
	 */
	@DirectMethod
	public JsonObject clearCache(String className){
		JsonObject json = new JsonObject();
		try {
			if (className == null || "".equals(className)) {
				CacheService.instance().clear();
			} else {
				CacheService.instance().removeByClass(className);
			}
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 生成图标样式文件
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject buildIconCssFile() {
		JsonObject json = new JsonObject();
		String iconFolderPath = ContextHolder.instance().getRealPath(
				"/resources/icon/");
		String iconFileName = ContextHolder.instance().getRealPath(
				"/runtime/css/asc.icon.css");
		IconTool.buildIconCssFile(iconFolderPath, iconFileName);
		json.addProperty("success", true);
		return json;
	}

	/**
	 * 编译运行时文件
	 * @param type	生成文件类型
	 * @return
	 */
	@DirectMethod
	public JsonObject buildFiles(String type){
		JsonObject json = new JsonObject();
		try {
			json.addProperty("success", true);
			if("action".equals(type)){
				RuntimeFileBuilder.instance().buildPortalActions();
			}else{
				json.addProperty("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载应用程序
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject loadApplications(HttpServletRequest request) {
		// 根据request获得当前登录用户信息
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		DirectList directList = new DirectList();
		try {
			// 读取应用程序记录
			PortalDirectDataService.instance().loadApplications(directList, user);
			directList.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}

	/**
	 * 装载导航菜单
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject loadPortalNavigators() {
		// 根据request获得当前登录用户信息
		User user = null; // CentralAuthenticationService.instance().getUserInfo(request);
		DirectList directList = new DirectList();
		try {
			PortalDirectDataService.instance().loadNavigators(directList, user);
			directList.setSuccess(true);
		} catch (PortalDataServiceException e) {
			directList.setSuccess(true);
			e.printStackTrace();
		}
		return directList.getOutput();
	}

	/**
	 * 装载桌面数据
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject loadPortalData(HttpServletRequest request) {
		// 根据request获得当前登录用户信息
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		JsonObject json = new JsonObject();
		// 门户标题
		json.addProperty("title", "平台应用门户");
		// 门户导航
		try {
			json.add("navigators", PortalDirectDataService.instance()
					.getNavigators(user));
		} catch (PortalDataServiceException e1) {
			e1.printStackTrace();
		}
		// 用户信息
		JsonObject userJson = new JsonObject();
		userJson.addProperty("id", user.getId());
		userJson.addProperty("f_caption", user.getF_caption());
		userJson.addProperty("f_unit_id", user.getF_company_id());
		userJson.addProperty("f_unit_caption", user.getF_company_caption());
		userJson.addProperty("f_dept_id", user.getF_dept_id());
		userJson.addProperty("f_dept_caption", user.getF_dept_caption());
		userJson.addProperty("f_org_fullpath", user.getF_company_caption() + "/" + user.getF_dept_caption());
		userJson.addProperty("f_type", user.getF_type());
		json.add("user", userJson);
		Preferences preferences = null;
		// 桌面配置
		try {
			preferences = PreferencesManager.instance().getPreferences(user);
			JsonObject pJson = preferences.toJsonObject();
			pJson.addProperty("key", user.getF_name());
			json.add("preferences", pJson);
		} catch (PortalDataServiceException e) {
			e.printStackTrace();
			json.addProperty("success", false);
		}

		json.addProperty("success", true);
		return json;
	}

	/**
	 * 保存用户配置信息
	 * 
	 * @param modelKey
	 * @param preferences
	 * @return
	 */
	@DirectMethod
	public JsonObject savePreferences(String key, JsonObject data,
			HttpServletRequest request) {
		// 根据request获得当前登录用户信息
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		Preferences preferences = new Preferences();
		if (key == null || "".equals(key)) {
			preferences.setKey(user.getF_name());
		} else {
			preferences.setKey(key);
		}
		preferences.fromJson(data);
		JsonObject json = new JsonObject();
		try {
			json.addProperty("success", PreferencesManager.instance()
					.savePreferences(preferences));
		} catch (PortalDataServiceException e) {
			e.printStackTrace();
			json.addProperty("success", false);
		}
		return json;
	}

	/**
	 * 删除参数选择信息
	 * 
	 * @param key
	 * @return
	 */
	@DirectMethod
	public JsonObject deletePreferences(String key, HttpServletRequest request){
		JsonObject json = new JsonObject();
		try {
			json.addProperty("success", PreferencesManager.instance()
					.deletePreferences(key));
		} catch (PortalDataServiceException e) {
			json.addProperty("success", false);
		}
		return json;
	}

	/**
	 * 装载墙纸
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject loadWallpapers() {
		User user = null;
		DirectList directList = new DirectList();
		try {
			PortalDirectDataService.instance().getWallpaperList(directList, user);
			directList.setSuccess(true);
		} catch (PortalDataServiceException e) {
			directList.setSuccess(false);
			e.printStackTrace();
		}
		return directList.getOutput();
	}
	
	
	/**
	 * 门户显示列表
	 * @return
	 */
	@DirectMethod
	public JsonObject loadPortalViewList() {
		User user = null;
		DirectList directList = new DirectList();
		try {
			PortalDirectDataService.instance().loadPortalViewList(directList, user);
			directList.setSuccess(true);
		} catch (PortalDataServiceException e) {
			directList.setSuccess(false);
			e.printStackTrace();
		}
		return directList.getOutput();
	}
	
	
	/**
	 * 获取帮助文档的主题树
	 * @param docKey
	 * @param request
	 * @return
	 */
	@DirectMethod
	public JsonObject getDocTree(String docKey, HttpServletRequest request) {
		String filePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "app" + File.separator + "docs" + File.separator + "doctree.json";
		File jsonFile = new File(filePath);
		InputStream input = null;
		JsonReader reader = null;
		JsonArray jsonArray = null;
		JsonObject json = new JsonObject();
		try {
			input = new FileInputStream(jsonFile);
			reader = new JsonReader(new InputStreamReader(input));
			jsonArray = new JsonParser().parse(reader).getAsJsonArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(jsonArray != null){
			json.addProperty("success", true);
			json.add("treeNodes", jsonArray);
		}else{
			json.addProperty("success", false);
		}
		return json;
	}

	/**
	 * 修改密码
	 * 
	 * @param pwdOld
	 *            原始密码
	 * @param pwdNew
	 *            新密码
	 * @param pwdNew2
	 *            确认新密码
	 * @return
	 */
	@DirectMethod
	public JsonObject changePassword(String pwdOld, String pwdNew,
			String pwdNew2, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		try {
			User user = CentralAuthenticationService.instance().getUserInfo(request);
			long userId = user.getId();
			if (OrganizationService.instance().validationUserPswd(userId, pwdOld)){
				OrganizationService.instance().updateUserPswd(userId, pwdNew);
				// 清除缓存
				CacheService.instance().clear();
				json.addProperty("success", true);
				json.addProperty("msg", "密码修改成功");
			} else {
				json.addProperty("success", false);
				json.addProperty("msg", "原始密码错误");
			}
		} catch(Exception e) {
			json.addProperty("success", false);
			json.addProperty("msg", "密码修改失败");
		}
		return json;
	}
	
	/**
	 * 获取桌面栏目列表
	 * @param request
	 * @return
	 */
	@DirectMethod
	public JsonArray getPortlets(String appIdsStr, HttpServletRequest request){
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		String[] appIds = appIdsStr.split(";");
		try {
			return PortalDirectDataService.instance().getPortlets(appIds, user);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonArray();
		}
	}
	
	/**
	 * 获取快捷按钮列表
	 * @param request
	 * @return
	 */
	@DirectMethod
	public JsonArray getShortcuts(String appIdsStr, HttpServletRequest request){
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		String[] appIds = appIdsStr.split(";");
		try {
			return PortalDirectDataService.instance().getShortcuts(appIds, user);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonArray();
		}
	}
	
	/**
	 * 获取样式列表
	 * @return
	 */
	@DirectMethod
	public JsonArray loadDeskStyles(){
		try {
			return PortalDirectDataService.instance().loadDeskStyles();
		} catch (PortalDataServiceException e) {
			return new JsonArray();
		}
	}
	
	@DirectMethod
	public JsonObject savePreferencesTpl(String key, JsonObject data,
			HttpServletRequest request) {
		JsonObject json = new JsonObject();
		Preferences preferences = new Preferences();
		//没有key直接保存失败
		if (key == null || "".equals(key)) {
			json.addProperty("success", false);
			return json;
		}
		preferences.setKey(key);
		preferences.fromJson(data);
		try {
			json.addProperty("success", PreferencesManager.instance()
					.savePreferencesTpl(preferences));
		} catch (PortalDataServiceException e) {
			json.addProperty("success", false);
		}
		return json;
	}
	
	@DirectMethod
	public JsonArray findAllPreferenceTpl(){
		JsonArray rs = new JsonArray();
		try {
			List<Preferences> ps = PreferencesManager.instance().findAllPreferencesTpl();
			for(Preferences p : ps){
				JsonObject jo = new JsonObject();
				jo.addProperty("key", p.getKey());
				jo.add("preferences", p.toJsonObject());
				rs.add(jo);
			}
			return rs;
		} catch (PortalDataServiceException e) {
			return rs;
		}
	}
	
}
