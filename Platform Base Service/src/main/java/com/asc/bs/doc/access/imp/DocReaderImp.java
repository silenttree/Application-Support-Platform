package com.asc.bs.doc.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.List;

import com.asc.commons.doc.access.IDocReader;
import com.asc.commons.doc.entity.Doc;
import com.asc.commons.doc.exception.DocException;
import com.asc.commons.exception.DbAccessException;

public class DocReaderImp implements IDocReader {

	@Override
	public List<Doc> findDocsByParentId(long parentId) throws DocException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Doc.class) + " where f_parent_id = ?";
			return db().listObjects(sql, new Object[]{parentId}, Doc.class, 0, 0);
		} catch (DbAccessException e) {
			throw DocException.forFindDocsByParentId(parentId, e);
		} finally {
			db().endTransaction();
		}
	}
	
	@Override
	public List<Doc> findDocsByKey(String key) throws DocException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Doc.class) + " where f_key = ?";
			return db().listObjects(sql, new Object[]{key}, Doc.class, 0, 0);
		} catch (DbAccessException e) {
			throw DocException.forFindDocsByKey(key, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public Doc getDocById(long id) throws DocException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Doc.class) + " where id = ?";
			return db().getObject(sql, new Object[]{id}, Doc.class);
		} catch (DbAccessException e) {
			throw DocException.forGetDocById(id, e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public List<Doc> findAll() throws DocException {
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Doc.class);
			return db().listObjects(sql, new Object[]{}, Doc.class, 0, 0);
		} catch (DbAccessException e) {
			throw DocException.forFindAll(e);
		} finally {
			db().endTransaction();
		}
	}

}
