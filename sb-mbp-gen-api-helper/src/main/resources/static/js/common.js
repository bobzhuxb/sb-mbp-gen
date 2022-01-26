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
// YAPI配置预览框
var yapiJsonDialog;
// 确认对话框
var confirmDialog;
// JSON导入对话框
var jsonImportDialog;
// JSON导入表单
var jsonImportForm;
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
        if (obj[item] != 0 && !obj[item]) {
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

function mapToObj(strMap) {
    let obj= Object.create(null);
    for (let[k,v] of strMap) {
        obj[k] = v;
    }
    return obj;
}

function formatJson(msg, tabSpaceCount) {
    var rep = "~";
    var jsonStr = JSON.stringify(msg, null, rep);
    var str = "";
    var tabSpace = "";
    for (var i = 0; i < tabSpaceCount; i++) {
        tabSpace += "&nbsp;";
    }
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
            jsonStr += tabSpace;
        } else {
            jsonStr += text;
        }
        if (i == str.length - 2) {
            jsonStr += "<br/>";
        }
    }
    return jsonStr;
}

function showLoading(elementTag, message) {
    var msg = message ? message : "";
    $("<div class=\"loading-mask\"></div>").css({
        display: "block", width: "100%",
        height: $(window).height(),
        'z-index': 1200
    }).appendTo(elementTag);
    $("<div class=\"loading-mask-msg\"></div>")
        .html(msg)
        .appendTo(elementTag).css({ display: "block", left: "48%", top: ($(window).height() - 45) / 2, 'z-index': 1200 });
}

function closeLoading() {
    $('.loading-mask').remove();
    $('.loading-mask-msg').remove();
}

function downLoadFile(options) {
    var config = $.extend(true, {
        method: 'post',
        isNewWinOpen: false,
        onLoad:function () {

        }
    }, options);
    var frameName = 'downloadFrame_' + new Date().getTime();
    var $iframe = $('<div style="display: none"><iframe name="' + frameName + '" src="about:blank"></iframe></div>');
    var $form = $('<form target="' + frameName + '" method="' + config.method + '" action="' + config.url + '"></form>');
    if (config.isNewWinOpen) {
        $form.attr("target", "_blank");
    }
    showLoading("body");
    $iframe.children().load(function () {
        closeLoading();
        try {
            var jsonStr = $(this).contents("body").text();
            var jsonObj;
            if(jsonStr != ""){
                jsonObj= JSON.parse(jsonStr);
                config.onLoad(jsonObj);
            }
        } catch (e) {

        }
        setTimeout(function () {
            $(this).parent().remove();
        }.bind(this), 3000);
    });
    /*拼接参数*/
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $("body").append($iframe);
    $form.submit();
}

var Snowflake = (function() {
    function Snowflake(_workerId, _dataCenterId, _sequence) {
        this.twepoch = 1288834974657n;
        //this.twepoch = 0n;
        this.workerIdBits = 5n;
        this.dataCenterIdBits = 5n;
        this.maxWrokerId = -1n ^ (-1n << this.workerIdBits); // 值为：31
        this.maxDataCenterId = -1n ^ (-1n << this.dataCenterIdBits); // 值为：31
        this.sequenceBits = 12n;
        this.workerIdShift = this.sequenceBits; // 值为：12
        this.dataCenterIdShift = this.sequenceBits + this.workerIdBits; // 值为：17
        this.timestampLeftShift = this.sequenceBits + this.workerIdBits + this.dataCenterIdBits; // 值为：22
        this.sequenceMask = -1n ^ (-1n << this.sequenceBits); // 值为：4095
        this.lastTimestamp = -1n;
        //设置默认值,从环境变量取
        this.workerId = 1n;
        this.dataCenterId = 1n;
        this.sequence = 0n;
        if (this.workerId > this.maxWrokerId || this.workerId < 0) {
            throw new Error('_workerId must max than 0 and small than maxWrokerId-[' + this.maxWrokerId + ']');
        }
        if (this.dataCenterId > this.maxDataCenterId || this.dataCenterId < 0) {
            throw new Error('_dataCenterId must max than 0 and small than maxDataCenterId-[' + this.maxDataCenterId + ']');
        }

        this.workerId = BigInt(_workerId);
        this.dataCenterId = BigInt(_dataCenterId);
        this.sequence = BigInt(_sequence);
    }
    Snowflake.prototype.tilNextMillis = function(lastTimestamp) {
        var timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return BigInt(timestamp);
    };
    Snowflake.prototype.timeGen = function() {
        return BigInt(Date.now());
    };
    Snowflake.prototype.nextId = function() {
        var timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new Error('Clock moved backwards. Refusing to generate id for ' +
                (this.lastTimestamp - timestamp));
        }
        if (this.lastTimestamp === timestamp) {
            this.sequence = (this.sequence + 1n) & this.sequenceMask;
            if (this.sequence === 0n) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0n;
        }
        this.lastTimestamp = timestamp;
        return ((timestamp - this.twepoch) << this.timestampLeftShift) |
            (this.dataCenterId << this.dataCenterIdShift) |
            (this.workerId << this.workerIdShift) |
            this.sequence;
    };
    return Snowflake;
}());

function toastSuccess(message) {
    $.Toast("成功", message, "success", {position_class: "toast-top-right"});
}

function toastError(message) {
    $.Toast("错误", message, "error", {position_class: "toast-top-right"});
}

function toastWarn(message) {
    $.Toast("警告", message, "warning", {position_class: "toast-top-right"});
}

function toastNotice(message) {
    $.Toast("通知", message, "notice", {position_class: "toast-top-right"});
}

function toastInfo(message) {
    $.Toast("信息", message, "info", {position_class: "toast-top-right"});
}