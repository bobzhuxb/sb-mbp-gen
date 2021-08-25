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
    refreshLayoutWest();
}

/**
 * 初始化左侧数据和事件
 */
function refreshLayoutWest() {
    refreshProjects();
}

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
                        + project.name + "_" + project.descr + "' "
                        + "urlPrefix='" + project.urlPrefix + "'" + "title='" + project.descr + "' "
                        + "onclick='selectProject(this);'><span>" + project.name
                        + "</span><a href='#' style='margin-left: 10px;' title='点击修改' "
                        + "onclick='openUpdateProjectDialog(\"" + project.id + "\")'>M</a>"
                        + "<a href='#' style='margin-left: 10px;' title='点击删除' "
                        + "onclick='openDeleteProjectDialog(\"" + project.id + "\")'>X</a></div>";
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
function openUpdateProjectDialog(projectId) {
    $("#projectDialog").find("input[name='id']").val(projectId);
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
function openDeleteProjectDialog() {

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
 * 添加接口
 */
function addInterface() {
    alert("add-interface");
}

/**
 * 工程选中事件
 */
function selectProject(project) {
    var newProjectId = $(project).attr("identify");
    if (projectSelected == null || newProjectId != projectSelected.id) {
        interfaceSelected = null;
    }
    projectSelected = getProject(newProjectId);
    refreshAllData();
    // 获取接口
    var interfaces = [
        {"id": "inter001", "interNo" : "list", "httpUrl" : "/cmn-after-sale", "interDescr": "售后服务页面", "httpMethod": "GET", "addDefaultPrefix": "yes"},
        {"id": "inter002", "interNo" : "list", "httpUrl" : "/cmn-city-all", "interDescr": "归属地市列表页面", "httpMethod": "GET", "addDefaultPrefix": "yes"},
        {"id": "inter003", "interNo" : "all", "httpUrl" : "/cmn-financial-report-supervise-export", "interDescr": "财务报表导出", "httpMethod": "GET", "addDefaultPrefix": "yes"},
        {"id": "inter004", "interNo" : "list", "httpUrl" : "/cmn-purchase-contract", "interDescr": "采购合同页面", "httpMethod": "GET", "addDefaultPrefix": "yes"},
        {"id": "inter005", "interNo" : "all", "httpUrl" : "/mis-accept-export", "interDescr": "单条验收单明细导出", "httpMethod": "GET", "addDefaultPrefix": "yes"},
    ];
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
    if (changeInterface) {
        refreshAllData();
    }
}

/**
 * 从后台获取project信息
 */
function getProject(projectId) {
    // TODO
    var project = new Object();
    project.id = "proj001";
    project.name = "toronto";
    project.descr = "药械进销存子系统";
    project.urlPrefix = "/api/drag";
    return project;
}

/**
 * 从后台获取interface信息
 */
function getInterface(interfaceId) {
    // TODO
    var inter = new Object();
    inter.id = "inter001";
    inter.interNo = "list";
    inter.httpUrl = "/cmn-after-sale";
    inter.interDescr = "售后服务页面";
    inter.httpMethod = "GET";
    inter.addDefaultPrefix = "yes";
    return inter;
}

/**
 * 刷新页面数据（配置文件内容）
 */
function refreshAllData() {
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