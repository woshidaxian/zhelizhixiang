$(function(){
    function time(creatTime) {
        var date = new Date(creatTime);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes() + ':';
        s = date.getSeconds();
        return(Y+M+D+h+m+s);
    }
    var ids = [];
    oData = '';
    $.ajax({
        url:"/initiatingActivities/article",
        type:'get',
        async:false,
        data:{},
        success:function(data){
            var article = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id": data[i].id,
                    "writer": data[i].user.nickname,
                    "time": time(data[i].createTime),
                    "title": data[i].title,
                    "city": data[i].city,
                    "type": data[i].category.name,
                })
            }
            var json = JSON.stringify(article);
            oData = JSON.parse(json);
        },
        error:function(){
            alert('请求出错');
        }
    })
    $('.btn-sure').click(function(){
        $('.popup').animateCss('bounceOut',function(){
            $('.mask').css({
                display:'none'
            })
        });
        $.ajax({
            url:'',
            type:'post',
            data:{"id":ids},
            success:function(code){
                if(code==1){
                    alert('删除成功');
                    $("#table").bootstrapTable('remove', {field: 'id', values:ids});
                }else{
                    alert('删除失败，请稍后重试');
                }
            },
            error:function(){
                alert('请求出错')
            }
        })
    })
    // 取消按钮
    $('.btn-cancel').click(function(){
        $('.popup').animateCss('bounceOut',function(){
            $('.mask').css({
                display:'none'
            })
        });
    })
    $('.closepopup').click(function(){
        $('.popup').animateCss('bounceOut',function(){
            $('.mask').css({
                display:'none'
            })
        });
    })
    //关闭详情弹出窗口
    $('.closediv').click(function(){
        $('body').css({overflowY:'auto'})
        $('.otherboxcenter').html(' ');
        $('.con-otherbox').fadeOut();
    })

    window.operateEvents = {
        //详情按钮
        'click .btn-info':function(e,value,row,index){
                    var oBoxn = '';
                    var data = [['img/show1/2.jpg'],['浙江省文物保护单位。位于宁波城东20公里处鄞县五乡镇宝幢阿育王山西麓。西晋太康三年(282)始建。相传阿育王寺的开山祖师慧达为求贮有释迦牟尼真身舍利的宝塔，行至会稽之山，在今古育王地祀祷，忽闻地下有钟磬之声，有舍利宝塔自地下涌出，遂建庐守护。因原址狭隘，后又另择宝地即今址建造殿宇。南朝梁武帝普通三年(522)赐额"阿育王寺"，而其坐山也以阿育王名。历1700多年来，几经兴衰，现存寺院由山门、钟楼、天王殿、大殿、舍利殿、法堂及僧房、斋堂、偏殿等以及元代的东西二塔、新建的仿楼阁式塔等建筑组成，占地12.44万平方米，建筑面积2.3万平方米。阿育王寺不但风光秀丽，殿宇巍峨，在中国佛教史上也占有重要地位，南宋理宗时将其列为"天下五山之第二"，明洪武十五年(1382)诏定为"天下禅宗五山第五"。宋高宗、宋孝宗、清乾隆帝等曾御书匾额，唐代高僧鉴真在东渡日本时曾卓锡在此。另外，寺内还藏有许多文物，有唐代书法家范的所书"大唐阿育王寺常住田碑"，宋代张九成撰写的"妙喜泉铭"碑，唐贯休十六尊者像石刻以及"钦锡龙藏"经等。寺附近有"佛迹亭"、"极目亭"、"仙人岩"、"月现岩"、"七佛潭"等胜景。\',']];

                    for(var o = 0;o<data[0].length;o++){
                        oBoxn+='<img src="'+ data[0][o] +'" alt="" class="divImgStyle">'
                    }
                    oBoxn='<div class="div-img">'+ oBoxn +'</div>'+'<div class="div-word">'+ data[1] +'</div>';
                    $('.otherboxcenter').append(oBoxn);
                    $('body').css({overflowY:'hidden'})
                    $('.con-otherbox').fadeIn();
                    $('.otherbox').animateCss('slideInUp');
        },
        // 删除按钮
        'click .btn-danger':function(e,value,row,index){
            ids = [];
            $('.mask').fadeIn();
            $('.popup').animateCss('bounceIn');
            ids.push(row.id);
        },
        // 通过按钮
        'click .btn-success':function(e,value,row,index){
            $.ajax({
                url:'/initiatingActivities/pass/'+row.id,
                type:'get',
                success:function(code){
                    if(code[0]==1){
                        alert('通过成功');
                        $('.btn-success').eq(index).attr('disabled','disabled').text('已通过');
                    }else{
                        alert('失败，请稍后重试');
                    }
                },
                error:function(){
                    alert('请求出错');
                }
            })
        }
    }
    // oData =[
    //     {
    //         id:1,
    //         writer:'gang',
    //         time:'2019-8-8',
    //         title:'测试文字',
    //         city:'杭州市',
    //         type:'先贤文化',
    //     },{
    //         id:2,
    //         writer:'gang',
    //         time:'2019-8-8',
    //         title:'测试文字',
    //         city:'杭州市',
    //         type:'先贤文化',
    //     },{
    //         id:3,
    //         writer:'gang',
    //         time:'2019-8-8',
    //         title:'测试文字',
    //         city:'杭州市',
    //         type:'先贤文化',
    //     },{
    //         id:4,
    //         writer:'gang',
    //         time:'2019-8-8',
    //         title:'测试文字',
    //         city:'杭州市',
    //         type:'先贤文化',
    //     },{
    //         id:5,
    //         writer:'gang',
    //         time:'2019-8-8',
    //         title:'测试文字',
    //         city:'杭州市',
    //         type:'先贤文化',
    //     }
    // ]
    // 表格
    $('#table').bootstrapTable({
        data:oData,
        pagination:true,     //分页
        sidePagination:'client',    //客户端分页‘client’,后台分页‘server’
        pageNumber:1,    
        pageSize:10,    
        pageList:[10, 25, 50, 100,'ALL'],
        search:true,    //搜索框
        striped: true,      //条纹
        cache:false,        //禁用ajax缓存
        showRefresh: true,   //刷新表格
        showColumns: true,    
        showPaginationSwitch:true,
        columns:[{
            checkbox:true,
            align:'center',
            valign:'middle'
        },{
            field:'id',
            title:'ID',
            align:'center',
            valign : 'middle'
        },{
            field:'writer',
            title:'作者',
            align:'center',
            valign : 'middle'
        },{
            field:'time',
            title:'来稿时间',
            align:'center',
            valign : 'middle'
        },{
            field:'title',
            title:'标题',
            align:'center',
            valign : 'middle'
        },{
            field:'city',
            title:'所属地市',
            align:'center',
            valign : 'middle'
        },{
            field:'type',
            title:'所属类别',
            align:'center',
            valign : 'middle'
        },{
            field:'operate',
            title:'操作',
            align:'center',
            valign : 'middle',
            events:operateEvents,
            formatter:operateFormatter
        }]
    })
    function operateFormatter(value, row, index) {
        return [
            '<button class="btn btn-info">详情</button>',
            '<button class="btn btn-danger" style="margin-right:10px;margin-left:10px">删除</button>',
            '<button class="btn btn-success">通过</button>',
        ].join('');
    };
})