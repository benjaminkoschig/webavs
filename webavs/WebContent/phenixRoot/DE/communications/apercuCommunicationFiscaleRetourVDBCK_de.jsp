<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1017";
CPCommunicationFiscaleRetourVDViewBean viewBean = (CPCommunicationFiscaleRetourVDViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean-idRetour"+viewBean.getIdRetour();
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

function retourOriginale() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.retournerOriginale";
		document.forms[0].submit();
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Steuerangaben<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						
<TR>
	<TD><a  onclick="retourOriginale()" href="#">Ecraser les données avec originale (irréversible)</a></TD>
</TR>
          <TR>
            <TD nowrap width="180">Versicherter</TD>
            <TD nowrap >
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNomPrenom()%>" class="libelleLongDisabled" readonly>
		<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
		<%--	<%
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
            <TD nowrap >
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
	
            </TD>
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
            <TD nowrap>Erhaltene Mitglied-Nr.</TD>
            <TD nowrap><INPUT type="text" name="vdNumAffilie"
									class="libelleLongDisabled" value="<%=viewBean.getVdNumAffilie()%>" readonly></TD>
        </TR>
	 	<TR>
           <TD nowrap width="180">Kassen-Nr.</TD>
            <TD nowrap >
            	<INPUT type="text" name="vdNumCaisse" class="libelleLong" value="<%=viewBean.getVdNumCaisse()%>">
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Erhaltene Name, Vorname</TD>
           <TD nowrap >
				<INPUT type="text" name="vdNomPrenom" class="libelleLongDisabled" value="<%=viewBean.getVdNomPrenom()%>" readonly>
 	        </TD>    
 	    </TR>
 	    <TR>
           <TD nowrap width="180">Anfrage-Nr.</TD>
            <TD nowrap >
            	<INPUT type="text" name="vdNumDemande" class="libelleLong" value="<%=viewBean.getVdNumDemande()%>">
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Erhaltene SVN</TD>
           <TD nowrap >
           		<INPUT type="text" name="vdNumAvs" class="libelleLongDisabled" value="<%=viewBean.getVdNumAvs()%>" readonly>
 	        </TD>    
 	    </TR>
        <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
             <TD nowrap width="140">Erhaltene Steuer-Nr.</TD>
           <TD nowrap >
           		<INPUT type="text" name="vdNumContribuable" class="libelleLongDisabled" value="<%=viewBean.getVdNumContribuable()%>" readonly>
 	        </TD>    
        </TR>
         <TR>
            <TD nowrap width="180">Einkommen 1</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" style="width : 2.45cm;"></TD>
          	<TD></TD>
            <TD nowrap width="140">Einkommen 2</TD>
            <TD nowrap><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" style="width : 2.45cm;"></TD>
         </TR>
         <TR>
            <TD nowrap width="140">Kapital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
       	</TR>
       	<TR>
            <TD nowrap width="140">Vermögen</TD>
            <TD nowrap><INPUT type="text" name="fortune" value="<%=viewBean.getFortune()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
       	</TR>
 	    <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
 	    
 	    <TR>
            <TD nowrap width="140">Beginnperiode</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDebPeriode" value="<%=viewBean.getVdDebPeriode()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Endperiode</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdFinPeriode" value="<%=viewBean.getVdFinPeriode()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Adresse c/o</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdAddChez" value="<%=viewBean.getVdAddChez()%>" class="libelleLong" ></TD>
            <TD></TD>
            <TD nowrap>Adresse Strasse</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdAddRue" value="<%=viewBean.getVdAddRue()%>" class="libelleLong" ></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">PLZ</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdNumLocalite" value="<%=viewBean.getVdNumLocalite()%>" class="libelleLong"></TD>
            <TD></TD>
            <TD nowrap>Geburtsdatum</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDatNaissance" value="<%=viewBean.getVdDatNaissance()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Steuerpflicht des Steuerzahlers</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdAssujCtb" value="<%=viewBean.getVdAssujCtb()%>" class="libelleShort" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Erhaltene Alimente</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdPenAliObtenue" value="<%=viewBean.getVdPenAliObtenue()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Wohnrecht</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDroitHabitation" value="<%=viewBean.getVdDroitHabitation()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Type taxation</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdTypeTax" value="<%=viewBean.getVdTypeTax()%>" class="libelleShort" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Anfang Steuerpflicht</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDebAssuj" value="<%=viewBean.getVdDebAssuj()%>" class="libelleShort" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Ende Steuerpflicht</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdFinAssuj" value="<%=viewBean.getVdFinAssuj()%>" class="libelleShort" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Anlagekapital</TD>
            <TD nowrap><INPUT type="text" name="vdCapInvesti" value="<%=viewBean.getVdCapInvesti()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Entscheidendes Datum Anlagekapital</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDatDetCapInvesti" value="<%=viewBean.getVdDatDetCapInvesti()%>" class="libelleShort" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Einkommen selbständige Erwerbstätigkeit</TD>
            <TD nowrap><INPUT type="text" name="vdRevActInd" value="<%=viewBean.getVdRevActInd()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>G.I. geschäftlich</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdGIprof" value="<%=viewBean.getVdGIprof()%>" class="libelleShort" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Übermass Liquidität</TD>
            <TD nowrap><INPUT type="text" name="vdExcesLiquidite" value="<%=viewBean.getVdExcesLiquidite()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Gehalt ausgezahlt Ehepartner</TD>
            <TD nowrap><INPUT type="text" name="vdSalVerseCjt" value="<%=viewBean.getVdSalVerseCjt()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Steuermeldungsart</TD>
            <TD nowrap><INPUT type="text" name="vdNatureComm" value="<%=viewBean.getVdNatureComm()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Entrichtendes Gehalt</TD>
            <TD nowrap><INPUT type="text" name="vdSalaireCotisant" value="<%=viewBean.getVdSalaireCotisant()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Steuerbares Vermögen</TD>
            <TD nowrap><INPUT type="text" name="vdFortuneImposable" value="<%=viewBean.getVdFortuneImposable()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap>Massgebendest Datum Vermögen</TD>
            <TD nowrap><INPUT type="text" name="vdDateDetFortune" value="<%=viewBean.getVdDateDetFortune()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Betrag Rente</TD>
            <TD nowrap><INPUT type="text" name="vdMontantRente" value="<%=viewBean.getVdMontantRente()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap width="140">Code Rentenart</TD>
            <TD nowrap><INPUT type="text" name="vdCodeNatureRente" value="<%=viewBean.getVdCodeNatureRente()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Erwerbeseinkommen</TD>
            <TD nowrap><INPUT type="text" name="vdRevenuActivitesLucratives" value="<%=viewBean.getVdRevenuActivitesLucratives()%>" class="montant" style="width : 2.45cm;"></TD>            
            <TD></TD>
            <TD nowrap>Netto Einkommen</TD>
            <TD nowrap><INPUT type="text" name="vdRevenuNet" value="<%=viewBean.getVdRevenuNet()%>" class="montant" style="width : 2.45cm;"></TD>
       	</TR>
       	
       	<TR>
            <TD nowrap width="140">Kommentar</TD>
            <TD COLSPAN=4><TEXTAREA rows="3" name="vdCommentaire" style="width : 21.00cm;"><%=viewBean.getVdCommentaire()%></TEXTAREA></TD>
       	</TR>
 	    
 	    <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
        <TR>
            <TD nowrap width="140">Übermitteltete Mitgliedsart</TD>
            <TD nowrap><INPUT name="vdGenreAffilie" type="text" tabindex="-1" value="<%=viewBean.getVdGenreAffilie()%>" class="libelle"></TD>
            <TD width="10"></TD>
            <TD nowrap>Antragsdatum</TD>
            <TD nowrap><INPUT align="left" type="text" name="vdDateDemande" value="<%=viewBean.getVdDateDemande()%>" class="libelleShort" style="width : 2.45cm;"></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Empfangsdatum</TD>
            <TD nowrap>
				<ct:FWCalendarTag name="dateRetour" 
				value="<%=viewBean.getDateRetour()%>" 
				doClientValidation="CALENDAR"
		 		/>
	     	</TD>
        </TR>
        <TR>
            <TD nowrap width="140">Status</TD>
            <TD nowrap><INPUT name="etat" tabindex="-1" type="text" value="<%=viewBean.getVisibleStatus()%>" class="libelleLongDisabled" readonly></TD>
        </TR>
        <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
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
			<TD></TD>
        </TR>
	    <TR>
            <TD nowrap width="140"><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
            <TD nowrap><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
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