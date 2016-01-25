<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.nnss.CIConcordanceNNSSViewBean viewBean = (globaz.pavo.db.nnss.CIConcordanceNNSSViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getConcordanceId();
	userActionValue = "pavo.nnss.concordanceNNSS.modifier";
	idEcran = "CCI0036";
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";

%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script>
	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="pavo.nnss.concordanceNNSS.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
	}
	function upd() {
	}
	function add() {
    	document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.ajouter"
	}
	
	function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.nnss.concordanceNNSS.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.nnss.concordanceNNSS.modifier";
    
    return state;

	}
	function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="pavo.nnss.concordanceNNSS.afficher";
	}
	function updateEmployeur(tag){
		if (tag.select) {
			document.getElementById('infoAff').value = tag.select[tag.select.selectedIndex].nom;
		}
	}
	
	function resetEmployeur(){
		document.getElementById('infoAff').value = "";
	}
	function updateInfoAssure(tag){
		if (tag.select) {
			document.getElementById('assureInfo').value = tag.select[tag.select.selectedIndex].etatFormate;
			document.getElementById('nomPrenom').value = tag.select[tag.select.selectedIndex].nom;
			
		}
	}
	function resetInfoAssure(){
		document.getElementById('assureInfo').value = "";
		document.getElementById('nomPrenom').value ="";
	}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Concordance entre No Avs / NNSS / No Affillié<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
							No Affilié
							</td>
							
							<td>
							<ct:FWPopupList
							value="<%=viewBean.getNoAffillie()%>"
							onChange="updateEmployeur(tag);"
							onFailure="resetEmployeur();" name="noAffillie"
							size="15" jspName="<%=jspLocation2%>" className="hiddenPage"
							minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>" />
							&nbsp;&nbsp;
								<input name="infoAff" tabindex = "-1" type ="text" class="disabled" value = "<%=viewBean.getInfoAffilie()%>">
							</td>
						</tr>
						
						<tr>
						<td>No Avs</td>
						<td><nss:nssPopup validateOnChange="true" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNoAvs())%>"
							name="noAvs" cssclass="visible" jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" 
							newnss="false"
							nssMinNbrDigit="7" nssAutoNbrDigit="10"  
							onChange="updateInfoAssure(tag);" onFailure="resetInfoAssure()"/>
						&nbsp;
							<input type ="text" tabindex = "-1" class="disabled" readonly name="nomPrenom"  size = "40" value = "<%=viewBean.getNomPrenom()%>">
						</td>
						</tr>
						<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<input type ="text" tabindex = "-1" class="disabled" readonly name="assureInfo"  size = "20" value = "<%=viewBean.getEtatFormate()%>">
						</td>
						</tr>
						<tr>
							<td>
								NNSS
							</td>
							<td>
								<nss:nssPopup value="<%=viewBean.getNNSSSansPrefixe()%>" 
								newnss="true"
								name="nnss"
								avsMinNbrDigit="99" avsAutoNbrDigit="99" 
								nssMinNbrDigit="99" nssAutoNbrDigit="99"  />
							</td>						
						</tr>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>