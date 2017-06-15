package com.asc.bs.organization.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.ArrayList;
import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.organization.access.IOrganizationReader;
import com.asc.commons.organization.custom.IRoleParser;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.OrgAttribute;
import com.asc.commons.organization.entity.OrgLog;
import com.asc.commons.organization.entity.OrgUser;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.Role.RoleTypes;
import com.asc.commons.organization.entity.RoleUser;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.mixky.toolkit.ListTool;
import com.mixky.toolkit.ReflectTool;

public class OrganizationReaderImp implements IOrganizationReader {
	
	private CommonDatabaseAccess getDbAccess() {
		return db();
	}
	
	@Override
	public User getUserByName(String name) throws OrganizationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(User.class) + " where f_name=?";
			return getDbAccess().getObject(sql, new Object[]{name}, User.class);
		} catch (DbAccessException e) {
			OrganizationException.forUserName(name);
		} finally {
			getDbAccess().endTransaction();
		}
		return null;
	}

	@Override
	public User getUserById(long userId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(User.class) + 
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{userId}, User.class);
		} catch (DbAccessException e) {
			OrganizationException.forUser(userId);
		} finally {
			db().endTransaction();
		}
		return null;
	}

	@Override
	public Org getOrgById(long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) + 
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{orgId}, Org.class);
		} catch (DbAccessException e) {
			OrganizationException.forOrgId(orgId);
		} finally {
			db().endTransaction();
		}
		return null;
	}


	@Override
	public List<Org> findOrgsByParent(long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) + 
					" where f_parent_id = ? order by f_order";
			return getDbAccess().listObjects(sql, new Object[]{orgId}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取机构列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<User> findOrgUsers(long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "";
			if(orgId == 0){
				sql = "select * from " + CommonDatabaseAccess.getTableName(User.class) +
						" where f_company_id is null or f_company_id = 0 " +
						" order by f_caption";
				return getDbAccess().listObjects(sql, new Object[]{}, User.class, 0, 0);
			}else{
				sql = "select a.* from t_asc_user a inner join t_asc_org_user b on a.id = b.f_user_id " +
					"where b.f_org_id = ? and (a.f_company_id = ? or a.f_dept_id = ?) " +
					"and a.f_state <> "+ User.States.Deleted.intValue() + 
					" order by b.f_order";
				return getDbAccess().listObjects(sql, new Object[]{orgId, orgId, orgId}, User.class, 0, 0);
			}
		} catch (DbAccessException e) {
			throw new OrganizationException("获取机构用户列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Role getRoleById(long roleId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Role.class) + 
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{roleId}, Role.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取角色失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findCompanies(long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + CommonDatabaseAccess.getTableName(Org.class) + " where f_parent_id = ?" + 
					" and f_type = 0 ORDER BY f_no";
			return getDbAccess().listObjects(sql, new Object[]{orgId}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取机构列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	/**
	 * 获取用户所在单位列表
	 */
	@Override
	public List<Org> findUserCompanies(long userId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select a.* from t_asc_org a inner join t_asc_org_user b on a.id = b.f_org_id where b.f_user_id = ? and a.f_type = 0";
			return getDbAccess().listObjects(sql, new Object[]{userId}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取用户所在单位列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findUserOrgs(long userId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select a.* from t_asc_org a inner join t_asc_org_user b on a.id = b.f_org_id where b.f_user_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{userId}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取用户所在机构列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Role> findCompanyRoles(long companyId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Role.class) + " where f_org_id = ? order by f_caption";
			return getDbAccess().listObjects(sql, new Object[]{companyId}, Role.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取机构角色列表失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Role> findUserRoles(long userId) throws OrganizationException {
		List<Role> roles = ListTool.newArrayList();
		db().beginTransaction();
		try {
			// 查询常规角色
			String sql = "select a.* from t_asc_role a inner join t_asc_role_user b on a.id = b.f_role_id where b.f_user_id = ?";
			roles = getDbAccess().listObjects(sql, new Object[]{userId}, Role.class, 0, 0);
			// 查询自定义角色
			sql = "select * from t_asc_role where f_type=?";
			List<Role> croles = getDbAccess().listObjects(sql, new Object[]{RoleTypes.Custom.intValue()}, Role.class, 0, 0);
			for (Role role : croles) {
				IRoleParser rp = ReflectTool.newInstance(role.getF_config());
				if (rp.isMember(role.getId(), userId)) {
					roles.add(role);
				}
			}
		} catch (DbAccessException e) {
			throw new OrganizationException("获取用户角色列表失败", e);
		} finally {
			db().endTransaction();
		}
		return roles;
	}

	@Override
	public List<User> findRoleUsers(long companyId, long roleId) throws OrganizationException {
		List<User> users = ListTool.newArrayList();
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Role.class) + " where id = ?";
			Role r = getDbAccess().getObject(sql, new Object[]{ roleId }, Role.class);
			if (r != null) {
				if (r.getF_type() == RoleTypes.Normal.intValue()) {
					// 普通类型角色，使用SQL查询角色成员
					sql = "select a.* from t_asc_user a inner join t_asc_role_user b on a.id = b.f_user_id where b.f_role_id = "+ roleId +
							(companyId != 0L ? (" and b.f_company_id = " + companyId) : "");
					users = getDbAccess().listObjects(sql, null, User.class, 0, 0);
				} else if (r.getF_type() == RoleTypes.Custom.intValue()) {
					// 自定义类型角色，使用角色自定义方法解析角色成员
					if (r.getF_config() == null || "".equals(r.getF_config())) {
						throw OrganizationException.forRoleCutomClassNotSpecified(r.getF_caption());
					}
					IRoleParser rp = ReflectTool.newInstance(r.getF_config());
					if (rp != null) {
						users = rp.listMembers(roleId);
					}
				}
			} 
		} catch (DbAccessException e) {
			throw new OrganizationException("获取角色用户列表失败", e);
		} finally {
			db().endTransaction();
		}
		return users;
	}

	@Override
	public OrgUser findOrgUserById(long id) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{id}, OrgUser.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户获取失败。", e);
		} finally {
			db().endTransaction();
		}
	}
	

	@Override
	public List<OrgUser> getOrgUserByOrgAndUser(long userId, long orgId)
			throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_org_user where f_org_id = ? and f_user_id = ? order by f_order";
			return getDbAccess().listObjects(sql, new Object[]{orgId, userId}, OrgUser.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户获取失败。", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<OrgUser> getOrgUsers(long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_org_user where f_org_id = ? order by f_order";
			return getDbAccess().listObjects(sql, new Object[]{orgId}, OrgUser.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户获取失败。", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> getOrgList(String query) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where f_caption like ? order by f_no";
			return getDbAccess().listObjects(sql, new Object[]{"%" + query + "%"}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构搜索失败。");
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public OrgUser findUserDefaultOrg(User user) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_org_user where f_user_id = " + user.getId() +
					" and (f_org_id = " + user.getF_company_id() + " or f_org_id = " + user.getF_dept_id() + ")";
			return getDbAccess().getObject(sql, null, OrgUser.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("用户默认机构获取失败。", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<User> findUsers(String query) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_user where f_caption like ?";
			return getDbAccess().listObjects(sql, new Object[]{"%" + query + "%"}, User.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("用户查询错误。", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public RoleUser findRoleUser(long orgId, long roleId, long id) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from t_asc_role_user where f_company_id = ? and f_role_id = ? and f_user_id = ?";
			return getDbAccess().getObject(sql, new Object[]{orgId, roleId, id}, RoleUser.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("角色用户查找失败。");
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public OrgUser findOrgUser(long id, long orgId) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where f_user_id = ? and f_org_id = ?";
			return getDbAccess().getObject(sql, new Object[]{id, orgId}, OrgUser.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户查找失败。", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<User> findDelUser() throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(User.class) + 
					" where f_state = " +  User.States.Deleted.intValue() + 
					" order by f_caption";
			return getDbAccess().listObjects(sql, null, User.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("已删除列表获取失败！", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findLowerOrgs(long id) throws OrganizationException {
		Org org = this.getOrgById(id);
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where f_no like ?" +
					" order by f_order";
			// 南昌局实施时，f_no字段不维护，只能通过父子关系查找上下级
			//String sql = "select a.* from t_asc_org a start with a.id=? connect by prior a.id=a.f_parent_id";
			return getDbAccess().listObjects(sql, new Object[]{org.getId()}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("下级机构列表获取失败！", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findUpperOrgs(long id) throws OrganizationException {
		Org org = this.getOrgById(id);
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where ? like f_no || '_%'" +
					" order by f_order";
			// 南昌局实施时，f_no字段不维护，只能通过父子关系查找上下级
			//String sql = "select a.* from t_asc_org a start with a.id=? connect by prior a.f_parent_id=a.id";
			return getDbAccess().listObjects(sql, new Object[]{org.getId()}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("上级机构列表获取失败！", e);
		} catch (Exception e) {
			throw new OrganizationException("查询组织机构数据失败," +e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Role> findRolesByUserAndCom(long userId, long companyId)
			throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select a.* from " + CommonDatabaseAccess.getTableName(Role.class) + " a" +
					" left join " + CommonDatabaseAccess.getTableName(RoleUser.class) + " b" +
					" on a.f_id = b.f_role_id" +
					" where b.f_user_id = ? and b.f_company_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{userId, companyId}, Role.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取角色失败", e);
		}finally {
			db().endTransaction();
		}
	}

	@Override
	public Role getRoleByKey(String key) throws OrganizationException {
		db().beginTransaction();
		try{		
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Role.class) + 
					" where f_key = ?";
			return getDbAccess().getObject(sql, new Object[]{key}, Role.class);
		} catch (DbAccessException e) {
			throw new OrganizationException("获取角色失败", e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public void updateUserPswd(long userId, String password)
			throws OrganizationException {
		throw new OrganizationException("只读实现，未实现更新密码方法");
	}

	@Override
	public List<Org> findOrgByOrgTypeAndParId(String orgType, long parentId)
			throws OrganizationException {
		List<Org> res = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +" where 1=1 ";
			String wheresql = "";
			List<Object> qlist = new ArrayList<Object>(); 
			
			if(orgType != null && !"".equals(orgType)){
				wheresql += " and f_orgtype = ? ";
				qlist.add(orgType);
			}
			if(parentId != 0L){
				wheresql += " and asc_nvl(f_parent_id,0) = ? ";
				qlist.add(parentId);
			}
			if(!"".equals(wheresql)){
				sql += wheresql + "order by f_order";
				res = getDbAccess().listObjects(sql, qlist.toArray(), Org.class, 0, 0);
			}
			return res;
		} catch (DbAccessException e) {
			throw new OrganizationException("组织机构列表获取失败！", e);
		} catch (Exception e) {
			throw new OrganizationException("查询组织机构数据失败," +e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Org getOrgByOrgCode(String orgCode) throws OrganizationException {
		Org org = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where f_code = ? order by f_order";
			List<Org> OrgList = getDbAccess().listObjects(sql, new Object[]{orgCode}, Org.class, 0, 0);
			if(OrgList.size()>0){
				org = OrgList.get(0);
			}
		} catch (DbAccessException e) {
			throw new OrganizationException("组织机构列表获取失败！", e);
		} catch (Exception e) {
			throw new OrganizationException("查询组织机构数据失败," +e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
		return org;
	}

	@Override
	public List<Org> findOrgByOrgType(String orgType) throws OrganizationException {
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where f_orgtype = ? order by f_order";
			return getDbAccess().listObjects(sql, new Object[]{orgType}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("组织机构列表获取失败！", e);
		} catch (Exception e) {
			throw new OrganizationException("查询组织机构数据失败," +e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> getOrgListByPidAndQuery(long parentId, String query)
			throws OrganizationException {
		db().beginTransaction();
		String param = null;
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where 1=1 ";
			if(parentId != -1){
				sql += " and asc_nvl(f_parent_id,0) = ? ";
				param = String.valueOf(parentId);
			}else{
				sql += " and F_ORGTYPE = ? ";
				param = "TLJ";
			}
			sql += "and ( f_caption = ? or f_shortname = ? ) order by f_no";
			return getDbAccess().listObjects(sql, new Object[]{param,query,query}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw new OrganizationException("机构搜索失败。");
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<OrgLog> findOrgLog(String operateType,String beginTime, String endTime) throws OrganizationException {
		List<OrgLog> res = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(OrgLog.class) + " where 1=1 ";
			String wheresql = "";
			List<Object> qList = new ArrayList<Object>();
			if(operateType!=null && !"".equals(operateType)){
				wheresql += " and f_operate_type = ?";
				qList.add(operateType);
			}
			if(beginTime!=null && !"".equals(beginTime)){
				wheresql += " and f_operate_time >= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(beginTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				wheresql += " and f_operate_time <= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(endTime);
			}
			if(!"".equals(wheresql)){
				sql = sql + wheresql;
				res = getDbAccess().listObjects(sql,qList.toArray(), OrgLog.class, 0, 0);
			}
			return res;
		} catch (DbAccessException e) {
			throw new OrganizationException("机构日志查找失败");
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findUpdateOrgsByTimeFrame(String beginTime, String endTime) throws OrganizationException {
		List<Org> res = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) + " t1 where 1=1";
			String logsql = "select distinct f_org_id from " + CommonDatabaseAccess.getTableName(OrgLog.class) + " t2 where t2.f_org_id = t1.id";;
			String wheresql = ""; 
			List<Object> qList = new ArrayList<Object>();
 			if(beginTime!=null && !"".equals(beginTime)){
				wheresql += " and f_operate_time >= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(beginTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				wheresql += " and f_operate_time <= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(endTime);
			}
			if(!"".equals(wheresql)){
				sql += " and exists ( "+logsql+wheresql+" )";
				res = getDbAccess().listObjects(sql, qList.toArray(), Org.class, 0, 0);
			}
			return res;
		} catch (DbAccessException e) {
			throw new OrganizationException("组织机构列表获取失败！", e);
		} catch (Exception e) {
			throw new OrganizationException("查询组织机构数据失败," +e.getMessage(), e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findInsertOrgsByTimeFrame(String beginTime, String endTime) throws OrganizationException {
		List<Org> res = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Org.class) + " where 1=1 ";
			String wheresql = "";
			List<Object> qList = new ArrayList<Object>();
			if(beginTime!=null && !"".equals(beginTime)){
				wheresql += " and f_create_time >= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(beginTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				wheresql += " and f_create_time <= ASC_TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')";
				qList.add(endTime);
			}
			if(!"".equals(wheresql)){
				sql = sql + wheresql;
				res = getDbAccess().listObjects(sql,qList.toArray(), Org.class, 0, 0);
			}
			return res;
		} catch (DbAccessException e) {
			throw OrganizationException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Org> findOrgList(String currentCode,String parentCode, String orgType, String appendSQL) throws OrganizationException {
		List<Org> result = ListTool.newArrayList();
		db().beginTransaction();
		try {
			String orgTablename = getTableName(Org.class);
			String sql = "select * from " + orgTablename + " where 1=1";
			if (parentCode != null && !"".equals(parentCode)) {
				sql = sql + " and f_parent_id=(select id from " + orgTablename + " where f_code='" + parentCode + "')";
			}
			if (orgType != null && !"".equals(orgType)) {
				sql = sql + " and f_orgtype='" + orgType + "'";
			}
			if (currentCode != null && !"".equals(currentCode)) {
				sql = sql + " and f_code='" + currentCode + "'";
			}
			if (appendSQL != null && !"".equals(appendSQL)) {
				sql = sql + " " + appendSQL;
			}
			result = getDbAccess().listObjects(sql, new Object[]{}, Org.class, 0, 0);
		} catch (DbAccessException e) {
			throw OrganizationException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
		return result;
	}

	@Override
	public OrgAttribute getOrgAttributeByOrgCode(String orgCode)throws OrganizationException {
		db().beginTransaction("rdmsappDsn");
		String sql = "select * from T_RDMS_BASE_ORG_ATTRIBUTE where F_ORG_CODE = ?";
		try {
			return db().getObject(sql, new Object[]{orgCode}, OrgAttribute.class);
		} catch (DbAccessException e) {
			throw OrganizationException.forDbAccessException(e);
		} finally{
			db().endTransaction();
		}
	}

}
