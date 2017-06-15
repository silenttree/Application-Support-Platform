package com.asc.bs.extproperty.access.imp;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.extproperty.access.IExtpropertyReader;
import com.asc.commons.extproperty.entity.Extproperty;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.asc.commons.extproperty.exception.ExtpropertyException;

public class ExtpropertyReaderImp implements IExtpropertyReader {

	@Override
	public List<Extproperty> findExtpropertyListByTablename(String tablename, long dataId) throws ExtpropertyException{
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Extproperty.class) +
					" where f_table_name = ? and (f_data_id = ? or f_data_id = 0) order by f_field_caption";
			return CommonDatabaseAccess.instance().listObjects(sql, new Object[]{tablename, dataId}, Extproperty.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ExtpropertyException("根据表名查找扩展属性失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public List<ExtpropertyData> findExtpropertyData(long dataId, long proId) throws ExtpropertyException {
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(ExtpropertyData.class) +
					" where f_property_id = ?";
			return CommonDatabaseAccess.instance().listObjects(sql, new Object[]{proId}, ExtpropertyData.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ExtpropertyException("扩展属性数据查找失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public Extproperty getExtproperty(long id) throws ExtpropertyException {
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Extproperty.class) +
					" where id = ?";
			return CommonDatabaseAccess.instance().getObject(sql, new Object[]{id}, Extproperty.class);
		} catch (DbAccessException e) {
			throw new ExtpropertyException("根据ID获取扩展属性查找失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public long findExtpropertyListByFieldName(String fieldName, long dataid) throws ExtpropertyException {
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "select COUNT(*) from " + CommonDatabaseAccess.getTableName(Extproperty.class) +
					" where f_field_name = ? and (f_data_id = ? or f_data_id = 0)";
			return DataValueUtil.getLong(CommonDatabaseAccess.instance().getFirstCell(sql, new Object[]{fieldName, dataid}), 0L);
		} catch (DbAccessException e) {
			throw new ExtpropertyException("扩展属性是否存在查找失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public ExtpropertyData getExtprodata(long dataId, long proId) throws ExtpropertyException {
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(ExtpropertyData.class) +
					" where f_dataid = ? and f_property_id = ?";
			return CommonDatabaseAccess.instance().getObject(sql, new Object[]{dataId, proId}, ExtpropertyData.class);
		} catch (DbAccessException e) {
			throw new ExtpropertyException("根据Id获取扩展属性数据查找失败！", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

}
