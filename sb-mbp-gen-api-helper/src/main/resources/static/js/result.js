// 从fromObject拖拽是是否包括子元素（业务意义上）
var fromChildInclude = true;

/**
 * 初始化当前页的事件
 */
function initResultTabEvents() {
    initFromLineLevel();
    initToDataLineLevel();
    fromLineEvent();
    toDataLineEvent();
    toInsertLineEvent();
    toNameTextEvent();
    toDescrTextEvent();
}

/**
 * 刷新结果数据
 */
function refreshResultData() {

}

/**
 * 初始化from层级
 */
function initFromLineLevel() {
    var $fromDivs = $("#fromObject div");
    for (var i = 0; i < $fromDivs.length; i++) {
        var fromDiv = $fromDivs[i];
        var level = $(fromDiv).attr("level");
        $(fromDiv).css("padding-left", (level * 10) + "px");
    }
}

/**
 * 初始化to层级
 */
function initToDataLineLevel() {
    var $toDataLines = $(".to-data-line");
    for (var i = 0; i < $toDataLines.length; i++) {
        var toDataLine = $toDataLines[i];
        var level = $(toDataLine).attr("level");
        $(toDataLine).css("padding-left", (level * 10) + "px");
    }
}

/**
 * 转换前行的触发事件
 */
function fromLineEvent() {
    $("#fromObject div").draggable({
        revert: true,
        revertDuration: 100,
    });
}

/**
 * 转换后行的触发事件
 */
function toDataLineEvent() {
    $(".to-data-line").draggable({
        revert: true,
        revertDuration: 100,
    });
}

/**
 * 转换后属性名更新事件
 */
function toNameTextEvent() {
    $(".to-name-text").keydown(function (e) {
        if (e.keyCode == 13) {
            toNameTextChanged(this);
        }
    });
    $(".to-name-text").blur(function () {
        toNameTextChanged(this);
    });
}

/**
 * 转换后属性名更新
 */
function toNameTextChanged(obj) {
    var $toNameDisplay = $(obj).parent().children(".to-name");
    $toNameDisplay.html($(obj).val());
    $toNameDisplay.show();
    $(obj).hide();
}

/**
 * 转换后描述更新事件
 */
function toDescrTextEvent() {
    $(".to-descr-text").keydown(function (e) {
        if (e.keyCode == 13) {
            toDescrTextChanged(this);
        }
    });
    $(".to-descr-text").blur(function () {
        toDescrTextChanged(this);
    });
}

/**
 * 转换后描述更新
 */
function toDescrTextChanged(obj) {
    var $toDescrDisplay = $(obj).parent().children(".to-descr");
    $toDescrDisplay.html($(obj).val());
    $toDescrDisplay.show();
    $(obj).hide();
}

/**
 * 转换后数据行的触发事件
 */
function toInsertLineEvent() {
    $(".to-insert-line").droppable({
        over: function(event) {
            $(this).addClass("over-to-line");
        },
        out: function(event) {
            $(this).removeClass("over-to-line");
        },
        drop: function(event, ui) {
            // 样式恢复
            $(this).removeClass("over-to-line");
            // 获取拖拽主体
            var $dragging = ui.helper;
            var parentId = $dragging.parent().attr("id");
            if (parentId == "fromObject") {
                // 从转换前字段拖拽而来
                fromLineDropped($dragging, $(this));
            } else {
                // 从转换后字段拖拽而来（上下移动）
                toLineDropped($dragging, $(this));
            }
        }
    });
}

/**
 * 从转换前字段拖拽
 */
function fromLineDropped($dragging, $toObj) {
    // 插入字段和空行
    var draggingLevel = $dragging.attr("level");
    var identify = $dragging.attr("identify");
    var type = $dragging.attr("type");
    var fullName = $dragging.attr("fullName");
    var descr = $dragging.attr("descr");
    var parent = $dragging.attr("parent");
    var draggingContent = $dragging.children("span[class='from-name']").html();
    // 第一层不存在父级
    if (typeof(parent) == "undefined") {
        parent = null;
    }
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "parent": parent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine($toObj, identify, mayModify, draggingLevel, fullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    parent = mayModify.parent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, identify, type, fullName, descr, parent, draggingContent);
    // 插入行之后的操作
    doAfterDroppedHtmlFormed();
    if (fromChildInclude) {
        // 子元素一并拖拽
        // 如果被拖拽的元素是object或list，必须带着所有子元素（业务意义上）一起拖拽
        if (type == "object" || type == "list") {
            // 递归调用，所以只调用拖拽下一层级的即可
            var children = $("#fromObject").children("div[parent='" + identify + "']");
            if (typeof(children) == "undefined") {
                // 没有子元素
                return;
            }
            for (var i = children.length - 1; i >= 0; i--) {
                var $child = $(children[i]);
                var $childToObj = $prevInsertLine;
                fromLineDropped($child, $childToObj);
            }
        }
    }
}

