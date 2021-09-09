
clientwh.USER_INFO = "userinfo";

clientwh.idWorkflows = {
	'QUOTATION': 1,
	'PURCHASE': 2,
	'REQUEST': 3,
	'MATERIALIMPORT': 4,
	'MATERIALEXPORT': 5,
	'MATERIALBASELINE': 6,
	'MATERIALPRE': 7,
	'MATERIALTECH': 8,
	'REQUESTEXTRA': 9
};

clientwh.targetCopy =[{name:'clientwh_left_material_materialform_prematerial', value:'materialformprematerial'},{name:'clientwh_left_material_materialform_techmaterial', value:'materialformtechmaterial'},{name:'clientwh_left_material_import', value:'materialimport'},{name:'clientwh_left_material_export', value:'materialexport'},{name:'clientwh_left_supply_chain_material_Purchase', value:'purchase'},{name:'clientwh_left_supply_chain_material_request', value:'requestnormal'},{name:'clientwh_left_supply_chain_material_baseline', value:'materialbaseline'},{name:'clientwh_left_supply_chain_quotation', value:'quotation'}]   

clientwh.sortByNameCatalog = [
	{
		display:'clientwh_home_name',
		value:'name'
	},
	{
		display:'clientwh_home_code',
		value:'code'
	}
];
clientwh.sortByNameRequest = [
	{
		display:'clientwh_home_idstore',
		value:'idstore'
	},
	{
		display:'clientwh_home_idwriter',
		value:'idwriter'
	},
	{
		display:'clientwh_home_idreceiver',
		value:'idreceiver'
	},
	{
		display:'clientwh_home_idresponsible',
		value:'idresponsible'
	},
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_home_name',
		value:'name'
	}
];
clientwh.sortByNameAttachment = [
	{
		display:'clientwh_home_name',
		value:'filename'
	},
	{
		display:'clientwh_home_description',
		value:'description'
	}
];

clientwh.sortByNameMaterialImport = [
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_materialimport_store',
		value:'idstore'
	},
	{
		display:'clientwh_materialimport_idimporter',
		value:'idimporter'
	},
	{
		display:'clientwh_materialimport_importdate',
		value:'importdate'
	}

];

clientwh.sortByNameMaterialImportDetail = [
	{
		display:'clientwh_materialimportdetail_price',
		value:'price'
	},
	{
		display:'clientwh_materialimportdetail_quantity',
		value:'quantity'
	},
	{
		display:'clientwh_materialimportdetail_amount',
		value:'amount'
	}
];

clientwh.sortByNameMaterialExportDetail = [
	{
		display:'clientwh_materialexportdetail_quantity',
		value:'quantity'
	}
];

clientwh.sortByNameMaterialExport = [
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_materialexport_store',
		value:'idstore'
	},
	{
		display:'clientwh_materialexport_idexporter',
		value:'idexporter'
	},
	{
		display:'clientwh_materialexport_exportdate',
		value:'exportdate'
	}
];


clientwh.sortByNameMaterialform = [
	{
		display:'clientwh_materialform_name',
		value:'name'
	},
	{
		display:'clientwh_materialform_code',
		value:'code'
	},
	{
		display:'clientwh_materialform_formdate',
		value:'formdate'
	},
	{
		display:'clientwh_materialform_totalamount',
		value:'totalamount'
	},
];

clientwh.sortByNamematerialformdetaildetail = [
	{
		display:'clientwh_materialformdetail_idmaterial',
		value:'idmaterial'
	},
	{
		display:'clientwh_materialformdetail_quantity',
		value:'quantity'
	},
	{
		display:'clientwh_materialformdetail_price',
		value:'price'
	},
];

clientwh.sortByNameMaterialBaseline = [
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_materialbaseline_name',
		value:'name'
	},
	{
		display:'clientwh_materialbaseline_store',
		value:'idstore'
	},
	{
		display:'clientwh_materialbaseline_totalamount',
		value:'totalamount'
	},
	{
		display:'clientwh_materialbaseline_baselinedate',
		value:'baselinedate'
	}
];

clientwh.sortByNameMaterialBaselineDetail = [
	{
		display:'clientwh_materialbaselinedetail_idsupplier',
		value:'idsupplier'
	},
	{
		display:'clientwh_materialbaselinedetail_price',
		value:'price'
	},
	{
		display:'clientwh_materialbaselinedetail_quantity',
		value:'quantity'
	},
	{
		display:'clientwh_materialbaselinedetail_amount',
		value:'amount'
	},
	{
		display:'clientwh_materialbaselinedetail_startdate',
		value:'startdate'
	},
	{
		display:'clientwh_materialbaselinedetail_enddate',
		value:'enddate'
	}
];

clientwh.sortByNamePurchase = [
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_purchase_name',
		value:'name'
	},
	{
		display:'clientwh_purchase_store',
		value:'idstore'
	},
	{
		display:'clientwh_purchase_idreceiver',
		value:'idreceiver'
	},
	{
		display:'clientwh_purchase_idcontact',
		value:'idcontact'
	},
	{
		display:'clientwh_purchase_idsupplier',
		value:'idsupplier'
	},
	{
		display:'clientwh_purchase_totalamount',
		value:'totalamount'
	},
	{
		display:'clientwh_purchase_deliverydate',
		value:'deliverydate'
	}

];

clientwh.sortByNamePurchaseDetail = [
	{
		display:'clientwh_purchasedetail_materialname',
		value:'materialname'
	},
	{
		display:'clientwh_purchasedetail_price',
		value:'price'
	},
	{
		display:'clientwh_purchasedetail_quantity',
		value:'quantity'
	},
	{
		display:'clientwh_purchasedetail_amount',
		value:'amount'
	}
];

clientwh.sortByNameComment = [
	{
		display:'clientwh_comment_username',
		value:'username'
	},
	{
		display:'clientwh_comment_updatedate',
		value:'updatedate'
	}
	
];

clientwh.sortByNameType = [
	{
		display:'clientwh_home_name',
		value:'name'
	},
	{
		display:'clientwh_home_code',
		value:'code'
	},
	{
		display:'clientwh_catalog_type_system',
		value:'idparent'
	}
];

clientwh.workflowStatuses = {
	'NEW': 1,
	'UPDATE': 2,
	'DELETE': 3,
	'NOTUSE': 4,
	
	'WAITAPPROVE': 10,
	'SEND': 11,
	'RECEIVE': 12,
	'GETBACK': 13,
	'SENDBACK': 14,
	'CANCEL': 15,
	'APPROVE': 16,
	'RECEIVESENDBACK': 17
};

clientwh.tabs = [
	{
		id:'created', 
		name:'clientwh_home_created'
	},
	{
		id:'received', 
		name:'clientwh_home_received'
	},
	{
		id:'assigned', 
		name:'clientwh_home_assigned'
	},
	{
		id:'deleted', 
		name:'clientwh_home_deleted'
	}
];

clientwh.workflowexecuteView = clientwh.contextPath + "/view/workflowexecute_form.html";
