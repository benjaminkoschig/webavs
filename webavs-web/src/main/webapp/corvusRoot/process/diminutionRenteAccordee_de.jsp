<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<SCRIPT type="text/javascript" src="<%=servletContext%>/corvusRoot/ajax.js"></SCRIPT>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE0007";
	globaz.corvus.vb.process.REDiminutionRenteAccordeeViewBean viewBean = (globaz.corvus.vb.process.REDiminutionRenteAccordeeViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	selectedIdValue = viewBean.getIdRenteAccordee();
	String noRenteAccordee = viewBean.getIdRenteAccordee();
	String idTierBeneficiaire = viewBean.getIdTiersBeneficiaire();
	globaz.globall.db.BSession objSession = viewBean.getSession();
	String requerantDescription = viewBean.getTiersBeneficiaireInfo();
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE + ".executer";

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");

	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	showProcessButton = false;

	java.util.List cucsCodeMutation = globaz.prestation.tools.PRCodeSystem.getCUCS(objSession, globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_CODE_MUTATION);
	
	String btnValLabel = "Valider";
	String btnCanLabel = "Annuler";
	if("DE".equals(languePage)) {
		btnValLabel = "Best&auml;tigen";
		btnCanLabel = "Annullieren";
	}
%>



<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsdiminutionrentes">
			<ct:menuSetAllParams key="idRenteAccordee" 		value="<%=noRenteAccordee%>"/>
			<ct:menuSetAllParams key="noDemandeRente" 		value="<%=noDemandeRente%>"/>
			<ct:menuSetAllParams key="idTierRequerant" 		value="<%=viewBean.getIdTierRequerant(noDemandeRente)%>"/>
			<ct:menuSetAllParams key="idRenteCalculee" 		value="<%=viewBean.getIdRenteCalculee(noDemandeRente)%>"/>
			<ct:menuSetAllParams key="montantRenteAccordee" value="<%=viewBean.getMontantRA()%>"/>
			<ct:menuSetAllParams key="idTiersBeneficiaire"  value="<%=idTierBeneficiaire%>"/>
			<ct:menuSetAllParams key="selectedId"			value="<%=noRenteAccordee%>"/>
	</ct:menuChange>
<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=noRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTierRequerant(noDemandeRente)%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=noRenteAccordee%>"/>
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>"/>
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>
		<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			    
			  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(dateFinDroit))
			  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>
	</ct:menuChange>
<%}%>

<SCRIPT type="text/javascript">
function cancel(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.chercher";
   	   document.forms[0].submit();
}


function valider(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE%>.executer";
   	   document.forms[0].submit();   	   
}


function upd(){
     ;
}

function add(){
	;
}

function del(){
   ;
}

function init(){

<%--		<%if(vBeanHasErrors){%>
			errorObj.text = "<%=viewBean.getMessage().trim()%>";
			showErrors();
			errorObj.text = "";
		<%}%>
--%>
}

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
	}
}
function doInitThings() {

	showErrors();
}

function testRenteAccordee() {
	fieldFormat(document.getElementById("dateFinDroit"),"CALENDAR_MONTH");
	doQuery("<%=servletContext%><%=mainServletPath%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE%>.testRetenueError&idRenteAccordee=<%=noRenteAccordee%>&idTiersBeneficiaire=<%=idTierBeneficiaire%>&dateDebutDroit=" + document.getElementById('dateDebutDroit').value + "&dateFinDroit=" + document.getElementById('dateFinDroit').value + "&csGenreDiminution=" + document.getElementById('csCodeTraitement').value, afficheTestError);	
}

function afficheTestError(msgRetenue){

	
	var monEl = document.getElementById("btnOk");
	jscss("add", monEl, "inactive", null);
	
	document.getElementById("infoRetenueError").innerHTML='<strong>' + msgRetenue + '</strong>';
	document.getElementById("errorMsg").value=msgRetenue;
	if (msgRetenue == '') {
		var monEl = document.getElementById("btnOk");
		jscss("remove", monEl, "inactive", null);

		doQuery("<%=servletContext%><%=mainServletPath%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE%>.testRetenueWarning&idRenteAccordee=<%=noRenteAccordee%>&idTiersBeneficiaire=<%=idTierBeneficiaire%>&dateDebutDroit=" + document.getElementById('dateDebutDroit').value + "&dateFinDroit=" + document.getElementById('dateFinDroit').value + "&csGenreDiminution=" + document.getElementById('csCodeTraitement').value, afficheTestWarning);
	}
	
	doQuery("<%=servletContext%><%=mainServletPath%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE%>.afficheMontantArestituer&idRenteAccordee=<%=noRenteAccordee%>&idTiersBeneficiaire=<%=idTierBeneficiaire%>&dateDebutDroit=" + document.getElementById('dateDebutDroit').value + "&dateFinDroit=" + document.getElementById('dateFinDroit').value + "&csGenreDiminution=" + document.getElementById('csCodeTraitement').value, afficheMontantArestituer);	

}

function afficheTestWarning(msgRetenue){
		document.getElementById("infoRetenueWarning").innerHTML='<strong>' + msgRetenue + '</strong>';	
		document.getElementById("warningMsg").value=msgRetenue;
}

function afficheMontantArestituer(msgRetenue){
		document.getElementById("test").innerHTML='<strong>' + msgRetenue + '</strong>';	
}

 
$(document).ready(function() {
	$('#btnOk').one('click', function() {
		doOkAction();
		$('#btnOk').prop('disabled', true);
	});
});

