//*** ハウス情報の削除
function del(){
	// ハウスの数
	var size = document.forms["f"].elements["Housesize"].value;
	for( var i = 0; i < size; i++ ){
		document.forms["form2"].elements["houseId" + i].value = document.forms["f"].elements["houseId" + i].value; 
	}
	document.forms["form2"].elements["num"].value = document.forms["f"].elements["num"].value; 				
	document.form2.submit();
}
