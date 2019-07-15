<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>实时热搜</title>
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
</head>
<body>

	<div class="container-fluid">
		<!-- Static navbar -->
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">

					</ul>
				</div>
			</div>
		</nav>

		<div class="row">
			<div class="text-center">
				<a class="btn btn-primary" alt="/hotword/001" role="button">热词_001</a>
				<a class="btn btn-success" alt="/hotword/002" role="button">热词_002</a>
				<a class="btn btn-info" alt="/hotword/003" role="button">热词_003</a>
				<a class="btn btn-danger" alt="/hotword/004" role="button">热词_004</a>
				<a class="btn btn-warning" alt="/hotword/005" role="button">热词_005</a>
			</div>
		</div>

		<div class="row">
			<div class="text-center">
				<h3>热搜排行榜</h3>
			</div>
		</div>
		<div class="row">
			<div class="text-center">
				<ul style="padding-left: 0px;" id="hotwords">

				</ul>
			</div>
		</div>
	</div>

	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<script>
            $('.btn').click(function(){
                $.get($(this).attr("alt"),function(){},"json");
            });

            var websocket;
            function initWebsocket() {
                if ('WebSocket' in window) {
                    websocket = new WebSocket("ws://127.0.0.1:9090/ws");
                } else if ('MozWebSocket' in window) {
                    websocket = new MozWebSocket("ws://127.0.0.1:9090/ws");
                } else {
                    websocket = new SockJS("http://127.0.0.1:9090/ws");
                }
                websocket.onopen = function(event) {
                    console.log("WebSocket:已连接");
                };
                websocket.onmessage = function(ev) {
                    var obj=JSON.parse(ev.data);
                    if(!obj || obj == undefined) {
                        return false;
                    }
                    if(!!obj) {//如果空消息不予处理
                        console.log(obj);
                        var ulContent="";
						$.each(obj,function(index,element){
                            ulContent=ulContent+"<li><h4><a title='score:"+element.score+"'>"+"热词_"+element.value+"</a></h4></li>";
						});
						$("#hotwords").html(ulContent);
                    }
                };
                websocket.onerror = function(event) {
                    console.log("WebSocket:发生错误 ");
                };
                websocket.onclose = function(event) {
                    console.log("WebSocket:已关闭");
                }
            }
            initWebsocket();
	</script>
</body>
</html>