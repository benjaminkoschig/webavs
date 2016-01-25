<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="CCP0009";
globaz.phenix.db.principale.CPCotisationViewBean viewBean = (globaz.phenix.db.principale.CPCotisationViewBean)session.getAttribute ("viewBean");
key="globaz.phenix.db.principale.CPCotisationViewBean-idCotisation"+viewBean.getIdCotisation();
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
top.document.title = "Beitrag - Beiträge Detail"
function add() {
	document.forms[0].elements('userAction').value="phenix.principale.cotisation.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.cotisation.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.cotisation.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.cotisation.chercher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.principale.cotisation.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Beiträge<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap width="140">Versicherter</TD>
            <TD nowrap >
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
	     </TD>
	<TD width="10"></TD>
            <TD nowrap width="50" align="left">Mitglied-Nr.</TD>
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
            <TD nowrap align="left">SVN</TD>
            <TD nowrap>
			<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     </TD>
		<TD>
	     </TD>
          </TR>
	 <TR>
            <TD nowrap width="140">Verfügung</TD>
            <TD nowrap >
		<INPUT type="text" name="decision" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getDescriptionDecision()%>" readonly>
 	     </TD>
            <TD></TD>
            <TD nowrap align="left">Periodizität</TD>
            <TD nowrap><INPUT type="text" name="libellePeriodicite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLibellePeriodicite()%>" readonly>
            <input type="hidden" name="periodicite" value="<%=viewBean.getPeriodicite()%>"/></TD>
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
            <TD nowrap>Beitragsperiode</TD>
            <TD>
			<ct:FWCalendarTag name="debutCotisation" 
				value="<%=viewBean.getDebutCotisation()%>" 
				errorMessage="Das Beginndatum ist falsch."
				doClientValidation="CALENDAR,NOT_EMPTY"
		  	/>&nbsp;au&nbsp;
	   		<ct:FWCalendarTag name="finCotisation" 
				value="<%=viewBean.getFinCotisation()%>"
				errorMessage="Das Enddatum ist falsch."
				doClientValidation="CALENDAR,NOT_EMPTY"
			 /></TD>
			 <TD nowrap></TD>
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
            <TD nowrap width="140">Versicherung</TD>
            <TD nowrap>
            
		<ct:FWListSelectTag name="idCotiAffiliation"
            		defaut="<%=viewBean.getIdCotiAffiliation()%>"
            		data="<%=globaz.naos.db.cotisation.AFCotisation.getListAssuranceAffiliation(session, viewBean.getIdAffiliation())%>"/>
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
            <TD nowrap width="140">Monatliche Beiträge</TD>
            <TD nowrap><INPUT type="text" name="montantMensuel" class="montant" value="<%=viewBean.getMontantMensuel()%>"></TD>
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
            <TD nowrap width="140">Vierteljährliche Beiträge</TD>
            <TD nowrap><INPUT type="text" name="montantTrimestriel" class="montant" value="<%=viewBean.getMontantTrimestriel()%>"></TD>
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
            <TD nowrap width="140">Halbjährliche Beiträge</TD>
            <TD nowrap><INPUT type="text" name="montantSemestriel" class="montant" value="<%=viewBean.getMontantSemestriel()%>"></TD>
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
            <TD nowrap width="140">Jährliche Beiträge</TD>
            <TD nowrap><INPUT type="text" name="montantAnnuel" class="montant" value="<%=viewBean.getMontantAnnuel()%>"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap><INPUT type="hidden" name="idDecision" value="<%=viewBean.getIdDecision()%>"></TD>
          </TR>
          <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
	  <TR>
         <TD nowrap width="140">Bereits fakturiert</TD>
            <TD nowrap><INPUT type="text" name="cotisationFacture" class="montantDisabled" value="<%=viewBean.getMontantFacture()%>" readonly></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="140">Beitragssatz</TD>
            <TD nowrap><INPUT type="text" name="taux" style="text-transform : capitalize;" class="montant" value="<%=viewBean.getTaux()%>"></TD>
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

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>