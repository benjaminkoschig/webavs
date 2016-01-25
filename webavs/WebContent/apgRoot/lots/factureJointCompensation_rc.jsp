<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0025";

globaz.apg.vb.lots.APLotViewBean viewBean = (globaz.apg.vb.lots.APLotViewBean) request.getAttribute("viewBean");

bButtonNew = false;
IFrameDetailHeight = "300";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_COMPENSATIONS_LOT%>.lister";
	detailLink = "<%=servletContext + mainServletPath%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_FACTURES_LOT%>.actionAfficherEcranDE&idLot=<%=viewBean.getIdLot()%>&choisir=<%=request.getParameter("choisir")%>";	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_COMPENSATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD><TABLE>
							<TR>
								<TD><ct:FWLabel key="JSP_NO_LOT"/></TD>
								<TD><INPUT type="text" name="" value="<%=viewBean.getNoLot()%>" class="numeroCourtDisabled" readonly></TD>
								<TD><ct:FWLabel key=""/></TD>
								<TD><ct:FWLabel key="JSP_LIBELLE"/></TD>
								<TD>
									<INPUT type="text" name="" value="<%=viewBean.getDescription()%>" class="disabled" readonly>
									<INPUT type="hidden" name="forIdLot" value="<%=viewBean.getIdLot()%>">
								</TD>
							</TR>
						</TABLE></TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>