package com.example.demo.service;

import com.example.demo.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    /**
     * 添加或更新标签
     */

    Tag saveTag(Tag tag);

    /**
     * 根据Id获取标签
     */
    Tag getTagById(Integer id);

    /**
     * 根据Id删除标签
     */

    Boolean removeTagById(Integer id);

    /**
     * 获取标签列表
     */
    Page<Tag> getListTags(Pageable pageable);

    /**
     * 根据标签名获取标签
     * @param name
     * @return
     */
    Tag listTagsByName(String name);

    /**
     * 获得标签列表,不分页
     */

    List<Tag> listTags();
}
