
$(document).ready(() => {

  // 대분류에 맞는 중분류 가져오기
  $('#cate_large').on('change', (e)=>{
    let cate_large = e.currentTarget.value
    $.ajax({
      url: '/host/product/category/' + cate_large,
      type: 'get',
      success: (list) => {
        document.getElementById('cate_middle').replaceChildren()
        $('#cate_middle').append("<option value='0'>2차 카테고리</option>")
        for (let entity of list) {
          $('#cate_middle').append("<option value='" + entity.cate_middle + "'>" + entity.cate_middle + "</option>")
        }
      }
    })
  })


  // 판매종료일 : 지정한 날짜까지 판매일 최대 날짜 현재일로 부터 한달 지정
  let now = new Date()
  let maxDate = new Date(now.setMonth(now.getMonth() + 1)).toISOString().split("T")[0]
  let minDate = new Date(new Date().setDate(new Date().getDate()+6)).toISOString().split("T")[0]
  document.getElementById('endate_option2').setAttribute('max', maxDate)
  document.getElementById('endate_option2').setAttribute('min', minDate)


  // 판매 유형 이벤트
  $('#one').on('click', ()=>{
    if (confirm('기존에 입력하신 옵션들이 초기화 됩니다. 유형을 변경하시겠습니까?')) {
      // 원데이클래스 옵션창 보여주기
      document.getElementById('cont_option_one').removeAttribute('hidden')
      // 인원권/회차권 옵션창 숨기기
      document.getElementById('cont_option_prod').setAttribute('hidden', true)
      // 인원권/회차권 옵션창 reset
      let prod_name = $('input[name="p_name"]')
      prod_name[0].value = null
      let prod_qty = $('input[name="p_qty"]')
      prod_qty[0].value = null
      let prod_price = $('input[name="p_price"]')
      prod_price[0].value = null
      let option_row_prod = document.getElementById('option_row_prod')
      option_row_prod.replaceChildren()
    } else {
      return false
    }
  })

  $('#prod').on('click', ()=>{
    if (confirm('기존에 입력하신 옵션들이 초기화 됩니다. 유형을 변경하시겠습니까?')) {
      // 인원권/회차권 옵션창 보여주기
      document.getElementById('cont_option_one').setAttribute('hidden', true)
      // 원데이클래스 옵션창 숨기기
      document.getElementById('cont_option_prod').removeAttribute('hidden')
      // 원데이클래스 옵션창 reset
      let one_date = $('input[name="o_name"]')
      one_date[0].value = null
      let one_maxqty = $('input[name="o_qty"]')
      one_maxqty[0].value = null
      let one_price = $('input[name="o_price"]')
      one_price[0].value = null
      let option_row_one = document.getElementById('option_row_one')
      option_row_one.replaceChildren()
    } else {
      return false
    }
  })


  // 옵션 목록 이벤트 : 인원권/회차권
  let row_prod = "<tr>\n" +
      "                      <td><input class=\"form-check-input\" type=\"checkbox\" name=\"cont_option_prod\" id=\"\"></td>\n" +
      "                      <td>\n" +
      "                        <div>\n" +
      "                          <input type=\"text\" name='p_name' class=\"form-control\">\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                      <td>\n" +
      "                        <div>\n" +
      "                          <input type=\"number\" name='p_qty' min='0' class=\"form-control\">\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                      <td>\n" +
      "                        <div class=\"input-group mb-2\">\n" +
      "                          <span class=\"input-group-text\">판매가</span>\n" +
      "                          <input type=\"number\" class=\"form-control\" name='p_price' min='0' aria-label=\"Amount (to the nearest dollar)\">\n" +
      "                          <span class=\"input-group-text\">원</span>\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                    </tr>"
  $('#add_option_prod').on('click', ()=>{
    $('#option_row_prod').append(row_prod)
  })

  $('#option_remove_prod').on('click', () => {
    let checked = $('input:checkbox[name="cont_option_prod"]:checked')

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
  })

  // 옵션 목록 이벤트 : 원데이 클래스
  let row_one = "<tr>\n" +
      "                      <td><input class='form-check-input' type='checkbox' name='cont_option_one' id=''></td>\n" +
      "                      <td>\n" +
      "                        <div>\n" +
      "                          <input class='form-control' name='o_name' max=" + maxDate + " min=" + minDate + " type='datetime-local'>\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                      <td>\n" +
      "                        <div>\n" +
      "                          <input type='number' name='o_qty' min='0' class='form-control'>\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                      <td>\n" +
      "                        <div class='input-group mb-2'>\n" +
      "                          <span class='input-group-text'>판매가</span>\n" +
      "                          <input type='number' class='form-control' name='o_price' min='0' aria-label='Amount (to the nearest dollar)'>\n" +
      "                          <span class='input-group-text'>원</span>\n" +
      "                        </div>\n" +
      "                      </td>\n" +
      "                    </tr>"
  $('#add_option_one').on('click', ()=>{
    $('#option_row_one').append(row_one)
  })

  $('#option_remove_one').on('click', () => {
    let checked = $('input:checkbox[name="cont_option_one"]:checked')

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
  })


  /* 썸머 노트 config */
  $('#summernote').summernote({
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
        RealTimeImageUpdate(files, this);
      },
      onChange: function(contents, $editable){ //텍스트 글자수 및 이미지등록개수
        setContentsLength(contents, 0);
      }
    }
  })
})

