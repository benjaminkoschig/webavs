//******* <DEBUG JS TOOLS> **********************************
var debugModeEnable=false;

function mlog(msg,color){
	if(debugModeEnable){
		
		var msgTab = msg.split(" ");
		var newMsg = "";
		for(i in msgTab){
		
			if(msgTab[i].charAt(0)=='@'){
				newMsg+='<a target="_blank" href="'+msgTab[i].substr(1)+'">'+msgTab[i].substr(1)+'</a> ';
			}
			else{
				newMsg+=msgTab[i]+" ";
			}
		}
		
		strHtml="<span style=\"color:"+color+"\">"+newMsg+"</span><br/>";
		var buffer = "";
		
		if(document.getElementById('debugConsole')){
			buffer = document.getElementById('debugConsole').innerHTML;
			document.getElementById('debugConsole').innerHTML="";
			document.getElementById('debugConsole').innerHTML+=strHtml;
			document.getElementById('debugConsole').innerHTML+=buffer;
		}
		else
			alert('Veuillez appeler initDebugManager() dans postInit pour utiliser la fonction debug');
	}
}


function extractUrlParams () {
	var t = location.search.substring(1).split('&');
	var f = [];
	for (var i=0; i<t.length; i++) {
		var x = t[ i ].split('=');
		f[x[0]]=x[1];
	}
	return f;
}