package ch.globaz.al.businessimpl.rafam;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.al.business.constantes.enumerations.RafamError;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumEtatDroit;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumSexe;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumTypeNaissance;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.models.rafam.OverlapInformationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.businessimpl.rafam.schema.Action;
import ch.globaz.al.businessimpl.rafam.schema.Check;
import ch.globaz.al.businessimpl.rafam.schema.Data;
import ch.globaz.al.businessimpl.rafam.schema.DataImport;
import ch.globaz.al.businessimpl.rafam.schema.Load;
import ch.globaz.al.businessimpl.rafam.schema.Tests;
import ch.globaz.al.businessimpl.rafam.schema.Validation;
import ch.globaz.al.businessimpl.rafam.sedex.ImportAnnoncesRafam;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

/**
 * @author jts
 * 
 */
@RunWith(Parameterized.class)
public class AnnonceRafamEnregistrementsTest extends ALTestCaseJU4 {

    @Parameters
    public static List<Object[]> getParametres() {
        // return Arrays.asList(new Object[][] { { "src/ch/globaz/al/businessimpl/rafam/res/Droits_ENF.xml" },
        // { "src/ch/globaz/al/businessimpl/rafam/res/Droits_NAIS.xml" },
        // { "src/ch/globaz/al/businessimpl/rafam/res/Droits_FORM_Anticipee.xml" },
        // { "src/ch/globaz/al/businessimpl/rafam/res/Correction_Bugs_1-8-00.xml" },
        // { "src/ch/globaz/al/businessimpl/rafam/res/Correction_Bugs_1-10-00.xml" } });

        return Arrays.asList(new Object[][] { { "src/ch/globaz/al/businessimpl/rafam/res/Droits_ENF.xml" } });
    }

    /**
     * Chemin du fichier contenant les cas de test
     */
    private String pFile;

    public AnnonceRafamEnregistrementsTest(String pFile) {
        this.pFile = pFile;
    }

