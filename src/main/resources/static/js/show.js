$(function() {
  var $$ = mdui.JQ;
  var ww = window.innerWidth;
  var wh = window.innerHeight;

  // 获取 CSRF Token
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  // 更新内容
  var arrimg = [
    "img/show1/1.jpg",
    "img/show1/2.jpg",
    "img/show1/3.jpg",
    "img/show1/4.jpg",
    "img/show1/5.jpg",
    "img/show1/6.jpg",
    "img/show1/7.jpg"
  ];

  var arrtitle = [
    "阿育王寺",
    "四明山",
    "河姆渡遗址",
    "梁祝古迹遗址",
    "上林湖青瓷窑址",
    "保国寺",
    "天童禅寺"
  ];
  var arrnr = [
    '浙江省文物保护单位。位于宁波城东20公里处鄞县五乡镇宝幢阿育王山西麓。西晋太康三年(282)始建。相传阿育王寺的开山祖师慧达为求贮有释迦牟尼真身舍利的宝塔，行至会稽之山，在今古育王地祀祷，忽闻地下有钟磬之声，有舍利宝塔自地下涌出，遂建庐守护。因原址狭隘，后又另择宝地即今址建造殿宇。南朝梁武帝普通三年(522)赐额"阿育王寺"，而其坐山也以阿育王名。历1700多年来，几经兴衰，现存寺院由山门、钟楼、天王殿、大殿、舍利殿、法堂及僧房、斋堂、偏殿等以及元代的东西二塔、新建的仿楼阁式塔等建筑组成，占地12.44万平方米，建筑面积2.3万平方米。阿育王寺不但风光秀丽，殿宇巍峨，在中国佛教史上也占有重要地位，南宋理宗时将其列为"天下五山之第二"，明洪武十五年(1382)诏定为"天下禅宗五山第五"。宋高宗、宋孝宗、清乾隆帝等曾御书匾额，唐代高僧鉴真在东渡日本时曾卓锡在此。另外，寺内还藏有许多文物，有唐代书法家范的所书"大唐阿育王寺常住田碑"，宋代张九成撰写的"妙喜泉铭"碑，唐贯休十六尊者像石刻以及"钦锡龙藏"经等。寺附近有"佛迹亭"、"极目亭"、"仙人岩"、"月现岩"、"七佛潭"等胜景。',
    "四明山，也称金钟山，为浙江省境内四明山脉主峰，位于嵊州境内。浙江四明山有着第二庐山之称，林深茂密，青山碧水，各种鸟兽出没其间，生态环境十分优越，被誉为天然“氧吧”。四明山平均海拔在700米左右，是休闲避暑的理想之地。四明山曾是革命根据地之一，也是中国南方七大游击区之一，是浙东纵队的主要活动区。主峰在嵊州市黄泽镇北部，海拔1012米（现调整为1018米）。景观入胜，层峦叠嶂，山奇水秀，闻名遐迩。给这座绵亘七个县市的浙东名山笼罩上一层神秘的面纱。据《剡录·山水志》载：“四明山境四周八百余里……东为惊浪之山，西拒奔牛之垄，南则驱羊之势，北起走蛇之峭。”四明山多峰，但若以高度来确定，当以海拔1021米的覆船山为最。但因位于其南偏西的山头，高峻挺拔，四面玲珑，独领风骚，而被冠名为 “四明山主峰”。四明山云蒸霞蔚，地处华东前沿，曾有中国第二庐山之称。于公园内的各个景区，为华东一带罕见的天象景观。每到冬季，漫山雾气升腾，凝成闪亮的银屑，点点滴滴堆嵌在高山之巅的松树和其他树木之上，或绣出各式各样的冰凌花，或结成钟乳石笋般的冰挂。遍山的花草树木，恍若银枝玉叶。这时的四明山俨如天上广寒宫，海中珊瑚岛。",
    "全国重点文物保护单位。位于宁波城西北25公里处余姚市河姆渡镇。1973年发现。遗址面积4万平方米，堆积厚度4米，由相互叠压的4个文化层组成。经两期考古发掘，共出土文物7000余件，早期文化遗存距今已有6900多年的历史。遗址的第四文化层里发现大量人工栽培的水稻和生产工具骨耜，证明长江下游是世界上最早栽培水稻的地区之一。遗址出土的成排的木桩、板、柱等木构建筑残件，分布井然有序，部分凿有榫卯，为干栏式建筑遗存，是人类建筑技术上的奇迹。出土的工艺品有小猪、小狗、木碗、骨匕、象牙饰件等，用彩绘、刻画、捏塑、编结等手法制成。遗址出土了6支木桨，说明宁波的先民们早在7000年前就开始了水上活动。遗址还出土大量骨器、木器、陶器和少量的石器以及亚洲象、犀牛、四不像、狝猴、梅花鹿等动物的遗骨。继河姆渡遗址之后，市域内又发现了八字桥、辰蛟、董家跳、淄山、童家岙、茗山、塔山等属河姆渡文化的遗址30余处。河姆渡遗址的发现，证明长江流域和黄河流域一样都是中华民族古老文化的发祥地",
    "宁波市鄞县文物保护单位。位于宁波城南8公里处鄞县高桥镇。遗址由梁山伯庙、梁山伯祝英台合葬墓组成。相传梁山伯为晋代人，曾任县令，有政绩，晋安帝时追封为义忠王，建义忠王庙，后称梁山伯庙。梁祝的爱情故事，千百年来流传甚广。梁祝故事最早见于梁元帝《金缕子》一书，宋代的《四明干道图经》有详细记载。墓地内曾有大量的晋墓砖出土，全国许多地方都有梁祝故事传说，但遗留有墓与庙的仅鄞县一处，现已辟为梁祝文化公园。",
    "全国重点文物保护单位。位于宁波城西北60公里处慈溪市桥头镇境内。越窑是中国著名的瓷窑之一，主要分布于绍兴的曹娥江两岸和宁波地区，而上林湖窑址则为越窑的中心产地。上林湖坐落在栲栳山下，四面环山，湖形狭长，岸线曲折，长达20公里的湖岸上分布着东汉、三国、唐、五代至北宋的120余座青瓷窑址。上林湖青瓷胎质细腻，釉色晶莹。唐陆龟蒙在《秘色越瓷》诗中赞道：'九秋风露越窑开，夺得千峰翠色来。'青翠如玉的秘色瓷不但成了奉给皇上的贡品，自唐以后还由明州远销高丽、日本以及阿拉伯诸国，成为中国古代外销商品中的大宗商品，形成了海上的'瓷器之路'。上林湖的高峰期为晚唐，其主要产品有壶、罐、碗、盘、盏、托、盒、杯、唾盂、水注、香炉、砚等，并烧造出珍贵的'秘色瓷'。瓷器的纹饰，以花草、鱼龟等为题材，采用刻、画、镂、堆塑等手法，技巧娴熟。北宋后期，越窑生产衰落。",
    "全国重点文物保护单位。位于宁波市区北部江北区洪塘镇安山村灵山山腰。相传创建于东汉，原名灵山寺。唐会昌五年(845)灭法，寺毁；广明元年(880)重建，改名保国寺。现建筑群由山门、经幢、天王殿、大雄宝殿、观音殿、藏经楼及钟鼓楼、斋房、僧房、偏殿等组成。主体建筑大殿重建于北宋大中祥符六年(1013)，距今已有980余年，是中国古建筑中的瑰宝。大殿的平面进深大于面阔，前槽安装有3个镂空藻井，斗拱古朴粗壮，为七铺作单拱双杪双下昂偷心造。柱子的设计，别具一格，以小拼大'四段合'的做法为全国现存木构建筑中的孤例，斗的做法也各不一样，海棠斗、靴角斗等在现存的木构中也极为罕见。大殿的外观原为三开间单檐歇山式，清康熙二十三年(1684)增建了下檐，现为重檐歇山式。保国寺大殿反映了当时高超的建筑艺术，前人题赞'山岙藏得古招提，宫殿岿然结构奇'。寺内还藏有珍贵的唐代经幢两座。寺四周层峦耸翠，流水潺潺，亭台楼阁点缀其间，是宁波市内著名的旅游胜地",
    "浙江省文物保护单位。位于宁波城东南27公里处鄞县东吴镇太白山麓。始建于西晋太康元年(280)，原址在今称之为'古天童'的东谷，开山祖师为义兴。唐重建时名'太白精舍'，唐至德三年(758)因东谷地狭谷浅，徙建于太白峰下即今址。干元二年(759)，因传祖师义兴在修行诵经时，感动玉帝派童子下凡入侍，又因太白山有玲珑岩，唐肃宗赐名'天童玲珑寺'。宋景德四年(1007)敕赐为'天童景德禅寺'，南宋嘉定年间列为天下五山之第三山。明洪武十五年(1382)赐名'天童禅寺'。几经兴衰，现寺宇坐北朝南，依山而建，布局严谨，气势轩昂，中轴线上有伏虎亭、古山门、内外万工池、七浮图、照壁、天王殿、大雄宝殿、法堂(藏经楼)、先贤堂、罗汉堂等，两侧为配殿、禅堂、钟鼓楼、僧房、客房等，总计999间，占地面积7.64万平方米，建筑面积2.88万平方米。其规模之大，全国少见。天童寺在海内外负有盛名，为日本曹洞宗的祖庭。天童寺和太白山现辟为天童寺风景名胜区。风景区内，青松夹道，爽气扑面，抬头可见翠岗之上古塔挺秀，此为进山入寺的第一门户小白岭，岭上有五佛镇蟒塔、铁蛇关、揖让亭等胜迹。过小白岭，渐入佳境。太白山主峰高653.3米，重峦叠嶂，怪石嶙峋，潭深溪长，松茂竹密，与千年古刹天童寺构成了'深径回松'、'清关喷雪'、'东谷秋红'、'南山晓翠'、'平台铺月'、'凤岗修竹'等10大胜景。旖旎的山水风光，吸引了许多文人墨客来此游览抒怀，王安石游览天童时有诗：'村村桑柘绿浮空，春日莺啼谷口风。二十里松行欲尽，青山捧出梵王宫。'描绘了深山藏古寺的意境。今天太白山的山林和生态环境还保持着原始风貌，并作为国家森林公园予以保护。"
  ];

  // //////////////////////////////////////////////////////////////////////////////////////////////////wave
  function toggleTween(tween) {
    tween.reversed() ? tween.play() : tween.reverse();
  }
  const bigwave = new TimelineLite({ paused: true, reversed: true });
  bigwave.staggerFromTo(".flow", 2, { y: 0 }, { y: -4600 }, 0.2);
  // circle的each点击事件
  var navigation_circle_list_item = $$(".navigation-circle-list-item");
  $$.each(navigation_circle_list_item, function(i, value) {
    value.index = i;
  });
  navigation_circle_list_item.on("click", function() {
    btnclick.reverse();
    bigwave.restart();
    setTimeout(() => {
      $$(".show").css({
      background: "url(" + arrimg[this.index] + ") no-repeat",
      backgroundSize: "100%"
    });
      $$(".show h1").html(arrtitle[this.index]);
      $$(".show h4").html(arrnr[this.index]);
    }, 1100);
    setTimeout(() => {
      nr_appear.restart();
    }, 1200);
    console.log(this.index);
  });

  // 内容出现动画
  const nr_appear = new TimelineLite({ paused: true, reversed: true });
  nr_appear.fromTo(
    ".show_nr",
    1,
    { y: 2000, scale: 0 },
    {
      y: 0,
      scale: 1,
      ease: Expo.easeOut
    }
  );
  nr_appear.restart();
  // //////////////////////////////////////////////////////////////////////////////////////////////////color_btnclick
  var fab = new mdui.Fab("#fab");
  const btnclick = new TimelineLite({ paused: true, reversed: true });
  const btnclick_c = new TimelineLite({ paused: true, reversed: true });
  btnclick.to(".show", 0.2, { filter: "blur(15px)" }).fromTo(
    ".navigation-circle",
    0.25,
    { opacity: 0, scale: 1.2, rotation: -30 },
    {
      opacity: 1,
      scale: 1,
      rotation: 0
    }
  );
  $$("#fab .mdui-color-orange").on("click", function() {
    toggleTween(btnclick);
  });
  var comment_dialog = document.getElementById("comment_dialog");
  comment_dialog.addEventListener("close.mdui.dialog", function() {
    toggleTween(btnclick_c);
  });

  btnclick_c
    .staggerFrom(".mdui-comments", 0.5, { y: 300, opacity: 0 }, 0.3)
    .from(".comment_dialog .mdui-textfield", 0.5, { opacity: 0 })
    .from(".comment_dialog_b", 0.5, { x: -600 });
  $$("#fab .mdui-color-red").on("click", function() {
    toggleTween(btnclick_c);
  });
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
          mdui.snackbar({
            message: data.message,
            position: "right-top",
          });
          //window.location.href = '/index';
        } else if (data.status == 'error') {
          mdui.snackbar({
            message: data.message,
            position: "right-top",
          });
        }
      },
      error: function () {
        alert("error");
      }
    });
    return false;
  });
  // mdui初始化
  mdui.mutation();
  // scrolltext
  var written = false,
    firstMess = 0,
    App;

  App = {
    messages: [
      "阿育王寺",
      "四明山",
      "河姆渡遗址",
      "梁祝古迹遗址",
      "上林湖青瓷窑址",
      "保国寺",
      "天童禅寺"
    ],

    init: function() {
      this.cacheDom();
      this.createMessage(App.messages[firstMess]);

      setTimeout(function() {
        App.replaceName();
      }, 250);

      setTimeout(function() {
        App.messageInterval();
      }, 3000);
    },

    cacheDom: function() {
      this.$textCont = $("#textContainer");
      this.$button = $(".Button");
      this.$body = $("body");
    },

    createMessage: function(message) {
      var mLength = message.length,
        fullMess = "",
        finalMess,
        bold;

      for (var i = 0; i < mLength; i++) {
        var rdm = Math.floor(Math.random() * 200) + 100,
          negOrPos = Math.round(Math.random()),
          letter = message.charAt(i),
          finalLetter = letter.toUpperCase(),
          output;

        if (!negOrPos) {
          rdm = "-" + rdm;
        }

        if (this.isUpperCase(letter)) {
          bold = "bold";
        } else {
          bold = "";
        }

        if (letter === " ") {
          output = '</span><span class="word">';
        } else {
          output =
            '<span class="letter ' +
            bold +
            '" style="transform: matrix(1, 0, 0, 1, 0, ' +
            rdm +
            ");" +
            '">' +
            finalLetter +
            "</span>";
        }
        fullMess += output;
        finalMess = '<span class="word">' + fullMess + "</span>";
      }
      console.log(finalMess);
      this.$textCont.html(finalMess);
    },

    replaceName: function() {
      var $who = $("#textContainer").find(".letter"),
        each = $who.length;

      for (var i = 0; i < each; i++) {
        var rdm = Math.random() * 1 + 1.5;
        TweenLite.to($who.eq(i), rdm, {
          ease: Power3.easeOut,
          y: 0,
          opacity: 1
        });
      }
      written = true;
    },

    scrambleName: function() {
      var $who = $("#textContainer").find(".letter"),
        each = $who.length;

      for (var i = 0; i < each; i++) {
        var rdm = Math.floor(Math.random() * 200) + 100,
          negOrPos = Math.round(Math.random());

        if (!negOrPos) {
          rdm = "-" + rdm;
        }

        TweenLite.to($who.eq(i), 2, {
          ease: Power3.easeInOut,
          y: rdm,
          opacity: 0
        });
      }
      written = false;
    },

    messageInterval: function(index) {
      var which = index || firstMess + 1,
        arrayLength = this.messages.length - 1;

      var wordCarousel = setInterval(function() {
        if (written) {
          setTimeout(function() {
            App.scrambleName();
          }, 2500);
        } else {
          App.createMessage(App.messages[which]);
          which < arrayLength ? which++ : (which = 0);
          setTimeout(function() {
            App.replaceName();
          }, 250);
        }
      }, 5000);
    },

    isUpperCase: function(str) {
      var reg = /[^a-z|,|.|!|?|&]/;
      return reg.test(str);
    }
  };

  App.init();
  // mdui初始化
  mdui.mutation();

  $$(".fabu").on('click',function () {
    var content = $('#inpinp').val();
    $('#inpinp').val(' ');
    $$(".comment_dialog_t").append("          <div class=\"mdui-comments\">\n" +
        "            <img src=\"img/to_bar_2.jpg\" class=\"mdui-comments-head\" alt=\"\" />\n" +
        "            <span class=\"mdui-comments-name\">guest</span>\n" +
        "            <span class=\"mdui-float-right mdui-comments-time\">刚刚</span>\n" +
        "            <p class=\"mdui-comments-body\">\n"+content +"</p>\n" +
        "            <p class=\"mdui-comments-bottom\">\n" +
        "              <i class=\"mdui-icon material-icons\">thumb_up</i>\n" +
        "            </p>\n" +
        "            <div class=\"mdui-typo\">\n" +
        "              <hr />\n" +
        "            </div>\n" +
        "          </div>")
  });

});

