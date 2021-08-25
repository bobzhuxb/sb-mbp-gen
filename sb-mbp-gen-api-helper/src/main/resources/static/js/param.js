/**
 * 初始化当前页的事件
 */
function initParamTabEvents() {

}

/**
 * 刷新参数数据
 */
function refreshParamData() {

}

/**
 * 添加sqlColumn
 */
function subMainAddLine(obj, toAddDomId) {
    var $toAdd = $("#" + toAddDomId).clone();
    $toAdd.removeAttr("id");
    $(obj).parent().append($toAdd);
    $toAdd.show();
}

/**
 * 删除sqlColumn
 */
function removeSubMainLine(obj) {
    $(obj).parent().parent().remove();
}