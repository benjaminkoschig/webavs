<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.vb.journal.TUJournalRecapF1V1ViewBean viewBean = (globaz.tucana.vb.journal.TUJournalRecapF1V1ViewBean) session.getAttribute("viewBean");
	idEcran = "TU-201";
	String btnGenEnLabel = objSession.getLabel("BTN_GENERER");
	String btnPdfEnLabel = objSession.getLabel("BTN_PDF");
	String year = request.getParameter("year");
	String month = request.getParameter("month");
	String csAgence = request.getParameter("csAgence");
	if (csAgence == null || csAgence.length() == 0){
		csAgence = globaz.globall.api.GlobazSystem.getApplication(globaz.tucana.application.TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(globaz.tucana.application.TUApplication.CS_AGENCE);
	}		

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
function onClickPdf(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.journal.journalRecapF1V1.pdf";
	chargeUrl(state);
}
function onClickGenerer(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.journal.journalRecapF1V1.generer";
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
			<ct:FWLabel key="TIT_JOURNAL_RECAP_F1V1"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText name="eMail" styleClass="libelleLong"/>
							<script>
								document.getElementsByName("email")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>

						<TR>
							<TD width="20%"><ct:FWLabel key="AGENCE"/></TD>
							
							<TD width="80%">
								<ct:select id="csAgence" name="csAgence" wantBlank="true" tabindex="1" defaultValue="<%=csAgence%>">
									<ct:optionsCodesSystems csFamille="TU_AGENCE"/>
								</ct:select>

							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="ANNEE"/></TD>
							
							<TD width="80%">
								<ct:inputText name="annee" styleClass="numero" defaultValue="<%=year%>" style="text-align: right;" tabindex="2" id="annee"/>
							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="MOIS"/></TD>
							
							<TD width="80%">
								<ct:inputText name="mois" styleClass="numeroCourt" defaultValue="<%=month%>" style="text-align: right;" tabindex="3" id="mois"/>
							</TD>
						</TR>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- Bouton "Pdf" --%>
				<INPUT type="button" class="btnCtrl" id="btnPdfEn" value="<%=btnPdfEnLabel%>" onclick="onClickPdf();" tabindex="5">
				<%-- Bouton "Générer" --%>
				<INPUT type="button" class="btnCtrl" id="btnGenEn" value="<%=btnGenEnLabel%>" onclick="onClickGenerer();" tabindex="4">
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