<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2004/07/xpath-functions" xmlns:xdt="http://www.w3.org/2004/07/xpath-datatypes">
	<xsl:output method="html" encoding="ISO-8859-1"/>
	<xsl:template match="/">
		<xsl:apply-templates select="journal"/>
	</xsl:template>

	<xsl:template match="journal">	
		<div id="journal">
			<table class="entete" cellpadding="2" cellspacing="8" border="0">
				<tbody>
					<tr>
						<td class="entete">Signature</td>
						<td class="entete" align="center" height="20px"><xsl:value-of select="@libelleAgence"/></td>
						
						<td class="entete" align="right"><xsl:value-of select="@dateCreation"/></td>
					</tr>
					<tr>
						
						<td class="entete" colspan="3" align="center">Journal récapitulatif pour le mois :&#160;
							<xsl:value-of select="@moisAlpha"/>&#160;<xsl:value-of select="@annee"/>
						</td>
					</tr>
				</tbody>
			</table>			
			<table cellpadding="2" cellspacing="0" border="1">
				<tbody>
					<tr>
						<!--
						<th>&#160;</th>
						<th>&#160;</th>
					-->
						<th>&#160;</th>
						<!--<xsl:apply-templates select="colonne" mode="entete"/>-->
						<th class="background">En faveur du siège</th>
						<th class="background">En faveur de l'agence</th>
					</tr>
					<xsl:apply-templates select="groupeCategories"/>
				</tbody>
			</table>
			<div id="copyright">Cette page est à usage interne, toute personne qui transmettrait cette page à quiconque en externe serait passible de poursuite pénale</div>
		</div>
	</xsl:template>

	<xsl:template match="colonne" mode="entete">
		<th class="background">
			<xsl:value-of select="@abreviation"/>
		</th>
	</xsl:template>

	<xsl:template match="groupeCategories">
		<tr>
			<xsl:element name="td">
				<xsl:attribute name="class">background-bold</xsl:attribute>				
					<xsl:choose>
						 <xsl:when test="@affiche = 'true'">
							<xsl:value-of select="@libelle"/>
						</xsl:when>
						<xsl:otherwise>
						   &#160;
						</xsl:otherwise>
					</xsl:choose>				
			</xsl:element>
			<xsl:apply-templates select="categorie"/>
		</tr>
	</xsl:template>

	<xsl:template match="categorie">
<!-- 		<xsl:variable name="nbrFormat">###,###.00</xsl:variable> -->
		<xsl:choose>
			<xsl:when test="(../@signe = 'positif') and (@total &gt;0)">
				<td class="number">
					<!-- <xsl:value-of select="format-number(@total, $nbrFormat)"/> -->
					<xsl:value-of select="@totalFmtPositif"/>
				</td>
				<td>
					&#160;
				</td>
			</xsl:when>
			<xsl:when test="(../@signe = 'negatif') and (@total &lt; 0)">
				<td class="number">
					<!-- <xsl:value-of select="format-number(@total, $nbrFormat)"/> -->
						<xsl:value-of select="@totalFmtPositif"/>
					</td>
					<td>
						&#160;
					</td>
				</xsl:when>
				<xsl:otherwise>
					<td>&#160;
<!--  											
						<xsl:value-of select="../@signe"/>
						<xsl:value-of select="@total"/>
						<xsl:value-of select="@totalFmt"/>-->
					</td>
					<td class="number">
<!-- 						<xsl:value-of select="format-number(@total, $nbrFormat)"/> -->
					<xsl:value-of select="@totalFmtPositif"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
