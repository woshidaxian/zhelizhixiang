package com.example.demo.service.impl;

import com.example.demo.VO.ResultVO;
import com.example.demo.entity.MailRetrieve;
import com.example.demo.repository.MailRetrieveRepository;
import com.example.demo.service.MailRetrieveService;
import com.example.demo.utils.MD5Util;
import com.example.demo.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class MailRetrieveServiceImpl implements MailRetrieveService {

    @Autowired
    private MailRetrieveRepository mailRetrieveRepository;
    @Override
    public String getEmailUrl(String basePath, String account) {
        //生成邮件URL唯一地址
        String key = RandomUtil.getRandom(6) + "";
        long outtimes = System.currentTimeMillis() + 30 * 60 * 1000;
        String sid = account + "&" + key + "&" + outtimes;
        MailRetrieve mailRetrieve = new MailRetrieve(account, MD5Util.encode(sid),outtimes);
        MailRetrieve findMailRetrieve = mailRetrieveRepository.findByAccount(account);
        if (findMailRetrieve != null){
            mailRetrieveRepository.delete(findMailRetrieve);
        }
        try {
            mailRetrieveRepository.save(mailRetrieve);
        }catch (Exception e){
            e.printStackTrace();
        }
        String url = basePath + "?sid=" + MD5Util.encode(sid) + "&username" + account;
        return url;
    }

    @Override
    public MailRetrieve getMailRetrieveByAccount(String account) {
        return mailRetrieveRepository.findByAccount(account);
    }

    @Override
    public ResultVO verifyMailUrl(String sid, String account) {
        ResultVO resultVO = new ResultVO();
        MailRetrieve mailRetrieve = mailRetrieveRepository.findByAccount(account);
        if (mailRetrieve != null){
            long outTime = mailRetrieve.getOutTime();
            long nowTime = System.currentTimeMillis();
            if (outTime < nowTime){
                resultVO.setCode(1);
                resultVO.setMsg("邮件已经过期!");
            }else if ("".equals(sid)){
                resultVO.setCode(1);
                resultVO.setMsg("sid不完整!");
            }else if (!sid.equals(mailRetrieve.getSid())){
                resultVO.setCode(1);
                resultVO.setMsg("sid错误！");
            }else {
                resultVO.setCode(0);
                resultVO.setMsg("验证成功！");
            }
        }else {
            //account 对应的用户不存在
            resultVO.setCode(1);
            resultVO.setMsg("链接无效！");
        }
        return resultVO;
    }

    @Override
    public void removeMailRetrieve(Long id) {
        mailRetrieveRepository.deleteById(id);
    }
}
