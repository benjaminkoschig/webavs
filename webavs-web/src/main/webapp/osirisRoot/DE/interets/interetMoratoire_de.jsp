<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.musca.util.FAUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0034"; %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.framework.util.*" %>
<%@ page import="globaz.osiris.application.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.globall.util.JAUtil" %>
<%@ page import="globaz.musca.db.facturation.FAEnteteFacture" %>
<%
	CAInteretMoratoireViewBean viewBean = (CAInteretMoratoireViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.interets.interetMoratoire.modifier";
	selectedIdValue = viewBean.getIdInteretMoratoire();

	String secondServletPath = viewBean.isDomaineCA()?"/osiris":"/musca";

	if(viewBean.getStatus().equals(CAInteretMoratoireViewBean.STATUS_COMPTABILISE) || viewBean.getStatus().equals(CAInteretMoratoireViewBean.STATUS_BLOQUE)|| CAInteretMoratoireViewBean.STATUS_COMPTABILISE.equalsIgnoreCase(FAUtil.getPassageStatus(viewBean.getIdJournalFacturation(),session)))  {
		bButtonUpdate = false;
		bButtonDelete = false;
	}
	

%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String tmpDomaine = "";
	if (viewBean.isDomaineCA()) {
		tmpDomaine = "CA";
	} else {
		tmpDomaine = "FA";
	}
%>

<ct:menuChange displayId="options" menuId="CA-CalculInteretsMoratoires" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdInteretMoratoire()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdInteretMoratoire()%>"/>
	<ct:menuSetAllParams key="idInteretMoratoire" value="<%=viewBean.getIdInteretMoratoire()%>"/>
	<ct:menuSetAllParams key="domaine" value="<%=tmpDomaine%>"/>

	<%
		if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalFacturation())) {
	%>
		<ct:menuActivateNode active="no" nodeId="imprimerDecisionInteret"/>
	<%
		} else {
	%>
		<ct:menuActivateNode active="yes" nodeId="imprimerDecisionInteret"/>
	<%
		}
	%>
</ct:menuChange>


<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

$(function () {
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getWarningVerifierCompensation())){%>
		globazNotation.utils.consoleWarn("<%=viewBean.getWarningVerifierCompensation()%>",'Warnung',true);
	<%}%>
});

function add() {
}

function upd() {
	document.forms[0].elements('userAction').value="osiris.interets.interetMoratoire.modifier";
	document.forms[0].idExterneRoleEcran.focus();

	//désactivation des champs qui ont été activé par la fonction action(type) (appelé lors du clic sur le bouton "Modifier")
	document.forms[0].elements['idExterneRoleEcran'].disabled = true;
	document.forms[0].elements['SelectionRole'].disabled = true;

	if (document.forms[0].elements['CompteAnnexeSelector'] != null) {
		document.forms[0].elements['CompteAnnexeSelector'].disabled = true;
	}

	document.forms[0].elements['idExterneSectionEcran'].disabled = true;

	if (document.forms[0].elements['idTypeSectionEcran'] != null) {
		document.forms[0].elements['idTypeSectionEcran'].disabled = true;
	}

	document.forms[0].elements['idGenreInteret'].disabled = true;
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Buchung zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.interets.interetMoratoire.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
    		document.forms[0].elements('userAction').value="osiris.interets.interetMoratoire.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.interets.interetMoratoire.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
	{
		document.forms[0].elements('userAction').value="osiris.interets.interetMoratoire.afficher";
	}
}
function init(){}

// Les fontionnalites !!!

function jsRechercheCompteAnnexeEcran(){
	document.forms[0].rechercheCompteAnnexeEcran.value="true";
	getSl('osiris.interets.interetMoratoire.afficher','osiris.comptes.rechercheCompteAnnexe.chercher', 'idCompteAnnexe','');
}
function jsInitRechercheCompteAnnexeEcran(){
	document.forms[0].rechercheCompteAnnexeEcran.value="false";
}

function jsRechercheSectionEcran(){
	document.forms[0].rechercheSectionEcran.value="true";
	getSl('osiris.interets.interetMoratoire.afficher','osiris.comptes.rechercheSection.chercher', 'idSection','');
}
function jsInitRechercheSectionEcran(){
	document.forms[0].rechercheSectionEcran.value="false";
}
function jsRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="true";
	getSl('osiris.interets.interetMoratoire.afficher','osiris.comptes.rechercheRubrique.chercher', 'idCompte','');
}
function jsInitRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="false";
}

