var address ="xxxxx";
var addressHref="http://maps.google.com.au/maps?q=xxxxx";
var telephone ="";
var telephoneHref ="tel:"+telephone;
var mobilePhone ="";
var mobilePhoneHref ="tel:"+mobilePhone;


var mail ="xxxx@gmail.com";
var mailHref="mailto:"+mail+"?cc=&subject=Hi,%20Our%20Castle%20&body=";

var nowDate = new Date();
var nowDateStr=nowDate.getFullYear() + '-' +  (nowDate.getMonth() + 1) + '-' + nowDate.getDate() ;

var maxDate = new Date(2015,8-1,30);


$( document ).ready(function() {
	 changeCarouselHeight();
	initial();
});


$( window ).resize(function() {
	 changeCarouselHeight();
});



var serverHost=""; //http://localhost




function initial(){
	blinklink();
	$('#ui-datepicker-div').hide();
	initial_contantInfo();	
	initial_uiObject();
	
}

function initial_contantInfo(){
	$('#a_address').text(address);
	$("#a_address").attr("href", addressHref);
	

	$('#a_telephoneLink').text(telephone);
	$("#a_telephoneLink").attr("href", telephoneHref);
	$("#a_bookingTelephoneLink").attr("href", telephoneHref);	
	$('#a_mobilePhoneLink').text(mobilePhone);
	$("#a_mobilePhoneLink").attr("href", mobilePhoneHref);
	
	$('#a_mailLink').text(mail);
	$("#a_mailLink").attr("href", mailHref);
		
}

function changeCarouselHeight(){
	//mix heitht
	// var winHeight="";
	// if ($(window).height()<500){
		// return;
	// }
// 
	// winHeight=$(window).height()+'px';
	// if($(window).height()>700){
		// winHeight='700px';
	// }
	
	
	// $("#carousel-0").css("height", winHeight);
	// $("#carousel-1").css("height", winHeight);
	// $("#carousel-2").css("height", winHeight);
	// $("#carousel-3").css("height", winHeight);
	// $("#carousel-4").css("height", winHeight);
	// $("#carousel-5").css("height", winHeight);
	// $("#carousel-6").css("height", winHeight);
	// $("#carousel-7").css("height", winHeight);
	// $("#carousel").css("height", winHeight);
	// $(".carousel-control.right").css("height", winHeight);
	// $(".carousel-control.left").css("height", winHeight);
}



function initial_uiObject(){
	
	$( ".date-input-css" ).datepicker();
	$("#inputBookingDate").datepicker( "option", "minDate", nowDate );
//	$("#inputBookingDate").datepicker( "option", "maxDate", "+13d" );
	
    $("#inputBookingDate" ).datepicker( "option", "maxDate", new Date(2015, 8 - 1,30));
    
	$("#inputBookingDate").datepicker( "option", "dateFormat", "yy-mm-dd");
	$( "#inputBookingDate").datepicker( "setDate", nowDateStr );
	$( "#inputBookingDate").text(nowDateStr);
	
	$( "#inputBookingDate").datepicker( "option", "closeText", "Close" );
	
	if(nowDate>=maxDate){
		//sunset booking
		$('#booking_li').hide(); 
		$('#booking_a').hide();
		document.getElementById('ui-datepicker-div').style.display = 'none'; 
		$('#booking_divider').hide(); 
		$('#booking').hide();
		$('#oldProductInfo').hide();
		
		
		
		
	}else{
    	queryAvailbePeriod();
    }							

}

function clearData(){
	
	$("#ck_period_11").prop("checked", false);
	$("#ck_period_12").prop("checked", false);
	$("#ck_period_13").prop("checked", false);
	$("#ck_period_14").prop("checked", false);
	
	$("#inputParentName").val("");
	$("#inputKidsQuantity").val("");
	$("#inputConfirmTelephone").val("");
	$("#inputTelephone").val("");
	mail= $("#inputEmail").val("");
	
}

