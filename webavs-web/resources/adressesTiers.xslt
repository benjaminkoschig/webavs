<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:variable name="lc" select="'abcdefghijklmnopqrstuvwxyz'"/>
  <xsl:variable name="uc" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
  <xsl:param name="hidefrom" select="0"/>
  
  
  <xsl:template match="/">
    <html>
      <head>
      <title><xsl:value-of select="adresses/tiers/name"/></title>
      <script>
      
        
      	function showHide(name) {
      		var elem = document.getElementById(name);
      		if (elem.style.display!="none") {
      		elem.style.display="none";
      		} else{
      		elem.style.display="block";
      		}
      	}
      	
      	function showHideIdAdresse () {
		      		var elems = document.getElementsByTagName("span");
		      		for(i=0;i&lt;elems.length;i++) {
							      			var elem = elems[i];

							      			if (elem.name =="idAdresse") {
												if (elem.style.display!="none") {
														elem.style.display="none";
												} else {
														elem.style.display="block";
												}
											}							      			
		      		}
		      		
      	}
      	
      	
      	
      	
      	function showHideNonActuel () {
		      		
		      		var elems = document.getElementsByTagName("tr");
		      		for(i=0;i&lt;elems.length;i++) {
							      			var elem = elems[i];

							      			if (elem.name =="pas_actuel") {
												if (elem.style.display!="none") {
														elem.style.display="none";
												} else {
														elem.style.display="block";
												}
											}							      			
		      		}
		      		
		      		var elems = document.getElementsByTagName("span");
		      		for(i=0;i&lt;elems.length;i++) {
							      			var elem = elems[i];

							      			if (elem.name =="pas_actuel") {
												if (elem.style.display!="none") {
														elem.style.display="none";
												} else {
														elem.style.display="block";
												}
											}							      			
		      		}
		      		
		      		
		      		
      	}
      	var idAdrSel =""
      	function showSame(obj) {
				idAdrSel = "";
				
				if (event.ctrlKey) {
					var els = new Array();
					els2 = parent.innerFrame.document.getElementsByTagName("div");
					try {
					els1 = parent.innerFrame2.document.getElementsByTagName("div");
					} catch( v) {els1= new Array()}
					var els = new Array();
					var pos = 0;
					for (i=0;i&lt;els1.length;i++) {
						els[pos++] = els1[i];
					}
					for (i=0;i&lt;els2.length;i++) {
						els[pos++] = els2[i];
					}
					
					cpt = 0;
					for (i=0;i&lt;els.length;i++) {
						
						if (els[i].name == obj.name) {
						cpt++;
						}
					}
					
					resetSame(els);
					
					if (cpt &gt; 1) {
						for (i=0;i&lt;els.length;i++) {
							if (els[i].name == obj.name) {
									idAdrSel = els[i].name;
									
									elLinks = els[i].getElementsByTagName("a");
									
									for (j=0;j&lt;elLinks.length;j++) {
										if (elLinks[j].name=="all") {
											elLinks[j].style.display = "inline";
										}
										if (elLinks[j].name=="single") {
											elLinks[j].style.display = "none";
										}
										
									}
									
									
									els[i].style.borderColor = "red";
									els[i].style.margin = "0";
									els[i].style.borderWidth = "2";
							} 
						}
					}
					
					
				}
				if (event.shiftKey) {
					var els = new Array();
					els2 = parent.innerFrame.document.getElementsByTagName("div");
					try {
					els1 = parent.innerFrame2.document.getElementsByTagName("div");
					} catch( v) {els1= new Array()}
					var els = new Array();
					var pos = 0;
					for (i=0;i&lt;els1.length;i++) {
						els[pos++] = els1[i];
					}
					for (i=0;i&lt;els2.length;i++) {
						els[pos++] = els2[i];
					}
					resetSame(els);
				}
	
				
				
			}

			function resetSame(els) {
				for (i=0;i&lt;els.length;i++) {
				
						elLinks = els[i].getElementsByTagName("a");
									
						for (j=0;j&lt;elLinks.length;j++) {
							if (elLinks[j].name=="all") {
								elLinks[j].style.display = "none";
							}
							if (elLinks[j].name=="single") {
								elLinks[j].style.display = "inline";
							}
							
						}
				
				
						els[i].style.borderColor = "gray";
						els[i].style.borderWidth = "1";
						els[i].style.margin = "1";
				}
			}
      	
      </script>
      </head>
      <body>
      <style>
	  
	  
	  .mdiv {
	  	border:solid 1px gray;
	  	margin: 1 1 1 1;
	  	background-color:white;
	  	color : black;
	  	font-family: verdana;
	  	font-size : 6 pt;
	  	
	  }
	  
	  .application {
	  	background-color : #226194;
	  	color : white;
	  	font-family : verdana;
	  	font-size : 10pt;
	  	font-weight : bold;
	  	border-top : solid 1 black;
	  	border-left : solid 1 black;
	  	border-bottom : solid 1 black;
	  	padding : 1 1 1 1;
	  	
	  }
	  
	   
	  
	  .type {
	  
	  	
	  	color : black;
	  	font-family : verdana;
	  	font-size : 10pt;
	  	font-weight : bold;
	  	padding : 1 1 1 1;
	  	
	  }
	  
	  
	  .adresse {
	  	background-color : #F0F0F0;
	  	color : black;
	  	font-family : verdana;
	  	font-size : 10pt;
	  	font-weight : bold;
	  	padding : 1 1 1 1;
	  
	  }
	  
	  .container {
	  	border : solid 1 black;
	  	padding : 1 1 1 1;
	  	background-color : #E8EEF4;
	  	
	  
	  }
	  	  .containerTiers {
	  	  	border : solid 1 black;
	  	  	padding : 1 1 1 1;
	  	  	background-color : #B3C4DB;
	  	  	
	  	  
	  }
	  
	  </style>
	  	  
	  <table cellspacing="0" width="100%" >
			  	<tr >
			  		<xsl:element name="td">
			  			<xsl:attribute name="class">application</xsl:attribute>
			  			<xsl:attribute name="align">center</xsl:attribute>
			  			<!--
			  			<xsl:attribute name="onClick">parent.location.href='pyxis?userAction=pyxis.tiers.tiers.afficher&amp;_method=&amp;selectedId=<xsl:value-of select="/adresses/tiers/id"/>'</xsl:attribute>
			  			-->
			  		
						<span style="font-family :wingdings;font-size:20pt;">+</span><span style="font-family :webdings;font-size:20pt;">€</span>
						<br/><input name="toggleHist" accesskey="X" type ="checkbox" checked="" onClick="showHideNonActuel()"/>[Alt+X]
						<input name="toggleMaxMin" accesskey="W" type ="checkbox"  onClick="parent.toggleMinMax();"/>[Alt+W]
					
			  		</xsl:element>
			  		<td  class="containerTiers">
			  			<table><tr>
			  			<td class="type"><xsl:value-of select="/adresses/tiers/name"/><br/>
			  		
			  			</td>
			  			</tr>
			  			<tr><td>
			  				<xsl:for-each select="/adresses/application">
			  				<xsl:if test="id=519004">
							  <xsl:element name="input">
							   <xsl:attribute name="onClick">javascript:showHide('<xsl:value-of select="id"/>')</xsl:attribute>
							   <xsl:attribute name="type">checkbox</xsl:attribute>
							   <xsl:attribute name="CHECKED"></xsl:attribute>
							  <xsl:value-of select="name"/>
							  </xsl:element><span style="width:0.5 cm"></span>
							  </xsl:if>
	  					</xsl:for-each>
	  					<xsl:for-each select="/adresses/application">
			  				<xsl:if test="id!=519004">
							  <xsl:element name="input">
							   <xsl:attribute name="onClick">javascript:showHide('<xsl:value-of select="id"/>')</xsl:attribute>
							   <xsl:attribute name="type">checkbox</xsl:attribute>
							   <xsl:attribute name="CHECKED"></xsl:attribute>
							  <xsl:value-of select="name"/>
							  </xsl:element><span style="width:0.5 cm"></span>
							  </xsl:if>
	  					</xsl:for-each>
	  					
			  			</td></tr>
			  			</table>
			  		</td>
	  	</tr>
	  	<xsl:for-each select="/adresses/application">
	  		<xsl:if test="id=519004">
	  			<xsl:call-template name="application"/>
	  		</xsl:if>
		</xsl:for-each>
	  	<xsl:for-each select="/adresses/application">
	  		<xsl:if test="id!=519004">
	  			<xsl:call-template name="application"/>
	  		</xsl:if>
		</xsl:for-each>
		</table>
		
		<script>
		if (parent.historyMaximized == false) {
			document.getElementsByName("toggleHist")[0].click();
		}
		</script>
		
      </body>
    </html>
  </xsl:template>
