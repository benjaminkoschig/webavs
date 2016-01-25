<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0015";
rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
String forNumCaisse = request.getParameter("forNumCaisse");
String forOrdreRegroupement = request.getParameter("forOrdreRegroupement");
String forNature = request.getParameter("forNature");
%>
<SCRIPT>
usrAction = "musca.facturation.ordreRegroupement.lister";
bFind = true;
</SCRIPT>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des ordres et des regroupements<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
            <TD nowrap width="149">Ordre</TD>
            <TD nowrap>
            	<INPUT type="text" name="forOrdreRegroupement" class="libelleLong" value='<%=forOrdreRegroupement!=null?forOrdreRegroupement:""%>'>
          	</TD>
		</TR>
		<TR>
            <TD nowrap width="149">Numéro de caisse</TD>
            <TD nowrap>
            	<INPUT type="text" name="forNumCaisse" class="libelleLong" value='<%=forNumCaisse!=null?forNumCaisse:""%>'>
          	</TD>
		</TR>
		<TR>
            <TD>Nature</TD>
            <TD><ct:FWSystemCodeSelectTag name="forNature"
            		defaut='<%=forNature!=null?forNature:""%>'
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsCatNewFacture(session)%>"/></TD>
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