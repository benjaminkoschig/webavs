package ch.globaz.al.businessimpl.services.echeances;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.exceptions.droitEcheance.ALDroitEcheanceException;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Implémentation des services liées à l'échéance des droits
 * 
 * @author PTA
 * 
 */
public class DroitEcheanceServiceImpl extends ALAbstractBusinessServiceImpl implements DroitEcheanceService {

    @Override
    public String getLibelleMotif(DroitComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres
        if (droit == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl# getLibelleMotif: droit is null");

        }

        String dateNaissance = droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                .getDateNaissance();
        // la date de naissance peut être vide (cas d'un droit ménage)
        if (!JadeStringUtil.isBlankOrZero(dateNaissance) && !JadeDateUtil.isGlobazDate(dateNaissance)) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl# getLibelleMotif: " + dateNaissance
                    + " is not a valid globaz's date");
        }

        JadeI18n i18n = JadeI18n.getInstance();

        // traitement motif Echéance ménage
        if (droit.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_MEN)) {
            return i18n.getMessage(langue, "al.documentCommun.echeances.motifMenage");
        }

        // calcul de l'âge à l'échéance pour les enfants
        else if (droit.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_ENF)) {

            return getLibellePourDroitEnfant(droit, langue, dateNaissance, i18n);
        }

        // les droits type formation
        else if (droit.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_FORM)) {

            return getLibellePourDroitFormation(droit, langue, dateNaissance, i18n);

        } else {
            throw new ALDroitEcheanceException(
                    "DroitEcheanceServiceImpl#getLibelleMotif : impossible de déterminer un libellé pour un droit de type "
                            + droit.getDroitModel().getTypeDroit());
        }
    }

    @Override
    public String getLibelleMotif(DroitEcheanceComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres
        if (droit == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#getLibelleMotif: droit is null");
        }

        DroitComplexModel droitComplexModel = ALServiceLocator.getDroitComplexModelService().read(
                droit.getDroitModel().getIdDroit());

        return this.getLibelleMotif(droitComplexModel, langue);

    }

    private String getLibellePourDroitEnfant(DroitComplexModel droit, String langue, String dateNaissance, JadeI18n i18n)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        String ageEnfant = ALImplServiceLocator.getDatesEcheancePrivateService().getAgeEnfant(dateNaissance,
                droit.getDroitModel().getFinDroitForcee());
        // changement de tarif
        if (droit.getDroitModel().getMotifFin().equals(ALCSDroit.MOTIF_FIN_CTAR)) {
            return (i18n.getMessage(langue, "al.documentCommun.echeances.motifFinChgmt") + " " + ageEnfant + " " + i18n
                    .getMessage(langue, "al.documentCommun.echeances.motifAge"));

            // radiation
        } else if (droit.getDroitModel().getMotifFin().equals(ALCSDroit.MOTIF_FIN_RAD)) {
            return (i18n.getMessage(langue, "al.documentCommun.echeances.fin.radiation"));
        }

        // les autres cas enfant
        else {
            if (droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && "16".equals(ageEnfant)) {
                return ageEnfant + " " + i18n.getMessage(langue, "al.documentCommun.echeances.motifAge");
            }

            if (!droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && "20".equals(ageEnfant)) {
                return ageEnfant + " " + i18n.getMessage(langue, "al.documentCommun.echeances.motifAge");
            }

            return i18n.getMessage(langue, "al.documentCommun.echeances.motifEchu");

        }
    }

    private String getLibellePourDroitFormation(DroitComplexModel droit, String langue, String dateNaissance,
            JadeI18n i18n) throws JadeApplicationException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        String ageEnfant = ALImplServiceLocator.getDatesEcheancePrivateService().getAgeEnfant(dateNaissance,
                droit.getDroitModel().getFinDroitForcee());

        // changement de tarif
        if (droit.getDroitModel().getMotifFin().equals(ALCSDroit.MOTIF_FIN_CTAR)) {

            if ("25".equals(ageEnfant)) {
                return i18n.getMessage(langue, "al.documentCommun.echeances.motifFin25ans");
            }

            return (i18n.getMessage(langue, "al.documentCommun.echeances.motifFinChgmt") + " " + ageEnfant + " " + i18n
                    .getMessage(langue, "al.documentCommun.echeances.motifAge"));

            // radiation
        } else if (droit.getDroitModel().getMotifFin().equals(ALCSDroit.MOTIF_FIN_RAD)) {
            return (i18n.getMessage(langue, "al.documentCommun.echeances.fin.radiation"));
        }

        // les autres cas enfant
        else {

            if ("25".equals(ageEnfant)) {
                return i18n.getMessage(langue, "al.documentCommun.echeances.motifFin25ans");
            }

            return i18n.getMessage(langue, "al.documentCommun.echeances.motifFinEt");

        }

    }

    /**
     * Méthode quei retourne les droits unique
     * 
     * @param droitsEcheanceSearchModel
     *            résultats de la recherche
     * @return listDroitsEcheances
     * @throws JadeApplicationException
     */
    private ArrayList<DroitEcheanceComplexModel> getListDroitsUnique(
            DroitEcheanceComplexSearchModel droitsEcheanceSearchModel) throws JadeApplicationException {
        // contrôle paramètre
        if (droitsEcheanceSearchModel == null) {
            throw new ALEcheanceModelException(
                    "ALEcheancesImprimerProcess# getListDroitsUnique: droitsEcheancesSearchModel is null");
        }
        ArrayList<DroitEcheanceComplexModel> listDroitsEcheance = new ArrayList<DroitEcheanceComplexModel>();
        if (droitsEcheanceSearchModel.getSize() > 0) {
            String idDroit = "";

            // élémine les doublons au niveau du droit
            for (int i = 0; i < droitsEcheanceSearchModel.getSize(); i++) {
                DroitEcheanceComplexModel droitModel = ((DroitEcheanceComplexModel) droitsEcheanceSearchModel
                        .getSearchResults()[i]);

                if (!JadeStringUtil.equals(idDroit, droitModel.getDroitModel().getIdDroit(), false)) {
                    listDroitsEcheance.add(droitModel);
                }
                idDroit = droitModel.getDroitModel().getIdDroit();
            }
        }

        return listDroitsEcheance;
    }

    private HashSet getListEtatDroit() throws JadeApplicationException, JadePersistenceException {
        HashSet etatDroit = new HashSet();
        etatDroit.add(ALCSDroit.ETAT_S);
        etatDroit.add(ALCSDroit.ETAT_G);

        return etatDroit;
    }

    @Override
    public HashSet getListMotifsAutres() throws JadeApplicationException, JadePersistenceException {
        HashSet avisEcheance = new HashSet();

        // type de fin de droit pris en compte pour les dossiers à réviser
        avisEcheance.add(ALCSDroit.MOTIF_FIN_CTAR);
        // avisEcheance.add(ALCSDroit.MOTIF_FIN_RAD);

        return avisEcheance;
    }

    @Override
    public HashSet getListMotifsAvis() throws JadeApplicationException, JadePersistenceException {
        HashSet autreEcheance = new HashSet();

        // type de fin de droit pris en compte pour les avis d'échéances
        autreEcheance.add(ALCSDroit.MOTIF_FIN_ECH);

        return autreEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.echeances.DroitEcheanceService# updateDroitImprimerEcheance
     * (ch.globaz.al.business.models.droit.DroitEcheanceComplexSearchModel)
     */
    @Override
    public HashSet getListTypeDroit() throws JadeApplicationException, JadePersistenceException {
        HashSet typeDroit = new HashSet();

        // type de droit pris en compte pour la recherche
        typeDroit.add(ALCSDroit.TYPE_ENF);
        typeDroit.add(ALCSDroit.TYPE_FORM);
        // typeDroit.add(ALCSDroit.TYPE_MEN);

        return typeDroit;

    }

    @Override
    public boolean getTypePaiement(DroitEcheanceComplexModel droitEcheance) throws JadeApplicationException,
            JadePersistenceException {

        boolean direct;

        // contrôle du paramètre
        if (droitEcheance == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#getTypePaiement:  droitEcheance is null");

        }

        if (JadeStringUtil.isBlankOrZero(droitEcheance.getIdTiersBeneficiaire())) {
            direct = false;

        } else {
            direct = true;
        }
        return direct;
    }

    @Override
    public ArrayList<DroitEcheanceComplexModel> searchDroitsForEcheance(HashSet motifFin, HashSet typeDroit,
            String dateEcheance, String typeBonification, String typeListe, Boolean adiDossier)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres
        if (motifFin == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#searchDroitsForEcheance: motifFin is null");

        }

        if (typeDroit == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#searchDroitsForEcheance: typeDroit is null");

        }

        if (!JadeDateUtil.isGlobazDate(dateEcheance)) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#searchDroitsForEcheance: " + dateEcheance
                    + " is not a globaz date valid");
        }
        // type de bonification
        if (!JadeStringUtil.equals(typeBonification, ALConstEcheances.TYPE_DIRECT, false)
                && !JadeStringUtil.equals(typeBonification, ALConstEcheances.TYPE_INDIRECT, false)
                && !JadeStringUtil.equals(typeBonification, ALConstEcheances.TYPE_ALL, false)) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#searchDroitForEcheance: typeBonification : "
                    + typeBonification + " is not valid");
        }
        // type de liste
        if (!JadeStringUtil.equals(typeListe, ALConstEcheances.LISTE_AUTRES_ECHEANCES, false)
                && !JadeStringUtil.equals(typeListe, ALConstEcheances.LISTE_AVIS_ECHEANCES, false)) {

            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#searchDroitForEcheance: typeListe : "
                    + typeListe + " is not valid");

        }
        DroitEcheanceComplexSearchModel droitEcheanceSearchDef = new DroitEcheanceComplexSearchModel();
        droitEcheanceSearchDef.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        ArrayList tempListDroit = new ArrayList();

        // effectue la recherche
        DroitEcheanceComplexSearchModel droitEcheanceSearch = new DroitEcheanceComplexSearchModel();

        droitEcheanceSearch.setForImprimerEcheance(Boolean.TRUE);
        droitEcheanceSearch.setInEtatDroit(getListEtatDroit());

        droitEcheanceSearch.setForFinDroitForce(dateEcheance);
        droitEcheanceSearch.setInMotifFin(motifFin);
        droitEcheanceSearch.setForEtatDossier(ALCSDossier.ETAT_ACTIF);
        // droitEcheanceSearch.setForIdTiersBenificiaire("0");
        droitEcheanceSearch.setInTypeDroit(typeDroit);
        droitEcheanceSearch.setForMotifFin(ALCSDroit.MOTIF_FIN_CTAR);
        droitEcheanceSearch.setForMotifFinEch(ALCSDroit.MOTIF_FIN_ECH);

        droitEcheanceSearch.setForTypeDroitEnfant(ALCSDroit.TYPE_ENF);
        droitEcheanceSearch.setForTypeDroitForm(ALCSDroit.TYPE_FORM);
        // statut du dossier (pas prendre en compte les internatinaux supplétif)selon Adi ou nom
        // FIXME : à corriger, comparaison == Boolean.TRUE/FALSE à éviter
        if (adiDossier == Boolean.TRUE) {
            droitEcheanceSearch.setForNotStatuDossier(ALCSDossier.STATUT_IS);
        }

        if (ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
            droitEcheanceSearch.setForTypeLiaison(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);
        }

        if (typeBonification.equals(ALConstEcheances.TYPE_DIRECT)) {
            droitEcheanceSearch.setForPaiementDirect("0");
        } else if (typeBonification.equals(ALConstEcheances.TYPE_INDIRECT)) {
            droitEcheanceSearch.setForPaiementIndirect("0");
        } else if (typeBonification.equals(ALConstEcheances.TYPE_ALL)) {
            droitEcheanceSearch.setForPaiementDirect(null);
            droitEcheanceSearch.setForPaiementIndirect(null);
        }

        droitEcheanceSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        // liste définitive de la recherche
        if (typeListe.equals(ALConstEcheances.LISTE_AUTRES_ECHEANCES)) {
            droitEcheanceSearch.setWhereKey("listeAutresEcheances");

            // effectue la recherche
            droitEcheanceSearch = ALServiceLocator.getDroitEcheanceComplexModelService().search(droitEcheanceSearch);

            // boucle sur le résultat de la recherche
            for (int i = 0; i < droitEcheanceSearch.getSize(); i++) {
                JadeAbstractModel[] listeDroits = droitEcheanceSearch.getSearchResults();
                DroitEcheanceComplexModel model = ((DroitEcheanceComplexModel) listeDroits[i]);

                // ajouter 25 ans à la date de naissance
                String dateNaissancePlus25Ans = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addAnneesDate(25,
                        ALDateUtils.getCalendarDate(model.getDateNaissanceEnfant())).getTime());

                // vérification que la date de naissance + 25 soit antérieure ou
                // égale à
                // la date de de fin de droit
                if (JadeDateUtil.isDateBefore(dateNaissancePlus25Ans, model.getDroitModel().getFinDroitForcee())
                        || JadeStringUtil.equals(dateNaissancePlus25Ans, model.getDroitModel().getFinDroitForcee(),
                                false)) {

                    // ajouter le droit à la liste provisoire
                    tempListDroit.add(model);

                }

            }

            // initialisation un tableau abstrait à la grandeur de la liste
            // provisoire
            JadeAbstractModel[] newResults = new JadeAbstractModel[tempListDroit.size()];

            Iterator iter = tempListDroit.iterator();
            // iterateur pour le tableau abstrait
            int j = 0;
            // boucle pour la liste des droits
            while (iter.hasNext()) {
                // traitement de l'allocataire
                DroitEcheanceComplexModel droitsEcheance = (DroitEcheanceComplexModel) iter.next();

                // ajouit de chaque droit au tableau abstrait
                newResults[j] = droitsEcheance;
                j++;

            }
            droitEcheanceSearchDef.setSearchResults(newResults);
            droitEcheanceSearchDef.setForFinDroitForce(dateEcheance);

        } else if (typeListe.equals(ALConstEcheances.LISTE_AVIS_ECHEANCES)) {
            droitEcheanceSearch.setWhereKey("listeAvisEcheances");

            // effectue la recherche
            droitEcheanceSearchDef = ALServiceLocator.getDroitEcheanceComplexModelService().search(droitEcheanceSearch);

            // boucle sur le résultat de la recherche
            for (int i = 0; i < droitEcheanceSearch.getSize(); i++) {
                JadeAbstractModel[] listeDroits = droitEcheanceSearch.getSearchResults();
                DroitEcheanceComplexModel model = ((DroitEcheanceComplexModel) listeDroits[i]);

                String dateNaissancePlus25Ans = null;
                // ajouter 25 ans à la date de naissance si date naissance pas
                // vide (droit ménage pas date naissance)
                if (!JadeStringUtil.isBlankOrZero(model.getDateNaissanceEnfant())) {
                    dateNaissancePlus25Ans = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addAnneesDate(25,
                            ALDateUtils.getCalendarDate(model.getDateNaissanceEnfant())).getTime());
                }
                // vérification que la date de naissance + 25 soit postérieure à
                // la date de de fin de droit
                if (JadeDateUtil.isDateAfter(dateNaissancePlus25Ans, model.getDroitModel().getFinDroitForcee())
                        || JadeStringUtil.isBlankOrZero(model.getDateNaissanceEnfant())) {

                    // ajouter le droit à la liste provisoire
                    tempListDroit.add(model);

                }

            }

            // initialisation un tableau abstrait à la grandeur de la liste
            // provisoire
            JadeAbstractModel[] newResults = new JadeAbstractModel[tempListDroit.size()];

            Iterator iter = tempListDroit.iterator();
            // iterateur pour le tableau abstrait
            int j = 0;
            // boucle pour la liste des droits
            while (iter.hasNext()) {
                // traitement de l'allocataire
                DroitEcheanceComplexModel droitsEcheance = (DroitEcheanceComplexModel) iter.next();

                // ajouit de chaque droit au tableau abstrait
                newResults[j] = droitsEcheance;
                j++;

            }
            droitEcheanceSearchDef.setSearchResults(newResults);
            droitEcheanceSearchDef.setForFinDroitForce(dateEcheance);

        }
        ArrayList<DroitEcheanceComplexModel> listEcheanceDroits = new ArrayList<DroitEcheanceComplexModel>();
        listEcheanceDroits = getListDroitsUnique(droitEcheanceSearchDef);

        return listEcheanceDroits;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.echeances.DroitEcheanceService# updateDroitImprimerEcheance
     * (ch.globaz.al.business.models.droit.DroitEcheanceComplexSearchModel)
     */
    @Override
    public void updateDroitImprimerEcheance(ArrayList<DroitEcheanceComplexModel> droits, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle du paramètres
        if (droits == null) {
            throw new ALDroitEcheanceException("DroitEcheanceServiceImpl#updateDroitImprimerEcheance: droits is null");
        }

        for (int i = 0; i < droits.size(); i++) {

            DroitEcheanceComplexModel droitModel = droits.get(i);
            try {
                droitModel.getDroitModel().setImprimerEcheance(Boolean.FALSE);
                ALImplServiceLocator.getDroitModelService().update(droitModel.getDroitModel());

                if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                    JadeLogger.error(this, droitModel.getId() + "  pas décoché");
                    logger.getErrorsLogger(droitModel.getDroitModel().getIdDossier(),
                            "Dossier #" + droitModel.getDroitModel().getIdDossier()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, DroitEcheanceServiceImpl.class
                                    .getName(), droitModel.getDroitModel().getIdDossier()
                                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                            "al.protocoles.impressionAvisEcheances.erreur.dossierId", null)));
                    JadeThread.rollbackSession();
                    JadeThread.logClear();
                } else {
                    logger.getInfosLogger(droitModel.getDroitModel().getIdDossier(),
                            "Dossier #" + droitModel.getDroitModel().getIdDossier()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.INFO, DroitEcheanceServiceImpl.class
                                    .getName(), droitModel.getDroitModel().getIdDossier()
                                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                            "al.protocoles.impressionAvisEcheances.info.dossierId", null)));
                    JadeThread.commitSession();
                }

            } catch (Exception e) {
                JadeLogger.error(this, "Une erreur s'est produite pendant la mise à jour (annoncer échéance) du droit "
                        + droitModel.getId() + " : " + e.getMessage());
            }

        }
    }

}
