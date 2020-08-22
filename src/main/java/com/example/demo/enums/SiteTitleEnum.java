package com.example.demo.enums;

public enum SiteTitleEnum {

    HOME_INDEX("首页", "知乡"),

    LOGIN_PAGE("登陆页面","知乡用户登陆"),

    REGISTER_PAGE("注册页面","知乡用户注册"),
    QUESTION_HOME("问题首页", "最新问题 - ZhiXiang"),
    ARITICLE_MANAGENMENT("文献名邦管理页面","文献名邦管理"),

    REGISTER_SUCCESS_PAGE("注册成功页面","知乡"),

    BLOG_DETAIL_PAGE("博客详情页", " - 知乡博客"),
    QUESTION_DETAIL_PAGE("求助详情页", " - 知乡问答频道"),
    DISCUSS_DETAIL_PAGE("讨论详情页", " - 知乡讨论区"),
    USER_HOME_PAGE("用户主页", "的主页 - ZhiXiang"),
    FORGET_PAGE("忘记密码页面", "找回密码 - ZhiXiang"),
    RESET_PASSWORD_PAGE("重置密码页面", "重置密码 - ZhiXiang"),
    RESET_PASSWORD_VERIFY_FAIL("重置密码邮件验证失败", "重置密码邮件验证失败 - ZhiXiang"),

    //用户中心

    USER_SPACE("用户个人中心", "个人中心 - ZhiXiang"),
    USER_SPACE_AVATAR("用户头像", "个人头像 - ZhiXiang"),
    USER_SPACE_PROFILE("用户资料", "个人基本资料 - ZhiXiang"),
    USER_SPACE_SECURITY("账号安全", "账号安全 - ZhiXiang"),
    USER_SPACE_LOGIN_LOG("登录日志", "登录日志 - ZhiXiang"),
    USER_SPACE_BINGINGS("第三方绑定", "第三方绑定 - ZhiXiang"),

    RELATIONSHIP_MANAGE("关系管理", "关系管理 - ZhiXiang"),
    MESSAGE_MANAGE("私信管理", "私信管理 - ZhiXiang"),
    NOTICE_MANAGE("通知管理", "通知管理 - ZhiXiang"),
    REPLY_MANAGE("回复管理", "回复管理 - ZhiXiang"),
    USER_SETTINGS("用户设置", "用户设置 - ZhiXiang"),
    COMMENT_MANAGE("评论管理", "评论管理 - ZhiXiang"),

    EMAIL_VERIFY_SUCCESS("邮箱验证成功", "邮箱验证成功 - ZhiXiang"),
    EMAIL_VERIFY_FAIL("邮箱验证失败", "邮箱验证失败 - ZhiXiang"),

    //文章管理
    BLOG_MANAGE("文章管理", "文章管理 - ZhiXiang"),
    BLOG_EDIT("修改文章", "文章编辑 - ZhiXiang"),
    BLOG_CREATE("写文章", "文章撰写 - ZhiXiang"),
    CATEGORY_MANAGE("分类管理", "分类管理 - ZhiXiang"),
    BLOG_REPLY_MANAGE("评论管理", "文章评论管理 - ZhiXiang"),


    //收藏管理
    BOOKMARK_MANAGE("收藏管理", "收藏管理 - ZhiXiang"),

    //问答管理
    QUESTION_CREATE("提出问题", "提出问题 - ZhiXiang"),
    QUESTION_EDIT("修改问题", "修改问题 - ZhiXiang"),
    MY_ANSWER_MANAGE("我的回答管理", "我的回答管理 - ZhiXiang"),
    MY_QUESTION_MANAGE("我的问题管理", "我的问题管理 - ZhiXiang"),
    QUESTION_MANAGE("问答管理", "问答管理 - ZhiXiang"),
    QUESTION_REPLY_MANAGE("问答管理", "问答回复管理 - ZhiXiang"),

    //讨论区管理
    bulletin_MANAGE("话题管理", "话题管理 - ZhiXiang"),
    bulletin_CREATE("话题添加", "添加新话题 - ZhiXiang"),
    bulletin_EDIT("话题修改", "修改话题 - ZhiXiang"),
    bulletin_DETAIL("话题详情页", " - ZhiXiang社区"),


    //绑定第三方
    BIND_PAGE("绑定第三方账号", "绑定第三方账号 - CoderGroup"),


    //    后台
    ARTICLE_ADMIN("文章管理", "文章管理 - CoderGroup后台管理系统"),
    COMMENT_ADMIN("话题管理", "话题管理 - CoderGroup后台管理系统"),
    QUESTION_ADMIN("问题管理", "问题管理 - CoderGroup后台管理系统"),
    ANSWER_ADMIN("回答管理", "回答管理 - CoderGroup后台管理系统"),
    bulletin_ADMIN("话题管理", "话题管理 - CoderGroup后台管理系统"),
    TAG_ADMIN("标签管理", "标签管理 - CoderGroup后台管理系统"),
    USER_ADMIN("用户管理", "标签管理 - CoderGroup后台管理系统"),
    USER_LOGINLOG_ADMIN("用户登录日志管理", "标签管理 - CoderGroup后台管理系统"),
    NOTICE_ADMIN("公告栏管理", "标签管理 - CoderGroup后台管理系统"),

            ;

    private String description;
    private String title;

    SiteTitleEnum(String description,String title){
        this.description = description;
        this.title = title;
    }
    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}

}
