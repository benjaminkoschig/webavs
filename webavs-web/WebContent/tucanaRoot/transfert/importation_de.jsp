<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%

formAction= request.getContextPath()+mainServletPath+"Root/transfert/importation_upload_de.jsp";
	globaz.tucana.vb.transfert.TUImportationViewBean viewBean = (globaz.tucana.vb.transfert.TUImportationViewBean) session.getAttribute("viewBean");
	idEcran = "TU-902";
	String btnImpEnLabel = objSession.getLabel("BTN_IMPORTER");
	boolean myError = false;
	userActionValue = "tucana.transfert.importation.importer";
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
function onClickImporter(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.transfert.importation.importer";
	chargeUrl(state);
}

<%-- méthode qui charge l'url (fait un submit) --%>
function chargeUrl(state){
	if (state){
		action(COMMIT);
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ct:FWLabel key="TIT_IMPORTATION"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TABLE>
						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText name="eMailAdress" styleClass="libelleLong"/>
							<script>
								document.getElementsByName("eMailAdress")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="SOURCE"/></TD>
							
							<TD width="80%">
								<%--<ct:inputText name="source" styleClass="libelleLong" style="text-align: left;" tabindex="1" id="source"/>--%>
								<input type="file" name="fileName" id="fileName" style="width : 500px;" value="<%=viewBean.getFileName()%> %>" class="libelleLong" tabindex="1">
							</TD>
						</TR>
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