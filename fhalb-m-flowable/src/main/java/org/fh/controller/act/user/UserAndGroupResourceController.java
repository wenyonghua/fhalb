package org.fh.controller.act.user;

import org.apache.commons.lang3.StringUtils;
import org.fh.entity.PageData;
import org.fh.service.act.UserRoleService;
import org.fh.util.Tools;
import org.flowable.engine.ManagementService;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：流程编辑器分配用户
 * 作者：FH Admin fh 3 1 3 5 9 6 79 0qq(青苔)
 * 官网：www.fhadmin.org
 */
@RestController
@RequestMapping("app")
public class UserAndGroupResourceController {

	@Autowired
	protected ManagementService managementService;
	@Autowired
	protected IdmIdentityService idmIdentityService;
	@Autowired
	protected UserRoleService userRoleService;

	@RequestMapping(value = "/rest/editor-groups", method = RequestMethod.GET)
	public ResultListDataRepresentation getGroups(@RequestParam(required = false, value = "filter") String filter) {
		if (StringUtils.isNotBlank(filter)) {
			filter = filter.trim();
			List<GroupRepresentation> result = new ArrayList<>();
			PageData pd = new PageData();
			String KEYWORDS = filter;						//关键词检索条件
			if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
			try {
				pd.put("tokenKey", Tools.creatTokenKey("listRolesByMs"));
				List<PageData> roleList = userRoleService.listRolesByMs(pd);//列出所有角色
				for(PageData rpd : roleList) {
					GroupRepresentation rg = new GroupRepresentation();
					rg.setId(rpd.getString("RNUMBER"));
					rg.setName(rpd.getString("ROLE_NAME"));
					result.add(rg);
				}
			} catch (Exception e) {}		
			return new ResultListDataRepresentation(result);
		}
		return null;
	}

	@RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
	public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
		if (StringUtils.isNotBlank(filter)) {
			filter = filter.trim();
			List<UserRepresentation> userRepresentations = new ArrayList<>();
			PageData pd = new PageData();
			String KEYWORDS = filter;						//关键词检索条件
			if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
			try {
				pd.put("tokenKey", Tools.creatTokenKey("listUsersByMs"));
				List<PageData>	userList = userRoleService.listUsersByMs(pd);//列出用户列表
				for(PageData upd : userList) {
					UserRepresentation ur = new UserRepresentation();
					ur.setId(upd.getString("USERNAME"));
					ur.setFirstName(upd.getString("USERNAME"));
					ur.setFullName("");
					ur.setLastName("");
					userRepresentations.add(ur);
				}
			} catch (Exception e) {}	
			return new ResultListDataRepresentation(userRepresentations);
		}
		return null;
	}

}