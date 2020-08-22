window.onload = function() {
    var $$ = mdui.JQ;
    // 点击加号选择发帖或投稿

    console.log(tiezi_swiper);
    var wenzhang_dialog = new mdui.Dialog("#wenzhang_dialog");
    var tiezi_dialog = new mdui.Dialog("#tiezi_dialog");
    var tiezi_dialogid = document.getElementById("tiezi_dialog");
    $$("#add_two-dialog").on("click", function() {
        mdui.dialog({
            title: "选择主题",
            buttons: [
                {
                    text: "发表帖子",
                    onClick: function(inst) {
                        tiezi_dialog.open();
                    }
                },
                {
                    text: "文章投稿",
                    onClick: function(inst) {
                        self.open('article');
                    }
                }
            ]
        });
    });
    //当dialog实体出现时才有里面的swiper
    tiezi_dialogid.addEventListener("opened.mdui.dialog", function() {
        var tiezi_swiper = new Swiper("#tiezi_swiper", {
            freeMode: false,
            freeModeMomentumBounce: true,
            observer: true,
            on: {
                slideChangeTransitionEnd: function() {
                    var nowslide = this.activeIndex; //切换结束时，告诉我现在是第几个slide
                    var slidename = ["主题赛事", "热点投票", "日常动态", "提问"];
                    $$("#tiezi_upload_btn_f").text(slidename[nowslide]);
                }
            }
        });
        //改变帖子类型按钮名字
        $$("#choose_type .mdui-menu-item").on("click", function() {
            var newtext = $$(this).text();
            var btn_eq = $$(this).index("#choose_type li");
            console.log(btn_eq);
            $$("#tiezi_upload_btn_f").text(newtext);
            tiezi_swiper.slideTo(btn_eq);
        });
    });

    //文章发布成功按钮
    $$("#article_upload_btn").on("click", function() {


        // 获取 CSRF Token
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var article = $(".w-e-text").html();
        $.ajax({
            url: "/article/add",
            type: 'get',
            data: {
                "article":article,
            },
            beforeSend: function (request) {
                request.setRequestHeader(header, token); // 添加  CSRF Token
            },
            success: function (data) {
                mdui.alert(article);
                wenzhang_dialog.close();
            },
            error: function () {
                window.location.reload();
            }
        });


    });
    //帖子成功发布按钮
    $$("#tiezi_upload_btn").on("click", function() {
        tiezi_dialog.close();
        mdui.alert("发表成功");
    });
    //配置富文本编辑器
    var E = window.wangEditor;
    var article_upload = new E("#article_upload");
    var slide_3_wang = new E("#slide_3_wang");
    var slide_2_wang = new E("#slide_2_wang");
    var upload_menu = ["fontSize", "bold", "justify", "emoticon", "image"];
    var emoji = [
        {
            // tab 的标题
            title: "emoji",
            // type -> 'emoji' / 'image'
            type: "emoji",
            // content -> 数组
            content: [
                "😀",
                "😁",
                "😂",
                "😃",
                "😄",
                "😅",
                "😆",
                "😉",
                "😊",
                "😋",
                "😎",
                "😍",
                "😘",
                "😗",
                "😙",
                "😚",
                "😇",
                "😐",
                "😑",
                "😶",
                "😏",
                "😣",
                "😥",
                "😮",
                "😯",
                "😪",
                "😫",
                "😴",
                "😌",
                "😛",
                "😜",
                "😝",
                "😒",
                "😓",
                "😔",
                "😕",
                "😲",
                "😷",
                "😖",
                "😞",
                "😟",
                "😤",
                "😢",
                "😭",
                "😦",
                "😧",
                "😨",
                "😬",
                "😰",
                "😱",
                "😳",
                "😵",
                "😡",
                "😠",
                "👏",
                "👍"
            ]
        }
    ];

    function wang_peizhi(dx) {
        dx.customConfig.menus = upload_menu;
        dx.customConfig.emotions = emoji;
        dx.customConfig.uploadImgShowBase64 = true; //base64本地
        dx.create();
    }
    wang_peizhi(article_upload);
    wang_peizhi(slide_2_wang);
    wang_peizhi(slide_3_wang);
    //////////////////////////////////////////////////////////////////////////////////////////three_part_swiper
    // var three_part = new Swiper("#three_part", {
    //   noSwiping: true,
    //   noSwipingClass: "stop-swiping"
    // });
    // $$(".three_part_btn").on("click", function() {
    //   var btn_eq = $$(this).index("#three_part_btn_fa a");
    //   three_part.slideTo(btn_eq);
    // });
    //////////////////////////////////////////////////////////////////////////////////////////个人中心点击tab响应右侧
    var to_where = $$(".to_where");
    var tab_item = $$("#geren_tab .mdui-list-item");
    $$.each(tab_item, function(i, value) {
        value.index = i;
    });
    tab_item.on("click", function() {
        to_where
            .removeClass("to_appear")
            .eq(this.index)
            .addClass("to_appear");
        tab_item
            .removeClass("colorful")
            .eq(this.index)
            .addClass("colorful");
    });
    $$("#geren_body button").on("click", function() {
        to_where
            .removeClass("to_appear")
            .eq(5)
            .addClass("to_appear");
        tab_item
            .removeClass("colorful")
            .eq(5)
            .addClass("colorful");
    });
    //  //////////////////////////////////////////////////////////////////////////////////////////点击弹出其他人的页面



    $$(".icon-ac").each(function() {
        $$(this).attr("index", 1);
    });
    $$(".icon-ac").on("click", function() {
        if ($$(this).attr("index") == 1) {
            $$(this).css("color", "red");
            $$(this).attr("index", 2);
        } else {
            $$(this).css("color", "inherit");
            $$(this).attr("index", 1);
        }
    });

    $$(".oook,.nooo").on("click", function() {
        mdui.dialog({
            title: "投票成功",
            buttons: [
                {
                    text: "确认"
                }
            ]
        });
    });
    $$(".oook").on("click", function() {
        var nowwidth = $$(".mdui-slider-fill").width();
        $$(".mdui-slider-fill").css("width", nowwidth + 10 + "px");
    });


    $$(".article").on("click", function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var articleId = $(this).attr("id");
        var url = "talkbar/article/"+articleId;
        var taren_dialog = new mdui.Dialog("#taren_dialog");
        $$(".mdui-comments-name").on("click", function() {
            taren_dialog.open();
            guanzhuName = $$(this).html();

        });
        $.ajax({
            url: url,
            type:"get",
            data:{async:true},
            beforeSend: function (request) {
                request.setRequestHeader(header, token); // 添加  CSRF Token
            },
            success:function (result) {
                // while ($(".content").attr("data-article-id") == articleId){
                //     $(this.content).html(result.content);
                // }
                // $(".content").html(result.content);
                for (var i = 0; i < result.length; i++) {
                    mdui.JQ(".mdui-comments").prepend("<img src=\"img/to_bar_2.jpg\" class=\"mdui-comments-head\" alt=\"\">\n" +
                        "                  <span class=\"mdui-comments-name\">" + result[i].user.username + "</span>\n" +
                        "                  <span class='mdui-float-right mdui-comments-time'>"+result[i].easyCreateTime+"</span>\n" +
                        "                  <p class=\"mdui-comments-body\">" + result[i].content + "</p>\n" +
                        "                  <p class=\"mdui-comments-bottom\"><i class=\"mdui-icon material-icons icon-ac\">thumb_up</i></p>\n" +
                        "                  <div class=\"mdui-typo\">\n" +
                        "                  <hr/>\n" +
                        "                </div>")
                }


            },
            error:function () {
            }
        })
    });

    $$(".comments").on("click", function() {
        var  content = $(".comment_neirong").val();
        article_id = $(this).attr("data-article-id")
        $.ajax({
            url:"/comment",
            type:"get",
            data:{"content":content,"articleId":article_id},
            async:false,
            success:function (data) {
                mdui.snackbar({message:data.message})
                $(".mdui-comments").append("<img src=\"img/to_bar_2.jpg\" class=\"mdui-comments-head\" alt=\"\">\n" +
                    "                  <span class=\"mdui-comments-name\">" + data.body + "</span>\n" +
                    "                  <span class='mdui-float-right mdui-comments-time'>刚刚</span>\n" +
                    "                  <p class=\"mdui-comments-body\">" + content + "</p>\n" +
                    "                  <p class=\"mdui-comments-bottom\"><i class=\"mdui-icon material-icons icon-ac\">thumb_up</i></p>\n" +
                    "                  <div class=\"mdui-typo\">\n" +
                    "                  <hr/>\n");
            }
        })
    });

    $$(".addzan").on("click", function() {
        var content = $("#comment_neirong").val();
        article_id = $(this).attr("data-article-id")
        $.ajax({
            url:"/article/addzan",
            type:"get",
            data:{"articleId":article_id},
            success:function () {
            }
        })
    });

    $$(".star").on("click", function() {
        var  content = $("#comment_neirong").val();
        article_id = $(this).attr("data-article-id")
        $.ajax({
            url:"/article/star",
            type:"get",
            data:{"articleId":article_id},
            success:function (data) {
                if (data[0] == 1){
                    mdui.snackbar({message:"收藏成功"})
                }else {
                    mdui.snackbar({message:"收藏失败"})
                }
            }
        })
    });

    $$("#ta_body button").on("click", function() {
        $.ajax({
            url:"/manage/follow",
            type:"get",
            data:{"name":guanzhuName},
            success:function () {
                if ($$(this).text() == "关注") {
                    $$(this).text("已关注");
                } else {
                    $$(this).text("关注");
                }
            },
            error:function () {
                alert("网络出错啦");
            }
        })

    });

    mdui.mutation();

};

