package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("dgbAuthenticationSuccessHandler")
public class DgbAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

}
