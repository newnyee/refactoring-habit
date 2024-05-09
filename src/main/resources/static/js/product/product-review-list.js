$(document).ready(() => {

  // textarea 자동 줄 높이 조정
  let txtArea = $(".review-content-text");
  if (txtArea) {
    txtArea.each(function () {
      $(this).height(this.scrollHeight)
    })
  }
})