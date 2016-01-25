<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "CDS0006";%>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%
globaz.draco.db.declaration.DSSaisieMasseTotauxViewBean viewBean = (globaz.draco.db.declaration.DSSaisieMasseTotauxViewBean)session.getAttribute ("viewBean");
userActionValue = "draco.declaration.saisieMasseMontant.ajouter";
String jspLocation = servletContext + mainServletPath + "Root/tiForDeclarationInformation_select.jsp";
int autoDigitAff = globaz.draco.util.DSUtil.getAutoDigitAff(session);

%>
<script>var annee;</script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
var annee =<%=globaz.globall.util.JACalendar.today().getYear()-1%>;
<!--hide this script from non-javascript-enabled browsers
function add() {
}

function upd() {
}

function validate() {
	 document.forms[0].elements('userAction').value="draco.declaration.saisieMasseTotaux.valider";
     return true;
}
function cancel() {
	document.forms[0].elements('userAction').value="draco.declaration.saisieMasseTotaux.afficherApresAnnulation";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="draco.declaration.saisieMasseTotaux.supprimer";
		document.forms[0].submit();
	}
}
function init() {}

function updateInfoAffilie(tag) {
	//Si on a déclanché une erreur dans la validation, on ne relance pas le searching
	//pour ne pas perdre tous les montants entrés
	<%if(!viewBean.getMsgType().equals(globaz.framework.util.FWMessage.ERROR)){%>
	if(tag.select && tag.select.selectedIndex != -1){
 		document.getElementById('descriptionTiers').value = tag.select[tag.select.selectedIndex].nom;
 		document.getElementById('affiliationId').value = tag.select[tag.select.selectedIndex].affiliationId;
 		document.getElementById('affilieDesEcran').value = tag.select[tag.select.selectedIndex].affilieDesEcran;
 		document.getElementById('affilieRadieEcran').value = tag.select[tag.select.selectedIndex].affilieRadieEcran;
 		document.getElementById('typeAffiliationEcran').value = tag.select[tag.select.selectedIndex].typeAffiliationEcran;
 		document.getElementById('annee').value = tag.select[tag.select.selectedIndex].annee;
 		document.getElementById('dateRetourEff').value = tag.select[tag.select.selectedIndex].dateRetourEff;
 		document.getElementById('masseSalTotal').value = tag.select[tag.select.selectedIndex].masseSalTotal;
 		document.getElementById('masseACTotal').value = tag.select[tag.select.selectedIndex].masseACTotal;
 		document.getElementById('masseAC2Total').value = tag.select[tag.select.selectedIndex].masseAC2Total;
 		document.getElementById('totalControleDS').value = tag.select[tag.select.selectedIndex].totalControleDS;
 		document.getElementById('typeDeclaration').value = tag.select[tag.select.selectedIndex].typeDeclaration;
 	}
 	<%}%>
}
function resetInfoAffilie() {
 	document.getElementById('descriptionTiers').value = '';
 	document.getElementById('affiliationId').value = '';
 	document.getElementById('affilieDesEcran').value = '';
 	document.getElementById('affilieRadieEcran').value = '';
 	document.getElementById('typeAffiliationEcran').value = '';
 	document.getElementById('annee').value = '';
 	document.getElementById('dateRetourEff').value = '';
 	document.getElementById('masseSalTotal').value = '';
 	document.getElementById('masseACTotal').value = '';
 	document.getElementById('masseAC2Total').value = '';
 	document.getElementById('totalControleDS').value = '';
 	document.getElementById('typeDeclaration').value = '';
 	miseAJourAutoComplete();
}

