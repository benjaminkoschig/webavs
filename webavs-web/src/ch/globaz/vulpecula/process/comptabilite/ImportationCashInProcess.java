package ch.globaz.vulpecula.process.comptabilite;

import globaz.aquila.db.rdp.cashin.importer.CashinImportData;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Role;
import ch.globaz.vulpecula.util.RubriqueUtil;

/**
 * Process permettant de lancer le traitement d'importation d'un fichier Cashin dans la compta auxiliaire
 * 
 * @since WebBMS 1.0
 */
public class ImportationCashInProcess extends BProcessWithContext {
    private static final long serialVersionUID = -6431396237521702640L;

    private String csvFilename = "";
    private String journalLibelle = "";
    private String journalDate = "";
    private TypeImportation typeImportation;

    private static final String JDBC = "jdbc://";
    private static final String WORK_DIR = "work/";
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String DATE_PATTERN_SWISS = "dd.MM.yyyy";
    private final static Logger LOGGER = LoggerFactory.getLogger(ImportationCashInProcess.class);
    private static final String ID_REFERENCE_RUBRIQUE_FDP = "237099";
    private static final String LIBELLE_JOURNAL_DEFAULT = "Importation frais Cashin";

    public enum TypeImportation {
        FRAIS,
        PAIEMENT;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        String filepath = getFileFromDB();
        boolean result = false;
        switch (typeImportation) {
            case FRAIS:
                result = importationFrais(filepath);
                break;
            case PAIEMENT:
                result = importationPaiement(filepath);
                break;
            default:
                getTransaction().addErrors(getSession().getLabel("ERREUR_IMPORTATION_CASHIN_TYPE_INCONNU") + "\r\n");
                break;
        }

        return result;

    }

    private String getFileFromDB() throws Exception {
        String filepath = Jade.getInstance().getHomeDir() + WORK_DIR + csvFilename;

        // Lecture
        try {
            String uriFile = JDBC + Jade.getInstance().getDefaultJdbcSchema() + "/" + csvFilename;

            JadeFsFacade.copyFile(uriFile, filepath);
            JadeFsFacade.delete(uriFile);
        } catch (JadeServiceLocatorException e) {
            throw new Exception(e.toString());
        } catch (JadeServiceActivatorException e) {
            throw new Exception(e.toString());
        } catch (NullPointerException e) {
            throw new Exception(e.toString());
        } catch (ClassCastException e) {
            throw new Exception(e.toString());
        } catch (JadeClassCastException e) {
            throw new Exception(e.toString());
        }
        return filepath;
    }

    private boolean importationPaiement(String filepath) throws IOException {
        getTransaction().addErrors("Importation des paiements pas encore implémenté !");
        return true;
    }

    private boolean importationFrais(String filepath) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filepath));
            String line;
            String[] donnees_fichier;
            ArrayList<CashinImportData> arrayLignesFichier = new ArrayList<CashinImportData>();

            while ((line = in.readLine()) != null) {
                // TODO Gérer le séparateur dans les properties ?
                donnees_fichier = line.split("\\t");

                if (donnees_fichier.length != CashinImportData.NOMBRE_DE_CHAMPS) {
                    throw new Exception("Nombre de colonnes dans le fichier source incorrect !");
                }

                CashinImportData cashinImportData = new CashinImportData(donnees_fichier);
                arrayLignesFichier.add(cashinImportData);
            }

            JournalConteneur jc = createJournal();

            for (CashinImportData cashinImportData : arrayLignesFichier) {
                creerEcritures(cashinImportData, jc);
            }
            CABusinessServiceLocator.getJournalService().comptabilise(
                    CABusinessServiceLocator.getJournalService().createJournalAndOperations(jc));

            JadeThread.commitSession();

        } catch (Exception e) {
            getTransaction().addErrors(
                    getSession().getLabel("ERREUR_IMPORTATION_FRAIS_CASHIN") + "\r\n" + e.getMessage());
            return false;
        } finally {
            in.close();
        }
        return true;
    }

    private JournalConteneur createJournal() {
        try {

            if (!JadeDateUtil.isGlobazDate(journalDate)) {
                journalDate = Date.now().getSwissValue();
            }

            if (JadeStringUtil.isEmpty(journalLibelle)) {
                journalLibelle = LIBELLE_JOURNAL_DEFAULT;
            }
            JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournal(journalLibelle,
                    journalDate);

            JournalConteneur jc = new JournalConteneur();
            jc.AddJournal(journal);

            // Commit le journal car si le premier cas plante, le journal ne sera pas créé
            JadeThread.commitSession();
            return jc;
        } catch (Exception e2) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e2);
        }
    }

    private void creerEcritures(CashinImportData cashinImportData, JournalConteneur jc) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // Rechercher le compteAnnexe
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdRole(Role.AFFILIE_PARITAIRE.getValue());
        compteAnnexe.setIdExterneRole(cashinImportData.getNoExtDebiteur());
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.setSession(session);
        compteAnnexe.retrieve();

        // Rechercher la section
        SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                compteAnnexe.getIdCompteAnnexe(), cashinImportData.getNoExtLitigeType(),
                cashinImportData.getNoExtLitigeSansType(), jc.getJournalModel());

        EcritureSimpleModel operationModel = new EcritureSimpleModel();
        operationModel.setCodeDebitCredit(computeCodeDebitCredit(cashinImportData.getMontantFrais()));
        operationModel.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        operationModel.setIdSection(section.getIdSection());
        operationModel.setDate(cashinImportData.getDateFrais().getSwissValue());

        APIRubrique rubriquePartCot = RubriqueUtil.retrieveRubriqueForReference(session, ID_REFERENCE_RUBRIQUE_FDP);
        operationModel.setIdRubrique(rubriquePartCot.getIdRubrique());
        operationModel.setMontant(cashinImportData.getMontantFrais().toString());

        jc.addEcriture(operationModel);

    }

    private String computeCodeDebitCredit(Montant montant) {
        if (montant.isPositive()) {
            return APIEcriture.DEBIT;
        } else {
            return APIEcriture.CREDIT;
        }
    }

    @Override
    protected String getEMailObject() {
        return "Importation écritures Cashin terminée";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @return the csvFilepath
     */
    public String getCsvFilename() {
        return csvFilename;
    }

    /**
     * @return the journalLibelle
     */
    public String getJournalLibelle() {
        return journalLibelle;
    }

    /**
     * @return the journalDate
     */
    public String getJournalDate() {
        return journalDate;
    }

    /**
     * @param csvFilepath the csvFilepath to set
     */
    public void setCsvFilename(String csvFilename) {
        this.csvFilename = csvFilename;
    }

    /**
     * @param journalLibelle the journalLibelle to set
     */
    public void setJournalLibelle(String journalLibelle) {
        this.journalLibelle = journalLibelle;
    }

    /**
     * @param journalDate the journalDate to set
     */
    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public TypeImportation getTypeImportation() {
        return typeImportation;
    }

    public void setTypeImportation(TypeImportation typeImportation) {
        this.typeImportation = typeImportation;
    }

}
