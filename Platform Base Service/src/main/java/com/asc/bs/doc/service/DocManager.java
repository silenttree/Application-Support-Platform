package com.asc.bs.doc.service;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.Date;
import java.util.List;

import com.asc.commons.context.ContextHolder;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.doc.access.IDocReader;
import com.asc.commons.doc.access.IDocWriter;
import com.asc.commons.doc.entity.Doc;
import com.asc.commons.doc.exception.DocException;
import com.asc.commons.exception.DbAccessException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;
import com.mixky.toolkit.StringTool;

public class DocManager {
	private static DocManager singleton;
	
	public static DocManager instance() {
		if(singleton == null) {
			singleton = ContextHolder.instance().getBean("DocManager");
		}
		return singleton;
	}
	
	private IDocReader reader;
	private IDocWriter writer;
	

	public IDocReader getReader() {
		return reader;
	}
	public void setReader(IDocReader reader) {
		this.reader = reader;
	}
	public IDocWriter getWriter() {
		return writer;
	}
	public void setWriter(IDocWriter writer) {
		this.writer = writer;
	}

	public Doc saveDoc(long docId, JsonObject data) throws DocException {
		Doc doc = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		// 创建标记
		boolean isCreate = false;
		if (id > 0) {
			doc = reader.getDocById(id);
			if (doc == null) {
				isCreate = true;
				doc = new Doc();
				doc.setF_parent_id(docId);
			}
		} else {
			isCreate = true;
			doc = new Doc();
			doc.setF_parent_id(docId);
		}

		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, doc);

		// 数据校验
		if(StringTool.isEmpty(doc.getF_caption())) {
			throw new DocException("文档的标题不能为空");
		}
		if(StringTool.isEmpty(doc.getF_key())) {
			throw new DocException("文档的标识不能为空");
		}
		
		if (isCreate) {
			doc.setF_create_time(new Date());
			IDbacTransaction tx = db().beginTransaction();
			try {
				id = db().newSeqId(getTableName(Doc.class));
				doc.setId(id);
				tx.commit();
			} catch (DbAccessException e) {
				tx.rollback();
				throw new DocException("获取新的文档ID失败！", e);
			} finally {
				db().endTransaction();
			}
		}
		
		List<Doc> docsByKey = reader.findDocsByKey(doc.getF_key());
		
		if(docsByKey != null) {
			if(docsByKey.size() > 1) {
				throw new DocException("文档的标识不能重复！");
			} else if(docsByKey.size() == 1 && docsByKey.get(0).getId() != doc.getId()) {
				throw new DocException("文档的标识不能重复！");
			}
		}
		
		doc.setF_update_time(new Date());
		IDbacTransaction tx = db().beginTransaction();
		try {
			writer.saveDoc(doc);
			tx.commit();
		} catch(DocException e){
			tx.rollback();
			e.printStackTrace();
			throw e;
		} catch(DbAccessException e){
			tx.rollback();
			throw new DocException("文档保存失败！", e);
		} finally {
			db().endTransaction();
		}
		return doc;
	}

	public void deleteDoc(long id) throws DocException {
		IDbacTransaction tx = db().beginTransaction();
		try {
			writer.deleteDoc(id);
			tx.commit();
		} catch (DocException e) {
			tx.rollback();
			throw e;
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DocException("文档删除失败！");
		} finally {
			db().endTransaction();
		}
		
	}
}
