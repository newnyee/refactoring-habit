let slides
let totalSlides

let slideIndex = 1;
let maxSlides = 4; // 한 번에 보여줄 슬라이드 개수

const tagGender = {
  MALE: '남자',
  FEMALE: '여자',
  NONE: '성별 상관 없음'
}

const tagAge = {
  20: '20대',
  30: '30대',
  40: '40대',
  50: '50대 이상'
}

const tagWith = {
  COUPLE: '커플과 함께',
  FRIEND: '친구와 함께',
  SINGLE: '혼자'
}

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

const createImageElement = (imageFileName) => {
  return '<img class="product-image" src="/storage/' + imageFileName + '"/>'
}

const renderSimpleHostInfo = (simpleHostInfo) => {
  // 이미지
  let hostImage = $('.host-image')
  hostImage.attr('src', `/storage/${simpleHostInfo.profileImage}`)

  // 닉네임
  let hostNickName = $('.host-nickname')
  hostNickName.text(simpleHostInfo.nickName)

  // 호스트 정보
  let hostInfo = $('.host-info')
  hostInfo.text(
      `해빗 ${simpleHostInfo.totalProductCount} | 리뷰 ${simpleHostInfo.reviewCount}`)
}

const createMap = (addr1) => {
  // 지도 API
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
      mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
      };

  // 지도를 생성합니다
  var map = new kakao.maps.Map(mapContainer, mapOption);

  // 주소-좌표 변환 객체를 생성합니다
  var geocoder = new kakao.maps.services.Geocoder();

  // 주소로 좌표를 검색합니다
  geocoder.addressSearch(addr1, function (result, status) {

    // 정상적으로 검색이 완료됐으면
    if (status === kakao.maps.services.Status.OK) {

      var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

      // 결과값으로 받은 위치를 마커로 표시합니다
      var marker = new kakao.maps.Marker({
        map: map,
        position: coords
      });

      // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
      map.setCenter(coords);
    }
  });
}

const translateHashTagNamesToKorean = (hashTagList, type) => {
  let tagType
  if (type === 'gender') {
    tagType = tagGender
  } else if (type === 'age') {
    tagType = tagAge
  } else {
    tagType = tagWith
  }

  let koreanHashtags = []
  for (const hashTag of hashTagList) {
    koreanHashtags.push(tagType[hashTag])
  }
  return koreanHashtags
}

const createHashTagElement = (hashTag) => {
  return '<span class="RoundTag">' + hashTag + '</span>'
}

const appendHashTagElement = (parentElement, hashTagList) => {
  for (const hashTag of hashTagList) {
    parentElement.append(createHashTagElement(hashTag))
  }
}

const renderProductDetails = (productDetails) => {
  // 이미지 파일
  let imageSlides = $('.image-slides')
  let imageFileNameList = getImageFileNameList(productDetails.imageFileNames);
  for (const imageFileName of imageFileNameList) {
    imageSlides.append(createImageElement(imageFileName))
  }
  slides = $('.product-image')
  totalSlides = slides.length

  // 상품명
  $('.BIS_title').text(productDetails.name)

  // 상품 가격
  $('.product-price').text(formatCurrency(productDetails.price))

  // 리뷰 평점 이미지
  let reviewAverage = productDetails.reviewAverage
  $('.review-star-wrapper').append(createStarScoreImage(reviewAverage))

  // 리뷰 평점
  $('.ReviewSummary_Aver').text(reviewAverage)

  // 리뷰 수
  $('.review-count').text(productDetails.reviewCount)

  // 해빗 소개
  $('.product-description').html(productDetails.description)

  // 진행하는 장소 (kakao 지도)
  createMap(productDetails.address1)

  // 진행하는 장소 주소
  let zipCode = '[' + productDetails.zipCode + '] '
  let address1 = productDetails.address1
  let address2 = productDetails.address2 != null ? ' / '
      + productDetails.address2 : ''
  let extraAddress = productDetails.extraAddress != null ? ' / '
      + productDetails.address2 : ''
  $('.address').text(zipCode + address1 + address2 + extraAddress)

  // 해시 태그
  let tagGenderList = productDetails.tagGender.split('|')
  let tagAgeList = productDetails.tagAge.split('|')
  let tagWithList = productDetails.tagWith.split('|')
  let hashTagWrapper = $('.Classsupplies')

  appendHashTagElement(hashTagWrapper,
      translateHashTagNamesToKorean(tagGenderList, 'gender'))
  appendHashTagElement(hashTagWrapper,
      translateHashTagNamesToKorean(tagAgeList, 'age'))
  appendHashTagElement(hashTagWrapper,
      translateHashTagNamesToKorean(tagWithList, 'with'))

  // 찜
  $('.wish-count').text(productDetails.wishCount)
}

const createReviewCardElement = (review) => {
  return '<div class="Reviewgreen">\n'
      + '  <img src="/storage/' + getImageFileNameList(review.image)[0] + '" alt=""/>\n'
      + '  <div class="CoverReviewCard_User">\n'
      + '    <div class="CoverReviewCard_ProfileImg">\n'
      + '      <img src="/storage/' + review.memberProfileImage + '" alt=""/>\n'
      + '    </div>\n'
      + '    <div class="CoverReviewCard_UserInfo">\n'
      + '      <p>' + review.memberNickName + '</p>\n'
      + '    </div>\n'
      + '    <p class="CorverReivewCard_p"></p>\n'
      + '  </div>\n'
      + '  <span class="SpanLineClamp">' + review.content + '</span>\n'
      + '</div>'
}

