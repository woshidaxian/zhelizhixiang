package com.example.demo.service;

import com.example.demo.VO.UserSessionVO;
import com.example.demo.entity.Bind;
import com.example.demo.entity.BindType;
import com.example.demo.entity.User;

import javax.xml.ws.BindingType;
import java.util.List;

public interface BindService {
    /**
     * 根据id获取绑定
     * @param id
     * @return
     */
    Bind getBindById(Long id);

    /**
     * 添加绑定
     * @param bind
     */
    void saveBind(Bind bind);

    /**
     * 解除绑定
     * @param bind
     */
    void removeBind(Bind bind);

    /**
     * 根据凭据获得用户sessionVO
     * @param bindType
     * @param identifier
     * @return
     */
    UserSessionVO getUserSessionVO(BindType bindType, String identifier);

    /**
     * 获得绑定列表
     * @param user
     * @return
     */
    List<Bind> listBinds(User user);
}
