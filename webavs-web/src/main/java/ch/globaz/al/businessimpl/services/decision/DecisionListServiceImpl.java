/**
 * 
 */
package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.journalisation.constantes.JOConstantes;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionListService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Journalisation;
import ch.globaz.libra.business.model.JournalisationSearch;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Classe d'implémentatiion de DecisionListService Implémentation
 * 
 * @author pta
 * 
 */
public class DecisionListServiceImpl implements DecisionListService {

    @Override
    public StringBuffer getDonneesListDossier(DossierComplexSearchModel listDossier, String dateDebut, String dateFin)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètres
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDecisionException("DecisionListServiceImpl#getDonneesListDossier: dateDebut: " + dateDebut
                    + " is not a valid date");

        }
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDecisionException("DecisionListServiceImpl#getDonneesListDossier: dateDebut " + dateFin
                    + " is not a valid date");

        }
        if (listDossier == null) {
            throw new ALDecisionException("DecisionListServiceImpl#getDonneesListDossier:listDossier is null");
        }

        StringBuffer donnees = new StringBuffer();

        donnees.append("Liste des rétroactifs pour la période du " + dateDebut + " au " + dateFin + ";" + "\n");
        if (listDossier.getSize() == 0) {
            donnees = donnees.append("Aucun dossier trouvé pour cette période");
        } else {

            donnees = donnees.append("Numero Affilié" + ";");
            donnees = donnees.append("Numero Dossier" + ";");
            donnees = donnees.append("NSS Allocataire" + ";");
            donnees = donnees.append("Nom Allocataire" + ";");
            donnees = donnees.append("Prénom Allocataire" + ";");
            donnees = donnees.append("\n");

            for (int i = 0; i < listDossier.getSize(); i++) {
                DossierComplexModel dossier = (DossierComplexModel) listDossier.getSearchResults()[i];
                // numéro de l'affilié
                donnees = donnees.append(dossier.getDossierModel().getNumeroAffilie() + ";");
                // numéro du dossier
                donnees = donnees.append(dossier.getDossierModel().getIdDossier() + ";");
                // nss de l'allocataire
                donnees = donnees.append(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel()
                        + ";");
                // nom de l'allocataire
                donnees = donnees.append(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation1()
                        + ";");
                // prénom de l'allocataire
                donnees = donnees.append(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation2()
                        + ";");
                donnees = donnees.append("\n");
            }

        }

        return donnees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.decision.DecisionListService#getListDossier(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<String> getListDossierJournaliser(String dateDebut, String dateFin)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètre
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDecisionException("DecisionListServiceImpl#getListDossier: dateDebut: " + dateDebut
                    + " is not a valid date");

        }
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDecisionException("DecisionListServiceImpl#getListDossier: dateDebut " + dateFin
                    + " is not a valid date");

        }

        ArrayList<String> listDossier = new ArrayList<String>();
        // recherche des dossiers ayant fit objet d'une journalisation entre les deux dates

        // // recherche des dossier journalisés dans l'applciation libra
        JournalisationSearch searchJournalisation = new JournalisationSearch();

        searchJournalisation.setForDateReceptionInitial(dateDebut);
        searchJournalisation.setForDateReceptionFinal(dateFin);
        searchJournalisation.setForCsDomaine(ILIConstantesExternes.CS_DOMAINE_AF);
        searchJournalisation.setForLibelle(ALConstJournalisation.DECISION_MOTIF_JOURNALISATION);
        searchJournalisation.setForCsType(JOConstantes.CS_JO_FMT_AUTOMATIQUE);

        List<Journalisation> listJournalisations = new ArrayList<Journalisation>();
        try {
            listJournalisations = LibraServiceLocator.getJournalisationService().search(searchJournalisation,
                    BManager.SIZE_NOLIMIT);
        } catch (LibraException e) {
            throw new ALDecisionException(
                    "DecisionListServiceImpl#getListDossier:probleme with LibraService: exception  " + e);
        }
        for (int i = 0; i < listJournalisations.size(); i++) {
            Journalisation ech = listJournalisations.get(i);
            String idDossier = ech.getIdExterne();

            if (isDossierCheckable(idDossier, dateDebut)) {
                listDossier.add(ech.getIdExterne());
            }
        }

        return listDossier;
    }

    @Override
    public DossierComplexSearchModel getListDossierRetroActif(String dateDebut, String dateFin,
            ArrayList<String> listDossier) throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètres
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDecisionException("DecisionListServiceImpl#getListDossierretroActif: dateDebut: " + dateDebut
                    + " is not a valid date");

        }
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDecisionException("DecisionListServiceImpl#getListDossierretroActif: dateDebut " + dateFin
                    + " is not a valid date");

        }
        if (listDossier == null) {
            throw new ALDecisionException("DecisionListServiceImpl#getListDossierretroActif: listDossier is null");
        }

        // si présence de droit date de début antérieure recherche de prestations antérieures
        // list de dossier
        ArrayList<String> listDossierRetroAfaire = new ArrayList<String>();
        for (String idDossier : listDossier) {

            DeclarationVersementDetailleSearchComplexModel prestationSearch = new DeclarationVersementDetailleSearchComplexModel();

            prestationSearch.setForIdDossier(idDossier);
            prestationSearch.setForEtat(ALCSPrestation.ETAT_SA);
            prestationSearch.setForDateDebut(dateDebut.substring(3));
            prestationSearch.setWhereKey("prestaRetroActives");
            prestationSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            prestationSearch = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    prestationSearch);

            // si pour un des droits aucune prestation n'a été versée récupérer l'id du dossier (parcouri la liste pour
            // voir si il existe et le récupéer s'il n'existe pas
            if (prestationSearch.getSize() == 0) {
                if (!listDossierRetroAfaire.contains(idDossier)) {
                    listDossierRetroAfaire.add(idDossier);
                }
            }

        }

        // on recharge les dossiers selon les id stockés en appliquant le tri désiré
        DossierComplexSearchModel listDossierComplex = new DossierComplexSearchModel();

        if ((listDossierRetroAfaire.size() > 0)) {

            listDossierComplex.setInIdDossier(listDossierRetroAfaire);
            listDossierComplex.setWhereKey("listDossier");
            listDossierComplex.setOrderKey("numAffilieAlloc");
            listDossierComplex.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            listDossierComplex = ALServiceLocator.getDossierComplexModelService().search(listDossierComplex);
        }

        return listDossierComplex;
    }

    private boolean isDossierCheckable(String idDossier, String dateCtrl) {
        boolean checkable = false;

        DossierModel currentDossier;
        try {
            currentDossier = ALServiceLocator.getDossierModelService().read(idDossier);
        } catch (Exception e) {
            return false;
        }
        String moisAnneeDebValiditeDossier = JadeDateUtil.isGlobazDate(currentDossier.getDebutValidite()) ? currentDossier
                .getDebutValidite().substring(3) : "";
        String moisAnneeFinValiditeDossier = JadeDateUtil.isGlobazDate(currentDossier.getFinValidite()) ? currentDossier
                .getFinValidite().substring(3) : "";

        String moisAnneeDateCtrl = dateCtrl.substring(3);
        // prise en compte uniquement des dossiers dont date de début validité <= mois dernière factu
        if (!JadeStringUtil.isEmpty(moisAnneeDebValiditeDossier)
                && (JadeDateUtil.isDateMonthYearBefore(moisAnneeDebValiditeDossier, moisAnneeDateCtrl) || JadeDateUtil
                        .areDatesEquals("01.".concat(moisAnneeDebValiditeDossier), "01.".concat(moisAnneeDateCtrl)))) {

            checkable = true;

        }
        // TODO: checker les cas de certificats de radiation
        // si fin validité < période dernière factu, le dossier devra paraître sur la liste si il n'y a pas de
        // prestation moisfactu extourne SA
        // et qu'il y prestation CO moisfactu
        /*
         * if (!JadeStringUtil.isEmpty(moisAnneeFinValiditeDossier) &&
         * JadeDateUtil.isDateMonthYearBefore(moisAnneeFinValiditeDossier, moisAnneeDateCtrl)) { checkable = true; }
         */

        return checkable;

    }
}
