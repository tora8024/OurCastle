


Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'ux');
Ext.require([
    'Ext.LoadMask.*'
]);


var serverHost=""; //http://localhost


var newData =[
  {"childrenCnt":"1","mail":"tora8024@gmail.com","phoneNum":"0989355758","remark":"","bookingDate":"2015-04-03","periodId":"P12","parentName":"黃偉傑","bookingStatus":"Open","createDate":"2015-04-03 23:31:04","updateTime":"2015-04-03 23:31:04","orderId":"20150403-233104789-P12"}
  ,{"childrenCnt":"2","mail":"tora8024@gmail.com","phoneNum":"0989355758","remark":"","bookingDate":"2015-04-04","periodId":"P12","parentName":"黃偉傑","bookingStatus":"Open","createDate":"2015-04-03 23:31:04","updateTime":"2015-04-03 23:31:04","orderId":"20150403-233104789-P12"}
  ];

Ext.onReady(function() {
	
	
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading...",id:'myMask'});   
   
	initial();
   
});


function initial(){
	var now = new Date();
	 
    Ext.getCmp('startDate').setValue(now);	//set current date
    Ext.getCmp('endDate').setValue(now);	//set current date
     
     
	Ext.getCmp('today').setText(Ext.Date.format(now, 'Y-m-d'));
					
     // var nextdate = Ext.Date.add(now, Ext.Date.DAY, 1); //add one day
     // Ext.getCmp('endDate').setValue(nextdate);	
     queryOrder();
}


function queryOrder(){
	
	var startDate = Ext.getCmp('startDate').getValue();
	var startDateStr=Ext.Date.format(startDate, 'Y-m-d');
  
	var endDate = Ext.getCmp('endDate').getValue();
	var endDateStr=Ext.Date.format(endDate, 'Y-m-d');
	var periodId = Ext.getCmp('periodId').getValue().toString();
	var parentName = Ext.getCmp('parentName').getValue();
	var phoneNum = Ext.getCmp('phoneNum').getValue();
	var mail = Ext.getCmp('mail').getValue();
	
	var startDateStr=Ext.Date.format(startDate, 'Y-m-d');
	
	//Ext.Msg.alert('toQuery',startDateStr + endDateStr +periodId+ parentName + phoneNum +mail);
	
	if(periodId!=null && periodId.toString().split(",").length>3||periodId.toString().split(",").length==0){
		periodId="";
	}
	
	var postDate={  
				"startDate":startDateStr,
				"endDate":endDateStr,
				"periodId":periodId,
				"parentName":phoneNum,
				"phoneNum":phoneNum,
				"mail":mail
				};
	
	Ext.getCmp('myMask').show();
	//Ext.Msg.alert('postDate',Ext.JSON.encode(postDate));
	Ext.Ajax.request({
            url: serverHost+'/rs/findOrderByParam', //http://www.ourcastle.com.tw/OurCastle/rs/findOrderByParam
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            params : Ext.JSON.encode(postDate),
            success: function(conn, response, options, eOpts) {
                var result = Ext.JSON.decode(conn.responseText);
                if (result.status=="Ok") {
                	var resultData=Ext.JSON.decode(result.response);
					Ext.getCmp('grid').store.loadData(resultData);
					Ext.getCmp('grid').getView().refresh();
					Ext.getCmp('totalCnt').setText(resultData.length);
                } else {
                    Ext.Msg.alert('error',result.response);
                }
				Ext.getCmp('myMask').hide();
            },
            failure: function(conn, response, options, eOpts) {
                Ext.Msg.alert('錯誤訊息',conn.responseText);
				Ext.getCmp('myMask').hide();

            }
        });
  
}


