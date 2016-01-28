	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="GLI0003";

	globaz.libra.vb.journalisations.LIEcheancesDetailViewBean viewBean = (globaz.libra.vb.journalisations.LIEcheancesDetailViewBean) session.getAttribute("viewBean");

	bButtonUpdate = true;
	bButtonDelete = false;
	
	String idTiers = request.getParameter("idTiers");
	String idDossier = request.getParameter("idDossier");

	String[] users   = viewBean.getUsersList(idDossier);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsecheances">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getJournalisation().getIdJournalisation()%>"/>

	<%if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getRefDestination().getIdReferenceDestination())){%>
			<ct:menuActivateNode active="no" nodeId="rappel"/>
	<%} else { %>
			<ct:menuActivateNode active="yes" nodeId="rappel"/>
    <%}%>	
	
</ct:menuChange>

<script language="JavaScript">

  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly &&
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function add() {
  }

  function upd() {
  }

  function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_RC %>.chercher"; 	  	
  }
	   
  function validate() { 
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DE%>.modifier";
	    }
	  	return state;	    
  }
  
  function init(){
  }

  function postInit(){
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_ECH_DE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<!-- Si nouveau -->
		<% if (JadeStringUtil.isBlankOrZero(viewBean.getJournalisation().getIdJournalisation())){ %>

		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_ECH_DE_TIERS_DO"/></b></td>
								<td width="80%">
									<b><%= viewBean.getDetailTiersLigneNew(idTiers) %></b>
									<input type="hidden" name="idTiers" value="<%= idTiers %>">
									<input type="hidden" name="idDossier" value="<%= idDossier %>">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">			
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_ECH_DE_TYPE_ECH"/></td>
								<td width="80%">				
									<ct:select name="csType" defaultValue="" wantBlank="false">
										<ct:optionsCodesSystems csFamille="<%=ch.globaz.libra.constantes.ILIConstantesExternes.JOURNALISATIONS_TYPE %>">
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANUELLE_RECEPTION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ATTRIBUTION_DOSSIER%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE_AVEC_LIBELLE%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ETABLISSEMENT_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_TRANSFERT_DOSSIER%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE_AVEC_BLOCAGE%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANDAT%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_SOUS_MANDAT%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_SOUS_MANDAT_SMR%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_RAPPEL%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_RECEPTION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_DELAI%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_TEMP_ENVOI%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_REIMPRESSION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_RAPPEL_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_RECEPTION_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_DELAI_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_TEMP_ENVOI_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_REIMPRESSION_DECISION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ACTION%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_RAPPEL%>"/>
											<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ENVOI_SIMPLE%>"/>
										</ct:optionsCodesSystems>
									</ct:select>	
								</td>
							</tr>	
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_ECH_DE_DATE_ECH"/></td>
								<td width="80%"><ct:FWCalendarTag name="dateRappel" value="<%= viewBean.getGroupeJournal().getDateRappel() %>"/></td>
							</tr>
							<tr>
								<td valign="top"><ct:FWLabel key="ECRAN_ECH_DE_LIBELLE"/></td>
								<td>
									<textarea cols="93" rows="4" name="libelle"><%= viewBean.getJournalisation().getLibelle() %></textarea>
								</td>
							</tr>																										
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>				
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">								
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_ECH_DE_GEST"/></TD>
								<TD colspan="3">							
									<select name="idGestionnaire" id="idGestionnaire">
										<%for (int i=0; i < users.length; i = i+2){%>
											<OPTION value="<%=users[i+1]%>" <%= users[i+1].equals(viewBean.getUserDefault())?"selected":"" %>><%=users[i+1]%></OPTION>
										<% } %>
									</select>	
								</TD>
							</TR>																
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>					
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_REMARQUE"/></td>
								<td width="80%">
									<textarea cols="93" rows="8" name="remarque"><%= viewBean.getUtils().getRemarque() %></textarea>
								</td>
							</tr>																
						</table>
					</td>
				</tr>				
			</table>
		</TD>				 		
		
		
		<!-- Si détail existant -->
		<% } else { %>

		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_ECH_DE_TIERS_DO"/></b></td>
								<td width="80%"><b><%= viewBean.getDetailTiersLigne()%></b></td>
							</tr>
						</table>
					</td>
				</tr>
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">			
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_ECH_DE_TYPE_ECH"/></td>
								<td width="80%"><%= ((globaz.globall.db.BSession)viewBean.getISession()).getCodeLibelle(viewBean.getComplementJournal().getValeurCodeSysteme()) %></td>
							</tr>								
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_ECH_DE_DATE_ECH"/></td>
								<td width="80%"><ct:FWCalendarTag name="dateRappel" value="<%= viewBean.getGroupeJournal().getDateRappel() %>"/></td>
							</tr>
							<tr>
								<td valign="top"><ct:FWLabel key="ECRAN_ECH_DE_LIBELLE"/></td>
								<td>
									<textarea cols="93" rows="4" name="libelle"><%= viewBean.getJournalisation().getLibelle() %></textarea>
								</td>
							</tr>								
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>				
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">									
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_ECH_DE_GEST"/></TD>
								<TD colspan="3">							
									<select name="idGestionnaire" id="idGestionnaire">
										<%for (int i=0; i < users.length; i = i+2){%>
											<OPTION value="<%=users[i+1]%>" <%= users[i+1].equals(viewBean.getUserDefault())?"selected":"" %>><%=users[i+1]%></OPTION>
										<% } %>
									</select>	
								</TD>
							</TR>																
						</table>
					</td>
				</tr>
				<% if (viewBean.getComplementJournal().getValeurCodeSysteme().equals(globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE)){ %>
					
					<tr><td colspan="6"><br/></td></tr>	
	  				<tr>
						<td colspan="6" class="areaGlobazBlue">
						    <table width="100%">
							    <tr>
							    	<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_DETAIL"/></td>
							    	<td width="80%">
							    		<table>
									    	<th><ct:FWLabel key="ECRAN_ECH_DE_DETAIL"/></th>
									    	<th><ct:FWLabel key="ECRAN_ECH_DE_DATE_REC"/></th>
									    	<th><ct:FWLabel key="ECRAN_ECH_DE_LIBELLE"/></th>
															  				
							  				<%   		
							  					int nb = 0;		
							  					for (java.util.Iterator iterator = viewBean.getEcheanceMultipleManager().iterator(); iterator.hasNext();) {
													globaz.libra.db.journalisations.LIEcheancesMultiple echeanceMul = (globaz.libra.db.journalisations.LIEcheancesMultiple) iterator.next();
													nb++;		
											%>
													<tr>
														<td align="center">
															<input type="checkbox" name="isRecu_<%=nb%>" value="on"<%= echeanceMul.getIsRecu().booleanValue()?"checked":""%>>
														</td>
														<td align="center">
															<ct:FWCalendarTag name='<%="dateRe_"+nb%>' value="<%= echeanceMul.getDateReception() %>"/>
														</td>
														<td width="462"> 
															<input type="hidden" name="idEchM_<%=nb%>" value="<%= echeanceMul.getIdEcheanceMultiple() %>">
															
															<%=echeanceMul.getLibelle()%> 
																<% if (!JadeStringUtil.isBlankOrZero(echeanceMul.getAnnee())){ %>
																	(<%= echeanceMul.getAnnee() %>)																
																<% } %>

														</td>
													</tr>
											<%		
													
												}
							  				%>	
							  			</table>							    				
							    	</td>
		  						</tr>													
							</table>
						</td>
					</tr>					

				<% } %>
				<tr><td colspan="6"><br/></td></tr>					
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_REMARQUE"/></td>
								<td width="80%">
									<textarea cols="93" rows="8" name="remarque"><%= viewBean.getUtils().getRemarque() %></textarea>
								</td>
							</tr>																
						</table>
					</td>
				</tr>									
			</TABLE>
		</TD>
		<% } %>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>