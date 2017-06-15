package com.asc.portal.direct;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.asc.common.direct.DirectList;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.applicationentrance.service.ApplicationEntranceService;
import com.asc.commons.context.ContextHolder;
import com.asc.commons.engine.authority.OrganizationRelationService;
import com.asc.commons.engine.authority.exception.RelationException;
import com.asc.commons.organization.entity.User;
import com.asc.framework.designobject.service.DesignObjectService;
import com.asc.portal.exception.PortalDataServiceException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PortalDirectDataService {
	private static PortalDirectDataService singleton;
	public static final String PORTLET_TYPE_NAME = "portlet";
	public static final String SHORTCUT_TYPE_NAME = "shortcut";
	
	public static PortalDirectDataService instance() {
		if (singleton == null) {
			singleton = new PortalDirectDataService();
		}
		return singleton;
	}
	
	/**
	 * 获取样式列表
	 * @return
	 * @throws PortalDataServiceException
	 */
	public JsonArray loadDeskStyles() throws PortalDataServiceException {
		JsonArray rs = new JsonArray();
		//先加入默认的CSS
		JsonObject defaultjson = new JsonObject();
	    defaultjson.addProperty("id", "default");
	    defaultjson.addProperty("thumbnail", "resources/themethumbnail/default.png");
	    defaultjson.addProperty("path", "dependencies/ext-4.2.1/resources/css/ext-all.css");
	    rs.add(defaultjson);
		//加入resources/xtheme文件夹下的样式
	    String themeFolderName = "dependencies/ext-4.2.1/resources";
	    File themeFolder = new File(ContextHolder.instance().getRealPath(themeFolderName));
	    try{
	      File[] folders = themeFolder.listFiles();
	      for (int i = 0; i < folders.length; i++) {
	        File folder = folders[i];
	        if ((folder.isDirectory()) && (folder.getName().startsWith("ext-theme-"))) {
	          JsonObject json = new JsonObject();
	          json.addProperty("id", folder.getName());
	          json.addProperty("thumbnail", "resources/themethumbnail/" + folder.getName() + ".png");
	          json.addProperty("path", "dependencies/ext-4.2.1/resources/" + folder.getName() + "/" + folder.getName() + "-all.css");
	          rs.add(json);
	        }
	      }
	    }catch (Exception e) {
			throw new PortalDataServiceException("获取样式列表失败", e);
		}
		return rs;
	}

	/**
	 * 装载应用程序数据
	 * 
	 * @param directList
	 *            Direct格式数据对象
	 */
	public void loadApplications(DirectList directList, User user) throws PortalDataServiceException {
		try {
			JsonArray datas = directList.getDatas();
			// 获得应用对象列表
			List<Application> applications = ApplicationService.instance()
					.findApplication();
			for (int i = 0; i < applications.size(); i++) {
				Application app = applications.get(i);
				JsonObject appJson = new JsonObject();
				appJson.addProperty("id", app.getId());
				appJson.addProperty("f_key", app.getF_key());
				appJson.addProperty("f_caption", app.getF_caption());
				//appJson.addProperty("f_url", app.getUrl());
				appJson.addProperty("f_url", app.getF_domain());
				appJson.addProperty("f_note", app.getF_note());
				appJson.addProperty("f_group", app.getF_group());
				datas.add(appJson);
			}
		} catch (ApplicationException e) {
			throw PortalDataServiceException.forLoadApplicationFaild(e
					.getMessage());
		}
	}

	/**
	 * 获得用户导航菜单数据
	 * 
	 * @param user
	 * @return
	 */
	public JsonArray getNavigators(User user) throws PortalDataServiceException{
		JsonArray datas = new JsonArray();
		try {
			String userExps = OrganizationRelationService.instance().getUserExpsStr(user.getId());
			List<ApplicationEntrance> appents = ApplicationEntranceService.instance().findApplicationEntranceByUser(userExps);
			/*// 根据级别和序号进行排序
			Collections.sort(appents, new Comparator<ApplicationEntrance>(){
				@Override
				public int compare(ApplicationEntrance o1,
						ApplicationEntrance o2) {
					if(o1 == o2){
						return 0;
					}
					if(o1 == null){
						return 1;
					}
					if(o2 == null){
						return -1;
					}
					if(o1.getF_level() != o2.getF_level()){
						return o1.getF_level() - o2.getF_level();
					}
					return o1.getF_order() - o2.getF_order();
				}
			});*/
			if(appents != null){
				for(ApplicationEntrance appent : appents){
					JsonObject menu1 = new JsonObject();
					menu1.addProperty("id", appent.getId());
					menu1.addProperty("f_parent_id", appent.getF_parent_id());
					menu1.addProperty("f_no",appent.getF_order());
					menu1.addProperty("f_key", appent.getF_key());
					menu1.addProperty("f_caption", appent.getF_caption());
					menu1.addProperty("f_disabled", appent.getF_disabled());
					if(appent.getF_icon() != null){
						menu1.addProperty("f_icon", appent.getF_icon());
					}
					menu1.addProperty("f_note", appent.getF_note());
					if(appent.getF_application_key() != null){
						menu1.addProperty("f_application_key", appent.getF_application_key());
						if(appent.getF_module_key() != null){
							menu1.addProperty("f_module_key", appent.getF_module_key());
						}
					}
					if(appent.getF_script() != null){
						menu1.addProperty("f_script", appent.getF_script());
					}
					datas.add(menu1);
				}
			}
		} catch (ApplicationEntranceException e) {
			throw new PortalDataServiceException("根据用户获取导航菜单失败", e);
		} catch (RelationException e) {
			throw new PortalDataServiceException("根据用户获取导航菜单失败", e);
		}
		return datas;
	}

	/**
	 * 装载导航菜单
	 * 
	 * @param user
	 */
	public void loadNavigators(DirectList directList, User user) throws PortalDataServiceException{
		directList.setDatas(getNavigators(user));
	}
	
	public JsonArray getPortlets(String[] appIds, User user) throws PortalDataServiceException{
		JsonArray rs = new JsonArray();
		if(appIds.length == 0){
			return rs;
		}
		try {
			List<Application> apps = new ArrayList<Application>();
			for (String appId : appIds) {
				if(appId == null || "".equals(appId)) {
					continue;
				}
				Application app = ApplicationService.instance().getApplicationById(Long.parseLong(appId));
				if(app != null){					
					apps.add(app);
				}
			}
			for (int i = 0; i < apps.size(); i++) {
				JsonObject appJo = new JsonObject();
				appJo.addProperty("f_key", apps.get(i).getF_key());
				appJo.addProperty("f_caption", apps.get(i).getF_caption());
				appJo.addProperty("iconCls", "icon-sys-portlet");
				appJo.addProperty("leaf", false);
				appJo.addProperty("expanded", true);
				String wsdlurl = getServiceUrl(apps.get(i));
				JsonArray portlets = DesignObjectService.instance().findObjects(wsdlurl, null, PORTLET_TYPE_NAME);
				if (portlets != null) {
					for (int j = 0; j < portlets.size(); j++) {
						portlets.get(j).getAsJsonObject().addProperty("key", apps.get(i).getF_key() + "." + portlets.get(j).getAsJsonObject().get("id").getAsString());
						portlets.get(j).getAsJsonObject().addProperty("f_app_key", apps.get(i).getF_key());
						if(portlets.get(j).getAsJsonObject().has("f_icon") && !portlets.get(j).getAsJsonObject().get("f_icon").isJsonNull()){							
							portlets.get(j).getAsJsonObject().addProperty("iconCls",portlets.get(j).getAsJsonObject().get("f_icon").getAsString());
						}
						portlets.get(j).getAsJsonObject().addProperty("leaf", true);
						portlets.get(j).getAsJsonObject().addProperty("checked", false);
						portlets.get(j).getAsJsonObject().remove("id");
					}
				}
				if(portlets.size() > 0) {
					appJo.add("children", portlets);
					rs.add(appJo);
				}
			}
		} catch (Exception e) {
			throw new PortalDataServiceException("获取门户栏目失败", e);
		}
		return rs;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public JsonArray getShortcuts(String[] appIds, User user) throws PortalDataServiceException{
		JsonArray rs = new JsonArray();
		if(appIds.length == 0){
			return rs;
		}
		try {
			List<Application> apps = new ArrayList<Application>();
			for (String appId : appIds) {
				if(appId == null || "".equals(appId)) {
					continue;
				}
				Application app = ApplicationService.instance().getApplicationById(Long.parseLong(appId));
				if(app != null){					
					apps.add(app);
				}
			}
			for(int i = 0; i < apps.size(); i++){
				JsonObject appJo = new JsonObject();
				appJo.addProperty("f_key", apps.get(i).getF_key());
				appJo.addProperty("f_caption", apps.get(i).getF_caption());
				appJo.addProperty("iconCls", "icon-sys-portlet");
				appJo.addProperty("leaf", false);
				appJo.addProperty("expanded", true);
				String wsdlurl = getServiceUrl(apps.get(i));
				JsonArray shortCuts = DesignObjectService.instance().findObjects(wsdlurl, null, SHORTCUT_TYPE_NAME);
				if(shortCuts != null){
					for(int j = 0; j < shortCuts.size(); j++){
						shortCuts.get(j).getAsJsonObject().addProperty("key", apps.get(i).getF_key() + "." + shortCuts.get(j).getAsJsonObject().get("id").getAsString());
						shortCuts.get(j).getAsJsonObject().addProperty("f_app_key", apps.get(i).getF_key());
						if(shortCuts.get(j).getAsJsonObject().has("f_icon") && !shortCuts.get(j).getAsJsonObject().get("f_icon").isJsonNull()) {							
							shortCuts.get(j).getAsJsonObject().addProperty("iconCls",shortCuts.get(j).getAsJsonObject().get("f_icon").getAsString());
						}
						shortCuts.get(j).getAsJsonObject().addProperty("leaf", true);
						shortCuts.get(j).getAsJsonObject().addProperty("checked", false);
						shortCuts.get(j).getAsJsonObject().remove("id");
					}
				}
				if(shortCuts.size() > 0){					
					appJo.add("children", shortCuts);
					rs.add(appJo);
				}
			}
		} catch (Exception e) {
			throw new PortalDataServiceException("获取快捷按钮失败", e);
		}
		return rs;
	}
	
	/**
	 * 获得门户显示列表
	 * @param directList
	 * @param user
	 * @return
	 */
	public void loadPortalViewList(DirectList directList, User user) throws PortalDataServiceException{
		JsonArray wallpapers = new JsonArray();

		String portalViewFolderName = "/resources/portalview";
		File portalViewFolder = new File(ContextHolder.instance().getRealPath(
				portalViewFolderName));
		try {
			File[] folders = portalViewFolder.listFiles();
			for (int i = 0; i < folders.length; i++) {
				File file = folders[i];
				if (file.isDirectory()) {
					continue;
				}

				if (file.getName().toLowerCase().lastIndexOf(".jpg") == -1
						&& file.getName().toLowerCase().lastIndexOf(".gif") == -1
						&& file.getName().toLowerCase().lastIndexOf(".png") == -1) {
					continue;
				}
				JsonObject json = new JsonObject();
				json.addProperty("id", file.getName().substring(0, file.getName().length() - 4));
				json.addProperty("view", portalViewFolderName
						+ "/" + file.getName());
				wallpapers.add(json);
			}
			directList.setDatas(wallpapers);
			directList.setTotal(wallpapers.size());
			directList.setSuccess(true);
		} catch (Exception e) {
			throw new PortalDataServiceException("获取门户视图列表失败", e);
		}

	}

	/**
	 * 获得墙纸列表
	 * 
	 * @param directList
	 * @param user
	 * @return
	 */
	public void getWallpaperList(DirectList directList, User user) throws PortalDataServiceException{
		JsonArray wallpapers = new JsonArray();

		String wallpaperFolderName = "/resources/wallpapers";
		File wallpaperFolder = new File(ContextHolder.instance().getRealPath(
				wallpaperFolderName));
		try {
			File[] folders = wallpaperFolder.listFiles();
			for (int i = 0; i < folders.length; i++) {
				File file = folders[i];
				if (file.isDirectory()) {
					continue;
				}

				if (file.getName().toLowerCase().lastIndexOf(".jpg") == -1
						&& file.getName().toLowerCase().lastIndexOf(".gif") == -1) {
					continue;
				}
				JsonObject json = new JsonObject();
				json.addProperty("id", file.getName());
				json.addProperty("thumbnail", wallpaperFolderName
						+ "/thumbnails/" + file.getName());
				wallpapers.add(json);
			}
			directList.setDatas(wallpapers);
			directList.setTotal(wallpapers.size());
		} catch (Exception e) {
			throw new PortalDataServiceException("加载壁纸列表失败", e);
		}

	}

	/**
	 * 保存用户桌面配置
	 * 
	 * @param modelKey
	 * @param preferences
	 * @param user
	 */
	public void savePreferences(String modelKey, JsonObject preferences,
			User user) {
		System.out.println(preferences.toString());
	}
	
	private String getServiceUrl(Application app) {
		if(app == null){
			return null;
		}
		String url = app.getUrl() + "/services/DesignObjectAccessService?wsdl";
		return url;
//		return "http://localhost:8080/app/services/DesignObjectAccessService?wsdl";
	}

}
