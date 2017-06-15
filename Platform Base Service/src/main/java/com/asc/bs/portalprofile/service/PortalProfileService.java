package com.asc.bs.portalprofile.service;

import java.util.List;

import com.asc.bs.portalprofile.access.IPortalProfileReader;
import com.asc.bs.portalprofile.access.imp.DbacPortalProfileReader;
import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.commons.dbac.CommonDatabaseAccess;

public class PortalProfileService {

	private IPortalProfileReader reader;
	private static PortalProfileService singleton;

	private PortalProfileService() {
		reader = new DbacPortalProfileReader();
	}

	public static PortalProfileService instance() {
		if (singleton == null) {
			singleton = new PortalProfileService();
		}
		return singleton;
	}
	
	/**
	 * 根据ID获取门户模板
	 * @param id
	 * @return
	 * @throws PortalProfileException
	 */
	public PortalProfile getById(long id) throws PortalProfileException {
		CommonDatabaseAccess.instance().beginTransaction();
		try{			
			return reader.getById(id);
		} catch (Exception e){
			throw PortalProfileException.forGetObjectFaild(String.valueOf(id), e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}
	
	/**
	 * 根据key获取门户模板
	 * @param id
	 * @return
	 * @throws PortalProfileException
	 */
	public PortalProfile getByKey(String key) throws PortalProfileException{
		CommonDatabaseAccess.instance().beginTransaction();
		try{			
			return reader.getByKey(key);
		} catch (Exception e){
			throw PortalProfileException.forGetObjectFaild(key, e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}
	
	/**
	 * 获取所有的门户模板
	 * @return
	 * @throws PortalProfileException
	 */
	public List<PortalProfile> findAll() throws PortalProfileException{
		CommonDatabaseAccess.instance().beginTransaction();
		try{			
			return reader.findAll();
		} catch (Exception e){
			throw PortalProfileException.forGetObjectFaild(null, e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	} 

}
