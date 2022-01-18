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
            west__size: 500,
            spacing_open: 12,//边框的间隙
            spacing_closed: 12,//关闭时边框的间隙
            // resizerTip: "可调整大小",//鼠标移到边框时，提示语
            // resizerCursor:"resize-p",// 鼠标移上的指针样式
            // resizerDragOpacity: 0.9,//调整大小边框移动时的透明度
            maskIframesOnResize: "#ifa",//在改变大小的时候，标记iframe（未通过测试）
            sliderTip: "显示/隐藏侧边栏",//在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
            sliderCursor: "pointer",//在某个Pane隐藏后，当鼠标移到边框上时的指针样式。
            slideTrigger_open: "dblclick",//在某个Pane隐藏后，鼠标触发其显示的事件。(click", "dblclick", "mouseover)
            slideTrigger_close: "click",//在某个Pane隐藏后，鼠标触发其关闭的事件。("click", "mouseout")
            togglerTip_open: "关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语
            togglerTip_closed: "打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
            togglerLength_open: 100,//pane打开时，边框按钮的长度
            togglerLength_closed: 100,//pane关闭时，边框按钮的长度
            hideTogglerOnSlide: true,//在边框上隐藏打开/关闭按钮(测试未通过)
            togglerAlign_open: "center",//pane打开时，边框按钮显示的位置
            togglerAlign_closed: "center",//pane关闭时，边框按钮显示的位置
            togglerContent_open: "<div style='background:red'>&lt;</div>",//pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
            togglerContent_closed: "<div style='background:red'>&gt;</div>",//pane关闭时，同上。
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
    $("#centerTabs").tabs();
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
        height: 600,
        width: 530,
        modal: true,
        close: function() {
            uploadClassForm[0].reset();
        }
    });
    uploadClassForm = uploadClassDialog.find("form").on("submit", function(event) {
        event.preventDefault();
        uploadClassFiles();
    });
    interJsonDialog = $("#interJsonDialog").dialog({
        autoOpen: false,
        height: 600,
        width: 800,
        modal: true
    });
    jsonImportDialog = $("#jsonImportDialog").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        close: function() {
            uploadClassForm[0].reset();
        }
    });
    jsonImportForm = jsonImportDialog.find("form").on("submit", function(event) {
        event.preventDefault();
        importInterJsonFiles();
    });
    confirmDialog = $("#confirmDialog").dialog({
        autoOpen: false,
        height: 200,
        width: 300,
        modal: true
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
    showLoading("body");
    $.ajax({
        url: "/api/ah-project-all",
        type: "GET",
        dataType: "JSON",
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("刷新工程失败");
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
                        + "onclick='openUpdateProjectDialog(this);'>M</a>"
                        + "<a href='#' style='margin-left: 10px; color: red;' title='点击删除' "
                        + "onclick='openDeleteProjectDialog(this);'>X</a></div>";
                    var $projectLine = $(projectHtml);
                    $("#projects").append($projectLine);
                }
                if (projectSelected != null) {
                    $(".project[identify='" + projectSelected.id + "']").addClass("list-selected");
                }
            }
        },
        error: function () {
            closeLoading();
            toastError("刷新工程失败");
        }
    });
}

/**
 * 打开接口JSON预览对话框
 */
function openInterJsonDialog(content, saveCallback, exportCallback) {
    var addButtons = {
        "保存": function () {
            saveCallback();
            interJsonDialog.dialog("close");
        },
        "保存并导出": function () {
            exportCallback();
            interJsonDialog.dialog("close");
        },
        "取消": function() {
            interJsonDialog.dialog("close");
        }
    };
    $("#interJsonShow").html(content);
    $("#interJsonDialog").dialog("option", "buttons", addButtons);
    interJsonDialog.dialog("open");
}

/**
 * 打开确认对话框
 */
