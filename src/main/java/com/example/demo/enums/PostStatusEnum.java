package com.example.demo.enums;

public enum PostStatusEnum {
    ALL_POST("all", "全部"),
    PUBLISH_POST("publish", "已发布"),
    DRAFT_POST("draft", "草稿"),
    PRIVATE_POST("private", "私密"),
    STICKY_POST("sticky", "置顶"),
    DELETED_POST("deleted", "已删除"),
    CLOSED_POST("closed","已关闭"),
    RESOLVED_POST("resolved","已解决"),
    VERIFY_POST("verify","验证"),
    ACCEPT_POST("accepted","已采纳"),
    UNANSWERED_POST("unanswered","零回答"),
    FATIE_POST("fatie","fatie");
    private String code;

    private String msg;
    PostStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
