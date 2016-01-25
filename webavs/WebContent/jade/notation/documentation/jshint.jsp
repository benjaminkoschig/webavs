<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% String servletContext = "../../../";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
	<meta name="User-Lang" content="FR"/> 
	<meta name="Context_URL" content="<%=request.getContextPath()%>"/> 
	<meta name="formAction" content="/webavs/pegasus"/>   
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Test Notation for js</title>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery.js"> </script>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery-ui.js"> </script>
	<script type="text/javascript" src="<%=servletContext%>scripts/ValidationGroups.js"> </script>
	<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"> </script>
	<script type="text/javascript" src="../javascriptLibs/Highcharts/js/highcharts.js"> </script>
  	<%@ include file="../notationLibJs.jspf" %> 
  	
  	<script type="text/javascript" src="<%=servletContext%>scripts/ajax/ajaxUtils.js"/></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>
  	
  	<script type="text/javascript" src="../javascriptLibs/jsHint/jshint.js""></script>
    <script type="text/javascript" src="testJshint.js"></script>
    <link type="text/css" href="../javascriptLibs/jsHint/jshint.css" rel="stylesheet" />
    
</head>
	<script language="JavaScript">
	$(function () {
		testJsHint.init();
	});
	</script>
<body>
	<div id="containerStatJsAnalayse"  style="width: 100%; height: 990px"></div>
	<form action="">
		<div class="report">
 
 		</div> 
	</form>
</body>

</html>