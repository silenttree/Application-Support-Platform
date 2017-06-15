package com.asc.bs.dictionary.access.imp;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.dictionary.access.IDictionaryReader;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.mixky.toolkit.StringTool;

public class DictionaryReaderImp implements IDictionaryReader {

	@Override
	public Dictionary findDictionaryById(long dicId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Dictionary.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{dicId}, Dictionary.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<Dictionary> getDictionaryList(long dicId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Dictionary.class) +" where f_parent_id = ? "+
					" order by f_key asc";
			return getDbAccess().listObjects(sql, new Object[]{dicId}, Dictionary.class, 0, 0);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典列表获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<DictionaryData> getDictionaryDataList(long dicId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) +
					" where f_dictionary_id = ?" + " order by f_order asc";
			return getDbAccess().listObjects(sql, new Object[]{dicId}, DictionaryData.class, 0, 0);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据列表获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public DictionaryData findDictionaryDataById(long id) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{id}, DictionaryData.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public Dictionary findDictionaryById(String key) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Dictionary.class) +
					" where f_key = ?";
			return getDbAccess().getObject(sql, new Object[]{key}, Dictionary.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	

	@Override
	public DictionaryData findDictionaryDataById(String key, long dicId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) +
					" where f_key = ? and f_dictionary_id = ?";
			return getDbAccess().getObject(sql, new Object[]{key,dicId}, DictionaryData.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	
	@Override
	public List<DictionaryData> getDictionaryDataListByParId(long dicId, long dataId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) +
					" where f_dictionary_id = ? and f_parent_dictionary_data_id = ? order by id";
			return getDbAccess().listObjects(sql, new Object[]{dicId, dataId}, DictionaryData.class, 0, 0);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据列表获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public List<DictionaryData> getDictionaryDataListByDicId(long dicId,
			int page, int limit,String query) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) + " where f_dictionary_id = ?";
			if(query != null && !"".equals(query)){
				sql = "select * from " + CommonDatabaseAccess.getTableName(DictionaryData.class) + " where f_dictionary_id = ?"+" and ( f_value like '%"+query+"%' or f_key like '%"+query+"%' )";
			}
			return getDbAccess().listObjects(sql, new Object[]{dicId}, DictionaryData.class, page, limit);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据列表获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public long getDictionaryDataTotalById(long dicId) throws DictionaryException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select count(*) from " + CommonDatabaseAccess.getTableName(DictionaryData.class) + " where f_dictionary_id = ?";
			return DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{dicId}), 0L);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据数量获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public ExtpropertyData getExtPropertyByDicDataKey(String dicDataKey, String dicKey, String fieldName)
			throws DictionaryException {
		if(StringTool.isEmpty(dicDataKey)) {
			throw new IllegalArgumentException("字典数据的KEY不能为空");
		}
		if(StringTool.isEmpty(dicKey)) {
			throw new IllegalArgumentException("字典的KEY不能为空");
		}
		if(StringTool.isEmpty(fieldName)) {
			throw new IllegalArgumentException("扩展字典名不能为空");
		}
		getDbAccess().beginTransaction();
		try {
			String sql = 
					"select extpd.* " +
							"  from t_asc_ext_property_data extpd " + 
							"  left join t_asc_ext_property extp " + 
							"    on upper(extp.f_field_name) = ? " + 
							"   and extpd.f_property_id = extp.id " + 
							"  left join t_asc_dictionary dic " + 
							"    on extp.f_data_id = dic.id\n" + 
							"   and upper(extp.f_table_name) = upper('t_asc_dictionary_data') " + 
							"  left join t_asc_dictionary_data dicd " + 
							"    on extpd.f_dataid = dicd.id " + 
							"    and dicd.f_dictionary_id = dic.id " + 
							" where dic.f_key = ? " + 
							"   and dicd.f_key = ? ";
			return getDbAccess().getObject(sql, new Object[]{fieldName.toUpperCase(), dicKey, dicDataKey}, ExtpropertyData.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("获取字典扩展属性失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public ExtpropertyData getExtPropertyByDicDataId(long dicDataId, String fieldName) throws DictionaryException {
		if(dicDataId < 1L) {
			throw new IllegalArgumentException("字典数据的ID不合法");
		}
		if(StringTool.isEmpty(fieldName)) {
			throw new IllegalArgumentException("扩展字典名不能为空");
		}
		getDbAccess().beginTransaction();
		try {
			String sql = 
					"select extpd.* " +
							"  from t_asc_ext_property_data extpd " + 
							"  left join t_asc_ext_property extp " + 
							"    on upper(extp.f_field_name) = ? " + 
							"   and extpd.f_property_id = extp.id " + 
							"  left join t_asc_dictionary dic " + 
							"    on extp.f_data_id = dic.id\n" + 
							"   and upper(extp.f_table_name) = upper('t_asc_dictionary_data') " + 
							"  left join t_asc_dictionary_data dicd " + 
							"    on extpd.f_dataid = dicd.id " + 
							"    and dicd.f_dictionary_id = dic.id " + 
							" where extpd.f_dataid = ? ";
			return getDbAccess().getObject(sql, new Object[]{fieldName.toUpperCase(), dicDataId}, ExtpropertyData.class);
		} catch (DbAccessException e) {
			throw new DictionaryException("获取字典扩展属性失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<DictionaryData> getDicDataListByKeyAndExtend(String dicKey,
			String fieldName, String filedValue) throws DictionaryException {
		if(StringTool.isEmpty(dicKey)) {
			throw new IllegalArgumentException("字典的KEY不能为空");
		}
		if(StringTool.isEmpty(fieldName)) {
			throw new IllegalArgumentException("扩展字典名不能为空");
		}
		if(StringTool.isEmpty(filedValue)) {
			throw new IllegalArgumentException("扩展字典值不能为空");
		}
		getDbAccess().beginTransaction();
		try {
			String sql = 
					"select dicd.* "+
					"  from t_asc_ext_property_data extpd "+
					"  left join t_asc_ext_property extp"+
					"    on upper(extp.f_field_name) = ? "+
					"   and extpd.f_property_id = extp.id "+
					"  left join t_asc_dictionary dic "+
					"    on extp.f_data_id = dic.id "+
					"   and upper(extp.f_table_name) = upper('t_asc_dictionary_data') "+
					"  left join t_asc_dictionary_data dicd "+
					"    on extpd.f_dataid = dicd.id "+
					"   and dicd.f_dictionary_id = dic.id "+
					" where dic.f_key = ? "+
					"   and to_char(extpd.f_value) like '%"+filedValue+"%' "+
					" order by dicd.f_order";
			 return getDbAccess().listObjects(sql, new Object[]{fieldName.toUpperCase(), dicKey}, DictionaryData.class, 0, 0);
		} catch (DbAccessException e) {
			throw new DictionaryException("获取字典扩展属性失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}
}
