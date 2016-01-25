<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <% String servletContext ="../../../";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
	<meta name="User-Lang" content="FR"/> 
	<meta name="Context_URL" content="<%=request.getContextPath()%>"/> 
	<meta name="formAction" content="/webavs/pegasus"/>   
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Test Notation for js</title>
	<script type="text/javascript" src="../../../scripts/jquery.js"> </script>
	<script type="text/javascript" src="../../../scripts/jquery-ui.js"> </script>
	<script type="text/javascript" src="../../../scripts/ValidationGroups.js"> </script>
	<script type="text/javascript" src="../../../scripts/widget/globazwidget.js"> </script>
    <script type="text/javascript" src="quinitNotation.js"> </script>
    
  	<%@ include file="../notationLibJs.jspf" %> 
  	
  	<script type="text/javascript" src="../../../scripts/ajax/ajaxUtils.js"/></script>
	<script type="text/javascript" src="../../../scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
	<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>
  	
	<script type="text/javascript" src="../javascriptLibs/qunit/qunit.js"></script>  
    <link type="text/css" href="testNotation.css" rel="stylesheet" />
    <link type="text/css" href="../../../theme/jquery/jquery-ui.css" rel="stylesheet" />
    <link type="text/css" href="../../../theme/master.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="../javascriptLibs/qunit/qunit.css" media="screen" />
	
</head>
<body>
<form action="">
<div>
 <input type="button" id='affiche' value="affiche element tested" />
 </div>
 <h1 id="qunit-header">QUnit for notation</h1>
 <h2 id="qunit-banner"></h2>
 <h2 id="qunit-userAgent"></h2> 
 <ol id="qunit-tests"> </ol>
 <div id="qunit-fixture">test markup, will be hidden</div>
 <div id="testInput" style="display:none"> 
 	 <%@ include file="../fragementNotation.jspf" %> 
 </div> 
 </form>
</body>

</html>