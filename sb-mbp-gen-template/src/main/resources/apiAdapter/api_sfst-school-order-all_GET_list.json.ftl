{
    "interNo": "list",
    "httpMethod": "GET",
    "httpUrl": "/api/sfst-school-order-all",
    "interDescr": "此接口针对xx页面",
    "param": {
        "criteriaList": [
            {
                "fromParam":"orderOrMaterialNoLike",
                "toCriteriaList": [
                    "orderOrMaterialNoLike",
                    "sfstSchoolBatchList.sfstSupplierInboundBatch.sfstMaterial.orderOrMaterialNoLike"
                ]
            },
            {
                "fromParam":"orderNo_e",
                "toCriteriaList": [
                    "orderNo.equals"
                ]
            }
        ],
        "associationNameList": [
            "associationDetail",
            "sfstSupplier",
            "sfstSchool",
            "sfstSchoolBatchList",
            "sfstSchoolBatchList.sfstSupplierInboundBatch",
            "sfstSchoolBatchList.sfstSupplierInboundBatch.productionDate",
            "sfstSchoolBatchList.sfstMaterial",
            "sfstSchoolBatchList.sfstMaterial.sfstSchoolMaterialAttr",
            "sfstSchoolBatchList.sfstMaterial.sfstMaterialPriceList",
            "sfstSchoolBatchList.sfstMaterial.sfstMaterialPriceList.unitPrice"
        ],
        "dictionaryNameList":[
            "categoryValue"
        ]
    },
    "result":{
        "fieldList":[
            {"name":"id", "fromName":"id"},
            {"name":"myOrderNo", "fromName":"orderNo"},
            {"name":"supplierName", "fromName":"sfstSupplier.name"},
            {"name":"batchList", "type":"list", "fromName":"sfstSchoolBatchList"},
            {"name":"batchList.test", "type":"object"},
            {"name":"batchList.test.abc", "fromName":"sfstSchoolBatchList.schoolBatchNo"},
            {"name":"batchList.sbNo", "fromName":"sfstSchoolBatchList.schoolBatchNo"},
            {"name":"batchList.productionDate", "fromName":"sfstSchoolBatchList.sfstSupplierInboundBatch.productionDate"},
            {"name":"supplier", "type":"object"},
            {"name":"supplier.ddd", "fromName":"sfstSupplier.name"},
            {"name":"supplier.myList", "type":"list", "fromName":"sfstSchoolBatchList"},
            {"name":"supplier.myList.ooo", "type":"object"},
            {"name":"supplier.myList.ooo.yourList", "type":"list", "fromName":"sfstSchoolBatchList.sfstMaterial.sfstMaterialPriceList"},
            {"name":"supplier.myList.ooo.yourList.qqqPrice", "fromName":"sfstSchoolBatchList.sfstMaterial.sfstMaterialPriceList.unitPrice"},
            {"name":"supplier.myList.ooo.yourList.myDate", "fromName":"sfstSchoolBatchList.sfstMaterial.sfstMaterialPriceList.executeDate"}
        ]
    },
    "sqlColumnList":[
        "id", "number", "material.name", "materialAttrList.useUpDaily", "schoolBatchList.batchNo",
        "schoolBatchList.school.name", "material.name", "material.upSupplier.name"
    ]
}