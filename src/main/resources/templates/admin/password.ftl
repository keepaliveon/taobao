<!DOCTYPE html>
<html lang="en">
<head>
    <title>密码修改</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${request.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.4.8/lib/theme-chalk/index.css">
    <style>
        th, td {
            white-space: nowrap;
            text-align: center
        }
    </style>
    <script src="${request.contextPath}/static/js/jquery.js"></script>
    <script src="${request.contextPath}/static/js/bootstrap.js"></script>
</head>
<body>
<nav class="navbar bg-dark navbar-expand-md navbar-dark">
    <a class="navbar-brand" href="#">有梦管理后台</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/8088/admin">运行监控</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/8088/user">用户管理</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    卡密管理
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/taoshelf/8088/card_new">未使用卡密</a>
                    <a class="dropdown-item" href="/taoshelf/8088/card_old">已使用卡密</a>
                </div>
            </li>
            <li class="nav-item dropdown active">
                <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">${admin.username}</a>
                <div class="dropdown-menu">
                    <a class="dropdown-item active" href="/taoshelf/8088/password">修改密码</a>
                    <a class="dropdown-item" href="/taoshelf/8088/logout">安全退出</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="container" style="padding-top: 15px">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <#if info?exists>
                <#if info.type="success">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>提示!</strong> ${info.message}
            </div>
                <#elseif info.type="error">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>错误!</strong> ${info.message}
            </div>
                </#if>
            </#if>
            <h2>密码修改</h2>
            <form method="post" action="/taoshelf/8088/do_password_mod" onsubmit="return validate()">
                <div class="form-group">
                    <label>旧密码：</label>
                    <input type="password" class="form-control" id="old_password" name="oldPassword"
                           placeholder="请输入原密码" required autocomplete="off">
                </div>
                <div class="form-group">
                    <label>新密码：</label>
                    <input type="password" class="form-control" id="new_password" name="newPassword"
                           onkeyup="validate()" placeholder="请输入新密码" required autocomplete="off">
                </div>
                <div class="form-group">
                    <label>新密码：</label>
                    <input type="password" class="form-control" id="new_password_" onkeyup="validate()"
                           placeholder="请再次输入新密码" autocomplete="off">
                </div>
                <input type="submit" class="form-control btn btn-primary" value="修改密码">
                <p id="info_message" style="color: red">${message!}</p>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
</body>
<script>
    let password_1 = $("#new_password");
    let password_2 = $("#new_password_");
    let info = $("#info_message");

    function validate() {
        if (password_1.val() !== password_2.val()) {
            info.text("两次密码输入不一致");
            return false;
        } else {
            info.text("");
            return true;
        }
    }
</script>
</html>