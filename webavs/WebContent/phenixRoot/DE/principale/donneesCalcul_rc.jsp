<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP0010";%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
top.document.title = "Beiträge - Berechnungsangaben"
usrAction = "phenix.principale.donneesCalcul.lister";
servlet = "phenix";
bFind=true;
</SCRIPT>
<%
globaz.phenix.db.principale.CPDonneesCalculViewBean viewBean = (globaz.phenix.db.principale.CPDonneesCalculViewBean)session.getAttribute ("viewBean");							
bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Berechnungsangaben<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

	<TR>
            <TD nowrap width="128">
            	<ct:ifhasright element="pyxis.tiers.tiers.diriger" crud="r">
            	<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Versicherter</A>
            	</ct:ifhasright>
            </TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
	     </TD>
	<TD width="10"></TD>
            <TD nowrap width="120" align="left">Mitglied-Nr.</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
		<TD nowrap width="128"></TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
	</TD>
	<TD width="10"></TD>
            <TD nowrap width="120" align="left">SVN</TD>
            <TD nowrap>
			<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     </TD>
	     <TD width="50">
			<INPUT type="hidden" name="idDecision" value='<%=request.getParameter("idDecision")%>' >
		</TD>
          </TR>
	 <TR>
		<TD nowrap width="128">Verfügung</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="decision" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getDescriptionDecision()%>" readonly>
 	     </TD>
 	     <TD width="10"></TD>
            <TD nowrap width="120" align="left">Periodizität</TD>
            <TD nowrap>
		<INPUT type="text" name="libellePeriodicite" tabindex="-1" value="<%=viewBean.getLibellePeriodicite()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
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