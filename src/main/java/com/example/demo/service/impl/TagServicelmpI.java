package com.example.demo.service.impl;

import com.example.demo.entity.Tag;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.TagService;
import com.example.demo.utils.RedisOperator;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "tags")
public class TagServicelmpI implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RedisOperator redis;

    @Transactional
    @Override
    @CachePut(value = "7d",key = "'tags:'+#p0.id",unless = "#tag eq null")
    public Tag saveTag(Tag tag) {
        redis.del("tags:list");
        return tagRepository.save(tag);
    }

    @Override
    @Cacheable(value = "7d",key = "'tags'+#id.toString()")
    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).get();
    }

    @Override
    @CacheEvict(value = "7d",key = "'tags'+#id.toString()",condition = "#result eq true ")
    public Boolean removeTagById(Integer id) {
        tagRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<Tag> getListTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag listTagsByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    @Cacheable(value = "7d",key = "'tags:hotTagist'")
    public List<Tag> listTags() {
        return (List<Tag>) tagRepository.findAll();
    }
}
