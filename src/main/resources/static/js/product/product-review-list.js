const addCallGetReviewsByProductIdApiMethod = (pageNumber) => {
  return ' onclick="callGetReviewsByProductIdApi(' + pageNumber + ')"'
}

const createPageArrowButton = (buttonType, buttonStatus, pageNumber) => {
  let element = '          <button class="paging-button-' + buttonStatus + '"'
  if (buttonStatus === 'active') {
    element += addCallGetReviewsByProductIdApiMethod(pageNumber)
  }
  if (buttonType === 'prev') {
    element += '><</button>\n'
  } else {
    element += '>></button>'
  }
  return element;
}

const createPageNumberButton = (pageNumber, addClass) => {
  return '          <button onclick="callGetReviewsByProductIdApi(' + pageNumber + ')" class="paging-button-active '+ addClass +'">' + pageNumber + '</button>\n'
}

const addPageButton = (pagingWrapper, currentPage, totalRecord) => {
  let displayPageNumber = paging.displayPageNumber
  let totalPages = Math.ceil(totalRecord / paging.recordPerPage)
  let startPage = (Math.ceil(currentPage / displayPageNumber) - 1) * displayPageNumber + 1
  let endPage = (Math.ceil(currentPage / displayPageNumber) * displayPageNumber)
  if (endPage > totalPages) {
    endPage = totalPages
  }
  let prevStatus = (startPage === 1) ? 'disabled' : 'active'
  let nextStatus = (endPage === totalPages) ? 'disabled' : 'active'

  pagingWrapper.append(createPageArrowButton('prev', prevStatus, startPage - 1))
  for (let i = startPage; i <= endPage; i++) {
    if (i === currentPage) {
      pagingWrapper.append(createPageNumberButton(i, 'now'));
      continue
    }
    pagingWrapper.append(createPageNumberButton(i));
  }
  pagingWrapper.append(createPageArrowButton('next', nextStatus, endPage + 1))
}

const createReviewCard = (review) => {
  return '        <div class="review-card">\n'
      + '          <div class="review-card-header">\n'
      + '            <div class="member-info-container">\n'
      + '              <div class="member-profile-image-container">\n'
      + '                <img class="member-profile-image" src="/storage/' + review.memberProfileImage + '">\n'
      + '              </div>\n'
      + '              <div class="member-profile-container">\n'
      + '                <div class="member-nick-name">' + review.memberNickName + '</div>\n'
      + '                <div class="review-star-score-container">\n'
      + '                  <div class="review-star-score-image-container">\n'
      + createStarScoreImage(review.starScore)
      + '                  </div>\n'
      + '                  <span class="review-update-at">' + formatToYearMonthDayHour(review.updateAt) + ' 작성 ' + isModifiedReview(review) + '</span>\n'
      + '                </div>\n'
      + '              </div>\n'
      + '            </div>\n'
      + '          </div>\n'
      + '          <div class="product-and-option-name-container">\n'
      + '            <div class="product-name-container"><a class="product-name" href="/product/' + review.productAltId + '">' + review.productName + '</a></div>\n'
      + '            <p class="option-name">' + review.optionName + ' 참여</p>\n'
      + '          </div>\n'
      + '          <div class="review-card-content">\n'
      + '            <p class="review-content-text">' + review.content + '</p>\n'
      + '          </div>\n'
      + '          <div class="review-img-container">\n'
      + createReviewImageElements(review.image)
      + '          </div>\n'
      + '        </div>'
}

const updateOrderByButtonText = () => {
  let currentTargetValue = $('[name=orderBy]:checked').val()
  let value = ''
  if (currentTargetValue === 'createAt') {
    value += '최신순'
  } else if (currentTargetValue === 'reviewAveragedDesc') {
    value += '평점 높은순'
  } else {
    value += '평점 낮은순'
  }
  $('.order-by-button').text(value)
}

const callGetReviewsByProductIdApi = (page) => {
  updateOrderByButtonText()
  let requestUrl = '/api/v2/products/' + productAltId + '/reviews'
  if (page) {
    let pageNumber = Number(page) - 1
    requestUrl += '?page=' + pageNumber + '&size=' + paging.recordPerPage
  }
  requestUrl += '&order-by=' + $('[name=orderBy]:checked').val()

  $.ajax({
    url: requestUrl,
    method: 'GET',
    success: (response) => {
      let simpleProductInfo = response.data.simpleProductInfoDto
      let reviewList = response.data.reviewDetailDtos
      let totalRecord = response.data.reviewCount

      let reviewAverageImageContainer = $('.review-average-image-container')
      reviewAverageImageContainer.children().remove()
      reviewAverageImageContainer.append(createStarScoreImage(simpleProductInfo.reviewAverage))
      $('.review-count').text(totalRecord)
      $('.review-average').text(simpleProductInfo.reviewAverage)

      let pagingWrapper = $('.paging')
      pagingWrapper.children().remove()
      if (page) {
        addPageButton(pagingWrapper, page, totalRecord)
      } else {
        addPageButton(pagingWrapper,1, totalRecord)
      }

      let reviewsWrapper = $('.review-card-container')
      reviewsWrapper.children().remove()
      addReviewCards(reviewsWrapper, reviewList, reviewList.length)

      $('html, body').animate({ scrollTop: 0 }, 'slow')
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {
  callGetReviewsByProductIdApi(1)

  $('.order-by-button-container').on('click', () => {
    let orderByArrowImage = $('.order-by-arrow-image')
    let orderByList = $('.order-by-list')
    if (orderByList.css('display') === 'none') {
      orderByArrowImage.css('transform', 'rotate(270deg)')
      orderByList.css('display', 'block')
    } else {
      orderByArrowImage.css('transform', 'rotate(90deg)')
      orderByList.css('display', 'none')
    }
  })

  $('[name=orderBy]').on('change', () => {
    callGetReviewsByProductIdApi(1)
    $('.order-by-list').css('display', 'none');
  })
})
