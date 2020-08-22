$(function() {
  var $$ = mdui.JQ;
  var ww = window.innerWidth;
  var wh = window.innerHeight;

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  //////////////////////////////////////////////////////////////////////////////////////////////bar
  const t_bigger = new TimelineLite({ paused: true, reversed: true });
  function slideMenu() {
    var activeState = $("#menu-container .menu-list").hasClass("active");
    $("#menu-container .menu-list").animate(
      { left: activeState ? "0%" : "-100%" },
      400
    );
  }
  $("#menu-wrapper").click(function(event) {
    event.stopPropagation();
    $("#hamburger-menu").toggleClass("open");
    $("#menu-container .menu-list").toggleClass("active");

    slideMenu();
    if (t_bigger.isActive()) {
      event.preventDefault();
      event.stopImmediatePropagation();
      return false;
    }
    toggleTween(t_bigger);
  });
  t_bigger
    .fromTo(
      "#bigger",
      0.8,
      {
        scale: 0
      },
      {
        scale: 1,
        width: 4000,
        height: 3000,
        onComplete: function() {
          $$("#bigger_rightin").toggleClass("fade_bigger_a");
          $("#sign_in_btn i").toggleClass("change_color");
          $("#banquan").toggleClass("change_color");
        },
        onReverseComplete: function() {
          $$("#bigger_rightin").toggleClass("fade_bigger_a");
          $("#sign_in_btn i").toggleClass("change_color");
          $("#banquan").toggleClass("change_color");
        }
      }
    )
    .fromTo(
      "#bigger_rightin",
      0.5,
      {
        x: 2000
      },
      { x: 0 }
    )
    .from("#map_yd", 0.5, { y: 50, opacity: 0 });

  function toggleTween(tween) {
    tween.reversed() ? tween.play() : tween.reverse();
  }
  // map
  var myChart = echarts.init(document.getElementById("zj-map"));
  showProvince("zhejiang", "浙江");
  // 初始化echarts
  function initEcharts(pName, Chinese_) {
    var tmpSeriesData = pName === "china" ? seriesData : [];
    var option = {
      title: {
        text: Chinese_ || pName,
        left: "center"
      },
      series: [
        {
          name: Chinese_ || pName,
          type: "map",
          mapType: pName,
          roam: true, //是否开启鼠标缩放和平移漫游
          data: tmpSeriesData,
          top: "3%", //组件距离容器的距离
          zoom: 1,
          selectedMode: "single",

          label: {
            normal: {
              show: true, //显示省份标签
              textStyle: { color: "#fbfdfe" }, //省份标签字体颜色
              rich: {
                til: {
                  color: "#000",
                  fontSize: 14,
                  align: "center",
                  lineHeight: 21
                },
                desc: {
                  color: "#000",
                  fontSize: 9,
                  align: "center",
                  lineHeight: 21
                },
                high: {
                  color: "#f8473d",
                  fontSize: 9,
                  align: "center",
                  lineHeight: 21
                },
                middle: {
                  color: "#f9d03c",
                  fontSize: 9,
                  align: "center",
                  lineHeight: 21
                },
                low: {
                  color: "#44d2ca",
                  fontSize: 9,
                  align: "center",
                  lineHeight: 21
                }
              }
            },

            emphasis: {
              //对应的鼠标悬浮效果
              show: true,
              textStyle: { color: "#323232" }
            }
          },
          itemStyle: {
            normal: {
              borderWidth: 0.5, //区域边框宽度
              borderColor: "#0550c3", //区域边框颜色
              areaColor: "#4ea397" //区域颜色
            },

            emphasis: {
              borderWidth: 0.5,
              borderColor: "#4b0082",
              areaColor: "#ece39e"
            }
          }
        }
      ]
    };
    myChart.setOption(option);
    myChart.on("click", function(param) {
      console.log(param.event.target.id);
      // var nowid = param.event.target.id;
      // if (nowid == 2526) {
      //   alert("湖州");
      // } else if (nowid == 2513) {
      //   alert("嘉兴");
      // } else if (nowid == 2336) {
      //   alert("杭州");
      // } else if (nowid == 2530) {
      //   alert("绍兴");
      // } else if (nowid == 2340) {
      //   alert("宁波");
      // } else if (nowid == 2542) {
      //   alert("舟山");
      // } else if (nowid == 2538) {
      //   alert("衢州");
      // } else if (nowid == 2534) {
      //   alert("金华");
      // } else if (nowid == 2796) {
      //   alert("台州");
      // } else if (nowid == 2895) {
      //   alert("丽水");
      // } else if (nowid == 2412) {
      //   alert("温州");
      // }
      // 湖州 2526 嘉兴 2513 杭州 2336 绍兴2530 宁波2340 舟山 2542衢州2538 金华2534 台州2796 丽水 2895温州2412
      const map_appear = new TimelineLite({ paused: false, reversed: false });
      map_appear.to("#map_yd", 0.5, { y: -50, opacity: 0 }).to("#zj-map", 1, {
        scale: 0,
        opacity: 0,
        x: -1000,
        onComplete: function() {
          $(".map-div").removeClass("fade_bigger_a");
          $(".map-div").css("display", "none");
          t_bigger.staggerTo(
            ".bigger_bar_item",
            0.2,
            { opacity: 1, y: -60 },
            0.1
          );
          $$('.bar_to_2').addClass("fade_bigger_a");
          bar_to_2.play();
        }
      });
    });
  }
  // 展示对应的省
  function showProvince(pName, Chinese_) {
    //这写省份的js都是通过在线构建工具生成的，保存在本地，需要时加载使用即可，最好不要一开始全部直接引入。
    loadBdScript(
      "$" + pName + "JS",
      "./js/map/province/" + pName + ".js",
      function() {
        initEcharts(Chinese_);
      }
    );
  }
  // 加载对应的JS
  function loadBdScript(scriptId, url, callback) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    if (script.readyState) {
      //IE
      script.onreadystatechange = function() {
        if (
          script.readyState === "loaded" ||
          script.readyState === "complete"
        ) {
          script.onreadystatechange = null;
          callback();
        }
      };
    } else {
      // Others
      script.onload = function() {
        callback();
      };
    }
    script.src = url;
    script.id = scriptId;
    document.getElementsByTagName("head")[0].appendChild(script);
  }
  //////////////////////////////////////////////////////////////////////////////////////////////bar内部
  window.onresize = function() {
    ww = window.innerWidth;
    wh = window.innerHeight;
    $$(".bigger_bar").width(ww * 0.12);
    $$(".bigger_bar").css("left", -ww * 0.12 + "px");
  };
  $$(".bigger_bar").width(ww * 0.12);
  $$(".bigger_bar").css("left", -ww * 0.12 + "px");
  /////////////////////////
  const bar_to_2 = new TimelineLite({ paused: true, reversed: true });
  const bar_to_3 = new TimelineLite({ paused: true, reversed: true });
  const bar_to_4 = new TimelineLite({ paused: true, reversed: true });
  const bar_to_5 = new TimelineLite({ paused: true, reversed: true });
  const bar_to_6 = new TimelineLite({ paused: true, reversed: true });
  var bar_to = $$(".bar_to");
  var bigger_bar_item = $$(".bigger_bar_item");
  $$.each(bigger_bar_item, function(i, value) {
    value.index = i;
  });
  bigger_bar_item.on("click", function() {
    bigger_bar_item
      .removeClass("bigger_bar_item_active")
      .eq(this.index)
      .addClass("bigger_bar_item_active");
    bar_to
      .removeClass("fade_bigger_a")
      .eq(this.index)
      .addClass("fade_bigger_a");
    bar_to_2.pause(0, true);
    bar_to_3.pause(0, true);
    bar_to_4.pause(0, true);
    bar_to_5.pause(0, true);
    bar_to_6.pause(0, true);
    if (this.index == 0) {
      bar_to_2.play();
    } else if (this.index == 1) {
      bar_to_3.play();
    } else if (this.index == 2) {
      bar_to_4.play();
    } else if (this.index == 3) {
      bar_to_5.play();
    } else if (this.index == 4) {
      bar_to_6.play();
    }
  });
  bar_to_2
    .fromTo(
      ".bar_to_2 .to_bar_img",
      0.5,
      { opacity: 0, scale: 1.1 },
      { opacity: 1, scale: 1 }
    )
    .staggerFrom(".bar_to_2 .bar_to_n_c>div", 0.5, { opacity: 0, x: 50 }, 0.2);
  bar_to_3
    .fromTo(
      ".bar_to_3 .to_bar_img",
      0.5,
      { opacity: 0, scale: 1.1 },
      { opacity: 1, scale: 1 }
    )
    .staggerFrom(".bar_to_3 .bar_to_n_c>div", 0.5, { opacity: 0, x: 50 }, 0.2);
  bar_to_4
    .fromTo(
      ".bar_to_4 .to_bar_img",
      0.5,
      { opacity: 0, scale: 1.1 },
      { opacity: 1, scale: 1 }
    )
    .staggerFrom(".bar_to_4 .bar_to_n_c>div", 0.5, { opacity: 0, x: 50 }, 0.2);
  bar_to_5
    .fromTo(
      ".bar_to_5 .to_bar_img",
      0.5,
      { opacity: 0, scale: 1.1 },
      { opacity: 1, scale: 1 }
    )
    .staggerFrom(".bar_to_5 .bar_to_n_c>div", 0.5, { opacity: 0, x: 50 }, 0.2);
  bar_to_6.staggerFromTo(
    ".bar_to_6 .mdui-shadow-8",
    0.5,
    { opacity: 0, scale: 1.1 },
    { opacity: 1, scale: 1 },
    0.3
  );

  //////////////////////////////////////////////////////////////////////////////////////////////login
  //解决初始没有滚动条的问题
  var login_dialog = document.getElementById("login_dialog");
  login_dialog.addEventListener("opened.mdui.dialog", function() {
    new mdui.Tab("#login_dialog .mdui-tab").handleUpdate();
  });
  // tab切换
  $$("#sign_in_a1").on("click", function(e) {
    $$("#sign_in_dl").removeClass("mdui-hidden");
    $$("#sign_in_zc").addClass("mdui-hidden");
  });
  $$("#sign_in_a2").on("click", function(e) {
    $$("#sign_in_dl").addClass("mdui-hidden");
    $$("#sign_in_zc").removeClass("mdui-hidden");
  });
  var login_tab = new mdui.Dialog("#login_dialog", { history: true });
  document
    .getElementById("login_dialog")
    .addEventListener("open.mdui.dialog", function() {
      login_tab.handleUpdate();
    });
  // 确认密码未加
  //注册登录按下后提示成功    (还缺少跳转相关、账号密码是否符合 )
  $$("#login_btn_zc").on("click", function() {
    var username = $$("#username").val();
    var password = $$("#password").val();
    var confirm_password = $$("#confirm_password").val();
      $.ajax({
        url: "user/register",
        type: "post",
        data: {"password": password, "username": username,"confirm_password":confirm_password},
        beforeSend: function (request) {
          request.setRequestHeader(header, token);
        },
        success: function (response) {
          if (response.success) {
            mdui.snackbar({
              message: response.message,
              position: "right-top"
            });
            window.location.href = '/register-success';
          } else if (response.success == false) {
            mdui.snackbar({
              message: response.message,
              position: "right-top"
            });
          }
        },
      });
      return false;
  });

  //登陆
  $$("#login_btn_dl").on("click", function() {
    var username = $$("#l_username").val();
    var password = $$("#l_password").val();
    $.ajax({
      url: "login",
      type: 'post',
      data: {"password": password, "username": username},
      beforeSend: function (request) {
        request.setRequestHeader(header, token);
      },
      success: function (data) {
        if (data.status == 'ok') {
          //登录成功
          mdui.alert(data.message);
          //window.location.href = '/index';
        } else if (data.status == 'error') {
          mdui.alert(data.message);

        }
      },
      error: function () {
        mdui.alert("error");
      }
    });
    return false;
  });

  var data = $("#user").val();
  var val = eval("("+data+")");

  $$("#article6_go").on("click", function() {
    window.location.href = '/talkbar';
    });
  
  $$("#article6_ly").on("click",function () {
    var name = $("#name").val();
    var contact = $("#contact").val();
    var content = $("#content").val();
    $.ajax({
      url:"/message/add",
      type:"get",
      data:{"name":name,"contact":contact,"content":content},
      success:function (data) {
        if (data[0] == 1){
          mdui.snackbar({message:"留言成功"})
        } else {
          mdui.snackbar({message:"留言失败"})
        }

      }
    })
  });

  $(document).on('click', '.bar_to_lm > span', function () {
    window.location.href = '/show'
  });

  // mdui初始化
  mdui.mutation();
});
