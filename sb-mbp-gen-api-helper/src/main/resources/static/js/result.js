// 从fromObject拖拽是是否包括子元素（业务意义上）
var fromChildInclude = true;

/**
 * 初始化当前页的事件
 */
function initResultTabEvents() {
    $(".compare-div").height($(document).height() - 180);
    initToDataLineLevel();
    toDataLineEvent();
    toInsertLineEvent();
    toNameTextEvent();
    toDescrTextEvent();
}

/**
 * 刷新fromLine之后的操作
 */
function doAfterFromLineRefreshed() {
    initFromLineLevel();
    fromLineEvent();
}

/**
 * 刷新toLine之后的操作
 */
function doAfterToLineRefreshed() {
    initToDataLineLevel();
}

/**
 * 初始化返回类型搜索框
 */
function initResultSearch() {
    var classCodeList = projectSelected.ahClassCodeList;
    var basePackage = projectSelected.basePackage;
    if (classCodeList != null && classCodeList.length > 0) {
        var resultSearchDataList = new Array();
        for (var i = 0; i < classCodeList.length; i++) {
            var classCode = classCodeList[i];
            var packageName = classCode.packageName;
            if (!packageName.startsWith(basePackage)) {
                continue;
            }
            var className = classCode.className;
            var searchData = packageName.substring(basePackage.length + 1) + "." + className;
            resultSearchDataList.push(searchData);
        }
        $("#resultSearch").autocomplete({
            source: resultSearchDataList
        });
    }
}

/**
 * 加载结果类到fromObject和toObject
 */
function loadReturnClassToResult() {
    // fromObject加载
    var searchFullClassName = $("#resultBasePackage").html() + $("#resultSearch").val();
    for (var i = 0; i < projectSelected.ahClassCodeList.length; i++) {
        var ahClassCode = projectSelected.ahClassCodeList[i];
        var curFullClassName = ahClassCode.packageName + "." + ahClassCode.className;
        if (curFullClassName == searchFullClassName) {
            // 初始化左侧列表
            var ahClassCodeFull = getClassCode(ahClassCode.id);
            var fromLinesHtml = formFromLines(ahClassCodeFull, 1, "", "");
            // 刷新来源行
            $("#fromObject").html(fromLinesHtml);
            doAfterFromLineRefreshed();
            break;
        }
    }
    // toObject加载
    var interfaceData = JSON.parse(interfaceSelected.dataJson);
    var fieldList = interfaceData.result.fieldList;
    if (fieldList != null && fieldList.length > 0) {
        // 给每个field添加一个标识
        for (var i = 0; i < fieldList.length; i++) {
            var field = fieldList[i];
            // 生成相关属性
            var toIdentify = generateUUID();
            field.identify = toIdentify;
        }
        // 对fieldList重新排序，符合父子结构
        var sortedFieldList = new Array();
        // 从第1层级开始处理
        var result = sortFieldList(searchFullClassName, fieldList, sortedFieldList, 1);
        if (!result || sortedFieldList == null || sortedFieldList.length == 0) {
            // 上一步解析发生错误
            return;
        }
        // 生成一行行的数据行和插入行
        for (var i = 0; i < sortedFieldList.length; i++) {
            var field = sortedFieldList[i];
            // 生成相关属性
            var toIdentify = field.identify;
            // 字段类别（normal、object、list）
            var type = field.type;
            if (type == null) {
                type = "normal";
            }
            // 来源全路径名
            var fullName = field.fromName;
            var splitFullNameArr = fullName.split(".");
            // 层级
            var level = splitFullNameArr.length;
            // 转换后名称
            var nameContent = field.name;
            // 转换后描述
            var descr = field.descr;
            // 父级的类（上一步骤已设置）
            var toParent = field.toParent;
            // 该字段的实际数据类型名（上一步骤已设置）
            var fieldRealTypeName = field.fieldRealTypeName;
            // 字段行HTML
            var dataLineHtml = "<div"
                + " identify='" + toIdentify + "'"
                + " level='" + level + "'"
                + " type='" + type + "'"
                + " fieldRealTypeName=' " + fieldRealTypeName + "'"
                + " title='来源全路径：" + fullName + "\n来源类型：" + fieldRealTypeName + "'"
                + " fullName='" + fullName + "'";
            if (toParent != null) {
                // 第一层级不存在parent属性
                dataLineHtml += " parent='" + toParent + "'";
            }
            dataLineHtml += " class='to-data-line";
            if (type == "object") {
                // object的特殊样式
                dataLineHtml += " data-is-object";
            } else if (type == "list") {
                // list的特殊样式
                dataLineHtml += " data-is-list";
            }
            dataLineHtml += "'><span class='fold-span'></span> "
                + "<span class='to-name' ondblclick='changeToName(this);'>" + nameContent + "</span>"
                + "<input type='text' class='to-name-text' style='width:200px;display:none;' value='" + nameContent + "' />"
                + " | "
                + "<span class='to-descr' ondblclick='changeToDescr(this);'>" + descr + "</span>"
                + "<input type='text' class='to-descr-text' style='width:200px;display:none;' value='" + descr + "' />"
                + " | "
                + "<span class='to-level'>（" + level + "）</span>"
                + "<span style='margin-left: 30px;' title='点击删除' onclick='openDeleteToLineDialog(this);'>X</span>"
                + "</div>";
            // 准备插入行
            var $newDataLine = $(dataLineHtml);
            var $newInsertLine = $("<div class=\"to-insert-line\"></div>");
            $("#toObject").append($newInsertLine);
            $("#toObject").append($newDataLine);
        }
        // 插入行之后的操作
        doAfterDroppedHtmlFormed();
        refreshToObjectLines();
    }
}

