package com.example.demo.VO;

import lombok.Data;

@Data
public class TagVO {
    private static final long serialVersionUID = -9192192840980858203L;

    private String name;

    public TagVO() {
    }

    public TagVO(String name) {
        this.name = name;
    }
}
