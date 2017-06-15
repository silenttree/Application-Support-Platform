package com.asc.bs.portalprofile.entity;

import com.asc.commons.dbac.DbAccessSupport;
import com.asc.commons.dbac.annotation.Entity;


@Entity(tableName="t_asc_portal_profile", isCache=true)
public class PortalProfile extends DbAccessSupport{

	private static final long serialVersionUID = -709970916061773199L;
	private String f_key;
	private String f_caption;
	private String f_configuration;
	private int f_order;
	private String f_scope_expression;
	private String f_scope_display;
	
	public String getF_key() {
		return f_key;
	}
	public void setF_key(String f_key) {
		this.f_key = f_key;
	}
	public String getF_caption() {
		return f_caption;
	}
	public void setF_caption(String f_caption) {
		this.f_caption = f_caption;
	}
	public String getF_configuration() {
		return f_configuration;
	}
	public void setF_configuration(String f_configuration) {
		this.f_configuration = f_configuration;
	}
	public int getF_order() {
		return f_order;
	}
	public void setF_order(int f_order) {
		this.f_order = f_order;
	}
	public String getF_scope_expression() {
		return f_scope_expression;
	}
	public void setF_scope_expression(String f_scope_expression) {
		this.f_scope_expression = f_scope_expression;
	}
	public String getF_scope_display() {
		return f_scope_display;
	}
	public void setF_scope_display(String f_scope_display) {
		this.f_scope_display = f_scope_display;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "PortalProfile [f_key=" + f_key + ", f_caption=" + f_caption
				+ ", f_configuration=" + f_configuration + ", f_order="
				+ f_order + ", f_scope_expression=" + f_scope_expression
				+ ", f_scope_display=" + f_scope_display + "]";
	}

}
