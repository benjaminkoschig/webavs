<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2004/07/xpath-functions" xmlns:xdt="http://www.w3.org/2004/07/xpath-datatypes">
	<xsl:output method="html" encoding="ISO-8859-1"/>
<!-- <xsl:variable name="maxLineEntete">8</xsl:variable> -->
  	<xsl:variable name="maxLine">9</xsl:variable>
	<xsl:template match="/">
		<HTML>
			<BODY>
				<xsl:apply-templates select="journal" />
			</BODY>
		</HTML>
	</xsl:template>

	<xsl:template match="journal">
	<style>
		table{
			width: 100%;
			font-family : Verdana,Courrier;
			font-size : 8pt;
			border: 1px solid black;
		
		}
		table.entete{
			width: 100%;
			font-family : Verdana,Courrier;
			font-size : 10pt;
			border: 1px solid black;
		
		}
		th {
			background-color: #226194;
			color: white;
		}
		th.background {
			font-weight: bold;
		}
		td{
			background-color: #B3C4DB;
		}
		td.background{
		
		}
		td.entete{
			font-weight: bold;
			background-color: white;
			color: black;
			height: 50px;
		}
		
		td.background-bold{
			font-weight: bold;
			background-color: #226194;
			color: white;
		}
		td.number{
			text-align: right;
		}
		@page {
			  size: landscape;
			  margin-top: 10cm
		}
		#copyright {
			display: none;
		}
<!--  balise exclusivement dédiée à Excel pour un entête de page :-(-->
		@page	{
			mso-header-data:"&amp;C&amp;EA L F A&amp;E&#160;PAR MOIS / PAR AGENCE :&#160;<xsl:value-of select="@annee"/> &#160;Mois :&#160;<xsl:value-of select="@moisNume"/>";
		}
		
	</style>
		<div id="journal">
<!-- 	<table class="entete" cellpadding="2" cellspacing="8" border="0"> 
				<tbody>
 				<tr>
					<td class="entete" align="center" height="20px">ALFA - &#160;<xsl:value-of select="@dateCreation"/></td>
				</tr>
				<tr>
					<td class="entete" align="center" height="20px">PAR MOIS / PAR TRIMESTRE / PAR SEMESTRE :&#160;<xsl:value-of select="@annee"/></td>
				</tr>
 -->				
<!--  				<tr>					
						<td class="entete" align="center" height="20px">PAR MOIS/AGENCE :&#160;<xsl:value-of select="@annee"/></td>
						<xsl:element name="td">
							<xsl:attribute name="colspan"><xsl:value-of select="count(colonne)+1"/></xsl:attribute>
							<xsl:attribute name="class">entete</xsl:attribute>
							<xsl:attribute name="align">center</xsl:attribute>
							&#160;
						</xsl:element>
					</tr>
					<tr>
						<td class="entete" align="center" height="20px">ALFA - &#160;<xsl:value-of select="@dateCreation"/></td>
						<xsl:element name="td">
							<xsl:attribute name="colspan"><xsl:value-of select="count(colonne)+1"/></xsl:attribute>
							<xsl:attribute name="class">entete</xsl:attribute>
							<xsl:attribute name="align">right</xsl:attribute> 
						</xsl:element>
					</tr>
						
				</tbody>
			</table>
-->			
			<table cellpadding="2" cellspacing="0" border="1">
				<tbody>
					<tr>
						<!--
						<th>&#160;</th>
						<th>&#160;</th>
						
						<th colspan="2">&#160;</th>
					-->
						<th>&#160;</th>
						<xsl:apply-templates select="colonne" mode="entete"/>
						<th class="background">Totaux</th>
					</tr>
					<xsl:apply-templates select="groupeCategories" />
				</tbody>
			</table>
<!-- 		<div id="copyright">Cette page est à usage interne, toute personne qui transmettrait cette page à quiconque en externe serait passible de poursuite pénale</div> -->
		</div>
	</xsl:template>

	<xsl:template match="colonne" mode="entete">

		<th class="background">
			<xsl:value-of select="@libelle"/>
		</th>
	</xsl:template>

	<xsl:template match="colonne" mode="enteteSuite">
		<th style="border-top:1px solid black;" class="background">
			<xsl:value-of select="@libelle"/>
		</th>
	</xsl:template>
	<xsl:template match="groupeCategories">
		<xsl:variable name="nbColspan">
			<xsl:value-of select="count(../colonne)+2"/>
		</xsl:variable>
		<!-- Appel de l'entête suite -->
<!-- 		
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

				<xsl:if test="position() != 45 and position() != 2 and position() != 11 and (position() mod $maxLine) = 0">
					<tr>
						<th style="page-break-before:always; border-top:1px solid black;" colspan="2">&#160;	</th>
						<xsl:apply-templates select="/journal/colonne" mode="enteteSuite"/>
						<th style="border-top:1px solid black;" class="background">Totaux</th>	
					</tr>
				</xsl:if>	  
			</xsl:otherwise>
		</xsl:choose>
 -->	
	
		<tr>
			<xsl:element name="td">
				<xsl:attribute name="colspan"><xsl:value-of select="$nbColspan"/></xsl:attribute>
				<xsl:attribute name="class">background-bold</xsl:attribute>
				<xsl:value-of select="@abreviation"/> 
			</xsl:element>
		</tr>
		<xsl:apply-templates select="categorie" mode="std">
			<!-- <xsl:with-param name="myPosition" select="position()" />   -->
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="categorie" mode="std">
		<!-- <xsl:param name="myPosition" />  -->
		<tr>
			<td style="padding-right 20xp;">&#160;-&#160;
<!-- 		<xsl:value-of select="@libelle"/>  -->
			<xsl:value-of select="@abreviation"/>
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

	<xsl:template name="nbOfColonne">
		<xsl:value-of select="count(/journal/colonne)"/>
	</xsl:template>

	<xsl:template name="posColonne">
		<xsl:param name="libelleCol"/>
		<xsl:for-each select="/journal/colonne">
			<xsl:if test="@libelle=$libelleCol">
				<xsl:value-of select="position()"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="categorie" mode="recursif">
		<xsl:param name="currentPos"/>
		<xsl:param name="entryPos"/>
		<xsl:param name="entryLength"/>
		<xsl:variable name="colonnePos">
			<xsl:apply-templates select="entry[position() = $entryPos]" mode="colonnePos"/>
			<!--			<xsl:call-template name="posColonne">
				<xsl:with-param name="abr" select="/colonne/@abreviation"/>
			</xsl:call-template>-->
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

	<xsl:template match="entry" mode="total">
		<xsl:value-of select="@totalFmt"/>
<!--		<xsl:choose>
  			<xsl:when test="../../@identifiant != 940119 and ../../@identifiant != 940120
			 and ../../@identifiant != 940118 and ../../@identifiant != 940121 and ../../@identifiant != 940122">
				<xsl:value-of select="@totalFmt"/>
			</xsl:when>
			<xsl:otherwise>&#160;</xsl:otherwise>
		</xsl:choose>
--> 
	</xsl:template>

	<xsl:template match="entry" mode="colonnePos">
		<xsl:call-template name="posColonne">
			<xsl:with-param name="libelleCol" select="colonne/@libelle"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>