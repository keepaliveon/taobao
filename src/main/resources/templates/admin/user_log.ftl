<!DOCTYPE html>
<html lang="en">
<head>
    <title>有梦管理后台</title>
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
    <script src="/taoshelf/static/js/jquery.js"></script>
    <script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>
    <script src="/taoshelf/static/js/bootstrap.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.min.js"></script>
    <script src="https://unpkg.com/element-ui@2.4.8/lib/index.js"></script>
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
            <li class="nav-item active">
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
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">${admin.username}</a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/taoshelf/8088/password">修改密码</a>
                    <a class="dropdown-item" href="/taoshelf/8088/logout">安全退出</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="container-fluid" id="app">
    <div class="row" style="margin-top: 15px">
        <h2 class="col-12">${nick}用户日志列表</h2>
    </div>
    <div class="table-responsive">
        <table class="table table-hover table-sm">
            <thead>
            <tr>
                <th>#</th>
                <th>类型</th>
                <th>时间</th>
                <th>信息</th>
            </tr>
            </thead>
            <tbody>
            <#list logs as log>
            <tr>
                <td>${log_index+1}</td>
                <td>${log.type}</td>
                <td>${log.time?string('yyyy-MM-dd HH:mm:ss')}</td>
                <td>${log.message}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="currentPage"
                       :page-sizes="[15, 100, 200, 300, 400]"
                       :page-size="pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="${log_total?c}">
        </el-pagination>
        <p style="margin-bottom: 15px"></p>
    </div>
</div>
</body>
<script>
    new Vue({
        el: '#app',
        methods: {
            handleSizeChange(val) {
                window.location.href = "/taoshelf/8088/user_log?nick=${nick}&page_no=" + this.currentPage + "&page_size=" + val;
            },
            handleCurrentChange(val) {
                window.location.href = "/taoshelf/8088/user_log?nick=${nick}&page_no=" + val + "&page_size=" + this.pageSize;
            }
        },
        data() {
            return {
                currentPage: ${page_no?c},
                pageSize: ${page_size?c}
            };
        }
    })
</script>
</html>