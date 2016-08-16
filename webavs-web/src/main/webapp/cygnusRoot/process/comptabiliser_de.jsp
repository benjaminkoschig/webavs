<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.process.RFComptabiliserViewBean"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.cygnus.api.decisions.IRFDecisions"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LER_D"

	idEcran="PRF0058";
	
	RFComptabiliserViewBean viewBean = (RFComptabiliserViewBean) session.getAttribute("viewBean");
	
	userActionValue=IRFActions.ACTION_COMPTABILISER + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

function selectChangeISO20022(s) {
	changeAffichageISO20022(s[s.selectedIndex].id);
}

function changeAffichageISO20022(cs) {
	if(cs==258003){ // 258003 traitement ISO20022
		$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
	} else {
		$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
	} 
}


function init(){

	
	
	<%-- $("#radioCreationDecision").click(function() {
		$(".hiddenSometimes").removeAttr("style");
	});
	$("#radioSimulationCreationDecision").click(function() {
		$(".hiddenSometimes").attr("style","display:none");		
	}); --%>
	
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors();
	errorObj.text="";
	<%}%>				
	
	
}	

$(document).ready(function() {
	//ne prend pas l'init! pourquoi? 
	selectChangeISO20022(document.getElementById("idOrganeExecution"));
});
	<%-- $(function(){
		<if("validationReelle".equals(viewBean.getTypeValidation())){%>
			$("#radioCreationDecision").attr('checked','checked');
			$(".hiddenSometimes").removeAttr("style");		
		<} else {>	
			$("#radioSimulationCreationDecision").attr('checked','checked');
			$(".hiddenSometimes").attr("style","display:none");		
		< } >
	});
	
	$(document).ready(function() {
			
		$("#radioCreationDecision").click(function() {
			$(".hiddenSometimes").removeAttr("style");
		});
		$("#radioSimulationCreationDecision").click(function() {
			$(".hiddenSometimes").attr("style","display:none");		
		});
	}); --%>

</script>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PROCESS_COMPTABILISER_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="200px"><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
							<TD colspan="5">
								<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
		                        																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
							</TD>
						</TR>
						<TR>
							<!-- <TD><LABEL for="descriptionLot"><ct:FWLabel key="JSP_VALID_LOT_DESCR"/></LABEL></TD> -->
							<TD>							
								<INPUT type="hidden" name="idLot" size="6" maxlength="6" readonly="true" disabled="true" value="<%=viewBean.getIdLot()%>" class="libelleShort">
								<INPUT type="hidden" name="descriptionLot" readonly="true" disabled="true" value="<%=viewBean.getDescriptionLot()%>" class="libelleLong">
							</TD>
						</TR>					
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_TR_PREPARER_DECISIONS_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress())?
																				 viewBean.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>			

												
						<!-- <TR>																						
							<TD><ct:FWLabel key="JSP_RF_SIMULER_COMPTABILISATION_DECISION" /></TD>
							<TD>
								<INPUT type="radio" id="radioSimulationCreationDecision" name="typeValidation" value="<IRFDecisions.TYPE_VALIDATION_DECISION_SIMULATION%>"
								<(JadeStringUtil.isEmpty(viewBean.getTypeValidation()) || "1".equals(viewBean.getTypeValidation()))?"CHECKED":""%> />
							</TD>
						</TR>
						<TR>						
							<TD><ct:FWLabel key="JSP_RF_COMPTABILISATION_DECISION" /></TD>
							<TD>
								<INPUT type="radio" id="radioCreationDecision" name="typeValidation" value="<IRFDecisions.TYPE_VALIDATION_DECISION_NORMAL%>"
								<(!JadeStringUtil.isEmpty(viewBean.getTypeValidation()) && "2".equals(viewBean.getTypeValidation()))?"CHECKED":""%> />								
							</TD>
							<INPUT type="hidden" name="_typeValidation" />
						</TR> -->										
						<TR>
							<TD class="hiddenSometimes"><LABEL for="dateComptable"><ct:FWLabel key="JSP_RF_COMPTABILISATION_DATE_COMPTABLE"/></LABEL></TD>
							<!-- <TD class="hiddenSometimes"><ct:FWCalendarTag name="dateComptable" value="<viewBean.getDateComptable()%>"/></TD> -->
							<TD><input data-g-calendar=" "  name="dateComptable" value="<%=viewBean.getDateComptable()%>"/></TD>													
						</TR>	
						<TR><TD class="hiddenSometimes" colspan="6">&nbsp;</TD></TR>																		
						<TR>
							<TD class="hiddenSometimes"><LABEL for="dateEcheancePaiement"><ct:FWLabel key="JSP_EPM_DATE_ECHEANCE"/></LABEL></TD>
							<!-- <TD class="hiddenSometimes"><ct:FWCalendarTag name="dateEcheancePaiement" value="<viewBean.getDateEcheancePaiement()>"/></TD> -->											
							<TD><input data-g-calendar=" "  name="dateEcheancePaiement" value="<%=viewBean.getDateEcheancePaiement()%>"/></TD>
						</TR>
						<TR>
							<TD class="hiddenSometimes"><LABEL for="idOrganeExecution"><ct:FWLabel key="JSP_EPM_ORGANE_EXECUTION"/></LABEL></TD>
							<TD class="hiddenSometimes">