/**
 * 对fieldList重新排序，符合父子结构
 */
function sortFieldList(nowReturnTypeName, fieldList, sortedFieldList, processingLevel) {
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        // 来源全路径名
        var fullName = field.fromName;
        var splitFullNameArr = fullName.split(".");
        // 层级
        var level = splitFullNameArr.length;
        if (level != processingLevel) {
            // 过滤掉不是当前处理层级的
            continue;
        }
        // 根据fullName查找fieldRealTypeName
        field.fieldRealTypeName = getFieldRealTypeName(fullName, nowReturnTypeName);
        if (field.fieldRealTypeName == null) {
            return false;
        }
        if (processingLevel == 1) {
            // 第一层的parent为null
            field.toParent = null;
            // 第一层直接添加
            sortedFieldList.push(field);
        } else {
            // 其他层级
            var parentFromName = fullName.substring(0, fullName.lastIndexOf(".") - 1);
            for (var j = 0; j < fieldList.length; j++) {
                var findParentField = fieldList[i];
                var findParentFieldFromName = findParentField.fromName;
                if (findParentFieldFromName == parentFromName) {
                    // 找到对应的parent了
                    // 设置当前的parent的identify
                    field.toParent = findParentField.identify;
                    // 在当前的parentField后面追加
                    for (var k = 0; k < sortedFieldList.length; k++) {
                        var sortedField = sortedFieldList[k];
                        if (sortedField.identify = findParentField.identify) {
                            // 找到的指定位置为k
                            break;
                        }
                    }
                    sortedFieldList.splice(k + 1, 0, field);
                    break;
                }
            }
        }
        // 标记该field已处理完
        field.processed = true;
    }
    // 每次处理完一个层级之后，都验证一下是否全部处理完成
    var allProcessed = true;
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        if (!field.processed) {
            allProcessed = false;
            break;
        }
    }
    // 未处理完成，则继续处理下一层级，处理完成则结束
    if (!allProcessed) {
        var result = sortFieldList(fieldList, sortedFieldList, processingLevel + 1);
        if (!result) {
            return false;
        }
    }
    // 处理完成
    return true;
}

/**
 * 根据全路径名获取field类型全称
 */
function getFieldRealTypeName(fullPathName, nowClassTypeName) {
    var fullPathNameArr = fullPathName.split(".");
    var fieldRealTypeName = getFieldRealTypeNameByArr(fullPathNameArr, 0, nowClassTypeName);
    return fieldRealTypeName;
}

/**
 * 递归调用获取field类型全称
 */