function openConfirmDialog(content, callback, callbackParam) {
    var addButtons = {
        "确定": function () {
            callback(callbackParam);
            confirmDialog.dialog("close");
        },
        "取消": function() {
            confirmDialog.dialog("close");
        }
    };
    $("#confirmDialogContent").html(content);
    $("#confirmDialog").dialog("option", "buttons", addButtons);
    confirmDialog.dialog("open");
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
 * 打开删除工程确认框
 */
function openDeleteProjectDialog(domObj) {
    var projectId = $(domObj).parent().attr("identify");
    openConfirmDialog("确认删除工程？<br/><span style='color: red; font-weight: bold;'>"
        + "注意：该操作会删除工程及其关联的所有数据！</span>", deleteProject, projectId);
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
    showLoading("body");
    $.ajax({
        url: "/api/ah-project",
        type: httpType,
        data: JSON.stringify(reqData),
        contentType: "application/json",
        dataType: "JSON",
        success: function(result) {
            closeLoading();
            if (result.resultCode == "1") {
                toastSuccess("操作成功");
                projectDialog.dialog("close");
                refreshProjects();
            } else {
                toastError("操作失败。" + result.errMsg);
            }
        },
        error: function () {
            closeLoading();
            toastError("操作失败");
        }
    });
}

/**
 * 删除工程
 */
function deleteProject(projectId) {
    showLoading("body");
    $.ajax({
        url: "/api/ah-project/" + projectId,
        type: "DELETE",
        contentType: "application/json",
        success: function(result) {
            closeLoading();
            confirmDialog.dialog("close");
            refreshProjects();
        },
        error: function () {
            closeLoading();
            toastError("操作失败");
        }
    });
}

/**
 * 打开删除接口确认框
 */
function openDeleteInterfaceDialog(domObj) {
    var interfaceId = $(domObj).parent().attr("identify");
    openConfirmDialog("确认删除接口？", deleteInterface, interfaceId);
}

/**
 * 删除接口
 */
function deleteInterface(interfaceId) {
    showLoading("body");
    $.ajax({
        url: "/api/ah-interface/" + interfaceId,
        type: "DELETE",
        contentType: "application/json",
        success: function(result) {
            closeLoading();
            confirmDialog.dialog("close");
            refreshInterfaces(projectSelected.id);
        },
        error: function () {
            closeLoading();
            toastError("操作失败");
        }
    });
}

/**
 * 打开上传Java或Class文件对话框
 */
function openUploadClassFilesDialog() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    var exceptClassNames = projectSelected.exceptClassNames;
    if (exceptClassNames != null && exceptClassNames.trim() != '') {
        $("#uploadClassDialog").find("textarea[name='exceptClassNames']").html(exceptClassNames);
    }
    var addButtons = {
        "上传": uploadClassFiles,
        "取消": function() {
            uploadClassDialog.dialog("close");
        }
    };
    $("#uploadClassDialog").dialog("option", "buttons", addButtons);
    uploadClassDialog.dialog("open");
    $("#classFilesDiv input:visible").remove();
    $("#classFilesDiv a").remove();
}

/**
 * 上传Java或Class文件
 */
function uploadClassFiles() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    $("#uploadClassDialog").find("input[name='projectId']").val(projectSelected.id);
    showLoading("body");
    $.ajax({
        url : "/api/ah-class-code-folder-upload",
        type : "POST",
        cache : false,
        data : new FormData(uploadClassForm[0]),
        processData : false,
        contentType : false,
        success : function(result) {
            closeLoading();
            if (result.resultCode == "1") {
                toastSuccess("上传完成");
                uploadClassDialog.dialog("close");
                $(".project[identify='" + projectSelected.id + "']").trigger("click");
            } else {
                toastError("上传失败。" + result.errMsg);
            }
        },
        error: function () {
            closeLoading();
            toastError("上传失败");
        }
    });
}

/**
 * 准备添加接口
 */
function prepareForAddInterface() {
    addingInterface = true;
    interfaceSelected = null;
    refreshInterfaceData();
    $(".interface").removeClass("list-selected");
    $("#curInterface").removeAttr("title");
    $("#curInterface").html("新增中...");
    // 显示centerTabs
    $("#centerTabs").show();
    initResultTabEvents();
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
        // 隐藏centerTab信息
        $("#centerTabs").hide();
    }
}

/**
 * 获取并刷新接口列表
 */
