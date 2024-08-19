let days = ['일', '월', '화','수', '목', '금', '토']

const calcAll = () => {
  let qty = 0
  let price = 0
  for (let i = 0; i < $(".PurchaseCell_Wrapper").length; i++) {
    let one_qty = parseInt($(".Counter_Value").eq(i).val());
    let one_price = parseInt(
        $(".PurchaseCell_Price").eq(i).text().replace(",", "").replace("원", ""));
    qty += one_qty;
    price += one_qty * one_price;
  }

  $(".OptionBottomSheet_Count").text("총  " + qty + "개");
  $(".OptionBottomSheet_Price").text(price.toLocaleString() + " 원");
}


const getDayFromLocalDateTime = (localDateTime) => {
  let date = new Date(localDateTime)
  let day = date.getDay()
  return days[day]
}

const generateReservationOptionName = (optionName) => {
  let dateAndTime = optionName.split(' ')
  let date = dateAndTime[0]
  let time = dateAndTime[1].substring(0, 5)
  return `${date} (${getDayFromLocalDateTime(optionName)}) ${time}`
}

const getOptionNameByProductType = (type, optionName) => {
  if (type === 'RESERVATION') {
    return generateReservationOptionName(optionName)
  }
  return optionName
}

const createOptionCard = (option, isLastElement) => {
  let addStyle = 'style="border-bottom: 1px solid #cccccc94"'
  if (isLastElement) {
    addStyle = ''
  }
  return '            <div class="OptionItem_Container" ' + addStyle + '>\n'
      + '              <input type="hidden" value="' + option.altId + '" class="Option_Head optionAltId">\n'
      + '              <div class="OptionItem_ContentContainer">\n'
      + '                <div class="OptionItem_TitleContainer">\n'
      + '                  <span class="OptionItem_Title">' + getOptionNameByProductType(option.productType, option.name) + '</span>\n'
      + '                </div>\n'
      + '                <div class="OptionItem_PriceContainer">\n'
      + '                  <span class="OptionItem_Price">' + formatCurrency(option.price) + '</span>\n'
      + '                  <span class="OptionItem_Remains"><span class="option-quantity">' + option.quantity + '</span>개 남음</span>\n'
      + '                </div>\n'
      + '              </div>\n'
      + '            </div>'
}

const renderOptionDetails = (optionList) => {
  options = optionList
  let $optionItemWrapper = $('.OptionItem_Wrapper')
  let optionListSize = optionList.length
  for (let i = 0; i < optionListSize; i++) {
    let isLastElement = false
    if (i === optionListSize - 1) {
      isLastElement = true
    }
    $optionItemWrapper.append(createOptionCard(optionList[i], isLastElement))
  }
}

const createChooseOptionCard = (option) => {
  return '      <div class="PurchaseCell_Wrapper">\n'
      + '        <input type="hidden" class="add-option-alt-id" value="' + option.altId + '">'
      + '        <div class="purchaseCell_TitleWrapper">\n'
      + '          <div class="PurchaseCell_Title">' + getOptionNameByProductType(option.productType, option.name) + '</div>\n'
      + '          <img src="/img/close_button.png" class="PurchaseCell_DeleteIcon">\n'
      + '        </div>\n'
      + '        <div class="PurchaseCell_PriceWrapper">\n'
      + '          <div class="Counter_Wrapper">\n'
      + '            <img src="/img/minus-btn.svg" class="Counter_ControlButton minus-btn">\n'
      + '            <input type="number" min="1" class="Counter_Value" value="1" readonly>\n'
      + '            <img src="/img/plus-btn.svg" class="Counter_ControlButton plus-btn">\n'
      + '          </div>\n'
      + '          <span class="PurchaseCell_Price">' + formatCurrency(option.price) + '</span>\n'
      + '        </div>\n'
      + '      </div>'
}

const getCreateCartRequestDto = (shouldDeleteCart) => {
  let chooseOptions = $('.PurchaseCell_Wrapper')
  let chooseOptionInfoList = []
  for (const chooseOption of chooseOptions) {
    let target = $(chooseOption)
    chooseOptionInfoList.push({
      optionAltId: target.find('.add-option-alt-id').val(),
      quantity: target.find('.Counter_Value').val()
    })
  }

  return {
    productAltId: productId,
    chooseOptionInfoDtos: chooseOptionInfoList,
    shouldDeleteCart: shouldDeleteCart
  }
}

