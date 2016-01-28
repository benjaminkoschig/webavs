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
			<xsl:element name="depPSAAVS">
				<xsl:value-of select="avsPSA/depPSAAVS"/>
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
				<xsl:value-of select="avsPci/datDetCapInvAVS"/>
			</xsl:element>
			<xsl:element name="Capital">
				<xsl:value-of select="avsPci/totCapInvAVS"/>
			</xsl:element>
			<xsl:element name="DebutExercice1">
				<xsl:value-of select="identificationDemande/debPrdCcn"/>
			</xsl:element>
			<xsl:element name="DateDemande">
				<xsl:value-of select="identificationDemande/datDem"/>
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
			<xsl:element name="debPrdCcn">
				<xsl:value-of select="identificationDemande/debPrdCcn"/>
			</xsl:element>
			<xsl:element name="debPrdCcn">
				<xsl:value-of select="identificationDemande/debPrdCcn"/>
			</xsl:element>
			<xsl:element name="finPrdCcn">
				<xsl:value-of select="identificationDemande/finPrdCcn"/>
			</xsl:element>
			<xsl:element name="nomCtb">
				<xsl:value-of select="identificationCtb/nomCtb"/>
			</xsl:element>
			<xsl:element name="adrCtbChez">
				<xsl:value-of select="adresseCourrier/adrCtbChez"/>
			</xsl:element>
			<xsl:element name="adrCtbRue">
				<xsl:value-of select="adresseCourrier/adrCtbRue"/>
			</xsl:element>
			<xsl:element name="adrCtbLieu">
				<xsl:value-of select="adresseCourrier/adrCtbLieu"/>
			</xsl:element>
			<xsl:element name="datNai">
				<xsl:value-of select="situation/datNai"/>
			</xsl:element>
			<xsl:element name="perFis">
				<xsl:value-of select="assujettissementCtb/perFis"/>
			</xsl:element>
			<xsl:element name="penAliObtAVS">
				<xsl:value-of select="avsPSA/penAliObtAVS"/>
			</xsl:element>
			<xsl:element name="drtHabGraRoyAVS">
				<xsl:value-of select="avsPSA/drtHabGraRoyAVS"/>
			</xsl:element>
			<xsl:element name="typTaxAVS">
				<xsl:value-of select="avs/typTaxAVS"/>
			</xsl:element>
			<xsl:element name="numCaiAvsPci">
				<xsl:value-of select="avsPci/numCaiAvsPci"/>
			</xsl:element>
			<xsl:element name="numCaiAvsPSA">
				<xsl:value-of select="avsPSA/numCaiAvsPSA"/>
			</xsl:element>
			<xsl:element name="numCaiAvsSal">
				<xsl:value-of select="avsSal/numCaiAvsSal"/>
			</xsl:element>
			<xsl:element name="numCaiAvsAgr">
				<xsl:value-of select="avsAgr/numCaiAvsAgr"/>
			</xsl:element>
			<xsl:element name="datDebAsjAVS">
				<xsl:value-of select="avsPci/datDebAsjAVS"/>
			</xsl:element>
			<xsl:element name="datFinAsjAVS">
				<xsl:value-of select="avsPci/datFinAsjAVS"/>
			</xsl:element>
			<xsl:element name="totCapInvAVS">
				<xsl:value-of select="avsPci/totCapInvAVS"/>
			</xsl:element>
			<xsl:element name="datDetCapInvAVS">
				<xsl:value-of select="avsPci/datDetCapInvAVS"/>
			</xsl:element>
			<xsl:element name="revActIdpAvs">
				<xsl:value-of select="avsPci/revActIdpAvs"/>
			</xsl:element>
			<xsl:element name="giPrfOcc">
				<xsl:value-of select="avsPci/giPrfOcc"/>
			</xsl:element>
			<xsl:element name="excLiqPrfOcc">
				<xsl:value-of select="avsPci/excLiqPrfOcc"/>
			</xsl:element>
			<xsl:element name="salVerConAVS">
				<xsl:value-of select="avsPci/salVerConAVS"/>
			</xsl:element>
			<xsl:element name="natComAvsPci">
				<xsl:value-of select="avsPci/natComAvsPci"/>
			</xsl:element>
			<xsl:element name="natComAvsPSA">
				<xsl:value-of select="avsPSA/natComAvsPSA"/>
			</xsl:element>
			<xsl:element name="natComAvsSal">
				<xsl:value-of select="avsSal/natComAvsSal"/>
			</xsl:element>
			<xsl:element name="natComAvsAgr">
				<xsl:value-of select="avsAgr/natComAvsAgr"/>
			</xsl:element>
			<xsl:element name="salCotAvs">
				<xsl:value-of select="avsSal/salCotAvs"/>
			</xsl:element>
			<xsl:element name="frtPSAAVS">
				<xsl:value-of select="avsPSA/frtPSAAVS"/>
			</xsl:element>
			<xsl:element name="datDetForPSAAVS">
				<xsl:value-of select="avsPSA/datDetForPSAAVS"/>
			</xsl:element>
			<xsl:element name="mtPSAAVS">
				<xsl:value-of select="avsPSA/rntPSAAVS"/>
			</xsl:element>
			<xsl:element name="revActLucPSAAVS">
				<xsl:value-of select="avsPSA/revActLucPSAAVS"/>
			</xsl:element>
			<xsl:element name="natRntPSAAVS">
				<xsl:value-of select="avsPSA/natRntPSAAVS"/>
			</xsl:element>
			<xsl:element name="revNet">
				<xsl:value-of select="avsPci/revActIdpAvs"/>
			</xsl:element>
			<xsl:element name="rmqAvsPci">
				<xsl:value-of select="avsPci/rmqAvsPci"/>
			</xsl:element>
			<xsl:element name="rmqAvsPSA">
				<xsl:value-of select="avsPSA/rmqAvsPSA"/>
			</xsl:element>
			<xsl:element name="rmqAvsSal">
				<xsl:value-of select="avsSal/rmqAvsSal"/>
			</xsl:element>
			<xsl:element name="rmqAvsAgr">
				<xsl:value-of select="avsAgr/rmqAvsAgr"/>
			</xsl:element>
			<xsl:element name="revNetAgr">
				<xsl:value-of select="synDecisionTaxation/revNet"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
