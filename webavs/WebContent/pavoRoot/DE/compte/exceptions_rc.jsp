<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.globall.util.*" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
	idEcran = "CCI0026";
 	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
usrAction = "pavo.compte.exceptions.lister";
detailLink = servlet+"?userAction=pavo.compte.InscriptionsManuelles.afficher&_method=add";
subTableHeight = 50;
bFind= false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				Verwaltung der Ausnahmen
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Abr.-Nr.</TD>
							<TD>
							<ct:FWPopupList name="likeNumeroAffilie" value="" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3" 
								size="15" maxlength="15"/>
							</TD>
							</TR>
							<TR>
							<TD>SVN</TD>
							<TD><nss:nssPopup value='' name="likeNumeroAvs" 
							jspName="<%=jspLocation%>" 
							cssclass="visible"  avsMinNbrDigit="8" avsAutoNbrDigit="11"
							nssMinNbrDigit="7" nssAutoNbrDigit="10"
							/></td>
							</TR>
						
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>