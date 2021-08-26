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