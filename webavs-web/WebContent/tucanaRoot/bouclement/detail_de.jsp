<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
	globaz.tucana.db.bouclement.TUDetailViewBean viewBean = (globaz.tucana.db.bouclement.TUDetailViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdDetail();
	viewBean.setIdBouclement(request.getParameter("idBouclement"));
	idEcran = "TU-103";
	
	if(! viewBean.getCsTypeRubrique().equals(globaz.tucana.constantes.ITUCSConstantes.CS_TY_RUBR_MANUELLE)
	|| ! viewBean.getBouclement().getCsEtat().equals(globaz.tucana.constantes.ITUCSConstantes.CS_ETAT_ENCOURS)){
		bButtonUpdate = false;
		bButtonDelete = false;
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers


function add() {
    document.forms[0].elements('userAction').value="tucana.bouclement.detail.ajouter";
    document.getElementsByName("csTypeRubrique")[0].disabled=true;
	document.getElementsByName("canton")[0].value = "<%=globaz.globall.api.GlobazSystem.getApplication(globaz.tucana.application.TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(globaz.tucana.application.TUApplication.DEFAULT_CANTON)%>";
<%--   	<%if (objSession.hasRight("tucana.bouclement.csTypeRubrique",globaz.framework.secure.FWSecureConstants.UPDATE)){%>
		document.getElementById("csTypeRubrique").disabled = false;
	<%} else {%>
		document.getElementById("csTypeRubrique").disabled = true;
	<%}%>
--%>
}
function upd() {
	document.getElementsByName("dateImport")[0].focus();
<%--	<%if (objSession.hasRight("tucana.bouclement.csTypeRubrique",globaz.framework.secure.FWSecureConstants.UPDATE)){%>
		document.getElementById("csTypeRubrique").disabled = false;
	<%} else {%>
		document.getElementById("csTypeRubrique").disabled = true;
	<%}%>
--%>
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="tucana.bouclement.detail.ajouter";
    else
        document.forms[0].elements('userAction').value="tucana.bouclement.detail.modifier";
    return state;
}
function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="tucana.bouclement.detail.chercher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="tucana.bouclement.detail.afficher";
	<%}%>
}
function del() {
	var msg = '<%=objSession.getLabel("SUPPRESSION")%>';
    if (window.confirm(msg)){
        document.forms[0].elements('userAction').value="tucana.bouclement.detail.supprimer";
        document.forms[0].submit();
    }
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="ID_BOUCLEMENT" />
							</TD>
							<TD>
								<ct:inputText name="idBouclement" readonly="true" styleClass="numero" style="text-align: right;" tabindex="-1"/>
							</TD>
						</TR>
						<TR>
							<TD width="20%">
								<ct:FWLabel key="ID_DETAIL" />
							</TD>
							<TD  width="80%">
								<ct:inputText name="idDetail" readonly="true" styleClass="numero" style="text-align: right;" tabindex="-1"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="NO_PASSAGE" />
							</TD>
							<TD>
								<ct:inputText name="noPassage" styleClass="numeroCourt" style="text-align: right;" defaultValue="" tabindex="1"/>
							</TD>
						</TR>

						<TR>
							<TD>
								<ct:FWLabel key="DATE_IMPORTATION" />
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateImport" value="<%=viewBean.getDateImport()%>" doClientValidation="CALENDAR,NOT_EMPTY"/>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="APPLICATION" />
							</TD>
							<TD>
								<ct:select name="csApplication">
									<ct:optionsCodesSystems csFamille="TU_APPLI"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="CANTON" />
							</TD>
							<TD>
								<ct:inputText name="canton" styleClass="numeroCourt" style="text-align: left;" onchange="majuscule(this);" 
								defaultValue="" />
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="RUBRIQUE" />
							</TD>
							<TD>
								<ct:select name="csRubrique" disabled="true">
									<ct:optionsCodesSystems csFamille="TU_RUBR"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="TYPE_RUBRIQUE" />
							</TD>
							<TD>
							
								<ct:select name="csTypeRubrique"  disabled="disabled" defaultValue="<%=globaz.tucana.constantes.ITUCSConstantes.CS_TY_RUBR_MANUELLE%>">
									<ct:optionsCodesSystems csFamille="TU_TYRUBR" />
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="MONTANT_NOMBRE" />
							</TD>
							<TD>
								<ct:inputText name="nombreMontant" styleClass="numeroLong" style="text-align: right;"/>								
							</TD>
						</TR>


						<TR>
							<TD height="30px">&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
	<ct:menuChange displayId="options" menuId="ODTUDetail" showTab="options"/>
	<script>
		try {
			document.getElementsByName("dateImport")[0].tabIndex="2";
			document.getElementsByName("csApplication")[0].tabIndex="3";
			document.getElementsByName("canton")[0].tabIndex="4";
			document.getElementsByName("csRubrique")[0].tabIndex="5";
			document.getElementsByName("nombreMontant")[0].tabIndex="6";
			document.getElementById("btnUpd").tabIndex="7";
			document.getElementById("btnDel").tabIndex="8";
			document.getElementById("btnVal").tabIndex="9";
			document.getElementById("btnCan").tabIndex="10";
		} catch(e) {}
	</script>
<%--	<ct:menuSetAllParams key="idBouclement" value="<%=viewBean.getIdBouclement()%>" menuId="ODTUDetail"/> --%>
	<ct:menuSetAllParams key="idBouclement" value="idBouclement" scope="request" menuId="ODTUDetail"/>
	<ct:menuSetAllParams key="idDetail" value="<%=viewBean.getIdDetail()%>" menuId="ODTUDetail"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>