function queryOrder(){
	
	var startDate = Ext.getCmp('startDate').getValue();
	var startDateStr=Ext.Date.format(startDate, 'Y-m-d');
  
	var endDate = Ext.getCmp('endDate').getValue();
	var endDateStr=Ext.Date.format(endDate, 'Y-m-d');
	var periodId = Ext.getCmp('periodId').getValue().toString();
	var parentName = Ext.getCmp('parentName').getValue();
	var phoneNum = Ext.getCmp('phoneNum').getValue();
	var mail = Ext.getCmp('mail').getValue();
	
	var startDateStr=Ext.Date.format(startDate, 'Y-m-d');
	
	//Ext.Msg.alert('toQuery',startDateStr + endDateStr +periodId+ parentName + phoneNum +mail);
	
	if(periodId!=null && periodId.toString().split(",").length>3||periodId.toString().split(",").length==0){
		periodId="";
	}
	
	var postDate={  
				"startDate":startDateStr,
				"endDate":endDateStr,
				"periodId":periodId,
				"parentName":phoneNum,
				"phoneNum":phoneNum,
				"mail":mail
				};
	
	Ext.getCmp('myMask').show();
				
	//Ext.Msg.alert('postDate',Ext.JSON.encode(postDate));
	Ext.Ajax.request({
            url: serverHost+'/rs/findOrderByParam', //http://www.ourcastle.com.tw/OurCastle/rs/findOrderByParam
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            params : Ext.JSON.encode(postDate),
            success: function(conn, response, options, eOpts) {
            	
            	
            	
	
				var nowTime = new Date();
				var lastQueryTime=Ext.Date.format(nowTime, 'Y-m-d H:i:s');
				Ext.getCmp('lastQueryTime').setText("Last Query:"+lastQueryTime);
					
                var result = Ext.JSON.decode(conn.responseText);
                if (result.status=="Ok") {
                	var resultData=Ext.JSON.decode(result.response);
					Ext.getCmp('grid').store.loadData(resultData);
					Ext.getCmp('grid').getView().refresh();
					Ext.getCmp('grid').store.filter('bookingStatus', 'Open');
					setTodayBookingCnt(resultData);
                } else {
                    Ext.Msg.alert('error',result.response);
                }
				Ext.getCmp('myMask').hide();
            },
            failure: function(conn, response, options, eOpts) {
                Ext.Msg.alert('錯誤訊息',conn.responseText);
				Ext.getCmp('myMask').hide();

            }
        });
  
}

function cancelOrder(){
	
	if(Ext.getCmp('grid').getSelectionModel().getSelection().length==0){
		Ext.Msg.alert('錯誤訊息', "請選取一筆訂單");
		return;
	}
	
	var row = Ext.getCmp('grid').getSelectionModel().getSelection()[0].data;
	
	var bookingDate = row['bookingDate'];
	var periodId= row['periodId'];
	var orderId =  row['orderId'];
	var phoneNum =  row['phoneNum'];

	var postDate={  
				"bookingDate":bookingDate,
				"periodId":periodId,
				"phoneNum":phoneNum,
				"orderId":orderId
				};
	
	Ext.getCmp('myMask').show();
	//Ext.Msg.alert('postDate',Ext.JSON.encode(postDate));
	Ext.Ajax.request({
            url: serverHost+'/rs/cancelOrder', 
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            params : Ext.JSON.encode(postDate),
            success: function(conn, response, options, eOpts) {
                var result = Ext.JSON.decode(conn.responseText);
                if (result.status=="Ok") {
                	queryOrder();
                } else {
                    Ext.Msg.alert('error',result.response);
                }
				Ext.getCmp('myMask').hide();
            },
            failure: function(conn, response, options, eOpts) {
                Ext.Msg.alert('錯誤訊息',conn.responseText);
				Ext.getCmp('myMask').hide();
            }
        }); 
}

function setTodayBookingCnt(resultData){
	
	var bookingCnt_P11=0;
	var bookingCnt_P12=0;
	var bookingCnt_P13=0;
	var bookingCnt_P14=0;
	
	if(resultData!=null&&resultData.length>0){
		for(var i=0;i<resultData.length;i++){
			if(Ext.getCmp('today').text==resultData[i].bookingDate
			&& resultData[i].bookingStatus=="Open"){
				if(resultData[i].periodId=="P11"){
					bookingCnt_P11=parseInt(bookingCnt_P11)+parseInt(resultData[i].childrenCnt);
				}
				if(resultData[i].periodId=="P12"){
					bookingCnt_P12=parseInt(bookingCnt_P12)+parseInt(resultData[i].childrenCnt);
				}
				if(resultData[i].periodId=="P13"){
					bookingCnt_P13=parseInt(bookingCnt_P13)+parseInt(resultData[i].childrenCnt);
				}
				if(resultData[i].periodId=="P14"){
					bookingCnt_P14=parseInt(bookingCnt_P14)+parseInt(resultData[i].childrenCnt);
				}
			}
			
		}
		
	}	
	
	Ext.getCmp('bookingCnt_P11').setText(bookingCnt_P11);
	Ext.getCmp('bookingCnt_P12').setText(bookingCnt_P12);
	Ext.getCmp('bookingCnt_P13').setText(bookingCnt_P13);
	Ext.getCmp('bookingCnt_P14').setText(bookingCnt_P14);
	
	Ext.getCmp('totalCnt').setText(parseInt(bookingCnt_P11)+parseInt(bookingCnt_P12)+parseInt(bookingCnt_P13)+parseInt(bookingCnt_P14));
}
   

      
