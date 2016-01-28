<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "CDS0005";%>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%
globaz.draco.db.declaration.DSSaisieMasseDateReceptionViewBean viewBean = (globaz.draco.db.declaration.DSSaisieMasseDateReceptionViewBean)session.getAttribute ("viewBean");
userActionValue = "draco.declaration.saisieMasseDateReception.ajouter";
String jspLocation = servletContext + mainServletPath + "Root/tiForDeclaration_select.jsp";
int autoDigitAff = globaz.draco.util.DSUtil.getAutoDigitAff(session);
String annee = String.valueOf(globaz.globall.util.JACalendar.today().getYear()-1);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
}

function upd() {
}

function validate() {
		
	 document.forms[0].elements('userAction').value="draco.declaration.saisieMasseDateReception.ajouter";
     return true;
}
function cancel() {
	document.forms[0].elements('userAction').value="draco.declaration.saisieMasseDateReception.afficherApresAnnulation";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="draco.declaration.saisieMasseDateReception.supprimer";
		document.forms[0].submit();
	}
}
function init() {}

function updateInfoAffilie(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
 		document.getElementById('descriptionTiers').value = tag.select[tag.select.selectedIndex].nom;
 		document.getElementById('affiliationId').value = tag.select[tag.select.selectedIndex].affiliationId;
 		document.getElementById('affilieDesEcran').value = tag.select[tag.select.selectedIndex].affilieDesEcran;
 		document.getElementById('affilieRadieEcran').value = tag.select[tag.select.selectedIndex].affilieRadieEcran;
 		document.getElementById('typeAffiliationEcran').value = tag.select[tag.select.selectedIndex].typeAffiliationEcran;
 	}
}
function resetInfoAffilie() {
 	document.getElementById('descriptionTiers').value = '';
 	document.getElementById('affiliationId').value = '';
 	document.getElementById('affilieDesEcran').value = '';
 	document.getElementById('affilieRadieEcran').value = '';
 	document.getElementById('typeAffiliationEcran').value = '';
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Eingabe des Empfangsdatums - Detail <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						        <TR>
                                    <td nowrap height="18" rowspan="3" width="128"><input type="hidden" name="saisieEcran" value="true" tabindex="-1"> Partner </td>
                                    <td>
                                    	<%if (viewBean.isNew()) {%>
                                    	<ct:FWPopupList name="affilieNumero" 
											value="<%=viewBean.getAffilieForEcran()%>"
											validateOnChange="true"
											autoNbrDigit="<%=autoDigitAff%>"
                                    		jspName="<%=jspLocation%>"  minNbrDigit="3"  onChange="updateInfoAffilie(tag)" 
                                    		onFailure="resetInfoAffilie()" maxlength="15" size="15"/>
                                    	<%} else {%>
                                    	<input name="affilieNumero"  value="<%=viewBean.getAffiliation().getAffilieNumero()%>" maxlength="15" size="15" class="disabled" readonly />
                                    	<%}%>
                                    	<input type="hidden" name="affiliationId" value="<%=viewBean.getAffiliationId()%>">
                                    <td nowrap width="76">Erfasst seit</td>
                                    <td nowrap width="350">
                                    <p><input type="text" name="affilieDesEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieDesEcranFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></p>
                                    </td>
                                </TR>
                                <TR>
                                	<td><TEXTAREA id="descriptionTiers" cols="35" class="libelleLongDisabled" tabindex="-1"
										readonly="readonly" rows="4"><%=viewBean.getDescriptionTiersAvecVides()%></TEXTAREA></td>
                                    <td nowrap width="76">Abgeschlossen seit</td>
                                    <td nowrap width="350"><input type="text" name="affilieRadieEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieRadieFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></td>
                                </TR>
                                <TR>
                                	<td></td>
                                    <td nowrap width="76">Erfassungsart</td>
                                    <td nowrap width="350"><input type="text" name="typeAffiliationEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieTypeFind()%>" class="libelleLongDisabled" tabindex="-1" readOnly></td>
                                </TR>
                                <TR>
                                <td nowrap colspan="7" height="11">
                                    <hr size="4">
                                    </td>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Jahr</TD>
                                    <TD nowrap height="31" width="294">
                                    	<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getAnnee())){

                	                   		String anneeSave = "";
											if(!JadeStringUtil.isBlankOrZero(request.getParameter("annee"))){
												anneeSave = request.getParameter("annee");
											
                                    		}%>
                                    	<INPUT name="annee" size="4" maxlength="4" type="text"  value="<%=!JadeStringUtil.isBlankOrZero(anneeSave)?new Integer(anneeSave).intValue():globaz.globall.util.JACalendar.today().getYear()-1%>">
                                    	<%}else{%>
		
                                    	<INPUT name="annee" size="4" maxlength="4" type="text" value="<%=viewBean.getAnnee()%>">
                                    	<%}%>
                                    </TD>
                                    <TD nowrap height="31" width="76"></TD>
                                    <TD nowrap height="31" width="350"></TD>
                                </TR>
                                <%
									String dateRetourEff = ""; 
									if(JadeStringUtil.isBlankOrZero(request.getParameter("dateRetourEff"))){
										dateRetourEff = JACalendar.todayJJsMMsAAAA();
									}else{
										dateRetourEff = request.getParameter("dateRetourEff");
									}
								%> 
                                
                                <TR>
                                    <TD nowrap height="30" width="128">Empfangsdatum</TD>
                                    <TD nowrap height="31" width="294"><ct:FWCalendarTag name="dateRetourEff" value="<%=dateRetourEff%>"/></TD>
                                </TR>
                                 <TR>
                                    <TD nowrap height="14" width="128">Lohnbescheinigungstyp</TD>
                                     <%if (viewBean.isNew()) {
                                    	String typeSave = "";
                                    	if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getTypeDeclaration())){

                	                   		
											if(!JadeStringUtil.isBlankOrZero(request.getParameter("typeDeclaration"))){
												typeSave = request.getParameter("typeDeclaration");
											}	
											
                                    	}else{
                                    		typeSave = DSDeclarationViewBean.CS_PRINCIPALE;
                                    	}%>	
                                    <TD nowrap height="31" width="294"><ct:FWCodeSelectTag name="typeDeclaration" defaut="<%=typeSave%>" codeType="DRATYPDECL"/></TD>
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
