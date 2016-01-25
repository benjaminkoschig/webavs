<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1018";
CPCommunicationFiscaleRetourNEViewBean viewBean = (CPCommunicationFiscaleRetourNEViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean-idRetour"+viewBean.getIdRetour();
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
            <TD nowrap width="50" align="left">Affilié</TD>
            <TD nowrap >
		<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     </TD>
	</TR>
		<TR>
            <TD nowrap width="180"></TD>
            <TD nowrap >
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
	
            <TD></TD>
            <TD nowrap align="left">NSS</TD>
            <TD nowrap>
				<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
	     	</TD>
	     	<TD>
	     		<INPUT type="hidden" name="idCommunication" value="<%=viewBean.getIdCommunication()%>">
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
				<INPUT type="text" name="numContribuableDisplay" class="libelleLongDisabled" readonly value="<%=viewBean.getNumContribuable()%>" >
	     </TD>
          </TR>
	  	<TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap>  <INPUT type="hidden"  name="isForBackup" value="<%=viewBean.isForBackup()%>"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
	 	<TR>
           <TD nowrap width="180">Numéro de Caisse</TD>
            <TD nowrap >
				<INPUT type="text" name="neNumCaisse" class="numeroCourt" value="<%=viewBean.getNeNumCaisse()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Genre affilié</TD>
            <TD nowrap>
               	<ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=viewBean.getGenreAffilie()%>"
					codeType="CPGENDECIS"
					wantBlank="false"
				/>
            </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Numéro de Commune</TD>
            <TD nowrap >
				<INPUT type="text" name="neNumCommune" class="numeroCourt" value="<%=viewBean.getNeNumCommune()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Numéro AVS Client</TD>
            <TD nowrap >
            	<INPUT type="text" name="neNumAvs" tabindex="-1" value="<%=viewBean.getNeNumAvs()%>" class="libelleLongDisabled" readonly>
		       </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Numéro de Client</TD>
            <TD nowrap >
				<INPUT type="text" name="neNumClient" class="numeroLong" value="<%=viewBean.getNeNumClient()%>" >
 	        </TD>    
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">N° contribuable reçu</TD>
            <TD nowrap><INPUT type="text" name="neNumContribuable" class="numeroLong" value="<%=viewBean.getNeNumContribuable()%>">
            </TD>
        </TR>
        <TR>
           <TD nowrap width="180">Numéro BDP</TD>
            <TD nowrap >
				<INPUT type="text" name="neNumBDP" class="numeroLong" value="<%=viewBean.getNeNumBDP()%>" >
 	        </TD> 
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
            <TH colspan="6" align="center">Indépendant</TH>
        </TR>
        <TR></TR>
        <TR>
            <TD nowrap width="180">Année 1</TD>
            <TD nowrap >
				<INPUT type="text" name="annee1" class="numeroCourt" value="<%=viewBean.getAnnee1()%>" >
 	        <TD></TD>
 	        <TD nowrap width="140">Année 2</TD>
            <TD nowrap >
				<INPUT type="text" name="annee2" class="numeroCourt" value="<%=viewBean.getAnnee2()%>" >
 	        <TD></TD>
      		
            <TD nowrap height="31" width="25"><INPUT type="hidden" name="idIfd" value='<%=viewBean.getIdIfd()%>'></TD>
         </TR>
         <TR>
            <TD nowrap width="180">Revenu 1</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" style="width : 2.45cm;"></TD>
          	<TD></TD>
            <TD nowrap width="140">Revenu 2</TD>
            <TD nowrap><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" style="width : 2.45cm;"></TD>
         </TR>
   <!--      <TR>
            <TD nowrap width="180">Cotisation 1</TD>
            <TD nowrap><INPUT name="cotisation1" type="text" value="<%=viewBean.getCotisation1()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap width="140">Cotisation 2</TD>
            <TD nowrap><INPUT name="cotisation2" type="text" value="<%=viewBean.getCotisation2()%>" class="montant" style="width : 2.45cm;"></TD>
         </TR> -->
         <TR>
            <TD nowrap width="140">Capital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
       	</TR>
         <TR> 
            <TD nowrap width="180">Exercice1 du</TD>
            <TD nowrap>
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
            <TD nowrap></TD>
            <TD nowrap width="140">Exercice2 du</TD>
            <TD nowrap>
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
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
            <TH colspan="6" align="center">Non Actif</TH>
        </TR>
        <TR>
            <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap></TD>
        </TR>
        <TR>
 	        <TD nowrap width="180" align="center"></TD>
            <TD nowrap >    Année 1</TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140" align="center"></TD>
            <TD nowrap >    Année 2</TD>
 	    </TR>
         <TR>
 	        <TD nowrap width="180">Fortune</TD>
            <TD nowrap >
				<INPUT type="text" name="neFortuneAnnee1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeFortuneAnnee1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Fortune</TD>
            <TD nowrap >
				<INPUT type="text" name="fortune"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getFortune()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Pension</TD>
            <TD nowrap >
				<INPUT type="text" name="nePensionAnnee1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNePensionAnnee1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Pension</TD>
            <TD nowrap >
				<INPUT type="text" name="nePension"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNePension()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Pension Alimentaire</TD>
            <TD nowrap >
				<INPUT type="text" name="nePensionAlimentaire1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNePensionAlimentaire1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Pension Alimentaire</TD>
            <TD nowrap >
				<INPUT type="text" name="nePensionAlimentaire"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNePensionAlimentaire()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Rente Viagère</TD>
            <TD nowrap >
				<INPUT type="text" name="neRenteViagere1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeRenteViagere1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Rente Viagère</TD>
            <TD nowrap >
				<INPUT type="text" name="neRenteViagere"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeRenteViagere()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Indémnité Journalière</TD>
            <TD nowrap >
				<INPUT type="text" name="neIndemniteJour1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeIndemniteJour1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Indémnité Journalière</TD>
            <TD nowrap >
				<INPUT type="text" name="neIndemniteJour"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeIndemniteJour()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	        <TD nowrap width="180">Rente Totale</TD>
            <TD nowrap >
				<INPUT type="text" name="neRenteTotale1"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeRenteTotale1()%>" >
 	        </TD>
 	        <TD width="10"></TD>
 	        <TD nowrap width="140">Rente Totale</TD>
            <TD nowrap >
				<INPUT type="text" name="neRenteTotale"  class="montant" style="width : 2.45cm;" value="<%=viewBean.getNeRenteTotale()%>" >
 	        </TD>
 	    </TR>
 	    <TR>
 	    	<TD colspan="6"><HR></TD>
 	    </TR>
        <TR>
            <TD nowrap width="140">Genre taxation</TD>
            <TD nowrap><INPUT name="libelleGenreTaxation" type="text" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getGenreTaxation())%>" class="libelleLongDisabled" readonly></TD>
            <TD width="10"></TD>
            <TD nowrap>Taxation rectificative</TD>
           	<TD nowrap><input type="checkbox" name="neTaxationRectificative" <%=(viewBean.getNeTaxationRectificative().booleanValue())? "checked" : "unchecked"%>></TD>
         </TR>
         <TR>
            <TD nowrap width="140">Dossier taxé</TD>
            <TD nowrap><INPUT name="libelleDossierTaxe" type="text" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(session,viewBean.getNeDossierTaxe())%>" class="libelleLongDisabled" readonly></TD>
            <TD width="10"></TD>
            <TD>Assujetissement</TD>
           	<TD><ct:FWCalendarTag name="neDateDebutAssuj" 
				value="<%=viewBean.getNeDateDebutAssuj()%>"
				doClientValidation="CALENDAR"
	 			/>
	 		</TD>
         </TR>  
  	   	<TR>
  	   		<TD nowrap width="140">Dossier trouvé</TD>
            <TD nowrap><INPUT name="dossierTrouve" type="text" value="<%=viewBean.getNeDossierTrouve()%>" class="numeroCourtDisabled" readonly></TD>
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
			<% if (!JadeStringUtil .isIntegerEmpty(viewBean.getIdAffiliation())) {%> 
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdTiers()%>&selectedId2=<%=viewBean.getIdAffiliation()%>" class="external_link">Décisions</A>
				</ct:ifhasright>
			<%}%>
			</TD>
        </TR>
      
	    <TR>
            <TD nowrap width="140"><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
            <TD nowrap><INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>"></TD>
            <TD></TD>
            <TD nowrap></TD>
            <TD nowrap><INPUT type="hidden" name="allComm" value='<%=request.getParameter("allComm")%>'></TD>
        </TR>
        <TR>
        	<TD>&nbsp;</TD>
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