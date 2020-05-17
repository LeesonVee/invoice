<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String url = request.getSession().getAttribute("qrCodeUrl").toString();
//    String url = "http://192.168.40.19:9955/agency-front/agency/invoice/computerbill/industryMain.do?noise=660dfbea011640e59dcce494441d79b6&eBillPicUrls=1a1050027010d3e3ae424b3495bb631b8e128b0125f9d6528020eb6a9cd826724aad34944349d908c31b5be49899861841af8d6b7e9f1ab8fd394b0ed022b38978edbb1d091e1e51616bcf0d82891c62d2834ce421b7bd3d8cf566f4778acbc2604ef04e1f01312d90dabb33c03bbf91c5103e46dbcf7a8f1b547d219b54427535b4c6f49c56d24b22dc90879229cb4aafc9401bba1cc31e452a85b0bb232eb774e28a53f1aefd67c1f44ae81e6b6a32";
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=device-dpi" />
    <title>我的票据</title>
    <!-- 引入样式 -->
    <link rel="stylesheet" href="/invoice/lib/eleme/element-min.css">

    <!-- 引入组件库 -->
    <script src="/invoice/lib/vue2/vue.js"></script>
    <script src="/invoice/lib/eleme/element-min.js"></script>
    <style type="text/css">
        html,head,body{
            margin: 0px;
            padding: 0px;

        }
        .winDiv{
            margin: 0px;
            padding: 0px;
            width:100%;
            height:100%;
        }
    </style>
</head>
<body>
<%--http://61.152.146.52:9955/agency-front/agency/invoice/computerbill/industryMain.do?noise=eaa0036dff8b49818ecc77041fe0bda1&eBillPicUrls=1a1050027010d3e3ae424b3495bb631b8e128b0125f9d6528020eb6a9cd826724aad34944349d908c31b5be4989986183c17fa987c3c950560f690a53d9924b8ef9ddc0236b4cfa967d8d1f871e89f512064ebf063fef3ffd6102b5bf8cd465436cd16fc5432a8eef4ad3feddbea74255ea8a2b315ff2e61b801c2a29a341767403d8728349fe05cbfbf3cc224c67b7d3c3a8b652bfb2cad3bc6a4cd24f3b0c3a0434ecc413df7480a7c009bba64d623--%>
<div id="app" class="winDiv">
    <iframe ref="iframe" src="<%=url%>" :style="{width:curWidth+'px',height:curHeight+'px'}" frameborder="0"></iframe>
</div>
<script type="text/javascript">

    new Vue({
        el: '#app',
        data: {
            curHeight:300,
            curWidth:200
        },
        created(){
            this.beforeMountHeight()
        },
        methods: {
            beforeMountHeight:function(){
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                let w = document.documentElement.clientWidth || document.body.clientWidth;
                this.curHeight = h;
                this.curWidth=w;
            },
        }
    });
</script>
</body>
</html>