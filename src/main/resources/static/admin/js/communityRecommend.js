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
    var oData = '';
    $.ajax({
        url:'/communityRecommend/article',
        type:'get',
        async:false,
        data:{},
        success:function(data){
            var article = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id": data[i].id,
                    "admin": data[i].user.nickname,
                    "title": data[i].title,
                    "time": time(data[i].createTime),
                    "status":data[i].status,
                    "linkUrl": data[i].guid
                })
            }

            var json = JSON.stringify(article);
            oData = JSON.parse(json);
        },
        error:function(){
            alert('请求出错')
        }
    })
    $('.btn-sure').click(function(){
        $('.popup').animateCss('bounceOut',function(){
            $('.mask').css({
                display:'none'
            })
        });
        $.ajax({
            url:'/communityRecommend/delete/'+ids,
            type:'get',
            success:function(code){
                if (code[0]==1){
                    alert('删除成功');
                    $("#table").bootstrapTable('remove', {field: 'id', values:ids});
                }else{
                    alert('删除失败，请稍后重试');
                }
            },
            error:function(){
                alert('请求出错');
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
    window.operateEvents = {
        // 删除按钮
        'click .btn-danger':function(e,value,row,index){
            ids = [];
            $('.mask').fadeIn();
            $('.popup').animateCss('bounceIn');
            ids.push(row.id);
        },
        // 下线按钮
        'click .btn-warning':function(e,value,row,index){
            var aa = {"status":'已下线'};
            if(row.status == '已上线'){
                $.ajax({
                    url:'/communityRecommend/status/'+row.id,
                    type:'get',
                    data:{"status":"已下线"},
                    success:function(code){
                        if(code[0]==1){
                            $('#table').bootstrapTable('updateRow',{index:index,row:aa});
                            alert('已下线');
                        }else{
                            alert('操作失败，请稍后重试');
                        }
                    },
                    error:function(){
                        alert('请求失败，稍后再试');
                    }
                })
            }else{
                alert('请求无效');
            }
        },
        // 上线按钮
        'click .btn-success':function(e,value,row,index){
            var bb = {"status":'已上线'};
            if(row.status == '已下线'){
                $.ajax({
                    url:'/communityRecommend/status/'+row.id,
                    type:'get',
                    data:{"status":"已上线"},
                    success:function(){
                        $('#table').bootstrapTable('updateRow',{index:index,row:bb});
                        alert('已上线');
                    },
                    error:function(){
                        alert('请求失败，稍后再试');
                    }
                })
            }else{
                alert('请求无效')
            }
        }
    }
    // oData=[{
    //     id:1,
    //     title:'aaa',
    //     status:'已上线',
    //     time:'2019-9-9',
    //     admin:'gang',
    //     linkUrl:'http://baidu.com'
    // },{
    //     id:2,
    //     title:'aaa',
    //     status:'已下线',
    //     time:'2019-9-1',
    //     admin:'admin',
    //     linkUrl:'http://qq.com'
    // }]
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
            valign : 'middle',
            width:'40'
        },{
            field:'title',
            title:'标题',
            align:'center',
            valign : 'middle'
        },{
            field:'linkUrl',
            title:'链接',
            align:'center',
            valign:'middle',
            formatter:urlFormatter
        },{
            field:'time',
            title:'时间',
            align:'center',
            valign:'middle'
        },{
            field:'admin',
            title:'管理员',
            align:'center',
            valign:'middle'
        },{
            field:'status',
            title:'状态',
            align:'center',
            valign:'middle',
            cellStyle:operateColor
        },{
            field:'operate',
            title:'操作',
            align:'center',
            valign : 'middle',
            events:operateEvents,
            formatter:operateFormatter
        }]
    })
    function urlFormatter(value,row,index){
        return [
            '<a href="'+ row.linkUrl +'" target="_blank">'+ row.linkUrl +'</a>',
        ].join('');
    }
    function operateFormatter(value, row, index) {
        return [
            '<button class="btn btn-danger">删除</button>',
            '<button class="btn btn-warning" style="margin-left:10px;margin-right:10px">下线</button>',
            '<button class="btn btn-success">上线</button>',
        ].join('');
    };
    // 状态样式
    function operateColor(value,row,index){
        if(row.status=='已下线'){
            return {css:{"color":"red"}}
        }else if(row.status=='已上线'){
            return {css:{"color":"green"}}
        }
    }
    //添加新纪录
    $('#btn-add').click(function(){
        var title = $('#exampletitle').val();
        var link = $('#exampleurl').val();
        var count = $('#table').bootstrapTable('getData').length;
        var date = new Date();
        var nowDate = date.getFullYear()+'-'+parseInt(date.getMonth()+1)+'-'+date.getDate();
        if(!title||!link){
            alert('填写不完整！！！');
        }else{
            $.ajax({
                url:'/selectedArticle/add',
                type:'get',
                data:{"title":title,"link":link},
                success:function(code){
                    if(code[0]==1){
                        $('#table').bootstrapTable('insertRow',{
                            index:count,
                            row:{
                                id:count,
                                title:title,
                                status:"已上线",
                                time:nowDate,
                                admin:account,
                                linkUrl:link
                            }
                        })
                        alert('添加成功！');
                    }else{
                        alert('添加失败，请稍后重试');
                    }
                },
                error:function(){
                    alert('添加失败')
                }
            })
        }
    })
})