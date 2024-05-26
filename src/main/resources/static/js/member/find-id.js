$(document).ready(() => {
  let responseData = sessionStorage.getItem('responseData')

  if (responseData === null) {
    alert('핸드폰번호, 생년월일을 먼저 인증해주세요.')
    window.location.href = '/find-member'

  } else {
    let email = JSON.parse(responseData).data
    let showEmail = ''

    for (let i = 0; i < email.length; i++) {
      if (i < 4 || email[i] === '@' || (i >= 4 && email[i] === '\.')) {
        showEmail += email[i]
      } else {
        showEmail += '*'
      }
    }

    $('#found_email').text(showEmail)
    sessionStorage.removeItem('responseData')
  }
})