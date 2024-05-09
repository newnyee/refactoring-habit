$(document).ready(() => {

  // 필터 모달창 열기
  $('.Button1').on('click', () => {
    $('.Filter_Container_Modal').css('display', 'flex')
  })

  // 필터 모달창 닫기
  $('.filter-close').on('click', () => {
    $('.Filter_Container_Modal').css('display', 'none')
  })
})