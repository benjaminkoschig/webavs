<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0017";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.tiers.succursale.lister";
bFind = true;
</SCRIPT>
<%
	globaz.pyxis.db.tiers.TISuccursaleViewBean viewBean = (globaz.pyxis.db.tiers.TISuccursaleViewBean)session.getAttribute ("viewBean");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Positionierung<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

	<TR>
            <TD nowrap width="128">Hauptsitz</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom(viewBean.getIdTiersParent())%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD nowrap width="50" align="right">&nbsp;</TD>
            <TD nowrap width="50" align="left">Mitglied</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNumAffilie(viewBean.getIdTiersParent())%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
		<TD nowrap width="128"></TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite(viewBean.getIdTiersParent())%>" readonly>
		<INPUT type="hidden" name="forIdTiersParent" value='<%=request.getParameter("forIdTiersParent")%>' >
	     </TD>
          </TR>

	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>

		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>