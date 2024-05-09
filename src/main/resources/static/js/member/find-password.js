$(document).ready(()=>{
  window.onload=function(){
       //common.js
       common();
  }

  $('#sendEmail').on('click', ()=>{

       if (confirm("임시 비밀번호를 발급받으시겠습니까?")) {
            let checkMethod = $('input[type=radio][name="method"]:checked')
            let methodOfFine = checkMethod.attr("id")
            let methodValue = checkMethod.val()

            let requestData = {
                 'methodOfFine': methodOfFine,
                 'methodValue': methodValue
            }

            $.ajax({
                 url: '/findpw/result',
                 type: 'post',
                 dataType: 'json',
                 data: requestData,
                 success: (result) => {
                      if (result == 1) {
                           $('#methodSelect').attr('hidden', true);
                           $('#result').removeAttr('hidden')
                      }
                 },
                 beforeSend: () => {
                      $('#div_load_img').css('display', 'flex')
                 },
                 complete: () => {
                      $('#div_load_img').css('display', 'none')
                 }
            })
       } else {
            return false
       }

  })
})
