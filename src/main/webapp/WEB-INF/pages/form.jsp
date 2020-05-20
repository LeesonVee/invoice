<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
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
            background-color: #E9EEF3;
        }
        .winDiv .bgImg{
            margin: 0px;
            padding: 0px;
            width:100%;
            height:100%;
            background: url("/invoice/lib/img/bg.png") no-repeat;
            background-size:100% 100%;
            background-attachment:fixed;
        }
        .el-container .box{
            height:81px;
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
        .remark_top{
            padding-top: 20px;
        }
        .remark{
            padding-top: 2px;
            font-size: 10px;
            /*color: #D6D6D6;*/
        }
    </style>
</head>
<body>
<div id="app" class="winDiv">
    <el-row style="height:30%;"><div class="bgImg"><el-col :span="24">&nbsp;</el-col></div></el-row>
    <el-row>
        <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px" class="demo-ruleForm">
            <el-col style="padding-top:10px;padding-right: 50px;">
                <template v-if="typeNum=='1'">
                    <el-form-item label="身份证" prop="idCardNo">
                        <el-input v-model="ruleForm.idCardNo"></el-input>
                    </el-form-item>
                    <el-form-item label="姓　名" prop="name">
                        <el-input v-model="ruleForm.name"></el-input>
                    </el-form-item>
                </template>
            </el-col>
        </el-form>
    </el-row>
    <el-row><el-col :span="24" style="text-align: center;"><el-button type="primary" style="width: 80%;" @click="queryInvoiceByIdCard('ruleForm')">查询</el-button></el-col></el-row>
    <el-row class="remark remark_top"><el-col :span="24">1.系统提供本院自2020年5月22日之后的电子票据查询功能。&nbsp;&nbsp;</el-col></el-row>
    <el-row class="remark"><el-col :span="24">2.如需查验电子票据,可通过财政部财政电子票据查验平台http://pjcy.mof.gov.cn/进行。</el-col></el-row>
    <el-row class="remark"><el-col :span="24">3.也可通过关注上海普陀健康公众号查询电子票据。&nbsp;&nbsp;</el-col></el-row>
</div>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            imgHeight:300,
            curHeight:300,
            curWidth:200,
            msg:123,
            typeNum:'1',
            ruleForm: {
                name:'',
                cardNo: '',
                idCardNo: '',
                type:'1',
            },
            rules: {
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' }
                ],
                idCardNo: [
                    { required: true, message: '请输入身份证', trigger: 'blur' },
                    { min: 18, max: 18, message: '请输入18位的身份证信息', trigger: 'blur' }
                ]
            }
        },
        created(){
            this.beforeMountHeight()
        },
//        mounted(){
//            this.beforeMountHeight()
//        },
        methods: {
            changeType:function(val){
                this.typeNum=val;
            },
            submitForm:function(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm:function(formName) {
                this.$refs[formName].resetFields();
            },
            queryInvoiceByIdCard:function(formName){
                const me = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const me= this;
                        $.ajax({
                            type:'POST',
                            url: "/invoice/checkIdCardNo.html",
                            dataType: "json",
                            data:this.ruleForm,
                            success: function(data) {//返回页面内容
                                if(data){
                                    window.location.href='/invoice/index.html?platformType=2&idCardNo='+me.ruleForm.idCardNo;
                                }else{
                                    me.myMessage('身份证和名字不匹配','error');
                                }
                            },
                            error:function(XMLHttpRequest, strError, strObj){
                                me.myMessage('身份证验证异常','error')
                            }
                        })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });

            },
            myMessage:function(message,type){
                this.$message({
                    showClose: false,
                    message: message,
                    type: type
                });
            },
            requestData:function(params){

            },
            beforeMountHeight:function(){
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                let w = document.documentElement.clientWidth || document.body.clientWidth;
                this.curHeight = h*0.5;
                this.curWidth=w;
            },
        }
    });
</script>
</body>
</html>