/**
 * 验证跟上一行dataLine同级的可能性（如果没有上一行dataLine，则取下一行dataLine，当前移动到第一行前面的情况）
 * $toObj表示的是insertLine行
 */
function validateFormLine($toObj, identify, mayModify, draggingLevel, fullName) {
    // $toObj的前一行
    var $prevDataLine = $toObj.prev();
    // $toObj的前一行是否为object或list
    var prevIsObjectOrList = false;
    if ($prevDataLine.length > 0) {
        var prevType = $prevDataLine.attr("type");
        if (prevType == "object" || prevType == "list") {
            prevIsObjectOrList = true;
        }
    }
    // 验证被拖拽的层级能否被提升或降低或变更
    // 验证逻辑（重点）：只要不跨越list（往上升级或往下降级），都是允许的
    // 最终要变成的level
    var finalLevel;
    // 最终要变成的fullName
    var finalFullName;
    // 最终要设置的parent
    var finalParent = null;
    if ($prevDataLine.length == 0) {
        // 移动到第一行
        finalLevel = 1;
        var lastDotIndex = fullName.lastIndexOf(".");
        // 当前fullName最后一个点后面的属性名
        if (lastDotIndex < 0) {
            // 当前原本是第一层
            finalFullName = fullName;
        } else {
            finalFullName = fullName.substring(lastDotIndex + 1);
        }
    } else {
        if (prevIsObjectOrList) {
            // 前一行的type是object或list
            // level比前一行多一层
            finalLevel = parseInt($prevDataLine.attr("level")) + 1;
            // parent就是前一行的identify
            finalParent = $prevDataLine.attr("identify");
            // 当前fullName最后一个点的位置
            var curLastDotIndex = fullName.lastIndexOf(".");
            // 当前fullName最后一个点后面的属性名
            var curLastName;
            if (curLastDotIndex < 0) {
                // 当前原本是第一层
                curLastName = fullName;
            } else {
                curLastName = fullName.substring(curLastDotIndex + 1);
            }
            // 前一行的fullName
            var prevFullName = $prevDataLine.attr("fullName");
            // 最终的fullName就是在前一行fullName的基础上，叠加上自己的lastName
            finalFullName = prevFullName + "." + curLastName;
        } else {
            // 前一行的type是normal（修改为与前一行相同）
            finalLevel = parseInt($prevDataLine.attr("level"));
            finalParent = $prevDataLine.attr("parent");
            // 当前fullName最后一个点的位置
            var curLastDotIndex = fullName.lastIndexOf(".");
            // 当前fullName最后一个点后面的属性名
            var curLastName;
            if (curLastDotIndex < 0) {
                // 当前原本是第一层
                curLastName = fullName;
            } else {
                curLastName = fullName.substring(curLastDotIndex + 1);
            }
            // 前一行的fullName
            var prevFullName = $prevDataLine.attr("fullName");
            // 前一行fullName最后一个点的位置
            var prevLastDotIndex = prevFullName.lastIndexOf(".");
            // 前一行fullName最后一个点前面的属性（包括最后一个点）
            var prevPrefix;
            if (prevLastDotIndex < 0) {
                // 前一行是第一层
                prevPrefix = "";
            } else {
                prevPrefix = prevFullName.substring(0, prevLastDotIndex + 1);
            }
            // 生成最终的fullName
            finalFullName = prevPrefix + curLastName;
        }
    }
    // 判断变更前后list层级是否相同
    var passList = passJsonList(identify, fullName, finalFullName);
    if (passList) {
        return false;
    }
    // 验证通过，设置level、fullName、parent属性
    mayModify.draggingLevel = finalLevel;
    mayModify.fullName = finalFullName;
    mayModify.parent = finalParent;
    return true;
}

