<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PT1300"/>
<c:set var="labelTitreEcran" value="JSP_CONGES_PAYES"/>
<c:set var="userActionCompteur" value="vulpecula.congepaye.compteurs.afficher" />
<c:set var="congePaye" value="${viewBean.congePaye}" />
<c:set var="travailleur" value="${viewBean.travailleur}" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="${congePaye.modifiable}" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="${congePaye.modifiable}" scope="page"/>
<c:set var="bButtonUpdate" value="false" />

<script type="text/javascript">
	var globazGlobal = globazGlobal || {};
	globazGlobal.compteurService = '${viewBean.compteurService}';
	globazGlobal.prestationsViewService = '${viewBean.prestationsViewService}';
	globazGlobal.isNouveau = ${viewBean.nouveau};
	globazGlobal.wantCotisations = ${viewBean.cotisations};
	globazGlobal.messageAnneeDebutNonVide = '<c:out value="${viewBean.messageAnneeDebutNonVide}" />';
	globazGlobal.messageAnneeFinNonVide = '<c:out value="${viewBean.messageAnneeFinNonVide}" />';
	globazGlobal.messageAnneeFinPlusGrandAnneeDebut = '<c:out value="${viewBean.messageAnneeFinPlusGrandAnneeDebut}" />';
	globazGlobal.messageDateRequise =  '<c:out value="${viewBean.messageDateRequise}" />';
	globazGlobal.messageAucunCompteur = '<c:out value="${viewBean.messageAucunCompteur}" />'
	globazGlobal.messageAucuneCaisseMetier = '<c:out value="${viewBean.messageAucuneCaisseMetier}" />'
	globazGlobal.messageLignePeriodeSaisie = '<ct:FWLabel key="JSP_LIGNE_PERIODE_SAISIE"/>'
	globazGlobal.mustSubstractCotisations = '${viewBean.mustSubstractCotisations}';
	<c:if test="${not empty viewBean.ajoutSucces}">
		globazGlobal.ajoutSucces = ${viewBean.ajoutSucces};
	</c:if>
	if(globazGlobal.ajoutSucces){
		parent.$(parent.document).trigger("ajoutSucces");
	}
</script> 

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/prestations/prestationsServices.js"></script>
<script type="text/javascript" src="${rootPath}/congepaye/congepaye_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript">
$(function() {
	$('a').click(function(event) {
		$this = $(this);
		event.preventDefault();
		parent.window.location.href = $this.attr('href');
	})		
});
</script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<style type="text/css">
	body {
		overflow : auto;
	}
	.pageDetail {
    	background-color : #226194;;
    	border: solid 1px;
    	border-color :  #88aaff #88aaff black black;
    	color : white;
    	padding : 3px 10px;
    	font-size : x-small;
    	font-family : "Lucida Sans Unicode",Verdana,Arial;
    	font-weight : bold;
    	margin-bottom : 0px;
    	text-align:center;
    	border-bottom: 1px solid gray;
    }	
    #mainWrapper {
    	background-color: #B3C4DB;
    	width: 100%;
    	height: ${tableHeight};
    }
    #title {
    	height: 10px;
    }
    #innerWrapper {
    	*margin-left:5px;
    	*margin-right:5px;
    	width: ${subTableWidth};
    }
    #mainForm {
    	margin-top:20px;
    }
    .lastModification {
    	margin:0 auto;
    	font-size : 9;
    	width : auto;
    }
    html, body {
    margin: 0px;
    padding: 0px;
    }

</style>

</head>
<body onload="doInitThings()" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();" style="width: 100%">
</div>
<div id="mainWrapper">
<div id="innerWrapper">
	<form id="mainForm" name="mainForm" action="${formAction}" method="post">
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="float:right; width: 30%;  height:100px;">
</div>
<div style="margin-top: 30px;">
<div id="mainContainer" class="blocLeft">
	<%@ include file="congepaye.jspf" %>
</div>
<div id="cotisationsBlock" style="margin-top: 100px;" class="blocLeft">
	<%@ include file="congepayecotisations.jspf" %>
</div>


<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>