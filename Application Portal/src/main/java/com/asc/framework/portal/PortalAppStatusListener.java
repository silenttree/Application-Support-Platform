package com.asc.framework.portal;

import com.asc.framework.app.IApplicationStatusListener;
import com.asc.framework.exception.FrameworkException;
import com.asc.portal.builder.FileBuilderException;
import com.asc.portal.builder.RuntimeFileBuilder;

/**
 * <pre>
 * Capital Group Finance Service
 * Application Portal
 * 应用加载状态监听实现
 * 
 * Mixky Co., Ltd. 2015
 * @author Bill
 * </pre>
 */
public class PortalAppStatusListener implements IApplicationStatusListener {

	@Override
	public void start() throws FrameworkException {
		try {
			RuntimeFileBuilder.instance().buildPortalActions();
		} catch (FileBuilderException e) {
			throw FrameworkException.forEngineStartFailed("生成运行时文件失败", e);
		}
	}

	@Override
	public void stop() throws FrameworkException {
	}

	@Override
	public void suspend() throws FrameworkException {
	}

	@Override
	public void resume() throws FrameworkException {
	}

}
