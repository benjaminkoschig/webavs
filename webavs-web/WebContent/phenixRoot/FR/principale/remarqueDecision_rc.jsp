
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.globall.util.*" %>
<%
	subTableHeight = 50;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
<%
idEcran = "CCP0014";
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
globaz.phenix.db.principale.CPRemarqueDecisionViewBean viewBean = (globaz.phenix.db.principale.CPRemarqueDecisionViewBean)session.getAttribute ("viewBean");
bButtonNew = objSession.hasRight("phenix.principale.remarqueDecision.afficher", globaz.framework.secure.FWSecureConstants.UPDATE);
%>
usrAction = "phenix.principale.remarqueDecision.lister";
detailLink = servlet+"?userAction=phenix.principale.remarqueDecision.afficher&_method=add";
top.document.title = "Cotisation - Remarque d'une décision"
//bFind=true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Remarques d'une décision<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
        <TR>
            <TD nowrap width="128">
            	<ct:ifhasright element="pyxis.tiers.tiers.diriger" crud="r">
            	<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>
            	</ct:ifhasright>
            </TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
	     </TD>
	<TD width="10"></TD>
            <TD nowrap width="120" align="left">Affilié</TD>
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
            <TD nowrap width="120" align="left">NSS</TD>
            <TD nowrap>
			<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
	     </TD>
          </TR>
	 <TR>
		<TD nowrap width="128">Décision</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="decision" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getDescriptionDecision()%>" readonly>
		</TD>
 	     <TD width="10"></TD>
            <TD nowrap width="120" align="left">Périodicité</TD>
            <TD nowrap>
		<INPUT type="text" name="libellePeriodicite" tabindex="-1" value="<%=viewBean.getLibellePeriodicite()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
	     </TD>
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