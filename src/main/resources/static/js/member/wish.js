//보여줄 상품 갯수
const showPro=8;


$(document).ready(function(){
  //common.js
  common();

 /* // 페이지 로드 후 페이징 초기화
  onPageClick(1);*/

    $(".Home_product_recommend_p").slice(0, showPro).css('display','block'); // 초기갯수

    var btn=$('.zzim_btn');
    btn.click(function (){

        //찜 해제할때
        /*$(this).parents().eq(3).remove();
        onPageClick($(".pactive"));*/
        window.location.reload();


    })
})


function onPageClick(ele) {

    //=====인덱스 전후 번튼 클릭시 페이징
    if(ele==-1) {
        //전버튼
        //console.log(ele);
        let activeIndex = $(".pactive").index();
        //console.log(activeIndex);
        if(activeIndex!='1'){
            paging($('.pactive'),ele);
        }


    }else if(ele==0){
        //후버튼
        //console.log(ele);
        let activeIndex = $(".pactive").index();
        //console.log(activeIndex);
        let length=$(".index_page_btn").length;

        if(activeIndex!=length){
            let way=1;
            paging($('.pactive'),way);
        }
    }else {
        //=====인덱스 숫자 클릭시 페이징
        $(".Home_product_recommend_p").css('display', 'none');

        let indexpage = $(ele).index();
        //console.log("기초"+$(ele).index());
        //console.log($(".index_page_btn").length);
        $(".Home_product_recommend_p").slice(showPro * (indexpage - 1), showPro * indexpage).show();


        //=====인덱스 화면상에서  3개만 보이게
        //첫번째 인덱스 클릭시
        if (indexpage == 1) {
            $(".index_page_btn").eq(indexpage - 1).css("display", "block");
            $(".index_page_btn").eq(indexpage).css("display", "block");
            $(".index_page_btn").eq(indexpage + 1).css("display", "block");
            //마지막 인덱스 클릭시
        } else if (indexpage == $(".index_page_btn").length) {
            $(".index_page_btn").eq(indexpage - 3).css("display", "block");
            $(".index_page_btn").eq(indexpage - 2).css("display", "block");
            $(".index_page_btn").eq(indexpage - 1).css("display", "block");
        } else {
            //인덱스 화면상에서  3개만 보이게
            $(".index_page_btn").css("display", "none");
            $(".index_page_btn").eq(indexpage - 2).css("display", "block");
            $(".index_page_btn").eq(indexpage - 1).css("display", "block");
            $(".index_page_btn").eq(indexpage).css("display", "block");
        }
        //클릭한 인덱스 색깔 바꾸기
        $(".paging>button").removeClass("pactive");
        $(ele).addClass("pactive");

    }
}

//인덱스전후 버튼 함수
function paging(ele,way){
    //=====인덱스 숫자 클릭시 페이징
    $(".Home_product_recommend_p").css('display', 'none');

    let indexpage = parseInt($(ele).index())+parseInt(way);
    //console.log(indexpage);
    //console.log("함수"+$(ele).index());
    //console.log($(".index_page_btn").length);
    $(".Home_product_recommend_p").slice(showPro* (indexpage - 1), showPro * indexpage).show();

    //첫번째 인덱스 클릭시
    if (indexpage == 1) {
        $(".index_page_btn").eq(indexpage - 1).css("display", "block");
        $(".index_page_btn").eq(indexpage).css("display", "block");
        $(".index_page_btn").eq(indexpage + 1).css("display", "block");
        //마지막 인덱스 클릭시
    } else if (indexpage == $(".index_page_btn").length) {
        $(".index_page_btn").eq(indexpage - 3).css("display", "block");
        $(".index_page_btn").eq(indexpage - 2).css("display", "block");
        $(".index_page_btn").eq(indexpage - 1).css("display", "block");
    } else {
        //인덱스 화면상에서  3개만 보이게
        $(".index_page_btn").css("display", "none");
        $(".index_page_btn").eq(indexpage - 2).css("display", "block");
        $(".index_page_btn").eq(indexpage - 1).css("display", "block");
        $(".index_page_btn").eq(indexpage).css("display", "block");
    }

    if(way==-1) {
        //클릭한 인덱스 색깔 바꾸기
        $(".paging>button").removeClass("pactive");
        $(ele).prev().addClass("pactive");
    }else{
        //클릭한 인덱스 색깔 바꾸기
        $(".paging>button").removeClass("pactive");
        $(ele).next().addClass("pactive");
    }
}




