package com.asc.bs.portalprofile.access;

import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.commons.dbac.IDbacTransaction;

public interface IPortalProfileWriter {

	/**
	 * 保存门户模板
	 * @param portalProfile
	 * @throws PortalProfileException
	 */
	public void save(PortalProfile portalProfile) throws PortalProfileException;
	
	/**
	 * 删除门户模板
	 * @param id
	 * @throws PortalProfileException
	 */
	public void delete(long id) throws PortalProfileException;

	/**
	 * 开启事务
	 * @return 返回事务的引用
	 */
	public IDbacTransaction beginTransaction();
	
	/**
	 * 关闭事务
	 */
	public void endTransaction();
}
