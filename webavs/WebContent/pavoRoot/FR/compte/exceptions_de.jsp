<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="java.util.HashSet"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.compte.CIExceptionsViewBean viewBean = (globaz.pavo.db.compte.CIExceptionsViewBean)session.getAttribute("viewBean");

	selectedIdValue=viewBean.getIdExceptions();
	userActionValue="pavo.compte.exceptions.modifier";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select_par.jsp";
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	idEcran ="CCI0027";
%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT>

function add(){
document.forms[0].elements('userAction').value="pavo.compte.exceptions.ajouter";}

function upd(){}

function validate(){
state = validateFields();
if(document.forms[0].elements('_method').value == "add")
document.forms[0].elements('userAction').value="pavo.compte.exceptions.ajouter";
else
document.forms[0].elements('userAction').value="pavo.compte.exceptions.modifier";

document.getElementById('nomJsp').value = document.getElementById('infoAssure').value;
return state;}

function cancel(){
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.exceptions.afficher";}

function del(){
if(window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
document.forms[0].elements('userAction').value="pavo.compte.exceptions.supprimer";
document.forms[0].submit();
}
}
function init(){}
function updateInfoAffilie(tag) {
	if (tag.select) {
 		document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('langueCorrespondance').value = tag.select[tag.select.selectedIndex].langue;
	}
}
function resetInfoAffilie() {
 	document.getElementById('infoAffilie').value = '';
 	document.getElementById('langueCorrespondance').value = '';
}
function updateInfoAssure(tag) {
	if (tag.select){
 		document.getElementById('infoAssure').value = tag.select[tag.select.selectedIndex].nom;
		document.forms[0].elements('dateNaissance').value = tag.select[tag.select.selectedIndex].date;
		document.forms[0].elements('sexe').value = tag.select[tag.select.selectedIndex].sexeFormate;
		document.forms[0].elements('pays').value = tag.select[tag.select.selectedIndex].paysFormate;
 	}
}
function resetInfoAssure() {
 	document.getElementById('infoAssure').value = '';
 	document.forms[0].elements('sexe').value = '';
	document.forms[0].elements('pays').value = '';
	document.forms[0].elements('dateNaissance').value = '';
}
$(function(){
<%
String warningToDisplay="";
if(!JadeStringUtil.isBlankOrZero(warningToDisplay = viewBean.getWarningEmployeurSansPersoOrAccountZero())){%>
globazNotation.utils.consoleWarn("<%=warningToDisplay%>",'Avertissement',true);
<%}%>});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une exception<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<tr>
						<td>NSS</td>
						<td><nss:nssPopup validateOnChange="true" loadValuesFromRequest="false" value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"
							name="numeroAvs" cssclass="visible" jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" 
							newnss="<%=viewBean.getNumeroAvsNNSS()%>"
							nssMinNbrDigit="7" nssAutoNbrDigit="10"  
							onChange="updateInfoAssure(tag);" onFailure="resetInfoAssure()"/>
						<input type="text" class="disabled" readonly name="infoAssure" size="73" maxlength="73" value="<%=viewBean.getInfoAssure()%>" tabIndex="-1"> 
						<input type="hidden" name="nomJsp" value=""/>
						<input type="hidden" name="isJsp" value="true"/>
						<input type="hidden" name="wantCheckDoublon" value="true"/>
						</td>
						</tr>
						<tr>
							<td>
								Date de naissance &nbsp;
							</td>
							<td>
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissance()%>">
								Sexe &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelle()%>">
								&nbsp;Pays &nbsp;
								<input type="text" size = "54" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormate()%>">
							</td>
						</tr>
					
						<% 
						
						String chkMemoriserNumeroAffilieParamValue = JadeStringUtil.isBlankOrZero(request.getParameter("chkMemoriserNumeroAffilie"))?"": request.getParameter("chkMemoriserNumeroAffilie");
						String defaultNumeroAffilie = viewBean.getAffilie();
						String defaultInfoAffilie = viewBean.getInfoAffilie();
						String defaultLangue = viewBean.getLangueCorrespondance();
						
						if("on".equalsIgnoreCase(chkMemoriserNumeroAffilieParamValue) && !JadeStringUtil.isBlankOrZero(request.getParameter("affilie")) &&  !JadeStringUtil.isBlankOrZero(request.getParameter("infoAffilie"))){
							defaultNumeroAffilie = request.getParameter("affilie");  	
							defaultInfoAffilie = request.getParameter("infoAffilie"); 
							defaultLangue = request.getParameter("langueCorrespondance"); 
						}
							 
						%>
						<tr>
						<td>Numéro d'affilié</td>
						<td><ct:FWPopupList name="affilie" value="<%=defaultNumeroAffilie%>" jspName="<%=jspLocation2%>" 
						autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3" size="15" maxlength="15" 
						onChange="updateInfoAffilie(tag);" onFailure="resetInfoAffilie()" validateOnChange="true"/>
						<input type="text" class="disabled" readonly name="infoAffilie" size="73" maxlength="73" value="<%=defaultInfoAffilie%>" tabIndex="-1">
						<%if("add".equalsIgnoreCase(request.getParameter("_method"))) { %>
							&nbsp;&nbsp;<input type="checkbox" id="chkMemoriserNumeroAffilie" name="chkMemoriserNumeroAffilie" <%="on".equalsIgnoreCase(chkMemoriserNumeroAffilieParamValue)?"checked":""%> >&nbsp;Mémoriser
						<%}%>
						</td>
						</tr>
						 <tr>
							<td >							
								Langue de correspondance
							</td>
						<td>
					  <%
						HashSet except = new HashSet();
						except.add("503003");
						except.add("503005");
						except.add("503006");
						except.add("503007");
						%>
						<ct:FWCodeSelectTag codeType="PYLANGUE" name="langueCorrespondance" defaut="<%=defaultLangue%>" wantBlank="false" except="<%=except%>"/>			
						</td>			            	 
					   </tr>
						
						
						<tr>
						<td>Date d'engagement</td>
						<td>
		 			 		<ct:FWCalendarTag name="dateEngagement" value="<%=viewBean.getDateEngagement()%>"/>
						</td>
						</tr>
						<tr>
						<td>Date de licenciement</td>
						<td>
							<ct:FWCalendarTag name="dateLicenciement" value="<%=viewBean.getDateLicenciement()%>"/>
						</td>
							
						</tr>
						<tr>
							<td>Date d'enregistrement</td>
							<TD><ct:FWCalendarTag name="dateEnregistrement" value="<%=globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getDateEngagement())?globaz.globall.util.JACalendar.todayJJsMMsAAAA():viewBean.getDateEnregistrement()%>"/></TD>
						</tr>					
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>