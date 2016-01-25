<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE2055";
	
	REDeuxiemeLivraisonCentraleViewBean viewBean = (REDeuxiemeLivraisonCentraleViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_2EME_ENVOI_CENTRALE + ".executer";
	
	String[] lots = viewBean.getLotHermesList();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.vb.adaptation.REDeuxiemeLivraisonCentraleViewBean"%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="MENU_OPTION_IMP_2_ANN_LST"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_EMAIL"/></TD>
						<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
					</TR>	
					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_MOIS_ANN"/></TD>
						<TD>
							<input	id="moisAnnee"
									name="moisAnnee"
									data-g-calendar="type:month"
									value="<%=REPmtMensuel.getDateProchainPmt(viewBean.getSession())%>" />
						</TD>
					</TR>				
						<TR>
							<TD><ct:FWLabel key="JSP_CIRC_LOT_HERMES"/></TD>
							<TD>
								<SELECT name="idLot">
									<%for (int i=0; i<lots.length; i=i+2){%>
										<OPTION value="<%=lots[i]%>"><%=lots[i+1]%></OPTION>
									<%}%>
								</SELECT>
							</TD>
						</TR>
											
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>