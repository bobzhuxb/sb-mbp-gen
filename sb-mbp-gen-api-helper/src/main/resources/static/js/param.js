/**
 * 初始化当前页的事件
 */
function initParamTabEvents() {

}

/**
 * 刷新参数数据
 */
function refreshParamData() {
    // 先清空数据
    $("#paramInfo").find(".copied").remove();
    $("#paramInfo").find("input").val("");
    if (interfaceSelected == null) {
        return;
    }
    // 获取JSON数据
    var interfaceData = JSON.parse(interfaceSelected.dataJson);
    var param = interfaceData.param;
    // 添加补充数据
    // 1、条件
    if (param.criteriaList != null && param.criteriaList.length > 0) {
        for (var i = 0; i < param.criteriaList.length; i++) {
            subMainAddLine(document.getElementById("criteriaAddBtn"), 'criteriaToAdd');
        }
        var blocks = $("div[idfrom='criteriaToAdd']");
        for (var i = 0; i < blocks.length; i++) {
            var criteria = param.criteriaList[i];
            $(blocks[i]).find("input[name='fromParam']").val(criteria.fromParam);
            $(blocks[i]).find("input[name='descr']").val(criteria.descr);
            $(blocks[i]).find("input[name='fixedValue']").val(criteria.fixedValue);
            if (criteria.fixedValueIsDigit) {
                $(blocks[i]).find("input[name='fixedValueIsDigit']").prop("checked",true);
            }
            $(blocks[i]).find("select[name='emptyToNull']").val(criteria.emptyToNull);
            var toCriteriaLines = $(blocks[i]).find("div[idfrom='toCriteriaToAdd']");
            for (var j = 0; j < toCriteriaLines.length; j++) {
                $(toCriteriaLines[j]).val(criteria.toCriteriaList[j]);
            }
        }
    }
    // 2、级联
    if (param.associationNameList != null && param.associationNameList.length > 0) {
        for (var i = 0; i < param.associationNameList.length; i++) {
            subMainAddLine(document.getElementById("associationNameAddBtn"), 'associationNameToAdd');
        }
        var lines = $("div[idfrom='associationNameToAdd']").find("input[name='associationNameList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(param.associationNameList[i]);
        }
    }
    // 3、数据字典
    if (param.dictionaryNameList != null && param.dictionaryNameList.length > 0) {
        for (var i = 0; i < param.dictionaryNameList.length; i++) {
            subMainAddLine(document.getElementById("dictionaryNameAddBtn"), 'dictionaryNameToAdd');
        }
        var lines = $("div[idfrom='dictionaryNameToAdd']").find("input[name='dictionaryNameList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(param.dictionaryNameList[i]);
        }
    }
    // 4、排序
    $("#paramInfo").find("input[name='orderBy']").val(param.orderBy);
    // 5、SQL列
    if (param.sqlColumnList != null && param.sqlColumnList.length > 0) {
        for (var i = 0; i < param.sqlColumnList.length; i++) {
            subMainAddLine(document.getElementById("sqlColumnAddBtn"), 'sqlColumnToAdd');
        }
        var lines = $("div[idfrom='sqlColumnToAdd']").find("input[name='sqlColumnList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(param.sqlColumnList[i]);
        }
    }
}

/**
 * 复制一行
 */
function subMainAddLine(obj, toAddDomId) {
    var $toAdd = $("#" + toAddDomId).clone();
    $toAdd.removeAttr("id");
    $toAdd.removeClass("template");
    $toAdd.attr("idFrom", toAddDomId);
    $toAdd.addClass("copied");
    $(obj).parent().append($toAdd);
}

/**
 * 删除sqlColumn
 */
function removeSubMainLine(obj) {
    $(obj).parent().parent().remove();
}