function queryAvailbePeriod(){
	
	var selectedDate = $( "#inputBookingDate" ).datepicker( "getDate" );
	var bookingDate=getFormattedDate(selectedDate);
	// alert(bookingDate);
	

	$('#ui-datepicker-div').hide();
	$("#div_period_11").hide();
	$("#div_period_12").hide();
	$("#div_period_13").hide();
	$("#div_period_14").hide();
	$("#div_no_period").hide();
	
	$("#cnt_11_post").show();
	$("#cnt_12_post").show();
	$("#cnt_13_post").show();
	$("#cnt_14_post").show();
	
	$("#cnt_11_pre").show();
	$("#cnt_12_pre").show();
	$("#cnt_13_pre").show();
	$("#cnt_14_pre").show();
	
	$("#ck_period_11").prop("checked", false);
	$("#ck_period_12").prop("checked", false);
	$("#ck_period_13").prop("checked", false);
	$("#ck_period_14").prop("checked", false);
	
	
	$("#ck_period_11").prop("disabled", false);
	$("#ck_period_12").prop("disabled", false);
	$("#ck_period_13").prop("disabled", false);
	$("#ck_period_14").prop("disabled", false);
	
    
    $('#cnt_11').text("0");
    $('#cnt_12').text("0");	
    $('#cnt_13').text("0");	
    $('#cnt_14').text("0");	
    
    
    
    loadingStart();
     $.ajax({
	    type: "get",
	    url: serverHost+"/rs/findAvailablePeriod/"+bookingDate,
	    dataType: "json",
	    contentType:"application/json",
    	cache : false,
	    success: function(data){
			loadingEnd();
        	//{"availableCnt":"20","desc":"","title":"早安場(09：30 - 12：00)","periodId":"N11","bookingDate":"2015-03-23"}
        	        	
	    	if (data.status=="Ok"){	
	        	var jsonArr=JSON.parse(data.response);
	        	if(jsonArr.length==0){
					$("#div_no_period").show();
	        	}else{
			        for(var i=0;i<jsonArr.length;i++){
			        	var periodId=jsonArr[i].periodId;
			        	var id=periodId.replace("P", "");
      		        	$('#cnt_'+id).text(jsonArr[i].availableCnt);	
	        			$("#div_period_"+id).show();
	        			if(jsonArr[i].availableCnt=="0"){
							$("#ck_period_"+id).prop("disabled", true);
							$("#cnt_"+id+"_post").hide();							
							$("#cnt_"+id+"_pre").hide();	
							$('#cnt_'+id).text("已額滿");	
	        			}
	        		}
	        	}
	        	
	        	

	        	$("#div_period_14").hide();
			    	
	    	}else{
	    		alert(data.response);

	    		$("#div_period_14").hide();
        	}
	    },
	    error: function(xhr, ajaxOptions) {
	    	loadingEnd();
	        alert("系統發生異常,請重新查詢, \n或撥 "+telephone+"/"+ mobilePhone+"\n查詢預約資訊");
			writeLog("status code:"+xhr.status +"\n statusText:"+xhr.statusText);
	    }
	 });
	 
}