<!-- 
*************************************************************
Application	
*************************************************************
-->
<xsl:template name="application">
<tr style="background-color : transparent">
	<td height="4" colspan="2" ><p></p></td>
</tr>
   <xsl:element name="tr">
        <xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>

	
	
	<td id="t1" align="center" class="application"><span style="font-family :wingdings;font-size:20pt;">1</span><br/><xsl:value-of select="name"/></td>
	<td id="t2" width="100%"  class="container">
		
		<table cellspacing="0" width="100%">
			<xsl:for-each select="type"><xsl:if test="id=508008"><xsl:call-template name="type"/></xsl:if></xsl:for-each>
			<xsl:for-each select="type"><xsl:if test="id=508001"><xsl:call-template name="type"/></xsl:if></xsl:for-each>
			<xsl:for-each select="type"><xsl:if test="id!=508008 and id!=508001"><xsl:call-template name="type"/></xsl:if></xsl:for-each>
		</table>

	</td>
	
</xsl:element>
</xsl:template>
<!-- 
*************************************************************
type	
*************************************************************
-->

<xsl:template name="type">


<tr>
<td nowrap=""   style="border-top : solid 1px white;border-bottom : solid 1px silver" class="type"><xsl:value-of select="name"/></td>
<td width="100%" style="border-right : solid 1px white;border-top : solid 1px white;border-bottom : solid 1px silver" align="left"><table>
<xsl:for-each select="idexterne"><xsl:call-template name="idexterne"/></xsl:for-each>
</table></td>
</tr>

