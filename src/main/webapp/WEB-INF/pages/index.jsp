<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<div id="app" class="winDiv" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
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
            <div v-for="item in invoiceData" @click="watchInvoice(item)">
            <el-container class="box">
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
            </div>
            <div v-if="invoiceData.length==0" style="text-align: center;">无数据</div>
        </el-main>
    </el-container>
    <el-dialog title="日期范围" :visible.sync="dialogVisible" width="100%" :before-close="handleClose" :close-on-click-modal="false">
        <el-form :form="form" size="mini">
            <el-form-item>
                <el-col :span="9">
                    <el-date-picker type="date" placeholder="选择时间" v-model="form.starttime" style="width: 100%;"></el-date-picker>
                </el-col>
                <el-col class="line" :span="1">&nbsp;-</el-col>
                <el-col :span="9">
                    <el-date-picker type="date" placeholder="选择时间" v-model="form.endtime" style="width: 100%;"></el-date-picker>
                </el-col>
                <el-col :span="3" style="padding: 0 5px;">
                    <el-button type="primary" @click="dateRangeQueryInvoice">确定</el-button>
                </el-col>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            popperClass:'popper-class',
            dialogVisible:false,
            loading:false,
            curHeight:500,
            currentDate:new Date(),
            starttime:'',
            endtime:'',
            invoiceData:[],
            form:{
                starttime:'',
                endtime:'',
                dateRange:''
            },
        },
        mounted(){
            this.beforeMountHeight();
            this.queryInvoice('1');
        },
        methods:{
            beforeMountHeight:function(height){
                height = height||0;
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                this.curHeight = h-height;
            },
            queryInvoice:function(type){

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
                    case '4':
                        this.form.dateRange='';
                        this.dialogVisible = true;
                        break;
                    default:break;
                }
                if(type!='4'){
                    this.loading = true;
                    var params={
                        starttime:this.starttime,
                        endtime:this.endtime
                    }
                    debugger;
                    this.requestData(params);
                }

            },
            watchInvoice:function(item){
                debugger;
                this.loading = true;
                var me= this;
                $.ajax({
                    type: "POST",//方法类型
                    url: "/invoice/queryInvoiceItemInfo.html",
                    dataType: "json",
                    data:item,
                    success: function(data) {//返回页面内容
                        me.loading=false;
                        var body = data.message;
                        if(body.errcode=='1'){
                            window.location.href='/invoice/iframe.html';
                        }else{
                            me.myMessage(body.info.errinfo,'warning');
                        }
                    },
                    error:function(XMLHttpRequest, strError, strObj){
                        me.loading=false;
                        me.myMessage('获取发票信息失败','warning');
                    }
                })

            },
            handleClose:function(){
                this.dialogVisible = false;
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
                        }else{
                            me.myMessage('获取发票信息失败','warning');
                        }
                        me.loading=false;
                    },
                    error:function(XMLHttpRequest, strError, strObj){
                        me.loading=false;
                        me.myMessage('获取发票信息失败','warning');
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
            },
            dateRangeQueryInvoice:function(){
                this.dialogVisible = false;
                if(!this.form.starttime||!this.form.endtime){
                    return;
                }
                var startTime = new Date(this.form.starttime).getTime();
                var endTime = new Date(this.form.endtime).getTime();
                if(startTime>endTime){
                    this.starttime=this.rangeDateToString(this.form.endtime);
                    this.endtime=this.rangeDateToString(this.form.starttime,true);
                }else{
                    this.starttime=this.rangeDateToString(this.form.starttime);
                    this.endtime=this.rangeDateToString(this.form.endtime,true);
                }
                this.loading = true;
                var params={
                    starttime:this.starttime,
                    endtime:this.endtime
                };
                this.requestData(params);
            },
            rangeDateToString:function(date,full){
                let d = new Date(date);
                let resDate = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate());
                if(full){
                    resDate+=' 23:59:59';
                }else{
                    resDate+=' 00:00:00';
                }
                return resDate;
            },
            p:function(s) {
                return s < 10 ? '0' + s : s
            },
            myMessage:function(msg,type){
                this.$message({
                    message: msg,
                    type: type
                });
            }

        }
    });
</script>
</body>
</html>
