<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="GLI0017";
 	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="li-optionsempty"/>

<SCRIPT language="JavaScript">
	bFind = false;
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_FX_RC%>.lister";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_RC_RUSER_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD><ct:FWLabel key="ECRAN_RC_RUSER_VISA"/></TD>
						<TD><INPUT title="visa" type="text" name="forVisa" class="libelleLong"></TD>
						<TD><ct:FWLabel key="ECRAN_RC_RUSER_NOM"/></TD>
						<TD><INPUT title="nom" type="text" name="likeLastName" class="libelleLong"></TD>
						<TD><ct:FWLabel key="ECRAN_RC_RUSER_PRENOM"/></TD>
						<TD><INPUT title="prenom" type="text" name="likeFirstName" class="libelleLong"></TD>										
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