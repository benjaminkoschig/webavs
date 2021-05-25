<%@page import="globaz.pegasus.vb.decision.PCListDecisionsValideesViewBean"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.lot.PCListViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PPC0203";
PCListDecisionsValideesViewBean viewBean = (PCListDecisionsValideesViewBean)session.getAttribute("viewBean");
	String rootPath = servletContext+(mainServletPath+"Root");
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript" src="<%=rootPath %>/scripts/jadeBaseFormulaire.js"></script>

<SCRIPT language="javascript">
var $buttonMont;
$(function(){
	$("#btnCtrlJade").hide();
});

jsManager.addAfter(function (){
	$("#monthButton").button("disable");
},"Gestion du bouton");


getDateDebut = function () {
	return  $("#dateMonthDebut").val();
}

getDateFin = function () {
	return  $("#dateMonthFin").val();
}
getIdGest = function () {
	var idgest=$("[name='idGestionnaire']").val();
	if(idgest!=null){
	return  $("[name='idGestionnaire']").val();
	}else{
		return "";
	}
}

getMyParams=function(){
	return getDateDebut()+","+getDateFin();
}
function checkEmpty(){
	var isEmpty=false;
	$("#dateMonthDebut,#dateMonthFin").each(function(){
		isEmpty=isEmpty||($.trim(this.value).length==0);
	});
	if(!isEmpty){
		$("#monthButton").button("enable");
	} else {
		$("#monthButton").button("disable");
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="JSP_LISTE_DECISIONS_VALIDEES_D_TITRE"/> 
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<tr>
		<td colspan="2" align="center">
			<label for="dateMonthDebut"><ct:FWLabel key="JSP_LISTE_DECISIONS_VALIDEES_D_DATE_DEBUT"/> </label>
			<input type="text" id="dateMonthDebut" data-g-calendar="mandatory:true, type:month" onchange="checkEmpty()">
			<label for="dateMonthFin"><ct:FWLabel key="JSP_LISTE_DECISIONS_VALIDEES_D_DATE_FIN"/> </label>
			<input type="text" id="dateMonthFin" data-g-calendar="mandatory:true, type:month" onchange="checkEmpty()">
		</td>
	</tr>
		<tr><td colspan="2" align="center">
			<a id="monthButton" data-g-download="docType:xls,
								dynParametres: getMyParams,
			                    serviceClassName: ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,
			                    serviceMethodName: createListeDecisionsValidees,
			                    docName: listeDecisionsValidees" />
	
		</td>
	</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
