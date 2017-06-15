package com.asc.portal.preferences.serialize;

import java.util.List;

import com.asc.portal.exception.PortalDataServiceException;
import com.asc.portal.preferences.dao.Preferences;

public interface IPreferencesSerialize {
	/**
	 * 查找是否存在特定的参数选择对象
	 * @param key
	 * @return
	 */
	public boolean hasPreferences(String key) throws PortalDataServiceException;
	/**
	 * 保存参数配置对象
	 * @param preferences
	 * @return
	 */
	public boolean savePreferences(Preferences preferences) throws PortalDataServiceException;
	/**
	 * 删除选择参数对象
	 * @param key
	 * @return
	 */
	public boolean deletePreferences(String key) throws PortalDataServiceException;
	/**
	 * 装载参数配置对象
	 * @param key
	 */
	public Preferences loadPreferences(String key) throws PortalDataServiceException;
	/**
	 * 装载所有数据
	 * @return
	 */
	public List<Preferences> loadPreferences() throws PortalDataServiceException;
	
	/**
	 * 保存参数配置对象(模板)
	 * @param preferences
	 * @return
	 */
	public boolean savePreferencesTpl(Preferences preferences) throws PortalDataServiceException;
	/**
	 * 装载参数配置对象(模板)
	 * @param key
	 */
	public Preferences loadPreferencesTpl(String key) throws PortalDataServiceException;
	/**
	 * 装载所有数据(模板)
	 * @return
	 */
	public List<Preferences> loadPreferencesTpl() throws PortalDataServiceException;
	
	
}