function miseAJourAutoComplete(){
	annee = document.getElementById('annee').value;
	affilieNumeroPopupTag.updateJspName('<%=jspLocation%>?annee=' + annee + '&like=');
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Saisie des totaux - détail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						        <TR>
                                    <td nowrap height="18" rowspan="3" width="128"><input type="hidden" name="saisieEcran" value="true" tabindex="-1">Tiers</td>
                                    <td>
                                    	<%if (viewBean.isNew()) {%>
                                    	<ct:FWPopupList name="affilieNumero" 
											value="<%=viewBean.getAffilieForEcran()%>"
											validateOnChange="true"
											autoNbrDigit="<%=autoDigitAff%>"
                                    		jspName="<%=jspLocation%>"  minNbrDigit="3"  onChange="updateInfoAffilie(tag);" 
                                    		onFailure="resetInfoAffilie()" maxlength="8" size="8"
                                    		params="annee=' + annee + '"/>
                                    	<%} else {%>
                                    	<input name="affilieNumero"  value="<%=viewBean.getAffiliation().getAffilieNumero()%>" maxlength="8" size="8" class="disabled" readonly />
                                    	<%}%>
                                    	<input type="hidden" name="affiliationId" value="<%=viewBean.getAffiliationId()%>">
                                    <td nowrap width="76">Affili&eacute; d&egrave;s</td>
                                    <td nowrap width="350">
                                    <p><input type="text" name="affilieDesEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieDesEcranFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></p>
                                    </td>
                                </TR>
                                <TR>
                                	<td><TEXTAREA id="descriptionTiers" cols="35" class="libelleLongDisabled" tabindex="-1"
										readonly="readonly" rows="4"><%=viewBean.getDescriptionTiersAvecVides()%></TEXTAREA></td>
                                    <td nowrap width="76">Radi&eacute; d&egrave;s</td>
                                    <td nowrap width="350"><input type="text" name="affilieRadieEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieRadieFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></td>
                                </TR>
                                <TR>
                                	<td></td>
                                    <td nowrap width="76">Genre</td>
                                    <td nowrap width="350"><input type="text" name="typeAffiliationEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieTypeFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></td>
                                </TR>
                                <TR>
                                <td nowrap colspan="7" height="11">
                                    <hr size="4">
                                    </td>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Année</TD>
                                    <TD nowrap height="31" width="294">
                                    	<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getAnnee())){%>
                                    	<INPUT name="annee" size="4" maxlength="4" type="text"  value="<%=globaz.globall.util.JACalendar.today().getYear()-1%>" onchange="miseAJourAutoComplete();">
                                    	<%}else{%>
                                    	<INPUT name="annee" size="4" maxlength="4" type="text"  value="<%=viewBean.getAnnee()%>" onchange="miseAJourAutoComplete();">
                                    	<%}%>
                                    </TD>
                                    <TD nowrap height="31" width="76"></TD>
                                    <TD nowrap height="31" width="350"></TD>
                                    
                                </TR>
                                <TR>
                                    <TD nowrap height="30" width="128">Date de réception</TD>
                                    <TD nowrap height="31" width="294"><ct:FWCalendarTag name="dateRetourEff" value="<%=viewBean.getDateRetourEff()%>"/></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Masse salariale AVS</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseSalTotal" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseSalTotal()%>"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Masse salariale AC I</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseACTotal" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseACTotal()%>"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Masse salariale AC II</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseAC2Total" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseAC2Total()%>"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                </TR> 
                                <TR>
                                    <TD nowrap height="14" width="128">Montant de contrôle</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="totalControleDS" size="20" maxlength="20" type="text" value="<%=viewBean.getTotalControleDSFormate()%>"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Type de déclaration</TD>
                                    <%if (viewBean.isNew()) {%>
                                    <TD nowrap height="31" width="294"><ct:FWCodeSelectTag name="typeDeclaration" defaut="<%=viewBean.getTypeDeclaration()%>" codeType="DRATYPDECL"/></TD>
                                    <%} else {%>
                                    <TD nowrap height="31" width="76" colspan="2"><INPUT name="typeDeclarationEcran" value="<%=CodeSystem.getLibelle(session,viewBean.getTypeDeclaration())%>" class="disabled" readonly ></TD>
                                    <%}%>
                                </TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>