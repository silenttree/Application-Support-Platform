package com.asc.bs.organization.access;


/**
 * Application Framework/OrganizationManagerEvent.java<br>
 * 组织机构信息维护监听事件
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public interface IOrganizationWriterListener {

	public void addUser2Org(long userId, long orgId);
	
	public void addUser2Role(long userId, long roleId);
	
	public void deleteUser(long userId);
	
	public void deleteRole(long roleId);
	
	public void deleteOrg(long orgId);
	
}
