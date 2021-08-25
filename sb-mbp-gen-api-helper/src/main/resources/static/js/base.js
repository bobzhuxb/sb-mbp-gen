/**
 * 初始化当前页的事件
 */
function initBaseTabEvents() {

}

/**
 * 刷新基本数据
 */
function refreshBaseData() {
    if (interfaceSelected == null) {
        $("#baseInfo").find("input").val("");
        $("#baseInfo").find("select").val("");
        return;
    }
    $("input[name='interNo']").val(interfaceSelected.interNo);
    $("input[name='httpMethod']").val(interfaceSelected.httpMethod);
    $("input[name='addDefaultPrefix']").val(interfaceSelected.addDefaultPrefix);
    $("input[name='httpUrl']").val(interfaceSelected.httpUrl);
    $("input[name='interDescr']").val(interfaceSelected.interDescr);
}