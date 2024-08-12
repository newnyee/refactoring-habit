let categories = []
const paging = {
  displayPageNumber: 4,
  recordPerPage: 12
}

const addCallGetProductsByCategoryLargeApiMethod = (pageNumber) => {
  return ' onclick="callGetProductsByCategoryLargeApi(' + pageNumber + ')"'
}

const createPageArrowButton = (buttonType, buttonStatus, pageNumber) => {
  let element = '          <button class="paging-button-' + buttonStatus + '"'
  if (buttonStatus === 'active') {
    element += addCallGetProductsByCategoryLargeApiMethod(pageNumber)
  }
  if (buttonType === 'prev') {
    element += '><</button>\n'
  } else {
    element += '>></button>'
  }
  return element;
}

const createPageNumberButton = (pageNumber, addClass) => {
  return '          <button onclick="callGetProductsByCategoryLargeApi(' + pageNumber + ')" class="paging-button-active '+ addClass +'">' + pageNumber + '</button>\n'
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

const findCategoryName = (categories, categoryEngName) => {
  return categories.find(value => value.engName === categoryEngName)
}

const getCategoryName = (categories, categoryEngName) => {
  let findCategory = findCategoryName(categories, categoryEngName)

  if (!findCategory) {
    for (const category of categories) {
      findCategory = findCategoryName(category.categoryMiddleList,
          categoryEngName)
      if (findCategory) {
        break
      }
    }
  }
  return findCategory.name
}

const callGetCategoriesApi = () => {
  $.ajax({
    url: '/api/v2/categories',
    method: 'GET',
    success: (response) => {
      categories = response.data
      let categoryName = getCategoryName(categories, categoryEngName)
      $('.category-name').text(categoryName)
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const callGetProductsByCategoryLargeApi = (page) => {
  let requestUrl = '/api/v2/categories/' + categoryEngName + '/products'
  if (page) {
    let pageNumber = Number(page) - 1
    requestUrl += '?page=' + pageNumber + '&size=' + paging.recordPerPage
  }
  requestUrl += '&order-by=' + $('[name=filter]:checked').val()

  $.ajax({
    url: requestUrl,
    method: 'GET',
    success: (response) => {
      let totalRecord = response.data.productsCount
      $('.product-list-size').text(totalRecord)

      let pagingWrapper = $('.paging')
      pagingWrapper.children().remove()
      if (page) {
        addPageButton(pagingWrapper, page, totalRecord)
      } else {
        addPageButton(pagingWrapper,1, totalRecord)
      }

      let productList = response.data.products;
      let productsWrapper = $('.card-product-list-wrapper')
      productsWrapper.children().remove()
      addProducts(productsWrapper, productList, productList.length)
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

$(document).ready(() => {
  callGetCategoriesApi()
  callGetProductsByCategoryLargeApi(1)

  // 필터 모달창 열기
  $('.FilterOpenButton').on('click', () => {
    $('.Filter_Container_Modal').css('display', 'flex')
  });

  // 필터 모달창 닫기
  $('.filter-close').on('click', () => {
    $('.Filter_Container_Modal').css('display', 'none')
  })

  // 필터 적용
  $('.filter-button').on('click', () => {
    callGetProductsByCategoryLargeApi(1)
    $('.Filter_Container_Modal').css('display', 'none')
    let filterOpenButton = $('.FilterOpenButton')
    filterOpenButton.text('적용됨')
    filterOpenButton.css('color', '#e83e8c')
    filterOpenButton.css('font-weight', 'bold')
  })
})