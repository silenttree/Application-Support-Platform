package com.asc.portal.preferences;

import java.util.List;

import com.asc.commons.context.ContextHolder;
import com.asc.commons.organization.entity.User;
import com.asc.portal.exception.PortalDataServiceException;
import com.asc.portal.preferences.cache.PreferencesCache;
import com.asc.portal.preferences.dao.Preferences;
import com.asc.portal.preferences.serialize.IPreferencesSerialize;

public class PreferencesManager {
	
	private static PreferencesManager singleton;
	// 默认一次装载所有数据
	private boolean autoLoad = false;
	// 持久化对象
	private IPreferencesSerialize serializer;
	// 数据缓存对象
	private static PreferencesCache cache;
	/**
	 * 构造函数
	 * @throws PortalDataServiceException 
	 */
	public PreferencesManager() throws PortalDataServiceException{
		cache = new PreferencesCache();
		// 处理自动装载
		if(autoLoad){
			List<Preferences> list = null;
			list = serializer.loadPreferences();
			if(list != null){
				for(int i=0;i<list.size();i++){
					cache.add(list.get(i));
				}
			}
		}
	}
	/**
	 * 单件实现
	 * @return
	 */
	public static PreferencesManager instance() throws PortalDataServiceException{
		if (singleton == null) {
			try {
				singleton = ContextHolder.instance().getBean("PreferencesManager");
			} catch (Exception e) {
				singleton = new PreferencesManager();
			}
		}
		return singleton;
	}
	public boolean isAutoLoad() {
		return autoLoad;
	}
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}
	public IPreferencesSerialize getSerializer() {
		return serializer;
	}
	public void setSerializer(IPreferencesSerialize serializer) {
		this.serializer = serializer;
	}
	/**
	 * 获得用户参数选择
	 * @param user
	 * @return
	 * @throws PortalDataServiceException 
	 */
	public Preferences getPreferences(User user) throws PortalDataServiceException {
		String key = user.getF_name();
		if(cache.has(key) || serializer.hasPreferences(key)){
			// 获得用户自定义参数选择
			return getPreferences(key);
		}else{
			key = getUserDefaultPreferencesKey(user);
			return getPreferences(key);
		}
	}
	/**
	 * 根据KEY获得参数选择对象
	 * @param key
	 * @return
	 * @throws PortalDataServiceException 
	 */
	public Preferences getPreferences(String key) throws PortalDataServiceException{
		Preferences preferences = null;
		if(cache.has(key)){
			// 从缓存中装载对象
			preferences = cache.get(key);
		}else if(serializer.hasPreferences(key)){
			// 从持久化目标中装载对象
			preferences = serializer.loadPreferences(key);
			cache.add(preferences);
		}else{
			// 创建新对象
			preferences = new Preferences();
		}
		return preferences;
	}
	
	/**
	 * @param key 根据KEY值获取配置模板
	 * @return
	 * @throws PortalDataServiceException
	 */
	public Preferences getPreferencesTpl(String key) throws PortalDataServiceException{
		Preferences preferences = null;
		if(cache.has(key)){
			// 从缓存中装载对象
			preferences = cache.get(key);
		}else if(serializer.hasPreferences(key)){
			// 从持久化目标中装载对象
			preferences = serializer.loadPreferencesTpl(key);
			cache.add(preferences);
		}else{
			// 创建新对象
			preferences = new Preferences();
		}
		return preferences;
	}
	
	/**
	 * 获得用户默认匹配的选择参数
	 * @param user
	 * @return
	 */
	public String getUserDefaultPreferencesKey(User user){
		// TODO 获得用户默认匹配的选择参数
		return "(default)";
	}
	/**
	 * 保存选择参数
	 * @param preferences
	 * @return
	 * @throws PortalDataServiceException 
	 */
	public boolean savePreferences(Preferences preferences) throws PortalDataServiceException{
		// 持久化
		boolean result = serializer.savePreferences(preferences);
		if(result){
			// 添加到缓存 
			cache.add(preferences);
		}
		return result;
	}
	

	public boolean savePreferencesTpl(Preferences preferences) throws PortalDataServiceException{
		// 持久化
		boolean result = serializer.savePreferencesTpl(preferences);
		if(result){
			// 添加到缓存 
			cache.add(preferences);
		}
		return result;
	}
	
	/**
	 * 删除选择参数
	 * @param key
	 * @throws PortalDataServiceException 
	 */
	public boolean deletePreferences(String key) throws PortalDataServiceException{
		// 持久化
		boolean result = serializer.deletePreferences(key);
		if(result){
			// 从缓存中删除
			cache.del(key);
		}
		return result;
	}
	
	public List<Preferences> findAllPreferencesTpl() throws PortalDataServiceException{
		return serializer.loadPreferencesTpl();
	}
	
}
