package com.asc.portal.preferences.serialize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.asc.commons.context.ContextHolder;
import com.asc.portal.preferences.dao.Preferences;
import com.google.gson.JsonObject;
import com.mixky.toolkit.FileTool;
import com.mixky.toolkit.JsonObjectTool;

public abstract class PreferencesFileSerialize implements IPreferencesSerialize{
	
	private String path = "resources/preferences";

	@Override
	public boolean hasPreferences(String key) {
		String filepath = ContextHolder.instance().getRealPath(path) + "/" + key + ".json";
		File file = new File(filepath);
		return file.exists();
	}

	@Override
	public boolean savePreferences(Preferences preferences) {
		String filepath = ContextHolder.instance().getRealPath(path) + "/" + preferences.getKey() + ".json";
		JsonObjectTool.writeJsonToFile(filepath, preferences.toJsonObject());
		return true;
	}

	@Override
	public boolean deletePreferences(String key) {
		String filepath = ContextHolder.instance().getRealPath(path) + "/" + key + ".json";
		File file = new File(filepath);
		return file.delete();
	}

	@Override
	public Preferences loadPreferences(String key) {
		String filepath = ContextHolder.instance().getRealPath(path) + "/" + key + ".json";
		JsonObject json = JsonObjectTool.readJsonFormFile(filepath);
		Preferences preferences = new Preferences();
		preferences.fromJson(json);
		return preferences;
	}

	@Override
	public List<Preferences> loadPreferences() {
		List<Preferences> preferencesList = new ArrayList<Preferences>();
		String folderpath = ContextHolder.instance().getRealPath(path);
		File filefolder = new File(folderpath);
		if(!filefolder.exists()){
			FileTool.createDirectory(folderpath);
			filefolder = new File(folderpath);
		}
		File[] fileList = filefolder.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if(fileList[i].isDirectory()){
				continue;
			}
			String key = fileList[i].getName().replaceAll(".json", "");
			Preferences preferences= loadPreferences(key);
			preferencesList.add(preferences);
		}
		return preferencesList;
	}



}
