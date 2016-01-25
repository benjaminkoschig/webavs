<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0049";
	globaz.naos.db.fact.AFFactViewBean viewBean = (globaz.naos.db.fact.AFFactViewBean)session.getAttribute ("viewBean");	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="naos.fact.fact.ajouter";
}

function upd() {
}

function validate() {
	var exit = true;
	var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgef�llt !";
	if (document.forms[0].elements('dateDebut').value == "")
	{
		message = message + "\nSie haben das Beginndatum nicht eingegeben !";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.fact.fact.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.fact.fact.ajouter";
	else
        document.forms[0].elements('userAction').value="naos.fact.fact.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
 		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="naos.fact.fact.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, das Detail der ausgew�hlten Fakturierung zu l�schen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="naos.fact.fact.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 
					Facturation - D&eacute;tail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR>
							<TD colspan="3">&nbsp;</TD>
						</TR>
						<TR>
							<TD nowrap height="31" width="150">Mitglied</TD>
							<TD nowrap width="30"></TD>
							<TD nowrap colspan="1" width="400"> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getFacturationId()%>'>
								<INPUT type="hidden" name="affiliationId" value='<%=viewBean.getAffiliationId()%>'>
								<INPUT type="hidden" name="cotisationId" value='<%=viewBean.getCotisationId()%>'>
								<INPUT type="hidden" name="facturerControle" value='<%=viewBean.isFacturerControle()%>'>
								<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly><BR>
								<INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" tabindex="-1" readOnly><BR>
								<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=viewBean.getTiers().getLocaliteLong()%>" tabindex="-1" readOnly>
							</TD>
							<TD nowrap width="100"></TD>
						</TR>
						<TR> 
							<TD colspan="3">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap height="11" colspan="7"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31"  width="111"><font size="2"><b>Beitrag</b></font></TD>
							<TD nowrap width="30"></TD>
							<!--TD width="274"><INPUT type="text" name="libelle" size="40" maxlength="40" value="<= viewBean.getCotisation().getAssurance().getAssuranceLibelleCourt()>" class="libelleCourtDisabled" tabindex="-1"readOnly></TD-->
							<TD nowrap width="181"></TD>
						</TR>
						<TR> 
							<TD nowrap height="31"  width="111">Periode</TD>
							<TD nowrap width="30" align="right">von</TD>
							<TD height="14" width="274">
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" /> 
								bis 
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Neue Lohnsumme</TD>
							<TD nowrap width="30"></TD>
							<TD width="274"> 
								<INPUT name="massePeriodiciteNouveau" size="20" type="text" value="<%=viewBean.getMassePeriodiciteNouveau()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Betrag</TD>
							<TD nowrap width="30"></TD>
							<TD width="274"> 
								<INPUT name="montant" size="20" type="text" value="<%=viewBean.getMontant()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Fakturierungstyp</TD>
							<TD nowrap width="30"></TD>
							<TD nowrap width="274">
								<ct:FWCodeSelectTag 
									name="typeFacturation" 
									defaut="<%=viewBean.getTypeFacturation()%>"
									codeType="MUTYPEFACT"
									wantBlank="true"/>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Stornieren</TD>
							<TD nowrap width="30"></TD>
							<TD width="274"> 
								<INPUT type="checkbox" name="extourner" <%=(viewBean.isExtourner().booleanValue())? "checked" : ""%>>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Job</TD>
							<TD nowrap width="30"></TD>
							<TD width="274"> 
								<INPUT name="passageId" size="20" type="text" value="<%=viewBean.getPassageId()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Fakturieren</TD>
							<TD nowrap width="30"></TD>
							<TD width="274"> 
								<INPUT type="checkbox" name="facturer" <%=(viewBean.isFacturer().booleanValue())? "checked" : ""%> disabled tabindex="-1" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Alter Beitragsatz</TD>
							<TD nowrap width="30"></TD>
							<TD> 
								<INPUT name="tauxAncien" size="20" type="text" value="<%=viewBean.getTauxAncien()%>" style="text-align : right;">
								/ 
								<INPUT name="fractionAncien" size="20" type="text" value="<%=viewBean.getFractionAncien()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Neue Beitragsatz</TD>
							<TD nowrap width="30"></TD>
							<TD> 
								<INPUT name="tauxNouveau" size="20" type="text" value="<%=viewBean.getTauxNouveau()%>" style="text-align : right;">
								/ 
								<INPUT name="fractionNouveau" size="20" type="text" value="<%=viewBean.getFractionNouveau()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="111" height="31" >Erstellt am</TD>
							<TD nowrap width="30"></TD>
							<TD> 
								<INPUT name="dateCreation" size="20" type="text" value="<%=viewBean.getDateCreation()%>" style="text-align : right;" class="dateDisabled" tabindex="-1" readOnly>
								am 
								<INPUT name="heureCreation" size="20" type="text" value="<%=viewBean.getHeureCreation()%>" style="text-align : right;" class="dateDisabled"  tabindex="-1" readOnly>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>