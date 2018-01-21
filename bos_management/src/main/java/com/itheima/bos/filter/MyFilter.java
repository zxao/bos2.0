package com.itheima.bos.filter;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyFilter extends StrutsPrepareAndExecuteFilter{

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if(request.getRequestURI().contains("/services/")){
            chain.doFilter(request,res);
        }else{
            super.doFilter(request, res, chain);
        }
    }
}
