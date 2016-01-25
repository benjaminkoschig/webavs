<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0018";

globaz.ij.vb.lots.IJLotViewBean viewBean = (globaz.ij.vb.lots.IJLotViewBean) request.getAttribute("viewBean");

bButtonNew = false;
IFrameDetailHeight = "300";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_COMPENSATIONS_LOT%>.lister";
	detailLink = "<%=servletContext + mainServletPath%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.actionAfficherEcranDE&idLot=<%=viewBean.getIdLot()%>&choisir=<%=request.getParameter("choisir")%>";
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
								<TD><ct:FWLabel key="JSP_DESCRIPTION"/></TD>
								<TD>
									<INPUT type="text" name="" value="<%=viewBean.getDescription()%>" class="PRlibelleExtraLongDisabled" readonly>
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