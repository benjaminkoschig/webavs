<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html class="area mainContainerAjax sansBordure">
	<head>
<% 
		String servletContext ="/webavs/";
%>		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

		<title>Framework de notation Javascript</title>

		<script type="text/javascript" src="<%=servletContext%>scripts/jquery.js"></script>
		<script type="text/javascript" src="<%=servletContext%>scripts/jquery-ui.js"></script>
		<script type="text/javascript" src="<%=servletContext%>scripts/ValidationGroups.js"></script>
		<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"></script>

		<link type="text/css" href="<%=servletContext%>jade/notation/test/testNotation.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>theme/jquery/jquery-ui.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>theme/master.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>theme/ajax/templateZoneAjax.css" rel="stylesheet" />
		<link type="text/css" href="<%=servletContext%>jade/notation/startPage.css" rel="stylesheet" />

		<script type="text/javascript" src="<%=servletContext%>jade/notation/startPage.js"></script>  
	</head>
	<body>
		<div id="navBar" class="content withBorder">
			<div id="innerNavBar">
				<span id="home">
				</span>
			</div>
		</div>
		<div id="intro" class="slidable">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix baniere avecOmbre">
				<h2>
					Bienvenue sur les notations
				</h2>
			</div>
			<div class="corps content avecOmbre">
				<div class="paragraphe1">
					Vous trouverez ici les informations nécessaire pour appréhender le framework de notation javascript de Globaz
				</div>
				<div class="paragraphe2">
					<ul>
						<li>
							<a href="http://sglobdoc1.ju.globaz.ch/wiki/index.php/JSNotation_-_G%C3%A9n%C3%A9ralit%C3%A9s">Document d'analyse des notations</a>
						</li>				
					</ul>
				</div>
			</div>
			<div class="divBoutons content withBorder">
				<div class="innerDivBoutons">
					<span id="demo" class="bouton" href="jade/notation/demo/demo.jsp">
						Demo des notations
					</span>
					<span id="doc" class="bouton" href="jade/notation/documentation/doc.jsp">
						Documentation global
					</span>
					<span id="docComposant" class="bouton" href="jade/notation/documentation/docComposant.jsp">
						Documentation des composants notations
					</span>
					<span id="docCreation" class="bouton" href="jade/notation/documentation/docCreation.jsp">
						Documentation pour créer un composant de la notation
					</span>
					<span id="demoQuirks" class="bouton" href="jade/notation/demo/demo_quirks.jsp">
						Demo avec mode Quirks (IE avec mauvais DOCTYPE)
					</span>
					<span id="testClone" class="bouton" href="jade/notation/test/testClone.jsp">
						Page de teste du clone
					</span>
					<span id="jsHint" class="bouton" href="jade/notation/documentation/jshint.jsp">
						JsHint
					</span>
					<span id="qunit" class="bouton" href="jade/notation/test/test.jsp">
						Testes des notations avec QUnit
					</span>
				</div>
			</div>
		</div>
	</body>
</html>