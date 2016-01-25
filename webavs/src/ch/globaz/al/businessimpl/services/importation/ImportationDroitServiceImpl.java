package ch.globaz.al.businessimpl.services.importation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import ch.globaz.al.business.exceptions.importation.ALSeveralPersonnesFoundException;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantComplexModelException;
import ch.globaz.al.business.exceptions.model.personne.ALPersonneAFComplexModelException;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.importation.ImportationDroitService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Implémentation du service d'importation des données de droits
 * 
 * @author jts
 * 
 */
public class ImportationDroitServiceImpl extends ALAbstractBusinessServiceImpl implements ImportationDroitService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.importation.ImportationDroitService#
     * searchEnfant(ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public EnfantComplexModel searchEnfant(EnfantComplexModel enfant, String idDossier,
            String idEnfantAlfaPersonnePsysique) throws Exception {
        EnfantComplexSearchModel search = new EnfantComplexSearchModel();

        // recherche par idTiersEnfant si tiers pas 0

        if (!JadeStringUtil.isBlankOrZero(enfant.getEnfantModel().getIdTiersEnfant())) {
            search.setForIdTiers(enfant.getEnfantModel().getIdTiersEnfant());
            search = ALImplServiceLocator.getEnfantComplexModelService().search(search);

            if (search.getSize() > 1) {
                throw new ALEnfantComplexModelException(
                        "ImportationDroitServiceImpl#searchEnfant: several EnfantComplexModel found (search by idTiersEnfant");
            } else if (search.getSize() == 0) {
                return enfant;

            } else if (search.getSize() == 1) {

                EnfantComplexModel enfantDB = (EnfantComplexModel) search.getSearchResults()[0];

                setEnfantNullToZero(enfant.getEnfantModel());

                if (!JadeStringUtil.equals(enfant.getEnfantModel().getCapableExercer().toString(), enfantDB
                        .getEnfantModel().getCapableExercer().toString(), false)
                        || !JadeStringUtil.equals(enfant.getEnfantModel().getAllocationNaissanceVersee().toString(),
                                enfantDB.getEnfantModel().getAllocationNaissanceVersee().toString(), false)
                        || !JadeStringUtil.equals(enfant.getEnfantModel().getCantonResidence(), enfantDB
                                .getEnfantModel().getCantonResidence(), false)
                        || !JadeStringUtil.equals(enfant.getEnfantModel().getIdPaysResidence(), enfantDB
                                .getEnfantModel().getIdPaysResidence(), false)
                        || !JadeStringUtil.equals(enfant.getEnfantModel().getMontantAllocationNaissanceFixe(), enfantDB
                                .getEnfantModel().getMontantAllocationNaissanceFixe(), false)
                        || !JadeStringUtil.equals(enfant.getEnfantModel().getTypeAllocationNaissance(), enfantDB
                                .getEnfantModel().getTypeAllocationNaissance(), false))

                {

                    throw new ALEnfantComplexModelException(
                            "ImportationDroitServiceImpl#searchEnfant : one register <idTiersEnfant>"
                                    + enfantDB.getEnfantModel().getIdTiersEnfant()
                                    + "</idTiersEnfant> exist but values are different: "

                                    + enfant.getEnfantModel().getCapableExercer() + " "
                                    + enfant.getEnfantModel().getAllocationNaissanceVersee() + " "
                                    + enfant.getEnfantModel().getCantonResidence() + " "
                                    + enfant.getEnfantModel().getIdPaysResidence() + " "
                                    + enfant.getEnfantModel().getMontantAllocationNaissanceFixe() + " "
                                    + enfant.getEnfantModel().getTypeAllocationNaissance() + " given "
                                    + enfantDB.getEnfantModel().getCapableExercer() + " "
                                    + enfantDB.getEnfantModel().getAllocationNaissanceVersee() + " "
                                    + enfantDB.getEnfantModel().getCantonResidence() + " "
                                    + enfantDB.getEnfantModel().getIdPaysResidence() + " "
                                    + enfantDB.getEnfantModel().getMontantAllocationNaissanceFixe() + " "
                                    + enfantDB.getEnfantModel().getTypeAllocationNaissance());

                } else {
                    return (EnfantComplexModel) search.getSearchResults()[0];
                }
            }

        }

        // recherche par le numéro NSS
        if (!JadeStringUtil.isBlankOrZero(enfant.getPersonneEtendueComplexModel().getPersonneEtendue()
                .getNumAvsActuel())) {

            search.setForNss(enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
            search = ALImplServiceLocator.getEnfantComplexModelService().search(search);

            if (search.getSize() > 1) {
                throw new ALEnfantComplexModelException(
                        "ImportationDroitServiceImpl#searchEnfant : serveral EnfantComplexModel found (search by NSS : "
                                + enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel() + ")");
            } else if (search.getSize() == 1) {

                EnfantComplexModel enfantDB = (EnfantComplexModel) search.getSearchResults()[0];
                if (!ALImplServiceLocator.getImportationAllocataireService().checkTiersData(
                        enfantDB.getPersonneEtendueComplexModel(), enfant.getPersonneEtendueComplexModel(), idDossier,
                        "enfant", idEnfantAlfaPersonnePsysique)) {

                    throw new ALEnfantComplexModelException(
                            "ImportationDroiServiceImpl#searchEnfant: one register <idTiersEnfant>"
                                    + enfantDB.getEnfantModel().getIdTiersEnfant()
                                    + "</idTiersEnfant> exists  but values are not the same: values found: "
                                    + enfantDB.getPersonneEtendueComplexModel().getTiers().getDesignation1() + " "
                                    + enfantDB.getPersonneEtendueComplexModel().getTiers().getDesignation2() + " "
                                    + enfantDB.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                                    + enfantDB.getPersonneEtendueComplexModel().getTiers().getIdPays() + ", given: "
                                    + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation1() + " "
                                    + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation2() + " "
                                    + enfant.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + " "
                                    + enfant.getPersonneEtendueComplexModel().getTiers().getIdPays());
                } else {
                    return (EnfantComplexModel) search.getSearchResults()[0];
                }
            }
        }

        // si l'enfant n'a pas pu être trouvé, tentative de recherche
        // par nom, prénom, date de naissance
        search.setForNss(null);
        search.setForNom(JadeStringUtil.convertSpecialChars(enfant.getPersonneEtendueComplexModel().getTiers()
                .getDesignation1().toUpperCase()));
        search.setForPrenom(JadeStringUtil.convertSpecialChars(enfant.getPersonneEtendueComplexModel().getTiers()
                .getDesignation2().toUpperCase()));
        search.setForDateNaissance((enfant.getPersonneEtendueComplexModel().getPersonne().getDateNaissance()));
        search.setWhereKey("import");
        search = ALImplServiceLocator.getEnfantComplexModelService().search(search);

        if (search.getSize() > 1) {

            throw new ALEnfantComplexModelException(
                    "ImportationDroitServiceImpl#searchEnfant : serveral EnfantComplexModel found (search by nom, prenom, nais : "
                            + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation1() + " "
                            + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation2() + ", "
                            + enfant.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + ")");

        } else if (search.getSize() == 1) {

            EnfantComplexModel enfantDB = (EnfantComplexModel) search.getSearchResults()[0];
            // Pas de NSS dans le XML, NSS dans DB
            if (JadeStringUtil.isEmpty(enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel())
                    && !JadeStringUtil.isEmpty(enfantDB.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {
                return enfantDB;
            } else if (!JadeStringUtil.isEmpty(enfant.getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel())
                    && JadeStringUtil.isEmpty(enfantDB.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {

                enfantDB.getPersonneEtendueComplexModel()
                        .getPersonneEtendue()
                        .setNumAvsActuel(enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
                enfantDB.getPersonneEtendueComplexModel().setMotifModifAvs(motifsModification.MOTIF_INCONNU);
                enfantDB.getPersonneEtendueComplexModel().setDateModifAvs(
                        JadeDateUtil.getGlobazFormattedDate(new Date()));

                enfantDB = ALServiceLocator.getEnfantComplexModelService().update(enfantDB);

                return enfantDB;

            } else if (!JadeStringUtil.isEmpty(enfant.getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel())
                    && !JadeStringUtil.isEmpty(enfantDB.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())
                    && !enfantDB.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()
                            .equals(enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel())) {

                throw new ALPersonneAFComplexModelException(
                        "ImportationDroitServiceImpl#searchEnfant : EnfantComplexModel found <idTiersEnfant>"
                                + enfantDB.getEnfantModel().getIdTiersEnfant()
                                + "</idTiersEnfant> but NSS are different. found ("
                                + enfantDB.getPersonneEtendueComplexModel().getTiers().getDesignation1() + ", "
                                + enfantDB.getPersonneEtendueComplexModel().getTiers().getDesignation2() + ", "
                                + enfantDB.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + ", "
                                + enfantDB.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()
                                + ") given (" + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                + ", " + enfant.getPersonneEtendueComplexModel().getTiers().getDesignation2() + ", "
                                + enfant.getPersonneEtendueComplexModel().getPersonne().getDateNaissance() + ", "
                                + enfant.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel() + ")");
            }

            return enfantDB;

        } else {
            return enfant;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.importation.ImportationDroitService# searchPersonne
     * (ch.globaz.pyxis.business.model.PersonneEtendueComplexModel)
     */
    @Override
    public PersonneEtendueComplexModel searchPersonne(PersonneEtendueComplexModel personne)
            throws JadeApplicationException, JadePersistenceException {

        // Recherche d'un tiers correspondant à la personne
        PersonneEtendueSearchComplexModel search = new PersonneEtendueSearchComplexModel();

        // recherche par le numéro NSS
        if (!JadeStringUtil.isBlankOrZero(personne.getPersonneEtendue().getNumAvsActuel())) {

            search.setForNumeroAvsActuel(personne.getPersonneEtendue().getNumAvsActuel());
            search = TIBusinessServiceLocator.getPersonneEtendueService().find(search);

            if (search.getSize() > 1) {
                throw new ALSeveralPersonnesFoundException(
                        "ImportationDroitServiceImpl#searchPersonne : serveral PersonneEtendueComplexModel found (search by NSS : "
                                + personne.getPersonneEtendue().getNumAvsActuel() + ")");
            } else if (search.getSize() == 1) {
                return (PersonneEtendueComplexModel) search.getSearchResults()[0];
            }
        }

        // si la personne n'a pas pu être trouvé, tentative de recherche
        // par nom, prénom, date de naissance
        search.setForNumeroAvsActuel(null);
        search.setForDesignation1Like(personne.getTiers().getDesignation1());
        search.setForDesignation2Like(personne.getTiers().getDesignation2());
        search.setForDateNaissance(personne.getPersonne().getDateNaissance());
        search = TIBusinessServiceLocator.getPersonneEtendueService().find(search);

        if (search.getSize() > 1) {
            throw new ALPersonneAFComplexModelException(
                    "ImportationDroitServiceImpl#searchPersonne : serveral PersonneEtendueComplexModel found (search by nom, prenom, nais : "
                            + personne.getTiers().getDesignation1()
                            + ", "
                            + personne.getTiers().getDesignation2()
                            + ", " + personne.getPersonne().getDateNaissance() + ")");
        } else if (search.getSize() == 1) {
            return (PersonneEtendueComplexModel) search.getSearchResults()[0];
        } else {
            return personne;
        }
    }

    private EnfantModel setEnfantNullToZero(EnfantModel enfant) {

        if (JadeStringUtil.equals(enfant.getCantonResidence(), null, false)) {
            enfant.setCantonResidence("0");
        }
        if (JadeStringUtil.equals(enfant.getIdPaysResidence(), null, false)) {
            enfant.setIdPaysResidence("0");

        }
        if (JadeStringUtil.equals(enfant.getTypeAllocationNaissance(), null, false)) {
            enfant.setTypeAllocationNaissance("0");

        }
        if (JadeStringUtil.equals(enfant.getMontantAllocationNaissanceFixe(), null, false)) {
            enfant.setTypeAllocationNaissance("0");

        }

        return enfant;
    }

    private void writeErrorInFile(String msg) throws IOException {

        FileWriter writer = null;
        String texte = msg;
        try {
            writer = new FileWriter("ccvd_protocole_run5.txt", true);
            writer.write(texte, 0, texte.length());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