/* 썸머 노트 config */
//글자수 체크
//태그와 줄바꿈, 공백을 제거하고 텍스트 글자수만 가져옵니다.
function setContentsLength(str, index) {
  var status = false;
  var textCnt = 0; //총 글자수
  var maxCnt = 60000; //최대 글자수
  var editorText = f_SkipTags_html(str); //에디터에서 태그를 삭제하고 내용만 가져오기
  editorText = editorText.replace(/\s/gi,""); //줄바꿈 제거
  editorText = editorText.replace(/&nbsp;/gi, ""); //공백제거

  textCnt = editorText.length;
  if(maxCnt > 0) {
    if(textCnt > maxCnt) {
      status = true;
    }
  }

  if(status) {
    var msg = "글자수는 최대 "+maxCnt+"까지 등록이 가능합니다. / 현재 글자수 : "+textCnt+"자";
    alert(msg)
  }
}

function RealTimeImageUpdate(files, editor) {
  var reg = /(.*?)\.(gif|jpg|png|jepg)$/; //허용할 확장자
  var formData = new FormData();
  var fileArr = Array.prototype.slice.call(files);
  var filename = "";
  var fileCnt = 0;
  fileArr.forEach(function(f){
    filename = f.name;
    if(filename.match(reg)) {
      formData.append('file[]', f);
      fileCnt++;
    }
  });
  formData.append('tempFolder', $('#tempFolder').val());
}

//에디터 내용 텍스트 제거
function f_SkipTags_html(input, allowed) {
  // 허용할 태그는 다음과 같이 소문자로 넘겨받습니다. (<a><b><c>)
  allowed = (((allowed || "") + "").toLowerCase().match(/<[a-z][a-z0-9]*>/g) || []).join('');
  var tags = /<\/?([a-z][a-z0-9]*)\b[^>]*>/gi,
      commentsAndPhpTags = /<!--[\s\S]*?-->|<\?(?:php)?[\s\S]*?\?>/gi;
  return input.replace(commentsAndPhpTags, '').replace(tags, function ($0, $1) {
    return allowed.indexOf('<' + $1.toLowerCase() + '>') > -1 ? $0 : '';
  });
}


// 카테고리 체크
const cateSelectCheck = () => {

  if ($('#cate_large').val() !== '0') {
    $('#cate_small').css('display', 'none')
  }

  if ($('#cate_middle').val() !== '0') {
    $('#cate_small').css('display', 'none')
  }
}


// 해빗 명 체크
const contNameCheck = () => {
  let cont_name = $('#cont_name')
  let cont_name_small = $('#cont_name_small')
  if(cont_name.val().length < 1) {
    cont_name_small.css('display', 'block')
    cont_name.focus()
    return
  } else {
    cont_name_small.css('display', 'none')
  }
}


// 판매 종료일 이벤트 체크
const contEndateOptionCheck1 = (e) => {
  let endate_option2 = $('#endate_option2')
  endate_option2.attr('disabled', true)
}

const contEndateOptionCheck2 = (e) => {
  let endate_option2 = $('#endate_option2')
  endate_option2.removeAttr('disabled')
}

const endDateCheck = () => {
  if ($('#endate_option2').val().length > 0) {
    $('#end_small').css('display', 'none')
  }
}


// 대표 이미지 체크
const contImgCheck = (imgs) => {
  let preview_img_container = $('#preview_img_container')
  let cont_img_small = $('#cont_img_small')
  if(imgs.files.length>3) { // 파일을 4개 이상 첨부한 경우
    let previewChildren = preview_img_container.children().children()
    for (let children of previewChildren) {
      children.getAttribute('src', '/img/No_image_available.png')
    }
    cont_img_small.css('display', 'block')
    return

  } else {
    preview_img_container.children().remove()
    for (let i = 1; i <= 3; i++) {
      if (imgs.files[i-1] !== undefined) {
        let tmpPath = URL.createObjectURL(imgs.files[i-1])
        str1 =
            "                <div>\n" +
            "                  <img src=' "+ tmpPath +" ' class='preview_img' id='preview_cont_img" + i + "' alt='이미지 없음' width='200px' height='200px' style='border-radius: 15px'>\n" +
            "                </div>"
        preview_img_container.append(str1)
      } else if (imgs.files[i-1] === undefined) {
        str2 =
            "                <div>\n" +
            "                  <img src='/img/No_image_available.png' class='preview_img' id='preview_cont_img" + i + "' alt='이미지 없음' width='200px' height='200px'>\n" +
            "                </div>"
        preview_img_container.append(str2)
      }
    }
  }

  if (imgs.files.length <= 3 && imgs.files.length > 0) {
    cont_img_small.css('display', 'none');
  }
}


