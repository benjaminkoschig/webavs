<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*, globaz.pavo.db.inscriptions.CIJournal"%>
<%
	idEcran = "CCI0014";
    globaz.pavo.db.inscriptions.CIJournalViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdJournal();
	userActionValue = "pavo.inscriptions.journal.modifier";
	String styleOuverture = "";
	
	boolean isJournalGebucht = CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat());
	boolean isJournalPartiel = CIJournal.CS_PARTIEL.equals(viewBean.getIdEtat());
	boolean isJournalOuvert = CIJournal.CS_OUVERT.equals(viewBean.getIdEtat());
	String jspLocation = servletContext + mainServletPath + "Root/tiForJournal_typeAff_select.jsp";
	
	//Uniquement les journaux avec le status 'OUVERT' ont le droit d'être supprimés
	//cf. point ouvert 83
	
	
	if(!isJournalOuvert) {
		styleOuverture = "class='disabled' readonly tabIndex='-1'";
	}
		
%>
<%	
	String styleHasEcriture="";
	if(isJournalOuvert && "True".equals(viewBean.getEcrituresPresentes())){
		styleHasEcriture = "class='disabled' readonly tabIndex='-1'";
		
	}%>
<%	
	
	if(objSession.getUserName().equals(viewBean.getProprietaire()) || globaz.pavo.util.CIUtil.isSpecialist(objSession)){
		
	}else{
		bButtonDelete=false;
		bButtonUpdate=false;
	}
	if (!CIJournal.CS_OUVERT.equals(viewBean.getIdEtat()))
		bButtonDelete = false;
%>
<%
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	
%>

<SCRIPT language="JavaScript">
top.document.title = "IK - Detail des Journals"
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>


<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.inscriptions.journal.ajouter";
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.inscriptions.journal.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.inscriptions.journal.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.inscriptions.journal.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.inscriptions.journal.supprimer";
        document.forms[0].submit();
    }
}


function init()
{
}

