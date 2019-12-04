package ch.globaz.al.businessimpl.services.affiliation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.affiliations.ALAffiliationException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.affiliation.RadiationAffilieService;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;
import ch.globaz.al.business.services.models.droit.DroitModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import org.apache.commons.lang.StringUtils;

public class RadiationAffilieServiceImpl extends ALAbstractBusinessServiceImpl implements RadiationAffilieService {

    @Override
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> copierDossier(DossierComplexModel dossier,
            String numAffilie, String dateDebutActivite) throws JadeApplicationException, JadePersistenceException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#copierDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.param.undefined", new String[] { "dossier" }));
        }

        if (JadeStringUtil.isBlankOrZero(numAffilie)) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#copierDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.param.undefined", new String[] { "numAffilie" }));
        }

        if (!JadeDateUtil.isGlobazDate(dateDebutActivite)) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#copierDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "dateDebutActivite" }));
        }

        DossierBusinessService dossierBusinessService = ALImplServiceLocator.getDossierBusinessService();
        DroitModelService droitModelService = ALImplServiceLocator.getDroitModelService();

        HashMap<ALEnumProtocoleRadiationAffilie, Object> result = new HashMap<ALEnumProtocoleRadiationAffilie, Object>();

        // création du nouveau dossier
        // ATTENTION, la méthode copierDossier ne fait pas une véritable copie, elle utilise la référence sur le dossier
        // d'origine. Après ce point, ne plus utiliser 'dossier' mais uniquement 'newDossier'
        DossierComplexModel newDossier = dossierBusinessService.copierDossier(dossier, ALCSDossier.ETAT_SUSPENDU);

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {

            // mise à jour des droits
            DroitSearchModel searchDroits = new DroitSearchModel();
            searchDroits.setForIdDossier(newDossier.getId());
            searchDroits.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchDroits = droitModelService.search(searchDroits);

            int nbDroits = 0;
            int nbDroitsActifs = 0;
            ArrayList<String> idDroits = new ArrayList<String>();

            for (JadeAbstractModel item : searchDroits.getSearchResults()) {
                DroitModel droit = (DroitModel) item;

                if (JadeDateUtil.isDateBefore(dateDebutActivite, droit.getFinDroitForcee())) {
                    droit.setDebutDroit(dateDebutActivite);
                    droit.setEtatDroit(ALCSDroit.ETAT_A);
                    nbDroitsActifs++;
                } else {
                    droit.setEtatDroit(ALCSDroit.ETAT_S);
                }

                idDroits.add(droit.getIdDroit());

                nbDroits++;

                droitModelService.update(droit);
            }

            result.put(ALEnumProtocoleRadiationAffilie.NB_DROITS, nbDroits);
            result.put(ALEnumProtocoleRadiationAffilie.NB_DROITS_ACTIFS, nbDroitsActifs);

            // mise à jour du dossier
            String etatDossier = (nbDroitsActifs > 0 ? ALCSDossier.ETAT_ACTIF : ALCSDossier.ETAT_SUSPENDU);
            newDossier.getDossierModel().setNumeroAffilie(numAffilie);
            newDossier.getDossierModel().setDebutActivite(dateDebutActivite);
            newDossier.getDossierModel().setDebutValidite(dateDebutActivite);
            newDossier.getDossierModel().setFinActivite(null);
            newDossier.getDossierModel().setFinValidite(null);
            newDossier.getDossierModel().setEtatDossier(etatDossier);
            newDossier = dossierBusinessService.updateDossier(newDossier, null, "");

            // ajout des copies par défaut
            ALServiceLocator.getCopiesBusinessService().createDefaultCopies(newDossier, ALCSCopie.TYPE_DECISION);

            result.put(ALEnumProtocoleRadiationAffilie.DOSSIER, newDossier);

            // recherche du nombre d'annonces
            if (idDroits.size() == 0) {
                result.put(ALEnumProtocoleRadiationAffilie.NB_ANNONCES, 0);
            } else {
                AnnonceRafamSearchModel searchRAFam = new AnnonceRafamSearchModel();
                searchRAFam.setInIdDroit(idDroits);
                searchRAFam.setForEtatAnnonce(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
                result.put(ALEnumProtocoleRadiationAffilie.NB_ANNONCES, ALServiceLocator.getAnnonceRafamModelService()
                        .count(searchRAFam));
            }

        }

        return result;
    }

    @Override
    public DetailPrestationComplexSearchModel genererPrestationForDossier(DossierComplexModel dossier,
            boolean hasTransfert, boolean isFromEbu) throws JadeApplicationException, JadePersistenceException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#genererPrestationForDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.param.undefined", new String[] { "dossier" }));
        }

        String moisRadiationDossier = JadeDateUtil.convertDateMonthYear(dossier.getDossierModel().getFinValidite());
        String moisCourant = JadeDateUtil.convertDateMonthYear(JadeDateUtil.getGlobazFormattedDate(new Date()));

        if (JadeDateUtil.isDateMonthYearBefore(moisRadiationDossier, moisCourant)) {

            // préparation des périodes
            int nbMonthToAdd = 1;
            // Si la génération provient du module E-Business, les prestations doivent être générées depuis le mois concerné
            // Notamment si la date de radiation est durant le mois et non le dernier jour du mois (restit au prorata)
            if(isFromEbu && !isDateRadiationLastDayOfMonth(dossier.getDossierModel().getFinValidite())){
                nbMonthToAdd = 0;
            }
            String debutPeriode = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                    JadeDateUtil.getFirstDateOfMonth(dossier.getDossierModel().getFinValidite()), nbMonthToAdd));
            String finPeriode = JadeDateUtil.convertDateMonthYear(JadeDateUtil.getGlobazFormattedDate(new Date()));

            // préparation du type de bonification
            Bonification bonification = Bonification.AUTO;
            if (ALImplServiceLocator.getDossierBusinessService().isModePaiementDirect(dossier.getDossierModel())) {
                if (hasTransfert) {
                    bonification = Bonification.EXTOURNE;
                } else {
                    bonification = Bonification.RESTITUTION;
                }
            } else if (ALCSDossier.STATUT_IS.equals(dossier.getDossierModel().getStatut())) {
                bonification = Bonification.EXTOURNE;
            }

            // génération
            ALServiceLocator.getGenerationDossierService().generationDossier(dossier, null, debutPeriode, finPeriode,
                    debutPeriode, finPeriode, "0", bonification, "1", null, null);

            // recherche des prestation créées
            DetailPrestationComplexSearchModel search = new DetailPrestationComplexSearchModel();
            search.setForIdDossier(dossier.getId());
            search.setForEtat(ALCSPrestation.ETAT_SA);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            return ALServiceLocator.getDetailPrestationComplexModelService().search(search);
        } else {
            return new DetailPrestationComplexSearchModel();
        }

    }

    /**
     * Méthode permettant de vérifier si la date passée en paramètre est le dernier jour du mois
     * Point PCA-602, si la radiation est le dernier jour du mois, pas besoin de créer de restit pour ce mois-là
     *
     * @param finValidite
     * @return Vrai si la date (dd.MM.yyyy) est le dernier jour du mois
     */
    private boolean isDateRadiationLastDayOfMonth(String finValidite) {
        return StringUtils.equals(finValidite, JadeDateUtil.getLastDateOfMonth(finValidite));
    }

    @Override
    public DossierComplexSearchModel getDossiersForAffilie(String numAffilie, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlank(numAffilie)) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#getDossiersForAffilie : "
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "ch.globaz.al.businessimpl.services.affiliation.RadiationAffilieServiceImpl.numAffilie"));
        }

        if (!JadeDateUtil.isGlobazDate(dateRadiation)) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#getDossiersForAffilie : "
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { dateRadiation }));
        }

        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForNumeroAffilie(numAffilie);
        search.setForFinValidite(dateRadiation);
        search.setWhereKey(DossierComplexSearchModel.SEARCH_RADIATION_AFFILIE);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return (DossierComplexSearchModel) JadePersistenceManager.search(search);
    }

    @Override
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> radierDossier(DossierComplexModel dossier,
            String dateRadiation, String reference) throws JadeApplicationException, JadePersistenceException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#radierDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.param.undefined", new String[] { "dossier" }));
        }

        if (!JadeDateUtil.isGlobazDate(dateRadiation)) {
            throw new ALAffiliationException("RadiationAffilieServiceImpl#radierDossier"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "dateRadiation" }));
        }

        HashMap<ALEnumProtocoleRadiationAffilie, Object> result = new HashMap<ALEnumProtocoleRadiationAffilie, Object>();

        dossier.setDossierModel(ALImplServiceLocator.getDossierBusinessService().radierDossier(
                dossier.getDossierModel(), dateRadiation, true,
                ALConstJournalisation.CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AFFILIE, reference));

        result.put(ALEnumProtocoleRadiationAffilie.DOSSIER, dossier);

        // récupération du nombre de droits
        DroitSearchModel searchDroit = new DroitSearchModel();
        searchDroit.setForIdDossier(dossier.getId());
        searchDroit.setForDateEcheanceForcee(dateRadiation);
        searchDroit.setWhereKey(DroitSearchModel.SEARCH_DROIT_RADIES);
        searchDroit.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchDroit = ALImplServiceLocator.getDroitModelService().search(searchDroit);
        result.put(ALEnumProtocoleRadiationAffilie.NB_DROITS_RADIES, searchDroit.getSize());

        // récupération du nombre d'annonces
        ArrayList<String> idDroits = new ArrayList<String>();
        for (JadeAbstractModel droit : searchDroit.getSearchResults()) {
            idDroits.add(((DroitModel) droit).getIdDroit());
        }

        if (idDroits.size() > 0) {
            AnnonceRafamSearchModel searchRAFam = new AnnonceRafamSearchModel();
            searchRAFam.setInIdDroit(idDroits);
            searchRAFam.setForEtatAnnonce(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
            result.put(ALEnumProtocoleRadiationAffilie.NB_ANNONCES, ALServiceLocator.getAnnonceRafamModelService()
                    .count(searchRAFam));
        } else {
            result.put(ALEnumProtocoleRadiationAffilie.NB_ANNONCES, 0);
        }

        return result;
    }
}
