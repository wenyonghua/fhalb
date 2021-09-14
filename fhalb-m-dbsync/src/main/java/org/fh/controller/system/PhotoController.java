package org.fh.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.fh.controller.base.BaseController;
import org.fh.entity.PageData;
import org.fh.service.system.PhotoService;
import org.fh.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：头像编辑处理类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/photo")
public class PhotoController extends BaseController {
	
	@Autowired
	private PhotoService photoService;
	
	/**新增
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object save(@RequestBody PageData pd) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		if(Tools.checkTokenKey("photoAdd", pd.getString("tokenKey"))) {	//检验tokenKey
			photoService.save(pd);
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
		if(Tools.checkTokenKey("photoEdit", pd.getString("tokenKey"))) {	//检验tokenKey
			photoService.edit(pd);
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}

}
