$(document).ready(()=>{
  let searchResult = $('#search-result')
  let tableBody = $('#table-body')
  let pagination = $('#pagination')

  const createTable = (map) => {
    searchResult.append("<p class='content-name'>검색 결과 : " + map.paging.totalRecord + " 건</p>")
    if (map.paging.totalRecord === 0) {
      tableBody.append("<tr><td colspan='6'>검색 결과가 없습니다</td></tr>")
      let pagingAppend =
          "  <li class='page-item disabled'>\n" +
          "    <a class='page-link'>&laquo;</a>\n" +
          "  </li>\n" +
          "  <li class='page-item disabled'>\n" +
          "    <a class='page-link'>&raquo;</a>\n" +
          "  </li>\n"
      pagination.append(pagingAppend)
    } else {
      let currentPage = map.paging.currentPage
      let totalRecord = map.paging.totalRecord
      let startPage = map.paging.startPage
      let endPage = map.paging.endPage
      let prev = map.paging.prev
      let next = map.paging.next
      for (let item of map.list) {
        // 검색 결과 리스트 (table)
        let str = "<tr>\n" +
            "    <td>" + item.user_id + "</td>\n" +
            "    <td><a href='#'>" + item.cont_name + "</a></td>\n" +
            "    <td>"
        for (let i=1; i<=item.review_star; i++) {
          str += "<img src='/img/star.png' alt='" + i + "' class='review_star'>\n"
        }
        str += "</td>\n" +
            "    <td>" + item.review_cont + "</td>\n" +
            "    <td>" + item.review_date + "</td>\n" +
            "    <td>\n" +
            "    <button class='btn btn-sm btn-primary' id='review-delete" + item.review_no + "'>삭제</button>\n" +
            "    </td>\n" +
            "</tr>"
        tableBody.append(str)
      }

      // 페이징
      let pagingAppend = ''

      if (prev === true) {
        pagingAppend +=
            "  <li class='page-item'>\n"
      } else {
        pagingAppend +=
            "  <li class='page-item disabled'>\n"
      }

      pagingAppend +=
          "    <a class='page-link'>&laquo;</a>\n" +
          "  </li>\n"

      for(let i=startPage; i<=endPage; i++) {
        if (i === currentPage) {
          pagingAppend +=
              "                            <li class='page-item'>\n" +
              "                                <a class='page-link active' id='page-of-search" + i + "'>" + i + "</a>\n" +
              "                            </li>"
        } else {
          pagingAppend +=
              "                            <li class='page-item'>\n" +
              "                                <a class='page-link' id='page-of-search" + i + "'>" + i + "</a>\n" +
              "                            </li>"
        }
      }

      if (next === true) {
        pagingAppend +=
            "  <li class='page-item'>\n"
      } else {
        pagingAppend +=
            "  <li class='page-item disabled'>\n"
      }

      pagingAppend +=
          "    <a class='page-link'>&raquo;</a>\n" +
          "  </li>\n"

      pagination.append(pagingAppend);
    }
  }


  // 리뷰 필터 적용 List ajax
  $('#searchReview').on('click', ()=>{

    // let queryString = $('form[name=reviewForm]').serialize()
    let list = $('input[name=starScore]:checked')
    const starScore = [];
    for (const score of list) {
      starScore.push(score.value)
    }

    let reviewContKeyword = $('#reviewContKeyword').val()
    let searchUserId = $('#searchUserId').val()

    let requestData = {
      'filter': 'filter',
      'starScore': starScore,
      'reviewContKeyword': reviewContKeyword,
      'searchUserId' : searchUserId
    }

    $.ajax({
      url: '/host/review.do',
      type: 'post',
      dataType: 'json',
      data: requestData,
      success: (map) => {
        searchResult.children().remove()
        tableBody.children().remove()
        pagination.children().remove()
        createTable(map)
      }
    })
  })

  // 리뷰 필터 적용 List ajax 페이징
  $('#pagination').on('click', '.page-link',(e)=>{

    let currentPage = e.currentTarget.id.substring(14)

    // let queryString = $('form[name=reviewForm]').serialize()
    let list = $('input[name=starScore]:checked')
    const starScore = [];
    for (const score of list) {
      starScore.push(score.value)
    }

    let reviewContKeyword = $('#reviewContKeyword').val()
    let searchUserId = $('#searchUserId').val()

    let requestData = {
      'filter': 'filter',
      'starScore': starScore,
      'reviewContKeyword': reviewContKeyword,
      'searchUserId' : searchUserId
    }

    $.ajax({
      url: '/host/review.do/' + currentPage,
      type: 'post',
      dataType: 'json',
      data: requestData,
      success: (map) => {
        searchResult.children().remove()
        tableBody.children().remove()
        pagination.children().remove()
        createTable(map)
      }
    })
  })

  // 리뷰 삭제
  $('#table-body').on('click', 'button', (e) => {

    if (confirm("리뷰를 삭제하여도 삭제된 리뷰의 별점은 그대로 반영됩니다.\n삭제하시겠습니까?")) {
      let review_no = e.currentTarget.id.substring(13);
      location.href="/host/review/delete/"+ review_no
    } else {
      return false
    }
  })
})

const reviewDetail = (review_no, e) => {
  $('.modal').css('display', 'block')
  let review_user_id = $(e).children('td:eq(0)').text(); // 회원ID
  let review_product_name = $(e).children('td:eq(1)').text(); // 해빗명
  let review_star_score = $(e).children('td:eq(2)').html(); // 별점
  let review_created_at = $(e).children('td:eq(4)').text(); // 리뷰 작성일

  $('.review_user_id').text(review_user_id)
  $('.review_product_name').text(review_product_name)
  $('.review_star_score').html(review_star_score)
  $('.review_created_at').text(review_created_at)
}

$(document).mouseup(function (e){

  let container = $('.modal');

  if( container.has(e.target).length === 0){

    container.css('display','none');

  }

});
