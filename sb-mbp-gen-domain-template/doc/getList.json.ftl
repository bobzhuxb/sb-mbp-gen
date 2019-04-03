{
    "httpMethod": "GET",
    "httpUrl": "/api/${entityUrl}-all",
    "interDescr": "获取所有-${entityComment}",
    "param": {
        "criteriaList": [
            {
                "fromParam":"associationNameList",
                "descr":"关联查询的字段${associationNameComment}"
            },
            {
                "fromParam":"dictionaryNameList",
                "descr":"关联查询的数据字典值${dictionaryNameList}"
            },
            {
                "fromParam":"sort",
                "descr":"排序"
            },
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
        "data": "查询出的实体列表",
        "fieldList":[
            {"name":"id", "descr":"主键ID"},
<#list fieldList as field>
            {"name":"${field.camelName}", "descr":"${field.comment}"},
</#list>
<#list toFromList as toFrom>
            {"name":"${toFrom.toFromEntityName}Id", "descr":"${toFrom.toFromComment}ID"},
            {"name":"${toFrom.toFromEntityName}", "type":"object", "descr":"${toFrom.toFromComment}（级联类型：${toFrom.toFromEntityType}）"},
</#list>
<#list fromToList as fromTo>
            {"name":"${fromTo.fromToEntityName}<#if fromTo.relationType == 'OneToMany'>List</#if>", "type":<#if fromTo.relationType == 'OneToOne'>"object"<#else>"list"</#if>, "descr":"${fromTo.fromToComment}<#if fromTo.relationType == 'OneToMany'>列表</#if>（级联类型：${fromTo.fromToEntityType}）"},
</#list>
            {"name":"xxxxxxxx", "descr":"xxxxxxxx"}
        ]
    }
}