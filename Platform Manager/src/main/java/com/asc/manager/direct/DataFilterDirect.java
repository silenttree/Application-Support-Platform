package com.asc.manager.direct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.datafilter.entity.Policy;
import com.asc.commons.datafilter.exception.DfSettingsAccessException;
import com.asc.commons.datafilter.service.DataFilterSettigsService;
import com.asc.commons.datafilter.service.DataFilterSettigsServiceWriter;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.framework.designobject.service.DesignObjectService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class DataFilterDirect {
	
	@DirectMethod
	public JsonObject listPoliciesByAppid(long appIds){
		JsonObject json = new JsonObject();
		JsonArray array = new JsonArray();
		String appId = Long.toString(appIds);
		try {
			List<Policy> list = DataFilterSettigsService.instance().listPolicys(appId);
			for(Policy policy:list){
				JsonObject obj = new JsonObject();
				obj.addProperty("f_app_name", policy.getF_app_title() + "-" + policy.getF_app_id());
				obj.addProperty("f_module_name", policy.getF_module_title() + "(" + policy.getF_module_id() + ")");
				obj.addProperty("id", policy.getId());
				obj.addProperty("f_function_name", policy.getF_function_name());
				obj.addProperty("f_object_name", policy.getF_object_name() + "(" + policy.getF_object_id() + ")");
				obj.addProperty("f_ue_captions", policy.getF_ue_captions());
				obj.addProperty("f_object_name", policy.getF_object_name() + "(" + policy.getF_object_id() + ")");
				obj.addProperty("f_ue_captions", policy.getF_ue_captions());
				obj.addProperty("f_tablename", policy.getF_tablename());
				obj.addProperty("f_sql_where", policy.getF_sql_where());
				obj.addProperty("f_note", policy.getF_note());
				obj.addProperty("f_create_time", getFormatDate(policy.getF_create_time()));
				obj.addProperty("f_creator_id", policy.getF_creator_id());
				obj.addProperty("f_creator_name", policy.getF_creator_name());
				obj.addProperty("f_ue_expressions", policy.getF_ue_expressions());
				array.add(obj);
			}
			json.addProperty("success", true);
			json.add("datas", array);
		} catch (DfSettingsAccessException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject getPolicuById(long id){
		JsonObject json = new JsonObject();
		JsonObject obj = new JsonObject();
		try {
			Policy policy= DataFilterSettigsService.instance().getPolicy(id);
			obj = JsonObjectTool.object2JsonObject(policy);
			json.addProperty("success", true);
			json.add("datas", obj);
		} catch (DfSettingsAccessException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject addPolicy(JsonObject data){
		JsonObject json = new JsonObject();
		Policy policy = JsonObjectTool.jsonObject2Object(data, Policy.class);
		try {
			DataFilterSettigsServiceWriter.instance().addPolicy(policy);
			json.addProperty("success", true);
		} catch (DfSettingsAccessException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject deletePolicy(long id){
		JsonObject json = new JsonObject();
		try {
			DataFilterSettigsServiceWriter.instance().deletePolicy(id);
			json.addProperty("success", true);
		} catch (DfSettingsAccessException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject copyPolicy(String[] arr){
		JsonObject json = new JsonObject();
		try {
			DataFilterSettigsServiceWriter.instance().addPolicyByArray(arr);
			json.addProperty("success", true);
		} catch (DfSettingsAccessException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject listModulesByAppId(long appId){
		JsonObject json = new JsonObject();
		try {
			JsonArray modules = DesignObjectService.instance().findObjects(this.getServiceUrl(appId), null, "module");
			JsonArray array  = new JsonArray();
			for(JsonElement element: modules){
				JsonObject obj = new JsonObject();
				obj.addProperty("key", element.getAsJsonObject().get("id").getAsString());
				obj.addProperty("value", element.getAsJsonObject().get("f_caption").getAsString());
				array.add(obj);
			}
			json.add("datas", array);
			json.addProperty("success", true);
		} catch (DesignObjectException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject listModuleNamesByAppId(long appId){
		JsonObject json = new JsonObject();
		try {
			JsonArray modules = DesignObjectService.instance().findObjects(this.getServiceUrl(appId), null, "module");
			JsonArray array  = new JsonArray();
			for(JsonElement element: modules){
				JsonObject obj = new JsonObject();
				obj.addProperty("key", element.getAsJsonObject().get("id").getAsString());
				obj.addProperty("value", element.getAsJsonObject().get("f_caption").getAsString());
				array.add(obj);
			}
			json.add("datas", array);
			json.addProperty("success", true);
		} catch (DesignObjectException e) {
			json.addProperty("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonArray listObjectsByAppIdAndModuleId(String appId,String moduleId){
		JsonArray object = new JsonArray();
		String []noContain = new String[]{"modulerole","authoritymap","extstate","useridentity","state"};
		try {
			object = DesignObjectService.instance().loadCheckTreeObjectsAndRefer(this.getServiceUrl(Long.parseLong(appId)), moduleId, noContain);
		} catch (DesignObjectException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	
	private String getServiceUrl(long appId) {
		Application app;
		try {
			app = ApplicationService.instance().getApplicationById(appId);
		} catch (ApplicationException e) {
			return null;
		}
		if (app == null) {
			return null;
		}
		String url = app.getF_domain() + "/services/DesignObjectAccessService?wsdl";
		return url;
	}
	
	@SuppressWarnings("unused")
	private String getJsonData(JsonObject json, String key) {
		String value = null;
		if (json != null && !json.isJsonNull()) {
			Object v = json.get(key);
			if (v != null && !json.get(key).isJsonNull()) {
				value = json.get(key).getAsString();
			}
		}
		return value;
	}
	
	private String getFormatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sdate = format.format(date);
		return sdate;
	}
}
