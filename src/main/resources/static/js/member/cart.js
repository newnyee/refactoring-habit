$(document).ready(function(){

    //수량 플러스 마이너스
    let min=$('.btn_min');

    min.click(function(){
        //alert(this.previousSibling.previousSibling.innerText);
        if(parseInt(this.previousSibling.previousSibling.value)==1){
            this.previousSibling.previousSibling.value=1;
        }else{
        this.previousSibling.previousSibling.value-=1;
        };

    })

    let plus=$('.btn_plus');

    plus.click(function(){

        this.previousSibling.previousSibling.previousSibling.previousSibling.value
        =parseInt(this.previousSibling.previousSibling.previousSibling.previousSibling.value)+1;

    })


    //전체선택
    $('.all_select').click(function(){
      //alert("ddd");
      $('.Home_cart_check').prop('checked',true);
    });


})


function calc(){
    let sumprice=0;

    let length1=$(".cartlist1").length;

    for(let i=0;i<length1;i++){
        let price=$(".oneprice"+i).text();
        price=price.replace(",","");
        let qty=$(".one"+i).val();
        sumprice+=price*qty;
    }


    sumprice = sumprice.toLocaleString();
    $(".totalPrice").text(sumprice);
}


function order(){
    location.href = "order.html" // 임시 이동
    let checkboxSize=$(".Home_cart_check").length;
    let checkCart="";
    let length="";
    for(let i=0;i<checkboxSize;i++) {
        if ($("#oneck" + i).is(":checked") == true) {
            checkCart += $("#oneck" + i).val() + "-";
            length +=$(".one"+i).val()+"-";
        }

        console.log(checkCart);

    }


    if(checkCart!=""){
        //수량변경되거 DB 반영
        $.ajax({
            type:"POST"
            ,url:"/cart/change"
            ,data:{"cl_nos":checkCart,"qty":length}
            ,async: false
            ,success:function(data){
                console.log(data);
            }
        });



        //if(confirm("주문할까요?")){
                        location.href='/cart/order/payPage?cartno='+checkCart;
        //}
    }

}