<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String idCardNo = request.getParameter("idCardNo");
    String cardNo = request.getParameter("cardNo");
//    System.out.println(idCardNo+cardNo);
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
        }
        .el-container .box{
            height:82px;
            border-bottom: 1px solid #696976;
            cursor: pointer;
        }
        .el-header, .el-footer {
            background-color: #B3C0D1;
            color: #333;
            text-align: center;
            line-height: 60px;
            padding: 0;
        }

        .el-aside {
            color: #333;
            text-align: center;
            line-height: 80px;
            height:80px;
        }

        .el-main {
            background-color: #E9EEF3;
            color: #333;
            text-align: center;
            padding: 0;
        }
        .el-row .nomal{
            height:25px;
            line-height:25px
        }
        .el-row .big{
            font-weight: bold;
            font-size: 18px;
            color: #ff9913;
            height:30px;
            line-height:30px;
        }
        .amount{
            text-align: right;
            padding-right: 5px;
        }
        .colors{
            color: #a9a6ae;
        }
        .font-weight{
            font-weight: bold;
        }
        div .item{
            width: 100%;
            cursor: pointer;
            border: 1px solid #696976;
            border-top:none;
            border-right: none;
        }
        .circle{
            width: 15px;
            height: 15px;
            background-color: red;
            border-radius: 50%;
            display: inline-block;
            margin-right: 5px;
        }

    </style>
</head>
<body>
<div id="app" class="winDiv">
    <el-container :style="{height:curHeight+'px'}">
        <el-header>
            <el-row>
                <el-col :span="6"><div class="item" @click="queryInvoice('1')">一周</div></el-col>
                <el-col :span="6"><div class="item" @click="queryInvoice('2')">一个月</div></el-col>
                <el-col :span="6"><div class="item" @click="queryInvoice('3')">六个月</div></el-col>
                <el-col :span="6"><div class="item" @click="queryInvoice('4')">更多</div></el-col>
            </el-row>
        </el-header>
        <el-main>
            <el-container class="box" v-for="item in invoiceData">
                <el-aside width="60px">
                    <el-row><div class="big"><el-col :span="24">&nbsp;</el-col></div></el-row>
                    <el-row><div class="nomal"><el-col :span="24"><div class="circle"></div></el-col></div></el-row>
                    <el-row><div class="nomal"><el-col :span="24">&nbsp;</el-col></div></el-row>

                </el-aside>
                <el-main style="text-align: left;">
                    <el-row>
                        <div class="nomal"><el-col :span="24"><span class="font-weight">票据类型：</span>{{item.TRADETYPE}}</el-col></div>
                    </el-row>
                    <el-row >
                        <div class="big">
                            <el-col :span="12">票据金额</el-col>
                            <el-col :span="12" class="amount">￥{{item.AMOUNT}}</el-col>
                        </div>
                    </el-row>
                    <el-row>
                        <div class="nomal colors">
                            <el-col :span="24"><span style="font-weight: bold;">交易日期：</span><span style="font-size: 13px;">{{item.TRADETIME}}</span></el-col>
                        </div>
                    </el-row>
                </el-main>
            </el-container>
            <div v-if="invoiceData.length==0" style="text-align: center;">无数据</div>
        </el-main>
    </el-container>
</div>
<script type="text/javascript">
    var idCardNo= <%=idCardNo%>;
    var cardNo= <%=cardNo%>;
    new Vue({
        el: '#app',
        data: {
            msg:123,
            curHeight:500,
            currentDate:new Date(),
            starttime:'',
            endtime:'',
            invoiceData:[],
//            invoiceData:[{
//                TRADETYPE:'门诊',
//                TRADETIME:'2020年05月14日 13:44:48',
//                AMOUNT:18,
//            },{
//                TRADETYPE:'门诊',
//                TRADETIME:'2020年05月09日 09:23:56',
//                AMOUNT:108,
//            }]
        },
        mounted(){
            this.beforeMountHeight()
        },
        methods:{
            beforeMountHeight:function(height){
                height = height||0;
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                this.curHeight = h-height;
            },
            queryInvoice:function(type){
                debugger;
                switch (type){
                    case '1':
                        this.setDateRange(7);
                        break;
                    case '2':
                        this.setMonth(1);
                        break;
                    case '3':
                        this.setMonth(6);
                        break;
                    case '4':break;
                    default:break;
                }
                var params={
                    starttime:this.starttime,
                    endtime:this.endtime,
                    idCardNo:idCardNo,
                    cardNo:cardNo
                }
                this.requestData(params);
                console.info(this.starttime);
                console.info(this.endtime);
            },
            requestData:function(params){
                const me = this;
                $.ajax({
                    type: "POST",//方法类型
                    url: "/invoice/queryInvoiceInfo.html",
                    dataType: "json",
                    data:params,
                    success: function(data) {//返回页面内容
                        var body = data.message;
                        if(body.errcode=='1'){
                            me.invoiceData=eval(body.info.tradeinfo);
                        }
                        debugger;
                    },
                    error:function(XMLHttpRequest, strError, strObj){
                        console.log("XMLHttpRequest:"+XMLHttpRequest);
                        console.log("strError:"+strError);
                        console.log("strObj:"+strObj);
                    }
                })
            },
            setDateRange:function(n){
                var date = this.currentDate;
                var year = date.getFullYear();
                var month = (date.getMonth()+1);
                var day = date.getDate();
                this.endtime=this.dateToString(year,month,day);
                date.setDate(date.getDate()-n);
                year = date.getFullYear();
                month = (date.getMonth()+1);
                day = date.getDate();
                this.starttime=this.dateToString(year,month,day,true);

            },
            setMonth:function(n){
                var date = this.currentDate;
                var year = date.getFullYear();
                var month = (date.getMonth()+1);
                var day = date.getDate();
                this.endtime=this.dateToString(year,month,day);
                if(month>n){
                    this.starttime=this.dateToString(year,month-n,day,true);
                }else{
                    this.starttime=this.dateToString(year-1,12+month-n,day,true);
                }
            },
            dateToString:function(year,month,day,isStart){
                var time=year+'-';
                if(month<10){
                    time+='0'+month+'-';
                }else{
                    time+=month+'-';
                }
                if(day<=10){
                    time+='0'+day;
                }else{
                    time+=day;
                }
                if(isStart){
                    time+= ' 00:00:00';
                }else{
                    time+= ' 23:59:59';
                }
                return time;
            }

        }
    });
</script>
</body>
</html>
