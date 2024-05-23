$(document).ready(()=> {
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
  }

  $('#sendEmail').on('click', ()=>{

    if (confirm("임시 비밀번호를 발급받으시겠습니까?")) {
      let responseData = sessionStorage.getItem('responseData')

      $.ajax({
        url: '/api/v2/auth/reset-password',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.parse(responseData).data,
        success: () => {
          sessionStorage.removeItem('responseData')
          $('#sendEmailContainer').attr('hidden', true)
          $('#result').attr('hidden', false)
        },
        beforeSend: () => {
          $('#div_load_img').css('display', 'flex')
        },
        complete: () => {
          $('#div_load_img').css('display', 'none')
        }
      })

    } else {
      sessionStorage.removeItem('responseData')
      return false
    }
  })
})
