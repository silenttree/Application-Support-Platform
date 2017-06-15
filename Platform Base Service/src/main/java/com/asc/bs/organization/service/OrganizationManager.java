package com.asc.bs.organization.service;

import java.util.Date;
import java.util.List;

import com.asc.bs.organization.access.imp.OrganizationWriterImp;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.organization.access.IOrganizationWriter;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.OrgUser;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.RoleUser;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.service.OrganizationService;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * Application Framework/OrganizationManager.java<br>
 * 组织机构数据维护代理类（依赖注入的接口实现完成数据维护工作）
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 * @author lhy 20160223 增加组织机构操作日志(对修改以及删除操作，将Org数据记录至OrgLog表中)
 */
public class OrganizationManager {
	private static OrganizationManager singleton;
	private IOrganizationWriter orgWriter;

	public static OrganizationManager instance() {
		if (singleton == null) {
			singleton = new OrganizationManager();
		}
		return singleton;
	}

	public OrganizationManager() {
		// TODO 将实现类写入配置文件中，从配置文件中读取
		orgWriter = new OrganizationWriterImp();
	}

	public void setOrgWriter(IOrganizationWriter orgWriter) {
		this.orgWriter = orgWriter;
	}

	public User createUser(String loginName, String caption) throws OrganizationException {
		if (orgWriter == null) {
			throw OrganizationException.forNotConfiguredProperly("未配置数据维护实现");
		}
		return orgWriter.createUser(loginName, caption);
	}

