package com.asc.bs.datafilter.service;

import com.asc.commons.context.ContextHolder;
import com.asc.commons.datafilter.access.IDataFilterSettingsWriter;
import com.asc.commons.datafilter.entity.Policy;
import com.asc.commons.datafilter.exception.DfSettingsAccessException;

/**
 * <pre>
 * Platform Base Service
 * 数据过滤设置数据存储实现代理类
 * 
 * Mixky Co., Ltd. 2016
 * @author Bill
 * </pre>
 */
public class DataFilterSettingsManager {
	private static DataFilterSettingsManager singleton;
	private IDataFilterSettingsWriter writerImpl;
	
	public static DataFilterSettingsManager instance() {
		if (singleton == null) {
			try {
				singleton = ContextHolder.instance().getBean("DataFilterSettingsManager");
			} catch (Exception e) {
				// 
			}
		}
		return singleton;
	}
	
	public void setWriterImpl(IDataFilterSettingsWriter writerImpl) {
		this.writerImpl = writerImpl;
	}
	
	/**
	 * 添加新的数据过滤策略配置
	 * 
	 * @param policy
	 * @throws DfSettingsAccessException
	 */
	public void addPolicy(Policy policy) throws DfSettingsAccessException {
		if (policy == null) {
			throw DfSettingsAccessException.forIllegalArgumentException("未提交注册策略或策略实例为空");
		}
		writerImpl.addPolicy(policy);
	}
	
	/**
	 * 删除已定义策略
	 * 
	 * @param pId
	 * @throws DfSettingsAccessException
	 */
	public void deletePolicy(long pId) throws DfSettingsAccessException {
		if (pId < 1) {
			throw DfSettingsAccessException.forIllegalArgumentException("未设置删除策略Id或Id为0");
		}
		writerImpl.deletePolicy(pId);
	}
	
}
