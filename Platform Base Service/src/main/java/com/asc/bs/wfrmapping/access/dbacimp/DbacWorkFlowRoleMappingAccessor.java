package com.asc.bs.wfrmapping.access.dbacimp;

import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.wfrolemapping.access.IWorkFlowRoleMappingReader;
import com.asc.commons.wfrolemapping.access.IWorkFlowRoleMappingWriter;
import com.asc.commons.wfrolemapping.entity.WorkFlowRoleMapping;
import com.asc.commons.wfrolemapping.exception.WorkFlowRoleMappingException;
import com.mixky.toolkit.MapTool;

public class DbacWorkFlowRoleMappingAccessor implements IWorkFlowRoleMappingReader, IWorkFlowRoleMappingWriter {

	@Override
	public void save(WorkFlowRoleMapping mfrm) throws WorkFlowRoleMappingException {
		try {
			Map<String, Object> map = MapTool.object2Map(mfrm);
			if (mfrm.getId() < 1) {
				map.put("F_CREATE_TIME", new Date());
			} else {
				map.remove("F_CREATE_TIME");
				map.put("F_UPDATE_TIME", new Date());
			}
			getDbAccess().saveEntries(map, getTableName(WorkFlowRoleMapping.class));
		} catch (DbAccessException e) {
			throw WorkFlowRoleMappingException.forSaveObjectFaild(mfrm.getId(), e);
		}
	}

	@Override
	public void delete(long id) throws WorkFlowRoleMappingException {
		try {
			String sql = "DELETE " + "FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " " + "WHERE ID = ?";
			getDbAccess().execute(sql, new Object[] { id });
		} catch (DbAccessException e) {
			throw WorkFlowRoleMappingException.forGetObjectFaild("id = " + id, e);
		}

	}

	@Override
	public WorkFlowRoleMapping get(long id) throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " + " FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE ID=?";
			return getDbAccess().getObject(sql, new Object[] { id },
					WorkFlowRoleMapping.class);
		} catch (DbAccessException e) {
			throw WorkFlowRoleMappingException.forGetObjectFaild("id = " + id, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<WorkFlowRoleMapping> findAll() throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class);
			return getDbAccess().listObjects(sql, null,
					WorkFlowRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException("查询流程角色列表失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public List<WorkFlowRoleMapping> findByAppAndKey(long appId, String roleKey)
			throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE F_APPLICATION_ID=? AND F_WROLE_KEY=?";

			return getDbAccess().listObjects(sql,
					new Object[] { appId, roleKey }, WorkFlowRoleMapping.class,
					0, 0);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException(
					"根据应用ID和流程角色KEY查询流程角色映射列表失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public WorkFlowRoleMapping getByOrgAndAppAndKey(long appId, long orgId,
			String roleKey) throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ? AND F_WROLE_KEY = ?";
			return getDbAccess().getObject(sql,
					new Object[] { appId, orgId, roleKey },
					WorkFlowRoleMapping.class);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException("根据机构ID：[" + orgId
					+ "] ,应用ID: [" + appId + "] 和流程角色key: [" + roleKey
					+ "] 查询流程角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<WorkFlowRoleMapping> findByExps(long appId, String exps)
			throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ?  AND ASC_AUTH(F_AUTH_EXPRESSION, '"
					+ exps + "' ) >= 0";
			return getDbAccess().listObjects(sql, new Object[] { appId },
					WorkFlowRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException("根据应用ID: [" + appId + "] 和权限表达式：[" + exps + "] 查询流程角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<WorkFlowRoleMapping> findByCompany(long appId, long companyId)
			throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ? ";
			return getDbAccess().listObjects(sql,
					new Object[] { appId, companyId },
					WorkFlowRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException("根据应用ID: [" + appId + "] 和单位ID：[" + companyId + "] 查询流程角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<WorkFlowRoleMapping> findByExpsAndCompany(long appId,
			String exps, long companyId) throws WorkFlowRoleMappingException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * FROM "
					+ CommonDatabaseAccess.getTableName(WorkFlowRoleMapping.class)
					+ " WHERE F_APPLICATION_ID = ? AND F_ORG_ID = ? AND ASC_AUTH(F_AUTH_EXPRESSION, '"
					+ exps + "' ) >= 0";
			return getDbAccess().listObjects(sql,
					new Object[] { appId, companyId },
					WorkFlowRoleMapping.class, 0, 0);
		} catch (DbAccessException e) {
			throw new WorkFlowRoleMappingException("根据应用ID: [" + appId
					+ "] ,单位ID：[" + companyId + "] 和权限表达式: [" + exps + "] 查询流程角色失败", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

}
