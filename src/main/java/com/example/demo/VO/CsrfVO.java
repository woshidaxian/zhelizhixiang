package com.example.demo.VO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.Serializable;
@Getter
@Setter
public class CsrfVO implements Serializable {
    public String token;

    public String headerName;

    public CsrfVO(CsrfToken token){
        this.setHeaderName(token.getHeaderName());
        this.setToken(token.getToken());
    }
}
