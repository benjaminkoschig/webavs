<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.db.bouclement.TUBouclementViewBean viewBean = (globaz.tucana.db.bouclement.TUBouclementViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdBouclement();
	idEcran = "TU-101";
	String defaultAgence = viewBean.getCsAgence();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.ajouter";
   	<%if (objSession.hasRight("tucana.bouclement.csAgence",globaz.framework.secure.FWSecureConstants.UPDATE)){%>
		document.getElementById("csAgence").disabled = false;
	<%} else {%>
		document.getElementById("csAgence").disabled = true;
	<%}%>
}
function upd() {
	<%if (objSession.hasRight("tucana.bouclement.csAgence",globaz.framework.secure.FWSecureConstants.UPDATE)){%>
		document.getElementById("csAgence").disabled = false;
		document.getElementById("csEtat").disabled = false;
	<%} else {%>
		document.getElementById("csAgence").disabled = true;
		document.getElementById("csEtat").disabled = true;
	<%}%>
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.ajouter";
    else
        document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.modifier";
    return state;
}
function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.chercher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.afficher";
	<%}%>
}
function del() {
	var msg = '<%=objSession.getLabel("SUPPRESSION")%>';
    if (window.confirm(msg)){
        document.forms[0].elements('userAction').value="tucana.bouclement.bouclement.supprimer";
        document.forms[0].submit();
    }
}
function init(){
	<%if (objSession.hasRight("tucana.bouclement.csAgence",globaz.framework.secure.FWSecureConstants.UPDATE)){%>
		document.getElementById("csAgence").disabled = false;
	<%} else {%>
		document.getElementById("csAgence").disabled = true;
	<%}%>	
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_DETAIL_BOUCLEMENT" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!-- Valeur -->
						<TR>
							<TD width="20%">
								<ct:FWLabel key="AGENCE" />
							</TD>
							<%--<TD width="80%" style="font-weight: bold;"><%=objSession.getCodeLibelle(globaz.globall.api.GlobazSystem.getApplication(globaz.tucana.application.TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(globaz.tucana.application.TUApplication.CS_AGENCE))%>--%>
							<TD width="80%" style="font-weight: bold;">

								<ct:select id="csAgence" name="csAgence" wantBlank="true" defaultValue="<%=defaultAgence%>">
									<ct:optionsCodesSystems csFamille="TU_AGENCE" />
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD height="30px">&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="ID_BOUCLEMENT" />
							</TD>
							<TD>
								<ct:inputText name="idBouclement" readonly="true" styleClass="numero" style="text-align: right;"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="DATE_CREATION" />
							</TD>
							<TD>
								<ct:inputText name="dateCreation" styleClass="date"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="MOIS" />
							</TD>
							<TD>
								<ct:inputText name="moisComptable" styleClass="numeroCourt" style="text-align: right;"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="ANNEE" />
							</TD>
							<TD>
								<ct:inputText name="anneeComptable" styleClass="numero" style="text-align: right;"/>
							</TD>
						</TR>
<%--						<TR>
							<TD>
								<ct:FWLabel key="SOLDE" />
							</TD>
							<TD>
								<ct:inputText name="soldeBouclement" styleClass="numeroLond" style="text-align: right;"/>
							</TD>
						</TR>
--%>						
						<TR>
							<TD>
								<ct:FWLabel key="DATE_ETAT" />
							</TD>
							<TD>
								<ct:inputText name="dateEtat" styleClass="date"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="ETAT_BOUCLEMENT" />
							</TD>
							<TD>
								<ct:select name="csEtat">
									<ct:optionsCodesSystems csFamille="TU_ETAT" />
								</ct:select>
							</TD>
						</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="ODTUBouclement" showTab="options">
	<ct:menuSetAllParams key="idBouclement" value="idBouclement" scope="viewbean"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>