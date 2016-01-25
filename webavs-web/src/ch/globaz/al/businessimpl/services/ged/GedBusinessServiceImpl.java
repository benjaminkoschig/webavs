package ch.globaz.al.businessimpl.services.ged;

import globaz.ged.AirsConstants;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.util.CommonNSSFormater;
import java.util.Date;
import java.util.Properties;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstGed;
import ch.globaz.al.business.exceptions.ged.ALGedException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.ged.GedBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.utils.ALErrorsUtils;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.business.service.TiersService;
import ch.globaz.pyxis.businessimpl.service.TiersServiceImpl;

/**
 * Implémentation de services GED
 * 
 * @author pta
 * 
 */
public class GedBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements GedBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.ged.GedBusinessService#getTypeSousDossier
     * (ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public String getTypeSousDossier(DossierModel dossierModel) {

        if (JadeStringUtil.equals(dossierModel.getStatut(), ALCSDossier.STATUT_IS, false)) {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_NGB;
        }
        if (!JadeStringUtil.equals(dossierModel.getStatut(), ALCSDossier.STATUT_IS, false)
                && JadeStringUtil.equals(dossierModel.getActiviteAllocataire(), ALCSDossier.ACTIVITE_NONACTIF, false)) {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_NA;
        }

        if (!JadeStringUtil.equals(dossierModel.getStatut(), ALCSDossier.STATUT_IS, false)
                && (JadeStringUtil.equals(dossierModel.getActiviteAllocataire(), ALCSDossier.ACTIVITE_AGRICULTEUR,
                        false) || JadeStringUtil.equals(dossierModel.getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false))) {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_AGR;
        }
        if (!JadeStringUtil.equals(dossierModel.getStatut(), ALCSDossier.STATUT_IS, false)
                && JadeStringUtil.equals(dossierModel.getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false)) {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_TA;
        }

        if (dossierModel.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_INDEPENDANT)) {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_IND;
        } else {
            return ALConstGed.VAL_TYPE_SOUS_DOSSIER_NG;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.ged.GedBusinessService#propagateAllocataire
     * (ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public void propagateAllocataire(DossierModel dossier) throws JadeApplicationException, JadePersistenceException {

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {

            // sauvegarde des messages de la session. L'ancienne persistance n'exécute pas correctement les SELECT si un
            // avertissement est présent dans la session. Ils sont sauvegardés avant d'être réinjectés dans la session à
            // la fin de la méthode
            JadeBusinessMessage[] logMessages = ALErrorsUtils.getMessageFromJadeThreadLog();

            AllocataireComplexModel allocataireToPropagate = ALServiceLocator.getAllocataireComplexModelService().read(
                    dossier.getIdAllocataire());
            Properties properties = new Properties();
            String numeroAffilie = dossier.getNumeroAffilie();
            String nss = allocataireToPropagate.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
            properties.setProperty(JadeGedTargetProperties.DESCRIPTION_4, nss);
            String service = ALConstGed.SERVICE_NAME_ALLOC;

            String typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_NG;
            String categorieLongue = ALConstGed.VAL_CAT_NG;

            if (dossier.getStatut().equalsIgnoreCase(ALCSDossier.STATUT_IS)) {
                typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_NGB;
                categorieLongue = ALConstGed.VAL_CAT_NGB;
            } else if (dossier.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_NONACTIF)) {
                typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_NA;
                categorieLongue = ALConstGed.VAL_CAT_NA;
            } else if ((dossier.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE))
                    || (dossier.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_AGRICULTEUR))) {
                typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_AGR;
                categorieLongue = ALConstGed.VAL_CAT_AGR;
            } else if (dossier.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE)) {
                typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_TA;
                categorieLongue = ALConstGed.VAL_CAT_TA;
            } else if (dossier.getActiviteAllocataire().equalsIgnoreCase(ALCSDossier.ACTIVITE_INDEPENDANT)) {
                typeSousDossier = ALConstGed.VAL_TYPE_SOUS_DOSSIER_IND;
                categorieLongue = ALConstGed.VAL_CAT_IND;
            }

            properties.setProperty(ALConstGed.PROP_NAME_TYPE_DOSSIER, ALConstGed.PROP_VAL_TYPE_DOSSIER_ALLOC);
            properties.setProperty(ALConstGed.PROP_NAME_NUMERO_AFFILIE, numeroAffilie);
            properties.setProperty(ALConstGed.PROP_NAME_NUMERO_AVS, nss);
            properties.setProperty(ALConstGed.PROP_NAME_TYPE_SOUS_DOSSIER, typeSousDossier);
            properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, service);
            String indexKey = numeroAffilie + "-" + nss + "-" + typeSousDossier;
            properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
            String indexText = typeSousDossier + "-" + numeroAffilie + "-" + nss;
            properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
            String description1 = categorieLongue + " - " + numeroAffilie;
            properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, description1);
            String description2 = allocataireToPropagate.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                    + " " + allocataireToPropagate.getPersonneEtendueComplexModel().getTiers().getDesignation2();
            properties.setProperty(JadeGedTargetProperties.DESCRIPTION_2, description2);
            if (!JadeStringUtil.isBlank(allocataireToPropagate.getPersonneEtendueComplexModel().getPersonne()
                    .getDateNaissance())) {
                properties.setProperty(
                        JadeGedTargetProperties.DESCRIPTION_3,
                        JACalendar.format(allocataireToPropagate.getPersonneEtendueComplexModel().getPersonne()
                                .getDateNaissance(), JACalendar.FORMAT_DDsMMsYYYY));
            }
            if (!JadeStringUtil.isBlank(dossier.getDebutActivite())) {
                properties.setProperty(ALConstGed.PROP_NAME_DATE_OUVERTURE_DOSSIER,
                        JACalendar.format(dossier.getDebutActivite(), JACalendar.FORMAT_YYYYMMDD));
            }
            String statutDossier = ALConstGed.STATUT_DOSSIER_A;
            if ((dossier.getEtatDossier().equalsIgnoreCase(ALCSDossier.ETAT_RADIE))
                    || (dossier.getEtatDossier().equalsIgnoreCase(ALCSDossier.ETAT_REFUSE))) {
                statutDossier = ALConstGed.STATUT_DOSSIER_1;
            } else if ((dossier.getEtatDossier().equalsIgnoreCase(ALCSDossier.ETAT_SUSPENDU))) {
                statutDossier = ALConstGed.STATUT_DOSSIER_W;
            }
            properties.setProperty(ALConstGed.PROP_NAME_CODE_STATUT_DOSSIER, statutDossier);
            String parentDatabaseCode = ALConstGed.PARENT_DB_CODE_AFFIL;
            properties.setProperty(ALConstGed.PROP_NAME_CODE_BASE_DOCUMENTS_PARENTE, parentDatabaseCode);
            properties.setProperty(ALConstGed.PROP_NAME_CODE_DOSSIER_CLASSEMENT_PARENT, numeroAffilie);
            String lien = "";
            if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_NA)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "12500";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "42500";
                } else {
                    lien = "72500";
                }
            } else if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_NG)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "12800";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "42800";
                } else {
                    lien = "72800";
                }
            } else if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_NGB)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "12700";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "42700";
                } else {
                    lien = "72700";
                }
            } else if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_AGR)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "12600";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "42600";
                } else {
                    lien = "72600";
                }
            } else if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_TA)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "13000";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "43000";
                } else {
                    lien = "73000";
                }
            } else if (categorieLongue.equalsIgnoreCase(ALConstGed.VAL_CAT_IND)) {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "13100";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "43100";
                } else {
                    lien = "73100";
                }
            } else {
                if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_A)) {
                    lien = "14000";
                } else if (statutDossier.equalsIgnoreCase(ALConstGed.STATUT_DOSSIER_W)) {
                    lien = "44000";
                } else {
                    lien = "74000";
                }
            }
            properties.setProperty(ALConstGed.PROP_NAME_CODE_LIEN_DOSSIER_PARENT, lien);

            // Propriétés nécessaires à la GED Airs

            String classNameNumAffFormatter = JadePropertiesService.getInstance()
                    .getProperty("common.formatNumAffilie");
            IFormatData formatterNumAffilie;
            try {
                formatterNumAffilie = (IFormatData) Class.forName(classNameNumAffFormatter).newInstance();

                String classNameNSSFormatter = JadePropertiesService.getInstance().getProperty("pyxis.formatNumAvs");
                IFormatData formatterNSS = (IFormatData) Class.forName(classNameNSSFormatter).newInstance();

                properties.setProperty(AirsConstants.NAFF, formatterNumAffilie.unformat(numeroAffilie));
                properties.setProperty(AirsConstants.NNSS, formatterNSS.unformat(nss));

            } catch (Exception e) {
                throw new ALGedException(
                        "GeBusinessServiceImpl#propagateAllocataire: unable to prepare data for ged airs (unformat)");
            }
            properties.setProperty(AirsConstants.NOM, allocataireToPropagate.getPersonneEtendueComplexModel()
                    .getTiers().getDesignation1());
            properties.setProperty(AirsConstants.PRENOM, allocataireToPropagate.getPersonneEtendueComplexModel()
                    .getTiers().getDesignation2());

            AdresseTiersDetail address = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    allocataireToPropagate.getAllocataireModel().getIdTiersAllocataire(), new Boolean(true),
                    JadeDateUtil.getGlobazFormattedDate(new Date()), ALCSTiers.DOMAINE_AF,
                    AdresseService.CS_TYPE_COURRIER, "");

            if ((address != null) && (address.getFields() != null)) {
                String paysISO = address.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO);
                String npa = address.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA);
                String localite = address.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

                properties.setProperty(AirsConstants.NPA, TILocalite.formatLocalite(paysISO, npa, localite));
            } else {
                properties.setProperty(AirsConstants.NPA, "?");
            }

            try {
                // FIXME : pourquoi est-ce que cette vérification n'est pas faite au début de la méthode ? Actuellement
                // on charge la totalité des données avant de savoir si elle devront être propagées ou non
                if (JadeGedFacade.isInstalled()) {
                    JadeGedFacade.propagate(properties);
                }
            } catch (JadeServiceLocatorException e) {
                throw new ALGedException(
                        "GedBusinessServiceImpl#propagateAllocataire : unable to propagate allocataire", e);

            } catch (JadeServiceActivatorException e) {
                throw new ALGedException(
                        "GedBusinessServiceImpl#propagateAllocataire : unable to propagate allocataire", e);

            } finally {
                ALErrorsUtils.addMessages(logMessages);
            }
        }
    }

    @Override
    public void propagateDroitForGEDFPV(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {
        if (droit != null) {
            try {
                Properties properties = new Properties();
                // on ne propage le droit que si c'est un droit de type formation
                if (ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit())) {

                    CommonNSSFormater nssformat = new CommonNSSFormater();
                    String nssEnfant = nssformat.unformat(droit.getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());

                    properties.setProperty(AirsConstants.NSS, nssEnfant);
                    properties.setProperty(AirsConstants.PRENOM, droit.getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation2());
                    properties.setProperty(AirsConstants.NOM, droit.getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation1());
                    properties.setProperty(AirsConstants.DOMAINE, "BENEFICIAIRE_CAF");
                    properties.setProperty(AirsConstants.DATE_CREATION, JACalendar.todayJJsMMsAAAA());
                    properties.setProperty(AirsConstants.USER_ID, JadeThread.currentContext().getUserId());

                    // on récupère le dossier parent
                    DossierComplexModel alDossier = ALServiceLocator.getDossierComplexModelService().read(
                            droit.getDroitModel().getIdDossier());

                    if (alDossier != null) {

                        String nssParent = nssformat.unformat(alDossier.getAllocataireComplexModel()
                                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
                        properties.setProperty(AirsConstants.NSS_PARENT, nssParent);
                        properties.setProperty(AirsConstants.PRENOM_PARENT, alDossier.getAllocataireComplexModel()
                                .getPersonneEtendueComplexModel().getTiers().getDesignation2());
                        properties.setProperty(AirsConstants.NOM_PARENT, alDossier.getAllocataireComplexModel()
                                .getPersonneEtendueComplexModel().getTiers().getDesignation1());
                    }
                    JadeGedFacade.propagate(properties);

                }
            } catch (Exception e) {
                JadeLogger.error(this, "could not propagate data for the following reasons : " + e);
            }

        }

    }

    @Override
    public void propagateDossierForGEDFPV(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {
        if (dossier != null) {
            try {
                Properties properties = new Properties();
                CommonNSSFormater nssformat = new CommonNSSFormater();
                String nss = nssformat.unformat(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel());
                // Il faut chercher le tiers à l'ancienne car vu que le dossier n'est pas encore persisté on n'a pas
                // toutes les données
                TiersService ts = new TiersServiceImpl();
                TiersSimpleModel tiersAlloc = ts.read(dossier.getAllocataireComplexModel()
                        .getPersonneEtendueComplexModel().getTiers().getIdTiers());
                String nom = tiersAlloc.getDesignation1();
                String prenom = tiersAlloc.getDesignation2();

                properties.setProperty(AirsConstants.NSS, nss);

                properties.setProperty(AirsConstants.PRENOM, prenom);
                properties.setProperty(AirsConstants.NOM, nom);
                properties.setProperty(AirsConstants.DOMAINE, "ALLOC");
                properties.setProperty(AirsConstants.DATE_CREATION, JACalendar.todayJJsMMsAAAA());
                properties.setProperty(AirsConstants.USER_ID, JadeThread.currentContext().getUserId());
                JadeGedFacade.propagate(properties);

            } catch (Exception e) {
                JadeLogger.error(this, "could not propagate data for the following reasons : " + e);
            }

        }

    }
}
