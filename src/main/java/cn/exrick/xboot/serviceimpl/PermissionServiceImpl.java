package cn.exrick.xboot.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.exrick.xboot.dao.PermissionDao;
import cn.exrick.xboot.entity.Permission;
import cn.exrick.xboot.service.PermissionService;

/**
 * 权限接口实现
 * @author Exrick
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public PermissionDao getRepository() {
        return permissionDao;
    }

    @Override
    public List<Permission> findByLevelOrderBySortOrder(Integer level) {

        return permissionDao.findByLevelOrderBySortOrder(level);
    }

    @Override
    public List<Permission> findByParentIdOrderBySortOrder(String parentId) {

        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Permission> findByLevelAndTypeOrderBySortOrder(Integer level, Integer type) {

        return permissionDao.findByLevelAndTypeOrderBySortOrder(level, type);
    }

    @Override
    public List<Permission> findByTypeAndParentIdOrderBySortOrder(Integer type, String parentId) {

        return permissionDao.findByTypeAndParentIdOrderBySortOrder(type, parentId);
    }
}