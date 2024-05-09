function setProfile(event) {
    var reader = new FileReader();

    reader.onload = function(event) {
      $('.Home_proimg').attr('src',event.target.result);
    };

    reader.readAsDataURL(event.target.files[0]);
  }

function checkLength(){


    if($('#user_phone').val().length!=3){
        $('#user_phone').val( $('#user_phone').val().slice(0,3));
    }

    if($('#user_phone2').val().length!=4){
        $('#user_phone2').val($('#user_phone2').val().slice(0,4));
    }

    if($('#user_phone3').val().length!=4){
        $('#user_phone3').val( $('#user_phone3').val().slice(0,4));
    }

}


  function join_check() {
      //변수에 담아주기
      let user_id = document.getElementById("user_id");
      let user_pw = document.getElementById("user_pw");
      let user_name = document.getElementById("user_name");
      let user_gender = document.getElementById("user_gender");
      let user_phone = document.getElementById("user_phone");
      let user_phone2 = document.getElementById("user_phone2");
      let user_phone3 = document.getElementById("user_phone3");
      let user_email = document.getElementById("user_email");
      let user_email2 = document.getElementById("user_email2");
      let user_birth = document.getElementById("user_birth");

      if (user_id.value == ""||user_id.value.length < 5) { //해당 입력값이 없을 경우 같은말: if(!uid.value)
          alert("아이디 중복확인해주세요.");
          user_id.focus(); //focus(): 커서가 깜빡이는 현상, blur(): 커서가 사라지는 현상
          return false; //return: 반환하다 return false:  아무것도 반환하지 말아라 아래 코드부터 아무것도 진행하지 말것
      };

      if (user_pw.value == ""||user_pw.value.length<8) {
          alert("비밀번호 8자 이상 입력해주세요");
          user_pw.focus();
          return false;
      };


      // 아이디 특수문자 검사
 /*     var re = /^[a-zA-Z0-9_!@#$%^&*()-]+$/;
      if (!re.test(id)) {
          alert("아이디는 영문자, 숫자, 특수문자(_!@#$%^&*())만 사용 가능합니다.");
          return false;
      }*/

      // 닉네임 길이 검사
      if (user_name.value.length < 3) {
          alert("닉네임은 3자 이상이어야 합니다.");
          user_name.focus();
          return false;
      }

      //성별검사
      if($("input[name='user_gender']:radio:checked").length<1){
          alert("성별을 선택해주세요");
          $("#user_gender").focus();
          return false;
      }

      //이메일
      if(user_email.value==""||$("select[name='user_email2']").val() == "0"){
          alert("이메일을 입력해주세요.");
          user_email.focus();
          return false;
      }
      //후대폰번호
      if(user_phone.value==""||user_phone2.value==""||user_phone3.value==""){
          alert("휴대폰 번호를 입력해주세요");
          document.getElementById("user_phone").focus();
          return false;
      }

      if(user_birth.value==""){
          alert("생년월일을 선택해주세요");
          document.getElementById("user_birth").focus();
          return  false;

    }   
}
