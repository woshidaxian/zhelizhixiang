$(function(){
    var ids = [];
    oData='';
    $.ajax({
        url:'/message/show',
        type:'get',
        async:false,
        success:function(data){
            var article = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id": data[i].id,
                    "account": data[i].name,
                    "chatWay": data[i].contact,
                    "mess": data[i].content,
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
            url:'/webmessage/delete/'+ids,
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
    }
    // oData = [
    //     {
    //         "id":1,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字"
    //     },{
    //         "id":2,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字测试文字测试文字"
    //     },{
    //         "id":3,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字测试文字测试文字测试文字测试文字"
    //     },{
    //         "id":4,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字测试文字测试文字"
    //     },{
    //         "id":5,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字"
    //     },{
    //         "id":6,
    //         "account":"gang",
    //         "chatWay":"1111111",
    //         "mess":"测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字"
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
            width: 60,
            valign : 'middle'
        },{
            field:'account',
            title:'对方称呼',
            align:'center',
            width: 130,
            valign : 'middle'
        },{
            field:'chatWay',
            title:'联系方式',
            align:'center',
            width: 150,
            valign : 'middle'
        },{
            field:'mess',
            title:'留言内容',
            align:'center',
            valign : 'middle'
        },{
            field:'operate',
            title:'操作',
            align:'center',
            width: 120,
            valign : 'middle',
            events:operateEvents,
            formatter:operateFormatter
        }]
    })
    function operateFormatter(value, row, index) {
        return [
            '<button class="btn btn-danger">删除</button>'
        ].join('');
    };
    // 状态样式，禁用为红，正常为黑
})