<!DOCTYPE html>
<html lang="en">
<head>
    <title>登陆页面</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/taoshelf/static/css/bootstrap.css">
    <script src="${request.contextPath}/taoshelf/static/js/jquery.js"></script>
    <script src="${request.contextPath}/taoshelf/static/js/bootstrap.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div class="card" style="margin-top: 35px;margin-bottom: 20px">
                <form action="/taoshelf/8088/login" method="post">
                    <div class="card-body">
                        <h3 class="card-title">管理员登陆</h3>
                        <div class="form-group">
                            <label for="username">账号:</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名" required>
                        </div>
                        <div class="form-group">
                            <label for="password">密码:</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required>
                        </div>
                    </div>
                    <div class="card-footer">
                        <button type="submit" class="btn btn-lg btn-primary">登录</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-md-3"></div>
    </div>
</div>
</body>
</html>