function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idRubrique.value = elementSelected.idCompte;
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterne;
		document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
	}
}

function updateLibelle(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("descriptionRubrique").value = element.libelle;
	}
}
function rubriqueManuelleOn(){
	document.forms[0].idRubrique.value="";
	//document.forms[0].idExterne.value="";
	document.forms[0].rubriqueDescription.value="";
}

top.document.title = "Comptes - detail d'une écriture - " + top.location.href;

function jsValeurSelectionRole(){
	vValeurCompte = document.forms[0].forSelectionRole.value;
}
// stop hiding -->
</SCRIPT>

<style type="text/css">
tr.row {
	height:32px;
	background-color:transparent;
}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Erfassung der Verzugszinsenverfügungen<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
            				<tr class="row">
						    	<td width="20%">
						    		<%= (viewBean.isDomaineCA()?"<a href="+request.getContextPath()+"/osiris?userAction=osiris.comptes.apercuComptes.afficher&id="+viewBean.getIdCompteAnnexe()+">Konto</a>":"Debitor") %>

						    	</td>
						    	<td width="40%">
						    		<input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>">
						    		<input type="hidden" name="nouvelleDecision" value = "<%=viewBean.isNouvelleDecision()%>">
						    		<input type="text" name="idExterneRoleEcran" value="<%=viewBean.getIdExterneRoleEcran()%>" class="disabled" readonly tabindex="-1" >
						    		<%
					              	CACompteAnnexe tempCompteAnnexe = new CACompteAnnexe();
					              	FAEnteteFacture enteteFacture = viewBean.getEnteteFacture();

					              	if (viewBean.isDomaineCA())
					              	{
										tempCompteAnnexe.setSession(objSession);
										tempCompteAnnexe.retrieve();
									//}
									//else
									//{
									//	enteteFacture.setSession(objSession);
									//	enteteFacture.retrieve();
									}%>

									<select name="SelectionRole" onChange="jsValeurSelectionRole()" class="disabled"  tabindex="-1" style="width:116px">
									<% String selectionRole = request.getParameter("forSelectionRole");
										if (selectionRole == null && viewBean.getIdRoleEcran() != null)
											selectionRole = viewBean.getIdRoleEcran();
										else if (selectionRole == null)
											selectionRole = "1000";%>
					                <option selected value="1000">Alle</option>
					                <%CARole tempRole;
										 		CARoleManager manRole = new CARoleManager();
												manRole.setSession(objSession);
												manRole.find();
												for(int i = 0; i < manRole.size(); i++){
													tempRole = (CARole)manRole.getEntity(i);
													if(selectionRole.equalsIgnoreCase(tempRole.getIdRole())){ %>
					    					           	 <option selected value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
					      					        <%}else{%>
					               						<option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
					                				<%}%>
					               				<%}%>
					              	</select>

									<%
									String redirectUrl = "/osirisRoot/FR/interets/interetMoratoire_de.jsp";

									if (viewBean.isDomaineCA())
									{
										Object[] compteAnnexeMethodsName = new Object[]{
										new String[]{"setIdCompteAnnexe","getIdCompteAnnexe"},
										new String[]{"setIdExterneRoleEcran","getIdExterneRole"}
										};
									%>
										<ct:FWSelectorTag
											name="CompteAnnexeSelector"

											methods="<%=compteAnnexeMethodsName%>"
											providerApplication ="osiris"
											providerPrefix="CA"
											providerAction ="osiris.comptes.apercuComptes.chercher"
											redirectUrl="<%=redirectUrl%>" target="fr_main"
										/>
								<% } %>
								</td>
								<td width="40%">
									<input type="text" name="Description" value="<%= viewBean.getNomTiers()%>" class="Disabled" readonly tabindex="-1" style="width:330px">
								</td>
						  	</tr>
						  	<tr class="row">
						  	  	<td>
						  	  		<a href="<%=request.getContextPath()+(viewBean.isDomaineCA()?"/osiris?userAction=osiris.comptes.apercuParSection.afficher&id="+viewBean.getIdSection():"/musca?userAction=musca.facturation.enteteFacture.afficher&selectedId="+viewBean.getIdSectionFacture())%>" > <%=viewBean.isDomaineCA()?"Sektion":"Abrechnung"%></a>
						  	  	</td>
						    	<td>
						    		<input type="text" name="idExterneSectionEcran" value="<%=viewBean.getIdExterneSectionEcran()%>" class="disabled" readonly tabindex="-1" style="width:90px">
						    		<% if (viewBean.isDomaineCA()) { %>
							    		<select name="idTypeSectionEcran" id="idTypeSectionEcran" onChange="jsInitRechercheSectionEcran()" >
						                <%CATypeSection tempTypeSection;
										  CATypeSectionManager manTypeSection = new CATypeSectionManager();
										  manTypeSection.setSession(objSession);
										  manTypeSection.find();
										  for(int i = 0; i < manTypeSection.size(); i++){
										    	tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);
												if(viewBean.getSection() == null) { %>
						                			<option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
						                		<%} else if  (viewBean.getSection().getIdTypeSection().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
						                			<option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
						                		<% } else { %>
						                			<option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
						                		<% } %>
						                <% } %>
						              	</select>
						 				<script language="Javascript">
						           			document.getElementById("idTypeSectionEcran").style.width=235;
						           		</script>
						           	<% } else { %>
							            <input type="text" name="idTypeEnteteFacture" value="<%=viewBean.getEnteteFactureTypeDescription()%>" class="disabled" readonly tabindex="-1" style="width:235px" size="23">
									<% } %>
						    	</td>
						    	<td>
						    		<input type="text" name="LibelleDescription" value="<%=viewBean.isDomaineCA()?viewBean.getSection().getDescription():viewBean.getEnteteFactureDescriptionDecompte()%>" class="Disabled" readonly tabindex="-1" style="width:330px">
						    	</td>
						  	</tr>
						  	<tr>
						    	<td colspan="3"><hr></td>
						  	</tr>
						  	<tr class="row">
						    	<td>Zinsenart</td>
						    	<td colspan="2">
						    		<ct:FWSystemCodeSelectTag
					           			name="idGenreInteret"
					           			defaut="<%=viewBean.getIdGenreInteret() %>"
					           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"
					           		/>
					           		<script language="Javascript">
					           		document.getElementById("idGenreInteret").style.width=311;
					           		</script>
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>BSV Rubrik</td>
						    	<td>
						    		<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>">

						    		<% if (viewBean.isNouvelleDecision()) {
						    			String jspLocation = servletContext + "/muscaRoot/" + languePage + "/facturation/rubrique_select.jsp";%>
										<ct:FWPopupList name="idExterne"
											onFailure="document.mainForm.idRubrique.value='';"
											onChange="if (tag.select) document.mainForm.idRubrique.value = tag.select[tag.select.selectedIndex].idRubrique;updateLibelle(tag);"
											validateOnChange="true"
											value="<%=viewBean.getIdExterneRubriqueEcran()%>"
											className="libelle" jspName="<%=jspLocation%>"
											minNbrDigit="1"
											forceSelection="true"/>&nbsp;&nbsp;
										</td><td>
										<INPUT name="descriptionRubrique" type="text" value="<%=viewBean.getLibelleRubrique()%>" class="disabled" readonly style="width:330px" tabindex="-1">
						    			</td>



						    		<% } else {
										String jspLocation = servletContext + mainServletPath + "Root/rubriqueInteretsMoratoires_select.jsp";
									%>
							    		<ct:FWPopupList name="idExterneRubriqueEcran" onFailure="rubriqueManuelleOn();"
											onChange="updateRubrique(tag.select);"
											value="<%=viewBean.getIdExterneRubriqueEcran()%>"
											className="libelle"
											jspName="<%=jspLocation%>"
											minNbrDigit="1"
											forceSelection="true"
											validateOnChange="false"
											 />
										<script language="Javascript">
											document.getElementById("idExterneRubriqueEcran").style.width=333;
										</script>
						    		<td>
							    		<input type="text" name="rubriqueDescription" value="<%=(viewBean.getRubrique()!=null)?viewBean.getRubrique().getDescription():""%>" class="disabled" readonly tabindex="-1" style="width:330px">
							    	</td>
								<% } %>

						  	</tr>
						  	<tr class="row">
						  	<% if (viewBean.isNouvelleDecision()) { %>
						  		<td>Berechnungsplan</td>
						  		<td>
						  			<SELECT name="idPlan" id="idPlan" onChange="jsInitRecherchePlanId()" >
							  			<%CAPlanCalculInteretManager manager = new CAPlanCalculInteretManager();
							  			CAPlanCalculInteret planCalcul;
							  			manager.setSession(objSession);
										manager.find();
										for(int i = 0; i < manager.size(); i++){
											planCalcul = (CAPlanCalculInteret)manager.getEntity(i);%>
						               	 	<option selected value="<%=planCalcul.getIdPlanCalculInteret()%>"><%=planCalcul.getLibelleDE()%></option>
						                <%}%>
						  			</SELECT>
						  		</td>
						  	<% } %>
						  	</tr>
						  	<tr class="row">
						    	<td>Berechnungsdatum</td>
						    	<td colspan="2">
						    		<ct:FWCalendarTag name="dateCalcul" value="<%=viewBean.getDateCalcul()%>" />
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Ausdruckdatum des Briefs</td>
						    	<td colspan="2">
						    		<ct:FWCalendarTag name="dateLettre" value="<%=viewBean.getDateLettre()%>" />
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Ausdruckdatum der Verfügung</td>
						    	<td colspan="2">
						    		<ct:FWCalendarTag name="dateDecision" value="<%=viewBean.getDateDecision()%>" />
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Wiederspruchsdatum</td>
						    	<td colspan="2">
						    		<ct:FWCalendarTag name="dateRecours" value="<%=viewBean.getDateRecours()%>" />
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Verfügungsgrund</td>
						    	<td colspan="2">
			<%
			   	java.util.HashSet except = new java.util.HashSet();
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE);

			   	if (!viewBean.isManuel()) {
			   		except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			   	}

			   	if (!viewBean.isAControler()) {
			   		except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_A_CONTROLER);
			   	}
			%>

						    	<ct:FWSystemCodeSelectTag
				           			name="motifcalcul"
				           			defaut="<%=viewBean.getMotifcalcul()%>"
				           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsMotifDecisionInteret(objSession)%>"
									except="<%=except%>"
				           		/>
						    	<script language="Javascript">
									document.getElementById("motifCalcul").style.width=330;
								</script>
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Bemerkungen</td>
						    	<td colspan="2">
						    		<TEXTAREA rows="4" width="360px" align="left" class="libelleLong" name="remarque"><%=viewBean.getRemarque()%></TEXTAREA>
						    		</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Rechnungsdatum</td>
						    	<td colspan="2">
						    		<input type="text" name="dateFacturation" value="<%=viewBean.getDateFacturation() %>" class="disabled" readonly tabindex="-1" style="width:330px">
						    		</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Zinsenbetrag</td>
						    	<td colspan="2"><input type="text" name="totalInteret" value="<%=viewBean.isNouvelleDecision()?"":viewBean.calculeTotalInteret().toString()%>" class="disabled" readonly tabindex="-1" style="width:330px"></td>
						  	</tr>
						  	<tr class="row">
						    	<td>Zu fakturieren auf der Sektion</td>
						    	<td colspan="2"><input type="text" name="numeroFactureGroupe" value="<%=!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getNumeroFactureGroupe())?viewBean.getNumeroFactureGroupe():""%>" class="disabled" readonly tabindex="-1" style="width:330px"></td>
						  	</tr>
						  	<tr class="row">
						    	<td>
						    		<% if (viewBean.isDomaineCA()) { %>
						    			<a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&id=<%=viewBean.getIdJournalCalcul()%>">Journal</a>
						    		<%} else { %>
						    			<a href="<%=request.getContextPath()%>/musca?userAction=musca.facturation.passage.afficher&selectedId=<%=viewBean.getIdJournalCalcul()%>">Journal</a>
						    		<% } %>
						    	</td>
						    	<td colspan="2">
						    		<input type="hidden" name="idJournalCalcul" value="<%=viewBean.getIdJournalCalcul()%>">
						    		<input type="hidden" name="idJournal" value="<%=viewBean.getIdJournalCalcul()%>">
              						<input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournalCalcul()%> - <%=viewBean.getJournalLibelle()%>" class="disabled" readonly tabindex="-1" style="width:330px">
						    	</td>
						  	</tr>
						  	<tr class="row">
						    	<td>Identifizierung</td>
						    	<td colspan="2"><input type="text" name="idInteretMoratoire" value="<%=viewBean.getIdInteretMoratoire()%>" class="disabled" readonly tabindex="-1" style="width:330px"></td>
						  	</tr>
         				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		//mettreTauxBase();
		</SCRIPT> <%	}
%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>