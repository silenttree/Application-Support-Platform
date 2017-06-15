package com.asc.util;

import java.util.List;

import junit.framework.TestCase;

import com.asc.util.clazz.ClassUtil;

/**
 * Mixky Toolkit/ClassUtilTest.java<br>
 * 类包扫描功能测试
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class ClassUtilTest extends TestCase {
	public void testScanPackage() {
		List<Class<?>> classes = ClassUtil.scanPackage("com.asc.framework.engine.design.entity");
		for (int i = 0; i < classes.size(); i++) {
			System.out.println(classes.get(i).getName());
		}
	}
}
