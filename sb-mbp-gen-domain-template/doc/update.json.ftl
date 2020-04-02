{
    "httpMethod": "PUT",
    "httpUrl": "${urlPrefix}/${entityUrl}",
    "interDescr": "修改${entityComment}",
    "param": {
        "jsonBody": {
            "id": "主键ID（无引号）",
<#list fieldList as field>
<#if field.camelName != 'insertTime' && field.camelName != 'insertUserId' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>
            "${field.camelName}": "${field.comment}<#if field.javaType != 'String'>（无引号）</#if>",
</#if>
</#list>
<#list toFromList as toFrom>
            "${toFrom.toFromEntityName}Id": "${toFrom.toFromComment}ID",
</#list>
<#list fromToList as fromTo>
            "${fromTo.fromToEntityName}<#if fromTo.relationType == 'OneToMany'>List</#if>": <#if fromTo.relationType == 'OneToMany'>[</#if>{"说明": "${fromTo.fromToComment}<#if fromTo.relationType == 'OneToMany'>列表</#if>（级联类型：${fromTo.fromToEntityType}）"}<#if fromTo.relationType == 'OneToMany'>]</#if>,
</#list>
            "xxxxxxx": "xxxxxxx"
        }
    },
    "result":{
        "resultCode": "1：操作成功  2：操作失败",
        "errMsg": "错误消息",
        "data": "修改的实体主键ID"
    }
}