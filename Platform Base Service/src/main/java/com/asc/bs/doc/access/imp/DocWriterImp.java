package com.asc.bs.doc.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import com.asc.commons.doc.access.IDocWriter;
import com.asc.commons.doc.entity.Doc;
import com.asc.commons.doc.exception.DocException;
import com.asc.commons.exception.DbAccessException;

public class DocWriterImp implements IDocWriter {

	@Override
	public void deleteDoc(long id) throws DocException {
		String sql;
		try {
			sql = "delete from " + getTableName(Doc.class) +
					" where id = ?";
			db().execute(sql, new Object[]{id});
		} catch (DbAccessException e) {
			throw new DocException("删除文档失败：" + e.getMessage(), e);
		}
		
	}

	@Override
	public void saveDoc(Doc doc) throws DocException {
		try {
			db().saveObject(doc);
		} catch (DbAccessException e) {
			throw new DocException("保存文档失败：" + e.getMessage(), e);
		}
		
	}

}