function getFieldRealTypeNameByArr(fullPathNameArr, index, nowClassTypeName) {
    // 找到对应的classCode
    var classCodeList = projectSelected.ahClassCodeList;
    var nowClassCode = null;
    for (var i = 0; i < classCodeList.length; i++) {
        var classCode = classCodeList[i];
        var classFullName = classCode.packageName + "." + classCode.className;
        if (classFullName == nowClassTypeName) {
            nowClassCode = classCode;
            break;
        }
    }
    if (nowClassCode == null) {
        // 找不到对应的类
        return null;
    }
    // 查找fieldList
    var nowClassCodeFull = getClassCode(nowClassCode.id);
    var fieldList = nowClassCodeFull.ahFieldList;
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        // 字段名称
        var fieldName = field.fieldName;
        // 获取field的实际类型
        var typeName = field.typeName;
        var fieldRealTypeName = typeName;
        if (fieldRealTypeName == "java.util.List") {
            fieldRealTypeName = field.genericTypeName;
        }
        if (fullPathNameArr[index] == fieldName) {
            // 找到了对应的字段
            if (index == fullPathNameArr.length - 1) {
                // 最后一层
                return fieldRealTypeName;
            } else {
                // 其他层，递归调用
                return getFieldRealTypeNameByArr(fullPathNameArr, index + 1, fieldRealTypeName);
            }
        }
    }
    // 未找到对应的字段
    return null;
}

/**
 * 生成来源数据行
 */
function formFromLines(ahClassCode, level, parentIdentify, parentFullName) {
    var basePackage = projectSelected.basePackage;
    var fromLinesHtml = "";
    var fieldList = ahClassCode.ahFieldList;
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        var identify = field.id;
        var fieldTypeName = field.typeName;
        var fieldName = field.fieldName;
        var genericTypeName = field.genericTypeName;
        if (fieldName.startsWith("_")) {
            // 该字段是为了匹配数据库使用的，过滤掉
            continue;
        }
        var isObject = false;
        var isArray = false;
        if (fieldTypeName == "java.util.List") {
            isArray = true;
        } else if (fieldTypeName.startsWith(basePackage + ".")) {
            isObject = true;
        }
        var fullName = fieldName;
        if (parentFullName != "") {
            fullName = parentFullName + "." + fullName;
        }
        // 组装HTML
        fromLinesHtml += "<div identify='" + identify + "' level='" + level + "'";
        if (isArray) {
            fromLinesHtml += " type='list'" + " genericTypeName='" + genericTypeName
                + "' class='from-data-line data-is-list'";
        } else if (isObject) {
            fromLinesHtml += " type='object' class='from-data-line data-is-object'";
        } else {
            fromLinesHtml += " type='normal' class='from-data-line'";
        }
        fromLinesHtml += " fullName='" + fullName + "' title='全路径：" + fullName + "\n类型：" + fieldTypeName
            + "' descr='（请填写注释）' fieldType='" + fieldTypeName + "' parent='" + parentIdentify + "'>";
        if (isArray || isObject) {
            fromLinesHtml += "<span class='fold-unfold' onclick='foldOrUnfoldFrom(this, false);' title='点击展开'>+</span> ";
        } else {
            fromLinesHtml += "<span class='no-fold'></span> ";
        }
        fromLinesHtml += "<span class='from-name'>" + fieldName + "</span> | （请填写注释）</div>";
    }
    return fromLinesHtml;
}

/**
 * fromObject：展开或关闭
 */
