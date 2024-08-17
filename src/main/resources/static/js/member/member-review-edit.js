// 기존의 리뷰 객체
let previousReview = {}

const resetPreviewImage = ($previewContainer, $previewInput) => {
  $previewContainer.children().remove()
  $previewContainer.text('+')
  $previewInput.val('')
}

const resetModal = () => {
  // 모달창 닫기
  $('.modal-review-edit-background').css('display', 'none')

  // 별점 초기화
  let starImages = $('.modal-review-edit-star-image')
  for (const starImage of starImages) {
    $(starImage).attr('src', '/img/review-star-empty.png')
  }

  // 사진 업로드 미리보기 초기화
  let previewElements = Array.from($('.modal-review-edit-image-preview-container').children())
  for (let i = 0; i < 3; i++) {
    resetPreviewImage($(previewElements[i]), $(`#modalInputImage${i}`))
  }

  // 리뷰 아이디 초기화
  $('#reviewAltId').val('')

  $('.modal-review-edit-submit-button').removeClass('active')

  // 기존의 리뷰 객체 초기화
  previousReview = {}

  // 에러 메세지 숨기기
  let errorMassageElements = $('.error-message')
  for (const element of errorMassageElements) {
    let $element = $(element)
    $element.css('display', 'none')
    $element.text('')
  }
}

const starScoreRender = (index, stars) => {
  for (let i = 0; i <= 4; i++) {
    let now = $(stars[i])
    if (i <= index) {
      now.attr('src', '/img/review-star-fill.png');
      now.addClass('select');
    } else {
      now.attr('src', '/img/review-star-empty.png');
      now.removeClass('select');
    }
  }

  let chooseScore = $('.modal-review-edit-guide')
  switch (index) {
    case 0:
      chooseScore.text('1점 (별로예요)')
      break;
    case 1:
      chooseScore.text('2점 (그저그래요)')
      break;
    case 2:
      chooseScore.text('3점 (괜찮아요)')
      break;
    case 3:
      chooseScore.text('4점 (좋아요)')
      break;
    default:
      chooseScore.text('5점 (최고예요)')
  }
}

const addPreviewImageElement = (previewContainer, filePath, className) => {
  previewContainer.text('')
  previewContainer.append('<img class="' + className + '" src="' + filePath + '">')
}

const appendReviewImages = (reviewImageList) => {
  let previewImageContainers = $('.modal-review-edit-image-preview')
  for (let i = 0; i < reviewImageList.length; i++) {
    let filePath = '/storage/' + reviewImageList[i]
    let nowPreviewContainer = $(previewImageContainers[i])
    addPreviewImageElement(nowPreviewContainer, filePath, 'modal-preview-image saved')
  }
}

const initReviewModal = (review, reviewAltId, productName) => {
  // 수정 전 리뷰 객체
  previousReview = review

  // 리뷰 대체키 저장
  $('#reviewAltId').val(reviewAltId)

  // 상품 이름
  $('.modal-review-edit-product-name').text(productName)

  // 별점
  let children = Array.from($('.modal-review-edit-star-image-container').children())
  const index = review.starScore - 1
  starScoreRender(index, children);

  // 리뷰 내용
  $('.modal-review-edit-content').val(review.content)

  // 리뷰 이미지
  let reviewImageList = getImageFileNameList(review.image)
  appendReviewImages(reviewImageList)
}

const hasReviewChanged = () => {
  let previousReviewImages = getImageFileNameList(previousReview.image)
  let nowContent = $('.modal-review-edit-content').val()
  let nowImages = $('.modal-preview-image')
  let nowStarScore = $('.select').length
  if (previousReview.content !== nowContent || previousReview.starScore !== nowStarScore || previousReviewImages.length !== nowImages.length) {
    return true
  }

  for (let i = 0; i < nowImages.length; i++) {
    let nowImageUrl = nowImages[i].src
    let nowImageName = nowImageUrl.substring(nowImageUrl.lastIndexOf('/') + 1)
    let findImage = previousReviewImages.find(value => value === nowImageName)
    if (findImage === undefined) {
      return true
    }
  }

  return false
}

const getReviewValues = () => {
  let starScore = $('.select').length
  let content = $('.modal-review-edit-content').val()
  let imageNames = []
  let images = $('.saved')
  for (const image of images) {
    let nowImageUrl = image.src
    let nowImageName = nowImageUrl.substring(nowImageUrl.lastIndexOf('/') + 1)
    imageNames.push(nowImageName)
  }

  let formData = new FormData();
  for (let i = 0; i < 3; i++) {
    let file = $(`#modalInputImage${i}`)[0].files[0]
    if (file !== undefined) {
      formData.append('imageFiles', file)
    }
  }

  let reviewUpdateInfo = JSON.stringify({
    starScore: starScore,
    content: content,
    image: imageNames.join('|')
  })
  let blob = new Blob([reviewUpdateInfo], {type:"application/json"})
  formData.append('reviewUpdateDto', blob)

  return formData
}

const setSubmitButtonState = () => {
  let submitButton = $('.modal-review-edit-submit-button')
  if (hasReviewChanged()) {
    submitButton.addClass('active')
  } else {
    submitButton.removeClass('active')
  }
}

