package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.text.DecimalFormat;
import java.util.*;

import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.droitEcheance.ALDroitEcheanceException;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseModelException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseBusinessService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe d'implémentation des services métier des récaps
 * 
 * @author GMO / PTA
 * 
 */
public class RecapitulatifEntrepriseBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements
        RecapitulatifEntrepriseBusinessService {

    @Override
    public String calculMontantPourUneRecapEntreprise(String idRecap, List listeRecap)
            throws JadePersistenceException, JadeApplicationException {
        double montantTotalRecap = 0.00d;
        // contrôle des paramètres
        if (JadeStringUtil.isEmpty(idRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#calculMontantPourUneRecapEntreprise: idRecap is empty");
        }

        if (listeRecap == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#calculMontantPourUneRecapEntreprise: listRecap is null");
        }

        for (int i = 0; i < listeRecap.size(); i++) {

            RecapitulatifEntrepriseImpressionComplexModel recap = ((RecapitulatifEntrepriseImpressionComplexModel) listeRecap
                    .get(i));

            if (JadeStringUtil.equals(recap.getRecapEntrepriseModel().getIdRecap(), idRecap, false)) {
                montantTotalRecap = montantTotalRecap + JadeStringUtil.parseDouble(recap.getMontant(), 0.00d);
            }
        }
        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");

        String montantTotalRec = null;
        montantTotalRec = String.valueOf(df.format(montantTotalRecap));

        return montantTotalRec;
    }

    @Override
    public String getDebutRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException {
        if (dossier == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getDebutRecap: dossier is null");
        }
        // contrôle des paramètres
        if (JadeStringUtil.isEmpty(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getDebutRecap: periode is empty");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getDebutRecap: periode is not mm.YYYY");

        }

        String periodicite = ALServiceLocator.getAffiliationBusinessService()
                .getAssuranceInfo(dossier, "01." + periode).getPeriodicitieAffiliation();

        String boni = JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                : ALCSPrestation.BONI_DIRECT;

        String periodeDebut = "";

        // dans le cas des dossiers indirects, périodes récaps varient selon
        // périodicité
        if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(boni)) {
            periodeDebut = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(periode);
        }

        else if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(boni)) {
            periodeDebut = "01." + periode.substring(3);
        } else {
            periodeDebut = periode;
        }

        return periodeDebut;
    }

    @Override
    public String getFinRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException {
        if (dossier == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getFinRecap: dossier is null");
        }
        // contrôle des paramètres
        if (JadeStringUtil.isEmpty(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getFinRecap: periode is empty");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getFinRecap: periode is not mm.YYYY");

        }
        String periodicite = ALServiceLocator.getAffiliationBusinessService()
                .getAssuranceInfo(dossier, "01." + periode).getPeriodicitieAffiliation();

        String boni = JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                : ALCSPrestation.BONI_DIRECT;

        // FIXME: bz 6693
        String periodeFin = "";
        if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(boni)) {
            periodeFin = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeFinTrimestre(periode);
        }

        else if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(boni)) {
            periodeFin = "12." + periode.substring(3);
        } else {
            periodeFin = periode;
        }

        return periodeFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. RecapitulatifEntrepriseBusinessService
     * #getNbPrestationsASaisir(java.lang.String)
     */
    @Override
    public int getNbPrestationsASaisir(String numRecap) throws JadePersistenceException, JadeApplicationException {
        if (JadeStringUtil.isEmpty(numRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to search model (EntetePrestationModel) - the numRecap criteria is undefined!");
        }
        EntetePrestationSearchModel searchModel = new EntetePrestationSearchModel();
        searchModel.setWhereKey("prestationZeroInRecap");
        searchModel.setForIdRecap(numRecap);
        searchModel.setForMontantTotal("0");
        searchModel = ALImplServiceLocator.getEntetePrestationModelService().search(searchModel);
        return searchModel.getSearchResults().length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. RecapitulatifEntrepriseBusinessService
     * #getNextPrestationASaisir(java.lang.String)
     */
    @Override
    public EntetePrestationModel getNextPrestationASaisir(String numRecap) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isEmpty(numRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to search model (EntetePrestationModel) - the numRecap criteria is undefined!");
        }
        EntetePrestationSearchModel searchModel = new EntetePrestationSearchModel();
        searchModel.setWhereKey("prestationZeroInRecap");
        searchModel.setForIdRecap(numRecap);
        searchModel.setForMontantTotal("0");
        searchModel = ALImplServiceLocator.getEntetePrestationModelService().search(searchModel);

        return (EntetePrestationModel) searchModel.getSearchResults()[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.prestation.
     * RecapitulatifEntrepriseBusinessService#getTotalRecap(java.lang.String)
     */
    @Override
    public String getTotalRecap(String idRecap) throws JadePersistenceException, JadeApplicationException {

        double montantTotalRecap = 0.00d;

        if (JadeStringUtil.isEmpty(idRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to get the total of recap (getTotalRecap) - the idRecap criteria is undefined!");
        }

        EntetePrestationSearchModel searchEntete = new EntetePrestationSearchModel();
        searchEntete.setForIdRecap(idRecap);
        searchEntete.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchEntete = ALServiceLocator.getEntetePrestationModelService().search(searchEntete);

        for (int i = 0; i < searchEntete.getSize(); i++) {
            montantTotalRecap = montantTotalRecap
                    + JadeStringUtil.parseDouble(
                            ((EntetePrestationModel) searchEntete.getSearchResults()[i]).getMontantTotal(), 0.00d);
        }

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");

        String montantTotalRec = null;
        montantTotalRec = String.valueOf(df.format(montantTotalRecap));

        return montantTotalRec;
    }

    @Override
    public RecapitulatifEntrepriseModel initRecap(DossierModel dossier, String periode, String bonification)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#initRecap: dossier is null");
        }
        if (JadeStringUtil.isEmpty(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#initRecap: periode is empty");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#initRecap: periode is not a valid (mm.yyyy)");
        }

        if (JadeStringUtil.isEmpty(bonification)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#initRecap: bonification is empty");
        }

        String genreAssurance = ALServiceLocator.getDossierBusinessService().isParitaire(
                dossier.getActiviteAllocataire()) ? ALCSAffilie.GENRE_ASSURANCE_PARITAIRE
                : ALCSAffilie.GENRE_ASSURANCE_INDEP;

        // recherche si récap ouverte existe pour ce n° affilié et type bonif
        // si oui => on la retourne
        // sinon on la créé
        String numFacture = ALServiceLocator.getNumeroFactureService().getNumFacture(periode, dossier);
        String debutRecap = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                .getDebutRecap(dossier, periode);
        String finRecap = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().getFinRecap(dossier, periode);

        // recherche d'une récap existante
        RecapitulatifEntrepriseSearchModel s = new RecapitulatifEntrepriseSearchModel();
        s.setForEtatRecap(ALCSPrestation.ETAT_SA);
        s.setForNumeroAffilie(dossier.getNumeroAffilie());
        s.setForBonification(bonification);
        s.setForNumeroFacture(numFacture);
        s.setForPeriodeDe(debutRecap);
        s.setForPeriodeA(finRecap);
        s.setForNumProcessusLie("0");
        s.setForGenreAssurance(genreAssurance);
        s = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(s);
        RecapitulatifEntrepriseModel recap = null;
        if (s.getSize() == 1) {
            recap = (RecapitulatifEntrepriseModel) s.getSearchResults()[0];
        } else if (s.getSize() > 1) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#initRecap: More than one recap were found");
        } else {
            recap = new RecapitulatifEntrepriseModel();
            recap.setEtatRecap(ALCSPrestation.ETAT_SA);
            recap.setNumeroAffilie(dossier.getNumeroAffilie());
            recap.setBonification(bonification);
            recap.setNumeroFacture(numFacture);
            recap.setPeriodeDe(debutRecap);
            recap.setPeriodeA(finRecap);
            recap.setGenreAssurance(genreAssurance);
            recap.setIdProcessusPeriodique("0");
            recap = ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recap);
        }
        return recap;

    }

    /**
     * Méthode qui initialise les éléments de recherche requis pour toutes les recherches des récapitulatifs,
     * personnelles, directs et paritaires (sauf recherche par un lot de lot)
     * 
     * @param etatRecap
     *            état de la récap
     * @param periodeA
     *            période de la récap
     * 
     * @return RecapitulatifEntrepriseImpressionComplexSearchModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private RecapitulatifEntrepriseImpressionComplexSearchModel initSearchForAllTypeRecap(String periodeA,
            String etatRecap) throws JadePersistenceException, JadeApplicationException {

        // Vérification des paramètres
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALDroitEcheanceException("RecapitulatifEntrepriseBusinessService#initSearchForAll: " + periodeA
                    + " is not a globaz date valid (MM.YYYY");
        }
        if (!JadeStringUtil.equals(ALCSPrestation.ETAT_CO, etatRecap, false)
                && (!JadeStringUtil.equals(ALCSPrestation.ETAT_PR, etatRecap, false)
                        && !JadeStringUtil.equals(ALCSPrestation.ETAT_SA, etatRecap, false) && !JadeStringUtil.equals(
                        ALCSPrestation.ETAT_TR, etatRecap, false))) {
            throw new ALDroitEcheanceException("RecapitulatifEntrepriseBusinessServiceImpl#initSearchForAll: "
                    + etatRecap + " is not valid etatRecap");

        }

        RecapitulatifEntrepriseImpressionComplexSearchModel recap = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        recap.setForEtatRecap(etatRecap);
        recap.setForPeriodeA(periodeA);
        if (ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
            recap.setForTypeLiaison(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);
        }
        recap.setForNumeroLot("");

        return recap;

    }

    @Override
    public boolean isRecapVerouillee(RecapitulatifEntrepriseModel recap) throws JadePersistenceException,
            JadeApplicationException {

        if (recap == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#isRecapVerouillee: recap is null");
        }

        ContextTucana.initContext(JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())));

        return ALCSPrestation.ETAT_TR.equals(recap.getEtatRecap())
                || ALCSPrestation.ETAT_CO.equals(recap.getEtatRecap())
                || (ContextTucana.tucanaIsEnabled() && ALCSPrestation.ETAT_SA.equals(recap.getEtatRecap()) && !JadeStringUtil
                        .isBlankOrZero(recap.getIdProcessusPeriodique()));
    }

    @Override
    public List<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecap(
            RecapitulatifEntrepriseImpressionComplexSearchModel recap, String typeRecap, String periodeA,
            String etatRecap) throws JadePersistenceException, JadeApplicationException {
        // vérification des paramètres
        if (recap == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#resultSearchRecap: recap is null");
        }
        if (!JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_DIRECT, false)
                && !JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_COT_PAR, false)
                && !JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_COT_PERS, false)
                && !JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_INDIRECT_GROUPE, false)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#resultSearchRecap: " + typeRecap
                            + " is not valid typeRecap");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#resultSearchRecap: " + periodeA
                            + " is not a globaz date valid (MM.YYYY");
        }

        if (!JadeStringUtil.equals(ALCSPrestation.ETAT_CO, etatRecap, false)
                && (!JadeStringUtil.equals(ALCSPrestation.ETAT_PR, etatRecap, false)
                        && !JadeStringUtil.equals(ALCSPrestation.ETAT_SA, etatRecap, false) && !JadeStringUtil.equals(
                        ALCSPrestation.ETAT_TR, etatRecap, false))) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#resultSearchRecap: " + etatRecap
                            + " is not valid etatRecap");

        }

        List<RecapitulatifEntrepriseImpressionComplexSearchModel> listRecap = new ArrayList<>();
        // cotisations groupées
        if (JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_INDIRECT_GROUPE, false)) {

            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            // ajout de la bonif indirect
            Set inTypeBonification = new HashSet();
            inTypeBonification.add(ALCSPrestation.BONI_INDIRECT);
            recap.setInTypeBonification(inTypeBonification);
            // HashSet des type d'activit exclure
            Set<String> notInActivite = new HashSet<>();
            notInActivite.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_INDEPENDANT);
            notInActivite.add(ALCSDossier.ACTIVITE_PECHEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_TSE);
            notInActivite.add(ALCSDossier.ACTIVITE_VIGNERON);
            notInActivite.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);

            recap.setNotInActiviteAlloc(notInActivite);
            // effectue la recherche
            recap = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(recap);

            // ajout au tableau
            listRecap.add(recap);
        }
        // cotisations paritaires
        if (JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_COT_PAR, false)) {

            // Activité coti. paritaires, exclure
            // HashSet activiteAllocataire = ALServiceLocator
            // .getDossierBusinessService().getActivitesCategorieCotPers();
            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            // ajout de la bonif indirect
            Set inTypeBonification = new HashSet();
            inTypeBonification.add(ALCSPrestation.BONI_INDIRECT);
            recap.setInTypeBonification(inTypeBonification);
            // HashSet des type d'activit exclure
            Set<String> notInActivite = new HashSet<>();
            notInActivite.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_INDEPENDANT);
            notInActivite.add(ALCSDossier.ACTIVITE_NONACTIF);
            notInActivite.add(ALCSDossier.ACTIVITE_PECHEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_TSE);
            notInActivite.add(ALCSDossier.ACTIVITE_VIGNERON);
            notInActivite.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);
            recap.setNotInActiviteAlloc(notInActivite);
            // effectue la recherche
            recap = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(recap);

            // ajout au tableau
            listRecap.add(recap);
        }

        // cotisations pers
        if (JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_COT_PERS, false)) {

            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            // ajout de la bonif indirect
            Set inTypeBonification = new HashSet();
            inTypeBonification.add(ALCSPrestation.BONI_INDIRECT);
            recap.setInTypeBonification(inTypeBonification);
            // HashSet des type d'activit exclure
            Set<String> notInActivite = new HashSet<>();

            notInActivite.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE);
            notInActivite.add(ALCSDossier.ACTIVITE_INDEPENDANT);
            notInActivite.add(ALCSDossier.ACTIVITE_SALARIE);
            notInActivite.add(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE);
            notInActivite.add(ALCSDossier.ACTIVITE_PECHEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_TSE);
            notInActivite.add(ALCSDossier.ACTIVITE_VIGNERON);
            notInActivite.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);
            recap.setNotInActiviteAlloc(notInActivite);
            // effectue la recherche
            recap = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(recap);

            // ajout au tableau
            listRecap.add(recap);
        }
        // paiements direct
        if (JadeStringUtil.equals(typeRecap, ALConstPrestations.TYPE_DIRECT, false)) {

            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            Set typeBonification = new HashSet();
            typeBonification.add(ALCSPrestation.BONI_DIRECT);
            typeBonification.add(ALCSPrestation.BONI_RESTITUTION);

            recap.setInTypeBonification(typeBonification);
            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            recap = initSearchForAllTypeRecap(periodeA, etatRecap);
            recap.setInTypeBonification(typeBonification);
            // exclure les catégories
            // HashSet des type d'activit exclure
            Set<String> notInActivite = new HashSet<>();

            notInActivite.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_INDEPENDANT);
            notInActivite.add(ALCSDossier.ACTIVITE_PECHEUR);
            notInActivite.add(ALCSDossier.ACTIVITE_TSE);
            notInActivite.add(ALCSDossier.ACTIVITE_VIGNERON);
            notInActivite.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);

            recap.setNotInActiviteAlloc(notInActivite);

            // effectue la recherche
            recap = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(recap);

            listRecap.add(recap);

        }

        return listRecap;
    }

    @Override
    public List<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecap(String noLot)
            throws JadePersistenceException, JadeApplicationException {
        List<RecapitulatifEntrepriseImpressionComplexSearchModel> recap = new ArrayList<>();
        RecapitulatifEntrepriseImpressionComplexSearchModel recapParLot = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        recapParLot = searchRecapNumLot(noLot);

        recap.add(recapParLot);
        return recap;
    }

    @Override
    public List<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecapNumProcessus(
            String idProcessus) throws JadePersistenceException, JadeApplicationException {
        // Contrôle du paramètre
        if (!JadeNumericUtil.isInteger(idProcessus)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#resultSearchRecapNumProcessus: " + idProcessus
                            + "is not a integer more than 0");
        }
        List<RecapitulatifEntrepriseImpressionComplexSearchModel> recap = new ArrayList<>();
        RecapitulatifEntrepriseImpressionComplexSearchModel recapSearch = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        recapSearch.setForNumProcessus(idProcessus);
        recapSearch.setWhereKey("numProcessus");

        // exclure les catégories
        // HashSet des type d'activit exclure
        Set<String> notInActivite = new HashSet<>();

        notInActivite.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
        notInActivite.add(ALCSDossier.ACTIVITE_INDEPENDANT);
        notInActivite.add(ALCSDossier.ACTIVITE_PECHEUR);
        notInActivite.add(ALCSDossier.ACTIVITE_TSE);
        notInActivite.add(ALCSDossier.ACTIVITE_VIGNERON);
        notInActivite.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);

        recapSearch.setNotInActiviteAlloc(notInActivite);

        recapSearch = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService()
                .search(recapSearch);
        recap.add(recapSearch);

        return recap;
    }

    @Override
    public List<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecapNumRecap(String numRecap)
            throws JadePersistenceException, JadeApplicationException {
        // Contrôle du paramètre
        if (!JadeNumericUtil.isInteger(numRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessServiceImpl#resultSearchRecapNumRecap: " + numRecap
                            + "is not a integer more than 0");
        }
        List<RecapitulatifEntrepriseImpressionComplexSearchModel> recap = new ArrayList<>();
        RecapitulatifEntrepriseImpressionComplexSearchModel recapSearch = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        recapSearch.setForIdRecap(numRecap);
        if (ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
            recapSearch.setForTypeLiaison(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);
        }
        recapSearch.setWhereKey("numRecap");
        recapSearch = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService()
                .search(recapSearch);
        recap.add(recapSearch);

        return recap;
    }

    /**
     * Méthode qui réalise recherche des recap par numero de lot
     * 
     * @param noLot
     *            numéro de lot
     * @return searchRecapNoLot
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private RecapitulatifEntrepriseImpressionComplexSearchModel searchRecapNumLot(String noLot)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle du paramètre
        if (!JadeNumericUtil.isIntegerPositif(noLot)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#searchRecapForNoLot : " + noLot
                            + "is not a integer more than 0");
        }

        RecapitulatifEntrepriseImpressionComplexSearchModel searchRecapNoLot = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        searchRecapNoLot.setForNumeroLot(noLot);
        if (ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
            searchRecapNoLot.setForTypeLiaison(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);
        }
        searchRecapNoLot = ALImplServiceLocator.getRecapitulatifEntrepriseImpressionComplexModelService().search(
                searchRecapNoLot);

        return searchRecapNoLot;
    }

}
