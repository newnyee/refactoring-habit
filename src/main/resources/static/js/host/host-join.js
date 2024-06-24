// 닉네임 중복 확인
const nickNameDuplicateCheck = () => {
  let nickName = hostInfo.$nickName
  let nickNameErrorMessage = findErrorMassageElement(nickName)
  let nickNameDuplicateCheckButton = $('#nick_name_duplicate_check_button')
  let nickNameDuplicateSuccessMassage = $('#nick_name_duplicate_success')

  if (nickNameDuplicateCheckButton.text() === '닉네임 변경') {
    nickNameDuplicateSuccessMassage.css('display', 'none')
    nickNameDuplicateCheckButton.text('중복확인')
    nickNameDuplicateCheckButton.removeClass('btn-outline-primary').addClass('btn-primary')
    nickName.prop('disabled', false)
    return false
  }

  if (!nickNameValidation()) {
    return false
  }

  $.ajax({
    url: '/api/v2/hosts/check-nick-name',
    method: 'GET',
    data: {
      nickName: nickName.val()
    },
    success: (response) => {
      if (response.data) {
        nickNameErrorMessage.css('display', 'block')
        nickNameErrorMessage.text('❌이미 사용중인 이메일입니다')
      } else {
        nickNameErrorMessage.css('display', 'none')
        nickName.prop('disabled', true)

        nickNameDuplicateCheckButton.text('닉네임 변경')
        nickNameDuplicateCheckButton.removeClass('btn-primary').addClass('btn-outline-primary')
        nickNameDuplicateSuccessMassage.css('display', 'block')
      }
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

// 호스트 가입 api 호출
const callHostJoinApi = () => {

  let requestHostInfo = {
    nickName: hostInfo.$nickName.val(),
    email: hostInfo.$email.val(),
    phone: hostInfo.$phone.val(),
    introduction: hostInfo.$introduction.val(),
    bank: hostInfo.$bank.val(),
    accountHolder: hostInfo.$accountHolder.val(),
    accountNumber: hostInfo.$accountNumber.val()
  }

  let formData = new FormData()
  let requestHostInfoJson = JSON.stringify(requestHostInfo)
  let blob = new Blob([requestHostInfoJson], {type: "application/json"})

  let profileImgFiles = hostInfo.$image[0].files;
  let profileImgFile
      = (profileImgFiles.length !== 0)
      ? profileImgFiles[0]
      : null

  formData.append('hostInfo', blob)
  formData.append('profileImgFile', profileImgFile)

  $.ajax({
    url: '/api/v2/hosts',
    method: 'POST',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: () => {
      alert("호스트 가입이 완료되었습니다.")
      window.location.replace("/host")
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {

  // 호스트 가입 버튼 클릭
  $('#host-join-submit-button').on('click', () => {

    // 닉네임 중복 검사 확인
    let nickName = hostInfo.$nickName
    if (!nickName.prop('disabled')) {
      let nickNameErrorMessage = findErrorMassageElement(nickName)
      showErrorMessage(nickNameErrorMessage)
      return false
    }

    // 유효성 검사
    if (!(nickNameValidation() && emailValidation()
        && phoneValidation() && introductionValidation()
        && accountHolderValidation() && selectBankValidation()
        && accountNumberValidation())) {
      return false
    }

    // 호스트 가입 api 호출
    callHostJoinApi()
  })
})