// //////////////////////////////////////////////////////////////////////////////////////////////////circle
const pointCount = 7;
const circleRadius = 300;
const startAnimDelta = 5;
const circumference = Math.PI * circleRadius * 2;

var selectedItemIndex = -1;

var circlePath = document.getElementById("mask-circle");

/**
 * @description On Mouse Leave event handler for points
 */
const onMouseLeave = () => {
  let index = selectedItemIndex !== -1 ? selectedItemIndex : 0;
  calculateOffset(index);
};

/**
 * @description On Click event handler for points
 * @param {Number} index - Index of list item
 */
const onClick = index => {
  //If already selected, deselect
  selectedItemIndex = selectedItemIndex === index ? -1 : index;
  calculateOffset(index);

  //Find active item, deselect
  let activeListItem = document.querySelectorAll(
    ".navigation-circle-list-item.active"
  );
  if (activeListItem.length > 0) activeListItem[0].classList.remove("active");

  //Find new item by index, select
  let listItem = document.querySelectorAll(
    ".navigation-circle-list-item:nth-of-type(" + selectedItemIndex + ")"
  );
  if (listItem.length > 0) listItem[0].classList.add("active");
};

/**
 * @description - Calculate offset for circle path by index of list item
 * @param {Number} index - Index of list item
 */
const calculateOffset = (index = 0) => {
  let offset = 0;

  if (index !== 0) offset = (circumference / pointCount) * (pointCount - index);

  circlePath.style.strokeDashoffset = `${offset}px`;
};

// INTRO

let buffer = 1000;
let delay =
  2000 * (1 + pointCount / startAnimDelta - 1 / startAnimDelta) + buffer;

setTimeout(() => onClick(1), delay);
