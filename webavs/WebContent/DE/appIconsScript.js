var keyselected = "";
var selected = "";

function changeStyle(mId,color,flag) {
	mId.style.background=color;
	if (flag == "1"){
		mId.style.border="solid 1 #43546B";
	} else {
		mId.style.border="solid 1 #B3C4DB";
	}
}