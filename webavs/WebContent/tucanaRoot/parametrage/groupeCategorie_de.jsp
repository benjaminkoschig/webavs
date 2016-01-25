<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.db.parametrage.TUGroupeCategorieViewBean viewBean = (globaz.tucana.db.parametrage.TUGroupeCategorieViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdGroupeCategorie();
	idEcran = "TU-401";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.ajouter";
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.ajouter";
    else
        document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.modifier";
    return state;
}
function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.chercher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.afficher";
	<%}%>
}
function del() {
	var msg = '<%=objSession.getLabel("SUPPRESSION")%>';
    if (window.confirm(msg)){
        document.forms[0].elements('userAction').value="tucana.parametrage.groupeCategorie.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_DETAIL_GROUPE_CATEGORIE" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!-- Valeur -->
						<TR>
							<TD width="25%">
								<ct:FWLabel key="ID_GROUPE_CATEGORIE" />
							</TD>
							<TD width="80%">
								<ct:inputText name="idGroupeCategorie" readonly="true" styleClass="numero" style="text-align: right;"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="GROUPE_RUBRIQUE" />
							</TD>
							<TD>
								<ct:select name="csGroupeRubrique">
									<ct:optionsCodesSystems csFamille="TU_GRRUBR" />
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="CATEGORIE" />
							</TD>
							<TD>
								<ct:select name="csCategorie">
									<ct:optionsCodesSystems csFamille="TU_CATEG" />
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="TYPE" />
							</TD>
							<TD>
								<ct:select name="csType">
									<ct:optionsCodesSystems csFamille="TU_TYGRP" />
								</ct:select>
							</TD>
						</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="ODTUGroupeCategorie" showTab="options">
	<ct:menuSetAllParams key="idGroupeCategorie" value="idGroupeCategorie" scope="viewbean"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>