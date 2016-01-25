package globaz.naos.util;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.ide.AFIdeAnnonce;
import globaz.naos.translation.CodeSystem;
import ch.globaz.common.properties.PropertiesException;

/**
 * recueil de règles qui passée une à une génère les annonces IDE nécessaires à la modification
 * </br><b>NB</b>: les <u>Annonces de mutation</u> sont liées à l'extrernal service et sont directement générées dans
 * {@link globaz.naos.externalservices.AFAnnonceIde}
 * 
 * @see globaz.naos.externalservices.AFAnnonceIde
 * 
 */
public class AFIDERules {
    // private static final Logger LOG = LoggerFactory.getLogger(AFIDERules.class);

    /**
     * Règles déterminant quelle(s) annonces sont a générer sur l'enregistement/modification/création d'une affiliation
     */
    public static String controleGenerateAnnonceOnAffiliation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {

        StringBuilder verbose = new StringBuilder();

        verbose.append("\n ruleReactNewAffiliation :" + ruleReactNewAffiliation(session, currentAff, oldAff));
        verbose.append("\n ruleReactReouvertureAffiliation :"
                + ruleReactReouvertureAffiliation(session, currentAff, oldAff));

        verbose.append("\n ruleReactFinProvisoirAffiliation :"
                + ruleReactFinProvisoirAffiliation(session, currentAff, oldAff));

        verbose.append("\n ruleClotureAffiliationAutre :" + ruleClotureAffiliationAutre(session, currentAff, oldAff));

        verbose.append("\n ruleClotureAffiliationCessation :"
                + ruleClotureAffiliationCessation(session, currentAff, oldAff));
        verbose.append("\n ruleModifDateDeFinAffiliationCessation :"
                + ruleModifDateDeFinAffiliationCessation(session, currentAff, oldAff));
        verbose.append("\n ruleModifMotifFinDepuisCessation :"
                + ruleModifMotifFinDepuisCessation(session, currentAff, oldAff));
        verbose.append("\n ruleModifMotifFinVersCessation :"
                + ruleModifMotifFinVersCessation(session, currentAff, oldAff));
        verbose.append("\n ruleAnnonceEnregistrementPassive: "
                + ruleAnnonceEnregistrementPassive(session, currentAff, oldAff));
        verbose.append("\n ruleAnnonceDesenregistementPassive: "
                + ruleAnnonceDesenregistementPassive(session, currentAff, oldAff));

        verbose.append("\n ruleDesenregistrementActif: " + ruleDesenregistrementActif(session, currentAff, oldAff));

        verbose.append("\n ruleEnregistrementAcif: " + ruleEnregistrementAcif(session, currentAff, oldAff));
        // TODO utiliser le logger logback de la version 1.15.01
        // en l'absence de logger, sysout crado pour dev.
        // System.out.println(verbose);
        return null;
    }

    /**
     * Règles déterminant quelle(s) annonces sont a générer sur l'enregistement/modification d'une Coti
     */
    public static void controleGenerateAnnonceModifCotisation(BSession session, AFCotisation afCotisation)
            throws Exception {
        AFCotisation oldCotisation = new AFCotisation();
        oldCotisation.setCotisationId(afCotisation.getCotisationId());
        oldCotisation.setAdhesionId(afCotisation.getAdhesionId());
        oldCotisation.setSession(afCotisation.getSession());
        oldCotisation.retrieve();

        StringBuilder verbose = new StringBuilder();

        verbose.append("\n ruleClotureCotiAVS :" + ruleClotureCotiAVS(session, afCotisation, oldCotisation));
        verbose.append("\n ruleReOuvertureCotiAVS :" + ruleReOuvertureCotiAVS(session, afCotisation, oldCotisation));
        // TODO utiliser le logger logback de la version 1.15.01
        // en l'absence de logger, sysout crado pour dev.
        System.out.println(verbose);
    }

    /**
     * Règles déterminant quelle(s) annonces sont a générer sur la création d'une Coti
     */
    public static void controleGenerateAnnonceOuvertureCotisation(BSession session, AFCotisation afCotisation)
            throws Exception {
        StringBuilder verbose = new StringBuilder();
        verbose.append("ruleOuvertureCotiAVS :" + ruleOuvertureCotiAVS(session, afCotisation));
        // TODO utiliser le logger logback de la version 1.15.01
        // en l'absence de logger, sysout crado pour dev.
        // System.out.println(verbose);
    }

