var trade=new Array(5);
var cards=new Array(5);
var eventCards=new Array(5);
var me;
var playerId;
var select;
var spieler=new Array(4);
for(var i=0;i<5;i++){
    trade[i]=0;
    cards[i]=5;
}
var RightClick = false;
var i;
function setDice(dice0,dice1){
    $("#dice0").css({'background-image': 'url("../images/cube/wuerfel'+dice0+'.jpg")'});

    $("#dice1").css({'background-image': 'url("../images/cube/wuerfel'+dice1+'.jpg")'});
}

function fitToWindow() {
    // maximum height and width of field is half the size of fieldWrapper
    var windowW=$(window).innerWidth();
    var windowH= $(window).innerHeight();

    if(windowW>windowH){// info right
        $("#fieldWrapper").width("80%");
        $("#fieldWrapper").height("100%");
        $(".playerInfo").width("20%");
        $(".playerInfo").height("95%");
        $(".playerInfo").css({top: "5%", left: "80%"});
        $("#console").width("20%");
        $("#console").height("5%");
        $(".info").width(100/3+"%");
        $(".info").height($(".info").width());
        $("#console").css({top: "0%", left: "80%"});
        $(".info").css({
            'line-height': ''+1.65*$(".info").width()+'px',
            'font-size':''+1/3*$(".info").width()+'px'
        });
        $("#chat").height(($(".playerInfo").height()-(5*$(".info").height())-22)+'px');
        console.log(($(".playerInfo").height()-(5*$(".info").height())-22));

    }else{// info bottom
        $("#fieldWrapper").width("100%");
        $("#fieldWrapper").height("80%");
        $(".playerInfo").width("100%");
        $(".playerInfo").height("15%");
        $(".playerInfo").css({top: "85%", left: "0%"});
        $("#console").width("100%");
        $("#console").height("5%");
        $("#console").css({top: "80%", left: "0%"});
        $(".info").width("12.5%");
        $(".info").height($(".info").width());
        $(".info").css({
            'line-height': ''+1.65*$(".info").width()+'px',
            'font-size':''+1/3*$(".info").width()+'px'
        });
    }
    var $field = $("#field");
    var parentW = $field.parent().width(); // Get the height of whatever the
											// parent of field is
    var parentH = $field.parent().height(); 


    var div1 = document.getElementById("field");
    var players = document.getElementsByClassName("player");

    var fieldW = $field.width();// Grab the width of .square
    var fieldH = $field.height(); 
    /*
	 * var x = (parseFloat(div1.getAttribute('data-x')) || 0), y =
	 * (parseFloat(div1.getAttribute('data-y')) || 0);
	 */
    var zoom1 = (parentW / 417); 
    var zoom2 = (parentH / 362); 
    var zoom;
    if(parentW>parentH){
        zoom=parentW/100/5;
    }else{
        zoom=parentH/100/5;
    }

    if(zoom1<zoom2){
    }else{
        zoom1 = zoom2;
    }
    console.log(zoom);
    var i = players.length;

    while(i--) {
        players[i].style.webkitTransform =
            players[i].style.transform ='scale('+zoom/2+')';
    }


    div1.style.webkitTransform =
        div1.style.transform =
        ' translate(' + ((parentW/2)-(417/2)) + 'px, ' + (parentH/2-(362/2)) + 'px)'+'scale('+zoom1+')';
    div1.setAttribute('scale', zoom1);
    div1.setAttribute('data-x', parentW/2-(417/2));
    div1.setAttribute('data-y', parentH/2-(362/2));
    // $field.css('zoom', zoom);

}

/*
 * addListener('START', function(event){ var stringFromServer = event.data; var
 * arr = stringFromServer.split(','); playerMessage = arr[291];
 * document.getElementById("Player").innerHTML = playerMessage;
 */

$(window).on('load',function () {
    sendDataToServer("GETME");
    console.log( "window load" );
    showLobby();
    if (navigator.appVersion.indexOf("X11")!=-1)
    {
        $(".StreetRow:nth-child(8)").css({"left":"86px"});
        $(".StreetRow:nth-child(9)").css({"left":"58px"});
        $(".StreetRow:nth-child(10)").css({"left":"29px"});
        $(".StreetRow:nth-child(11)").css({"left":"0px"});
        $(".StreetRow:nth-child(12)").css({"left":"29px"});
        $(".StreetRow:nth-child(13)").css({"left":"57px"});	
        $(".StreetRow:nth-child(14)").css({"left":"86px"});
        $(".Street:nth-child(5n+5)").css({"margin-right":"-28.4px"});
    }
    setupChat();
    fitToWindow();
    setupEvent();
    // setup("53411505415322241344031670528157342698060510063002100101000151515242524333");
    updateCards(0,0,0,0,0);
    setDice(2,3);


});


function setPlayerColor(){

    var house =  $("#House");
    var settlement = $("#Settlement");
    var street = $("#Street");

    house.css({
        'background-image':'url("../images/'+playerId+'/1.png")'
    });   
    settlement.css({
        'background-image':'url("../images/'+playerId+'/2.png")'
    }); 
    street.css({
        'background-image':'url("../images/'+playerId+'/3.png")',
        'width':'15px',
        'height':'60px'
    }); 







}
function scaleUp(){
    var target = document.getElementById("field"),

        x = (parseFloat(target.getAttribute('data-x')) || 0),
        y = (parseFloat(target.getAttribute('data-y')) || 0);
    var s = (parseFloat(target.getAttribute('scale')) || 1);
    var $field = $("#field");
    var parentW = $field.parent().width(); // Get the height of whatever the
											// parent of square is
    var parentH = $field.parent().height(); 
    if(parentW<parentH){
        if(parentW<(s*417/2)){

        }else{
            s=s+0.1;
        }
    }else{
        if(parentH<(s*362/2)){

        }else{
            s=s+0.1;        
        }
    }
    console.log( s );
    target.style.webkitTransform =
        target.style.transform =
        ' translate(' + x + 'px, ' + y + 'px)'+'scale('+s+')';
    target.setAttribute('scale', s);
    target.style.transition = "none";
}




function scaleDown(){
    var target = document.getElementById("field"),

        x = (parseFloat(target.getAttribute('data-x')) || 0),
        y = (parseFloat(target.getAttribute('data-y')) || 0);
    var s = (parseFloat(target.getAttribute('scale')) || 1);
    if(s>0.5){
        s=s-0.1;    
    }
    console.log( s );
    target.style.webkitTransform =
        target.style.transform =
        ' translate(' + x + 'px, ' + y + 'px)'+'scale('+s+')';
    target.setAttribute('scale', s);
    target.style.transition = "none";
}



$(document).ready(function() {
    $("#field").bind('mousewheel DOMMouseScroll', function(event) {
        if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1) {
            if (event.originalEvent.detail > 0) {
                // scroll down
                scaleDown();
            } else {
                // scroll up
                scaleUp();
            }
        } else {
            if (event.originalEvent.wheelDelta < 0) {
                // scroll down
                scaleDown();
            } else {
                // scroll up
                scaleUp();
            }
        }
    });


    $('.tooltip').tooltipster({
        animation: 'grow',
        delay: [1500,50],
        theme: 'tooltipster-punk',
        maxWidth: 300
    });

    $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('disable');



    $('#trade').on( 'click', function() {
        startTrade();
    });
    $('#endTurn').on('click',function(){
        sendDataToServer("finish");
    });
    $('#buyProgress').on('click',function(){
        sendDataToServer("BUYEVENT");
    });
    $('#roadmaking').on('click',function(){
        sendDataToServer("PLAYEVENT0");
    });
    $('#monopol').on('click',function(){
        startSelect(1);
    });
    $('#invention').on('click',function(){
        startSelect(2);
    });
    $('#knight').on('click',function(){
        sendDataToServer("PLAYEVENT3");
    });
    $('#victory').on('click',function(){
        sendDataToServer("PLAYEVENT4");
    });

});


