const showDetail = (paymentId,e) => {

  $('.show-modal').css('display','block')
  // 지급서 제목
  let paymentTitle = $(e).children('td:eq(0)').text();
  // 총 지급액
  let totalPaymentAmount = $(e).children('td:eq(1)').children().text();
  // 지급 요청일
  let requestDate = $(e).children('td:eq(2)').text();
  // 지급 완료일
  let completeDate = $(e).children('td:eq(3)').text();
  // 지급 상태
  let paymentStatus = $(e).children('td:eq(4)').text();

  $(".payment-title").text(paymentTitle);
  $(".total-payment-amount").text(totalPaymentAmount);
  $(".request-date").text(requestDate);
  $(".complete-date").text(completeDate);
  $(".payment-status").text(paymentStatus);

  let params={"calc_no":paymentId};
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
        })
      }
    }
  })
}

//모달창 외부 클릭시 닫기
$(document).mouseup(function (e){

  var container = $('.show-modal')

  if( container.has(e.target).length === 0){
    container.css('display','none')
  }
})

$(document).ready(()=>{

  //모달창 엑스 버튼 눌러서 닫기
  $('.close-modal').click(function () {
    $('.show-modal').css("display", "none")
  })
})
