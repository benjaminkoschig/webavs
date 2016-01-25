<%@page import="globaz.pyxis.services.lettrestype.TIStartLettreTypeViewBean"%>
<html>
<head>

<%
	TIStartLettreTypeViewBean viewBean = (TIStartLettreTypeViewBean)session.getAttribute("viewBean");
%>

<script language="VBScript">
Dim WordApp
sub startWord (file)
	Set WordApp = CreateObject("Word.Application")
	WordApp.visible = true
	WordApp.Documents.Open(file)
	<%=viewBean.getScript()%>
	WordApp.ActiveDocument.fields.Update
end sub

sub replace (signet,text)
	Dim BMRange
    Set BMRange = WordApp.ActiveDocument.Bookmarks(signet).Range
    BMRange.Text = text
    WordApp.ActiveDocument.Bookmarks.Add signet, BMRange
end sub

</script>

</head>
<body>
	<h1>Starting document</h1>
	<script language='javascript'>startWord('<%=viewBean.getFile()%>')</script>
</body>
</html>