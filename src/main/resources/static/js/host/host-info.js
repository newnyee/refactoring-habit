// 호스트 정보 수정 api 호출
const callHostInfoUpdateApi = () => {

  let requestHostInfo = {
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
    url: '/api/v2/hosts/' + hostAltId,
    method: 'PUT',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: () => {
      alert("호스트 정보 수정이 완료되었습니다.")
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
  let isFormSubmitted = false

  // 호스트 정보 수정 버튼 클릭
  $('#host-info-update-button').on('click', () => {

    // 유효성 검사
    if (!(emailValidation() && phoneValidation() && introductionValidation()
        && accountHolderValidation() && selectBankValidation()
        && accountNumberValidation())) {
      return false
    }

    if (confirm("호스트 정보를 수정하시겠습니까?")) {
      callHostInfoUpdateApi()
      isFormSubmitted = true
    }
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