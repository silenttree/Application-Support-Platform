package com.asc.bs.system.loader;

import javax.servlet.ServletContext;

import com.asc.commons.log.LogFactory;
import com.asc.framework.system.loader.IContextLoadListener;

/**
 * Platform Base Service<br>
 * 日志初始化系统装载接口实现
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class LogFactoryLoader implements IContextLoadListener {

	@Override
	public void contextInitialized(ServletContext context) {
		LogFactory.instance().initialize();
	}

	@Override
	public void contextDestroyed(ServletContext context) {
	}

}
