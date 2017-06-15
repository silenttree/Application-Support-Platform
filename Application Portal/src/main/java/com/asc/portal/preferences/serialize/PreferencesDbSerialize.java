package com.asc.portal.preferences.serialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.portalprofile.entity.PortalProfile;
import com.asc.commons.portalprofile.exception.PortalProfileException;
import com.asc.commons.portalprofile.service.PortalProfileService;
import com.asc.portal.exception.PortalDataServiceException;
import com.asc.portal.preferences.dao.Preferences;
import com.asc.portal.preferences.dao.UserPreferences;
import com.mixky.toolkit.JsonObjectTool;

public class PreferencesDbSerialize implements IPreferencesSerialize {

	@Override
	public boolean hasPreferences(String key) throws PortalDataServiceException {
		getDbac().beginTransaction();
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ CommonDatabaseAccess.getTableName(UserPreferences.class)
					+ " WHERE F_USER_NAME = ?";
			long count = DataValueUtil.getLong(getDbac().getFirstCell(sql, new Object[] {key}), 0l);
			return count > 0;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找用户的配置是否存在发生错误", e);
		} finally {
			getDbac().endTransaction();
		}
	}

	@Override
	public boolean savePreferences(Preferences preferences)
			throws PortalDataServiceException {
		String key = preferences.getKey();
		UserPreferences up = getUserPreferences(key);
		if (up == null) {
			up = new UserPreferences();
			up.setF_user_name(key);
			up.setF_create_time(new Date());
		}
		up.setF_configuration(preferences.toJsonObject().toString());
		up.setF_update_time(new Date());
		IDbacTransaction tx = getDbac().beginTransaction();
		try {
			getDbac().saveObject(up,
					CommonDatabaseAccess.getTableName(UserPreferences.class));
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new PortalDataServiceException("保存用户配置发生错误", e);
		} finally {
			getDbac().endTransaction();
		}
		return true;
	}

	@Override
	public boolean deletePreferences(String key)
			throws PortalDataServiceException {
		IDbacTransaction tx = CommonDatabaseAccess.instance()
				.beginTransaction();
		try {
			String sql = "DELETE FROM "
					+ CommonDatabaseAccess.getTableName(UserPreferences.class)
					+ " WHERE F_USER_NAME = ?";
			getDbac().execute(sql,
					new Object[] { key });
			tx.commit();
			return true;
		} catch (DbAccessException e) {
			tx.rollback();
			throw new PortalDataServiceException("删除用户配置错误", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}

	}

	@Override
	public Preferences loadPreferences(String key)
			throws PortalDataServiceException {
		UserPreferences up = getUserPreferences(key);
		try {
			Preferences p;
			if (up == null) {
				return null;
			} else {
				p = new Preferences();
				p.fromJson(JsonObjectTool.string2JsonObject(up
						.getF_configuration()));
			}
			return p;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找用户的配置错误", e);
		}
	}

	@Override
	public List<Preferences> loadPreferences()
			throws PortalDataServiceException {
		getDbac().beginTransaction();
		try {
			List<Preferences> ps = new ArrayList<Preferences>();
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(UserPreferences.class);
			List<UserPreferences> ups = getDbac().listObjects(sql,
					new Object[] {},
					UserPreferences.class, 0, 0);
			for (UserPreferences up : ups) {
				Preferences p = new Preferences();
				p.fromJson(JsonObjectTool.string2JsonObject(up
						.getF_configuration()));
				ps.add(p);
			}
			return ps;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找用户的配置错误", e);
		} finally {
			getDbac().endTransaction();
		}
	}

	private CommonDatabaseAccess getDbac() {
		return CommonDatabaseAccess.instance();
	}


	@Override
	public boolean savePreferencesTpl(Preferences preferences) throws PortalDataServiceException {
		String key = preferences.getKey();
		PortalProfile pp = null;
		try {
			pp = getPreferencesTpl(key);
		} catch (PortalProfileException e1) {
			throw new PortalDataServiceException("根据KEY: [ " + key + "] 查找模板时发生错误", e1);
		}
		if (pp == null) {
			throw new PortalDataServiceException("根据KEY: [ " + key + "] 未能找到模板" );
		}
		pp.setF_configuration(preferences.toJsonObject().toString());
		pp.setF_update_time(new Date());
		
		try {
			throw new UnsupportedOperationException();
//			PortalProfileManager.instance().save(pp);
		} catch (Exception e) {
			throw new PortalDataServiceException("保存门户模板失败", e);
		}
	}

	@Override
	public Preferences loadPreferencesTpl(String key)
			throws PortalDataServiceException {
		try {
			PortalProfile pp = getPreferencesTpl(key);
			Preferences p = null;
			if (pp == null) {
				return null;
			} else {
				p = new Preferences();
				p.fromJson(JsonObjectTool.string2JsonObject(pp
						.getF_configuration()));
			}
			return p;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找配置模板错误", e);
		}
	}

	@Override
	public List<Preferences> loadPreferencesTpl()
			throws PortalDataServiceException {
		try {
			List<Preferences> ps = new ArrayList<Preferences>();
			List<PortalProfile> pps = PortalProfileService.instance().findAll();
			for (PortalProfile pp : pps) {
				Preferences p = new Preferences();
				p.fromJson(JsonObjectTool.string2JsonObject(pp
						.getF_configuration()));
				ps.add(p);
			}
			return ps;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找用户的配置错误", e);
		}
	}

	private UserPreferences getUserPreferences(String key)
			throws PortalDataServiceException {
		getDbac().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(UserPreferences.class)
					+ " WHERE F_USER_NAME = ?";
			UserPreferences up = getDbac().getObject(sql,
					new Object[] { key },
					UserPreferences.class);
			return up;
		} catch (Exception e) {
			throw new PortalDataServiceException("查找用户的配置错误", e);
		} finally {
			getDbac().endTransaction();
		}
	}

	private PortalProfile getPreferencesTpl(String key) throws PortalProfileException {
		return PortalProfileService.instance().getByKey(key);
	}

}
