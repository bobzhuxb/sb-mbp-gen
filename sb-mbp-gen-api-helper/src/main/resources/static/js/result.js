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
 * 加载结果类到fromObject
 */
function loadReturnClassFromObject() {
    // fromObject加载
    var searchFullClassName = $("#resultBasePackage").html() + $("#resultSearch").val().trim();
    // 每次加载时，设置返回结果类型
    returnTypeName = searchFullClassName;
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
}

/**
 * 初始化加载field的JSON到toObject
 */
function loadDataJsonToObject() {
    var searchFullClassName = $("#resultBasePackage").html() + $("#resultSearch").val();
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
        // Key：field所在的from全路径  Value：field的实际类型
        var fieldPathTypeMap = new Map();
        // Key：分页field所在的from全路径  Value：field的实际类型
        var pageTypeMap = new Map();
        fieldPathTypeMap.set("-", searchFullClassName);
        // 从第1层级开始处理fromName，形成Map
        sortFieldFromMap(fieldPathTypeMap, pageTypeMap, fieldList, 1);
        // 从第1层级开始处理，根据name（to）进行排序
        var result = sortFieldList(fieldPathTypeMap, fieldList, sortedFieldList, 1);
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
            // 目前全路径名
            var fullName = field.name;
            // 来源全路径名
            var oriFullName = field.fromName;
            // 计算当前全路径的层级
            var splitFullNameArr = fullName.split(".");
            // 当前层级
            var level = splitFullNameArr.length;
            // 转换后名称
            var nameContent = field.name;
            if (nameContent.lastIndexOf(".") > 0) {
                nameContent = nameContent.substring(nameContent.lastIndexOf(".") + 1);
            }
            // 转换后描述
            var descr = field.descr;
            // 父级的类（上一步骤已设置）
            var toParent = field.toParent;
            // 该字段的实际数据类型名（上一步骤已设置）
            var fieldRealTypeName = field.realTypeName;
            // 字段行HTML
            var dataLineHtml = "<div"
                + " identify='" + toIdentify + "'"
                + " level='" + level + "'"
                + " type='" + type + "'"
                + " fieldRealTypeName='" + fieldRealTypeName + "'"
                + " title='来源全路径：" + oriFullName + "\n来源类型：" + fieldRealTypeName + "\n当前全路径：" + fullName + "'"
                + " fullName='" + fullName + "'"
                + " oriFullName='" + oriFullName + "'";
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
            $("#toObject").append($newDataLine);
            $("#toObject").append($newInsertLine);
        }
        // 插入行之后的操作
        doAfterDroppedHtmlFormed();
        refreshToObjectLines();
    }
}

/**
 * 用于为sortedFieldList生成fieldPathTypeMap
 */
function sortFieldFromMap(fieldPathTypeMap, pageTypeMap, fieldList, processingLevel) {
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        // from全路径名
        var oriFullName = field.fromName;
        // from全路径拆解
        var splitFromFullNameArr = oriFullName.split(".");
        // to所在层级
        var level = splitFromFullNameArr.length;
        if (level != processingLevel) {
            // 过滤掉不是当前处理层级的
            continue;
        }
        // 获取from中该字段所在类的类型
        var key;
        if (oriFullName.indexOf(".") < 0) {
            // oriFullName没有点，说明from中在第一层
            key = "-";
        } else {
            // from中在其他层
            key = oriFullName.substring(0, oriFullName.lastIndexOf("."));
        }
        var parentTypeName = fieldPathTypeMap.get(key);
        // 根据oriFullName查找fieldRealTypeName
        var fieldRealTypeName = getFieldRealTypeName(oriFullName, parentTypeName, fieldPathTypeMap, pageTypeMap);
        if (fieldRealTypeName == null) {
            return false;
        }
        field.realTypeName = fieldRealTypeName;
        // 设置Map
        fieldPathTypeMap.set(oriFullName, field.realTypeName);
        // 标记该field已处理完
        field.fromProcessed = true;
    }
    // 每次处理完一个层级之后，都验证一下是否全部处理完成
    var allProcessed = true;
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        if (!field.fromProcessed) {
            allProcessed = false;
            break;
        }
    }
    // 未处理完成，则继续处理下一层级，处理完成则结束
    if (!allProcessed) {
        var result = sortFieldFromMap(fieldPathTypeMap, pageTypeMap, fieldList, processingLevel + 1);
        if (!result) {
            return false;
        }
    }
    // 处理完成
    return true;
}

/**
 * 对fieldList重新排序，符合父子结构
 */
