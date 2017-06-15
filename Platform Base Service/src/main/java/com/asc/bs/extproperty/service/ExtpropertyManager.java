package com.asc.bs.extproperty.service;

import java.util.Date;

import com.asc.bs.extproperty.access.imp.ExtpropertyWriteImp;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.extproperty.access.IExtpropertyWrite;
import com.asc.commons.extproperty.entity.Extproperty;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.asc.commons.extproperty.exception.ExtpropertyException;
import com.asc.commons.extproperty.service.ExtpropertyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class ExtpropertyManager {
	private static ExtpropertyManager singleton;
	private IExtpropertyWrite extproWrite;

	public static ExtpropertyManager instance() {
		if (singleton == null) {
			singleton = new ExtpropertyManager();
		}
		return singleton;
	}
	
	public ExtpropertyManager(){
		extproWrite = new ExtpropertyWriteImp();
	}
	
	/**
	 * 保存扩展属性
	 * 
	 * @param dataId
	 * @param data
	 * @return
	 * @throws ExtpropertyException
	 */
	public Extproperty saveExtproperty(long dataId, JsonObject data) throws ExtpropertyException {
		if(data.has("f_field_name")){
			long num = ExtpropertyService.instance().findExtpropertyListByFieldName(data.get("f_field_name").getAsString(), dataId);
			if(num > 0){
				throw new ExtpropertyException("字段重复！");
			}
		}
		
		Extproperty extproperty = null;
		//判断是更新数据还是添加数据
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		boolean iscreate = false;
		if(id > 0){
			extproperty = ExtpropertyService.instance().getExtproperty(id);
			if(extproperty == null){
				iscreate = true;
				extproperty = new Extproperty();
			}
		}else{
			iscreate = true;
			extproperty = new Extproperty();
		}
		//从json把数据添加到对象
		JsonObjectTool.jsonObject2Object(data, extproperty);
		//添加配置
		if(data.has("f_config") && data.get("f_config").isJsonObject()){
			extproperty.setF_config(data.get("f_config").getAsJsonObject().toString());
		}
		
		if("".equals(extproperty.getF_field_name())){
			throw new ExtpropertyException("字段不能为空！");
		}
		
		if("".equals(extproperty.getF_field_caption())){
			throw new ExtpropertyException("字段名不能为空！");
		}
		//添加对象则添加创建时间
		if(iscreate){
			extproperty.setF_create_time(new Date());
			extproperty.setF_data_id(dataId);
		}
		extproperty.setF_update_time(new Date());
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			extproperty.save();
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		return extproperty;
	}

	/**
	 * 删除扩展属性
	 * 
	 * @param id
	 * @throws ExtpropertyException
	 */
	public void deleteExtproperty(long id) throws ExtpropertyException {
		extproWrite.deleteExtproperty(id);
	}

	/**
	 * 保存扩展属性数据
	 * 
	 * @param id
	 * @param dataId
	 * @param tablename
	 * @param data
	 * @throws ExtpropertyException
	 */
	public void saveExtpropertyData(long id, long dataId, String tablename, JsonObject data) throws ExtpropertyException {
		//获取表的扩展属性并转为jsonarray
		JsonArray extpropertys = JsonObjectTool.objectList2JsonArray(ExtpropertyService.instance().findExtpropertyListByTablename(tablename, dataId));
		//保存字典的扩展属性
		if(extpropertys.size() > 0){
			for(int i = 0; i < extpropertys.size(); i++){
				JsonObject extproperty = extpropertys.get(i).getAsJsonObject();
				//如果跟新或添加的data包含扩展属性数据则添加改扩展属性数据
				if(data.has(extproperty.get("f_field_name").getAsString())){
					//查找扩展属性数据是否存在 不存在则创建
					ExtpropertyData extpropertyData = ExtpropertyService.instance().getExtprodata(id, extproperty.get("id").getAsLong());
					if(extpropertyData == null){
						extpropertyData = new ExtpropertyData();
					}
					//添加扩展属性字段
					extpropertyData.setF_dataid(id); //添加数据id
					extpropertyData.setF_value(data.get(extproperty.get("f_field_name").getAsString()).getAsString()); //添加属性数据
					extpropertyData.setF_property_id(extproperty.get("id").getAsLong()); //添加扩展属性id
					//保存扩展属性数据
					IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
					try {
						extpropertyData.save();
						tx.commit();
					} catch (DbAccessException e) {
						tx.rollback();
					} finally {
						CommonDatabaseAccess.instance().endTransaction();
					}
				}
			}
		}
	}
}
