<%@page import="globaz.pegasus.vb.revenusdepenses.PCListAllocationsFamilialesViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.lot.PCListViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PPC0201";
	PCListAllocationsFamilialesViewBean viewBean = (PCListAllocationsFamilialesViewBean)session.getAttribute("viewBean");
	String rootPath = servletContext+(mainServletPath+"Root");
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript" src="<%=rootPath %>/scripts/jadeBaseFormulaire.js"></script>

<SCRIPT language="javascript">
var $dateMonth, $buttonMont;
$(function(){
	$("#btnCtrlJade").hide();
	$dateMonth = $("#dateMonth");
	$buttonMonth =  $("#monthButton") ;
	$dateMonth.focusout(function () {
		if($.trim(this.value).length){
			$buttonMonth.button("enable");
		} else {
			$buttonMonth.button("disable");
		}
	});
});

jsManager.addAfter(function (){
	$buttonMonth.button("disable");
},"Gestion du bouton");

getDate = function () {
	return  $dateMonth.val();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="JSP_LISTE_CAS_AF_D_TITRE"/> 
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<tr>

		<td colspan="2" align="center">
			<label for="dateMonth"><ct:FWLabel key="JSP_LISTE_CAS_AF_D_DATE"/> </label>
			<input type="text" id="dateMonth" data-g-calendar="mandatory:true, type:month"> 
		</td>
	</tr>		
		<tr><td colspan="2" align="center">
			<a id="monthButton" data-g-download="docType:xls,
								dynParametres: getDate,
			                    serviceClassName: ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,
			                    serviceMethodName: createListeAllocationsFamiliales,
			                    docName: listeAllocationsFamiliales"
			/>
					
		</td>
	</tr>		
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>