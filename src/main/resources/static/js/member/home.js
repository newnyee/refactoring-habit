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