    /**
     * Rules determinant les annonces réelles a créer suite a une demande d'annonce de creation IDE
     */
    public static String controleGenerateAnnonceCreationOnAffiliation(BSession session, AFAffiliation currentAff)
            throws Exception {

        if (currentAff.isIdeAnnoncePassive()) {
            return (session.getLabel("NAOS_ANNONCE_IDE_CREATION_AVERTISSEMENT_IDE_ANNONCE_PASSIVE"));
        }
        if (!AFIDEUtil.getListGenreAffilieActif().contains(currentAff.getTypeAffiliation())) {
            return (session.getLabel("NAOS_ANNONCE_IDE_CREATION_AVERTISSEMENT_GENRE_AFFILIATION"));
        }
        String message = AFIDEUtil.generateAnnonceCreationIde(session, currentAff);
        if (JadeStringUtil.isBlankOrZero(message)) {
            controleGenerateAnnonceCreationCloture(session, currentAff);
        }
        return message;

    }

    public static void controleGenerateAnnonceCreationCloture(BSession session, AFAffiliation currentAff)
            throws Exception {
        StringBuilder verbose = new StringBuilder();
        verbose.append("\n ruleCreationClotureAffiliationAutre :"
                + ruleCreationClotureAffiliationAutre(session, currentAff));
        verbose.append("\n ruleCreationClotureAffiliationCessation :"
                + ruleCreationClotureAffiliationCessation(session, currentAff));
        // TODO utiliser le logger logback de la version 1.15.01
        // en l'absence de logger, sysout crado pour dev.
        // System.out.println(verbose);
    }

    // ********************************
    // * Rules on Affiliation update *
    // ********************************

