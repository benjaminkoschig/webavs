<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPCommunicationPlausibiliteManager"%>
<%@page import="globaz.phenix.db.communications.CPCommunicationPlausibilite"%>
<%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1017";
CPCommunicationFiscaleRetourVSViewBean viewBean = (CPCommunicationFiscaleRetourVSViewBean)session.getAttribute ("viewBean");
boolean hasJournal = true;
String forIdJournalRetour = viewBean.getIdJournalRetour();
globaz.phenix.db.communications.CPJournalRetour journal = viewBean.getJournalRetour();
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean-idRetour"+viewBean.getIdRetour();
%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausiManager"%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausi"%>
<%@page import="globaz.phenix.interfaces.ICommunicationRetour"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean"%><SCRIPT language="JavaScript">
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
top.document.title = "Cotisation - Donn?es fiscales D?tail"
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
	if (window.confirm("Sie sind dabei, das ausgew?hlte Objekt zu l?schen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.supprimer";
		document.forms[0].submit();
	}
}
function init(){
	showPartieFisc();
}

//Affichage donn?es g?n?riques
function showPartieGenerique() {
	document.all('tPartieGenerique').style.display='block';
	document.all('tPartieCaisse').style.display='none';
	document.all('tPartieFisc').style.display='none';
	document.getElementById('gen').className = 'selectedId';
	document.getElementById('caisse').className = 'none';
	document.getElementById('fisc').className = 'none';
}
//Affichage donn?es de la caisse
function showPartieCaisse() {
	document.all('tPartieGenerique').style.display='none';
	document.all('tPartieCaisse').style.display='block';
	document.all('tPartieFisc').style.display='none';
	document.getElementById('gen').className = 'none';
	document.getElementById('caisse').className = 'selectedId';
	document.getElementById('fisc').className = 'none';
}
//Affichage donn?es du fisc
function showPartieFisc() {
	document.all('tPartieGenerique').style.display='none';
	document.all('tPartieCaisse').style.display='none';
	document.all('tPartieFisc').style.display='block';
	document.getElementById('gen').className = 'none';
	document.getElementById('caisse').className = 'none';
	document.getElementById('fisc').className = 'selectedId';
}
function retourOriginale() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.retournerOriginale";
		document.forms[0].submit();
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Donn?es fiscales<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						
 <div id="tabs"> 
		<ul>
  			<li><a class="gen" href="javascript:showPartieGenerique()">Donn?es g?n?riques</a></li>
  			<li><a class="caisse" href="javascript:showPartieCaisse()">Donn?es caisses</a></li>
  			<li><a class="fisc" href="javascript:showPartieFisc()">Donn?es du fisc</a></li>
  						
	   
 	   	</ul>
		</div>	
	<TR>
	<TD>
	<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartieGenerique" >
	 <TR>
            <TD nowrap width="160">&nbsp;</TD>
            <TD nowrap></TD>
            <TD nowrap width="200">&nbsp;</TD>
	     	<TD nowrap width="300">&nbsp;</TD>
       	</TR>
       	<TR>
			<TD><a  onclick="retourOriginale()" href="#">Ecraser les donn?es avec originale (irr?versible)</a></TD>
		</TR>
	<TR>
            <TD nowrap >Partner</TD>
            <TD nowrap >
				<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNomPrenom()%>" class="libelleLongDisabled" readonly>
				<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
		     </TD>
		     <TD></TD>
		    <TD ></TD>
   </TR>
   <TR>
   			<TD nowrap ></TD>
       		<TD nowrap><INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly></TD>
       		<TD ></TD>
		    <TD ></TD>
    </TR>
    <TR>
           <TD nowrap width="140">&nbsp;</TD>
            <TD nowrap ></TD>
	     	<TD width="10"></TD>
	     	<TD width="10"></TD>
	</TR>
    <TR>
           <TD nowrap align="left">Mitglied</TD>
            <TD nowrap >
			<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
	     	</TD>
	     	<TD width="10"></TD>
	     	<TD width="10"></TD>
	</TR>
	<TR>
            <TD nowrap align="left">SVN</TD>
            <TD nowrap>
			    <INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs(1)%>" class="libelleLongDisabled" readonly>
	     	</TD>
	        <TD width="10"><INPUT type="hidden" name="idCommunication" value="<%=viewBean.getIdCommunication()%>"></TD>
	     	<TD width="10"></TD>
	</TR> 
	<TR>
            <TD nowrap>Steuer-Nr.</TD>
            <TD nowrap><INPUT name="numContribuable" type="text" value="<%=viewBean.getNumContribuable()%>" class="libelleLongDisabled" readonly></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
	  </TR>
	  
	   <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap ></TD>
	     	<TD width="10"></TD>
	     	<TD width="10"></TD>
	  </TR>
	  
	  	<TR>
           <TD nowrap width="140">Periode DBST-Nr.</TD>
            <TD nowrap><INPUT type="text" name="numIfd" class="numeroCourtDisabled" value="<%=viewBean.getNumIfd()%>" readonly> 
            	<INPUT type="text" name="numIfd" class="numeroCourtDisabled" value="<%=viewBean.getAnnee1()%>" readonly>
            </TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
	   </TR>
	    <TR>
           <TD nowrap>&nbsp;</TD>
            <TD nowrap ></TD>
	     	<TD width="10"></TD>
	     	<TD width="10"></TD>
	  </TR>
	   <TR>
            <TD>Mitgliedsart</TD>
		    <TD ><INPUT type="text" name="libGenreAffilie" tabindex="-1" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(viewBean.getSession(), viewBean.getGenreAffilie())%>" class="libelleLongDisabled" readonly></TD>	
		   <TD nowrap ></TD>
		    <TD nowrap ></TD>
    </TR>
     <TR>
            <TD>Ehepartnersart</TD>
		    <TD ><INPUT type="text" name="libGenreConjoint" tabindex="-1" value="<%=globaz.phenix.translation.CodeSystem.getLibelle(viewBean.getSession(), viewBean.getGenreAffilie())%>" class="libelleLongDisabled" readonly></TD>
			 <TD nowrap ></TD>
			 <TD nowrap ></TD>
		</TR>
    <!-- 
      	<TR>
           <TD nowrap>Revenu non agricole</TD>
            <TD nowrap><INPUT name="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant" style="width : 2.45cm;"></TD>
 	        <TD width="10"></TD>
	     	<TD width="10"></TD>
              
 	    </TR>
         <TR>
            <TD nowrap>Revenu agricole</TD>
            <TD nowrap><INPUT name="revenu2" type="text" value="<%=viewBean.getRevenu2()%>" class="montant" style="width : 2.45cm;"></TD>
          	<TD width="10"></TD>
	     	<TD width="10"></TD>
         </TR>
         <TR>
            <TD nowrap>Capital</TD>
            <TD nowrap><INPUT type="text" name="capital" value="<%=viewBean.getCapital()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
       	<TR>
            <TD nowrap>Fortune</TD>
            <TD nowrap><INPUT type="text" name="fortune" value="<%=viewBean.getFortune()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
       	-->
       	<TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
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
	     	 <TD nowrap>Status</TD>
            <TD nowrap><INPUT name="etat" tabindex="-1" type="text" value="<%=viewBean.getVisibleStatus()%>" class="libelleLongDisabled" readonly></TD>
       		<TD width="10"></TD>
	     	<TD width="10"></TD>
 	    </TR>
       	 <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
       	   <TR>
            <TD nowrap>
            	<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.chercher" crud="r">
            	<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher<%=hasJournal?"&idJournalRetour="+forIdJournalRetour:""%>" tabindex="-1" class="external_link">Retour ? la liste des communications</A>
            	</ct:ifhasright>
            	
            	<br>
			</TD>
			<TD>
			<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdAffiliation())) {%>
				<ct:ifhasright element="phenix.principale.decision.chercher" crud="r"> 
				<A href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&selectedId=<%=viewBean.getIdTiers()%>&selectedId2=<%=viewBean.getIdAffiliation()%>" class="external_link">Verf?gungs</A>
				</ct:ifhasright>
			<%}%>
			</TD>
			<TD></TD>
        </TR>
      
	    <TR>
            <TD nowrap><INPUT type="hidden" name="idJournalRetour" value="<%=forIdJournalRetour%>"></TD>
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
          <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
         </TABLE>
	<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartieCaisse">
	  <TR>
            <TD nowrap width="200">&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	
		<TR>
 	    <TD nowrap>Steuer-Nr.</TD>
 	    <TD nowrap><INPUT type="text" name="vsNumCtb" class="libelleLong" value="<%=viewBean.getVsNumCtb()%>"></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
       	   <TR>
 	    <TD nowrap>Zivilstand</TD>
 	    <TD nowrap><INPUT type="text" name="vsEtatCivilAffilie" value="<%=viewBean.getVsEtatCivilAffilie()%>" class="numeroCourtDisabled" readonly "></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap>Geschlecht</TD>
 	    <TD nowrap><INPUT type="text" name="vsSexeAffilie" class="numeroCourtDisabled" readonly value="<%=viewBean.getVsSexeAffilie()%>"></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
 	   	<TD nowrap></TD>
        <TD nowrap align="center"><b><font size=3>Steuer</font></b></TD>
     	<TD width="10"></TD>
	    <TD nowrap align="center"><b><font size=3>Ehepartner</font></b></TD>
 	    </TR>
 	    	<TR>
 	    <TD nowrap>Name</TD>
 	    <TD nowrap><INPUT name="vsNomAffilie" type="text" value="<%=viewBean.getVsNomAffilie()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT name="vsNomConjoint" type="text" value="<%=viewBean.getVsNomConjoint()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	   <TR>       
        <TD nowrap>Geburtsdatum</TD>
        <TD nowrap><INPUT type="text" name="vsDateNaissanceAffilie" class="dateDisabled" readonly value="<%=viewBean.getVsDateNaissanceAffilie()%>"></TD>
	   	<TD width="10"></TD>
	    <TD width="10"><INPUT type="text" name="vsDateNaissanceConjoint" class="dateDisabled" readonly value="<%=viewBean.getVsDateNaissanceConjoint()%>"></TD>
 	     </TR>  
 	    <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
 	   	    <TR>
 		<TD nowrap>Abr.-Nr.</TD>
        <TD nowrap><INPUT name="vsNumAffilie1" type="text" value="<%=viewBean.formaterNumAffilieVS(viewBean.getVsNumAffilie(0))%>" class="libelleLongDisabled" readonly></TD>
     	<TD width="10"></TD>
	    <TD width="10"><INPUT name="vsNumAffilieConjoint1" type="text" value="<%=viewBean.formaterNumAffilieVS(viewBean.getVsNumAffilieConjoint(0))%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 		<TD nowrap>SVN</TD>
        <TD nowrap><INPUT name="vsNumAvsAffilie" type="text" value="<%=viewBean.getVsNumAvsAffilie(1)%>" class="libelleLongDisabled" readonly></TD>
     	<TD width="10"></TD>
	    <TD width="10"><INPUT name="vsNumAvsConjoint" type="text" value="<%=viewBean.getVsNumAvsConjoint(1)%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap>Adresse</TD>
 	    <TD nowrap><INPUT name="vsAdresseAffilie1" type="text" value="<%=viewBean.getVsAdresseAffilie1()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	    <TD nowrap><INPUT name="vsAdresseConjoint1" type="text" value="<%=viewBean.getVsAdresseConjoint1()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap></TD>
 	    <TD nowrap><INPUT name="vsAdresseAffilie2" type="text" value="<%=viewBean.getVsAdresseAffilie2()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	    <TD nowrap><INPUT name="vsAdresseConjoint2" type="text" value="<%=viewBean.getVsAdresseConjoint2()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap></TD>
 	    <TD nowrap><INPUT name="vsAdresseAffilie3" type="text" value="<%=viewBean.getVsAdresseAffilie3()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	    <TD nowrap><INPUT name="vsAdresseConjoint3" type="text" value="<%=viewBean.getVsAdresseConjoint3()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap></TD>
 	    <TD nowrap><INPUT name="vsAdresseAffilie4" type="text" value="<%=viewBean.getVsAdresseAffilie4()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	    <TD nowrap><INPUT name="vsAdresseConjoint4" type="text" value="<%=viewBean.getVsAdresseConjoint4()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap></TD>
 	    <TD nowrap><INPUT name="vsNoPostalLocalite" type="text" value="<%=viewBean.getVsNoPostalLocalite()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	    <TD nowrap><INPUT name="vsNumPostalLocaliteConjoint" type="text" value="<%=viewBean.getVsNumPostalLocaliteConjoint()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
 	    <TR>
 	    <TD nowrap>Kassen</TD>
 	    <TD nowrap><INPUT name="vsNoCaisseAgenceAffilieFormate" type="text" value="<%=viewBean.getVsNoCaisseAgenceAffilie(1)%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT name="vsNumCaisseAgenceConjointFormate" type="text" value="<%=viewBean.getVsNumCaisseAgenceConjoint(1)%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	     <TR>
 	    <TD nowrap>Erfassungperiode</TD>
 	    <TD nowrap><INPUT value="<%=viewBean.getVsDateDebutAffiliation()+ " - " + viewBean.getVsDateFinAffiliation()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT value="<%=viewBean.getVsDateDebutAffiliationConjoint()+ " - " + viewBean.getVsDateFinAffiliationConjoint()%>" class="libelleLongDisabled" readonly></TD>
 	     </TR>
 	   <TR>
 	    <TD nowrap>Caisse professionnelle</TD>
 	    <TD nowrap><INPUT name="vsNoCaisseProfessionnelleAffilie" type="text" value="<%=viewBean.getVsNoCaisseProfessionnelleAffilie()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT name="vsNumCaisseProfessionnelleConjoint" type="text" value="<%=viewBean.getVsNumCaisseProfessionnelleConjoint()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap>Interne Nr.</TD>
 	    <TD nowrap><INPUT name="vsNumAffilieInterneCaisseProfessionnelle" type="text" value="<%=viewBean.getVsNumAffilieInterneCaisseProfessionnelle()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT name="vsNumAffilieInterneConjointCaisseProfessionnelle" type="text" value="<%=viewBean.getVsNumAffilieInterneConjointCaisseProfessionnelle()%>" class="libelleLongDisabled" readonly></TD>
 	    </TR>
 	       <TR>
 	     <TD nowrap>P?riode caisse professionnelle</TD>
 	    <TD nowrap><INPUT value="<%=viewBean.getVsDateDebutAffiliationCaisseProfessionnelle() + " - " +  viewBean.getVsDateFinAffiliationCaisseProfessionnelle()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap> <INPUT value="<%=viewBean.getVsDateDebutAffiliationConjointCaisseProfessionnelle()+ " - " + viewBean.getVsDateFinAffiliationConjointCaisseProfessionnelle()%>" class="libelleLongDisabled" readonly></TD>
 	     </TR>
 	       <TR>
 	    <TD nowrap>Beitrag AHV</TD>
 		<TD nowrap><INPUT name="vsCotisationAvsAffilie" type="text" value="<%=viewBean.getVsCotisationAvsAffilie()%>" class="libelleLongDisabled" readonly></TD> 
 	   <TD width="10"></TD>
		<TD nowrap><INPUT name="vsCotisationAvsConjoint" type="text" value="<%=viewBean.getVsCotisationAvsConjoint()%>" class="libelleLongDisabled" readonly></TD>
	     </TR>
	      <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	</TABLE>
	
	<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartieFisc">
	 <TR>
            <TD nowrap width="200">&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	  <TR>
	    <TD nowrap>?bermittelte Name</TD>
	    <TD nowrap><INPUT name="vsNomPrenomContribuableAnnee" type="text" value="<%=viewBean.getVsNomPrenomContribuableAnnee()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
 	      
       <TR>
 	     <TD nowrap align="left">?bermittelte NSS</TD>
         <TD nowrap><INPUT type="text" name="vsNumAvsCtb" tabindex="-1" value="<%=viewBean.getVsNumAvsCtb(1)%>" class="libelleLongDisabled" readonly></TD>
         <TD width="10"></TD>
         <TD width="10"></TD>
	   </TR>
	    <TR>
	    <TD nowrap>Adresse</TD>
	    <TD nowrap><INPUT name="vsAdresseCtb1" type="text" value="<%=viewBean.getVsAdresseCtb1()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
	      <TR>
	    <TD nowrap></TD>
	    <TD nowrap><INPUT name="vsAdresseCtb2" type="text" value="<%=viewBean.getVsAdresseCtb2()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
	     <TR>
	    <TD nowrap></TD>
	    <TD nowrap><INPUT name="vsAdresseCtb3" type="text" value="<%=viewBean.getVsAdresseCtb3()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
	     <TR>
	    <TD nowrap></TD>
	    <TD nowrap><INPUT name="vsAdresseCtb4" type="text" value="<%=viewBean.getVsAdresseCtb4()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
	     <TR>
	    <TD nowrap></TD>
	    <TD nowrap><INPUT name="vsNumPostalLocaliteCtb" type="text" value="<%=viewBean.getVsNumPostalLocaliteCtb()%>" class="libelleLongDisabled" readonly></TD>
	     <TD width="10"></TD>
	      <TD width="10"></TD>
	     </TR>
	        <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	     <TR>
        <TD nowrap>Steuermeldungsdatum</TD>
	   	<TD nowrap><INPUT type="text" name="vsDateCommunication" class="dateDisabled" readonly value="<%=viewBean.getVsDateCommunication()%>"></TD>
	    <TD nowrap> Veranlagungsdatum</TD>
	    <TD nowrap><INPUT type="text" name="vsDateTaxation" class="dateDisabled" readonly value="<%=viewBean.getVsDateTaxation()%>"></TD>
 	    </TR>
 	    <TR>       
        <TD nowrap>Geburtsdatum</TD>
        <TD nowrap><INPUT type="text" name="vsDateNaissanceCtb" class="dateDisabled" readonly value="<%=viewBean.getVsDateNaissanceCtb()%>"></TD>
	   	<TD width="10">Todesdatum</TD>
	    <TD width="10"><INPUT type="text" name="vsDateDecesCtb" class="dateDisabled" readonly value="<%=viewBean.getVsDateDecesCtb()%>"></TD>
 	     </TR> 
	      <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
       	   <TR>
 	    <TD nowrap>Zivilstand</TD>
 	    <TD nowrap><INPUT type="text" name="vsEtatCivilCtb" value="<%=viewBean.getVsEtatCivilCtb()%>" class="numeroCourtDisabled" readonly "></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap>Geschlecht</TD>
 	    <TD nowrap><INPUT type="text" name="vsSexeCtb" class="numeroCourtDisabled" readonly value="<%=viewBean.getVsSexeCtb()%>"></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
 	    <TD nowrap>Sprache</TD>
 	    <TD nowrap><INPUT type="text" name="vsLangue" class="numeroCourtDisabled" readonly value="<%=viewBean.getVsLangue()%>"></TD>
 	    <TD width="10"></TD>
	    <TD width="10"></TD>
 	    </TR>
 	    <TR>
 	   	<TD nowrap></TD>
        <TD nowrap align="center"><b><font size=3>Steuer</font></b></TD>
     	<TD width="10"></TD>
	    <TD nowrap align="center"><b><font size=3>Ehepartner</font></b></TD>
 	    </TR>
	        <TR>
 	    <TD nowrap>P?riode d'activit?</TD>
 	    <TD nowrap><INPUT value="<%=viewBean.getVsDebutActiviteCtb()+ " - " + viewBean.getVsFinActiviteCtb()%>" class="libelleLongDisabled" readonly></TD>
 	    <TD width="10"></TD>
	   <TD nowrap><INPUT value="<%=viewBean.getVsFinActiviteCtb()+ " - " + viewBean.getVsFinActiviteConjoint()%>" class="libelleLongDisabled" readonly></TD>
 	     </TR>
	     <TR>
             <TD nowrap>Nicht landwirtschaftliches Einkommen</TD>
            <TD><INPUT name="vsRevenuNonAgricoleCtb" type="text" value="<%=viewBean.getVsRevenuNonAgricoleCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
             <TD width="10"></TD>      
            <TD><INPUT name="vsRevenuNonAgricoleConjoint" type="text" value="<%=viewBean.getVsRevenuNonAgricoleConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
 	     </TR>
 	     <TR>  
             <TD>Landwirtschaftliches Einkommen</TD>
            <TD><INPUT name="vsRevenuAgricoleCtb" type="text" value="<%=viewBean.getVsRevenuAgricoleCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
             <TD width="10"></TD>
            <TD><INPUT name="vsRevenuAgricoleConjoint" type="text" value="<%=viewBean.getVsRevenuAgricoleConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
         </TR>
         <TR>
            <TD nowrap="nowrap">Kapital im Betrieb investiert</TD>
			<TD><INPUT name="vsCapitalPropreEngageEntrepriseCtb" type="text" value="<%=viewBean.getVsCapitalPropreEngageEntrepriseCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
      		 <TD width="10"></TD>
             <TD><INPUT name="vsCapitalPropreEngageEntrepriseConjoint" type="text" value="<%=viewBean.getVsCapitalPropreEngageEntrepriseConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        </TR>
        <TR>
        	 <TD nowrap="nowrap">Renteneinkommen</TD>
            <TD><INPUT name="vsRevenuRenteCtb" type="text" value="<%=viewBean.getVsRevenuRenteCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        	 <TD width="10"></TD>
            <TD><INPUT name="vsRevenuRenteConjoint" type="text" value="<%=viewBean.getVsRevenuRenteConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
       
        </TR>
        <TR>
           <TD nowrap="nowrap">Privates Verm?gen</TD>
            <TD><INPUT name="vsFortunePriveeCtb" type="text" value="<%=viewBean.getVsFortunePriveeCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        	 <TD width="10"></TD>
            <TD><INPUT name="vsFortunePriveeConjoint" type="text" value="<%=viewBean.getVsFortunePriveeConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        
        </TR>
         <TR>
           <TD nowrap="nowrap">Einkommen</TD>
            <TD><INPUT name="vsSalairesContribuable" type="text" value="<%=viewBean.getVsSalairesContribuable()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
         	<TD width="10"></TD>
          <TD><INPUT name="vsSalairesConjoint" type="text" value="<%=viewBean.getVsSalairesConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        </TR> 
        <TR>
          <TD nowrap="nowrap">Andere Einkommen</TD>
            <TD><INPUT name="vsAutresRevenusCtb" type="text" value="<%=viewBean.getVsAutresRevenusCtb()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        	 <TD width="10"></TD>
            <TD><INPUT name="vsAutresRevenusConjoint" type="text" value="<%=viewBean.getVsAutresRevenusConjoint()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
  	    </TR>
  	    <TR>
          <TD nowrap="nowrap">Einkauf BVG</TD>
            <TD><INPUT name="vsRachatLpp" type="text" value="<%=viewBean.getVsRachatLpp()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
        	 <TD width="10"></TD>
            <TD><INPUT name="vsRachatLppCjt" type="text" value="<%=viewBean.getVsRachatLpp()%>" class="montantDisabled" style="width : 8cm;" readonly></TD>
  	    </TR>
	    <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	     <TR>	
 	    <TD nowrap>Veranlagungsart</TD>
         <TD nowrap><INPUT name="vsTypeTaxation" type="text" tabindex="-1" value="<%=viewBean.getVsTypeTaxation()%>" class="libelleLongDisabled" readonly></TD>
         <TD width="10"></TD>
         <TD width="10"></TD>
 	     </TR>
 	     <TR>
            <TD nowrap>Veranlagungscode 1</TD>
            <TD><INPUT type="text" name="" value="<%=viewBean.getVsCodeTaxation1Libelle()%>" class="libelleLongDisabled" readonly></TD>
            <TD width="10"></TD>
         <TD width="10"></TD>
       </TR>
       <TR>
            <TD nowrap>Veranlagungscode 2</TD>
            <TD><INPUT type="text" name="" value="<%=viewBean.getVsCodeTaxation2Libelle()%>" class="libelleLongDisabled" readonly></TD>
            <TD width="10"></TD>
         <TD width="10"></TD>
       </TR>
        <TR>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap></TD>
            <TD width="10"></TD>
	     	<TD width="10"></TD>
       	</TR>
	</TABLE>
	</TD>
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