const renderReviewList = (reviewList) => {
  let reviewContainer = $('.Reviewflex');

  if (reviewList.length === 0) {
    let reviewWrapper = $('.Reviewblack')
    reviewWrapper.children().remove()
    reviewWrapper.append('<div>해당 리뷰가 없습니다</div>\n<div>첫 리뷰를 작성해주세요!</div>')
    reviewWrapper.addClass('no-review')
  }

  for (const review of reviewList) {
    reviewContainer.append(createReviewCardElement(review))
  }
  // 리뷰 슬라이드 초기화
  showSlides(slideIndex);
}

const renderWishAltId = (wishAltId) => {
  let wishButtonImage = $('.wish-button-image')
  let wishId = $('.wish-id')
  if (wishAltId !== '') {
    wishButtonImage.attr('src', '/img/redheart2.png')
    wishId.val(wishAltId)
  }
}

const callGetProductByIdApi = () => {
  $.ajax({
    url: '/api/v2/products/' + productId,
    method: 'GET',
    success: (response) => {
      console.log(response.data)
      let simpleHostInfo = response.data.simpleHostInfoDto
      let productDetails = response.data.productDetailDto
      let reviewList = response.data.reviewDetailDto
      let wishAltId = response.data.wishAltId

      renderSimpleHostInfo(simpleHostInfo)
      renderProductDetails(productDetails)
      renderReviewList(reviewList)
      renderWishAltId(wishAltId)
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}

const updateSlide = (currentIndex) => {
  const newTransformValue = `translateX(-${currentIndex * 100}%)`
  $('.image-slides').css('transform', newTransformValue);
}

const callCreateWishApi = (wishId, wishButtonImage, wishCount) => {
  $.ajax({
    url: '/api/v2/wishes/' + productId,
    method: 'POST',
    success: (response) => {
      wishId.val(response.data)
      wishButtonImage.attr('src', '/img/redheart2.png')
      wishCount.text(parseInt(wishCount.text()) + 1)
    },
    error: (e) => {
      if (e.status === 401) {
        alert("로그인 후 이용 가능합니다.")
      }
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.");
      }
    }
  })
}

const callDeleteWishApi = (wishId, wishButtonImage, wishCount) => {
  $.ajax({
    url: `/api/v2/wishes/${productId}/${wishId.val()}`,
    method: 'DELETE',
    success: () => {
      wishId.val('')
      wishButtonImage.attr('src', '/img/black2.png')
      wishCount.text(parseInt(wishCount.text()) - 1)
    },
    error: (e) => {
      if (e.status === 401) {
        alert("로그인 후 이용 가능합니다.")
      }
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.");
      }
    }
  })
}

$(document).ready(() => {
  // 상품 정보 api 호출
  callGetProductByIdApi()

  // 상품 이미지 슬라이드
  let currentIndex = 0;
  $('.image-prev').on('click', () => {
    currentIndex = (currentIndex - 1 + totalSlides) % totalSlides
    updateSlide(currentIndex)
  })

  $('.image-next').on('click', () => {
    currentIndex = (currentIndex + 1) % totalSlides
    updateSlide(currentIndex)
  })

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
        ? descriptionSectionContainer.css('max-height', '600px')
        : descriptionSectionContainer.css('max-height', '')
  })

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
  $(".OptionItem_Container").on('click', (e) => {
    // 클릭한 상품명과 가격을 PurchaseCell_Wrapper 내에 적용합니다.
    let selectedItemName = e.currentTarget.querySelector(
        '.OptionItem_Title').textContent
    let selectedItemPrice = e.currentTarget.querySelector(
        '#option_price').textContent

    let optionAppend =
        '      <div class="PurchaseCell_Wrapper">\n'
        + '        <div class="purchaseCell_TitleWrapper">\n'
        + '          <div class="PurchaseCell_Title">' + selectedItemName
        + '</div>\n'
        + '          <img src="/img/close_button.png" class="PurchaseCell_DeleteIcon">\n'
        + '        </div>\n'
        + '        <div class="PurchaseCell_PriceWrapper">\n'
        + '          <div class="Counter_Wrapper">\n'
        + '            <img src="/img/minus-btn.svg" class="Counter_ControlButton minus-btn">\n'
        + '            <input type="hidden" class="option-price" value='
        + selectedItemPrice + '>'
        + '            <input type="number" min="1" class="Counter_Value" value="1" readonly>\n'
        + '            <img src="/img/plus-btn.svg" class="Counter_ControlButton plus-btn">\n'
        + '          </div>\n'
        + '          <span class="PurchaseCell_Price"><span class="option-total-price">'
        + selectedItemPrice + '</span>원</span>\n'
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

  // 아코디언 박스 클릭
  $('.Accordion_Container').on('click', (e) => {
    let parentElement = $(e.currentTarget).parent()
    let accordionContainer = parentElement.children('.Accordion_Container')
    let accordionContent = parentElement.children('.Accordion_Content')
    let accordionIconArrow = parentElement.children(
        '.Accordion_Container').children('.Accordion_Icon').children(
        '.Accordion_IconArrow')

    if (accordionContent.css('display') === 'none') {
      accordionContent.addClass('show').slideToggle()
      accordionContainer.css('font-weight', 'bold')
      accordionIconArrow.css('transform', 'rotate(270deg)')
    } else {
      accordionContent.removeClass('show').slideToggle()
      accordionContainer.css('font-weight', 'normal')
      accordionIconArrow.css('transform', 'rotate(90deg)')
    }
  })

  // 찜 버튼 클릭
  $('.wish-button').on('click', () => {
    let wishButtonImage = $('.wish-button-image')
    let wishCount = $('.wish-count')
    let wishId = $('.wish-id')

    if (wishId.val() === '') {
      callCreateWishApi(wishId, wishButtonImage, wishCount);
    } else {
      callDeleteWishApi(wishId, wishButtonImage, wishCount)
    }
  })
})
