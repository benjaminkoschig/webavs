<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3005";

	globaz.apg.vb.process.APGenererCompensationsViewBean viewBean = (globaz.apg.vb.process.APGenererCompensationsViewBean)(session.getAttribute("viewBean"));
	
	//On redirige vers le _rc des lots si on n'a rien à faire ici si le lot n'est pas ouvert
	
	boolean isOuvertOuVideOuCompense = globaz.apg.api.lots.IAPLot.CS_OUVERT.equals(viewBean.getEtatLot()) || globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getEtatLot()) || globaz.apg.api.lots.IAPLot.CS_COMPENSE.equals(viewBean.getEtatLot());
	if (!isOuvertOuVideOuCompense){
		userActionValue="apg.lots.lot.chercher";
	} else {
	 	userActionValue="apg.process.genererCompensations.executer";
	}
	
	java.util.Vector v = globaz.apg.db.lots.APLotManager.getIdsDescriptionsLotsOuvertsOuCompenses(viewBean.getSession(), null);
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<%@page import="java.util.Vector"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_COMPENSATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" <%=(isOuvertOuVideOuCompense)?"":"class=\"disabled\" readonly disabled"%>></TD>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						
						<TR>
							<TD><ct:FWLabel key="JSP_LOTS_NON_VALIDES"/></TD>
							<TD>
								<%if (v!=null && v.size()>0){%>
									<%if (!isOuvertOuVideOuCompense) {%>

										<input type="text" name ="lot" value="<%=viewBean.getDescriptionLot()%>">
										<input type="hidden" name ="forIdLot" value="<%=viewBean.getForIdLot()%>">
									<%} else {%>
											<ct:FWListSelectTag data="<%=v%>" 
													defaut="<%=viewBean.getForIdLot()%>" 
													name="forIdLot"/>								
								
									<%}
								}
								else{%>
									<ct:FWLabel key="JSP_AUCUN_LOT_NON_VALIDE"/>
								<%}%>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOIS_PROCHAINE_FACTURATION"/></TD>
							<TD>
							<ct:FWLabel key="JSP_MOIS"/>	
							<ct:FWListSelectTag data="<%=viewBean.getMonths()%>" defaut="<%=viewBean.getMoisPeriodeFacturation()%>" name="MoisPeriodeFacturation"/>
							</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>