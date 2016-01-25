<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.prestation.api.IPRDemande"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.apg.vb.process.APListePrestationVerseeViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP2004";

	userActionValue="apg.process.listePrestationVersee.executer";
	APListePrestationVerseeViewBean viewBean = (APListePrestationVerseeViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/date.js"></script>

<%
String keyTypePrestation = (String) PRSessionDataContainerHelper.getData(session,PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION);

if(IPRDemande.CS_TYPE_APG.equalsIgnoreCase(keyTypePrestation)){
%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>	
<%} else {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
<% } %>

<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_NUMERO_AFFILIE"/>&nbsp;
							</TD>
							<TD>
							<input type="text" id="numeroAffilie" name="numeroAffilie" value="<%=viewBean.getNumeroAffilie()%>" data-g-autocomplete="service:ch.globaz.naos.business.service.AffiliationService,
                                method:widgetFind,
                                wantInitThreadContext:true,
                                criterias:¦{
                                    likeNumeroAffilie : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NUMERO_AFFILIE"/>',
                                    likeNomUpper : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NOM"/>'
                                }¦,
                                modelReturnVariables:¦affiliation.affilieNumero¦,
                                lineFormatter:#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt},
                                functionReturn:¦
                                    function(element){
                                        this.value= $(element).attr('affiliation.affilieNumero');
                                        }¦">
                                        
						</TD>
					</TR>
					
					<TR>
						<TD>
							<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_GENRE_PERIODE"/>&nbsp;
						</TD>
						<TD>
							<ct:FWCodeSelectTag name="selecteurPrestation" defaut="<%=viewBean.getSelecteurPrestation()%>" codeType="APLPVPRSEL" wantBlank="false"  />                                        
						</TD>
					</TR>
					
					<TR>
						<TD>
							<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_PERIODE_DU"/>&nbsp;
						</TD>
						<TD>
							<input type="text" id="dateDebut" name="dateDebut" value="<%=viewBean.getDateDebut()%>" data-g-calendar="mandatory:true"> 
							&nbsp;&nbsp; <ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_PERIODE_AU"/> &nbsp;&nbsp;
							<input type="text" id="dateFin" name="dateFin" value="<%=viewBean.getDateFin()%>" data-g-calendar="mandatory:true">
						</TD>
					</TR>
					
					<TR>
						<TD>
							<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_MAIL"/>&nbsp;
						</TD>
						<TD>
							<INPUT type="text" id="eMailAddress" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
						</TD>
					</TR>
					
					<%if(viewBean.isDisplayGedCheckbox()) { %>
						<input type="hidden" id="displayGedCheckbox" name="displayGedCheckbox" value="on">
						<TR>
							<TD>
								<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_ENVOYER_GED"/>&nbsp;
							</TD>
							<TD>
								<input type="checkbox" id="envoyerGed" name="envoyerGed" checked="checked">
							</TD>
						</TR>
					<% } %>
					
					
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>