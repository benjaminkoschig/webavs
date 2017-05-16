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
 * Classe d'impl�mentatiion de DecisionListService Impl�mentation
 * 
 * @author pta
 * 
 */
public class DecisionListServiceImpl implements DecisionListService {

    @Override
    public StringBuffer getDonneesListDossier(DossierComplexSearchModel listDossier, String dateDebut, String dateFin,
            String periodicite) throws JadePersistenceException, JadeApplicationException {
        // contr�le des param�tres
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

        donnees.append("Liste des r�troactifs (" + periodicite + ") pour la p�riode du " + dateDebut + " au " + dateFin
                + ";" + "\n");
        if (listDossier.getSize() == 0) {
            donnees = donnees.append("Aucun dossier trouv� pour cette p�riode");
        } else {

            donnees = donnees.append("Numero Affili�" + ";");
            donnees = donnees.append("Numero Dossier" + ";");
            donnees = donnees.append("NSS Allocataire" + ";");
            donnees = donnees.append("Nom Allocataire" + ";");
            donnees = donnees.append("Pr�nom Allocataire" + ";");
            donnees = donnees.append("\n");

            for (int i = 0; i < listDossier.getSize(); i++) {
                DossierComplexModel dossier = (DossierComplexModel) listDossier.getSearchResults()[i];
                // num�ro de l'affili�
                donnees = donnees.append(dossier.getDossierModel().getNumeroAffilie() + ";");
                // num�ro du dossier
                donnees = donnees.append(dossier.getDossierModel().getIdDossier() + ";");
                // nss de l'allocataire
                donnees = donnees.append(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel()
                        + ";");
                // nom de l'allocataire
                donnees = donnees.append(dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation1()
                        + ";");
                // pr�nom de l'allocataire
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
        // contr�le des param�tre
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

        // // recherche des dossier journalis�s dans l'applciation libra
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
        // contr�le des param�tres
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

        // Todo : check la p�riodicit� ici
        List<List<String>> splitedlistDossierRetroAfaire = Lists.partition(new ArrayList<String>(listDossier), 1000);
        List<String> listHasPeriodicite = new ArrayList<String>();

        for (List<String> idsDossier : splitedlistDossierRetroAfaire) {

            List<String> listIdNotfound = loadIdDossierAndFilterByIdNotLoaded(dateDebut, idsDossier, csPeriodicite);

            if (!listIdNotfound.isEmpty()) {
                listHasPeriodicite
                        .addAll(SCM
                                .newInstance(String.class)
                                .query("SELECT distinct MATPER as periodicite, schema.ALDOS.EID as idDossier FROM schema.AFAFFIP "
                                        + "INNER JOIN schema.ALDOS ON schema.AFAFFIP.MALNAF = schema.ALDOS.MALNAF "
                                        + "WHERE schema.ALDOS.EID in ("
                                        + Joiner.on(",").join(listIdNotfound)
                                        + ") and MATPER = " + csPeriodicite).execute());
            }

        }

        Set<String> listDossierRetroAfaire = new HashSet<String>();
        for (String idDossier : listDossier) {
            // si pour un des droits aucune prestation n'a �t� vers�e r�cup�rer l'id du dossier (parcouri la liste pour
            // voir si il existe et le r�cup�er s'il n'existe pas
            if (listHasPeriodicite.contains(idDossier)) {
                listDossierRetroAfaire.add(idDossier);
            }
        }

        // on recharge les dossiers selon les id stock�s en appliquant le tri d�sir�
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
        // si pr�sence de droit date de d�but ant�rieure recherche de prestations ant�rieures
        // list de dossier
        DeclarationVersementDetailleSearchComplexModel prestationSearch = new DeclarationVersementDetailleSearchComplexModel();

        prestationSearch.setInIdDossier(idsDossier);

        prestationSearch.setForEtat(ALCSPrestation.ETAT_SA);
        prestationSearch.setForDateDebut(dateDebut.substring(3));

        // Check du mode de traitement (mensuel ou trimestriel)
        if (CodeSystem.PERIODICITE_MENSUELLE.equals(periodicite)) {
            prestationSearch.setWhereKey("prestaRetroActives");
        } else {
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
        // prise en compte uniquement des dossiers dont date de d�but validit� <= mois derni�re factu
        if (!JadeStringUtil.isEmpty(moisAnneeDebValiditeDossier)
                && (JadeDateUtil.isDateMonthYearBefore(moisAnneeDebValiditeDossier, moisAnneeDateCtrl) || JadeDateUtil
                        .areDatesEquals("01.".concat(moisAnneeDebValiditeDossier), "01.".concat(moisAnneeDateCtrl)))) {

            checkable = true;

        }
        // TODO: checker les cas de certificats de radiation
        // si fin validit� < p�riode derni�re factu, le dossier devra para�tre sur la liste si il n'y a pas de
        // prestation moisfactu extourne SA
        // et qu'il y prestation CO moisfactu
        /*
         * if (!JadeStringUtil.isEmpty(moisAnneeFinValiditeDossier) &&
         * JadeDateUtil.isDateMonthYearBefore(moisAnneeFinValiditeDossier, moisAnneeDateCtrl)) { checkable = true; }
         */

        return checkable;

    }
}
