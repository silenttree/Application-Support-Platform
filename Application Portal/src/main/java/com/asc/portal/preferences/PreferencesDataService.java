package com.asc.portal.preferences;

import java.io.File;

import com.asc.commons.context.ContextHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PreferencesDataService {
	// 墙纸存储路径
	static final String wallpaperFolderName = "resources/wallpapers";
	// 界面样式路径
	static final String themeFolderName = "dependencies/ext-4.2.1/resources";
	
	private static PreferencesDataService singleton;

	public static PreferencesDataService instance() {
		if (singleton == null) {
			singleton = new PreferencesDataService();
		}
		return singleton;
	}
	/**
	 * 获得墙纸列表
	 * @return
	 */
	public JsonArray getWallpapers(){
		JsonArray wallpapers = new JsonArray();
		
		File wallpaperFolder = new File(ContextHolder.instance().getRealPath(wallpaperFolderName));
		try{
			File[] folders = wallpaperFolder.listFiles();
			for (int i = 0; i < folders.length; i++) {
				File file = folders[i];
				if(file.isDirectory()){
					continue;
				}
				if (file.getName().toLowerCase().lastIndexOf(".jpg") == -1 && file.getName().toLowerCase().lastIndexOf(".gif") == -1) {
					continue;
				}
				JsonObject json = new JsonObject();
				json.addProperty("id", file.getName());
				json.addProperty("thumbnail", wallpaperFolderName + "/thumbnails/" + file.getName());
				
				wallpapers.add(json);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return wallpapers;
	}
	/**
	 * 获得墙纸列表
	 * @return
	 */
	public JsonArray getThemes(){
		JsonArray themes = new JsonArray();
		
		File themeFolder = new File(ContextHolder.instance().getRealPath(themeFolderName));
		try{
			File[] folders = themeFolder.listFiles();
			for (int i = 0; i < folders.length; i++) {
				File file = folders[i];
				if(!file.isDirectory() || file.getName().startsWith("ext-theme-")){
					continue;
				}
				String key = file.getName().replaceAll("ext-theme-", "");
				JsonObject json = new JsonObject();
				json.addProperty("id", key);
				json.addProperty("path", themeFolderName + "/" + file.getName() + "/" + file.getName() + "-all.css");
				
				themes.add(json);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return themes;
	}
	/**
	 * 获得桌面栏目列表
	 * @return
	 */
	public JsonArray getPortlets(){
		JsonArray portlets = new JsonArray();

		return portlets;
	}
	/**
	 * 获得桌面按钮列表
	 * @return
	 */
	public JsonArray getShortcuts(){
		JsonArray shortcuts = new JsonArray();

		return shortcuts;
	}
}
