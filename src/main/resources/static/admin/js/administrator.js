$(function(){

    var ids = [];
    var updateIndex;
    oData = '';
    $.ajax({
        url:'/administrator/show',
        type:'get',
        async:false,
        data:{},
        success:function(data){
            var article = [];
            var city = [];
            for (var i = 0 ; i < data.length;i++) {
                article.push({
                    "id": data[i].id,
                    "account": data[i].username,
                    "realName": data[i].nickname,
                    "ablity": [data[i].city],
                    "email": data[i].email,
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
            url:'user/delete/'+ids,
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
    // 关闭编辑弹出窗口
    $('.closediv').click(function(){
        $('body').css({overflowY:'auto'})
        $('.con-otherbox').fadeOut();
        $('input[name="city"]').prop('checked',false) 
    })
    //保存按钮
    $('.btn-save').click(function(){
        var chkValue = [];
        var newA = $('#inputAccount').val();
        var newP = $('#inputPassword').val();
        var newN = $('#inputName').val();
        var newE = $('#inputEmail').val();
        $('input[name="city"]:checked').each(function(){   
            chkValue.push($(this).val());   
        });
        $.ajax({
            url:"",
            type:'post',
            data:{"id":ids,"account":newA,"password":newP,"name":newN,"email":newE,"chkValue":chkValue},
            success:function(code){
                if(code==1){
                    alert('保存成功');
                    $('#table').bootstrapTable('updateRow',{
                        index:updateIndex,
                        row:{
                            account:newA,
                            realName:newN,
                            ablity:chkValue,
                            email:newE
                        }
                    });
                    $('.closediv').click();
                }else{
                    alert('保存失败');
                }
            },
            error:function(){
                alert('请求出错');
            }
        })
    })
    window.operateEvents = {
        // 删除按钮
        'click .btn-danger':function(e,value,row,index){
            ids = [];
            $('.mask').fadeIn();
            $('.popup').animateCss('bounceIn');
            ids.push(row.id);
        },
        // 编辑按钮
        'click .btn-info':function(e,value,row,index){
            ids=[];
            ids.push(row.id);
            updateIndex=index;
            $('#inputAccount').val(row.account);
            $('#inputName').val(row.realName);
            $('#inputEmail').val(row.email);
            for(var a=0;a<row.ablity.length;a++){
                for(var b=0;b<13;b++){
                    if($('#inlineCheckbox'+b).val()==row.ablity[a]){
                        $('#inlineCheckbox'+b).prop('checked',true);
                    }
                }
            }
            $('body').css({overflowY:'hidden'})
            $('.con-otherbox').fadeIn();
            $('.otherbox').animateCss('slideInUp');
        },
    }
    // oData = [
    //     {
    //         id:1,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['超级管理'],
    //         email:'dsadaassadas'
    //     },{
    //         id:2,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['湖州'],
    //         email:'dsadaassadas'
    //     },{
    //         id:3,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['用户管理','嘉兴'],
    //         email:'dsadaassadas'
    //     },{
    //         id:4,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['绍兴','杭州'],
    //         email:'dsadaassadas'
    //     },{
    //         id:5,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['杭州','衢州'],
    //         email:'dsadaassadas'
    //     },{
    //         id:6,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['杭州','衢州','宁波'],
    //         email:'dsadaassadas'
    //     },{
    //         id:7,
    //         account:'gang',
    //         realName:'gang',
    //         ablity:['温州','台州','丽水'],
    //         email:'dsadaassadas'
    //     }
    //]
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
            field:'realName',
            title:'真实姓名',
            align:'center',
            valign : 'middle'
        },{
            field:'ablity',
            title:'权限',
            align:'center',
            valign:'middle',
            formatter:styleAblity
        },{
            field:'email',
            title:'邮箱',
            width:'240',
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
            '<button class="btn btn-info">编辑</button>',
        ].join('');
    };
    function styleAblity(value,row,index){
        var box=[];
        for(var i=0;i<row.ablity.length;i++){
            box.push('<span style="background:#0cc28b;margin-left:5px;margin-right:5px;padding:2px 4px;color:#fff">'+ row.ablity[i] +'</span>')
        };
        return box.join('');
    }
    $('.btn-info').eq(0).attr('disabled','disabled');
    $('.btn-danger').eq(0).attr('disabled','disabled');
})