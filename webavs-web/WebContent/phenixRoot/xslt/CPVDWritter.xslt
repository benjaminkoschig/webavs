<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="CommunicationInfoPivot">
		<xsl:element name="CommunicationReception">
			<xsl:apply-templates select="enteteCommunication"/>
			<xsl:apply-templates select="individuOK"/>
			<xsl:apply-templates select="queueCommunication"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="queueCommunication">
		<xsl:element name="QueueCommunication">
			<xsl:element name="NbreCommOK">
				<xsl:value-of select="../queueCommunication/nbrIdvOKCom"/>
			</xsl:element>
			<xsl:element name="NbreCommKO">
				<xsl:value-of select="../queueCommunication/nbrIdvKOCom"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="enteteCommunication">
		<xsl:element name="EnteteCommunication">
			<xsl:element name="NbreReceptionCom">
				<!--<xsl:value-of select="../queueCommunication/nbrIdvOKCom"/>-->
				<xsl:value-of select="count(//individuOK)"></xsl:value-of> 
			</xsl:element>
			<xsl:element name="DateReceptionCom">
				<xsl:value-of select="IdentificationCommunication/datRps"/>
			</xsl:element>
			<xsl:element name="DateTransformationCom">
				<xsl:value-of select="IdentificationCommunication/datRpsFrt"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="individuOK">
		<xsl:element name="Communication">
			<xsl:element name="GenreAffilie">
			<!--	<xsl:value-of select="avs/typTaxAVS"/> -->
				<xsl:value-of select="identificationDemande/codTypCom"/>
			</xsl:element>
			<xsl:element name="Annee1">
				<xsl:value-of select="identificationDemande/prdFisCcn"/>
			</xsl:element>
			<xsl:element name="Revenu1">
				<xsl:value-of select="avsPci/revActIdpAvs"/>
			</xsl:element>	
			<xsl:element name="Revenu2">
				<xsl:value-of select="avsPSA/revActLucPSAAVS"/>
			</xsl:element>		
			<xsl:element name="Cotisation1">
				<!-- <xsl:value-of select="identificationDemande/prdFisCcn"/> -->
			</xsl:element>
			<xsl:element name="Cotisation2">
				<!-- <xsl:value-of select="identificationDemande/prdFisCcn"/> -->
			</xsl:element>
			<xsl:element name="Fortune">
				<xsl:value-of select="avsPSA/frtPSAAVS"/>
			</xsl:element>
			<xsl:element name="DateFortune">
				<xsl:value-of select="avsPSA/datDetForPSAAVS"/>
			</xsl:element>
			<xsl:element name="Capital">
				<xsl:value-of select="avsPci/totCapInvAVS"/>
			</xsl:element>
			<xsl:element name="DebutExercice1">
				<xsl:value-of select="identificationDemande/debPrdCcn"/>
			</xsl:element>
			<xsl:element name="FinExercice1">
				<xsl:value-of select="identificationDemande/finPrdCcn"/>
			</xsl:element>
			<xsl:element name="DebutExercice2">
				<!-- <xsl:value-of select="identificationDemande/prdFisCcn"/> -->
			</xsl:element>
			<xsl:element name="FinExercice2">
				<!-- <xsl:value-of select="identificationDemande/prdFisCcn"/> -->
			</xsl:element>
			<xsl:element name="GenreTaxation">
				<xsl:value-of select="avs/typTaxAVS"/>
			</xsl:element>
			<xsl:element name="PeriodeIFD">
				<xsl:value-of select="assujettissementCtb/perFis"/>
			</xsl:element>
			<xsl:element name="NumCaisse">
				<xsl:value-of select="avsPSA/numCaiAvsPSA"/>
			</xsl:element>
			<xsl:element name="NumDemande">
				<xsl:value-of select="identificationDemande/numDem"/>
			</xsl:element>	
			<xsl:element name="NumAffilie">
				<xsl:value-of select="identificationDemande/numAffAvs"/>
			</xsl:element>
			<xsl:element name="NumAvs">
				<xsl:value-of select="identificationDemande/numAvsIdv"/>
			</xsl:element>
			<xsl:element name="NomPrenom">
				<xsl:value-of select="adresseCourrier/adrCtbNom1"/>
			</xsl:element>
			<xsl:element name="NumContribuable">
				<xsl:value-of select="identificationCtb/numCtb"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
