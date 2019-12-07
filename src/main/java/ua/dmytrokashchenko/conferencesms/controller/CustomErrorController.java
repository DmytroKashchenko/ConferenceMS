package ua.dmytrokashchenko.conferencesms.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            int code = Integer.parseInt(statusCode.toString());
            if (code == HttpStatus.NOT_FOUND.value()) {
                return "error/error-404";
            } else if (code == HttpStatus.FORBIDDEN.value()) {
                return "error/error-403";
            } else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error-500";
            }
        }
        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
