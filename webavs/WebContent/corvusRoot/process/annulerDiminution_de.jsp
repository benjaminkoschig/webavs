<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="java.util.Iterator"%>
<%@page import="globaz.corvus.vb.process.REAnnulerDiminutionViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<SCRIPT type="text/javascript" src="<%=servletContext%>/corvusRoot/ajax.js"></SCRIPT>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE0079";

	REAnnulerDiminutionViewBean viewBean = (REAnnulerDiminutionViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	selectedIdValue = viewBean.getIdRenteAccordee();
	String noRenteAccordee = viewBean.getIdRenteAccordee();
	String idTierBeneficiaire = viewBean.getIdTiersBeneficiaire();
	globaz.globall.db.BSession objSession = viewBean.getSession();
	String requerantDescription = viewBean.getTiersBeneficiaireInfo();
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE + ".executer";

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idBaseCalcul = request.getParameter("idBaseCalcul");				
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");	
	showProcessButton = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT type="text/javascript">
function cancel(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.chercher";
   	   document.forms[0].submit();
}


function valider(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE%>.executer";
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
}

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
	}
}
function doInitThings() {

	showErrors();
}


function afficheTestError(msgRetenue){
	
	var monEl = document.getElementById("btnOk");
	jscss("add", monEl, "inactive", null);
	
	document.getElementById("infoAnnulerDiminutionError").innerHTML='<strong>' + msgRetenue + '</strong>';
	document.getElementById("errorMsg").value=msgRetenue;
	if (msgRetenue == '') {
		var monEl = document.getElementById("btnOk");
		jscss("remove", monEl, "inactive", null);
	}
}


function testExistanceAnnonceDiminution() {		
	doQuery("<%=servletContext%><%=mainServletPath%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE%>.testAnnonceDiminutionError&idRenteAccordee=<%=noRenteAccordee%>&idTiersBeneficiaire=<%=idTierBeneficiaire%>&dateDebutDroitRA=" + document.getElementById('dateDebutDroitRA').value + "&dateFinDroitRA=" + document.getElementById('dateFinDroitRA').value + "&genreRente=" + document.getElementById('genreRente').value + "&codeMutation=" + document.getElementById('codeMutation').value, afficheTestError);	
}


</SCRIPT>



<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNULER_DIM_R_A_TITRE"/><%-- /tpl:put --%>

<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR><TD colspan="6" style="color:red;"><SPAN id="infoAnnulerDiminutionError">&nbsp;</SPAN></TD></TR>
	<input type="hidden" name="errorMsg"/>
	
	<table border="0" width="100%">
	<TR>
		<TD width="15%"><LABEL for="tiersBeneficiaireInfo"><b><ct:FWLabel key="JSP_DIM_R_A_BENEFICIAIRE"/></b></LABEL></TD>
		<TD colspan="5">			
			<ct:inputText name="tiersBeneficiaireInfo" readonly="true" styleClass="RElibelleExtraLongDisabled" disabled="true" />
			<ct:inputHidden name="idTiersBeneficiaire"/>
		</TD>
	</TR>
	<TR><TD colspan="6"><HR></TD></TR>
	<TR>
		<TD width="15%"><LABEL for="idRenteAccordee"><ct:FWLabel key="JSP_DIM_R_A_NO"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="idRenteAccordee" readonly="true" styleClass="numeroCourt" style="text-align: right;" disabled="true" /></TD>
		<TD colspan="4">&nbsp;</TD>
	</TR>
	<TR>
		<TD colspan="6" height="20px;">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="15%"><LABEL for="genreRente"><ct:FWLabel key="JSP_DIM_R_A_GENRE"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="genreRente" readonly="true" styleClass="numeroCourt" style="text-align: right;" disabled="true" /></TD>
		<TD width="15%"><LABEL for="montant"><ct:FWLabel key="JSP_DIM_R_A_MONTANT"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="montant" readonly="true" styleClass="numero" style="text-align: right;" disabled="true" /></TD>
		<TD width="10%"><LABEL for="codeMut"><ct:FWLabel key="JSP_DIM_R_A_CODE_MUTATION"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="descriptionCodeMutation" readonly="true" styleClass="PRlibelleLongDisabled" disabled="true" /></TD>		
		
	</TR>


	<TR>
		<TD width="15%"><LABEL for="dateDebutDroit"><ct:FWLabel key="JSP_DIM_R_A_DATE_DEBUT_DROIT"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="dateDebutDroitRA" readonly="true" styleClass="date" disabled="true" style="text-align: right;" /></TD>
		<TD width="15%"><LABEL for="dateFinDroit"><ct:FWLabel key="JSP_DIM_R_A_DATE_FIN_DROIT"/></LABEL></TD>
		<TD width="15%"><ct:inputText name="dateFinDroitRA" readonly="true" styleClass="date" disabled="true" style="text-align: right;" /></TD>		
		<td/><td/>			
	</TR>

	<TR>
		<TD/>
		<TD/>
		<TD><LABEL for="dateFinDroitModifiee"><ct:FWLabel key="JSP_ANNUL_DIM_R_A_DATE_FIN_DROIT_M"/></LABEL></TD>
		<TD colspan="3">
			<input	id="dateFinDroitModifiee"
					name="dateFinDroitModifiee"
					data-g-calendar="type:month"
					value="" /> 
			[mm.aaaa]
		</TD>
	</TR>

	<TR>
		<TD/>
		<TD/>
		<TD width="15%"><LABEL for="codeMutation"><ct:FWLabel key="JSP_ANNUL_DIM_R_A_NOUV_CODE_MUT"/></LABEL></TD>				
		<TD colspan="3">
		
			<SELECT name="csCodeMutation">
			<OPTION value=""> </OPTION>
			<%
					Iterator<String> iter = viewBean.getCUCSCodeMutation().iterator();
			
			
					 while (iter.hasNext()) {
						 String cu = iter.next();
						 String cs = iter.next();
						 %>
						 	<OPTION value="<%=cs%>" <%=viewBean.getCsCodeMutation().equals(cs)?"selected":""%>>
						 		<%=cu + " - " + viewBean.getDescriptionCodeMutation(cs)%>
						 	</OPTION>						 	
						 <%					 				
					 }
					%>											
			</SELECT>
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
	</TR>
	<TR><TD colspan="6" height="20"></TD></TR>
	</table>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
	<% if (!processLaunched) { %>
	<div align="right">
		<tr align="right">
			<td bgcolor="#FFFFFF" colspan="3" align="center">			
				<input id="btnOk" type="button" value="Valider" onClick="doOkAction();">  			
				<input type="button" value="Annuler" onclick="cancel();">
			</td>
		</tr>
	</div>
	<% } %>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>

<script>
testExistanceAnnonceDiminution();
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

