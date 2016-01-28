<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="CCP0011";
globaz.phenix.db.principale.CPDonneesCalculViewBean viewBean = (globaz.phenix.db.principale.CPDonneesCalculViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Donnes du calcul - Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.principale.donneesCalcul.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.donneesCalcul.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.donneesCalcul.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.donneesCalcul.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.principale.donneesCalcul.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Données du calcul<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap width="140">Tiers</TD>
            <TD nowrap >
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
	     </TD>
	<TD width="10"></TD>
            <TD nowrap width="50" align="left">Affilié</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
            <TD nowrap width="140"></TD>
            <TD nowrap >
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
		</TD>
            <TD></TD>
            <TD nowrap align="left">NSS</TD>
            <TD nowrap>
			<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
	     </TD>
          </TR>
	 <TR>
            <TD nowrap width="140">Décision</TD>
            <TD nowrap >
		<INPUT type="text" name="decision" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getDescriptionDecision()%>" readonly>
 	     </TD>
            <TD></TD>
            <TD nowrap align="left">Périodicité</TD>
            <TD nowrap>
		<INPUT type="text" name="libellePeriodicite" tabindex="-1" value="<%=viewBean.getLibellePeriodicite()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
	     </TD>
          </TR>
           <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap><INPUT type="hidden" name="idDecision" value='<%=viewBean.getIdDecision()%>'></TD>
            <TD nowrap></TD>
          </TR>
        <TR>
            <TD nowrap width="140">Détail</TD>
            <TD nowrap><%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
			<ct:FWCodeSelectTag name="idDonneesCalcul"
			defaut="<%=viewBean.getIdDonneesCalcul()%>"
			codeType="CPDOCALCUL" />
			<%} else {%>
	 		<input  type="text" readonly class="libelleLongDisabled" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session, viewBean.getIdDonneesCalcul())%>" >
	 		<%}%>
            </TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
	 <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
  	 <TR>
            <TD nowrap width="140">Valeur</TD>
            <TD nowrap><INPUT type="text" name="montant" class="montant" value="<%=viewBean.getMontant()%>"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>