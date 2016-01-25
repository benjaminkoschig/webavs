<HTML>
	<HEAD>
		<%@ taglib uri="/WEB-INF/TUTaglib.tld" prefix="tu" %>
		<%
			String servletContext = request.getContextPath();
			String mainServletPath = (String)request.getAttribute("mainServletPath");
			String cssScreenPath = servletContext+mainServletPath+"Root/css/journal.css";
			String cssPrintPath = servletContext+mainServletPath+"Root/css/journalPrint.css";
		%>
		<link rel="stylesheet" type="text/css" href="<%=cssScreenPath%>" media="screen"/>
		<link rel="stylesheet" type="text/css" href="<%=cssPrintPath%>" media="print"/>
		<script>
			function ouverture(){
				if(window.name!='imprime'){
					var url = window.location.href + "?annee=<%=request.getParameter("annee")%>&mois=<%=request.getParameter("mois")%>&csAgence=<%=request.getParameter("csAgence")%>&userAction=<%=request.getParameter("userAction")%>"
					window.open(url,'imprime','width='+(parseInt(screen.availWidth)-10)+',height='+(parseInt(screen.availHeight)-90)+',top=0,left=0,toolbar=no, menubar=yes, scrollbars=yes') ;
				}
			}
		</script>
	</HEAD>
	<body>
		<div>
			<tu:xslTransform beanName="journal" beanProperty="xmlDocument" xslFile="tucanaRoot/xsl/transformxslF1V1.xsl"/>
		</div>
		<div id="divButton">
			<BUTTON id="tagButton" onclick="ouverture()" tabindex="1"><IMG src="<%=servletContext%>/images/icon_print.png"></IMG>
			</BUTTON>
		</div>
		
		<script>
			if (window.name != "imprime"){
				document.getElementById("divButton").style.display="block";
				document.getElementById("tagButton").focus();
			} else {
				document.getElementById("divButton").style.display="none";
			}
		</script>	
	</body>
</HTML>