package com.asc.bs.portalprofile.access.imp;

import java.util.List;

import com.asc.bs.portalprofile.access.IPortalProfileReader;
import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;

public class DbacPortalProfileReader implements IPortalProfileReader {

	@Override
	public PortalProfile getById(long id) throws PortalProfileException {
		try {
			String sql = "SELECT * FROM " + CommonDatabaseAccess.getTableName(PortalProfile.class) 
					+ " WHERE ID = ?";
			return this.getDbAccess().getObject(sql, new Object[]{id}, PortalProfile.class);
		} catch (DbAccessException e) {
			throw PortalProfileException.forGetObjectFaild("id = " + id, e);
		}
	}

	@Override
	public PortalProfile getByKey(String key) throws PortalProfileException {
		try {
			String sql = "SELECT * FROM " + CommonDatabaseAccess.getTableName(PortalProfile.class) 
					+ " WHERE F_KEY = ?";
			return this.getDbAccess().getObject(sql, new Object[]{key}, PortalProfile.class);
		} catch (DbAccessException e) {
			throw PortalProfileException.forGetObjectFaild("F_KEY = " + key, e);
		}
	}

	@Override
	public List<PortalProfile> findAll() throws PortalProfileException {
		try {
			String sql = "SELECT * FROM " + CommonDatabaseAccess.getTableName(PortalProfile.class);
			return this.getDbAccess().listObjects(sql, null, PortalProfile.class, 0, 0); 
		} catch (DbAccessException e) {
			throw PortalProfileException.forFindObjectFaild("无参数", e);
		}
	}
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

}
