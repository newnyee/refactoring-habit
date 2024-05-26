// 필수 입력 확인
const isNotBlank = (value, errorMessage) => {
  if (value.length < 1 || value === '') {
    errorMessage.css('display', 'block')
    errorMessage.text('필수 입력란 입니다')
    return false
  }
  errorMessage.css('display', 'none')
  return true
}

// 휴대폰 번호 검증
const phoneValidation = () =>{
  let phone = $('#phone')
  let errorMessage = $('#phone-error-message')

  // 휴대폰 번호 not blank check
  isNotBlank(phone.val(), errorMessage)

  // 이메일 형식 check
  let phoneCheck = /^(01[016789]{1})[0-9]{3,4}[0-9]{4}$/
  if (phoneCheck.test(phone.val().trim()) === false) {
    errorMessage.text("휴대폰 번호 형식에 맞게 입력해주세요")
    errorMessage.css('display', 'block')
    return false
  }

  errorMessage.css('display', 'none')
  return true
}

// 생년월일 검증
const birthValidation = () => {
  let birth = $('#birth')
  let errorMessage = $('#birth-error-message')

  isNotBlank(birth.val(), errorMessage)

  let birthSplit = birth.val().split('-')
  let year = birthSplit[0]
  let month = birthSplit[1]
  let date = birthSplit[2]

  let inputValue = new Date(year, month, date)
  let now = new Date()
  if (year < now.getFullYear() - 100 || inputValue > now) {
    errorMessage.css('display', 'block')
    errorMessage.text('생년월일을 확인해주세요')
    return false
  }

  errorMessage.css('display', 'none');
  return true
}

$(document).ready(()=>{

  $('#phone').on('change', ()=> {
    phoneValidation()
  })

  $('#birth').on('change', () => {
    birthValidation()
  })

  $('.Home_submit').on('click', (e) => {
    if(!phoneValidation()) return
    if(!birthValidation()) return

    let getButtonId = $(e.currentTarget).attr('id')
    let phone = $('#phone').val()
    let birth = $('#birth').val()
    let errorMessage = $('#birth-error-message')

    $.ajax({
      url: '/api/v2/auth/find-email',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        phone: phone,
        birth: birth
      }),
      success: (response) => {
        sessionStorage.setItem("responseData", JSON.stringify(response))
        window.location.href = '/find-member/' + getButtonId
      },
      error: (error) => {
        if (error.responseJSON.code === 'U001') {
          errorMessage.text('입력하신 정보와 일치하는 정보가 없습니다.')
          errorMessage.css('display', 'block')

        } else if (error.status === 500) {
          errorMessage.text('회원 정보를 찾을 수 없습니다. 관리자에게 문의해주세요.')
          errorMessage.css('display', 'block')
        }
      }
    })
  })
})
