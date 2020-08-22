$(function () {
// 获取个人资料
    function getgeren(pageIndex, dataType) {

        // 获取 CSRF Token
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/manage/' + dataType,
            type: 'GET',
            data: {
                "async": true,
                "pageIndex": pageIndex,
            },
            beforeSend: function (request) {
                request.setRequestHeader(header, token); // 添加  CSRF Token
            },
            success: function (data) {
                $("#geren_tab_to").html(data);
            },
            error: function () {
                window.location.reload();
            }
        });
    };

    $(document).on('click', '#geren_tab > ul > li', function () {
        var dateType = $(this).attr("id");
        getgeren(1,dateType);
    });

    $(document).on('click', '#bianji', function () {
        getgeren(1,profile);
    });

    $(document).on('click', '#edit', function () {
        // 获取 CSRF Token
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var nickname = $("#nickname").val();
        var profile = $("#content").val();
        var sex = $("input[name='group1']:checked").val();
        $.ajax({
            url: "/manage/edit",
            type: 'get',
            data: {
                "async": true,
                "nickname": nickname,
                "profile":profile,
                "sex":sex
            },
            beforeSend: function (request) {
                request.setRequestHeader(header, token); // 添加  CSRF Token
            },
            success: function (data) {
                mdui.alert(data.message);
            },
            error: function () {
                window.location.reload();
            }
        });
    });

})