	/**
	 * 保存组织
	 * 
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public Org saveOrg(long orgId, JsonObject data) throws OrganizationException {
		Org org = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		if(data.has("f_found_time") && data.get("f_found_time").isJsonNull()){
			data.addProperty("f_found_time", "");
		}
		// 创建标记
		boolean isCreate = false;
		if (id > 0) {
			org = OrganizationService.instance().getOrgById(id);
			if (org == null) {
				isCreate = true;
				org = new Org();
				org.setF_parent_id(orgId);
			}
		} else {
			isCreate = true;
			org = new Org();
			org.setF_parent_id(orgId);
		}
 		//判断上级单位类型
		Org upOrg = OrganizationService.instance().getOrgById(org.getF_parent_id());
		if (upOrg != null) {
			if(upOrg.getF_type() == Org.Types.Department.intValue() && data.get("f_type").getAsLong() == Org.Types.Company.intValue()){
				throw new OrganizationException("部门下不能存在单位。");
			}
		}
		//TODO 加入组织层级的判断

		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, org);

		//20160327 lhy add
		//TODO 关键字段非空校验
		if(org.getF_code() == null||"".equals(org.getF_code())){
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
		}
		//TODO 增加机构编码不重复校验 
		String orgCode = org.getF_code();
		Org existOrg = OrganizationService.instance().getOrgByOrgCode(orgCode);
		if (isCreate) {
			if (existOrg != null) {
				throw new OrganizationException("机构编码已经存在");
			}
		} else {
			if (existOrg != null && existOrg.getId() != id) {
				throw new OrganizationException("机构编码已经存在");
			}
		}
		//TODO 增加同一上级单位下部门全称简称不重复校验
		List<Org> brotherList = OrganizationService.instance().findByOrgtypeAndparId(null, orgId);
		for (Org brother : brotherList) {
			long bro_id = brother.getId();
			String captionName = brother.getF_caption();
			String shortName = brother.getF_shortname();
			if((org.getId() != bro_id) && (org.getF_caption().equals(captionName)||org.getF_shortname().equals(shortName))){
				throw new OrganizationException("同一上级单位下兄弟部门全称以及简称不能重复");
			}
		}
		//add end
		
		if (isCreate) {
			org.setF_create_time(new Date());
			//添加组织的单位信息
			if(org.getF_type() == Org.Types.Company.intValue()){

				IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
				try {
					id = CommonDatabaseAccess.instance().newSeqId("T_ASC_ORG");
					org.setF_company_id(id);
					org.setId(id);
					tx.commit();
				} catch (DbAccessException e) {
					tx.rollback();
					throw new OrganizationException("机构ID获取失败！", e);
				} finally {
					CommonDatabaseAccess.instance().endTransaction();
				}

				org.setF_company_caption(org.getF_caption());
			}else{
				//获取机构单位
				Org company = OrganizationService.instance().getCompany(org.getF_parent_id());
				org.setF_company_id(company.getId());
				org.setF_company_caption(company.getF_caption());
			}
		}
		org.setF_update_time(new Date());
		Org oldOrg =  OrganizationService.instance().getOrgById(id);//获取数据库Org原数据 lhy start
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.saveOrg(org);
			if(!isCreate){//当类型为更新时记录日志 lhy
				orgWriter.saveOrgLog(oldOrg, "update");
			}
			tx.commit();
		} catch(OrganizationException e){
			tx.rollback();
			e.printStackTrace();
			throw new OrganizationException("机构保存失败: "+e.getMessage(), e);
		} catch(DbAccessException e){
			tx.rollback();
			throw new OrganizationException("机构保存失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		return org;
	}
	

	/**
	 * 删除组织
	 * 
	 * @param id
	 * @throws OrganizationException 
	 */
	public void deleteOrg(long id) throws OrganizationException {
		//获取 原Org数据  lhy start
		Org oldOrg =  OrganizationService.instance().getOrgById(id);
		//end
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.deleteOrg(id);
			orgWriter.saveOrgLog(oldOrg, "delete");
			tx.commit();
		} catch (OrganizationException e) {
			tx.rollback();
			throw e;
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("组织删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 保存用户
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public User saveUser(long orgId, JsonObject data) throws OrganizationException {
		//根据选中组织ID获取到组织信息
		Org org = OrganizationService.instance().getOrgById(orgId);
		//定义组织父节点组织
		Org parOry = null;
		User user = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		// 创建标记
		boolean isCreate = false;
		if(id > 0){
			user = OrganizationService.instance().getUserById(id);
			if(user == null){
				isCreate = true;
				user = new User();
				// 写入对象数据
				JsonObjectTool.jsonObject2Object(data, user);
				if(data.has("f_dept_id")){
					user.setF_dept_id(data.get("f_dept_id").getAsLong());
					Org dept = OrganizationService.instance().getOrgById(data.get("f_dept_id").getAsLong());
					Org cmpn = OrganizationService.instance().getCompany(dept.getId());
					user.setF_dept_caption(dept.getF_caption());
					user.setF_company_id(cmpn.getId());
					user.setF_company_caption(cmpn.getF_caption());
					}
			}else{
				isCreate = true;
				// 写入对象数据
				JsonObjectTool.jsonObject2Object(data, user);
				if(data.has("f_dept_id")){
					user.setF_dept_id(data.get("f_dept_id").getAsLong());
					Org dept = OrganizationService.instance().getOrgById(data.get("f_dept_id").getAsLong());
					Org cmpn = OrganizationService.instance().getCompany(dept.getId());
					user.setF_dept_caption(dept.getF_caption());
					user.setF_company_id(cmpn.getId());
					user.setF_company_caption(cmpn.getF_caption());
				}else{
					if(org == null) {
						user.setF_company_id(0L);
						user.setF_company_caption(null);
						user.setF_dept_id(0L);
						user.setF_dept_caption(null);
					}else if(org.getF_type() == Org.Types.Company.intValue()){
						user.setF_company_id(org.getId());
						user.setF_company_caption(org.getF_caption());
						user.setF_dept_id(org.getId());
						user.setF_dept_caption(org.getF_caption());
					}else{
						parOry = OrganizationService.instance().getCompany(org.getF_parent_id());
						user.setF_company_id(parOry.getId());
						user.setF_company_caption(parOry.getF_caption());
						user.setF_dept_id(org.getId());
						user.setF_dept_caption(org.getF_caption());
					}
				}
			}
		}else{
			isCreate = true;
			user = new User();
			// 写入对象数据
			JsonObjectTool.jsonObject2Object(data, user);
			if(org == null) {
				user.setF_company_id(0L);
				user.setF_company_caption(null);
				user.setF_dept_id(0L);
				user.setF_dept_caption(null);
			}else if(org.getF_type() == Org.Types.Company.intValue()){
				//判断选中组织是否是单位  如不是单位则去查找到组织所属单位
				
				user.setF_company_id(org.getId());
				user.setF_company_caption(org.getF_caption());
				user.setF_dept_id(org.getId());
				user.setF_dept_caption(org.getF_caption());
			}else{
				parOry = OrganizationService.instance().getCompany(org.getF_parent_id());
				user.setF_company_id(parOry.getId());
				user.setF_company_caption(parOry.getF_caption());
				user.setF_dept_id(org.getId());
				user.setF_dept_caption(org.getF_caption());
			}
		}
		if (isCreate) {
			user.setF_create_time(new Date());
		}
		user.setF_update_time(new Date());
		
		// 检查用户名是否与其他用户重复
		User u = OrganizationService.instance().getUserByName(user.getF_name());
		if (u != null && u.getId() != user.getId()) {
			throw OrganizationException.forDuplicatedLoginName(user.getF_name());
		}

		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.saveUser(user);
			//创建新用户时生成机构用户映射
			if(id == 0){
				//创建机构用户映射
				orgWriter.createOrgUser(user);
			}
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("用户保存失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}

		return user;
	}

	/**
	 * 保存角色
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public Role saveRole(long orgId, JsonObject data) throws OrganizationException {
		Role role = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		// 创建标记
		boolean isCreate = false;
		if(id > 0){
			role = OrganizationService.instance().getRoleById(id);
			if(role == null){
				isCreate = true;
				role = new Role();
				role.setF_org_id(orgId);
			}
		}else{
			isCreate = true;
			role = new Role();
			role.setF_org_id(orgId);
		}
		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, role);
		if (isCreate) {
			role.setF_create_time(new Date());
		}
		role.setF_update_time(new Date());
		//角色标识重复校验
		Role roleByKey = OrganizationService.instance().getRoleByKey(role.getF_key());
		if(roleByKey != null && roleByKey.getId() != role.getId()){
			throw new OrganizationException("角色标识已存在 ");
		}
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.saveRole(role);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("角色保存失败！"+e.getMessage());
		} finally{
			CommonDatabaseAccess.instance().endTransaction();
		}
		return role;
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @throws OrganizationException
	 */
	public void delRole(long id) throws OrganizationException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.deleteRole(id);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("角色删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @throws OrganizationException
	 */
	public void delUserById(long id) throws OrganizationException {
		User user = OrganizationService.instance().getUserById(id);
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.deleteUser(user);
			tx.commit();
		} catch (OrganizationException e) {
			tx.rollback();
			throw e;
		} catch (DbAccessException e) {
			throw new OrganizationException("用户删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}
	
	/**
	 *  物理删除用户
	 * @param id
	 * @throws OrganizationException
	 */
	public void delUserByIdInFact(long id) throws OrganizationException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.deleteUserInFact(id);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("用户删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}
	/**
	 * 保存机构用户
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public OrgUser saveOrgUsers(long orgUserId, JsonObject data) throws OrganizationException {
		//根据选中组织用户ID获取到组织信息
		OrgUser orgUser = null;
		//定义组织父节点组织
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		// 创建
		if(id > 0){
			orgUser = OrganizationService.instance().findOrgUserById(id);
		}else{
			orgUser = new OrgUser();
			orgUser.setId(0);
			orgUser.setF_user_id(orgUserId);

			if(data.has("f_org_id")){
				orgUser.setF_org_id(data.get("f_org_id").getAsInt());
			}

			if(data.has("f_note")){
				orgUser.setF_note(data.get("f_note").getAsString());
			}
			orgUser.setF_create_time(new Date());
		}

		if(data.has("f_order")){
			orgUser.setF_order(data.get("f_order").getAsInt());
		}

		if(data.has("f_type")){
			orgUser.setF_type(data.get("f_type").getAsInt());
		}
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		//保存组织用户
		try {
			orgUser.save();
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("保存组织用户错误", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		return orgUser;
	}

	/**
	 * 删除角色用户
	 * 
	 * @param roleId
	 * @param userId
	 * @throws OrganizationException
	 */
	public void delRoleUser(long roleId, long userId) throws OrganizationException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.deleteRoleUsers(roleId, userId);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("角色用户删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 删除机构用户
	 * 
	 * @param id
	 * @throws OrganizationException
	 */
	public void deleteOrgUser(long id) throws OrganizationException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.delOrgUser(id);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("机构用户删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * 				用户ID
	 * @param password
	 * 				修改后的密码
	 * @throws OrganizationException
	 */
	public void updateUserPswd(long userId, String password) throws OrganizationException {
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.updateUserPswd(userId, password);
		} catch (OrganizationException e) {
			throw new OrganizationException("用户密码修改失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 用户原始密码校验
	 * 
	 * @param userId
	 * @param confirmPswd
	 * @throws OrganizationException
	 */
	public boolean validationUserPswd(long userId, String confirmPswd) throws OrganizationException{
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			return orgWriter.validationUserPswd(userId, confirmPswd);
		} catch (OrganizationException e) {
			throw new OrganizationException("用户密码修改校验失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 机构转移
	 * 
	 * @param orgId
	 * @param targetId
	 * @throws OrganizationException
	 */
	public void moveOrg(long orgId, long targetId) throws OrganizationException {
		//判断机构是否同一机构
		if(orgId == targetId){
			throw new OrganizationException("不能选择被转移机构。");
		}
		//获取被转移机构信息
		Org org = OrganizationService.instance().getOrgById(orgId);
		//获取目的机构信息
		Org targetorg = OrganizationService.instance().getOrgById(targetId);
		//判断目的机构类型是否为单位
		if(targetorg.getF_type() == Org.Types.Company.intValue()){
			org.setF_parent_id(targetId);
		}else{
			//判断被转移机构是否为单位
			if(org.getF_type() == Org.Types.Company.intValue()){
				throw new OrganizationException("单位不能移动到部门下。");
			}
			org.setF_parent_id(targetId);
			org.setF_company_caption(targetorg.getF_company_caption());
			org.setF_company_id(targetorg.getF_company_id());
		}
		//跟新时间
		org.setF_update_time(new Date());
		//更新f_order
		int order;
		List<Org> orderList = OrganizationService.instance().getOrgList(targetId);
		if(orderList==null){
			order=1;
		}else{
			order = orderList.get(orderList.size()-1).getF_order()+1;
		}
		org.setF_order(order);
		
		//保存被转移机构
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.saveOrg(org);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("组织保存失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 添加角色用户
	 * 
	 * @param roleUser
	 * @throws OrganizationException
	 */
	public void saveRoleuser(RoleUser roleUser) throws OrganizationException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.saveRoleuser(roleUser);
			tx.commit();
		} catch (OrganizationException e) {
			tx.rollback();
			throw new OrganizationException("角色用户添加失败！");
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("角色用户添加失败！");
		}finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	public void delRoleUserById(long roleUserId,long userId) throws OrganizationException{
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			orgWriter.delRoleUserById(roleUserId,userId);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("角色用户删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}

	
}