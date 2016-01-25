<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="globaz.jade.common.Jade"%>
<%@page import="globaz.orion.vb.swissdec.EBPucsValidationListViewBean"%>
<%@page import="globaz.globall.db.BSession"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>


<%
	idEcran="EB0003";
	
	EBPucsValidationListViewBean viewBean = (EBPucsValidationListViewBean) session.getAttribute("viewBean");
	
	String action =  servletContext + mainServletPath + "?userAction=orion.swissdec.pucsValidationDetail.afficher&selectedId=";	
	String afficherContenuRefuser = servletContext + mainServletPath + "?userAction=orion.swissdec.pucsValidationList.afficher&contenu=refuser";	
	String afficherContenuAValider = servletContext + mainServletPath + "?userAction=orion.swissdec.pucsValidationList.afficher&contenu=avalider";	
%>
 

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/> 
<meta name="Context_URL" content="<%=servletContext%>"/> 
<meta name="formAction" content="<%=formAction%>"/>   
<meta name="TypePage" content="AJAX"/> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>
<%-- <script type="text/javascript" src="<%=servletContext%>/scripts/jquery-latest.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=servletContext%>/scripts/jquery.tablesorter.js"></script>  --%>

<%@ include file="/jade/notation/notationLibJs.jspf" %> 
<script type="text/javascript">

jQuery.fn.liveUpdate = function($list, selectorTargetTotal){
	var $selectorTargetTotal;
	
	if(selectorTargetTotal){
		$selectorTargetTotal = $(selectorTargetTotal);
	}
	
	if ( $list ) {
		cache = $list.map(function(){
			return jQuery(this).text().toLowerCase();
		});
		this.keyup(filter).keyup();
	}

	
	function filter(){
		var compteur = 0;
		var term = jQuery.trim( jQuery(this).val().toLowerCase() ), scores = [], n_nbTables = 0;
		if ( !term ) {
			$list.show();
			compteur =  $list.length;
		} else {
			$list.hide();
			cache.each(function(i){
				if ( this.indexOf( term ) > -1 ) {
					var $that = jQuery($list[i]);
					jQuery($list[i]).show();
					scores.push($list[i]);
					compteur++;
				}
			});
		}
		if(selectorTargetTotal){
			$selectorTargetTotal.text(compteur);
		}
	}
	return this;
};

$(document).ready(function() { 
	
	$("#search").liveUpdate($("#tablePucs > tbody:first").children("tr"), "#total");
	
	$("#tablePucs > tbody:first").on("click","td", function () {
		var $tr = $(this).closest("tr");
		
		if($tr.attr("lock")==="false" || $tr.attr("affExistante")==="false") {
			if(!$("#refuser").is(':checked')) {
				window.location="<%=action%>"+$tr.attr("id");
			} else {
				window.location="<%=action%>"+$tr.attr("id")+"&refuser=true";
			}
		}
	});
	
 	if('${viewBean.contenu}'==="refuser"){
 		$("#refuser").prop("checked",true);
 	}
	
	$("#refuser").click(function(){
		if(this.checked) {
			$(location).attr('href', '<%=afficherContenuRefuser%>')
		} else {
			$(location).attr('href', '<%=afficherContenuAValider%>')
		}
	});
}); 
</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<style>

	.centre {
		vertical-align: middle  !important; 
		text-align: center !important;
		text-align: center;
	}
	
	.unlock {
		cursor: pointer; cursor: hand;
	}
</style>

<%-- /tpl:insert --%>
<TITLE><%=idEcran%></TITLE>
</HEAD>
<BODY>
<table cellspacing="0" cellpadding="0" style="background-color: #B3C4DB; width: 100%; height: <%=tableHeight%>;">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="thDetail">
				<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="ORION_JSP_GEB0003_TITRE_LIST_FICHIERS_SWISSDEC"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>


	 <tr>
		<td colspan="3">&nbsp;</td>		
	 </tr>
	  <tr>
		<td colspan="3"><input type="checkbox" name="refuser" id="refuser"/> <ct:FWLabel key="ORION_JSP_GEB0004_AFFICHE_REFUSER"/></td>		
	 </tr>
			<table id="tablePucs" class="table table-bordered table-hover table-condensed">
			<caption class ="text-left"><ct:FWLabel key="RECHERCHE"/><input id="search"  type="text"/></caption>
				<thead>
					<th class="centre"><ct:FWLabel key="NO_AFFILIE"/></th>
					<th ><ct:FWLabel key="NOM"/></th>
					<th class="centre"><ct:FWLabel key="ANNEE"/></th>
					<th class="centre"><ct:FWLabel key="DATE_RECEPTION"/></th>
					<th class="montant"><ct:FWLabel key="TOTAL_CONTROLE"/></th>
					<th class="centre"><ct:FWLabel key="NB_SALAIRE"/></th>
					<th class="centre"><ct:FWLabel key="AF_SEUL"/></th>
					<th class="centre"><ct:FWLabel key="NOM_FICHIER"/></th>
				</thead>
				<tbody>
					<c:forEach var="pucs" varStatus="status" items="${viewBean.listPucsFile}">
						
						<tr id="${pucs.id}" class="${pucs.numeroAffilie} <c:if test="${!pucs.isLock() || !pucs.isAffiliationExistante()}">unlock</c:if>" lock="${pucs.isLock()}" affExistante="${pucs.isAffiliationExistante()}"> 
							<td class="centre">${pucs.numeroAffilie}</td> 
							<td style="vertical-align: middle;" > 
								<INPUT type="hidden" name="userAction" value=""/>	
								<c:if test="${pucs.forTest}">
									<i title ="<ct:FWLabel key='ORION_JSP_GEB0004_PUCS_TEST_FILE'/>"  class="icon-check"></i>
								</c:if>
								<c:if test="${pucs.duplicate}">
									<i title ="<ct:FWLabel key='ORION_JSP_GEB0004_PUCS_DUPLICATE'/>"  class="icon-warning-sign"></i>
								</c:if>
								<c:if test="${pucs.isLock() && pucs.isAffiliationExistante()}"><i title ="<ct:FWLabel key='NIVEAU_SECURITE_INSUFFISANT'/>" class="icon-lock"></i></c:if> 
								<c:if test="${!pucs.isAffiliationExistante()}"><i title ="<ct:FWLabel key='ORION_JSP_GEB0004_AUCUNE_AFFILIATION'/>" class="icon-ban-circle"></i></c:if>
								${pucs.nomAffilie}
							</td> 
							<td class="centre">${pucs.anneeDeclaration}</td> 
							<td class="centre">${pucs.dateDeReception}</td> 
							<td class="montant" style="vertical-align: middle;" >${pucs.totalControle}</td> 
							<td class="centre">${pucs.nbSalaires}</td> 
							<td class="centre">
								<c:if test="${pucs.afSeul}">
									<i class="icon-ok"></i>
								</c:if>
							</td> 
							<td class="left">${pucs.id}.xml</td> 
						</tr> 
					
					</c:forEach>
				</tbody>
				<tbody>
					<tr>
						<td class="left" colspan="8"><strong><ct:FWLabel key="TOTAL_FICHIER"/><span id="total">${viewBean.listPucsFile.size()}</span></strong></td>							
					</tr>
				 </tbody>
			</table>








	 <tr>
		<td colspan="3"></td>		
	 </tr>

	 		<%-- /tpl:insert --%>
					</TBODY>
				</TABLE>
			</FORM>
			</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
	 	<TR valign ="bottom">

				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>