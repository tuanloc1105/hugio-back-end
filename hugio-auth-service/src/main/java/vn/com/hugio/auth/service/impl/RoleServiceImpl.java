package vn.com.hugio.auth.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.com.hugio.auth.dto.RoleDto;
import vn.com.hugio.auth.entity.Role;
import vn.com.hugio.auth.entity.repository.RoleRepo;
import vn.com.hugio.auth.mapper.RoleMapper;
import vn.com.hugio.auth.service.RoleService;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.service.BaseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class RoleServiceImpl extends BaseService<Role, RoleRepo> implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepo repository, RoleMapper roleMapper) {
        super(repository);
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDto> findRoleByListName(List<String> roleName) {
        return this.repository.findByRoleNameIn(roleName).stream().map(this.roleMapper::roleDtoMapper).collect(Collectors.toList());
    }

    @Override
    public List<Role> findRoleByList(List<String> roleName) {
        return this.repository.findByRoleNameIn(roleName);
    }

    @Override
    public Page<Role> all(PagableRequest request) {
        return this.repository.findByActiveIsTrue(PageLink.create(request).toPageable());
    }

}
