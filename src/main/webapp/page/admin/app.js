/*global Ext:false */
/*

 This file is part of Ext JS 4

 Copyright (c) 2011 Sencha Inc

 Contact:  http://www.sencha.com/contact

 GNU General Public License Usage
 This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

 If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'ux');
Ext.require(['Ext.grid.*', 'Ext.data.*', 'Ext.ux.grid.FiltersFeature', 'Ext.toolbar.Paging']);

Ext.define('Product', {
	extend : 'Ext.data.Model',
	fields : [{
		name : 'orderId',
		type : 'string'
	}, {
		name : 'bookingDate',
		type : 'string'
	}, {
		name : 'periodId',
		type : 'string'
	}, {
		name : 'periodTitle',
		type : 'string'
	}, {
		name : 'parentName',
		type : 'string'
	}, {
		name : 'phoneNum',
		type : 'string'
	}, {
		name : 'mail',
		type : 'string'
	}, {
		name : 'childrenCnt',
		type : 'numeric'
	}, {
		name : 'bookingStatus',
		type : 'string'
	}, {
		name : 'createDate',
		type : 'string'
	}, {
		name : 'updateTime',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}]
});

Ext.onReady(function() {

	Ext.QuickTips.init();

	// The data store containing the list of states
	var periodList = Ext.create('Ext.data.Store', {
		fields : ['abbr', 'name'],
		data : [{
			"abbr" : "P11",
			"name" : "早安場"
		}, {
			"abbr" : "P12",
			"name" : "午安場"
		}, {
			"abbr" : "P13",
			"name" : "下午場"
		}, {
			"abbr" : "P14",
			"name" : "晚安場"
		}]
	});

	// for this demo configure local and remote urls for demo purposes
	var url = {
		local : 'grid-filter.json', // static data file
		remote : 'grid-filter.php'
	};

	// configure whether filter query is encoded or not (initially)
	var encode = false;

	// configure whether filtering is performed locally or remotely (initially)
	var local = true;

	var store = Ext.create('Ext.data.JsonStore', {
		// store configs
		autoDestroy : true,
		model : 'Product',
		proxy : {
			type : 'ajax',
			url : ( local ? url.local : url.remote),
			reader : {
				type : 'json',
				root : 'data',
				idProperty : 'orderId',
				totalProperty : 'total'
			}
		},
		remoteSort : false,
		sorters : [{
			property : 'bookingDate',
			direction : 'ASC'
		}, {
			property : 'periodId',
			direction : 'ASC'
		}],
		pageSize : 5
	});

	var filters = {
		ftype : 'filters',
		// encode and local configuration options defined previously for easier reuse
		encode : encode, // json encode the filter query
		local : local, // defaults to false (remote filtering)

		// Filters are most naturally placed in the column definition, but can also be
		// added here.
		filters : [{
			type : 'boolean',
			dataIndex : 'visible'
		}]
	};

	var columns = [{
		dataIndex : 'bookingDate',
		text : '預約日期',
		filter : true,
		renderer : Ext.util.Format.dateRenderer('Y-m-d'),
		width : 90
	}, {
		dataIndex : 'periodId',
		hidden : false,
		text : '場次編號',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 70
	}, {
		dataIndex : 'periodTitle',
		text : '場次',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 150
	}, {
		dataIndex : 'bookingStatus',
		text : '狀態',
		filter : {
			type : 'string'
			// type: 'list',
			// options: ['small', 'medium', 'large', 'extra large']
			//,phpMode: true
		},
		width : 70
	}, {
		dataIndex : 'parentName',
		text : '家長姓名',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 60
	}, {
		dataIndex : 'phoneNum',
		text : '電話號碼',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 90
	}, {
		dataIndex : 'childrenCnt',
		text : '小孩人數',
		filter : {
			type : 'numeric' // specify type here or in store fields config
		},
		width : 70
	}, {
		dataIndex : 'mail',
		text : 'E-Mail',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 200
		// ,renderer: function(value, metaData, record, rowIdx, colIdx, store) {
	        // value = Ext.String.htmlEncode(value);
	        // metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
	        // return value;
	   	// }
	}, {
		dataIndex : 'remark',
		text : '備註',
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 50
	}, {
		dataIndex : 'createDate',
		text : '建立日期',
		filter : true,
		renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
		width : 150
	}, {
		dataIndex : 'updateTime',
		text : '修改時間',
		filter : true,
		renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
		width : 150
	},{
		dataIndex : 'orderId',
		text : '編號',
		flex : 1,
		filterable : false,
		filter : {
			type : 'string'
			// specify disabled to disable the filter menu
			//, disabled: true
		},
		width : 50
	}];

	var grid = Ext.create('Ext.grid.Panel', {
		border : true,
		scroll : 'both',
		editable : true,
		region : 'center',
		layout : 'fit',
		id : 'grid',
		title : '預約紀錄',
		store : store,
		width : '100%',
		height : 500,
		columns : columns,
		loadMask : true,
		features : [filters],
		viewConfig: {
			enableTextSelection: true
		},
		dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			items : [{
				xtype : 'button',
				text : '取消預約',
				icon : '../assets/icon/cancel.png',
				style : {
					'font-weight' : 'bold'
				},
				handler : function() {
					cancelOrder();
				}
			}, '-', {
				xtype : 'tbfill'
			}, '-', {
				xtype : 'tbspacer'
			}, {
				xtype : 'label',
				id:'today',
				text : 'Y-m-d',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' ,
					'color': 'red'

				},
				margin : '0 15 0 0'
			}, {
				xtype : 'label',
				text : '今日預約:',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 

				},
				margin : '0 15 0 0'
			},  {
				xtype : 'label',
				text : '早安場: ',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 

				}
			}, {
				xtype : 'label',
				id:'bookingCnt_P11',
				text : '0',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' ,
					'color': 'red'

				},
				margin : '0 10 0 0'
			}, {
				xtype : 'label',
				text : '午安場: ',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 

				}
			}, {
				xtype : 'label',
				id:'bookingCnt_P12',
				text : '0',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' ,
					'color': 'red'

				},
				margin : '0 10 0 0'
			}, {
				xtype : 'label',
				text : '下午場: ',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 

				}
			}, {
				xtype : 'label',
				id:'bookingCnt_P13',
				text : '0',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' ,
					'color': 'red'

				},
				margin : '0 10 0 0'
			}, {
				xtype : 'label',
				text : '晚安場: ',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 

				}
			}, {
				xtype : 'label',
				id:'bookingCnt_P14',
				text : '0',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' ,
					'color': 'red'

				},
				margin : '0 10 0 0'
			}, '-', {
				xtype : 'label',
				text : 'totals: ',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px' 


				},
				bodyStyle : {
					'padding-left' : '100px'
				}
			}, {
				xtype : 'label',
				id : 'totalCnt',
				text : '0',
				style : {
					'font-weight' : 'bold',
					'paddingRight' : '8px'  ,
					'color': 'red'
				}
			}]
		}]

		// ,
		// dockedItems: [Ext.create('Ext.toolbar.Paging', {
		// dock: 'bottom',
		// store: store
		// })]
	});

	var resultsPanel = Ext.create('Ext.panel.Panel', {
		title : '預約管理系統',
		region : 'center',
		layout : 'fit',
		width : '100%',
		height : 600,
		renderTo : Ext.getBody(),
		layout : {
			type : 'vbox', // Arrange child items vertically
			align : 'stretch', // Each takes up full width
			padding : 5
		},
		items : [{// Details Panel specified as a config object (no xtype defaults to 'panel').
			bodyPadding : 5,
			items : [{
				layout : 'column',
				border : false,
				items : [{
					xtype : 'datefield',
					anchor : '100%',
					fieldLabel : '預約日期',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					editable : false,
					labelWidth : 70,
					width : 175,
					id : 'startDate',
					// The value matches the format; will be parsed and displayed using that format.
					format : 'Y-m-d',
					value : '1978 2 4 '
				}, {
					xtype : 'label',
					anchor : '100%',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'center'
					},
					text : '~',
					labelWidth : 5,
					width : 20
				}, {
					xtype : 'datefield',
					anchor : '100%',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					editable : false,
					labelWidth : 70,
					width : 100,
					id : 'endDate',
					// The value matches the format; will be parsed and displayed using that format.
					format : 'Y-m-d',
					value : '1978 2 4 '
				}, {
					fieldLabel : '場次',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					xtype : 'combo',
					multiSelect : true,
					id : 'periodId',
					labelWidth : 50,
					width : 150,
					editable : false,
					store : periodList,
					queryMode : 'local',
					displayField : 'name',
					valueField : 'abbr'
				}, {
					fieldLabel : '家長姓名',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					id : 'parentName',
					labelWidth : 70,
					width : 200,
					xtype : 'textfield'
				}, {
					fieldLabel : '電話號碼',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					id : 'phoneNum',
					labelWidth : 70,
					width : 220,
					xtype : 'textfield'
				}, {
					fieldLabel : 'E-Mail',
					style : {
						'font-weight' : 'bold',
						'textAlign' : 'right'
					},
					id : 'mail',
					labelWidth : 70,
					width : 295,
					xtype : 'textfield'
				}, {
					xtype : 'button',
					text : '查詢',
					icon : '../assets/icon/search.png',
					style : {
						'font-weight' : 'bold'
					},
					handler : function() {
						queryOrder();
					},
					margin : '0 0 0 20'
				}, {
					xtype : 'label',
					text : '',
					id : 'lastQueryTime',
					width : 180,
					margin : '5 0 0 10'

				}]
			}], // An array of form fields
			flex : 2 // Use 2/3 of Container's height (hint to Box layout)
		}, {
			xtype : 'splitter' // A splitter between the two child items
		}, grid]
	});

	function cellToopTip(value, metaData, record, rowIdx, colIdx, store) {
        value = Ext.String.htmlEncode(value);
        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
        return value;
   	}
	store.load();

	// Ext.EventManager.onWindowsize(function(){
	// var width =Ext.getBody().getViewSize().width -20;
	// var height =Ext.getBody().getViewSize().height -20;
	// resultsPanel.setSize(width,height);
	// });

	// add some buttons to bottom toolbar just for demonstration purposes
	// grid.child('pagingtoolbar').add([
	// '->',
	// {
	// text: 'Encode: ' + (encode ? 'On' : 'Off'),
	// tooltip: 'Toggle Filter encoding on/off',
	// enableToggle: true,
	// handler: function (button, state) {
	// var encode = (grid.filters.encode !== true);
	// var text = 'Encode: ' + (encode ? 'On' : 'Off');
	// grid.filters.encode = encode;
	// grid.filters.reload();
	// button.setText(text);
	// }
	// },
	// {
	// text: 'Local Filtering: ' + (local ? 'On' : 'Off'),
	// tooltip: 'Toggle Filtering between remote/local',
	// enableToggle: true,
	// handler: function (button, state) {
	// var local = (grid.filters.local !== true),
	// text = 'Local Filtering: ' + (local ? 'On' : 'Off'),
	// newUrl = local ? url.local : url.remote,
	// store = grid.view.getStore();
	//
	// // update the GridFilter setting
	// grid.filters.local = local;
	// // bind the store again so GridFilters is listening to appropriate store event
	// grid.filters.bindStore(store);
	// // update the url for the proxy
	// store.proxy.url = newUrl;
	//
	// button.setText(text);
	// store.load();
	// }
	// },
	// {
	// text: 'All Filter Data',
	// tooltip: 'Get Filter Data for Grid',
	// handler: function () {
	// var data = Ext.encode(grid.filters.getFilterData());
	// Ext.Msg.alert('All Filter Data',data);
	// }
	// },{
	// text: 'Clear Filter Data',
	// handler: function () {
	// grid.filters.clearFilters();
	// }
	// },{
	// text: 'Add Columns',
	// handler: function () {
	// if (grid.headerCt.items.length < 6) {
	// grid.headerCt.add(createColumns(6, 4));
	// grid.view.refresh();
	// this.disable();
	// }
	// }
	// }
	// ]);
});

