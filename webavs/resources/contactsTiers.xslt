<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
 <html>
 <head>
 	<style>
 		div { padding-left:0.5cm; padding-right:0.5cm; margin-bottom:0.1cm }
 		.contact {border-top : solid 1px gray; padding-top:.1cm;padding-bottom:.1cm;}
 		.tiers {border : solid 1px black;}
 		.containerTiers {
			  	  	padding : 2 2 2 2;
			  	  	background-color : #B3C4DB;
	 	 }
	  	 .type {
	  	  	color : black;
	  	  	font-family : verdana;
	  	  	font-size : 10pt;
	  	  	font-weight : bold;
	  	  	padding : 2 2 2 2;
	  	  	border-bottom : solid 1px gray;
	  	}
	  	.idexterne {
	  	  	color : black;
	  	  	font-family : verdana;
	  	  	font-size : 10pt;
	  	  	font-weight : bold;
	  	  	padding : 1 1 1 1;
	  	}
 	</style>
 </head>
 <body>
  	<xsl:apply-templates select="/contacts/tiers"/>
 </body>
 </html>
</xsl:template>

<!-- ######################################################################### -->

<xsl:template match="tiers">
  <td  class="containerTiers">
	<table  cellspacing="0" width="100%" >
		<col width="20"/>
		<col width="20"/>
		<col width=""/>
		<col width="*"/>
		<!--
		<tr class="containerTiers">
			<td class="type" colspan="5"><xsl:value-of select="name"/><br/></td>
		</tr>
		-->
		<tr><td colspan="5"><hr/></td></tr>
		<xsl:apply-templates select="/contacts/idexterne"/>
	</table>
  </td>
</xsl:template>

<!--
*************************************************************
idexterne
*************************************************************
-->
<xsl:template match="idexterne">
	<tr>
		<td colspan="5" class="idexterne" width="100%" align="left"><xsl:value-of select="name"/></td>
	</tr>
	<xsl:for-each select="contact"><xsl:call-template name="contact"/></xsl:for-each>
</xsl:template>

<!-- ######################################################################### -->

<xsl:template name="contact">
  	<tr >
  		<td></td>
  		<td colspan="4">
			  	<b>
		  		<xsl:element name="a">
		  				<xsl:attribute name="accesskey">s</xsl:attribute>
						<xsl:attribute name="style">color:blue</xsl:attribute>
						<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
						<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
						<xsl:attribute name="href">javascript:parent.location.href='pyxis?userAction=pyxis.tiers.avoirContact.afficher&amp;_method=&amp;idContact=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/contacts/tiers/id"/>'</xsl:attribute>
						<xsl:value-of select="name"/>
				</xsl:element>
			  	</b>
				&#160;
				<xsl:if test="/contacts/showMajLink">
					<xsl:element name="a">
						<xsl:attribute name="style">color:blue</xsl:attribute>
						<xsl:attribute name="accesskey">m</xsl:attribute>
						<xsl:attribute name="onfocus">this.style.border='solid 1  blue'</xsl:attribute>
						<xsl:attribute name="onblur">this.style.border=''</xsl:attribute>
						<xsl:attribute name="href">javascript:parent.location.href='pyxis?userAction=pyxis.tiers.moyenCommunication.chercher&amp;idContact=<xsl:value-of select="id"/>&amp;idTiers=<xsl:value-of select="/contacts/tiers/id"/>'</xsl:attribute>
						MAJ
					</xsl:element>
				</xsl:if>
  		</td>
  	</tr>
  	<xsl:apply-templates select="moyen"/>
  	<tr>
  	<td colspan="5">
  		<hr/>
  	</td>
  	</tr>
</xsl:template>

<!-- ######################################################################### -->

<xsl:template match="moyen">
 	<xsl:element name="tr">
 		<xsl:if test="not (position()mod 2)">
		<xsl:attribute name="style">background :#E8EEF4;</xsl:attribute>
 		</xsl:if>

 		<td></td>
 		<td></td>
 		<td>
  			<xsl:value-of select="type"/>
  		</td>
  		<td>
  			<xsl:value-of select="numero"/>
  		</td>
  		<td>
  			<xsl:value-of select="application"/>
  		</td>

  	</xsl:element>
</xsl:template>

</xsl:stylesheet>
