<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String url = request.getSession().getAttribute("url").toString();
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
    <script src="/invoice/lib/jquery/jquery-2-1-4-min.js"></script>
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
        img{max-width: 100%; height: auto;}
    </style>
</head>
<body>
<%--http://61.152.146.52:9955/agency-front/agency/invoice/computerbill/industryMain.do?noise=eaa0036dff8b49818ecc77041fe0bda1&eBillPicUrls=1a1050027010d3e3ae424b3495bb631b8e128b0125f9d6528020eb6a9cd826724aad34944349d908c31b5be4989986183c17fa987c3c950560f690a53d9924b8ef9ddc0236b4cfa967d8d1f871e89f512064ebf063fef3ffd6102b5bf8cd465436cd16fc5432a8eef4ad3feddbea74255ea8a2b315ff2e61b801c2a29a341767403d8728349fe05cbfbf3cc224c67b7d3c3a8b652bfb2cad3bc6a4cd24f3b0c3a0434ecc413df7480a7c009bba64d623--%>
<div id="app" class="winDiv">
    <div id="weixinTip" :style="{display:showStatus}" @click="hiddenTips"><p><img src="/invoice/lib/img/wechat_tip.png" alt="微信打开"/></p></div>
    <el-button type="primary" size="small" @click="downloadImg">下载图片</el-button>
    <iframe ref="iframe" src="<%=url%>" :style="{width:curWidth+'px',height:curHeight+'px'}" frameborder="0"></iframe>
    <el-row><el-col :span="24" style="text-align: center;"><el-button type="primary" style="width: 80%;" @click="back">确定</el-button></el-col></el-row>
</div>
<script type="text/javascript">

    new Vue({
        el: '#app',
        data: {
            showStatus:'none',
            curHeight:300,
            curWidth:200
        },
        created(){
            this.beforeMountHeight()
        },
        methods: {
            hiddenTips:function(){
                this.showStatus='none';
            },
            back:function(){
                window.history.go(-1);
            },
            beforeMountHeight:function(){
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                let w = document.documentElement.clientWidth || document.body.clientWidth;
                this.curHeight = h*0.7;
                this.curWidth=w;
            },
            downloadImg:function(){
                this.showStatus='block';
                let winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight;
                $("#weixinTip").css({
                    "position":"fixed",
                    "left":"0",
                    "top":"0",
                    "height":winHeight,
                    "width":"100%",
                    "z-index":"1000",
                    "background-color":"rgba(0,0,0,0.8)",
                    "filter":"alpha(opacity=80)",
                });
                $("#weixinTip p").css({
                    "text-align":"center",
                    "margin-top":"10%",
                    "padding-left":"5%",
                    "padding-right":"5%"
                });
            }
        }
    });
</script>
</body>
</html>