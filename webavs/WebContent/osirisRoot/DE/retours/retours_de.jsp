
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.osiris.db.retours.CARetours"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA0070"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CARetoursViewBean viewBean = (CARetoursViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);

if(viewBean.isNew() &&
   !JadeStringUtil.isNull(request.getParameter("idLot"))){
	
	viewBean.setIdLot(request.getParameter("idLot"));
}

bButtonDelete = bButtonDelete && viewBean.isRetourModifiable();
bButtonUpdate = bButtonUpdate && viewBean.isLignesRetourModifiables();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.osiris.db.retours.CARetoursViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<script type="text/javascript"
	src="osirisRoot/scripts/historiqueChamps.js">
</script>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

$(function(){
	<%viewBean.loadLastSaisieUtilisateur();%>
	historiqueChamps.init("#idExterneRoleEcran, #dateRetour, #csMotifRetour, #libelleRetour",<%=CAOperation.convertHashMapForJQuery(viewBean.getMapValeurUtilisateur())%>);
		
}) 

function add() {
  document.forms[0].elements('userAction').value="osiris.retours.retours.ajouter";
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.retours.retours.modifier";
  
  <%if(!viewBean.isRetourModifiable()){%>
	document.getElementById("idExterneRoleEcran").readOnly=true;
	document.getElementById("idExterneRoleEcran").disabled=true;
	document.getElementById("dateRetour").readOnly=true;
	document.getElementById("dateRetour").disabled=true;
	document.getElementById("anchor_dateRetour").disabled=true;
	document.getElementById("montantRetour").readOnly=true;
	document.getElementById("montantRetour").disabled=true;
  <%} %>
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Rolle zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.retours.retours.supprimer";
        document.forms[0].submit();
    }
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.retours.retours.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.retours.retours.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.retours.retours.chercher";
}

function init(){
	validateFloatNumber(document.forms[0].elements('montantRetour'));
	
  	<%if(!viewBean.isRetourModifiable()){%>
		document.getElementById("idExterneRoleEcran").readOnly=true;
		document.getElementById("idExterneRoleEcran").disabled=true;
		document.getElementById("dateRetour").readOnly=true;
		document.getElementById("dateRetour").disabled=true;
		document.getElementById("anchor_dateRetour").disabled=true;
		document.getElementById("montantRetour").readOnly=true;
		document.getElementById("montantRetour").disabled=true;
  	<%} %>
}

