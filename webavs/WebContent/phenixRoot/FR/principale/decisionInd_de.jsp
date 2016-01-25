<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@page import="globaz.phenix.db.principale.CPDonneesBase"%>
<%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.phenix.application.CPApplication"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><SCRIPT language="JavaScript">
<%
	idEcran="CCP0003";
	globaz.phenix.db.principale.CPDecisionViewBean viewBean = (globaz.phenix.db.principale.CPDecisionViewBean)session.getAttribute ("viewBean");
	subTableWidth = "0";
	if (globaz.phenix.translation.CodeSystem.getLibelle(session, globaz.phenix.db.principale.CPDecision.CS_VALIDATION).equalsIgnoreCase(viewBean.getEtatDecision())){
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonNew = false;
	}
	key="globaz.phenix.db.principale.CPDecisionViewBean-idDecision"+viewBean.getIdDecision();
	
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication phoeApp = globaz.globall.api.GlobazSystem.getApplication(CPApplication.DEFAULT_APPLICATION_PHENIX);
		gedFolderType = phoeApp.getProperty("ged.folder.type", "");
		gedServiceName = phoeApp.getProperty("ged.servicename.id", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
%>
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
top.document.title = "Cotisation - Décision indépendant"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="phenix.principale.decision.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields();

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.decision.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.decision.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.decision.chercher";
	document.forms[0].elements('selectedId2').value="<%=viewBean.loadAffiliation().getAffiliationId()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.loadTiers().getIdTiers()%>";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.principale.decision.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/

function showBlock(blockToShow) {
	document.all(blockToShow).style.display='block';
}
function hideBlock(blockToHide) {
	document.all(blockToHide).style.display='block';
}

function showInline(inputToShow) {
	document.all(inputToShow).style.display = 'inline';
}

function hide(inputToHide) {
	document.all(inputToHide).style.display = 'none';
}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>
<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdDecision()%>" tableSource="globaz.phenix.db.principale.CPDecisionViewBean"/></span>Décision - Détail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!-- Entete -->
         		<tr><td>
         		<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
          			<TR>
            			<TD nowrap width="200" height="10">Tiers</TD>
            			<TD nowrap width="150">
              				<INPUT type="text" name="numAffilie" value="<%=viewBean.loadAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" tabindex="-1" readonly>
	     				</TD>
            			<TD nowrap width="25"></TD>
            			<TD nowrap colspan="2">
            				<INPUT type="text" name="nom" value="<%=viewBean.loadTiers().getNom()%>" class="libelleLongDisabled" tabindex="-1" readonly>
              				<input name="etatDecision" type="text" value="<%=viewBean.getEtatDecision()%>" class="numeroDisabled"   tabindex="-1"  readonly>
            			</TD>
          			</TR>
	  				<TR>
	   					<TD nowrap  height="11" colspan="5">
              				<hr size="3" width="100%">
            			</TD>
          			</TR>
          			<TR>
            			<TD nowrap width="157">Décision pour</TD>
            			<TD nowrap width="244">
              				<INPUT name="libelleGenreAffilie" type="text" value="<%=viewBean.getLibelleGenreAffilie()%>" class="libelleLongDisabled" tabindex="-1" readonly>
            			</TD>
            			<TD nowrap width="25"></TD>
            			<TD nowrap width="110">Responsable</TD>
            			<TD nowrap width="171">
            				<ct:FWListSelectTag name="responsable"
            				defaut="<%=viewBean.getResponsable()%>"
            				data="<%=viewBean.getUserList(session)%>"/>
            			</TD>
          			</TR>
          			<TR>
            			<TD nowrap width="157">Type de décision</TD>
            			<TD nowrap><INPUT name="libelleTypeDecision" type="text" value="<%=viewBean.getLibelleTypeDecision()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
			            <TD nowrap width="25"></TD>
			            <TD nowrap width="110">N° IFD associé</TD>
			            <TD nowrap width="171"><INPUT name="numIfdDefinitif" type="text" value="<%=viewBean.getNumIfdDefinitif()%>" class="numeroCourtDisabled" tabindex="-1" readonly></TD>
          			</TR>
	    			<TR>
            			<TD nowrap width="157">Conjoint</TD>
            			<TD nowrap colspan="4"><INPUT type="text" name="selectionCjt" value="<%=viewBean.getSelectionCjt()%>" class="numeroId" style="color : purple;">
				 		  	<%
							Object[] tiersMethodsName= new Object[]{
								new String[]{"setSelectionCjt","getNumAffilieActuel"},
								new String[]{"setIdConjoint","getIdTiers"},
								new String[]{"setConjoint","getNom"},
							};
							Object[] tiersParams = new Object[]{
								new String[]{"selection","_pos"},
							};

							String redirectUrl1 = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionInd_de.jsp";
							%>
							<ct:ifhasright element="pyxis.tiers.tiers.chercher" crud="r">
							<ct:FWSelectorTag
								name="tiersSelector"

								methods="<%=tiersMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.tiers.chercher"
								providerActionParams ="<%=tiersParams%>"
								redirectUrl="<%=redirectUrl1%>"
							/>
							</ct:ifhasright>
							<INPUT name="idConjoint" type="hidden" value="<%=viewBean.getIdConjoint()%>"><INPUT name="conjoint" type="text" value="<%=viewBean.getConjoint()%>" class="libelleLongDisabled" tabindex="-1"  readonly  style="color : purple;">
							<input type="checkbox" name="division2" <%=(viewBean.getDivision2().booleanValue())? "checked" : "unchecked"%>> (simule conjoint)</TD>
          			</TR>
	   				<TR>
			            <TD nowrap width="157">Année de décision</TD>
			            <TD nowrap><INPUT name="anneeDecision" type="text" size="4" value="<%=viewBean.getAnneeDecision()%>" maxlength="4" class="numeroCourtDisabled" tabindex="-1" readonly></TD>
			            <TD nowrap width="25"></TD>
	                <%
		      	String idEntete = viewBean.rechercheIdEnteteFacture();
			    String linkFacture ="";
		      	if(!JadeStringUtil.isBlankOrZero(idEntete)){
		      		linkFacture = "musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=" + idEntete + "&idPassage=" + viewBean.getIdPassage();
		      		
		      	%>
		      	<TD width="165">
		      	<ct:ifhasright element="musca.facturation.afact.chercher" crud="r">
		   		<A href="<%=request.getContextPath() + "/" + linkFacture%> class="external_link">N° de passage</A>
		   		</ct:ifhasright>
		   		</TD>
		   		<%} else {%>
		   		  <TD width="165">N° de passage</TD>
		   		  <%}%>
			            <TD width="513">
              				<INPUT type="text" name="idPassage" class="numeroCourtDisabled" tabindex="-1"  value="<%=viewBean.getIdPassage()%>">
						</TD>
          			</TR>
          			<TR>
						<TD nowrap width="157">P&eacute;riode de la décision</TD>
            			<TD nowrap width="280">
							<ct:FWCalendarTag name="debutDecision"
							value="<%=viewBean.getDebutDecision()%>"
							errorMessage="la date de début est incorrecte"
							doClientValidation="CALENDAR,NOT_EMPTY"
						  	/> &nbsp;au&nbsp;
						  	<ct:FWCalendarTag name="finDecision"
							value="<%=viewBean.getFinDecision()%>"
							errorMessage="la date de fin est incorrecte"
							doClientValidation="CALENDAR,NOT_EMPTY"
						 	/>
	 					</TD>
			            <TD nowrap width="25"></TD>
			            <TD nowrap width="110">Indication au</TD>
			            <TD nowrap width="171">
			            	<ct:FWCalendarTag name="dateInformation"
							value="<%=viewBean.getDateInformation()%>"
							errorMessage="la date d'information est incorrecte"
							doClientValidation="CALENDAR,NOT_EMPTY"/>
						</TD>
       				</TR>
           			<TR>
			             <TD nowrap height="31" width="110">Période totale</TD>
	            		<TD nowrap height="31">
	            			<INPUT type="text" name="nombreMoisTotalDecision" class="numeroCourt" value="<%=viewBean.getNombreMoisTotalDecision()%>">(plusieurs décisions dans l'année)
	            		</TD>
			            <TD nowrap width="25"></TD>
			            <TD width="165">Source d'information</TD>
			            <TD width="513">
			            	<%
					     		java.util.HashSet exceptSource = new java.util.HashSet();
		            			if(!viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_DEFINITIVE)&&!viewBean.getTypeDecision().equalsIgnoreCase(CPDecision.CS_RECTIFICATION)) {
		            				exceptSource.add(CPDonneesBase.CS_REPRISE_IMPOT);
		            			}
		            		    %>
			            	 <ct:FWCodeSelectTag name="sourceInformation"
				      		defaut="<%=viewBean.getSourceInformation()%>"
				      		wantBlank="<%=false%>"
				      		except="<%=exceptSource%>"
				     	    codeType="CPSRCINFO"/>
			              </TD>
          			</TR>
	   				<TR>
            			<TD nowrap  height="11" colspan="5">
            				<HR size="3" width="100%">
            			</TD>
          			</TR>
          	</TABLE>
      <!-- Page 1 -->
      		<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
        <!--<COL span="1" width="100">-->
          			<TR>
            			<TD nowrap width="200">Période de cotisation</TD>
            			<TD nowrap colspan="2">
                  			<INPUT name="anneeRevenuDebut" type="text" value="<%=viewBean.getAnneeRevenuDebut()%>" class="numeroCourtDisabled" tabindex="-1"   readonly>
              				<%
							Object[] ifdMethodsName = new Object[]{
								new String[]{"setIdIfdProvisoire","getIdIfd"},
								new String[]{"setIdIfdDefinitif","getIdIfd"},
								new String[]{"setNumIfdDefinitif","getNumIfd"},
								new String[]{"setAnneeRevenuDebut","getAnneeRevenuDebut"},
								new String[]{"setAnneeRevenuFin","getAnneeRevenuFin"},
								new String[]{"setDebutExercice1","getDebutRevenu1"},
								new String[]{"setFinExercice1","getFinRevenu1"},
								new String[]{"setDebutExercice2","getDebutRevenu2"},
								new String[]{"setFinExercice2","getFinRevenu2"}
							};
							Object[] ifdParams= new Object[]{
								new String[]{"numIfdDefinitif","fromNumIfd"},
					                     new String[]{"anneeDecision ","forAnneeDecisionDebut"}
							};
							String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionInd_de.jsp";
							%>
							<ct:FWSelectorTag
							name="ifdSelector"

							methods="<%=ifdMethodsName%>"
							providerApplication ="phenix"
							providerPrefix="CP"
							providerAction ="phenix.divers.periodeFiscale.chercher"
							providerActionParams ="<%=ifdParams%>"
							redirectUrl="<%=redirectUrl%>"

							/>
            			</TD>
            			<TD nowrap width="24"></TD>
			            <TD nowrap width="108"></TD>
			            <TD nowrap colspan="2"><INPUT name="anneeRevenuFin" type="text" value="<%=viewBean.getAnneeRevenuFin()%>" class="numeroCourtDisabled"  tabindex="-1"  readonly></TD>
          			</TR>
          			<TR>
			            <TD nowrap width="157">Revenu</TD>
			            <TD nowrap width="96"><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" data-g-integer="sizeMax:8" style="width : 2.85cm;"></TD>
			            <TD nowrap width="184"></TD>
						<TD nowrap width="24">&nbsp; </TD>
			            <TD nowrap width="108"></TD>
			            <TD nowrap width="98"><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" data-g-integer="sizeMax:8" style="width : 2.85cm;"> </TD>
			            <TD id="nbMoisEx2" nowrap width="96"></TD>
          			</TR>
          			<TR id="ligneRevenuAutre">
          			<%
		      		if(((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).isRevenuAgricole()) {
		      		%>
			            <TD nowrap width="157"><%=viewBean.getSession().getApplication().getLabel("REVENU_AGRICOLE", "FR")%></TD>
			        <% } else { %>
						<TD nowrap width="157"><%=viewBean.getSession().getApplication().getLabel("REVENU_AUTRE", "FR")%></TD>
			        <% } %>
			            <TD nowrap width="96"><INPUT name="revenuAutre1" type="text" value="<%=viewBean.getRevenuAutre1()%>" class="montant" data-g-integer="sizeMax:8"  style="width : 2.85cm;"></TD>
			            <TD nowrap width="184"></TD>
			            <TD nowrap width="24">&nbsp;</TD>
			            <TD nowrap width="108"></TD>
			            <TD nowrap width="98"><INPUT name="revenuAutre2" type="text" value="<%=viewBean.getRevenuAutre2()%>" class="montant" data-g-integer="sizeMax:8"  style="width : 2.85cm;"> </TD>
			            <TD id="nbMoisAutre2" nowrap width="96"></TD>
					</TR>
           			<TR>
			            <TD nowrap width="157">Cotisations</TD>
			            <TD nowrap width="96"><INPUT name="cotisation1" type="text" value="<%=viewBean.getCotisation1()%>" class="montant"  style="width : 2.85cm;"></TD>
			            <!-- D0108 - Ajout du libellé sur la détermination de la cotisation  -->
			            <%
		      			if(CPToolBox.isCotisationCalculeeSelonDispositionLegale(viewBean.getAffiliation().getTypeAffiliation(), viewBean.getIdCommunication(), viewBean.getCotisation1())) {
		      			%>
			            	<TD nowrap colspan="5">&nbsp;<%=viewBean.getSession().getApplication().getLabel(
                                "CP_COTISATION_CALCULEE_AUTO", "FR") %></TD>
			            <% } else { %>
			             	<TD nowrap width="184"></TD>
				          	<TD nowrap width="24">&nbsp; </TD>
				            <TD nowrap width="108"></TD>
				            <TD nowrap width="98"><INPUT name="cotisation2" type="text" value="<%=viewBean.getCotisation2()%>" class="montant"  style="width : 2.85cm;"> </TD>
				            <TD nowrap width="96"></TD>
			            <% }%>
          			</TR>
					<TR id="ligneExercice">
            			<TD nowrap width="157" height="14">Exercice du</TD>
            			<TD nowrap height="31" colspan="2">
            				<ct:FWCalendarTag name="debutExercice1"
							value="<%=viewBean.getDebutExercice1()%>"
							errorMessage="la date de début est incorrecte"
							doClientValidation="CALENDAR"
						  	/> &nbsp;au&nbsp;
						  	<ct:FWCalendarTag name="finExercice1"
							value="<%=viewBean.getFinExercice1()%>"
							errorMessage="la date de fin est incorrecte"
							doClientValidation="CALENDAR"
	 						/>
	 					</TD>
            			<TD nowrap height="31" width="24"></TD>
			            <TD nowrap height="31" width="108">&nbsp;</TD>
			            <TD id="exercice2" nowrap height="31" colspan="2">
				            <ct:FWCalendarTag name="debutExercice2"
							value="<%=viewBean.getDebutExercice2()%>"
							errorMessage="la date de début est incorrecte"
							doClientValidation="CALENDAR"
						  	/> au
						  	<ct:FWCalendarTag name="finExercice2"
							value="<%=viewBean.getFinExercice2()%>"
							errorMessage="la date de fin est incorrecte"
							doClientValidation="CALENDAR"
						 	/>
					 	</TD>
          			</TR>
					<TR>
						<TD nowrap width="157">Capital investi</TD>
						<TD nowrap height="31" colspan="2">
							<INPUT name="capital" type="text" value="<%=viewBean.getCapital()%>" class="montant">
						</TD>
						<TD nowrap height="31" width="24"></TD>
						<TD nowrap height="31" width="108"></TD>
						<TD nowrap height="31" colspan="2"></TD>
					</TR>
	   </TABLE>
 	   <!-- Page 1 -->
 	   <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie4">
		  	<TR>
	            <TD nowrap width="200"></TD>
	            <TD nowrap width="280"></TD>
	            <TD nowrap width="25"></TD>
	            <TD nowrap width="108"></TD>
	            <TD nowrap width="195"></TD>
	      	</TR>
	  </TABLE>
      <!-- récapitulatif -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie6">
		  <TR>
	            <TD nowrap width="200"></TD>
	            <TD nowrap width="150" align="center">&nbsp;</TD>
	            <TD nowrap width="50"></TD>
	            <TD nowrap align="center" width="165"></TD>
	            <TD nowrap width="46"></TD>
	            <TD nowrap align="center" width="196"></TD>
	      </TR>
	      <TR>
	       	<TD nowrap  height="11" colspan="6">
	       		<HR size="3" width="100%">
	       	</TD>
          </TR>
          <TR>
          	<TD nowrap>Revenu déterminant</TD>
          	<TD nowrap width="150">
          		<INPUT name="revenuDeterminant" type="text"
				value="<%=viewBean.getRevenuDeterminant()%>"
				class="montantDisabled" tabindex="-1"  readonly style="color: olive">
			</TD>
            <TD nowrap width="40"></TD>
            <TD nowrap width="165"></TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196"></TD>
          </TR>
          <tr>
          <TD valign="top"  width="100">
				<%
             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
             		String gedNumAvs = viewBean.getNumAVS();
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = "";
             	%>
				
				<%@ include file="/theme/gedCall.jspf" %>
			</TD>
			</tr>
          <TR>
          	<TD nowrap width="157">
	          	<%
		      	String idRetour =viewBean.getIdCommunication();
		      	if(!JadeStringUtil.isBlankOrZero(idRetour)){
		      	%>
		      	<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficher" crud="r">
		   		<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.afficher&selectedId=<%=viewBean.getIdCommunication()%>" class="external_link">Données fiscales</A>
		   		</ct:ifhasright>
		   		<%}%>
           	</TD>
            <TD nowrap width="150" align="center"></TD>
            <TD nowrap width="50"></TD>
            <TD nowrap align="center" width="165">
            	<INPUT type="hidden" name="selectedId2" value='<%=viewBean.loadAffiliation().getAffiliationId()%>'>
            </TD>
            <TD nowrap width="46">
            	<INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'>
            </TD>
	    	<TD nowrap width="195"><B><A href="javascript:showPartie1()">Page 1</A></B> -- <A href="javascript:showPartie2()">Page 2</A></TD>
	    </TR>
	  </TABLE>
      <!-- Page 2 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie5" style="display:none">
			<TR>
	            <TD nowrap width="200" height="14">Facturation</TD>
	            <TD nowrap height="31" width="280"><INPUT type="text" name="periodicite" value="<%=viewBean.getPeriodicite()%>"  tabindex="-1"  readonly class="inputDisabled" size="12"></TD>
	            <TD nowrap height="31" width="25"></TD>
	            <TD nowrap height="31" width="110">N° de décision</TD>
	            <TD nowrap height="31"><INPUT type="text" name="idDecision" value="<%=viewBean.getIdDecision()%>" class="inputDisabled"  tabindex="-1"  readonly size="12"></TD>
          	</TR>
          	<TR>
	            <TD nowrap width="157" height="14">Revenu CI forcé</TD>
	            <TD nowrap height="31" width="280">
	              <INPUT name="revenuCiForce" type="text" value="<%=viewBean.getRevenuCiForce()%>" class="montant">
	            </TD>
            	<TD nowrap height="31" width="25"></TD>
       <!--     <TD nowrap height="31" width="110">Valeur à 0 ?</TD>
            	<TD nowrap height="31">
	          		<input type="checkbox" name="revenuCiForce0" <%=(viewBean.getRevenuCiForce0().booleanValue())? "checked" : "unchecked"%>>
            	</TD>-->
             	<TD nowrap height="31" width="110"></TD>
            	<TD nowrap height="31"></TD>
          	</TR>
          	<TR>
	            <TD nowrap width="157" height="14">Facturation</TD>
	            <TD nowrap height="31" width="259"><input type="checkbox" name="facturation" <%=(viewBean.getFacturation().booleanValue())? "checked" : "unchecked"%>></TD>
	            <TD nowrap height="31" width="25"></TD>
	            <TD nowrap height="31" width="110">Impression</TD>
	            <TD nowrap height="31"><input type="checkbox" name="impression" <%=(viewBean.getImpression().booleanValue())? "checked" : "unchecked"%>></TD>
          	</TR>
          	<TR>
	            <TD nowrap width="157" height="14">Soumis intérêts</TD>
	            <TD nowrap height="31" width="259">
            			<%
			     			java.util.HashSet except = new java.util.HashSet();
			            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_A_CONTROLER);
			            %>

					<ct:FWSystemCodeSelectTag name="interet"
					defaut="<%=viewBean.getInteret()%>"
					codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsMotifIm(session)%>"
					except="<%=except%>"
					/>
	     		</TD>
	            <TD nowrap height="31" width="25"></TD>
	           	<TD nowrap height="31" width="110">Complémentaire</TD>
	            <TD nowrap height="31">
	            	<INPUT type="checkbox" name="complementaire" <%=(viewBean.getComplementaire().booleanValue())? "checked" : "unchecked"%>>
	            </TD>
	       <!-- <TD nowrap height="31" width="110">Suspend</TD>
            	<TD nowrap height="31"><input type="checkbox" name="bloque" <%=(viewBean.getBloque().booleanValue())? "checked" : "unchecked"%>></TD>
      		-->
          	</TR>
         	<TR>
	            <TD nowrap width="157" height="14">Debut activité</TD>
	            <TD nowrap height="31" width="259"><input type="checkbox" name="debutActivite" <%=(viewBean.getDebutActivite().booleanValue())? "checked" : "unchecked"%>></TD>
	            <TD nowrap height="31" width="25"></TD>
	            <TD nowrap width="110" height="31">Première assurance</TD>
	            <TD nowrap height="31"><input type="checkbox" name="premiereAssurance" <%=(viewBean.getPremiereAssurance().booleanValue())? "checked" : "unchecked"%>></TD>
        	</TR>
	   		<TR>
	            <TD nowrap width="157" height="14">Opposition</TD>
	            <TD nowrap height="31" width="259"><input type="checkbox" name="opposition" <%=(viewBean.getOpposition().booleanValue())? "checked" : "unchecked"%>></TD>
	            <TD nowrap height="31" width="25"></TD>
	            <TD nowrap width="110" height="31">DIN 1181</TD>
	            <TD nowrap height="31"><input type="checkbox" name="cotiMinimumPayeEnSalarie" <%=(viewBean.getCotiMinimumPayeEnSalarie().booleanValue())? "checked" : "unchecked"%>>
	             &nbsp;(cotisation minimum payée en tant que salarié)</TD>
	      	</TR>
          	<TR>
	            <TD nowrap width="157" height="14">Recours</TD>
	            <TD nowrap height="31" width="259"><input type="checkbox" name="recours" <%=(viewBean.getRecours().booleanValue())? "checked" : "unchecked"%>></TD>
	            <TD nowrap height="31" width="25"></TD>
	            <TD nowrap width="110" height="31">Recommandé</TD>
	            <TD nowrap height="31"><input type="checkbox" name="lettreSignature" <%=(viewBean.getLettreSignature().booleanValue())? "checked" : "unchecked"%>></TD>
          	</TR>
          	<TR>
	            <TD nowrap width="157" height="14">&nbsp;</TD>
	            <TD nowrap height="31" width="259">&nbsp;</TD>
	            <TD nowrap height="31" width="25"><INPUT type="hidden" name="idIfdProvisoire" value='<%=viewBean.getIdIfdProvisoire()%>'></TD>
	            <TD nowrap height="31" width="110"><INPUT type="hidden" name="taxation" value='<%=viewBean.getTaxation()%>'></TD>
	            <TD nowrap height="31"><A href="javascript:showPartie1()">Page 1</A>-- <B><A href="javascript:showPartie2()">Page 2</A></B> </TD>
          	</TR>
          </TABLE>
          </td></tr>
         <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-decision" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDecision()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>"/>
</ct:menuChange>
<script>
// Affichage page 1
function showPartie1() {
	document.all('tPartie5').style.display='none';
	document.all('tPartie1').style.display='block';
	document.all('tPartie4').style.display='block';
	document.all('tPartie6').style.display='block';
	showInline("ligneExercice");
	showInline("ligneRevenuAutre");

	<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
		hide("anneeRevenuFin");
		hide("revenu2");
		hide("revenuAutre2");
		hide("exercice2");
		hide("cotisation2");
	<% } %>
	<% if ((viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE))||
	(viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) { %>
		<% if(!((CPApplication)viewBean.getSession().getApplication()).isRevenuAgricole()
		&& !((CPApplication)viewBean.getSession().getApplication()).isRevenuAf()){ %>
			hide("ligneRevenuAutre");
		<% } %>
		<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
			hide("anneeRevenuFin");
			hide("revenu2");
			hide("exercice2");
			hide("cotisation2");
		<% } %>
	<% } %>

}
// Affichage page 2
function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie4').style.display='none';
	document.all('tPartie6').style.display='none';
	document.all('tPartie5').style.display='block';
}

<% if (viewBean.isDateExercice()) { %>
	showInline("ligneExercice");
	showInline("ligneRevenuAutre");
<% } %>

<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
	hide("anneeRevenuFin");
	hide("revenu2");
	hide("revenuAutre2");
	hide("exercice2");
	hide("cotisation2");
<% } %>

<% if ((viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE))||
(viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) { %>
	showInline("ligneExercice");
	<% if(!((CPApplication)viewBean.getSession().getApplication()).isRevenuAgricole()
	&&!((CPApplication)viewBean.getSession().getApplication()).isRevenuAf()){ %>
			hide("ligneRevenuAutre");
	<% } %>
	<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
		hide("anneeRevenuFin");
		hide("revenu2");
		hide("exercice2");
		hide("cotisation2");
	<% } %>
<% } %>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>