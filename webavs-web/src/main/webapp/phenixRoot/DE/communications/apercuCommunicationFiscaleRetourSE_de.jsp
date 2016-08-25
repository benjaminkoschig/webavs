<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationPlausibilite"%>
<%@page import="globaz.phenix.db.communications.CPCommunicationPlausibiliteManager"%>
<%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@page import="globaz.phenix.translation.CodeSystem"%>
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
globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
bButtonDelete = journal.canDeleteCommunication(bButtonDelete);
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean-idRetour"+viewBean.getIdRetour();
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
top.document.title = "Beiträge - Steuermeldungsdaten"
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
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.modifierCustom";
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

function afficherDonneesPrivees() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesPrivees";
		document.forms[0].submit();
}

function afficherDonneesCommunication() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommunication";
		document.forms[0].submit();
}

function afficherContribuable() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherContribuable";
		document.forms[0].submit();
}

function afficherDonneesBase() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesBase";
		document.forms[0].submit();
}

function afficherDonneesCommerciales() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommerciales";
		document.forms[0].submit();
}

function afficherConjoint() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherConjoint";
		document.forms[0].submit();
}
function afficherRenteAVSWIRR() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherRenteAVSWIRR";
	document.forms[0].submit();
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Steuermeldungsdaten<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<%
		boolean coupleNonActif = false;
		String infoRevenu = "(Einkommen ohne Landwirtschaftliches Einkommen)";
		if(CPDecision.CS_NON_ACTIF.equalsIgnoreCase(viewBean.getGenreAffilie())){
			infoRevenu = "";
		}
		if(CPDecision.CS_NON_ACTIF.equalsIgnoreCase(viewBean.getGenreAffilie()) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(viewBean.getGenreConjoint())){
			coupleNonActif = true;
			infoRevenu = "(mit Ehepartnerrenten)";
		}
		if ((JadeStringUtil.isIntegerEmpty(viewBean.getGenreAffilie()) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(viewBean
				.getGenreConjoint()))
				|| (JadeStringUtil.isIntegerEmpty(viewBean.getGenreConjoint()) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(viewBean
						.getGenreAffilie()))) {
			infoRevenu = "(mit Ehepartnerrenten)";
		}
		%>
		<div id="tabs"> 
		<ul>
  			<li><a class="selected">Steuermeldungsdaten</a></li>
  			<li><a onclick="afficherContribuable()" href="#">Steuer</a></li>
  			<li><a onclick="afficherDonneesBase()" href="#">Basisdaten</a></li>
  			<li><a onclick="afficherDonneesPrivees()" href="#">Privatdaten</a></li>
  			<li><a onclick="afficherDonneesCommerciales()" href="#">Geschaeftsdaten</a></li>
  			<li><a onclick="afficherConjoint()" href="#">Ehepartner</a></li>
  			<li><a onclick="afficherRenteAVSWIRR()" href="#">AHV Einkommen</a></li> 	
		</ul>
		</div>
		
          <TR>
            <TD nowrap width="120">Partner</TD>
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
       	      <TD nowrap >
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
		</TD>
	    </TR>
		<TR>
		  <TD nowrap>Steuer-Nr.</TD>
            <TD nowrap><INPUT type="text" name="numContribuable"
									class="libelleLong" value="<%=viewBean.getNumContribuable()%>"></TD>
        
            <TD></TD>
            <TD nowrap></TD>
	       	<TD>
	     	 <INPUT type="hidden" name="idCommunication" value="<%=viewBean.getIdCommunication()%>">
	     </TD>
        </TR>
        	 <TR>
				<TD nowrap  height="11" colspan="4">
       				<hr size="3" width="100%">
     			</TD>
			</TR>
           <TR>
	  	<TR>
           <TD nowrap>Periode DBST-Nr.</TD>
            <TD nowrap><INPUT type="text" name="numIfd" class="numeroCourtDisabled" value="<%=viewBean.getPeriodeFiscaleAffichage().getNumIfd()%>" readonly>

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
            <INPUT name="annee" type="text" value="<%=viewBean.getAnnee1()%>" class="numeroCourtDisabled"  style="width : 2.45cm;" readonly>
            </TD>
            <TD></TD>
            <TD></TD>
             </TR>
         <TR>
            <TD nowrap>&nbsp;</TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
         </TR>
      
        </TR>
             <TR style="font-size: 16; font-style: italic;">
            <TD nowrap></TD>
            <TD>Steuer</TD>
            <TD></TD>
            <TD>Ehepartner</TD>
         </TR>
          <TR>
            <TD nowrap>&nbsp;</TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
         </TR>
          <TR>
              <TD nowrap align="left">Mitglied</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
            <TD></TD>
             <%if (viewBean.getAffiliationConjoint()!=null) {%>
            <TD><INPUT type="text" name="numAffilieCjt" tabindex="-1" value="<%=viewBean.getAffiliationConjoint().getAffilieNumero()%>" class="libelleLongDisabled" readonly></TD>
            <%} else {%>
             <TD><INPUT type="text" name="numAffilieCjt" tabindex="-1" value="" class="libelleLongDisabled" readonly></TD>
            <%}%>
	     </TR>
	      <TR>
             <TD nowrap align="left">SVN</TD>
          	<%if (viewBean.getTiers()!=null) {%>
     	    <TD><INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getTiers().getNumAvsActuel()%>" class="libelleLongDisabled" readonly></TD>
            <%} else {%>
            <TD><INPUT type="text" name="numAvs" tabindex="-1" value="" class="libelleLongDisabled" readonly></TD>
            <%}%>
	        <TD></TD>
            <%if (viewBean.getConjoint()!=null) {%>
            <TD><INPUT type="text" name="numAvsCjt" tabindex="-1" value="<%=viewBean.getConjoint().getNumAvsActuel()%>" class="libelleLongDisabled" readonly></TD>
            <%} else {%>
            <TD><INPUT type="text" name="numAvsCjt" tabindex="-1" value="" class="libelleLongDisabled" readonly></TD>
            <%}%>
            
	     </TR>
	      <TR>
             <TD nowrap align="left">Mitgliedsart</TD>
          	<%if (viewBean.getTiers()!=null) {%>
     	    <TD><INPUT type="text" name="genreDecision" tabindex="-1" value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getGenreAffilie())%>" class="libelleLongDisabled" readonly></TD>
            <%} else {%>
            <TD><INPUT type="text" name="genreDecision" tabindex="-1" value="" class="libelleLongDisabled" readonly></TD>
            <%}%>
	        <TD></TD>
            <%if (viewBean.getConjoint()!=null) {%>
            <TD><INPUT type="text" name="genreDecisionCjt" tabindex="-1" value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getGenreConjoint())%>" class="libelleLongDisabled" readonly></TD>
            <%} else {%>
            <TD><INPUT type="text" name="genreDecisionCjt" tabindex="-1" value="" class="libelleLongDisabled" readonly></TD>
            <%}%>
            
	     </TR>
	     
	     
	      <TR>
            <TD nowrap>&nbsp;</TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
         </TR>
         <TR>
            <TD nowrap>Einkommen/Renten</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"> <%=infoRevenu%> </TD>
          	<TD></TD>
          	<% if (!coupleNonActif) { %>
            	<TD nowrap><INPUT name="revenu1Cjt" type="text" value="<%=viewBean.getRevenuNAConjoint()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"></TD>
            <% } else { %>
            	<TD nowrap></TD>
            <% }%> 
         </TR>
         <TR>
            <TD nowrap>Landwirtschaftliches Einkommen</TD>
            <TD nowrap><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montantDisabled" readonly="readonly"style="width : 2.45cm;"></TD>
          	<TD></TD>
          	<% if (!coupleNonActif) { %>
            	<TD nowrap><INPUT name="revenu2Cjt" type="text" value="<%=viewBean.getRevenuAConjoint()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"></TD>
            <% } else { %>
            	<TD nowrap></TD>
            <% }%> 
         </TR>
         <TR>
            <TD nowrap>Investiertes Eigenkapital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"></TD>
            <TD></TD>
            <% if (!coupleNonActif) { %>
            	<TD nowrap><INPUT type="text" name="capitalCjt" value="<%=viewBean.getCapitalEntrepriseConjoint()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"></TD>
            <% } else { %>
            	<TD nowrap></TD>
            <% }%> 
       	</TR>
       	   <TR>
            <TD nowrap>&nbsp;</TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
         </TR>
       	<TR>
            <TD nowrap>Vermögen</TD>
            <TD nowrap><INPUT type="text" name="fortune" value="<%=viewBean.getFortune()%>" class="montantDisabled" readonly="readonly" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap></TD>
       	</TR>
 	    <TR>
 	    	<TD colspan="4"><HR></TD>
 	    </TR>

        <TR>
            <TD nowrap>Empfangsdatum</TD>
            <TD nowrap>
				<ct:FWCalendarTag name="dateRetour" 
				value="<%=viewBean.getDateRetour()%>" 
				doClientValidation="CALENDAR"
		 		/>
	     	</TD>
        </TR>
        <TR>
            <TD nowrap>&nbsp;</TD>
            <TD> </TD>
            <TD></TD>
            <TD></TD>
         </TR>
        <TR>
            <TD nowrap>
            	<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.chercher" crud="r">
            	<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher<%=hasJournal?"&idJournalRetour="+forIdJournalRetour:""%>" tabindex="-1" class="external_link">Retour à la liste des communications</A>
            	</ct:ifhasright>
            	<br>
			</TD>
			<TD>
			<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdAffiliation())) {%> 
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdTiers()%>&selectedId2=<%=viewBean.getIdAffiliation()%>" class="external_link">Steuersverfügungs</A>
				</ct:ifhasright>
			<%}%>&nbsp;
			<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdAffiliationConjoint())) {%> 
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdConjoint()%>&selectedId2=<%=viewBean.getIdAffiliationConjoint()%>" class="external_link">Ehepartnersverfügungs</A>
				</ct:ifhasright>
			<%}%>
			</TD>
			<TD></TD>
        </TR>
      
	    <TR>
            <TD nowrap><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
            <TD nowrap><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
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
							<TH width="15">Type</TH>
							<TH colspan="3" width="1000"  >Message</TH>
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