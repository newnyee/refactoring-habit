let page = 1;

const createReviewCard = (review) => {
  return '    <div class="review-card">\n'
      + '      <div class="review-product-name-container">\n'
      + '        <a href="/product/' + review.productAltId + '" class="review-product-name">\n'
      + '          <span class="product-name-container"><span class="product-name">' + review.productName + '</span> > </span>\n'
      + '        </a>\n'
      + '        <div>\n'
      + '          <input class="review-alt-id" type="hidden" value="'+ review.reviewAltId +'">'
      + '          <button class="review_modify_btn">수정</button>\n'
      + '          <button class="review_delete_btn">삭제</button>\n'
      + '        </div>\n'
      + '      </div>\n'
      + '      <div class="review-option-name">\n'
      + '        <span>' + review.optionName + ' 참여</span>\n'
      + '      </div>\n'
      + '      <div class="review-star-score-container">\n'
      + createStarScoreImage(review.starScore)
      + '        <span>' + formatToYearMonthDayHour(review.updateAt) + ' 작성 ' + isModifiedReview(review) + '</span>\n'
      + '      </div>\n'
      + '      <div>\n'
      + '        <p class="review-content-text">' + review.content + '</p>\n'
      + '        <div class="review-content-img">\n'
      + createReviewImageElements(review.image)
      + '        </div>\n'
      + '      </div>\n'
      + '    </div>'
}

const callGetReviewsApi = () => {
  let recordPerPage = paging.recordPerPage
  let pageNumber = Number(page) - 1
  let requestUrl = '/api/v2/members/' + memberAltId + '/reviews' + '?page=' + pageNumber + '&size=' + recordPerPage

  $.ajax({
    url: requestUrl,
    method: 'GET',
    success: (response) => {
      // 리뷰 목록 렌더링
      let totalRecord = response.data.reviewCount
      let reviewList = response.data.reviewDetailDtos
      $('.review-count').text(totalRecord)
      addReviewCards($('.review-cards-container'), reviewList)

      // 페이징
      let totalPages = Math.ceil(totalRecord / recordPerPage)
      page++
      let seeMoreButtonContainer = $('.see-more-button-container')
      seeMoreButtonContainer.children().remove()
      if (totalPages >= page) {
        seeMoreButtonContainer.append('<button class="see-more">더보기</button>')
      }
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {
  // 리뷰 목록 Api 호출
  callGetReviewsApi(page)

  // 리뷰 더보기 버튼 클릭 이벤트
  $(document).on('click', '.see-more', () => {
    callGetReviewsApi(page)
  })
})
