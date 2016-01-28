<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP0022";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Abgang - Abgangsbetr�ge"
usrAction = "phenix.principale.sortieMontant.lister";
servlet = "phenix";
bFind=true;
</SCRIPT>
<%
globaz.phenix.db.principale.CPSortieMontantViewBean viewBean = (globaz.phenix.db.principale.CPSortieMontantViewBean )session.getAttribute ("viewBean");							
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Abgangsbetr�ge<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
				        <TR>
				            <TD nowrap width="128"align="left">Versicherter</TD>
				            <TD nowrap colspan="2">
								<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
							</TD>
							<TD width="50"></TD>
						    <TD nowrap width="128" align="left">Mitglied</TD>
					    	<TD nowrap >
								<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
						    </TD>
						</TR>
						<TR>
					        <TD nowrap width="128" align="left">Abgangs-Nr.</TD>
					        <TD nowrap colspan="2">
								<INPUT type="text" name="idSortie" tabindex="-1" value="<%=viewBean.getIdSortie()%>" class="libelleLongDisabled" readonly>
						    </TD>
							<TD width="50"></TD>
						    <TD nowrap width="128" align="left">Jahr</TD>
					    	<TD nowrap >
								<INPUT type="text" name="annee" tabindex="-1" value="<%=viewBean.getAnnee()%>" class="libelleLongDisabled" readonly>
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