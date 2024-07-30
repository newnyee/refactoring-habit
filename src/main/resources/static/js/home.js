const getImageFileNameList = (imageFileNames) => {
    return imageFileNames.split("|")
}

const formatCurrency = (amount) => {
    return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' 원';
}

const createStarScoreImage = (starScore) => {
    let starScoreImage = '';
    if (starScore > 0) {
        let fullStar = '                            <img src="/img/star.png" alt="" class="review-star">\n';
        let halfStar = '                            <img src="/img/halfstar.png" alt="" class="review-star">\n'
        let fullStarCount = Math.floor(starScore)
        let hasHalfStar = (starScore - fullStarCount) >= 0.5

        for (let i = 0; i < fullStarCount; i++) {
            starScoreImage += fullStar
        }

        if (hasHalfStar) {
            starScoreImage += halfStar
        }
    }

    return starScoreImage
}

const createProductElement = (product) => {
    const imageFileNameList = getImageFileNameList(product.imageFileNames)
    return '    <div class="card-product-wrapper">\n'
        + '        <a href="/product/' + product.productAltId + '">\n'
        + '            <div class="product-image">\n'
        + '                <img src="/storage/'+ imageFileNameList[0] +'" width="150px" height="150px">\n'
        + '            </div>\n'
        + '            <div class="wish-button-wrapper">\n'
        + '                <button class="wish-button" onclick="hello()" onsubmit="return false">\n'
        + '                    <img src="/img/black2.png" alt="" width="40px">\n'
        + '                </button>\n'
        + '            </div>\n'
        + '            <div class="product-info-wrapper">\n'
        + '                <div>\n'
        + '                    <div class="product-name">' + product.name + '</div>\n'
        + '                    <div class="review-info">\n'
        + '                        <div class="review-average">\n'
        + createStarScoreImage(product.reviewAverage)
        + '                        </div>\n'
        + '                        <div class="review-count">\n'
        + '                            <span>리뷰 ' + product.reviewCount + '</span>\n'
        + '                        </div>\n'
        + '                    </div>\n'
        + '                    <hr class="Home_recommend_hr">\n'
        + '                    <div class="product-price">' + formatCurrency(product.minPrice) + '</div>\n'
        + '                </div>\n'
        + '            </div>\n'
        + '        </a>\n'
        + '    </div>'
}

const addProducts = (containerElement, productList) => {
    for (let i = 0; i < 4; i++) {
        containerElement.append(createProductElement(productList[i]))
    }
}

