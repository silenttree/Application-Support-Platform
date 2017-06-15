package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.dictionary.access.IDictionaryReader;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * @author zhsq
 * 
 *         <pre>
 * 包装DictionaryReaderImp,为其他程序提供web service
 * 其中的方法和被包装的类一一对应
 * 所有方法的参数处理成字符串
 * 所有重载的方法重新起名字
 * 返回值为一个标准的json字符串
 * 	其中success标识成功与否
 * 	data为成功时的返回数据
 * 	message为失败时的错误信息
 * </pre>
 */
@WebService
public class DictionaryWebService {
	private IDictionaryReader reader;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(IDictionaryReader reader) {
		this.reader = reader;
	}
	
	public String findDictionaryById(String dicIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			Dictionary dic = reader.findDictionaryById(dicId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dic));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getDictionaryList(String dicIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			List<Dictionary> dics = reader.getDictionaryList(dicId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dics));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getDictionaryDataList(String dicIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			List<DictionaryData> dicDatas = reader.getDictionaryDataList(dicId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dicDatas));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findDictionaryDataById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			DictionaryData dicData = reader.findDictionaryDataById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dicData));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	/**
	 * 这个方法的reader方法声明有问题
	 * @param key
	 * @return
	 */
	public String findDictionaryByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			Dictionary dic = reader.findDictionaryById(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dic));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典获取失败");
		}
		return gson.toJson(result);
	}
	
	/**
	 * 这个方法的reader方法声明有问题
	 * @param key
	 * @return
	 */
	public String findDictionaryDataByKey(String key, String dicIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			DictionaryData dicData = reader.findDictionaryDataById(key, dicId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dicData));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据名称获取用户失败");
		}
		return gson.toJson(result);
	}
	
	public String getDictionaryDataListByParId(String dicIdStr, String dataIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			long dataId = Long.parseLong(dataIdStr);
			List<DictionaryData> dicDatas = reader
					.getDictionaryDataListByParId(dicId, dataId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dicDatas));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getDictionaryDataListByDicId(String dicIdStr,
			String pageStr, String limitStr, String query) {
		JsonObject result = new JsonObject();
		try {
			long dicId = Long.parseLong(dicIdStr);
			int page = Integer.parseInt(pageStr);
			int limit = Integer.parseInt(limitStr);
			List<DictionaryData> dicDatas = reader
					.getDictionaryDataListByDicId(dicId, page, limit, query);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dicDatas));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getExtPropertyByDicDataKey(String dicDataKey, String dicKey, String fieldName) {
		JsonObject result = new JsonObject();
		try {
			ExtpropertyData extpData = reader.getExtPropertyByDicDataKey(dicDataKey, dicKey, fieldName);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(extpData));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取字典扩展属性失败");
		}
		return gson.toJson(result);
	} 
	
	public String getExtPropertyByDicDataId(String dicDataIdStr, String fieldName) {
		JsonObject result = new JsonObject();
		try {
			long dicDataId = Long.parseLong(dicDataIdStr);
			ExtpropertyData extpData = reader.getExtPropertyByDicDataId(dicDataId, fieldName);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(extpData));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取字典扩展属性失败");
		}
		return gson.toJson(result);
	} 

	public String getDicDataListByKeyAndExtend(String dicKey,
			String fieldName, String filedValue) {
		JsonObject result = new JsonObject();
		try {
			List<DictionaryData> list = reader.getDicDataListByKeyAndExtend(dicKey, fieldName, filedValue);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (DictionaryException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取字典失败");
		}
		return gson.toJson(result);
	} 
}
