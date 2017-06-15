package com.asc.common.direct;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DirectList {
	// 获取数据结果
	private boolean success;
	// 数据总数
	private int total;
	// 返回消息
	private String message;
	// 数据
	private JsonArray datas;
	// 数据关键字段名
	private String idName;
	// 构造函数
	public DirectList(){
		datas = new JsonArray();
		idName = "id";
		message = "";
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	
	public JsonArray getDatas() {
		return datas;
	}
	public void setDatas(JsonArray datas) {
		this.datas = datas;
	}

	/**
	 * 输出返回串
	 * @return
	 */
	public JsonObject getOutput(){
		JsonObject metaData = new JsonObject();
		metaData.addProperty("root", "datas");
		metaData.addProperty("idProperty", idName);
		metaData.addProperty("totalProperty", "total");
		metaData.addProperty("successProperty", "success");
		metaData.addProperty("messageProperty", "message");
		JsonObject json = new JsonObject();
		json.addProperty("total", total == 0 ? datas.size(): total);
		json.addProperty("success", success);
		json.addProperty("message", message);
		json.add("datas", datas);
		//json.add("metaData", metaData);
		return json;
	}
}
