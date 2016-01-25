<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String LABEL_DIALOG_TITLE = "Warning(s)";
String LABEL_YOU_HAVE_ERRORS = "Vous avez des warnings!";
if ("DE".equalsIgnoreCase(languePage)) {
	LABEL_DIALOG_TITLE = "Warning(s)";
	LABEL_YOU_HAVE_ERRORS = "Sie haben warnings!";
}
%>
<HEAD>
<TITLE><%=LABEL_DIALOG_TITLE%></TITLE>
<META name="Generator" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META NAME="Author" CONTENT="VCH">
<META NAME="Keywords" CONTENT="?">
<META NAME="Description" CONTENT="Boîte de dialogue modale">
<META http-equiv="Content-Style-Type" content="text/css">

<SCRIPT>
var errors = window.dialogArguments;
var errorsStr = errors.text;

function hitOk() {
	var vReturnValue = new Object();
    vReturnValue.isBack = "false";
    window.returnValue = vReturnValue;
	window.close();
}

function hitBack() {
	var vReturnValue = new Object();
    vReturnValue.isBack = "true";
    window.returnValue = vReturnValue;
	window.close();
}
</SCRIPT>

</HEAD>

<BODY>
<DIV style="text-align:center">
	&nbsp;<BR>
	<%=LABEL_YOU_HAVE_ERRORS%><BR>
		<INPUT style="padding: 3;"type="button" onclick="hitOk()" value="OK">
		<INPUT style="padding: 3;"type="button" onclick="hitBack()" value="Retour">
	<HR>
	<SCRIPT>
		document.write(errorsStr);
	</SCRIPT>
</DIV>
</BODY>
</HTML>