<%-- 								<ct:FWListSelectTag name="idOrganeExecution" data="<%=viewBean.getOrganesExecution()%>" defaut='<%=JadeStringUtil.isEmpty(viewBean.getIdOrganeExecution())?"":viewBean.getIdOrganeExecution()%>'/> --%>
									<% 	Vector<String[]> _CsOrganeExecution = viewBean.getOrganesExecution();%> 
									<select id="idOrganeExecution" name="idOrganeExecution" onchange="selectChangeISO20022(this)">
								    <%for (int i=0; i < _CsOrganeExecution.size(); i++) {
										String[] _organeExecution = _CsOrganeExecution.get(i);
										if (_organeExecution[0].equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
	               							<option selected value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
	                
	             					<%} else { %>			
									  		<option value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
									<%} 
									}%>
							</TD>													
						</TR>		

						<TR class="classNonIso">
							<TD class="hiddenSometimes"><LABEL for="numeroOG"><ct:FWLabel key="JSP_EPM_NUMERO_OG"/></LABEL></TD>
							<TD class="hiddenSometimes">
								<INPUT type="text" name="numeroOG" value="<%=viewBean.getNumeroOG()%>" class="libelleShort"/>
							</TD>													
						</TR>
						<TR class="classIso">
							<TD class="hiddenSometimes"><LABEL for="isoGestionnaire"><ct:FWLabel key="JSP_EPM_ISO_GEST"/></LABEL></TD>
							<TD class="hiddenSometimes">
								<INPUT type="text" name="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" class="libelleShort"/>
							</TD>													
						</TR>
						<TR class="classIso">
							<TD class="hiddenSometimes"><LABEL for="isoHighPriority"><ct:FWLabel key="JSP_EPM_ISO_PRIO"/></LABEL></TD>
							<TD class="hiddenSometimes">
											<select id="isoHighPriority" name="isoHighPriority">
								                <OPTION selected value="0">normale</OPTION>
								                <OPTION value="1">haute</OPTION>
							              	</select>
							</TD>													
						</TR>
						<TR class="classIso">
							<TD class="hiddenSometimes"><LABEL for=isoCsTypeAvis><ct:FWLabel key="JSP_EPM_ISO_TYPE_AVIS"/></LABEL></TD>
							<TD class="hiddenSometimes">
								<ct:FWCodeSelectTag name="isoCsTypeAvis" defaut="<%=viewBean.getIsoCsTypeAvis()%>" codeType="OSIOGTYA" />
							</TD>													
						</TR>
						<TR><TD class="hiddenSometimes" colspan="6">&nbsp;</TD></TR>											
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) 
{ %> 
<%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>