</SCRIPT>










<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DIM_R_A_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR><TD colspan="6" style="color:red;"><SPAN id="infoRetenueError">&nbsp;</SPAN></TD></TR>
	<TR><TD colspan="6" style="color:#008800;"><SPAN id="infoRetenueWarning">&nbsp;</SPAN></TD></TR>
<%--
<%if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
	<TR><TD colspan="6" style="color:red;">HACK-FWK.error : <%=viewBean.getMessage()%></TD></TR>
<%}%>
--%>
	<ct:inputHidden name="idTiersBeneficiaire"/>
	<input type="hidden" name="errorMsg"/>
	<input type="hidden" name="warningMsg"/>	
	<TR>
		<TD><LABEL for="tiersBeneficiaireInfo"><b><ct:FWLabel key="JSP_DIM_R_A_BENEFICIAIRE"/></b></LABEL></TD>
		<TD colspan="5">
			<input type="hidden" name="idTierBeneficiaire" value="idTierBeneficiaire"/>
			<ct:inputText name="tiersBeneficiaireInfo" readonly="true" styleClass="RElibelleExtraLongDisabled" disabled="true" />
		</TD>
	</TR>
	<TR><TD colspan="6"><HR></TD></TR>
	<TR>
		<TD><LABEL for="idRenteAccordee"><ct:FWLabel key="JSP_DIM_R_A_NO"/></LABEL></TD>
		<TD><ct:inputText name="idRenteAccordee" readonly="true" styleClass="numeroCourt" style="text-align: right;" disabled="true" /></TD>
	</TR>
	<TR>
		<TD colspan="6" height="20px;">&nbsp;</TD>
	</TR>
	<TR>
		<TD><LABEL for="genreDiminution"><ct:FWLabel key="JSP_DIM_R_A_GENRE"/></LABEL></TD>
		<TD><ct:inputText name="genreDiminution" readonly="true" styleClass="numeroCourt" style="text-align: right;" disabled="true" /></TD>
		<TD><LABEL for="montant"><ct:FWLabel key="JSP_DIM_R_A_MONTANT"/></LABEL></TD>
		<TD><ct:inputText name="montant" readonly="true" styleClass="numero" style="text-align: right;" disabled="true" /></TD>
		<TD colspan="2"></TD>
	</TR>
	<TR>
		<TD><LABEL for="dateDebutDroit"><ct:FWLabel key="JSP_DIM_R_A_DATE_DEBUT_DROIT"/></LABEL></TD>
		<TD><ct:inputText name="dateDebutDroit" readonly="true" styleClass="date" disabled="true" style="text-align: right;" /></TD>
		<TD><LABEL for="dateFinDroit"><ct:FWLabel key="JSP_DIM_R_A_DATE_FIN_DROIT"/></LABEL>&nbsp;<ct:FWLabel key="JSP_DIM_R_A_FORMAT_DATE_FIN_DROIT"/></TD>
		<TD>
			<input	id="dateFinDroit"
					name="dateFinDroit"
					data-g-calendar="type:month"
					value="<%=viewBean.getDateFinDroit()%>" />

				<script type="text/javascript">
					document.getElementById("dateFinDroit").onchange = testRenteAccordee;
				</script>

		</TD>
		<TD><LABEL for="csCodeMutation"><ct:FWLabel key="JSP_DIM_R_A_CODE_MUTATION"/></LABEL></TD>
		<TD>
			<ct:select name="csCodeMutation" onchange="testRenteAccordee()">

					<%java.util.Iterator iterator = cucsCodeMutation.iterator();
				 		while(iterator.hasNext()){
				 			String cu = (String)(iterator.next());
				 			String cs = (String)(iterator.next());%>

							<option value="<%=cs%>" <%=cs.equals(viewBean.getCsCodeMutation())?"selected":""%>><%=cu%> - <%=objSession.getCodeLibelle(cs)%> </option>

					<% } %>

			</ct:select>
		</TD>
	</TR>
	<TR>
		<TD colspan="4">&nbsp;</TD>
		<TD><LABEL for="csCodeTraitement"><ct:FWLabel key="JSP_DIM_R_A_TRAITEMENT"/></LABEL></TD>
		<TD>
			<ct:select name="csCodeTraitement" wantBlank="true" onchange="testRenteAccordee()">
				<ct:optionsCodesSystems csFamille="REGENDIM"/>
			</ct:select>
		</TD>
	</TR>
	<TR>
		<TD colspan="6" height="20px;">&nbsp;</TD>
	</TR>
	<TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
		<TD><SPAN id="test">&nbsp;</SPAN></TD>
	</TR>
	<TR><TD colspan="6" height="20">	
	
	</TD></TR>




<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
	<% if (!processLaunched) { %>
	<div align="right">
		<tr align="right">
			<td bgcolor="#FFFFFF" colspan="3" align="center">					
				<input id="btnOk" name="btnOk" type="button" value="<%=btnValLabel%>" onclick=""> 			
				<input type="button" value="<%=btnCanLabel%>" onclick="cancel();">
			</td>
		</tr>
	</div>
	<% } %>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<script>
testRenteAccordee();
</script>
<%-- /tpl:put --%>
<script>
try {
		document.getElementById("btnOk").tabIndex="3";
	} catch (ex) {
		//do nothing, valid is ok
	}
</script>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>

