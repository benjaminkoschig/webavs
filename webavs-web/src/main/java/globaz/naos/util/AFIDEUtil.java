package globaz.naos.util;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.controller.FWController;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.ide.AFIdeAnnonce;
import globaz.naos.db.ide.AFIdeAnnonceManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.exceptions.AFIdeNumberNoMatchException;
import globaz.naos.properties.AFProperties;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import idech.admin.bit.xmlns.uid_wse_shared._1.RegisterDeregisterStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Classe utilitaire de gestion du numéro IDE, des annonces IDE
 */
public class AFIDEUtil {

    public static final String TYPE_RECHERCHE_NUM_IDE = "searchNumeroIde";
    public static final String TYPE_RECHERCHE_RAISON_SOCIALE = "searchRaisonSociale";
    public static final String IDE_PREFIXE = "CHE";
    public static final String IDE_FORMATED_PREFIXE = "CHE-";
    public static final String IDE_FORMAT = "   -   .   .   ";
    public static final String IDE_FORMAT_SANS_CHE = "   .   .   ";
    public static final String OFS_NOGA_INDETERMINE = "990099";

    /**
     * Determine si l'annonce peut être supprimée
     * Suppression autorisée si l'état est "SUPSPEND", "ENREGISTRE"
     * 
     * @return boolean
     */
    public static boolean isAnnonceSupprimable(String etatAnnonce, String typeAnnonce) {
        return AFIDEUtil.getListEtatAnnonceIdeEnCours().contains(etatAnnonce);

    }

    /**
     * retourne les types d'annonce ENTRANTE qui sont passive
     * 
     * @return List<String>
     */
    public static List<String> getListTypeAnnoncePassive() {

        List<String> listTypeAnnoncePassive = new ArrayList<String>();

        listTypeAnnoncePassive.add(CodeSystem.TYPE_ANNONCE_IDE_CREATION_INFO_ABO);
        listTypeAnnoncePassive.add(CodeSystem.TYPE_ANNONCE_IDE_MUTATION_INFO_ABO);

        return listTypeAnnoncePassive;
    }

    /**
     * Determine si l'annonce entrante (INFO_ABO) est passive <br/>
     * Retour SEDEX passif si l'ide est enregistré passivement (annonce sortantes)
     * 
     * @param annonce
     * @return
     */
    public static boolean isAnnoncePassive(AFIdeAnnonce annonce) {

        String typeAnnonce = annonce.getIdeAnnonceType();

        return getListTypeAnnoncePassive().contains(typeAnnonce);
        // return CodeSystem.TYPE_ANNONCE_IDE_MUTATION_INFO_ABO.equalsIgnoreCase(typeAnnonce)
        // || CodeSystem.TYPE_ANNONCE_IDE_CREATION_INFO_ABO.equalsIgnoreCase(typeAnnonce);
    }

    public static StringBuffer removeEndingSpacesAndDoublePoint(StringBuffer sb) {

        if (sb == null || JadeStringUtil.isBlank(sb.toString())) {
            return sb;
        }

        String sbContent = sb.toString();
        sbContent = sbContent.replaceAll("\\s+$", "");

        if (sbContent.endsWith(":")) {
            sbContent = sbContent.substring(0, sbContent.length() - 1);
            sbContent = sbContent.replaceAll("\\s+$", "");
        }

        return new StringBuffer(sbContent);

    }

    public static StringBuilder removeEndingSpacesAndDoublePoint(StringBuilder sb) {

        if (sb == null || JadeStringUtil.isBlank(sb.toString())) {
            return sb;
        }

        String sbContent = sb.toString();
        sbContent = sbContent.replaceAll("\\s+$", "");

        if (sbContent.endsWith(":")) {
            sbContent = sbContent.substring(0, sbContent.length() - 1);
            sbContent = sbContent.replaceAll("\\s+$", "");
        }

        return new StringBuilder(sbContent);

    }

