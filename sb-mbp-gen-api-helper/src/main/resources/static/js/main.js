$(function() {
    // 初始化布局
    doLayout();
    // 初始化左侧数据和事件
    initLayoutWest();
    // 初始化Center的事件
    initBaseTabEvents();
    initParamTabEvents();
    initResultTabEvents();
});

/**
 * 初始化布局
 */
function doLayout() {
    $("body").layout(
        {
            applyDefaultStyles: true,//应用默认样式
            scrollToBookmarkOnLoad: false,//页加载时滚动到标签
            showOverflowOnHover: false,//鼠标移过显示被隐藏的，只在禁用滚动条时用。
            north__closable: false,//可以被关闭
            north__resizable: false,//可以改变大小
            north__size: 50,//pane的大小
            spacing_open: 8,//边框的间隙
            spacing_closed: 60,//关闭时边框的间隙
            resizerTip: "可调整大小",//鼠标移到边框时，提示语
            resizerCursor:"resize-p",// 鼠标移上的指针样式
            resizerDragOpacity: 0.9,//调整大小边框移动时的透明度
            maskIframesOnResize: "#ifa",//在改变大小的时候，标记iframe（未通过测试）
            sliderTip: "显示/隐藏侧边栏",//在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
            sliderCursor: "pointer",//在某个Pane隐藏后，当鼠标移到边框上时的指针样式。
            slideTrigger_open: "dblclick",//在某个Pane隐藏后，鼠标触发其显示的事件。(click", "dblclick", "mouseover)
            slideTrigger_close: "click",//在某个Pane隐藏后，鼠标触发其关闭的事件。("click", "mouseout")
            togglerTip_open: "关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语
            togglerTip_closed: "打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
            togglerLength_open: 100,//pane打开时，边框按钮的长度
            togglerLength_closed: 200,//pane关闭时，边框按钮的长度
            hideTogglerOnSlide: true,//在边框上隐藏打开/关闭按钮(测试未通过)
            togglerAlign_open: "left",//pane打开时，边框按钮显示的位置
            togglerAlign_closed: "right",//pane关闭时，边框按钮显示的位置
            togglerContent_open: "<div style='background:red'>AAA</div>",//pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
            togglerContent_closed: "<img/>",//pane关闭时，同上。
            enableCursorHotkey: true,//启用快捷键CTRL或shift + 上下左右。
            customHotkeyModifier: "shift",//自定义快捷键控制键("CTRL", "SHIFT", "CTRL+SHIFT"),不能使用alt
            south__customHotkey: "shift+0",//自定义快捷键（测试未通过）
            fxName: "drop",//打开关闭的动画效果
            fxSpeed: "slow"//动画速度
            //fxSettings: { duration: 500, easing: "bounceInOut" }//自定义动画设置(未通过测试)
            //initClosed:true,//初始时，所有pane关闭
            //initHidden:true //初始时，所有pane隐藏
            //onresize: ons,//调整大小时调用的函数
            //onshow_start: start,
            //onshow_end: end
            /*
            其他回调函数

            显示时调用
            onshow = ""
            onshow_start = ""
            onshow_end = ""
            隐藏时调用
            onhide = ""
            onhide_start = ""
            onhide_end = ""
            打开时调用
            onopen = ""
            onopen_start = ""
            onopen_end = ""
            关闭时调用
            onclose = ""
            onclose_start = ""
            onclose_end = ""
            改变大小时调用
            onresize = ""
            onresize_start = ""
            onresize_end = ""
            */
        }
    );

    initTab();
}

/**
 * 初始化Tab页
 */
function initTab() {
    $("#leftTabs").tabs();
    $("#tabs").tabs();
}

/**
 * 初始化左侧数据和事件
 */
function initLayoutWest() {
    $("#projectSearch").on("input", function () {
        var keyword = $(this).val().trim();
        if (keyword == "") {
            $(".project").show();
        } else {
            $(".project").hide();
            $(".project[search*='" + keyword + "']").show();
        }
    });
    $("#interfaceSearch").on("input", function () {
        var keyword = $(this).val().trim();
        if (keyword == "") {
            $(".interface").show();
        } else {
            $(".interface").hide();
            $(".interface[search*='" + keyword + "']").show();
        }
    });
    projectDialog = $("#projectDialog").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        close: function() {
            projectAddForm[0].reset();
        }
    });
    projectAddForm = projectDialog.find("form").on("submit", function(event) {
        event.preventDefault();
        var projectId = $("#projectDialog").find("input[name='id']").val();
        if (projectId == "") {
            addProject();
        } else {
            updateProject(projectId);
        }
    });
    uploadClassDialog = $("#uploadClassDialog").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        close: function() {
            uploadClassForm[0].reset();
        }
    });
    uploadClassForm = uploadClassDialog.find("form").on("submit", function(event) {
        event.preventDefault();
        uploadClassFiles();
    });
    refreshLayoutWest();
}

