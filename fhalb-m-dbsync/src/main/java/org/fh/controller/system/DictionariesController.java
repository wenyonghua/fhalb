package org.fh.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.fh.controller.base.BaseController;
import org.fh.entity.PageData;
import org.fh.service.system.DictionariesService;
import org.fh.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：数据字典处理类
 * 作者：FH Admin Q 3-13596-790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/dictionaries")
public class DictionariesController extends BaseController {
	
	@Autowired
	private DictionariesService dictionariesService;
	
	/**新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(@RequestBody PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		if(Tools.checkTokenKey("dictionariesAdd", pd.getString("tokenKey"))) {	//检验tokenKey
			dictionariesService.save(pd);
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestBody PageData pd) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		if(Tools.checkTokenKey("dictionariesEdit", pd.getString("tokenKey"))) {	//检验tokenKey
			dictionariesService.edit(pd);
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**
	 * 删除
	 * @param DICTIONARIES_ID
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestBody PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		if(Tools.checkTokenKey("dictionariesDel", pd.getString("tokenKey"))) {	//检验tokenKey
			dictionariesService.delete(pd);			//执行删除
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}

}
