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
idEcran="CCP1017";
globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
bButtonDelete = journal.canDeleteCommunication(bButtonDelete);
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean-idRetour"+viewBean.getIdRetour();
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
			<%-- tpl:put name="zoneTitle" --%>Steuerangaben<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
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
			    <INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs(1)%>" class="libelleLongDisabled" readonly>
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

            <TD nowrap>Steuer-Nr.</TD>
            <TD nowrap><INPUT type="text" name="numContribuable"
									class="libelleLong" value="<%=viewBean.getNumContribuable()%>"></TD>
        </TR>
	 	<TR>
           <TD nowrap width="180"></TD>
            <TD nowrap >
 	        </TD>
 	        <TD nowrap width="140">erhaltene Steuer-Nr.</TD>
           <TD nowrap >
				<INPUT type="text" name="juNumContribuable" class="libelleLong" value="<%=viewBean.getJuNumContribuable()%>">
 	        </TD>    
 	    </TR>
 	    
 	    <TR>
 	        <TD nowrap width="180">Job</TD>
            <TD nowrap >
				<INPUT type="text" name="juLot" class="numeroCourt" value="<%=viewBean.getJuLot()%>">
 	        </TD>    
 	        <TD nowrap width="140">Mitgliedsart</TD>
            <TD nowrap>
               	<ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=viewBean.getGenreAffilie()%>"
					codeType="CPGENDECIS"
					wantBlank="false"
				/>
            </TD>
        </TR>
        <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
            <TD nowrap width="180">Anzahl Tage 1</TD>
            <TD nowrap >
				<INPUT type="text" name="juNbrJour1" class="numeroCourt" value="<%=viewBean.getJuNbrJour1()%>">
 	        </TD>
 	        <TD nowrap width="140">Anzahl Tage 2</TD>
            <TD nowrap >
				<INPUT type="text" name="juNbrJour2" class="numeroCourt" value="<%=viewBean.getJuNbrJour2()%>">
 	        </TD>
      		
            <TD nowrap height="31" width="25"><INPUT type="hidden" name="idIfd" value='<%=viewBean.getIdIfd()%>'></TD>
         </TR>
         <TR>
            <TD nowrap width="180">Einkommen 1</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap width="140">Einkommen 2</TD>
            <TD nowrap><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" style="width : 2.45cm;"></TD>
         </TR>
         <TR>
            <TD nowrap width="140">Kapital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
       	</TR>
       	<TR>
            <TD nowrap width="140">Vermögen</TD>
            <TD nowrap><INPUT type="text" name="fortune" value="<%=viewBean.getFortune()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
       	</TR>
 	    <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
        <TR>
            <TD nowrap width="140">Übermittelte Mitgliedsart</TD>
            <TD nowrap><INPUT name="juGenreAffilie" type="text" tabindex="-1" value="<%=viewBean.getJuGenreAffilie()%>" class="libelle"></TD>
            <TD nowrap width="140">Einschätzungsart</TD>
            <TD nowrap><INPUT name="juGenreTaxation" type="text" tabindex="-1" value="<%=viewBean.getJuGenreTaxation()%>" class="libelle"></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Geburtsdatum</TD>
            <TD nowrap><INPUT name="juDateNaissance" type="text" tabindex="-1" value="<%=viewBean.getJuDateNaissance()%>" class="libelle"></TD>
            <TD nowrap width="140">Eingangsdatum</TD>
            <TD nowrap>
				<ct:FWCalendarTag name="dateRetour" 
				value="<%=viewBean.getDateRetour()%>" 
				doClientValidation="CALENDAR"
		 		/>
	     	</TD>
        </TR>
        <TR>
            <TD nowrap width="140">Neuer Steuer-Nr.</TD>
            <TD nowrap><INPUT name="juNewNumContribuable" type="text" tabindex="-1" value="<%=viewBean.getJuNewNumContribuable()%>" class="libelleLong"></TD>
            <TD nowrap width="140"></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
            <TD nowrap width="140">Status</TD>
            <TD nowrap><INPUT name="etat" tabindex="-1" type="text" value="<%=viewBean.getVisibleStatus()%>" class="libelleLongDisabled" readonly></TD>
  
            <TD nowrap width="140">Fehlende Einschätzung</TD>
            <TD nowrap><input type="checkbox" name="juTaxeMan" <%=(viewBean.getJuTaxeMan().booleanValue())? "checked" : "unchecked"%>></TD>
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
			<TD></TD>
        </TR>
      
	    <TR>
            <TD nowrap width="140"><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
            <TD nowrap><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
            <TD nowrap></TD>
            <TD nowrap><INPUT type="hidden" name="allComm" value='<%=request.getParameter("allComm")%>'></TD>
        </TR>
        <TR>
         	<TD COLSPAN="4">
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