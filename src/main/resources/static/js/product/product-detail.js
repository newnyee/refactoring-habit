let slideIndex = 1;
let maxSlides = 4; // 한 번에 보여줄 슬라이드 개수

const showSlides = (n) => {
  let slides = $('.Reviewgreen')
  // 모든 슬라이드 숨기기
  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  // 선택한 범위의 슬라이드를 보이게 설정
  let startIndex = (n - 1) * maxSlides;
  for (let i = startIndex; i < Math.min(startIndex + maxSlides, slides.length); i++) {
    slides[i].style.display = "block";
  }
}

const plusSlides = (n) => {
  let slides = $('.Reviewgreen');
  let maxIndex = Math.ceil(slides.length / maxSlides); // 최대 인덱스 계산

  slideIndex += n;
  if (slideIndex > maxIndex) {
    slideIndex = 1;
  } else if (slideIndex < 1) {
    slideIndex = maxIndex;
  }
  showSlides(slideIndex);
}

const calcAll = () => {
  let qty= 0
  let price= 0
  for(let i=0; i < $(".PurchaseCell_Wrapper").length; i++){
    let one_qty= parseInt($(".Counter_Value").eq(i).val());
    let one_price= parseInt($(".PurchaseCell_Price").eq(i).text().replace(",","").replace("원",""));
    //console.log(one_price);
    qty+=one_qty;
    price+=one_qty*one_price;
  }

  $(".OptionBottomSheet_Count").text("총  "+qty+"개");
  $(".OptionBottomSheet_Price").text(price.toLocaleString()+" 원");
}

$(document).ready(() => {
  // 리뷰 슬라이드 초기화
  showSlides(slideIndex);

  // 해빗 소개 상세 정보 더보기
  $('#detailButton').on('click', (e) => {
    // 버튼 텍스트 변경
    let buttonText = e.currentTarget.querySelector('span')
    buttonText.textContent === "상세정보 더보기"
        ? buttonText.textContent = "간략히"
        : buttonText.textContent = "상세정보 더보기"

    // 화살표 방향 변경
    let arrowIcon = e.currentTarget.querySelector(".arrow-icon");
    arrowIcon.classList.toggle('rotated')

    let descriptionSectionContainer = $('.DescriptionSection_Container')
    buttonText.textContent === "상세정보 더보기"
        ? descriptionSectionContainer.css('height', '400px')
        : descriptionSectionContainer.css('height', '100%')
  })

  // 참여하기 모달창 open
  $('#openModalButton').on('click', () => {
    $('html').css('scrollbar-gutter', 'stable')
    $('body').css('overflow', 'hidden')
    $('#FloatingActionBar').css('display', 'none')
    $('#productOptionsModal').css('display', 'block')
  })

  // 참여하기 모달창 close
  $('.Toggle_Purch').on('click', () => {
    $('html').css('scrollbar-gutter', '')
    $('body').css('overflow', '')
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
    if (optionItemsWrapper.css('display') === 'none' || optionItemsWrapper.css('display') === '') {
      optionItemsWrapper.css('display', 'block');
    } else {
      optionItemsWrapper.css('display', 'none');
    }
  })

  // 옵션 클릭
  $(".OptionItem_Container").on('click', (e) => {
    // 클릭한 상품명과 가격을 PurchaseCell_Wrapper 내에 적용합니다.
    let selectedItemName = e.currentTarget.querySelector('.OptionItem_Title').textContent
    let selectedItemPrice = e.currentTarget.querySelector('#option_price').textContent

    let optionAppend =
          '      <div class="PurchaseCell_Wrapper">\n'
        + '        <div class="purchaseCell_TitleWrapper">\n'
        + '          <div class="PurchaseCell_Title">' + selectedItemName + '</div>\n'
        + '          <img src="/img/exit_button.png" class="PurchaseCell_DeleteIcon">\n'
        + '        </div>\n'
        + '        <div class="PurchaseCell_PriceWrapper">\n'
        + '          <div class="Counter_Wrapper">\n'
        + '            <img src="/img/minus-btn.svg" class="Counter_ControlButton minus-btn">\n'
        + '            <input type="hidden" class="option-price" value='+ selectedItemPrice +'>'
        + '            <input type="number" min="1" class="Counter_Value" value="1" readonly>\n'
        + '            <img src="/img/plus-btn.svg" class="Counter_ControlButton plus-btn">\n'
        + '          </div>\n'
        + '          <span class="PurchaseCell_Price"><span class="option-total-price">' + selectedItemPrice + '</span>원</span>\n'
        + '        </div>\n'
        + '      </div>'
    $(".OptionBottomSheet").append(optionAppend)

    // 수량이 변경될 때마다 calculateTotal 함수를 호출하도록 이벤트 리스너를 추가
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
})
