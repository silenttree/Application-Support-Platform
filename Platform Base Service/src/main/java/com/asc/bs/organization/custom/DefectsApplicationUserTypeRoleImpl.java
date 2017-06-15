package com.asc.bs.organization.custom;

import java.util.List;

import com.asc.commons.organization.custom.IRoleParser;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.Org.OrgTypes;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.service.OrganizationService;

/**
 * <pre>
 * 伤损应用系统，用户类型角色解析实现
 * 实现如铁路局、工务段、工区用户类型角色的解析
 * </pre>
 * 
 * @author Bill
 */
public class DefectsApplicationUserTypeRoleImpl implements IRoleParser {

	@Override
	public List<User> listMembers(long roleId) throws OrganizationException {
		return null;
	}

	@Override
	public boolean isMember(long roleId, long userId) throws OrganizationException {
		boolean result = false;
		Role role = OrganizationService.instance().getRoleById(roleId);
		User user = OrganizationService.instance().getUserById(userId);
		if (role != null && user != null) {
			if ("CUSTOM_ZGS".equals(role.getF_key())) {
				// 铁路总公司类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.TZ.toString())) {
					result = true;
				}
			} else if ("CUSTOM_TLJ".equals(role.getF_key())) {
				// 铁路局类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.TLJ.toString())) {
					result = true;
				}
			} else if ("CUSTOM_GWD".equals(role.getF_key())) {
				// 工务段类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.GWD.toString())) {
					result = true;
				}
			} else if ("CUSTOM_TSCJ".equals(role.getF_key())) {
				// 探伤车间类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.CJ.toString())) {
					result = true;
				}
			} else if ("CUSTOM_TSGQ".equals(role.getF_key())) {
				// 探伤工区类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.GQ.toString())) {
					result = true;
				}
			} else if ("CUSTOM_TSBZ".equals(role.getF_key())) {
				// 探伤班组类型用户
				Org company = OrganizationService.instance().getCompany(user.getF_company_id());
				if (company == null) {
					OrganizationException.forCompanyNotExist(user.getF_company_id());
				}
				if (company.getF_orgtype().equals(OrgTypes.BZ.toString())) {
					result = true;
				}
			} else {
				// TODO 不能识别的自定义类型
			}
		}
		return result;
	}

}
