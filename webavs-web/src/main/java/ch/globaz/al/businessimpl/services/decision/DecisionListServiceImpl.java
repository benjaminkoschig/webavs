/**
 * 
 */
package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.journalisation.constantes.JOConstantes;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionListService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Journalisation;
import ch.globaz.libra.business.model.JournalisationSearch;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.queryexec.bridge.jade.SCM;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Classe d'implémentatiion de DecisionListService Implémentation
 * 
 * @author pta
 * 
 */
public class DecisionListServiceImpl implements DecisionListService {

    @Override
    public StringBuffer getDonneesListDossier(DossierComplexSearchModel listDossier, String dateDebut, String dateFin,
            String periodicite) throws JadePersistenceException, JadeApplicationException {
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

        donnees.append("Liste des rétroactifs (" + periodicite + ") pour la période du " + dateDebut + " au " + dateFin
                + ";" + "\n");
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
            ArrayList<String> listDossier, String csPeriodicite) throws JadePersistenceException,
            JadeApplicationException {
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

        // check de la périodicité ici
        List<List<String>> splitedlistDossierRetroAfaire = Lists.partition(new ArrayList<String>(listDossier), 1000);
        List<String> listHasPeriodicite = new ArrayList<String>();

        for (List<String> idsDossier : splitedlistDossierRetroAfaire) {

            List<String> listIdNotfound = loadIdDossierAndFilterByIdNotLoaded(dateDebut, idsDossier, csPeriodicite);

            if (!listIdNotfound.isEmpty()) {
                if (CodeSystem.PERIODICITE_MENSUELLE.equals(csPeriodicite)) {
                    listHasPeriodicite
                            .addAll(SCM
                                    .newInstance(String.class)
                                    .query("SELECT distinct MATPER as periodicite, schema.ALDOS.EID as idDossier FROM schema.AFAFFIP "
                                            + "INNER JOIN schema.ALDOS ON schema.AFAFFIP.MALNAF = schema.ALDOS.MALNAF "
                                            + "WHERE schema.ALDOS.EID in ("
                                            + Joiner.on(",").join(listIdNotfound)
                                            + ") and (MATPER = " + csPeriodicite + ") OR (IDTIBE <> 0)").execute());
                } else {

                    String dateCompleteFinDernierTrimestre = JadeDateUtil.getYMDDate(JadeDateUtil
                            .getGlobazDate(JadeDateUtil.getLastDateOfMonth(getDateFinPrecedentTrimestre(dateDebut))));

                    listHasPeriodicite
                            .addAll(SCM
                                    .newInstance(String.class)
                                    .query("SELECT distinct MATPER as periodicite, schema.ALDOS.EID as idDossier FROM schema.AFAFFIP "
                                            + "INNER JOIN schema.ALDOS ON schema.AFAFFIP.MALNAF = schema.ALDOS.MALNAF "
                                            + "WHERE schema.ALDOS.EID in ("
                                            + Joiner.on(",").join(listIdNotfound)
                                            + ") and MATPER = "
                                            + csPeriodicite
                                            + " AND IDTIBE = 0 AND EDVAL <= "
                                            + dateCompleteFinDernierTrimestre).execute());
                }

            }

        }

        Set<String> listDossierRetroAfaire = new HashSet<String>();
        for (String idDossier : listDossier) {
            // si pour un des droits aucune prestation n'a été versée récupérer l'id du dossier (parcouri la liste pour
            // voir si il existe et le récupéer s'il n'existe pas
            if (listHasPeriodicite.contains(idDossier)) {
                listDossierRetroAfaire.add(idDossier);
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

    private List<String> loadIdDossierAndFilterByIdNotLoaded(String dateDebut, List<String> idsDossier,
            String periodicite) throws JadeApplicationException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // si présence de droit date de début antérieure recherche de prestations antérieures
        // list de dossier
        DeclarationVersementDetailleSearchComplexModel prestationSearch = new DeclarationVersementDetailleSearchComplexModel();

        prestationSearch.setInIdDossier(idsDossier);

        prestationSearch.setForEtat(ALCSPrestation.ETAT_SA);

        // Check du mode de traitement (mensuel ou trimestriel)
        if (CodeSystem.PERIODICITE_MENSUELLE.equals(periodicite)) {
            prestationSearch.setForDateDebut(dateDebut.substring(3));
            prestationSearch.setWhereKey("prestaRetroActives");
        } else {
            String dateFinPrecedentTrimestriel = getDateFinPrecedentTrimestre(dateDebut);
            prestationSearch.setForDateDebut(dateFinPrecedentTrimestriel);
            prestationSearch.setWhereKey("prestaRetroActivesTrimestriel");
        }

        prestationSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        prestationSearch = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                prestationSearch);

        List<String> existedID = new ArrayList<String>();
        for (JadeAbstractModel model : prestationSearch.getSearchResults()) {
            DeclarationVersementDetailleComplexModel declaration = (DeclarationVersementDetailleComplexModel) model;
            existedID.add(declaration.getIdDossier());
        }

        List<String> idsNotFound = new ArrayList<String>();

        for (String id : idsDossier) {
            if (!existedID.contains(id)) {
                idsNotFound.add(id);
            }
        }
        return idsNotFound;
    }

    private String getDateFinPrecedentTrimestre(String dateDebut) {
        HashMap<String, String> finTrimestreAvant = new HashMap<String, String>();
        finTrimestreAvant.put("01", "12");
        finTrimestreAvant.put("02", "12");
        finTrimestreAvant.put("03", "12");
        finTrimestreAvant.put("04", "03");
        finTrimestreAvant.put("05", "03");
        finTrimestreAvant.put("06", "03");
        finTrimestreAvant.put("07", "06");
        finTrimestreAvant.put("08", "06");
        finTrimestreAvant.put("09", "06");
        finTrimestreAvant.put("10", "09");
        finTrimestreAvant.put("11", "09");
        finTrimestreAvant.put("12", "09");

        String anneeDebut = dateDebut.substring(6);
        String moisDebut = dateDebut.substring(3, 5);

        String moisFinTrimestre = finTrimestreAvant.get(moisDebut);

        if ("12".equals(moisFinTrimestre)) {
            anneeDebut = String.valueOf((Integer.parseInt(anneeDebut) - 1));
        }

        return moisFinTrimestre + "." + anneeDebut;

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
