package com.asc.bs.portalprofile.access;

import java.util.List;

import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;

public interface IPortalProfileReader {
	
	/**
	 * 根据ID获取PortalProfile对象
	 * @param id
	 * @return
	 */
	public PortalProfile getById(long id) throws PortalProfileException;
	
	/**
	 * 根据key获取PortalProfile对象
	 * @param key
	 * @return
	 */
	public PortalProfile getByKey(String key) throws PortalProfileException;
	
	/**
	 * 获取所有的PortalProfile列表
	 * @return
	 */
	public List<PortalProfile> findAll() throws PortalProfileException;
}
