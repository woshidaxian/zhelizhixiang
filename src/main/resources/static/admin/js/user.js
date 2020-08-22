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
    // 单击操作
    // 确定按钮
    var ids = [];
    oData='';
    $.ajax({
        url:"/user/show",
        type:'get',
        async:false,
        data:{},
        success:function(data){
            var article = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id":data[i].id,
                    "account":data[i].username,
                    "sex":data[i].articleSize,
                    "time":time(data[i].createTime),
                    "showt":data[i].profile,
                    "status":data[i].status,
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
            url:'/user/delete/'+ids,
            type:'get',
            success:function(code){
                if(code[0]==1){
                    alert('删除成功');
                    $("#table").bootstrapTable('remove', {field: 'id', values:ids});
                }else{
                    alert('删除失败');
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
        // 改密按钮
        'click .btn-info':function(e,value,row,index){
            var newPassword = prompt("请输入新的密码","");
            if(newPassword){
                $.ajax({
                    url:'/user/modifypwd/'+row.id,
                    type:'get',
                    data:{"id":row.id,"password":newPassword},
                    success:function(code){
                        if(code[0]==1){
                            alert('修改成功');
                        }else{
                            alert('修改失败');
                        }
                    },
                    error:function(){
                        alert('请求出错，请稍后再试');
                    }
                });
            }
        },
        // 禁用按钮
        'click .btn-warning':function(e,value,row,index){
            var aa = {"status":'nologin'};
            if(row.status == 'normal'){
                $.ajax({
                    url:'/user/changestatus/'+row.id,
                    type:'get',
                    data:{"status":"nologin"},
                    success:function(code){
                        if(code[0]==1){
                            $('#table').bootstrapTable('updateRow',{index:index,row:aa});
                            alert('已禁用');
                        }else{
                            alert('失败');
                        }
                    },
                    error:function(){
                        alert('请求失败，稍后再试');
                    }
                })
            }else{
                alert('已禁用');
            }
        },
        // 解禁按钮
        'click .btn-success':function(e,value,row,index){
            var bb = {"status":'normal'};
            if(row.status == 'nologin'){
                $.ajax({
                    url:'/user/changestatus/'+row.id,
                    type:'get',
                    data:{"id":row.id,"status":"normal"},
                    success:function(code){
                        if(code[0]==1){
                            $('#table').bootstrapTable('updateRow',{index:index,row:bb});
                            alert('已解禁');
                        }else{
                            alert('失败');
                        }
                    },
                    error:function(){
                        alert('请求失败，稍后再试');
                    }
                })
            }else{
                alert('本次请求无效')
            }
        }
    }
    // var oData = [
    //     {
    //         "id":1,
    //         "account":"user1",
    //         "sex":"男",
    //         "time":"2017-10-10",
    //         "showt":'测试文字测试文字测试文字',
    //         "status":"正常"
    //     },
    //     {
    //         "id":2,
    //         "account":"user2",
    //         "sex":"男",
    //         "time":"2017-10-10",
    //         "showt":'测试文字测试文字测试文字',
    //         "status":"正常"
    //     },
    //     {
    //         "id":3,
    //         "account":"user3",
    //         "sex":"男",
    //         "showt":'测试文字测试文字测试文字',
    //         "time":"2017-10-10",
    //         "status":"正常"
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
            field:'account',
            title:'账号',
            align:'center',
            valign : 'middle'
        },{
            field:'sex',
            title:'文章数',
            align:'center',
            valign : 'middle'
        },{
            field:'time',
            title:'注册时间',
            align:'center',
            valign : 'middle'
        },{
            field:'showt',
            title:"个性签名",
            align:'center',
            valign:'middle'
        },{
            field:'status',
            title:'状态',
            align:'center',
            valign : 'middle',
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
    function operateFormatter(value, row, index) {
        return [
            '<button class="btn btn-danger">删除</button>',
            '<button class="btn btn-info" style="margin:0 10px">改密</button>',
            '<button class="btn btn-warning">禁用</button>',
            '<button class="btn btn-success" style="margin-left:10px">解禁</button>',
        ].join('');
    };
    // 状态样式，禁用为红，normal
    function operateColor(value,row,index){
        if(row.status=='nologin'){
            return {css:{"color":"red"}}
        }else if(row.status=='normal'){
            return {css:{"color":"black"}}
        }
    }
})