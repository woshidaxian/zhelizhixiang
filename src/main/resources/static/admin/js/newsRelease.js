$(function(){
    $('#btn-newsRelease').css({
        background:'#f3f3f3'
    })
    $('#ul-two-news').css({
        display:'block',
    })
    $('.li-one').eq(1).css({
        background:'#fff'
    })
    $('.li-one').eq(1).mouseleave(function(){
        $(this).css({
            background:'#fff'
        })
    })
    
    var E = window.wangEditor
    var editor = new E('#editor');
    editor.customConfig.uploadImgServer = '/upload';
    editor.create();
    $('.w-e-text-container').css({
        height:'600px'
    })
    $('.btn-up').click(function(){
        var title = $('#newsTitle').val();
        var main = editor.txt.text();
        var writer = $('#exampleInputAmount').val();
        if( title!='' && main!='' && writer!=''){
            $('.mask').fadeIn();
            $('.popup-1').fadeIn();
            $.ajax({
                url:'',
                type:'post',
                data:{"title":title,"main":main,"writer":writer},
                success:function(){
                    $('.popup-1').css({
                        display:'none'
                    });
                    $('.popup-2').fadeIn();
                    setTimeout(function(){
                        $('.mask').fadeOut();
                        $('.popup-2').fadeOut();
                    },1000);
                },
                error:function(){
                    $('.popup-1').css({
                        display:"none"
                    })
                    $('.popup-3').fadeIn();
                    setTimeout(function(){
                        $('.mask').fadeOut();
                        $('.popup-3').fadeOut();
                    },1000);
                }
            })
        }else{
            alert('信息不完整')
        }
    })
})