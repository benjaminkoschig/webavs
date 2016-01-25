package ch.globaz.al.businessimpl.services.importation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.exceptions.business.ALAllocataireBusinessException;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireAgricoleComplexModelException;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireComplexModelException;
import ch.globaz.al.business.exceptions.model.allocataire.ALRevenuModelException;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantComplexModelException;
import ch.globaz.al.business.exceptions.model.personne.ALPersonneAFComplexModelException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;
import ch.globaz.al.business.services.importation.ImportationAllocataireService;
import ch.globaz.al.business.services.models.allocataire.AgricoleModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;

/**
 * Implémentation du service d'importation des données allocataire
 * 
 * @author jts
 * 
 */
public class ImportationAllocataireServiceImpl extends ALAbstractBusinessServiceImpl implements
        ImportationAllocataireService {

    // TODO récativer ce code pour autre caisse que FVE
    // return !(!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation1()),
    // JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation1()), true)
    // || (!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation2()),
    // JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation2()), true))
    // || (!JadeStringUtil.equals(persDB.getPersonne().getDateNaissance(), persXML.getPersonne()
    // .getDateNaissance(), false)) || (!JadeStringUtil.equals(persDB.getTiers().getIdPays(), persXML
    // .getTiers().getIdPays(), false)));
    // }

    @Override
    public boolean checkTiersData(PersonneEtendueComplexModel persDB, PersonneEtendueComplexModel persXML,
            String idDossier, String type, String idPersonnePhyAlfagest) throws Exception {

        // TODO spécial FVE, voir ci-sessous code désactivé pour les autres caisse
        // if (!JadeStringUtil.isBlank(ALImportUtils.caisse) && "FVE".equals(ALImportUtils.caisse.toUpperCase())) {
        //
        // if ((this.donneesTierAllocConcordante(persDB, persXML))) {
        //
        // // constAllocataire etALImportUtils.SYMB_ALLOC
        // // Récupére l'idAllocAlfagest, idTiers constAllocataire etALImportUtils.SYMB_ALLOC
        // ImportDossiers.protocole.getWarningsLogger(idDossier, "warn").addMessage(
        // new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "dossier ", idDossier + ";"
        // + persDB.getTiers().getIdTiers() + ";" + idPersonnePhyAlfagest + ";" + /*
        // * ALImportUtils.
        // * SYMB_ALLOC
        // */type));
        // return true;
        // }

        // else if (!JadeStringUtil.equals(persDB.getTiers().getIdPays(), persXML.getTiers().getIdPays(), false)) {
        // persDB.getTiers().setIdPays(persXML.getTiers().getIdPays());
        // persDB.setDateModifPays(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // persDB.setMotifModifPays(motifsModification.MOTIF_INCONNU);
        // persDB = TIBusinessServiceLocator.getPersonneEtendueService().update(persDB);
        // }

        return !(!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation1()),
                JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation1()), true)
                || (!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation2()),
                        JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation2()), true)) || (!JadeStringUtil
                    .equals(persDB.getPersonne().getDateNaissance(), persXML.getPersonne().getDateNaissance(), false)));

        // } else {
        // return !(!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation1()),
        // JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation1()), true)
        // || (!JadeStringUtil.equals(JadeStringUtil.convertSpecialChars(persDB.getTiers().getDesignation2()),
        // JadeStringUtil.convertSpecialChars(persXML.getTiers().getDesignation2()), true))
        // || (!JadeStringUtil.equals(persDB.getPersonne().getDateNaissance(), persXML.getPersonne()
        // .getDateNaissance(), false)) || (!JadeStringUtil.equals(persDB.getTiers().getIdPays(),
        // persXML.getTiers().getIdPays(), false)));
        // }
    }

    /**
     * Méthode qui compare les valeurs (NSS, Date de naissance et nationalité) de deux personnes une valeur boolean
     * (vrai si valeurs comparées sont identiques et sinon false)
     * 
     * @param personneWebTiers
     * @param personneAlfagest
     * @return boolean
     */
    private boolean donneesTierAllocConcordante(PersonneEtendueComplexModel personneWebTiers,
            PersonneEtendueComplexModel personneAlfagest) {
        boolean donneesConcordante = false;
        if ((!JadeStringUtil.isBlankOrZero(personneWebTiers.getPersonneEtendue().getNumAvsActuel()) && JadeStringUtil
                .equals(personneWebTiers.getPersonneEtendue().getNumAvsActuel(), personneAlfagest.getPersonneEtendue()
                        .getNumAvsActuel(), false))
                && (!JadeStringUtil.isBlankOrZero(personneWebTiers.getTiers().getIdPays()) && JadeStringUtil.equals(
                        personneWebTiers.getTiers().getIdPays(), personneAlfagest.getTiers().getIdPays(), false))
                && (!JadeStringUtil.isBlankOrZero(personneWebTiers.getPersonne().getDateNaissance()) && JadeStringUtil
                        .equals(personneWebTiers.getPersonne().getDateNaissance(), personneAlfagest.getPersonne()
                                .getDateNaissance(), false))) {
            donneesConcordante = true;
        }
        return donneesConcordante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.importation.ImportationAllocataireService #importAllocataireAgricole
     * (ch.globaz.al.business.models.allocataire.AgricoleModel, java.lang.String)
     */
    @Override
    public void importAllocataireAgricole(AgricoleModel agricole, String idAllocataire)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAllocataire)) {
            throw new ALAllocataireBusinessException(
                    "ImportationAllocataireServiceImpl#importAllocataireAgricole : idAllocataire is empty or zero");
        }

        if (agricole != null) {

            AgricoleModelService service = ALImplServiceLocator.getAgricoleModelService();

            AgricoleSearchModel search = new AgricoleSearchModel();
            search.setForIdAllocataire(idAllocataire);
            search = service.search(search);

            if (search.getSize() > 0) {
                AgricoleModel dbModel = (AgricoleModel) search.getSearchResults()[0];

                if (!dbModel.getDomaineMontagne().equals(agricole.getDomaineMontagne())) {
                    throw new ALAllocataireBusinessException(
                            "ImportationAllocataireServiceImpl#importAllocataireAgricole : an agricole information has been found for this allocataire ("
                                    + idAllocataire + ") but domains are not the same");
                }
            } else if (search.getSize() == 0) {
                agricole.setIdAllocataire(idAllocataire);
                service.create(agricole);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService
     * #importRevenus(ch.globaz.al.business.models.allocataire. AllocataireComplexModel, java.util.ArrayList)
     */
    @Override
    public void importRevenus(String idAllocataire, ArrayList<RevenuModel> revenus) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAllocataire)) {
            throw new ALAllocataireBusinessException(
                    "ImportationAllocataireServiceImpl#importRevenus : idAllocataire is empty or zero");
        }

        if (revenus.size() != 0) {
            for (int i = 0; i < revenus.size(); i++) {
                if (!(revenus.get(i) instanceof RevenuModel)) {
                    throw new ALRevenuModelException("The model is not a model RevenuModel");
                }

                RevenuModel revenuModel = revenus.get(i);

                // identifiant de l'allocataire pour le revenu
                revenuModel.setIdAllocataire(idAllocataire);

                // vérification de l'existence du revenu
                RevenuSearchModel sr = new RevenuSearchModel();
                sr.setForDate(revenuModel.getDate());
                sr.setForIdAllocataire(revenuModel.getIdAllocataire());
                sr.setForIsConjoint(revenuModel.getRevenuConjoint());
                sr.setForMontant(revenuModel.getMontant());
                sr.setForIsIfd(revenuModel.getRevenuIFD());

                sr = ALImplServiceLocator.getRevenuModelService().search(sr);

                if (sr.getSize() == 0) {

                    // création du revenu
                    revenuModel = ALImplServiceLocator.getRevenuModelService().create(revenuModel);
                }
            }
        }
    }

    @Override
    public AllocataireComplexModel searchAllocataire(AllocataireComplexModel allocataire, String idDossier,
            String idPersonnePhyAlfagest) throws Exception {

        AllocataireComplexSearchModel search = new AllocataireComplexSearchModel();

        if (!JadeStringUtil.isBlankOrZero(allocataire.getAllocataireModel().getIdTiersAllocataire())) {
            search.setForIdTiers(allocataire.getAllocataireModel().getIdTiersAllocataire());
            search = ALImplServiceLocator.getAllocataireComplexModelService().search(search);
            if (search.getSize() > 1) {
                throw new ALAllocataireComplexModelException(
                        "ImportationAllocataireServiceImpl#searchAllocataire : several AllocataireComplexModel found (search by idTiers)");

            } else if (search.getSize() == 0) {
                // throw new ALAllocataireComplexModelException(
                // "ImportationAllocataireServiceImpl#searchAllocataire : no AllocataireComplexModel found (search by idTiers)");

                return allocataire;

            } else if (search.getSize() == 1) {
                // comparaison canton de résidence, pays de résidence, permis
                AllocataireComplexModel alloc = (AllocataireComplexModel) search.getSearchResults()[0];

                setAllocataireNullToZero(allocataire.getAllocataireModel());

                // POUR la FVE
                // if (JadeStringUtil.equals(ALImportUtils.caisse, "FVE", true)
                // && this.donneesTierAllocConcordante(alloc.getPersonneEtendueComplexModel(),
                // allocataire.getPersonneEtendueComplexModel())) {
                //
                // // constAllocataire etALImportUtils.SYMB_ALLOC
                // // Récupére l'idAllocAlfagest, idTiers constAllocataire etALImportUtils.SYMB_ALLOC
                // ImportDossiers.protocole.getWarningsLogger(idDossier, "warn").addMessage(
                // new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "dossier ", idDossier + ";"
                // + alloc.getAllocataireModel().getIdTiersAllocataire() + ";" + idPersonnePhyAlfagest
                // + ";" + ALImportUtils.SYMB_ALLOC));
                //
                // // reprend le tiers comme allocataire
                // return alloc;
                // // }

                /* else */if (!JadeStringUtil.equals(allocataire.getAllocataireModel().getIdPaysResidence(), alloc
                        .getAllocataireModel().getIdPaysResidence(), true)
                        || (!JadeStringUtil.equals(allocataire.getAllocataireModel().getPermis(), alloc
                                .getAllocataireModel().getPermis(), true))
                        || (!JadeStringUtil.equals(allocataire.getAllocataireModel().getCantonResidence(), alloc
                                .getAllocataireModel().getCantonResidence(), false))) {

                    throw new ALAllocataireComplexModelException(
                            "ImportationAllocataireServiceImpl#searchAllocataire: one register exists  but values are not the same: values found: "
                                    + alloc.getAllocataireModel().getIdPaysResidence() + " "
                                    + alloc.getAllocataireModel().getPermis() + " "
                                    + alloc.getAllocataireModel().getCantonResidence() + ", given "
                                    + allocataire.getAllocataireModel().getIdPaysResidence()
                                    + allocataire.getAllocataireModel().getPermis() + " "
                                    + allocataire.getAllocataireModel().getCantonResidence());

                } else {
                    return (AllocataireComplexModel) search.getSearchResults()[0];
                }
            }
        }

        // recherche par le numéro NSS
        else if (!JadeStringUtil.isBlankOrZero(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue()
                .getNumAvsActuel())) {

            search.setForNumNss(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
            search = ALImplServiceLocator.getAllocataireComplexModelService().search(search);

            if (search.getSize() > 1) {
                throw new ALAllocataireAgricoleComplexModelException(
                        "ImportationAllocataireServiceImpl#searchAllocataire : serveral AllocataireComplexModel found (search by NSS)");
            } else if (search.getSize() == 1) {
                AllocataireComplexModel alloc = (AllocataireComplexModel) search.getSearchResults()[0];

                // FVE: si le numéro NSS identique, Nationalité identique et
                // date de naissance identique on reprend le tiers
                // if (JadeStringUtil.equals(ALImportUtils.caisse, "FVE", true)
                // && this.donneesTierAllocConcordante(alloc.getPersonneEtendueComplexModel(),
                // allocataire.getPersonneEtendueComplexModel())) {
                // // Récupére l'idAllocAlfagest, idTiers constAllocataire etALImportUtils.SYMB_ALLOC
                // ImportDossiers.protocole.getWarningsLogger(idDossier, "").addMessage(
                // new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "", idDossier + ";"
                // + alloc.getAllocataireModel().getIdTiersAllocataire() + ";" + idPersonnePhyAlfagest
                // + ";" + ALImportUtils.SYMB_ALLOC));
                // // reprend le tiers comme allocataire
                // return alloc;
                // }

                /* else */if (!checkTiersData(alloc.getPersonneEtendueComplexModel(),
                        allocataire.getPersonneEtendueComplexModel(), idDossier, "alloc", idPersonnePhyAlfagest)) {

                    throw new ALAllocataireAgricoleComplexModelException(
                            "ImportationAllocataireServiceImpl#searchAllocataire: (1) one register exists  but values are not the same: values found: "
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                    + " "
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation2()
                                    + " "
                                    + alloc.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getIdPays()
                                    + ", given: "
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                    + " "
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation2()
                                    + " "
                                    + allocataire.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getIdPays());

                }
                // else if () {
                //
                // }

                else {
                    return (AllocataireComplexModel) search.getSearchResults()[0];
                }
            }
        }

        // si l'allocataire n'a pas pu être trouvé, tentative de recherche
        // par nom, prénom, date de naissance
        search.setForNumNss(null);
        search.setLikeNomAllocataire(JadeStringUtil.convertSpecialChars(allocataire.getPersonneEtendueComplexModel()
                .getTiers().getDesignation1().toUpperCase()));
        search.setLikePrenomAllocataire(JadeStringUtil.convertSpecialChars(allocataire.getPersonneEtendueComplexModel()
                .getTiers().getDesignation2().toUpperCase()));
        search.setForDateNaissance((allocataire.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()));
        search.setWhereKey("import");
        search = ALImplServiceLocator.getAllocataireComplexModelService().search(search);

        if (search.getSize() > 1) {
            throw new ALEnfantComplexModelException(
                    "ImportationAllocataireServiceImpl#searchAllocataire : serveral AllocataireComplexModel found (search by nom, prenom, nais)");
        } else if (search.getSize() == 1) {

            AllocataireComplexModel alloc = (AllocataireComplexModel) search.getSearchResults()[0];

            // Pas de NSS dans le XML, NSS dans la DB
            if (JadeStringUtil.isEmpty(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel())
                    && !JadeStringUtil.isEmpty(alloc.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {
                return alloc;

                // NSS dans le XML mais pas dans la DB
            } else if (!JadeStringUtil.isEmpty(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel())
                    && JadeStringUtil.isEmpty(alloc.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {

                alloc.getPersonneEtendueComplexModel()
                        .getPersonneEtendue()
                        .setNumAvsActuel(
                                allocataire.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
                alloc.getPersonneEtendueComplexModel().setMotifModifAvs(motifsModification.MOTIF_INCONNU);
                alloc.getPersonneEtendueComplexModel().setDateModifAvs(JadeDateUtil.getGlobazFormattedDate(new Date()));
                alloc.getPersonneEtendueComplexModel().getTiers().setPersonnePhysique(true);

                if (!checkTiersData(alloc.getPersonneEtendueComplexModel(),
                        allocataire.getPersonneEtendueComplexModel(), idDossier, "alloc", idPersonnePhyAlfagest)) {

                    throw new ALAllocataireAgricoleComplexModelException(
                            "ImportationAllocataireServiceImpl#searchAllocataire : (2) one register exists  but values are not the same: values found: "
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                    + " "
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation2()
                                    + " "
                                    + alloc.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                                    + alloc.getPersonneEtendueComplexModel().getTiers().getIdPays()
                                    + ", given: "
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                    + " "
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation2()
                                    + " "
                                    + allocataire.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                                    + allocataire.getPersonneEtendueComplexModel().getTiers().getIdPays());

                }

                alloc = ALImplServiceLocator.getAllocataireComplexModelService().update(alloc);

                return alloc;
            } else if (!JadeStringUtil.isEmpty(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel())
                    && !JadeStringUtil.isEmpty(alloc.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())
                    && !alloc
                            .getPersonneEtendueComplexModel()
                            .getPersonneEtendue()
                            .getNumAvsActuel()
                            .equals(allocataire.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel())) {
                throw new ALPersonneAFComplexModelException(
                        "ImportationAllocataireServiceImpl#searchAllocataire : AllocataireComplexModel found but NSS are different. found ("
                                + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation1() + ", "
                                + alloc.getPersonneEtendueComplexModel().getTiers().getDesignation2() + ", "
                                + alloc.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + ", "
                                + alloc.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()
                                + ") given ("
                                + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation1() + ", "
                                + allocataire.getPersonneEtendueComplexModel().getTiers().getDesignation2() + ", "
                                + allocataire.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + ", "
                                + allocataire.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()
                                + ")");
            }

            return alloc;
        } else {
            return allocataire;
        }
    }

    private AllocataireModel setAllocataireNullToZero(AllocataireModel alloc) {

        if (JadeStringUtil.equals(alloc.getCantonResidence(), null, false)) {
            alloc.setCantonResidence("0");
        }
        if (JadeStringUtil.equals(alloc.getIdPaysResidence(), null, false)) {
            alloc.setIdPaysResidence("0");

        }
        if (JadeStringUtil.equals(alloc.getPermis(), null, false)) {
            alloc.setPermis("0");

        }

        return alloc;
    }

}
