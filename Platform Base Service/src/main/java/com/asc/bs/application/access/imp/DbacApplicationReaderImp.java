package com.asc.bs.application.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.List;

import com.asc.commons.application.access.IApplicationReader;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.entity.ApplicationInstance;
import com.asc.commons.application.entity.Database;
import com.asc.commons.application.entity.Databasessync;
import com.asc.commons.application.entity.DatabasessyncTable;
import com.asc.commons.application.entity.Datasource;
import com.asc.commons.application.entity.Datasourcemapping;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.exception.DbAccessException;

/**
 * </pre>
 * Platform Base Service
 * 应用配置数据读取实现类
 * 
 * Mixky Co., Ltd. 2013
 * @author SKS
 * <pre>
 */
public class DbacApplicationReaderImp implements IApplicationReader {
	
	private CommonDatabaseAccess db() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public Application getApplicationByKey(String key) throws ApplicationException {
		Application app = null;
		db().beginTransaction();
		try {
			// 默认查找分组中包含default的应用，不存在则仅查询应用标识符一致的应用
			String sql = "select * from " + getTableName(Application.class) + " where f_key=? and f_group like '%default%'";
			List<Application> apps = db().listObjects(sql, new Object[]{key}, Application.class, 0, 0);
			if (apps.size() > 0) {
				// 存在包含default的应用
				app = apps.get(0);
			} else {
				// 未配置default应用，查询相同标识符应用
				sql = "SELECT * FROM " + getTableName(Application.class) + " WHERE F_KEY=?";
				app = db().getObject(sql, new Object[]{key}, Application.class);
			}
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage() + ",key=" + key, e);
		} finally {
			db().endTransaction();
		}
		return app;
	}

	@Override
	public Application getApplicationById(long id) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * FROM " + getTableName(Application.class) + " WHERE ID=?";
			return db().getObject(sql, new Object[]{id}, Application.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Application> findApplication() throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * FROM " + getTableName(Application.class) + " ORDER BY F_KEY";
			return db().listObjects(sql, null, Application.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public long getApplicationInstanceCount(long applicationId) throws ApplicationException{
		db().beginTransaction();
		try {
			String sql = "SELECT count(*) FROM " + getTableName(ApplicationInstance.class) + " WHERE F_APPLICATION_ID=?";
			return DataValueUtil.getLong(db().getFirstCell(sql, new Object[]{applicationId}), 0L);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage() + ",applicationId=" + applicationId, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public ApplicationInstance getApplicationInstance(String key) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * FROM " + getTableName(ApplicationInstance.class) + " WHERE F_KEY=?";
			return db().getObject(sql, new Object[]{key}, ApplicationInstance.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage() + ",f_key = " + key, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public ApplicationInstance getApplicationInstance(long id) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + getTableName(ApplicationInstance.class) + " " + 
					"WHERE ID = ?";
			return db().getObject(sql, new Object[]{id}, ApplicationInstance.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage() + ",id=" + id, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<ApplicationInstance> findApplicationInstance(long applicationId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + getTableName(ApplicationInstance.class) + " " + 
					"WHERE F_APPLICATION_ID = ? " +
					"ORDER BY F_SERIALNUMBER";
			return db().listObjects(sql, new Object[]{applicationId}, ApplicationInstance.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage() + ",applicationId=" + applicationId, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Database getDatabaseById(long id) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Database.class) +
					" where id = ?";
			return db().getObject(sql, new Object[]{id}, Database.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Database> getDatabaseList(String location) throws ApplicationException {
		db().beginTransaction();
		try {
			List<Database> dbList = null;
			String sql = "select * from " + getTableName(Database.class);
			if ("".equals(location)) {
				dbList = db().listObjects(sql, new Object[]{}, Database.class, 0, 0);
			} else {
				sql = sql + " where f_location = ?";
				dbList = db().listObjects(sql, new Object[]{location}, Database.class, 0, 0);
			}
			return dbList;
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Datasource getDatasourceByKey(String key) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Datasource.class) +
					" where f_key = ?";
			return db().getObject(sql, new Object[]{key}, Datasource.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Datasourcemapping getDatasourcemapping(long dsId, long dbId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Datasourcemapping.class) +
					" where f_database_id = ? and f_datasource_id = ?";
			return db().getObject(sql, new Object[]{dbId, dsId}, Datasourcemapping.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Datasourcemapping> findDsmList(String key) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select a.* from t_asc_datasource_mapping a inner join t_asc_datasource b on a.f_datasource_id = b.id" +
					" where b.f_key = ?";
			return db().listObjects(sql, new Object[]{key}, Datasourcemapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Datasource getDatasourceById(long dsId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Datasource.class) +
					" where id = ?";
			return db().getObject(sql, new Object[]{dsId}, Datasource.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Databasessync getDatabasessyncById(long dbsId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Databasessync.class) +
					" where id = ?";
			return db().getObject(sql, new Object[]{dbsId}, Databasessync.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Datasourcemapping getDatasourcemappingById(long dsmId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_datasource_mapping " +
					" where id = ?";
			return db().getObject(sql, new Object[]{dsmId}, Datasourcemapping.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<DatabasessyncTable> loadDataSyncTable(long syncId) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(DatabasessyncTable.class) +
					" where f_database_sync_id = ?";
			return db().listObjects(sql, new Object[]{syncId}, DatabasessyncTable.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public DatabasessyncTable getDatabasetableByName(long syncId, String tablename) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(DatabasessyncTable.class) +
					" where f_database_sync_id = ? and f_tablename = ?";
			return db().getObject(sql, new Object[]{syncId, tablename}, DatabasessyncTable.class);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Datasource> getDatasourceList(long id) throws ApplicationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Datasource.class) +
					" where id = ? order by f_caption";
			return db().listObjects(sql, new Object[]{id}, Datasource.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationException.forDbAccessException(e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}
	
}
