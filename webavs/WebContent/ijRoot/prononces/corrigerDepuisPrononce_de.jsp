<%@page import="globaz.ij.servlet.IIJActions"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.ij.vb.prononces.IJCorrigerDepuisPrononceViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page isELIgnored ="false" %>
<%
	idEcran="PIJ3010";
	IJCorrigerDepuisPrononceViewBean viewBean = (IJCorrigerDepuisPrononceViewBean) session.getAttribute(FWServlet.VIEWBEAN);
	
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
var $btnValidate;

function validate() {
    state = true;
    	    
    $userAction.val("<%=IIJActions.ACTION_CORRIGER_DEPUIS_PRONONCE%>.modifier");

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
	
	//On spécifie quelle action doit être effectué lorsqu'on presse sur enter  
    $userAction.val("<%=IIJActions.ACTION_CORRIGER_DEPUIS_PRONONCE%>.modifier");
}

$(document).ready(function () {
	$userAction = $('[name="userAction"]');
// 	$btnValidate = $('#btnVal');
// 	$btnValidate.focus();


});



</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_CORR_PRONONCE_ECRAN_TITLE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:insert attribute="zoneMain" --%>	
	<tr>
		<td align="center">
			<table border=0 cellpadding="4" align=left>
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
						${viewBean.prononce.id}
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
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="4"><HR></td>
				</tr>
				<tr>
					<td>
						<b><ct:FWLabel key="JSP_CORR_PRONONCE_DATE_CORRECTION"/></b>
					</td>
					<td> 
						<input data-g-calendar="mandatory:true" name="dateCorrection" type="text" value=""/>
					</td> 
				</tr>
				<tr>
					<td colspan="4"></td>
				</tr>
				<tr valign="top">
					<td  colspan="1" align="left" >
						<b><ct:FWLabel key="JSP_REMARQUE"/></b> 
					</td>
					<td  colspan="3" align="left">
						<ct:FWLabel key="JSP_CORR_PRONONCE_EXPLICATION_ECRAN_TITLE"/>
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
