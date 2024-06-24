// 호스트 정보 선택자 객체
let hostInfo = {}
let banksPattern = {}

// 해당 선택자의 에러 메세지 요소 찾기
const findErrorMassageElement = (selector) => {
  return selector.closest('.item2').find('.error-message')
}

// 에러 메세지 보이기
const showErrorMessage = (errorMessageElement, message) => {
  errorMessageElement.css('display', 'block');
  errorMessageElement.text(message);
}

// 에러 메세지 숨기기
const hideErrorMessage = (errorMessageElement) => {
  errorMessageElement.css('display', 'none')
}

// 필수 입력 확인
const isNotBlank = (value, errorMessageElement) => {
  if (value.length === 0 || value === '') {
    showErrorMessage(errorMessageElement, '필수 입력란 입니다')
    return false
  }
  hideErrorMessage(errorMessageElement)
  return true
}

// 이미지 파일 검증
const previewProfile = (imgs) => {
  let reader = new FileReader();
  let profileImageErrorMessage = findErrorMassageElement(hostInfo.$image)

  if (imgs !== undefined && imgs.files.length > 0) {
    let imgSize = imgs.files[0].size // 이미지 사이즈
    let maxSize = 2 * 1024 *1024 // 2MB
    let fileExtension = imgs.files[0].name.split('\.').slice(-1)[0] // 파일 확장자

    if (imgSize > maxSize || (fileExtension !== 'png' && fileExtension !== 'jpg')) {
      showErrorMessage(profileImageErrorMessage, '2MB 이하의 png, jpg 파일만 가능합니다')
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

  hideErrorMessage(profileImageErrorMessage)
  return true
}

// 닉네임 검증
const nickNameValidation = () => {
  let nickNameValue = hostInfo.$nickName.val()
  let nickNameErrorMessage = findErrorMassageElement(hostInfo.$nickName)

  if (!isNotBlank(nickNameValue, nickNameErrorMessage)) {
    return false
  }

  if (nickNameValue.length === 1 || nickNameValue.length > 8) {
    showErrorMessage(nickNameErrorMessage, '닉네임은 2 ~ 8자 입니다');
    return false
  }

  let nickNameCheck = /^[ㄱ-ㅎ가-힣a-zA-Z0-9_]+$/
  if (!nickNameCheck.test(nickNameValue) && nickNameValue.length > 0) {
    showErrorMessage(nickNameErrorMessage,'"_" 외의 특수문자는 사용할 수 없습니다')
    return false
  }

  hideErrorMessage(nickNameErrorMessage)
  return true
}

// 이메일 검증
const emailValidation = () => {
  let emailValue = hostInfo.$email.val()
  let emailErrorMessage = findErrorMassageElement(hostInfo.$email)

  let emailCheck = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/
  if (emailCheck.test(emailValue) === false && emailValue.length > 0) {
    showErrorMessage(emailErrorMessage, '이메일 형식에 맞게 입력해주세요')
    return false
  }

  hideErrorMessage(emailErrorMessage)
  return true
}

// 공개 연락처 검증
const phoneValidation = () => {
  let phoneValue = hostInfo.$phone.val()
  let phoneErrorMessage = findErrorMassageElement(hostInfo.$phone)

  let containsSpecialChars = /-/
  if (containsSpecialChars.test(phoneValue)) {
    phoneErrorMessage.css('display', 'block')
    phoneErrorMessage.text('"-"기호를 제외한 번호만 입력해주세요')
    return false
  }

  let phoneCheck = /^(010)[0-9]{3,4}[0-9]{4}$/
  if (!phoneCheck.test(phoneValue) && phoneValue.length > 0) {
    showErrorMessage(phoneErrorMessage, '연락처를 확인해주세요')
    return false
  }

  hideErrorMessage(phoneErrorMessage)
  return true
}

// 소개글 검증
const introductionValidation = () => {
  let introduction = hostInfo.$introduction
  let introductionErrorMessage = findErrorMassageElement(hostInfo.$introduction)

  if (introduction.val().length > 500) {
    introduction.val(introduction.val().slice(0, 500))
  }
  hostInfo.$introductionLength.text(introduction.val().length)

  if (!isNotBlank(introduction.val(), introductionErrorMessage)) {
    return false
  }

  if (introduction.val().length > 0 && introduction.val().length < 5) {
    showErrorMessage(introductionErrorMessage, '소개글을 5자 이상 입력해주세요');
    return false
  }

  hideErrorMessage(introductionErrorMessage)
  return true
}

// 예금주 검증
const accountHolderValidation = () => {
  let accountHolderValue = hostInfo.$accountHolder.val()
  let accountHolderErrorMessage = findErrorMassageElement(hostInfo.$accountHolder)

  if (!isNotBlank(accountHolderValue, accountHolderErrorMessage)) {
    return false
  }

  if (accountHolderValue.length < 2 || accountHolderValue.length > 10) {
    showErrorMessage(accountHolderErrorMessage, '예금주는 2 ~ 10자 입니다');
    return false
  }

  let accountHolderCheck = /^[ㄱ-ㅎ가-힣a-zA-Z!@#$%^&*()_\-\[\]=+~'";:,<>./?]+$/
  if (!accountHolderCheck.test(accountHolderValue)) {
    showErrorMessage(accountHolderErrorMessage,'사용할 수 없는 문자입니다')
    return false
  }

  hideErrorMessage(accountHolderErrorMessage);
  return true
}

// 은행 선택 검증
const selectBankValidation = () => {
  let bankValue = hostInfo.$bank.val()
  let bankErrorMessage = findErrorMassageElement(hostInfo.$bank)

  if (bankValue === '0') {
    showErrorMessage(bankErrorMessage, '은행을 선택해주세요')
    return false
  }

  hideErrorMessage(bankErrorMessage)
  return true
}

// 계좌번호 검증 → 은행 선택이 선행되야함
const accountNumberValidation = () => {

  let accountNumberCheck = /[^0-9]/g
  hostInfo.$accountNumber.val(hostInfo.$accountNumber.val().replace(accountNumberCheck, ''))

  if (!selectBankValidation()) {
    return false
  }

  let bankValue = hostInfo.$bank.val()
  let accountNumberValue = hostInfo.$accountNumber.val()
  let accountNumberErrorMessage = findErrorMassageElement(hostInfo.$accountNumber)

  let accountNumberPatternCheck = banksPattern[bankValue];
  if (!accountNumberPatternCheck.test(accountNumberValue)) {
    showErrorMessage(accountNumberErrorMessage, '선택한 은행의 계좌번호 형식과 맞지 않습니다')
    return false
  }

  hideErrorMessage(accountNumberErrorMessage)
  return true
}

// 은행 목록 가져오기
const getBankList = () => {
  $.ajax({
    url: '/api/v2/banks',
    method: 'GET',
    dataType: 'json',
    success: (response) => {
      banks = response.data
      for (const bank of banks) {
        let selected = ''
        if (bank.code === hostBank) {
          selected = "selected"
        }
        $('#bank').append("<option value='"+ bank.code +"' " + selected + ">" + bank.name + "</option>");
      }
    }
  })
}

$(document).ready(() => {
  // 호스트 정보 선택자 객체
  hostInfo = {
    $image: $('#image'),
    $preview: $('#preview'),
    $nickName: $('#nickName'),
    $email: $('#email'),
    $phone: $('#phone'),
    $introduction: $('#introduction'),
    $introductionLength: $('#introduction-length'),
    $bank: $('#bank'),
    $accountHolder: $('#accountHolder'),
    $accountNumber: $('#accountNumber')
  }

  // 은행 정규표현식 리스트
  banksPattern = {
    '011' : /^[0-9]{3}[0-9]{4}[0-9]{4}[0-9]{2}$/,
    '004' : /^[0-9]{6}[0-9]{2}[0-9]{6}$/,
    '020' : /^[0-9]{4}[0-9]{3}[0-9]{6}$/,
    '088' : /^[0-9]{3}[0-9]{3}[0-9]{6}$/,
    '003' : /^[0-9]{3}[0-9]{6}[0-9]{2}[0-9]{3}$/,
    '081' : /^[0-9]{3}[0-9]{6}[0-9]{5}$/,
    '090' : /^[0-9]{4}[0-9]{2}[0-9]{7}$/
  }

  getBankList()
})
