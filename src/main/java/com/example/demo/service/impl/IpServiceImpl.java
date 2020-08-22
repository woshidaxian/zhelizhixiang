package com.example.demo.service.impl;

import com.example.demo.service.IpService;
import com.example.demo.utils.IPSeekerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class IpServiceImpl implements IpService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static IPSeekerUtil ipSeeker;
    @Value("${qqwry.dat.path}")
    private String filepath;
    @Override
    public String getIpArea(String ip) {
        String result = "";
        if (ipSeeker == null) {
            try {
                ipSeeker = new IPSeekerUtil(new File(filepath));
                result = ipSeeker.getAddress(ip);
            } catch (Exception e) {
                logger.error("IP地址库实例化出错", e);
            }
        }
        return result;
    }
}
