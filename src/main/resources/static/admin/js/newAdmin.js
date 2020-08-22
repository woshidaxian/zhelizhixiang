$(function(){
    var account='';
    var password = '';
    var twoPasssword = '';
    var realname = '';
    var email = '';
    var baseTable = '';
    var chkValue = [];
    $('.btn-block').click(function(){
        account=$('#adminAccount').val();
        password=$('#adminPassword').val();
        twoPasssword=$('#adminPasswordSure').val();
        realname=$('#adminName').val();
        email=$('#adminEmail').val();
        $('input[name="city"]:checked').each(function(){   
            chkValue.push($(this).val());   
        });
        if(!account||!password||!twoPasssword||!realname||!email){
            alert('请输入完整！！！');
        }else if(password!=twoPasssword){
            $('.adminPasswordSureDiv').addClass('has-error');
            $('.adminPasswordSureDiv').append('<span style="color:red">两次的密码不一样</span>')
        }else{
            var  obj = document.getElementsByName("city");

            for(var i in obj){
                if(obj[i].checked){
                    baseTable = obj[i].value;
                    break;
                }
            }
            $.ajax({
                url:'/administrator/add',
                type:'get',
                data:{"account":account,"password":password,"realname":realname,"email":email,"city":baseTable},
                success:function(code){
                    if(code[0]==1){
                        alert('保存成功');
                    }else if (code[0] = 0) {
                        alert('邮箱已被注册');
                    }else if (code[0] = 2){
                        alert("账号已被注册")
                    }  else {
                        alert('保存失败')
                    }
                },
                error:function(){
                    alert('前方出现不明错误！')
                }
            })
        }
    })
})