<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.vb.journal.TUJournalRecapF2ViewBean viewBean = (globaz.tucana.vb.journal.TUJournalRecapF2ViewBean) session.getAttribute("viewBean");
	idEcran = "TU-250";
	String btnGenEnLabel = objSession.getLabel("BTN_GENERER");
	String btnPdfEnLabel = objSession.getLabel("BTN_PDF");
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
	document.getElementById("annee").focus();
}
function onClickGenerer(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.journal.journalRecapF2.generer";
	chargeUrl(state);
}
function onClickPdf(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.journal.journalRecapF2.pdf";
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
			<ct:FWLabel key="TIT_JOURNAL_RECAP_F2"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText name="eMail" styleClass="libelleLong"  tabindex="1"/>
							<script>
								document.getElementsByName("email")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="ANNEE"/></TD>
							
							<TD width="80%">
								<ct:inputText name="annee" styleClass="numero" style="text-align: right;" tabindex="1" id="annee"/>
							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="AGENCE"/></TD>
							
							<TD width="80%">
								<ct:select id="csAgence" name="csAgence" wantBlank="true" tabindex="2">
									<ct:optionsCodesSystems csFamille="TU_AGENCE"/>
								</ct:select>
							</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- Bouton "Pdf" --%>
				<INPUT type="button" class="btnCtrl" id="btnPdfEn" value="<%=btnPdfEnLabel%>" onclick="onClickPdf();" tabindex="5">
				<%-- Bouton "Générer" --%>
				<INPUT type="button" class="btnCtrl" id="btnGenEn" value="<%=btnGenEnLabel%>" onclick="onClickGenerer();" tabindex="3">
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
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>