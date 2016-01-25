<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
			globaz.tucana.vb.bouclement.TUDetailInsertionACMViewBean viewBean = (globaz.tucana.vb.bouclement.TUDetailInsertionACMViewBean) session
			.getAttribute("viewBean");
	selectedIdValue = "null";
	viewBean.setIdBouclement(request.getParameter("idBouclement"));
	idEcran = "TU-105";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques"%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers


function add() {
    document.forms[0].elements('userAction').value="tucana.bouclement.detailImportationACM.ajouter";
}
function upd() {
	document.forms[0].elements('userAction').value="tucana.bouclement.detailImportationACM.ajouter";
}
function validate() {
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.bouclement.detailImportationACM.ajouter";
    return state;
}
function cancel() {
	  document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.chercher";
}
function del() {
<%--	var msg = '<%=objSession.getLabel("SUPPRESSION")%>';
    if (window.confirm(msg)){
        document.forms[0].elements('userAction').value="tucana.bouclement.detail.supprimer";
        document.forms[0].submit();
    }
--%>
}
function init(){
}

function majuscule(monPar){
	monPar.value = monPar.value.toUpperCase();
}



// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_DETAIL_INSERTION_ACM" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD><ct:FWLabel key="ID_BOUCLEMENT" /></TD>
						<TD><ct:inputText name="idBouclement" readonly="true"
							styleClass="numero" style="text-align: right;" /></TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="CANTON" /></TD>
						<TD><ct:inputText name="canton" styleClass="numeroCourt"
							style="text-align: left;" onchange="majuscule(this);"
							defaultValue="" tabindex="1"/></TD>
					</TR>
					<TR>
						<TD width="35%"><%=objSession.getCodeLibelle(ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_MATERNITE_CAPG)%>

						</TD>
						<TD width="70%"><ct:inputText name="carteMaternite"
							styleClass="numero" style="text-align: right;" tabindex="2" /></TD>
					</TR>
					<TR>
						<TD><%=objSession.getCodeLibelle(ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_MATERNITE_CAPG)%>

						</TD>
						<TD><ct:inputText name="jourMaternite" styleClass="numero"
							style="text-align: right;" tabindex="3" /></TD>
					</TR>
					<TR>
						<TD><%=objSession.getCodeLibelle(ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_SERVICE_MILITAIRE_CAPG)%>

						</TD>
						<TD><ct:inputText name="carteMilitaire" styleClass="numero"
							style="text-align: right;" tabindex="4" /></TD>
					</TR>
					<TR>
						<TD><%=objSession
							.getCodeLibelle(ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_SERVICE_MILITAIRE_CAPG)%>

						</TD>
						<TD><ct:inputText name="jourMilitaire" styleClass="numero"
							style="text-align: right;" tabindex="5" /></TD>
					</TR>
					<TR>
						<TD height="30px">&nbsp;</TD>
						<TD>&nbsp;</TD>
					</TR>

					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%> 
<script>
document.getElementById('btnVal').tabIndex="6";
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="ODTUDetailInsertionACM" showTab="options" checkAdd="no"/>
<ct:menuSetAllParams key="idBouclement" value="idBouclement" scope="request" menuId="ODTUDetailInsertionACM" checkAdd="no"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
