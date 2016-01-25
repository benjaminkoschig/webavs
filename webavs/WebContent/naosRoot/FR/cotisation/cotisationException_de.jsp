<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0011";
	globaz.naos.db.cotisation.AFCotisationViewBean viewBean = (globaz.naos.db.cotisation.AFCotisationViewBean)session.getAttribute ("viewBean");
	
	//détermine le titre de la page en fonction du parametre
	String titre="Affiliation assurance - Modifier une exception ";
	if(request.getParameter("_method")!=null){
		if(request.getParameter("_method").equals("add")){
			titre="Affiliation assurance - Créer une exception";	
		}
	}
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
}

function upd() {
	
}

function validate() {
	var exit = true;
	if (document.forms[0].elements('_method').value == "add")
    	document.forms[0].elements('userAction').value="naos.cotisation.cotisation.creeException";
	else
        document.forms[0].elements('userAction').value="naos.cotisation.cotisation.modifier";
	return (exit);
}

function cancel() {
	document.forms[0].elements('_method').value="";
	document.forms[0].elements('userAction').value="back";
	
	//document.forms[0].elements('userAction').value="naos.cotisation.cotisation.afficherModifierException";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'exception sélectionnée! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.cotisation.cotisation.supprimer";
		document.forms[0].submit();}
}

function init() {
}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					<%out.println(titre); %>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD> 
								<TABLE border="0" cellspacing="0" cellpadding="0">
									<TBODY>
									<naos:AFInfoAffiliation name="selectedId" affiliation="<%=viewBean.getAffiliation()%>" colspan="5"/>
									
									<TR> 
										<TD nowrap  height="11" colspan="6"> 
											<hr size="3" width="100%">
											<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
											<INPUT type="hidden" name="planAffiliationId" value='<%=viewBean.getPlanAffiliationId()%>'>
											<INPUT type="hidden" name="affiliationId" value='<%=viewBean.getPlanAffiliation().getAffiliationId()%>'>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31">Plan d'affiliation</TD>
										<TD nowrap width="270">
											<INPUT type="text" name="planAffiliationReadOnly" size="35" value="<%=viewBean.getPlanAffiliation().getLibelle()%>" class="Disabled" tabindex="-1" readOnly>
										</TD>
										<TD width="32">&nbsp;</TD>
										<TD nowrap width="125">Caisse</TD>
										<TD width="31"></TD>
										<TD nowrap width="308"> 
											<% 
												String stCaisse = "";
												if (viewBean.getCaisse().getAdministration() != null) {
													stCaisse = viewBean.getCaisse().getAdministration().getNom();
												}									
											%>
											<INPUT type="text" name="caisse" size="35" value="<%=stCaisse%>" class="Disabled" tabindex="-1" readOnly>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31" >Assurance</TD>
										<TD nowrap width="270"> 
											<%
												String libelleAssurance = "";
												if (viewBean != null &&  viewBean.getAssurance() != null) {
													libelleAssurance = viewBean.getAssurance().getAssuranceLibelleCourt();
												}
											%>
											<INPUT type="text" name="libelleAssuranceReadOnly" size="35" value="<%=libelleAssurance%>"class="libelleLongDisabled" tabindex="-1" readOnly>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31">P&eacute;riode</TD>
										<TD nowrap width="270"> 
											<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" /> 
											&agrave; 
											<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" /> 
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Motif de fin</TD>
										<TD width="31"></TD>
										<TD nowrap width="308">
											<INPUT type="hidden" name="motifFin" value="<%=globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION%>">
											<INPUT type="text" name="motifFinLibelle" size="10" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION)%>" class="Disabled" tabindex="-1" readOnly>
										</TD>
									</TR>
									<TR> 
									<TR> 
										<TD nowrap height="31" width="175">Cotisation Paritaire</TD>
										<TD colspan="5"></TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31" >Nouvelle Masse ou cotisation ?</TD>
						
										<TD nowrap width="270"> 
											<INPUT type="text" name="nouvelleMasse" size="19" value="<%=viewBean.getMassePeriodicite()%>" style="text-align : right;">
										</TD>
										<TD width="32"></TD>										
										<TD nowrap width="125">P&eacute;riodicit&eacute; de Facturation</TD>
										<TD width="31"></TD>
										<TD nowrap height="31" width="307"> 
											<INPUT type="text" name="periodiciteReadOnly" size="36" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getAffiliation().getPeriodicite())%>" class="Disabled" tabindex="-1" readOnly>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31" ></TD>	
																			
										<TD height="31"> 
											<%
												String tmpPeriodicite = "";
												if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getPeriodicite())) {
													tmpPeriodicite = viewBean.getAffiliation().getPeriodicite();
												} else {
													tmpPeriodicite = viewBean.getPeriodicite();
												}
											%>
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>" 
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE))? "checked" : ""%>>
												Annuelle
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>"
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE))? "checked" : ""%>>
												Trimestrielle 
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>" 
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE))? "checked" : ""%>>
												Mensuelle 
										</TD>
									</TR>
									</TBODY> 
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<% } %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>