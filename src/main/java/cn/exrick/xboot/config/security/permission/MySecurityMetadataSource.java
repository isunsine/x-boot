<<<<<<< HEAD
package cn.exrick.xboot.config.security.permission;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.exrick.xboot.common.constant.RedisKeyConstant;
import cn.exrick.xboot.common.utils.RedisUtils;
import cn.exrick.xboot.entity.Permission;
import cn.exrick.xboot.service.PermissionService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 权限资源管理器 为权限决断器提供支持
 * 
 * @author Exrickx
 */
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private PermissionService permissionService;

	private Map<String, Collection<ConfigAttribute>> map = null;
	
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 加载权限表中所有权限
	 */
	public void loadResourceDefine() {

		map = Maps.newConcurrentMap();//.newHashMapWithExpectedSize(16);
		Collection<ConfigAttribute> configAttributes;
		ConfigAttribute cfg;
		List<Permission> permissions = redisUtils.get(RedisKeyConstant.CACHE_KEY, List.class);
		if(CollectionUtil.isEmpty(permissions)) {
			permissions = permissionService.getAll();
			redisUtils.set(RedisKeyConstant.CACHE_KEY, permissions, 60 * 60 * 24);
		}
		for (Permission permission : permissions) {
			configAttributes = Lists.newArrayList();
			cfg = new SecurityConfig(permission.getTitle());
			// 作为MyAccessDecisionManager类的decide的第三个参数
			configAttributes.add(cfg);
			// 用权限的path作为map的key，用ConfigAttribute的集合作为value
			map.put(permission.getPath(), configAttributes);
		}

	}

	/**
	 * 判定用户请求的url是否在权限表中 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限 如果不在权限表中则放行
	 * 
	 * @param o
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

		if (map == null) {
			loadResourceDefine();
		}
		// Object中包含用户请求request
		String url = ((FilterInvocation) o).getRequestUrl();
		PathMatcher pathMatcher = new AntPathMatcher();
		for (String resURL : map.keySet()) {
			if (StrUtil.isNotBlank(resURL) && pathMatcher.match(resURL, url)) {
				return map.get(resURL);
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
=======
package cn.exrick.xboot.config.security.permission;

import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.entity.Permission;
import cn.exrick.xboot.service.PermissionService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * 权限资源管理器
 * 为权限决断器提供支持
 * @author Exrickx
 */
@Slf4j
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PermissionService permissionService;

    private Map<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有操作请求权限
     */
    public void loadResourceDefine(){

        map = new HashMap<>(16);
        Collection<ConfigAttribute> configAttributes;
        ConfigAttribute cfg;
        // 获取启用的权限操作请求
        List<Permission> permissions = permissionService.findByTypeAndStatusOrderBySortOrder(CommonConstant.PERMISSION_OPERATION, CommonConstant.STATUS_NORMAL);
        for(Permission permission : permissions) {
            if(StrUtil.isNotBlank(permission.getTitle())&&StrUtil.isNotBlank(permission.getPath())){
                configAttributes = new ArrayList<>();
                cfg = new SecurityConfig(permission.getTitle());
                //作为MyAccessDecisionManager类的decide的第三个参数
                configAttributes.add(cfg);
                //用权限的path作为map的key，用ConfigAttribute的集合作为value
                map.put(permission.getPath(), configAttributes);
            }
        }
    }

    /**
     * 判定用户请求的url是否在权限表中
     * 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限
     * 如果不在权限表中则放行
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        if(map == null){
            loadResourceDefine();
        }
        //Object中包含用户请求request
        String url = ((FilterInvocation) o).getRequestUrl();
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String resURL = iterator.next();
            if (StrUtil.isNotBlank(resURL)&&pathMatcher.match(resURL,url)) {
                return map.get(resURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
>>>>>>> remotes/upstream/master
