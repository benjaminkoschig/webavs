package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.calculmoissuivant.CalculMoisSuivantBuilder;
import globaz.pegasus.utils.calculmoissuivant.DonneeFinanciereDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculMoisSuivantService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class CalculMoisSuivantServiceImpl extends PegasusAbstractServiceImpl implements CalculMoisSuivantService {

    /**
     * Mise à jour de la date de fin de la donnée financiere précédente. Doit obligatoirement être appelé APRES la mise
     * à jour de la date de debut de la donne courante.
     * 
     * @param dfToUpdate
     * @param date
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    private void createDonneFinanciereHeaderDateDeFin(AbstractDonneeFinanciereModel dfToUpdate, String idVersionDroit,
            String date, JadeApplicationService service) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException {

        // Mise a jour date de fin
        dfToUpdate.getSimpleDonneeFinanciereHeader().setDateFin(date);
        dfToUpdate.getSimpleDonneeFinanciereHeader().setIdVersionDroit(idVersionDroit);
        dfToUpdate.setIsNew();
        // creation
        PegasusServiceLocator.getDroitService().saveDonneefinanciereCalculMoisSuivant(dfToUpdate, service);
    }

    /**
     * Retourne une instance du Builder pour le calcul mois suivant
     */
    @Override
    public CalculMoisSuivantBuilder getCalculMoisSuivantBuilder() {
        return new CalculMoisSuivantBuilder();
    }

    /**
     * Retourne la date de fin a setter à la donné financiere précdéente (dateProchainPaieemt -1) Format mm.aaaa
     * 
     * @param dateProchainPaiement
     * @return
     */
    private String getDateFinToSetToDonneeFinancierePrecedente(String dateProchainPaiement) {
        // on retourne la date, format mm.aaaa, de la date du prochain paiement - 1 mois
        return JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateProchainPaiement, -1));
    }

    @Override
    public void updateDonneeFinancieres(List<String> idsDfForVersion, String noVersion) throws CalculException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            PmtMensuelException, SecurityException, NoSuchMethodException, DroitException {

        Map<String, DonneeFinanciereDescriptor> dfDescriptor = CalculMoisSuivantBuilder
                .getDonneFinanciereDroitDescriptor();
        // la liste ne doit pas être nulle, instancié par défaut
        if (idsDfForVersion == null) {
            throw new CalculException(
                    "Unable to deal donneeFinacieres for Calcul mois suivants, the list of ids df passed is null [CalculMoisSuivant]");
        }

        // On ne traite que si la liste contient bien des données (idEntityGroup)
        if (idsDfForVersion.size() > 0) {
            // On s'assure qu'il n'y a pas deux idEntityGroup identique
            Set<String> idsSet = new HashSet<String>(idsDfForVersion);
            if (idsSet.size() != idsDfForVersion.size()) {
                throw new CalculException(
                        "The list of the id's entity group contain more than one same idDEntityGroup [CalculMoisSuivant]");
            }

            // recherche des données financières
            DonneeFinanciereHeaderSearch dfSearch = new DonneeFinanciereHeaderSearch();
            dfSearch.setWhereKey("dfForModificationDateCMS");
            dfSearch.setOrderBy("byDateDebut");
            dfSearch.setForIdEntityGroupIn(idsDfForVersion);
            dfSearch.setForNumeroVersion(noVersion);
            dfSearch = PegasusImplServiceLocator.getDonneeFinanciereHeaderService().search(dfSearch);

            // Si recherche vide
            if (dfSearch.getSearchResults().length == 0) {
                throw new CalculException(
                        "The search results list contaisn no results when it should contain [CalculMoisSuivant]");
            }

            // groupement par membre famille
            // Groupement de df par idEntityGroup
            List<DonneeFinanciereHeader> listeDF = PersistenceUtil.typeSearch(dfSearch, DonneeFinanciereHeader.class);
            // map groupé par membre famille
            // map groupés
            Map<String, List<DonneeFinanciereHeader>> dfsByMembreFamilleGroup = JadeListUtil.groupBy(listeDF,
                    new Key<DonneeFinanciereHeader>() {
                        @Override
                        public String exec(DonneeFinanciereHeader df) {
                            return df.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
                        }
                    });
            String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Iteration sur les membre de famille
            for (String idMembreFamille : dfsByMembreFamilleGroup.keySet()) {
                // Groupement de df par idEntityGroup
                List<DonneeFinanciereHeader> listeDFMembreFamille = PersistenceUtil.typeSearch(dfSearch,
                        DonneeFinanciereHeader.class);
                // map groupés
                Map<String, List<DonneeFinanciereHeader>> dfsByEntityGroup = JadeListUtil.groupBy(
                        dfsByMembreFamilleGroup.get(idMembreFamille), new Key<DonneeFinanciereHeader>() {
                            @Override
                            public String exec(DonneeFinanciereHeader df) {
                                return df.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
                            }
                        });

                // Iteration et traitement des lignes (entityGroup)
                for (String idEntityGroup : dfsByEntityGroup.keySet()) {

                    DonneeFinanciereHeader donneePrecedente = null;
                    DonneeFinanciereHeader donneeCourante = null;

                    // ************************************ PlUS DE 2 DF RETOURNEES
                    // ***************************************
                    if (dfsByEntityGroup.get(idEntityGroup).size() > 2) {
                        // la premiere est de toute façon la donnee courante
                        donneeCourante = dfsByEntityGroup.get(idEntityGroup).get(0);
                        // on itere sur les autres
                        boolean dfIdVersionFind = false;
                        // ************************** Recherche si idVersionDroit egal ***********************//
                        for (int cpt = 1; cpt < dfsByEntityGroup.get(idEntityGroup).size(); cpt++) {
                            // Si id version droit egal, ok
                            DonneeFinanciereHeader df = dfsByEntityGroup.get(idEntityGroup).get(cpt);
                            if (df.getSimpleDonneeFinanciereHeader().getIdVersionDroit()
                                    .equals(donneeCourante.getSimpleDonneeFinanciereHeader().getIdVersionDroit())
                                    && !dfIdVersionFind) {
                                // si on a dejà trouvé une df
                                if (dfIdVersionFind) {
                                    throw new CalculException(
                                            "More than 2 DonneFinacieres found for the same idEntityGroup [CalculMoisSuivant]");
                                }
                                donneePrecedente = df;
                                dfIdVersionFind = true;
                            }
                        }

                        // si pas d'autres df trouvée, recheche sur una autre version de droit avec date
                        if (!dfIdVersionFind) {
                            String dateFinDonneeRecherche = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                                    "01." + donneeCourante.getSimpleDonneeFinanciereHeader().getDateDebut(), -1));
                            // on recherche une df avec une date de fin egale
                            for (int cpt = 1; cpt < dfsByEntityGroup.get(idEntityGroup).size(); cpt++) {
                                // Si id version droit egal, ok
                                DonneeFinanciereHeader df = dfsByEntityGroup.get(idEntityGroup).get(cpt);
                                if (df.getSimpleDonneeFinanciereHeader().getDateFin().equals(dateFinDonneeRecherche)) {
                                    // si on a dejà trouvé une df
                                    if (dfIdVersionFind) {
                                        throw new CalculException(
                                                "More than 2 DonneFinacieres found for the same idEntityGroup [CalculMoisSuivant]");
                                    }
                                    donneePrecedente = df;
                                    dfIdVersionFind = true;
                                }
                            }
                        }

                        // Si pas de df trouvées
                        if (!dfIdVersionFind) {
                            throw new CalculException("Only 1 df find, 2 expected [CalculMoisSuivant]");
                        }
                        // update standard
                        updateDonneFinanciereHeaderDateDeDebut(donneeCourante, dateProchainPaiement);
                        updateDonneFinanciereHeaderDateDeFin(donneePrecedente, dateProchainPaiement);
                    }
                    // ************************************ 2 DF RETROUNEES ***************************************
                    else if (dfsByEntityGroup.get(idEntityGroup).size() == 2) {
                        // la premiere est de toute façon la donnee courante
                        donneeCourante = dfsByEntityGroup.get(idEntityGroup).get(0);
                        // Si la version de la deuuxieme n'est pas celle du droit on doit upadter via le service du
                        // droit
                        donneePrecedente = (dfsByEntityGroup).get(idEntityGroup).get(1);
                        if (donneeCourante.getSimpleDonneeFinanciereHeader().getIdVersionDroit()
                                .equals(donneePrecedente.getSimpleDonneeFinanciereHeader().getIdVersionDroit())) {
                            // update standard
                            updateDonneFinanciereHeaderDateDeDebut(donneeCourante, dateProchainPaiement);
                            updateDonneFinanciereHeaderDateDeFin(donneePrecedente, dateProchainPaiement);
                        } else {

                            AbstractDonneeFinanciereModel df = null;
                            // Chargement de l'entite a recreer
                            JadeApplicationService serviceForDf = dfDescriptor.get(
                                    donneeCourante.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere())
                                    .getService();
                            // introspection sur la methode read, on est certain du type passé en param --> String
                            Method methodRead = serviceForDf.getClass().getMethod("readByIdDonneeFinanciereHeader",
                                    new Class[] { String.class });

                            try {
                                // modèle abstrait de la donnée financiere
                                df = (AbstractDonneeFinanciereModel) methodRead.invoke(serviceForDf,
                                        new Object[] { donneePrecedente.getSimpleDonneeFinanciereHeader()
                                                .getIdDonneeFinanciereHeader() });

                            } catch (IllegalArgumentException e) {
                                JadeThread.logError(
                                        toString(),
                                        "Problem during reflection!. IllegalArgumentException occured: "
                                                + e.getMessage() + e.getStackTrace());
                            } catch (IllegalAccessException e) {
                                JadeThread.logError(
                                        toString(),
                                        "Problem during reflection!. IllegalArgumentException occured: "
                                                + e.getMessage() + e.getStackTrace());
                            } catch (InvocationTargetException e) {
                                JadeThread.logError(
                                        toString(),
                                        "Problem during reflection!. InvocationTargetsException occured: "
                                                + e.getMessage() + e.getStackTrace());
                            }

                            // update standard donne courante
                            updateDonneFinanciereHeaderDateDeDebut(donneeCourante, dateProchainPaiement);
                            // Create donne financiere posant problème
                            createDonneFinanciereHeaderDateDeFin(df, donneeCourante.getSimpleDonneeFinanciereHeader()
                                    .getIdVersionDroit(),
                                    getDateFinToSetToDonneeFinancierePrecedente(dateProchainPaiement), serviceForDf);

                        }

                    }
                    // ************************************ 1 DF RETROUNEE ***************************************
                    else if (dfsByEntityGroup.get(idEntityGroup).size() == 1) {
                        DonneeFinanciereHeader df = dfsByEntityGroup.get(idEntityGroup).get(0);

                        // si la donnée financière à une date de fin calcul 100% retro, on ne la prend pas en compte
                        if (JadeStringUtil.isBlankOrZero(df.getSimpleDonneeFinanciereHeader().getDateFin())) {
                            updateDonneFinanciereHeaderDateDeDebut(dfsByEntityGroup.get(idEntityGroup).get(0),
                                    dateProchainPaiement);
                        }
                    }

                }
            }

        }

    }

    /**
     * Mise à jour de la date de debut de la donne courante.
     * 
     * @param dfToUpdate
     * @param date
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDonneFinanciereHeaderDateDeDebut(DonneeFinanciereHeader dfToUpdate, String date)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Mise a jour date de debut
        dfToUpdate.getSimpleDonneeFinanciereHeader().setDateDebut(date);
        // update
        dfToUpdate.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                .update(dfToUpdate.getSimpleDonneeFinanciereHeader()));

    }

    /**
     * Mise à jour de la date de fin de la donnée financiere précédente. Doit obligatoirement être appelé APRES la mise
     * à jour de la date de debut de la donne courante.
     * 
     * @param dfToUpdate
     * @param date
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void updateDonneFinanciereHeaderDateDeFin(DonneeFinanciereHeader dfToUpdate, String date)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Mise a jour date de fin
        dfToUpdate.getSimpleDonneeFinanciereHeader().setDateFin(getDateFinToSetToDonneeFinancierePrecedente(date));
        // update
        dfToUpdate.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                .update(dfToUpdate.getSimpleDonneeFinanciereHeader()));
    }

}
