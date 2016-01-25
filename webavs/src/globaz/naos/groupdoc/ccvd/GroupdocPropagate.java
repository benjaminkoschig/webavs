package globaz.naos.groupdoc.ccvd;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Properties;

/**
 * <code>GroupdocPropagate</code> est un service externe qui cr�e/met � jour les dossiers des tiers dans Groupdoc pour
 * la CCVD.
 * 
 * @author Emmanuel Fleury
 */
public class GroupdocPropagate extends BAbstractEntityExternalService {
    // commonProperties
    public final static String PROPERTY_ADAPTER_NAME = "adapterName";

    private String adapterName = null;

    /**
     * Retourne le nom de l'adaptateur � utiliser.
     * 
     * @return le nom de l'adaptateur � utiliser, <code>null</code> pour utiliser l'adaptateur GED par d�faut
     */
    private final String _getAdapterName() {
        return adapterName;
    }

    /**
     * Ex�cute un service externe apr�s ajout d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    protected void _propagateData(BEntity entity) throws Throwable {
        try {
            if (TIAvoirAdresse.class.isAssignableFrom(entity.getClass())) {
                TIAvoirAdresse adresse = (TIAvoirAdresse) entity;
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(adresse.getSession());
                tiers.setIdTiers(adresse.getIdTiers());
                tiers.retrieve();
                entity = tiers;
            }
            if (TITiersViewBean.class.isAssignableFrom(entity.getClass())) {
                TITiersViewBean tiers = (TITiersViewBean) entity;
                Properties properties = null;
                properties = new Properties();
                properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "tiers");
                properties.setProperty("pyxis.tiers.id", tiers.getIdTiers());
                String key = tiers.getIdTiers();
                if (key == null) {
                    key = "";
                }
                if (key.length() > 14) {
                    key = JadeStringUtil.substring(key, 0, 14);
                }
                key = JadeStringUtil.rightJustifyInteger(key, 14);
                properties.setProperty(JadeGedTargetProperties.INDEX_KEY, key);
                properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, tiers.getIdTiers());
                properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1,
                        JANumberFormatter.fmt(tiers.getIdTiers(), true, false, false, 0));
                properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, tiers.getNomPrenom());
                properties.setProperty(JadeGedTargetProperties.DESCRIPTION_3, tiers.getLocaliteLong());
                properties.setProperty(JadeGedTargetProperties.DESCRIPTION_4, tiers.getNumAvsActuel());
                if (isVerbose()) {
                    JadeLogger.info(this, "GroupDoc propagate (" + key + ")...");
                }
                JadeGedFacade.propagate(_getAdapterName(), properties);
                if (isVerbose()) {
                    JadeLogger.info(this, "GroupDoc propagation (" + key + ") OK");
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, "Could not propagate data for " + entity + ": " + e.toString());
        }
    }

    /**
     * D�finit le nom de l'adaptateur � utiliser.
     * 
     * @param adapterName
     *            l'adaptateur � utiliser, <code>null</code> pour utiliser l'adaptateur GED par d�faut
     */
    private final void _setAdapterName(String adapterName) {
        if (JadeStringUtil.isBlank(adapterName)) {
            this.adapterName = null;
        } else {
            this.adapterName = adapterName;
        }
    }

    /**
     * Ex�cute un service externe apr�s ajout d'une entit�.
     * 
     * @param entity
     *            l'entit�
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
     * Ex�cute un service externe apr�s suppression d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe apr�s chargement d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe apr�s mise � jour d'une entit�.
     * 
     * @param entity
     *            l'entit�
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
     * Ex�cute un service externe avant ajout d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe avant suppression d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe avant chargement d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe avant mise � jour d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    /**
     * Ex�cute un service externe pour initialiser une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity entity) throws Throwable {
    }

    /**
     * D�finit les propri�t�s g�n�rales du service.
     * 
     * @param properties
     *            les propri�t�s g�n�rales du service
     */
    public final void setCommonProperties(Properties properties) {
        adapterName = null;
        if (properties == null) {
            return;
        }
        _setAdapterName(properties.getProperty(GroupdocPropagate.PROPERTY_ADAPTER_NAME));
    }

    /**
     * Ex�cute un service externe pour valider le contenu d'une entit�.
     * 
     * @param entity
     *            l'entit�
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