$(window).on('resize', function () {
    console.log( "resize" );
    fitToWindow();
});



// target elements with the "draggable" class
interact('#field')
    .draggable({
    // enable inertial throwing
    inertia: true,
    // keep the element within the area of it's parent
    restrict: {
        restriction: "parent",
        endOnly: true,
        // elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
        elementRect: { left: 0.5, right: 0.5, top: 0.5, bottom: 0.5 }
    },
    // enable autoScroll
    autoScroll: true,

    // call this function on every dragmove event
    onmove: dragMoveListener,
    // call this function on every dragend event
    onend: function (event) {

    }
}); 

function dragMoveListener (event) {
    var target = event.target,
        // keep the dragged position in the data-x/data-y attributes
        s = (parseFloat(target.getAttribute('scale')) || 1),
        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

    // translate the element
    target.style.webkitTransform =
        target.style.transform =
        'translate(' + x + 'px, ' + y + 'px)'+' scale('+s+')';
    // update the posiion attributes
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
}

// this is used later in the resizing and gesture demos
window.dragMoveListener = dragMoveListener;



function setup(string){
    for(var i=0; i<19;i++){
        setField(i,Number(string.charAt(i)),Number(string.charAt(i+19)));
    }
    for(var j=38; j<56;j++){
        placePortAt(j-38,Number(string.charAt(j)),Number(string.charAt(j+18)));
    }
}


function setField(id, type, chip){
    var img;
    switch (type){
        case 0:
            img='./images/dessert.png';
            break;
        case 1:
            img='./images/sheep.png';
            placeChipAt(id,chip);
            break;
        case 2:
            img='./images/clay.png';
            placeChipAt(id,chip);
            break;
        case 3:
            img='./images/ore.png';
            placeChipAt(id,chip);
            break;
        case 4:
            img='./images/wheat.png';
            placeChipAt(id,chip);
            break;
        case 5:
            img='./images/wood.png';
            placeChipAt(id,chip);
            break;
        default:
            console.log(type);
    }


    document.head.appendChild(document.createElement("style")).innerHTML = '#playField'+id+':before {background-image:url('+img+');}';
}

function placeThiefAt(id){
    $(".chip").html("");
    $("#chip"+id).html('<p style="margin-top: -6px; color:black; font-size: 24px;">&#x25cf</p>');
}

function placePortAt(id,type,rotation){
    var img;
    if(type<1||type>6){
    }else{
        switch (type){
            case 1:
                img='31';
                break;
            case 2:
                img='sheep';
                break;
            case 3:
                img='wood';
                break;
            case 4:
                img='wheat';
                break;
            case 5:
                img='clay';
                break;
            case 6:
                img='ore';
                break;
        }
        var target = document.getElementById("field");
        var scale = (parseFloat(target.getAttribute('scale')) || 1);
        var rot = 150+rotation*60;
        var field = $("#waterField"+id);
        var game = $("#field");
        var position = field.offset();
        var gamePosition = game.offset();

        var element = $('<div />',{'id': ("port"+id), 'class': "port"})
        .css({ 'left': Math.ceil((position.left+21*scale-gamePosition.left)/scale), 'top': ((position.top-gamePosition.top)/scale),
              'width':'58',
              'height':'58',
              'background-image':'url("../images/ports/'+img+'Port.png")',
              'background-size': '100%',
              'position': 'absolute',
              'transform':'rotate('+rot+'deg)',
              'z-index':'4'


             })
        ;  
        $(".portContainer").append(element); 
    }
}


function placeChipAt(id,value){
    var field = $("#playField"+id);
    var game = $("#field");
    var position = field.offset();
    var gamePosition = game.offset();
    var target = document.getElementById("field");
    var scale = (parseFloat(target.getAttribute('scale')) || 1);
    var element = $('<div />',{'id': ("chip"+id), 'class': "chip"})
    .css({ 'left': ((position.left+40*scale-gamePosition.left)/scale), 'top': ((position.top-gamePosition.top+18*scale)/scale),
          'width':'20',
          'height':'20',
          'background-image':'url("../images/chips/'+value+'.png")',
          'background-size': '100%',
          'position': 'absolute',
         })
    ;  
    $(".portContainer").append(element); 
}



function setupScore(id){
    var field = $("#player"+id);
    var element = $('<div />',{'id': ("playerName"+id), 'class': "count" })
    .css({
        'top':"7px",
        'text-align':'center'
    })
    .html(spieler[id]);
    field.append(element);

    element = $('<div />',{'id': ("cardCount"+id), 'class': "count" })
        .css({
        'top':"23px"
    })
        .html(toHTML(0));
    field.append(element);

    element = $('<div />',{'id': ("progressCount"+id), 'class': "count" })
        .css({
        'top':"36px"
    })
        .html(toHTML(0));
    field.append(element);

    element = $('<div />',{'id': ("victoryCount"+id), 'class': "count" })
        .css({
        'top':"49px"
    })
        .html(toHTML(0));
    field.append(element);

    element = $('<div />',{'id': ("knightCount"+id), 'class': "count" })
        .css({
        'top':"62px"
    })
        .html(toHTML(0));
    field.append(element);

    element = $('<div />',{'id': ("streetCount"+id), 'class': "count" })
        .css({
        'top':"75px"
    })
        .html(toHTML(0));
    field.append(element);

}

function updateScore(id,cardCount,progressCount,victoryCount,knightCount,streetCount,longestStreet,knightForce){
    $("#cardCount"+id).html(toHTML(cardCount));
    $("#progressCount"+id).html(toHTML(progressCount));
    $("#victoryCount"+id).html(toHTML(victoryCount));
    $("#knightCount"+id).html(toHTML(knightCount));
    $("#streetCount"+id).html(toHTML(streetCount));
    
    if(id==longestStreet){
            $("#streetCount"+id).css({
        "background-color":"beige"
        });
    }else{
        $("#streetCount"+id).css({
            "background-color":"transparent"
            }); 
    }
    if(id==knightForce){
        $("#knightCount"+id).css({
            "background-color":"beige"
            });
    }else{
        $("knightCount"+id).css({
            "background-color":"transparent"
            }); 
    }
}


function updateCards(wheat,wood,ore,sheep,clay){
    $("#wheat").html(toHTML(wheat));
    $("#wood").html(toHTML(wood));
    $("#ore").html(toHTML(ore));
    $("#sheep").html(toHTML(sheep));
    $("#clay").html(toHTML(clay));
    cards[0]=wheat;
    cards[1]=wood;
    cards[2]=ore;
    cards[3]=sheep;
    cards[4]=clay;
}

function setupEvent(){
    updateEvent(0,0,0,0,0);
}

