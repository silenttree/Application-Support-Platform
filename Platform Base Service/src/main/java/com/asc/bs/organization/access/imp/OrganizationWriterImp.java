package com.asc.bs.organization.access.imp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.organization.access.IOrganizationWriter;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.OrgLog;
import com.asc.commons.organization.entity.OrgUser;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.RoleUser;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.security.PasswordEncoder;
import com.asc.commons.organization.service.OrganizationService;

/**
 * Application Framework/OrganizationMaintainDbacImp.java<br>
 * 数据库方式的组织机构数据维护实现
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class OrganizationWriterImp implements IOrganizationWriter {

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}
	
	@Override
	public User createUser(String loginName, String caption) throws OrganizationException {
		// 检查用户名是否已被占用
		User user = OrganizationService.instance().getUserByName(loginName);
		if (user != null && user.getId() > 0) {
			throw OrganizationException.forDuplicatedLoginName(loginName);
		}
		// 创建用户
		user = new User();
		user.setF_name(loginName);
		user.setF_caption(caption);
		user.setF_create_time(new Date());

		return user;
	}

	@Override
	public void saveUser(User user) throws OrganizationException {
		// 检查用户信息是否合法（关键字段）
		if (user.getF_name() == null || "".equals(user.getF_name())) {
			throw new OrganizationException("未指定用户登录名");
		}
		if (user.getF_caption() == null || "".equals(user.getF_caption())) {
			throw new OrganizationException("未指定用户姓名");
		}
		// 未指定密码时使用默认密码
		if (user.getF_password() == null || "".equals(user.getF_password())) {
			PasswordEncoder encoder = new PasswordEncoder();
			user.setF_password(encoder.encode("1234"));
		}

		// 保存用户实体对象
		user.setF_update_time(new Date());
		try {
			getDbAccess().saveObject(user);
		} catch (DbAccessException e) {
			throw new OrganizationException("保存用户对象失败：" + e.getMessage(), e);
		}

		// TODO 记录日志
	}

	@Override
	public Org createOrg(long parentOrgId, String orgNo, String caption, Org.Types type) throws OrganizationException {
		// TODO:检查编号规则是否合法

		// TODO:检查名称是否合法

		// 检查相同编号的部门是否已经存在(不允许相同编号的部门存在)
		/*Org org = OrganizationService.instance().getOrgByNo(orgNo);
		if (org != null && org.getId() > 0) {
			throw OrganizationException.forDuplicatedOrgNo(orgNo);
		}
		org = new Org();
		org.setF_parent_id(parentOrgId);
		org.setF_no(orgNo);
		org.setF_caption(caption);
//		org.setF_type(type.);
		org.setF_create_time(new Date());
		 */
		return null;
	}

	@Override
	public void saveOrg(Org org) throws OrganizationException {
		// TODO:信息合法性校验 
		//将其判断逻辑转移至manager下
		/*if(org.getF_code() == null||"".equals(org.getF_code())){
			throw new OrganizationException("未定义机构编码");
		}
		if(org.getF_caption() == null||"".equals(org.getF_caption())){
			throw new OrganizationException("未定义机构全称");
		}
		if(org.getF_shortname() == null||"".equals(org.getF_shortname())){
			throw new OrganizationException("未定义机构简称");
		}
		if(org.getF_parent_id() == 0 && org.getF_type() == Org.Types.Department.intValue()){
			throw new OrganizationException("根节点不能存在部门");
		}*/
		// TODO:检查相同编号的部门是否已经存在（不允许相同编号的部门存在)
		
		// 保存组织实体对象
		org.setF_update_time(new Date());
		try {
			getDbAccess().saveObject(org);
		} catch (DbAccessException e) {
			throw new OrganizationException("保存组织机构对象失败：" + e.getMessage(), e);
		}
		// TODO:记录日志
	}

	@Override
	public Role createRole(String roleName, String roleCaption) throws OrganizationException {
		// 校验参数
		if (roleName == null || "".equals(roleName)) {
			throw new OrganizationException("未指定角色标识");
		}
		if (roleCaption == null || "".equals(roleCaption)) {
			throw new OrganizationException("未指定角色名称");
		}
		Role role = null;// OrganizationService.instance().getRoleByName(roleName);
		//		// 检查相同名称的角色是否已经存在
		//		if (role != null && role.getId() > 0) {
		//			throw OrganizationException.forDuplicatedRoleName(roleName);
		//		}
		
		role = new Role();
		role.setF_key(roleName);
		role.setF_caption(roleCaption);
		role.setF_create_time(new Date());
		// 
		return role;
	}

	@Override
	public void saveRole(Role role) throws OrganizationException {
		// 信息合法性校验
		if (role.getF_key() == null || "".equals(role.getF_key())) {
			throw new OrganizationException("未指定角色标识");
		}
		if (role.getF_caption() == null || "".equals(role.getF_caption())) {
			throw new OrganizationException("未指定角色名称");
		}
		// 保存角色实体对象
		role.setF_update_time(new Date());
		try {
			getDbAccess().saveObject(role);
		} catch (DbAccessException e) {
			throw new OrganizationException("保存角色对象失败：" + e.getMessage(), e);
		}
		// TODO 记录日志
	}

	@Override
	public void addUser2Org(long userId, long orgId, OrgUser.Types relation) throws OrganizationException {

	}

	@Override
	public void removeUserFromOrg(long userId, long orgId) throws OrganizationException {

	}

	@Override
	public void addUser2Role(long userId, long roleId, long orgId) throws OrganizationException {
		if (userId < 1 || roleId < 1) {
			throw new OrganizationException("添加用户到角色当中失败：未指定用户或角色ID");
		}
		// 判断用户是否已经在角色当中（相同组织层次）
		RoleUser ru;
		try {
			ru = getRoleUser(userId, roleId, orgId);
			if (ru != null) {
				throw new OrganizationException("添加用户到角色当中失败：用户已是该组织角色成员");
			}
		} catch (DbAccessException e) {
			throw new OrganizationException("添加用户到角色当中失败：" + e.getMessage(), e);
		}

		// 创建角色用户关联信息
		ru = new RoleUser();
		ru.setF_role_id(roleId);
		ru.setF_user_id(userId);
		ru.setF_company_id(orgId);
		ru.setF_create_time(new Date());
		// 保存角色用户关联信息
		try {
			getDbAccess().saveObject(ru);
		} catch (DbAccessException e) {
			throw new OrganizationException("添加用户到角色当中失败：" + e.getMessage(), e);
		}
		// TODO： 记录日志
	}

	@Override
	public void removeUserFromRole(long userId, long roleId, long orgId) throws OrganizationException {
		if (userId < 1 || roleId < 1) {
			throw new OrganizationException("从角色当中移除用户失败：未指定用户或角色ID");
		}
		RoleUser ru;
		try {
			// 判断用户是否已经在角色当中（相同组织层次）
			ru = getRoleUser(userId, roleId, orgId);
			if (ru == null) {
				throw new OrganizationException("从角色当中移除用户失败：用户不是该组织角色成员");
			}
			// 删除角色用户映射关系
			getDbAccess().deleteObject(ru);
			// TODO: 更新角色（最后更新时间）

			// TODO: 记录日志

		} catch (DbAccessException e) {
			throw new OrganizationException("从角色当中移除用户失败：" + e.getMessage(), e);
		}
	}

	@Override
	public void deleteUser(User user) throws OrganizationException {
		try {
			// 检查用户是否存在
			long userId = user.getId();
			String sql = "select id from " + CommonDatabaseAccess.getTableName(User.class) +
					" where id=?";
			long count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{userId}), 0L);
			if (count == 0) {
				throw OrganizationException.forUserNotExist(userId);
			}
			
			// 从用户所在组织中移除
			sql = "delete from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where f_user_id=?";
			getDbAccess().execute(sql, new Object[]{userId});
			// 从用户所在角色中移除
			sql = "delete from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
					" where f_user_id=?";
			getDbAccess().execute(sql, new Object[]{userId});
			
			// 标记用户删除
			user.setF_update_time(new Date());
			user.setF_state(User.States.Deleted.intValue());
			getDbAccess().saveObject(user);
			
			// TODO: 记录日志
		} catch (DbAccessException e) {
			throw new OrganizationException("删除用户失败：" + e.getMessage(), e);
		} catch (Exception e){
			throw new OrganizationException("删除用户失败：" + e.getMessage(), e);
		}
	}

	@Override
	public void deleteOrg(long orgId) throws OrganizationException {
		try {
			String orgTable = CommonDatabaseAccess.getTableName(Org.class);
			String sql = null;

			// 检查组织是否包含下级组织（包含下级组织时，不允许删除该组织）
			sql = "select count(*) from " + orgTable +
					" where f_parent_id=?";
			long count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{orgId}), 0L);
			if (count > 0) {
				throw new OrganizationException("删除组织机构失败：组织[ID=" + orgId + "]包含下级机构");
			}

			// 检查组织是否包含用户（ 包含用户时，不允许删除组织）
			sql = "select count(*) from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where f_org_id=?";
			count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{orgId}), 0L);
			if (count > 0) {
				throw new OrganizationException("删除组织机构失败：组织[ID=" + orgId + "]包含用户");
			}
			
			// 是否有用户的默认组织是这个单位（ 包含用户时，不允许删除组织）
			sql = "select count(*) from " + CommonDatabaseAccess.getTableName(User.class) +
					" where f_state <> 3 and (f_dept_id = ? or f_company_id = ?)";

			count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{orgId, orgId}), 0L);
			if (count > 0) {
				throw new OrganizationException("删除组织机构失败：组织[ID=" + orgId + "]包含用户");
			}
			// TODO: 触发组织删除事件

			// 删除组织机构信息
			sql = "delete from " + CommonDatabaseAccess.getTableName(Org.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{orgId});

			// TODO: 记录日志
		} catch (DbAccessException e) {
			throw new OrganizationException("删除组织机构失败：" + e.getMessage(), e);
		}
	}

	@Override
	public void deleteRole(long roleId) throws OrganizationException {
		try {
			// 检查角色是否包含用户（包含用户时，不允许删除该角色）
			String sql = "select id from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
					" where f_role_id=?";
			long count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{roleId}), 0L);
			if (count > 0) {
				throw new OrganizationException("删除角色失败：角色[ID=" + roleId + "]包含用户");
			}
			// TODO: 触发角色删除事件

			// 删除角色信息
			sql = "delete from " + CommonDatabaseAccess.getTableName(Role.class) +
					" where id=?";
			getDbAccess().execute(sql, new Object[]{roleId});

			// TODO： 记录日志
		} catch (DbAccessException e) {
			throw new OrganizationException("删除角色失败：" + e.getMessage(), e);
		}
	}

	@Override
	public void updateUserPswd(long userId, String password)
			throws OrganizationException {
		try {
			String sql = "update " + CommonDatabaseAccess.getTableName(User.class) +
					" set f_password = ?" + 
					" where id=?";
			getDbAccess().execute(sql, new Object[]{password, userId});
		} catch (DbAccessException e) {
			throw new OrganizationException("用户密码修改失败。", e);
		}
	}

	@Override
	public boolean validationUserPswd(long userId, String confirmPswd)
			throws OrganizationException {
		boolean confirm = false;
		if("".equals(confirmPswd) || confirmPswd == null){
			throw new OrganizationException("确认密码为空。");
		}
		User user = OrganizationService.instance().getUserById(userId);
		//判断用户密码和确认密码是否一致
		if(user.getF_password().equals(confirmPswd)){
			confirm = true;
		}
		return confirm;
	}

	private RoleUser getRoleUser(long userId, long roleId, long orgId) throws OrganizationException, DbAccessException {
		String sql = "select * from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
				" where f_user_id=? and f_role_id=? and f_org_id=?";
		return getDbAccess().getObject(sql, new Object[]{userId, roleId, orgId}, RoleUser.class);
	}

	@Override
	public void delOrgUser(long id) throws OrganizationException {
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户删除失败。", e);
		}
	}
	
	@Override
	public void deleteUserInFact(long userId) throws OrganizationException {
		try {
			// 检查用户是否存在
			String sql = "select id from " + CommonDatabaseAccess.getTableName(User.class) +
					" where id=?";
			long count = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{userId}), 0L);
			if (count == 0) {
				throw OrganizationException.forUserNotExist(userId);
			}
			// 从用户所在组织中移除
			sql = "delete from " + CommonDatabaseAccess.getTableName(OrgUser.class) +
					" where f_user_id=?";
			getDbAccess().execute(sql, new Object[]{userId});
			// 从用户所在角色中移除
			sql = "delete from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
					" where f_user_id=?";
			getDbAccess().execute(sql, new Object[]{userId});
			// 删除用户
			sql = "delete from " + CommonDatabaseAccess.getTableName(User.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{userId});
			// TODO: 记录日志
		} catch (DbAccessException e) {
			throw new OrganizationException("删除用户失败：" + e.getMessage(), e);
		} catch (Exception e){
			throw new OrganizationException("删除用户失败：" + e.getMessage(), e);
		}
	}


	@Override
	public void deleteRoleUsers(long roleId, long userId) throws OrganizationException {
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
					" where f_role_id=? and f_user_id = ?";
			getDbAccess().execute(sql, new Object[]{roleId, userId});
		} catch (DbAccessException e) {
			throw new OrganizationException("删除角色用户失败。", e);
		}
	}

	@Override
	public void createOrgUser(User user) throws OrganizationException {
		//创建用户所在单位映射
		OrgUser orgUser = new OrgUser();
		orgUser.setF_type(0);
		orgUser.setF_user_id(user.getId());
		orgUser.setId(0);
		orgUser.setF_create_time(new Date());
		orgUser.setF_order(0);
		//创建用户所在部门映射
		if(user.getF_dept_id() == 0){
			orgUser.setF_org_id(user.getF_company_id());
		}else{
			orgUser.setF_org_id(user.getF_dept_id());
		}
		
		try {
			orgUser.save();
		} catch (DbAccessException e) {
			throw new OrganizationException("机构用户映射保存失败！");
		}
	}

	@Override
	public void saveRoleuser(RoleUser roleUser) throws OrganizationException {
		try {
			getDbAccess().saveObject(roleUser);
		} catch (DbAccessException e) {
			throw new OrganizationException("角色用户添加失败！" + e.getStackTrace());
		}
	}

	@Override
	public void saveOrgLog(Org org,String operateType) throws OrganizationException {
		try {
			OrgLog orgLog = loadOrgLog(org, operateType);
			getDbAccess().saveObject(orgLog);
		} catch (DbAccessException e) {
			throw new OrganizationException("组织机构操作日志记录失败：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 处理通过Org装载OrgLog实体类
	 * @author lhy
	 * @date 2016年2月23日 下午2:49:36
	 * @param org
	 * @param operateType
	 * @return
	 */
	private OrgLog loadOrgLog(Org org,String operateType){
		OrgLog orgLog = new OrgLog();
		Log logger = LogFactory.getLog("com.mixky.toolkit.ReflectTool");
		Field [] fields = org.getClass().getDeclaredFields();
		Method [] orgMethods = org.getClass().getMethods();
		Method [] logMethods = orgLog.getClass().getMethods();
		Method setMethod = null;
		Method getMethod = null;
		for (Field field : fields) {
			String fieldname = field.getName();
			if(!fieldname.startsWith("f_") && !fieldname.equals("id")){
				continue;
			}
			String postfix = fieldname.substring(0,1).toUpperCase() + fieldname.toLowerCase().substring(1);
			String setter = "set" + postfix;
			String getter = "get" + postfix;
			for (int i = 0; i < orgMethods.length; i++) {
				if(getter.equals(orgMethods[i].getName())){
					getMethod = orgMethods[i];
					break;
				}
			}
			for (int i = 0; i < logMethods.length; i++) {
				if(setter.equals(logMethods[i].getName())){
					setMethod = logMethods[i];
					break;
				}
			}
			if(getMethod!=null && setMethod!=null){
				try {
					setMethod.invoke(orgLog, getMethod.invoke(org));
				} catch (InvocationTargetException e) {
					logger.error("InvocationTargetException:" + e.getMessage());
					if(logger.isDebugEnabled()){
						e.printStackTrace();
					}
				} catch (IllegalAccessException e) {
					logger.error("IllegalAccessException:" + e.getMessage());
					if(logger.isDebugEnabled()){
						e.printStackTrace();
					}
				} catch (IllegalArgumentException e) {
					logger.error("IllegalArgumentException:" + e.getMessage());
					if(logger.isDebugEnabled()){
						e.printStackTrace();
					}
				}
			}
		}
		//设置其他字段
		orgLog.setF_update_time(org.getF_update_time());
		orgLog.setF_create_time(org.getF_create_time());
		orgLog.setF_org_id(org.getId());
		orgLog.setF_operate_time(new Date());
		orgLog.setF_operate_type(operateType);
		return orgLog;
	}

	@Override
	public void delRoleUserById(long roleUserId,long userId) throws OrganizationException {
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(RoleUser.class) +
					" where F_ROLE_ID = ? and F_USER_ID =?";
			getDbAccess().execute(sql, new Object[]{roleUserId,userId});
		} catch (DbAccessException e) {
			throw new OrganizationException("删除角色用户失败。", e);
		}
		
	}

	
	
}
