package com.example.shoppingmall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    @Autowired
    WebApplicationContext servletAC; // Servlet AC

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        // 원래는 request.getServletContext()지만, 컨트롤러는 HttpServlet을 상속받지 않아서 아래와 같이 해야함.
        ServletContext sc = request.getSession().getServletContext(); // ApplicationContextFacade
        WebApplicationContext rootAC = WebApplicationContextUtils.getWebApplicationContext(sc); // Root AC

        System.out.println("webApplicationContext = " + rootAC); // Root ApplicationContext
        System.out.println("servletAC = " + servletAC); // Servlet ApplicationContext

        System.out.println("rootAC.getBeanDefinitionNames() = " + Arrays.toString(rootAC.getBeanDefinitionNames())); // Bean 이름 목록(DAO, Service, Component)
        System.out.println("servletAC.getBeanDefinitionNames() = " + Arrays.toString(servletAC.getBeanDefinitionNames())); // Bean 이름 목럭(Controller, HandlerMapping)

        System.out.println("rootAC.getBeanDefinitionCount() = " + rootAC.getBeanDefinitionCount()); // 등록된 Bean 개수
        System.out.println("servletAC.getBeanDefinitionCount() = " + servletAC.getBeanDefinitionCount()); // 등록된 Bean 개수

        System.out.println("servletAC.getParent()==rootAC = " + (servletAC.getParent() == rootAC)); // servletAC.getParent()==rootAC = true

        return "home";
    }
}