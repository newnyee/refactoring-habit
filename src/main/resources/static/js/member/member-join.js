// 필수 입력 확인
const isNotBlank = (value, errorMessage) => {
  if (value.length === 0 || value === '') {
    errorMessage.css('display', 'block')
    errorMessage.text('❌필수 입력란 입니다')
    return false
  }
  errorMessage.css('display', 'none')
  return true
}

// 이메일 검증
const emailValidation = () => {
  let email = $('#email')
  let emailErrorMessage
      = email.closest('.Home_form_div')
        .find('.error-message')

  let emailCheck = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/

  if (!isNotBlank(email.val(), emailErrorMessage)) {
    return false
  }

  if (emailCheck.test(email.val()) === false) {
    emailErrorMessage.css('display', 'block');
    emailErrorMessage.text('❌이메일 형식에 맞게 입력해주세요');
    return false
  }

  emailErrorMessage.css('display', 'none')
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
      = password.closest('.Home_form_div')
        .find('.error-message')

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

// 닉네임 검증
const nickNameValidation = () => {
  let nickName = $('#nick_name')
  let nickNameErrorMessage = nickName.closest('.Home_form_div').find('.error-message')

  if (!isNotBlank(nickName.val(), nickNameErrorMessage)) {
    return false
  }

  if (nickName.val().length < 2) {
    nickNameErrorMessage.css('display', 'block')
    nickNameErrorMessage.text('❌닉네임을 2자 이상 입력해주세요')
    return false
  }

  nickNameErrorMessage.css('display', 'none')
  return true
}

// 성별 검증
const genderValidation = () => {
  let genderErrorMessage = $('#gender_male').closest('.Home_form_div').find('.error-message')

  if ($("input[name='gender']:radio:checked").length < 1) {
    genderErrorMessage.css('display', 'block')
    genderErrorMessage.text('❌성별을 선택해주세요')
    return false
  }

  genderErrorMessage.css('display', 'none')
  return true
}

// 휴대폰 번호 검증
const phoneNumberLengthLimit = () => {
  let phoneNumbers = $("input[name='phone']")
  let firstNumber = phoneNumbers.eq(0)
  let middleNumber = phoneNumbers.eq(1)
  let lastNumber = phoneNumbers.eq(2)
  let phoneErrorMessage = firstNumber.closest('.Home_form_div').find('.error-message')

  if(firstNumber.val().length !== 3){
    firstNumber.val(firstNumber.val().slice(0,3));
  }

  if(middleNumber.val().length !== 4){
    middleNumber.val(middleNumber.val().slice(0,4));
  }

  if(lastNumber.val().length !== 4){
    lastNumber.val(lastNumber.val().slice(0,4));
  }

  for (let i = 0; i<phoneNumbers.length; i++) {
    if (phoneNumbers.eq(i).val().length === 0) {
      phoneErrorMessage.css('display', 'block')
      phoneErrorMessage.text('❌필수 입력란 입니다')
      return false
    }
  }

  phoneErrorMessage.css('display', 'none')
  return true
}

// 생년월일 검증
const birthValidation = () => {
  let birth = $('#birth')
  let birthErrorMessage = birth.closest('.Home_form_div').find('.error-message')

  if (!isNotBlank(birth.val(), birthErrorMessage)) {
    return false
  }

  let year = birth.val().split('-')[0];
  let month = birth.val().split('-')[1]
  let date = birth.val().split('-')[2]

  let inputValue = new Date(year, month, date)
  let now = new Date()
  if (year < now.getFullYear() - 100 || inputValue > now) {
    birthErrorMessage.css('display', 'block')
    birthErrorMessage.text('❌생년월일을 확인해주세요')
    return false
  }

  birthErrorMessage.css('display', 'none');
  return true
}

// 이미지 파일 검증
const previewProfile = (imgs) => {
  let reader = new FileReader();
  let profileImageErrorMessage = $('.Home_form_div_p').find('.error-message')

  if (imgs !== undefined) {
    let imgSize = imgs.files[0].size // 이미지 사이즈
    let maxSize = 2 * 1024 *1024 // 2MB
    let fileExtension = imgs.files[0].name.split('\.').slice(-1)[0] // 파일 확장자

    if (imgSize > maxSize || (fileExtension !== 'png' && fileExtension !== 'jpg')) {
      profileImageErrorMessage.css('display', 'block')
      profileImageErrorMessage.text('❌2MB 이하의 png, jpg 파일만 가능합니다')
      imgs = undefined
      return false
    }

    reader.onload = (img) => {
      $('#preview').attr('src', img.target.result)
    }
    reader.readAsDataURL(imgs.files[0]);
  } else {
    $('#preview').attr('src', '/img/noimg.png')
  }

  profileImageErrorMessage.css('display', 'none')
  return true
}

// 이메일 중복 확인
const checkEmail = () => {
  let email = $('#email')
  let emailErrorMessage = email.closest('.Home_form_div').find('.error-message')
  let checkEmailButton = $('#checkEmail')
  let submitMassage = $('.check-email-submit-message')

  if (checkEmailButton.text() === '이메일 변경') {
    submitMassage.css('display', 'none')
    checkEmailButton.text('중복확인')
    checkEmailButton.removeClass('change-email').addClass('check-email')
    email.prop('disabled', false)
    return false
  }

  if (!emailValidation()) {
    return false
  }

  $.ajax({
    url: '/api/v2/auth/check-email',
    method: 'GET',
    data: {
      email: email.val()
    },
    success: (response) => {
      if (response.data) {
        emailErrorMessage.css('display', 'block')
        emailErrorMessage.text('❌이미 사용중인 이메일입니다')
      } else {
        emailErrorMessage.css('display', 'none')
        email.prop('disabled', true)

        checkEmailButton.text('이메일 변경')
        checkEmailButton.removeClass('check-email').addClass('change-email')
        submitMassage.css('display', 'block')
      }
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const requestJoinApi = () => {

  let phoneNumbers = []
  for (let i = 0; i < 3; i++) {
    phoneNumbers[i] = $("input[name='phone']").eq(i).val()
  }

  let memberInfo = {
    'email' : $('#email').val(),
    'password' : $('#password').val(),
    'nickName' : $('#nick_name').val(),
    'phone' : phoneNumbers.join('-'),
    'birth' : $('#birth').val(),
    'gender' : $("input[name='gender']:radio:checked").val()
  }

  let formData = new FormData()
  let memberInfoJson = JSON.stringify(memberInfo)
  let blob = new Blob([memberInfoJson], {type: "application/json"})

  let profileImgFiles = $('#profile_img_file')[0].files;
  let profileImgFile
      = (profileImgFiles.length !== 0)
      ? profileImgFiles[0]
      : null

  formData.append("memberInfo", blob)
  formData.append('profileImgFile', profileImgFile)

  $.ajax({
    url: '/api/v2/members',
    method: 'POST',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: (data) => {
      console.log(data)
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const joinSubmit = () => {

  // 이메일
  if (!emailValidation()) {
    return false
  }

  let email = $('#email')
  if (!email.prop('disabled')) {
    let emailErrorMessage = email.closest('.Home_form_div').find('.error-message')
    emailErrorMessage.css('display', 'block')
    emailErrorMessage.text('❌이메일 중복확인을 해주세요')
    return false
  }

  // 비밀번호
  if (!passwordValidation()) {
    return false
  }

  // 비밀번호 확인
  if (!passwordMatch()) {
    return false
  }

  // 닉네임
  if (!nickNameValidation()) {
    return false
  }

  //성별
  if (!genderValidation()) {
    return false
  }

  // 휴대폰 번호
  if (!phoneNumberLengthLimit()) {
    return false
  }

  // 생년월일
  if (!birthValidation()) {
    return false
  }

  // 이미지 파일
  if (!previewProfile()) {
    return false
  }

  // 회원가입 api 호출
  requestJoinApi()
}
