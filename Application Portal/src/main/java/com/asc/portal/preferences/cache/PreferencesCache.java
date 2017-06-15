package com.asc.portal.preferences.cache;

import java.util.HashMap;
import java.util.Map;

import com.asc.portal.preferences.dao.Preferences;

public class PreferencesCache {
	// 参数对象缓存
	private Map<String, Preferences> preferencesList;
	
	public PreferencesCache(){
		preferencesList = new HashMap<String, Preferences>();
	}
	/**
	 * 添加选择参数对象
	 * @param preferences
	 */
	public void add(Preferences preferences){
		preferencesList.put(preferences.getKey(), preferences);
	}
	/**
	 * 删除选择参数对象
	 * @param key
	 */
	public void del(String key){
		preferencesList.remove(key);
	}
	/**
	 * 获得选择参数对象
	 * @param key
	 * @return
	 */
	public Preferences get(String key){
		if(preferencesList.containsKey(key)){
			return preferencesList.get(key);
		}else{
			return new Preferences();
		}
	}
	/**
	 * 判断选择参数对象是否存在
	 * @param key
	 * @return
	 */
	public boolean has(String key){
		if(preferencesList.containsKey(key)){
			return true;
		}else{
			return false;
		}
	}
}