function updateEvent(roadmaking,monopol,invention,knight,victory){
    $("#roadmaking").html(toHTML(roadmaking));
    $("#monopol").html(toHTML(monopol));
    $("#invention").html(toHTML(invention));
    $("#knight").html(toHTML(knight));
    $("#victory").html(toHTML(victory));
    eventCards[0]=roadmaking;
    eventCards[1]=monopol;
    eventCards[2]=invention;
    eventCards[3]=knight;
    eventCards[4]=victory;
}

function setStreet(id,player){

    if(player<4){
        $("#street"+id).css({
            'background-image':'url("../images/'+player+'/3.png")'
        }); 
    }else{
        $("#street"+id).css({
            'background-image':'none'
        });
    }

}

function setIntersection(id,player,type){  

    if(player<4){
        var element = $('<div />',{'id': ("settlement"+id), 'class': "playerSettlement_Intersection"})
        .css({ 
            'background-image':'url("../images/'+player+'/'+type+'.png")'  
        })
        ;  
        $("#intersection"+id).html(element); 
    }else{

    }
}



function toHTML(int){

    switch(int){
        case 0:
            return "&#127244";
        case 1:
            return "&#10122";
        case 2:
            return "&#10123";
        case 3:
            return "&#10124";
        case 4:
            return "&#10125";
        case 5:
            return "&#10126";
        case 6:
            return "&#10127";
        case 7:
            return "&#10128";
        case 8:
            return "&#10129";
        case 9:
            return "&#10130";
        case 10:
            return "&#10131";
        case 11:
            return "&#9451";
        case 12:
            return "&#9452";
        case 13:
            return "&#9453";
        case 14:
            return "&#9454";
        case 15:
            return "&#9455";
        case 16:
            return "&#9456";
        case 17:
            return "&#9457";
        case 18:
            return "&#9458";
        case 19:
            return "&#9459";
        case 20:
            return "&#9460";
    }
    return int;
}








function startTrade(){
    $('#trade').off('click');   
    $('#endTurn').off('click');
    $('#trade').on( "click", function() {
        endTrade(false);  
    });
    $('#endTurn').on( "click", function() {
        endTrade(true);  
    });


    // SET STYLE TRADING


    $('#trade').tooltipster('content', 'Klicke mich um den Handel abzubrechen.');
    $('#endTurn').tooltipster('content', 'Klicke mich um den Handel abzuschliessen.');
    $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('enable');



    $("#endTurn").css({
        'background-image': 'url("../images/card/accept.png"'
    });
    $("#trade").css({
        'background-image': 'url("../images/card/decline.png"'
    });
    $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
        '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
        '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
        'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
    });  


    // add listeners for materials
    $("#wheat, #wood, #ore, #sheep, #clay").on("click", function(){

        var temp;
        switch($(this).attr('id')){
            case "wheat":
                temp=0;
                break;
            case "wood":
                temp=1;
                break;
            case "ore":
                temp=2;
                break;
            case "sheep":
                temp=3;
                break;
            case "clay":
                temp=4;
                break;
        }
        trade[temp]+=1;

        if(trade[temp]<0){
            // ZAHL UNTER 0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(229, 38, 32,1)'
            });  
            $(this).html(toHTML(cards[temp])+" <span style='color:rgb(229, 38, 32)'>"+toHTML(Math.abs(trade[temp]))+"</span>");
        }else if(trade[temp]==0){
            // ZAHL==0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
            });
            $(this).html(toHTML(cards[temp]));
        }else{
            // ZAHL GROESSER 0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(59, 170, 53,1)'
            });
            $(this).html(toHTML(cards[temp])+" <span style='color:rgb(59, 170, 53)'>"+toHTML(trade[temp])+"</span>");
        }
    });
    $("#wheat, #wood, #ore, #sheep, #clay").on("contextmenu", function(e){
        e.preventDefault();
        var temp;
        switch($(this).attr('id')){
            case "wheat":
                temp=0;
                break;
            case "wood":
                temp=1;
                break;
            case "ore":
                temp=2;
                break;
            case "sheep":
                temp=3;
                break;
            case "clay":
                temp=4;
                break;
        }
        if(trade[temp]>0||cards[temp]>Math.abs(trade[temp])){
            trade[temp]-=1;
        }
        if(trade[temp]<0){
            // ZAHL UNTER 0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(229, 38, 32,1)'
            });  
            $(this).html(toHTML(cards[temp])+" <span style='color:rgb(229, 38, 32)'>"+toHTML(Math.abs(trade[temp]))+"</span>");
        }else if(trade[temp]==0){
            // ZAHL==0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
            });
            $(this).html(toHTML(cards[temp]));
        }else{
            // ZAHL GROESSER 0
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(59, 170, 53,1)'
            });
            $(this).html(toHTML(cards[temp])+" <span style='color:rgb(59, 170, 53)'>"+toHTML(trade[temp])+"</span>");
        }

    });


}

function endTrade(accept){
    if(accept){
        sendDataToServer("TRADE,"+trade[0]+","+trade[1]+","+trade[2]+","+trade[3]+","+trade[4]);
    }
    
    $('#trade').off('click');
    $('#endTurn').off('click');

    $("#wheat, #wood, #ore, #sheep, #clay").off('click');
    $("#wheat, #wood, #ore, #sheep, #clay").off('contextmenu');

    $("#wheat").html(toHTML(cards[0]));
    $("#wood").html(toHTML(cards[1]));
    $("#ore").html(toHTML(cards[2]));
    $("#sheep").html(toHTML(cards[3]));
    $("#clay").html(toHTML(cards[4]));


    $('#trade').tooltipster('content', 'Klicke mich um einen Handel zu Starten.');
    $('#endTurn').tooltipster('content', 'Klicke mich um deinen Zug zu beenden.');
    $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('disable');


    // SET STYLE BACK TO NORMAL
    $("#endTurn").css({
        'background-image': 'url("../images/card/endTurn.png"'
    });
    $("#trade").css({
        'background-image': 'url("../images/card/tradeCard.png"'
    });
    $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
        '-webkit-box-shadow': 'none',
        '-moz-box-shadow': 'none',
        'box-shadow': 'none'
    }); 
    // RESET TRADE ARRAY
    for(var i=0; i<5; i++){
        trade[i]=0;
    }   

    $('#endTurn').on('click',function(){
        sendDataToServer("finish");
    });
    $('#trade').on( "click", function() {
        startTrade();  
    });
}



