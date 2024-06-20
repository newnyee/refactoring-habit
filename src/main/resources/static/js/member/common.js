// 로그아웃 api 호출
const callLogoutApi = () => {
    $.ajax({
        method: "POST",
        url: "/api/v2/auth/sign-out",
        success: () => {
            alert("로그아웃 되었습니다.")
            location.href = "/"
        }
    })
}

$(document).ready(function(){
    if($.cookie("modal")!='check'){
        $('.intro_modal').css('display','block');
    }

    //검색어 쿠키
    let html = "";
    for (let i = 0; i < 6; i++) {
        if ($.cookie("search" + i) != undefined) {
            let search = $.cookie("search" + i);
            html += "<a href='/search?recentSearch=" + search + "'><div class=\"global_modal_searchNew\">" + search + "</div></a>";
        }
    }
    $(".recent_search").append(html);

    $('.search_input').click(function(){
        $('.global_modal').css('display','block');

        let modalHeight=$('.global_modal').css('height');
        let formHeight=$('.global_modal_form').css('height');
        modalHeight=modalHeight.substr(0,modalHeight.length-2);
        formHeight=formHeight.substr(0,formHeight.length-2);
        
        let height=modalHeight-formHeight;
        $('.global_modal_bottom').css('height',height);
        $('.global_modal_bottom').click(function(){
            if($('.global_modal').css('display')==='block'){
                $('.global_modal').css('display','none');
            }
        });

        //인기검색어
        $.ajax({
            type: "GET"
            , url: "/search/hotSearch"
            , async: false
            , success: function (data) {
                console.log(data);
                if ($(".global_modal_searchResult").length!=0) {
                    $(".hot_search").children().remove();
                }
                let html="";
                $(data).each(function(index,value){
                    html+="<a href='/search?recentSearch="+value+"'><div class=\"global_modal_searchResult\">"+value+"</div></a>";
                });
                $(".hot_search").append(html);
            }
        });
    });

    $('.logout_button').on('click', () => {
        if (confirm("로그아웃 하시겠습니까?")) {
            $(window).off("beforeunload");
            callLogoutApi();
        }
    })
})

const setCookie = (name, value, minutesToLive) => {
    let expirationDate = new Date()
    expirationDate.setTime(expirationDate.getTime() + (minutesToLive * 60 * 1000))
    let expires = "expires=" + expirationDate.toUTCString()
    document.cookie = name + "=" + value + ";" + expires + ";path=/"
}

const getCookie = (name) => {
    let cookieName = name + "="
    let decodedCookie = decodeURIComponent(document.cookie)
    let cookieArray = decodedCookie.split(';')

    for (let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i].trim()
        if (cookie.indexOf(cookieName) === 0) {
            return cookie.substring(cookieName.length, cookie.length)
        }
    }
    return null
}

function checkSearch(){
    if($(".search_input").val()==""){
        alert("검색어를 입력해주세요");
        return false;
    }
}

//최근검색어 지우기
function delSearch(){

    for (let i = 0; i < 6; i++) {
        if ($.cookie("search" + i) != undefined) {
            $.removeCookie('search'+i);
            $(".recent_search").children().remove();

        }
    }
}
