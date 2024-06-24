const isNotBlank = (value, errorMessage) => {
  if (value.length === 0 || value === '') {
    errorMessage.css('display', 'block')
    errorMessage.text('필수 입력란 입니다')
    return false
  }
  errorMessage.css('display', 'none')
  return true
}

// 이메일 검증
const emailValidation = () => {
  let email = $('#email')
  let errorMessage = $('.error-message')

  let emailCheck = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/

  if (!isNotBlank(email.val(), errorMessage)) {
    return false
  }

  if (emailCheck.test(email.val()) === false) {
    errorMessage.css('display', 'block');
    errorMessage.text('이메일 형식에 맞게 입력해주세요');
    return false
  }

  errorMessage.css('display', 'none')
  return true
}

// 비밀번호 검증
const passwordValidation = () => {
  let password = $('#password')
  let errorMessage = $('.error-message')

  if (!isNotBlank(password.val(), errorMessage)) {
    return false
  }

  if (password.val().length < 8 || password.val().length > 20) {
    errorMessage.css('display', 'block')
    errorMessage.text('비밀번호를 8~20자 이내로 입력해주세요')
    return false
  }

  if (password.val().search(/\s/) !== -1) {
    errorMessage.css('display', 'block')
    errorMessage.text('비밀번호는 공백 없이 입력해주세요')
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

const callLoginApi = () => {
  $.ajax({
    url: '/api/v2/auth/sign-in',
    method: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
      email: $('#email').val(),
      password: $('#password').val()
    }),
    success: () => {
      let redirectURL = getCookie('redirectURL')
      setCookie('redirectURL', '', 0)
      if (redirectURL === null) {
        redirectURL = '/'
      }
      window.location.replace(redirectURL);
    },
    error: (error) => {
      if (error.responseJSON.code === 'U002' || error.responseJSON.code === 'A005') {
        alert("이메일 또는 비밀번호를 확인해주세요.");
      } else {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {

  // 로그인 api 호출
  $('.Home_login_btn').on('click', ()=>{
    if (!emailValidation()) {
      return false
    }

    if (!passwordValidation()) {
      return false
    }

    callLoginApi()
  })

  // 엔터 키 누를 시 로그인 api 호출
  $('.Home_loginForm').on('keypress', (e) => {
    if (e.key === 'Enter') {
      if (!emailValidation()) {
        return false
      }

      if (!passwordValidation()) {
        return false
      }

      callLoginApi()
    }
  })
})