const callGetReviewApi = (reviewAltId, productName) => {
  $.ajax({
    url: '/api/v2/reviews/' + reviewAltId,
    method: 'GET',
    success: (response) => {
      initReviewModal(response.data, reviewAltId, productName)
      $('.modal-review-edit-background').css('display', 'flex')
    },
    error: () => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const callDeleteReviewApi = (reviewAltId) => {
  $.ajax({
    url: '/api/v2/reviews/' + reviewAltId,
    method: 'DELETE',
    success: () => {
      alert('리뷰가 삭제되었습니다.')

      $('.review-cards-container').children().remove()
      page = 1
      callGetReviewsApi()
    },
    error: () => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const callUpdateReviewApi = (reviewAltId) => {
  let formData = getReviewValues()
  $.ajax({
    url: '/api/v2/reviews/' + reviewAltId,
    method: 'PUT',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: () => {
      alert('리뷰 수정이 완료되었습니다.')
      resetModal()
      window.location.href = '/my-page/review-list'
    },
    error: () => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {
  // 리뷰 삭제 버튼 클릭 이벤트
  $(document).on('click', '.review_delete_btn', (e) => {
    if (confirm("리뷰를 삭제하시겠습니까?")) {
      let reviewAltId = $(e.currentTarget).parent().children('.review-alt-id').val()
      callDeleteReviewApi(reviewAltId)
    }
  })

  // 리뷰 수정 버튼 클릭 이벤트 - 모달창 열기
  $(document).on('click', '.review_modify_btn', (e) => {
    let parentElement = $(e.currentTarget).parent()
    let reviewAltId = parentElement.children('.review-alt-id').val()
    let productName = parentElement.parent().children('.review-product-name').children('.product-name-container').children('.product-name').text()
    callGetReviewApi(reviewAltId, productName)
  })

  // 모달창 닫기
  $('.modal-review-edit-cancel-button, .modal-exit').on('click', () => {
    if (hasReviewChanged()) {
      if (confirm("입력하신 정보가 저장되지 않습니다. 리뷰 수정을 취소하시겠습니까?")) {
        resetModal()
      }
    } else {
      resetModal()
    }
  })

  // 별점 마우스 오버 이벤트
  $(document).on('mouseover', '.modal-review-edit-star-image', (e) => {
    let target = e.target
    let children = Array.from($('.modal-review-edit-star-image-container').children())
    const index = children.indexOf(target)
    for (let i = 0; i <= 4; i++) {
      let now = $(children[i])
      if (!now.hasClass('select')) {
        if (i <= index) {
          now.attr('src', '/img/review-star-fill.png');
        } else {
          now.attr('src', '/img/review-star-empty.png');
        }
      }
    }
  })

  // 별점 클릭 이벤트
  $(document).on('click', '.modal-review-edit-star-image', (e) => {
    let target = e.target;
    let children = Array.from($('.modal-review-edit-star-image-container').children())
    const index = children.indexOf(target)
    starScoreRender(index, children);
    setSubmitButtonState()
  })

  $('.modal-review-edit-star-image-container').on('mouseout', () => {
    let children = Array.from($('.modal-review-edit-star-image-container').children())
    for (let i = 0; i <= 4; i++) {
      let now = $(children[i])
      if (!now.hasClass('select')) {
        now.attr('src', '/img/review-star-empty.png');
        now.removeClass('select');
      }
    }
  })

  // 리뷰 글자 수 제한
  $('.modal-review-edit-content').on('input', () => {
    let content = $('.modal-review-edit-content')
    if (content.val().length > 1200) {
      content.val(content.val().substring(0, 1200))
    }
    $('.content-length').text(content.val().length)
    setSubmitButtonState()
  })

  // 사진 첨부
  $(document).on('input', '.modal-review-edit-image-preview-input', (e) => {
    let target = e.target;
    let file = $(target)[0].files[0]

    let inputElements = Array.from($('.modal-review-edit-image-preview-input-container').children())
    let index = inputElements.indexOf(target)

    let previewElements = Array.from($('.modal-review-edit-image-preview-container').children())
    let $appendImage = $(previewElements[index])

    if (file !== undefined) {
      let filePath = URL.createObjectURL(file);
      addPreviewImageElement($appendImage, filePath, 'modal-preview-image')
    } else {
      resetPreviewImage($appendImage, $(target));
    }
    setSubmitButtonState()
  })

  // 뒤로가기 버튼 클릭시 - 모달창이 열려있는경우
  $(window).on('beforeunload', (e) => {
    if ($('.modal-review-edit-background').css('display') === 'flex') {
      if (hasReviewChanged()) {
        // 사용자 정의 메시지를 설정하더라도 최신 브라우저에서는 무시됨
        let message = "변경사항이 저장되지 않을 수 있습니다.";
        e.returnValue = message;
        return message;
      }
    }
  })

  // 리뷰 수정 확인 버튼 클릭
  $(document).on('click', '.modal-review-edit-submit-button.active', (e) => {
    if (reviewValidations()) {
      if (confirm("리뷰를 수정하시겠습니까?")) {
        let reviewAltId = $(e.target).parent().children('#reviewAltId').val();
        callUpdateReviewApi(reviewAltId);
      }
    }
  })
})