/**
 * 初始化左侧数据和事件
 */
function refreshLayoutWest() {
    refreshProjects();
}

/**
 * 获取并刷新工程
 */
function refreshProjects() {
    $.ajax({
        url: "/api/ah-project-all",
        type: "GET",
        dataType: "JSON",
        success: function(result) {
            if (result.resultCode != "1") {
                alert("刷新工程失败");
                return;
            }
            var projects = result.data;
            // 刷新工程
            $("#projects").html("");
            if (projects != null) {
                for (var i = 0; i < projects.length; i++) {
                    var project = projects[i];
                    var projectHtml = "<div identify='" + project.id + "' class='project' search='"
                        + project.name + "_" + project.descr + "' projectName='" + project.name
                        + "' projectDescr='" + project.descr + "' basePackage='" + project.basePackage
                        + "' urlPrefix='" + project.urlPrefix + "'" + "title='" + project.descr
                        + "' onclick='selectProject(this);'><span>" + project.name
                        + "</span><a href='#' style='margin-left: 10px; color: dodgerblue;' title='点击修改' "
                        + "onclick='openUpdateProjectDialog(this)'>M</a>"
                        + "<a href='#' style='margin-left: 10px; color: red;' title='点击删除' "
                        + "onclick='openDeleteProjectDialog(this)'>X</a></div>";
                    var $projectLine = $(projectHtml);
                    $("#projects").append($projectLine);
                }
            }
        },
        error: function () {
            alert("刷新工程失败");
        }
    });
}

/**
 * 打开添加工程对话框
 */
function openAddProjectDialog() {
    $("#projectDialog").find("input[name='id']").val("");
    var addButtons = {
        "新增": addProject,
        "取消": function() {
            projectDialog.dialog("close");
        }
    };
    $("#projectDialog").dialog("option", "buttons", addButtons);
    projectDialog.dialog("open");
}

/**
 * 打开修改工程对话框
 */
function openUpdateProjectDialog(domObj) {
    // 刷新数据
    var projectId = $(domObj).parent().attr("identify");
    var projectName = $(domObj).parent().attr("projectName");
    var projectDescr = $(domObj).parent().attr("projectDescr");
    var urlPrefix = $(domObj).parent().attr("urlPrefix");
    var basePackage = $(domObj).parent().attr("basePackage");
    $("#projectDialog").find("input[name='id']").val(projectId);
    $("#projectDialog").find("input[name='name']").val(projectName);
    $("#projectDialog").find("input[name='descr']").val(projectDescr);
    $("#projectDialog").find("input[name='urlPrefix']").val(urlPrefix);
    $("#projectDialog").find("input[name='basePackage']").val(basePackage);
    // 弹对话框
    var updateButtons = {
        "修改": updateProject,
        "取消": function() {
            projectDialog.dialog("close");
        }
    };
    $("#projectDialog").dialog("option", "buttons", updateButtons);
    projectDialog.dialog("open");
}

/**
 * 打开修改工程确认框
 */
function openDeleteProjectDialog(domObj) {
    var projectId = $(domObj).parent().attr("identify");

}

/**
 * 添加工程
 */
function addProject() {
    var httpType = "POST";
    addOrUpdateProject(httpType);
}

/**
 * 修改工程
 */
function updateProject() {
    var httpType = "PUT";
    addOrUpdateProject(httpType);
}

/**
 * 新增或修改工程
 */
function addOrUpdateProject(httpType) {
    var reqData = $("#projectDialog").find("form").toJSON();
    $.ajax({
        url: "/api/ah-project",
        type: httpType,
        data: JSON.stringify(reqData),
        contentType: "application/json",
        dataType: "JSON",
        success: function(result) {
            projectDialog.dialog("close");
            refreshProjects();
        },
        error: function () {
            alert("操作失败");
        }
    });
}

/**
 * 打开上传Java或Class文件对话框
 */
function openUploadClassFilesDialog() {
    if (projectSelected == null) {
        alert("请先选择工程");
        return;
    }
    var addButtons = {
        "上传": uploadClassFiles,
        "取消": function() {
            uploadClassDialog.dialog("close");
        }
    };
    $("#uploadClassDialog").dialog("option", "buttons", addButtons);
    uploadClassDialog.dialog("open");
}

/**
 * 上传Java或Class文件
 */
function uploadClassFiles() {
    if (projectSelected == null) {
        alert("请先选择工程");
        return;
    }
    $("#uploadClassDialog").find("input[name='projectId']").val(projectSelected.id);
    $("#uploadClassDialog form").submit();
}

