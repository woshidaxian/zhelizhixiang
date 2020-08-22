$(function(){
    var city = ['杭州市','宁波市','温州市','绍兴市','湖州市','嘉兴市','金华市','衢州市','台州市','丽水市','舟山市'];
    function messBox(a,time){
        $('.messBox').html(a);
        $('.messBox').fadeIn().delay(time);
        $('.messBox').fadeOut();
    }
    $('.btn-up').click(function(){
        var account = $('.inp-account').val();
        var password = $('.inp-password').val();
        var nowCity = $('.nowCity').text();
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        document.cookie = "isLogin_zx" + account +"/;path = /";
        document.cookie = "nowCity_zx" + city +"/;path = /";
        if(account==''||password==''){
            messBox('您忘记输密码或者账号了!',1000);
        }else{
            $.ajax({
                url:"/admin/loginuser",
                type:"post",
                async:false,
                data:{"city":nowCity,"account":account,"password":password},
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                success:function(result){
                    if (result.code == "1") {
                        location.href = '/home';
                        alert("登陆成功")
                    }else if (result.code == "-1") {
                        alert("没有权限或者密码错误")
                        messBox("没有权限或者密码错误!");
                    }else {
                        alert("账号密码错误")
                    }
                },
                error:function(){
                    messBox("登录出错！请稍后再试",1000);
                }
            });
            return false;
        }
    });
    $('.city-div').click(function(){
        $('.haveCity-blank').slideDown();
    })
    $('.blank-li').click(function(){
        var indexOfCity = $(this).attr("index");
        $('.haveCity-blank').slideUp();
        $('.blank-li').removeClass('blank-li-active');
        $(this).addClass('blank-li-active')
        $('.nowCity').html(city[indexOfCity]);
    })
})