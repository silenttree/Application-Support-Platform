package com.asc.bs.mrmapping.access.dbacimp;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.modulerolemapping.access.IModuleRoleMappingReader;
import com.asc.commons.modulerolemapping.access.IModuleRoleMappingWriter;
import com.asc.commons.modulerolemapping.entity.ModuleRoleMapping;
import com.asc.commons.modulerolemapping.exception.ModuleRoleMappingException;

public class DbacModuleRoleMappingAccessor implements IModuleRoleMappingReader, IModuleRoleMappingWriter {

	@Override
	public void save(ModuleRoleMapping moduleRoleMapping) throws ModuleRoleMappingException {
		try {
			getDbAccess().saveObject(moduleRoleMapping);
		} catch (DbAccessException e) {
			throw ModuleRoleMappingException.forSaveObjectFaild(moduleRoleMapping.getId(), e); 
		}
	}

	@Override
	public void delete(long id) throws ModuleRoleMappingException {
		try {
			String sql = "DELETE " +
					"FROM " + CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) + " " + 
					"WHERE ID = ?";
			getDbAccess().execute(sql, new Object[]{id}); 
		} catch (DbAccessException e) {
			throw ModuleRoleMappingException.forGetObjectFaild("id = " + id, e);
		} 

	}

	@Override
	public ModuleRoleMapping getById(long id) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " +
					" FROM " + CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) + 
					" WHERE ID=?";
			return getDbAccess().getObject(sql, new Object[]{id}, ModuleRoleMapping.class);
		} catch (DbAccessException e) {
			throw ModuleRoleMappingException.forGetObjectFaild("id = " + id, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	
	@Override
	public List<ModuleRoleMapping> findByAppAndKey(long appId, String roleKey) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " +
					" FROM " + CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) + 
					" WHERE F_APPLICATION_ID = ? AND F_MROLE_KEY = ?"; 
			return getDbAccess().listObjects(sql, new Object[]{appId, roleKey},ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("根据模块角色的KEY查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public ModuleRoleMapping getByOrgAndAppAndKey(long appId, long orgId, String roleKey)
			throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ? AND F_MROLE_KEY = ?";
			return getDbAccess().getObject(sql, new Object[]{appId, orgId, roleKey}, ModuleRoleMapping.class);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("根据机构ID：[" + orgId + "] ,应用ID: [" + appId + "] 和模块角色key: ["+ roleKey +"] 查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	
	@Override
	public List<ModuleRoleMapping> findByOrgAndApp(long appId, long orgId) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ?";
			return getDbAccess().listObjects(sql, new Object[]{appId, orgId}, ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("根据机构ID：[" + orgId + "] 和应用ID: [" + appId + "] 查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ModuleRoleMapping> findAll() throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class);
			return getDbAccess().listObjects(sql, null, ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ModuleRoleMapping> findByExps(long appId, String exps) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) 
					+ " WHERE F_APPLICATION_ID = ? AND ASC_AUTH(F_AUTH_EXPRESSION, '" + exps + "' ) >= 0";
			return getDbAccess().listObjects(sql, new Object[]{appId}, ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	
	@Override
	public List<ModuleRoleMapping> findByCompany(long appId, long companyId) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) 
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ?";
			return getDbAccess().listObjects(sql, new Object[]{appId, companyId}, ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ModuleRoleMapping> findByExpsAndCompany(long appId, String exps, long companyId) throws ModuleRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(ModuleRoleMapping.class) 
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ? AND ASC_AUTH(F_AUTH_EXPRESSION, '" + exps + "' ) >= 0";
			return getDbAccess().listObjects(sql, new Object[]{appId, companyId}, ModuleRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ModuleRoleMappingException("查询模块角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	private CommonDatabaseAccess getDbAccess(){
		return CommonDatabaseAccess.instance();
	}

}
