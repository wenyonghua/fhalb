package org.fh.controller.act.ruprocdef;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fh.controller.act.AcBusinessController;
import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.service.act.HiprocdefService;
import org.fh.service.act.RuprocdefService;
import org.fh.util.DateUtil;
import org.fh.util.Jurisdiction;
import org.fh.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：正在运行的流程
 * 作者：FH Admin Q31-359-6790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping(value="/ruprocdef")
public class RuprocdefController extends AcBusinessController {
	
	@Autowired
	private RuprocdefService ruprocdefService;
	
	@Autowired
	private HiprocdefService hiprocdefService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("ruprocdef:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("keywords", KEYWORDS.trim());
		String STRARTTIME = pd.getString("STRARTTIME");					//开始时间
		String ENDTIME = pd.getString("ENDTIME");						//结束时间
		if(Tools.notEmpty(STRARTTIME))pd.put("lastStart", STRARTTIME+" 00:00:00");
		if(Tools.notEmpty(ENDTIME))pd.put("lastEnd", ENDTIME+" 00:00:00");
		page.setPd(pd);
		List<PageData>	varList = ruprocdefService.list(page);			//列出Ruprocdef列表
		for(int i=0;i<varList.size();i++){
			varList.get(i).put("INITATOR", getInitiator(varList.get(i).getString("PROC_INST_ID_")));//流程申请人
		}
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**委派
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delegate")
	@RequiresPermissions("Delegate")
	@ResponseBody
	public Object delegate() throws Exception{
		Map<String,Object> zmap = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		String OPINION = " 任务由 ["+Jurisdiction.getUsername()+"] 委派  " + ",fh,"+pd.getString("REASON");//缘由
		map.put("审批结果", OPINION);				//审批结果中记录委派
		setVariablesByTaskIdAsMap(pd.getString("ID_"),map);						//设置流程变量
		setAssignee(pd.getString("ID_"),pd.getString("ASSIGNEE_"));
		zmap.put("ASSIGNEE_",pd.getString("ASSIGNEE_"));						//用于给待办人发送新任务消息
		zmap.put("result", errInfo);				//返回结果
		return zmap;
	}
	
	/**激活or挂起任务
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/onoffTask")
	@RequiresPermissions("ruprocdef:edit")
	@ResponseBody
	public Object onoffTask()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		ruprocdefService.onoffTask(pd);
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**作废流程
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("Abolish")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String reason = "【作废】"+Jurisdiction.getName()+"："+pd.getString("reason");	//作废原因
		/**任务结束时发站内信通知审批结束*/
		List<PageData>	hivarList = hiprocdefService.hivarList(pd);			//列出历史流程变量列表
		for(int i=0;i<hivarList.size();i++){
			if("USERNAME".equals(hivarList.get(i).getString("NAME_"))){
				sendSms(hivarList.get(i).getString("TEXT_"));
				break;
			}
		}
		deleteProcessInstance(pd.getString("PROC_INST_ID_"),reason);		//作废流程
		map.put("result", errInfo);
		return map;
	}
	
	/**节点跳转
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/move")
	@ResponseBody
	public Object move()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		Map<String,Object> rmap = new LinkedHashMap<String, Object>();
		String OPINION = " 任务由 ["+Jurisdiction.getUsername()+"] 跳转到此  " + ",fh,"+pd.getString("REASON");//缘由
		rmap.put("审批结果", OPINION);					//审批结果中记录跳转
		setVariablesByTaskIdAsMap(pd.getString("ID_"),rmap);						//设置流程变量
		moveActivityIdTo(pd.getString("PROC_INST_ID_"),pd.getString("nodeId"),pd.getString("toNodeId"));
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**获取流程发起人
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getInitiator")
	@ResponseBody
	public Object getInitiator()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		map.put("INITATOR", getInitiator(pd.getString("PROC_INST_ID_")));//获取流程发起人
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
	/**发站内信通知审批结束
	 * @param USERNAME
	 * @throws Exception
	 */
	public void sendSms(String USERNAME) throws Exception{
		PageData pd = new PageData();
		pd.put("SANME_ID", this.get32UUID());			//ID
		pd.put("SEND_TIME", DateUtil.getTime());		//发送时间
		pd.put("FHSMS_ID", this.get32UUID());			//主键
		pd.put("TYPE", "1");							//类型1：收信
		pd.put("FROM_USERNAME", USERNAME);				//收信人
		pd.put("TO_USERNAME", "系统消息");
		pd.put("CONTENT", "您申请的任务已经被作废,请到已办任务列表查看");
		pd.put("STATUS", "2");
		ruprocdefService.sendSms(pd);
	}

}