function sortFieldList(fieldPathTypeMap, fieldList, sortedFieldList, processingLevel) {
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        // to全路径名
        var fullName = field.name;
        // // from全路径名
        // var oriFullName = field.fromName;
        // to全路径拆解
        var splitFullNameArr = fullName.split(".");
        // to所在层级
        var level = splitFullNameArr.length;
        if (level != processingLevel) {
            // 过滤掉不是当前处理层级的
            continue;
        }
        // // 获取from中该字段所在类的类型
        // var key;
        // if (oriFullName.indexOf(".") < 0) {
        //     // oriFullName没有点，说明from中在第一层
        //     key = "-";
        // } else {
        //     // from中在其他层
        //     key = oriFullName.substring(0, oriFullName.lastIndexOf("."));
        // }
        // var parentTypeName = fieldPathTypeMap.get(key);
        // // 根据oriFullName查找fieldRealTypeName
        // var fieldRealTypeName = getFieldRealTypeName(oriFullName, parentTypeName);
        // if (fieldRealTypeName == null) {
        //     return false;
        // }
        // field.realTypeName = fieldRealTypeName;
        // 添加进sortedFieldList，并设置toParent
        if (processingLevel == 1) {
            // 第一层的parent为null
            field.toParent = null;
            // 第一层直接添加
            sortedFieldList.push(field);
        } else {
            // 其他层级
            var parentName = fullName.substring(0, fullName.lastIndexOf("."));
            for (var j = 0; j < fieldList.length; j++) {
                var findParentField = fieldList[j];
                var findParentFieldName = findParentField.name;
                if (findParentFieldName == parentName) {
                    // 找到对应的parent了
                    // 设置当前的parent的identify
                    field.toParent = findParentField.identify;
                    // 在当前的parentField后面追加
                    for (var k = 0; k < sortedFieldList.length; k++) {
                        var sortedField = sortedFieldList[k];
                        if (sortedField.identify == findParentField.identify) {
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
        var result = sortFieldList(fieldPathTypeMap, fieldList, sortedFieldList, processingLevel + 1);
        if (!result) {
            return false;
        }
    }
    // 处理完成
    return true;
}

/**
 * 根据来源全路径名获取field类型全称
 */
function getFieldRealTypeName(oriFullPathName, nowClassTypeName, fieldPathTypeMap, pageTypeMap) {
    var fullPathNameArr = oriFullPathName.split(".");
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
        // 找不到对应的类（当前字段所在的类）
        return null;
    }
    // 查找fieldList
    var nowClassCodeFull = getClassCode(nowClassCode.id);
    // 当前字段所在类的所有字段
    var fieldList = nowClassCodeFull.ahFieldList;
    for (var i = 0; i < fieldList.length; i++) {
        var field = fieldList[i];
        // 字段名称
        var fieldName = field.fieldName;
        // 获取field的实际类型
        var fieldRealTypeName = field.typeName;
        if (fieldRealTypeName == "java.util.List") {
            fieldRealTypeName = field.genericTypeName;
        }
        if (fieldRealTypeName == projectSelected.basePackage + ".IPage"
                || fieldRealTypeName == projectSelected.basePackage + ".MbpPage") {
            pageTypeMap.set(oriFullPathName + ".records", field.genericTypeName);
        }
        if (fullPathNameArr[fullPathNameArr.length - 1] == fieldName) {
            // 找到了对应的字段，返回当前字段的类型
            if (fieldRealTypeName == "T") {
                // IPage类型的特殊处理
                fieldRealTypeName = pageTypeMap.get(oriFullPathName);
            }
            return fieldRealTypeName;
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
        // 随机生成唯一值
        // var identify = field.id;
        var identify = generateUUID();
        var fieldTypeName = field.typeName;
        var fieldName = field.fieldName;
        var genericTypeName = field.genericTypeName;
        var descr = field.descr;
        if (fieldName.startsWith("_")) {
            // 该字段是为了匹配数据库使用的，过滤掉
            continue;
        }
        if (descr == null || descr.trim == "") {
            descr = "（请填写注释）";
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
        // 泛型的genericTypeName初始化为父级的genericTypeName
        if (genericTypeName != null && genericTypeName.trim() != "") {
            if (genericTypeName.indexOf(".") < 0) {
                genericTypeName = $("#fromObject").find("div[identify='" + parentIdentify + "']").attr("genericTypeName");
            }
        }
        if (isArray) {
            fromLinesHtml += " type='list'" + " genericTypeName='" + genericTypeName
                + "' class='from-data-line data-is-list'";
        } else if (isObject) {
            fromLinesHtml += " type='object'";
            if (genericTypeName != null && genericTypeName.trim() != "") {
                fromLinesHtml += " genericTypeName='" + genericTypeName + "'";
            }
            fromLinesHtml += " class='from-data-line data-is-object'";
        } else {
            fromLinesHtml += " type='normal' class='from-data-line'";
        }
        fromLinesHtml += " fullName='" + fullName + "' title='全路径：" + fullName + "\n类型：" + fieldTypeName
            + "' descr='" + descr + "' fieldType='" + fieldTypeName + "' parent='" + parentIdentify + "'>";
        if (isArray || isObject) {
            fromLinesHtml += "<span class='fold-unfold' onclick='foldOrUnfoldFrom(this, false);' title='点击展开'>+</span> ";
        } else {
            fromLinesHtml += "<span class='no-fold'></span> ";
        }
        fromLinesHtml += "<span class='from-name'>" + fieldName + "</span> | " + descr + "</div>";
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
            var fieldType = $(obj).parent().attr("fieldType");
            var loadingTypeName;
            if (type == "object") {
                if (fieldType.indexOf(".") < 0) {
                    // 泛型，获取父级的genericTypeName
                    loadingTypeName = $("#fromObject").find("div[identify='" + myIdentify + "']").attr("genericTypeName");
                } else {
                    // 普通Object
                    loadingTypeName = fieldType;
                }
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
    // 初始化结果页事件
    initResultTabEvents();
    if (interfaceSelected == null) {
        $("#resultSearch").val("");
        return;
    }
    var returnTypeName = interfaceSelected.returnType;
    if (returnTypeName == null || !returnTypeName.startsWith(basePackage + ".")) {
        return;
    }
    // 设置returnType搜索内容
    var searchWord = returnTypeName.substring(basePackage.length + 1);
    $("#resultSearch").val(searchWord);
    // 加载结果类到fromObject和toObject
    loadReturnClassFromObject();
    loadDataJsonToObject();
}

function returnClassLoadBtnClicked() {
    loadReturnClassFromObject();
    $("#toObject").html("<div class='to-insert-line'></div>");
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
    // 获取数据并显示
    var $toNameDisplay = $(obj).parent().children(".to-name");
    var newVal = $(obj).val();
    // 更新前的fullName
    var fullNamePrefix = $(obj).parent().attr("fullName");
    $toNameDisplay.html(newVal);
    $toNameDisplay.show();
    $(obj).hide();
    toNameTextChangeRelate($(obj).parent(), fullNamePrefix, newVal);
}


/**
 * 级联更新子目录的fullName及title
 */
function toNameTextChangeRelate($divObj, fullNamePrefix, newVal) {
    // 更新相关字段
    var oriFullName = $divObj.attr("oriFullName");
    var fieldRealTypeName = $divObj.attr("fieldRealTypeName");
    var fullName = $divObj.attr("fullName");
    // fullNamePrefix要变更成的全名
    var tmpFullName;
    if (fullName.lastIndexOf(".") < 0) {
        tmpFullName = newVal;
    } else {
        tmpFullName = fullNamePrefix.substring(0, fullNamePrefix.lastIndexOf(".") + 1) + newVal;
    }
    if (fullName == fullNamePrefix) {
        // 当前实际修改的字段
        fullName = tmpFullName;
    } else {
        // 被关联修改的子目录
        fullName = tmpFullName + fullName.substring(fullNamePrefix.length);
    }
    $divObj.attr("fullName", fullName);
    $divObj.attr("title", "来源全路径：" + oriFullName + "\n来源类型：" + fieldRealTypeName + "\n当前全路径：" + fullName);
    // 递归修改所有子目录的fullName和title
    var myIdentify = $divObj.attr("identify");
    var children = $(".to-data-line[parent='" + myIdentify + "']");
    for (var i = 0; i < children.length; i++) {
        var childDiv = children[i];
        toNameTextChangeRelate($(childDiv), fullNamePrefix, newVal);
    }
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
                fromLineDropped($dragging, $(this), true);
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
 * 打开清空toObject的数据对话框
 */
function openClearToObjectDialog() {
    openConfirmDialog("确认清空转换后数据？", clearToObject);
}

/**
 * 清空toObject的数据
 */
function clearToObject() {
    $("#toObject").html("<div class='to-insert-line'></div>");
    // 初始化结果页事件
    initResultTabEvents();
}

/**
 * 打开清空toObject的数据，并从fromObject拷贝所有对话框
 */
function openClearToAndCopyAllDialog() {
    openConfirmDialog("确认全量覆盖？", clearToAndCopyAll);
}

/**
 * 清空toObject的数据，并从fromObject拷贝所有
 */
function clearToAndCopyAll() {
    // 清空toObject
    clearToObject();
    // 从fromObject拷贝所有
    var fromLines = $(".from-data-line[level='1']:visible");
    // 初始的插入行
    for (var i = fromLines.length - 1; i >= 0; i--) {
        var fromLine = fromLines[i];
        var $toObj = $("#toObject .to-insert-line:first");
        fromLineDropped($(fromLine), $toObj, true);
    }
    // 插入行之后的操作
    doAfterDroppedHtmlFormed();
    refreshToObjectLines();
}

/**
 * 从转换前字段拖拽
 */
function fromLineDropped($dragging, $toObj, childInclude) {
    // 插入字段和空行
    var draggingLevel = $dragging.attr("level");
    var fromIdentify = $dragging.attr("identify");
    var type = $dragging.attr("type");
    var oriFullName = $dragging.attr("fullName");
    var descr = $dragging.attr("descr");
    var fromParent = $dragging.attr("parent");
    var draggingContent = $dragging.children("span[class='from-name']").html();
    var fieldType = $dragging.attr("fieldType");
    var genericTypeName = $dragging.attr("genericTypeName");
    // toObject的相关属性生成
    var toIdentify = generateUUID();
    // 分析该字段的实际类型
    var fieldRealTypeName = fieldType;
    if (fieldType == "java.util.List"
            || fieldType == projectSelected.packageName + ".IPage"
            || fieldType == projectSelected.packageName + ".MbpPage") {
        fieldRealTypeName = genericTypeName;
    }
    // 第一层不存在父级
    if (typeof(fromParent) == "undefined") {
        fromParent = null;
    }
    var fullName = oriFullName;
    var toParent = null;
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "toParent": toParent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine("fromObject", $toObj, fromIdentify, mayModify, draggingLevel, oriFullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    toParent = mayModify.toParent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, toIdentify, type, fieldRealTypeName,
        oriFullName, fullName, descr, toParent, draggingContent);
    if (childInclude) {
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
                    fromLineDropped($child, $childToObj, true);
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
function validateFormLine(fromDivDomId, $toLineObj, fromIdentify, mayModify, draggingLevel, oriFullName) {
    // $toObj的前一行
    var $prevDataLineDiv = $toLineObj.prev();
    // $toObj的前一行是否为object或list
    var prevIsObjectOrList = false;
    if ($prevDataLineDiv.length > 0) {
        var prevType = $prevDataLineDiv.attr("type");
        if (prevType == "object" || prevType == "list") {
            prevIsObjectOrList = true;
        }
    }
    // 验证被拖拽的层级能否被提升或降低或变更
    // 验证逻辑（重点）：只要不跨越list（往上升级或往下降级），都是允许的
    // 最终要变成的level
    var finalLevel;
    // 最终要设置的parent
    var finalParent = null;
    // 最终要变成的fullName
    var finalFullName;
    if ($prevDataLineDiv.length == 0) {
        // 移动到第一行
        finalLevel = 1;
        finalParent = null;
        var lastDotIndex = oriFullName.lastIndexOf(".");
        // 当前fullName最后一个点后面的属性名
        if (lastDotIndex < 0) {
            // 当前原本是第一层
            finalFullName = oriFullName;
        } else {
            finalFullName = oriFullName.substring(lastDotIndex + 1);
        }
    } else {
        if (prevIsObjectOrList) {
            // 前一行的type是object或list
            // level比前一行多一层
            finalLevel = parseInt($prevDataLineDiv.attr("level")) + 1;
            // parent就是前一行的identify
            finalParent = $prevDataLineDiv.attr("identify");
            // 当前fullName最后一个点的位置
            var curLastDotIndex = oriFullName.lastIndexOf(".");
            // 当前fullName最后一个点后面的属性名
            var curLastName;
            if (curLastDotIndex < 0) {
                // 当前原本是第一层
                curLastName = oriFullName;
            } else {
                curLastName = oriFullName.substring(curLastDotIndex + 1);
            }
            // 前一行的fullName
            var prevFullName = $prevDataLineDiv.attr("fullName");
            // 最终的fullName就是在前一行fullName的基础上，叠加上自己的lastName
            finalFullName = prevFullName + "." + curLastName;
        } else {
            // 前一行的type是normal（修改为与前一行相同）
            finalLevel = parseInt($prevDataLineDiv.attr("level"));
            finalParent = $prevDataLineDiv.attr("parent");
            if (typeof(finalParent) == "undefined") {
                finalParent = null;
            }
            // 当前fullName最后一个点的位置
            var curLastDotIndex = oriFullName.lastIndexOf(".");
            // 当前fullName最后一个点后面的属性名
            var curLastName;
            if (curLastDotIndex < 0) {
                // 当前原本是第一层
                curLastName = oriFullName;
            } else {
                curLastName = oriFullName.substring(curLastDotIndex + 1);
            }
            // 前一行的fullName
            var prevFullName = $prevDataLineDiv.attr("fullName");
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
    var passList = passJsonList(fromDivDomId, fromIdentify, oriFullName, finalFullName);
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
        toastError("不得跨越list层级");
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
function formDataLineAndInsertLine($toObj, draggingLevel, toIdentify, type, fieldRealTypeName, oriFullName, fullName,
                                   descr, toParent, draggingContent) {
    // 字段行HTML
    var dataLineHtml = "<div"
        + " identify='" + toIdentify + "'"
        + " level='" + draggingLevel + "'"
        + " type='" + type + "'"
        + " fieldRealTypeName='" + fieldRealTypeName + "'"
        + " title='来源全路径：" + oriFullName + "\n来源类型：" + fieldRealTypeName + "\n当前全路径：" + fullName + "'"
        + " fullName='" + fullName + "'"
        + " oriFullName='" + oriFullName + "'";
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
 * 按属性名称排序
 */
function sortByPropName(nameClass) {
    return function(a, b) {
        var name1 = $($(a).children(nameClass)[0]).html();
        var name2 = $($(b).children(nameClass)[0]).html();
        return name1.localeCompare(name2);
    }
}

/**
 * fromObject的排序
 */
function orderFromLines() {
    var lines = $("#fromObject div[level='1']");
    var sortedLines = new Array();
    if (typeof(lines) != "undefined" && lines.length > 0) {
        orderLinesOfCurLevel("fromObject", ".from-name", lines, sortedLines);
    }
    $("#fromObject").html("");
    for (var i = 0; i < sortedLines.length; i++) {
        var line = sortedLines[i];
        $("#fromObject").append($(line).prop("outerHTML"));
    }
}

/**
 * toObject的排序
 */
function orderToLines() {
    var dataLines = $("#toObject div[level='1']");
    var sortedLines = new Array();
    if (typeof(dataLines) != "undefined" && dataLines.length > 0) {
        orderLinesOfCurLevel("toObject", ".to-name", dataLines, sortedLines);
    }
    $("#toObject").html("<div class='to-insert-line'></div>");
    for (var i = 0; i < sortedLines.length; i++) {
        var line = sortedLines[i];
        $("#toObject").append($(line).prop("outerHTML"));
        $("#toObject").append("<div class='to-insert-line'></div>");
    }
    doAfterDroppedHtmlFormed();
}

/**
 * 递归排序每一层数据
 */
function orderLinesOfCurLevel(objectDivId, className, lines, sortedLines) {
    lines.sort(sortByPropName(className));
    for (var i = 0; i < lines.length; i++) {
        var line = lines[i];
        sortedLines.push(line);
        var identify = $(line).attr("identify");
        var subLines = $("#" + objectDivId + " div[parent='" + identify + "']");
        if (typeof(subLines) != "undefined" && subLines.length > 0) {
            orderLinesOfCurLevel(objectDivId, className, subLines, sortedLines);
        }
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
    var oriFullName = $dragging.attr("oriFullName");
    var descr = $dragging.children(".to-descr").html();
    var parent = $dragging.attr("parent");
    var draggingContent = $dragging.children(".to-name").html();
    var fieldRealTypeName = $dragging.attr("fieldRealTypeName");
    // 第一层不存在父级
    if (typeof(parent) == "undefined") {
        parent = null;
    }
    var fullName = oriFullName;
    var toParent = null;
    // 待修改的Object
    var mayModify = {"draggingLevel": draggingLevel, "fullName": fullName, "toParent": toParent};
    // 验证是否可插入到此处
    var validateResult = validateFormLine("toObject", $toObj, identify, mayModify, draggingLevel, oriFullName);
    if (!validateResult) {
        return;
    }
    // 待修改的Object的字段重新设置回来（有可能已经修改过）
    draggingLevel = mayModify.draggingLevel;
    fullName = mayModify.fullName;
    toParent = mayModify.toParent;
    // 生成数据行和插入行
    var $prevInsertLine = formDataLineAndInsertLine($toObj, draggingLevel, identify, type, fieldRealTypeName,
        oriFullName, fullName, descr, toParent, draggingContent);
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