    // IDE ANNONCE PASSIVE
    private static boolean ruleAnnonceEnregistrementPassive(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (isAnnonceIDEPassive(currentAff)
                && (!isAnnonceIDEPassive(oldAff) || hasIDENumberChanged(currentAff, oldAff))) {
            AFIDEUtil.generateAnnonceEnregistrementPassif(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * Si on passe d'une affiliation Passive à active -> désenregistrement passif
     * Si on change le num IDE et que la oldAff était passive -> désenregistrement passif de la oldAff
     * 
     */
    private static boolean ruleAnnonceDesenregistementPassive(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (!isAnnonceIDEPassive(currentAff) && isAnnonceIDEPassive(oldAff)) {
            AFIDEUtil.generateAnnonceDesenregistrementPassif(session, currentAff);
            return true;
        }
        if (isAnnonceIDEPassive(oldAff) && hasIDENumberChanged(currentAff, oldAff) && isIDENumberBlank(currentAff)
                && !isIDENumberBlank(oldAff)) {
            AFIDEUtil.generateAnnonceDesenregistrementPassif(session, oldAff);
        }
        return false;
    }

    /**
     * Si l'affiliation était active et ne l'est plus,</br> <i>ou</i> que l'on a supprimé le numéro IDE;</br> -> générer
     * un
     * <b>Désenregistrement Actif</b>
     */
    private static boolean ruleDesenregistrementActif(BSession session, AFAffiliation currentAff, AFAffiliation oldAff)
            throws Exception {
        if (!isAffiliationActive(currentAff) && isAffiliationActive(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceDesenregistrementActif(session, currentAff, null);
            return true;
        } else if (isIDENumberBlank(currentAff) && !isIDENumberBlank(oldAff) && isAffiliationActive(oldAff)
                && hasIDENumberChanged(currentAff, oldAff)) {
            // NB: Désenregisrement Actif sur la oldAFF,
            // persister le numéro IDE a traiter puisque perdu de l'aff
            AFIdeAnnonce annonce = AFIDEUtil.generateAnnonceDesenregistrementActif(session, oldAff, null);
            if (annonce != null) {
                annonce.setNumeroIdeRemplacement(oldAff.getNumeroIDE());
                annonce.update();
                return true;
            }
        }
        return false;
    }

    /**
     * générer un enregistrement actif sur la réactivation d'un provisoire ou un ideAnnoncePassive
     */
    private static boolean ruleEnregistrementAcif(BSession session, AFAffiliation currentAff, AFAffiliation oldAff)
            throws Exception {
        if (isAffiliationActive(currentAff)
                && !isAnnonceIDEPassive(currentAff)
                && (isIDENumberBlank(oldAff) || isAnnonceIDEPassive(oldAff)
                        || (!hasDateDeFin(currentAff) && !isAffiliationActive(oldAff)) || (!hasDateDeFin(currentAff) && !isFirstAffiliationDateFin(oldAff)))) {
            if (!isNumeroIdeInactifRadie(currentAff)) {
                AFIDEUtil.generateAnnonceEnregistrementActif(session, currentAff, null);
            } else {
                AFIDEUtil.generateAnnonceReactivationIde(session, currentAff);
            }
            return true;
        }
        return false;
    }

    // Réactivation
    /**
     * *<b>Réactivation</b></br>
     * Nouvelle Affiliation sur un IDE radié/Inactif -> réactivation
     */
    private static boolean ruleReactNewAffiliation(BSession session, AFAffiliation currentAff, AFAffiliation oldAff)
            throws Exception {
        if (isAffiliationActive(currentAff) && !hasDateDeFin(currentAff) && isNumeroIdeInactifRadie(currentAff)
                && hasChangeFromInactifRadie(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceReactivationIde(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * *<b>Réactivation</b></br>
     * réouverture (retirer la date de fin) d'un affilié sur un IDE radié/Inactif -> réactivation
     */
    private static boolean ruleReactReouvertureAffiliation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (isAffiliationActive(currentAff) && !hasDateDeFin(currentAff) && !isFirstAffiliationDateFin(oldAff)
                && isNumeroIdeInactifRadie(currentAff) && !isNewAffiliation(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceReactivationIde(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * <b>Réactivation</b></br>
     * l'affiliation était provisoire (inactive) et ne l'est plus, sur un IDE radié/Inactif -> réactivation
     */
    private static boolean ruleReactFinProvisoirAffiliation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (isAffiliationActive(currentAff) && !isAffiliationActive(oldAff) && !hasDateDeFin(currentAff)
                && isNumeroIdeInactifRadie(currentAff) && !isNewAffiliation(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceReactivationIde(session, currentAff);
            return true;
        }
        return false;
    }

    // Radiation pour Autre
    /**
     * *<b>Radiation pour Autre</b></br>
     * clôture de dossier avec un motif ni radiation, ni transfert -> désenregistrement
     */
    private static boolean ruleClotureAffiliationAutre(BSession session, AFAffiliation currentAff, AFAffiliation oldAff)
            throws Exception {
        if (isAffiliationActive(currentAff)
                && hasDateDeFin(currentAff) // && !isMotifChangementCaisse(currentAff)
                                            // autre = !cessation
                && !isMotifCessation(currentAff) && isFirstAffiliationDateFin(oldAff)
                && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceDesenregistrementActif(session, currentAff, null);
            return true;
        }
        return false;
    }

    // Radiation pour cessation d'activité
    /**
     * *<b>Radiation pour cessation d'activité</b></br>
     * clôture de dossier avec un motif Changement de caisse -> pas de radiation mais désenregistrement
     */
    private static boolean ruleClotureAffiliationCessation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (isAffiliationActive(currentAff) && hasDateDeFin(currentAff) && isMotifCessation(currentAff)
                && isFirstAffiliationDateFin(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceRadiationIde(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * *<b>Radiation pour cessation d'activité</b></br>
     * Modification de la date de fin d'affiliation, ne doit pas générer d'annonce
     */
    private static boolean ruleModifDateDeFinAffiliationCessation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (!isNewAffiliation(oldAff) && isAffiliationActive(currentAff) && hasDateDeFin(currentAff)
                && isMotifCessation(currentAff) && isSameMotifFin(currentAff, oldAff)
                && !isFirstAffiliationDateFin(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            // do nothing
            return true;
        }
        return false;
    }

    /**
     * <b>Editer une aff. Radié : depuis Cessation vers .?.</b></br>
     * reactivation et se desenregistrer
     */
    private static boolean ruleModifMotifFinDepuisCessation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (!isNewAffiliation(oldAff) && isAffiliationActive(currentAff) && hasDateDeFin(currentAff)
                && !isSameMotifFin(currentAff, oldAff) && isMotifCessation(oldAff) && !isMotifCessation(currentAff)
                && !isFirstAffiliationDateFin(oldAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnoncesReactivation(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * * <b>Editer une aff. Radié : depuis .?. vers Cessation</b></br>
     * au besoin, Radiation
     */
    private static boolean ruleModifMotifFinVersCessation(BSession session, AFAffiliation currentAff,
            AFAffiliation oldAff) throws Exception {
        if (!isNewAffiliation(oldAff) && isAffiliationActive(currentAff) && hasDateDeFin(currentAff)
                && !isSameMotifFin(currentAff, oldAff) && !isMotifCessation(oldAff) && isMotifCessation(currentAff)
                && !isFirstAffiliationDateFin(oldAff) && !isAnnonceIDEPassive(currentAff)
                && !isNumeroIdeInactifRadie(currentAff)) {
            AFIDEUtil.generateAnnoncesRadiation(session, currentAff);
            return true;
        }
        return false;
    }

    // ******************************
    // * Rules on Cotisation update *
    // ******************************
    private static boolean ruleClotureCotiAVS(BSession session, AFCotisation afCotisation, AFCotisation oldCotisation)
            throws Exception {
        AFAffiliation currentAff = afCotisation.getAffiliation();
        if (isAffiliationActive(currentAff) && hasDateDeFinCoti(afCotisation) && !hasDateDeFinCoti(oldCotisation)
                && isOnlyOneCotiAVSAIOpenForAff(session, currentAff, afCotisation) && isCotiAVSAI(afCotisation)) {
            AFIDEUtil.generateAnnonceDesenregistrementActifFromCoti(session, currentAff, afCotisation);
            return true;
        }
        return false;
    }

    /**
     * Réouverture d'une coti (retirer la date de fin), forcer l'affiliation à redevenir active, retirer l'eventuel
     * enregistrement passif
     */
    private static boolean ruleReOuvertureCotiAVS(BSession session, AFCotisation afCotisation,
            AFCotisation oldCotisation) throws Exception {
        AFAffiliation currentAff = afCotisation.getAffiliation();
        if (isAffiliationActive(currentAff) && !hasDateDeFinCoti(afCotisation) && hasDateDeFinCoti(oldCotisation)
                && isCotiAVSAI(afCotisation)) {
            if (AFIDEUtil.forceUncheckIDEAnnoncePassive(currentAff)) {
                AFIDEUtil.generateAnnonceDesenregistrementPassif(session, currentAff);
            }
            AFIDEUtil.generateAnnonceEnregistrementActifFromCoti(session, currentAff, afCotisation);
            return true;
        }
        return false;
    }

    /**
     * ouverture de coti
     */
    private static boolean ruleOuvertureCotiAVS(BSession session, AFCotisation afCotisation) throws Exception {
        AFAffiliation currentAff = afCotisation.getAffiliation();
        if (isAffiliationActive(currentAff) && !hasDateDeFinCoti(afCotisation) && isCotiAVSAI(afCotisation)) {
            if (AFIDEUtil.forceUncheckIDEAnnoncePassive(currentAff)) {
                AFIDEUtil.generateAnnonceDesenregistrementPassif(session, currentAff);
            }
            AFIDEUtil.generateAnnonceEnregistrementActifFromCoti(session, currentAff, afCotisation);
            return true;
        }
        return false;
    }

    // *******************************
    // * CREATION D'AFFILIATION *
    // *******************************
    /**
     * *<b>Creation Affiliation</b></br>
     * clôture de dossier avec un motif Cessation -> radiation sans ide
     */
    private static boolean ruleCreationClotureAffiliationCessation(BSession session, AFAffiliation currentAff)
            throws Exception {
        if (isAffiliationActive(currentAff) && hasDateDeFin(currentAff) && isMotifCessation(currentAff)
                && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceRadiationSansIde(session, currentAff);
            return true;
        }
        return false;
    }

    /**
     * *<b>Radiation pour motif Autre</b></br>
     * clôture de dossier avec un motif Autre -> pas de radiation mais désenregistrement
     */
    private static boolean ruleCreationClotureAffiliationAutre(BSession session, AFAffiliation currentAff)
            throws Exception {
        if (isAffiliationActive(currentAff) && hasDateDeFin(currentAff) // && !isMotifChangementCaisse(currentAff)
                                                                        // autre = !cessasstion
                && !isMotifCessation(currentAff) && !isAnnonceIDEPassive(currentAff)) {
            AFIDEUtil.generateAnnonceDesenregistrementActifSansIde(session, currentAff, null);
            return true;
        }
        return false;
    }

    // ***************************************************************
    // * Sous règles propre à affiliation *
    // ***************************************************************

    private static boolean isAnnonceIDEPassive(AFAffiliation currentAff) {
        if (currentAff == null) {
            return false;
        }
        return currentAff.isIdeAnnoncePassive();
    }

    private static boolean isAffiliationActive(AFAffiliation currentAff) throws PropertiesException {
        if (currentAff == null) {
            return false;
        }
        return AFIDEUtil.hasDroitEnregistrementActif(currentAff);
    }

    private static boolean hasDateDeFin(AFAffiliation currentAff) {
        return !JadeStringUtil.isBlankOrZero(currentAff.getDateFin());
    }

    /**
     * Détermine si le motif de fin de l'affiliation est dans la liste des motif de fin a considérer comme de la
     * Cessation(et donc radiation pour l'ide)
     * 
     * @param currentAff
     * @return
     * @throws PropertiesException
     */
    private static boolean isMotifCessation(AFAffiliation currentAff) throws PropertiesException {
        return AFIDEUtil.getListMotifsFinCessation().contains(currentAff.getMotifFin());
    }

    /**
     * si la oldaff n'avait pas encore de date de fin de renseignée (ou qu'elle est null càd création d'aff)
     */
    private static boolean isFirstAffiliationDateFin(AFAffiliation oldAff) {
        if (isNewAffiliation(oldAff)) {
            return true;
        }
        return oldAff.getDateFin().isEmpty();
    }

    /** sur la oldaff, si le numIDE etait null ou que la oldaff etait null (création), similaire à FirstNumeroIDE **/
    private static boolean isIDENumberBlank(AFAffiliation oldAff) {
        if (isNewAffiliation(oldAff)) {
            return true;
        }
        return JadeStringUtil.isBlankOrZero(oldAff.getNumeroIDE());
    }

    private static boolean hasIDENumberChanged(AFAffiliation currentAff, AFAffiliation oldAff) {
        if (isNewAffiliation(oldAff)) {
            return true;
        }
        return !currentAff.getNumeroIDE().equals(oldAff.getNumeroIDE());
    }

    private static boolean isSameMotifFin(AFAffiliation currentAff, AFAffiliation oldAff) {
        if (isNewAffiliation(oldAff)) {
            return false;
        }
        return currentAff.getMotifFin().equals(oldAff.getMotifFin());
    }

    private static boolean isNumeroIdeInactifRadie(AFAffiliation currentAff) {
        return CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(currentAff.getIdeStatut());
    }

    private static boolean hasChangeFromInactifRadie(AFAffiliation oldAff) {
        if (isNewAffiliation(oldAff)) {
            return true;
        }
        return !isNumeroIdeInactifRadie(oldAff);
    }

    private static boolean isNewAffiliation(AFAffiliation oldAff) {
        return oldAff == null;
    }

    // ***********************************************
    // * Sous règles propre au cotisation *
    // ***********************************************
    private static boolean hasDateDeFinCoti(AFCotisation afCotisation) {
        return !JadeStringUtil.isBlankOrZero(afCotisation.getDateFin());
    }

    private static boolean isCotiAVSAI(AFCotisation afCotisation) {
        return CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(afCotisation.getAssurance().getTypeAssurance());
    }

    private static boolean isOnlyOneCotiAVSAIOpenForAff(BSession session, AFAffiliation currentAff,
            AFCotisation afCotisation) throws Exception {
        return !AFIDEUtil.hasAffiliationCotisationAVSSansDateFinOtherThanThisCoti(session,
                currentAff.getAffiliationId(), afCotisation.getCotisationId());
    }
}
