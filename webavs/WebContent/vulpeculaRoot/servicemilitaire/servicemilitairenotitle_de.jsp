<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PT1400"/>
<c:set var="labelTitreEcran" value="JSP_SERVICE_MILITAIRE"/>

<c:set var="servicemilitaire" value="${viewBean.servicemilitaire}" />
<c:set var="travailleur" value="${viewBean.travailleur}" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="${servicemilitaire.modifiable}" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="${servicemilitaire.modifiable}" scope="page"/>
<c:set var="bButtonUpdate" value="false" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/servicemilitaire/servicemilitaire_de.js"></script>
<style>
.border_bottom td {
  border-bottom:2px double grey;
}
.border_top td {
  border-top:2px double grey;
}
.border_left {
  border-left:2px double grey;
}
.border_right {
  border-right:2px double grey;
}
.indentLeft {
	padding-left:10px;
}
</style>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.decompteSalaireViewService = '${viewBean.decompteSalaireViewService}';
globazGlobal.prestationsViewService = '${viewBean.prestationsViewService}';
globazGlobal.dateViewService = '${viewBean.dateViewService}';
globazGlobal.isNouveau = ${viewBean.nouveau};
<c:if test="${not empty servicemilitaire.idPosteTravail}">
	globazGlobal.idPosteTravail = ${servicemilitaire.idPosteTravail};
</c:if>
globazGlobal.messagePeriodeNonVide = '<c:out value="${viewBean.messagePeriodeNonVide}" />';
globazGlobal.messagePeriodeFinNonSaisie = '<c:out value="${viewBean.messagePeriodeFinNonSaisie}" />';
globazGlobal.messagePeriodeDebutPlusGrandePeriodeFin = '<c:out value="${viewBean.messagePeriodeDebutPlusGrandePeriodeFin}" />';
globazGlobal.messageAucuneCaisseMetier = '<c:out value="${viewBean.messageAucuneCaisseMetier}" />';
globazGlobal.messageNombreJourMaximum = '<c:out value="${viewBean.messageNombreJourMaximum}" />';
globazGlobal.messageNombreJourMinimum = '<c:out value="${viewBean.messageNombreJourMinimum}" />';
globazGlobal.beneficiaireTravailleur = ${viewBean.beneficiaireTravailleur};
globazGlobal.messageVersementAPG = '<c:out value="${viewBean.messageVersementAPG}" />';
<c:if test="${not empty viewBean.ajoutSucces}">
	globazGlobal.ajoutSucces = ${viewBean.ajoutSucces};
</c:if>
if(globazGlobal.ajoutSucces){
	parent.$(parent.document).trigger("ajoutSucces");
}
</script>


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
<div id="mainWrapper">
<div id="innerWrapper">
	<form id="mainForm" name="mainForm" action="${formAction}" method="post">
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="float:right; width: 30%; height:100px">
</div>
<div>
	<%@ include file="servicemilitaire.jspf" %>
</div>
</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>