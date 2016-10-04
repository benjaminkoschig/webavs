<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@page import="globaz.phenix.db.principale.CPDonneesBase"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->


<%@page import="globaz.jade.client.util.JadeStringUtil"%><SCRIPT language="JavaScript">
<%
	idEcran="CCP0004";
	globaz.phenix.db.principale.CPDecisionViewBean viewBean = (globaz.phenix.db.principale.CPDecisionViewBean)session.getAttribute ("viewBean");
	subTableWidth = "0";
	if (globaz.phenix.translation.CodeSystem.getLibelle(session, globaz.phenix.db.principale.CPDecision.CS_VALIDATION).equalsIgnoreCase(viewBean.getEtatDecision())){
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonNew = false;
	}
	key="globaz.phenix.db.principale.CPDecisionViewBean-idDecision"+viewBean.getIdDecision();
%>
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
<SCRIPT language="JavaScript">
top.document.title = "Beiträge - NE Verfügung"
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
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
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
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdDecision()%>" tableSource="globaz.phenix.db.principale.CPDecisionViewBean"/></span>Verfügung - Detail<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<!-- Entete -->
          <TR>
            <TD nowrap width="200" height="10">Versicherter</TD>
            <TD nowrap width="150">
              <INPUT type="text" name="numAffilie" value="<%=viewBean.loadAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" tabindex="-1" readonly>
	     </TD>
            <TD nowrap width="25"></TD>
            <TD nowrap colspan="2"><INPUT type="text" name="nom" value="<%=viewBean.loadTiers().getNom()%>" class="libelleLongDisabled" tabindex="-1" readonly>
              <input name="etatDecision" type="text" value="<%=viewBean.getEtatDecision()%>" class="numeroDisabled" tabindex="-1" readonly>
            </TD>
          </TR>
	  <TR>
	   <TD nowrap  height="11" colspan="5">
              <hr size="3" width="100%">
            </TD>
          </TR>
          <TR>
            <TD nowrap width="157">Verfügung als </TD>
            <TD nowrap width="244">
              <INPUT name="libelleGenreAffilie" type="text" value="<%=viewBean.getLibelleGenreAffilie()%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">Sachbearbeiter</TD>
            <TD nowrap width="171">
            	<ct:FWListSelectTag name="responsable"
            		defaut="<%=viewBean.getResponsable()%>"
            		data="<%=viewBean.getUserList(session)%>"/>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="157">Art</TD>
            <TD nowrap><INPUT name="libelleTypeDecision" type="text" value="<%=viewBean.getLibelleTypeDecision()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">DBST-Nr.</TD>
            <TD nowrap width="171"><INPUT name="numIfdDefinitif" type="text" value="<%=viewBean.getNumIfdDefinitif()%>" class="numeroCourtDisabled" tabindex="-1" readonly></TD>
          </TR>
	    <TR>
            <TD nowrap width="157">Ehepartner</TD>
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

			String redirectUrl1 = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionNac_de.jsp?appel=1";
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
			<INPUT name="idConjoint" type="hidden" value="<%=viewBean.getIdConjoint()%>"><INPUT name="conjoint" type="text" value="<%=viewBean.getConjoint()%>" class="libelleLongDisabled" tabindex="-1" readonly style="color : purple;">
		<input type="checkbox" name="division2" <%=(viewBean.getDivision2().booleanValue())? "checked" : "unchecked"%>> (simuliert Ehegatten)</TD>
          </TR>
	   <TR>
            <TD nowrap width="157">Jahr</TD>
            <TD nowrap><INPUT name="anneeDecision" type="text" size="4" value="<%=viewBean.getAnneeDecision()%>" maxlength="4" class="numeroCourtDisabled" tabindex="-1" readonly></TD>
            <TD nowrap width="25"></TD>
         <%
		      	String idEntete =viewBean.rechercheIdEnteteFacture();
			    String linkFacture ="";
		      	if(!JadeStringUtil.isBlankOrZero(idEntete)){
		      		linkFacture = "musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=" + idEntete + "&idPassage=" + viewBean.getIdPassage();
		      		
		      	%>
		      	<TD width="165">
		      	<ct:ifhasright element="musca.facturation.afact.chercher" crud="r">
		   		<A href="<%=request.getContextPath() + "/" + linkFacture%> class="external_link">Job-Nr.</A>
		   		</ct:ifhasright>
		   		</TD>
		   		<%} else {%>
		   		  <TD width="165">job-Nr.</TD>
		   		  <%}%>            <TD width="513">
              <INPUT type="text" name="idPassage" maxlength="15" size="15" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" readonly>
			</TD>
          </TR>
          <TR>
		<TD nowrap width="157">Verfügungsperiode</TD>
            <TD nowrap width="280">
		<ct:FWCalendarTag name="debutDecision"
		value="<%=viewBean.getDebutDecision()%>"
		errorMessage="Das Beginndatum ist falsch."
		doClientValidation="CALENDAR,NOT_EMPTY"
	  /> &nbsp;bis &nbsp; <ct:FWCalendarTag name="finDecision"
		value="<%=viewBean.getFinDecision()%>"
		errorMessage="Das Enddatum ist falsch."
		doClientValidation="CALENDAR,NOT_EMPTY"
	 /> </TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">Anzeige am</TD>
            <TD nowrap width="171"><ct:FWCalendarTag
									name="dateInformation"
		value="<%=viewBean.getDateInformation()%>"
		errorMessage="Das Datum ist falsch."
		doClientValidation="CALENDAR,NOT_EMPTY"/></TD>
          </TR>
          <TR>
            <TD nowrap height="31" width="110">Total Periode</TD>
    		<TD nowrap height="31">
    			<INPUT type="text" name="nombreMoisTotalDecision" class="numeroCourt" value="<%=viewBean.getNombreMoisTotalDecision()%>">(mehrere Verfügungen im Jahr)
    		</TD>
            <TD nowrap width="25"></TD>
            <TD width="165">Informationsquelle</TD>
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

      <!-- Seite 1 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
        <COL span="1" width="100">
        <TBODY>
          <TR>
            <TD nowrap width="200">Beitragsperiode</TD>
            <TD nowrap colspan="2">
                  <INPUT name="anneeRevenuDebut" type="text" value="<%=viewBean.getAnneeRevenuDebut()%>" class="numeroCourtDisabled" tabindex="-1" readonly>
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
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionNac_de.jsp?appel=1";
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
            <TD nowrap colspan="2"><INPUT name="anneeRevenuFin" type="text" value="<%=viewBean.getAnneeRevenuFin()%>" class="numeroCourtDisabled" tabindex="-1" readonly></TD>
          </TR>
          <TR>
            <TD nowrap width="157">Renteneinkommen</TD>
            <TD nowrap width="96"><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap width="184"> für <INPUT name="nbMoisExercice1" type="text" value="<%=viewBean.getNbMoisExercice1()%>" size="2" maxlength="2"> Monat
            </TD>
		<TD nowrap width="24">&nbsp; </TD>
            <TD nowrap width="108"></TD>
            <TD nowrap width="98"><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" style="width : 2.45cm;"> </TD>
            <TD id="nbMoisEx2" nowrap width="110"> für <INPUT name="nbMoisExercice2" type="text" value="<%=viewBean.getNbMoisExercice2()%>" size="2" maxlength="2"> Monat
             </TD>
          </TR>
          <TR>
          	<TD nowrap width="157">Einkommen AHV Rente(n)</TD>
          	<TD nowrap width="96" colspan="6"><INPUT name="montantTotalRenteAVS" type="text" value="<%=viewBean.getMontantTotalRenteAVS()%>" class="montant" style="width : 2.45cm;"></TD>
          </TR>
          <TR id="ligneRevenuAutre">
            <TD nowrap width="157">Andere</TD>
            <TD nowrap width="96"><INPUT name="revenuAutre1" type="text" value="<%=viewBean.getRevenuAutre1()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap width="184"> für <INPUT name="nbMoisRevenuAutre1" type="text" value="<%=viewBean.getNbMoisRevenuAutre1()%>" size="2" maxlength="2"> Monat
            </TD>
            <TD nowrap width="24">&nbsp;</TD>
            <TD nowrap width="108"></TD>
            <TD nowrap width="98"><INPUT name="revenuAutre2" type="text" value="<%=viewBean.getRevenuAutre2()%>" class="montant" style="width : 2.45cm;"> </TD>
            <TD id="nbMoisAutre2" nowrap width="110"> für <INPUT name="nbMoisRevenuAutre2" type="text" value="<%=viewBean.getNbMoisRevenuAutre2()%>" size="2" maxlength="2"> Monat
            </TD>
          </TR>
	<TR id="ligneExercice">
            <TD nowrap width="157" height="14">Rechnungsjahr vom</TD>
            <TD nowrap height="31" colspan="2"><ct:FWCalendarTag name="debutExercice1"
		value="<%=viewBean.getDebutExercice1()%>"
		errorMessage="Das Begindatum ist falsch."
		doClientValidation="CALENDAR"
	  /> &nbsp;bis&nbsp; <ct:FWCalendarTag name="finExercice1"
		value="<%=viewBean.getFinExercice1()%>"
		errorMessage="Das Enddatum ist falsch."
		doClientValidation="CALENDAR"
	 /> </TD>
            <TD nowrap height="31" width="24"></TD>
            <TD nowrap height="31" width="108">&nbsp;</TD>
            <TD id="exercice2" nowrap height="31" colspan="2"><ct:FWCalendarTag name="debutExercice2"
		value="<%=viewBean.getDebutExercice2()%>"
		errorMessage="Das Begindatum ist falsch."
		doClientValidation="CALENDAR"
	  /> bis<ct:FWCalendarTag name="finExercice2"
		value="<%=viewBean.getFinExercice2()%>"
		errorMessage="Das Enddatum ist falsch."
		doClientValidation="CALENDAR"
	 /> </TD>
          </TR>
	   <TR>
            <TD nowrap width="157">Vermögen per</TD>
            <TD nowrap height="31" colspan="2"><ct:FWCalendarTag name="dateFortune"
		value="<%=viewBean.getDateFortune()%>"
		errorMessage="Das Vermögendatum is falsch."
		doClientValidation="CALENDAR,NOT_EMPTY"
	  	/></TD>
            <TD nowrap height="31" width="24"></TD>
            <TD nowrap height="31" width="108">&nbsp;</TD>
            <TD nowrap height="31" colspan="2"></TD>
          </TR>
					<TR>
						<TD nowrap width="157">Total Vermögen</TD>
						<TD nowrap height="31" colspan="2">
						<%if (JadeStringUtil.isNull(request.getParameter("appel"))== false) {%>
							<INPUT name="fortuneTotale" type="text" value="<%=viewBean.getFortuneTotale()%>" class="montantDisabled" tabindex="-1" readonly style="color : olive;">
						<% } else { %>
	 						<% if (((viewBean.isCPCALCIMMO()) && (!viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE)))
		 					&& (!viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) { %>
                	     		<INPUT name="fortuneTotale" type="text" value="<%=viewBean.getFortuneTotale()%>" class="montantDisabled" tabindex="-1" readonly style="color : olive;">
							<%} else {%>
		    					<INPUT name="fortuneTotale" type="text" value="<%=viewBean.getDivision2().booleanValue() || !JadeStringUtil.isBlankOrZero(viewBean.getIdConjoint())? viewBean.getFortuneTotaleFois2() : viewBean.getFortuneTotale()%>" class="montant">
	 						<%}%>
	 					<%}%>	
	 					</TD>
	 					<TD nowrap height="31" width="24"></TD>
						<TD nowrap height="31" width="108"></TD>
						<TD nowrap height="31" colspan="2"></TD>
					</TR>
				</TBODY>
	   </TABLE>
      <!-- Seite 1 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie2">
           <TBODY>
          <TR>
            <TD nowrap width="200">Liegenschaften</TD>
            <TD nowrap width="150" align="center">Steuerwert</TD>
            <TD nowrap width="50"></TD>
            <TD nowrap align="left" width="165">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Kanton</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap align="left" width="196">Geländeart</TD>
          </TR>
	   <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap width="150"><INPUT name="montantImmobilier1" type="text" value="<%=viewBean.getMontantImmobilier1()%>" class="montant"></TD>
            <TD nowrap width="40"></TD>
            <TD nowrap width="165">
		<ct:FWCodeSelectTag name="canton1"
			defaut="<%=viewBean.getCanton1()%>"
			wantBlank="<%=true%>"
			libelle="codeLibelle"
		        codeType="PYCANTON"/>

		</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196">
            <ct:FWCodeSelectTag name="typeTerrain1"
	      		defaut="<%=viewBean.getTypeTerrain1()%>"
	      		wantBlank="<%=true%>"
	    	        codeType="CPTYPTERRE"/>
	  </TD>
          </TR>
          <TR>
           <TD nowrap>&nbsp;</TD>
           <TD nowrap width="150"><INPUT name="montantImmobilier2" type="text" value="<%=viewBean.getMontantImmobilier2()%>" class="montant"></TD>
            <TD nowrap width="40">

            </TD>
            <TD nowrap width="165">
		<ct:FWCodeSelectTag name="canton2"
			defaut="<%=viewBean.getCanton2()%>"
			wantBlank="<%=true%>"
			libelle="codeLibelle"
		        codeType="PYCANTON"/>
            </TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196">
            <ct:FWCodeSelectTag name="typeTerrain2"
	      		defaut="<%=viewBean.getTypeTerrain2()%>"
	      		wantBlank="<%=true%>"
	    	        codeType="CPTYPTERRE"/>
           </TD>
          </TR>
	 <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap width="150"><INPUT name="montantImmobilier3" type="text" value="<%=viewBean.getMontantImmobilier3()%>" class="montant"></TD>
            <TD nowrap width="40">

            </TD>
            <TD nowrap width="165">
		<ct:FWCodeSelectTag name="canton3"
			defaut="<%=viewBean.getCanton3()%>"
			wantBlank="<%=true%>"
			libelle="codeLibelle"
		        codeType="PYCANTON"/>
		</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196">
            <ct:FWCodeSelectTag name="typeTerrain3"
	      		defaut="<%=viewBean.getTypeTerrain3()%>"
	      		wantBlank="<%=true%>"
	    	        codeType="CPTYPTERRE"/>
	    </TD>
          </TR>
	 <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap width="150"><INPUT name="montantImmobilier4" type="text" value="<%=viewBean.getMontantImmobilier4()%>" class="montant"></TD>
            <TD nowrap width="40"></TD>
            <TD nowrap width="165">
		<ct:FWCodeSelectTag name="canton4"
			defaut="<%=viewBean.getCanton4()%>"
			wantBlank="<%=true%>"
			libelle="codeLibelle"
		        codeType="PYCANTON"/>
		</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196">
            <ct:FWCodeSelectTag name="typeTerrain4"
	      		defaut="<%=viewBean.getTypeTerrain4()%>"
	      		wantBlank="<%=true%>"
	    	        codeType="CPTYPTERRE"/>
	  </TD>
          </TR>
		</TBODY>
	   </TABLE>
      <!-- Seite 1 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie3">
         <TBODY>
	  <TR>
            <TD nowrap width="157">&nbsp;</TD>
            <TD nowrap width="280"></TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="107"></TD>
            <TD nowrap width="196"></TD>
          </TR>
	  <TR id="ligneFortune">
            <TD nowrap width="200">Übriges Vermögen</TD>
            <TD nowrap width="280"><INPUT name="autreFortune" type="text" value="<%=viewBean.getAutreFortune()%>" class="montant"></TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="107">Passiven</TD>
            <TD nowrap width="196"><INPUT name="dette" type="text" value="<%=viewBean.getDette()%>" class="montant"></TD>
          </TR>
	   </TBODY>
   	</TABLE>
      <!-- Seite 1 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie4">
         <TBODY>
	  <TR>
            <TD nowrap width="200"></TD>
            <TD nowrap width="280"></TD>
            <TD nowrap width="25"></TD>
            <TD id="titreCotSalarie" nowrap width="108">AHV/IV/EO-Beiträge aus Erwerbsätigkeit</TD>
            <TD nowrap width="195"><INPUT name="cotisationSalarie" type="text" value="<%=viewBean.getCotisationSalarie()%>" class="montant"></TD>
          </TR>
	   </TBODY>
   	</TABLE>
      <!-- récapitulatif -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie6">
           <TBODY>
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
            <TD nowrap width="200"></TD>
            <TD nowrap width="150" align="left">Total Vermögen</TD>
            <TD nowrap width="50"></TD>
            <TD nowrap align="center" width="165">Massgebendes Vermögen</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap align="left" width="196"></TD>
          </TR>
	   <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap width="150">
            <INPUT name="fortuneTotaleCalculee" type="text" value="<%=viewBean.getFortuneTotale()%>" class="montantDisabled" tabindex="-1" readonly style="color : olive;">
		</TD>
            <TD nowrap width="40"></TD>
            <TD nowrap width="165">
		    <INPUT name="fortuneDeterminante" type="text" value="<%=viewBean.getFortuneDeterminante()%>" class="montantDisabled" tabindex="-1" readonly style="color : olive;">
		</TD>
            <TD nowrap width="46"></TD>
            <TD nowrap width="196">
	  </TD>
          </TR>
	<TR>
           <TD nowrap width="157">
            <%
	    	String idRetour =viewBean.getIdCommunication();
	        if(!JadeStringUtil.isEmpty(idRetour)){
	     %>
	     		<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficher" crud="r">
	           <A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.afficher&selectedId=<%=viewBean.getIdCommunication()%>" class="external_link">Steuerdaten</A>
	           </ct:ifhasright>
	   <%}%>
           </TD>
            <TD nowrap width="150" align="center"></TD>
            <TD nowrap width="50"></TD>
            <TD nowrap align="center" width="165"><INPUT type="hidden" name="selectedId2" value='<%=viewBean.loadAffiliation().getAffiliationId()%>'></TD>
            <TD nowrap width="46"><INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'></TD>
	    <TD nowrap width="195"><B><A href="javascript:showPartie1()">Seite 1</A></B> -- <A href="javascript:showPartie2()">Seite 2</A></TD>
          </TR>
      	</TBODY>
	   </TABLE>
      <!-- Seite 2 -->
      <TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie5" style="display:none">
          <TBODY>
	<TR>
            <TD nowrap width="200" height="14">Fakturierung</TD>
            <TD nowrap height="31" width="280"><INPUT type="text" name="periodicite" value="<%=viewBean.getPeriodicite()%>" tabindex="-1" tabindex="-1" readonly class="inputDisabled" size="12"></TD>
            <TD nowrap height="31" width="25"></TD>
            <TD nowrap height="31" width="110">Verfügung-Nr.</TD>
            <TD nowrap height="31"><INPUT type="text" name="idDecision" value="<%=viewBean.getIdDecision()%>" class="inputDisabled" tabindex="-1" tabindex="-1" readonly size="12"></TD>
          </TR>
          <TR>
            <TD nowrap width="157" height="14">Forciertes IK Einkommen</TD>
            <TD nowrap height="31" width="280">
              <INPUT name="revenuCiForce" type="text" value="<%=viewBean.getRevenuCiForce()%>" class="montant">
            </TD>
            <TD nowrap height="31" width="25"></TD>
         <!--   <TD nowrap height="31" width="110">Steuerwert ist 0 ?</TD>
            <TD nowrap height="31">
	          <input type="checkbox" name="revenuCiForce0" <%=(viewBean.getRevenuCiForce0().booleanValue())? "checked" : "unchecked"%>>
            </TD>
        -->
            <TD nowrap height="31" width="110"></TD>
            <TD nowrap height="31"></TD>
          </TR>
          <TR>
            <TD nowrap width="157" height="14">Fakturierung</TD>
            <TD nowrap height="31" width="259"><input type="checkbox" name="facturation" <%=(viewBean.getFacturation().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD nowrap height="31" width="25"></TD>
            <TD nowrap height="31" width="110">Ausdruck</TD>
            <TD nowrap height="31"><input type="checkbox" name="impression" <%=(viewBean.getImpression().booleanValue())? "checked" : "unchecked"%>></TD>
          </TR>
          <TR>
            <TD nowrap width="157" height="14">Zinspflichtig</TD>
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
            <TD nowrap height="31" width="110">Ergänzend</TD>
            <TD nowrap height="31"><INPUT type="checkbox"
							name="complementaire"
							<%=(viewBean.getComplementaire().booleanValue())? "checked" : "unchecked"%>></TD>

	<!-- <TD nowrap height="31" width="110">Pendent</TD>
            <TD nowrap height="31"><input type="checkbox" name="bloque" <%=(viewBean.getBloque().booleanValue())? "checked" : "unchecked"%>></TD>
-->
          </TR>
	   <TR>
            <TD nowrap width="157" height="14">Einsprache</TD>
            <TD nowrap height="31" width="259"><input type="checkbox" name="opposition" <%=(viewBean.getOpposition().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD nowrap height="31" width="25"></TD>
            <TD nowrap width="110" height="31">LSI</TD>
            <TD nowrap height="31"><input type="checkbox" name="lettreSignature" <%=(viewBean.getLettreSignature().booleanValue())? "checked" : "unchecked"%>></TD>
          </TR>
          <TR>
	        <TD nowrap width="157" height="14">Rückgriff</TD>
	        <TD nowrap height="31" width="259"><input type="checkbox" name="recours" <%=(viewBean.getRecours().booleanValue())? "checked" : "unchecked"%>></TD>
	        <TD nowrap height="31" width="25"></TD>
	        <TD nowrap width="110" height="31"></TD>
	        <TD nowrap height="31"></TD>
          </TR>
          <TR>
            <TD nowrap width="157" height="14">&nbsp;</TD>
            <TD nowrap height="31" width="259">&nbsp;</TD>
            <TD nowrap height="31" width="25"><INPUT type="hidden" name="idIfdProvisoire" value='<%=viewBean.getIdIfdProvisoire()%>'></TD>
            <TD nowrap height="31" width="110"><INPUT type="hidden" name="taxation" value='<%=viewBean.getTaxation()%>'></TD>
            <TD nowrap height="31"><A href="javascript:showPartie1()">Seite 1</A>-- <B><A href="javascript:showPartie2()">Seite 2</A></B> </TD>
          </TR>
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
	<% if (viewBean.isCPCALCIMMO()) { %>
		showBlock("tpartie2");
		showBlock("tpartie3");
		showInline("ligneFortune");
	<% } else { %>
		hide("tpartie2");
		hide("tpartie3");
		hide("ligneFortune");
	<% } %>

	<% if (viewBean.isDateExercice()) { %>
		showInline("ligneExercice");
		hide("ligneRevenuAutre");
		hide("nbMoisEx2");
	<% } else { %>
		showInline("ligneRevenuAutre");
		showInline("nbMoisEx2");
		hide("ligneExercice");
	<% } %>

	<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
		hide("anneeRevenuFin");
		hide("revenu2");
		hide("nbMoisEx2");
		hide("revenuAutre2");
		hide("nbMoisAutre2");
		hide("exercice2");
	<% } %>
	<% if ((viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE))||
	(viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) { %>
		hide("tpartie2");
		hide("tpartie3");
		hide("ligneFortune");
		hide("nbMoisEx2");
		//hide("titreCotSalarie");
		//hide("cotisationSalarie");
		hide("ligneRevenuAutre");
		<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
			hide("anneeRevenuFin");
			hide("revenu2");
			hide("exercice2");
		<% } %>
	<% } %>

}
// Affichage page 2
function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie2').style.display='none';
	document.all('tPartie3').style.display='none';
	document.all('tPartie4').style.display='none';
	document.all('tPartie6').style.display='none';
	document.all('tPartie5').style.display='block';
}