</xsl:template>
<!-- 
*************************************************************
idexterne	
*************************************************************
-->
<xsl:template name="idexterne">

<tr>
<td nowrap="" class="type"><xsl:value-of select="name"/></td>
<td width="100%" align="left"><table><tr>
<xsl:for-each select="adresse"><xsl:call-template name="adresse"/></xsl:for-each>
</tr></table></td>
</tr>

</xsl:template>



<!-- 
*************************************************************
adresse	
*************************************************************
-->
<xsl:template name="adresse">
<xsl:element name="td">


<xsl:element name="div">
	<xsl:attribute name="class">mdiv</xsl:attribute>
	<xsl:attribute name="name"><xsl:value-of select="idadresse"/></xsl:attribute>
	<xsl:attribute name="onmouseover">showSame(this)</xsl:attribute>
	<xsl:attribute name="onmouseout">resetSame(this)</xsl:attribute>
	<xsl:if test="not(actuel) and ((position() &lt; (last()-$hidefrom+1)))">
		<span name="pas_actuel"  style="display:none;font-size:8pt;background:#e0e0e0">X</span>
	</xsl:if>
	<table  cellpadding="2" cellspacing="0">
	<xsl:element name="tr">
	<xsl:choose>
	<xsl:when test="actuel">
			<xsl:attribute name="style">background-color:white ; color:black</xsl:attribute>	
	</xsl:when>
	<xsl:otherwise>
			<xsl:attribute name="style">background-color:#e0e0e0 ; color:black</xsl:attribute>	
	</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
	<xsl:when test="actuel or (position() &gt;= (last()-$hidefrom+1))">
			<xsl:attribute name="name">actuel</xsl:attribute>	
	</xsl:when>
	<xsl:otherwise>
			<xsl:attribute name="name">pas_actuel</xsl:attribute>
	</xsl:otherwise>
	</xsl:choose>
	
	
	<td style="font-size:8 pt;" nowrap="">
	
	<xsl:element name="a">
			<xsl:attribute name="style">color:blue</xsl:attribute>
			<xsl:attribute name="accesskey">s</xsl:attribute>
			<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
			<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
			
			<xsl:attribute name="href">javascript:parent.location.href='pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&amp;selectedId=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/adresses/tiers/id"/>'</xsl:attribute>
			<span style="font-family:wingdings">+</span><span style="font-family:webdings">€</span>
	</xsl:element>&#160;
	
	
	
	
	<span>		
	<b><xsl:value-of select="dateDebut"/></b>&#160;<span  style="font-family=wingdings">è</span><xsl:value-of select="dateFin"/>
	</span>
	<!-- pour copie d'une adresse -->

	<xsl:if test="showUpdateButton">
		&#160;
		<xsl:element name="a">
				<xsl:attribute name="name">single</xsl:attribute>
				<xsl:attribute name="style">color:blue</xsl:attribute>
				<xsl:attribute name="accesskey">m</xsl:attribute>
				<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
				<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
				
				<xsl:attribute name="href">javascript:parent.location.href='pyxis?synch=false&amp;userAction=pyxis.adressecourrier.avoirAdresse.copier&amp;selectedId=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/adresses/tiers/id"/>&amp;idAdresse=<xsl:value-of select="idadresse"/>'</xsl:attribute>MAJ</xsl:element>		
		
		<xsl:element name="a">
				<xsl:attribute name="name">all</xsl:attribute>
				<xsl:attribute name="style">color:red;display:none</xsl:attribute>
				<xsl:attribute name="accesskey">m</xsl:attribute>
				<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
				<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
				<xsl:attribute name="href">javascript:parent.location.href='pyxis?synch=true&amp;userAction=pyxis.adressecourrier.avoirAdresse.copier&amp;selectedId=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/adresses/tiers/id"/>&amp;idAdresse=<xsl:value-of select="idadresse"/>'</xsl:attribute>MAJ</xsl:element>		

	</xsl:if>
		
	<xsl:if test="showCorrectionButton">
		&#160;
		<xsl:element name="a">
				<xsl:attribute name="name">single</xsl:attribute>
				<xsl:attribute name="style">color:blue</xsl:attribute>
				<xsl:attribute name="accesskey">c</xsl:attribute>
				<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
				<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
				
				<xsl:attribute name="href">javascript:parent.location.href='pyxis?synch=false&amp;userAction=pyxis.adressecourrier.avoirAdresse.correction&amp;back=_sl&amp;selectedId=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/adresses/tiers/id"/>&amp;idAdresse=<xsl:value-of select="idadresse"/>'</xsl:attribute>COR</xsl:element>		
		
		<xsl:element name="a">
				<xsl:attribute name="name">all</xsl:attribute>
				<xsl:attribute name="style">color:red;display:none</xsl:attribute>
				<xsl:attribute name="accesskey">c</xsl:attribute>
				<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
				<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
				<xsl:attribute name="href">javascript:parent.location.href='pyxis?synch=true&amp;userAction=pyxis.adressecourrier.avoirAdresse.correction&amp;back=_sl&amp;selectedId=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/adresses/tiers/id"/>&amp;idAdresse=<xsl:value-of select="idadresse"/>'</xsl:attribute>COR</xsl:element>		


	</xsl:if>

	<!-- fin copy -->
	
	<span name="idAdresse" style="display:block" >
	<pre style="margin-top:.1 cm;margin-bottom:.1 cm;"><xsl:value-of select="value"/></pre>
	</span>
	</td>
	</xsl:element>
	</table>
</xsl:element>	<!-- div -->

</xsl:element>
</xsl:template>





</xsl:stylesheet>
