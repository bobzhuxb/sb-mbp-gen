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
    // 添加补充数据
    if (interfaceData.associationNameList != null && interfaceData.associationNameList.length > 0) {
        for (var i = 0; i < interfaceData.associationNameList.length; i++) {
            subMainAddLine(document.getElementById("associationNameAddBtn"), 'associationNameToAdd');
        }
        var lines = $("div[idfrom='associationNameToAdd']").find("input[name='associationNameList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(interfaceData.associationNameList[i]);
        }
    }
    if (interfaceData.dictionaryNameList != null && interfaceData.dictionaryNameList.length > 0) {
        for (var i = 0; i < interfaceData.dictionaryNameList.length; i++) {
            subMainAddLine(document.getElementById("dictionaryNameAddBtn"), 'dictionaryNameToAdd');
        }
        var lines = $("div[idfrom='dictionaryNameToAdd']").find("input[name='dictionaryNameList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(interfaceData.dictionaryNameList[i]);
        }
    }
    $("#paramInfo").find("input[name='orderBy']").val(interfaceData.param.orderBy);
    if (interfaceData.sqlColumnList != null && interfaceData.sqlColumnList.length > 0) {
        for (var i = 0; i < interfaceData.sqlColumnList.length; i++) {
            subMainAddLine(document.getElementById("sqlColumnAddBtn"), 'sqlColumnToAdd');
        }
        var lines = $("div[idfrom='sqlColumnToAdd']").find("input[name='sqlColumnList']");
        for (var i = 0; i < lines.length; i++) {
            $(lines[i]).val(interfaceData.sqlColumnList[i]);
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