function refreshInterfaces(projectId) {
    showLoading("body");
    $.ajax({
        url: "/api/ah-interface-all?projectIdEq=" + projectId,
        type: "GET",
        dataType: "JSON",
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("刷新接口失败");
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
                        + "title='" + interface.interDescr + "\n" + interface.httpMethod + "\n"
                        + interface.interNo + "' onclick='selectInterface(this);'>"
                        + "<span>" + interface.httpUrl + "（" + interface.httpMethod + "_" + interface.interNo + "）" + "</span>"
                        + "<a href='#' style='margin-left: 10px; color: red;' title='点击删除' "
                        + "onclick='openDeleteInterfaceDialog(this);'>X</a></div>";
                    var $interfaceLine = $(interfaceHtml);
                    $("#interfaces").append($interfaceLine);
                }
            }
            if (interfaceSelected != null) {
                selectInterface($(".interface[identify='" + interfaceSelected.id + "']"));
            }
        },
        error: function () {
            closeLoading();
            toastError("刷新接口失败");
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
    if (typeof(newInterfaceId) == "undefined") {
        // 删除后，默认选择第一个接口
        var $firstInter = $(".interface:first");
        if (typeof($firstInter) == "undefined") {
            return;
        }
        newInterfaceId = $firstInter.attr("identify");
        inter = $firstInter.get(0);
    }
    interfaceSelected = getInterface(newInterfaceId);
    if (interfaceSelected == null) {
        return;
    }
    // 显示centerTabs
    $("#centerTabs").show();
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
    showLoading("body");
    $.ajax({
        url: "/api/ah-project/" + projectId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("获取工程失败");
                return;
            }
            project = result.data;
        },
        error: function () {
            closeLoading();
            toastError("获取工程失败");
        }
    });
    return project;
}

/**
 * 从后台获取interface信息
 */
function getInterface(interfaceId) {
    var inter = null;
    showLoading("body");
    $.ajax({
        url: "/api/ah-interface/" + interfaceId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("获取接口失败");
                return;
            }
            inter = result.data;
        },
        error: function () {
            closeLoading();
            toastError("获取接口失败");
        }
    });
    return inter;
}

/**
 * 获取实体类
 */
function getClassCode(classId) {
    var clazz = null;
    showLoading("body");
    $.ajax({
        url: "/api/ah-class-code/" + classId,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("获取实体类失败");
                return;
            }
            clazz = result.data;
        },
        error: function () {
            closeLoading();
            toastError("获取实体类失败");
        }
    });
    return clazz;
}

/**
 * 获取实体类
 */
function getClassCodeByFullName(fullClassName) {
    var clazz = null;
    showLoading("body");
    $.ajax({
        url: "/api/ah-class-code-one?projectIdEq=" + projectSelected.id + "&fullNameEq=" + fullClassName,
        type: "GET",
        dataType: "JSON",
        async: false,
        success: function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError("获取实体类失败");
                return;
            }
            clazz = result.data;
        },
        error: function () {
            closeLoading();
            toastError("获取实体类失败");
        }
    });
    return clazz;
}

/**
 * 放弃修改接口确认框
 */
function confirmAbortChangingInterfaceDialog() {
    openConfirmDialog("确认放弃当前修改并刷新？", abortChangingInterface);
}

/**
 * 放弃修改接口
 */
function abortChangingInterface() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    if (interfaceSelected == null && !addingInterface) {
        toastWarn("请先选择接口或新增接口");
        return;
    }
    refreshInterfaceData();
    if (addingInterface) {
        $("#curInterface").removeAttr("title");
        $("#curInterface").html("新增中...");
    }
}

/**
 * 打开克隆接口对话框
 */
function openCloneInterfaceDialog() {
    openConfirmDialog("确认从当前页面克隆接口？<br/><span style='color: red; font-weight: bold;'>"
        + "注意：请先保存当前接口数据！</span>", cloneInterface);
}

/**
 * 从当前克隆一份接口
 */
function cloneInterface() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    if (interfaceSelected == null) {
        toastWarn("请先选择接口");
        return;
    }
    addingInterface = true;
    interfaceSelected = null;
    $(".interface").removeClass("list-selected");
    $("#curInterface").removeAttr("title");
    $("#curInterface").html("新增中...");
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
 * 显示当前页面的JSON
 */
function showCurrentJson() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    if (interfaceSelected == null && !addingInterface) {
        toastWarn("请先选择接口或新增接口");
        return;
    }
    var interInfoData = validAndGenInterData();
    if (interInfoData == null) {
        return;
    }
    openInterJsonDialog(formatJson(interInfoData), saveInterface, saveInterfaceAndExport);
}

