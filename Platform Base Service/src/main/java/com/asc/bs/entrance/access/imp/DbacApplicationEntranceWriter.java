package com.asc.bs.entrance.access.imp;

import com.asc.commons.applicationentrance.access.IApplicationEntranceWriter;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;

public class DbacApplicationEntranceWriter implements IApplicationEntranceWriter {
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}
	
	@Override
	public void saveApplicationEntrance(
			ApplicationEntrance applicationEntrance)
			throws ApplicationEntranceException {
		try {
			getDbAccess().saveObject(applicationEntrance);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forSaveObjectFaild(applicationEntrance.getId(), e);
		}

	}

	@Override
	public void deleteApplicationEntrance(ApplicationEntrance applicationEntrance)
			throws ApplicationEntranceException {
		try {
			getDbAccess().deleteObject(applicationEntrance);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forDeleteObjectFaild(applicationEntrance.getId(), e);
		}

	}

}
