const callLogoutApi = () => {
  $.ajax({
    method: "POST",
    url: "/api/v2/auth/sign-out",
    success: () => {
      alert("로그아웃 되었습니다.")
      location.href = "/"
    }
  })
}

$(document).ready(() => {
  // 로그 아웃 버튼 클릭
  $('#logoutButton').on('click', () => {
    if (confirm("로그아웃 하시겠습니까?")) {
      $(window).off("beforeunload");
      callLogoutApi()
    }
  })

  // 해빗 홈으로 이동
  $('#habitHomeButton').on('click', () => {
    window.location.href = '/'
  })
})