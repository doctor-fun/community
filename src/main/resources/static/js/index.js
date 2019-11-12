
//<button type="button" class="btn btn-primary" id="publishBtn">发布</button>
//前端页面有一个button它的id是PublishBtn
$(function(){
	$("#publishBtn").click(publish);//这个button被click时调用publish（）这个方法
});

function publish() {
	$("#publishModal").modal("hide");//发布弹窗 隐藏
	//获取标题和内容
	var title = $("#recipient-name").val();//取得recipient-name这一元素的值
	var content=$("#message-text").val();
	//发送异步请求(post)
	$.post(
		CONTEXT_PATH+"/disscuss/add",
		{"title":title,"content":content},//带上索要传入的数据数据
			function(data){//回调函数处理返回的结果
			//解析data这一json串，
				data=$.parseJSON(data);
				//在提示框中显示返回信息
				$("#hintBody").text(data.msg);
				//显示提示框
				$("#hintModal").modal("show");
				//2秒后，自动隐藏提示框
				setTimeout(function () {
					$("#hintModal").modal("hide");
					if(data.code==0){
						window.location.reload()//重新加载当前页面
					}
					},2000);

		}
	);


	$("#hintModal").modal("show");//显示提示框
	setTimeout(function(){//
		$("#hintModal").modal("hide");
	}, 2000);
}