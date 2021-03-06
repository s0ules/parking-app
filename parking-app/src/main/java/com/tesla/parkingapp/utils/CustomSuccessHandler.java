package com.tesla.parkingapp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.UserService;

@Component
@SessionAttributes("user")
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	

	@Autowired
	private UserService userService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
    	
    	User user = userService.findUserByEmail(authentication.getName());
    	request.getSession().setAttribute("user", user);
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("Can't redirect");
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
    	
        String url = "";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        if (isAdmin(roles)) {
            url = "/admin";
        } else if (isUser(roles)) {
            url = "/user";
        } else if (isFirma(roles)){
        	url = "/admin";
        }
        	else {
            url = "/accessDenied";
        }

        return url;
    }

    private boolean isUser(List<String> roles) {
        if (roles.contains("SITE_USER")) {
            return true;
        }
        return false;
    }

    private boolean isAdmin(List<String> roles) {
        if (roles.contains("ADMIN_USER")) {
            return true;
        }
        return false;
    }

    private boolean isFirma(List<String> roles) {
        if (roles.contains("FIRMA_USER")) {
            return true;
        }
        return false;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}