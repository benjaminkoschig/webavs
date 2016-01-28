<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1019";
CPCommunicationFiscaleRetourGEViewBean viewBean = (CPCommunicationFiscaleRetourGEViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean-idRetour"+viewBean.getIdRetour();
%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausiManager"%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausi"%>
<%@page import="globaz.phenix.interfaces.ICommunicationRetour"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
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
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Données fiscales Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields();

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.supprimer";
		document.forms[0].submit();
	}
}
function init(){}

function retourOriginale() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.retournerOriginale";
		document.forms[0].submit();
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Données fiscales<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<TR>
			<TD><a  onclick="retourOriginale()" href="#">Ecraser les données avec originale (irréversible)</a></TD>
		</TR>
         <TR>
            <TD nowrap width="180">Tiers</TD>
            <TD nowrap >
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNomPrenom()%>" class="libelleLongDisabled" readonly>
		<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
	<%--		<%
			Object[] tiersMethodsName= new Object[]{
				new String[]{"setIdTiers","getIdTiers"},
				new String[]{"setNom","getNom"},
				new String[]{"setNumAffilie","getNumAffilieActuel"},
				new String[]{"setNumAvs","getNumAvsActuel"},
				new String[]{"setLocalite","getLocalite"}
			};
			Object[]  tiersParams = new Object[]{
			};
			%>
			<ct:FWSelectorTag
				name="tiersSelector"

				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=tiersParams %>"
			/> --%>

	     </TD>
       	<TD width="10"></TD>
            <TD nowrap width="50" align="left">Affilié</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
            <TD nowrap width="180"></TD>
            <TD nowrap ><INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly></TD>
	        <TD><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
            <TD nowrap align="left">NSS</TD>
            <TD nowrap>
				<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     	</TD>
	     	<TD>
	     		<INPUT type="hidden" name="idCommunication" value="<%=viewBean.getIdCommunication()%>">
	     		  <INPUT type="hidden"  name="isForBackup" value="<%=viewBean.isForBackup()%>">
	     	</TD>
        </TR>
        <TR>
             <TD nowrap width="140">Période IFD n°</TD>
            <TD nowrap><INPUT type="text" name="numIfd" class="numeroCourtDisabled" value="<%=viewBean.getNumIfd()%>" readonly>
	         	<%
				Object[] ifdMethodsName = new Object[]{
					new String[]{"setIdIfd","getIdIfd"},
					new String[]{"setNumIfd","getNumIfd"},
					new String[]{"setAnnee","getAnneeDecisionDebut"},
					new String[]{"setDebutExercice1","getDebutRevenu1"},
					new String[]{"setFinExercice1","getFinRevenu1"},
					new String[]{"setDebutExercice2","getDebutRevenu2"},
					new String[]{"setFinExercice2","getFinRevenu2"}
				};
				Object[] ifdParams= new Object[]{
					new String[]{"numIfd","fromNumIfd"},
		                     new String[]{"annee","forAnneeDecisionDebut"}
				};
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/communications/apercuCommunicationFiscaleRetour_de.jsp";
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
            <TD></TD>
            <TD nowrap align="left">N° de contribuable</TD>
            <TD nowrap>
				<INPUT type="text" name="numContribuableDisplay" class="libelleLongDisabled" readonly value="<%=viewBean.getNumContribuable()%>" readonly>
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
 	        <TD nowrap width="180">N° contribuable reçu</TD>
            <TD nowrap >
				<INPUT type="text" name="geNumContribuable" class="disabled" value="<%=viewBean.getGeNumContribuable()%>" readonly >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Nom</TD>
            <TD nowrap><INPUT type="text" name="geNom" class="libelleLongDisabled" value="<%=viewBean.getGeNom()%>" readonly></TD>
        </TR>
        <TR>
           <TD nowrap width="180">N° affilie reçu</TD>
            <TD nowrap ><INPUT type="text" name="geNumAffilie" class="disabled" value="<%=viewBean.getGeNumAffilie()%>" readonly></TD>
            <TD></TD>
            <TD nowrap>Prénom</TD>
            <TD nowrap><INPUT type="text" name="gePrenom" class="libelleLongDisabled" value="<%=viewBean.getGePrenom()%>" readonly></TD>
        </TR>
        <TR>
 	        <TD nowrap width="180">N° AVS du conjoint</TD>
            <TD nowrap ><INPUT type="text" name="geNumAvsConjoint" class="disabled" value="<%=viewBean.getGeNumAvsConjoint()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Nom du conjoint</TD>
            <TD nowrap><INPUT type="text" name="geNomConjoint" class="libelleLongDisabled" value="<%=viewBean.getGeNomConjoint()%>" readonly></TD>
        </TR>
        <TR>
           <TD nowrap width="140">Genre affilié</TD>
           <TD nowrap>
     <%--          	<ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=viewBean.getGenreAffilie()%>"
					codeType="CPGENDECIS"
					wantBlank="false"
				/> --%>
			<INPUT type="text" name="libelleGenreAffilie" tabindex="-1" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getGenreAffilie())%>" type="text" class="libelleLongDisabled" readonly>
			<INPUT type="text" name="geGenreAffilie" tabindex="-1" value="<%=viewBean.getGeGenreAffilie()%>" type="text" style="width: 0.6cm" class="numeroCourtDisabled" readonly></TD>
            <TD width="10"></TD>
            <TD nowrap>Prénom du conjoint</TD>
            <TD nowrap><INPUT type="text" name="gePrenomConjoint" class="libelleLongDisabled" value="<%=viewBean.getGePrenomConjoint()%>" readonly></TD>
        </TR>
	 	<TR>
           <TD nowrap width="180">Numéro de Caisse</TD>
            <TD nowrap ><INPUT type="text" name="geNumCaisse" class="libelleDisabled" value="<%=viewBean.getGeNumCaisse()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Nom AFC</TD>
            <TD nowrap><INPUT type="text" name="geNomAFC" class="libelleLongDisabled" value="<%=viewBean.getGeNomAFC()%>" readonly></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Numéro de demande</TD>
            <TD nowrap ><INPUT type="text" name="geNumDemande" class="libelleDisabled" value="<%=viewBean.getGeNumDemande()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Prénom AFC</TD>
 	        <TD nowrap><INPUT type="text" name="gePrenomAFC" class="libelleLongDisabled" value="<%=viewBean.getGePrenomAFC()%>" readonly></TD>
        </TR>
 	    <TR>
 	        <TD nowrap width="180">N° de communication</TD>
            <TD nowrap >
				<INPUT type="text" name="geNumCommunication" class="libelleDisabled" value="<%=viewBean.getGeNumCommunication()%>" readonly>
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Genre taxation</TD>
            <TD nowrap >
		       <INPUT name="libelleGenreTaxation" type="text"
									value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getGenreTaxation())%>"
									class="libelleLongDisabled" readonly></TD>
 	    </TR>
        <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
       <TR>
            <TD nowrap width="180">Année</TD>
            <TD nowrap ><INPUT type="text" name="annee1" class="libelleDisabled" value="<%=viewBean.getAnnee1()%>" readonly></TD>
 	        <TD></TD>
 	        <TD nowrap width="140">Exercice du</TD>
            <TD nowrap >
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

            <TD nowrap height="31" width="25"><INPUT type="hidden" name="idIfd" value='<%=viewBean.getIdIfd()%>'></TD>
         </TR>
         <TR>
            <TD nowrap width="180">Revenu</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
          	<TD></TD>
            <TD nowrap width="140">Fortune</TD>
            <TD nowrap><INPUT type="text" name="fortune" class="libelleDisabled"
									style="width: 2.45cm" value="<%=viewBean.getFortune()%>" readonly="readonly"></TD>
         </TR>
        <TR>
            <TD nowrap width="140">Capital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
            <TD></TD>
            <TD nowrap width="140">Personne non identifiée</TD>
            <TD nowrap><input type="checkbox" name="gePersonneNonIdentifiee" <%=(viewBean.getGePersonneNonIdentifiee().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
       	</TR>
         <TR>
            <TD nowrap width="180"></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
            <TD nowrap width="140"></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Imposition à la source</TD>
            <TD nowrap><input type="checkbox" name="geImpotSource" <%=(viewBean.getGeImpotSource().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
            <TD nowrap>Non assujetti à l'IBO</TD>
            <TD nowrap><input type="checkbox" name="geNonAssujettiIBO" <%=(viewBean.getGeNonAssujettiIBO().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
        </TR>
         <TR>
            <TD nowrap width="180">Taxation office</TD>
            <TD nowrap><input type="checkbox" name="geTaxationOffice" <%=(viewBean.getGeTaxationOffice().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
 	        <TD nowrap width="140">Non assujetti à l'IFD</TD>
            <TD nowrap><input type="checkbox" name="geNonAssujettiIFD" <%=(viewBean.getGeNonAssujettiIFD().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	       <TD nowrap width="180">Imposition selon dépense</TD>
            <TD nowrap><input type="checkbox" name="geImpositionSelonDepense" <%=(viewBean.getGeImpositionSelonDepense().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Pas d'activité de déclarée</TD>
            <TD nowrap><input type="checkbox" name="gePasActiviteDeclaree" <%=(viewBean.getGePasActiviteDeclaree().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Pension</TD>
            <TD nowrap><input type="checkbox" name="gePension" <%=(viewBean.getGePension().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Pension Alimentaire</TD>
            <TD nowrap><input type="checkbox" name="gePensionAlimentaire" <%=(viewBean.getGePensionAlimentaire().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Rente vieillesse</TD>
           <TD nowrap><input type="checkbox" name="geRenteVieillesse" <%=(viewBean.getGeRenteVieillesse().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Rente viagère</TD>
            <TD nowrap><input type="checkbox" name="geRenteViagere" <%=(viewBean.getGeRenteViagere().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Rente invalidité</TD>
           <TD nowrap><input type="checkbox" name="geRenteInvalidite" <%=(viewBean.getGeRenteInvalidite().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Indémnité Journalière</TD>
            <TD nowrap><input type="checkbox" name="geIndemniteJournaliere" <%=(viewBean.getGeIndemniteJournaliere().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Retraite</TD>
            <TD nowrap><input type="checkbox" name="geRetraite" <%=(viewBean.getGeRetraite().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Bourses</TD>
            <TD nowrap><input type="checkbox" name="geBourses" <%=(viewBean.getGeBourses().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
        <TR>
            <TD nowrap width="140">Divers</TD>
            <TD nowrap><INPUT type="checkbox" name="geDivers" <%=(viewBean.getGeDivers().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
            <TD nowrap>Explications</TD>
           	<TD nowrap>
           		<INPUT  name="geExplicationsDivers" type="text" maxlength="50"
						value="<%=viewBean.getGeExplicationsDivers()%>"
						readonly
						class="libelleLongDisabled"></TD>
         </TR>
         <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
  	   	<TR>
  	   		<TD nowrap width="140">Date transfert MAD</TD>
    <%--         <TD nowrap><ct:FWCalendarTag name="geDateTransfertMAD"
									value="<%=viewBean.getGeDateTransfertMAD()%>"
									errorMessage="la date de début est incorrecte"
									doClientValidation="CALENDAR" /></TD> --%>
			<TD nowrap><INPUT type="text" name="geDateTransfertMAD" value="<%=viewBean.getGeDateTransfertMAD()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
            <TD width="10"></TD>
            <TD nowrap width="140">Date de reception</TD>
            <TD nowrap>
				<ct:FWCalendarTag name="dateRetour"
				value="<%=viewBean.getDateRetour()%>"
				doClientValidation="CALENDAR"
		 		/>
	     	</TD>
        </TR>
        <TR>
        <TD nowrap width="140">Observations</TD>
	        <TD colspan="4"><TEXTAREA rows="3" cols="94" name="geObservations"><%=viewBean.getGeObservations()%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Génération</TD>
            <TD nowrap><input type="checkbox" name="generation" <%=(viewBean.getGeneration().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD></TD>
            <TD nowrap>
            	<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.chercher" crud="r">
            	<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher<%=hasJournal?"&idJournalRetour="+forIdJournalRetour:""%>" tabindex="-1" class="external_link">Retour à la liste des communications</A>
            	</ct:ifhasright>
            	<br>
			</TD>
			<TD>
			<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdAffiliation())) {%>
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdTiers()%>&selectedId2=<%=viewBean.getIdAffiliation()%>" class="external_link">Décisions</A>
				</ct:ifhasright>
			<%}%>
			</TD>
        </TR>

	    <TR>
            <TD nowrap width="140"><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap><INPUT type="hidden" name="allComm" value='<%=request.getParameter("allComm")%>'></TD>
        </TR>

       <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getIdRetour()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>