function foldOrUnfoldFrom(obj, forceUnfold) {
    var myIdentify = $(obj).parent().attr("identify");
    var myFullName = $(obj).parent().attr("fullName");
    // 子目录
    var $childrenDivs = $(".from-data-line[parent='" + myIdentify + "']");
    if (!forceUnfold && $(obj).html() == "+") {
        // 准备展开（只展开下一层）
        if (typeof($childrenDivs) != "undefined" && $childrenDivs.length > 0) {
            // 已加载过，直接显示
            $childrenDivs.show();
        } else {
            // 未加载过，调用接口获取数据加载
            var type = $(obj).parent().attr("type");
            var loadingTypeName;
            if (type == "object") {
                loadingTypeName = $(obj).parent().attr("fieldType");
            } else if (type == "list") {
                loadingTypeName = $(obj).parent().attr("genericTypeName");
            } else {
                // 无需加载
                return;
            }
            var ahClassCodeFull = getClassCodeByFullName(loadingTypeName);
            var myLevel = $(obj).parent().attr("level");
            var nextLevel = parseInt(myLevel) + 1;
            var fromLinesHtml = formFromLines(ahClassCodeFull, nextLevel, myIdentify, myFullName);
            // 刷新来源行
            $(obj).parent().after(fromLinesHtml);
        }
        $(obj).attr("title", "点击收起");
        $(obj).html("-");
        doAfterFromLineRefreshed();
    } else {
        // 准备合起
        // 先对子目录进行合起操作
        for (var i = 0; i < $childrenDivs.length; i++) {
            var childDiv = $childrenDivs[i];
            var childType = $(childDiv).attr("type");
            if (childType != "normal") {
                var childSpan = $(childDiv).find(".fold-unfold");
                if (childSpan.html() == "-") {
                    foldOrUnfoldFrom(childSpan[0], true);
                }
            }
            // 再对自己进行合起操作
            $(childDiv).hide();
        }
        // 再对自己进行合起操作
        $(obj).attr("title", "点击展开");
        $(obj).html("+");
    }
}

/**
 * 刷新结果数据
 */
function refreshResultData() {
    var basePackage = projectSelected.basePackage;
    $("#fromObject").html("");
    $("#toObject").html("<div class='to-insert-line'></div>");
    if (interfaceSelected == null) {
        $("#resultSearch").val("");
        return;
    }
    var returnTypeName = interfaceSelected.returnTypeName;
    if (returnTypeName == null || !returnTypeName.startsWith(basePackage + ".")) {
        return;
    }
    // 设置returnType搜索内容
    var searchWord = returnTypeName.substring(basePackage.length + 1);
    $("#resultSearch").val(searchWord);
    // 加载结果类到fromObject和toObject
    loadReturnClassToResult();
    // 初始化结果页事件
    initResultTabEvents();
}

/**
 * 初始化from层级
 */
function initFromLineLevel() {
    var $fromDivs = $("#fromObject div");
    for (var i = 0; i < $fromDivs.length; i++) {
        var fromDiv = $fromDivs[i];
        var level = $(fromDiv).attr("level");
        $(fromDiv).css("padding-left", ((level - 1) * 20) + "px");
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
        $(toDataLine).css("padding-left", ((level - 1) * 20) + "px");
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
            // 插入行之后的操作
            doAfterDroppedHtmlFormed();
            refreshToObjectLines();
        }
    });
}

/**
 * 从转换前字段拖拽
 */
function fromLineDropped($dragging, $toObj) {
    // 插入字段和空行
    var draggingLevel = $dragging.attr("level");
    var fromIdentify = $dragging.attr("identify");
    var type = $dragging.attr("type");
    var fullName = $dragging.attr("fullName");
    var descr = $dragging.attr("descr");
    var fromParent = $dragging.attr("parent");
    var draggingContent = $dragging.children("span[class='from-name']").html();
    var fieldType = $dragging.attr("fieldType");
    var genericTypeName = $dragging.attr("genericTypeName");
    // toObject的相关属性生成
    var toIdentify = generateUUID();
    var toParent = null;
    // 分析该字段的实际类型
    var fieldRealTypeName = fieldType;
    if (fieldType == "java.util.List") {
        fieldRealTypeName = genericTypeName;
    }
    // 第一层不存在父级
    if (typeof(fromParent) == "undefined") {
        fromParent = null;
    }
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "toParent": toParent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine("fromObject", $toObj, fromIdentify, mayModify, draggingLevel, fullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    toParent = mayModify.toParent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, toIdentify, type, fieldRealTypeName,
        fullName, descr, toParent, draggingContent);
    if (fromChildInclude) {
        // 子元素一并拖拽
        // 如果被拖拽的元素是object或list，必须带着所有子元素（业务意义上）一起拖拽
        if (type == "object" || type == "list") {
            // 递归调用，所以只调用拖拽下一层级的即可
            var children = $("#fromObject").children("div[parent='" + fromIdentify + "']");
            if (typeof(children) == "undefined") {
                // 没有子元素
                return;
            }
            for (var i = children.length - 1; i >= 0; i--) {
                var $child = $(children[i]);
                var $childToObj = $prevInsertLine;
                // 继续拖拽子目录（非折叠的）
                if ($child.css("display") != "none") {
                    fromLineDropped($child, $childToObj);
                }
            }
        }
    }
}

