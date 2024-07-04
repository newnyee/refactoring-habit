// 상품 정보 선택자 객체
let isFormSubmitted = false
let categories = []

let now = new Date()

let oneYearLater = new Date()
oneYearLater.setFullYear(oneYearLater.getFullYear() + 1)
oneYearLater.setDate(oneYearLater.getDate() - 1)

let oneMonthLater = new Date()
oneMonthLater.setMonth(oneMonthLater.getMonth() + 1)
oneMonthLater.setDate(oneMonthLater.getDate() - 1)

let sevenDaysLater = new Date()
sevenDaysLater.setDate(sevenDaysLater.getDate() + 6)

let setClosedAt = oneMonthLater.toISOString().split("T")[0]
let maxDate = oneYearLater.toISOString().split("T")[0]
let minDate = sevenDaysLater.toISOString().split("T")[0]


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


// 정규식에 맞지않는 문자 변경
const replaceCharToAgainstRegExp  = (selector, regExp, replaceValue) => {
  selector.val(selector.val().replace(regExp, replaceValue))
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


// 판매 종료일 설정(기본)
const configDefaultProductClosedAt = () => {
  let closedAt = oneMonthLater.toISOString().split("T")[0]
  productInfo.$closedAt.val(closedAt)
}


// 판매 종료일 설정(지정한 날짜까지 판매) : 최대 날짜 현재일로 부터 일년 지정
const configProductClosedAt = () => {
  let closedAt = productInfo.$closedAt
  closedAt.attr('max', maxDate)
  closedAt.attr('min', minDate)
}


// 판매 종료일 선택에 따른 이벤트 - 기본 선택
const selectDefaultClosedAt = () => {
  let closedAt = productInfo.$closedAt
  closedAt.attr('disabled', true)
  setClosedAt = closedAt.val()
  configDefaultProductClosedAt()
}


// 판매 종료일 선택에 따른 이벤트 - 지정 선택
const selectSetClosedAt = () => {
  let closedAt = productInfo.$closedAt
  closedAt.val(setClosedAt)
  closedAt.removeAttr('disabled')
}


// 옵션 추가에 사용될 요소 - pass
const addPassOptionRowElement = () => {
  return '                     <tr class="option-row">\n' +
      '                      <td><input class="form-check-input check-option" type="checkbox" name="checkOption" id=""></td>\n' +
      '                      <td>\n' +
      '                        <div>\n' +
      '                          <input type="text" name="optionName" class="form-control option-pass">\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                      <td>\n' +
      '                        <div>\n' +
      '                          <input type="number" name="optionQuantity" class="form-control">\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                      <td>\n' +
      '                        <div class="input-group mb-2">\n' +
      '                          <span class="input-group-text">판매가</span>\n' +
      '                          <input type="number" class="form-control" name="optionPrice" aria-label="Amount (to the nearest dollar)">\n' +
      '                          <span class="input-group-text">원</span>\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                    </tr>'
}


// 옵션 추가에 사용될 요소 - RESERVATION
const addReservationOptionRowElement = () => {
  let closedAtTime = productInfo.$closedAt.val() + 'T23:59'
  let minDateTime = minDate + 'T00:00'
  return '                     <tr class="option-row">\n' +
      '                      <td><input class="form-check-input check-option" type="checkbox" name="checkOption" id=""></td>\n' +
      '                      <td>\n' +
      '                        <div>\n' +
      '                          <input class="form-control option-reservation" name="optionName" min=' + minDateTime + ' max=' + closedAtTime + ' type="datetime-local">\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                      <td>\n' +
      '                        <div>\n' +
      '                          <input type="number" name="optionQuantity" min="0" class="form-control">\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                      <td>\n' +
      '                        <div class="input-group mb-2">\n' +
      '                          <span class="input-group-text">판매가</span>\n' +
      '                          <input type="number" class="form-control" name="optionPrice" min="0" aria-label="Amount (to the nearest dollar)">\n' +
      '                          <span class="input-group-text">원</span>\n' +
      '                        </div>\n' +
      '                      </td>\n' +
      '                    </tr>'
}


// 옵션 목록 추가 이벤트
const addOptionRow = (optionType) => {
  let rowElement = ''
  if (optionType === "PASS") {
    rowElement = addPassOptionRowElement();
  } else {
    rowElement = addReservationOptionRowElement()

  }
  $('#options').append(rowElement);
}


// 판매 유형 선택에 따른 이벤트
const selectOptionType = (showSelector, hideSelector) => {
  if (confirm('기존에 입력하신 옵션들이 초기화 됩니다. 유형을 변경하시겠습니까?')) {
    let type = $('[name=type]:checked').val()

    showSelector.removeAttr('hidden')
    hideSelector.prop('hidden', true)

    // 인원권/회차권 옵션창 reset
    $('#options').empty()
    addOptionRow(type)
  }
}


// 옵션 삭제 이벤트
const removeOptionRow = () => {
  let checked = $('input:checkbox[name="checkOption"]:checked')
  if(checked.length === 0) {
    alert("삭제할 항목을 선택해 주세요")
    return
  }

  let checkedSize = checked.length
  if(confirm('선택된 ' + checkedSize + '개의 옵션을 삭제하시겠습니까?')) {
    checked.each((i, value)=>{
      let checkRow = value.parentElement.parentElement
      $(checkRow).remove()
    })
  }
}


// 옵션 check 이벤트
const optionCheck = (e) => {
  let clickedOption = $(e.currentTarget).parent().find('.check-option')
  if (clickedOption.prop('checked') === false) {
    clickedOption.prop('checked', true)
  } else {
    clickedOption.prop('checked', false)
  }
}


// 이미지 미리보기 요소 생성
const createPreviewElement = (imageURL) => {
  return "                <div>\n" +
      "                  <img src=" + imageURL + " class='preview_img' alt='이미지 없음' width='200px' height='200px' style='border-radius: 15px'>\n" +
      "                </div>"
}


// 이미지 미리보기 요소 추가
const addPreviewElement = (container, images) => {
  for (let i = 0; i < 3; i++) {
    let imageURL = (images[i] !== undefined)
        ? URL.createObjectURL(images[i])
        : '/img/No_image_available.png'

    container.append(createPreviewElement(imageURL))
  }
}


// 대표 이미지 체크
const contImgCheck = () => {
  let preview = productInfo.$preview
  let images = productInfo.$image[0].files
  preview.empty()

  if (!imageVerify()) {
    addPreviewElement(preview, images)
    return false
  }

  addPreviewElement(preview, images)
  return true
}


// 썸머노트 setting 객체
let setSummerNote = {
  height: 300, // 에디터 높이
  minHeight: null, // 최소 높이
  maxHeight: null, // 최대 높이
  focus: false, // 에디터 로딩후 포커스를 맞출지 여부
  lang: "ko-KR", // 한글 설정
  placeholder: '호스트님의 상품을 소개해주세요 :)',	//placeholder 설정
  toolbar:[
    ['fontname', ['fontname']],
    ['fontsize', ['fontsize']],
    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
    ['color', ['forecolor','color']],
    ['table', ['table']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['height', ['height']],
    ['insert',['picture','link']],
    ['view', ['codeview', 'help']]
  ],
  fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
  fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
  callbacks: {
    onImageUpload: function (files) { //이미지 업로드 처리
      RealTimeImageUpdate(files);
    },
    onChange: function(contents, $editable){ //텍스트 글자수 및 이미지등록개수
      setContentsLength(contents);
    }
  }
}


// 썸머 노트 글자수 체크
function setContentsLength(str) {
  let status = false
  let textCnt = 0 // 총 글자수
  let maxCnt = 1000000 // 최대 글자수
  let editorText = f_SkipTags_html(str) // 에디터에서 태그를 삭제하고 내용만 가져오기

  textCnt = editorText.length
  if(maxCnt > 0) {
    if(textCnt > maxCnt) {
      status = true;
    }
  }

  if(status) {
    let msg = "글자수는 최대 " + maxCnt + "까지 등록이 가능합니다. / 현재 글자수 : " + textCnt + "자"
    alert(msg)
  }
}


// 썸머노트 파일 첨부
function RealTimeImageUpdate(files) {
  let reg = /(.*?)\.(gif|jpg|png|jepg)$/; //허용할 확장자
  let formData = new FormData();
  let fileArr = Array.prototype.slice.call(files);
  let filename = "";
  let fileCnt = 0;

  fileArr.forEach(function(f){
    filename = f.name;
    if(filename.match(reg)) {
      formData.append('file[]', f);
      fileCnt++;
    }
  })

  formData.append('tempFolder', $('#tempFolder').val())
}


//에디터 내용 텍스트 제거
function f_SkipTags_html(input, allowed) {
  // 허용할 태그는 다음과 같이 소문자로 넘겨받습니다. (<a><b><c>)
  allowed = (((allowed || "") + "").toLowerCase().match(/<[a-z][a-z0-9]*>/g) || []).join('');
  let tags = /<\/?([a-z][a-z0-9]*)\b[^>]*>/gi,
      commentsAndPhpTags = /<!--[\s\S]*?-->|<\?(?:php)?[\s\S]*?\?>/gi;
  return input.replace(commentsAndPhpTags, '').replace(tags, function ($0, $1) {
    return allowed.indexOf('<' + $1.toLowerCase() + '>') > -1 ? $0 : '';
  });
}


// 상품 생성 api 호출
const callProductCreateApi = () => {
  let tagAges = []
  $('input:checkbox[name="tagAge"]:checked').each((i, element) => {
    tagAges[i] = $(element).val()
  })

  let tagWiths = []
  $('input:checkbox[name="tagWith"]:checked').each((i, element) => {
    tagWiths[i] = $(element).val()
  })

  let options = []
  $('[class="option-row"]').each((i, element) => {
    let option = {
      name : $(element).find('[name=optionName]').val(),
      quantity : $(element).find('[name=optionQuantity]').val(),
      price : $(element).find('[name=optionPrice]').val()
    }
    options.push(option)
  })

  let productInformation = {
    categoryMiddleAltId: productInfo.$categoryMiddle.val(),
    productName: productInfo.$productName.val(),
    zipCode : productInfo.$zipcode.val(),
    address1 : productInfo.$address1.val(),
    address2 : productInfo.$address2.val(),
    extraAddress : productInfo.$extraAddress.val(),
    tagGender : $('[name="tagGender"]:checked').val(),
    tagAge: tagAges.join('|'),
    tagWith: tagWiths.join('|'),
    closedAt: productInfo.$closedAt.val(),
    type: $('[name="type"]:checked').val(),
    description: productInfo.$description.val(),
    optionInfoList: options
  }

  let formData = new FormData()
  let productInfoJson = JSON.stringify(productInformation)
  let blob = new Blob([productInfoJson], {type: "application/json"})
  formData.append('productInfo', blob)

  let files = $('#image')[0].files
  for (const file of files) {
    formData.append('profileImgFiles', file)
  }

  $.ajax({
    url: '/api/v2/hosts/' + hostAltId + '/products',
    method: 'POST',
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    data: formData,
    success: () => {
      isFormSubmitted = true
      alert("상품 등록이 완료되었습니다.")
      window.location.replace("/host/product/list")
    },
    error: (e) => {
      if (e.responseJSON.status === 500) {
        alert("오류가 발생했습니다. 관리자에게 문의하세요.")
      }
    }
  })
}


$(document).ready(() => {

  // 상품 정보 선택자 객체
  productInfo.$setDefaultProductClosedAt = $('#setDefaultProductClosedAt')
  productInfo.$setProductClosedAt = $('#setProductClosedAt')
  productInfo.$reservation = $('#reservation')
  productInfo.$pass = $('#pass')
  productInfo.$optionType_reservation = $('#optionType_reservation')
  productInfo.$optionType_pass = $('#optionType_pass')
  productInfo.$addOption = $('#addOption')
  productInfo.$removeOption = $('#removeOption')
  productInfo.$preview = $('#preview')
  productInfo.$tagBox = $('#tagBox')
  productInfo.$introductionLength = $('#introductionLength')

  // 기본 옵션 row 추가
  addOptionRow("PASS")

  // 판매 종료일 설정(기본)
  configDefaultProductClosedAt()

  // 판매 종료일 설정(지정한 날짜까지 판매) : 최대 날짜 현재일로 부터 일년 지정
  configProductClosedAt()

  // 카테고리 목록 가져오기
  getCategories()

  // 대분류에 따른 중분류 카테고리 목록 selectbox에 추가
  productInfo.$categoryLarge.on('change', () => {
    addCategoryMiddlesOfCategoryLarge()
  })

  // 판매 종료일 선택에 따른 이벤트
  productInfo.$setDefaultProductClosedAt.on('click', () => {
    selectDefaultClosedAt()
  })

  productInfo.$setProductClosedAt.on('click', () => {
    selectSetClosedAt()
  })

  // 판매 종료일에 따른 예약형 옵션 최대 날짜 설정
  productInfo.$closedAt.on('change', () => {
    let optionNames = $('input[type=datetime-local][name=optionName]')
    let closedAtValue = productInfo.$closedAt.val()
    if (optionNames.length > 0) {
      optionNames.each((i, optionName) => {
        $(optionName).attr('max', closedAtValue + 'T23:59')
      })
    }
  })

  // 판매 유형 선택 이벤트
  productInfo.$optionType_reservation.on('click', ()=>{
    selectOptionType(productInfo.$reservation, productInfo.$pass)
  })

  productInfo.$optionType_pass.on('click', ()=>{
    selectOptionType(productInfo.$pass, productInfo.$reservation)
  })

  // 옵션 목록 추가 이벤트
  productInfo.$addOption.on('click', ()=>{
    let getOptionType = $('[name=type]:checked').val()
    addOptionRow(getOptionType)
  })

  // 옵션 삭제 버튼 클릭
  productInfo.$removeOption.on('click', () => {
    removeOptionRow()
  })

  // 옵션 선택 클릭 이벤트
  $(document).on('click', '.option-row > td:nth-child(n+2)', (e) => {
    optionCheck(e)
  })

  // 옵션 수량, 옵션 가격 - 숫자 외 문자 삭제
  $(document).on('input', '[name=optionQuantity], [name=optionPrice]', (e) => {
    let target = $(e.currentTarget)
    let numberCheck = /[^0-9]/g
    replaceCharToAgainstRegExp(target, numberCheck, '')
  })

  // 썸머 노트 config
  productInfo.$description.summernote(setSummerNote)

  // 해빗 등록 버튼 클릭
  $('#productCreateSubmitButton').on('click', () => {
    if (!(categorySelectVerify() && productNameVerify() && addressVerify()
        && tagVerify() && productClosedAtVerify() && optionsVerify()
        && imageVerify() && descriptionVerify())) {
      return
    }

    if (confirm('해빗을 등록하시겠습니까?')) {
      callProductCreateApi()
    }
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
