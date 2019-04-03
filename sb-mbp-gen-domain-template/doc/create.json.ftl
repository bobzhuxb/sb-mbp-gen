{
    "httpMethod": "POST",
    "httpUrl": "/api/${entityUrl}",
    "interDescr": "新增${entityComment}",
    "param": {
        "jsonBody": {
<#list fieldList as field>
<#if field.camelName != 'insertTime' && field.camelName != 'insertUserId' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>
            "${field.camelName}": "${field.comment}<#if field.javaType != 'String'>（无引号）</#if>",
</#if>
</#list>
<#list toFromList as toFrom>
            "${toFrom.toFromEntityName}Id": "${toFrom.toFromComment}ID（无引号）",
</#list>
<#list fromToList as fromTo>
            "${fromTo.fromToEntityName}<#if fromTo.relationType == 'OneToMany'>List</#if>": <#if fromTo.relationType == 'OneToMany'>[</#if>{"说明": "${fromTo.fromToComment}<#if fromTo.relationType == 'OneToMany'>列表</#if>（级联类型：${fromTo.fromToEntityType}）"}<#if fromTo.relationType == 'OneToMany'>]</#if>,
</#list>
            "xxxxxxxx": "xxxxxxxx"
        }
    },
    "result":{
        "resultCode": "1：操作成功  2：操作失败",
        "errMsg": "错误消息",
        "data": "新增的实体主键ID"
    }
}