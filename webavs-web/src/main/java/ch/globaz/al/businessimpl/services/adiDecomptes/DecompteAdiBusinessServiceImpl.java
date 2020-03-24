package ch.globaz.al.businessimpl.services.adiDecomptes;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.business.ALPrestationBusinessException;
import ch.globaz.al.business.exceptions.model.adi.ALDecompteAdiModelException;
import ch.globaz.al.business.models.adi.*;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService;
import ch.globaz.al.businessimpl.calcul.modes.CalculImpotSource;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.service.ParamServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 
 * Impl�mentation des services m�tier li�s aux d�comptes ADI
 * 
 * @author GMO
 * 
 */
public class DecompteAdiBusinessServiceImpl implements DecompteAdiBusinessService {

    private DetailPrestationComplexSearchModel completePrestationsTravailDossier(String idDossier, String periodeDebut,
            String periodeFin) throws JadeApplicationException, JadePersistenceException {

        if (idDossier == null) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#completePrestationsDossier : unable to complete prestations, idDossier is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeDebut)) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#completePrestationsDossier : unable to complete prestations, periodeDebut is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeFin)) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#completePrestationsDossier : unable to complete prestations, periodeFin is not a valid period");
        }
        DetailPrestationComplexSearchModel prestComplexSearchModel = new DetailPrestationComplexSearchModel();
        prestComplexSearchModel.setForPeriodeDe(periodeDebut);
        prestComplexSearchModel.setForPeriodeA(periodeFin);
        prestComplexSearchModel.setForEtat(ALCSPrestation.ETAT_TMP);
        prestComplexSearchModel.setForStatut(ALCSPrestation.STATUT_ADI);
        prestComplexSearchModel.setNotForTarif(ALCSTarif.CATEGORIE_SUP_HORLO);
        prestComplexSearchModel.setForIdDossier(idDossier);
        prestComplexSearchModel.setWhereKey("prestationTravailPlagePeriode");
        prestComplexSearchModel.setOrderKey("periodeOrder");

        prestComplexSearchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(
                prestComplexSearchModel);

        // on compl�te ce qui manque par rapport aux prestations
        // existantes, attention on ne remplace pas les existantes car elles
        // peuvent �tre sp�cifiques pas reproductibles par le calcul

        HashSet periodeExistantes = new HashSet();

        // pour chaque prestation existante
        for (int j = 0; j < prestComplexSearchModel.getSize(); j++) {

            periodeExistantes.add(((DetailPrestationComplexModel) prestComplexSearchModel.getSearchResults()[j])
                    .getDetailPrestationModel().getPeriodeValidite());

        }

