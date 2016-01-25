<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>about.jsp</TITLE>
</HEAD>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.api.BISession currBISession = controller.getSession();
globaz.globall.db.BSession currSession = (globaz.globall.db.BSession)currBISession;
globaz.jade.admin.user.bean.JadeUser user = currSession.getUserInfo();
globaz.globall.db.BApplication currApplication = currSession.getApplication();
//String jdbcDriverName = (currApplication != null ? currApplication.getProperty(currApplication.PROPERTY_JDBCDRIVERNAME) : null);
//String jdbcUrl = (currApplication != null ? currApplication.getProperty(currApplication.PROPERTY_JDBCURL) : null);
//globaz.jade.jdbc.JadeJdbcDatasource datasource = (jdbcUrl != null ? globaz.jade.jdbc.JadeJdbcDriver.getInstance().getDatasource(jdbcUrl) : null);
String jdbcUrl = globaz.jade.common.Jade.getInstance().getDefaultJdbcUrl();
globaz.jade.jdbc.JadeJdbcDatasource datasource = (jdbcUrl != null ? globaz.jade.jdbc.JadeJdbcDriver.getInstance().getDatasource(jdbcUrl) : null);
String jdbcDriverName = "";
String jdbcDriverUrl = "";
if (datasource != null) {
	try {
		globaz.jade.jdbc.JadeJdbcDatasourceDirect dsDirect = (globaz.jade.jdbc.JadeJdbcDatasourceDirect)datasource;
		jdbcDriverName = dsDirect.getDriverClassName();
		jdbcDriverUrl = dsDirect.getUrl();
	} catch (Exception e) {
	}
}

%>
<BODY>
<TABLE class="find" cellspacing="0" height="600">
        <TR>
            <TH class="title" height="19">Application user guides</TH>
        </TR>
        <TR valign="top">
            <TD>
            	<br/>
            	<TABLE cellpadding="0" cellspacing="0" border="0" style="border-left:solid 1px silver;border-top:solid 1px silver;border-bottom:solid 1px silver;">
            	<TR>
                <TD>&nbsp;</TD>
                <TD class="mtd">COMMON</TD>
                <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_common_user_guide_FR.pdf" target="_blank">Généralités</A></TD>
                <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_common_user_guide_FR.pdf" target="_blank">Allgemeines</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">NAOS</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_naos_user_guide_FR.pdf" target="_blank">Affiliation</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_naos_user_guide_FR.pdf" target="_blank">Kassenerfassung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">APG</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_apg_user_guide_FR.pdf" target="_blank">APG</A></TD>
					<TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_apg_user_guide_FR.pdf" target="_blank">EO</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">HERMES</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_hermes_user_guide_FR.pdf" target="_blank">ARC/ZAS</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_hermes_user_guide_FR.pdf" target="_blank">MZR-ZAS</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">BABEL</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_babel_user_guide_FR.pdf" target="_blank">Catalogue de texte</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_babel_user_guide_FR.pdf" target="_blank">Textkatalog</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">OSIRIS</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_osiris_user_guide_FR.pdf" target="_blank">Comptabilité auxiliaire</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_osiris_user_guide_FR.pdf" target="_blank">Hilfsbuchhaltung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">HELIOS</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_helios_user_guide_FR.pdf" target="_blank">Comptabilité générale</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_helios_user_guide_FR.pdf" target="_blank">Finanzbuchhaltung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">LYNX</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_lynx_user_guide_FR.pdf" target="_blank">Comptabilité fournisseur</A></TD>
                    <TD class="mtd">&nbsp;</TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">PAVO</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_pavo_user_guide_FR.pdf" target="_blank">Comptes individuels</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_pavo_user_guide_FR.pdf" target="_blank">IK-Wesen</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">AQUILA</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_aquila_user_guide_FR.pdf" target="_blank">Contentieux</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_aquila_user_guide_FR.pdf" target="_blank">Rechtspflege</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">PHENIX</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_phenix_user_guide_FR.pdf" target="_blank">Cotisations personnelles</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_phenix_user_guide_FR.pdf" target="_blank">Persönliche Beiträge</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">DRACO</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_draco_user_guide_FR.pdf" target="_blank">Déclaration de salaires</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_draco_user_guide_FR.pdf" target="_blank">Lohnbescheinigung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">LYRA</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_lyra_user_guide_FR.pdf" target="_blank">Echéancier</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_lyra_user_guide_FR.pdf" target="_blank">Terminkalender</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">MUSCA</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_musca_user_guide_FR.pdf" target="_blank">Facturation</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_musca_user_guide_FR.pdf" target="_blank">Fakturierung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">CAMPUS</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_campus_user_guide_FR.pdf" target="_blank">Gestion des étudiants</A></TD>
					<TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_campus_user_guide_FR.pdf" target="_blank">Verwaltung der Studenten</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">PYXIS</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_pyxis_user_guide_FR.pdf" target="_blank">Gestion des tiers</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webavs_pyxis_user_guide_FR.pdf" target="_blank">Partnerverwaltung</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">IJAI</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_ijai_user_guide_FR.pdf" target="_blank">Indemnités journalières</A></TD>
					<TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_ijai_user_guide_FR.pdf" target="_blank">IV-Taggeld</A></TD>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TD class="mtd">HERA</TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_hera_user_guide_FR.pdf" target="_blank">Situation familiale</A></TD>
                    <TD class="mtd"><A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>user_guides/globaz_webprestations_hera_user_guide_FR.pdf" target="_blank">Familiären Verhältnisse</A></TD>
                </TR>
            	</TABLE>
            </TD>
        </TR>
</TABLE>
</BODY>
</HTML>
