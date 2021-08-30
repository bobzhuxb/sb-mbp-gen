// 当前选中的项目
var projectSelected = null;
// 当前选中的接口
var interfaceSelected = null;
// 当前的返回类型
var returnTypeName = null;
// 工程新增对话框
var projectDialog;
// 工程新增表单
var projectAddForm;
// 类文件上传对话框
var uploadClassDialog;
// 类文件上传表单
var uploadClassForm;
// 接口JSON预览框
var interJsonDialog;
// 确认对话框
var confirmDialog;
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

/**
 * 移除空值属性
 */
function removeNullProperty(obj, excludeProList) {
    Object.keys(obj).forEach(item => {
        if (typeof(excludeProList) != "undefined" && excludeProList.includes(item)) {
            return;
        }
        if (!obj[item]) {
            delete obj[item];
        }
    });
    return obj;
}

/**
 * 产生随机UUID
 * @returns {string}
 */
function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
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

function formatJson(msg) {
    var rep = "~";
    var jsonStr = JSON.stringify(msg, null, rep);
    var str = "";
    for (var i = 0; i < jsonStr.length; i++) {
        var text2 = jsonStr.charAt(i);
        if (i > 1) {
            var text = jsonStr.charAt(i - 1);
            if (rep != text && rep == text2) {
                str += "<br/>";
            }
        }
        str += text2;
    }
    jsonStr = "";
    for (var i = 0; i < str.length; i++) {
        var text = str.charAt(i);
        if (rep == text) {
            jsonStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        } else {
            jsonStr += text;
        }
        if (i == str.length - 2) {
            jsonStr += "<br/>";
        }
    }
    return jsonStr;
}