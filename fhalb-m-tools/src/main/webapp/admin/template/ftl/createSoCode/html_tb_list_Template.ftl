<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>FH Admin</title>
    <!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 10]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
    <!-- Meta -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="author" content="FH Admin QQ313596790" />

    <link rel="icon" href="../../../assets/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../../assets/fonts/material/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="../../../assets/fonts/fontawesome/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../../../assets/plugins/animation/css/animate.min.css">
    <link rel="stylesheet" href="../../../assets/css/style.css">
    
    <!-- vue -->
	<script src="../../../assets/js-vue/vue.js"></script>
    <!--全局配置-->
    <script src="../../../assets/js-v/config.js"></script>
    <style type="text/css">
		table{
			border-collapse: collapse;
			border: 0px solid #DFDFDF;
		}
		table td {
			border-top: 0;
			border-right: 1px solid #DFDFDF;
			border-bottom: 1px solid #DFDFDF;
			border-left: 0;
		}
		table tr.lastrow td {
			border-bottom: 0;
		}
		table tr td.lastCol {
			border-right: 0;
		}
	</style>
</head>

<body style="background-color: white;">
    
    <!-- [加载状态 ] start -->
    <div class="loader-bg">
        <div class="loader-track">
            <div class="loader-fill"></div>
        </div>
    </div>
    <!-- [ 加载状态  ] End -->

	    <!-- [ Hover-table ] start -->
        <div style="padding-top: -35px;" id="app">
            <div>
	
				<div>
    				<div>
						<table width="99.8%" border="0" cellpadding="1" cellspacing="1" align="center" style="border:1px solid #DFDFDF;border-collapse:collapse;border-top: 0px;border-right: 0px;border-left: 0px;">
							<thead>
								<tr>
									<td style="width: 20px;text-align: center;" id="cts">
                                    	<input type="checkbox" name="fhcheckbox" id="zcheckbox">
									</td>
									<td style="text-align: center;width:20px;">NO</td>
								<#list fieldList as var>
									<td style="text-align: center;">${var[2]}</td>
								</#list>
									<td style="text-align: center;border-right: 0px;">操作</td>
								</tr>
							</thead>
													
							<tbody>
								<!-- 开始循环 -->	
								<template v-for="(data,index) in varList">
									<tr>
										<td style="text-align: center;">
                                        	<input type="checkbox" v-bind:id="'zcheckbox'+index" name='ids' v-bind:value="data.${objectNameUpper}_ID">
										</td>
										<td style="text-align: center;">{{page.showCount*(page.currentPage-1)+index+1}}</td>
								<#list fieldList as var>
									<#if var[1] == 'String' && var[7] != 'null'>
										<td style="text-align: center;">{{data.DNAME${var_index+1}}}</td>
									<#else>
										<td style="text-align: center;">{{data.${var[0]}}}</td>
									</#if>
								</#list>
										<td style="text-align: center;border-right: 0px;">
											<a title="查看" v-on:click="goView(data.${objectNameUpper}_ID)" style="cursor:pointer;"><i class="feather icon-eye"></i></a>
											<a title="修改" v-on:click="goEdit(data.${objectNameUpper}_ID)" style="cursor:pointer;"><i class="feather icon-edit-2"></i></a>
							                <a title="删除" v-on:click="goDel(data.${objectNameUpper}_ID)" style="cursor:pointer;"><i class="feather icon-x"></i></a>
										</td>
									</tr>
								</template>
								<tr v-show="varList.length==0">
									<td colspan="100" style="text-align: center;border-right: 0px;">没有相关数据</td>
								</tr>
							</tbody>
						</table>
						
						<table style="width:100%;margin-top:15px;">
							<tr>
								<td style="vertical-align:top;border-right: 0px;padding-left: 5px;">
									<i v-on:click="goExcel" class="mdi mdi-cloud-download" style="cursor:pointer;" title="导出到excel表格"></i>
									<input type="text" v-model="KEYWORDS" style="padding-top:5px;border-left-width:0px;border-top-width:0px;border-right-width:0px;border-bottom: 1px solid #dbdbdb"/>
									<i v-on:click="getList" class="feather icon-search" style="cursor:pointer;" title="检索"></i>
									<i v-on:click="goAdd" class="fas fa-plus" style="cursor:pointer;color: #04A9F5;" title="新增"></i>
									<i v-on:click="makeAll('确定要删除选中的数据吗?')" class="fas fa-trash-alt" style="cursor:pointer;color: #FB5430;" title="删除"></i>
								</td>
								<td style="vertical-align:top;border-right: 0px;"><div style="float: right;padding-top: 0px;margin-top: 0px;" v-html="page.pageStr"></div></td>
							</tr>
						</table>
					</div>
				</div>	

            </div>
        </div>
        <!-- [ Hover-table ] end -->
    
