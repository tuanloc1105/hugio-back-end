package vn.com.hugio.common.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.filter.AuthenResponse;

@Service
public class CurrentUserService {

    public String getUsername() {
        var authInfo = (AuthenResponse) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return authInfo.getUsername();
    }

}
