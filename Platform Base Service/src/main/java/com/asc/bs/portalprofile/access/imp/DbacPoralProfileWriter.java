package com.asc.bs.portalprofile.access.imp;

import com.asc.bs.portalprofile.access.IPortalProfileWriter;
import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;

public class DbacPoralProfileWriter implements IPortalProfileWriter {
	
	@Override
	public void save(PortalProfile portalProfile) throws PortalProfileException {
		try {
			this.getDbAccess().saveObject(portalProfile);
		} catch (DbAccessException e) {
			throw PortalProfileException.forSaveObjectFaild(portalProfile.getId(), e);
		}
	}

	@Override
	public void delete(long id) throws PortalProfileException {
		try {
			PortalProfile p = new PortalProfile();
			p.setId(id);
			this.getDbAccess().deleteObject(p);
		} catch (DbAccessException e) {
			throw PortalProfileException.forDeleteObjectFaild(id, e);
		}
	}
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public IDbacTransaction beginTransaction() {
		return this.getDbAccess().beginTransaction();
	}

	@Override
	public void endTransaction() {
		this.getDbAccess().endTransaction();
	}

}
