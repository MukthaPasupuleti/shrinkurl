package com.mukku.shrinkurl.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.mukku.shrinkurl.config.UrlUtil;
import com.mukku.shrinkurl.dto.FullUrl;
import com.mukku.shrinkurl.dto.ShortUrl;
import com.mukku.shrinkurl.dto.UserDto;
import com.mukku.shrinkurl.dto.UrlDto;
import com.mukku.shrinkurl.entity.User;
import com.mukku.shrinkurl.service.UrlService;
import com.mukku.shrinkurl.service.UserService;


@Controller
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private UserService userService;
    protected final UrlService urlService;
    
    @Autowired
    public AuthController(UserService userService,UrlService urlService) {
        this.userService = userService;
        this.urlService = urlService;
    }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }


    @GetMapping("/profile")
    public String listRegisteredUrl(Model model,UserDto user)
    {
        List<UrlDto> url = urlService.findAllUrl(user.getEmail());
        model.addAttribute("profile", url);
        return "profile";
    }

    @PostMapping("/shorten")
    public String saveUrl(@RequestBody FullUrl fullUrl, HttpServletRequest request) {

        String baseUrl = null;

        try {
            baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
            logger.error("Malformed request url");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request url is invalid", e);
        }

        ShortUrl shortUrl = urlService.getShortUrl(fullUrl);
        shortUrl.setShortUrl(baseUrl + shortUrl.getShortUrl());

        logger.debug(String.format("ShortUrl for FullUrl %s is %s", fullUrl.getFullUrl(), shortUrl.getShortUrl()));
        
        return "index";
    }

    @GetMapping("/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            FullUrl fullUrl = urlService.getFullUrl(shortenString);

            logger.info(String.format("Redirecting to %s", fullUrl.getFullUrl()));

            response.sendRedirect(fullUrl.getFullUrl());
        } catch (NoSuchElementException e) {
            logger.error(String.format("No URL found for %s in the db", shortenString));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            logger.error("Could not redirect to the full url");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }
}
