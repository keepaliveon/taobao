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
    <script src="${request.contextPath}/static/js/jquery.js"></script>
    <script src="${request.contextPath}/static/js/bootstrap.js"></script>
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
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/8088/user">用户管理</a>
            </li>
            <li class="nav-item dropdown active">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    卡密管理
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item active" href="/taoshelf/8088/card_new">未使用卡密</a>
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
        <h2 class="col-6">未使用卡密</h2>
        <div class="col-6">
            <button class="btn btn-danger float-right" onclick="deleteKey()" style="margin-left: 15px">批量删除</button>
            <button onclick="javascript:$('#myModal').modal('show')" class="btn btn-primary float-right">批量导入卡密</button>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover table-sm">
            <thead>
            <tr>
                <th><input type="checkbox" id="select_all" onclick="selectAll(this)"></th>
                <th>#</th>
                <th>卡密</th>
                <th>时长(天)</th>
                <th>添加时间</th>
            </tr>
            </thead>
            <tbody>
            <#list cards as card>
            <tr>
                <th><input type="checkbox" value="${card.id}"></th>
                <td>${card_index+1}</td>
                <td>${card.id}</td>
                <td>${card.day}</td>
                <td>${card.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
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
                       :total="${card_total?c}">
        </el-pagination>
        <p style="margin-bottom: 15px"></p>
    </div>
</div>
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/taoshelf/8088/import_cards" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h4 class="modal-title">批量导入卡密</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <label>卡密时长<input type="number" required name="day">天</label><br>
                    <div class="form-group">
                        <input type="file" id="file" name="file" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                    <button type="submit" class="btn btn-success">导入</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script>
    function selectAll(checkbox) {
        $('input[type=checkbox]').prop('checked', $(checkbox).prop('checked'));
    }

    function deleteKey() {
        let data = [];
        $("input[type='checkbox']:checked").each(function () {
            let value = $(this).val();
            if (value !== 'on') {
                data.push(value);
            }
        });
        $.ajax({
            type: 'post',
            url: "/taoshelf/8088/delete_cards",
            data: {
                "data": JSON.stringify(data)
            },
            beforeSend: function () {
                return confirm("确认删除？");
            },
            success: function (response) {
                alert("删除" + response + "个");
                location.reload();
            }
        })
    }

    new Vue({
        el: '#app',
        methods: {
            handleSizeChange(val) {
                window.location.href = "/taoshelf/8088/card_new?page_no=" + this.currentPage + "&page_size=" + val;
            },
            handleCurrentChange(val) {
                window.location.href = "/taoshelf/8088/card_new?page_no=" + val + "&page_size=" + this.pageSize;
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