package com.asc.demo.direct;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.asc.commons.certification.AscUserCertification;
import com.asc.commons.organization.entity.User;
import com.asc.workflow.WorkflowException;
import com.asc.workflow.action.FlowAction;
import com.asc.workflow.action.FlowActionUtil;
import com.asc.workflow.batch.BatchHelper;
import com.asc.workflow.core.dao.FlowLog;
import com.asc.workflow.core.dao.NodeLog;
import com.asc.workflow.core.dao.ProcessLog;
import com.asc.workflow.core.template.Flow;
import com.asc.workflow.core.util.InstanceDataUtil;
import com.asc.workflow.core.util.WorkflowObjectUtil;
import com.asc.workflow.opinion.OpinionDataUtil;
import com.asc.workflow.opinion.dao.OpinionLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

@DirectAction(action = {"AppDemoWorkflowDemoDirect" })
public class WorkflowDemoDirect {
	
	@DirectMethod
	public JsonObject batchSubmit(String dataType, Long[] dataIds, String routeCaption, String opinionTxt, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			HttpSession session = request.getSession();
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
//				throw 
			}
			Map<Long, WorkflowException> exps = BatchHelper.batchSubmit(dataType, dataIds, routeCaption, opinionTxt, user, session);
			if (exps.size() > 0) {
				StringBuffer sb = new StringBuffer();
				Iterator<Map.Entry<Long, WorkflowException>> it = exps.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Long, WorkflowException> entry = it.next();
					WorkflowException exp = entry.getValue();
					sb.append("[").append(entry.getKey()).append(",");
					sb.append(exp.getMessage()).append("]\n");
					exp.printStackTrace();	// FIXME
				}
				json.addProperty("message", sb.toString());
			} else {
				json.addProperty("success", true);
			}
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	@DirectMethod
	public JsonObject batchStartWf(String dataType, Long[] dataIds, String flowId, int versionId, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			HttpSession session = request.getSession();
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
//				throw 
			}
			Map<Long, WorkflowException> exps = BatchHelper.batchStartWf(dataType, dataIds, flowId, versionId, user, session);
			if (exps.size() > 0) {
				StringBuffer sb = new StringBuffer();
				Iterator<Map.Entry<Long, WorkflowException>> it = exps.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Long, WorkflowException> entry = it.next();
					WorkflowException exp = entry.getValue();
					sb.append("[").append(entry.getKey()).append(",");
					sb.append(exp.getMessage()).append("]\n");
					exp.printStackTrace();	// FIXME
				}
				json.addProperty("message", sb.toString());
			} else {
				json.addProperty("success", true);
			}
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject batchStartWf1(HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			String dataType = "mDemo.docZlyy";
			Long[] dataIds = {2338L};
			String flowId = "flowInfo";
			int versionId = 1;
			
			HttpSession session = request.getSession();
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
//				throw 
			}
			Map<Long, WorkflowException> exps = BatchHelper.batchStartWf(dataType, dataIds, flowId, versionId, user, session);
			if (exps.size() > 0) {
				StringBuffer sb = new StringBuffer();
				Iterator<Map.Entry<Long, WorkflowException>> it = exps.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Long, WorkflowException> entry = it.next();
					WorkflowException exp = entry.getValue();
					sb.append("[").append(entry.getKey()).append(",");
					sb.append(exp.getMessage()).append("]\n");
					exp.printStackTrace();	// FIXME
				}
				json.addProperty("message", sb.toString());
			} else {
				json.addProperty("success", true);
			}
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 提交流程办理意见
	 * 
	 * @param processId
	 * @param objectId
	 * @param opinion
	 * @param request
	 * @return
	 */
	public JsonObject submitOpinion(long processId, String objectId, String opinion, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			if (processId < 1) {
				throw new IllegalArgumentException("未设置用户办理实例ID");
			}
			if (opinion == null || "".equals(opinion)) {
				throw new IllegalArgumentException("未提交意见或意见为空");
			}
			User user = AscUserCertification.instance().getUser(request);
			OpinionDataUtil.submitOpinion(processId, objectId, opinion, user.getId(), user.getF_caption());
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 装载办理实例对应的流程办理意见
	 * 
	 * @param processId
	 *            用户办理实例id
	 * @param objectId
	 *            意见对应的对象标识符
	 * @param request
	 * @return
	 */
	public JsonObject loadOpinion(long processId, String objectId, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			if (processId < 1) {
				throw new IllegalArgumentException("未设置用户办理实例ID");
			}
			// TODO 增加用户是否已经登录的校验
			List<OpinionLog> ologs = OpinionDataUtil.listOpinionLogs(null, 0, 0, processId, objectId);
			JsonArray ops = new JsonArray();
			for (OpinionLog ol : ologs) {
				ops.add(OpinionDataUtil.formatDisplayText(ol));
			}
			json.add("opinions", ops);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 装载流程启动时可选的路由按钮
	 * 
	 * @param flowId
	 *            流程标识符
	 * @param versionId
	 *            版本号
	 * @param request
	 * @return
	 */
	public JsonObject listRouteButton(String flowId, int versionId, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			if (flowId == null || "".equals(flowId)) {
				throw new IllegalArgumentException("未设置流程标识符");
			}
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
				throw new IllegalStateException("用户未登录");
			}
			List<FlowAction> actions = FlowActionUtil.listCreatorRoutesAsActions(flowId, versionId, user);
			JsonArray jactions = new JsonArray();
			if (actions != null) {
				for (FlowAction fa : actions) {
					jactions.add(JsonObjectTool.object2JsonObject(fa));;
				}
			}
			json.add("actions", jactions);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 装载流程实例路由按钮
	 * 
	 * @param flowlogId
	 * @param request
	 * @return
	 */
	public JsonObject listRouteButton(long flowlogId, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			if (flowlogId < 1) {
				throw new IllegalArgumentException("未设置流程实例ID");
			}
			NodeLog cnlog = InstanceDataUtil.getFlowCurrentNodelog(flowlogId);
			if (cnlog == null) {
				throw new IllegalArgumentException("节点实例不存在");
			}
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
				throw new IllegalStateException("用户未登录");
			}
			ProcessLog plog = InstanceDataUtil.getProcessingLog(cnlog.getId(), user.getId());
			if (plog == null) {
				throw new IllegalArgumentException("用户办理实例不存在");
			}
			List<FlowAction> actions = FlowActionUtil.listRoutesAsActions(plog.getId(), user);
			JsonArray jactions = new JsonArray();
			if (actions != null) {
				for (FlowAction fa : actions) {
					jactions.add(JsonObjectTool.object2JsonObject(fa));;
				}
			}
			json.add("actions", jactions);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 查询流程批量提交处理信息
	 * 
	 * @param flowlogId
	 * @param request
	 * @return
	 */
	public JsonObject getBatchProcessInfo(long flowlogId, HttpServletRequest request) {
		JsonObject json = new JsonObject();
		json.addProperty("success", false);
		try {
			FlowLog flog = InstanceDataUtil.getFlowLog(flowlogId);
			if (flog == null) {
				throw new IllegalArgumentException("流程实例不存在");
			}
			Flow flow = WorkflowObjectUtil.getFlowObject(flog.getF_flow_id(), flog.getF_flow_versionid());
			if (flow == null) {
				throw new IllegalArgumentException("流程模板不存在");
			}
			NodeLog nlog = InstanceDataUtil.getFlowCurrentNodelog(flowlogId);
			if (nlog == null) {
				throw new IllegalArgumentException("节点实例不存在");
			}
			User user = AscUserCertification.instance().getUser(request);
			if (user == null) {
				throw new IllegalStateException("用户未登录");
			}
			ProcessLog plog = InstanceDataUtil.getProcessingLog(nlog.getId(), user.getId());
			if (plog == null) {
				throw new IllegalArgumentException("用户办理实例不存在");
			}
			JsonObject jInfo = new JsonObject();
			jInfo.addProperty("flowTitle", flow.getF_caption());

			List<OpinionLog> ologs = OpinionDataUtil.listOpinionLogs(null, 0, 0, plog.getId(), null);
			JsonArray ops = new JsonArray();
			for (OpinionLog ol : ologs) {
				ops.add(OpinionDataUtil.formatDisplayText(ol));
			}
			jInfo.add("opinion", ops);
			jInfo.addProperty("state", nlog.getF_node_caption());
			jInfo.addProperty("processor", user.getF_caption());
			json.add("info", jInfo);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
}