const callPopularProductsApi = () => {
    let popularProductContainer = $('.popular-product')
    $.ajax({
        url: '/api/v2/products/popular',
        method: 'GET',
        success: (response) => {
            console.log(response.data)
            addProducts(popularProductContainer, response.data)
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

const callNewProductsApi = () => {
    let newProductContainer = $('.new-product')
    $.ajax({
        url: '/api/v2/products/new',
        method: 'GET',
        success: (response) => {
            console.log(response.data)
            addProducts(newProductContainer, response.data)
        },
        error: (e) => {
            if (e.responseJSON.status === 500) {
                alert("오류가 발생했습니다. 관리자에게 문의하세요.")
            }
        }
    })
}

$(document).ready(() => {
    callPopularProductsApi()
    callNewProductsApi()

    $('.see-more-popular-products').on('click', () => {
        window.location.href = '/product/popular-list'
    })

    $('.see-more-new-products').on('click', () => {
        window.location.href = '/product/new-list'
    })
})

$(document).ready(function(){

    var btn=$('.zzim_btn');
    btn.click(function(){

        if($(".s_id").text()!="") {
            //찜아닐떄
            if (this.children[0].src.indexOf("black2.png") != -1) {
                this.children[0].src = "/img/redheart2.png";
                let id = this.children[0].id;
                console.log(id);
                let indexOf = id.indexOf("_");

                id = id.substring(indexOf+3, id.length);

                //$.ajax 써야 함. async:true 잊지 않고 추가하기
                if ($(".s_id").text() != "") {
                    $.ajax({
                        type: "POST"
                        , url: "/zzim/insert"
                        , data: {"cont_no": id}
                        , async: false
                        , success: function (data) {
                            console.log(data);
                            getZzim();
                        }
                    });
                }
            } else {   //찜일떄
                this.children[0].src = "/img/black2.png";
                let id = this.children[0].id;

                console.log(id);
                let indexOf = id.indexOf("_");

                id = id.substring(indexOf+3, id.length);
                //$.ajax 써야 함. async:true 잊지 않고 추가하기
                if ($(".s_id").text() != "") {
                    $.ajax({
                        type: "POST"
                        , url: "/zzim/del"
                        , data: {"cont_no": id}
                        , async: false
                        , success: function (data) {
                            console.log(data);
                            getZzim();
                        }
                    });
                }
            }
        }else{
            location.href="/login";
        }

    });

    $('.next').click(function(){
        //alert('hello');
        let matrix=$('.slide').css('transform');
        //alert(matrix);
        maX=matrix.split(',');
        
        //alert(maX[4]);
        if(parseInt(maX[4])%740==0){
            if(maX[4]===' -2960'){
                //alert("ddd");
                $('.slide').css('transition','transform 0s');
                $('.slide').css('transform','translate(0)');
                maX[4]=0;
            }
            else if(matrix==='none'){
                maX[4]=0;
                //alert(maX[4]);
            }
            maX[4]=(maX[4]-740)+'px';
            //alert(maX[4]);
            
            //transition:transform 0.5s;
            setTimeout(function(){
            $('.slide').css('transition','transform 0.5s');
            $('.slide').css('transform','translate('+maX[4]+')');
            },200)
        }
    });
    
    $('.prev').click(function(){
        let matrix=$('.slide').css('transform');
        maX=matrix.split(',');
        
        if(parseInt(maX[4])%740==0){
            if(maX[4]===' 0'||maX[4]===undefined){
                $('.slide').css('transition','transform 0s');
                $('.slide').css('transform','translate(-2960px)');
                maX[4]=-2960;
            }
            else if(matrix==='none'){
                maX[4]=0;
            }
            maX[4]=(parseInt(maX[4])+740)+'px';
             setTimeout(function(){
            $('.slide').css('transition','transform 0.5s');

            $('.slide').css('transform','translate('+maX[4]+')');
            },200)
        }
    });

    function next(){
        let matrix=$('.slide').css('transform');
        maX=matrix.split(',');
        
        if(parseInt(maX[4])%740==0){
            if(maX[4]===' -2960'){
                $('.slide').css('transition','transform 0s');
                $('.slide').css('transform','translate(0)');
                maX[4]=0;
            }
            else if(matrix==='none'){
                maX[4]=0;
            }
            maX[4]=(maX[4]-740)+'px';
            setTimeout(function(){
            $('.slide').css('transition','transform 0.5s');
            $('.slide').css('transform','translate('+maX[4]+')');

        },200)
        }
    }
   setInterval(next,8000);
})

function getZzim(){
    $(".zzim_img").attr("src", "/img/black2.png");

    $.ajax({
        type: "GET"
        , url: "/zzim/getZzim"
        , async: false
        , success: function (data) {
            //console.log(data);
            $(data).each(function (index, value) {
                //console.log(value);

                $("#cont_no" + value).attr("src", "/img/redheart2.png");
                $("#newcont_no"+value).attr("src", "/img/redheart2.png");
                $("#avgcont_no"+value).attr("src", "/img/redheart2.png");

            });
        }
    });
}

function closeEvent(){
    if($('.modal_day').is(':checked')){
       
      $.cookie('modal','check',{expires:1});
      console.log($.cookie("modal"));
            
    }
    $('.intro_modal').css('display','none');
}

