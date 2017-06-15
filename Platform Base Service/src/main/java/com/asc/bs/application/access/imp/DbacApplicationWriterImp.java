package com.asc.bs.application.access.imp;

import java.util.Date;

import com.asc.commons.application.access.IApplicationWriter;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.entity.ApplicationInstance;
import com.asc.commons.application.entity.Database;
import com.asc.commons.application.entity.Databasessync;
import com.asc.commons.application.entity.DatabasessyncTable;
import com.asc.commons.application.entity.Datasourcemapping;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;

public class DbacApplicationWriterImp implements IApplicationWriter {

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public void saveApplication(Application app) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			getDbAccess().saveObject(app);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			ApplicationException.forSaveObjectFaild(e, app.getId());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void deleteApplication(Application app) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "DELETE FROM " + CommonDatabaseAccess.getTableName(Application.class)  + 
					" WHERE ID =?";
			getDbAccess().execute(sql, new Object[]{app.getId()});
			getDbAccess().deleteObject(app);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			ApplicationException.forDeleteObjectFaild(e, app.getId());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void saveApplicationInstance(ApplicationInstance appIns) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			getDbAccess().saveObject(appIns);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			ApplicationException.forSaveObjectFaild(e, appIns.getId());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void deleteApplicationInstance(ApplicationInstance appIns) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			getDbAccess().deleteObject(appIns);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			ApplicationException.forDeleteObjectFaild(e, appIns.getId());
		} finally {
			getDbAccess().endTransaction();
		}

	}

	@Override
	public void saveDatabase(Database database) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		//判断是否有key
		if(database.getF_key() == "" || "".equals(database.getF_key())){
			throw new ApplicationException("未指定数据库标识。");
		}
		//判断是否有caption
		if(database.getF_caption() == "" || "".equals(database.getF_caption())){
			throw new ApplicationException("未指定数据库名称。");
		}
		
		try {
			getDbAccess().saveObject(database);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("数据库保存失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void deleteDatabase(long id) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(Database.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("数据库删除失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void deleteDatasourcemapping(long id) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(Datasourcemapping.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("数据库实例删除失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void addDatabasesExchange(long innerId, long outId) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			Databasessync dbs = new Databasessync();
			dbs.setF_inner_database_id(innerId);
			dbs.setF_outer_database_id(outId);
			dbs.setF_create_time(new Date());
			getDbAccess().saveObject(dbs);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("添加内外网交换错误！");
		} finally {
			getDbAccess().endTransaction();
		}
		
	}

	@Override
	public void deleteDatabasessync(long id) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(Databasessync.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("内外网交换配置删除失败！", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public DatabasessyncTable saveSyncTable(long syncId, String tablename) throws ApplicationException {
		DatabasessyncTable dbst = ApplicationService.instance().getDatabasetableByName(syncId, tablename);
		if(dbst != null){
			throw new ApplicationException("数据表已存在。");
		}
		dbst = new DatabasessyncTable();
		dbst.setF_database_sync_id(syncId);
		dbst.setF_tablename(tablename);
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			dbst.save();
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("内外网交换数据表保存错误！");
		} finally {
			getDbAccess().endTransaction();
		}
		return dbst;
	}

	@Override
	public void deleteSyncTable(long syncId) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(DatabasessyncTable.class)+
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{syncId});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("内外网交换数据表删除失败！");
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void updateAppState(long appId, int state) throws ApplicationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "update " + CommonDatabaseAccess.getTableName(Application.class) +
					" set f_state = ?  where id = ?";
			getDbAccess().execute(sql, new Object[]{state, appId});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("更新应用系统的运行状态失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}
	}
}
