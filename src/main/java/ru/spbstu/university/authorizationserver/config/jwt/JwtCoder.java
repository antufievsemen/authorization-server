package ru.spbstu.university.authorizationserver.config.jwt;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class JwtCoder extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

    }
}
