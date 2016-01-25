<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0001";
	globaz.naos.db.affiliation.AFAutreDossierViewBean viewBean = (globaz.naos.db.affiliation.AFAutreDossierViewBean)session.getAttribute ("viewBean");
	bButtonDelete = false;
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	userActionValue = "naos.affiliation.autreDossier.modifier";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
}

function upd() {
}

function validate() {
	state = validateFields(); 
	//document.forms[0].elements('userAction').value="naos.affiliation.autreDossier.modifier";
	return (state);
}

function cancel() {
}

function del() {
	
}

function init(){
	if(document.getElementById('numAffilie').value != "") {
		var error = "<%=viewBean.getMsgType()%>";
		if(error != "<%=globaz.framework.bean.FWViewBeanInterface.ERROR%>") {
		action(COMMIT);
	}
}
}

function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('numAffilie').value =  tag.select[tag.select.selectedIndex].value;
		parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=naos.affiliation.autreDossier.modifier&numAffilie=" + document.getElementById('numAffilie').value + "&forAction="+document.getElementById('forAction').value;
	}	
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Autre dossier
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<td>
								<table>	
								<tr> 
									<td width="100" nowrap>N° affili&eacute;</td>         
									<td>
										<ct:FWPopupList 
											name="numAffilie" 
											value="<%=viewBean.getNumAffilie()%>" 
											className="libelle" 
											jspName="<%=jspLocation%>" 
											autoNbrDigit="<%=autoDigiAff%>" 
											size="25"
											onChange="updateNumAffilie(tag);"
											minNbrDigit="3"/>
										<!--IMG
											src="<%=servletContext%>/images/down.gif"
											alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
											title="presser sur la touche 'flèche bas' pour effectuer une recherche"
											onclick="if (document.forms[0].elements('numAffilie').value != '') numAffiliePopupTag.validate();"-->
									</td>
									<td>		
										<%
											Object[] tiersMethodsName= new Object[]{
												new String[]{"setNumAffilie","getNumAffilieActuel"},
												new String[]{"setIdTiers","getIdTiers"},};
											Object[] tiersParams = new Object[]{
												new String[]{"selection","_pos"},};
											String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/affiliation/autreDossier_de.jsp";	
										%>
										<ct:FWSelectorTag 
											name="tiersSelector" 
											
											methods="<%=tiersMethodsName%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.tiers.chercher"
											providerActionParams ="<%=tiersParams%>"
											redirectUrl="<%=redirectUrl%>"/>
										<input type="hidden" value="<%=viewBean.getIdTiers()%>" name="idTiers">
										<input type="hidden" value="<%=request.getParameter("forAction")%>" name="forAction">
									</td>
								</tr>
								</table>
							</td>
							<TD width="192"><% bButtonValidate = false; bButtonCancel = false;%></TD>
						</TR>   
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>