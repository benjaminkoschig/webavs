<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.db.parametrage.TUCategorieRubriqueViewBean viewBean = (globaz.tucana.db.parametrage.TUCategorieRubriqueViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdCategorieRubrique();
	idEcran = "TU-403";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.ajouter";
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.ajouter";
    else
        document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.modifier";
    return state;
}
function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.chercher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.afficher";
	<%}%>
}
function del() {
	var msg = '<%=objSession.getLabel("SUPPRESSION")%>';
    if (window.confirm(msg)){
        document.forms[0].elements('userAction').value="tucana.parametrage.categorieRubrique.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_DETAIL_CATEGORIE_RUBRIQUE" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!-- Valeur -->
						<TR>
							<TD width="25%">
								<ct:FWLabel key="ID_GROUPE_CATEGORIE" />
							</TD>
							<TD width="75%">
								<ct:inputText name="idGroupeCategorie" readonly="true" styleClass="numero" style="text-align: right;"/>
							</TD>
						</TR>
						<TR>
							<TD width="25%">
								<ct:FWLabel key="ID_CATEGORIE_RUBRIQUE" />
							</TD>
							<TD width="75%">
								<ct:inputText name="idGroupeCategorie" readonly="true" styleClass="numero" style="text-align: right;"/>
							</TD>
						</TR>

						<TR>
							<TD>
								<ct:FWLabel key="RUBRIQUE" />
							</TD>
							<TD>
								<ct:select name="csRubrique" style="width=80%;">
									<ct:optionsCodesSystems csFamille="TU_RUBR"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="OPERATION" />
							</TD>
							<TD>
								<ct:select name="csOperation">
									<ct:optionsCodesSystems csFamille="TU_OPER" />
								</ct:select>
							</TD>
						</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="ODTUCategorieRubrique" showTab="options">
	<ct:menuSetAllParams key="idCategorieRubrique" value="idCategorieRubrique" scope="viewbean"/>
	<ct:menuSetAllParams key="idGroupeCategorie" value="idGroupeCategorie" scope="viewbean"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>