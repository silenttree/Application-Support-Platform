package com.asc.bs.extproperty.access.imp;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.extproperty.access.IExtpropertyWrite;
import com.asc.commons.extproperty.entity.Extproperty;

public class ExtpropertyWriteImp implements IExtpropertyWrite {

	@Override
	public void deleteExtproperty(long id) {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			//删除扩展属性
			String sql = "delete from " + CommonDatabaseAccess.getTableName(Extproperty.class) +
					" where id = ?";
			CommonDatabaseAccess.instance().execute(sql, new Object[]{id});
			//删除扩展属性数据
			sql = "delete from t_asc_ext_property_data " +
					" where f_property_id = ?";
			CommonDatabaseAccess.instance().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}
}
