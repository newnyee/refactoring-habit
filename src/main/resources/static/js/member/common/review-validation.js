const getErrorElement = (containerElement) => {
  return containerElement.children('.error-message')
}

const showErrorMessage = (errorMessageElement, message) => {
  errorMessageElement.css('display', 'block')
  errorMessageElement.text(message)
  return false
}

const hiddenErrorMessage = (errorMessageElement) => {
  errorMessageElement.css('display', 'none')
  errorMessageElement.text('')
  return true
}

const starScoreValidation = (elements) => {
  let errorMessageElement = getErrorElement($('.modal-review-edit-star-score-container'))
  if (elements.length === 0) {
    return showErrorMessage(errorMessageElement, '별점을 선택해주세요')
  }
  return hiddenErrorMessage(errorMessageElement)
}

const contentValidation = (element) => {
  let errorMessageElement = getErrorElement($('.modal-review-edit-content-container'))
  let contentLength = element.val().length
  if (contentLength < 5 || contentLength > 1200) {
    element.focus()
    if (contentLength === 0) {
      return showErrorMessage(errorMessageElement, '필수 입력입니다')
    } else {
      return showErrorMessage(errorMessageElement, '5 ~ 1200자 이내로 작성해주세요')
    }
  }
  return hiddenErrorMessage(errorMessageElement)
}

const imagesValidation = (elements) => {
  let errorMessageElement = getErrorElement($('.modal-review-edit-attach-image-container'))
  if (elements.length === 0) {
    return showErrorMessage(errorMessageElement, '1장 이상의 이미지를 첨부해주세요')
  }
  return hiddenErrorMessage(errorMessageElement)
}

const reviewValidations = () => {
  let starScoreElements = $('.select')
  let contentElement = $('.modal-review-edit-content')
  let imageElements = $('.modal-preview-image')
  return starScoreValidation(starScoreElements) && contentValidation(contentElement) && imagesValidation(imageElements)
}