const callCreateOrUpdateCartApi = (shouldDeleteCart) => {
  $.ajax({
    url: '/api/v2/carts',
    method: 'PUT',
    contentType: 'application/json',
    data: JSON.stringify(getCreateCartRequestDto(shouldDeleteCart)),
    success: () => {
      if (confirm("장바구니에 해빗을 담았습니다. 장바구니로 이동하시겠습니까?")) {
        window.location.href = '/cart'
      }
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.");
      }
    }
 })
}

const callExistsCartsByProductApi = () => {
  $.ajax({
    url: '/api/v2/carts/products/' + productId + '/exists',
    method: 'GET',
    success: (response) => {
      if (response.data) {
        if (confirm("같은 해빗의 옵션만 담을 수 있습니다."
            + "다른 해빗의 옵션을 카트에 담으실 경우 이전에 담은 해빗 옵션이 삭제됩니다.")) {
          callCreateOrUpdateCartApi(true)
        }
      } else {
        callCreateOrUpdateCartApi(false)
      }
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.");
      }
    }
  })
}

$(document).ready(() => {
  // 참여하기 모달창 open
  $('#openModalButton').on('click', () => {
    $('#FloatingActionBar').css('display', 'none')
    $('#productOptionsModal').css('display', 'block')
  })

  // 참여하기 모달창 close
  $('.Toggle_Purch').on('click', () => {
    $('#FloatingActionBar').css('display', 'block')
    $('#productOptionsModal').css('display', 'none')
  })

  // 기본 옵션 창 토글
  $('.OptionSelect_Title').on('click', (e) => {
    // 화살표 방향 변경
    let arrowIcon = e.currentTarget.querySelector('.arrow-icon')
    arrowIcon.classList.toggle('rotated')

    // 기본 옵션 창 토글
    let optionItemsWrapper = $('.OptionItem_Wrapper')
    if (optionItemsWrapper.css('display') === 'none' || optionItemsWrapper.css(
        'display') === '') {
      optionItemsWrapper.css('display', 'block');
    } else {
      optionItemsWrapper.css('display', 'none');
    }
  })

  // 옵션 클릭
  $(document).on('click', '.OptionItem_Container', (e) => {
    let optionElements = $('.OptionItem_Container')
    let target = $(e.target).closest('.OptionItem_Container')
    let index = optionElements.index(target)
    let option = options[index]

    let addedOptionElements = $('.PurchaseCell_Wrapper')
    for (const element of addedOptionElements) {
      let $element = $(element)
      let elementAltId = $element.children('.add-option-alt-id').val()
      if (option.altId === elementAltId) {
        let $optionQuantity = $element.find('.Counter_Value')
        $optionQuantity.val(parseInt($optionQuantity.val()) + 1)
        calcAll()
        return
      }
    }
    $(".OptionBottomSheet").append(createChooseOptionCard(option))
    calcAll()
  })

  // 옵션 삭제
  $(document).on('click', '.PurchaseCell_DeleteIcon', (e) => {
    $(e.target).parent().parent().remove()
    calcAll()
  })

  // 옵션 수량 조절 - 마이너스
  $(document).on('click', '.minus-btn', (e) => {
    // 수량 조절
    let counterWrapper = $(e.currentTarget).closest('.Counter_Wrapper')
    let qty = counterWrapper.find('.Counter_Value')
    if (qty.val() > 1) {
      qty.val(parseInt(qty.val()) - 1)
    }
    calcAll()
  })

  // 옵션 수량 조절 - 플러스
  $(document).on('click', '.plus-btn', (e) => {
    // 수량 조절
    let counterWrapper = $(e.currentTarget).closest('.Counter_Wrapper')
    let qty = counterWrapper.find('.Counter_Value')
    qty.val(parseInt(qty.val()) + 1)
    calcAll()
  })

  // 장바구니 클릭
  $(document).on('click', '.OptionBottomSheet_Button', () => {
    callExistsCartsByProductApi()
  })
})