/**
 * 根据fullName和finalFullName进行比较，判断变更前后list层级标识是否一致
 * 如果list层级不一致，则验证不通过
 * 前缀元素要么是object，要么是list
 * 过滤掉object元素，比较剩下的list元素，list元素的字段名、数目、顺序必须完全一致才可通过
 */
function passJsonList(identify, oriFullName, finalFullName) {
    // 当前fullName最后一个点的位置
    var oriLastDotIndex = oriFullName.lastIndexOf(".");
    // 当前fullName最后一个点前面的路径
    var oriFullPath;
    if (oriLastDotIndex < 0) {
        // 当前原本是第一层
        oriFullPath = "";
    } else {
        oriFullPath = oriFullName.substring(0, oriLastDotIndex);
    }
    // 当前finalFullName最后一个点的位置
    var finalLastDotIndex = finalFullName.lastIndexOf(".");
    // 当前finalFullName最后一个点前面的路径
    var finalFullPath;
    if (finalLastDotIndex < 0) {
        // 当前原本是第一层
        finalFullPath = "";
    } else {
        finalFullPath = finalFullName.substring(0, finalLastDotIndex);
    }
    var oriSubNames = oriFullPath.split(".");
    var finalSubNames = finalFullPath.split(".");
    // 只包含list元素的全路径
    var oriListFullPath = getListFullPath(identify, null);
    var finalListFullPath = getListFullPath(null, finalSubNames);
    if (oriListFullPath != finalListFullPath) {
        alert("不得跨越list层级");
        return true;
    }
    return false;
}

/**
 * 抽取全路径中list的路径
 * subNames表示全路径（不包括最后的字段名）以.切割的数组
 */
function getListFullPath(identify, subNames) {
    if (subNames != null && subNames.length == 0) {
        return "";
    }
    // 只包含list元素的全路径
    var listFullPath = "";
    // 外层div
    var $outerDiv;
    if (identify != null) {
        // fromObject的处理
        $outerDiv = $("#fromObject");
        // 接下来获取最原始的div元素的parent元素
        // 对应的最原始的div元素
        var $fromDiv = $($("#fromObject").children("div[identify='" + identify + "']")[0]);
        // 获取最原始的fullName
        var fromFullName = $fromDiv.attr("fullName");
        // 最原始的fullName最后一个点的位置
        var fromLastDotIndex = fromFullName.lastIndexOf(".");
        // 最原始的fullName最后一个点前面的路径
        var fromFullPath;
        if (fromLastDotIndex < 0) {
            // 最原始原本是第一层
            fromFullPath = "";
        } else {
            fromFullPath = fromFullName.substring(0, fromLastDotIndex);
        }
        subNames = fromFullPath.split(".");
        if (subNames.length == 0) {
            return "";
        }
    } else {
        // toObject的处理
        $outerDiv = $("#toObject");
    }
    // 验证list
    var subNameAdder = "";
    for (var i = 0; i < subNames.length; i++) {
        var subName = subNames[i];
        if (subNameAdder != "") {
            subNameAdder += ".";
        }
        subNameAdder += subName;
        // 验证每一层是否有list
        var parentObj = $outerDiv.find("div[fullName='" + subNameAdder + "']")[0];
        var parentType = $(parentObj).attr("type");
        if (parentType == "list") {
            if (listFullPath != "") {
                listFullPath += ".";
            }
            listFullPath += subName;
        }
    }
    return listFullPath;
}

/**
 * 生成数据行和插入行
 */
