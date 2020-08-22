package com.example.demo.VO;

import lombok.Data;

@Data
public class HotArticleVO {

    private Long id;

    private String title;

    private String guid;

    private Integer commentSize;

    private int viewSize;

    private int zanSize;

}
