<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP0001";
	globaz.phenix.db.principale.CPAutreDossierViewBean viewBean = (globaz.phenix.db.principale.CPAutreDossierViewBean )session.getAttribute ("viewBean");
	bButtonDelete = false;
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	userActionValue = "phenix.principale.autreDossier.afficher";
%>
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
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
	String numAffilie = request.getParameter("numAffilie");
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>
top.document.title = "Decision - Autre dossier"
function add() {
}
function upd() {
}
function validate() {
	state = validateFields(); 
	document.forms[0].elements('userAction').value="phenix.principale.autreDossier.modifier";
	return (state);
}
function cancel() {
}
function del() {
	
}
function init(){}

function updateIdAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		disableBtn(document.getElementById('btnVal'));
		document.getElementById('idTiers').value=tag.select[tag.select.selectedIndex].idTiers;
	}
}

function updateNumAffilie(data){
	document.getElementById('numAffilie').value = data;
}
function selectAff(tag){
	if(tag.select && tag.select.selectedIndex != -1){
	parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=phenix.principale.autreDossier.modifier&numAffilie="+tag.select[tag.select.selectedIndex].numAffilie;
	}
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<div style="width: 100%;">neue Mitglied-Nr.</div>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
      	 <TR>
			<td><table>	
	          <tr> 
	             <td width="100" nowrap>Mitglied-Nr. </td>
	             <td>
	            	<ct:FWPopupList 
	            		name="numAffilie" 
	            		value="<%=viewBean.getNumAffilie()%>" 
	            		className="libelle" 
	            		jspName="<%=jspLocation%>" 
	            		autoNbrDigit="<%=autoDigiAff%>" 
	            		size="25"
	            		onChange="updateIdAffilie(tag); updateNumAffilie(document.getElementById('numAffilie').value);selectAff(tag);"
	            		minNbrDigit="3"
	            		/>
	            	<SCRIPT>
	            		document.getElementById("numAffilie").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	            	</SCRIPT>
	            </td>
	            
	            <td>		
		 		  <%
					Object[] tiersMethodsName= new Object[]{
						new String[]{"setNumAffilie","getNumAffilieActuel"},
						new String[]{"setIdTiers","getIdTiers"},
					};
					Object[] tiersParams = new Object[]{
						new String[]{"selection","_pos"},
					};
					String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/autreDossier_de.jsp";	
					%>
					<ct:ifhasright element="pyxis.tiers.tiers.chercher" crud="r">
					<ct:FWSelectorTag 
						name="tiersSelector" 
						
						methods="<%=tiersMethodsName%>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.tiers.chercher"
						providerActionParams ="<%=tiersParams%>"
						redirectUrl="<%=redirectUrl%>"/>
						</ct:ifhasright>
				<input type="hidden" value="<%=viewBean.getIdTiers()%>" name="idTiers"></td>
			  </tr>
		
			</table></td>
			<TD width="192"></TD>
          </TR>   
   	  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>