$(function() {
  //모달창 닫기
  $('.close-modal').click(function () {
    $('.show-modal').css("display", "none");
  });

  //조회기간
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

  //=========페이징
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
      location.href = "/host/adjust?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/adjust?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/adjust" + window.location.search + appendHtml;
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
      location.href = "/host/adjust?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/adjust?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/adjust" + window.location.search + appendHtml;
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
      location.href = "/host/adjust?paging=" + pagingNum
    } else {

      if (urlParams.has("paging")) {
        //alert("ddd");
        urlParams.set("paging", pagingNum);
        location.href = "/host/adjust?" + urlParams.toString();
      } else {
        let appendHtml = "&paging=" + pagingNum;
        location.href = "/host/adjust" + window.location.search + appendHtml;
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
});

function showDetail(adjustNo,e2){
  //console.log(adjustNo);
  $(".show-modal").css("display","block");
  //정산날짜
  let adjustDate=$(e2).children('td:eq(1)').text();
  //정산제목
  let adjustName=$(e2).children('td:eq(2)').text();
  //총액
  let ttlprice=$(e2).children('td:eq(3)').text();
  //총 수수료
  let comprice=$(e2).children('td:eq(4)').text();
  //총정산액
  let adjustprice=$(e2).children('td:eq(5)').text();

  $(".adjust-name").text(adjustName);
  $(".adjust-no").text(adjustNo);
  $(".adjust-date").text(adjustDate);
  $(".ttlprice").text(ttlprice);
  $(".comprice").text(comprice);
  $(".adjustprice").text(adjustprice);

  let params={"calc_no":adjustNo};
  //해당정산 디테일 가져오기
  let html;
  $.ajax({
    type:"GET"
    ,url : "/host/adjustDetail"
    ,data:params
    ,async:false
    ,success:function(data){
      $(".detail-content").html("");
      console.log(data);
      if(data.length!=0){
        $(data).each(function(index,value){
          if(index==data.length-1){
            $(".bankName").text(value.host_bank);
            $(".acholName").text(value.host_acholder);
            $(".accountNum").text(value.host_account);
          }else {
            let status = "";
            if (value.calcd_status == "Y") {
              status = "결제완료";
            } else if (value.calcd_status == "R") {
              status = "환불"
            } else if (value.calcd_status == "C") {
              status = "호스트 취소"
            }
            let price = value.calcd_price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            html = "<tr>";
            html += "  <td>" + value.name + "</td>";
            html += "  <td>" + price + "</td>";
            html += "  <td>" + value.calcd_qty + "</td>";
            html += "  <td>" + status + "</td>";
            html += "  <td>" + value.calcd_date + "</td>";
            html += "</tr>";

            $(".detail-content").append(html);
          }
        });


      }
    }
  });

}

//모달창닫기

$(document).mouseup(function (e){

  var container = $('.show-modal');

  if( container.has(e.target).length === 0){

    container.css('display','none');

  }

});

//필터링 form submit할때 마지막 날짜 비워있으면 넣어주기
function checkFiltering(){
  if($("#endDate").val()==""){
    let today= new Date();
    $("#endDate").val(today.toISOString().slice(0,10));
  }

  if($('#cont_date option:selected').val()=="cont_stdate" ||$('#cont_date option:selected').val()=="cont_endate"){
    if($("#startDate").val()==""||$("#endDate").val()==""){
      alert("정산서 조회시 날짜를 선택해주세요");
      return false;
    }
  }

  if($("#startDate").val()!=""&& $("#endDate").val()!="") {
    if ($('#cont_date option:selected').val() == "cont_no") {
      alert("정산서 조회시 옵션를 선택해주세요");
      return false;
    }
  }
};

function changeStatus(){
  //let find=$(".checkBtn").checked();
  let val=$("input:checkbox[name='money']:checked").val();
  let IdName=$("input:checkbox[name='money']:checked").attr("id");
  console.log(IdName);
  console.log(val);
  let params={"calc_no":val};
  $.ajax({
    type:"POST"
    ,url :"/host/adjustGive"
    ,data:params
    ,async:false
    ,success:function(data) {
      console.log(data);

      if(data=="OK"){
        location.reload();
      }else if(data=="NOK"){
        alert("내 정보에서 계좌정보를 확인해주세요");
        location.href="/host/info";
      }
    }
  });

}