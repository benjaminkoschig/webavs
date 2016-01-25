package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.TarifAggregator;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.courrier.LettreAccompagnementCopieService;
import ch.globaz.al.business.services.decision.DecisionService;
import ch.globaz.al.businessimpl.copies.defaut.ContextDefaultCopiesLoader;
import ch.globaz.al.businessimpl.copies.defaut.DefaultCopiesLoaderFactory;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALDeepCopy;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Class racine qui permet de remplir les données nécessaires à la génération des documents de décisions
 * 
 * @author JER/PTA/JTS
 */
public abstract class DecisionAbstractServiceImpl extends AbstractDocument implements DecisionService {

    /**
     * Indique si la page d'accompagnement d'une copie doit être générée
     * 
     * @param dossier
     *            dossier pour lequel généré la page d'accompagnement
     * @param idTiersDestinataire
     *            id du tiers destinataire de la copie
     * @return <code>true</code> si la page d'accompagnement doit être générée, <code>false</code> sinon
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier
     */
    private boolean genererPageAccompagnement(DossierComplexModel dossier, String idTiersDestinataire)
            throws JadePersistenceException, JadeApplicationException {

        if (idTiersDestinataire.equals(AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie()))
                || idTiersDestinataire.equals(dossier.getAllocataireComplexModel().getAllocataireModel()
                        .getIdTiersAllocataire())) {
            return false;
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getDossierModel().getActiviteAllocataire())
                && ALImplServiceLocator.getDossierBusinessService().isAgenceCommunale(idTiersDestinataire)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Méthode qui retourne une chaîne de caractères avec l'adresse de la banque
     * 
     * @param adress
     *            adresse de paiement
     * @return String valeur du ccp ou données bancaires
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String getAdressePaiement(AdresseTiersDetail adress, String langueDocument) throws JadeApplicationException {
        String adressePaiement = null;
        // contrôle des paramètres
        if (adress.getFields() == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getAdressePaiement: adress.getFields() is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#getAdressePaiement: language  " + langueDocument
                    + " is not  valid ");
        }
        // si l'adresse de paiement est un compte CCP
        if (!JadeStringUtil.isBlankOrZero(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
            adressePaiement = this.getText("al.decision.versementDirect.info.adressePaiement.ccp", langueDocument)
                    + " " + adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP);
        }
        // si adresse de paiement n'est pas un compte postal, mais un compte
        // bancaire
        else if (!JadeStringUtil.isBlank(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))) {
            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1) + "\n"
                    + this.getText("al.decision.versementDirect.info.compte.label", langueDocument) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE);
        }

        // si l'adresse de paiement n'a ni banque ni ccp
        else if (JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))
                && JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {

            // Rue, numero, Numéro postal et localité
            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) + "\n"
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

        }

        return adressePaiement;
    }

    /**
     * Méthode qui retourne une chaîne de caractères avec l'adresse de la banque
     * 
     * @param adress
     *            adresse de paiement
     * @return String valeur du ccp ou données bancaires
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String getCompteDroitBeneficiaire(AdresseTiersDetail adress, String langueDocument)
            throws JadeApplicationException {
        String adressePaiement = null;
        // contrôle du paramètre
        if (adress == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getAdressePaiement: adress is null");
        } else if (adress.getFields() == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getAdressePaiement: adress.getFields() is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#getAdressePaiement: language  " + langueDocument
                    + " is not  valid ");
        }
        // si l'adresse de paiement est un compte CCP
        if (!JadeStringUtil.isBlankOrZero(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
            adressePaiement = this.getText("al.decision.versementDirect.info.adressePaiement.ccp", langueDocument)
                    + " " + adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP);
        }
        // si adresse de paiement n'est pas un compte postal, mais un compte
        // bancaire
        else if (!JadeStringUtil.isBlank(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))) {

            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1);

            adressePaiement = this.getText("al.decision.versementDirect.info.compte.label", langueDocument) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1);

        }

        // si l'adresse de paiement n'a ni banque ni ccp
        else if (JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))
                && JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
            // si l'adresse de paiement est identique à l'adresse du tiers
            // bénéficiaire

            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

        }

        return adressePaiement;
    }

    /**
     * Retourne la date à utiliser pour le calcul de la décision selon le dossier
     * 
     * @param dossierComplexModel
     *            dossier
     * @return la date à utiliser pour le calcul dans la décision
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private final String getDateCalcul(DossierComplexModel dossierComplexModel) throws JadeApplicationException {

        if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
            return dossierComplexModel.getDossierModel().getFinValidite();

        } else {
            return dossierComplexModel.getDossierModel().getDebutValidite();
        }
    }

    /**
     * Méthode qui remplit la référence voulu
     * 
     * @param documentData
     *            Document à remplir
     * @param dossierComplexModel
     * @param visaUtilisateur
     *            visa d'utilisateur
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void getDossierReference(DocumentData documentData, DossierComplexModel dossierComplexModel,
            HashMap<String, String> userInfos, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle des paramètre
        if (documentData == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getDossierReference documentData is null");
        }
        if (dossierComplexModel == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getDossierReference dossierComplexModel is null");
        }
        if (userInfos == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getDossierReference userInfos is null");
        }

        if (JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse(), ALConstCaisse.CAISSE_H515,
                true)
                || JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse(),
                        ALConstCaisse.CAISSE_CCVD, true)) {
            documentData.addData("entete_reference", userInfos.get(ALConstDocument.USER_VISA));
        } else if (JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse(),
                ALConstCaisse.CAISSE_FPV, true)) {
            documentData.addData("entete_reference", userInfos.get(ALConstDocument.USER_NAME));
        } else if (JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse().toUpperCase(),
                ALConstCaisse.CAISSE_FVE, true)) {
            documentData.addData("traiterPar", this.getText("al.decision.standard.dossierTraite", langueDocument)
                    + userInfos.get(ALConstDocument.USER_NAME));
        } else {
            documentData.addData("entete_reference", dossierComplexModel.getDossierModel().getReference());
        }
    }

    /***
     * Effectue une requête afin de récupérer tous les droits contenus dans un dossier
     * 
     * @param idDossier
     *            id du dossier à rechercher
     * @return Retourne une liste de <{@link DroitModel}> contenus dans le dossier
     */
    private List<DroitModel> getDroitsActifs(String idDossier, String dateImpression) {
        try {
            return ALImplServiceLocator.getDroitModelService().findActifs(idDossier, dateImpression);
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        } catch (JadeApplicationException e) {
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Méthode qui retourne le nom, prénom et/ou la localité, selon les cas d'un tiers bénéficiaire
     * 
     * @param adress
     *            détail de l'adresse du tiers bénéficiaire
     * @return le texte "en faveur de..."
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String getFaveurDe(AdresseTiersDetail adress) throws JadeApplicationException {
        String enFaveurDe = null;
        // contrôle du paramètre
        if (adress == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getFaveurDe: adress is null");
        }
        // si il n'y a pas d'adresse ccp ou banque en retourne le nom prénom
        if (JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))
                && JadeStringUtil.isEmpty(adress.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))) {
            enFaveurDe = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);

        } else {
            enFaveurDe = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2) + "\n"
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

        }
        return enFaveurDe;
    }

    /**
     * retourne une hashmap contenant comme clé activité de l'allocataire et valeur: libellé de cette activité
     * 
     * @return the activiteAlloc
     */
    private HashMap<String, String> getlibellesActivitesAlloc() {

        HashMap<String, String> activiteAlloc = new HashMap<String, String>();

        activiteAlloc.put(ALCSDossier.ACTIVITE_AGRICULTEUR, "al.decision.standard.agriculteurs");
        activiteAlloc.put(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, "al.decision.standard.collaAgricoles");
        activiteAlloc.put(ALCSDossier.ACTIVITE_INDEPENDANT, "al.decision.standard.independants");
        activiteAlloc.put(ALCSDossier.ACTIVITE_NONACTIF, "al.decision.standard.nonActifs");
        activiteAlloc.put(ALCSDossier.ACTIVITE_SALARIE, "al.decision.standard.salarie");
        activiteAlloc.put(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, "al.decision.standard.travailleurAgri");
        activiteAlloc.put(ALCSDossier.ACTIVITE_PECHEUR, "al.decision.standard.pecheur");
        activiteAlloc.put(ALCSDossier.ACTIVITE_TSE, "al.decision.standard.travailleurSansEmployeur");
        activiteAlloc.put(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE, "al.decision.standard.exploitAlpage");

        return activiteAlloc;
    }

    /**
     * <p>
     * Récupère la loi selon l'algorithme suivant :
     * </p>
     * <li>
     * Si un tarif est forcé sur un des droits du dossier, alors on utilise ce droit</li> <li>
     * Sinon si le tarif est forcé sur le dossier à {@link ALCSTarif#CATEGORIE_VD_DROIT_ACQUIS}, alors on utilise le
     * libelle "Droits Acquis Vaud"</li> <li>
     * Sinon si le tarif est forcé sur le dossier, on récupère le code raccourci du code système (par ex : "VS" pour
     * "Valais")</li> <li>
     * Sinon on prend le canton de l'affilié</li>
     * 
     * @param dossierComplexModel
     *            Le dossier de la décision
     * @param assInfo
     *            Informations quant à l'assurance
     * @return String : loi appliqué sur la décision
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    protected String getLoi(DossierComplexModel dossierComplexModel, AssuranceInfo assInfo, String dateImpression)
            throws JadeApplicationException, JadeApplicationServiceNotAvailableException {
        // INFOROMD0028, on affiche la loi "Droits Acquis Vaud" si ce tarif est forcé

        // Recherche des tarifs forcés dans les droits
        String loi = getLoiDroits(dossierComplexModel, dateImpression);

        if (!TarifAggregator.TARIF_UNDEFINED.equals(loi) && !"0".equals(loi)) {
            if (!TarifAggregator.TARIF_MULTIPLE.equals(loi)) {
                loi = JadeCodesSystemsUtil.getCode(loi);
            } else {
                // Des valeurs différentes sont fixées sur les droits, de ce fait, on affiche une étoile sur le
                // document.
            }
        } else if (ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS.equals(dossierComplexModel.getDossierModel().getTarifForce())) {
            // INFOROMD0028 - AF - Modifications montants AF VD (CBU)
            loi = JadeCodesSystemsUtil.getCodeLibelle(dossierComplexModel.getDossierModel().getTarifForce());
            // FIN INFOROMD0028
        } else if (!"0".equals(dossierComplexModel.getDossierModel().getTarifForce())) {
            loi = JadeCodesSystemsUtil.getCode(dossierComplexModel.getDossierModel().getTarifForce());
        } else {
            loi = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(assInfo.getCanton());
            loi = JadeCodesSystemsUtil.getCode(loi);
        }
        return loi;
    }

    /**
     * Retourne la loi (categorie de tarif pour un canton) qui s'applique pour les droits contenus dans un dossier.
     * 
     * @param dossierComplexModel
     *            Le dossier en cours de traitement
     * @return La loi s'appliquant au dossier, ou "*" (étoile) si plusieurs lois sont appliquées.
     */
    private String getLoiDroits(DossierComplexModel dossierComplexModel, String dateImpression) {
        return TarifAggregator.calculerTarif(getTarifs(dossierComplexModel.getId(), dateImpression));
    }

    /**
     * Retourne une liste contenant les tarifs des droits contenus dans un dossier
     * 
     * @param idDossier
     *            id du dossier à rechercher
     * @return Liste des codes systèmes représentant les cantons. La valeur 0 correspond à l'absence d'un code système,
     *         et donc le fait que le tarif n'ai pas été forcé
     */
    private List<String> getTarifs(String idDossier, String dateImpression) {
        List<String> tarifsCanton = new ArrayList<String>();
        for (DroitModel droitModel : getDroitsActifs(idDossier, dateImpression)) {
            if (!droitModel.getTypeDroit().equals(ALCSDroit.TYPE_MEN)) {
                tarifsCanton.add(droitModel.getTarifForce());
            }
        }
        return tarifsCanton;
    }

    /**
     * Texte du libellé pour la référence
     * 
     * @param documentData
     *            document à créer
     * @param langueDocument
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void getTexteLibelleReference(DocumentData documentData, String langueDocument)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        // contrôle des paramètres
        if (documentData == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getTexteLibelleReference documentData is null");
        }

        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getTexteLibelleReference language  "
                    + langueDocument + " is not  valid ");
        }
        if (JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse(), ALConstCaisse.CAISSE_FPV,
                true)) {
            documentData.addData("entete_reference_label",
                    this.getText("al.decision.standard.dossierTraite", langueDocument));

        } else {
            documentData.addData("entete_reference_label",
                    this.getText("al.decision.standard.entete.reference", langueDocument));
        }
    }

    /**
     * Charge une ArrayList avec les données des champs copies de l'écran de décision
     * 
     * @param dossier
     *            Le dossier lié aux copies
     * @param isPaiementDirect
     *            Défini si on est en paiement direct
     * @param copies
     *            Liste des copies
     * @return Liste des textes de copie
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private ArrayList<String> getTextesCopies(DossierComplexModel dossier, boolean isPaiementDirect,
            ArrayList<CopieComplexModel> copies) throws JadePersistenceException, JadeApplicationException {

        ArrayList<String> listCopies = new ArrayList<String>();

        for (int i = 1; i < copies.size(); i++) {
            listCopies.add(ALServiceLocator.getCopiesBusinessService().getLibelleCopie(dossier,
                    copies.get(i).getCopieModel().getIdTiersDestinataire(), ALCSCopie.TYPE_DECISION));

        }

        return listCopies;
    }

    /**
     * Méthode qui retourne true si tous les bénéficiaires des droits sont différents du bénéficiaire du dossier et
     * false dans tous les autres cas
     * 
     * @param listTiersBeneficiaireDroit
     *            listes des tiersBénéficiaires des différents droits
     * @param idBeneficiaireDossier
     *            tiers bénéficiaire du dossier
     * @return boolean
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected boolean isNotAllDroitBenEgalBenDos(ArrayList<String> listTiersBeneficiaireDroit,
            String idBeneficiaireDossier) throws JadeApplicationException {
        // contrôle de paramètres
        if (listTiersBeneficiaireDroit == null) {
            throw new ALDecisionException(
                    "DecisionAbsractServiceImpl#isNotAllDroitBenEgalBenDos: listTiersBeneficiaireDroit is null");
        }
        if (!JadeNumericUtil.isInteger(idBeneficiaireDossier)) {
            throw new ALDecisionException(
                    "DecisionAbstractServiceImpl#isNotAllDroitBenEgalBenDos:idBeneficiaireDossier is not a integer");
        }
        boolean droitBen = false;

        Iterator<String> it = listTiersBeneficiaireDroit.iterator();

        while (it.hasNext()) {
            String s = it.next();
            if (JadeStringUtil.equals(s, idBeneficiaireDossier, false) || JadeStringUtil.isBlankOrZero(s)) {
                return droitBen = false;
            } else {
                droitBen = true;
            }

        }
        return droitBen;
    }

    /**
     * Retourne le commentaire à afficher sur la décision selon les règles suivantes :
     * 
     * *
     * <TABLE>
     * <TR>
     * <TD><B>Coche gestion texte libre</B></TD>
     * <TD><B>Texte mentionn&eacute; dans nouvel &eacute;cran</B></TD>
     * <TD><B>Texte mentionn&eacute; dans &eacute;cran AL0016</B></TD>
     * <TD><B>R&eacute;sultat</B></TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le dossier</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran puis du texte figurant dans le
     * dossier</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * </TABLE>
     * 
     * @param dossierComplexModel
     *            Le dossier lié au commentaire
     * @return Un CommentaireModel rempli
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String loadCommentaireDossier(DossierComplexModel dossierComplexModel, String texteLibre,
            boolean gestionTexteLibre) throws JadePersistenceException, JadeApplicationException {

        // Chargement du texte libre du dossier
        String texteDossier = "";
        CommentaireSearchModel commentaireSearch = new CommentaireSearchModel();
        commentaireSearch.setForIdDossier(dossierComplexModel.getId());
        commentaireSearch = ALImplServiceLocator.getCommentaireModelService().search(commentaireSearch);
        if (commentaireSearch.getSize() != 0) {
            texteDossier = ((CommentaireModel) commentaireSearch.getSearchResults()[0]).getTexte();
        }

        // gestion texte libre activée : oui
        if (gestionTexteLibre) {
            // texte libre renseigné : non
            if (JadeStringUtil.isBlank(texteLibre)) {

                // text dossier renseigné : non
                if (JadeStringUtil.isBlank(texteDossier)) {
                    return "";
                    // text dossier renseigné : oui
                } else {
                    return texteDossier;
                }

                // texte libre renseigné : non
            } else {
                // text dossier renseigné : non
                if (JadeStringUtil.isBlank(texteDossier)) {
                    return texteLibre;
                    // text dossier renseigné : oui
                } else {
                    return texteLibre + System.getProperty("line.separator") + texteDossier;
                }
            }
            // gestion texte libre activée : non
        } else {
            // texte libre renseigné : non
            if (JadeStringUtil.isBlank(texteLibre)) {
                return "";

                // texte libre renseigné : non
            } else {
                return texteLibre;
            }
        }
    }

    /**
     * Déclenche la collecte des datas pour la création des décisions
     * 
     * @param dossier
     *            Le dossier de la décision en cours
     * @param copies
     *            Liste des copies
     * @param dateImpression
     *            Date d'impression à afficher sur la décision
     * @return Décision
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected DocumentData loadData(DossierComplexModel dossier, ArrayList<CopieComplexModel> copies,
            String dateImpression, String langueDocument, HashMap<String, String> userInfos, String texteLibre,
            boolean gestionTexteLibre) throws JadeApplicationException, JadePersistenceException {

        DocumentData documentData = new DocumentData();
        setIdDocument(documentData);
        this.setIdEntete(documentData, dossier.getDossierModel().getActiviteAllocataire(),
                ALConstDocument.DOCUMENT_DECISION, langueDocument, userInfos);

        this.setIdSignature(documentData, dossier.getDossierModel().getActiviteAllocataire(),
                ALConstDocument.DOCUMENT_DECISION, langueDocument);

        // Chargement des textes de copies et commentaire du dossier
        String commentaire = loadCommentaireDossier(dossier, texteLibre, gestionTexteLibre);

        ArrayList<String> listCopies = getTextesCopies(dossier, ALServiceLocator.getDossierBusinessService()
                .isModePaiementDirect(dossier.getDossierModel()), copies);

        // reprend le tiers destinataire de la decision originale
        String idTiersCopieOriginale = copies.get(0).getCopieModel().getIdTiersDestinataire();

        // recherche idTiersAffilie
        String idTiersAFfilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie());

        if (JadeStringUtil.equals(idTiersCopieOriginale, idTiersAFfilie, false)) {
            this.addDateAdresse(documentData, dateImpression, idTiersCopieOriginale, langueDocument, dossier
                    .getDossierModel().getNumeroAffilie());
        } else {
            this.addDateAdresse(documentData, dateImpression, idTiersCopieOriginale, langueDocument, null);
        }

        loadTextEntete(documentData, dossier, userInfos, langueDocument, dateImpression);

        ArrayList<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService().getCalcul(dossier,
                getDateCalcul(dossier));
        loadListDroit(documentData, dossier, resultatCalcul, getDateCalcul(dossier), langueDocument);
        loadListNaissance(documentData, dossier, resultatCalcul, getDateCalcul(dossier), langueDocument);

        loadTextesDecision(documentData, dossier, commentaire, langueDocument);
        loadTextCopies(documentData, listCopies, dossier, langueDocument);

        return documentData;

    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfo) throws JadeApplicationException,
            JadePersistenceException {
        return this.loadData(dossier, dateImpression, langueDocument, userInfo, "", true);
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfos, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException {
        return this.loadData(dossier, dateImpression, langueDocument, userInfos, true, texteLibre, gestionTexteLibre);
    }

    @Override
    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfos, boolean gestionCopie, String texteLibre,
            boolean gestionTexteLibre) throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres
        if (dossier == null) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#loadData: dossier is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#loadData: " + dateImpression
                    + " is not valid date");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionAbstractServiceImpl3loadData: language  " + langueDocument
                    + " is not  valid ");
        }

        ArrayList<CopieComplexModel> listeCopies = new ArrayList<CopieComplexModel>();
        // la gestion des copie signifie prendre les données de copie en DB
        if (gestionCopie) {
            CopieComplexSearchModel search = searchCopies(dossier);
            for (int i = 0; i < search.getSearchResults().length; i++) {
                listeCopies.add((CopieComplexModel) search.getSearchResults()[i]);
            }
            // sinon on prend le comportement par défaut (comme à la creation)
        } else {
            listeCopies = DefaultCopiesLoaderFactory.getDefaultCopiesLoader(
                    ContextDefaultCopiesLoader.getInstance(dossier, ALCSCopie.TYPE_DECISION)).getListCopies();
        }

        DocumentData document = this.loadData(dossier, listeCopies, dateImpression, langueDocument, userInfos,
                texteLibre, gestionTexteLibre);

        ArrayList<DocumentData> copies = new ArrayList<DocumentData>();
        ArrayList<DocumentData> decisionsOriginales = new ArrayList<DocumentData>();
        decisionsOriginales.add(document);

        LettreAccompagnementCopieService sCopies = ALServiceLocator.getLettreAccompagnementCopieService();

        String idTiersNumAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie());

        for (int i = 1; i < listeCopies.size(); i++) {
            DocumentData newDocData = (DocumentData) ALDeepCopy.copy(document);

            String idTiers = listeCopies.get(i).getCopieModel().getIdTiersDestinataire();

            // lettre accompagnement
            if (genererPageAccompagnement(dossier, idTiers)) {
                copies.add(sCopies.loadData(new ProtocoleLogger(), idTiers, ALCSCopie.TYPE_DECISION, dossier.getId(),
                        langueDocument).getDocument());
            }
            // TODO faire appel à méthode avec également numéro d'affiliée si le tiers destinataire correspond au tiers
            // affilié
            // ajout de l'adresse sur le document

            // si idTiersCopie idnetique à idTiersnumAffilie reprendre avec addDateAdresse avec numAffilie sinon à null

            String idTiersCopie = ALImplServiceLocator.getCopiesService().getIdTiersAdresse(dossier,
                    ALCSCopie.TYPE_DECISION, idTiers);

            if (JadeStringUtil.equals(idTiersCopie, idTiersNumAffilie, false)) {
                this.addDateAdresse(newDocData, dateImpression, idTiersCopie, langueDocument, dossier.getDossierModel()
                        .getNumeroAffilie());
            } else {
                this.addDateAdresse(newDocData, dateImpression, idTiersCopie, langueDocument, null);
            }
            copies.add(newDocData);
        }

        HashMap<String, ArrayList<DocumentData>> docDataList = new HashMap<String, ArrayList<DocumentData>>();
        docDataList.put(ALConstDecisions.DECISION_ORIGINALE, decisionsOriginales);
        docDataList.put(ALConstDecisions.DECISION_COPIES, copies);

        return docDataList;
    }

    /**
     * Méthode qui charge le données liées à un bénéficiaire d'un droit
     * 
     * @param droitModel
     * @param droitBeneficiaire
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void loadDroitTiersBeneficiaire(DroitComplexModel droitModel, DataList droitBeneficiaire,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // recherche de l'adresse de paiement du tiers bénéficiaire
        // FIXME: CS_DOMAINE_AF mais il faut que service gère la
        // cascade...
        AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                droitModel.getDroitModel().getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                JadeDateUtil.getGlobazFormattedDate(new Date()), "");

        String adressePaiement = getCompteDroitBeneficiaire(adress, langueDocument);
        String enFaveurDe = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);

        // si le droit n'est pas ménage
        if (!JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)
                && !JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LFA, false)
                && !JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LJA, false)) {
            // le tiers bénéficiaire correspond au tiers du droit
            if (JadeStringUtil.equals(droitModel.getDroitModel().getIdTiersBeneficiaire(), droitModel
                    .getEnfantComplexModel().getEnfantModel().getIdTiersEnfant(), false)) {

                // label adresse de paiement
                droitBeneficiaire.addData("droit_beneficiaire",
                        this.getText("al.decision.versementDirect.info.droitBeneficiaire", langueDocument) + " "
                                + adressePaiement);

            }

            // si le tiers du droit ne correspond pas au tiers bénéficiaire et
            // que le tiers bénéficiaire est autre que 0
            else if (!JadeStringUtil.equals(droitModel.getDroitModel().getIdTiersBeneficiaire(), droitModel
                    .getEnfantComplexModel().getEnfantModel().getIdTiersEnfant(), false)
                    && (!JadeStringUtil.isBlankOrZero(droitModel.getDroitModel().getIdTiersBeneficiaire()))) {
                // label en faveur de
                droitBeneficiaire.addData("droit_beneficiaire",
                        this.getText("al.decision.versementDirect.info.droitBeneficiaire", langueDocument) + " "
                                + enFaveurDe + " " + adressePaiement);
            }
        } else if (JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)
                || JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LFA, false)
                || JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LJA, false)) {
            droitBeneficiaire.addData("droit_beneficiaire",
                    this.getText("al.decision.versementDirect.info.droitBeneficiaire", langueDocument)
                            + droitModel.getTiersBeneficiaireModel().getDesignation1() + " "
                            + droitModel.getTiersBeneficiaireModel().getDesignation1());

        }

    }

    /**
     * Méthode qui charge les données de versement liée à un paiement direct
     * 
     * @param documentData
     *            DocumentData à charger
     * @param dossierModel
     *            les données du dossier
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void loadInfoVersementDirect(DocumentData documentData, DossierComplexModel dossierModel,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {
        // vérification des paramètres
        if (documentData == null) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#loadInfoVersementDirect : documentData is null");

        }
        if (dossierModel == null) {
            throw new ALDecisionException("DecisionAbstractServiceImpl#loadInfoVersementDirect : dossierModel is null");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#loadInfoVersementDirect: language  "
                    + langueDocument + " is not  valid ");
        }

        // recherche de l'adresse de paiement du tiers bénéficiaire
        // FIXME: CS_DOMAINE_AF mais il faut que service gère la
        // cascade...
        AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                dossierModel.getDossierModel().getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                JadeDateUtil.getGlobazFormattedDate(new Date()), "");
        // this.loadAdressePaiement(documentData, adress, dossierModel);
        String adressePaiement = getAdressePaiement(adress, langueDocument);
        String enFaveurDe = getFaveurDe(adress);

        // si le tiers bénéficiaire correspond au tiers allocataire
        if (JadeStringUtil.equals(dossierModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire(), dossierModel.getDossierModel().getIdTiersBeneficiaire(), false)) {

            // label adresse de paiement
            documentData.addData("adresse_paiement",
                    this.getText("al.decision.versementDirect.info.adressePaiement.label", langueDocument));

            // valeur adresse de paiement
            documentData.addData("adresse_paiement_valeur", adressePaiement);
        }

        // si le tiers allocataire ne correspond pas au tiers bénéficiaire et
        // que le tiers bénéficiaire est autre que 0
        else if (!JadeStringUtil.equals(dossierModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire(), dossierModel.getDossierModel().getIdTiersBeneficiaire(), false)
                && (!JadeStringUtil.isBlankOrZero(dossierModel.getDossierModel().getIdTiersBeneficiaire()))) {
            // label en faveur de
            documentData.addData("adresse_paiement",
                    this.getText("al.decision.versementDirect.info.adressePaiement.faveur.label", langueDocument));
            // valeur en faveur de

            documentData.addData("adresse_paiement_valeur", enFaveurDe);
            // label adresse de paiement
            documentData.addData("adresse_paiement_compl",
                    this.getText("al.decision.versementDirect.info.adressePaiement.label", langueDocument));

            // valeur adress de paiement
            documentData.addData("adresse_paiement_compl_val", adressePaiement);

        }
    }

    /**
     * Charge les données de la liste des droits AF dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossier
     *            Le dossier de la décision
     * @param calcul
     *            Le calcul des droits issus du dossier
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected abstract void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
            ArrayList<CalculBusinessModel> calcul, String date, String langueDocument) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Charge les données concernant les allocations de naissance dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossier
     *            Le dossier de la décision
     * @param calcul
     *            Le calcul des droits issus du dossier
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier
     */
    protected abstract void loadListNaissance(DocumentData documentData, DossierComplexModel dossier,
            ArrayList<CalculBusinessModel> calcul, String dateDossierCalcul, String langueAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * 
     * @param dossier
     * @param calcul
     * @param total
     * @param date
     * @param list
     * @param tableau_sous_total
     * @param langueDocument
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    protected Collection loadMontantJoursDebut(DossierComplexModel dossier, ArrayList<CalculBusinessModel> calcul,
            HashMap total, String date, DataList list, Collection tableau_sous_total, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        if (!JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getNbJoursDebut())) {
            String dateDebut = dossier.getDossierModel().getDebutValidite();
            String dateFin = ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite());

            // si même mois debut et fin, date fin = date de fin
            // réelle
            if (!JadeStringUtil.isEmpty(dossier.getDossierModel().getFinValidite())
                    && ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite()).equals(
                            ALDateUtils.getDateFinMois(dossier.getDossierModel().getFinValidite()))) {
                dateFin = dossier.getDossierModel().getFinValidite();
            }

            total = ALServiceLocator.getCalculBusinessService().getTotal(dossier.getDossierModel(), calcul,
                    ALCSDossier.UNITE_CALCUL_JOUR, dossier.getDossierModel().getNbJoursDebut(), false, date);
            calcul = (ArrayList<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

            list = new DataList("subtotal");
            list.addData(
                    "tableau_sous_total_0",
                    this.getText("al.decision.liste.droit.alloc.dossier.jour", langueDocument)
                            + " "
                            + dateDebut
                            + " "
                            + this.getText("al.decision.liste.droit.alloc.dossier.jour.au", langueDocument)
                            + " "
                            + dateFin
                            + ", "
                            + dossier.getDossierModel().getNbJoursDebut()
                            + " "
                            + this.getText("al.decision.liste.droit.alloc.dossier.jour.jours", langueDocument)
                            + " "
                            + this.getText("al.decision.liste.droit.chf", langueDocument)
                            + " "
                            + JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF), true, true,
                                    false, 2));

            list.addData("tableau_sous_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_sous_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_EFFECTIF), true, true, false, 2));
            tableau_sous_total.add(list);
        }
        return tableau_sous_total;
    }

    protected Collection loadMontantJoursFin(DossierComplexModel dossier, ArrayList<CalculBusinessModel> calcul,
            HashMap total, String date, DataList list, Collection tableau_sous_total, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        if (!JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getNbJoursFin())) {

            String dateDebut = ALDateUtils.getDateDebutMois(dossier.getDossierModel().getFinValidite());
            String dateFin = dossier.getDossierModel().getFinValidite();

            // si même mois debut et fin, date début = date de début
            // réelle
            if (!JadeStringUtil.isEmpty(dossier.getDossierModel().getDebutValidite())
                    && ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite()).equals(
                            ALDateUtils.getDateFinMois(dossier.getDossierModel().getFinValidite()))) {
                dateDebut = dossier.getDossierModel().getDebutValidite();
            }

            String UniteEffectif = JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF), true,
                    true, false, 2);

            // calcul du montant en fonction du nombre de jour pour le début de
            // la période
            total = ALServiceLocator.getCalculBusinessService().getTotal(dossier.getDossierModel(), calcul,
                    ALCSDossier.UNITE_CALCUL_JOUR, dossier.getDossierModel().getNbJoursFin(), false, date);
            calcul = (ArrayList<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);
            list = new DataList("subtotal");
            list.addData(
                    "tableau_sous_total_0",
                    this.getText("al.decision.liste.droit.alloc.dossier.jour", langueDocument) + " " + dateDebut + " "
                            + this.getText("al.decision.liste.droit.alloc.dossier.jour.au", langueDocument) + " "
                            + dateFin + ", " + dossier.getDossierModel().getNbJoursFin() + " "
                            + this.getText("al.decision.liste.droit.alloc.dossier.jour.jours", langueDocument) + " "
                            + this.getText("al.decision.liste.droit.chf", langueDocument) + " " + UniteEffectif);
            list.addData("tableau_sous_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_sous_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_EFFECTIF), true, true, false, 2));
            tableau_sous_total.add(list);
        }
        return tableau_sous_total;
    }

    /**
     * charge le texte "concerne" en fonction du dossier (type d'allocataire, début / fin validité)
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Le dossier de la décision
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private final void loadTextConcerne(DocumentData documentData, DossierComplexModel dossierComplexModel,
            String langueDocument) throws JadeApplicationException {

        if (dossierComplexModel.getDossierModel().getEtatDossier().equals(ALCSDossier.ETAT_RADIE)) {
            documentData.addData("entete_concerne_label_decision",
                    this.getText("al.decision.radie.entete.concerne", langueDocument));
        } else {
            documentData.addData("entete_concerne_label_decision",
                    this.getText("al.decision.standard.entete.concerne", langueDocument));
        }

        documentData.addData("entete_concerne_type_alloc", this.getText(
                getlibellesActivitesAlloc().get(dossierComplexModel.getDossierModel().getActiviteAllocataire()),
                langueDocument));

        if (ALCSDossier.ETAT_RADIE.equals(dossierComplexModel.getDossierModel().getEtatDossier())) {
            documentData.addData("entete_concerne_label_au",
                    this.getText("al.decision.standard.entete.concerne.au", langueDocument));
            documentData.addData("entete_concerne_date_fin", dossierComplexModel.getDossierModel().getFinValidite());
        } else {
            if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getDebutValidite())
                    && JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
                documentData.addData("entete_concerne_label_des_le",
                        this.getText("al.decision.standard.entete.concerne.des", langueDocument));
                documentData.addData("entete_concerne_date", " "
                        + dossierComplexModel.getDossierModel().getDebutValidite());
                documentData.addData("entete_concerne_label_au",
                        " " + this.getText("al.decision.standard.entete.concerne.jusqu", langueDocument));
                documentData
                        .addData("entete_concerne_date_fin", dossierComplexModel.getDossierModel().getFinValidite());
            } else {
                if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
                    documentData.addData("entete_concerne_label_au",
                            this.getText("al.decision.standard.entete.concerne.jusqu", langueDocument));
                    documentData.addData("entete_concerne_date_fin", " "
                            + dossierComplexModel.getDossierModel().getFinValidite());

                } else {

                    documentData.addData("entete_concerne_label_des_le",
                            this.getText("al.decision.standard.entete.concerne.des", langueDocument));
                    documentData.addData("entete_concerne_date", " "
                            + dossierComplexModel.getDossierModel().getDebutValidite());
                }
            }
        }

    }

    /**
     * Charge les données concernant les copies dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param listCopies
     *            Liste des textes de copie du document
     * @param dossierComplexModel
     *            Dossier de l'allocataire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void loadTextCopies(DocumentData documentData, ArrayList<String> listCopies,
            DossierComplexModel dossierComplexModel, String langueDocument) throws JadeApplicationException {
        Collection tableau_copie = new Collection("tableau_copie_definition");
        if (!listCopies.isEmpty()) {
            for (int i = 0; i < listCopies.size(); i++) {
                DataList list = new DataList("copie");
                if (i == 0) {
                    list.addData("copie_label", this.getText("al.decision.standard.copies.copie", langueDocument));
                } else {
                    list.addData("copie_label", "");
                }

                if (JadeStringUtil.equals(listCopies.get(i), ALConstDocument.SALARIE, true)) {
                    list.addData("copie_destinataire",
                            this.getText("al.decision.standard.copie.salarie", langueDocument));

                } else {
                    list.addData("copie_destinataire", listCopies.get(i));
                }
                tableau_copie.add(list);
            }
        } else {
            DataList list = new DataList("copie");
            tableau_copie.add(list);
        }
        documentData.add(tableau_copie);
    }

    /**
     * Charge les données concernant les textes d'entêtes de document dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Le dossier de la décision
     * @param dateImpression
     *            La date d'impression du document
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     */
    protected void loadTextEntete(DocumentData documentData, DossierComplexModel dossierComplexModel,
            HashMap<String, String> userInfos, String langueDocument, String dateImpression)
            throws JadeApplicationException, JadePersistenceException {

        AssuranceInfo assInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                dossierComplexModel.getDossierModel(), dossierComplexModel.getDossierModel().getDebutValidite());
        documentData.addData("entete_affilie_label",
                this.getText("al.decision.standard.entete.affilie", langueDocument));
        documentData.addData("entete_affilie_num", dossierComplexModel.getDossierModel().getNumeroAffilie());
        documentData.addData("entete_loi_label", this.getText("al.decision.entete.loi", langueDocument));
        if (JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                ALCSDossier.ACTIVITE_AGRICULTEUR, false)
                || JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false)
                || JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false)
                || JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE, false)
                || JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_PECHEUR, false)) {
            documentData.addData("entete_loi_valeur", this.getText("al.decision.entete.loi.agricole", langueDocument));
        } else {
            String loi = getLoi(dossierComplexModel, assInfo, dateImpression);

            // documentData.addData("entete_loi_valeur", this.getText("al.decision.entete.loi.jura", langueDocument));
            documentData.addData("entete_loi_valeur", loi);
        }

        // récupération du libellé de texte pour la référecence
        getTexteLibelleReference(documentData, langueDocument);

        // récupération du visa de l'utilisateur ou du responsable du dossier
        getDossierReference(documentData, dossierComplexModel, userInfos, langueDocument);

        loadTextConcerne(documentData, dossierComplexModel, langueDocument);

        documentData.addData("entete_reference_label_allocataire",
                this.getText("al.decision.standard.entete.allocataire", langueDocument));
        documentData.addData("entete_reference_nom_allocataire", dossierComplexModel.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                + " "
                + dossierComplexModel.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                        .getDesignation2());
        documentData.addData("entete_reference_label_nss",
                this.getText("al.decision.standard.entete.num.nss", langueDocument));
        documentData.addData("entete_reference_num_nss", dossierComplexModel.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
        documentData.addData("entete_reference_label_dossier",
                this.getText("al.decision.standard.entete.num.dossier", langueDocument));
        documentData.addData("entete_reference_num_dossier", dossierComplexModel.getDossierModel().getIdDossier());
    }

    /**
     * Charge les données concernant les textes standards dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Dossier de l'allocataire
     * @param commentaire
     *            Texte libre de l'utilisateur
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     */
    protected void loadTextesDecision(DocumentData documentData, DossierComplexModel dossierComplexModel,
            String commentaire, String langueDocument) throws JadeApplicationException, JadePersistenceException {
        if (!JadeStringUtil.isBlank(commentaire)) {
            documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
        }

        if (JadeStringUtil.equals(dossierComplexModel.getDossierModel().getStatut(), ALCSDossier.STATUT_IS, false)) {

            documentData.addData("texte_paragraphe_adi",
                    this.getText("al.decision.standard.dossier.adi", langueDocument));
        }
        if (JadeStringUtil.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire(),
                ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false)) {
            documentData.addData("texte_para_trav_agri",
                    this.getText("al.decision.standard.travailleurAgri.verseAlloc", langueDocument));
        }

        // INFOROMD0028 - AF - Modifications montants AF VD (CBU)
        if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS, dossierComplexModel.getDossierModel()
                .getTarifForce(), false)) {
            documentData.addData("texte_paragraphe_droitAcquis",
                    this.getText("al.decision.standard.paragraphe.droitAcquis", langueDocument));
        }
        // FIN INFOROMD0028

        documentData.addData("texte_paragraphe_1", this.getText("al.decision.standard.paragraphe1", langueDocument)
                + " " + this.getText("al.decision.standard.paragraphe2", langueDocument));
        // documentData.addData("texte_paragraphe_2", this
        // .getText("al.decision.standard.paragraphe2"));

        documentData.addData("texte_paragraphe_3", this.getText("al.decision.standard.paragraphe3", langueDocument));

    }

    /**
     * Charge les copies liées au dossier passé en paramètre
     * 
     * @param dossier
     *            Dossier pour lequel chercher les copies
     * @return Résultat de la recherche des copies
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private CopieComplexSearchModel searchCopies(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        CopieComplexSearchModel copieComplexSearchModel = new CopieComplexSearchModel();
        copieComplexSearchModel.setForIdDossier(dossier.getId());
        copieComplexSearchModel.setForTypeCopie(ALCSCopie.TYPE_DECISION);
        return ALServiceLocator.getCopieComplexModelService().search(copieComplexSearchModel);
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("DecisionAbstractServiceImpl#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_decisionProcess");
    }

}
