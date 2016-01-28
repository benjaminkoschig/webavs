<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	subTableHeight = 50;
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	idEcran = "CCI0025";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	bButtonNew = objSession.hasRight("pavo.compte.InscriptionsManuelles.afficher","ADD");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="Javascript">
usrAction = "pavo.compte.InscriptionsManuelles.lister";
detailLink = servlet+"?userAction=pavo.compte.InscriptionsManuelles.afficher&_method=add";
subTableHeight = 50;
bFind= false;
</SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche d'une inscription manuelle<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Affilié</TD>
							<TD>
							<ct:FWPopupList name="fromAffilie" value="" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3"/>
							</TD>
							</TR>
							<TR>
							<TD>Année</TD>
							<TD><input type="text" name="fromAnnee" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"/></td>
							</TR>
							<TR>
							<TD>Montant</TD>
							<TD><INPUT type="text" name="fromMontant" onkeypress="return filterCharForPositivFloat(window.event);" size="12"/></TD>
							</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>