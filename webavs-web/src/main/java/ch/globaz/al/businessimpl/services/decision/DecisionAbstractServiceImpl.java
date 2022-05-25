package ch.globaz.al.businessimpl.services.decision;

import ch.globaz.al.business.models.dossier.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class racine qui permet de remplir les donn�es n�cessaires � la g�n�ration des documents de d�cisions
 * 
 * @author JER/PTA/JTS
 */
public abstract class DecisionAbstractServiceImpl extends AbstractDocument implements DecisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionAbstractServiceImpl.class);

    /**
     * Indique si la page d'accompagnement d'une copie doit �tre g�n�r�e
     * 
     * @param dossier
     *            dossier pour lequel g�n�r� la page d'accompagnement
     * @param idTiersDestinataire
     *            id du tiers destinataire de la copie
     * @return <code>true</code> si la page d'accompagnement doit �tre g�n�r�e, <code>false</code> sinon
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier
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
     * M�thode qui retourne une cha�ne de caract�res avec l'adresse de la banque
     * 
     * @param adress
     *            adresse de paiement
     * @return String valeur du ccp ou donn�es bancaires
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private String getAdressePaiement(AdresseTiersDetail adress, String langueDocument) throws JadeApplicationException {
        String adressePaiement = null;
        // contr�le des param�tres
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

            // Rue, numero, Num�ro postal et localit�
            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) + "\n"
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

        }

        return adressePaiement;
    }

    /**
     * M�thode qui retourne une cha�ne de caract�res avec l'adresse de la banque
     * 
     * @param adress
     *            adresse de paiement
     * @return String valeur du ccp ou donn�es bancaires
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private String getCompteDroitBeneficiaire(AdresseTiersDetail adress, String langueDocument)
            throws JadeApplicationException {
        String adressePaiement = null;
        // contr�le du param�tre
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
            // si l'adresse de paiement est identique � l'adresse du tiers
            // b�n�ficiaire

            adressePaiement = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);

        }

        return adressePaiement;
    }

    /**
     * Retourne la date � utiliser pour le calcul de la d�cision selon le dossier
     * 
     * @param dossierComplexModel
     *            dossier
     * @return la date � utiliser pour le calcul dans la d�cision
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private final String getDateCalcul(DossierComplexModel dossierComplexModel) throws JadeApplicationException {

        if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
            return dossierComplexModel.getDossierModel().getFinValidite();

        } else {
            return dossierComplexModel.getDossierModel().getDebutValidite();
        }
    }

    /**
     * M�thode qui remplit la r�f�rence voulu
     * 
     * @param documentData
     *            Document � remplir
     * @param dossierComplexModel
     * @param userInfos
     *            infromations d'utilisateur
     * @param langueDocument : la langue du document
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private void getDossierReference(DocumentData documentData, DossierComplexModel dossierComplexModel,
            Map<String, String> userInfos, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // contr�le des param�tre
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
     * Effectue une requ�te afin de r�cup�rer tous les droits contenus dans un dossier
     * 
     * @param idDossier
     *            id du dossier � rechercher
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
     * M�thode qui retourne le nom, pr�nom et/ou la localit�, selon les cas d'un tiers b�n�ficiaire
     * 
     * @param adress
     *            d�tail de l'adresse du tiers b�n�ficiaire
     * @return le texte "en faveur de..."
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private String getFaveurDe(AdresseTiersDetail adress) throws JadeApplicationException {
        String enFaveurDe = null;
        // contr�le du param�tre
        if (adress == null) {
            throw new ALDecisionException("DecisionAbsractServiceImpl#getFaveurDe: adress is null");
        }
        // si il n'y a pas d'adresse ccp ou banque en retourne le nom pr�nom
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
     * retourne une hashmap contenant comme cl� activit� de l'allocataire et valeur: libell� de cette activit�
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
     * R�cup�re la loi selon l'algorithme suivant :
     * </p>
     * <li>
     * Si un tarif est forc� sur un des droits du dossier, alors on utilise ce droit</li> <li>
     * Sinon si le tarif est forc� sur le dossier � {@link ALCSTarif#CATEGORIE_VD_DROIT_ACQUIS}, alors on utilise le
     * libelle "Droits Acquis Vaud"</li> <li>
     * Sinon si le tarif est forc� sur le dossier, on r�cup�re le code raccourci du code syst�me (par ex : "VS" pour
     * "Valais")</li> <li>
     * Sinon on prend le canton de l'affili�</li>
     * 
     * @param dossierComplexModel
     *            Le dossier de la d�cision
     * @param assInfo
     *            Informations quant � l'assurance
     * @return String : loi appliqu� sur la d�cision
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    protected String getLoi(DossierComplexModel dossierComplexModel, AssuranceInfo assInfo, String dateImpression)
            throws JadeApplicationException, JadeApplicationServiceNotAvailableException {
        // INFOROMD0028, on affiche la loi "Droits Acquis Vaud" si ce tarif est forc�

        // Recherche des tarifs forc�s dans les droits
        String loi = getLoiDroits(dossierComplexModel, dateImpression);

        if (!TarifAggregator.TARIF_UNDEFINED.equals(loi) && !"0".equals(loi)) {
            if (!TarifAggregator.TARIF_MULTIPLE.equals(loi)) {
                loi = JadeCodesSystemsUtil.getCode(loi);
            } else {
                // Des valeurs diff�rentes sont fix�es sur les droits, de ce fait, on affiche une �toile sur le
                // document.
            }
        } else if (ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS.equals(dossierComplexModel.getDossierModel().getTarifForce()) ||
                   ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS_2022.equals(dossierComplexModel.getDossierModel().getTarifForce())) {
            // INFOROMD0028 - AF - Modifications montants AF VD (CBU)
            // S211020_004 - AF - Modifications montants AF VD 2022 (JJO)
            loi = JadeCodesSystemsUtil.getCodeLibelle(dossierComplexModel.getDossierModel().getTarifForce());
            // FIN INFOROMD0028
            // FIN S211020_004 - AF - Modifications montants AF VD 2022 (JJO)
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
     * @return La loi s'appliquant au dossier, ou "*" (�toile) si plusieurs lois sont appliqu�es.
     */
    private String getLoiDroits(DossierComplexModel dossierComplexModel, String dateImpression) {
        return TarifAggregator.calculerTarif(getTarifs(dossierComplexModel.getId(), dateImpression));
    }

    /**
     * Retourne une liste contenant les tarifs des droits contenus dans un dossier
     * 
     * @param idDossier
     *            id du dossier � rechercher
     * @return Liste des codes syst�mes repr�sentant les cantons. La valeur 0 correspond � l'absence d'un code syst�me,
     *         et donc le fait que le tarif n'ai pas �t� forc�
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
     * Texte du libell� pour la r�f�rence
     * 
     * @param documentData
     *            document � cr�er
     * @param langueDocument
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void getTexteLibelleReference(DocumentData documentData, String langueDocument)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        // contr�le des param�tres
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
     * Charge une ArrayList avec les donn�es des champs copies de l'�cran de d�cision
     * 
     * @param dossier
     *            Le dossier li� aux copies
     * @param isPaiementDirect
     *            D�fini si on est en paiement direct
     * @param copies
     *            Liste des copies
     * @return Liste des textes de copie
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * M�thode qui retourne true si tous les b�n�ficiaires des droits sont diff�rents du b�n�ficiaire du dossier et
     * false dans tous les autres cas
     * 
     * @param listTiersBeneficiaireDroit
     *            listes des tiersB�n�ficiaires des diff�rents droits
     * @param idBeneficiaireDossier
     *            tiers b�n�ficiaire du dossier
     * @return boolean
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected boolean isNotAllDroitBenEgalBenDos(List<String> listTiersBeneficiaireDroit,
            String idBeneficiaireDossier) throws JadeApplicationException {
        // contr�le de param�tres
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
     * Retourne le commentaire � afficher sur la d�cision selon les r�gles suivantes :
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
     *            Le dossier li� au commentaire
     * @return Un CommentaireModel rempli
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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

        // gestion texte libre activ�e : oui
        if (gestionTexteLibre) {
            // texte libre renseign� : non
            if (JadeStringUtil.isBlank(texteLibre)) {

                // text dossier renseign� : non
                if (JadeStringUtil.isBlank(texteDossier)) {
                    return "";
                    // text dossier renseign� : oui
                } else {
                    return texteDossier;
                }

                // texte libre renseign� : non
            } else {
                // text dossier renseign� : non
                if (JadeStringUtil.isBlank(texteDossier)) {
                    return texteLibre;
                    // text dossier renseign� : oui
                } else {
                    return texteLibre + System.getProperty("line.separator") + texteDossier;
                }
            }
            // gestion texte libre activ�e : non
        } else {
            // texte libre renseign� : non
            if (JadeStringUtil.isBlank(texteLibre)) {
                return "";

                // texte libre renseign� : non
            } else {
                return texteLibre;
            }
        }
    }

    /**
     * D�clenche la collecte des datas pour la cr�ation des d�cisions
     * 
     * @param dossier
     *            Le dossier de la d�cision en cours
     * @param copies
     *            Liste des copies
     * @param dateImpression
     *            Date d'impression � afficher sur la d�cision
     * @return D�cision
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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

        List<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService().getCalcul(dossier,
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

        // contr�le des param�tres
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
        // la gestion des copie signifie prendre les donn�es de copie en DB
        if (gestionCopie) {
            CopieComplexSearchModel search = searchCopies(dossier);
            for (int i = 0; i < search.getSearchResults().length; i++) {
                listeCopies.add((CopieComplexModel) search.getSearchResults()[i]);
            }
            // sinon on prend le comportement par d�faut (comme � la creation)
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
                        langueDocument, dateImpression).getDocument());
            }
            // TODO faire appel � m�thode avec �galement num�ro d'affili�e si le tiers destinataire correspond au tiers
            // affili�
            // ajout de l'adresse sur le document

            // si idTiersCopie idnetique � idTiersnumAffilie reprendre avec addDateAdresse avec numAffilie sinon � null

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
     * M�thode qui charge le donn�es li�es � un b�n�ficiaire d'un droit
     *
     * @param droitModel
     * @param droitBeneficiaire
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void loadDroitTiersBeneficiaire(DroitComplexModel droitModel, DataList droitBeneficiaire,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // recherche de l'adresse de paiement du tiers b�n�ficiaire
        // FIXME: CS_DOMAINE_AF mais il faut que service g�re la
        // cascade...
        AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                droitModel.getDroitModel().getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                JadeDateUtil.getGlobazFormattedDate(new Date()), "");

        String adressePaiement = getCompteDroitBeneficiaire(adress, langueDocument);
        String enFaveurDe = adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);

        // si le droit n'est pas m�nage
        if (!JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)
                && !JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LFA, false)
                && !JadeStringUtil.equals(droitModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN_LJA, false)) {
            // le tiers b�n�ficiaire correspond au tiers du droit
            if (JadeStringUtil.equals(droitModel.getDroitModel().getIdTiersBeneficiaire(), droitModel
                    .getEnfantComplexModel().getEnfantModel().getIdTiersEnfant(), false)) {

                // label adresse de paiement
                droitBeneficiaire.addData("droit_beneficiaire",
                        this.getText("al.decision.versementDirect.info.droitBeneficiaire", langueDocument) + " "
                                + adressePaiement);

            }

            // si le tiers du droit ne correspond pas au tiers b�n�ficiaire et
            // que le tiers b�n�ficiaire est autre que 0
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
     * M�thode qui charge les donn�es de versement li�e � un paiement direct
     * 
     * @param documentData
     *            DocumentData � charger
     * @param dossierModel
     *            les donn�es du dossier
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void loadInfoVersementDirect(DocumentData documentData, DossierComplexModel dossierModel,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {
        // v�rification des param�tres
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

        // recherche de l'adresse de paiement du tiers b�n�ficiaire
        // FIXME: CS_DOMAINE_AF mais il faut que service g�re la
        // cascade...
        AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                dossierModel.getDossierModel().getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                JadeDateUtil.getGlobazFormattedDate(new Date()), "");
        // this.loadAdressePaiement(documentData, adress, dossierModel);
        String adressePaiement = getAdressePaiement(adress, langueDocument);
        String enFaveurDe = getFaveurDe(adress);

        // si le tiers b�n�ficiaire correspond au tiers allocataire
        if (JadeStringUtil.equals(dossierModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire(), dossierModel.getDossierModel().getIdTiersBeneficiaire(), false)) {

            // label adresse de paiement
            documentData.addData("adresse_paiement",
                    this.getText("al.decision.versementDirect.info.adressePaiement.label", langueDocument));

            // valeur adresse de paiement
            documentData.addData("adresse_paiement_valeur", adressePaiement);
        }

        // si le tiers allocataire ne correspond pas au tiers b�n�ficiaire et
        // que le tiers b�n�ficiaire est autre que 0
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
     * Charge les donn�es de la liste des droits AF dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossier
     *            Le dossier de la d�cision
     * @param calcul
     *            Le calcul des droits issus du dossier
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected abstract void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
            List<CalculBusinessModel> calcul, String date, String langueDocument) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Charge les donn�es concernant les allocations de naissance dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossier
     *            Le dossier de la d�cision
     * @param calcul
     *            Le calcul des droits issus du dossier
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier
     */
    protected abstract void loadListNaissance(DocumentData documentData, DossierComplexModel dossier,
            List<CalculBusinessModel> calcul, String dateDossierCalcul, String langueAffilie)
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
    protected Collection loadMontantJoursDebut(DossierComplexModel dossier, List<CalculBusinessModel> calcul,
                                               Map total, String date, DataList list, Collection tableau_sous_total, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        if (!JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getNbJoursDebut())) {
            String dateDebut = dossier.getDossierModel().getDebutValidite();
            String dateFin = ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite());

            // si m�me mois debut et fin, date fin = date de fin
            // r�elle
            if (!JadeStringUtil.isEmpty(dossier.getDossierModel().getFinValidite())
                    && ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite()).equals(
                            ALDateUtils.getDateFinMois(dossier.getDossierModel().getFinValidite()))) {
                dateFin = dossier.getDossierModel().getFinValidite();
            }

            total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
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

    protected Collection loadMontantJoursFin(DossierComplexModel dossier, List<CalculBusinessModel> calcul,
            Map total, String date, DataList list, Collection tableau_sous_total, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        if (!JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getNbJoursFin())) {

            String dateDebut = ALDateUtils.getDateDebutMois(dossier.getDossierModel().getFinValidite());
            String dateFin = dossier.getDossierModel().getFinValidite();

            // si m�me mois debut et fin, date d�but = date de d�but
            // r�elle
            if (!JadeStringUtil.isEmpty(dossier.getDossierModel().getDebutValidite())
                    && ALDateUtils.getDateFinMois(dossier.getDossierModel().getDebutValidite()).equals(
                            ALDateUtils.getDateFinMois(dossier.getDossierModel().getFinValidite()))) {
                dateDebut = dossier.getDossierModel().getDebutValidite();
            }

            String UniteEffectif = JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF), true,
                    true, false, 2);

            // calcul du montant en fonction du nombre de jour pour le d�but de
            // la p�riode
            total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
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
     * charge le texte "concerne" en fonction du dossier (type d'allocataire, d�but / fin validit�)
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Le dossier de la d�cision
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * Charge les donn�es concernant les copies dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param listCopies
     *            Liste des textes de copie du document
     * @param dossierComplexModel
     *            Dossier de l'allocataire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void loadTextCopies(DocumentData documentData, List<String> listCopies,
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
     * Charge les donn�es concernant les textes d'ent�tes de document dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Le dossier de la d�cision
     * @param dateImpression
     *            La date d'impression du document
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     */
    protected void loadTextEntete(DocumentData documentData, DossierComplexModel dossierComplexModel,
            Map<String, String> userInfos, String langueDocument, String dateImpression)
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

        // r�cup�ration du libell� de texte pour la r�f�recence
        getTexteLibelleReference(documentData, langueDocument);

        // r�cup�ration du visa de l'utilisateur ou du responsable du dossier
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
     * Charge les donn�es concernant les textes standards dans le documentData
     * 
     * @param documentData
     *            Le documentData du document en cours
     * @param dossierComplexModel
     *            Dossier de l'allocataire
     * @param commentaire
     *            Texte libre de l'utilisateur
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
        // S211020_004 - AF - Modifications montants AF VD 2022 (JJO)
        } else if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS_2022, dossierComplexModel.getDossierModel()
                .getTarifForce(), false)) {
            documentData.addData("texte_paragraphe_droitAcquis",
                    this.getText("al.decision.standard.paragraphe.droitAcquis2022", langueDocument));
        }
        // FIN INFOROMD0028
        // FIN S211020_004 - AF - Modifications montants AF VD 2022 (JJO)

        documentData.addData("texte_paragraphe_1", this.getText("al.decision.standard.paragraphe1", langueDocument)
                + " " + this.getText("al.decision.standard.paragraphe2", langueDocument));
        // documentData.addData("texte_paragraphe_2", this
        // .getText("al.decision.standard.paragraphe2"));

        if (ALCSDossier.ACTIVITE_SALARIE.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire())) {
            documentData.addData("texte_paragraphe_4", generateParagraphe4(langueDocument, dossierComplexModel.getDossierModel()));
        }

        documentData.addData("texte_paragraphe_3", this.getText("al.decision.standard.paragraphe3", langueDocument));

    }

    private String generateParagraphe4(String langueDocument, DossierModel dossierModel) {
        try {
            String dateDebutValidite = dossierModel.getDebutValidite();
            if (!JadeDateUtil.isGlobazDate(dateDebutValidite)) {
                LOGGER.warn("Impossible de trouver la date de d�but de validit� de la d�cision, le paragraphe ne sera pas g�n�r� dans la d�cision.");
                return null;
            }

            BSession session = BSessionUtil.getSessionFromThreadContext();
            String SALBMINMO = JANumberFormatter.formatNoRound(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "SALBMINMO", dateDebutValidite, "", 0));
            String SALBMINAN = JANumberFormatter.formatNoRound(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "SALBMINAN", dateDebutValidite, "", 0));
            String REVENMAXMO = JANumberFormatter.formatNoRound(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "REVENMAXMO", dateDebutValidite, "", 0));
            String REVEFMAXAN = JANumberFormatter.formatNoRound(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "REVEFMAXAN", dateDebutValidite, "", 0));

            String paragraphe4 = this.getText("al.decision.standard.paragraphe4", langueDocument);
            paragraphe4 = paragraphe4.replace("{SALBMINMO}", SALBMINMO);
            paragraphe4 = paragraphe4.replace("{SALBMINAN}", SALBMINAN);
            paragraphe4 = paragraphe4.replace("{REVENMAXMO}", REVENMAXMO);
            paragraphe4 = paragraphe4.replace("{REVEFMAXAN}", REVEFMAXAN);
            return paragraphe4;

        } catch (Exception e) {
            LOGGER.error("Une erreur est intervenue lors de la recherche des plages de valeurs n�cessaire au paragraphe de la d�cision.", e);
            return null;
        }
    }

    /**
     * Charge les copies li�es au dossier pass� en param�tre
     * 
     * @param dossier
     *            Dossier pour lequel chercher les copies
     * @return R�sultat de la recherche des copies
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
