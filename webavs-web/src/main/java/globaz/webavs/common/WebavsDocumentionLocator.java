package globaz.webavs.common;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class WebavsDocumentionLocator {

    public static final VersionProperties VERSION_PROPERTIES = loadAnCreateObject();
    private static final String WEBCONTENTS_DOCUMENTS_PATH = "documents/";

    /**
     * Retourne la date de version actuelle.
     *
     * @return la date de version actuelle.
     */
    public static String getDate() {
        return VERSION_PROPERTIES.getDate();
    }

    /**
     * Retourne l'emplacement de la documentation. Si non spécifiée dans Jade.xml => documents/ sous répertoire de
     * webavs/Web Content.
     *
     * @return
     */
    public static String getDocumentationLocation() {
        String documentUrl = "";
        if (JadeStringUtil.isBlank(Jade.getInstance().getDocumentationUrl())) {
            documentUrl = WEBCONTENTS_DOCUMENTS_PATH;
        } else {
            if (!Jade.getInstance().getDocumentationUrl().endsWith("/")) {
                documentUrl = Jade.getInstance().getDocumentationUrl() + "/";
            } else {
                documentUrl = Jade.getInstance().getDocumentationUrl();
            }
        }

        return documentUrl + getName() + "/" + getVersion() + "/";
    }

    /**
     * Retourne le nom du projet (webavs).
     *
     * @return le nom du projet (webavs).
     */
    public static String getName() {
        return VERSION_PROPERTIES.getProjectName();
    }

    /**
     * Retourne le numéro de version du service pack actuelle.
     *
     * @return le numéro de version du service pack actuelle.
     */
    public static String getServicePackVersion() {
        return VERSION_PROPERTIES.getVersionServicePack();
    }

    /**
     * Retourne le numéro de version business actuelle.
     *
     * @return le numéro de version business actuelle.
     */
    public static String getVersion() {
        return VERSION_PROPERTIES.getProjectVersion();
    }

    /**
     * Retourne le numéro de version technique.
     *
     * @return le numéro de version technique.
     */
    public static String getVersionTechnique() {
        return VERSION_PROPERTIES.getProjectVersionTechnical();
    }

    /**
     * Charge les propriétés webavs.version.
     */
    private static Properties loadProperties() {
        Properties prop = new Properties();
        try {
            InputStream is = WebavsDocumentionLocator.class.getResourceAsStream("/webavs.version");
            prop.load(is);
        } catch (IOException e) {
            LOG.error("Unable to load the resource webavs.version", e);
            prop = null;
        }
        return prop;
    }

    private static VersionProperties loadAnCreateObject() {
        Properties versionProp = loadProperties();
        VersionProperties technicalProperties = new VersionProperties();

        if (versionProp != null) {
            technicalProperties.setProjectName(versionProp.getProperty("name"));
            technicalProperties.setProjectVersion(versionProp.getProperty("version"));
            technicalProperties.setProjectVersionTechnical(versionProp.getProperty("version.technical"));
            technicalProperties.setDate(versionProp.getProperty("date"));
            technicalProperties.setVersionServicePack(versionProp.getProperty("version.servicepack"));

            technicalProperties.setBuildNumber(versionProp.getProperty("git.commit.id.abbrev"));
            technicalProperties.setBuildNumberUnique(versionProp.getProperty("git.build.number.unique"));
            technicalProperties.setClosestTagName(versionProp.getProperty("git.closest.tag.name"));
            technicalProperties.setCommitId(versionProp.getProperty("git.commit.id.full"));
            technicalProperties.setCommitIdAbbrev(versionProp.getProperty("git.commit.id.abbrev"));
            technicalProperties.setCommitIdDescribe(versionProp.getProperty("git.commit.id.describe"));
            technicalProperties.setCommitIdDescribeShort(versionProp.getProperty("git.commit.id.describe-short"));

            technicalProperties.setScmBranch(versionProp.getProperty("git.branch"));
            technicalProperties.setTimestamp(versionProp.getProperty("git.build.time"));
        }
        return technicalProperties;
    }
}
