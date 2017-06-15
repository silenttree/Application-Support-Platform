package com.asc.bs.log.aop;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.asc.commons.log.ActivityResultTypes;
import com.asc.commons.log.ILog;
import com.asc.commons.log.LogFactory;
import com.asc.commons.log.LogTypes;
import com.google.common.collect.Maps;
import com.mixky.toolkit.ListTool;

/**
 * Platform Base Service<br>
 * service访问方法调用拦截（实现透明调用日志）
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class ServiceInvokeLogInterceptor implements MethodInterceptor {
	private Map<Class<?>, ILog> loggers;
	
	public ServiceInvokeLogInterceptor() {
		loggers = new HashMap<Class<?>, ILog>();
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//String targetName = invocation.getThis().getClass().getName();
		String methodName = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		ILog logger = getLogger(invocation.getThis().getClass());
		String fromHost = getRemoteHost();
		// FIXME 升级为不缺分大小写的kv
		Map<String, String> headers = getRemoteHeader();
		String fromAppId = null;
		String fromNodeId = null;
		if (headers != null) {
			fromAppId = headers.get("appId");
			fromNodeId = headers.get("nodeId");
		}
		String args = objectArray2String(arguments);
		String log = "来自应用[" + fromAppId + "(node:" + fromNodeId + ",ip:" + fromHost + ")]的请求，远程调用[" + methodName + "]，参数[" + args + "].";
		Object result = null;
		try {
			result = invocation.proceed();
			logger.info(methodName, args, result==null?"":result.toString(), log, ActivityResultTypes.Successed, null);
		} catch (Exception e) {
			logger.warning(methodName, objectArray2String(arguments), result==null?"":result.toString(), log + " 异常," + e.getMessage(), ActivityResultTypes.Failed, null);
		}
		return result;
	}
	
	private ILog getLogger(Class<?> clazz) {
		if (!loggers.containsKey(clazz)) {
			loggers.put(clazz, LogFactory.instance().getLogger(LogTypes.SystemLog, clazz.getName(), clazz));
		}
		return loggers.get(clazz);
	}
	
	private String objectArray2String(Object[] objs) {
		return ListTool.arrayToString(objs, null, ",");
	}
	
	/**
	 * 从请求信息中获取远端请求时添加的头信息
	 * 
	 * @return
	 */
	private Map<String, String> getRemoteHeader() {
		Map<String, String> headers = Maps.newHashMap();
		/*MessageContext mc = MessageContext.getCurrentMessageContext();
		SOAPHeader header = mc.getEnvelope().getHeader();
		Iterator<OMNode> it = header.getChildElements();
		while (it.hasNext()) {
			Object obj = it.next();
			OMElement el = (OMElement)obj;
			String key = el.getLocalName();
			String value = el.getText();
			headers.put(key, value);
			
		}*/
		return headers;
	}
	
	/**
	 * 获取webservice请求客户端ip地址信息
	 * 
	 * @return
	 */
	private String getRemoteHost() {
		/*MessageContext mc = MessageContext.getCurrentMessageContext();
		HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		if (request != null) {
			return request.getRemoteHost();
		}*/
		return null;
	}
}