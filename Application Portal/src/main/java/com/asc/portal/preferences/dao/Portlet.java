package com.asc.portal.preferences.dao;

import com.google.gson.JsonObject;

public class Portlet {

	private String id;

	private String key;

	private String appKey;

	private String portletId;

	private String f_app_key;

	private String f_caption;

	private String f_icon;

	private String f_key;

	private String f_name;

	private long f_refresh_interval;

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
			if (json.has("portletId") && !json.get("portletId").isJsonNull()) {
				portletId = json.get("portletId").getAsString();
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
			if (json.has("f_refresh_interval")
					&& !json.get("f_refresh_interval").isJsonNull()) {
				f_refresh_interval = json.get("f_refresh_interval").getAsLong();
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
		if (id != null) {
			json.addProperty("id", id);
		}
		if (key != null) {
			json.addProperty("key", key);
		}
		if (appKey != null) {
			json.addProperty("appKey", appKey);
		}
		json.addProperty("portletId", portletId);
		if (f_app_key != null) {
			json.addProperty("f_app_key", f_app_key);
		}
		if (f_caption != null) {
			json.addProperty("f_caption", f_caption);
		}
		if (f_icon != null) {
			json.addProperty("f_icon", f_icon);
		}
		if (f_key != null) {
			json.addProperty("f_key", f_key);
		}
		if (f_name != null) {			
			json.addProperty("f_name", f_name);
		}
		if (f_refresh_interval != 0L) {			
			json.addProperty("f_refresh_interval", f_refresh_interval);
		}
		return json;
	}

	@Override
	public String toString() {
		return "Portlet [id=" + id + ", f_app_key=" + f_app_key
				+ ", f_caption=" + f_caption + ", f_icon=" + f_icon
				+ ", f_key=" + f_key + ", f_name=" + f_name
				+ ", f_refresh_interval=" + f_refresh_interval + "]";
	}

	public String getId() {
		return id;
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
	 * @param appKey
	 *            the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * @return the portletId
	 */
	public String getPortletId() {
		return portletId;
	}

	/**
	 * @param portletId
	 *            the portletId to set
	 */
	public void setPortletId(String portletId) {
		this.portletId = portletId;
	}

	public String getF_app_key() {
		return f_app_key;
	}

	public void setF_app_key(String f_app_key) {
		this.f_app_key = f_app_key;
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

	public long getF_refresh_interval() {
		return f_refresh_interval;
	}

	public void setF_refresh_interval(long f_refresh_interval) {
		this.f_refresh_interval = f_refresh_interval;
	}

}
