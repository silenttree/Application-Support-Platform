package com.asc.portal.preferences.dao;

import com.google.gson.JsonObject;

public class Theme {
	private String id;
	private String thumbnail;	//缩略图地址
	private String path;		//CSS路径
	
	/**
	 * 从JSON对象初始化值
	 * @param json
	 */
	public void fromJsonObject(JsonObject json){
		if(json != null){
			if(json.has("id") && !json.get("id").isJsonNull()){
				id = json.get("id").getAsString();
			}
			if(json.has("thumbnail") && !json.get("thumbnail").isJsonNull()){
				thumbnail = json.get("thumbnail").getAsString();
			}
			if(json.has("path") && !json.get("path").isJsonNull()){
				path = json.get("path").getAsString();
			}
		}
	}
	
	/**
	 * 生成Json对象
	 * @return
	 */
	public JsonObject toJsonObject(){
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("thumbnail", thumbnail);
		json.addProperty("path", path);
		return json;
	}
	
	/**
	 * 生成Json字符串
	 */
	public String toString(){
		return toJsonObject().toString();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	

}
