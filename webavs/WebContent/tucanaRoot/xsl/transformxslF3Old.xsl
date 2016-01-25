<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2004/07/xpath-functions" xmlns:xdt="http://www.w3.org/2004/07/xpath-datatypes">
	<xsl:output method="html" encoding="ISO-8859-1"/>
  	<xsl:variable name="maxLineEntete">8</xsl:variable>
  	<xsl:variable name="maxLine">9</xsl:variable>
  	<!-- ******************************************************************************************************
		Début
      ******************************************************************************************************--> 
	<xsl:template match="/">
		<xsl:apply-templates select="journal" />
		 
	</xsl:template>
<!-- ******************************************************************************************************
		Journal
      *****************************************************************************************************--> 
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
						
						<td class="entete" colspan="3" align="center">Journal récapitulatif pour l'année :&#160;
							<xsl:value-of select="@annee"/>
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
						<th colspan="2">&#160;</th>
						<xsl:apply-templates select="colonne" mode="entete"/>
						<th class="background">Totaux</th>
					</tr>
					<xsl:apply-templates select="groupeCategories" />
				</tbody>
			</table>
			<div id="copyright">Cette page est à usage interne, toute personne qui transmettrait cette page à quiconque en externe serait passible de poursuite pénale</div>
		</div>
	</xsl:template>
<!-- ******************************************************************************************************
		Entête de colonne
      *****************************************************************************************************--> 
	<xsl:template match="colonne" mode="entete">

		<th class="background">
			<xsl:value-of select="@libelle"/>
		</th>
	</xsl:template>
<!-- ******************************************************************************************************
		Entête suite
      *****************************************************************************************************--> 
	<xsl:template match="colonne" mode="enteteSuite">
		<th style="border-top:1px solid black;" class="background">
			<xsl:value-of select="@libelle"/>
		</th>
	</xsl:template>
	<!-- ******************************************************************************************************
		Groupe catégories
      *****************************************************************************************************--> 
	<xsl:template match="groupeCategories">
		<xsl:variable name="nbColspan">
			<xsl:value-of select="count(../colonne)+3"/>
		</xsl:variable>
		<!-- Appel de l'entête suite -->
		
		<xsl:choose>
			<xsl:when test="position() = $maxLineEntete + 1">
				<xsl:if test="position() != 1 and (position() mod $maxLineEntete) = 1">
					<tr>
						<th style="page-break-before:always; border-top:1px solid black;" colspan="2">&#160;</th>
						<xsl:apply-templates select="/journal/colonne" mode="enteteSuite"/>
						<th style="border-top:1px solid black;" class="background">Totaux</th>	
					</tr>
				</xsl:if>	  
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="position() != 1 and position() != 10 and (position() mod $maxLine) = 1">
					<tr>
						<th style="page-break-before:always; border-top:1px solid black;" colspan="2">&#160;</th>
						<xsl:apply-templates select="/journal/colonne" mode="enteteSuite"/>
						<th style="border-top:1px solid black;" class="background">Totaux</th>	
					</tr>
				</xsl:if>	  
			</xsl:otherwise>
		</xsl:choose>
		<tr>
			<xsl:element name="td">
				<xsl:attribute name="colspan"><xsl:value-of select="$nbColspan"/></xsl:attribute>
				<xsl:attribute name="class">background-bold</xsl:attribute>
				<xsl:value-of select="@libelle"/>
			</xsl:element>
		</tr>
		<xsl:apply-templates select="categorie" mode="std">
			<!-- <xsl:with-param name="myPosition" select="position()" />   -->
		</xsl:apply-templates>
	</xsl:template>
<!-- ******************************************************************************************************
		Catégorie
      *****************************************************************************************************--> 
	<xsl:template match="categorie" mode="std">
		<!-- <xsl:param name="myPosition" />  -->
		<tr>
			<td colspan="2" style="padding-right 20xp;">&#160;-&#160;
			<xsl:value-of select="@libelle"/>
			</td>
			<xsl:apply-templates select="." mode="recursif">
				<xsl:with-param name="currentPos">1</xsl:with-param>
				<xsl:with-param name="entryPos">1</xsl:with-param>
				<xsl:with-param name="entryLength">
					<xsl:call-template name="nbOfColonne"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<td class="number">
				<xsl:value-of select="@totalFmt"/>
			</td>
		</tr>
	</xsl:template>
<!-- ******************************************************************************************************
		Nombre de colonne
      *****************************************************************************************************--> 
	<xsl:template name="nbOfColonne">
		<xsl:value-of select="count(/journal/colonne)"/>
	</xsl:template>
<!-- ******************************************************************************************************
		Position colonne
      *****************************************************************************************************--> 

	<xsl:template name="posColonne">
		<xsl:param name="abr"/>
		<xsl:param name="rng"/>
		<xsl:for-each select="/journal/colonne">
			<xsl:if test="@abreviation=$abr and @rang=$rng">
				<xsl:value-of select="position()"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
<!-- ******************************************************************************************************
		Catégorie en boucle
      *****************************************************************************************************--> 
	<xsl:template match="categorie" mode="recursif">
		<xsl:param name="currentPos"/>
		<xsl:param name="entryPos"/>
		<xsl:param name="entryLength"/>
		<xsl:variable name="colonnePos">
			<xsl:apply-templates select="entry[position() = $entryPos]" mode="colonnePos"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$currentPos = $colonnePos">
				<xsl:element name="td">
					<xsl:attribute name="class">number</xsl:attribute>
					<xsl:attribute name="title"><xsl:value-of select="entry[position()=$entryPos]/colonne/@libelle"/></xsl:attribute>
					<xsl:apply-templates select="entry[position()=$entryPos]" mode="total"/>
				</xsl:element>	
			
				<xsl:if test="$currentPos&lt;$entryLength">
					<xsl:apply-templates select="." mode="recursif">
						<xsl:with-param name="currentPos">
							<xsl:value-of select="$currentPos + 1"/>
						</xsl:with-param>
						<xsl:with-param name="entryPos">
							<xsl:value-of select="$entryPos + 1"/>
						</xsl:with-param>
						<xsl:with-param name="entryLength">
							<xsl:value-of select="$entryLength"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<td>&#160;</td>
				<xsl:if test="$currentPos&lt;$entryLength">
					<xsl:apply-templates select="." mode="recursif">
						<xsl:with-param name="currentPos">
							<xsl:value-of select="$currentPos + 1"/>
						</xsl:with-param>
						<xsl:with-param name="entryPos">
							<xsl:value-of select="$entryPos"/>
						</xsl:with-param>
						<xsl:with-param name="entryLength">
							<xsl:value-of select="$entryLength"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
<!-- ******************************************************************************************************
		Entry total
      *****************************************************************************************************--> 
	<xsl:template match="entry" mode="total">
		<xsl:value-of select="@totalFmt"/>
	</xsl:template>
<!-- ******************************************************************************************************
		Position colonne dans entry
      ***************************************************************************************************** -->
	<xsl:template match="entry" mode="colonnePos">
		<xsl:call-template name="posColonne">
			<xsl:with-param name="abr" select="colonne/@abreviation"/>
			<xsl:with-param name="rng" select="colonne/@rang"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
