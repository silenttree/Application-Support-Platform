package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.doc.access.IDocReader;
import com.asc.commons.doc.entity.Doc;
import com.asc.commons.doc.exception.DocException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;


@WebService
public class DocWebService {
	private IDocReader reader;
	private Gson gson = new Gson();
	
	@WebMethod(exclude = true)
	public void setReader(IDocReader reader) {
		this.reader = reader;
	}
	
	/**
	 * 根据父ID获取文档列表
	 * @param parentIdStr
	 * @return
	 */
	public String findDocsByParentId(String parentIdStr) {
		JsonObject result = new JsonObject();
		try {
			long parentId = Long.parseLong(parentIdStr);
			List<Doc> datas = reader.findDocsByParentId(parentId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(datas));
		} catch (DocException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "文档数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	/**
	 * 查询所有的文档
	 * @return
	 */
	public String findAll() {
		JsonObject result = new JsonObject();
		try {
			List<Doc> datas = reader.findAll();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(datas));
		} catch (DocException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "文档数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	/**
	 * 根据文档ID获取文档对象
	 * @param idStr
	 * @return
	 */
	public String getDocById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			Doc data = reader.getDocById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(data));
		} catch (DocException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "文档数据获取失败");
		}
		return gson.toJson(result);
	}
	
	/**
	 * 根据KEY获取文档列表
	 * @param key
	 * @return
	 */
	public String findDocsByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			List<Doc> data = reader.findDocsByKey(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(data));
		} catch (DocException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "文档数据列表获取失败");
		}
		return gson.toJson(result);
	}
}