    /**
     * Return property 'GroupID' used by process to send mail at end of process
     * 
     * @param idGroupe
     * @return groupid
     * @throws Exception
     */
    public static List<String> giveMeUserGroupMail(String idGroupe) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idGroupe)) {
            throw new Exception(
                    "Technical Error AFIDEUtil.giveMeUserGroupMail : unable to find user group mail if user group is blank or zero ");
        }

        List<String> listUserMail = new ArrayList<String>();

        String[] tabUserVisa = JadeAdminServiceLocatorProvider.getLocator().getUserGroupService()
                .findAllIdUserForIdGroup(idGroupe);

        if (tabUserVisa != null) {

            for (String userVisa : tabUserVisa) {
                JadeUser jadeUser = JadeAdminServiceLocatorProvider.getLocator().getUserService().loadForVisa(userVisa);
                String userMail = jadeUser.getEmail();
                if (!JadeStringUtil.isBlankOrZero(userMail)) {
                    listUserMail.add(userMail);
                }

            }
        }

        return listUserMail;
    }

    public static String giveMeFirstIdAnnonceCreationEnCoursForIdTiers(BSession session, String idTiers) {

        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return "";
        }

        try {

            AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
            ideAnnonceManager.setSession(session);
            ideAnnonceManager.setForIdTiers(idTiers);
            ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
            ideAnnonceManager.setForType(CodeSystem.TYPE_ANNONCE_IDE_CREATION);
            ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
            ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

            if (ideAnnonceManager.size() >= 1) {
                return ((AFIdeAnnonce) ideAnnonceManager.getFirstEntity()).getIdeAnnonceIdAnnonce();
            }

        } catch (Exception e) {
            return "";
        }

        return "";

    }

    public static String giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul(AFIdeAnnonce ideAnnonce) {

        String numAff = ideAnnonce.getNumeroAffilie();
        String numAffLiee = ideAnnonce.getIdeAnnonceListNumeroAffilieLiee();
        // D0181 si j'ai un num affilié historisé sur l'annonce
        String numAffForEcran = (ideAnnonce.getHistNumeroAffilie().isEmpty() ? numAff : ideAnnonce
                .getHistNumeroAffilie());
        if (!JadeStringUtil.isBlankOrZero(numAff) && !JadeStringUtil.isBlankOrZero(numAffLiee)) {
            numAffForEcran = numAffForEcran + ",";
        }

        return numAffForEcran + numAffLiee;

    }

    public static boolean isAnnonceRespectedMultiAffMandatory(BSession session, AFIdeAnnonce annonce, String numeroIde)
            throws Exception {

        if (!JadeStringUtil.isBlankOrZero(numeroIde)) {

            if (CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI.equalsIgnoreCase(annonce.getIdeAnnonceCategorie())
                    && AFIDEUtil.isSameAnnonceEnCours(session, annonce, numeroIde)) {
                return false;
            }

            List<AFAffiliation> listAffiliation = AFAffiliationUtil.loadAffiliationUsingNumeroIdeForCheckMultiAff(
                    session, numeroIde, annonce.getIdeAnnonceIdAffiliation());

            if (listAffiliation != null) {

                if (CodeSystem.TYPE_ANNONCE_IDE_RADIATION.equalsIgnoreCase(annonce.getIdeAnnonceType())) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (JadeStringUtil.isBlankOrZero(aAffiliation.getDateFin())) {
                            return false;
                        }
                    }

                }

                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF.equalsIgnoreCase(annonce.getIdeAnnonceType())) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (aAffiliation.isIdeAnnoncePassive()) {
                            return false;
                        }
                    }

                }

                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equalsIgnoreCase(annonce.getIdeAnnonceType())) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (!AFIDEUtil.isAffiliationRespectedDesenregistrementActifMultiAff(session, aAffiliation)) {
                            return false;
                        }
                    }

                }

            }

        }

        return true;

    }

    public static boolean isAnnonceRespectedMultiAffMandatoryMultiAff(BSession session, String ideAnnonceType,
            String ideAnnonceCategorie, String ideAnnonceIdAffiliation, String numeroIde) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(numeroIde)) {

            if (CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI.equalsIgnoreCase(ideAnnonceCategorie)
                    && AFIDEUtil.hasAnnonceEnCoursMultiAff(session, ideAnnonceType, ideAnnonceCategorie, numeroIde)) {
                return false;
            }

            List<AFAffiliation> listAffiliation = AFAffiliationUtil.loadAffiliationUsingNumeroIdeForCheckMultiAff(
                    session, numeroIde, ideAnnonceIdAffiliation);

            if (listAffiliation != null) {

                if (CodeSystem.TYPE_ANNONCE_IDE_RADIATION.equalsIgnoreCase(ideAnnonceType)) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (JadeStringUtil.isBlankOrZero(aAffiliation.getDateFin())) {
                            return false;
                        }
                    }

                }

                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF.equalsIgnoreCase(ideAnnonceType)) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (aAffiliation.isIdeAnnoncePassive()) {
                            return false;
                        }
                    }

                }

                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equalsIgnoreCase(ideAnnonceType)) {
                    for (AFAffiliation aAffiliation : listAffiliation) {
                        if (!AFIDEUtil.isAffiliationRespectedDesenregistrementActifMultiAff(session, aAffiliation)) {
                            return false;
                        }
                    }

                }

            }

        }

        return true;

    }

    public static boolean isAffiliationRespectedDesenregistrementActifMultiAff(BSession session,
            AFAffiliation affiliation) throws Exception {

        if (hasAnnonceEnCours(session, affiliation.getAffiliationId(), CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)) {
            return false;
        }

        if (hasDroitEnregistrementActif(affiliation)
                && AFIDEUtil.hasAffiliationCotisationAVSSansDateFin(session, affiliation.getAffiliationId())) {
            return false;
        }

        return true;

    }

    /**
     * Si une annonce en cours existe en DB avec les même caractéristiques (Numéro IDE, Type et Catégorie)
     * 
     * @return si une annonce de même type et catégorie existe pour ce numéro IDE </br>(<b>NB</b>: à tester avant de
     *         persister sinon l'annonce est comparée à elle même)
     * @throws Exception
     */
    public static boolean isSameAnnonceEnCours(BSession session, AFIdeAnnonce ideAnnonce, String numeroIde)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroIde)) {
            return false;
        }

        return hasAnnonceEnCoursMultiAff(session, ideAnnonce.getIdeAnnonceType(), ideAnnonce.getIdeAnnonceCategorie(),
                numeroIde);
    }

    public static boolean hasAnnonceEnCoursMultiAff(BSession session, String ideAnnonceType,
            String ideAnnonceCategorie, String numeroIde) throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroIde)) {
            return false;
        }

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForType(ideAnnonceType);
        ideAnnonceManager.setForCategorie(ideAnnonceCategorie);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
        ideAnnonceManager.setForNumeroIde(numeroIde);

        return ideAnnonceManager.getCount() >= 1;

    }

    public static boolean isAnnonceAnnulationDoublon(AFIdeAnnonce annonce) {

        return CodeSystem.TYPE_ANNONCE_IDE_ANNULEE.equalsIgnoreCase(annonce.getIdeAnnonceType())
                && !JadeStringUtil.isBlankOrZero(annonce.getNumeroIdeRemplacement());

    }

    public static boolean isAnnonceAnnulationSansRemplacement(AFIdeAnnonce annonce) {

        return CodeSystem.TYPE_ANNONCE_IDE_ANNULEE.equalsIgnoreCase(annonce.getIdeAnnonceType())
                && JadeStringUtil.isBlankOrZero(annonce.getNumeroIdeRemplacement());

    }

    public static String checkAnnonceCreationMandatory(BSession session, String idAffiliation) throws Exception {

        AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(idAffiliation, session);
        StringBuilder errorBuffer = new StringBuilder();

        errorBuffer.append(checkAnnonceCreationMutationCommonMandatory(session, affiliation));

        if (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MANDATORY_ERREUR_NUMERO_IDE_AFFILIATION_NOT_BLANK"));

        }
        if (JadeStringUtil.isBlankOrZero(affiliation.getActivite())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MANDATORY_ERREUR_ACTIVITE_AFFILIATION_NOT_BLANK"));

        }

        return errorBuffer.toString();
    }

    public static String checkAnnonceEntranteMandatory(BSession session, AFIdeAnnonce ideAnnonceEntrante) {

        StringBuffer errorBuffer = new StringBuffer();

        if (JadeStringUtil.isBlankOrZero(ideAnnonceEntrante.getHistNumeroIde())) {
            errorBuffer.append(session
                    .getLabel("NAOS_IDE_UTIL_CHECK_ANNONCE_ENTRANTE_MANDATORY_ERREUR_NUMERO_IDE_BLANK_OR_ZERO"));
        }

        return errorBuffer.toString();

    }

    public static void createHistoriqueDataInAnnonce(BSession session, IDEDataBean ideDataBean, AFIdeAnnonce ideAnnonce)
            throws Exception {

        ideAnnonce.setHistAdresse(ideDataBean.getAdresse());
        ideAnnonce.setHistRue(ideDataBean.getRue() + " " + ideDataBean.getNumeroRue());
        ideAnnonce.setHistNPA(ideDataBean.getNpa());
        ideAnnonce.setHistLocalite(ideDataBean.getLocalite());
        ideAnnonce.setHistBrancheEconomique(CodeSystem.getLibelle(session, ideDataBean.getBrancheEconomique()));
        ideAnnonce.setHistFormeJuridique(CodeSystem.getLibelle(session, ideDataBean.getPersonnaliteJuridique()));
        ideAnnonce.setHistCanton(ideDataBean.getCanton());
        ideAnnonce.setHistLangueTiers(ideDataBean.getLangue());
        ideAnnonce.setHistNumeroIde(ideDataBean.getNumeroIDE());
        ideAnnonce.setHistRaisonSociale(ideDataBean.getRaisonSociale());
        ideAnnonce.setHistStatutIde(ideDataBean.getStatut());
        // D0181
        ideAnnonce.setHistNumeroAffilie(ideDataBean.getNumeroAffilie());
        ideAnnonce.setHistNaissance(ideDataBean.getNaissance());
        ideAnnonce.setHistActivite(ideDataBean.getActivite());
        ideAnnonce.setHistNoga(ideDataBean.getNogaCode());

        // si la date de validité OFS est dans le passé, alors la date du jour est persistée à la place
        ideAnnonce.setHistTypeAnnonceDate(!JadeStringUtil.isBlankOrZero(ideAnnonce.getTypeAnnonceDate())
                && BSessionUtil.compareDateFirstGreater(session, ideAnnonce.getTypeAnnonceDate(),
                        JACalendar.todayJJsMMsAAAA()) ? ideAnnonce.getTypeAnnonceDate() : JACalendar.todayJJsMMsAAAA());
    }

    public static void updateHistoriqueDataInAnnonce(IDEDataBean ideDataBean, AFIdeAnnonce ideAnnonce) {

        ideAnnonce.setHistNumeroIde(ideDataBean.getNumeroIDE());
        ideAnnonce.setHistStatutIde(ideDataBean.getStatut());

    }

    public static String checkAnnonceMutationMandatory(BSession session, String idAffiliation) throws Exception {

        AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(idAffiliation, session);
        StringBuilder errorBuffer = new StringBuilder();

        errorBuffer.append(checkAnnonceCreationMutationCommonMandatory(session, affiliation));

        // TODO CEL verifier si Mut sur Definitif only
        if (JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MUTATION_MANDATORY_ERREUR_NUMERO_IDE_NON_VALIDE"));

        }

        return errorBuffer.toString();

    }

    private static String checkAnnonceCreationMutationCommonMandatory(BSession session, AFAffiliation affiliation)
            throws Exception {

        TITiersViewBean tiers = affiliation.getTiers();
        StringBuilder errorBuffer = new StringBuilder();

        if (affiliation.isTraitement()) {
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_AFFILIATION_PROVISOIRE"));
        }

        if (affiliation.isIdeAnnoncePassive()) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_AVERTISSEMENT_IDE_ANNONCE_PASSIVE"));
        }

        if (!AFIDEUtil.getListGenreAffilieActif().contains(affiliation.getTypeAffiliation())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_GENRE_AFFILIE_NON_PERMIS"));
        }

        if (JadeStringUtil.isBlankOrZero(affiliation.getRaisonSociale())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_RAISON_SOCIALE_AFFILIATION_BLANK"));

        }

        if (JadeStringUtil.isBlankOrZero(tiers.getLangueIso())) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_LANGUE_TIERS_BLANK"));

        }

        if (!AFIDEUtil.hasTiersAdresseForIdeOrFromCascade(session, affiliation)) {
            if (errorBuffer.length() > 0) {
                errorBuffer.append(" / ");
            }
            errorBuffer.append(session
                    .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_ADRESSE_TIERS_BLANK"));

        }

        return errorBuffer.toString();

    }

    public static String logExceptionAndCreateMessageForUser(BSession session, Exception e) {

        if (e != null && session != null) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            if (e instanceof AFIdeNumberNoMatchException) {
                return e.getMessage();
            } else {
                return session.getLabel("NAOS_IDE_MESSAGE_ERREUR_GENERIQUE_EXCEPTION");
            }
        }

        return "";

    }

    public static final void handleError(BSession session, Exception e, AFIdeAnnonce ideAnnonce) {

        String messageErreurForUser = AFIDEUtil.logExceptionAndCreateMessageForUser(session, e);

        String erreur = ideAnnonce.getMessageErreurForBusinessUser();
        if (!JadeStringUtil.isBlankOrZero(erreur)) {
            erreur = erreur + " / ";
        }
        erreur = erreur + messageErreurForUser;

        ideAnnonce.setMessageErreurForBusinessUser(erreur);
    }

    public static String giveMeStatusAnnonceApresTraitementAccordingToError(AFIdeAnnonce ideAnnonce,
            boolean errorMustFlagAnnonceAsSuccess) {

        if (ideAnnonce.hasAnnonceErreur() && !errorMustFlagAnnonceAsSuccess) {
            return CodeSystem.ETAT_ANNONCE_IDE_ERREUR;
        }
        return CodeSystem.ETAT_ANNONCE_IDE_TRAITE;
    }

    // **********************************
    // * Traitement des DATA pour IDEAnnonce object *
    // **********************************
    /**
     * 
     * @param adresseDataSource
     * @return
     * @throws Exception
     */
    public static String formatAdresseForIde(TIAdresseDataSource adresseDataSource) throws Exception {

        if (adresseDataSource != null) {
            StringBuilder adresseStringBuilder = new StringBuilder();
            adresseStringBuilder.append(adresseDataSource.rue);
            adresseStringBuilder.append(" ");
            adresseStringBuilder.append(adresseDataSource.numeroRue);
            adresseStringBuilder.append(" - ");
            adresseStringBuilder.append(adresseDataSource.localiteNpa);
            adresseStringBuilder.append(" ");
            adresseStringBuilder.append(adresseDataSource.localiteNom);

            return adresseStringBuilder.toString();
        }

        return "";
    }

    public static IDEDataBean transformIdeAnnonceEnIdeDataBean(AFIdeAnnonce ideAnnonce, BSession session)
            throws Exception {

        TITiersViewBean tiers = loadTiers(session, ideAnnonce.getIdTiers());
        AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(ideAnnonce.getIdeAnnonceIdAffiliation(), session);

        TIAdresseDataSource adresseDataSource = loadAdresseFromCascadeIde(affiliation);

        IDEDataBean ideDataBean = new IDEDataBean();

        if (adresseDataSource != null) {
            ideDataBean.setAdresse(AFIDEUtil.formatAdresseForIde(adresseDataSource));
            ideDataBean.setCanton(adresseDataSource.canton_court);
            ideDataBean.setLocalite(adresseDataSource.localiteNom);
            ideDataBean.setNpa(adresseDataSource.localiteNpa);
            ideDataBean.setRue(adresseDataSource.rue);
            ideDataBean.setNumeroRue(adresseDataSource.numeroRue);
            ideDataBean.setCareOf(adresseDataSource.attention);
        }

        ideDataBean.setLangue(tiers.getLangueIso().toUpperCase());
        ideDataBean.setNumeroIDE(ideAnnonce.getNumeroIde());
        ideDataBean.setNumeroIDERemplacement(ideAnnonce.getNumeroIdeRemplacement());
        // D0181//ideDataBean.setRaisonSociale(ideAnnonce.getRaisonSociale());
        ideDataBean.setStatut(ideAnnonce.getStatutIde());
        ideDataBean.setBrancheEconomique(affiliation.getBrancheEconomique());
        ideDataBean.setPersonnaliteJuridique(affiliation.getPersonnaliteJuridique());
        // D0181
        if (tiers.getPersonnePhysique()) {
            ideDataBean.setNaissance(tiers.getDateNaissance());
            ideDataBean.setRaisonSociale(tiers.getPrenomNom());
        } else {
            ideDataBean.setRaisonSociale(ideAnnonce.getRaisonSociale());
        }
        ideDataBean.setActivite(affiliation.getActivite());
        ideDataBean.setNumeroAffilie(ideAnnonce.getNumeroAffilie());
        // le code noga n'est rempli qu'en annonce entrante
        ideDataBean.setNogaCode("");

        return ideDataBean;
    }

    /**
     * Si l'annonce est une radiation, et aue la date de fin d'affiliation liée est une date future, alors retourne
     * cette date future formatée pour le WebService du Registre
     * 
     * @param ideAnnonce l'annonce de radiation
     * @param session
     * @param transaction transaction pour le convertisseur de date (message d'erreur)
     *            {@link ch.admin.uid.xmlns.uid_wse.IDEServiceCallUtil#convertDateAMJtoXMLDateGregorian(BSession, BTransaction, String, String)}
     * 
     * @return null si la date n'est pas future
     * @throws Exception
     */
    public static XMLGregorianCalendar getDateRadiationSiFutur(AFIdeAnnonce ideAnnonce, BSession session,
            BTransaction transaction) throws Exception {
        if (CodeSystem.TYPE_ANNONCE_IDE_RADIATION.equalsIgnoreCase(ideAnnonce.getIdeAnnonceType())) {
            AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(ideAnnonce.getIdeAnnonceIdAffiliation(),
                    session);
            if (!JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                String dateString = affiliation.getDateFin();
                if (JadeDateUtil.isDateAfter(dateString, JACalendar.todayJJsMMsAAAA())) {
                    XMLGregorianCalendar scheduleDate = IDEServiceCallUtil.convertDateAMJtoXMLDateGregorian(session,
                            transaction, dateString, affiliation.getAffilieNumero());
                    return scheduleDate;
                }
            }
        }
        return null;

    }

    /**
     * si l'affiliation a (au moins) une annonce <b>en cours</b>
     */
    public static int countAffiliationAnnonceEnCours(BSession session, String idAffiliation) throws Exception {

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setForIdAffiliation(idAffiliation);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());

        return ideAnnonceManager.getCount();

    }

    /**
     * si une annonce <b>en cours</b> existe pour l'affiliation de <u>type</u> et <u>catégorie</u> spécifié
     * 
     * @param typeAnnonce (ex. CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF)
     * @param categorie (ex. CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)
     */
    public static boolean hasAnnonceEnCours(BSession session, String idAffiliation, String typeAnnonce, String categorie)
            throws Exception {

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForIdAffiliation(idAffiliation);
        ideAnnonceManager.setForType(typeAnnonce);
        ideAnnonceManager.setForCategorie(categorie);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());

        return ideAnnonceManager.getCount() >= 1;

    }

    // public static boolean hasStatutProvisoire(BSession session, AFAffiliation affiliation) throws Exception {
    //
    // return affiliation.getIdeStatut().equalsIgnoreCase(CodeSystem.STATUT_IDE_PROVISOIRE);
    //
    // /*
    // * Voir s'il faut activer le test suivant en cas d'erreur de saisi d'un numéro avec statut provisoire
    // *
    // * return affiliation.getIdeStatut().equalsIgnoreCase(CodeSystem.STATUT_IDE_PROVISOIRE)
    // * && !hasAnnonceEnCours(session, affiliation.getAffiliationId(), CodeSystem.TYPE_ANNONCE_IDE_MUTATION,
    // * CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
    // */
    //
    // }

    /**
     * si l'affiliation a déjà une annonce <b>d'envoi, en cours</b> autre que de type <b>(dé)enregistrement</b>
     * autre que RADIATION
     */
    public static boolean hasAnnonceUnitaireEnCoursForAffiliation(BSession session, String idAffiliation)
            throws Exception {

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForIdAffiliation(idAffiliation);
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
        List<String> list = AFIDEUtil.getListTypeAnnonceIdeSortanteATraiter();
        list.add(CodeSystem.TYPE_ANNONCE_IDE_RADIATION);
        ideAnnonceManager.setNotInType(list);

        return ideAnnonceManager.getCount() >= 1 || AFIDEUtil.hasAffiliationAnnonceLieeEnCours(session, idAffiliation);
    }

    /**
     * si l'affiliation a déjà une annonce <b>d'envoi, en cours</b>
     */
    public static boolean hasAffiliationAnnonceSortanteEnCours(BSession session, String idAffiliation) throws Exception {

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForIdAffiliation(idAffiliation);
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());

        return ideAnnonceManager.getCount() >= 1;
    }

    /**
     * 
     * @param session
     * @param idAffiliation
     * @return
     * @throws Exception
     */
    public static boolean hasAffiliationAnnonceLieeEnCours(BSession session, String idAffiliation) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idAffiliation)) {

            throw new Exception(
                    "AFIDEUtil.hasAffiliationAnnonceLieeEnCours : unable to tell if affiliation has annonce liee en cours if idAffiliation is blank or zero");

        }

        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
        ideAnnonceManager.setLikeIdAffiliationLiee(idAffiliation);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
        ideAnnonceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ideAnnonceManager.size(); i++) {
            AFIdeAnnonce ideAnnonce = (AFIdeAnnonce) ideAnnonceManager.getEntity(i);
            String[] tabIdAffiliationLiee = ideAnnonce.getIdeAnnonceListIdAffiliationLiee().split(",");
            for (int k = 0; k < tabIdAffiliationLiee.length; k++) {
                if (idAffiliation.equalsIgnoreCase(tabIdAffiliationLiee[k])) {
                    return true;
                }
            }

        }

        return false;
    }

    public static boolean isAnnonceCreationEnregistre() {
        return true;
    }

    public static TIAdresseDataSource loadAdresseForIde(BSession session, AFAffiliation affiliation) throws Exception {
        TIAdresseDataSource tiAdrssDataSource = affiliation.getTiers().getAdresseAsDataSource(
                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, ICommonConstantes.CS_APPLICATION_COTISATION,
                affiliation.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(), true, "");
        if (tiAdrssDataSource == null) {
            tiAdrssDataSource = affiliation.getTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, affiliation.getAffilieNumero(),
                    JACalendar.todayJJsMMsAAAA(), true, "");
        }
        return tiAdrssDataSource;
    }

    public static TIAdresseDataSource loadAdresseFromCascadeIde(AFAffiliation affiliation) throws PropertiesException,
            JadeApplicationServiceNotAvailableException {
        return AFBusinessServiceLocator.getCascadeAdresseIdeService().getAdresseFromCascadeIde(
                affiliation.getTiers().getPersonneMorale(), affiliation.getAffilieNumero(), affiliation.getIdTiers());
    }

    /***
     * Méthode qui retourne une adresse, soit via la cascade normale ou via les cascade IDE si les deux sont définies
     * dans les propriétés.
     * 
     * @param session
     * @param affiliation
     * @return
     * @throws PropertiesException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws Exception
     */
    public static TIAdresseDataSource loadAdresseForIdeOrFromCascadeIde(BSession session, AFAffiliation affiliation)
            throws Exception {

        TIAdresseDataSource adresseDataSource;

        // Si une des deux propriétés est vide on utilise l'ancienne méthode pour retrouver l'adresse
        if (AFProperties.IDE_CASCADE_ADRESSE_PHYSIQUE.getValue().isEmpty()
                || AFProperties.IDE_CASCADE_ADRESSE_MORALE.getValue().isEmpty()) {
            adresseDataSource = loadAdresseForIde(session, affiliation);
        } else {
            adresseDataSource = loadAdresseFromCascadeIde(affiliation);
        }

        return adresseDataSource;
    }

    /***
     * Permet de savoir si l'affilaition à une adresse.
     * Si les propriétés cascade IDE sont renseignés on cherche une adresse via les cascades
     * 
     * @param session
     * @param affiliation
     * @return
     * @throws Exception
     */
    public static boolean hasTiersAdresseForIdeOrFromCascade(BSession session, AFAffiliation affiliation)
            throws Exception {
        TIAdresseDataSource adresseDataSource = loadAdresseFromCascadeIde(affiliation);

        // Si pas d'id_adresse ça veut dire pas d'adresse
        return !adresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_ID_ADRESSE).isEmpty();
    }

    public static boolean hasDroitEnregistrementActif(AFAffiliation affiliation) throws PropertiesException {

        boolean hasDroitEnregistrementActif = !affiliation.isTraitement();

        hasDroitEnregistrementActif = hasDroitEnregistrementActif
                && AFIDEUtil.getListGenreAffilieActif().contains(affiliation.getTypeAffiliation());

        return hasDroitEnregistrementActif;

    }

    public static boolean hasDroitEnregistrementPassif(AFAffiliation affiliation) {

        boolean hasDroitEnregistrementPassif = !affiliation.isTraitement();

        hasDroitEnregistrementPassif = hasDroitEnregistrementPassif
                && !JadeStringUtil.isEmpty(affiliation.getNumeroIDE());

        // PASSIF pas uniquement sur
        // hasDroitEnregistrementPassif = hasDroitEnregistrementPassif
        // && AFIDEUtil.getListGenreAffilieNonPermis().contains(affiliation.getTypeAffiliation());

        return hasDroitEnregistrementPassif;

    }

    public static TITiersViewBean loadTiers(BSession session, String idTiers) throws Exception {

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);

        tiers.retrieve();

        return tiers;

    }

    public static boolean isRegisterDeregisterStatusError(RegisterDeregisterStatus registerDeregisterStatus) {

        boolean isErrorStatus = false;

        isErrorStatus = isErrorStatus || RegisterDeregisterStatus.DELETION_REQUIRED.equals(registerDeregisterStatus);
        isErrorStatus = isErrorStatus
                || RegisterDeregisterStatus.REACTIVATION_REQUIRED.equals(registerDeregisterStatus);
        isErrorStatus = isErrorStatus
                || RegisterDeregisterStatus.REGISTRATION_NOT_ALLOWED.equals(registerDeregisterStatus);
        isErrorStatus = isErrorStatus || RegisterDeregisterStatus.SYSTEM_ERROR.equals(registerDeregisterStatus);
        isErrorStatus = isErrorStatus || RegisterDeregisterStatus.UID_NOT_FOUND.equals(registerDeregisterStatus);

        return isErrorStatus;
    }

    public static String translateRegisterDeregisterStatus(BSession session,
            RegisterDeregisterStatus registerDeregisterStatus) {

        if (RegisterDeregisterStatus.DELETION_REQUIRED.equals(registerDeregisterStatus)) {
            return session.getLabel("NAOS_IDE_TRANSLATE_REGISTER_DEREGISTER_STATUS_DELETION_REQUIRED");
        }

        if (RegisterDeregisterStatus.REACTIVATION_REQUIRED.equals(registerDeregisterStatus)) {
            return session.getLabel("NAOS_IDE_TRANSLATE_REGISTER_DEREGISTER_STATUS_REACTIVATION_REQUIRED");
        }

        if (RegisterDeregisterStatus.REGISTRATION_NOT_ALLOWED.equals(registerDeregisterStatus)) {
            return session.getLabel("NAOS_IDE_TRANSLATE_REGISTER_DEREGISTER_STATUS_REGISTRATION_NOT_ALLOWED");
        }

        if (RegisterDeregisterStatus.SYSTEM_ERROR.equals(registerDeregisterStatus)) {
            return session.getLabel("NAOS_IDE_TRANSLATE_REGISTER_DEREGISTER_STATUS_SYSTEM_ERROR");
        }

        if (RegisterDeregisterStatus.UID_NOT_FOUND.equals(registerDeregisterStatus)) {
            return session.getLabel("NAOS_IDE_TRANSLATE_REGISTER_DEREGISTER_STATUS_UID_NOT_FOUND");
        }

        return "";

    }

    public static String translateCodeStatut(Integer codeStatut) {
        String codeSystemPourLibelle = "";
        switch (codeStatut) {
            case 1:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_PROVISOIRE;
                break;
            case 2:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_REACTIVATION;
                break;
            case 3:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_DEFINITIF;
                break;
            case 4:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_MUTATION;
                break;
            case 5:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_RADIE;
                break;
            case 6:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_DEFINITIF_RADIE;
                break;
            case 7:
                codeSystemPourLibelle = CodeSystem.STATUT_IDE_ANNULE;
                break;
            default:
                break;
        }
        return codeSystemPourLibelle;
    }

    public static String translateInfoAboMessageTypeInTypeAnnonceIde(String infoAboMessageType) {
        if ("1".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_CREATION_INFO_ABO;
        } else if ("2".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_MUTATION_INFO_ABO;
        } else if ("3".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_MUTATION_REJETEE;
        } else if ("4".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_MUTATION_CONFIRMEE;
        } else if ("5".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_ANNULEE;
        } else if ("6".equalsIgnoreCase(infoAboMessageType)) {
            return CodeSystem.TYPE_ANNONCE_IDE_CREATION_CONFIRMEE;
        } else {
            return "";
        }

    }

    /**
     * Mapping personnalité juridique -> LegalForm
     * 
     * @param persJuri
     * @return LegalForm Code
     */
    public static String translatePersJuriVersLegalForm(String persJuri) {
        int value = 0;
        try {
            value = Integer.valueOf(persJuri);
        } catch (Exception e) {
            JadeLogger
                    .error(AFIDEUtil.class,
                            "Mapping [AFIDEUtil.translatePersJuriVersLegalForm] Impossible de récupérer la valeur numéraire de la personalité juridique "
                                    + persJuri + " : " + e.getMessage());
        }
        switch (value) {
            case 806001:// Raison individuelle
                return "0101";// Entreprise individuelle

            case 806002:// Société en nom collectif
                return "0103";// Société en nom collectif

            case 806003:// Société en commandite simple
                return "0104";// Société en commandite

            case 806004:// Société anonyme
                return "0106";// Société par actions

            case 806005:// Société à responsabilité limitée
                return "0107";// Société à responsabilité limitée GMBH / SARL

            case 806006:// Société coopérative
                return "0108";// Société coopérative

            case 806008:// Société en commandite par actions
                return "0105";// Société en commandite par actions

            case 806009:// Association
                return "0109";// Association (les églises reconnues par l’Etat apparaissent sous cette forme)

            case 806012:// Corporation de droit public
                return "0234";// Corporation de droit public (entreprise). Il s’agit notamment de toutes les entreprises
                              // de droit public, qui ne peuvent être répertoriées sous les points Entreprises de la
                              // Confédération, du canton, du district ou de la commune, ex. les entreprises forestières
                              // des communes bourgeoises.

            case 806013:// Fondation
                return "0110";// Fondation

            case 806007:// Société simple
            case 806014:// Hoirie
                return "0302";// Société simple

            case 806019:// Indéfini
                return "0118";// Procurations non-commerciales
            case 806018:// NA
                return "";
            default:
                return "";
        }

    }

    /**
     * Mapping type entreprise (selon la personnalité juridique)
     */
    public static String translateTypeEntrepriseVersOrganisationType(String persJuridique) {
        int value = 0;
        try {
            value = Integer.valueOf(persJuridique);
        } catch (Exception e) {
            // can't have int value from persisted persJuri
        }
        switch (value) {
            case 806001:// Raison individuelle
                return "7"; // Einzelfirma

            case 806002:// Société en nom collectif
            case 806003:// Société en commandite simple
            case 806004:// Société anonyme
            case 806005:// Société à responsabilité limitée
            case 806006:// Société coopérative
            case 806008:// Société en commandite par actions
                return "1";// Juristische Person

            case 806007:// Société simple
            case 806014:// Hoirie
            case 806019:// Indéfini
            case 806018:// NA
                return "6"; // Einfache Gesellschaft

            case 806009:// Association
                return "10"; // Verein

            case 806012:// Corporation de droit public
                return "8"; // Öffentlich-rechtliches Unternehmen

            case 806013:// Fondation
                return "11";// Stiftung

            default:
                return "6"; // Einfache Gesellschaft
        }
    }

    public static String translateMotifFinGlobazVersIDE(String motifFin) {
        // 1 = Fusion avec absorption (eCH-0098: Fusion)
        // 2 = Fusion avec combinaison (eCH-0098: Fusion)
        // 3 = Cessation d’activité/ scission / retraite/ salarié (eCH-0098: Liquidation)
        // 4 = Pas d’autorisation (eCH-0098: Liquidation)
        // 5 = Transmission d’une entreprise / changement de société (eCH-0098: reprise)
        // 6 = ARGE terminé/ projet terminé / fête terminée / activité en Suisse terminée (eCH-
        // 0098: Liquidation)
        // 7 = Décès (eCH-0098: Liquidation)
        // 8 = Doublon / erreur (eCH-0098: Liquidation))

        if (CodeSystem.MOTIF_FIN_SORTIE_FUSION.equalsIgnoreCase(motifFin)) {
            return "1";
            // } else if(){
            // return "2";
            // } else if(){
            // return "4";
        } else if (CodeSystem.MOTIF_FIN_REORGANISATION.equalsIgnoreCase(motifFin)) {
            return "5";
            // } else
            // if(CodeSystem.MOTIF_FIN_FIN_ADHESION.equalsIgnoreCase(motifFin)||CodeSystem.MOTIF_FIN_FIN_ADHESION.equalsIgnoreCase(motifFin)){
            // Code 6 inconnu point de vue métier dans webavs
            // return "6";
        } else if (CodeSystem.MOTIF_FIN_DECES.equalsIgnoreCase(motifFin)) {
            return "7";
            // } else if(){
            // return "8";
        } else {
            // par défaut
            return "3";
        }
    }

    public static String formatNumIDE(String numeroIde) {

        String unformattedSource = (numeroIde.indexOf('.') < 0) ? numeroIde : unformatNumIDE(numeroIde);
        return JAStringFormatter.format(unformattedSource, IDE_FORMAT);
    }

    /**
     * Permet de créer un enregistrement d'annonce IDE de type mutation
     */
    public static void generateAnnonceMutationIde(BSession session, AFAffiliation affiliation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_MUTATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnonceMutationMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        affiliation);
                createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_MUTATION, affiliation);
            }
        } else {
            // System.out.println("******************* MultiAff don't delete anything");
        }
    }

    /**
     * Permet de créer un enregistrement d'annonce IDE de type radiation
     */
    public static void generateAnnonceRadiationIde(BSession session, AFAffiliation affiliation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnonceRadiationMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF,
                        affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation)) {
                    if (!CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(affiliation.getIdeStatut())) {
                        createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION, affiliation);
                    }
                }
            }
        }
    }

    /**
     * Permet de créer un enregistrement pour réactiver une annonce IDE
     */
    public static void generateAnnonceReactivationIde(BSession session, AFAffiliation affiliation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnonceReactivateMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION, affiliation)) {
                    createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation);
                }
            }
        }
    }

    /**
     * Permet de créer un enregistrement d'annonce de <b>désenregistrement ACTIF</b> IDE
     * 
     * @param cotisation si l'annonce provient d'une coti, maintenir le lien, sinon peut-être <b>null</b>
     * @return l'annonce créée, pour le cas ou le numéro IDE devrait être persisté dans le cas de la suppression du num
     *         ide dans l'aff.
     */
    public static AFIdeAnnonce generateAnnonceDesenregistrementActif(BSession session, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session,
                CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI,
                affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())
                    || AFIDEUtil.hasAnnonceEnCours(session, affiliation.getAffiliationId(),
                            CodeSystem.TYPE_ANNONCE_IDE_CREATION, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)
                    || AFIDEUtil.hasAffiliationAnnonceLieeEnCours(session, affiliation.getAffiliationId())) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF,
                        affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_MUTATION, affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF,
                        affiliation)) {
                    return createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF, affiliation,
                            cotisation);
                }
            }
        }
        return null;
    }

    /**
     * Permet de créer un enregistrement d'annonce de <b>désenregistrement PASSIF</b> IDE
     */
    public static void generateAnnonceDesenregistrementPassif(BSession session, AFAffiliation affiliation)
            throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session,
                CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI,
                affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnoncePassiveCreationMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        affiliation)) {
                    createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF, affiliation);
                }
            }
        }
    }

    /**
     * Permet de créer un enregistrement d'annonce d'<b>enregistrement ACTIF</b> IDE </br>
     * 
     * @param cotisation si l'annonce provient d'une coti, maintenir le lien, sinon peut-être <b>null</b>
     */
    public static void generateAnnonceEnregistrementActif(BSession session, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session,
                CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI,
                affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnonceActiveMandatory(session, affiliation)
                    && !hasAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_MUTATION,
                            CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getNumeroIDE())) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        affiliation);
                boolean flagCreateAnnonce = true;
                flagCreateAnnonce = flagCreateAnnonce
                        && !deleteTypeAnnonceEnCoursMultiAff(session,
                                CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF, affiliation);
                flagCreateAnnonce = !deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                        affiliation) && flagCreateAnnonce;
                if (flagCreateAnnonce) {
                    createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation, cotisation);
                }
            }
        }
    }

    /**
     * Permet de créer un enregistrement d'annonce passive IDE
     */
    public static void generateAnnonceEnregistrementPassif(BSession session, AFAffiliation affiliation)
            throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session,
                CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI,
                affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnoncePassiveCreationMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_MUTATION, affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF,
                        affiliation)) {
                    createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF, affiliation);
                }
            }
        }
    }

    /**
     * Appel la création d'annonce : {@link #createNewAnnonce(BSession, String, AFAffiliation)} </br>
     * avec le type CREATION et retournant le message à afficher dans l'écran
     * 
     * @param session
     * @param affiliation
     * @return message traduit à afficher dans l'écran
     * @throws Exception
     */
    public static String generateAnnonceCreationIde(BSession session, AFAffiliation affiliation) throws Exception {

        String erreurs = "";
        if (hasAffiliationAnnonceSortanteEnCours(session, affiliation.getAffiliationId())
                || hasAffiliationAnnonceLieeEnCours(session, affiliation.getAffiliationId())) {
            return session.getLabel("NAOS_ANNONCE_IDE_CREATION_ECHEC")
                    + " "
                    + session
                            .getLabel("NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_EXISTE_DEJA_ANNONCE_CREATION_EN_COURS");
        }

        erreurs = AFIDEUtil.checkAnnonceCreationMandatory(session, affiliation.getAffiliationId());

        if (JadeStringUtil.isBlankOrZero(erreurs)) {
            deleteTypeAnnonceEnCours(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF, affiliation);

            if (JadeStringUtil.isBlankOrZero(affiliation.getIdAnnonceIdeCreationLiee())) {
                createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_CREATION, affiliation);
            } else {
                deleteTypeAnnonceEnCours(session, CodeSystem.TYPE_ANNONCE_IDE_CREATION, affiliation);
                linkAffiliationToAnnonceCreationExistante(session, affiliation);
            }

            return "";
        }

        return session.getLabel("NAOS_ANNONCE_IDE_CREATION_ECHEC") + " " + erreurs;

    }

    private static void linkAffiliationToAnnonceCreationExistante(BSession session, AFAffiliation affiliation)
            throws Exception {

        AFIdeAnnonce ideAnnonce = new AFIdeAnnonce();
        ideAnnonce.setSession(session);
        ideAnnonce.setIdeAnnonceIdAnnonce(affiliation.getIdAnnonceIdeCreationLiee());
        ideAnnonce.retrieve();

        String listIdAffiliationLiee = ideAnnonce.getIdeAnnonceListIdAffiliationLiee();
        String listNumeroAffilieLiee = ideAnnonce.getIdeAnnonceListNumeroAffilieLiee();

        if (!JadeStringUtil.isBlankOrZero(listIdAffiliationLiee)) {
            listIdAffiliationLiee = listIdAffiliationLiee + ",";
        }
        listIdAffiliationLiee = listIdAffiliationLiee + affiliation.getAffiliationId();

        if (!JadeStringUtil.isBlankOrZero(listNumeroAffilieLiee)) {
            listNumeroAffilieLiee = listNumeroAffilieLiee + ",";
        }
        listNumeroAffilieLiee = listNumeroAffilieLiee + affiliation.getAffilieNumero();

        ideAnnonce.setIdeAnnonceListIdAffiliationLiee(listIdAffiliationLiee);
        ideAnnonce.setIdeAnnonceListNumeroAffilieLiee(listNumeroAffilieLiee);

        ideAnnonce.update();

    }

    /**
     * créer une nouvelle annonce de catégorie <b>ENVOI</b> et d'état <b>ENREGISTRE</b>
     * Supprime par la même occasion une annonce similaire qui ne serait pas encore traitée
     * 
     * @param session
     * @param typeAnnonce code du type d'annonce a créer (CodeSystem.TYPE_ANNONCE_IDE_*)
     * @param affiliation Objet AFAffiliation pour lier l'annonce à son id
     * @throws Exception à l'enregistrement en DB (ancienne persistance)
     */
    private static void createNewAnnonce(BSession session, String typeAnnonce, AFAffiliation affiliation)
            throws Exception {

        createNewAnnonce(session, typeAnnonce, affiliation, null);
    }

    private static AFIdeAnnonce createNewAnnonce(BSession session, String typeAnnonce, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (AFProperties.ANNONCE_GENERER.getBooleanValue()) {

            deleteTypeAnnonceEnCours(session, typeAnnonce, affiliation);

            // Création annonce
            AFIdeAnnonce annonce = new AFIdeAnnonce();
            annonce.setSession(session);
            annonce.setIdeAnnonceIdAffiliation(affiliation.getAffiliationId());
            if (cotisation != null) {
                annonce.setIdeAnnonceIdCotisation(cotisation.getCotisationId());
            }
            annonce.setIdeAnnonceCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);
            annonce.setIdeAnnonceDateCreation(JACalendar.todayJJsMMsAAAA());
            annonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
            annonce.setIdeAnnonceType(typeAnnonce);

            if (AFIDEUtil.isAnnonceRespectedMultiAffMandatory(session, annonce, affiliation.getNumeroIDE())) {
                annonce.add();
            } else {
                return null;
            }

            return annonce;
        }
        return null;
    }

    // ***********************************************************
    // * <i>Cas particulier</i> *
    // ***********************************************************

    /**
     * <i>Cas particulier (multi-Annonces)</i>, dans le <b>transfert de caisse</b> on désire avoir les deux annonces qui
     * normalement se suppriment l'une l'autre [Enreg. Actif + Radiation]
     */
    public static void generateAnnoncesRadiation(BSession session, AFAffiliation affiliation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            if (isAffiliationRespectAnnonceRadiationMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF,
                        affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF,
                        affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation)) {
                    AFIDEUtil.generateAnnonceEnregistrementActif(session, affiliation, null);
                    createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION, affiliation);
                }
            }
        }
    }

    /**
     * <i>Cas particulier (multi-Annonces)</i>, dans le cas de <b>cessation</b>, on désire avoir les deux annonces qui
     * normalement se suppriment l'une l'autre [Désenreg. Actif + Réactivation]
     */
    public static void generateAnnoncesReactivation(BSession session, AFAffiliation affiliation) throws Exception {
        if (AFIDEUtil.isAnnonceRespectedMultiAffMandatoryMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI, affiliation.getAffiliationId(), affiliation.getNumeroIDE())) {
            AFIDEUtil.generateAnnonceDesenregistrementActif(session, affiliation, null);
            if (isAffiliationRespectAnnonceReactivateMandatory(session, affiliation)) {
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation);
                deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF,
                        affiliation);
                if (!deleteTypeAnnonceEnCoursMultiAff(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION, affiliation)) {
                    if (CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(affiliation.getIdeStatut())) {
                        createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION, affiliation);
                    }
                }
            }
        }
    }

    /**
     * <i>Cas particulier (cotisation)</i>, L'annonce de <b>Désenregistrement Actif</b> ne doit pas sortir si une
     * annonce de
     * <b>Radiation</b> existe dans les annonces en envoi pour cette affiliation
     */
    public static void generateAnnonceDesenregistrementActifFromCoti(BSession session, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (!hasAnnonceEnCours(session, affiliation.getAffiliationId(), CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)) {
            generateAnnonceDesenregistrementActif(session, affiliation, cotisation);
        }

    }

    /**
     * <i>Cas particulier (cotisation)</i>, L'annonce de d'<b>Enregistrement Actif</b> ne doit pas sortir si une annonce
     * de
     * <b>Réactivation</b> existe dans les annonces en envoi pour cette affiliation
     */
    public static void generateAnnonceEnregistrementActifFromCoti(BSession session, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (!hasAnnonceEnCours(session, affiliation.getAffiliationId(), CodeSystem.TYPE_ANNONCE_IDE_REACTIVATION,
                CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)) {
            generateAnnonceEnregistrementActif(session, affiliation, cotisation);
        }
    }

    /**
     * * <i>Cas particulier</i>
     * Permet de créer un enregistrement d'annonce IDE de type radiation <b>sans numéro IDE</b>
     */
    public static void generateAnnonceRadiationSansIde(BSession session, AFAffiliation affiliation) throws Exception {

        if (hasDroitEnregistrementActif(affiliation)) {
            if (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())) {
                AFIDEUtil.generateAnnonceEnregistrementActif(session, affiliation, null);
            }
            createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_RADIATION, affiliation);
        }
    }

    /**
     * * <i>Cas particulier</i>
     * Permet de créer un enregistrement d'annonce de <b>désenregistrement ACTIF</b> sans IDE
     */
    public static void generateAnnonceDesenregistrementActifSansIde(BSession session, AFAffiliation affiliation,
            AFCotisation cotisation) throws Exception {
        if (!deleteTypeAnnonceEnCours(session, CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF, affiliation)) {
            if (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())) {
                AFIDEUtil.generateAnnonceEnregistrementActif(session, affiliation, null);
            }
            createNewAnnonce(session, CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF, affiliation, cotisation);
        }
    }

    /**
     * Méthode qui initialise le cotisation manager pour les contrôle d'affiliations
     * 
     * @author est
     * 
     * @param session
     * @param idAffiliation
     * @return
     * @throws PropertiesException
     * @throws Exception
     */
    private static AFCotisationManager initCotiManager(BSession session, String idAffiliation)
            throws PropertiesException, Exception {
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setSession(session);
        cotisMgr.setForAffiliationId(idAffiliation);
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        cotisMgr.setForDateDifferente(Boolean.TRUE);

        // Contrôle la propriété pour vérifier les plans inactifs
        if ("true".equalsIgnoreCase(AFProperties.IDE_PLAN_AFFILIATION_VERIFICATION_INACTIF.getValue())) {
            boolean hasAtLeastOnePlanInactif = false;

            AFPlanAffiliationManager planAffiMgr = new AFPlanAffiliationManager();
            planAffiMgr.setSession(session);
            planAffiMgr.setForAffiliationId(idAffiliation);
            planAffiMgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < planAffiMgr.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffiMgr.getEntity(i);
                if (planAffiliation.getInactif()) {
                    hasAtLeastOnePlanInactif = true;
                }
            }

            cotisMgr.setShowInactif(Boolean.toString(hasAtLeastOnePlanInactif));
        }

        // Contrôle la propriété pour vérifier si on prend en compte les affiliations "parents" pour les cotisations
        if ("true".equalsIgnoreCase(AFProperties.IDE_COTISATION_VERIFICATION_TAXE_SOUS.getValue())) {
            AFLienAffiliationManager lienMgr = getLienAffiManagerForAffiliationTaxeSous(session, idAffiliation);

            // Si le manager n'est pas vide ça veut dire qu'on à une affiliation qui détient ("père") notre affiliation
            if (lienMgr.getCount() == 1) {
                String idAffiliationDetientAffilie = ((AFLienAffiliation) lienMgr.getFirstEntity())
                        .getAff_AffiliationId();

                // Si le manager ne trouve pas de cotisation pour le "fils", on recherche des cotisations pour le "père"
                if (cotisMgr.getCount() == 0) {
                    cotisMgr = initCotiManager(session, idAffiliationDetientAffilie);
                }
            }
        }
        return cotisMgr;
    }

    /**
     * Récupère un manager contenant le lien de l'affiliation pour laquelle est est de type "taxé sous"
     * 
     * @param session
     * @param idAffiliation : id de l'affilaition dont on veut récupérer le lien dont elle est "taxée sous"
     * @return AFLienAffiliationManager
     * @throws Exception
     */
    public static AFLienAffiliationManager getLienAffiManagerForAffiliationTaxeSous(BSession session,
            String idAffiliation) throws Exception {
        AFLienAffiliationManager lienMgr = new AFLienAffiliationManager();
        lienMgr.setSession(session);
        lienMgr.setForTypeLien(CodeSystem.TYPE_LIEN_TAXE_SOUS);
        lienMgr.setForAffiliationId(idAffiliation);
        lienMgr.find(BManager.SIZE_NOLIMIT);

        return lienMgr;
    }

    /**
     * Determine si ou moins une coti est ouverte pour l'affilié. </br>
     * Lance une requête pour obtenir les Coti ouverte au moment de la requête. Si au moins une, return true
     * 
     * @return true : au moins une Coti ouverte.
     */
    public static boolean hasAffiliationCotisationAVSOuverte(BSession session, String idAffiliation) throws Exception {

        if (idAffiliation == null || idAffiliation.isEmpty() || JadeStringUtil.isBlankOrZero(idAffiliation)) {
            return false;
        }

        AFCotisationManager cotisMgr = initCotiManager(session, idAffiliation);

        cotisMgr.setDateFinGreater(JACalendar.todayJJsMMsAAAA());

        return cotisMgr.getCount() >= 1;
    }

    public static boolean hasAffiliationCotisationAVSSansDateFin(BSession session, String idAffiliation)
            throws Exception {

        if (idAffiliation == null || idAffiliation.isEmpty() || JadeStringUtil.isBlankOrZero(idAffiliation)) {
            return false;
        }

        AFCotisationManager cotisMgr = initCotiManager(session, idAffiliation);

        cotisMgr.setForDateFin("0");

        return cotisMgr.getCount() >= 1;

    }

    public static boolean hasAffiliationCotisationAVSSansDateFinOtherThanThisCoti(BSession session,
            String idAffiliation, String idCotiNotMe) throws Exception {

        if (idAffiliation == null || idAffiliation.isEmpty() || JadeStringUtil.isBlankOrZero(idAffiliation)) {
            return false;
        }

        AFCotisationManager cotisMgr = initCotiManager(session, idAffiliation);

        cotisMgr.setForDateFin("0");

        if (!(idCotiNotMe == null || idCotiNotMe.isEmpty() || JadeStringUtil.isBlankOrZero(idCotiNotMe))) {
            cotisMgr.setNotForCotisationId(idCotiNotMe);
        }
        return cotisMgr.getCount() >= 1;
    }

    public static boolean hasAffiliationCotisationAVS(BSession session, String idAffiliation) throws Exception {

        if (idAffiliation == null || idAffiliation.isEmpty() || JadeStringUtil.isBlankOrZero(idAffiliation)) {
            return false;
        }

        AFCotisationManager cotisMgr = initCotiManager(session, idAffiliation);

        return cotisMgr.getCount() >= 1;

    }

    public static boolean hasDateDeFinAffililation(BSession session, String idAffiliation) throws Exception {
        return !JadeStringUtil.isBlankOrZero(AFAffiliationUtil.getAffiliation(idAffiliation, session).getDateFin());
    }

    public static boolean isCasRadieAnticipe(BSession session, AFAffiliation affiliation) throws Exception {
        return CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(affiliation.getIdeStatut())
                && BSessionUtil
                        .compareDateFirstGreater(session, affiliation.getDateFin(), JACalendar.todayJJsMMsAAAA());
    }

    /**
     * si respecte les pré-conditions pour la creation d'una annonce ACTIVE
     */
    public static boolean isAffiliationRespectAnnonceActiveMandatory(BSession session, AFAffiliation affiliation)
            throws Exception {

        // boolean isAnnonceToCreate = !hasAffiliationAnnonceSortanteEnCours(session, affiliation.getAffiliationId());

        boolean isAnnonceToCreate = hasDroitEnregistrementActif(affiliation);

        isAnnonceToCreate = isAnnonceToCreate && !JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE());
        isAnnonceToCreate = isAnnonceToCreate
                && (CodeSystem.STATUT_IDE_PROVISOIRE.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_MUTATION.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_DEFINITIF.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(affiliation.getIdeStatut()) || isCasRadieAnticipe(
                            session, affiliation));

        return isAnnonceToCreate;

    }

    /**
     * Est-ce que les conditions sont réunies pour créer une annonce de MUTATION
     */
    public static boolean isAffiliationRespectAnnonceMutationMandatory(BSession session, AFAffiliation affiliation)
            throws Exception {

        // boolean isAnnonceToCreate = !hasAffiliationAnnonceSortanteEnCours(session, affiliation.getAffiliationId());

        boolean isAnnonceToCreate = hasDroitEnregistrementActif(affiliation);

        isAnnonceToCreate = isAnnonceToCreate && !JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE());
        isAnnonceToCreate = isAnnonceToCreate
                && (CodeSystem.STATUT_IDE_PROVISOIRE.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_MUTATION.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_DEFINITIF.equalsIgnoreCase(affiliation.getIdeStatut()) || isCasRadieAnticipe(
                            session, affiliation));
        isAnnonceToCreate = isAnnonceToCreate
                && !hasAnnonceUnitaireEnCoursForAffiliation(session, affiliation.getAffiliationId());

        return isAnnonceToCreate;

    }

    /**
     * Est-ce que les conditions sont réunies pour créer une annonce de RADIATION
     */
    public static boolean isAffiliationRespectAnnonceRadiationMandatory(BSession session, AFAffiliation affiliation)
            throws Exception {

        boolean isAnnonceToCreate = hasDroitEnregistrementActif(affiliation);

        isAnnonceToCreate = isAnnonceToCreate
                && (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())
                        || AFIDEUtil.hasAnnonceEnCours(session, affiliation.getAffiliationId(),
                                CodeSystem.TYPE_ANNONCE_IDE_CREATION, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI) || AFIDEUtil
                            .hasAffiliationAnnonceLieeEnCours(session, affiliation.getAffiliationId()));

        isAnnonceToCreate = isAnnonceToCreate
                && !hasAnnonceEnCours(session, affiliation.getAffiliationId(), CodeSystem.TYPE_ANNONCE_IDE_RADIATION,
                        CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI);

        return isAnnonceToCreate;

    }

    /**
     * TODO - Tests de création d'annonce passive à implémenter
     */
    public static boolean isAffiliationRespectAnnoncePassiveCreationMandatory(BSession session,
            AFAffiliation affiliation) throws Exception {

        return hasDroitEnregistrementPassif(affiliation);

    }

    public static boolean isAffiliationRespectAnnonceReactivateMandatory(BSession session, AFAffiliation affiliation)
            throws Exception {

        boolean isAnnonceToCreate = hasDroitEnregistrementActif(affiliation);

        isAnnonceToCreate = isAnnonceToCreate && !JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE());
        isAnnonceToCreate = isAnnonceToCreate
                && (CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_DEFINITIF.equalsIgnoreCase(affiliation.getIdeStatut())
                        || CodeSystem.STATUT_IDE_MUTATION.equalsIgnoreCase(affiliation.getIdeStatut()) || CodeSystem.STATUT_IDE_PROVISOIRE
                            .equalsIgnoreCase(affiliation.getIdeStatut()));

        return isAnnonceToCreate;

    }

    /**
     * Supprimer la ou les annonces non traitées de type communiqué pour l'affiliation communiquée.
     * </br></br>
     * <i><u>Manager FIX pour faire le delete sans JOIN</u></i>
     * </br>
     * <b>Attention,</b> seuls les arguments suivant sont pris en charge par le manager pour un delete(veuillez
     * documenter si vous élargissez)</br>
     * <ul>
     * <li>.setForIdAffiliation();</li>
     * <li>.setForType();</li>
     * <li>.setInEtat();</li>
     * </ul>
     * 
     * @return true si au moins une annonce a été supprimée
     * 
     */
    public static boolean deleteTypeAnnonceEnCours(BSession session, String typeAnnonce, AFAffiliation affiliation)
            throws Exception {
        AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
        ideAnnonceManager.setSession(session);
        ideAnnonceManager.setForIdAffiliation(affiliation.getAffiliationId());
        ideAnnonceManager.setForType(typeAnnonce);
        ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
        boolean returnValue = ideAnnonceManager.getCount() >= 1;
        ideAnnonceManager.delete();
        return returnValue;

    }

    /**
     * Supprimer la ou les annonces non traitées de type communiqué pour <b>toutes les affiliations qui partagent ce
     * numéro IDE.</b>
     * </br></br>
     * <i><u>Manager FIX pour faire le delete sans JOIN</u></i>
     * </br>
     * <b>Attention,</b> seuls les arguments suivant sont pris en charge par le manager pour un delete(veuillez
     * documenter si vous élargissez)</br>
     * <ul>
     * <li>.setForNumeroIde();</li>
     * <li>.setForType();</li>
     * <li>.setInEtat();</li>
     * </ul>
     * 
     * @return true si au moins une annonce a été supprimée. false si l'affiliation n'a pas de numéro ide
     * 
     */
    public static boolean deleteTypeAnnonceEnCoursMultiAff(BSession session, String typeAnnonce,
            AFAffiliation affiliation) throws Exception {
        if (affiliation != null && !JadeStringUtil.isBlankOrZero(affiliation.getNumeroIDE())) {

            AFIdeAnnonceManager ideAnnonceManager = new AFIdeAnnonceManager();
            ideAnnonceManager.setSession(session);
            ideAnnonceManager.setForNumeroIde(affiliation.getNumeroIDE());
            ideAnnonceManager.setForType(typeAnnonce);
            ideAnnonceManager.setInEtat(AFIDEUtil.getListEtatAnnonceIdeEnCours());
            boolean returnValue = ideAnnonceManager.getCount() >= 1;
            ideAnnonceManager.delete();
            // gère le cas particulier des annonces désenreg actif quand on a supprimé le num ide dans l'annonce
            if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equals(typeAnnonce)) {
                ideAnnonceManager.setForDesenregActif(affiliation.getNumeroIDE());
                returnValue = returnValue || ideAnnonceManager.getCount() >= 1;
                ideAnnonceManager.delete();
            }
            return returnValue;
        }
        return false;
    }

    public static String checkEnvoiAnnonceCreationMandatory(BSession session, String idAffiliation) throws Exception {

        StringBuilder errorBuffer = new StringBuilder();

        if (!hasAffiliationCotisationAVS(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS"));
        }

        errorBuffer.append(checkAnnonceCreationMandatory(session, idAffiliation));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceRadiationMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        if (!hasAffiliationCotisationAVS(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS"));
        }

        errorBuffer.append(checkNumeroIdeMandatory(session, ideAnnonce.getNumeroIde(), errorBuffer));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceReactivationMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        if (!hasAffiliationCotisationAVSOuverte(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS_OUVERTE"));
        }

        errorBuffer.append(checkNumeroIdeMandatory(session, ideAnnonce.getNumeroIde(), errorBuffer));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceEnregistrementActifMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        if (!hasAffiliationCotisationAVSOuverte(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS_OUVERTE"));
        }

        errorBuffer.append(checkNumeroIdeMandatory(session, ideAnnonce.getNumeroIde(), errorBuffer));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceEnregistrementPassifMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        errorBuffer.append(checkNumeroIdeMandatory(session, ideAnnonce.getNumeroIde(), errorBuffer));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceDesenregistrementActifMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        if (!hasAffiliationCotisationAVS(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS"));
        }

        String numeroIde = ideAnnonce.getNumeroIde();
        if (JadeStringUtil.isBlankOrZero(numeroIde)) {
            numeroIde = ideAnnonce.getNumeroIdeRemplacement();
        }

        errorBuffer.append(checkNumeroIdeMandatory(session, numeroIde, errorBuffer));

        return errorBuffer.toString();
    }

    public static String checkEnvoiAnnonceDesenregistrementPassifMandatory(BSession session, AFIdeAnnonce ideAnnonce,
            String idAffiliation) throws Exception {

        StringBuffer errorBuffer = new StringBuffer();

        errorBuffer.append(checkNumeroIdeMandatory(session, ideAnnonce.getNumeroIde(), errorBuffer));

        return errorBuffer.toString();
    }

    private static String checkNumeroIdeMandatory(BSession session, String numeroIDe, StringBuffer errorBuffer) {

        String messageErreur = "";
        if (JadeStringUtil.isBlankOrZero(numeroIDe)) {
            if (errorBuffer.length() > 0) {
                messageErreur = " / ";
            }
            messageErreur = messageErreur + session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_NUMERO_IDE_NON_VALIDE");

        }

        return messageErreur;

    }

    public static String checkEnvoiAnnonceMutationMandatory(BSession session, String idAffiliation) throws Exception {

        StringBuilder errorBuffer = new StringBuilder();

        if (!hasAffiliationCotisationAVSOuverte(session, idAffiliation)) {
            errorBuffer.append(session.getLabel("NAOS_ANNONCE_IDE_MANDATORY_ERREUR_PAS_COTISATION_AVS_OUVERTE"));
        }

        errorBuffer.append(checkAnnonceMutationMandatory(session, idAffiliation));

        return errorBuffer.toString();
    }

    /**
     * Décoche la case a cocher IDE Annonce Passive pour l'affiliation
     * 
     * @return true si le l'annonce passive é
     */
    public static boolean forceUncheckIDEAnnoncePassive(AFAffiliation currentAff) throws Exception {
        if (currentAff.isIdeAnnoncePassive()) {
            currentAff.setIdeAnnoncePassive(false);
            currentAff.setRulesByPass(true);

            boolean wantGenerationSuiviLAALPP = currentAff.isWantGenerationSuiviLAALPP();
            boolean wantCallExternalServices = currentAff.isCallExternalServices();
            boolean wantCallMethodAfter = currentAff.isCallMethodAfter();
            boolean wantCallMethodBefore = currentAff.isCallMethodBefore();
            boolean wantCallValidate = currentAff.isCallValidate();

            AFAffiliationUtil.disableExtraProcessingForAffiliation(currentAff);
            currentAff.update();

            currentAff.setWantGenerationSuiviLAALPP(wantGenerationSuiviLAALPP);
            currentAff.wantCallExternalServices(wantCallExternalServices);
            currentAff.wantCallMethodAfter(wantCallMethodAfter);
            currentAff.wantCallMethodBefore(wantCallMethodBefore);
            currentAff.wantCallValidate(wantCallValidate);

            return true;
        }
        return false;
    }

    public static List<String> getListEtatAnnonceIdeATraiter() {

        List<String> listEtatAnnonceIdeSortanteATraiter = new ArrayList<String>();

        listEtatAnnonceIdeSortanteATraiter.add(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
        listEtatAnnonceIdeSortanteATraiter.add(CodeSystem.ETAT_ANNONCE_IDE_ATTENTE);
        listEtatAnnonceIdeSortanteATraiter.add(CodeSystem.ETAT_ANNONCE_IDE_ERREUR);

        return listEtatAnnonceIdeSortanteATraiter;

    }

    public static List<String> getListEtatIDEAffiliationNonAutorise() {

        List<String> listEtatIDEAffiliationNonAutorise = new ArrayList<String>();

        listEtatIDEAffiliationNonAutorise.add(CodeSystem.STATUT_IDE_MUTATION);

        return listEtatIDEAffiliationNonAutorise;

    }

    public static List<String> getListTypeAnnonceIdeSortanteAIgnorer() {

        List<String> listTypeAnnonceIdeSortanteAIgnorer = new ArrayList<String>();

        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF);
        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF);

        return listTypeAnnonceIdeSortanteAIgnorer;

    }

    public static List<String> getListTypeAnnonceIdeSortanteEnregistrement() {

        List<String> listTypeAnnonceIdeSortanteAIgnorer = new ArrayList<String>();

        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF);

        return listTypeAnnonceIdeSortanteAIgnorer;

    }

    public static List<String> getListTypeAnnonceIdeSortanteDesenregistrement() {

        List<String> listTypeAnnonceIdeSortanteAIgnorer = new ArrayList<String>();

        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteAIgnorer.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF);

        return listTypeAnnonceIdeSortanteAIgnorer;

    }

    public static List<String> getListTypeAnnonceIdeSortanteATraiter() {

        List<String> listTypeAnnonceIdeSortanteATraiter = new ArrayList<String>();

        listTypeAnnonceIdeSortanteATraiter.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteATraiter.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF);
        listTypeAnnonceIdeSortanteATraiter.add(CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF);
        listTypeAnnonceIdeSortanteATraiter.add(CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF);

        return listTypeAnnonceIdeSortanteATraiter;

    }

    /**
     * Genre d'affilié pouvant être actif (et passif)
     * 
     * @return
     * @throws PropertiesException
     */
    public static List<String> getListGenreAffilieActif() throws PropertiesException {

        List<String> listGenreAffilieActif = new ArrayList<String>(Arrays.asList(AFProperties.GENRE_AFFILIE_ACTIF
                .getValue().split(",")));

        return listGenreAffilieActif;

    }

    /**
     * genre d'affilié ne pouvant être que passif
     * 
     * @return
     * @throws PropertiesException
     */
    public static List<String> getListGenreAffiliePassif() throws PropertiesException {

        List<String> listGenreAffilieNonPermis = new ArrayList<String>(Arrays.asList(AFProperties.GENRE_AFFILIE_PASSIF
                .getValue().split(",")));

        return listGenreAffilieNonPermis;

    }

    /**
     * genre d'affilié ne pouvant être que passif
     * 
     * @return
     * @throws PropertiesException
     */
    public static List<String> getListMotifsFinCessation() throws PropertiesException {

        List<String> listMotifsFinCessation = new ArrayList<String>(Arrays.asList(AFProperties.MOTIFS_FIN_CESSATION
                .getValue().split(",")));

        return listMotifsFinCessation;

    }

    public static List<String> getListEtatAnnonceIdeEnCours() {

        List<String> listEtatAnnonceIdeEnCours = new ArrayList<String>();

        listEtatAnnonceIdeEnCours.add(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
        listEtatAnnonceIdeEnCours.add(CodeSystem.ETAT_ANNONCE_IDE_ERREUR);
        listEtatAnnonceIdeEnCours.add(CodeSystem.ETAT_ANNONCE_IDE_ATTENTE);
        listEtatAnnonceIdeEnCours.add(CodeSystem.ETAT_ANNONCE_IDE_SUSPENDU);

        return listEtatAnnonceIdeEnCours;

    }

    public static List<String> getListEtatAnnonceIdeModifiable() {

        List<String> listEtatAnnonceIdeModifiable = new ArrayList<String>();

        listEtatAnnonceIdeModifiable.add(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
        listEtatAnnonceIdeModifiable.add(CodeSystem.ETAT_ANNONCE_IDE_SUSPENDU);

        return listEtatAnnonceIdeModifiable;

    }

    /**
     * Construit sur base de la propertie et de la langue de la session un lien (http url) vers le registre pour le
     * numero IDE fourni
     * 
     * @param numeroIDE
     * @return
     */
    public static String giveMeLienRegistreIde(BSession session, String numeroIDE) {
        String langueISO = "&lang=";
        try {
            AFApplication appAf = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);

            return appAf.getLienRegistreIde() + giveMeNumIdeUnformatedWithPrefix(numeroIDE) + langueISO
                    + session.getIdLangueISO();
        } catch (Exception e) {
            return "";
        }

    }

    // **********************************************
    // * Numero IDE (formnatage, info...)
    // **********************************************
    public static String unformatNumIDE(String numeroIde) {
        if (numeroIde.length() == 16) {
            return JAStringFormatter.unFormat(numeroIde, IDE_FORMAT);
        } else {
            String unformattedSource = numeroIde.replace(".", "");
            return unformattedSource.replace("-", "");
        }
    }

    public static String giveMeNumIdeFormatedWithPrefix(String numIde) {

        return formatNumIde(numIde, true);
    }

    public static String giveMeNumIdeFormatedWithoutPrefix(String numIde) {

        return formatNumIde(numIde, false);
    }

    private static String formatNumIde(String numIde, boolean wantPrefix) {

        if (!JadeStringUtil.isBlankOrZero(numIde)) {

            String prefixe = "";
            if (wantPrefix) {
                prefixe = IDE_FORMATED_PREFIXE;
            }

            String chiffreIde = numIde.replaceAll("[^\\d]", "");

            if (chiffreIde.length() == 9) {
                numIde = prefixe + chiffreIde.substring(0, 3) + "." + chiffreIde.substring(3, 6) + "."
                        + chiffreIde.substring(6, 9);
            }

        }

        return numIde;

    }

    public static String giveMeNumIdeUnformatedWithoutPrefix(String numIde) {

        return unformatNumIde(numIde, false);
    }

    public static boolean isUnformatedNumIdeWithoutPrefixValid(String numIde) {

        if (!JadeStringUtil.isBlankOrZero(numIde)) {
            return numIde.matches("[0-9]{9}");
        }

        return false;

    }

    public static String giveMeNumIdeUnformatedWithPrefix(String numIde) {

        return unformatNumIde(numIde, true);
    }

    private static String unformatNumIde(String numIde, boolean wantPrefix) {

        if (!JadeStringUtil.isBlankOrZero(numIde)) {

            String prefixe = "";

            if (wantPrefix) {
                prefixe = IDE_PREFIXE;
            }

            String chiffreIde = numIde.replaceAll("[^\\d]", "");

            if (chiffreIde.length() == 9) {
                numIde = prefixe + chiffreIde;
            }

        }

        return numIde;
    }

    public static String unformatNumIDESansCHE(String numeroIdeSansCHE) {

        if (numeroIdeSansCHE.length() == 12) {
            return JAStringFormatter.unFormat(numeroIdeSansCHE, IDE_FORMAT_SANS_CHE);
        } else {
            return numeroIdeSansCHE.replace(".", "");
        }

    }

    public static String formatNumIDESansCHE(String numeroIdeSansCHE) {

        String unformattedSource = (numeroIdeSansCHE.indexOf('.') < 0) ? numeroIdeSansCHE
                : unformatNumIDE(numeroIdeSansCHE);
        return JAStringFormatter.format(unformattedSource, IDE_FORMAT_SANS_CHE);
    }

    /***
     * Méthode qui permet de récupérer l'id d'un code noga
     * 
     * @param ideDataBean
     * @throws Exception
     */
    private static String getidCodePourCodeNoga(String codeNoga, BSession bsession) throws Exception {
        FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

        param.setSession(bsession);
        param.setForCodeUtilisateur(codeNoga);

        param.find(BManager.SIZE_NOLIMIT);

        if (param.size() == 0) {
            return "0";
        } else {
            return ((FWParametersSystemCode) param.getFirstEntity()).getIdCode();
        }
    }

    /***
     * Méthode qui permet de récupérer l'id d'une la catégorie d'un csCodeNoga
     * 
     * @param ideDataBean
     * @throws Exception
     */
    private static String getidCategoriePourCodeNoga(String codeNoga, BSession bsession) throws Exception {
        FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

        param.setSession(bsession);
        param.setForCodeUtilisateur(codeNoga);

        param.find(BManager.SIZE_NOLIMIT);

        if (param.size() == 0) {
            return "0";
        } else {
            return ((FWParametersSystemCode) param.getFirstEntity()).getIdSelection();
        }
    }

    public static String getIde(String like, BSession bsession) {

        String numIdeUnformatedWithoutPrefix = AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(like);
        boolean isValidNumIdeWithoutPrefix = AFIDEUtil
                .isUnformatedNumIdeWithoutPrefixValid(numIdeUnformatedWithoutPrefix);

        StringBuilder options = new StringBuilder();

        try {

            List<IDEDataBean> rechercheIDE = new ArrayList<IDEDataBean>();
            if (isValidNumIdeWithoutPrefix) {
                rechercheIDE = IDEServiceCallUtil.searchForNumeroIDE(numIdeUnformatedWithoutPrefix, bsession);
            }

            if (rechercheIDE.size() == 1) {

                IDEDataBean aIdeData = rechercheIDE.get(0);

                String codeSystemStatutIde = aIdeData.getStatut();

                String csCodeNoga = "0";
                String csCategorieNoga = "0";

                if (!AFProperties.NOGA_SYNCHRO_REGISTRE.getValue().isEmpty()
                        && AFProperties.NOGA_SYNCHRO_REGISTRE.getValue() != null
                        && AFProperties.NOGA_SYNCHRO_REGISTRE.getBooleanValue()) {
                    csCodeNoga = getidCodePourCodeNoga(aIdeData.getNogaCode(), bsession);
                    csCategorieNoga = getidCategoriePourCodeNoga(aIdeData.getNogaCode(), bsession);
                }

                options.append("<option numeroIdeFormatedWithoutPrefix=\"");
                options.append(AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(aIdeData.getNumeroIDE()));
                options.append("\" numeroIdeUnformatedWithPrefix= \"");
                options.append(AFIDEUtil.giveMeNumIdeUnformatedWithPrefix(aIdeData.getNumeroIDE()));
                options.append("\" raisonSociale=\"");
                options.append(aIdeData.getRaisonSociale());
                options.append("\" ideStatut=\"");
                options.append(codeSystemStatutIde);
                options.append("\"");
                options.append("\" libelleStatutIde=\"");
                options.append(CodeSystem.getLibelle(bsession, codeSystemStatutIde));
                options.append("\" categorieNoga=\"");
                options.append(csCategorieNoga);
                options.append("\" csCodeNoga=\"");
                options.append(csCodeNoga);
                options.append("\"");
                options.append(">");
                options.append(AFIDEUtil.giveMeNumIdeFormatedWithoutPrefix(aIdeData.getNumeroIDE()));
                options.append("</option>");

            } else {
                options.append("<option numeroIdeFormatedWithoutPrefix= \"");
                options.append(like);
                options.append("\" raisonSociale=\"\" ");
                options.append(" numeroIdeUnformatedWithPrefix=\"\" ");
                options.append(" libelleStatutIde=\"\" ");
                options.append(" ideStatut=\"\" ");
                options.append(">");
                options.append("</option>");
                return options.toString();

            }

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return options.toString();
    }

    public static String getIde(String like, HttpSession session) {
        return AFIDEUtil.getIde(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Contrôle si les date d'annonces actives peuvent être modifiées
     * 
     * @param affiliation
     * @return
     */
    public static boolean checkUpdateDateAnnonceActive(AFAffiliation affiliation) throws Exception {
        /*
         * La modification des dates d'annonces est possible si:
         * affiliation non provisoire
         * genre d’affiliation différent de Fichier Central, Travailleur sans employeur, Travailleur sans employeur
         * volontaire. TODO Cette liste n’est pas exhaustive et sera complétée par InfoRom)
         * la forme juridique fait partie de la liste des formes juridiques à prendre en compte. TODO Cette liste doit
         * être transmise par InfoRom... en attendant si elle est vide la mutation est permise.
         */

        // liste des types d'affiliation non modifiable
        AFApplication appAf = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        String listeTypeAffiliationNonModifiable = appAf.getListeIDETypeAffiliationNonModifiable();
        // liste des personnalités juridiques modifiables
        String listePersonnaliteJuridiqueModifiable = appAf.getListeIDEPersonnaliteJuridiqueModifiable();
        if (affiliation.getTraitement().equals(Boolean.FALSE)
                && !listeTypeAffiliationNonModifiable.contains(affiliation.getTypeAffiliation())
                && (listePersonnaliteJuridiqueModifiable.isEmpty() || listePersonnaliteJuridiqueModifiable
                        .contains(affiliation.getPersonnaliteJuridique()))) {
            return true;
        }
        return false;
    }

    /**
     * Contrôle si le statut de l'IDE peut être ouvert en mode éditable.
     * 
     * @param affiliation
     * @return
     */
    public static boolean checkUpdateIdeStatut(AFAffiliation affiliation) {
        /*
         * Le statut IDe peut être modifier uniquement en cas de mise à jour manuelle suite à une annonce entrante en
         * erreur
         */
        // TODO - Ajouter le test d'annonce en erreur
        return false;
    }

    /**
     * Contrôle si les date d'annonces passives peuvent être modifiées
     * 
     * @param affiliation
     * @return
     */
    public static boolean checkUpdateDateAnnoncePassive(AFAffiliation affiliation) {
        // liste des types d'affiliation non modifiable
        if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            return true;
        }
        return false;
    }

    /**
     * Renseigne lu numéro IDE pour l'affichage sur les documents.
     * 
     * @param session Une session valide
     * @param headerBean Conteneur de l'entete du document
     * @param idAffiliation Un id d'affiliation
     */
    public static void addNumeroIDEInDocForNumAffilie(BSession session, CaisseHeaderReportBean headerBean,
            String numAffilie) {
        List<AFAffiliation> lst_aff = null;

        try {
            lst_aff = AFAffiliationUtil.findAffiliations(session, numAffilie);
        } catch (Exception e) {
            JadeLogger.error(AFIDEUtil.class, "Impossible de récupérer le numéro IDE pour le numéro d'affilié"
                    + numAffilie + " : " + e.getMessage());
        }

        // Si aucune affiliation correspond, on sort
        if (lst_aff == null || lst_aff.size() == 0) {
            return;
        }

        String numeroIde = lst_aff.get(0).getNumeroIDE();
        String ideStatut = lst_aff.get(0).getIdeStatut();

        // Si on au moins une des affiliations n'a pas le même couple ide/statut, on sort
        for (AFAffiliation aff : lst_aff) {

            if (!numeroIde.equals(aff.getNumeroIDE())) {
                return;
            }

            if (!ideStatut.equals(aff.getIdeStatut())) {
                return;
            }

        }

        addNumeroIDEInDoc(headerBean, numeroIde, ideStatut);
    }

    /**
     * Renseigne lu numéro IDE pour l'affichage sur les documents.
     * 
     * @param session Une session valide
     * @param headerBean Conteneur de l'entete du document
     * @param idAffiliation Un id d'affiliation
     */
    public static void addNumeroIDEInDoc(BSession session, CaisseHeaderReportBean headerBean, String idAffiliation) {
        AFAffiliation aff = null;

        try {
            aff = AFAffiliationUtil.getAffiliation(idAffiliation, session);
        } catch (Exception e) {
            JadeLogger.error(AFIDEUtil.class, "Impossible de récupérer le numéro IDE pour l'id affiliation "
                    + idAffiliation + " : " + e.getMessage());
        }

        if (aff != null) {
            addNumeroIDEInDoc(headerBean, aff.getNumeroIDE(), aff.getIdeStatut());
        }
    }

    /**
     * Renseigne lu numéro IDE pour l'affichage sur les documents.
     * 
     * @param session Une session valide
     * @param headerBean Conteneur de l'entete du document
     * @param idTiers L'id du tiers de l'affiliation
     * @param numAffilie Un numéro d'affilieé
     * @param numDecompte Un numéro de décompte (Ex : 201518000)
     * @param idRole L'id role du l'affilié (PARITAIRE ou PERSONNEL)
     */
    public static void addNumeroIDEInDoc(BSession session, CaisseHeaderReportBean headerBean, String idTiers,
            String numAffilie, String numDecompte, String idRole) {
        AFAffiliation aff = null;

        try {
            aff = AFAffiliationUtil.loadAffiliation(session, idTiers, numAffilie, numDecompte, idRole);
        } catch (Exception e) {
            JadeLogger.error(AFIDEUtil.class, "Impossible de récupérer le numéro IDE pour l'affilié " + numAffilie
                    + "(idTiers: " + idTiers + " / numAffilie: " + numAffilie + " / numDecompte: " + numDecompte
                    + " / idRole: " + idRole + e.getMessage());
        }

        if (aff != null) {
            addNumeroIDEInDoc(headerBean, aff.getNumeroIDE(), aff.getIdeStatut());
        }
    }

    /**
     * Renseigne lu numéro IDE pour l'affichage sur les documents.
     * 
     * @param headerBean Conteneur de l'entete du document
     * @param numeroIde Un numéro IDE
     * @param ideStatut Le statut du numéro IDE (DEFINITIF, PROVISOIRE, ...)
     */
    public static void addNumeroIDEInDoc(CaisseHeaderReportBean headerBean, String numeroIde, String ideStatut) {

        if (headerBean == null) {
            JadeLogger.error(AFIDEUtil.class,
                    "Impossible de mettre à jour le conteneur pour l'entete du document. headerBean is null");
        }

        headerBean.setNumeroIDE(defineNumeroStatutForDoc(numeroIde, ideStatut));
    }

    /**
     * Permet de définir le numéro IDE suivant le statut ce celui ci
     * 
     * @param numeroIde
     * @param ideStatut
     * @return
     */
    public static String defineNumeroStatutForDoc(String numeroIde, String ideStatut) {
        if (!JadeStringUtil.isBlank(ideStatut) && !JadeStringUtil.isBlank(numeroIde)
                && CodeSystem.STATUT_IDE_DEFINITIF.equals(ideStatut)) {
            return AFIDEUtil.formatNumIDE(numeroIde);
        }

        return "";
    }

    /**
     * Permet de créer la chaine de caractère contenant le numéro d'affilié et le numéro IDE pour l'affichage dans les
     * documents <BR>
     * Du style : 1244.2545 / CHE-456.321.188
     * 
     * @param numeroAffilie Le numéro d'affilié
     * @param numeroIde Le numéro IDE
     * @param ideStatut Le statut du numéro IDE (DEFINITIF, PROVISOIRE, ...)
     * @return Une chaine de caractère contenant le numéro d'affilié et le numéro IDE
     */
    public static String createNumAffAndNumeroIdeForPrint(String numeroAffilie, String numeroIde, String ideStatut) {
        String numeroAffilieAndNumeroIde = "";

        if (numeroAffilie != null) {
            numeroAffilieAndNumeroIde = numeroAffilie.trim();
        }

        String numIde = defineNumeroStatutForDoc(numeroIde, ideStatut);

        if (!JadeStringUtil.isBlank(numIde)) {
            numeroAffilieAndNumeroIde += "/" + numIde;
        }

        return numeroAffilieAndNumeroIde;
    }

    /**
     * contrôle si l'affiliation liée à l'annonce est en statut provisoire
     * 
     * @param idAffiliation L'ID de l'affiliation à remonter pour faire le contrôle
     * @return boolean true si provisoire
     */
    public static boolean isProvisoirIdeInAffiliation(BSession session, String idAffiliation) throws Exception {
        AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(idAffiliation, session);
        return affiliation.isIdeProvisoire();
    }

    public static String formatNogaRegistre(String noga, BSession session) {
        if (noga.equals(OFS_NOGA_INDETERMINE)) {
            return session.getLabel("NAOS_IDE_UTIL_OFS_NOGA_UNDETERMINATED");
        }
        return noga;
    }

}
