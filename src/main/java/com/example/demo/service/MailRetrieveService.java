package com.example.demo.service;

import com.example.demo.VO.ResultVO;
import com.example.demo.entity.MailRetrieve;

public interface MailRetrieveService {
    /**
     * 生成验证邮件的URL
     * @param basePath
     * @param account
     * @return
     */
    String getEmailUrl(String basePath,String account);

    /**
     * 根据account名称获取记录
     * @param account
     * @return
     */
    MailRetrieve getMailRetrieveByAccount(String account);

    /**
     * 邮件找回密码URL校验
     * @param sid
     * @param account
     * @return
     */
    ResultVO verifyMailUrl(String sid, String account);

    /**
     * 删除邮件
     * @param id
     */
    void removeMailRetrieve(Long id);
}
