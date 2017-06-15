package com.asc.portal.preferences.dao;

import com.google.gson.JsonObject;

public class Shortcut {
	
	private String id; // 分组sys或appid
	
	private String key;
	
	private String appKey;
	
	private String shortcutId;
	
	private String f_app_key;

	private String f_caption; // 显示标题

	private String f_icon; // 图标

	private String f_key; // 标识

	private String f_name; // 名称

	private long f_order; // 序号
	
	private String f_script;
	

	public String getF_app_key() {
		return f_app_key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @return the appKey
	 */
	public String getAppKey() {
		return appKey;
	}

	/**
	 * @param appKey the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * @return the shortcutId
	 */
	public String getShortcutId() {
		return shortcutId;
	}

	/**
	 * @param shortcutId the shortcutId to set
	 */
	public void setShortcutId(String shortcutId) {
		this.shortcutId = shortcutId;
	}

	public void setF_app_key(String f_app_key) {
		this.f_app_key = f_app_key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getF_caption() {
		return f_caption;
	}

	public void setF_caption(String f_caption) {
		this.f_caption = f_caption;
	}

	public String getF_icon() {
		return f_icon;
	}

	public void setF_icon(String f_icon) {
		this.f_icon = f_icon;
	}

	public String getF_key() {
		return f_key;
	}

	public void setF_key(String f_key) {
		this.f_key = f_key;
	}

	public String getF_name() {
		return f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public long getF_order() {
		return f_order;
	}

	public void setF_order(long f_order) {
		this.f_order = f_order;
	}

	public String getF_script() {
		return f_script;
	}

	public void setF_script(String f_script) {
		this.f_script = f_script;
	}

	/**
	 * 从JSON对象初始化值
	 * 
	 * @param json
	 */
	public void fromJsonObject(JsonObject json) {
		if (json != null) {
			if (json.has("id") && !json.get("id").isJsonNull()) {
				id = json.get("id").getAsString();
			}
			if (json.has("key") && !json.get("key").isJsonNull()) {
				key = json.get("key").getAsString();
			}
			if (json.has("appKey") && !json.get("appKey").isJsonNull()) {
				appKey = json.get("appKey").getAsString();
			}
			if (json.has("shortcutId") && !json.get("shortcutId").isJsonNull()) {
				shortcutId = json.get("shortcutId").getAsString();
			}
			if (json.has("f_app_key") && !json.get("f_app_key").isJsonNull()) {
				f_app_key = json.get("f_app_key").getAsString();
			}
			if (json.has("f_caption") && !json.get("f_caption").isJsonNull()) {
				f_caption = json.get("f_caption").getAsString();
			}
			if (json.has("f_icon") && !json.get("f_icon").isJsonNull()) {
				f_icon = json.get("f_icon").getAsString();
			}
			if (json.has("f_key") && !json.get("f_key").isJsonNull()) {
				f_key = json.get("f_key").getAsString();
			}
			if (json.has("f_name") && !json.get("f_name").isJsonNull()) {
				f_name = json.get("f_name").getAsString();
			}
			if (json.has("f_order") && !json.get("f_order").isJsonNull()) {
				f_order = json.get("f_order").getAsLong();
			}
			if (json.has("f_script") && !json.get("f_script").isJsonNull()) {
				f_script = json.get("f_script").getAsString();
			}
		}
	}

	/**
	 * 生成Json对象
	 * 
	 * @return
	 */
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		addStrProperty(json, "id", id);
		addStrProperty(json, "key", key);
		addStrProperty(json, "appKey", appKey);
		addStrProperty(json, "shortcutId", shortcutId);
		addStrProperty(json, "f_app_key", f_app_key);
		addStrProperty(json, "f_caption", f_caption);
		addStrProperty(json, "f_icon", f_icon);
		addStrProperty(json, "f_key", f_key);
		addStrProperty(json, "f_name", f_name);
		addStrProperty(json, "f_script", f_script);
		if(f_order != 0L){			
			json.addProperty("f_order", f_order);
		}
		return json;
	}
	
	/**
	 * 如果是空的话就不加了
	 * @param json
	 * @param property
	 */
	private void addStrProperty(JsonObject json, String propertyName, String value) {
		if(value != null && !"".equals(value)){
			json.addProperty(propertyName, value);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Shortcut [id=" + id + ", key=" + key + ", appKey=" + appKey
				+ ", shortcutId=" + shortcutId + ", f_app_key=" + f_app_key
				+ ", f_caption=" + f_caption + ", f_icon=" + f_icon
				+ ", f_key=" + f_key + ", f_name=" + f_name + ", f_order="
				+ f_order + ", f_script=" + f_script + "]";
	}
	
}
