package globaz.naos.groupdoc.ccju;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.adapter.groupdoc.JadeGedAdapterAPIParameters;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFNaosAffiliationMapper;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Date;
import java.util.Properties;

/**
 * <code>GroupdocPropagate</code> est un service externe qui crée/met à jour les dossiers des tiers dans Groupdoc pour
 * la CCJU.
 * 
 * @author Emmanuel Fleury
 */
public class GroupdocPropagate extends BAbstractEntityExternalService {

    private final static String[] COTIS_TYPES = new String[] { "CTX", "DEC", "DSA", "FAC", "LAA" };
    // commonProperties
    public final static String PROPERTY_ADAPTER_NAME = "adapterName";

    private String adapterName = null;

    /**
     * Retourne le nom de l'adaptateur à utiliser.
     * 
     * @return le nom de l'adaptateur à utiliser, <code>null</code> pour utiliser l'adaptateur GED par défaut
     */
    private final String _getAdapterName() {
        return adapterName;
    }

    /**
     * Exécute un service externe après ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    protected void _propagateData(BEntity entity) throws Throwable {
        try {
            if (AFAffiliation.class.isAssignableFrom(entity.getClass())) {
                AFAffiliation affiliation = (AFAffiliation) entity;
                if (!JadeStringUtil.isBlank(affiliation.getAffilieNumero())) {
                    Properties properties = null;
                    // AFFIL
                    properties = new Properties();
                    properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "affilie");
                    properties.setProperty("numero.role.formatte", affiliation.getAffilieNumero());
                    properties.setProperty(JadeGedTargetProperties.INDEX_KEY, affiliation.getAffilieNumero());
                    properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, affiliation.getAffilieNumero());
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, affiliation.getAffilieNumero());
                    properties
                            .setProperty(JadeGedTargetProperties.DESCRIPTION_2, affiliation.getTiers().getNomPrenom());
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagate (" + affiliation.getAffilieNumero() + ")...");
                    }
                    JadeGedFacade.propagate(_getAdapterName(), properties);
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagation (" + affiliation.getAffilieNumero() + ") OK");
                    }
                    // COTIS
                    for (int i = 0; i < GroupdocPropagate.COTIS_TYPES.length; i++) {
                        properties = new Properties();
                        properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "cotisations");
                        properties.setProperty("numero.role.formatte", affiliation.getAffilieNumero());
                        properties.setProperty("babel.type.id", GroupdocPropagate.COTIS_TYPES[i]);
                        String indexKey = affiliation.getAffilieNumero() + "-" + GroupdocPropagate.COTIS_TYPES[i];
                        properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
                        String indexText = GroupdocPropagate.COTIS_TYPES[i] + "-" + affiliation.getAffilieNumero();
                        properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
                        properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, indexText);
                        properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, affiliation.getTiers()
                                .getNomPrenom());
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_BASE_DOCUMENTS_PARENTE, "AFFIL");
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_DOSSIER_CLASSEMENT_PARENT,
                                affiliation.getAffilieNumero());
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_LIEN_DOSSIER_PARENT, ""); // TODO:
                        // GroupDoc
                        // -
                        // obtenir
                        // le
                        // bon
                        // code_lien_dossier_parent
                        if (isVerbose()) {
                            JadeLogger.info(this, "GroupDoc propagate (" + indexKey + ")...");
                        }
                        JadeGedFacade.propagate(_getAdapterName(), properties);
                        if (isVerbose()) {
                            JadeLogger.info(this, "GroupDoc propagation (" + indexKey + ") OK");
                        }
                    }
                    // COMPT
                    String annee4 = JadeStringUtil.substring(JadeDateUtil.getYMDDate(new Date()), 0, 4);
                    String typeDossier = "CCA";
                    properties = new Properties();
                    properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "comptabilite");
                    properties.setProperty("numero.role.formatte", affiliation.getAffilieNumero());
                    properties.setProperty("annee", annee4);
                    properties.setProperty("babel.type.id", typeDossier);
                    String indexKey = affiliation.getAffilieNumero() + "-" + annee4 + "-" + typeDossier;
                    properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
                    String indexText = typeDossier + "-" + annee4 + "-" + affiliation.getAffilieNumero();
                    properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, indexText);
                    properties
                            .setProperty(JadeGedTargetProperties.DESCRIPTION_2, affiliation.getTiers().getNomPrenom());
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_BASE_DOCUMENTS_PARENTE, "AFFIL");
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_DOSSIER_CLASSEMENT_PARENT,
                            affiliation.getAffilieNumero());
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_LIEN_DOSSIER_PARENT, ""); // TODO:
                    // GroupDoc
                    // -
                    // obtenir
                    // le
                    // bon
                    // code_lien_dossier_parent
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagate (" + indexKey + ")...");
                    }
                    JadeGedFacade.propagate(_getAdapterName(), properties);
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagation (" + indexKey + ") OK");
                    }
                }
            } else if (TITiersViewBean.class.isAssignableFrom(entity.getClass())) {
                TITiersViewBean tiers = (TITiersViewBean) entity;
                AFNaosAffiliationMapper affMap = new AFNaosAffiliationMapper();
                String numAff = affMap.findNumAffTaxation(tiers.getIdTiers(), tiers.getSession());
                Properties properties = null;
                if (!JadeStringUtil.isBlank(numAff)) {
                    // AFFIL
                    properties = new Properties();
                    properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "affilie");
                    properties.setProperty("numero.role.formatte", numAff);
                    properties.setProperty(JadeGedTargetProperties.INDEX_KEY, numAff);
                    properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, numAff);
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, numAff);
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, tiers.getNomPrenom());
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagate (" + numAff + ")...");
                    }
                    JadeGedFacade.propagate(_getAdapterName(), properties);
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagation (" + numAff + ") OK");
                    }
                    // COTIS
                    for (int i = 0; i < GroupdocPropagate.COTIS_TYPES.length; i++) {
                        properties = new Properties();
                        properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "cotisations");
                        properties.setProperty("numero.role.formatte", numAff);
                        properties.setProperty("babel.type.id", GroupdocPropagate.COTIS_TYPES[i]);
                        String indexKey = numAff + "-" + GroupdocPropagate.COTIS_TYPES[i];
                        properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
                        String indexText = GroupdocPropagate.COTIS_TYPES[i] + "-" + numAff;
                        properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
                        properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, indexText);
                        properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, tiers.getNomPrenom());
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_BASE_DOCUMENTS_PARENTE, "AFFIL");
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_DOSSIER_CLASSEMENT_PARENT, numAff);
                        properties.setProperty(JadeGedAdapterAPIParameters.CODE_LIEN_DOSSIER_PARENT, ""); // TODO:
                        // GroupDoc
                        // -
                        // obtenir
                        // le
                        // bon
                        // code_lien_dossier_parent
                        if (isVerbose()) {
                            JadeLogger.info(this, "GroupDoc propagate (" + indexKey + ")...");
                        }
                        JadeGedFacade.propagate(_getAdapterName(), properties);
                        if (isVerbose()) {
                            JadeLogger.info(this, "GroupDoc propagation (" + indexKey + ") OK");
                        }
                    }
                    // COMPT
                    String annee4 = JadeStringUtil.substring(JadeDateUtil.getYMDDate(new Date()), 0, 4);
                    String typeDossier = "CCA";
                    properties = new Properties();
                    properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "comptabilite");
                    properties.setProperty("numero.role.formatte", numAff);
                    properties.setProperty("annee", annee4);
                    properties.setProperty("babel.type.id", typeDossier);
                    String indexKey = numAff + "-" + annee4 + "-" + typeDossier;
                    properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
                    String indexText = typeDossier + "-" + annee4 + "-" + numAff;
                    properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, indexText);
                    properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, tiers.getNomPrenom());
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_BASE_DOCUMENTS_PARENTE, "AFFIL");
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_DOSSIER_CLASSEMENT_PARENT, numAff);
                    properties.setProperty(JadeGedAdapterAPIParameters.CODE_LIEN_DOSSIER_PARENT, ""); // TODO:
                    // GroupDoc
                    // -
                    // obtenir
                    // le
                    // bon
                    // code_lien_dossier_parent
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagate (" + indexKey + ")...");
                    }
                    JadeGedFacade.propagate(_getAdapterName(), properties);
                    if (isVerbose()) {
                        JadeLogger.info(this, "GroupDoc propagation (" + indexKey + ") OK");
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, "Could not propagate data for " + entity + ": " + e.toString());
        }
    }

    /**
     * Définit le nom de l'adaptateur à utiliser.
     * 
     * @param adapterName
     *            l'adaptateur à utiliser, <code>null</code> pour utiliser l'adaptateur GED par défaut
     */
    private final void _setAdapterName(String adapterName) {
        if (JadeStringUtil.isBlank(adapterName)) {
            this.adapterName = null;
        } else {
            this.adapterName = adapterName;
        }
    }

    /**
     * Exécute un service externe après ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if (_isAfterAdd(entity)) {
            if (isVerbose()) {
                JadeLogger.info(this, "afterAdd(" + entity.getClass().getName() + ")");
            }
            _propagateData(entity);
        }
    }

    /**
     * Exécute un service externe après suppression d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après chargement d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après mise à jour d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        if (_isAfterUpdate(entity)) {
            if (isVerbose()) {
                JadeLogger.info(this, "afterUpdate(" + entity.getClass().getName() + ")");
            }
            _propagateData(entity);
        }
    }

    /**
     * Exécute un service externe avant ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant suppression d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant chargement d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant mise à jour d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe pour initialiser une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity entity) throws Throwable {
    }

    /**
     * Définit les propriétés générales du service.
     * 
     * @param properties
     *            les propriétés générales du service
     */
    public final void setCommonProperties(Properties properties) {
        adapterName = null;
        if (properties == null) {
            return;
        }
        _setAdapterName(properties.getProperty(GroupdocPropagate.PROPERTY_ADAPTER_NAME));
    }

    /**
     * Exécute un service externe pour valider le contenu d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
