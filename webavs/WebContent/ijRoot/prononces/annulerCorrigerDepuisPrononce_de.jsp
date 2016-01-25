<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.ij.vb.prononces.IJAnnulerCorrigerDepuisPrononceViewBean"%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page isELIgnored ="false" %>
<%
	idEcran="PIJ3011";
	IJAnnulerCorrigerDepuisPrononceViewBean viewBean = (IJAnnulerCorrigerDepuisPrononceViewBean) session.getAttribute(FWServlet.VIEWBEAN);
	
	bButtonUpdate = true;
	bButtonDelete = false;
	bButtonValidate = true;
%>


<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

var $userAction;

function validate() {
    state = true;
    	    
    $userAction.val("<%=IIJActions.ACTION_ANNULER_CORRIGER_DEPUIS_PRONONCE%>.supprimer");

    return state;
}
    

function add() {}
function upd() {}
function cancel() {
	document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";	
}
function del() {}

function init () {
	parent.isNouveau = true;
}

$(document).ready(function () {
	$userAction = $('[name="userAction"]');
});

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_ANNULER_CORRIGER_ECRAN_TITLE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:insert attribute="zoneMain" --%>	
	<tr>
		<td>
			<table border=0 cellpadding="2" align=left>
				<tr>
					<td>
						<b><ct:FWLabel key="JSP_FORMULAIRE_DETAIL_REQUERANT"/></b>
					</td>
					<td colspan="3">
						${viewBean.detailRequerantDetail}
						<input	type="hidden" 
								name="noAVS" 
								value="${viewBean.noAVS}" /> 
					</td>
				</tr>
				<tr>
					<td width="10%">
						<b><ct:FWLabel key="JSP_NO_PRONONCE"/></b>
					</td>
					<td width="5%">			
						${viewBean.idPrononce}
					</td>
					<td width="15%">
						<b><ct:FWLabel key="JSP_ANNONCES_PERIODE_DU"/> 
						<ct:FWLabel key="JSP_REP_R_PRONONCE"/>	</b>			
					</td>
					<td width="50%">
						${viewBean.prononce.dateDebutPrononce} - ${viewBean.prononce.dateFinPrononce} 
					</td>
				</tr>
				<tr>
					<td colspan="4"><HR></td>
				</tr>

				<tr valign="top">
					<td colspan="4" >				
						<%if(viewBean.getIsAnnulerPossible()) { %>
						<b><ct:FWLabel key="JSP_REMARQUE"/></b>
										
							<ct:FWLabel key="JSP_ANNULER_CORRIGER_DEPUIS_EXPLICATION"/>							
						
						<%} else {%>
							<div data-g-boxmessage="type:ERROR"> 
								${viewBean.messageAnnulerInterdit}						
							</div>		
							<% bButtonValidate = false; 
						} %>
					</td>
				</tr>
								
			</table>	
		</td>
	</tr>
			

			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
