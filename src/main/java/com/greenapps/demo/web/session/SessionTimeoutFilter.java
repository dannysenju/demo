/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.session;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danny
 */
@WebFilter("/views/*")
public class SessionTimeoutFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession(false);
        String requestPath = httpServletRequest.getServletPath();

        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            if (session == null) {
                httpServletRequest.getSession(true);
                String url = httpServletResponse.encodeRedirectURL(httpServletRequest.getContextPath() + "/views/logOn.xhtml");
                httpServletResponse.sendRedirect(url);
                return;
            } else {
                if (session.getAttribute("username") != null && requestPath.contains("views/logOn.xhtml")) {
//                    if (loginType.getLoginType().equalsIgnoreCase("login")) { --> en caso de super usuario
                    String url = httpServletResponse.encodeRedirectURL(httpServletRequest.getContextPath() + "/views/dashboard.xhtml");
                    httpServletResponse.sendRedirect(url);
//                    } else {
//                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/jsf/login/menuAdmin.jsf");
//                    }
                    return;
                } else if (session.getAttribute("username") == null && (!requestPath.contains("views/login/create.xhtml")) && (!requestPath.contains("views/login/activate.xhtml"))
                        && (!requestPath.contains("views/logOn.xhtml") && !requestPath.contains("views/login/changePass.xhtml"))) {
                    String url = httpServletResponse.encodeRedirectURL(httpServletRequest.getContextPath() + "/views/logOn.xhtml");
                    httpServletResponse.sendRedirect(url);
                    return;
                } 
                
                else if (session.getAttribute("username") != null && (requestPath.contains("views/admin/admin.xhtml") || requestPath.contains("views/sena/programa.xhtml"))
                        && !session.getAttribute("rol").equals("ADMIN")) {
                    String url = httpServletResponse.encodeRedirectURL(httpServletRequest.getContextPath() + "/views/error/not-access.xhtml");
                    httpServletResponse.sendRedirect(url);
                    return;
                }
                
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