function reciveTrade(trade){
    $('#trade').off('click');   
    $('#endTurn').off('click');
    $('#trade').on( "click", function() {
        acceptTrade(false);
    });
    $('#endTurn').on( "click", function() {
        acceptTrade(true);
    });
    

 
    $(".popup").html("<p class='popupMessage'>"+trade.split(",")[5]+" hat dir einen Handel vorgeschlagen!</p>");
       
    $(".popup").bPopup({
    speed: 650,
    transition: 'fadeIn',
    transitionClose: 'slideDown',
    autoClose: 500,
    position:['auto',100]
    });


    var temp = trade.split(",").map(Number);

    // alert(temp);
    // SET STYLE TRADING


    $('#trade').tooltipster('content', 'Klicke mich um den Handel abzulehnen.');
    $('#endTurn').tooltipster('content', 'Klicke mich um den Handel anzunehmen.');


    $("#endTurn").css({
        'background-image': 'url("../images/card/accept.png"'
    });
    $("#trade").css({
        'background-image': 'url("../images/card/decline.png"'
    });
    
    
    $("#trade,#endTurn").css({
        '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
        '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
        'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
    });  
   temp[5]=0;
    for(var i=0;i<5;i++){
    	temp[i]=temp[i]*(-1);
        var type;
        switch(i){
            case 0:
                type="wheat";
                break;
            case 1:
                type="wood";
                break;
            case 2:
                type="ore";
                break;
            case 3:
                type="sheep";
                break;
            case 4:
                type="clay";
                break;
        }
        
        if(temp[i]<0){
            // ZAHL UNTER 0
            $('#'+type).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(229, 38, 32,1)'
            });  
        $('#'+type).html(toHTML(cards[i])+" <span style='color:rgb(229, 38, 32)'>"+toHTML(Math.abs(temp[i]))+"</span>");
        }else if(temp[i]>0){
            // ZAHL GROESSER 0
            $('#'+type).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(59, 170, 53,1)'
            });
            $('#'+type).html(toHTML(cards[i])+" <span style='color:rgb(59, 170, 53)'>"+toHTML(temp[i])+"</span>");
        }
    }
    
}


function acceptTrade(accept){
    $('#trade').off('click');
    $('#endTurn').off('click');


    $("#wheat").html(toHTML(cards[0]));
    $("#wood").html(toHTML(cards[1]));
    $("#ore").html(toHTML(cards[2]));
    $("#sheep").html(toHTML(cards[3]));
    $("#clay").html(toHTML(cards[4]));


    $('#trade').tooltipster('content', 'Klicke mich um einen Handel zu Starten.');
    $('#endTurn').tooltipster('content', 'Klicke mich um deinen Zug zu beenden.');


    // SET STYLE BACK TO NORMAL
    $("#endTurn").css({
        'background-image': 'url("../images/card/endTurn.png"'
    });
    $("#trade").css({
        'background-image': 'url("../images/card/tradeCard.png"'
    });
    $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
        '-webkit-box-shadow': 'none',
        '-moz-box-shadow': 'none',
        'box-shadow': 'none'
    }); 
  
    $('#endTurn').on('click',function(){
        sendDataToServer("finish");
    });
    $('#trade').on( "click", function() {
        startTrade();  
    });
    if(accept){
        sendDataToServer("ACCEPTTRADE");
    }else{
        sendDataToServer("REJECTTRADE");
    }
}


function selectTrade(){
    var temp=[1,2];
    var victim;
    var container = $("<div></div>").css({
        "width": "250px",
        "height": "150px",
        "display": "flex",
        "flex-wrap": "wrap",
        "text-align": "center"
    });
    var selectText = $("<p>SPLIELER und SPIELER wollen deinen Handel annehmen. Waehle einen von ihnen aus!</p>").css({
        "width": "300px"
    });
    container.append(selectText);
    for(var i=1;i<temp.length;i++){
        var color;
        switch(temp[i]){
            case 0:
                color="#ffcc00"
                break;
            case 1:
                color="green"
                break;
            case 2:
                color="#3385ff"
                break;
            case 3:
                color="red"
                break;
        }
        victim=  $('<div />',{'id': ("selectTrade"+i)}).css({
            "width": "50px",
            "height": "50px",
            "border-radius":"50%",
            "background": color,
            "margin":"auto"
        });
        victim.on('click', function(){
            // endThief($(this).attr('id').replace("selectTrade",""),temp[0]);
            $("[id^='selectTrade']").off('click');
            $("#chat").tooltipster('close');
        });         
        container.append(victim);
    }
    $("#chat").tooltipster({
        animation: 'grow',
        trackOrigin: 'true',
        theme: 'tooltipster-punk',
        maxWidth: 300,
        interactive :'true',
        trigger: 'custom',
        triggerOpen: {
        },
        triggerClose: {
        }
    })
        .tooltipster('content', container)
        .tooltipster('open');
    
}















function startSelect(type){
    if(eventCards[type]>0){
        $('#trade').off('click');   
        $('#endTurn').off('click');
        $('#trade').on( "click", function() {
            endSelect(type,false);  
        });
        $('#endTurn').on( "click", function() {
            endSelect(type,true);  
        });



        $('#trade').tooltipster('content', 'Klicke mich, falls du es dir anders ueberlegt hast.');
        $('#endTurn').tooltipster('content', 'Klicke mich um die Karte zu spielen.');

        $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('content', 'Linksklicken um den Rohstoff auszuwaehlen.');
        $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('enable');

        // SET STYLE TRADING
        $("#endTurn").css({
            'background-image': 'url("../images/card/accept.png"'
        });
        $("#trade").css({
            'background-image': 'url("../images/card/decline.png"'
        });
        $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
            '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
            '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
            'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
        });  


        // add listeners for materials
        $("#wheat, #wood, #ore, #sheep, #clay").on("click", function(){

            switch($(this).attr('id')){
                case "wheat":
                    select=0;
                    break;
                case "wood":
                    select=1;
                    break;
                case "ore":
                    select=2;
                    break;
                case "sheep":
                    select=3;
                    break;
                case "clay":
                    select=4;
                    break;
            }        
            $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)'
            }); 
            $(this).css({
                '-webkit-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                '-moz-box-shadow': 'inset 0px 0px 10px 0px rgba(0,255,255,1)',
                'box-shadow': 'inset 0px 0px 10px 0px rgb(253, 95, 0)' 
            });
        });
    }
}


function endSelect(type,accept){
    $('#trade').off('click');
    $('#endTurn').off('click');

    $("#wheat, #wood, #ore, #sheep, #clay").off('click');

    $('#trade').tooltipster('content', 'Klicke mich um einen Handel zu Starten.');
    $('#endTurn').tooltipster('content', 'Klicke mich um deinen Zug zu beenden.');
    $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('disable');
    $("#wheat, #wood, #ore, #sheep, #clay").tooltipster('content', 'Linksklick: Rohstoff anfordern Rechtsklick: Rohstoff anbieten');

    // SET STYLE BACK TO NORMAL
    $("#endTurn").css({
        'background-image': 'url("../images/card/endTurn.png"'
    });
    $("#trade").css({
        'background-image': 'url("../images/card/tradeCard.png"'
    });
    $("#wheat, #wood, #ore, #sheep, #clay, #trade,#endTurn").css({
        '-webkit-box-shadow': 'none',
        '-moz-box-shadow': 'none',
        'box-shadow': 'none'
    }); 
    if(accept&&(select>-1)){
        sendDataToServer('PLAYEVENT'+type+','+select);
    }

    $('#endTurn').on('click',function(){
        sendDataToServer("finish");
    });
    $('#trade').on( "click", function() {
        startTrade();  
    });
    select=-1;

}




