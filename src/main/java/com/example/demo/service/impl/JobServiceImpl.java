package com.example.demo.service.impl;

import com.example.demo.entity.Job;
import com.example.demo.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class JobServiceImpl implements JobService {

    @Override
    public List<Job> listJobs() {
        return null;
    }

    @Override
    public void saveJob(Job job) {

    }

    @Override
    public void remoceJob(Integer id) {

    }

    @Override
    public Job findJobById(Integer id) {
        return null;
    }
}
