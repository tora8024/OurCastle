

function loadingStart(){
	$("#booking").mask("Waiting...");
}


function loadingEnd(){
	$("#booking").unmask();
}


function writeLog(msg){
	// if(navigator.userAgent.indexOf("MSIE")>0){
   // //ie
   // }else if(navigator.userAgent.indexOf("Firefox")>0){
   // //firefox
   // }else if(navigator.userAgent.indexOf("Chrome")>0){
   // //chrome
   	// console.log(msg);
   // }else if(navigator.userAgent.indexOf("Safari")>0){
   // //safari
   // }else{
   // //this part can be used as opera area
   // }
}


function blinklink() {
	if (!document.getElementById('a_promotionActivities').style.color) {
		document.getElementById('a_promotionActivities').style.color = "red"
	}
	if (document.getElementById('a_promotionActivities').style.color == "red") {
		document.getElementById('a_promotionActivities').style.color = "blue"
	} else {
		document.getElementById('a_promotionActivities').style.color = "red"
	}
	timer = setTimeout("blinklink()", 500)
}

function stoptimer() {
	clearTimeout(timer)
}
