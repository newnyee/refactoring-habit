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

const createNoContentElement = () => {
  return '<div class="no-content">해빗이 존재하지 않습니다</div>'
}

const addProducts = (containerElement, productList, limit) => {
  if (productList.length === 0) {
    containerElement.append(createNoContentElement())
    return
  }

  for (let i = 0; i < limit; i++) {
    containerElement.append(createProductElement(productList[i]));
  }
}