function formDataLineAndInsertLine($toObj, draggingLevel, identify, type, fullName, descr, parent, draggingContent) {
    // 字段行HTML
    var dataLineHtml = "<div"
        + " identify='" + identify + "'"
        + " level='" + draggingLevel + "'"
        + " type='" + type + "'"
        + " title='" + fullName + "'"
        + " fullName='" + fullName + "'";
    if (parent != null) {
        // 第一层级不存在parent属性
        dataLineHtml += " parent='" + parent + "'";
    }
    dataLineHtml += " class='to-data-line";
    if (type == "object") {
        // object的特殊样式
        dataLineHtml += " data-is-object";
    } else if (type == "list") {
        // list的特殊样式
        dataLineHtml += " data-is-list";
    }
    dataLineHtml += "'>"
        + "<span class='to-name' ondblclick='changeToName(this);'>" + draggingContent + "</span>"
        + "<input type='text' class='to-name-text' style='width:200px;display:none;' value='" + draggingContent + "' />"
        + " | "
        + "<span class='to-descr' ondblclick='changeToDescr(this);'>" + descr + "</span>"
        + "<input type='text' class='to-descr-text' style='width:200px;display:none;' value='" + descr + "' />"
        + " | "
        + "<span class='to-level'>（" + draggingLevel + "）</span>"
        + "<span style='margin-left: 50px;' title='点击删除' onclick='deleteToLine(this);'>X</span>"
        + "</div>";
    // 准备插入行
    var $newDataLine = $(dataLineHtml);
    var $newInsertLine = $("<div class=\"to-insert-line\"></div>");
    $toObj.after($newInsertLine);
    $toObj.after($newDataLine);
    return $newInsertLine;
}

/**
 * 删除拖拽后的数据行（级联删除）
 */
function deleteToLine(obj, needConfirm) {
    var $dataLineDiv = $(obj).parent();
    if (needConfirm) {
        $.confirm({
            confirm: function () {
                directDeleteToLine($dataLineDiv);
            }
        });
    } else {
        directDeleteToLine($dataLineDiv);
    }
}

/**
 * 删除拖拽后的数据行（级联删除）
 */
function directDeleteToLine($obj) {
    var identify = $obj.attr("identify");
    var children = $("#toObject").children("div[parent='" + identify + "']");
    if (typeof(children) != "undefined") {
        // 有子元素的情况下，先删子元素
        for (var i = 0; i < children.length; i++) {
            directDeleteToLine($(children[i]));
        }
    }
    // 删除自身即下一个insert行
    $obj.next().remove();
    $obj.remove();
}

/**
 * 修改转换后属性名操作
 */
function changeToName(obj) {
    $(obj).parent().children(".to-name").hide();
    $(obj).parent().children(".to-name-text").show();
    $(obj).parent().children(".to-name-text").focus();
    $(obj).parent().children(".to-name-text").select();
}

/**
 * 修改转换后描述操作
 */
function changeToDescr(obj) {
    $(obj).parent().children(".to-descr").hide();
    $(obj).parent().children(".to-descr-text").show();
    $(obj).parent().children(".to-descr-text").focus();
    $(obj).parent().children(".to-descr-text").select();
}

/**
 * 从转换后字段拖拽（上下移动）
 */
function toLineDropped($dragging, $toObj) {
    var draggingLevel = $dragging.attr("level");
    var identify = $dragging.attr("identify");
    var type = $dragging.attr("type");
    var fullName = $dragging.attr("fullName");
    var descr = $dragging.children(".to-descr").html();
    var parent = $dragging.attr("parent");
    var draggingContent = $dragging.children(".to-name").html();
    // 第一层不存在父级
    if (typeof(parent) == "undefined") {
        parent = null;
    }
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "parent": parent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine($toObj, identify, mayModify, draggingLevel, fullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    parent = mayModify.parent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, identify, type, fullName, descr, parent, draggingContent);
    // 删除原先行及其下一行
    $dragging.next().remove();
    $dragging.remove();
    // 插入行之后的操作
    doAfterDroppedHtmlFormed();
    // 如果被拖拽的元素是object或list，必须带着所有子元素（业务意义上）一起拖拽
    if (type == "object" || type == "list") {
        // 递归调用，所以只调用拖拽下一层级的即可
        var children = $("#toObject").children("div[parent='" + identify + "']");
        if (typeof(children) == "undefined") {
            // 没有子元素
            return;
        }
        for (var i = children.length - 1; i >= 0; i--) {
            var $child = $(children[i]);
            var $childToObj = $prevInsertLine;
            toLineDropped($child, $childToObj);
        }
    }
}

/**
 * 拖拽结束且HTML生成后的操作
 */
function doAfterDroppedHtmlFormed() {
    toDataLineEvent();
    toInsertLineEvent();
    initToDataLineLevel();
    toNameTextEvent();
    toDescrTextEvent();
}