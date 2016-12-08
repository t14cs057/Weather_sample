//*** ハウス情報の変更
function change(form){
	// document.forms["フォームの名前"(formタグのname属性)].elements["要素の名前(inputタグのname属性)"].value			
	// ハウスの数
	var size = document.forms["f"].elements["Housesize"].value;
	// 各ハウスの情報を登録
	for( var i = 0; i < size; i++ ){
		document.forms["form1"].elements["zipCode1" + i].value = document.forms["f"].elements["zipCode1" + i].value; 	
		document.forms["form1"].elements["zipCode2" + i].value = document.forms["f"].elements["zipCode2" + i].value; 	
		document.forms["form1"].elements["addr" + i].value = document.forms["f"].elements["addr" + i].value; 	
		document.forms["form1"].elements["pillar" + i].value = document.forms["f"].elements["pillar" + i].value; 	
		document.forms["form1"].elements["pillarNum" + i].value = document.forms["f"].elements["pillarNum" + i].value; 	
		document.forms["form1"].elements["vin" + i].value = document.forms["f"].elements["vin" + i].value; 	
		document.forms["form1"].elements["vinNum" + i].value = document.forms["f"].elements["vinNum" + i].value; 	
	}	
	document.form1.submit();
}