/**
 * 验证跟上一行dataLine同级的可能性（如果没有上一行dataLine，则取下一行dataLine，当前移动到第一行前面的情况）
 * $toObj表示的是insertLine行
 * 来源行可能是从fromObject或toObject而来
 */
function validateFormLine(fromDivDomId, $toObj, fromIdentify, mayModify, draggingLevel, fullName) {
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
    var passList = passJsonList(fromDivDomId, fromIdentify, fullName, finalFullName);
    if (passList) {
        return false;
    }
    // 验证通过，设置level、fullName、parent属性
    mayModify.draggingLevel = finalLevel;
    mayModify.fullName = finalFullName;
    mayModify.toParent = finalParent;
    return true;
}

/**
 * 根据fullName和finalFullName进行比较，判断变更前后list层级标识是否一致
 * 如果list层级不一致，则验证不通过
 * 前缀元素要么是object，要么是list
 * 过滤掉object元素，比较剩下的list元素，list元素的字段名、数目、顺序必须完全一致才可通过
 */
function passJsonList(fromDivDomId, fromIdentify, oriFullName, finalFullName) {
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
    var oriListFullPath = getListFullPath(fromDivDomId, fromIdentify, null);
    var finalListFullPath = getListFullPath(fromDivDomId, null, finalSubNames);
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
function getListFullPath(fromDivDomId, fromIdentify, subNames) {
    if (subNames != null && subNames.length == 0) {
        return "";
    }
    // 只包含list元素的全路径
    var listFullPath = "";
    // 外层div
    var $outerDiv;
    if (fromIdentify != null) {
        // fromObject的处理
        $outerDiv = $("#fromObject");
        // 接下来获取最原始的div元素的parent元素
        // 对应的最原始的div元素
        var $fromDiv = $($("#" + fromDivDomId).children("div[identify='" + fromIdentify + "']")[0]);
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
 * 生成toObject的数据行和插入行
 */
function formDataLineAndInsertLine($toObj, draggingLevel, toIdentify, type, fieldRealTypeName, fullName,
                                   descr, toParent, draggingContent) {
    // 字段行HTML
    var dataLineHtml = "<div"
        + " identify='" + toIdentify + "'"
        + " level='" + draggingLevel + "'"
        + " type='" + type + "'"
        + " fieldRealTypeName=' " + fieldRealTypeName + "'"
        + " title='来源全路径：" + fullName + "\n来源类型：" + fieldRealTypeName + "'"
        + " fullName='" + fullName + "'";
    if (toParent != null) {
        // 第一层级不存在parent属性
        dataLineHtml += " parent='" + toParent + "'";
    }
    dataLineHtml += " class='to-data-line";
    if (type == "object") {
        // object的特殊样式
        dataLineHtml += " data-is-object";
    } else if (type == "list") {
        // list的特殊样式
        dataLineHtml += " data-is-list";
    }
    dataLineHtml += "'><span class='fold-span'></span> "
        + "<span class='to-name' ondblclick='changeToName(this);'>" + draggingContent + "</span>"
        + "<input type='text' class='to-name-text' style='width:200px;display:none;' value='" + draggingContent + "' />"
        + " | "
        + "<span class='to-descr' ondblclick='changeToDescr(this);'>" + descr + "</span>"
        + "<input type='text' class='to-descr-text' style='width:200px;display:none;' value='" + descr + "' />"
        + " | "
        + "<span class='to-level'>（" + draggingLevel + "）</span>"
        + "<span style='margin-left: 30px;' title='点击删除' onclick='openDeleteToLineDialog(this);'>X</span>"
        + "</div>";
    // 准备插入行
    var $newDataLine = $(dataLineHtml);
    var $newInsertLine = $("<div class=\"to-insert-line\"></div>");
    $toObj.after($newInsertLine);
    $toObj.after($newDataLine);
    return $newInsertLine;
}

/**
 * 刷新toObject的行（加标识、设置样式等）
 */
function refreshToObjectLines() {
    var toDataLines = $(".to-data-line");
    // 有子数据（业务意义上）的行，追加加号或减号
    for (var i = 0; i < toDataLines.length; i++) {
        var toDataLine = toDataLines[i];
        var myIdentify = $(toDataLine).attr("identify");
        var myChildren = $(".to-data-line[parent='" + myIdentify + "']");
        var $foldSpan = $(toDataLine).find(".fold-span");
        if (typeof(myChildren) != "undefined" && myChildren.length > 0) {
            // 有子数据（业务意义上）的行
            var nowDisplay = $(myChildren[0]).css("display");
            if (nowDisplay == "none") {
                // 加号
                $foldSpan.html("+");
                $foldSpan.attr("title", "点击展开");
            } else {
                // 减号
                $foldSpan.html("-");
                $foldSpan.attr("title", "点击收起");
            }
            $foldSpan.removeClass("no-fold");
            $foldSpan.addClass("fold-unfold");
            $foldSpan.click(function() {
                foldOrUnfoldTo(this);
            });
        } else {
            // 没有子数据（业务意义上）的行
            $foldSpan.html("");
            $foldSpan.removeAttr("title");
            $foldSpan.removeClass("fold-unfold");
            $foldSpan.addClass("no-fold");
            $foldSpan.unbind("click");
        }
    }
    doAfterToLineRefreshed();
}

/**
 * toObject：展开或关闭
 */
function foldOrUnfoldTo(obj) {
    var myIdentify = $(obj).parent().attr("identify");
    // 子目录
    var $childrenDivs = $(".to-data-line[parent='" + myIdentify + "']");
    if ($(obj).html() == "+") {
        // 准备展开（只展开下一层）
        for (var i = 0; i < $childrenDivs.length; i++) {
            childDiv = $childrenDivs[i];
            $(childDiv).show();
            $(childDiv).next().show();
        }
        $(obj).attr("title", "点击收起");
        $(obj).html("-");
        doAfterToLineRefreshed();
    } else {
        // 准备合起
        // 先对子目录进行合起操作
        for (var i = 0; i < $childrenDivs.length; i++) {
            var childDiv = $childrenDivs[i];
            var childType = $(childDiv).attr("type");
            if (childType != "normal") {
                var childSpan = $(childDiv).children(".fold-span");
                if ($(childSpan[0]).html() == "-") {
                    foldOrUnfoldTo(childSpan[0]);
                }
            }
            // 再对自己进行合起操作
            $(childDiv).hide();
            // 对自己的下一行insertLine进行合起操作
            $(childDiv).next().hide();
        }
        // 再对自己进行合起操作
        $(obj).attr("title", "点击展开");
        $(obj).html("+");
    }
}

/**
 * 打开删除拖拽后的数据行确认框
 */
function openDeleteToLineDialog(obj) {
    var $dataLineDiv = $(obj).parent();
    openConfirmDialog("确认删除？<br/><span style='color: red; font-weight: bold;'>注意：此操作会同时删除所有的子数据</span>",
        deleteToLine, $dataLineDiv);
}

/**
 * 删除拖拽后的数据行操作
 */
function deleteToLine($dataLineDiv) {
    directDeleteToLine($dataLineDiv);
    refreshToObjectLines();
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
    var fieldRealTypeName = $dragging.attr("fieldRealTypeName");
    // 第一层不存在父级
    if (typeof(parent) == "undefined") {
        parent = null;
    }
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "parent": parent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine("toObject", $toObj, identify, mayModify, draggingLevel, fullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    parent = mayModify.parent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, identify, type, fieldRealTypeName,
        fullName, descr, parent, draggingContent);
    // 删除原先行及其下一行
    $dragging.next().remove();
    $dragging.remove();
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
    toNameTextEvent();
    toDescrTextEvent();
}