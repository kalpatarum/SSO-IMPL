package com.idp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LogoutController {

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(false).invalidate(); // Invalidate user session
        String redirectUri = request.getParameter("post_logout_redirect_uri");
        if (redirectUri != null) {
            response.sendRedirect(redirectUri);
        } else {
            response.getWriter().write("Logged out");
        }
    }
}
