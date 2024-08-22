const paging = {
    displayPageNumber: 4,
    recordPerPage: 12
}

const addCallGetProductsByCategoryLargeApiMethod = (pageNumber) => {
    return ` onclick="callGetWishesApi('${memberAltId}', ${pageNumber})"`
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
    return `          <button onclick="callGetWishesApi('${memberAltId}', ${pageNumber})" class="paging-button-active ${addClass}">${pageNumber}</button>\n`
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

const callGetWishesApi = (memberAltId, page) => {
    let productCardContainer = $('.card-product-list-wrapper')
    let pageWrapper = $('.paging')
    $.ajax({
        url: `/api/v2/members/${memberAltId}/wishes?page=${page-1}&size=${paging.recordPerPage}`,
        method: 'GET',
        success: (response) => {
            let wishList = response.data.wishes
            let wishCount = response.data.wishCount

            productCardContainer.children().remove()
            pageWrapper.children().remove()

            addProducts(productCardContainer, wishList, wishList.length)
            addPageButton(pageWrapper, page, wishCount)
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

$(document).ready(() => {
    callGetWishesApi(memberAltId, 1)
})
