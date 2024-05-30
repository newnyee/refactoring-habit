const reissueToken = async () => {
    console.log("access reissue Token js")
    try {
        const response = await fetch('/api/v2/auth/tokens', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                'Content-Type': 'application/json',
            },
        })

        if (response.ok) {
            const data = await response.json()
            localStorage.setItem('accessToken', data.data.accessToken)
            console.log(data.data.accessToken)
        } else {
            alert("세션이 만료되었습니다.")
            console.log("reissueToken error")
            window.location.href = '/login'
        }

    } catch (error) {
        console.error('There was a problem with the fetch operation:', error)
    }
}

const verifyToken = async () => {
    try {
        const response = await fetch('/api/v2/auth/verify-token', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            const data = await response.json()
            if (data.code === 'T004') { // 토큰 재발급
                await reissueToken();
            } else {
                alert("세션이 만료되었습니다.")
                window.location.href = '/login'
            }
        }
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error)
    }
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
})

function setCookie( name, value, exDay ) {
    var todayDate = new Date();
    todayDate.setDate( todayDate.getDate() + exDay ); 
    document.cookie = name+ "=" +  value + "; path=/; expires=" + todayDate.toGMTString() + ";"
    
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
    
    
  





    




