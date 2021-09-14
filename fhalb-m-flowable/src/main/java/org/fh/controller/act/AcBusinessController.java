package org.fh.controller.act;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.fh.controller.base.BaseController;
import org.fh.util.Const;
import org.fh.util.DelFileUtil;
import org.fh.util.FileUpload;
import org.fh.util.PathUtil;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;

/**
 * 说明：流程业务相关 
 * 作者：FH Admin fh3135967-90qq(青苔) 
 * 官网：www.fhadmin.org
 */
public class AcBusinessController extends BaseController {

	@Autowired
	private ProcessEngine processEngine; 		// 流程引擎对象

	@Autowired
	private RepositoryService repositoryService; // 管理流程定义 与流程定义和部署对象相关的Service

	@Autowired
	private RuntimeService runtimeService; 		// 与正在执行的流程实例和执行对象相关的Service(执行管理，包括启动、推进、删除流程实例等操作)

	@Autowired
	private TaskService taskService; 			// 任务管理 与正在执行的任务管理相关的Service

	@Autowired
	private HistoryService historyService; 		// 历史管理(执行完的数据的管理)
	
	/**
	 * 指派任务的代理人
	 * 
	 * @param Assignee 代理人
	 * @param taskId   任务ID
	 */
	protected void setAssignee(String taskId, String Assignee) {
		taskService.setAssignee(taskId, Assignee);
	}

	/**
	 * 设置流程变量(绑定任务)用Map形式
	 * 
	 * @param taskId 任务ID
	 * @param map
	 */
	protected void setVariablesByTaskIdAsMap(String taskId, Map<String, Object> map) {
		taskService.setVariablesLocal(taskId, map);
	}

	/**
	 * 获取流程变量
	 * 
	 * @param taskId 任务ID
	 * @param key    键
	 * @param map
	 */
	protected Object getVariablesByTaskIdAsMap(String taskId, String key) {
		return taskService.getVariable(taskId, key);
	}

	/**
	 * 设置流程变量(不绑定任务)
	 * 
	 * @param taskId 任务ID
	 * @param map
	 */
	protected void setVariablesByTaskId(String taskId, String key, String value) {
		taskService.setVariable(taskId, key, value);
	}

	/**
	 * 移除流程变量(从正在运行中)
	 * 
	 * @param PROC_INST_ID_ 流程实例ID
	 * @param map
	 */
	protected void removeVariablesByPROC_INST_ID_(String PROC_INST_ID_, String key) {
		runtimeService.removeVariable(PROC_INST_ID_, key);
	}

	/**
	 * 查询我的任务
	 * 
	 * @param USERNAME
	 * @return 返回任务列表
	 */
	protected List<Task> findMyPersonalTask(String USERNAME) {
		return taskService.createTaskQuery() 	// 创建查询对象
				.taskAssignee(USERNAME) 		// 指定办理人
				.list(); 						// 读出列表
	}

	/**
	 * 完成任务
	 * 
	 * @param taskId 任务ID
	 */
	protected void completeMyPersonalTask(String taskId) {
		taskService.complete(taskId);
	}

	/**
	 * 作废流程
	 * 
	 * @param processId 流程实例ID
	 * @param reason    作废原因
	 * @throws Exception
	 */
	protected void deleteProcessInstance(String processId, String reason) throws Exception {
		runtimeService.deleteProcessInstance(processId, reason);
	}

	/**
	 * 删除历史流程
	 * 
	 * @param PROC_INST_ID_ 流程实例ID
	 * @throws Exception
	 */
	protected void deleteHiProcessInstance(String PROC_INST_ID_) throws Exception {
		historyService.deleteHistoricProcessInstance(PROC_INST_ID_);
	}
	
	/**
	 * 生成当前任务节点流程图片PNG
	 * 
	 * @param PROC_INST_ID_ 	流程实例ID
	 * @param FILENAME     		图片名称
	 * @throws IOException
	 */
	protected void createXmlAndPngAtNowTask(String PROC_INST_ID_, String FILENAME, String type) throws IOException {
		DelFileUtil.delFolder(PathUtil.getProjectpath() + "uploadFiles/activitiFile"); // 生成先清空之前生成的文件
		InputStream in = genProcessDiagram(PROC_INST_ID_,type);
		FileUpload.copyFile(in, PathUtil.getProjectpath() + Const.FILEACTIVITI, FILENAME);// 把文件上传到文件目录里面
		in.close();
	}

	/**
	 * 获取当前任务流程图片的输入流
	 * @param PROC_INST_ID_ 流程实例ID
	 * @return
	 */
	public InputStream genProcessDiagram(String PROC_INST_ID_, String type){
		/* 获得活动的节点  */
        String processDefinitionId = "";
        if (this.isFinished(PROC_INST_ID_)) {	// 如果流程已经结束，则得到结束节点
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(PROC_INST_ID_).singleResult();
            processDefinitionId=pi.getProcessDefinitionId();
        } else {							// 如果流程没有结束，则取当前活动节点
        	/* 根据流程实例ID获得当前处于活动状态的ActivityId合集  */
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(PROC_INST_ID_).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }
        /* 获得活动的节点对象  */
        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(PROC_INST_ID_).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> highLightedActivitis = new ArrayList<String>();				//节点对象ID
        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);	//获取流程图
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        /* 获得活动的连线对象  */
        List<String> flows = new ArrayList<>();										//连线ID
        if("RU".equals(type)) {
        	List<ActivityInstance> highLightedFlowInstances = runtimeService.createActivityInstanceQuery().activityType(BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW).processInstanceId(PROC_INST_ID_).list();
            for(ActivityInstance ai : highLightedFlowInstances) {
            	flows.add(ai.getActivityId());	
            }
        }else {
        	List<HistoricActivityInstance> highLightedFlowInstances = historyService.createHistoricActivityInstanceQuery().activityType(BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW).processInstanceId(PROC_INST_ID_).list();
        	for(HistoricActivityInstance ha : highLightedFlowInstances) {
            	flows.add(ha.getActivityId());	
            }
        }
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true);
        return in;
	 }
	
	/**流程是否完成功能
	 * @param processInstanceId
	 * @return
	 */
	public boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
    }

	/**
	 * 获取发起人
	 * @param PROC_INST_ID_ 流程实例ID
	 * @return
	 */
	protected String getInitiator(String PROC_INST_ID_) {
		HistoricProcessInstance hi = historyService.createHistoricProcessInstanceQuery().processInstanceId(PROC_INST_ID_).singleResult();	//获取历史流程实例
		return hi.getStartUserId();
	}
	
	/**节点跳转
	 * @param PROC_INST_ID_ 流程实例ID
	 * @param nodeId
	 * @param toNodeId
	 */
	protected void moveActivityIdTo(String PROC_INST_ID_, String nodeId, String toNodeId){
		runtimeService.createChangeActivityStateBuilder().processInstanceId(PROC_INST_ID_).moveActivityIdTo(nodeId, toNodeId).changeState();
	}

}