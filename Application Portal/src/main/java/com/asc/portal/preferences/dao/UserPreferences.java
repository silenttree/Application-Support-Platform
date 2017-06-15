package com.asc.portal.preferences.dao;

import com.asc.commons.dbac.DbAccessSupport;
import com.asc.commons.dbac.annotation.Entity;

@Entity(tableName="t_asc_user_preference",isCache=false)
public class UserPreferences extends DbAccessSupport {

	private static final long serialVersionUID = 1L;
		
	private long f_user_id;
	private String f_user_name;
	private String f_configuration;
	
	public long getF_user_id() {
		return f_user_id;
	}
	public void setF_user_id(long f_user_id) {
		this.f_user_id = f_user_id;
	}
	public String getF_user_name() {
		return f_user_name;
	}
	public void setF_user_name(String f_user_name) {
		this.f_user_name = f_user_name;
	}
	public String getF_configuration() {
		return f_configuration;
	}
	public void setF_configuration(String f_configuration) {
		this.f_configuration = f_configuration;
	}
	
	@Override
	public String toString() {
		return "UserPreferences [f_user_id=" + f_user_id + ", f_user_name="
				+ f_user_name + ", f_configuration=" + f_configuration
				+ "]";
	}
	
}
