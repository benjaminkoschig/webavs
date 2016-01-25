<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.lot.PCListViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PPC0200";
	PCListViewBean viewBean = (PCListViewBean)session.getAttribute("viewBean");
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
	$buttonMonth.focus();
	//$buttonMonth.button("disable");
	setTimeout(function () {
		$buttonMonth.focus();
	},100)
},"Gestion du boutton");

getDate = function () {
	return  $dateMonth.val();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="JSP_LISTE_RECAP_D_TITRE"/> 
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<tr>

		<td colspan="2" align="center">
			<label for="dateMonth">Date </label>
			<input tabindex="1"  type="text" id="dateMonth" data-g-calendar="mandatory:true, type:month" value="<%=viewBean.getDateDernierPaiementt()%>"> 
		</td> 
	</tr>	
	<tr><td colspan="2" align="center">
			<a  tabindex="2" id="monthButton" data-g-download="docType:xls,
					dynParametres: getDate,
                    serviceClassName: ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,
                    serviceMethodName: createListeRecap,
                    docName: listeMontantMutationPCA"
			/>
	</td></tr>				
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>