<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0012";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.planCaisse.planCaisse.lister";
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Verwaltung der Versicherungspläne - Verbandskassen
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR>
                        	<TD nowrap width="128">Kassen-Nr.</TD>
                        	<TD nowrap colspan="2" width="200">
                        		<INPUT type="text" name="forNumeroCaisse" size="25" maxlength="25" tabindex="1">
                        	</TD>
               
 		 				</TR>
                    	<TR>
                        	<TD nowrap width="128">Versicherungsplan</TD>
                        	<TD nowrap colspan="2" width="200">
                        		<INPUT type="text" name="fromLibelle" size="40" maxlength="40" tabindex="2">
                        	</TD>
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