function showLobby(){
    var name = $("#login").text().split("|");
    var namesplit = name[0].substring(6,name[0].length-2);
    console.log(namesplit);
    $("#Game").append("<div class='lobbyShield'></div>");
    $(".lobbyShield").append("<div class='lobby'></div>");
    $(".lobby").append('<div id="headline"></div>');
    $(".lobby").append('<table class="tg"><tbody id="tableBody"><tr><th class="tg-s6z2">Spieler</th><th class="tg-baqh">Spielername</th><th class="tg-baqh">Status</th></tr></tbody></table>');


    $(".lobby").append('<a class="lobbyButton" id="button0">Spiel Starten!</a>');
    $(".lobby").append('<a class="lobbyButton" id="button1">Add Bot!</a>');

    $("#button0").on('click',function(){
        sendDataToServer("SETUP");
    });
    $("#button1").on('click',function(){
        sendDataToServer("ADDAI");
    });
    sendDataToServer("GETUSER");

}

function deleteLobby(){
    $(".lobbyShield").fadeOut(2000, function() { $(this).remove(); });
}

function setupChat(){
    $(".playerInfo").append("<div id='chat'></div>");
    $(".playerInfo").append("<input id='chatInput'></input>");
    $("#chatInput").keypress (function (e) {
        if (e.which == 13) {
            sendDataToServer("CHAT,"+$("#chatInput").val());
            console.log("CHAT,"+$("#chatInput").val());
            $("#chatInput").val("");
            return false;    
        }
    });
}




function startThief(temp){
    $(document).off('click','.Street');
    $(document).off('click','.Intersection');
    // alert("MOVETHIEF");
    
    
    if(1 == parseInt(temp)){
		$(".popup").html("<p class='popupMessage'>Du hast eine Ritterkarte gespielt! Plaziere den Dieb auf einer Marke.</p>");
        $(".popup").bPopup({
            speed: 650,
            transition: 'fadeIn',
            transitionClose: 'slideDown',
            position:['auto',100]
        });
    }else{
        $(".popup").html("<p class='popupMessage'>Du hast eine 7 gewuerfelt! Plaziere den Dieb auf einer Marke.</p>");
        $(".popup").bPopup({
            speed: 650,
            transition: 'fadeIn',
            transitionClose: 'slideDown',
            position:['auto',100]
        });
    }
    
    $(".chip").on('click', function(){
        // alert("click on chip");
        $(".chip").off('click');
        sendDataToServer('SETTHIEF,'+$(this).attr('id').replace('chip',''));    
        $(document).on('click','.Street',function (event) {
            clickStreat(event);
        });
        $(document).on('click','.Intersection',function (event) {
            clickedIntersection = $(this);
            clickIntersection(event);    
        });
    });
}

function selectVictim(str){
    $(document).off('click','.Street');
    $(document).off('click','.Intersection');
    console.log(str);
    var victim = new Array();
    var temp=str.split(",").map(Number);
    //
    var container = $("<div></div>").css({
        "width": "200px",
        "height": "100px",
        "display": "flex",
        "flex-wrap": "wrap",
        "text-align": "center"
    });
    var selectText = $("<p>Wen willst du bestehlen?</p>").css({
        "width": "300px"
    });
    container.append(selectText);
    for(var i=1;i<temp.length;i++){
        var color;
        switch(temp[i]){
            case 0:
                color="#ffcc00";
                break;
            case 1:
                color="green";
                break;
            case 2:
                color="#3385ff";
                break;
            case 3:
                color="red";
                break;
        }
        victim[i]=  $('<div />',{'id': ("selectThief"+temp[i])}).css({
            "width": "50px",
            "height": "50px",
            "border-radius":"50%",
            "background": color,
            "margin":"auto"
        });
        victim[i].on('click', function(){
            endThief($(this).attr('id').replace("selectThief",""),temp[0]);
        });         
        container.append(victim[i]);
    }
    $("#playField"+temp[0]).tooltipster({
        animation: 'grow',
        trackOrigin: 'true',
        theme: 'tooltipster-punk',
        maxWidth: 300,
        interactive :'true',
        trigger: 'custom',
        triggerOpen: {
        },
        triggerClose: {
        }
    })
        .tooltipster('content', container)
        .tooltipster('open');
}


function endThief(id,field){
    $("[id^='selectThief']").off('click');
    $("#playField"+field).tooltipster('close');
    sendDataToServer("STEALFROM,"+id);

    $(document).on('click','.Street',function (event) {
        clickStreat(event);
    });
    $(document).on('click','.Intersection',function (event) {
        clickedIntersection = $(this);
        clickIntersection(event);    
    });
}






/* Circle-Menue */

var menueIsOpen = false;
var firstTimeMenueOpened = false;
var clickedIntersection; 
var clickedStreet; 


function clickStreat(event){
	var setStreetBackground = event.currentTarget.style;
    if(event.currentTarget.id == "waterStreet" || event.currentTarget.id == "" || setStreetBackground.backgroundImage != "none" ){
        return
    }
    clickedStreet = event.currentTarget;

    $("#field").pageX
    var x = event.pageX;
    var y = event.pageY;

    var items = document.querySelectorAll('.circle a');
    if (window.CP.shouldStopExecution(1)) {
        return;
    }

    items[0].style.visibility = "hidden";
    items[1].style.visibility = "hidden";
    items[2].style.visibility = "visible";
    items[2].style.left = (50 - 35 * Math.cos(-0.5 * Math.PI - 2 * (1 / 1) * 0 * Math.PI)).toFixed(4) + '%';

    window.CP.exitedLoop(1);
    var elementStyle = document.getElementById("circle_menue_intersection").style;

    setPlayerColor();

    elementStyle.left = x - 10 + "px";
    elementStyle.top = y - 170 +"px";
    elementStyle.position = "absolute";



    if(menueIsOpen == false) {

        closeMenu(false);
        menueIsOpen = true; }
}
function clickIntersection(event){
	var whichPlayerColor;
	if(event.currentTarget.firstChild != null)
		whichPlayerColor = event.currentTarget.firstChild.style.backgroundImage;
	
    if(event.currentTarget.id == "" ){
        return
    }
    if(whichPlayerColor != null){
    	if(!whichPlayerColor.includes('/images/' + playerId)){
    		return
    	}
    }
   
    // clickedIntersection = $(this);
    var x = event.pageX;
    var y = event.pageY;
    var items = document.querySelectorAll('.circle a');
    if (window.CP.shouldStopExecution(1)) {
        return;

    }

    if(clickedIntersection[0].childNodes.length > 0){
        items[0].style.visibility = "hidden";
        items[1].style.visibility = "visible";
        items[1].style.top = (50 - 35 * Math.cos(-0.5 * Math.PI - 2 * (1 / 2) * 0 * Math.PI)).toFixed(4) + '%';
    }
    else{
        items[1].style.visibility = "hidden";
        items[0].style.visibility = "visible";
        items[0].style.left = (50 - 35 * Math.cos(-0.5 * Math.PI - 2 * (1 / 2) * 0 * Math.PI)).toFixed(4) + '%';

    }
    items[2].style.visibility = "hidden";

    window.CP.exitedLoop(1);
    var elementStyle = document.getElementById("circle_menue_intersection").style;

    setPlayerColor();
    elementStyle.left = x - 10 + "px";
    elementStyle.top = y - 170 +"px";
    elementStyle.position = "absolute";



    if(menueIsOpen == false) {
        closeMenu(false);
        menueIsOpen = true; 

    }
}

$(document).on('click','.Street',function (event) {

    clickStreat(event);
});


$(document).on('click','.Intersection',function (event) {
    clickedIntersection = $(this);
    clickIntersection(event);    
});




