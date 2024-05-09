$(document).ready(()=>{

  window.onload=function(){
      //common.js
      common();

  }

  $('#user_email').on('keyup', ()=> {
      let emailSmall = $('#email-small')
      emailSmall.text("")
      let user_email = $('#user_email').val().trim()
      let emailCheck = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/
      if (emailCheck.test(user_email) === false) {
          emailSmall.text("이메일 형식에 맞게 입력해주세요")
          emailSmall.removeAttr('hidden')
      } else {
          emailSmall.attr('hidden', true)
      }
  })

  // 유효성 검사
  checkForm = () =>{
      let emailSmall = $('#email-small')
      emailSmall.text("")

      // 이메일 입력 check
      if ($('#user_email').val().length < 1) {
          emailSmall.removeAttr('hidden')
          emailSmall.text("이메일을 입력해주세요")
          return false
      } else {
          emailSmall.attr('hidden', true)
      }

      // 이메일 형식 check
      let user_email = $('#user_email').val().trim()
      let emailCheck = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/
      if (emailCheck.test(user_email) === false) {
          emailSmall.removeAttr('hidden')
          emailSmall.text("이메일 형식에 맞게 입력해주세요")
          return false
      } else {
          emailSmall.attr('hidden', true)
      }
      return true
  }
})
