package org.fh.service.system.impl;

import org.fh.service.system.MenuService;
import org.fh.util.RedisUtil;

import java.util.List;

import org.fh.entity.PageData;
import org.fh.entity.system.Menu;
import org.fh.mapper.dsno1.system.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：菜单服务接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Service
@Transactional //开启事物
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**新增菜单
	 * @param menu
	 * @throws Exception
	 */
	@CacheEvict(value="menucache", allEntries=true)
	public void addMenu(Menu menu) throws Exception{
		this.cleanRedis();
		menuMapper.addMenu(menu);
	}
	
	/**保存修改菜单
	 * @param menu
	 * @throws Exception
	 */
	@CacheEvict(value="menucache", allEntries=true)
	public void edit(Menu menu) throws Exception{
		this.cleanRedis();
		menuMapper.edit(menu);
	}
	
	/**
	 * 通过菜单ID获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception {
		return menuMapper.getMenuById(pd);
	}

	/**获取最大的菜单ID
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMaxId(PageData pd) throws Exception{
		return menuMapper.findMaxId(pd);
	}
	
	/**
	 * 通过ID获取其子一级菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@Cacheable(key="'menu-'+#parentId",value="menucache")
	public List<Menu> listSubMenuByParentId(String parentId) throws Exception {
		return menuMapper.listSubMenuByParentId(parentId);
	}
	
	/**
	 * 获取所有菜单
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listAllMenu(String MENU_ID) throws Exception {
		RedisUtil redis = new RedisUtil(redisTemplate);
		List<Menu> menuList;
		try {
			if(redis.hasKey("menuList")) {
				Object value = redis.get("menuList");
				if(null != value) {	//链接上了，一切OK"
					menuList = (List<Menu>)value;
					return menuList;
				}else {				//链接上了，有key, 但值为null
					menuList = listAllMenuLoop(MENU_ID);
					redis.set("menuList", menuList);
					return menuList;
				}
			}else {					//链接上了或者没连上，没有key
				menuList = listAllMenuLoop(MENU_ID);
				redis.set("menuList", menuList);
				return menuList;
			}
		} catch (Exception e) {		//出异常了
			menuList = listAllMenuLoop(MENU_ID);
			return menuList;
		}
	}

	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuLoop(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID);
		for(Menu menu : menuList){
			menu.setMENU_URL("menu_edit.html?MENU_ID="+menu.getMENU_ID());
			menu.setSubMenu(this.listAllMenuLoop(menu.getMENU_ID()));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}
	
	/**
	 * 获取所有菜单
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listAllMenuQx(String MENU_ID) throws Exception {
		RedisUtil redis = new RedisUtil(redisTemplate);
		List<Menu> menuList;
		try {
			if(redis.hasKey("menuListQx")) {
				Object value = redis.get("menuListQx");
				if(null != value) {	//链接上了，一切OK"
					menuList = (List<Menu>)value;
					return menuList;
				}else {				//链接上了，有key, 但值为null
					menuList = listAllMenuQxLoop(MENU_ID);
					redis.set("menuListQx", menuList);
					return menuList;
				}
			}else {					//链接上了或者没连上，没有key
				menuList = listAllMenuQxLoop(MENU_ID);
				redis.set("menuListQx", menuList);
				return menuList;
			}
		} catch (Exception e) {		//出异常了
			menuList = listAllMenuQxLoop(MENU_ID);
			return menuList;
		}
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单列表)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQxLoop(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID);
		for(Menu menu : menuList){
			menu.setSubMenu(this.listAllMenuQxLoop(menu.getMENU_ID()));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}

	/**删除菜单
	 * @param MENU_ID
	 * @throws Exception
	 */
	@CacheEvict(value="menucache", allEntries=true)
	public void deleteMenuById(String MENU_ID) throws Exception{
		this.cleanRedis();
		menuMapper.deleteMenuById(MENU_ID);
	}
	
	/**保存菜单图标
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(value="menucache", allEntries=true)
	public void editicon(PageData pd) throws Exception{
		this.cleanRedis();
		menuMapper.editicon(pd);
	}
	
	/**
	 * 清空redis菜单缓存
	 */
	public void cleanRedis() {
		RedisUtil redis = new RedisUtil(redisTemplate);
		if(redis.hasKey("menuList")) {
			redis.del("menuList");
			if(redis.hasKey("menuListQx")) {
				redis.del("menuListQx");
			}
		}
	}
	
}