function buildStreat(){
    if(clickedStreet != null) {
        var Streetid = clickedStreet.id;
        var id = Streetid.replace("street", "");
        sendDataToServer("buildRoad," + id);
        // clickedStreet.style.background = "blue";
        clickedStreet = null;
    }
}

function buildSettlement(){
    if(clickedIntersection != null){
        var Intersectionid = clickedIntersection[0].id;
        var id = Intersectionid.replace("intersection", "");
        sendDataToServer("buildCity," + id);
        // clickedIntersection.html("<div class='playerSettlement_Intersection'
		// style='position: absolute; top: -3px;'></div>");
        clickedIntersection = null;

    }
}

function buildHouse(){
    if(clickedIntersection != null){
        var Intersectionid = clickedIntersection[0].id;
        var id = Intersectionid.replace("intersection", "");
        sendDataToServer("buildSettlement," + id);
        // clickedIntersection.html("<div class='playerHouse_Intersection'
		// style='position: absolute; top: -1px;'></div>");
        clickedIntersection = null;
    }
}

$( document ).ready(function() {

    $("#Settlement").on('click',function(){
        buildSettlement();
    });

    $("#Street").on('click',function(){
        buildStreat();
    });

    $("#House").on('click',function(){
        buildHouse();
    });

    $(document).click(function(event) {
        if(event.which == 1){
            if(menueIsOpen == true && firstTimeMenueOpened == true) {
                closeMenu(true);
                menueIsOpen = false;
                firstTimeMenueOpened = false;
            }
            else if (menueIsOpen == true){
                firstTimeMenueOpened = true;
            }


        }
    });

});

function closeMenu(close){
    var elementStyle = document.getElementById("circle_menue_intersection").style;
    var doc = document.querySelector('.circle');
    if(close == true){
        doc.classList.toggle('open'); 
        elementStyle.visibility = "hidden";}
    else{
        doc.classList.toggle('open'); 
        elementStyle.visibility = "visible";}
}

"use strict";
"object" != typeof window.CP && (window.CP = {}), window.CP.PenTimer = {
    programNoLongerBeingMonitored: !1,
    timeOfFirstCallToShouldStopLoop: 0,
    _loopExits: {},
    _loopTimers: {},
    START_MONITORING_AFTER: 2e3,
    STOP_ALL_MONITORING_TIMEOUT: 5e3,
    MAX_TIME_IN_LOOP_WO_EXIT: 2200,
    exitedLoop: function(o) {
        this._loopExits[o] = !0
    },
    shouldStopLoop: function(o) {
        if (this.programKilledSoStopMonitoring)
            return !0;
        if (this.programNoLongerBeingMonitored)
            return !1;
        if (this._loopExits[o])
            return !1;
        var t = this._getTime();
        if (0 === this.timeOfFirstCallToShouldStopLoop)
            return this.timeOfFirstCallToShouldStopLoop = t, !1;
        var i = t - this.timeOfFirstCallToShouldStopLoop;
        if (i < this.START_MONITORING_AFTER)
            return !1;
        if (i > this.STOP_ALL_MONITORING_TIMEOUT)
            return this.programNoLongerBeingMonitored = !0, !1;
        try {
            this._checkOnInfiniteLoop(o, t)
        } catch (e) {
            return this._sendErrorMessageToEditor(), this.programKilledSoStopMonitoring = !0, !0
        }
        return !1
    },
    _sendErrorMessageToEditor: function() {
        try {
            if (this._shouldPostMessage()) {
                var o = {
                    action: "infinite-loop",
                    line: this._findAroundLineNumber()
                };
                parent.postMessage(JSON.stringify(o), "*")
            } else
                this._throwAnErrorToStopPen()
                } catch (t) {
                    this._throwAnErrorToStopPen()
                }
    },
    _shouldPostMessage: function() {
        return document.location.href.match(/boomerang/)
    },
    _throwAnErrorToStopPen: function() {
        throw "We found an infinite loop in your Pen. We've stopped the Pen from running. Please correct it or contact support@codepen.io."
    },
    _findAroundLineNumber: function() {
        var o = new Error,
            t = 0;
        if (o.stack) {
            var i = o.stack.match(/boomerang\S+:(\d+):\d+/);
            i && (t = i[1])
        }
        return t
    },
    _checkOnInfiniteLoop: function(o, t) {
        if (!this._loopTimers[o])
            return this._loopTimers[o] = t, !1;
        var i = t - this._loopTimers[o];
        if (i > this.MAX_TIME_IN_LOOP_WO_EXIT)
            throw "Infinite Loop found on loop: " + o
            },
    _getTime: function() {
        return +new Date
    }
}, window.CP.shouldStopExecution = function(o) {
    var t = window.CP.PenTimer.shouldStopLoop(o);
    return t === !0 && console.warn("[CodePen]: An infinite loop (or a loop taking too long) was detected, so we stopped its execution. Sorry!"), t
}, window.CP.exitedLoop = function(o) {
    window.CP.PenTimer.exitedLoop(o)
};

// Listener
addListener('SETUP', function(event){
    deleteLobby();
    var stringFromServer = event.data;
    console.log(stringFromServer);
    setup(stringFromServer);
    setupScore(0);
    setupScore(1);
    setupScore(2);
    setupScore(3);
});
addListener('UPDATERESOURCE',function(event){
    var stringFromServer = event.data;
    var temp=stringFromServer.split(",").map(Number);
    updateCards(temp[0],temp[1],temp[2],temp[3],temp[4]);
    for(var i=5;i<9;i++){
        updateScore(i-5,temp[i],temp[i+4],temp[i+8],temp[i+12],temp[i+16],temp[25],temp[26]);
        console.log(temp);
    }
    

});


addListener('UPDATEDICE',function (event) {
    var stringFromServer = event.data;
    var dice = stringFromServer.split("");
    setDice(dice[0],dice[1]);

});

function scrollToBottom() {
    var height = 0;
    $('#chat p').each(function(i, value){
        height += parseInt($(this).height());
    });

    height += '';

    $('#chat').animate({scrollTop: height});
}
addListener('CHAT',function (event) {
    var stringFromServer = event.data;
    var message=stringFromServer.substring(1,stringFromServer.length);
    var id=parseInt(stringFromServer.substring(0,1));
    var color;
    switch(id){
        case 0:
            color="#ffcc00"
            break;
        case 1:
            color="green"
            break;
        case 2:
            color="#3385ff"
            break;
        case 3:
            color="red"
            break;
        case 4:
            color="#FD5F00"
            break;
    }
    var text;
    message=message.replace(/Kappa/g,"<img src='images/emotes/Kappa.png' height='30' />");
    message=message.replace(/BabyRage/g,"<img src='images/emotes/BabyRage.png' height='30' />");
    message=message.replace(/BibleThumb/g,"<img src='images/emotes/BibleThumb.png' height='30' />");
    message=message.replace(/DansGame/g,"<img src='images/emotes/DansGame.png' height='30' />");
    message=message.replace(/duDudu/g,"<img src='images/emotes/duDudu.png' height='30' />");
    message=message.replace(/FrankerZ/g,"<img src='images/emotes/FrankerZ.png' height='30' />");
    message=message.replace(/HeyGuys/g,"<img src='images/emotes/HeyGuys.png' height='30' />");
    message=message.replace(/Kreygasm/g,"<img src='images/emotes/Kreygasm.png' height='30' />");
    message=message.replace(/NotLikeThis/g,"<img src='images/emotes/NotLikeThis.png' height='30' />");
    message=message.replace(/PJSalt/g,"<img src='images/emotes/PJSalt.png' height='30' />");
    message=message.replace(/BILDKOSTEN/g,"<img src='images/baukosten.png' height='200' />");

    if(id==playerId){
        text=$("<p align='right'>" + message +"</p>");
    }
    else{
        text=$("<p align='left'>" + message +"</p>");
    }
    text.css({
        'background-color':color
    });
    $("#chat").append(text);
    
// if(id==4){
// $("#gelberBalken").html(message);
// }
    scrollToBottom();
    // $("#chat").append("<br>");


});