function submitOrder(){
	
	var selectedDate = $( "#inputBookingDate" ).datepicker( "getDate" );
	var bookingDate=getFormattedDate(selectedDate);
	var parentName= $("#inputParentName").val();
	var KidsQuantity= $("#inputKidsQuantity").val();
	var phone= $("#inputConfirmTelephone").val();
	var confirmPhone= $("#inputTelephone").val();
	var mail= $("#inputEmail").val();
	     
  	var   reg   =   /\s/g;     
  	mail = mail.replace(reg,   "");     
  	phone = phone.replace(reg,   "");
  	confirmPhone = confirmPhone.replace(reg,   "");

	if(parentName==null||parentName.length==0){
		alert("未填 家長姓名");
		return;
	}
	if(KidsQuantity==null||KidsQuantity.length==0){
		alert("未填 小孩人數");
		return;
	}
	
	// if(parseInt(KidsQuantity)>20){
		// $("#inputKidsQuantity").val("");
		// alert("小孩人數超過20,請重填小孩人數");
		// return;
	// }
	
	if(parseInt(KidsQuantity)==0){
	    $("#inputKidsQuantity").val("");
		alert("小孩人數為0,請重填小孩人數");
		return;
	}
	
	
	if(phone==null||phone.length==0){
		alert("未填 聯絡電話");
		return;
	}
	
	if(confirmPhone==null||confirmPhone.length==0){
		alert("未填 確認聯絡電話");
		return;
	}
	
	
	if(mail==null||mail.length==0){
		alert("未填 E-Mail信箱");
		return;
	}else{
		var strEmail = mail; 
		strEmail =strEmail.trim();
// 一個正確的 Email ，可能會有以下規則：
// (1) 必須以一個以上的文字&數字開頭
// (2) @ 之前可以出現 1 個以上的文字、數字與「-」的組合，例如 -abc-
// (3) @ 之前可以出現 1 個以上的文字、數字與「.」的組合，例如 .abc.
// (4) @ 之前以上兩項以 or 的關係出現，並且出現 0 次以上
// (5) 中間一定要出現一個 @
// (6) @ 之後出現一個以上的大小寫英文及數字的組合
// (7) @ 之後只能出現「.」或是「-」，但這兩個字元不能連續時出現
// (8) @ 之後出現 0 個以上的「.」或是「-」配上大小寫英文及數字的組合
// (9) @ 之後出現 1 個以上的「.」配上大小寫英文及數字的組合，結尾需為大小寫英文0-9]+)*\.[A-Za-z]+$/;
		var emailRule = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z]+$/; 
		if(strEmail.search(emailRule)== -1){
		    alert("E-Mail格式錯誤");  
			return;      
		}
	}
	if(confirmPhone!=phone){
		alert("電話號碼和確認電話號碼不一致");
		return;
	}
	
	
    var periodId = $('input[name="CheckBoxCities"]:checked', '#ck_period_area').val();

	if (periodId==null||periodId.length == 0) {
		alert("未選取預約時段");
		return;
	} else {
		var isOver = false;
		var availbePeriodCnt = "";
		var lbPeriodTitle = "";
		var cntId = periodId.replace("P", "cnt_");
		var lbPeriodId = periodId.replace("P", "lb_period_");
		availbePeriodCnt = $('#' + cntId).text();
		lbPeriodTitle = $('#' + lbPeriodId).text();
		if (parseInt(KidsQuantity) > parseInt(availbePeriodCnt)) {
			$("#inputKidsQuantity").val("");
			alert("已超出 " + lbPeriodTitle + ",\n可預約人數 : " + availbePeriodCnt);
			return;
		}
	}

	  
	  
	  
//	  var bookingData="{  "+
//						"   \"childrenCnt\":   \""+KidsQuantity +"\""+
//						"  ,\"mail\":          \""+mail	      +"\""+
//						"  ,\"phoneNum\":      \""+phone	      +"\""+
//						"  ,\"bookingDate\":   \""+bookingDate  +"\""+
//						"  ,\"remark\":        \"\""  			 +					
//						"  ,\"periodId\":      \""+periodId    +"\""+ 
//						"  ,\"parentName\":    \""+parentName   +"\""+ 
//					"  }";
	  
	  var bookingData={childrenCnt:KidsQuantity 
			  			,mail: mail	     
			  			,phoneNum:phone
			  			,bookingDate: bookingDate
			  			,remark: ""
			  			,periodId: periodId 
			  			,parentName: parentName };
				
	 loadingStart();	
	 $.ajax({
	    type: "post",
	    url: serverHost+"/rs/submitOrder",
	    data: JSON.stringify(bookingData),
	    dataType: "json",
    	cache : false,
	    contentType:"application/json",
	    success: function(data){
	    	loadingEnd();
	    	if (data.status=="Ok"){
	    		alert(data.response);
	    	}else{
	    		alert(data.response);
	    	}
	    	queryAvailbePeriod();
	    },
	    error: function(xhr, ajaxOptions) {
	    	loadingEnd();
			alert("系統發生異常,請重新預約, \n或撥 "+telephone+"/"+ mobilePhone+"\n預約場次");
			writeLog("status code:"+xhr.status +"\n statusText:"+xhr.statusText);
			
	    	queryAvailbePeriod();
	    }
	 });
  
  
}

function getFormattedDate(date) {
  var year = date.getFullYear();
  var month = (1 + date.getMonth()).toString();
  month = month.length > 1 ? month : '0' + month;
  var day = date.getDate().toString();
  day = day.length > 1 ? day : '0' + day;
  return year + '-' + month + '-' + day;
}
