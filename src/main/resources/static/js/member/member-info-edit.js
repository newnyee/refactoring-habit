function setProfile(event) {
    var reader = new FileReader();

    reader.onload = function(event) {
      $('.Home_proimg').attr('src',event.target.result);
    };

    reader.readAsDataURL(event.target.files[0]);
  }

function checkLength(){


    if($('#user_phone1').val().length!=3){
        $('#user_phone1').val( $('#user_phone1').val().slice(0,3));
    }

    if($('#user_phone2').val().length!=4){
        $('#user_phone2').val($('#user_phone2').val().slice(0,4));
    }

    if($('#user_phone3').val().length!=4){
        $('#user_phone3').val( $('#user_phone3').val().slice(0,4));
    }

}

function showModal(){
    $(".intro_modal").css("display","block");
}

function closeEvent(){
    $(".change_pw_com").html("현재 비밀번호를 입력해주세요.");
    $(".change_pw").html("비밀번호 확인");
    $(".pw_check").css("display","inline-block");
    $(".change_pw_btn").css("display","none");
    $(".intro_modal").css("display","none");
    $("#nowpw").attr("disabled",false);
    $("#nowpw").val("");
}

//비밀번호 확인
function checkPw(){

    let pw=$("#nowpw").val();
    let params={"pw":pw};
    $.ajax({
        type:"GET"
        ,url :"/mypage/checkpw"
        ,data:params
        ,async:false
        ,success:function(data) {
            console.log(data);

            if(data=="NOK"){
                $(".error_pw").html("현재 비밀번호와 다릅니다. 다시 입력해주세요.");
            }else if(data=="OK"){
                $(".error_pw").html("");
                $("#nowpw").val("");
                $(".pw_check").css("display","none");
                $(".change_pw_btn").css("display","inline-block");
                $(".change_pw").html("비밀번호 변경");
                $(".change_pw_com").html("변경하실 비밀번호를 입력해주세요.");
            }

        }
    });
}

//비밀번호 변경
function changePw(){
    let pw=$("#nowpw").val().trim();
    if(pw.length<8){
        alert("띄어쓰기 미포함 비밀번호 8자리 이상 입력해주세요");
    }else {
        let params = {"pw": pw};

        $.ajax({
            type: "POST"
            , url: "/mypage/changepw"
            , data: params
            , async: false
            , success: function (data) {
                //console.log(data);
                if (data.status == "OK") {
                    $("#user_pw").val(data.pw);
                    $(".change_pw_com").html("변경 성공");
                    $(".change_pw").html("비밀번호 변경 완료");
                    $(".change_pw_btn").css("display","none");
                    $("#nowpw").attr("disabled", "disabled");
                } else {
                    alert("변경실패 다시 시도해주세요.");
                }

            }
        });
    }
}

//
function checkNumber(obj){
    obj.value = obj.value.replace(/[^0-9]/g, "");
}

//submit 유효성 체크
function myProfile(){
    if($('#user_name').val()==""){
        alert("닉네임을 입력해주세요");
        $("#user_name").focus();
        return false;
    }

    if($("#user_phone1").val()==""||$("#user_phone2").val()==""||$("#user_phone3").val()==""){
        alert("전화번호를 정확하게 입력해주세요");
        $("#user_phone1").focus();
        return false;
    }

    if($("#user_email1").val()==""){
        alert("이메일을 입력해주세요");
        $("#user_email1").focus();
        return false;
    }

    if($("#user_pw").val()=="" || $("#user_pw").val().length<5){
        alert("비밀번호를 5글자이상 입력해주세요");
        $("#user_pw").focus();
        return false;
    }

    if($("#user_birth").val()==""){
        alert("생년월일을 입력해주세요");
        $("#user_birth").focus();
        return false;
    }
}












