//页面加载后,点击图片，切换验证码
$(function(){
	$('#login_info').click(checkcode);
	function checkcode(){
		var url='/image.do';
		$.getJSON(url,function(result){
			console.log(result);
		});
	}
});
