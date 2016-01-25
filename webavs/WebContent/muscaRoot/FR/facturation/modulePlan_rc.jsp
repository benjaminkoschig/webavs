<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA4003";
rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "musca.facturation.modulePlan.lister";
bFind = true;
</SCRIPT>
<%
	globaz.musca.db.facturation.FAModulePlanViewBean viewBean = (globaz.musca.db.facturation.FAModulePlanViewBean)session.getAttribute ("viewBean");
%>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des modules du plan<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  <TR>
            <TD nowrap width="160">Plan de facturation</TD>
            <TD nowrap><INPUT type="text" name="planFacturation" class="inputDisabled" value="<%=globaz.musca.util.FAUtil.getLibellePlan(session, viewBean.getIdPlanFacturation())%>" style="width : 10cm;"></TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>
	  <TR>
            <TD nowrap width="160">A partir de</TD>
            <TD nowrap><INPUT type="text" name="fromLibelle" style="width : 10cm;"></TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--
			<TD bgcolor="#FFFFFF" colspan="3" align="right">
				<A href="javascript:document.forms[0].submit();">
					<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
				</A>
			</TD>
-->
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>