/**
 * 验证并生成接口数据
 */
function validAndGenInterData() {
    // 1、基本信息
    var interInfoData = new Object();
    interInfoData.interNo = emptyStringToNull($("#baseInfo").find("input[name='interNo']").val());
    interInfoData.httpMethod = emptyStringToNull($("#baseInfo").find("select[name='httpMethod']").val());
    interInfoData.addDefaultPrefix = emptyStringToNull($("#baseInfo").find("select[name='addDefaultPrefix']").val());
    interInfoData.httpUrl = emptyStringToNull($("#baseInfo").find("input[name='httpUrl']").val());
    interInfoData.interDescr = emptyStringToNull($("#baseInfo").find("input[name='interDescr']").val());
    interInfoData.returnType = emptyStringToNull(returnTypeName);
    if (interInfoData.interNo == null || interInfoData.httpMethod == null || interInfoData.addDefaultPrefix == null
        || interInfoData.httpUrl == null || interInfoData.interDescr == null || interInfoData.returnType == null) {
        toastWarn("接口号、接口方法、追加默认前缀、接口URL、接口描述、接口返回类型不允许为空");
        return;
    }
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
                var fixedValueIsDigit = $(criteria).find("input[name='fixedValueIsDigit']").is(':checked');
                var emptyToNull = emptyStringToNull($(criteria).find("select[name='emptyToNull']").val());
                var toCriteriaList = getStringArrayFromList($(criteria).find("input[name='toCriteriaList']"));
                if (fromParam == null) {
                    continue;
                }
                var criteriaObj = new Object();
                criteriaObj.fromParam = fromParam;
                criteriaObj.descr = descr;
                criteriaObj.fixedValueIsDigit = fixedValueIsDigit;
                if (fixedValueIsDigit) {
                    criteriaObj.fixedValue = parseInt(fixedValue);
                } else {
                    criteriaObj.fixedValue = fixedValue;
                }
                criteriaObj.emptyToNull = parseInt(emptyToNull);
                criteriaObj.toCriteriaList = toCriteriaList;
                // 移除criteria的空值属性
                removeNullProperty(criteriaObj);
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
    // 移除param的空值属性
    removeNullProperty(interInfoData.param);
    // 3、返回信息
    interInfoData.result = new Object();
    interInfoData.result.resultCode = "1：操作成功  2：操作失败";
    interInfoData.result.errMsg = "错误消息";
    interInfoData.result.data = "返回数据";
    interInfoData.result.fieldList = new Array();
    var toObjectDataList = $("#toObject .to-data-line");
    // 重名验证
    var fullNameSet = new Set();
    for (var i = 0; i < toObjectDataList.length; i++) {
        var toObjectData = toObjectDataList[i];
        var fullName = $(toObjectData).attr("fullName");
        if (fullNameSet.has(fullName)) {
            toastError(fullName + "名称重复");
            return null;
        }
        fullNameSet.add(fullName);
    }
    for (var i = 0; i < toObjectDataList.length; i++) {
        var field = new Object();
        var toObjectData = toObjectDataList[i];
        var type = $(toObjectData).attr("type");
        if (type == "normal") {
            type = null;
        }
        field.name = $(toObjectData).attr("fullName");
        field.type = type;
        field.fromName = $(toObjectData).attr("oriFullName");
        field.descr = $(toObjectData).find(".to-descr").html();
        // 移除field的空值属性
        removeNullProperty(field);
        // 添加field
        interInfoData.result.fieldList.push(field);
    }
    // 移除result的空值属性
    removeNullProperty(interInfoData.param);
    // 移除最外层Object的空值属性
    removeNullProperty(interInfoData, ["param", "result"]);
    // 返回数据
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
function saveInterface(successCallback) {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    if (interfaceSelected == null && !addingInterface) {
        toastWarn("请先选择接口或新增接口");
        return;
    }
    var interInfoData = validAndGenInterData();
    if (interInfoData == null) {
        return;
    }
    var interInfoJson = JSON.stringify(interInfoData);
    // 保存参数
    var interParam = new Object();
    var method;
    if (interfaceSelected == null) {
        // 新增接口
        method = "POST";
    } else {
        // 更新接口
        method = "PUT";
        interParam.id = interfaceSelected.id;
    }
    interParam.interNo = interInfoData.interNo;
    interParam.httpUrl = interInfoData.httpUrl;
    interParam.httpMethod = interInfoData.httpMethod;
    interParam.addDefaultPrefix = interInfoData.addDefaultPrefix;
    interParam.interDescr = interInfoData.interDescr;
    interParam.returnType = interInfoData.returnType;
    interParam.dataJson = interInfoJson;
    interParam.ahProjectId = projectSelected.id;
    // 保存数据
    showLoading("body");
    $.ajax({
        url: "/api/ah-interface",
        type: method,
        data: JSON.stringify(interParam),
        contentType: "application/json",
        dataType: "JSON",
        success: function(result) {
            closeLoading();
            if (result.resultCode == "1") {
                if (typeof (successCallback) != "undefined") {
                    successCallback();
                } else {
                    toastSuccess("保存成功");
                }
                interJsonDialog.dialog("close");
                interfaceSelected = getInterface(result.data);
                $("#curInterface").html($("#baseInfo").find("input[name='httpUrl']").val());
                $("#curInterface").attr("title", interfaceSelected.httpUrl + "\n" + interfaceSelected.interNo
                    + "\n" + interfaceSelected.interDescr);
                refreshInterfaces(projectSelected.id);
            } else {
                toastError(result.errMsg);
            }
        },
        error: function () {
            closeLoading();
            toastError("保存失败");
        }
    });
}

/**
 * 保存并导出接口
 */
function saveInterfaceAndExport() {
    saveInterface(exportCurrentJson);
}

/**
 * 导出接口为JSON文件
 */
function exportCurrentJson() {
    downLoadFile({
        url: "/api/export-inter-json/" + interfaceSelected.id,
        data: {},
        isNewWinOpen: false,
        method: "GET"
    });
}

/**
 * 打开导入接口的JSON文件对话框
 */
function openImportInterJsonFilesDialog() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    var addButtons = {
        "导入": importInterJsonFiles,
        "取消": function() {
            jsonImportDialog.dialog("close");
        }
    };
    $("#jsonImportDialog").dialog("option", "buttons", addButtons);
    jsonImportDialog.dialog("open");
}

