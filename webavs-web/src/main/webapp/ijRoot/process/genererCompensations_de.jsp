<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ3002";
	globaz.ij.vb.process.IJGenererCompensationsViewBean viewBean = (globaz.ij.vb.process.IJGenererCompensationsViewBean)(session.getAttribute("viewBean"));
	java.util.Vector v = globaz.ij.db.lots.IJLotManager.getIdsDescriptionsLotsOuvertsOuCompenses(viewBean.getSession());
	//On redirige vers le _rc des lots si on n'a rien à faire ici si le lot n'est pas ouvert
	
	boolean isOuvertOuVideOuCompense = globaz.ij.api.lots.IIJLot.CS_OUVERT.equals(viewBean.getCsEtatLot()) || globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getCsEtatLot()) || globaz.ij.api.lots.IIJLot.CS_COMPENSE.equals(viewBean.getCsEtatLot());
	if (!isOuvertOuVideOuCompense){
		userActionValue="ij.lots.lot.chercher";
	} else {
	 	userActionValue="ij.process.genererCompensations.executer";
	}
	
	
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="java.util.Vector"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
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
								} else{%>
									<ct:FWLabel key="JSP_AUCUN_LOT_A_COMPENSER"/>
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