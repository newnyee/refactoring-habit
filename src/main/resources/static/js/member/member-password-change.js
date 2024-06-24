const isNotBlank = (value, errorMessage) => {
  if (value.length === 0 || value === '') {
    errorMessage.css('display', 'block')
    errorMessage.text('❌필수 입력란 입니다')
    return false
  }
  errorMessage.css('display', 'none')
  return true
}

// 비밀번호 확인 검증
const passwordMatch = () => {
  let password = $('#password')
  let passwordMatch = $('#password_match')
  let passwordMatchErrorMessage
      = passwordMatch.closest('.Home_form_div').find('.error-message')

  if (password.val() !== passwordMatch.val()) {
    passwordMatchErrorMessage.css('display', 'block')
    passwordMatchErrorMessage.text('❌비밀번호가 다릅니다')
    return false
  }

  passwordMatchErrorMessage.css('display', 'none')
  return true
}

// 비밀번호 검증
const passwordValidation = () => {
  let password = $('#password')
  let passwordErrorMessage
      = password.closest('.Home_form_div').find('.error-message')

  if (!isNotBlank(password.val(), passwordErrorMessage)) {
    return false
  }

  let match = $('#password_match')
  if (match.val().length > 0) {
    passwordMatch()
  }

  if (password.val().length < 8 || password.val().length > 20) {
    passwordErrorMessage.css('display', 'block')
    passwordErrorMessage.text('❌8~20자 이내로 입력해주세요')
    return false
  }

  if (password.val().search(/\s/) !== -1) {
    passwordErrorMessage.css('display', 'block')
    passwordErrorMessage.text('❌비밀번호는 공백 없이 입력해주세요')
    return false
  }

  if (
      password.val().search(/[0-9]/g) < 0 || // 숫자
      password.val().search(/[a-z]/ig) < 0 || // 영어
      password.val().search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi) < 0 // 특수문자
  ) {
    passwordErrorMessage.css('display', 'block')
    passwordErrorMessage.text('❌영문, 숫자, 특수문자를 혼합하여 입력해주세요')
    return false
  }

  passwordErrorMessage.css('display', 'none')
  return true
}

const callUpdatePasswordApi = () => {

  let updateInfo = {
    password: $('#password').val()
  }

  let formData = new FormData()
  let blob = new Blob([JSON.stringify(updateInfo)], {type: "application/json"})
  formData.append("updateInfo", blob)

  $.ajax({
    url: '/api/v2/members/' + altId,
    method: 'PATCH',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: (response) => {
      alert("비밀번호가 변경되었습니다.")
      $('#password').val('')
      $('#password_match').val('')
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const updatePasswordSubmit = () => {

  // 비밀번호
  if (!passwordValidation()) {
    return false
  }

  // 비밀번호 확인
  if (!passwordMatch()) {
    return false
  }

  callUpdatePasswordApi()
}

$(document).ready(() => {
  let passwordCheck = getCookie('password_check')
  let passwordCheckSession = getCookie('password_check_session')

  if (!(passwordCheck === 'ok' && passwordCheckSession === 'ok')) {
    alert('비밀번호 확인 후 접근 가능합니다.')
    window.location.href = '/my-page/info'
  }

  let isFormSubmitted = false

  $('#updatePasswordButton').on('click', () => {
    updatePasswordSubmit()
    isFormSubmitted = true
  })

  $(window).on('beforeunload', (e) => {
    if (!isFormSubmitted) {
      // 사용자 정의 메시지를 설정하더라도 최신 브라우저에서는 무시됨
      let message = "변경사항이 저장되지 않을 수 있습니다."
      e.returnValue = message
      return message
    }
  })
})