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
        $("select[name='httpMethod'] option:first").prop("selected", "selected");
        $("select[name='addDefaultPrefix'] option:first").prop("selected", "selected");
        return;
    }
    $("input[name='interNo']").val(interfaceSelected.interNo);
    $("select[name='httpMethod']").val(interfaceSelected.httpMethod);
    $("select[name='addDefaultPrefix']").val(interfaceSelected.addDefaultPrefix);
    $("input[name='httpUrl']").val(interfaceSelected.httpUrl);
    $("input[name='interDescr']").val(interfaceSelected.interDescr);
    $("input[name='yapiTags']").val(interfaceSelected.yapiTags);
}