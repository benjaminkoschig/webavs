<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF0062";
AFJournalQuittanceViewBean viewBean = new AFJournalQuittanceViewBean();
rememberSearchCriterias=true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- tpl:put name="zoneScripts" --%>
<!--<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>-->
<!--<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>-->
<%@page import="globaz.naos.db.beneficiairepc.AFJournalQuittanceViewBean"%>
<SCRIPT>
// menu 
top.document.title = "Anzeige der Quittungen"
usrAction = "naos.beneficiairepc.journalQuittance.lister";
servlet = "naos";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige des Quittungsjournals<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
<TR> 
            <TD nowrap width="80">Jahr</TD>
            <TD nowrap ><INPUT type="text" name="forAnnee"  class="numeroCourt"'></TD> 
            <TD>&nbsp;</TD>
</TR>
<TR>
            <TD nowrap width="100">Journal-Nr.</TD>
            <TD nowrap ><INPUT type="text" name="forIdJournalQuittance" class="numeroCourt"></TD>
            <TD>&nbsp;</TD>
</TR>
<TR>
            <TD width="150" height="20">Status</TD>
            <TD width="266">
             <%
				//java.util.HashSet except = new java.util.HashSet();
				//except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
				//except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				//except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
				//except.add(globaz.phenix.db.principale.CPDecision.CS_AGRICULTEUR);
				//except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
			%>
            <ct:FWCodeSelectTag name="forEtat"
					defaut="<%=viewBean.getEtat()%>"
					wantBlank="<%=true%>"
			        codeType="AFETJRNQUI"/>
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