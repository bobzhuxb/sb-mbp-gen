// 当前选中的项目
var projectSelected = null;
// 当前选中的接口
var interfaceSelected = null;
// 工程新增对话框
var projectDialog;
// 工程新增表单
var projectAddForm;
// 是否正在新增接口中
var addingInterface = false;

/**
 * 空值转null
 */
function emptyStringToNull(data) {
    if (typeof(data) == "undefined" || data == null || data.trim() == "") {
        return null;
    }
    return data;
}

// form转json
$.fn.toJSON = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};