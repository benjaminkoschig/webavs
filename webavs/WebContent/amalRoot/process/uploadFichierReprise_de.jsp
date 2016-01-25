<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.amal.vb.process.AMUploadFichierRepriseViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	formAction= request.getContextPath()+mainServletPath+"Root/process/uploadFichierRepriseProcess_de.jsp";

String exportResult = "";
try {
//	exportResult = request.getAttribute("exportResult").toString();
} catch (Exception e) {
	
}

	AMUploadFichierRepriseViewBean viewBean = (AMUploadFichierRepriseViewBean) session.getAttribute("viewBean");
	idEcran = "AM000XX";
	String btnImpEnLabel = objSession.getLabel("BTN_IMPORTER");
	userActionValue = "amal.process.uploadFichierReprise.exporter";	
%><%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT language="JavaScript">
function add() {}
function upd() {}
function validate() {}
function cancel() {}
function del() {}
function init(){
	document.getElementById("fileName").focus();
}

function postInit(){
	$(".notDisabled").removeProp("disabled");
	$("#fileName, #eMailAdress").removeProp("disabled");
}

function onClickImporter(){
	isFile2 = true;
	if ($("#fileName2").val().length==0) {
		$("#fileName2").remove();
		isFile2 = false;
	}
    state = validateFields();
    fullName1 = $("#fileName").val();
	fileName1 = fullName1.match(/[^\/\\]+$/);
	document.getElementById('fileInput1').value = fileName1;
	
	
//	if ($("#fileName2").val().length>0) {
	try {
		if (isFile2) {
			fullName2 = $("#fileName2").val();
			fileName2 = fullName2.match(/[^\/\\]+$/);
			$("#fileInput2").val(fileName2);
			document.getElementById('fileInput2').value = fileName2;
		}	
	} catch (Exception) {
		//On ne fais rien et c'est normal !
	}
    document.forms[0].elements('userAction').value="amal.process.uploadFichierReprise.exporter";
	chargeUrl(state);
}

<%-- méthode qui charge l'url (fait un submit) --%>
function chargeUrl(state){
	if (state){
		action(COMMIT);
	}
}
$(function(){	
});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ct:FWLabel key="TIT_IMPORTATION"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<ct:inputHidden name="fileInput1"/>
<ct:inputHidden name="fileInput2"/>
						<%-- tpl:put name="zoneMain" --%>
						<TABLE>
						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText id="eMailAdress" name="eMailAdress" styleClass="libelleLong"/>
							<script>
								document.getElementsByName("eMailAdress")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="SOURCE"/> fichier 1</TD>
							
							<TD width="80%">
								<input type="file" class="notDisabled" name="fileName" id="fileName" style="width : 500px;" value="" class="libelleLong" tabindex="1">
							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="SOURCE"/> fichier 2</TD>
							
							<TD width="80%">
								<input type="file" class="notDisabled" name="fileName2" id="fileName2" style="width : 500px;" value="" class="libelleLong" tabindex="2">
							</TD>
						</TR>
						<%if ("1".equals(exportResult)) { %>
						<TR>
							<TD colspan="2">								
							</TD>
						</TR>
						<TR>
							<TD colspan="2">
								Exportation en cours...
							</TD>
						</TR>
						<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- Bouton "Exporter" --%>
				<INPUT type="button" class="btnCtrl" id="btnImpEn" value="<%=btnImpEnLabel%>" onclick="onClickImporter();" tabindex="3">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<SCRIPT language="javascript">
<%-- rend innaccessible les boutons "valider", "ajouter", "modifier", "supprimer" --%>
<%if (bButtonValidate) {%>
	document.getElementById('btnVal').style.visibility="hidden";
<%}%>
<%if (bButtonNew) {%>
	document.getElementById('btnNew').style.visibility="hidden";
<%}%>	
<%if (bButtonUpdate) {%>
	document.getElementById('btnUpd').style.visibility="hidden";
<%}%>
<%if (bButtonDelete) {%>
	document.getElementById('btnDel').style.visibility="hidden";
<%}%>
document.forms[0].encoding = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>