//habit_create 유효성 검사
const habitCreateCheck = () => {

  // 카테고리 : 대분류
  let cate_large = $('#cate_large')
  if(cate_large.val() === '0') {
    $('#cate_small').css('display', 'block')
    cate_large.focus()
    return false
  }

  // 카테고리 : 중분류
  let cate_middle = $('#cate_middle')
  if(cate_middle.val() === '0') {
    $('#cate_small').css('display', 'block')
    cate_middle.focus()
    return false
  }

  // 해빗명
  let cont_name = $('#cont_name')
  if(cont_name.val().length<1 || cont_name.val().length>50) {
    $('#cont_name_small').css('display', 'block')
    cont_name.focus()
    return false
  }

  // 진행장소
  let zipcode = $('#zipcode')
  if (zipcode.val().length<1) {
    $('#address_small').css('display', 'block')
    zipcode.focus()
    return false
  }

  // 해빗 간단 정보
  let info2 = $('input:checkbox[name="prod_info2"]:checked')
  let info4 = $('input:checkbox[name="prod_info4"]:checked')

  if (info2.length === 0 || info4.length === 0) {
    alert("해시태그를 문항당 한개 이상 체크해주세요.")
    return false
  }

  // 판매 종료일 확인
  let cont_endate_type = $('input:radio[name="prod_end_type"]:checked')
  if (cont_endate_type.attr('id') === 'cont_endate_option2') {
    let endate_option2 = $('#endate_option2')
    if (endate_option2.val().length < 1) {
      $('#end_small').css('display', 'block')
      endate_option2.focus()
      return false
    }
  }

  // 옵션 목록 입력
  let pro = $('input[name="opt_type"]:checked');
  if(pro.attr('id') === 'prod') { // 선택된 옵션이 날짜 조율형 일때
    let prod_names = $('input[name="p_name"]')
    let count_prod_name = 0
    for (let prod_name of prod_names) {
      if(prod_name.value.length <1 ) {
        count_prod_name++
      }
    }
    if(count_prod_name>0) {
      alert('옵션명을 입력해주세요')
      return false
    }

    // 옵션 수량
    let prod_qtys = $('input[name="p_qty"]')
    let count_prod_qty = 0
    for (let prod_qty of prod_qtys) {
      if(prod_qty.value.length <1 ) {
        count_prod_qty++
      }
    }
    if(count_prod_qty>0) {
      alert('옵션의 수량을 입력해주세요')
      return false
    }

    // 옵션 가격
    let prod_prices = $('input[name="p_price"]')
    let count_prod_price = 0
    let value_prod_price = 0
    for (let prod_price of prod_prices) {
      if(prod_price.value.length <1 ) {
        count_prod_price++
      }
      if (prod_price.value < 5000) {
        value_prod_price++
      }
    }
    if(count_prod_price > 0) {
      alert('옵션의 금액을 입력해주세요')
      return false
    }
    if (value_prod_price > 0) {
      alert('옵션의 최소가격은 5000원 입니다')
      return false
    }

  } else if(pro.attr('id') === 'one') { // 선택된 옵션이 날짜 지정형 일때
    let one_names = $('input[name="o_name"]')
    let count_one_name = 0
    for (let one_name of one_names) {
      if(one_name.value.length <1 ) {
        count_one_name++
      }
    }
    if(count_one_name>0) {
      alert('옵션의 실행일자를 설정해주세요')
      return false
    }

    // 옵션 수량
    let one_qtys = $('input[name="o_qty"]')
    let count_one_qty = 0
    for (let one_qty of one_qtys) {
      if(one_qty.value.length <1 ) {
        count_one_qty++
      }
    }
    if(count_one_qty>0) {
      alert('옵션의 최대 모집인원을 설정해주세요')
      return false
    }

    // 옵션 가격
    let one_prices = $('input[name="o_price"]')
    let count_one_price = 0
    let value_one_price = 0
    for (let one_price of one_prices) {

      if(one_price.value.length <1 ) {
        count_one_price++
      }
      if (one_price.value < 5000) {
        value_one_price++
      }
    }
    if(count_one_price>0) {
      alert('옵션의 금액을 입력해주세요')
      return false
    }
    if (value_one_price > 0) {
      alert('옵션의 최소가격은 5000원 입니다')
      alert('옵션의 최소가격은 5000원 입니다')
      return false
    }
  }

  // 대표 이미지
  let cont_img = document.getElementById('cont_img').files
  if(cont_img.length<1 || $('#cont_img_small').css('display') === 'block') {
    $('#cont_img_small').css('display', 'block')
    $('#cont_img').focus()
    return false
  }

  // 해빗 상세 설명
  let summernote = $('#summernote')
  if(summernote.val().length<10) {
    alert('해빗 상세 설명을 10자 이상 입력해주세요.')
    summernote.focus()
    return false
  }

  if (confirm('해빗을 등록하시겠습니까?')) {
    return true
  } else {
    return false
  }
}