function updateIdCompteAnnexe(tag){
	if(tag.select!=null){
		if(tag.select && tag.select.selectedIndex != -1){
			document.getElementById('descCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedCompteAnnexeDesc;
			document.getElementById('idCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedIdCompteAnnexe;
		} else {
			document.getElementById('idCompteAnnexe').value = "";
			document.getElementById('descCompteAnnexe').value = "";
		}
	}
}
	
function addLigneRetourSurSection(){
	document.forms[0].elements('userAction').value = "osiris.retours.retours.actionAjouterLignesRetoursSurSection";
	document.forms[0].submit();
}

function addLigneRetourSurAdressePaiement(){
	document.forms[0].elements('userAction').value = "osiris.retours.retours.actionAjouterLignesRetoursSurAdressePaiement";
	document.forms[0].submit();
}

function creerRetourSplit(){
	document.forms[0].elements('userAction').value = "osiris.retours.retours.actionCreerRetourSplit";
	document.forms[0].submit();	
}

function annulerRetour(){
	document.forms[0].elements('userAction').value = "osiris.process.annulerRetours.afficher";
	document.forms[0].submit();
}

top.document.title = "Retours" + top.location.href;
// stop hiding -->
</SCRIPT>

<ct:menuChange displayId="options" menuId="CA-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(true, "retours", "CA-MenuPrincipal");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Rückkehr<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD>Job</TD>
            <TD  colspan="3"> 
              <INPUT type="text" name="lot" readonly class="disabled" value="<%=viewBean.getIdLot()+ " - "+viewBean.getLibelleLot()%>">
              <INPUT type="hidden" name="idLot" value="<%=viewBean.getIdLot()%>">
              <INPUT type="hidden" name="libelleLot" value="<%=viewBean.getLibelleLot()%>">
              <INPUT type="hidden" name="montantTotal" value="<%=viewBean.getMontantTotal()%>">
              <INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>">
              <INPUT type="hidden" name="csEtatLot" value="<%=viewBean.getCsEtatLot()%>">
            </TD>
          </TR>
          
          <TR>
            <TD>Rückkehr-Nr.</TD>
            <TD> 
              <INPUT type="text" name="idRetourAffiche" readonly class="disabled" value="<%=viewBean.getIdRetour()%>">
            </TD>
            <TD>Status</TD>
            <TD> 
              <INPUT type="text" name="CsEtatRetourLibelle" readonly class="disabled" value="<%=JadeStringUtil.isEmpty(viewBean.getCsEtatRetour())?viewBean.getSession().getCodeLibelle(CARetoursViewBean.CS_ETAT_RETOUR_OUVERT):viewBean.getCsEtatRetourLibelle()%>">
              <INPUT type="hidden" name="csEtatRetour" value="<%=JadeStringUtil.isEmpty(viewBean.getCsEtatRetour())?CARetoursViewBean.CS_ETAT_RETOUR_OUVERT:viewBean.getCsEtatRetour()%>">
            </TD>
          </TR>
          
          <TR><TD colspan="2">&nbsp;</TD></TR>
          
		  <TR>
		  	<TD>Abrechnungskonto</TD>
		  	<TD>
              <input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>" >


			  <%if(viewBean.getCompteAnnexe()==null){ %>
              <ct:FWPopupList
	           name="idExterneRoleEcran"
		           value=""
	           className="libelle"
	           jspName="<%=jspAffilieSelectLocation%>"
	           autoNbrDigit="<%=autoCompleteStart%>"
	           size="25"
	           onChange="updateIdCompteAnnexe(tag);"
	           minNbrDigit="1"/>
	           <%}else{%>
	              <ct:FWPopupList
		           name="idExterneRoleEcran"
		           value="<%=viewBean.getCompteAnnexe().getIdExterneRole()%>"
		           className="libelle"
		           jspName="<%=jspAffilieSelectLocation%>"
		           autoNbrDigit="<%=autoCompleteStart%>"
		           size="25"
		           onChange="updateIdCompteAnnexe(tag);"
		           minNbrDigit="1"/>
	           <%}%>
	          
	          <input type="text" name="descCompteAnnexe" maxlength="29" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
		  </TR>
		  
		  <TR>
		  	<TD>Rückkehrdatum</TD>
		  	<TD><ct:FWCalendarTag name="dateRetour" value="<%=viewBean.getDateRetour()%>"/></TD>
		  	<TD>Auftragsart</TD>
		  	<TD>
              <ct:FWCodeSelectTag codeType="OSIORDLIV" name="csNatureOrdre" defaut="<%=viewBean.getCsNatureOrdre()%>" wantBlank="true"/>
		  	</TD>  
		  </TR>
		  
		  <TR>
	        <TD>Grund</TD>
		  	<TD>
              <ct:FWCodeSelectTag codeType="OSIMOTRET" name="csMotifRetour" defaut="<%=viewBean.getCsMotifRetour()%>" wantBlank="true"/>
		  	</TD>
          </TR>
		  
		  <TR>
	        <TD>Bemerkung</TD>
		  	<TD><INPUT type="text" id="libelleRetour" name="libelleRetour" value="<%=viewBean.getLibelleRetour()%>"  size="40" maxlength="40"></TD>
		  	<%if(JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())){%>
				<TD colspan="2">&nbsp;</TD>
			<%}else{%>
				<TD>&nbsp;</TD>
				<TD>
					<A href="/webavs/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournal()%>" class="link">Journal</A>
				</TD>
			<%}%>
          </TR>
 
           <TR>
	        <TD>Betrag</TD>			
			<TD><INPUT type="text" name="montantRetour" class="montant" value="<%=viewBean.getMontantRetour()%>" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
			<%if(viewBean.isRetourModifiable()){%>
				<TD colspan="2">&nbsp;</TD>
			<%}else{%>
				<TD>Saldo</TD>
				<TD><INPUT type="text" value="<%=viewBean.getSolde()%>" readonly="readonly" class="montantDisabled" style="<%=new FWCurrency(viewBean.getSolde()).isZero()?"color=black;":"color=red;"%>" ></TD>
			<%}%>
          </TR>
          
          <%if(!viewBean.isRetourModifiable()){%>
          
			<TR>
				<TD colspan="4"><HR></TD>
			</TR>
			
			<TR>
				<TD colspan="4"><b>Kompensierungen auf bestehende Rechnungen</b></TD>
			</TR>
			
			<TR>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
			<TR>
				<TD colspan="4">
					<table width="100%">
						<TR>
							<TD>Buchungen</TD>
						  	<TD>
								<ct:FWListSelectTag data="<%=viewBean.getSectionsLibres()%>" defaut="" name="idSectionLigneRetourSurSection"/>
						  	</TD>
							<TD>Betrag zu kompensieren</TD>
							<TD>
								<INPUT type="text" name="montantLigneRetourSurSection" class="montant" value="" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
							</TD>
							<TD>
								<INPUT type="button" name="ajouterLigneRetourSurSection" value="Hinzfügen" onclick="addLigneRetourSurSection();">
							</TD>
						</TR>
					</table>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
			<TR>         
	          	<TD colspan="4">
					<IFRAME id="lignesRetoursSurSection" name="lignesRetoursSurSection" width="80%" height="100" 
							src="<%=request.getContextPath()%>/osiris?userAction=osiris.retours.actionListerLignesRetoursSurSection&forIdRetour=<%=viewBean.getIdRetour()%>">
					</IFRAME>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4" height="30">&nbsp;</TD>
			</TR>
			
			
			<TR>
				<TD colspan="4"><b>Zahlungen auf Zahlungsadresse</b></TD>
			</TR>
			
			<TR>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
			<TR>
				<TD colspan="4">
					<table width="100%">
						<TR>
							<TD>Zahlungsadresse:</TD>
							
							<TD><PRE><span class="IJAfficheText"><%=viewBean.getDescriptionLigneRetourSurAdressePaiementBeneficiaire()%></span></PRE></TD>
							<TD><PRE><span class="IJAfficheText"><%=viewBean.getDescriptionLigneRetourSurAdressePaiementAdresse()%></span></PRE></TD>
							
						  	<TD>
				            	<ct:FWSelectorTag 
									name="selecteurAdresses"
									methods="<%=viewBean.getMethodesSelectionAdresse()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.adressepaiement.adressePaiement.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
						  	</TD>
							<TD>Betrag</TD>
							<TD>
								<INPUT type="text" name="montantLigneRetourSurAdressePaiement" class="montant" value="" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
							</TD>
							<TD>
								<INPUT type="button" name="ajouterLigneRetourSurAdressePaiement" value="Hinzufügen" onclick="addLigneRetourSurAdressePaiement();">
							</TD>
						</TR>
					</table>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
			<TR>         
	          	<TD colspan="4">
					<IFRAME id="lignesRetoursSurAdressePaiement" name="lignesRetoursSurAdressePaiement" width="80%" height="150"
						src="<%=request.getContextPath()%>/osiris?userAction=osiris.retours.actionListerLignesRetoursSurAdressePaiement&forIdRetour=<%=viewBean.getIdRetour()%>">
					</IFRAME>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4" height="30">&nbsp;</TD>
			</TR>
					
				<%if(CARetours.CS_ETAT_RETOUR_SUSPENS.equalsIgnoreCase(viewBean.getCsEtatRetour())) { %>
					
					<TR>
						<TD colspan="4"><b>?Eclater le retour</b></TD>
					</TR>
			
					<TR>
						<TD colspan="4">&nbsp;</TD>
					</TR>
					
					<TR>
				        <TD>?Motif</TD>
					  	<TD><ct:FWCodeSelectTag codeType="OSIMOTRET" name="csMotifRetourSplit" defaut="<%=viewBean.getCsMotifRetourSplit()%>" wantBlank="true"/></TD>
			        </TR>
					  
					<TR>
				        <TD>?Remarque</TD>
					  	<TD><INPUT type="text" id="libelleRetourSplit" name="libelleRetourSplit" value="<%=viewBean.getLibelleRetourSplit()%>"  size="40" maxlength="40"></TD>
			       </TR>
			 
			       <TR>
				        <TD>?Montant</TD>			
						<TD><INPUT type="text" name="montantRetourSplit" class="montant" value="<%=viewBean.getMontantRetourSplit()%>" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
				   </TR>
				   
				   <TR>
				   		<TD colspan="2" align="center"><INPUT type="button" name="buttonCreerRetourSplit" value="?Créer" onclick="creerRetourSplit();"></TD>
				   </TR>
				  <%} %>	
			
			<%} %>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if(viewBean.isRetourAnnulable()){%>
					<ct:ifhasright element="osiris.process.annulerRetours.afficher" crud="u">
						<INPUT type="button" value="Rückkehr annullieren" onclick="annulerRetour();"/>
					</ct:ifhasright>
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>