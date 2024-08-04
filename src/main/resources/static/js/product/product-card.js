const createStarScoreImage = (starScore) => {
  let starScoreImage = '';
  if (starScore > 0) {
    let fullStar = '                            <img src="/img/star.png" alt="" class="review-star">\n';
    let halfStar = '                            <img src="/img/halfstar.png" alt="" class="review-star">\n'
    let fullStarCount = Math.floor(starScore)
    let hasHalfStar = (starScore - fullStarCount) >= 0.5

    for (let i = 0; i < fullStarCount; i++) {
      starScoreImage += fullStar
    }

    if (hasHalfStar) {
      starScoreImage += halfStar
    }
  }

  return starScoreImage
}

const createProductElement = (product) => {
  const imageFileNameList = getImageFileNameList(product.imageFileNames)
  return '    <div class="card-product-wrapper">\n'
      + '        <a href="/product/' + product.productAltId + '">\n'
      + '            <div class="product-image">\n'
      + '                <img src="/storage/' + imageFileNameList[0]
      + '" width="150px" height="150px">\n'
      + '            </div>\n'
      + '            <div class="wish-button-wrapper">\n'
      + '                <button type="button" class="wish-button" onclick="hello()">\n'
      + '                    <img src="/img/black2.png" alt="" width="40px">\n'
      + '                </button>\n'
      + '            </div>\n'
      + '            <div class="product-info-wrapper">\n'
      + '                <div>\n'
      + '                    <div class="product-name">' + product.name
      + '</div>\n'
      + '                    <div class="review-info">\n'
      + '                        <div class="review-average">\n'
      + createStarScoreImage(product.reviewAverage)
      + '                        </div>\n'
      + '                        <div class="review-count">\n'
      + '                            <span>리뷰 ' + product.reviewCount
      + '</span>\n'
      + '                        </div>\n'
      + '                    </div>\n'
      + '                    <hr class="Home_recommend_hr">\n'
      + '                    <div class="product-price">' + formatCurrency(
          product.minPrice) + '</div>\n'
      + '                </div>\n'
      + '            </div>\n'
      + '        </a>\n'
      + '    </div>'
}

const addProducts = (containerElement, productList, limit) => {
  for (let i = 0; i < limit; i++) {
    containerElement.append(createProductElement(productList[i]))
  }
}