<% if (viewBean.isCPCALCIMMO()) { %>
		showBlock("tpartie2");
		showBlock("tpartie3");
		showInline("ligneFortune");
	<% } else { %>
		hide("tpartie2");
		hide("tpartie3");
		hide("ligneFortune");
<% } %>

<% if (viewBean.isDateExercice()) { %>
	showInline("ligneExercice");
	hide("ligneRevenuAutre");
	hide("nbMoisEx2");
<% } else { %>
	showInline("ligneRevenuAutre");
	showInline("nbMoisEx2");
	hide("ligneExercice");
<% } %>

<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
	hide("anneeRevenuFin");
	hide("revenu2");
	hide("nbMoisEx2");
	hide("revenuAutre2");
	hide("nbMoisAutre2");
	hide("exercice2");
<% } %>

<% if ((viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE))||
(viewBean.getTypeDecision().equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) { %>
	hide("tpartie2");
	hide("tpartie3");
	hide("ligneFortune");
	hide("nbMoisEx2");
	//hide("titreCotSalarie");
	//hide("cotisationSalarie");
	<% if (viewBean.isDateExercice()) { %>
		showInline("ligneExercice");
	<% } else {%>
		hide("ligneExercice");
	<% } %>
	hide("ligneRevenuAutre");
	<% if (viewBean.getTaxation().equalsIgnoreCase("N")) { %>
		hide("anneeRevenuFin");
		hide("revenu2");
		hide("exercice2");
	<% } %>


<% } %>
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>