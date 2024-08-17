const paging = {
  displayPageNumber: 4,
  recordPerPage: 6
}

const createReviewImageElements = (imageFileNames) => {
  let imageFileNameList = getImageFileNameList(imageFileNames);
  let elements = ''
  for (const imageFileName of imageFileNameList) {
    elements += '<img src="/storage/' + imageFileName + '" class="review-image">\n'
  }
  return elements
}

const isModifiedReview = (review) => {
  if (review.updateAt !== review.createAt) {
    return '(수정됨)'
  }
}

const addReviewCards = (containerElement, reviewList) => {
  for (const review of reviewList) {
    containerElement.append(createReviewCard(review))
  }
}