/**
 * 批量导入接口的JSON文件
 */
function importInterJsonFiles() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    // 批量导入JSON
    $("#jsonImportDialog").find("input[name='projectId']").val(projectSelected.id);
    showLoading("body");
    $.ajax({
        url : "/api/ah-interface-json-upload",
        type : "POST",
        cache : false,
        data : new FormData(jsonImportForm[0]),
        processData : false,
        contentType : false,
        success : function(result) {
            closeLoading();
            if (result.resultCode != "1") {
                toastError(result.errMsg);
            } else {
                toastSuccess("上传完成");
                jsonImportDialog.dialog("close");
                refreshInterfaces(projectSelected.id);
            }
        },
        error: function () {
            closeLoading();
            toastError("上传失败");
        }
    });
}

/**
 * 打包并导出当前工程的所有JSON
 */
function exportCurrentProject() {
    if (projectSelected == null) {
        toastWarn("请先选择工程");
        return;
    }
    downLoadFile({
        url: "/api/export-project-inter-json/" + projectSelected.id,
        data: {},
        isNewWinOpen: false,
        method: "GET"
    });
}

function extendClassFile() {
    var classFileHtml = $(".class_file:first").prop("outerHTML");
    $("#classFilesDiv").append(classFileHtml);
    $(".class_file:last").attr("name", "folder");
    $(".class_file:last").show();
    $("#classFilesDiv").append("<a href='#' onclick='deleteClassFiles(this);' title='点击删除'>X</a>");
}

function extendClassFolder() {
    var classFileHtml = $(".class_folder:first").prop("outerHTML");
    $("#classFilesDiv").append(classFileHtml);
    $(".class_folder:last").attr("name", "folder");
    $(".class_folder:last").show();
    $("#classFilesDiv").append("<a href='#' onclick='deleteClassFiles(this);' title='点击删除'>X</a>");
}

function deleteClassFiles(obj) {
    $(obj).prev().remove();
    $(obj).remove();
}