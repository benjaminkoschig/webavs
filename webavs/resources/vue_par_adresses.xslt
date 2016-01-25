<?xml version="1.0"?> 
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"> 

<xsl:template name="idAdresse">
  <xsl:variable name="idAdr" select="."/>
  
  
  <tr>
  <td valign="top" style="padding-right:0.4cm;">
  <pre>     
  <xsl:value-of select="../value"/> 
	<!-- idadresse -->
	<!--<xsl:value-of select="."/>  <br/>-->
  </pre>
  </td>
  
  <td style="padding-left:0.4cm;border-left:dotted 1px silver" valign="top">
    <table>
    <xsl:for-each select="//idexterne/adresse[idadresse=$idAdr]">
       <tr>
      <xsl:choose>
	      <xsl:when test="not (actuel)">
	       
	          <!-- domaine-->
	          <td style="color:gray"><xsl:value-of select="../../../name"/></td> 
	          <!-- type-->
	          <td style="color:gray"><xsl:value-of select="../../name"/> </td>
	          <!-- idExterne-->
	          <td style="color:gray"><xsl:value-of select="../name"/> </td>
	          <!-- dates -->
	          <td style="color:gray"><xsl:value-of select="dateDebut"/> - <xsl:value-of select="dateFin"/></td>
	       
	      </xsl:when>
	      <xsl:otherwise>
	          <!-- domaine-->
	          <td><b><xsl:value-of select="../../../name"/></b> </td> 
	          <!-- type-->
	          <td><xsl:value-of select="../../name"/> </td>
	          <!-- idExterne-->
	          <td><xsl:value-of select="../name"/> </td>
	          <!-- dates -->
	          <td><xsl:value-of select="dateDebut"/> - <xsl:value-of select="dateFin"/></td>
	      </xsl:otherwise>
      </xsl:choose>
     
      </tr>
    </xsl:for-each>
    </table>
  </td>
  </tr>
  <xsl:if test="position() != last()">
  <tr><td colspan="2"><hr/></td></tr>
  </xsl:if>
</xsl:template>

<xsl:template match="/">
<!-- selection distinct des idadresses-->
<table >
<xsl:for-each select="//idadresse[not( preceding::idadresse/text() = text() )]">
  <xsl:call-template name="idAdresse"/>
</xsl:for-each>
<tr><td></td></tr>
</table>
</xsl:template>


</xsl:stylesheet>
