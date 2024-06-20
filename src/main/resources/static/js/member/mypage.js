// 비밀번호 검증
const passwordValidation = () => {
  let password = $('#password')
  let errorMessage = $('.error-message')

  if (password.length === 0) {
    errorMessage.css('display', 'block')
    errorMessage.text('필수 입력란 입니다')
    return false
  }

  if (password.val().length < 8 || password.val().length > 20) {
    errorMessage.css('display', 'block')
    errorMessage.text('8~20자 이내로 입력해주세요')
    return false
  }

  if (password.val().search(/\s/) !== -1) {
    errorMessage.css('display', 'block')
    errorMessage.text('공백 없이 입력해주세요')
    return false
  }

  if (
      password.val().search(/[0-9]/g) < 0 || // 숫자
      password.val().search(/[a-z]/ig) < 0 || // 영어
      password.val().search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi) < 0 // 특수문자
  ) {
    errorMessage.css('display', 'block')
    errorMessage.text('비밀번호는 영문, 숫자, 특수문자 혼합입니다')
    return false
  }

  errorMessage.css('display', 'none')
  return true
}

const verifyPasswordModalOpen = () => {
  let passwordCheck = getCookie('password_check')
  let passwordCheckSession = getCookie('password_check_session')

  if (passwordCheck !== null && passwordCheckSession !== null) {
    if (passwordCheck === 'ok' && passwordCheckSession === 'ok') {
      window.location.href = '/my-page/info'
      return
    }
  }
  $('.modal-background').css('display', 'flex')
}

const verifyPasswordModalClose = () => {
  $('#password').val('')
  $('.modal-background').css('display','none')
}

const callVerifyPasswordApi = () => {
  $.ajax({
    url: '/api/v2/auth/verify-password',
    method: 'POST',
    data: {
      password: $('#password').val()
    },
    success: () => {
      verifyPasswordModalClose()
      setCookie('password_check', 'ok', 30)
      setCookie('password_check_session', 'ok')
      window.location.href = '/my-page/info'
    },
    error: (error) => {
      if (error.responseJSON.code === 'A005') {
        alert("입력하신 비밀번호가 올바르지 않습니다. 다시 확인해주세요.");
      } else {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.");
      }
    }
  })
}

$(document).ready(() => {
  $('.update-my-info').on('click', () => {
    verifyPasswordModalOpen()
  })

  $('.check-password-button').on('click', () => {
    if (passwordValidation()) {
      callVerifyPasswordApi();
    }
  })

  $('.modal-background').on('keypress', (e) => {
    if (e.key === 'Enter') {
      if (passwordValidation()) {
        callVerifyPasswordApi();
      }
    }
  })
})

$(document).mousedown((e) => {
  if($('.modal-background').has(e.target).length === 0){
    verifyPasswordModalClose()
  }
})