<script type="text/javascript" src="../../../assets/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="../../../assets/js/pre-loader.js"></script>
<script src="../../../assets/plugins/sweetalert/js/sweetalert.min.js"></script>
<!-- 表单验证提示 -->
<script src="../../../assets/js/jquery.tips.js"></script>

<script type="text/javascript">

var httpurl = httpurlSystem; //默认指向system服务，如果代码放到别的服务，需要把 httpurlSystem 改成别的服务名称，httpurlSystem 是在config.js里面配置的

var vm = new Vue({
	el: '#app',
	
	data:{
		${faobject}_ID: '', //父ID
		varList: [],		//list
		page: [],			//分页类
		KEYWORDS:'',		//检索条件,关键词
		showCount: -1,		//每页显示条数(这个是系统设置里面配置的，初始为-1即可，固定此写法)
		currentPage: 1,		//当前页码
		toExcel:false,		//导出到excel权限
		loading:false		//加载状态
    },
    
	methods: {
		
        //初始执行
        init() {
        	//复选框控制全选,全不选 
    		$('#zcheckbox').click(function(){
	    		 if($(this).is(':checked')){
	    			 $("input[name='ids']").click();
	    		 }else{
	    			 $("input[name='ids']").attr("checked", false);
	    		 }
    		});
    		var FID = this.getUrlKey('${faobject}_ID');	//链接参数
        	if(null != FID){
        		this.${faobject}_ID = FID;
        	}
    		this.getList();
        },
        
        //获取列表
        getList: function(){
        	this.loading = true;
        	$.ajax({
        		xhrFields: {
                    withCredentials: true
                },
        		type: "POST",
        		url: httpurl+'${objectNameLower}/list?showCount='+this.showCount+'&currentPage='+this.currentPage,
        		data: {KEYWORDS:this.KEYWORDS,${faobject}_ID:this.${faobject}_ID,tm:new Date().getTime()},
        		dataType:"json",
        		success: function(data){
        		 if("success" == data.result){
        			 vm.varList = data.varList;
        			 vm.page = data.page;
        			 vm.hasButton();
        			 vm.loading = false;
        			 $("input[name='ids']").attr("checked", false);
        		 }else if ("exception" == data.result){
                 	showException("${TITLE}",data.exception);//显示异常
                 }
        		}
        	}).done().fail(function(){
                swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
                setTimeout(function () {
                	window.location.href = "../../login.html";
                }, 2000);
            });
        },
        
    	//新增
    	goAdd: function (){
    		 var diag = new top.Dialog();
    		 diag.Drag=true;
    		 diag.Title ="新增";
    		 diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_edit.html?${faobject}_ID='+this.${faobject}_ID;
    		 diag.Width = 800;
    		 diag.Height = 600;
    		 diag.Modal = false;			//有无遮罩窗口
    		 diag. ShowMaxButton = true;	//最大化按钮
    	     diag.ShowMinButton = true;		//最小化按钮 
    		 diag.CancelEvent = function(){ //关闭事件
    	    	 var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
    			 if(varSon != null && varSon.style.display == 'none'){
    				 vm.getList();
    			}
    			diag.close();
    		 };
    		 diag.show();
    	},
    	
    	//修改
    	goEdit: function(id){
    		 var diag = new top.Dialog();
    		 diag.Drag=true;
    		 diag.Title ="编辑";
    		 diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_edit.html?FID='+id;
    		 diag.Width = 800;
    		 diag.Height = 600;
    		 diag.Modal = false;			//有无遮罩窗口
    		 diag. ShowMaxButton = true;	//最大化按钮
    	     diag.ShowMinButton = true;		//最小化按钮 
    		 diag.CancelEvent = function(){ //关闭事件
    			 var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
    			 if(varSon != null && varSon.style.display == 'none'){
    				 vm.getList();
    			}
    			diag.close();
    		 };
    		 diag.show();
    	},

    	//查看
    	goView: function(id){
    		 var diag = new top.Dialog();
    		 diag.Drag=true;
    		 diag.Title ="查看";
    		 diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_view.html?FID='+id;
    		 diag.Width = 1000;
    		 diag.Height = 800;
    		 diag.Modal = false;			//有无遮罩窗口
    		 diag. ShowMaxButton = true;	//最大化按钮
    	     diag.ShowMinButton = true;		//最小化按钮 
    		 diag.CancelEvent = function(){ //关闭事件
    			diag.close();
    		 };
    		 diag.show();
    	},
    	
    	//删除
    	goDel: function (id){
    		swal({
    			title: '',
                text: "确定要删除吗?",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                	this.loading = true;
                	$.ajax({
                		xhrFields: {
                            withCredentials: true
                        },
            			type: "POST",
            			url: httpurl+'${objectNameLower}/delete',
            	    	data: {${objectNameUpper}_ID:id,tm:new Date().getTime()},
            			dataType:'json',
            			success: function(data){
            				 if("success" == data.result){
            					 swal("删除成功", "已经从列表中删除!", "success");
            				 }
            				 vm.getList();
            			}
            		});
                }
            });
    	},
    	
    	//批量操作
    	makeAll: function (msg){
    		swal({
                title: '',
                text: msg,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
    	        	var str = '';
    				for(var i=0;i < document.getElementsByName('ids').length;i++){
    					  if(document.getElementsByName('ids')[i].checked){
    					  	if(str=='') str += document.getElementsByName('ids')[i].value;
    					  	else str += ',' + document.getElementsByName('ids')[i].value;
    					  }
    				}
    				if(str==''){
    					$("#cts").tips({
    						side:3,
    			            msg:'点这里全选',
    			            bg:'#AE81FF',
    			            time:3
    			        });
    	                swal("", "您没有选择任何内容!", "warning");
    					return;
    				}else{
    					if(msg == '确定要删除选中的数据吗?'){
    						this.loading = true;
    						$.ajax({
    							xhrFields: {
    	                            withCredentials: true
    	                        },
    							type: "POST",
    							url: httpurl+'${objectNameLower}/deleteAll?tm='+new Date().getTime(),
    					    	data: {DATA_IDS:str},
    							dataType:'json',
    							success: function(data){
    								if("success" == data.result){
    									swal("删除成功", "已经从列表中删除!", "success");
    		        				 }
    								vm.getList();
    							}
    						});
    					}
    				}
                }
            });
    	},
        
      	//判断按钮权限，用于是否显示按钮
        hasButton: function(){
        	var keys = 'toExcel';
        	$.ajax({
        		xhrFields: {
                    withCredentials: true
                },
        		type: "POST",
        		url: httpurlSystem+'head/hasButton',
        		data: {keys:keys,tm:new Date().getTime()},
        		dataType:"json",
        		success: function(data){
        		 if("success" == data.result){
        			vm.toExcel = data.toExcel;						//导出到excel权限
        		 }else if ("exception" == data.result){
                 	showException("按钮权限",data.exception);		//显示异常
                 }
        		}
        	})
        },
        
        //导出excel
		goExcel: function (){
			swal({
               	title: '',
                text: '确定要导出到excel吗?',
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                	window.location.href = httpurl+'${objectNameLower}/excel?${faobject}_ID='+this.${faobject}_ID;         	
                }
            });
		},
		
		//根据url参数名称获取参数值
        getUrlKey: function (name) {
            return decodeURIComponent(
                (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null;
        },
        
        //-----分页必用----start
        nextPage: function (page){
        	this.currentPage = page;
        	this.getList();
        },
        changeCount: function (value){
        	this.showCount = value;
        	this.getList();
        },
        toTZ: function (){
        	var toPaggeVlue = document.getElementById("toGoPage").value;
        	if(toPaggeVlue == ''){document.getElementById("toGoPage").value=1;return;}
        	if(isNaN(Number(toPaggeVlue))){document.getElementById("toGoPage").value=1;return;}
        	this.nextPage(toPaggeVlue);
        }
       //-----分页必用----end
		
	},
	
	mounted(){
        this.init();
    }
})
		
</script>

</body>
</html>