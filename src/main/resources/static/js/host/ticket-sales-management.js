
$(function(){

  //====오늘날짜 넣기
  let today=new Date();
  $("#endDate").val(today.toISOString().slice(0,10));

  //====조회기간 넣기
  let dateBtn=$(".dateBtn");
  dateBtn.click(function(){
    //alert(this.id);
    $("#endDate").val(today.toISOString().slice(0,10));

    if(this.id=="btnradio1"){
      $("#startDate").val(today.toISOString().slice(0,10));
    }else if(this.id=="btnradio2"){
      let endDate=new Date();
      endDate.setMonth(today.getMonth()-1);
      $("#startDate").val(endDate.toISOString().slice(0,10));
    }else if(this.id=="btnradio3"){
      let endDate=new Date();
      endDate.setMonth(today.getMonth()-6);
      $("#startDate").val(endDate.toISOString().slice(0,10));
    }
    else if(this.id=="btnradio4"){
      let endDate=new Date();
      endDate.setMonth(today.getMonth()-12);
      $("#startDate").val(endDate.toISOString().slice(0,10));
    }else if(this.id=="btnradio5"){
      let endDate=new Date();
      endDate.setMonth(today.getMonth()-60);
      $("#startDate").val(endDate.toISOString().slice(0,10));
    }else if(this.id=="btnradio6"){
      $("#startDate").val("");
      $("#endDate").val(today.toISOString().slice(0,10));
    }

  });

  //======페이징

  //필터링했는지 쿼리파리미터 뽑기
  let searchFilter=window.location.search;
  let urlParams=new URLSearchParams(window.location.search);


  //페이지 인덱스 선택시 색깔바꾸기
  if (urlParams.has("paging")) {
    let activeBtn = urlParams.get("paging");
    $("#page" + activeBtn).addClass("active");
  } else {
    $("#page1").addClass("active");
  }

  //페이지 인덱스 클릭시 해당페이지 list 가져오기
  let page = $(".page");
  page.click(function () {
    $(".page").removeClass("active");
    $(this).addClass("active");
    //페이징 인덱스
    let pagingNum = this.innerHTML;
    //조겁없이 조회할때라 있이 조회할때 구분
    //필터링없이 페이징할때
    if (searchFilter === "") {
      //alert("hello");
      location.href = "/host/product?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/product?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/product" + window.location.search + appendHtml;
      }
    };
  });




  //페이지 prev, next 버튼
  let pageNum=$(".page-num").length;
  //console.log(pageNum);
  //페이지 인덱스가 하나일때 전후번튼 disabled
  if(pageNum==1){
    $(".page-way").addClass("disabled");
  };

  //페이지가 첫번째인덱스에있을때 전버튼 disabled
  if($(".page").first().hasClass("active")){
    $(".page-way").first().addClass("disabled");
  };

  //페이지가 마지막인덱스에 있을때 후버튼 disabled
  if($(".page").last().hasClass("active")){
    $(".page-way").last().addClass("disabled");
  };

  //페이지 전버튼 구현
  $("#pagePrev").click(function (){

    let prevPage=$(".page.active").parent().prev().children();
    $(".page").removeClass("active");
    prevPage.addClass("active");

    //페이징 인덱스
    let pagingNum =prevPage.text();

    //조겁없이 조회할때라 있이 조회할때 구분
    //필터링없이 페이징할때
    if (searchFilter === "") {
      //alert("hello");
      location.href = "/host/product?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/product?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/product" + window.location.search + appendHtml;
      }
    };

  });

  //페이지 후번튼 구현
  $("#pageNext").click(function (){

    let nextPage=$(".page.active").parent().next().children();
    $(".page").removeClass("active");
    nextPage.addClass("active");

    //페이징 인덱스
    let pagingNum =nextPage.text();

    //조겁없이 조회할때라 있이 조회할때 구분
    //필터링없이 페이징할때
    if (searchFilter === "") {
      //alert("hello");
      location.href = "/host/product?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/product?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/product" + window.location.search + appendHtml;
      }
    };

  });

  //=====인덱스 화면상에서  3개만 보이게
  //첫번째 인덱스 클릭시
  let indexpage=$(".page.active").parent().index();
  console.log(indexpage);
  console.log($(".index_page_btn").length);
  if (indexpage == 1) {
    $(".index_page_btn").parent().eq(indexpage - 1).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage + 1).css("display", "block");
    //마지막 인덱스 클릭시
  } else if (indexpage == $(".index_page_btn").length) {
    $(".index_page_btn").parent().eq(indexpage - 3).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage - 2).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage - 1).css("display", "block");
  } else {
    //인덱스 화면상에서  3개만 보이게
    $(".index_page_btn").parent().css("display", "none");
    $(".index_page_btn").parent().eq(indexpage - 2).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage - 1).css("display", "block");
    $(".index_page_btn").parent().eq(indexpage).css("display", "block");
  }



})

//====상태 변경  ajax
function changeStatus(e){
  //alert(e);
  let status=$("#staus"+e).val();
  //alert(status);
  let params={"payd_no":e,"status":status};
  if(status=="R"){
    alert("'사용중' 상태 이외의 상태로 처리해주세요.");
  }else {
    let check = confirm("상태를 변경하시겠습니까?");
    if ((status == "Y" || status=="C" )&& check) {
      let now=location.href;
      $.ajax({
        type: "POST"
        , url: "/host/status"
        , data: params
        , async: false
        , success: function (data) {
          //alert(data);
          if (data == "ROK" || data =="COK") {
            location.reload(true);
          };
        }
      });
    }

  };
}

//필터링 submit할때 enddate 비어있으면 오늘날짜 넣어주기
function checkFiltering(){
  if($("#endDate").val()==""){
    let today= new Date();
    $("#endDate").val(today.toISOString().slice(0,10));
  }
}

//유저 정보 보여주기
const showUserInfo = (e) => {
  let userId = $(e).text()
  $('.user-info-id').text(userId)
  $(".show_userInfo").css("display","block");
  $(document).on('mousemove', (e) => {
    let mouseX = e.pageX
    let mouseY = e.pageY
    $('.show_userInfo').css('left', mouseX + 'px').css('top', mouseY - 250 + 'px')
  })
}

const closeUserInfo = () => {
  $('.show_userInfo').css('display','none')
  $(document).off('mousemove')
}
