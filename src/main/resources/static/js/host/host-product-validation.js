
// 상품 정보 선택자 객체
let productInfo = {}


// 해당 선택자의 에러 메세지 요소 찾기
const findErrorMassageElement = (selector) => {
  return selector.closest('.item2').find('.error-message')
}


// 에러 메세지 보이기
const showErrorMessage = (selector, message) => {
  let errorMessageElement = findErrorMassageElement(selector)
  errorMessageElement.css('display', 'block');
  errorMessageElement.text(message);
  selector.focus()
}


// 에러 메세지 보이기
const showErrorMessageAndScroll = (selector, message) => {
  let errorMessageElement = findErrorMassageElement(selector)
  errorMessageElement.css('display', 'block');
  errorMessageElement.text(message);
  $('html, body').animate({
    scrollTop: selector.offset().top // 특정 요소로 스크롤
  }, 100); // 애니메이션 시간
}


// 에러 메세지 숨기기
const hideErrorMessage = (selector) => {
  let errorMessageElement = findErrorMassageElement(selector)
  errorMessageElement.css('display', 'none')
}


// 필수 입력 확인
const isNotBlank = (selector) => {
  let errorMessageElement = findErrorMassageElement(selector)
  if (selector.val().length === 0 || selector.val() === '') {
    selector.focus()
    showErrorMessage(errorMessageElement, '필수 입력란 입니다')
    return false
  }
  hideErrorMessage(errorMessageElement)
  return true
}


// 카테고리 선택 확인
const categorySelectVerify = () => {
  let categoryLarge = productInfo.$categoryLarge
  let categoryMiddle = productInfo.$categoryMiddle

  if (categoryLarge.val() === '-' || categoryMiddle.val() === '-') {
    showErrorMessage(categoryLarge, '카테고리를 선택해주세요')
    return false
  }

  hideErrorMessage(categoryLarge)
  return true
}


// 해빗명 확인
const productNameVerify = () => {
  let productName = productInfo.$productName
  let productNameValue = productInfo.$productName.val()

  if (productNameValue.length > 40) {
    productName.val(productNameValue.slice(0, 40))
  }

  return isNotBlank(productName)
}


// 진행 장소 확인
const addressVerify = () => {
  return isNotBlank(productInfo.$zipcode)
}


// 해빗 간단 정보 확인
const tagVerify = () => {
  let tabBox = productInfo.$tagBox
  let tagAge = $('input:checkbox[name=tagAge]:checked')
  let tagWith = $('input:checkbox[name=tagWith]:checked')

  if (tagAge.length === 0 || tagWith.length === 0) {
    showErrorMessageAndScroll(tabBox, '필수 선택입니다')
    return false
  }

  hideErrorMessage(tabBox)
  return true
}


// 판매 종료일 확인
const productClosedAtVerify = () => {
  return isNotBlank(productInfo.$closedAt)
}


// 옵션 확인
const optionInstanceVerify = (optionElements, maxLength) => {
  for (let i = 0; i < optionElements.length; i++) {
    let target = $(optionElements[i])
    if (!isNotBlank(target)) {
      return false
    }

    if (target.val().length > maxLength) {
      target.val(target.val().slice(0, maxLength))
      target.val().slice(0, maxLength)
      hideErrorMessage(target)
    }
  }

  hideErrorMessage($(optionElements[0]))
  return true
}

// 옵션 목록 확인
const optionsVerify = () => {
  let optionNamesReservation = $('input[type=datetime-local][name=optionName]')
  let optionNamesPass = $('input[type=text][name=optionName]')
  let optionQuantities = $('[name=optionQuantity]')
  let optionPrices = $('[name=optionPrice]')

  for (const element of optionNamesReservation) {
    if (!isNotBlank(optionNamesReservation)) {
      return false
    }
  }

  return optionInstanceVerify(optionNamesPass, 15)
      || optionInstanceVerify(optionQuantities, 4)
      || optionInstanceVerify(optionPrices, 7)
}

// 대표 이미지 확인
const imageVerify = () => {
  let preview = productInfo.$preview
  let image = productInfo.$image
  let images = image[0].files
  if(images.length === 0 || images.length > 3) {
    showErrorMessageAndScroll(preview, '이미지 파일을 1~3장 이내로 첨부해주세요')
    image.val('')
    return false
  }

  hideErrorMessage(preview)
  return true
}


// 해빗 상세 설명 확인
const descriptionVerify = () => {
  let summerNoteContainer = $('#summerNoteContainer')
  let description = productInfo.$description
  let tagRemoveText = f_SkipTags_html(description.val())

  if(tagRemoveText.length < 10) {
    showErrorMessageAndScroll(summerNoteContainer, '해빗 상세 설명을 10자 이상 입력해주세요');
    return false
  }

  hideErrorMessage(summerNoteContainer)
  return true
}


$(document).ready(() => {
  // 상품 정보 선택자 객체
  productInfo = {
    $categoryLarge: $('#categoryLarge'),
    $categoryMiddle: $('#categoryMiddle'),
    $productName: $('#productName'),
    $zipcode: $('#zipcode'),
    $address1: $('#address1'),
    $address2: $('#address2'),
    $extraAddress: $('#extraAddress'),
    $closedAt: $('#closedAt'),
    $image: $('#image'),
    $description: $('#summernote')
  }

  $('#findAddressButton').on('click', () => {
    let zipcode = productInfo.$zipcode
    hideErrorMessage(zipcode)
  })

  $(document).on('input', 'input[type=text][name=optionName]', (e) => {
    let targets = $(e.currentTarget)
    optionInstanceVerify(targets, 15)
  })

  $(document).on('input', 'input[type=datetime-local][name=optionName]', (e) => {
    let targets = $(e.currentTarget)
    for (const target of targets) {
      if (!isNotBlank($(target))) {
        return
      }
    }
  })

  $(document).on('input', '[name=optionQuantity]', (e) => {
    let targets = $(e.currentTarget)
    optionInstanceVerify(targets, 4)

  })

  $(document).on('input', '[name=optionPrice]', (e) => {
    let targets = $(e.currentTarget)
    optionInstanceVerify(targets, 7)
  })
})
