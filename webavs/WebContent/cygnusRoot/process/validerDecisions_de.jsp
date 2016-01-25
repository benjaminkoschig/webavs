<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.cygnus.utils.RFPropertiesUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.process.RFValiderDecisionsViewBean"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%@page import="globaz.cygnus.api.decisions.IRFDecisions"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_TR_PREPARER_DECISIONS"

	idEcran="PRF0042";
	
	RFValiderDecisionsViewBean viewBean = (RFValiderDecisionsViewBean) session.getAttribute("viewBean");
	
	userActionValue=IRFActions.ACTION_VALIDER_DECISIONS + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">


$(document).ready(function() {
	var $radioCreationDecision = $('#radioCreationDecision');
	var $radioSimulationCreationDecision = $('#radioSimulationCreationDecision');
	var $hiddenSometimes = $('.hiddenSometimes');
	
	
<%	if (IRFDecisions.TYPE_VALIDATION_DECISION_NORMAL.equals(viewBean.getTypeValidation())) { %>
		$radioCreationDecision.prop('checked', true);
		$hiddenSometimes.show();		
<%	} else { %>	
		$radioSimulationCreationDecision.prop('checked', true);
		$hiddenSometimes.hide();		
<%	} %>

	$radioCreationDecision.click(function() {
		$hiddenSometimes.show();
	});
	$radioSimulationCreationDecision.click(function() {
		$hiddenSometimes.hide();		
	});
});


function postInit (){
	var $miseEnGed = $('#miseEnGed');
	var $radioCreationDecision = $('#radioCreationDecision');
	var $radioSimulationCreationDecision = $('#radioSimulationCreationDecision');
	var $etatMiseEnGed = $('#etatMiseEnGed');
	
	$miseEnGed.hide();
	
	$radioSimulationCreationDecision.change(function (){
		$miseEnGed.hide();
		$etatMiseEnGed.removeAttr('checked');
	});
		
	$radioCreationDecision.change(function (){
		$miseEnGed.show();
	});
	
}

var isAmalIncoherent = false;

</script>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<%if(JadeStringUtil.isEmpty(viewBean.getIdDecision())){ %>
			<ct:FWLabel key="JSP_TR_VALIDER_DECISIONS_TITRE"/> <%-- /tpl:put --%>
<%}else { %>
			<ct:FWLabel key="JSP_TR_GENERER_DECISION_SELECTIONNEE_TITRE"/> <%-- /tpl:put --%>
<%} %>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
 						<%if (viewBean.validerDecisionGestionnaire()) {%>
							<TR>
								<TD width="200px"><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
								<TD colspan="5">
									<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
			                        																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
								</TD>
							</TR>
						<% } %>									
						<TR><TD colspan="6">&nbsp;</TD></TR>						
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_TR_PREPARER_DECISIONS_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress())?
																				 viewBean.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>						
						<TR>
							<TD><LABEL for="dateSurDocument"><ct:FWLabel key="JSP_TR_PREPARER_DECISIONS_DATE_SUR_DOCUMENT"/></LABEL></TD>
							<TD><input data-g-calendar=" " name="dateSurDocument" value="<%=viewBean.getDateSurDocument()%>"/></TD>
						</TR>
						<% if(!JadeStringUtil.isEmpty(viewBean.getIdDecision())){ %>	
							<TR>
								<TD><LABEL for="idDecision"><ct:FWLabel key="JSP_TR_VALIDER_DECISIONS_ID_DECISION"/></LABEL></TD>
								<TD><INPUT type="text" disabled="disabled" name="numeroDecision" value="<%=viewBean.getNumeroDecision()%>"></TD>
								<INPUT type="hidden" name="idDecision" value="<%=viewBean.getIdDecision()%>">
							</TR>
							<%if(viewBean.getDisplaySendToGed()==true) {%>
								<TR>
									<TD></TD>
									<TD>
										<DIV >
	        	       						<INPUT type="checkbox" value="on" name="miseEnGed" <%=viewBean.getMiseEnGed().booleanValue()?"CHECKED":""%>/>
	        	       						<ct:FWLabel key="JSP_PROCESS_MISE_EN_GED_VALIDATION"/>
	        	       					</DIV>
									</TD>
								</TR>
							<%}%> 
						<% } else { %>				
							<TR><TD colspan="6">&nbsp;</TD></TR>							
							<TR>																						
								<TD><ct:FWLabel key="JSP_RF_SIMULER_VALIDATION_DECISION" /></TD>
								<TD>
									<input	type="radio" 
											id="radioSimulationCreationDecision" 
											name="typeValidation" 
											value="<%=IRFDecisions.TYPE_VALIDATION_DECISION_SIMULATION%>" />
								</TD>
							</TR>
							<TR>						
								<TD><ct:FWLabel key="JSP_RF_VALIDATION_DECISION" /></TD>
								<TD>
									<input	type="radio" 
											id="radioCreationDecision" 
											name="typeValidation" 
											value="<%=IRFDecisions.TYPE_VALIDATION_DECISION_NORMAL%>" />								
								</TD>
								<INPUT type="hidden" name="_lancerValidation" />
							</TR>
	<%--					<TR>
								<TD></TD>
								<TD>
									<DIV id="miseEnGed">
        	       						<INPUT id="etatMiseEnGed" type="checkbox" checked value="on" name="miseEnGed" <%=viewBean.getMiseEnGed().booleanValue()?"CHECKED":""%> />
        	       						<ct:FWLabel key="JSP_PROCESS_MISE_EN_GED_VALIDATION"/>
        	       					</DIV>
								</TD>
							</TR>
	--%>	
						<% } %>	
												
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) 
{ %> 
<%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>