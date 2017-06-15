package com.asc.framework.developer;

import javax.servlet.ServletContext;

import com.asc.developer.designer.RuntimeFileBuilder;
import com.asc.developer.designer.exception.FileBuilderException;
import com.asc.framework.system.loader.IContextLoadListener;

/**
 * <pre>
 * Capital Group Finance Service
 * Application Developer
 * 开发工具启动加载监听实现
 * 
 * Mixky Co., Ltd. 2015
 * @author SKS
 * </pre>
 */
public class DeveloperAppContextLoader implements IContextLoadListener {

	@Override
	public void contextInitialized(ServletContext context) {
		try {
			RuntimeFileBuilder.instance().buildDesignerClasses();
			RuntimeFileBuilder.instance().buildDesignerActions();
			RuntimeFileBuilder.instance().buildIconCssFiles();
		} catch (FileBuilderException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContext context) {
	}

}
