package com.asc.portal.preferences.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Preferences {
	private String key;		// 用户名或默认桌面配置名
	
	private Theme theme;	// 界面风格样式

	private String wallpaper;	// 桌面墙纸
	
	private String wallpaperposition; //墙纸居中还是平铺
	
	private String bgcolor;		// 背景颜色
	
	private String navigatorMode;	// 导航条显示模式

	private int columns;	// 门户栏目数量
	
	private String uimode;
	
	private String wsMode;
	
	private List<Portlet> portlets;	//桌面栏目列表
	
	private List<Shortcut> shortcuts;	// 快捷菜单列表
	
	public Preferences(){
		key = "(default)";
		theme = null;
		wallpaperposition = "center";
		wallpaper = "01.jpg";
		bgcolor = "#FFFFFF";
		navigatorMode = "IconText";
		columns = 3;
		uimode = "webpage";
		wsMode = "AscDesktopWorkspaceMulti";
		portlets = new ArrayList<Portlet>();
		shortcuts = new ArrayList<Shortcut>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public String getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getNavigatorMode() {
		return navigatorMode;
	}

	public void setNavigatorMode(String navigatorMode) {
		this.navigatorMode = navigatorMode;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public List<Portlet> getPortlets() {
		return portlets;
	}

	public void setPortlets(List<Portlet> portlets) {
		this.portlets = portlets;
	}

	public List<Shortcut> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(List<Shortcut> shortcuts) {
		this.shortcuts = shortcuts;
	}
	
	
	public String getUimode() {
		return uimode;
	}

	public void setUimode(String uimode) {
		this.uimode = uimode;
	}
	
	

	/**
	 * @return the wsMode
	 */
	public String getWsMode() {
		return wsMode;
	}

	/**
	 * @param wsMode the wsMode to set
	 */
	public void setWsMode(String wsMode) {
		this.wsMode = wsMode;
	}

	/**
	 * 从JSON对象中读取参数
	 * @param json
	 */
	public void fromJson(JsonObject json){
		if(json != null){
			if(json.has("key") && !json.get("key").isJsonNull()){
				key = json.get("key").getAsString();
			}
			if(json.has("theme") && !json.get("theme").isJsonNull()){
				Theme t = new Theme();
				t.fromJsonObject(json.get("theme").getAsJsonObject());
				theme = t;
			}
			if(json.has("wallpaper") && !json.get("wallpaper").isJsonNull()){
				wallpaper = json.get("wallpaper").getAsString();
			}
			if(json.has("bgcolor") && !json.get("bgcolor").isJsonNull()){
				bgcolor = json.get("bgcolor").getAsString();
			}
			if(json.has("navigatorMode") && !json.get("navigatorMode").isJsonNull()){
				navigatorMode = json.get("navigatorMode").getAsString();
			}
			if(json.has("wsMode") && !json.get("wsMode").isJsonNull()){
				wsMode = json.get("wsMode").getAsString();
			}
			if(json.has("columns") && !json.get("columns").isJsonNull()){
				columns = json.get("columns").getAsInt();
			}
			
			if(json.has("webpage") && !json.get("webpage").isJsonNull()){
				uimode = json.get("webpage").getAsString();
			}
			if(json.has("shortcuts") && !json.get("shortcuts").isJsonNull()){
				shortcuts.clear();
				JsonArray datas = json.get("shortcuts").getAsJsonArray();
				for(int i=0;i<datas.size();i++){
					if(!datas.get(i).isJsonNull()){						
						JsonObject jsonObject = datas.get(i).getAsJsonObject();
						Shortcut dsc = new Shortcut();
						dsc.fromJsonObject(jsonObject);
						shortcuts.add(dsc);
					}
				}
			}
			if(json.has("portlets") && !json.get("portlets").isJsonNull()){
				portlets.clear();
				JsonArray subjectArray = json.get("portlets").getAsJsonArray();
				for(int i=0;i<subjectArray.size();i++){
					JsonObject jsonObject = subjectArray.get(i).getAsJsonObject();
					Portlet ds = new Portlet();
					ds.fromJsonObject(jsonObject);
					portlets.add(ds);
				}
			}
			if(json.has("wallpaperposition") && !json.get("wallpaperposition").isJsonNull()){
				wallpaperposition = json.get("wallpaperposition").getAsString();
			}
		}
	}
	/**
	 * 生成Json对象
	 * @return
	 */
	public JsonObject toJsonObject(){
		JsonObject json = new JsonObject();
		json.addProperty("key", key);
		if(theme != null) {			
			JsonObject t = theme.toJsonObject();
			json.add("theme", t);
		}
		json.addProperty("wallpaper", wallpaper);
		json.addProperty("columns", columns);
		json.addProperty("uimode", uimode);
		json.addProperty("wsMode", wsMode);
		json.addProperty("navigatorMode", navigatorMode);
		json.addProperty("wallpaper", wallpaper);
		json.addProperty("bgcolor", bgcolor);
		json.addProperty("wallpaperposition", wallpaperposition);
		// 桌面按钮
		JsonArray shortcutArrays = new JsonArray();
		for(int i=0;i<shortcuts.size();i++){
			Shortcut shortcut = shortcuts.get(i);
			shortcutArrays.add(shortcut.toJsonObject());
		}
		json.add("shortcuts", shortcutArrays);
		// 桌面栏目
		JsonArray portletArrays = new JsonArray();
		for(int i=0;i<portlets.size();i++){
			Portlet portlet = portlets.get(i);
			portletArrays.add(portlet.toJsonObject());
		}
		json.add("portlets", portletArrays);
		return json;
	}
	
	public String getWallpaperposition() {
		return wallpaperposition;
	}

	public void setWallpaperposition(String wallpaperposition) {
		this.wallpaperposition = wallpaperposition;
	}

	/**
	 * 生成Json字符串
	 */
	public String toString(){
		return toJsonObject().toString();
	}
}
