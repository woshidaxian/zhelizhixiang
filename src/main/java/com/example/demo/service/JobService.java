package com.example.demo.service;

import com.example.demo.entity.Job;

import java.util.List;

public interface JobService {

    /**
     * 列出所有的职业
     * @return
     */
    List<Job> listJobs();

    /**
     * 添加/跟新职业
     * @param job
     */
    void saveJob(Job job);

    /**
     * 删除职业
     * @param id
     */
    void remoceJob(Integer id);

    /**
     * 根据id获取职业
     * @param id
     * @return
     */
    Job findJobById(Integer id);
}
