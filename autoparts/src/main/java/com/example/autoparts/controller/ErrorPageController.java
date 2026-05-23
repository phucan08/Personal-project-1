package com.example.autoparts.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                return "error/404";
            }

            if (statusCode == 403) {
                return "error/403";
            }

            if (statusCode == 500) {
                return "error/500";
            }
        }

        return "error/500";
    }

    @GetMapping("/error/403")
    public String accessDenied() {
        return "error/403";
    }
}