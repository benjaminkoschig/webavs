<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.vb.list.TUAffiliationProblemListViewBean viewBean = (globaz.tucana.vb.list.TUAffiliationProblemListViewBean) session.getAttribute("viewBean");
	idEcran = "TU-202";
	String btnLancerEnLabel = objSession.getLabel("BTN_LANCER");
%>
<%-- /tpl:put --%>
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
function onClickLancer(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.list.affiliationProblemList.lancer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_AFFILIATION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText id="eMail" name="eMail" styleClass="libelleLong" tabindex="1"/>
							<script>
								document.getElementsByName("email")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>						
						<TR>
							<TD>
								<ct:FWLabel key="ANNEE" />
							</TD>
							<TD>
								<ct:inputText id="annee" name="annee" styleClass="numero" style="text-align: right;"  tabindex="3"/>
							</TD>
						</TR>						
						<TR>
							<TD><ct:FWLabel key="MOIS"/></TD>
							
							<TD>
								<ct:inputText id="mois" name="mois" styleClass="numeroCourt" style="text-align: right;" tabindex="4"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="ID_EXTERNE_RUBRIQUE"/></TD>
							
							<TD>
								<ct:inputText id="idRubrique" name="idRubrique" styleClass="numeroLong" style="text-align: right;" tabindex="5"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="GENRE_ASSURANCE"/></TD>
							
							<TD>
								<select id="genreAssu" name="genreAssu" tabindex="6">
									<option selected value="812002">Cotisation</option>
									<option value="812009">FFPP</option>
								</select>					
							</TD>
						</TR>



						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" class="btnCtrl" id="btnLancerEn" value="<%=btnLancerEnLabel%>" onclick="onClickLancer();" tabindex="6">
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