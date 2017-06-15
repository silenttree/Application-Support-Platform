package com.asc.bs.log.service;

import java.util.Date;

import com.asc.bs.log.access.imp.LogConfigWriteImpl;
import com.asc.commons.log.access.ILogConfigWrite;
import com.asc.commons.log.entity.LogConfigEntity;
import com.asc.commons.log.exception.LogException;
import com.asc.commons.log.service.LogConfigService;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class LogConfigManager {
	private ILogConfigWrite logWriter;
	private static LogConfigManager singleton;

	public static LogConfigManager instance() {
		if (singleton == null) {
			singleton = new LogConfigManager();
		}
		return singleton;
	}

	public LogConfigManager() {
		logWriter = new LogConfigWriteImpl();
	}
	
	/**
	 * 保存日志配置
	 * 
	 * @param data
	 * @return
	 * @throws LogException
	 */
	public LogConfigEntity saveLogConf(String appkey, JsonObject data) throws LogException {
		LogConfigEntity logConfig;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		
		boolean isCreate = false;
		if(id > 0){
			logConfig = LogConfigService.instance().getLogConf(id);
			if(logConfig == null){
				isCreate = true;
				logConfig = new LogConfigEntity();
			}
		}else{
			isCreate = true;
			logConfig = new LogConfigEntity();
		}
		
		JsonObjectTool.jsonObject2Object(data, logConfig);
		if(data.has("f_targets") && !data.get("f_targets").isJsonNull()){
			logConfig.setF_targets(data.get("f_targets").getAsJsonObject().toString());
		}
		if(isCreate){
			logConfig.setF_create_time(new Date());
			logConfig.setF_application_id(appkey);
		}
		logConfig.setF_update_time(new Date());
		
		logWriter.saveLogConfig(logConfig);
		return logConfig;
	}

	/**
	 * 删除日志配置
	 * 
	 * @param id
	 * @throws LogException
	 */
	public void delLogConf(long id) throws LogException {
		logWriter.delLogConf(id);
	}
}
