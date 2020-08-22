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
        url:"/communication/article",
        type:'get',
        async:false,
        data:{},
        success:function(data){
            var article = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id": data[i].id,
                    "poster": data[i].user.nickname,
                    "theme": data[i].title,
                    "time": time(data[i].createTime),
                    "zan": data[i].zanSize,
                    "mess": data[i].commentSize,
                    "type": data[i].category.name,
                })
            }

            var json = JSON.stringify(article);
            oData = JSON.parse(json);
        },
        error:function(){
            alert('前方网络拥挤')
        }
    })
    //表格
    // oData = [
    //     {
    //         id:1,
    //         poster:'aaa',
    //         theme:'dasjdnasda',
    //         time:'2019-9-9',
    //         zan:90,
    //         mess:2,
    //         type:'主题赛事'
    //     }
    // ]
    $('.btn-sure').click(function(){
        $('.popup').animateCss('bounceOut',function(){
            $('.mask').css({
                display:'none'
            })
        });
        $.ajax({
            url:'/communication/delete/'+ids,
            type:'get',
            success:function(code){
                if(code[0]==1){
                    alert('删除成功');
                    $("#table").bootstrapTable('remove', {field: 'id', values:ids});
                }else{
                    alert('删除失败，请重试')
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
    // 关闭详情弹出窗口
    $('.closediv').click(function(){
        $('body').css({overflowY:'auto'})
        $('.otherboxcenter').html(' ');
        $('.con-otherbox').fadeOut();
    })

    window.operateEvents = {
        // 删除按钮
        'click .btn-danger':function(e,value,row,index){
            ids = [];
            $('.mask').fadeIn();
            $('.popup').animateCss('bounceIn');
            ids.push(row.id);
        },

    }

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
            title:'序号',
            align:'center',
            valign : 'middle'
        },{
            field:'poster',
            title:'发帖人',
            align:'center',
            valign : 'middle'
        },{
            field:'theme',
            title:'帖子主题',
            align:'center',
            valign : 'middle'
        },{
            field:'time',
            title:'发帖时间',
            align:'center',
            valign:'middle'
        },{
            field:'zan',
            title:'获赞数',
            width:'80',
            align:'center',
            valign : 'middle'
        },{
            field:'mess',
            title:'评论量',
            width:'80',
            align:'center',
            valign : 'middle'
        },{
            field:'type',
            title:'类别',
            width:'120',
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
            '<button class="btn btn-danger" style="margin-right:10px">删除</button>',
        ].join('');
    };
})