/**
 * 准备添加接口
 */
function prepareForAddInterface() {
    addingInterface = true;
    interfaceSelected = null;
    refreshInterfaceData();
    $(".interface").removeClass("list-selected");
    $("#curInterface").html("新增中...");
}

/**
 * 工程选中事件
 */
function selectProject(project) {
    var newProjectId = $(project).attr("identify");
    var changeProject = false;
    if (projectSelected == null || newProjectId != projectSelected.id) {
        interfaceSelected = null;
        changeProject = true;
    }
    projectSelected = getProject(newProjectId);
    if (projectSelected == null) {
        return;
    }
    $(".project").removeClass("list-selected");
    $(project).addClass("list-selected");
    // 如果更新了选中，则刷新数据
    if (changeProject) {
        // 刷新接口列表
        refreshInterfaces(newProjectId);
        // 刷新接口数据（清空）
        refreshInterfaceData();
        // 更新结果页搜索框
        $("#resultBasePackage").html(projectSelected.basePackage + ".");
        initResultSearch();
    }
}

/**
 * 获取并刷新接口列表
 */
function refreshInterfaces(projectId) {
    prepareForAddInterface();
    $.ajax({
        url: "/api/ah-interface-all?projectIdEq=" + projectId,
        type: "GET",
        dataType: "JSON",
        success: function(result) {
            if (result.resultCode != "1") {
                alert("刷新接口失败");
                return;
            }
            var interfaces = result.data;
            // 刷新接口
            $("#interfaces").html("");
            if (interfaces != null) {
                for (var i = 0; i < interfaces.length; i++) {
                    var interface = interfaces[i];
                    var interfaceHtml = "<div identify='" + interface.id + "' class='interface' search='"
                        + interface.httpUrl + "_" + interface.interDescr + "' "
                        + "title='" + interface.interDescr + "' onclick='selectInterface(this);'>"
                        + interface.httpUrl + "</div>";
                    var $interfaceLine = $(interfaceHtml);
                    $("#interfaces").append($interfaceLine);
                }
            }
        },
        error: function () {
            alert("刷新接口失败");
        }
    });
}

/**
 * 接口选中事件
 */
function selectInterface(inter) {
    var newInterfaceId = $(inter).attr("identify");
    var changeInterface = false;
    if (interfaceSelected == null || newInterfaceId != interfaceSelected.id) {
        changeInterface = true;
    }
    interfaceSelected = getInterface(newInterfaceId);
    if (interfaceSelected == null) {
        return;
    }
    // 准备修改接口
    addingInterface = false;
    $(".interface").removeClass("list-selected");
    $(inter).addClass("list-selected");
    // 如果更新了选中，则刷新接口数据
    if (changeInterface) {
        refreshInterfaceData();
    }
}

/**
 * 从后台获取project信息
 */
function getProject(projectId) {
    var project = null;
    $.ajax({
        url: "/api/ah-project/" + projectId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            if (result.resultCode != "1") {
                alert("获取工程失败");
                return;
            }
            project = result.data;
        },
        error: function () {
            alert("获取工程失败");
        }
    });
    return project;
}

/**
 * 从后台获取interface信息
 */
function getInterface(interfaceId) {
    var inter = null;
    $.ajax({
        url: "/api/ah-interface/" + interfaceId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            if (result.resultCode != "1") {
                alert("获取工程失败");
                return;
            }
            inter = result.data;
        },
        error: function () {
            alert("获取工程失败");
        }
    });
    return inter;
}

/**
 * 获取实体类
 */
function getClassCode(classId) {
    var clazz = null;
    $.ajax({
        url: "/api/ah-class-code/" + classId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            if (result.resultCode != "1") {
                alert("获取实体类失败");
                return;
            }
            clazz = result.data;
        },
        error: function () {
            alert("获取实体类失败");
        }
    });
    return clazz;
}

/**
 * 放弃修改接口
 */
function abortChangingInterface() {
    refreshInterfaceData();
    if (addingInterface) {
        $("#curInterface").html("新增中...");
    }
}

/**
 * 刷新页面数据（配置文件内容）
 */
function refreshInterfaceData() {
    $("#curProject").html(projectSelected.name);
    $("#curProject").attr("title", projectSelected.name + "\n" + projectSelected.descr);
    if (interfaceSelected == null) {
        $("#curInterface").html("");
        $("#curInterface").attr("title", "");
    } else {
        $("#curInterface").html(interfaceSelected.httpUrl);
        $("#curInterface").attr("title", interfaceSelected.httpUrl + "\n" + interfaceSelected.interNo
            + "\n" + interfaceSelected.interDescr);
    }
    refreshBaseData();
    refreshParamData();
    refreshResultData();
}