addListener('STATUS',function (event) {
    var stringFromServer = event.data;
    
    $("#gelberBalken").html(stringFromServer);

});


addListener('ME', function(event){
    var stringFromServer = event.data;
    playerId = stringFromServer;
    switch(playerId){
    case "0":
        color="#ffcc00"
        break;
    case "1":
        color="green"
        break;
    case "2":
        color="#3385ff"
        break;
    case "3":
        color="red"
        break;
}
    $("#console").css({
        'background-color':color
    });
});





addListener('MOVETHIEF', function(event){
    var stringFromServer = event.data;
    startThief(stringFromServer);
});

addListener('SELECTVICTIM', function(event){
    var stringFromServer = event.data;
   // alert("SELECTVICTIM");
    selectVictim(stringFromServer);
});


addListener('THIEFMOVED', function(event){
    var stringFromServer = event.data;
    var temp = stringFromServer.split(",");
    placeThiefAt(temp[0]);
    if(temp[1]===temp[2]){
        $(".popup").html("<p class='popupMessage'>"+temp[1]+" hat den Raeuber verschoben und niemanden beraubt!</p>");
    }else{
        $(".popup").html("<p class='popupMessage'>"+temp[1]+" hat den Raeuber verschoben und "+temp[2]+" beraubt!</p>");
    }
    $(".popup").bPopup({
        speed: 650,
        transition: 'fadeIn',
        transitionClose: 'slideDown',
        autoClose: 500,
        position:['auto',100]
    });
});



addListener('SELECTTRADE', function(event){
    var stringFromServer = event.data;
    selectTrade(stringFromServer);
});


addListener('TRADECANCELLED', function(event){
    acceptTrade(false);
});




addListener('TRADE', function(event){
    var stringFromServer = event.data;
    // alert("recieve trade");
    reciveTrade(stringFromServer);
});





addListener('WHOSETURN', function(event){
    var stringFromServer = event.data;
    var color;
    var id=parseInt(stringFromServer);
    switch(id){
        case 0:
            color="#ffcc00"
            break;
        case 1:
            color="green"
            break;
        case 2:
            color="#3385ff"
            break;
        case 3:
            color="red"
            break;
    }
    $(".popup").css({
        'color':color
    });

    if(playerId==id){
        $(".popup").html("<p class='popupMessage'>Du bist dran!</p>");
    }else{
        $(".popup").html("<p class='popupMessage'>"+spieler[id]+" ist an der Reihe!</p>");
    }
    $(".popup").bPopup({
        speed: 650,
        transition: 'fadeIn',
        transitionClose: 'slideDown',
        autoClose: 500,
        position:['auto',100]
    });

});

addListener('USERJOINED',function (event) {
    var stringFromServer = event.data;
    // username*4
    var temp=stringFromServer.split(",");
    // alert(stringFromServer);
    var table='<tr><th class="tg-s6z2">Spielernummer</th><th class="tg-baqh">Name</th><th class="tg-baqh">Status</th></tr>';
    for(var i=0; i<temp.length;i++){
        spieler[i]=temp[i];
        table+=('<tr><th class="tg-s6z2">'+(i+1)+'</th><th class="tg-baqh">'+temp[i]+'</th><th class="tg-baqh">beigetreten</th></tr>');
    }
    $("#tableBody").html(table); 

});


addListener('UPDATEFIELD', function(event){
    var stringFromServer = event.data;
    for(var i = 0; i < 54;i++){

        // intersection 0=leer 1=siedlung, 2=stadt
        // i+54 owner
        setIntersection(i,stringFromServer.charAt(i+54),stringFromServer.charAt(i));
    }
    for(var i=108;i<180;i++){
        setStreet(i-108,stringFromServer.charAt(i));
    }
    console.log(stringFromServer);
})



addListener('UPDATEEVENT', function(event){
    var stringFromServer = event.data;
    var temp = stringFromServer.split(",").map(Number);
    updateEvent(temp[0],temp[1],temp[2],temp[3],temp[4]);
    // TODO REPLACE 0 WITH temp[5] ONCE IT GETS IMPLEMENTED
    temp = stringFromServer.split(",");
    switch(parseInt(temp[5])){
        case 0:
            $("#roadmaking").tooltipster('content', 'Du hast eine Strassenbaukarte gezogen.')
                .tooltipster('open')
            setTimeout(function(){
                $("#roadmaking").tooltipster('close');
                setTimeout(function(){
                    $("#roadmaking").tooltipster('content', 'Klicke mich, wenn du eine Strassenbaukarte besitzt, um zwei kostenlose Strassen zu platzieren.');
                }, 500);   
            }, 1000);
            break;
        case 1:
            $("#monopol").tooltipster('content', 'Du hast eine Monopolkarte gezogen.')
                .tooltipster('open')
            setTimeout(function(){
                $("#monopol").tooltipster('close');
                setTimeout(function(){
                    $("#monopol").tooltipster('content', 'Klicke mich, wenn du eine Monopolkarte besitzt, um die ausgewaehlte Ressource von allen Spielern zu erhalten.');
                }, 500);   
            }, 1000);
            break;
        case 2:
            $("#invention").tooltipster('content', 'Du hast eine Erfindungskarte gezogen.')
                .tooltipster('open')
            setTimeout(function(){
                $("#invention").tooltipster('close');
                setTimeout(function(){
                    $("#invention").tooltipster('content', 'Klicke mich, wenn du eine Erfindungskarte besitzt, um 2 Rohstoffe des ausgewaehlten Typs von der Bank zu erhalten.');
                }, 500);   
            }, 1000);
            break;
        case 3:
            $("#knight").tooltipster('content', 'Du hast eine Ritterkarte gezogen.')
                .tooltipster('open')
            setTimeout(function(){
                $("#knight").tooltipster('close');
                setTimeout(function(){
                    $("#knight").tooltipster('content', 'Klicke mich, wenn du eine Ritterkarte besitzt, um einen Ritter auszuspielen.');
                }, 500);   
            }, 1000);
            break;
        case 4:
            $("#victory").tooltipster('content', 'Du hast eine Siegpunktkarte gezogen.')
                .tooltipster('open')
            setTimeout(function(){
                $("#victory").tooltipster('close');
                setTimeout(function(){
                    $("#victory").tooltipster('content', 'Klicke mich, wenn du eine Siegpunktkarte besitzt, um einen Siegpunkt zu erhalten.');
                }, 500);   
            }, 1000);
            break;

    }


});

