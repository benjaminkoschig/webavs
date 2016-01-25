<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI0047";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressepaiement.tauxMonnaie.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Posizionamento<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
		<TR>
            <TD nowrap width="128">Indicazioni </TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="fromLibelle" size="35" maxlength="40" tabindex="-1">
	     </TD>
          </TR>

	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
			<TD bgcolor="#FFFFFF" colspan="3" align="right"><A href="javascript:document.forms[0].submit();">
				<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
			</A>
			<%  if (request.getParameter("_type") == null || request.getParameter("_type").equalsIgnoreCase("rc")) { %><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.adressepaiement.tauxMonnaie.afficher&_method=add'">
				<IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0">
				</A><%  }  %></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>