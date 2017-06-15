package com.asc.demo.entity;

import java.util.Date;

import com.asc.commons.dbac.DbAccessSupport;
import com.asc.commons.dbac.annotation.Entity;

@Entity(tableName = "t_app_demo")
public class AppDemo extends DbAccessSupport {
	private static final long serialVersionUID = -4983111047735000998L;
	private String f_type;
	private String f_title;
	private String f_name;
	private Date f_issue_date;
	private String f_content;
	private Date f_create_time;
	private String f_state;
	private long f_flowlog_id;

	public String getF_type() {
		return f_type;
	}

	public void setF_type(String f_type) {
		this.f_type = f_type;
	}

	public String getF_title() {
		return f_title;
	}

	public void setF_title(String f_title) {
		this.f_title = f_title;
	}

	public String getF_name() {
		return f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public Date getF_issue_date() {
		return f_issue_date;
	}

	public void setF_issue_date(Date f_issue_date) {
		this.f_issue_date = f_issue_date;
	}

	public String getF_content() {
		return f_content;
	}

	public void setF_content(String f_content) {
		this.f_content = f_content;
	}

	public Date getF_create_time() {
		return f_create_time;
	}

	public void setF_create_time(Date f_create_time) {
		this.f_create_time = f_create_time;
	}

	public String getF_state() {
		return f_state;
	}

	public void setF_state(String f_state) {
		this.f_state = f_state;
	}

	public long getF_flowlog_id() {
		return f_flowlog_id;
	}

	public void setF_flowlog_id(long f_flowlog_id) {
		this.f_flowlog_id = f_flowlog_id;
	}

}