addListener('END', function(event){
    var stringFromServer = event.data;
    var canvas = document.getElementsByTagName("canvas");
    $(".popup").html("<p class='popupMessage'>"+ stringFromServer +" hat gewonnen!</p>");
    
    $(".popup").bPopup({
        speed: 650,
        transition: 'fadeIn',
        transitionClose: 'slideDown',
        position:['auto',100]
    });
    
    canvas["0"].style.visibility = "visible";
    

});

// Fireworks
var SCREEN_WIDTH = document.getElementById('content').width,
    SCREEN_HEIGHT = document.getElementById('content').height,
    mousePos = {
        x: 400,
        y: 300
    },

    // create canvas
	canvas = document.createElement('canvas'),
    context = canvas.getContext('2d'),
    particles = [],
    rockets = [],
    MAX_PARTICLES = 400;


// init
$(document).ready(function() {
	document.getElementById('content').appendChild(canvas); // adds the canvas
															// to #someBox
    canvas.width = SCREEN_WIDTH;
    canvas.height = SCREEN_HEIGHT;
    setInterval(launch, 800);
    setInterval(loop, 1000 / 50);
});


// update mouse position
$(document).mousemove(function(e) {
    e.preventDefault();
    mousePos = {
        x: e.clientX,
        y: e.clientY
    };
});

// launch more rockets!!!
$(document).mousedown(function(e) {
    for (var i = 0; i < 5; i++) {
        launchFrom(Math.random() * SCREEN_WIDTH * 2 / 3 + SCREEN_WIDTH / 6);
    }
});

function launch() {
    launchFrom(mousePos.x);
}

function launchFrom(x) {
    if (rockets.length < 10) {
        var rocket = new Rocket(x);
        rocket.explosionColor = Math.floor(Math.random() * 360 / 10) * 10;
        rocket.vel.y = Math.random() * -3 - 4;
        rocket.vel.x = Math.random() * 6 - 3;
        rocket.size = 8;
        rocket.shrink = 0.999;
        rocket.gravity = 0.01;
        rockets.push(rocket);
    }
}

function loop() {
    // update screen size
    if (SCREEN_WIDTH != window.innerWidth) {
        canvas.width = SCREEN_WIDTH = window.innerWidth;
    }
    if (SCREEN_HEIGHT != window.innerHeight) {
        canvas.height = SCREEN_HEIGHT = window.innerHeight;
    }

    // clear canvas
    context.fillStyle = "rgba(0, 0, 0, 0.05)";
    context.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    var existingRockets = [];

    for (var i = 0; i < rockets.length; i++) {
        // update and render
        rockets[i].update();
        rockets[i].render(context);

        // calculate distance with Pythagoras
        var distance = Math.sqrt(Math.pow(mousePos.x - rockets[i].pos.x, 2) + Math.pow(mousePos.y - rockets[i].pos.y, 2));

        // random chance of 1% if rockets is above the middle
        var randomChance = rockets[i].pos.y < (SCREEN_HEIGHT * 2 / 3) ? (Math.random() * 100 <= 1) : false;

/*
 * Explosion rules - 80% of screen - going down - close to the mouse - 1% chance
 * of random explosion
 */
        if (rockets[i].pos.y < SCREEN_HEIGHT / 5 || rockets[i].vel.y >= 0 || distance < 50 || randomChance) {
            rockets[i].explode();
        } else {
            existingRockets.push(rockets[i]);
        }
    }

    rockets = existingRockets;

    var existingParticles = [];

    for (var i = 0; i < particles.length; i++) {
        particles[i].update();

        // render and save particles that can be rendered
        if (particles[i].exists()) {
            particles[i].render(context);
            existingParticles.push(particles[i]);
        }
    }

    // update array with existing particles - old particles should be garbage
	// collected
    particles = existingParticles;

    while (particles.length > MAX_PARTICLES) {
        particles.shift();
    }
}

function Particle(pos) {
    this.pos = {
        x: pos ? pos.x : 0,
        y: pos ? pos.y : 0
    };
    this.vel = {
        x: 0,
        y: 0
    };
    this.shrink = .97;
    this.size = 2;

    this.resistance = 1;
    this.gravity = 0;

    this.flick = false;

    this.alpha = 1;
    this.fade = 0;
    this.color = 0;
}

Particle.prototype.update = function() {
    // apply resistance
    this.vel.x *= this.resistance;
    this.vel.y *= this.resistance;

    // gravity down
    this.vel.y += this.gravity;

    // update position based on speed
    this.pos.x += this.vel.x;
    this.pos.y += this.vel.y;

    // shrink
    this.size *= this.shrink;

    // fade out
    this.alpha -= this.fade;
};

Particle.prototype.render = function(c) {
    if (!this.exists()) {
        return;
    }

    c.save();

    c.globalCompositeOperation = 'lighter';

    var x = this.pos.x,
        y = this.pos.y,
        r = this.size / 2;

    var gradient = c.createRadialGradient(x, y, 0.1, x, y, r);
    gradient.addColorStop(0.1, "rgba(255,255,255," + this.alpha + ")");
    gradient.addColorStop(0.8, "hsla(" + this.color + ", 100%, 50%, " + this.alpha + ")");
    gradient.addColorStop(1, "hsla(" + this.color + ", 100%, 50%, 0.1)");

    c.fillStyle = gradient;

    c.beginPath();
    c.arc(this.pos.x, this.pos.y, this.flick ? Math.random() * this.size : this.size, 0, Math.PI * 2, true);
    c.closePath();
    c.fill();

    c.restore();
};

Particle.prototype.exists = function() {
    return this.alpha >= 0.1 && this.size >= 1;
};

function Rocket(x) {
    Particle.apply(this, [{
        x: x,
        y: SCREEN_HEIGHT}]);

    this.explosionColor = 0;
}

Rocket.prototype = new Particle();
Rocket.prototype.constructor = Rocket;

Rocket.prototype.explode = function() {
    var count = Math.random() * 10 + 80;

    for (var i = 0; i < count; i++) {
        var particle = new Particle(this.pos);
        var angle = Math.random() * Math.PI * 2;

        // emulate 3D effect by using cosine and put more particles in the
		// middle
        var speed = Math.cos(Math.random() * Math.PI / 2) * 15;

        particle.vel.x = Math.cos(angle) * speed;
        particle.vel.y = Math.sin(angle) * speed;

        particle.size = 10;

        particle.gravity = 0.2;
        particle.resistance = 0.92;
        particle.shrink = Math.random() * 0.05 + 0.93;

        particle.flick = true;
        particle.color = this.explosionColor;

        particles.push(particle);
    }
};

Rocket.prototype.render = function(c) {
    if (!this.exists()) {
        return;
    }

    c.save();

    c.globalCompositeOperation = 'lighter';

    var x = this.pos.x,
        y = this.pos.y,
        r = this.size / 2;

    var gradient = c.createRadialGradient(x, y, 0.1, x, y, r);
    gradient.addColorStop(0.1, "rgba(255, 255, 255 ," + this.alpha + ")");
    gradient.addColorStop(1, "rgba(0, 0, 0, " + this.alpha + ")");

    c.fillStyle = gradient;

    c.beginPath();
    c.arc(this.pos.x, this.pos.y, this.flick ? Math.random() * this.size / 2 + this.size / 2 : this.size, 0, Math.PI * 2, true);
    c.closePath();
    c.fill();

    c.restore();
};
















