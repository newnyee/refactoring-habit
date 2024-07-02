
// 하위 태그 삭제
const childrenElementRemove = (selector) => {
  selector.children().remove()
}


// 대분류 카테고리 목록 selectbox에 추가
const addCategoryLarge = () => {
  for (let i = 0; i < categories.length; i++) {
    $('#categoryLarge').append("<option value=" + i + "> " + categories[i].name + "</option>");
  }
}


// 카테고리 목록 가져오기
const getCategories = () => {
  $.ajax({
    url: '/api/v2/categories',
    method: 'GET',
    dataType: 'json',
    success: (response) => {
      categories = response.data
      addCategoryLarge()
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}


// 대분류에 따른 중분류 카테고리 목록 selectbox에 추가
const addCategoryMiddlesOfCategoryLarge = () => {
  let index = productInfo.$categoryLarge.val()
  let categoryMiddle = productInfo.$categoryMiddle
  let defaultElement = "<option value='-'>2차 카테고리</option>"

  if (index === '-') {
    childrenElementRemove(categoryMiddle)
    categoryMiddle.append(defaultElement);
    return
  }

  let categoryMiddlesOfSelectedCategory = categories[index].categoryMiddleList;
  childrenElementRemove(categoryMiddle)
  categoryMiddle.append(defaultElement);
  for (const element of categoryMiddlesOfSelectedCategory) {
    categoryMiddle.append("<option value=" + element.altId + "> " + element.name + "</option>");
  }
}


$(document).ready(() => {

  // 카테고리 목록 가져오기
  getCategories()

  // 대분류에 따른 중분류 카테고리 목록 selectbox에 추가
  productInfo.$categoryLarge.on('change', () => {
    addCategoryMiddlesOfCategoryLarge()
  })

  $(window).on('beforeunload', (e) => {
    if (!isFormSubmitted) {
      // 사용자 정의 메시지를 설정하더라도 최신 브라우저에서는 무시됨
      let message = "변경사항이 저장되지 않을 수 있습니다.";
      e.returnValue = message;
      return message;
    }
  })
})