function updateInfoAffilie(tag) {
	if (tag.select)
 		document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom;
}
function resetInfoAffilie() {
 	document.getElementById('infoAffilie').value = '';
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Journals<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
            				<td height="38" width="31%">Journal</td>
            				<td height="38" width="8%"><input type="text" name="idJournal" size="10" value="<%=viewBean.getIdJournal()%>" class='disabled' readonly tabIndex="-1"></td>
            				<td height="38" width="41%">&nbsp;</td>
            				<td height="38" width="20%">&nbsp;</td>
          				</tr>
	   					<tr>
            				<td width="31%" height="2">Journalstyp</td>
            				<td colspan="2" height="2">			
            					<%
								java.util.HashSet typeCompte = new java.util.HashSet();
							
								if(!globaz.pavo.util.CIUtil.isAfficheJournalAssuranceFac(objSession)) {
									typeCompte.add(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_FACULTATIVE);
								}
								
								if("True".equals(viewBean.getEcrituresPresentes()) || isJournalGebucht|| isJournalPartiel) { %>
									<input name="idTypeInscriptionInv" readonly class="disabled" tabIndex="-1" size="60" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdTypeInscription(),session)%>" >
								<% } else { 
										if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
											<ct:FWCodeSelectTag name="idTypeInscription" defaut="<%=!JAUtil.isStringEmpty(viewBean.getIdTypeInscription())?viewBean.getIdTypeInscription():CIJournal.CS_DECLARATION_SALAIRES%>" except="<%=typeCompte%>" codeType="CITYPINS" wantBlank="false"/>
										<%} else {%>
											<ct:FWCodeSelectTag name="idTypeInscription" defaut="<%=viewBean.getIdTypeInscription()%>" except="<%=typeCompte%>" codeType="CITYPINS" wantBlank="false"/>
										<%}%> * 
              						<script>
									document.getElementById("idTypeInscription").style.width = "12cm";
									</script>
								<% } %>	
								</td>
            					<td width="20%" height="2">&nbsp;</td>
          						</tr>
          						<tr>
            						<td width="31%" height="17">Beitragsjahr</td>
									<td colspan="2" height="17">
									<!--JANumberFormatter.formatZeroValues(viewBean.getAnneeCotisation(),false,true)-->
									<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))){%>
										<input type="text" name="anneeCotisation" onkeypress="return filterCharForPositivInteger(window.event);" maxlength="4" size="4" 
										value="<%=viewBean.getAnneeCotisation()%>">
									 
									<%}else{%>
									<input type="text" <%if(!"0".equals(viewBean.getAnneeCotisation().trim())){%>value=<%=viewBean.getAnneeCotisation()%><%}else{
									%> value=""<%}%> size=4 <%=styleOuverture%> <%=styleHasEcriture%>>
									<%}%>
								</td>
            					<td width="20%" height="17"><div align="left"></div></td>
          						</tr>
          						<tr>
            						<td width="31%" height="31">Abr.-Nr.</td>
            						<td height="31" colspan="2">
									<%if (isJournalGebucht || isJournalPartiel || "True".equals(viewBean.getEcrituresPresentes())) {%>
										<input type="text" name="idAffiliation" maxlength="16" size="16" value="<%=viewBean.getNumeroAffilie()%>" <%=styleOuverture%><%=styleHasEcriture%>>
										<%} else {%>
            							<ct:FWPopupList name="idAffiliation" value="<%=viewBean.getNumeroAffilie()%>" jspName="<%=jspLocation%>" validateOnChange="true" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3" 
            							onChange="updateInfoAffilie(tag);" onFailure="resetInfoAffilie()" maxlength="16" size="16"/>
 										<%}%>            								
            						<input type="text" class="disabled" readonly name="infoAffilie" size="73" maxlength="73" value="<%=viewBean.getInfoAffilie()%>" tabIndex="-1">
								</td>
								<td height="31" width="20%">&nbsp;</td>
          						</tr>
        
          						<tr>
            						<td width="31%" height="2">Bezeichnung</td>
            						<td colspan="2" height="2">
            						<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))){%>
            							<input type="text" name="libelle" size="50" maxlentgh="50" value="<%=viewBean.getLibelle()%>">
            						<%}else{%>
            							<input type="text" name="libelle" size="50" maxlentgh="50" value="<%=viewBean.getLibelle()%>" <%=styleOuverture%>>
            						<%}%>
								
								</td>
            						<td height="2" width="20%">&nbsp;</td>
          						</tr>
 
          						<tr>
            							<td width="31%" height="12">Empfangsdatum</td>
            							<td colspan="2" height="12">
									<%if (isJournalGebucht || isJournalPartiel) {%>
 										<input size="10" type="text" name="dateReception" value="<%=viewBean.getDateReception()%>" <%=styleOuverture%>>
 										<%} else {%>
 										<ct:FWCalendarTag name="dateReception" value="<%=viewBean.getDateReception()%>"/>
 										<%}%>
								</td>
            							<td width="20%" height="12">
            								<div align="left"></div>
											<input type="hidden" name="idTypeCompte" value="<%=viewBean.CS_PROVISOIRE%>">
            							</td>
          						</tr>
								<tr>
										
            							<td width="31%" height="12">Buchungsdatum</td>
            							<td colspan="2" height="12">
            							<input size="10" type="text" name="dateInscription" value="<%=viewBean.getDateInscription()%>" class='disabled' readonly tabIndex='-1'>
 										
								</td>
            							<td width="20%" height="12">
            								<div align="left"></div>
            							</td>
          						</tr>
          						<tr>
            							<td width="31%" height="12">Kontrolltotal</td>
            							<td colspan="2" height="12">
									<%if (isJournalGebucht || isJournalPartiel) {%>
										<input  maxlength="18" size="18" type="text" name="totalControle" align="right" value="<%=viewBean.getTotalControleFormat()%>" <%=styleOuverture%>>
 									<%} 
 									else {
										if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")) && JAUtil.isIntegerEmpty(viewBean.getTotalControle())) { %>
												<input size="18" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" align="right" type="text" name="totalControle" maxlength="15" size="15" value="">
										<%} else {%>
											<input size="18" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" align="right" type="text" name="totalControle" maxlength="15" size="15" value="<%=viewBean.getTotalControleFormat()%>">
										<%}
									}%> 										 										
										</td>
            							<td width="20%" height="12">
            								<div align="left"></div>
            							</td>
          						</tr>
          						<tr>
            							<td width="31%" height="12">Speziale Korrektur des Einkommens</td>
            							<td colspan="2" height="12">
            							<%if (isJournalGebucht || isJournalPartiel) {%>
            							<input maxlength="15" size="18"" type="text" name="correctionSpeciale" align="right" value="<%=viewBean.getCorrectionSpecialeFormat()%>" <%=styleOuverture%>>
 										motif
									<input type="text" name="motifCorrection" size="30" maxlength="50" value="<%=viewBean.getMotifCorrection()%>" <%=styleOuverture%>>
 										<%} else {%>
 										<input size="18" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" align="right" type="text" name="correctionSpeciale" maxlength="15" size="15" value="<%=viewBean.getCorrectionSpecialeFormat()%>">
            								motif
									<input type="text" name="motifCorrection" size="30" maxlength="50" value="<%=viewBean.getMotifCorrection()%>">
										<%}%>
								</td>
            							<td width="20%" height="12">
            								<div align="left"></div>
            							</td>
          						</tr>
          						<tr>
            							<td width="31%" height="12">Journalsaldo</td>
            							<td colspan="2" height="12">
									<input type="text" size="18" class='disabled' readonly tabIndex="-1" name="totalInscritDisplay" maxlength="18"  value="<%=viewBean.getTotalInscritCorrigeFormat()%>">
								</td>
            							<td width="20%" height="12">
           								<div align="left"></div>
            							</td>
          						</tr>
          						<tr>
            							<td width="31%" height="12">Total fakturiert</td>
            							<td colspan="2" height="12">
									<input align="right" type="text" class='disabled' readonly tabIndex="-1" name="totalFacture" maxlength="18" size="18" value="">
								</td>
            							<td width="20%" height="12">
            								<div align="left"></div>
            							</td>
          						</tr>
          						<tr>
            							<td width="31%" height="2" valign="top">Bemerkung</td>
            							<td colspan="2" height="6" rowspan="2">
									<textarea name="remTexte" rows="3" style="width:8.5cm;"><%=viewBean.getRemTexte()%></textarea>
								</td>
            							<td height="2" width="20%">&nbsp;</td>
          						</tr>
          						<tr>
						            	<td width="31%" height="2">&nbsp;</td>
            							<td height="2" width="20%">&nbsp;</td>
          						</tr>
          						<tr>
            							<td width="31%" height="2">Benutzer</td>
            							<td colspan="2" height="2">
									<input type="text" class="disabled" name="proprietaireInv" maxlength="50" size="50" value="<%=viewBean.getProprietaireNomComplet()%>" readonly tabindex="-1">
									<input type="hidden" name="proprietaire" value="<%=viewBean.getProprietaire()%>">
									
								</td>
            							<td height="2" width="20%">&nbsp;</td>
          						</tr>
          						<tr>

            							<td width="31%" height="2">Erstellungsdatum</td>
            							<td colspan="2" height="2">
									<input type="text" name="date" maxlength="10" size="10" value="<%=viewBean.getDate()%>" class='disabled' readonly tabIndex="-1">
								</td>
            							<td height="2" width="20%">&nbsp;</td>
          						</tr>
          						<tr>
            							<td width="31%" height="2">Status</td>
            							<td colspan="2" height="2">
										<input name="idEtatInv" readonly class="disabled"  size="10" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdEtat(),session)%>" tabIndex="-1">
								</td>
            							<td height="2" width="20%">&nbsp;</td>
            							
            					
          						</tr>   
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>





	<%if(CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat()) || (!viewBean.getProprietaireNomComplet().equals(objSession.getUserFullName())) && !globaz.pavo.util.CIUtil.isSpecialist(session)){%>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"/>
		<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"/>
		<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"/>
		<ct:menuChange displayId="options" menuId="journal-detailDeclaration" showTab="options">
		</ct:menuChange>
	<%}else if(CIJournal.CS_DECLARATION_SALAIRES.equals(viewBean.getIdTypeInscription()) || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBean.getIdTypeInscription())){%>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"/>
			<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>"menuId="journal-detailDeclaration"/>
			<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>"menuId="journal-detailDeclaration"/>
		<ct:menuChange displayId="options" menuId="journal-detailDeclaration" showTab="options">
			
		</ct:menuChange>
<%		}else if(CIJournal.CS_DECISION_COT_PERS.equals(viewBean.getIdTypeInscription()) || CIJournal.CS_COTISATIONS_PERSONNELLES.equals(viewBean.getIdTypeInscription())){%>	
		<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailCotPers"/>
			<ct:menuSetAllParams key="libelle" value="<%=viewBean.getLibelle()%>" menuId="journal-detailCotPers"/>
		<ct:menuChange displayId="options" menuId="journal-detailCotPers" showTab="options">
			
		</ct:menuChange>		
		<%}else{%>
		<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
			<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
			
		<ct:menuChange displayId="options" menuId="journal-detail" showTab="options">
			
		</ct:menuChange>	
		
<%	} %>




<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>