    /**
     * 
     * @param check
     * @param annonces
     * @param idTest
     * @param idValidation
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void checkAnnonce(Check check, AnnonceRafamSearchModel annonces, String idTest, String idValidation)
            throws JadeApplicationException, JadePersistenceException {

        if ((check.isZeroPrestation() != null) && check.isZeroPrestation()) {
            Assert.assertEquals("Une prestation a été trouvée pour le test #" + idTest + ", validation #"
                    + idValidation + " (zéro attendu)", 0, annonces.getSize());
        } else if (annonces.getSize() == 0) {
            Assert.fail("Aucune annonce n'a été trouvée pour le test #" + idTest + ", validation #" + idValidation);
        } else {

            AnnonceRafamModel annonce = (AnnonceRafamModel) annonces.getSearchResults()[0];

            if (check.getRecordNumber() != null) {
                Assert.assertEquals("recordNumber du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getRecordNumber().toString(), annonce.getRecordNumber());
            }

            if (check.getNssEnfant() != null) {
                Assert.assertEquals("nssEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getNssEnfant(), annonce.getNssEnfant());
            }

            if (check.getNewNssEnfant() != null) {
                Assert.assertEquals("newNssEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getNewNssEnfant(), annonce.getNewNssEnfant());
            }

            if (check.getNomEnfant() != null) {
                Assert.assertEquals("nomEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getNomEnfant(), annonce.getNomEnfant());
            }

            if (check.getPrenomEnfant() != null) {
                Assert.assertEquals("prenomEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getPrenomEnfant(), annonce.getPrenomEnfant());
            }

            if (check.getSexeEnfant() != null) {
                Assert.assertEquals("sexeEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", ALEnumSexe.valueOf(check.getSexeEnfant()).getCS(),
                        annonce.getSexeEnfant());
            }

            if (check.getDateNaissanceEnfant() != null) {
                Assert.assertEquals("dateNaissanceEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateNaissanceEnfant(), annonce.getDateNaissanceEnfant());
            }

            if (check.getDateMortEnfant() != null) {
                Assert.assertEquals("dateMortEnfant du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateMortEnfant(), annonce.getDateMortEnfant());
            }

            if (check.getNssAllocataire() != null) {
                Assert.assertEquals("nssAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getNssAllocataire(), annonce.getNssAllocataire());
            }

            if (check.getNomAllocataire() != null) {
                Assert.assertEquals("nomAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getNomAllocataire(), annonce.getNomAllocataire());
            }

            if (check.getPrenomAllocataire() != null) {
                Assert.assertEquals("prenomAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getPrenomAllocataire(), annonce.getPrenomAllocataire());
            }

            if (check.getSexeAllocataire() != null) {
                Assert.assertEquals("sexeAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", ALEnumSexe.valueOf(check.getSexeAllocataire()).getCS(),
                        annonce.getSexeAllocataire());
            }

            if (check.getDateNaissanceAllocataire() != null) {
                Assert.assertEquals("dateNaissanceAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateNaissanceAllocataire(),
                        annonce.getDateNaissanceAllocataire());
            }

            if (check.getDateMortAllocataire() != null) {
                Assert.assertEquals("dateMortAllocataire du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateMortAllocataire(), annonce.getDateMortAllocataire());
            }

            if (check.getDebutDroit() != null) {
                Assert.assertEquals("debutDroit du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDebutDroit(), annonce.getDebutDroit());
            }

            if (check.getEcheanceDroit() != null) {
                Assert.assertEquals("echeanceDroit du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getEcheanceDroit(), annonce.getEcheanceDroit());
            }

            if (check.getBaseLegale() != null) {
                Assert.assertEquals("baseLegale du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamLegalBasis.valueOf(check.getBaseLegale()).getCodeCentrale(),
                        annonce.getBaseLegale());
            }

            if (check.getCanton() != null) {
                Assert.assertEquals(
                        "canton du test #" + idTest + ", validation #" + idValidation + " n'est pas valide",
                        check.getCanton(), annonce.getCanton());
            }

            if (check.getStatutFamilial() != null) {
                Assert.assertEquals("statutFamilial du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamFamilyStatus.valueOf(check.getStatutFamilial()).getCodeCentrale(),
                        annonce.getCodeStatutFamilial());
            }

            if (check.getGenrePrestation() != null) {
                Assert.assertEquals("genrePrestation du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamFamilyAllowanceType.valueOf(check.getGenrePrestation())
                        .getCodeCentrale(), annonce.getGenrePrestation());
            }

            if (check.getCodeTypeActivite() != null) {
                Assert.assertEquals("codeTypeActivite du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamOccupationStatus.valueOf(check.getCodeTypeActivite())
                        .getCodeCentrale(), annonce.getCodeTypeActivite());
            }

            if (check.getCodeRetour() != null) {
                Assert.assertEquals("codeRetour du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamReturnCode.valueOf(check.getCodeRetour()), annonce.getCodeRetour());
            }

            if (check.getDateCreation() != null) {
                Assert.assertEquals("dateCreation du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateCreation(), annonce.getDateCreation());
            }

            if (check.getDateMutation() != null) {
                Assert.assertEquals("dateMutation du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getDateMutation(), annonce.getDateMutation());
            }

            if (check.isCanceled() != null) {
                Assert.assertEquals("minimalStartFlag du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.isCanceled(), annonce.getCanceled());
            }

            if (check.getInternalOfficeReference() != null) {
                Assert.assertEquals("internalOfficeReference du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getInternalOfficeReference(), annonce.getInternalOfficeReference());
            }

            if (check.isInternalError() != null) {
                Assert.assertEquals("internalErrorMessage du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.isInternalError(), annonce.getInternalError());
            }

            if (check.getInternalErrorMessage() != null) {
                Assert.assertEquals("internalErrorMessage du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getInternalErrorMessage(), annonce.getInternalErrorMessage());
            }

            if (check.getTypeAnnonce() != null) {
                Assert.assertEquals("typeAnnonce du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", check.getTypeAnnonce(), annonce.getTypeAnnonce());
            }

            if (check.getEtat() != null) {
                Assert.assertEquals("etat du test #" + idTest + ", validation #" + idValidation + " n'est pas valide",
                        RafamEtatAnnonce.valueOf(check.getEtat()).getCS(), annonce.getEtat());
            }

            if (check.getEvDeclencheur() != null) {
                Assert.assertEquals("evDeclencheur du test #" + idTest + ", validation #" + idValidation
                        + " n'est pas valide", RafamEtatAnnonce.valueOf(check.getEvDeclencheur()).getCS(),
                        annonce.getEvDeclencheur());
            }

            AnnonceRafamErrorComplexSearchModel errors = ALServiceLocator.getAnnoncesRafamErrorBusinessService()
                    .getErrorsForAnnonce(annonce.getId());

            if (check.getCodeErreur() != null) {
                if (errors.getSize() == 0) {
                    Assert.fail("test #" + idTest + ", validation #" + idValidation
                            + " n'est pas valide, une erreur est attendue");
                } else {

                    AnnonceRafamErrorComplexModel erreur = (AnnonceRafamErrorComplexModel) errors.getSearchResults()[0];

                    if (check.getCodeErreur() != null) {
                        Assert.assertEquals("codeErreur du test #" + idTest + ", validation #" + idValidation
                                + " n'est pas valide", RafamError.valueOf(check.getCodeErreur()), erreur
                                .getErreurAnnonceRafamModel().getCode());
                    }

                    if (check.getOverlapPeriodeStart() != null) {
                        Assert.assertEquals("overlapPeriodeStart du test #" + idTest + ", validation #" + idValidation
                                + " n'est pas valide", check.getOverlapPeriodeStart(), erreur
                                .getOverlapInformationModel().getOverlapPeriodeStart());
                    }

                    if (check.getOverlapPeriodeEnd() != null) {
                        Assert.assertEquals("overlapPeriodeEnd du test #" + idTest + ", validation #" + idValidation
                                + " n'est pas valide", check.getOverlapPeriodeEnd(), erreur
                                .getOverlapInformationModel().getOverlapPeriodeEnd());
                    }

                    if (check.getDeliveryOfficeConflict() != null) {
                        Assert.assertEquals("deliveryOfficeConflict du test #" + idTest + ", validation #"
                                + idValidation + " n'est pas valide", check.getDeliveryOfficeConflict(), erreur
                                .getOverlapInformationModel().getOfficeIdentifier());
                    }

                    if (check.getMinimalStartFlag() != null) {
                        Assert.assertEquals("minimalStartFlag du test #" + idTest + ", validation #" + idValidation
                                + " n'est pas valide", check.getMinimalStartFlag(), erreur.getOverlapInformationModel()
                                .getMinimalStartFlag());
                    }
                }
            }
        }
    }

    /**
     * 
     * @param load
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private AnnonceRafamSearchModel loadAnnonce(Load load, String idDroit) throws JadeApplicationException,
            JadePersistenceException {

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();

        search.setForIdDroit(idDroit);

        if (load.getRecordNumber() != null) {
            search.setForRecordNumber(Long.toString(load.getRecordNumber()));
        }

        if (load.getGenrePrestation() != null) {
            search.setForGenrePrestation(RafamFamilyAllowanceType.valueOf(load.getGenrePrestation()).getCodeCentrale());
        }

        if (load.getTypeAnnonce() != null) {
            search.setForTypeAnnonce(load.getTypeAnnonce());
        }

        if (load.getEtat() != null) {
            search.setForEtatAnnonce(RafamEtatAnnonce.valueOf(load.getEtat()).getCS());
        }

        search.setWhereKey("JUnit");

        return ALServiceLocator.getAnnonceRafamModelService().search(search);
    }

    private void sendAnnonces() throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamComplexSearchModel annonces = ALImplServiceLocator.getAnnoncesRafamSearchService()
                .loadAnnoncesToSend();

        AnnonceRafamBusinessService s = ALImplServiceLocator.getAnnonceRafamBusinessService();

        for (int i = 0; i < annonces.getSize(); i++) {
            AnnonceRafamComplexModel annonce = ((AnnonceRafamComplexModel) annonces.getSearchResults()[i]);
            s.setEtat(annonce.getAnnonceRafamModel(), RafamEtatAnnonce.TRANSMIS);
        }
    }

    @Ignore("TEST FAILED LIEN FICHIER")
    @Test
    public void testRafam() {
        try {

            JAXBContext jc = JAXBContext.newInstance("ch.globaz.al.businessimpl.rafam.schema");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Tests tests = (Tests) unmarshaller.unmarshal(new File(pFile));

            Properties properties = new Properties();
            properties.setProperty("userSedex", "alfagest");
            properties.setProperty("passSedex", "glob4az");
            properties.setProperty("urlEmployeurDelegue", "");

            for (ch.globaz.al.businessimpl.rafam.schema.Test test : tests.getTest()) {

                System.out.println("Exécution du test : " + test.getDescription());

                String idDroit = Long.toString(test.getIdDroit());
                DroitComplexModel droit = ALServiceLocator.getDroitComplexModelService().read(
                        Long.toString(test.getIdDroit()));

                // exécution des actions
                for (Action action : test.getActions().getAction()) {

                    // mise à jour du droit
                    if (action.getUpdate() != null) {
                        droit = updateDroit(droit, action.getUpdate().getData());
                        if (printJadeBusinessMessage()) {
                            Assert.fail("Une erreur s'est produite pendant un update du test #"
                                    + Long.toString(test.getId()));
                        }
                    }

                    // Envoi des annonces
                    if ((action.isSend() != null) && action.isSend()) {
                        sendAnnonces();
                    }

                    // Importation des annonces
                    if (action.getImport() != null) {

                        if (action.getImport().getDataImport() != null) {
                            updateAnnonce(action.getImport().getDataImport(), idDroit);
                        }

                        if (action.getImport().getFile() != null) {

                            for (String file : action.getImport().getFile()) {
                                SimpleSedexMessage message = new SimpleSedexMessage();
                                message.fileLocation = file;
                                ImportAnnoncesRafam iam = new ImportAnnoncesRafam();
                                iam.setUp(properties);
                                iam.importMessage(message);
                            }
                        }
                    }
                }

                // vérification des annonces
                for (Validation validation : test.getValidations().getValidation()) {
                    AnnonceRafamSearchModel annonces = loadAnnonce(validation.getLoad(), idDroit);

                    if (annonces.getSize() > 1) {
                        Assert.fail("Plusieurs annonces ont été trouvées pour le test #" + Long.toString(test.getId())
                                + ", validation #" + Long.toString(validation.getId()));
                    }

                    checkAnnonce(validation.getCheck(), annonces, Long.toString(test.getId()),
                            Long.toString(validation.getId()));
                }

                JadeThread.rollbackSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    private void updateAnnonce(DataImport dataImport, String idDroit) throws Exception {

        AnnonceRafamModel annonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(idDroit,
                RafamFamilyAllowanceType.valueOf(dataImport.getGenrePrestation()));

        if (annonce.isNew()) {
            throw new Exception("updateAnnonce : impossible de trouver l'annonce");
        }

        if (dataImport.getCodeRetour() != null) {
            annonce.setCodeRetour(RafamReturnCode.valueOf(dataImport.getCodeRetour()).getCode());
        }

        if (dataImport.getDateCreation() != null) {
            annonce.setDateCreation(dataImport.getDateCreation());
        }

        if (dataImport.getDateMutation() != null) {
            annonce.setDateMutation(dataImport.getDateMutation());
        }

        if (dataImport.getEtat() != null) {
            annonce.setEtat(RafamEtatAnnonce.valueOf(dataImport.getEtat()).getCS());
        }

        annonce = ALServiceLocator.getAnnonceRafamModelService().update(annonce);

        if ((dataImport.getCodeErreur() != null)
                && !RafamError.valueOf(dataImport.getCodeErreur()).equals(RafamError._400_AUCUNE_ERREUR)) {
            ErreurAnnonceRafamModel erreurAnnonce = new ErreurAnnonceRafamModel();
            erreurAnnonce.setCode(RafamError.valueOf(dataImport.getCodeErreur()).getCode());
            erreurAnnonce.setIdAnnonce(annonce.getIdAnnonce());
            erreurAnnonce = ALServiceLocator.getErreurAnnonceRafamModelService().create(erreurAnnonce);

            if (RafamError.valueOf(dataImport.getCodeErreur()).equals(
                    RafamError._210_ALLOCATION_ENFANT_OU_ADOPTION_DEJA_EXISTANTE)
                    || RafamError.valueOf(dataImport.getCodeErreur()).equals(
                            RafamError._211_ALLOCATION_ANNONCEE_SUR_PERIODE_CHEVAUCHANT_AUTRE_PERIODE)) {

                if ((dataImport.getOverlapPeriodeStart() != null) && (dataImport.getOverlapPeriodeEnd() != null)
                        && (dataImport.getMinimalStartFlag() != null)) {

                    OverlapInformationModel overlapInfoModel = new OverlapInformationModel();
                    overlapInfoModel.setIdErreurAnnonce(erreurAnnonce.getIdErreurAnnonce());
                    overlapInfoModel.setOfficeIdentifier(dataImport.getDeliveryOfficeConflict());

                    overlapInfoModel.setOverlapPeriodeStart(dataImport.getOverlapPeriodeStart());
                    overlapInfoModel.setOverlapPeriodeEnd(dataImport.getOverlapPeriodeEnd());
                    overlapInfoModel.setMinimalStartFlag(dataImport.getMinimalStartFlag().toString());
                    overlapInfoModel.setInsignificance(false);

                    overlapInfoModel = ALServiceLocator.getOverlapInformationModelService().create(overlapInfoModel);
                }
            }
        }
    }

    /**
     * Met à jour le droit passé en paramètre avec les données contenues dans <code>data</code>
     * 
     * @param droit
     * @param data
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private DroitComplexModel updateDroit(DroitComplexModel droit, Data data) throws JadeApplicationException,
            JadePersistenceException {

        if (data != null) {

            if (data.getEtatDroit() != null) {
                droit.getDroitModel().setEtatDroit(ALEnumEtatDroit.valueOf(data.getEtatDroit()).getCS());
            }

            if (data.getDebutDroit() != null) {
                droit.getDroitModel().setDebutDroit(data.getDebutDroit());
            }

            if (data.getFinDroitForcee() != null) {
                droit.getDroitModel().setFinDroitForcee(data.getFinDroitForcee());
            }

            if (data.getMontantForce() != null) {
                droit.getDroitModel().setMontantForce(data.getMontantForce().toPlainString());
            }

            if (data.isForce() != null) {
                droit.getDroitModel().setForce(data.isForce());
            }

            if (data.isSupplementFnb() != null) {
                droit.getDroitModel().setSupplementFnb(data.isSupplementFnb());
            }

            if (data.isCapableExercer() != null) {
                droit.getEnfantComplexModel().getEnfantModel().setCapableExercer(data.isCapableExercer());
            }

            if (data.isAllocationNaissanceVersee() != null) {
                droit.getEnfantComplexModel().getEnfantModel()
                        .setAllocationNaissanceVersee(data.isAllocationNaissanceVersee());
            }

            if (data.getTypeAllocationNaissance() != null) {
                droit.getEnfantComplexModel()
                        .getEnfantModel()
                        .setTypeAllocationNaissance(
                                ALEnumTypeNaissance.valueOf(data.getTypeAllocationNaissance()).getCS());
            }
        }

        return ALServiceLocator.getDroitBusinessService().updateDroitEtEnvoyeAnnoncesRafam(droit);
    }
}
