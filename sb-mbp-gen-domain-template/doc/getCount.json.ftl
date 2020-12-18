{
    "httpMethod": "GET",
    "addDefaultPrefix": "yes",
    "httpUrl": "/${entityUrl}-count",
    "interDescr": "获取数量-${entityComment}",
    "param": {
        "criteriaList": [
<#list fieldList as field>
            {
                "fromParam":"${field.camelName}.xxx",
                "descr":"${field.comment}"
            },
</#list>
<#list toFromList as toFrom>
            {
                "fromParam":"${toFrom.toFromEntityName}Id.xxx",
                "descr":"${toFrom.toFromComment}ID"
            },
            {
                "fromParam":"${toFrom.toFromEntityName}.?.xxx",
                "descr":"关联的${toFrom.toFromComment}，其中 ? 对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"
            },
</#list>
            {
                "fromParam":"id.xxx",
                "descr":"主键ID"
            }
        ]
    },
    "result":{
        "resultCode": "1：操作成功  2：操作失败",
        "errMsg": "错误消息",
        "data": "查询出的数量"
    }
}