const isNotBlank = (value, errorMessage) => {
    if (value.length === 0 || value === '') {
        errorMessage.css('display', 'block')
        errorMessage.text('❌필수 입력란 입니다')
        return false
    }
    errorMessage.css('display', 'none')
    return true
}

// 이미지 파일 검증
const previewProfile = (imgs) => {
    let reader = new FileReader();
    let profileImageErrorMessage = $('#profileImageFile').closest('.Home_form_div').find('.error-message')

    if (imgs !== undefined && imgs.files.length > 0) {
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

// 닉네임 검증
const nickNameValidation = () => {
    let nickName = $('#nickName')
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

// 휴대폰 번호 검증
const phoneValidation = () => {
    let phone = $('#phone')
    let phoneErrorMessage = phone.closest('.Home_form_div').find('.error-message')

    if (!isNotBlank(phone.val(), phoneErrorMessage)) {
        return false
    }

    let phoneCheck = /^(010)[0-9]{3,4}[0-9]{4}$/
    if (!phoneCheck.test(phone.val())) {
        phoneErrorMessage.css('display', 'block')
        phoneErrorMessage.text('❌휴대폰 번호를 확인해주세요')
        return false
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

    let year = birth.val().split('-')[0]
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

// 비밀번호 검증
const passwordValidation = () => {
    let password = $('#password')
    let passwordErrorMessage = $('.modal-password-error-message')

    if (!isNotBlank(password.val(), passwordErrorMessage)) {
        return false
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

const callUpdateInfoApi = () => {

    let updateInfo = {
        'nickName' : $('#nickName').val(),
        'phone' : $('#phone').val(),
        'birth' : $('#birth').val(),
        'gender' : $("input[name='gender']:radio:checked").val()
    }

    let formData = new FormData()
    let updateInfoJson = JSON.stringify(updateInfo)
    let blob = new Blob([updateInfoJson], {type: "application/json"})

    let profileImgFiles = $('#profileImageFile')[0].files;
    let profileImgFile
        = (profileImgFiles.length !== 0)
        ? profileImgFiles[0]
        : null

    formData.append("updateInfo", blob)
    formData.append('profileImgFile', profileImgFile)

    $.ajax({
        url: '/api/v2/members/' + altId,
        method: 'PATCH',
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: formData,
        success: (response) => {
            alert("회원 정보가 수정되었습니다.")
            location.href = "/my-page/info"
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

const updateInfoSubmit = () => {

    // 이미지 파일
    if (!previewProfile()) {
        return false
    }

    // 닉네임
    if (!nickNameValidation()) {
        return false
    }

    // 휴대폰 번호
    if (!phoneValidation()) {
        return false
    }

    // 생년월일
    if (!birthValidation()) {
        return false
    }

    // 회원 정보 수정 api 호출
    callUpdateInfoApi()
}

const verifyPasswordModalOpen = () => {
    let passwordCheck = getCookie('password_check')
    let passwordCheckSession = getCookie('password_check_session')

    if (passwordCheck !== null && passwordCheckSession !== null) {
        if (passwordCheck === 'ok' && passwordCheckSession === 'ok') {
            window.location.href = '/my-page/password'
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
            setCookie('password_check', 'ok', 5)
            setCookie('password_check_session', 'ok')
            window.location.href = '/my-page/password'
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
    let isFormSubmitted = false

    $('#memberInfoUpdateButton').on('click', () => {
        updateInfoSubmit()
        isFormSubmitted = true
    })

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

    // 모달 창 오픈
    $('.password-change').on('click', () => {
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
