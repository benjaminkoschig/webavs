<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationPlausibilite"%>
<%@page import="globaz.phenix.db.communications.CPCommunicationPlausibiliteManager"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
if(session.getAttribute("bouton")!=null){
	if(session.getAttribute("bouton")=="false"){
		bButtonUpdate = false;
		bButtonDelete = false;
	}
}
idEcran="CCP1019";
globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
bButtonDelete = journal.canDeleteCommunication(bButtonDelete);
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
top.document.title = "Beitrag - Detail Steuerangaben"
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
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Steuerdaten<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
         <TR>
            <TD nowrap width="180">Versicherter</TD>
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
            <TD nowrap width="50" align="left">Mitglied</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
            <TD nowrap width="180"></TD>
            <TD nowrap ><INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly></TD>
	        <TD><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
            <TD nowrap align="left">SVN</TD>
            <TD nowrap>
				<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     	</TD>
	     	<TD>
	     		<INPUT type="hidden" name="idCommunication" value="<%=viewBean.getIdCommunication()%>">
	     	</TD>
        </TR>
        <TR>
             <TD nowrap width="140">DBST Periode-Nr.</TD>
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
            <TD nowrap align="left">Steuer-Nr.</TD>
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
 	        <TD nowrap width="180">Erhaltene Steuer-Nr.</TD>
            <TD nowrap >
				<INPUT type="text" name="geNumContribuable" class="disabled" value="<%=viewBean.getGeNumContribuable()%>" readonly >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Name</TD>
            <TD nowrap><INPUT type="text" name="geNom" class="libelleLongDisabled" value="<%=viewBean.getGeNom()%>" readonly></TD>
        </TR>
        <TR>
           <TD nowrap width="180">Erhaltene Mitglied-Nr.</TD>
            <TD nowrap ><INPUT type="text" name="geNumAffilie" class="disabled" value="<%=viewBean.getGeNumAffilie()%>" readonly></TD>
            <TD></TD>
            <TD nowrap>Prénom</TD>
            <TD nowrap><INPUT type="text" name="gePrenom" class="libelleLongDisabled" value="<%=viewBean.getGePrenom()%>" readonly></TD>
        </TR>
        <TR>
 	        <TD nowrap width="180">SVN des Ehepartners</TD>
            <TD nowrap ><INPUT type="text" name="geNumAvsConjoint" class="disabled" value="<%=viewBean.getGeNumAvsConjoint()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Name des Ehepartners</TD>
            <TD nowrap><INPUT type="text" name="geNomConjoint" class="libelleLongDisabled" value="<%=viewBean.getGeNomConjoint()%>" readonly></TD>
        </TR>
        <TR>
           <TD nowrap width="140">Erfassungsgrund</TD>
           <TD nowrap>
     <%--          	<ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=viewBean.getGenreAffilie()%>"
					codeType="CPGENDECIS"
					wantBlank="false"
				/> --%>
			<INPUT type="text" name="libelleGenreAffilie" tabindex="-1" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getGenreAffilie())%>" type="text" class="libelleLongDisabled" readonly>
			<INPUT type="text" name="geGenreAffilie" tabindex="-1" value="<%=viewBean.getGeGenreAffilie()%>" type="text" style="width: 0.6cm" class="numeroCourtDisabled" readonly></TD>
            <TD width="10"></TD>
            <TD nowrap>Vorname des Ehepartners</TD>
            <TD nowrap><INPUT type="text" name="gePrenomConjoint" class="libelleLongDisabled" value="<%=viewBean.getGePrenomConjoint()%>" readonly></TD>
        </TR>
	 	<TR>
           <TD nowrap width="180">Kassen-Nr.</TD>
            <TD nowrap ><INPUT type="text" name="geNumCaisse" class="libelleDisabled" value="<%=viewBean.getGeNumCaisse()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Name ESTV</TD>
            <TD nowrap><INPUT type="text" name="geNomAFC" class="libelleLongDisabled" value="<%=viewBean.getGeNomAFC()%>" readonly></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Antragsnummer</TD>
            <TD nowrap ><INPUT type="text" name="geNumDemande" class="libelleDisabled" value="<%=viewBean.getGeNumDemande()%>" readonly></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Vorname ESTV</TD>
 	        <TD nowrap><INPUT type="text" name="gePrenomAFC" class="libelleLongDisabled" value="<%=viewBean.getGePrenomAFC()%>" readonly></TD>
        </TR>
 	    <TR>
 	        <TD nowrap width="180">Mitteilungsnummer</TD>
            <TD nowrap >
				<INPUT type="text" name="geNumCommunication" class="libelleDisabled" value="<%=viewBean.getGeNumCommunication()%>" readonly>
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Steuererhebungsart</TD>
            <TD nowrap >
		       <INPUT name="libelleGenreTaxation" type="text"
									value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getGenreTaxation())%>"
									class="libelleLongDisabled" readonly></TD>
 	    </TR>
        <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
       <TR>
            <TD nowrap width="180">Jahr</TD>
            <TD nowrap ><INPUT type="text" name="annee1" class="libelleDisabled" value="<%=viewBean.getAnnee1()%>" readonly></TD>
 	        <TD></TD>
 	        <TD nowrap width="140">Periode von</TD>
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
            <TD nowrap width="180">Einkommen</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
          	<TD></TD>
            <TD nowrap width="140">Vermögen</TD>
            <TD nowrap><INPUT type="text" name="fortune" class="libelleDisabled"
									style="width: 2.45cm" value="<%=viewBean.getFortune()%>" readonly="readonly"></TD>
         </TR>
        <TR>
            <TD nowrap width="140">Kapital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
            <TD></TD>
            <TD nowrap width="140">nicht identifizierte Person</TD>
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
            <TD nowrap width="140">Quellensteuer</TD>
            <TD nowrap><input type="checkbox" name="geImpotSource" <%=(viewBean.getGeImpotSource().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
            <TD nowrap>dem IBO nicht unterstellt</TD>
            <TD nowrap><input type="checkbox" name="geNonAssujettiIBO" <%=(viewBean.getGeNonAssujettiIBO().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
        </TR>
         <TR>
            <TD nowrap width="180">Erfassung von Amtes wegen</TD>
            <TD nowrap><input type="checkbox" name="geTaxationOffice" <%=(viewBean.getGeTaxationOffice().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
 	        <TD nowrap width="140">dem DBG nicht unterstellt</TD>
            <TD nowrap><input type="checkbox" name="geNonAssujettiIFD" <%=(viewBean.getGeNonAssujettiIFD().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	       <TD nowrap width="180">Auflage nach Ausgaben</TD>
            <TD nowrap><input type="checkbox" name="geImpositionSelonDepense" <%=(viewBean.getGeImpositionSelonDepense().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">keine angemeldete Geschäftstätigkeit</TD>
            <TD nowrap><input type="checkbox" name="gePasActiviteDeclaree" <%=(viewBean.getGePasActiviteDeclaree().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Rente</TD>
            <TD nowrap><input type="checkbox" name="gePension" <%=(viewBean.getGePension().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Unterhaltsgeld</TD>
            <TD nowrap><input type="checkbox" name="gePensionAlimentaire" <%=(viewBean.getGePensionAlimentaire().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Altersrente</TD>
           <TD nowrap><input type="checkbox" name="geRenteVieillesse" <%=(viewBean.getGeRenteVieillesse().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Leibrente</TD>
            <TD nowrap><input type="checkbox" name="geRenteViagere" <%=(viewBean.getGeRenteViagere().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Invalidenrente</TD>
           <TD nowrap><input type="checkbox" name="geRenteInvalidite" <%=(viewBean.getGeRenteInvalidite().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">IV-Taggeld</TD>
            <TD nowrap><input type="checkbox" name="geIndemniteJournaliere" <%=(viewBean.getGeIndemniteJournaliere().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Vorpension</TD>
            <TD nowrap><input type="checkbox" name="geRetraite" <%=(viewBean.getGeRetraite().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	        <TD width="10"></TD>	
 	        <TD nowrap width="140">Börse</TD>
            <TD nowrap><input type="checkbox" name="geBourses" <%=(viewBean.getGeBourses().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
 	    </TR>
        <TR>
            <TD nowrap width="140">Diverses</TD>
            <TD nowrap><INPUT type="checkbox" name="geDivers" <%=(viewBean.getGeDivers().booleanValue())? "checked" : "unchecked"%> readonly="readonly" disabled="disabled"></TD>
            <TD width="10"></TD>
            <TD nowrap>Erklärungen</TD>
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
  	   		<TD nowrap width="140">Datum Übertragung MAD</TD>
    <%--         <TD nowrap><ct:FWCalendarTag name="geDateTransfertMAD"
									value="<%=viewBean.getGeDateTransfertMAD()%>"
									errorMessage="la date de début est incorrecte"
									doClientValidation="CALENDAR" /></TD> --%>
			<TD nowrap><INPUT type="text" name="geDateTransfertMAD" value="<%=viewBean.getGeDateTransfertMAD()%>" class="libelleDisabled" style="width : 2.45cm;" readonly></TD>
            <TD width="10"></TD>
            <TD nowrap width="140">Empfangsdatum</TD>
            <TD nowrap>
				<ct:FWCalendarTag name="dateRetour"
				value="<%=viewBean.getDateRetour()%>"
				doClientValidation="CALENDAR"
		 		/>
	     	</TD>
        </TR>
        <TR>
        <TD nowrap width="140">Beobachtungen</TD>
	        <TD colspan="4"><TEXTAREA rows="3" cols="94" name="geObservations"><%=viewBean.getGeObservations()%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Generierung</TD>
            <TD nowrap><input type="checkbox" name="generation" <%=(viewBean.getGeneration().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD></TD>
            <TD nowrap>
            	<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.chercher" crud="r">
            	<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher<%=hasJournal?"&idJournalRetour="+forIdJournalRetour:""%>" tabindex="-1" class="external_link">Zurück zur Steuermeldungsliste</A>
            	</ct:ifhasright>
            	<br>
			</TD>
			<TD>
			<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdAffiliation())) {%>
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdTiers()%>&selectedId2=<%=viewBean.getIdAffiliation()%>" class="external_link">Verfügungen</A>
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
        <TR>
         	<TD COLSPAN="5">
				<TABLE BORDER="2">
					<%
					CPCommunicationPlausibiliteManager manager = viewBean.getErreursPlausi();
						for (int i=0; i<manager.size(); i++) {
							CPCommunicationPlausibilite plausi = (CPCommunicationPlausibilite) manager.get(i);
							if(i==0){
						%>
						<THEAD>
						<TR>
							<TH width="15">Typ</TH>
							<TH colspan="3" width="1000"  >Mitteilung</TH>
						</TR>
						</THEAD>
						<TBODY>
						<%} %>
							<TR>
								<TD  width="15" ><IMG src='<%=viewBean.getLogImage(request, plausi.getIdPlausibilite(), objSession)%>' ></TD>
								<TD width="1000"><%=plausi.getLibellePlausibilite( objSession,(ICommunicationRetour)viewBean)%></TD>
							</TR>
						<% }%>
					<% if(manager.size()>0){ %>
						</TBODY>
					<%} %>	
				</TABLE>
			</TD>
         </TR>
         <%if(viewBean.getLastUser().length()>0){%>
			<TR>
         		<TD><%=viewBean.getLastUser()+"-"+viewBean.getLastDate()+"-"+viewBean.getLastTime()%></TD>
         	</TR>
        <% }%>
       <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-communicationFiscaleRetour" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getIdRetour()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>