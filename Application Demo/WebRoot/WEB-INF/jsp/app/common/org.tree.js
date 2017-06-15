<%@page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
// 设置默认的用户机构
var initUserOrg = function(userId, combos, callbackFn) {
	var tljCombo = combos.tljCombo;
	var gwdCombo = combos.gwdCombo;
	var cjCombo = combos.cjCombo;
	var xlCjCombo = combos.xlCjCombo;
	var tsCjCombo = combos.tsCjCombo;
	var gqCombo = combos.gqCombo;
	var xlGqCombo = combos.xlGqCombo;
	var tsGqCombo = combos.tsGqCombo;
	var bzCombo = combos.bzCombo;
	
	var getOrgTree = appManager.getAppDirectFn(appKey, "SchedulesDirect", "getOrgTree");
	//设置Combo组件（不从后台查询）
	var setNoneQueryCombo = function (cmp, val, display) {
		if (cmp) {
			cmp.getStore().add({
				key : val,
				value : display
			});
			cmp.setValue(val);
			cmp.setReadOnly(true);
		}
	};
	//设置Combo组件
	var setCombo = function (cmp, defaultValue, isDisabled, queryItem) {
		if(!cmp) {
			return;
		}
		cmp.getStore().load({
			params : {
				QI : queryItem
			}
		});
		if (defaultValue && defaultValue != null) {
			cmp.setValue(defaultValue);
			cmp.setReadOnly(true);
		}
	};
	getOrgTree(userId , function (result, event) {
		if (result.success) {
			if (result.BZ) { //当前为班组用户
				setNoneQueryCombo(bzCombo, result.BZ, result.BZ_NAME);
				setNoneQueryCombo(gqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(xlGqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(tsGqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(cjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(xlCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(tsCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(gwdCombo, result.GWD, result.GWD_NAME);
				setNoneQueryCombo(tljCombo, result.TLJ, result.TLJ_NAME);
			} else if (result.GQ) { //当前用户为工区级别用户
				//设置工区Combo选中（不查询直接本地增加并选定）
				setNoneQueryCombo(gqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(xlGqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(tsGqCombo, result.GQ, result.GQ_NAME);
				setNoneQueryCombo(cjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(xlCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(tsCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(gwdCombo, result.GWD, result.GWD_NAME);
				setNoneQueryCombo(tljCombo, result.TLJ, result.TLJ_NAME);
				//设置初始化线路列表
				setCombo(bzCombo, null, false, {
					orgType : 'BZ',
					parentCode : result.GQ
				});
			} else if (result.CJ) { //当前用户为车间级别用户
				setNoneQueryCombo(cjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(xlCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(tsCjCombo, result.CJ, result.CJ_NAME);
				setNoneQueryCombo(gwdCombo, result.GWD, result.GWD_NAME);
				setNoneQueryCombo(tljCombo, result.TLJ, result.TLJ_NAME);
				//使用车间CODE作为parentCode加载工区
				setCombo(gqCombo, null, false, {
					orgType : 'GQ',
					parentCode : result.CJ
				});
				setCombo(xlGqCombo, null, false, {
					orgType : 'GQ',
					attr : 'XLGQ',
					parentCode : result.CJ
				});
				setCombo(tsGqCombo, null, false, {
					orgType : 'GQ',
					attr : 'TSGQ',
					parentCode : result.CJ
				});

			} else if (result.GWD) { //当前用户为工务段级别用户
				setNoneQueryCombo(gwdCombo, result.GWD, result.GWD_NAME);
				setNoneQueryCombo(tljCombo, result.TLJ, result.TLJ_NAME);
				setCombo(cjCombo, null, false, {
					orgType : 'CJ',
					parentCode : result.GWD
				});
				setCombo(xlCjCombo, null, false, {
					orgType : 'CJ',
					attr : 'XLCJ',
					parentCode : result.GWD
				});
				setCombo(tsCjCombo, null, false, {
					orgType : 'CJ',
					attr : 'TSCJ',
					parentCode : result.GWD
				});
			} else if (result.TLJ) { //当前用户为铁路局级别用户
				setNoneQueryCombo(tljCombo, result.TLJ, result.TLJ_NAME);
				setCombo(gwdCombo, null, false, {
					orgType : 'GWD',
					parentCode : result.TLJ
				});
			} else { //当前用户为铁总或者管理员
				tljCombo.getStore().load({
					params : {
						QI : {
							orgType : 'TLJ'
						}
					}
				});
			}
			if (callbackFn) {
				callbackFn(result);
			}
		}
	});
};
