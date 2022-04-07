//package com.openclassrooms.payMyBuddy.controller;
//
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class CustomErrorController implements ErrorController {
//
//
//    @GetMapping("/error")
//    public ModelAndView handleError(HttpServletRequest request) {
//
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
//        Map<String, Object> model = new HashMap<>();
//        model.put("status", status);
//        model.put("message", message);
//        return new ModelAndView("error",model);
//    }
//
//}
