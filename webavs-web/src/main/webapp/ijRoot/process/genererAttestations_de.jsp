<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PIJ2003";
	userActionValue=globaz.ij.servlet.IIJActions.ACTION_GENERER_ATTESTATIONS_IJ + ".executer";
	globaz.ij.vb.process.IJGenererAttestationsViewBean viewBean = (globaz.ij.vb.process.IJGenererAttestationsViewBean)(session.getAttribute("viewBean"));
	String[] annees = viewBean.getAnneesList();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 

	function changeType() {
  		document.forms[0].elements("userAction").value = "<%=globaz.ij.servlet.IIJActions.ACTION_GENERER_ATTESTATIONS_IJ%>.afficher";
		document.forms[0].submit();
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_ATTESTATIONS_FISCALES"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="350">
									<TR>
										<TD><ct:FWLabel key="JSP_ATT_FISCALES_UNIQUE"/></TD>
										<TD>
											<INPUT type="radio" name="isGenerationUnique" value="on" <%=viewBean.getIsGenerationUnique().booleanValue()?"CHECKED":""%> onclick="changeType()"> 
										</TD>
									</TR>
									<% if (viewBean.getIsGenerationUnique().booleanValue()) { %> 
										<TR>
											<TD><ct:FWLabel key="JSP_ATT_FISCALES_NSS"/></TD>
											<TD>
				
												<%	String params = "&provenance1=TIERS";	
												   		    params += "&provenance2=CI";
													String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp"; %>
										
												<ct1:nssPopup name="NSS" onFailure="" onChange="" params="<%=params%>" 
													  value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>" 
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3"   />				
											</TD>
										</TR>
										<TR>
											<TD>&nbsp;</TD>
										</TR>
									<% } %>
									<TR>
										<TD><ct:FWLabel key="JSP_ATT_FISCALES_MULTIPLE"/></TD>
										<TD>
							   				<INPUT type="radio" name="isGenerationUnique" value="" <%=!viewBean.getIsGenerationUnique().booleanValue()?"CHECKED":""%> onclick="changeType()"> 
										</TD>
									</TR>

									<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
										<TR>
											<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
											<TD>
												<INPUT type="checkbox" name="isSendToGed" value="on" CHECKED>
												<INPUT type="hidden" name="displaySendToGed" value="1">
											</TD>
										</TR>
										<TR>
											<TD>&nbsp;</TD>
										</TR>
									<% } else {%>
										<INPUT type="hidden" name="isSendToGed" value="FALSE">
										<INPUT type="hidden" name="displaySendToGed" value="0">
									<%} %>
									<TR>
										<TD>&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GENERER_ATTESTATIONS_ANNEE"/></TD>
										<TD>
										   	<SELECT name="annee">
												<%for (int i=0; i<annees.length; i=i+2){%>
												<OPTION value="<%=annees[i]%>" <%=i==10?"selected":""%>><%=annees[i+1]%></OPTION>
												<%}%>
											</SELECT>
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_EMAIL"/></TD>
										<TD>
										    <INPUT type="text" name="email" value="<%=viewBean.getEmail()%>">
										</TD>
									</TR>
								</table>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>