$(function(){
    function getCookie(name) {
        var arr;
        var reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    };
    var account = getCookie("isLogin_zx");
    var city = getCookie("nowCity_zx");
    //account=1;
    //如果非法通过其他页面路径进入页面，获取不到cookie信息将会判为未登录，将会在3秒后跳转到登录界面
    if(!account){
        var time=3;
        var timer = setInterval(function(){
            $('#center-box').html('<div style="font-size:20px">请先登录,'+time+'秒后跳转到登录界面</div>');
            time--;
            if(time<0){
                clearInterval(timer);
                self.location='admin';
            }
        },1000)
    }

    $('.info-account').text('欢迎！'+account);
    $('.word-yun').text('知乡');
    $('.web-img').text(city);
    // 刷新
    $('.reload').click(function(){
        window.location.reload();
    })
    // 退出登录
    $('.info-config').click(function(){
        self.location = 'home'
    })
    // 导航栏效果
    function reset(){
        $('.ul-two').slideUp();
        $('.triangle').removeClass("fa-angle-down");
        $('.triangle').addClass("fa-angle-left");
        $('.li-one').css({
            background:"#fbf9f5"
        })
    }
    $('#news').click(function(){
        reset();
        $('.ul-two').eq(0).slideDown();
        $('.li-one').eq(1).css({
            background:"#FFF"
        })
        $('.triangle').eq(0).removeClass("fa-angle-left");
        $('.triangle').eq(0).addClass("fa-angle-down");
    })
    $('#active').click(function(){
        reset();
        $('.ul-two').eq(1).slideDown();
        $('.li-one').eq(2).css({
            background:"#FFF"
        })
        $('.triangle').eq(1).removeClass("fa-angle-left");
        $('.triangle').eq(1).addClass("fa-angle-down");
    })
    $('#shiwu').click(function(){
        reset();
        $('.ul-two').eq(2).slideDown();
        $('.li-one').eq(3).css({
            background:"#FFF"
        })
        $('.triangle').eq(2).removeClass("fa-angle-left");
        $('.triangle').eq(2).addClass("fa-angle-down");
    })
    $('#xianzhi').click(function(){
        reset();
        $('.ul-two').eq(3).slideDown();
        $('.li-one').eq(4).css({
            background:"#FFF"
        })
        $('.triangle').eq(3).removeClass("fa-angle-left");
        $('.triangle').eq(3).addClass("fa-angle-down");
    })
    $('#xinxiang').click(function(){
        reset();
        $('.ul-two').eq(4).slideDown();
        $('.li-one').eq(6).css({
            background:"#FFF"
        })
        $('.triangle').eq(4).removeClass("fa-angle-left");
        $('.triangle').eq(4).addClass("fa-angle-down");
    })
    $('.li-one').mouseover(function(){
        $(this).css({
            background:"#fff"
        })
    })
    $('.li-one').mouseleave(function(){
        $(this).css({
            background:"#fbf9f5"
        })
    })
    // 导航栏链接效果
    $('#btn-statistics').click(function(){self.location = 'home'});//网站统计

    $('#btn-newsRelease').click(function(){self.location = 'communication'});//社区热门
    $('#btn-newsStatistics').click(function(){self.location = 'communityRecommend'});//社区推荐
    $('#btn-notice').click(function(){self.location = 'initiatingActivities'});//社区来稿
    
    $('#btn-alreadyOnline').click(function(){self.location = 'administrator'});//管理员管理
    $('#btn-toBeAudited').click(function(){self.location = 'newAdmin'});//添加新管理

    $('#btn-published').click(function(){self.location = 'manuscriptStatistics'});//文稿统计
    $('#btn-toBeAudited-lost').click(function(){self.location = 'newManuscript'});//新建文稿

    
    $('#btn-user').click(function(){self.location = 'user'});//用户管理
    $('#btn-mess').click(function(){self.location = 'webMessage'})//网站留言
})