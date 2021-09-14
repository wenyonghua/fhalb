package org.fh.controller.form;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.fh.controller.base.BaseController;
import org.fh.entity.Page;
import org.fh.util.Const;
import org.fh.util.DateUtil;
import org.fh.util.DelFileUtil;
import org.fh.util.FileDownload;
import org.fh.util.FileUpload;
import org.fh.util.Jurisdiction;
import org.fh.util.PathUtil;
import org.fh.util.Tools;
import org.fh.entity.PageData;
import org.fh.service.form.FormDataService;
import org.fh.service.form.HangFormService;

/** 
 * 说明：表单数据
 * 作者：FH Admin QQ313596790
 * 官网：www.fhadmin.cn
 */
@Controller
@RequestMapping("/formdata")
public class FormDataController extends BaseController {
	
	@Autowired
	private FormDataService formdataService;
	@Autowired
	private HangFormService hangformService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("formdata:add")
	@ResponseBody
	public Object add() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FORMDATA_ID = this.get32UUID();
		pd.put("FORMDATA_ID", FORMDATA_ID);	//主键
		pd.put("CTIME", DateUtil.date2Str(new Date()));	//创建时间
		pd.put("ETIME", DateUtil.date2Str(new Date()));	//修改时间
		pd.put("USERNAME", Jurisdiction.getUsername());	//创建者
		pd.put("EDITNAME", Jurisdiction.getUsername());	//修改者
		formdataService.save(pd);
		map.put("result", errInfo);
		map.put("FORMDATA_ID", FORMDATA_ID);
		return map;
	}
	
	/**上传附件
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/upfile")
	@ResponseBody
	public Object upfile(
			@RequestParam(value="FFILE",required=false) MultipartFile file,
			@RequestParam(value="FORMDATA_ID",required=false) String FORMDATA_ID
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;	//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
			pd.put("FILEPATH", Const.FILEPATHFILE + ffile + "/" + fileName);			//文件目录
			pd.put("FORMDATA_ID", FORMDATA_ID); //主键
			pd.put("fmsg", "ffile");			//修改类型				
			formdataService.edit(pd);			//修改数据库上传文件目录
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("formdata:del")
	@ResponseBody
	public Object delete(@RequestParam String FORMDATA_ID,@RequestParam String FILEPATH) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FORMDATA_ID", FORMDATA_ID);
		if(Tools.notEmpty(FILEPATH)){
			DelFileUtil.delFolder(PathUtil.getProjectpath()+ FILEPATH.trim()); //删除文件
		}
		hangformService.delete(pd);	//删除挂靠记录
		formdataService.delete(pd);	//删除表单数据
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除附件
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delFile")
	@ResponseBody
	public Object delFile() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String FILEPATH = pd.getString("FILEPATH");	
		if(Tools.notEmpty(FILEPATH.trim())){								//文件路径
			DelFileUtil.delFolder(PathUtil.getProjectpath() + FILEPATH); 	//删除硬盘中的文件
		}
		if(FILEPATH != null){
			pd.put("FILEPATH", "");				//文件目录
			pd.put("fmsg", "ffile");			//修改类型				
			formdataService.edit(pd);			//修改数据库上传文件目录
		}	
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改规则
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("formdata:edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		formdataService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**修改数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editData")
	@RequiresPermissions("formdata:edit")
	@ResponseBody
	public Object editData() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ETIME", DateUtil.date2Str(new Date()));	//修改时间
		pd.put("EDITNAME", Jurisdiction.getUsername());	//修改者
		formdataService.edit(pd);
		map.put("result", errInfo);
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("formdata:list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");				//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		pd.put("USERNAME", Jurisdiction.getUsername());		//当前用户
		page.setPd(pd);
		List<PageData>	varList = formdataService.list(page);	//列出FormData列表
		map.put("varList", varList);
		map.put("UNAME", Jurisdiction.getUsername());			//当前用户名
		map.put("page", page);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去修改页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("formdata:edit")
	@ResponseBody
	public Object goEdit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = formdataService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}
	
	 /**去查看页面获取数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goView")
	@RequiresPermissions("formdata:cha")
	@ResponseBody
	public Object goView() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = formdataService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);
		return map;
	}		
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@RequiresPermissions("formdata:del")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd = this.getPageData();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			hangformService.deleteAll(ArrayDATA_IDS); //批量删除挂靠记录
			formdataService.deleteAll(ArrayDATA_IDS); //批量上传只删除数据库数据，不删除有附件的文件，需要删除的话，手动去硬盘删除
			errInfo = "success";
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**下载
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/download")
	public void download(HttpServletResponse response)throws NofileException {
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = formdataService.findById(pd);
			String FILEPATH = pd.getString("FILEPATH");
			String fileName = pd.getString("TITLE");
			FileDownload.fileDownload(response, PathUtil.getProjectpath() + FILEPATH, fileName+FILEPATH.substring(58, FILEPATH.length()));
		} catch (Exception e) {
			throw new NofileException("=========要下载的文件已经没有了=========");
		}
	}
	
}


/*
 * 自定义异常类
 */
class NofileException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NofileException() {
	    super();
	  }

	  public NofileException(String message) {
	    super(message);
	  }

	  public NofileException(String message, Throwable cause) {
	    super(message, cause);
	  }

	  public NofileException(Throwable cause) {
	    super(cause);
	  }
	  
}