/**
 * 验证并生成接口的JSON
 */
function validAndGenInter() {
    // 1、基本信息
    var interInfoData = new Object();
    interInfoData.interNo = emptyStringToNull($("#baseInfo").find("input[name='interNo']").val());
    interInfoData.httpMethod = emptyStringToNull($("#baseInfo").find("select[name='httpMethod']").val());
    interInfoData.addDefaultPrefix = emptyStringToNull($("#baseInfo").find("select[name='addDefaultPrefix']").val());
    interInfoData.httpUrl = emptyStringToNull($("#baseInfo").find("input[name='httpUrl']").val());
    interInfoData.interDescr = emptyStringToNull($("#baseInfo").find("input[name='interDescr']").val());
    // 2、参数信息
    interInfoData.param = new Object();
    // 2.1、条件信息
    var criteriaList = $("#paramInfo").find("div[idFrom='criteriaToAdd']");
    if (typeof(criteriaList) != "undefined") {
        if (criteriaList.length > 0) {
            interInfoData.param.criteriaList = new Array();
            for (var i = 0; i < criteriaList.length; i++) {
                var criteria = criteriaList[i];
                var fromParam = emptyStringToNull($(criteria).find("input[name='fromParam']").val());
                var descr = emptyStringToNull($(criteria).find("input[name='descr']").val());
                var fixedValue = emptyStringToNull($(criteria).find("input[name='fixedValue']").val());
                var emptyToNull = emptyStringToNull($(criteria).find("select[name='emptyToNull']").val());
                var toCriteriaList = getStringArrayFromList($(criteria).find("input[name='toCriteriaList']"));
                if (fromParam == null) {
                    continue;
                }
                var criteriaObj = new Object();
                criteriaObj.fromParam = fromParam;
                criteriaObj.descr = descr;
                criteriaObj.fixedValue = fixedValue;
                criteriaObj.emptyToNull = emptyToNull;
                criteriaObj.toCriteriaList = toCriteriaList;
                // 添加到criteriaList中
                interInfoData.param.criteriaList.push(criteriaObj);
            }
            if (interInfoData.param.criteriaList.length == 0) {
                interInfoData.param.criteriaList = null;
            }
        }
    }
    // 2.2、级联信息
    var associationNameList = $("#paramInfo").find("input[name='associationNameList']");
    interInfoData.param.associationNameList = getStringArrayFromList(associationNameList);
    // 2.3、字典信息
    var dictionaryNameList = $("#paramInfo").find("input[name='dictionaryNameList']");
    interInfoData.param.dictionaryNameList = getStringArrayFromList(dictionaryNameList);
    // 2.4、排序信息
    interInfoData.param.orderBy = emptyStringToNull($("#paramInfo").find("input[name='orderBy']").val());
    // 2.5、SQL列信息
    var sqlColumnList = $("#paramInfo").find("input[name='sqlColumnList']");
    interInfoData.param.sqlColumnList = getStringArrayFromList(sqlColumnList);
    // 3、返回信息
    interInfoData.result = new Object();
    interInfoData.result.resultCode = "1：操作成功  2：操作失败";
    interInfoData.result.errMsg = "错误消息";
    interInfoData.result.data = "返回数据";
    interInfoData.result.fieldList = new Array();
    var toObjectDataList = $("#toObject .to-data-line");
    for (var i = 0; i < toObjectDataList.length; i++) {
        var field = new Object();
        var toObjectData = toObjectDataList[i];
        var type = $(toObjectData).attr("type");
        if (type == "normal") {
            type = null;
        }
        field.name = $(toObjectData).find(".to-name").html();
        field.type = type;
        field.fromName = $(toObjectData).attr("fullName");
        field.descr = $(toObjectData).find(".to-descr").html();
        interInfoData.result.fieldList.push(field);
    }
    // 返回数据
    console.log(JSON.stringify(interInfoData));
    return interInfoData;
}

/**
 * 从obj列表中取值
 */
function getStringArrayFromList(domObjList) {
    var resultList = null;
    if (typeof(domObjList) != "undefined") {
        if (domObjList.length > 0) {
            resultList = new Array();
            for (var index = 0; index < domObjList.length; index++) {
                var toCriteriaValue = emptyStringToNull($(domObjList[index]).val());
                if (toCriteriaValue == null) {
                    continue;
                }
                resultList.push(toCriteriaValue);
            }
            if (resultList.length == 0) {
                resultList = null;
            }
        }
    }
    return resultList;
}

/**
 * 新增或更新接口
 */
function addOrUpdateInterface() {
    validAndGenInter();
    if (interfaceSelected != null) {
        // 更新
    }
}