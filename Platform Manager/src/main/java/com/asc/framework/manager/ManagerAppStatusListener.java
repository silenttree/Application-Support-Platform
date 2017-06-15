package com.asc.framework.manager;

import com.asc.framework.app.IApplicationStatusListener;
import com.asc.framework.exception.FrameworkException;
import com.asc.manager.common.RuntimeFileBuilder;

/**
 * <pre>
 * Capital Group Finance Service
 * Platform Manager
 * 管理工具应用状态监听实现
 * 
 * Mixky Co., Ltd. 2015
 * @author Bill
 * </pre>
 */
public class ManagerAppStatusListener implements IApplicationStatusListener {

	@Override
	public void start() throws FrameworkException {
		try {
			RuntimeFileBuilder.instance().buildClassFile();
			RuntimeFileBuilder.instance().buildActionFile();
		} catch (Exception e) {
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