        HashSet periodeAGenerer = new HashSet();
        String newPeriodeDate = "01.".concat(periodeDebut);
        do {
            periodeAGenerer.add(newPeriodeDate.substring(3));

            newPeriodeDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                    ALDateUtils.getCalendarDate(newPeriodeDate)).getTime());

        } while (!JadeDateUtil.isDateAfter(newPeriodeDate, "01.".concat(periodeFin)));
        // on soustrait l'existant de ce qu'il y a g�n�rer
        periodeAGenerer.removeAll(periodeExistantes);
        // On tri pour g�n�rer correctement
        ArrayList periodeAGenererSortable = new ArrayList(periodeAGenerer);
        Collections.sort(periodeAGenererSortable);

        boolean toGenerate = false;
        String debutGenerer = "";
        if (!periodeAGenererSortable.isEmpty()) {
            debutGenerer = periodeAGenererSortable.get(0).toString();
        }

        for (int i = 0; i < periodeAGenererSortable.size(); i++) {
            // si on a g�n�r� au passage pr�c�dent, cette p�riode sera le d�but
            // de la prochaine g�n�ration
            if (toGenerate) {
                debutGenerer = periodeAGenererSortable.get(i).toString();
            }

            String nextTheoricDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                    ALDateUtils.getCalendarDate("01.".concat(periodeAGenererSortable.get(i).toString()))).getTime());
            // on regarde si la prochaine date th�orique est la derni�re des p�riodes � g�n�rer
            // si oui, on d�clenche la g�n�ration
            if (i == periodeAGenererSortable.size() - 1) {
                toGenerate = true;
            } else {
                toGenerate = !JadeDateUtil.areDatesEquals("01.".concat(periodeAGenererSortable.get(i + 1).toString()),
                        nextTheoricDate);
            }

            if (toGenerate) {

                DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);
                // FIXME : le num de facture n'est pas utilis� dans le cadre des prestations suisses ADI
                // car pas de r�cap cr��e
                String numFacture = ALServiceLocator
                        .getNumeroFactureService()
                        .getNumFacture(
                                debutGenerer,
                                ALCSAffilie.PERIODICITE_MEN,
                                (JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                                        : ALCSPrestation.BONI_DIRECT), dossier.getDossierModel().getNumeroAffilie());

                ALServiceLocator.getGenerationDossierService().generationDossier(dossier, null, debutGenerer,
                        periodeAGenererSortable.get(i).toString(), debutGenerer, debutGenerer, "0", Bonification.AUTO,
                        "1", numFacture, "0");
            }

        }

        // apr�s avoir g�n�rer les prestations manquantes, on relance la
        // recherche pour compl�ter �galement le mod�le de recherche
        prestComplexSearchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(
                prestComplexSearchModel);

        return prestComplexSearchModel;
    }

    @Override
    public String comptabiliserDecompteLie(String idEnteteAdi) throws JadeApplicationException, JadePersistenceException {
        // on met le d�compte li� en CO
        DecompteAdiSearchModel decompteLie = new DecompteAdiSearchModel();
        decompteLie.setForIdPrestationAdi(idEnteteAdi);
        decompteLie = ALServiceLocator.getDecompteAdiModelService().search(decompteLie);
        if (decompteLie.getSize() > 0) {
            DecompteAdiModel decompteToUpd = (DecompteAdiModel) decompteLie.getSearchResults()[0];
            decompteToUpd.setEtatDecompte(ALCSPrestation.ETAT_CO);
            ALServiceLocator.getDecompteAdiModelService().update(decompteToUpd);
            return decompteToUpd.getIdDecompteAdi();
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService #controleSaisieDecompte
     * (ch.globaz.al.business.models.adi.DecompteAdiModel, ch.globaz.al.business.
     * models.prestation.DetailPrestationComplexSearchModel, ch.globaz.al.business.models.adi.AdiSaisieSearchModel)
     */
    @Override
    public HashMap controleSaisieDecompte(DecompteAdiModel decompte,
            DetailPrestationComplexSearchModel prestationTravail, AdiSaisieComplexSearchModel saisiesExistantes)
            throws JadePersistenceException, JadeApplicationException {

        if (decompte == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#controleSaisieDecompte: unable to control saisie, decompte is null");
        }
        if (prestationTravail == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#controleSaisieDecompte: unable to control saisie, prestationTravail is null");
        }
        if (saisiesExistantes == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#controleSaisieDecompte: unable to control saisie, saisiesExistantes is null");
        }

        HashMap SaisieEnfantMap = new HashMap();
        // pour chaque enfant trouv� dans les d�tails de la prestation de
        // travail, on introduit une map qui contient pour chaque p�riode des
        // d�tails la valeur 0.
        for (int i = 0; i < prestationTravail.getSize(); i++) {

            DetailPrestationComplexModel currentDetail = (DetailPrestationComplexModel) prestationTravail
                    .getSearchResults()[i];
            if (!SaisieEnfantMap.containsKey(currentDetail.getDroitComplexModel().getEnfantComplexModel().getId())) {
                // on veut tous les mois couverts par le d�compte en format
                // 1,2,3,...
                int debutDecompte = Integer.parseInt(decompte.getPeriodeDebut().substring(0, 2));
                int finDecompte = Integer.parseInt(decompte.getPeriodeFin().substring(0, 2));

                // pour un nouvel enfant trouv�, on cr�e une hashmap contenant
                // les p�riodes (d�sactiv�s,-1) du d�compte
                HashMap newEnfantMap = new HashMap();
                for (int j = debutDecompte; j < finDecompte + 1; j++) {
                    newEnfantMap.put(new Integer(j), new Integer(-1));
                }

                SaisieEnfantMap.put(currentDetail.getDroitComplexModel().getEnfantComplexModel().getId(), newEnfantMap);
            }

            ((HashMap) SaisieEnfantMap.get(currentDetail.getDroitComplexModel().getEnfantComplexModel().getId())).put(
                    new Integer(currentDetail.getDetailPrestationModel().getPeriodeValidite().substring(0, 2)),
                    new Integer(0));

        }

        // pour chaque saisie existante, on ajoute la valeur dans les hasmap
        // d'enfant
        for (int i = 0; i < saisiesExistantes.getSize(); i++) {

            AdiSaisieComplexModel currentSaisie = (AdiSaisieComplexModel) saisiesExistantes.getSearchResults()[i];

            // on met � jour la map saisie enfant(echelle) que si la saisie
            // courante porte sur un enfant de la prestations
            // de travail
            if (SaisieEnfantMap.containsKey(currentSaisie.getAdiSaisieModel().getIdEnfant())) {

                int debutCurrentSaisie = Integer.parseInt(currentSaisie.getAdiSaisieModel().getPeriodeDe()
                        .substring(0, 2));
                int finCurrentSaisie = Integer
                        .parseInt(currentSaisie.getAdiSaisieModel().getPeriodeA().substring(0, 2));

                // pour chaque saisie existante, on met a 1 la bonne cl� de
                // l'enfant
                // concern�
                for (int j = debutCurrentSaisie; j < finCurrentSaisie + 1; j++) {
                    HashMap enfantMap = (HashMap) SaisieEnfantMap.get(currentSaisie.getAdiSaisieModel().getIdEnfant());
                    enfantMap.remove(new Integer(j));
                    enfantMap.put(new Integer(j), new Integer(1));
                }
            }

        }

        return SaisieEnfantMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService
     * #genererPrestationAdi(ch.globaz.al.business.models.adi.DecompteAdiModel)
     */
    @Override
    public void genererPrestationAdi(DecompteAdiModel decompte, String periodeTraitement, String numFacture,
            String numProcessus) throws JadePersistenceException, JadeApplicationException {

        if (decompte == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#genererPrestationAdi: unable to generate prestation ADI from decompte");
        }

        if (JadeStringUtil.isEmpty(periodeTraitement)) {

            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#genererPrestationAdi: impossible de g�n�rer la prestation ADI - periodeTraitement n'est pas d�finie");
        }

        if (JadeStringUtil.isEmpty(numFacture)) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#genererPrestationAdi: unable to generate prestation ADI from decompte - numFacture is undefined");
        }
        if (JadeStringUtil.isEmpty(numProcessus)) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#genererPrestationAdi: unable to generate prestation ADI from decompte - numProcessus is undefined");
        }

        if (ALCSPrestation.ETAT_CO.equals(decompte.getEtatDecompte())) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl#genererPrestationAdi: on ne peut pas g�n�rer une prestation depuis un d�compte comptabilis�");
        }

        // Prestations de travail
        DetailPrestationComplexSearchModel prestationTravailSearchModel = new DetailPrestationComplexSearchModel();

        prestationTravailSearchModel.setForEtat(ALCSPrestation.ETAT_TMP);
        prestationTravailSearchModel.setForPeriodeA(decompte.getPeriodeFin());
        prestationTravailSearchModel.setForPeriodeDe(decompte.getPeriodeDebut());
        prestationTravailSearchModel.setForStatut(ALCSPrestation.STATUT_ADI);
        prestationTravailSearchModel.setForIdDossier(decompte.getIdDossier());
        prestationTravailSearchModel.setWhereKey("prestationTravailPlagePeriode");

        prestationTravailSearchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(
                prestationTravailSearchModel);

        AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearch = new AdiEnfantMoisComplexSearchModel();
        adiEnfantMoisComplexSearch.setForIdDecompteAdi(decompte.getId());

        adiEnfantMoisComplexSearch = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(
                adiEnfantMoisComplexSearch);

        HashMap<String, ArrayList<CalculBusinessModel>> listeCalculAdiEnfant = new HashMap<String, ArrayList<CalculBusinessModel>>();

        // Remplissage de la hashmap avec 1 cl� par p�riode, on affectera les
        // valeurs dans l'arraylist ensuite
        ArrayList<String> periodesCouvertesDecompte = ALDateUtils.enumPeriodeFromInterval(decompte.getPeriodeDebut(),
                decompte.getPeriodeFin());

        for (String periodeCouverte : periodesCouvertesDecompte) {
            listeCalculAdiEnfant.put(periodeCouverte, new ArrayList<CalculBusinessModel>());
        }

        // pour chaque adiEnfantMois li� au d�compte, on cr�e un
        // calculBusinessModel correspondant
        for (int i = 0; i < adiEnfantMoisComplexSearch.getSize(); i++) {
            AdiEnfantMoisComplexModel currentAdiEnfantMoisComplex = (AdiEnfantMoisComplexModel) adiEnfantMoisComplexSearch
                    .getSearchResults()[i];

            ArrayList<DetailPrestationComplexModel> listCorrespDetail = searchDetailInPrestationTravail(
                    prestationTravailSearchModel, currentAdiEnfantMoisComplex.getAdiEnfantMoisModel().getIdDroit(),
                    currentAdiEnfantMoisComplex.getAdiEnfantMoisModel().getMoisPeriode());
            if (listCorrespDetail == null) {
                throw new ALDecompteAdiModelException(
                        "DecompteAdiBusinessServiceImpl#genererPrestationAdi: impossible de g�n�rer la prestation ADI si il n'existe pas de prestation suisse correspondante");
            }
            for (int j = 0; j < listCorrespDetail.size(); j++) {

                DetailPrestationComplexModel correspDetail = listCorrespDetail.get(j);

                CalculBusinessModel currentCalculBusinessModel = new CalculBusinessModel();
                // gestion des suppl�ment horlogers
                if (JadeStringUtil.equals(correspDetail.getDetailPrestationModel().getCategorieTarif(),
                        ALCSTarif.CATEGORIE_SUP_HORLO, false)) {
                    currentCalculBusinessModel = prestationHorloger(correspDetail);
                } else {
                    currentCalculBusinessModel.setRang(correspDetail.getDetailPrestationModel().getRang());
                    currentCalculBusinessModel.setActif(true);
                    currentCalculBusinessModel.setCalculResultMontantBase(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAdi());
                    currentCalculBusinessModel.setCalculResultMontantBaseCaisse(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAllocCH());
                    currentCalculBusinessModel.setCalculResultMontantBaseCanton(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAllocCH());
                    currentCalculBusinessModel.setCalculResultMontantEffectif(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAdi());
                    currentCalculBusinessModel.setCalculResultMontantEffectifCaisse(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAdi());
                    currentCalculBusinessModel.setCalculResultMontantEffectifCanton(currentAdiEnfantMoisComplex
                            .getAdiEnfantMoisModel().getMontantAdi());
                    currentCalculBusinessModel.setDroit(currentAdiEnfantMoisComplex.getDroitComplexModel());
                    currentCalculBusinessModel.setExportable(true);
                    currentCalculBusinessModel.setEcheanceOverLimiteLegale(false);
                    currentCalculBusinessModel.setMontantAllocataire("0");
                    currentCalculBusinessModel.setMontantAutreParent("0");

                    currentCalculBusinessModel.setTarif(correspDetail.getDetailPrestationModel().getCategorieTarif());
                    currentCalculBusinessModel.setTarifCaisse(correspDetail.getDetailPrestationModel()
                            .getCategorieTarifCaisse());
                    currentCalculBusinessModel.setTarifCanton(correspDetail.getDetailPrestationModel()
                            .getCategorieTarifCanton());
                    currentCalculBusinessModel.setTarifForce(false);
                    currentCalculBusinessModel.setType(currentAdiEnfantMoisComplex.getDroitComplexModel()
                            .getDroitModel().getTypeDroit());
                }
                // On injecte le calculBusinessModel dans la hashmap
                ArrayList<CalculBusinessModel> listMois = listeCalculAdiEnfant.get(currentAdiEnfantMoisComplex
                        .getAdiEnfantMoisModel().getMoisPeriode());
                listMois.add(currentCalculBusinessModel);
            }

        }

        // G�n�ration effective de la prestation ADI
        DossierModel dossier = ALServiceLocator.getDossierModelService().read(decompte.getIdDossier());
        String bonification = JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                : ALCSPrestation.BONI_DIRECT;

        String periodicite = ALServiceLocator.getAffiliationBusinessService()
                .getAssuranceInfo(dossier, "01." + periodeTraitement).getPeriodicitieAffiliation();

        String periodeDebutTraitement = periodeTraitement;
        // dans le cas des dossiers indirects, p�riodes r�caps varient selon
        // p�riodicit�
        if (ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
                periodeDebutTraitement = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(
                        periodeTraitement);
            }
            if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite)) {
                periodeDebutTraitement = "01.".concat(periodeTraitement.substring(3));
            }
        }

        DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(decompte.getIdDossier());

        if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                && dossier.getRetenueImpot()
                && !ALCSDossier.PAIEMENT_INDIRECT.equals(CalculImpotSource.getPaiementMode(dossierComplex, JadeDateUtil.getFirstDateOfMonth(periodeTraitement)))) {
            TauxImpositionRepository tauxImpositionRepository = ALRepositoryLocator.getTauxImpositionRepository();
            TauxImpositions tauxGroupByCanton = tauxImpositionRepository.findAll(TypeImposition.IMPOT_SOURCE);

            for(String period : listeCalculAdiEnfant.keySet()) {
                String date = JadeDateUtil.getFirstDateOfMonth(period);
                AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
                for(CalculBusinessModel calcul:listeCalculAdiEnfant.get(period)) {
                    CalculImpotSource.computeISforDroit(calcul, calcul.getCalculResultMontantEffectif()
                            , tauxGroupByCanton, tauxImpositionRepository, infos.getCanton(), date);
                }
            }

        }

        // g�n�ration des prestations diff�rentes que suppl�ement horloger
        ALServiceLocator.getGenerationDossierService().generationDossierADI(
                dossierComplex,
                decompte.getPeriodeDebut(), decompte.getPeriodeFin(), periodeDebutTraitement, periodeTraitement,
                listeCalculAdiEnfant, numFacture, numProcessus);

        // si pour des dossiers Adi, il y a des prestations suppl�ments horlogeer, il faut �galment les g�n�rer

        // On lie l'�ventuel d�compte remplac� au d�compte en cours de
        // traitement
        decompte.setIdDecompteRemplace(getIdDecompteRemplace(decompte.getIdDossier(), decompte.getAnneeDecompte(),
                periodesCouvertesDecompte));

        // On lie la prestation g�n�r�e
        EntetePrestationSearchModel prestationAdiSearchModel = new EntetePrestationSearchModel();
        prestationAdiSearchModel.setForEtat(ALCSPrestation.ETAT_SA);
        prestationAdiSearchModel.setForPeriodeA(decompte.getPeriodeFin());
        prestationAdiSearchModel.setForPeriodeDe(decompte.getPeriodeDebut());
        prestationAdiSearchModel.setForStatut(ALCSPrestation.STATUT_ADI);
        prestationAdiSearchModel.setForIdDossier(decompte.getIdDossier());
        // prestation plus grand ou �gal � 0, les extournes ne sont pas li�s �
        // des d�comptes
        prestationAdiSearchModel.setForMontantTotal("0");
        prestationAdiSearchModel.setWhereKey("prestationTravailPlagePeriode");

        prestationAdiSearchModel = ALServiceLocator.getEntetePrestationModelService().search(prestationAdiSearchModel);
        if (prestationAdiSearchModel.getSize() > 0) {
            decompte.setIdPrestationAdi(prestationAdiSearchModel.getSearchResults()[0].getId());
            // on indique le d�compte comme saisi
            decompte.setEtatDecompte(ALCSPrestation.ETAT_SA);
            // Sauvegarde du d�compte avec les liens prestation adi et d�compte
            // remplac�
            ALServiceLocator.getDecompteAdiModelService().update(decompte);
        } else {
            // si il a y a de�j� une erreur dans le thread, il faut pas remonter l'exception mais laisser "sortir" le
            // message
            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length == 0) {
                throw new ALDecompteAdiModelException(
                        "DecompteAdiBusinessServiceImpl#genererPrestationAdi: unable to link prestation ADI to decompte - prestation ADI not found");
            }
        }

    }

    private Map<String, List<AdiEnfantMoisComplexModel>> getAdiParDroit(List<AdiEnfantMoisComplexModel> listAdi) {
        Map<String, List<AdiEnfantMoisComplexModel>> map = new HashMap<>();
        for(AdiEnfantMoisComplexModel adi : listAdi) {
            if(map.get(adi.getDroitComplexModel().getId()) == null) {
                map.put(adi.getDroitComplexModel().getId(), new ArrayList<>());
            }
            map.get(adi.getDroitComplexModel().getId()).add(adi);
        }
        return map;
    }


    /**
     * Cherche dans les d�comptes CO de l'ann�e pass�e en param�tre, le plus r�cent qui a au moins une p�riode commune
     * avec la liste des p�riodes pass� en param�tre
     * 
     * @param idDossier
     *            - id du dossier dans lequel chercher les d�comptes
     * @param anneeNouveauDecompte
     *            - ann�e des d�comptes en CO
     * @param periodesCouvertesNouveauDecompte
     *            - liste des p�riodes couvertes par le nouveau d�compte rempla�ant
     * @return id du d�compte remplac�
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private String getIdDecompteRemplace(String idDossier, String anneeNouveauDecompte,
            ArrayList<String> periodesCouvertesNouveauDecompte) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        String idDecompteRemplace = "0";
        DecompteAdiSearchModel searchDecompteRemplace = new DecompteAdiSearchModel();
        searchDecompteRemplace.setForIdDossier(idDossier);
        searchDecompteRemplace.setForAnneeDecompte(anneeNouveauDecompte);
        searchDecompteRemplace.setForEtat(ALCSPrestation.ETAT_CO);
        searchDecompteRemplace = ALServiceLocator.getDecompteAdiModelService().search(searchDecompteRemplace);

        if (searchDecompteRemplace.getSize() > 0) {

            for (int i = 0; i < searchDecompteRemplace.getSize(); i++) {

                // On r�cup�re la p�riode du d�compte le plus r�cent de la m�me
                // ann�e trouv�
                ArrayList<String> periodeCouverteDecompteCO = ALDateUtils.enumPeriodeFromInterval(
                        ((DecompteAdiModel) searchDecompteRemplace.getSearchResults()[i]).getPeriodeDebut(),
                        ((DecompteAdiModel) searchDecompteRemplace.getSearchResults()[i]).getPeriodeFin());

                for (String periodeCouverte : periodesCouvertesNouveauDecompte) {
                    // si parmi les p�riodes couvertes dans le d�compte CO
                    // trouv�, on une p�riode du nouveau d�compte
                    if (periodeCouverteDecompteCO.contains(periodeCouverte)) {
                        idDecompteRemplace = ((DecompteAdiModel) searchDecompteRemplace.getSearchResults()[i])
                                .getIdDecompteAdi();
                        break;
                    }

                }
            }

        }

        return idDecompteRemplace;
    }

    @Override
    public DetailPrestationComplexSearchModel getPrestationsTravailDossier(String idDossier, String periodeDebut,
            String periodeFin) throws JadeApplicationException, JadePersistenceException {
        if (idDossier == null) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#getPrestationsTravailDossier : unable to get prestations, idDossier is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeDebut)) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#getPrestationsTravailDossier : unable to get prestations, periodeDebut is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeFin)) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#getPrestationsTravailDossier : unable to get prestations, periodeFin is not a valid period");
        }

        String paramPrestTravailAuto = ParamServiceLocator
                .getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.ADI_AUTO_PRESTATIONS_CH,
                        JadeDateUtil.getGlobazFormattedDate(new Date())).getValeurAlphaParametre();

        if ("true".equals(paramPrestTravailAuto)) {
            return completePrestationsTravailDossier(idDossier, periodeDebut, periodeFin);

        } else {
            DetailPrestationComplexSearchModel prestComplexSearchModel = new DetailPrestationComplexSearchModel();
            prestComplexSearchModel.setForPeriodeDe(periodeDebut);
            prestComplexSearchModel.setForPeriodeA(periodeFin);
            prestComplexSearchModel.setForEtat(ALCSPrestation.ETAT_TMP);
            prestComplexSearchModel.setForStatut(ALCSPrestation.STATUT_ADI);
            prestComplexSearchModel.setNotForTarif(ALCSTarif.CATEGORIE_SUP_HORLO);
            prestComplexSearchModel.setForIdDossier(idDossier);
            prestComplexSearchModel.setWhereKey("prestationTravailPlagePeriode");
            prestComplexSearchModel.setOrderKey("periodeOrder");

            prestComplexSearchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(
                    prestComplexSearchModel);

            return prestComplexSearchModel;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService #isSaisieComplete(java.util.HashMap)
     */
    @Override
    public boolean isSaisieComplete(HashMap<String, HashMap> listeSaisie) throws JadePersistenceException,
            JadeApplicationException {

        if (listeSaisie == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl@isSaisieComplete : unable to check list, it's undefined");
        }

        int mapsize = listeSaisie.size();

        Iterator keyValuePairs1 = listeSaisie.entrySet().iterator();
        for (int i = 0; i < mapsize; i++) {
            Entry entry = (Entry) keyValuePairs1.next();
            HashMap value = (HashMap) entry.getValue();
            if (value.containsValue(new Integer(0))) {
                return false;
            }

        }

        return true;
    }

    /**
     * M�thode qui reprend le Calcul business model pour les prestationAdi
     * 
     * @param correspDetail
     * @return
     * @throws ALDecompteAdiModelException
     */

    private CalculBusinessModel prestationHorloger(DetailPrestationComplexModel correspDetail)
            throws JadeApplicationException {

        // contr�le du param�tre
        if (correspDetail == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiBusinessServiceImpl@prestationHorloger : unable to check correspDetail, it's null");
        }

        CalculBusinessModel currentCalculBusinessModel = new CalculBusinessModel();

        currentCalculBusinessModel.setRang(correspDetail.getDetailPrestationModel().getRang());
        currentCalculBusinessModel.setActif(true);
        currentCalculBusinessModel.setCalculResultMontantBase(correspDetail.getDetailPrestationModel().getMontant());
        currentCalculBusinessModel.setCalculResultMontantBaseCaisse(correspDetail.getDetailPrestationModel()
                .getMontantCanton());
        currentCalculBusinessModel.setCalculResultMontantBaseCanton(correspDetail.getDetailPrestationModel()
                .getMontantCanton());
        currentCalculBusinessModel
                .setCalculResultMontantEffectif(correspDetail.getDetailPrestationModel().getMontant());
        currentCalculBusinessModel.setCalculResultMontantEffectifCaisse(correspDetail.getDetailPrestationModel()
                .getMontant());
        currentCalculBusinessModel.setCalculResultMontantEffectifCanton(correspDetail.getDetailPrestationModel()
                .getMontant());
        currentCalculBusinessModel.setDroit(correspDetail.getDroitComplexModel());
        currentCalculBusinessModel.setExportable(true);
        currentCalculBusinessModel.setEcheanceOverLimiteLegale(false);
        currentCalculBusinessModel.setMontantAllocataire("0");
        currentCalculBusinessModel.setMontantAutreParent("0");

        currentCalculBusinessModel.setTarif(correspDetail.getDetailPrestationModel().getCategorieTarif());
        currentCalculBusinessModel.setTarifCaisse(correspDetail.getDetailPrestationModel().getCategorieTarifCaisse());
        currentCalculBusinessModel.setTarifCanton(correspDetail.getDetailPrestationModel().getCategorieTarifCanton());
        currentCalculBusinessModel.setTarifForce(false);
        currentCalculBusinessModel.setType(correspDetail.getDroitComplexModel().getDroitModel().getTypeDroit());

        return currentCalculBusinessModel;
    }

    /**
     * Retourne le d�tail de prestation dont la p�riode et l'enfant li� correspond � ceux pass�s en param�tres
     * 
     * @param detailsContainer
     *            Les d�tails de prestations parmi lesquels rechercher
     * 
     * @param idDroit
     *            droit dont on veut le d�tail
     * @param periode
     *            p�riode du d�tail voulu
     * @return le d�tail de prestation (DetailPrestationComplexModel)
     */
    private ArrayList<DetailPrestationComplexModel> searchDetailInPrestationTravail(
            DetailPrestationComplexSearchModel detailsContainer, String idDroit, String periode) {
        // on r�cup�re le d�tail de prestation de travail qui correspond
        // pour infos tarif et rang
        ArrayList<DetailPrestationComplexModel> listCorrespondingDetailPrestTravail = new ArrayList<DetailPrestationComplexModel>();
        DetailPrestationComplexModel correspondingDetailPrestTravail = null;
        for (int j = 0; j < detailsContainer.getSize(); j++) {
            correspondingDetailPrestTravail = (DetailPrestationComplexModel) detailsContainer.getSearchResults()[j];
            // si on a le bon d�tail (celui li� � l'enfant et bonne
            // p�riode), on arr�te la recherche (exclure les prestation suppl�ment horloger
            if (correspondingDetailPrestTravail.getDetailPrestationModel().getPeriodeValidite().equals(periode)
                    && correspondingDetailPrestTravail.getDroitComplexModel().getDroitModel().getIdDroit()
                            .equals(idDroit)
                    && !correspondingDetailPrestTravail.getDetailPrestationModel().getCategorieTarif()
                            .equals(ALCSTarif.CATEGORIE_SUP_HORLO)) {
                // break;
                listCorrespondingDetailPrestTravail.add(correspondingDetailPrestTravail);
            }
            // si p�riode identique et suppl�emnt horloger
            else if (correspondingDetailPrestTravail.getDetailPrestationModel().getPeriodeValidite().equals(periode)
                    && correspondingDetailPrestTravail.getDroitComplexModel().getDroitModel().getIdDroit()
                            .equals(idDroit)

                    && correspondingDetailPrestTravail.getDetailPrestationModel().getCategorieTarif()
                            .equals(ALCSTarif.CATEGORIE_SUP_HORLO)) {
                // break;
                listCorrespondingDetailPrestTravail.add(correspondingDetailPrestTravail);
            }
        }
        return listCorrespondingDetailPrestTravail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService
     * #supprimerPrestationTravailDossier(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void supprimerPrestationTravailDossier(String idDossier, String periodeDebut, String periodeFin)
            throws JadeApplicationException, JadePersistenceException {

        EntetePrestationSearchModel searchPrest = new EntetePrestationSearchModel();
        searchPrest.setForIdDossier(idDossier);
        searchPrest.setForPeriodeDe(periodeDebut);
        searchPrest.setForPeriodeA(periodeFin);
        searchPrest.setForEtat(ALCSPrestation.ETAT_TMP);
        searchPrest.setForStatut(ALCSPrestation.STATUT_ADI);
        searchPrest.setWhereKey("prestationTravailPlagePeriode");
        searchPrest = ALServiceLocator.getEntetePrestationModelService().search(searchPrest);
        for (int j = 0; j < searchPrest.getSize(); j++) {
            ALServiceLocator.getEntetePrestationModelService().delete(
                    (EntetePrestationModel) searchPrest.getSearchResults()[j]);
        }

    }

    public void creeAnnonceDepuisDecompteADI(List<String> idDecomptesADI) throws JadeApplicationException, JadePersistenceException {
        List<AdiEnfantMoisComplexModel> listTotalAdi = new ArrayList<>();
        for(String idDecompte: idDecomptesADI){
            listTotalAdi.addAll(getDetailDecompteADI(idDecompte));
        }

        Map<String, List<AdiEnfantMoisComplexModel>> adiParDroit = getAdiParDroit(listTotalAdi);

        for(Entry<String, List<AdiEnfantMoisComplexModel>> entry : adiParDroit.entrySet()) {
            // Cr�� les nouvelles annonces
            ALServiceLocator.getAnnonceRafamCreationService().creerAnnoncesADI(entry.getValue());
        }
    }

    private  List<AdiEnfantMoisComplexModel> getDetailDecompteADI(String idDecompteADI) throws JadeApplicationException, JadePersistenceException {
        AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearch = new AdiEnfantMoisComplexSearchModel();
        adiEnfantMoisComplexSearch.setForIdDecompteAdi(idDecompteADI);
        adiEnfantMoisComplexSearch = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(adiEnfantMoisComplexSearch);

        return Arrays.stream(adiEnfantMoisComplexSearch.getSearchResults())
                .map(obj -> (AdiEnfantMoisComplexModel) obj)
                .collect(Collectors.toList());
    }

}
