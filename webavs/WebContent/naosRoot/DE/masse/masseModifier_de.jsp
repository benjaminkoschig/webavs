<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.naos.application.AFApplication"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0057";
	globaz.naos.db.masse.AFMasseModifierViewBean viewBean = (globaz.naos.db.masse.AFMasseModifierViewBean)session.getAttribute ("viewBean");
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication naosApp = globaz.globall.api.GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
		gedFolderType =  naosApp.getProperty("ged.folder.type", "");
		gedServiceName =  naosApp.getProperty("ged.servicename.id", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.naos.translation.CodeSystem"%>

<SCRIPT language="JavaScript">

<%@ include file="/scripts/naos/mandatInfoRom/infoRom271.js" %>

function add() {}

function upd() {}

function validate() {
	var exit = true;
	var message = "";
	
	if (exit == false) {
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.masse.masseModifier.modifier";	
	return (exit);
}

function cancel() {
	document.forms[0].elements('userAction').value="naos.affiliation.affiliation.afficher";
}

function del() {}

function init() {}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Änderung der Lohnsummen
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD><INPUT type="hidden" name="selectedId" value="<%=viewBean.getAffiliationId()%>">
							<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
							<TBODY>
							<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100"/>
							
						
							<TR>	
								<TD width="110" height="31">Periode :</TD>	
								
								<TD>
									<INPUT type="text" name="dateDebutReadOnly"  size="10" value="<%=viewBean.getAffiliation().getDateDebut()%>" readonly="readonly" tabindex="-1" class="Disabled">
									&nbsp;bis&nbsp;
									<INPUT type="text" name="dateFinReadOnly"   size="10"  value="<%=viewBean.getAffiliation().getDateFin()%>" readonly="readonly" tabindex="-1" class="Disabled">
								</TD>
								
							</TR>
							<tr>
								<TD valign="top"  width="100">
								<%
				             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
				             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
				             		String gedIdTiers = viewBean.getAffiliation().getIdTiers();
				             		String gedIdRole = "";
				             		
				             	%>
								
								<%@ include file="/theme/gedCall.jspf" %></td>
							</tr>
							<TR> 
								<TD nowrap  height="12" colspan="2"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							<tr>
								<td><a href="#" onClick="openGedWindow()"><ct:FWLabel key="OPTION_GED"/></a></td>
							</tr>
							</TBODY>
							</TABLE>
							</TD>
						</TR> 
						<TR>
							<TD>
							<TABLE border="0" cellspacing="0" cellpadding="2">
							<TBODY>
								<TR>	
									<TD nowrap height="24">&nbsp;<b>Versicherung</b></TD>
									<TD nowrap align="left"><b>Lohnsumme</b></TD>
									<TD nowrap align="left"><b>Periodizität der eingegebene Lohnsumme</b></TD>
									<TD nowrap align="left"><b>Periodizität des Beitrags</b></TD>
									<TD nowrap align="left"><B>Gültig ab</B><TD>
								</TR> 
								<% 
									java.util.List cotisationList = viewBean.getCotisationList();
									
									int  cotiSize = cotisationList.size();
									for (int i=0 ; i < cotiSize ; i++) {
										globaz.naos.db.cotisation.AFCotisation cotisation =
										(globaz.naos.db.cotisation.AFCotisation)cotisationList.get(i);									
								%>
								<TR> 	
									<TD nowrap">&nbsp; 
										<INPUT name="assuranceLibelle<%=i%>" size="50" type="text" value="<%=cotisation.getAssurance().getAssuranceLibelle()%>" class="Disabled" tabindex="-1" readOnly>
									</TD>
									<TD nowrap> 
										<INPUT name="masseActu<%=i%>" size="20" type="text" value="<%=JANumberFormatter.format(cotisation.getMasseAnnuelle())%>" style="text-align : right;" tabindex="-1" class="Disabled" readonly>
										<INPUT name="masseActuHidden<%=i%>" type="hidden" value="<%=JANumberFormatter.format(cotisation.getMasseAnnuelle())%>">
									</TD>
									<TD nowrap rowspan="2">
										
										<INPUT type="radio" name="periodiciteNouvelleMasse<%=i%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>" checked onclick="reportPer(1,<%=i%>, 0)">
											Annuelle <br/>
										<INPUT type="radio" name="periodiciteNouvelleMasse<%=i%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>" onclick="reportPer(4,<%=i%>, 1)">
											Trimestrielle <br/>
										<INPUT type="radio" name="periodiciteNouvelleMasse<%=i%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>" onclick="reportPer(12,<%=i%>, 2)">
											Mensuelle
									</TD>
									<TD nowrap> 
										<INPUT type="text" name="periodiciteActu<%=i%>" size="13" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, cotisation.getPeriodicite())%>" class="Disabled" tabindex="-1" readOnly>
										<INPUT type="hidden" name="periodiciteActuId<%=i%>" value="<%=cotisation.getPeriodicite()%>">
									</TD>
								</TR>
								<TR>
									<TD nowrap align="right"><b>Neu:</b></TD>
									<TD nowrap> 
										<INPUT name="nouvelleMasse<%=i%>" size="20" type="text" value="" style="text-align : right;" onkeydown="focusNextMasse(event,<%=i%>)" onchange="validateFloatNumber(this);reportNumber(this,<%=i%>,'nouvelleMasse');">
										<INPUT type="hidden" id="forceNouvelleMasse<%=i%>" value="<%=String.valueOf(CodeSystem.TYPE_ASS_COTISATION_AC2.equalsIgnoreCase(cotisation.getAssurance().getTypeAssurance())|| cotisation.estCotisationSansMasse())%>" >
									</TD>
									<TD nowrap>
										 <% 
											String labelPeriodicite = "nouvellePeriodicite"+i;
										 %>
										 <ct:FWCodeSelectTag 
														name="<%=labelPeriodicite%>"
														defaut="<%=cotisation.getPeriodicite()%>"
														codeType="VEPERIODIC"
														doClientValidation=""
														/>
									</TD>

									<TD nowrap><ct:FWCalendarTag name="<%=\"dateModification\"+i%>" value=""/>
										<script>
											document.getElementsByName("dateModification<%=i%>")[0].onblur = new Function("","fieldFormat(this,'CALENDAR');reportNumber(this,<%=i%>,'dateModification')");
										</script>
									<TD>
								</TR>
								<% } // end for %>					
							</TBODY>
							</TABLE>
							<input type="hidden" value="<%=request.getParameter("forAction")%>" name="forAction">
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
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>