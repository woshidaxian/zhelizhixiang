package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.controller.common.BaseController;
import com.example.demo.entity.Slide;
import com.example.demo.entity.User;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.repository.SlideRepository;
import com.example.demo.service.UserService;
import com.example.demo.utils.BCryptUtil;
import com.example.demo.utils.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.*;

@Controller
public class AdminController extends BaseController {
    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private UserService userService;


}
