<%@ include file="/theme/detail/header.jspf" %>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.al.vb.decision.ALDecisionFileAttenteViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>


<%-- tpl:insert attribute="zoneInit" --%>
<%
ALDecisionFileAttenteViewBean viewBean = (ALDecisionFileAttenteViewBean) session.getAttribute("viewBean");

	btnValLabel = objSession.getLabel("VALIDER");
	
	bButtonNew = false;
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonCancel = false;

	boolean hasCreateRight = objSession.hasRight("al.decision.decisionFileAttente", FWSecureConstants.UPDATE);
	bButtonValidate = hasCreateRight;
	
	idEcran="AL0035";
	
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="globaz.al.vb.decision.ALDecisionListViewBean"%><script type="text/javascript">


function add() {	
}

function upd() { 
}

function validate() {
	document.forms[0].elements('userAction').value="al.decision.decisionFileAttente.executer";
	state = validateFields();
    return state; 
}

function cancel() {
}

function del() {	
}

function init(){
// 	alert("init");
// 	sdfg
}

function postInit(){
// 	alert("postInit");
// 	asdf
}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0035_TITRE" />
			<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<tr>
 		<td>
 			<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x">
				<table width="100%" align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
						<tr>
							<td style="padding:5px;">
			                   	<h4>Description</h4><br>
			                   	<ul>
									<li>Ce traitement permet d'éditer les décisions mises en file d'attente</li>
								</ul>
								<br>&nbsp;
							</td>
						</tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	
 	<tr><td>&nbsp;<td></tr>
	<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0035zone" class="zone">
				
				
				<tr>
					<td><ct:FWLabel key="AL0035_UTILISATEUR"/></td>
					
					<td>
					<ct:FWListSelectTag name="utilisateurSelectionne" data="<%=viewBean.getAlUsers()%>" defaut="<%=viewBean.getIdGestionnaireSelectionne()%>"  />
					</td>
					
					<td>&nbsp;</td>
					
					<td></td>
					
					<td><ct:FWLabel key="AL0035_DATE_IMPRESSION"/></td>
					<td>
					
					<input id="dateImpression" name="dateImpression" class="clearable" type="text"
								 value="<%= viewBean.getDateImpression() != null ? viewBean.getDateImpression() : "" %>"
								data-g-calendar="mandatory:true" />
								
					</td>	
							
				</tr>
				
				<tr>
				
					<td><ct:FWLabel key="AL0035_INSERTION_GED"/></td>
					<td><input  id="insertionGed"  name="insertionGed" type="checkbox" <%=(viewBean.isInsertionGed())?"CHECKED":""%>  /></td>
					
					<td>&nbsp;</td>				
					<td></td>
					
					<td><ct:FWLabel key="AL0035_EMAIL"/></td>
					<td><input id="email" name="email"  type="text"    value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/></td>	
								
				</tr>
				
				</table>
			</td>
		</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
	
				