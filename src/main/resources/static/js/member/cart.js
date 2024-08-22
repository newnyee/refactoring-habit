let cartList = {}

const textPriceParseInt = (textPrice) => {
    let replaceText = textPrice.replace(/[,원]/g, '')
    return parseInt(replaceText)
}

const totalPriceCalculator = () => {
    let priceElements = $('.price')
    let quantityElements = $('.quantity')

    let totalPrice = 0
    for (let i = 0; i < priceElements.length; i++) {
        let nowPrice = textPriceParseInt($(priceElements[i]).text())
        let nowQuantity = parseInt($(quantityElements[i]).text())
        totalPrice += nowPrice * nowQuantity
    }

    $('.total-price').text(formatCurrency(totalPrice))
}

const createCartCardElement = (cart) => {
    return '        <div class="cart-card">\n'
        + '          <input class="cart-alt-id" type="hidden" value="' + cart.cartAltId + '">\n'
        + '          <div class="selected-product-image-container">\n'
        + '            <img class="product-image" src="/storage/' + getImageFileNameList(cart.productImages)[0] + '">\n'
        + '          </div>\n'
        + '          <div class="selected-product-info-container">\n'
        + '            <a href="/product/' + cart.productAltId + '"><p class="product-name">' + cart.productName + '</p></a>\n'
        + '            <p class="option-name">' + cart.optionName + '</p>\n'
        + '            <p class="price">' + formatCurrency(cart.optionPrice) + '</p>\n'
        + '          </div>\n'
        + '          <div class="selected-option-quantity-container">\n'
        + '            <input class="Home_qty_btn_min btn_min" type="button" value="-"/>\n'
        + '            <div class="quantity">' + cart.cartQuantity + '</div>\n'
        + '            <input class="Home_qty_btn_plus btn_plus" type="button" value="+"/>\n'
        + '          </div>\n'
        + '        </div>'
}

const createEmptyCartContainerElement = () => {
  return '        <div class="empty-cart-container">\n'
      + '          <div class="empty-cart">장바구니에 담긴 해빗이 없습니다.</div>\n'
      + '          <span class="go-home-button">해빗 구경하러 가기 ></span>\n'
      + '        </div>'
      + '        <hr>'
}

const createNotEmptyCartContainerElements = () => {
    return '      <div>\n'
        + '        <button class="emptying-cart" type="button">장바구니 비우기</button>\n'
        + '      </div>\n'
        + '      <div class="cart-card-list">\n'
        + '      </div>\n'
        + '      <div class="Home_show_result">\n'
        + '        <hr>\n'
        + '        <p class="total-price-container">합계금액 :<span class="total-price"></span></p>\n'
        + '        <hr>\n'
        + '        <input class="go_order" type="button" value="결제하기">\n'
        + '      </div>'
}

const appendCartList = (cartList) => {
    let $homeForm = $('.Home_form')
    if (cartList.length === 0) {
        $homeForm.children().remove()
        $homeForm.append(createEmptyCartContainerElement())
        return
    }

    $homeForm.append(createNotEmptyCartContainerElements())
    let $cartCardContainer = $('.cart-card-list')
    for (const cart of cartList) {
        $cartCardContainer.append(createCartCardElement(cart))
    }
}

const callGetCartListApi = () => {
    $.ajax({
        url: `api/v2/members/${memberAltId}/carts`,
        method: 'GET',
        success: (response) => {
            $('.Home_form').children().remove()
            cartList = response.data
            appendCartList(cartList)
            totalPriceCalculator()
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

const callDeleteCartsByMember = () => {
    $.ajax({
        url: `/api/v2/members/${memberAltId}/carts`,
        method: 'DELETE',
        success: () => {
            callGetCartListApi()
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

const callDeleteCartApi = (cartAltId) => {
    $.ajax({
        url: `/api/v2/carts/${cartAltId}`,
        method: 'DELETE',
        success: () => {
            callGetCartListApi()
        },
        error: () => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

const callUpdateCartApi = (cartAltId, updateQuantity) => {
    $.ajax({
        url: `/api/v2/carts/${cartAltId}`,
        method: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify(updateQuantity)
    })
}

const updateCart = (cartCardElement) => {
    let cartAltId = cartCardElement.find('.cart-alt-id').val()
    let updateQuantity = parseInt(cartCardElement.find('.quantity').text())

    let foundCart = $.grep(cartList, (cart) => {
        return cart.cartAltId === cartAltId
    })[0];

    if (foundCart !== undefined) {
        if (foundCart.cartQuantity !== updateQuantity) {
            callUpdateCartApi(cartAltId, updateQuantity)
        }
    }
}

$(document).ready(() => {
    callGetCartListApi()

    // 수량 마이너스 버튼 클릭
    $(document).on('click', '.btn_min', (e) => {
        let $target = $(e.target)
        let $quantityContainer = $target.parent('.selected-option-quantity-container')
        let $quantity = $quantityContainer.find('.quantity')
        let quantity = parseInt($quantity.text())
        if (quantity === 1) {
            if (confirm('해당 옵션을 삭제하시겠습니까?')) {
                let $targetCartCard = $quantityContainer.parent('.cart-card')
                let cartAltId = $targetCartCard.find('.cart-alt-id').val()
                callDeleteCartApi(cartAltId)
            }
        } else {
            $quantity.text(quantity - 1)
        }
        totalPriceCalculator()
    })

    // 수량 플러스 버튼 클릭
    $(document).on('click', '.btn_plus', (e) => {
        let $target = $(e.target)
        let $quantityContainer = $target.parent('.selected-option-quantity-container')
        let $quantity = $quantityContainer.find('.quantity')
        let quantity = parseInt($quantity.text())

        let $targetCartCard = $quantityContainer.parent('.cart-card')
        let index = $('.cart-card').index($targetCartCard)
        if (cartList[index].optionQuantity === quantity) {
            alert('재고가 부족합니다.')
            return
        }
        $quantity.text(quantity + 1);
        totalPriceCalculator()
    })

    // 장바구니 비우기
    $(document).on('click', '.emptying-cart', () => {
        if (confirm(`${cartList.length}개의 상품을 장바구니에서 삭제하시겠습니까?`)) {
            callDeleteCartsByMember()
        }
    })

    // 해빗 구경하러 가기 클릭 이벤트 - 홈으로 이동
    $(document).on('click', '.go-home-button', () => {
        window.location.href = '/'
    })

    // 결제하기 버튼 클릭
    $(document).on('click', '.go_order', () => {
        window.location.href = '/order'
    })

    // 해당 페이지에서 벗어나는 경우
    $(window).on('beforeunload', () => {
        let cartCards = $('.cart-card')
        for (const cartCard of cartCards) {
            updateCart($(cartCard))
        }
    })
})
