/**
 * ajax封装向后台请求要发�?�的数据
 * url: 发�?�请求的 URL
 * temp: 要发送到服务器的数据,格式{name:name}
 * type: post/get
 * dataType: xml/html/script/json/jsonp/text
 */
function ajaxValidate(temp, url, type, dataType) {
    var res = null;
    //封装向后台请求要发�?�的数据数据
    $.ajax({
        url: url,//请求路径和我们的form里的action �?�?
        data: temp,					//请求数据配置
        type: type,					//请求方式
        dataType: dataType,				//数据类型
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',//内容配置信息
        cache: false,//是否使用缓存
        async: false,//是否是异�?
        // 当请求成功时运行的函�?
        success: function (result, status, xhr) {
            res = result;
        },
        // 如果请求失败要运行的函数
        error: function (xhr, status, error) {
            //alert("错误提示�? " + xhr.status + " " + xhr.statusText + " " + status);
        },
        // 请求完成时运行的函数（在请求成功或失败之后均调用，即�? success �? error 函数之后�?
        complete: function (xhr, status) {

        }
    });
    return res;
}

/**
 * Post 提交数据
 * @param to url
 * @param p 参数
 */
function doPost(to, p) {
    var myForm = document.createElement("form");
    myForm.style.display = 'none';
    myForm.method = "post";
    myForm.action = to;
    for (var i in p){
        var myInput = document.createElement("input");
        myInput.setAttribute("name", i);  // 为input对象设置name
        myInput.setAttribute("value", p[i]);  // 为input对象设置value
        myForm.appendChild(myInput);
    }
    document.body.appendChild(myForm);
    myForm.submit();
    document.body.removeChild(myForm);  // 提交后移除创建的form
}
/**
 * ajax封装向后台请求要发�?�的数据
 * url: 发�?�请求的 URL
 * temp: 要发送到服务器的数据,格式{name:name}
 * type: post/get
 * dataType: xml/html/script/json/jsonp/text
 */
function ajaxValidateForJson(temp, url, type) {
    var res = null;
    //封装向后台请求要发�?�的数据数据
    $.ajax({
        url: url,//请求路径和我们的form里的action �?�?
        data: JSON.stringify(temp),					//请求数据配置
        type: type,					//请求方式
        dataType: 'json',				//数据类型
        contentType : 'application/json;charset=utf-8', //设置请求头信�?
        cache: false,//是否使用缓存
        async: false,//是否是异�?
        // 当请求成功时运行的函�?
        success: function (result, status, xhr) {
            res = result;
        },
        // 如果请求失败要运行的函数
        error: function (xhr, status, error) {
            //alert("错误提示�? " + xhr.status + " " + xhr.statusText + " " + status);
        },
        // 请求完成时运行的函数（在请求成功或失败之后均调用，即�? success �? error 函数之后�?
        complete: function (xhr, status) {

        }
    });
    return res;
}
//ajax全局事件,session失效ajax处理
$(document).ajaxComplete(function(event, xhr, settings) {
    if(xhr.getResponseHeader("sessionstatus") === "timeOut"){
        if(xhr.getResponseHeader("loginPath")){
            alert("会话过期，请重新登陆!");
            window.location.replace("/laser/");
        }else{
            alert("请求超时请重新登陆 !");
        }
    }
});

//重置密码
function reset_password() {
    if ($("#passwordOld").val() == "") {
        alert("旧密码必须填写");
        return false;
    }
    if ($("#passwordNew").val() == "") {
        alert("新密码必须填写");
        return false;
    }
    if ($("#passwordNew_confirm").val() == "") {
        alert("确认密码必须填写");
        return false;
    }
    if ($("#passwordNew").val() != $("#passwordNew_confirm").val()) {
        alert("两次密码填写不一致");
        return false;
    }
    var temp = {passwordOld:$("#passwordOld").val(),passwordNew:$("#passwordNew").val()};
    var data = ajaxValidate(temp, "/laser/user/reset_password", "POST", "json");
    if (data.code === 0) {
        alert(data.data);$.unblockUI();
    }else{
        alert(data.msg);
    }
}

function clearPwdUI(){
    $("#passwordOld").val('');
    $("#passwordNew").val('');
    $("#passwordNew_confirm").val('');
}

function cancle_repwd() {
    $.unblockUI();
}

function cancle_logout() {
    $.unblockUI();
}