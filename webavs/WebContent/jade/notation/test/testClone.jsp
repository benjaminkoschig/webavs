<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<% String servletContext ="../../../";%>
<html lang="fr">
	<head>
		<meta name="User-Lang" content="FR" /> 
		<meta name="Context_URL" content="<%=request.getContextPath()%>" /> 
		<meta name="formAction" content="/webavs/pegasus" />
		<meta name="TypePage" content="AJAX11" /> 
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

		<title>Test Clone</title>

		<script type="text/javascript" src="<%=servletContext%>scripts/jquery.js"> </script>
		<script type="text/javascript" src="<%=servletContext%>scripts/jquery-ui.js"> </script>
		<script type="text/javascript" src="<%=servletContext%>scripts/ValidationGroups.js"> </script>
		<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"> </script>
		<%@ include file="../notationLibJs.jspf" %> 
		<script type="text/javascript" src="testClone.js"></script>

		<link type="text/css" href="testNotation.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>theme/jquery/jquery-ui.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>theme/master.css" rel="stylesheet" /> 
		<style type="text/css">
			#clones{
				padding-bottom: 100px;
			}
		</style>
	</head>
	<body>
		<form action="#">
			<div id="event">
				<div>
					<h3>Verrouillage:</h3> 
					<span id='locker'> Verrouiller </span>
					<span id='deLocker'> D&eacute;verrouiller </span>
				</div>
				<div>
					<h3>Validation:</h3>
					<span id="validateAndDisplayError">Valider et afficher les erreurs</span>
				</div>
				<div id="btnCtrlJade"> 
					<h3>&Eacute;v&eacute;nement li&eacute;s aux boutons&nbsp;:&nbsp;</h3> 
					<span name='btnCan' id='btnCan'> Cancel </span>
					<span name='btnNew' id='btnNew'> Add </span>
					<span name='btnVal' id='btnVal'> Valider </span>
					<span name='btnUpd' id='btnUpd'> Modifier </span>
					<span name='btnDel' id='btnDel'> Supprimer </span>
				</div>
				<div id="btnCtrlJade">  
					<h3>Autre &eacute;v&eacute;nement:</h3> 
					<span id='ajaxLoadData'> ajaxLoadData </span>
					<span id='ajaxShowDetailRefresh'> ajaxShowDetailRefresh </span>
					<span id='ajaxShowDetail'> ajaxShowDetail </span>
					<span id='ajaxStopEdition'> ajaxStopEdition </span>
					<span id='ajaxValidateEditon'> ajaxValidateEditon </span>
					<span id='ajaxUpdateComplete'> ajaxUpdateComplete </span>
				</div>
			</div>
			<br />
			<div id="selectCloneDiv">
				<h1>Sélectionnez la notation à cloner</h1>
			</div>
			<div class="mainContainerAjax">
				<%@ include file="../fragementNotation.jspf" %>
			</div>
		</form>
	</body>
</html>