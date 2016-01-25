package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.decompteannuelfonds.CADecompteAnnuelFonds;
import globaz.osiris.db.decompteannuelfonds.CADecompteAnnuelFondsManager;
import globaz.osiris.db.listfsfp.CANominativeFsfp;
import globaz.osiris.db.listfsfp.CANominativeFsfpManager;
import globaz.osiris.print.list.CAListDecompteAnnuelFonds;
import globaz.osiris.translation.CACodeSystem;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author sel Créé le 06 aout 2007
 */
public class CAProcessListDecompteAnnuelFonds extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_DATE_NON_RENSEIGNEE = "DATE_NON_RENSEIGNEE";

    private String dateDebutPeriode = new String();
    private String dateFinPeriode = new String();
    private String forIdCompteCourant = new String();

    private ArrayList idExterneEncaisseCompense = new ArrayList();
    // idexterne correspond au numéro externe de la rubrique
    private ArrayList idExterneFsfp = new ArrayList();

    private boolean isPoursuiteOuIrrecouvrable = false;

    private ArrayList sommeMontantEncaisseCompense = new ArrayList();
    private FWCurrency sommeMontantEncaisseCompenseTotal = new FWCurrency(0);
    private ArrayList sommeMontantFsfp = new ArrayList();

    private FWCurrency sommeMontantFsfpTotal = new FWCurrency(0);
    private FWCurrency sommeMontantPoursuiteIrrecouvrableTotal = new FWCurrency(0);
    private FWCurrency sommeMontantSoldeOuvert;

    public CAProcessListDecompteAnnuelFonds() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            createCalculCotisations();

            createCalculMontantEncaisseCompense();

            createCalculSoldeOuvert();

            createCalculSoldePoursuitresEtIrrec();

            createDocument();

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if ((JadeStringUtil.isBlank(getDateDebutPeriode()))) {
            this._addError(getSession().getLabel(CAProcessListDecompteAnnuelFonds.LABEL_DATE_NON_RENSEIGNEE));
        }
    }

    /**
     * Création du manager pour le calcul des cotisations
     * 
     * @throws Exception
     */
    private void createCalculCotisations() throws Exception {
        CADecompteAnnuelFondsManager managerMontantFsfp = new CADecompteAnnuelFondsManager(1);
        managerMontantFsfp.setSession(getSession());
        managerMontantFsfp.setForIdCompteCourant(getForIdCompteCourant());
        managerMontantFsfp.setDateDebutPeriode(JACalendar.format(getDateDebutPeriode(), JACalendar.FORMAT_YYYYMMDD));
        managerMontantFsfp.setDateFinPeriode(JACalendar.format(getDateFinPeriode(), JACalendar.FORMAT_YYYYMMDD));
        managerMontantFsfp.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerMontantFsfp.size(); i++) {
            CADecompteAnnuelFonds fondsFsfp = (CADecompteAnnuelFonds) managerMontantFsfp.getEntity(i);
            idExterneFsfp.add(fondsFsfp.getIdExterne());
            sommeMontantFsfp.add(fondsFsfp.getSommeMontant());
            // calcul du total
            sommeMontantFsfpTotal.add(fondsFsfp.getSommeMontant());
        }
    }

    /**
     * Création du manager pour le calcul du montant encaissé/compensé
     * 
     * @throws Exception
     */
    private void createCalculMontantEncaisseCompense() throws Exception {
        CADecompteAnnuelFondsManager managerMontantEncaisseCompense = new CADecompteAnnuelFondsManager(2);
        managerMontantEncaisseCompense.setSession(getSession());
        managerMontantEncaisseCompense.setForIdCompteCourant(getForIdCompteCourant());
        managerMontantEncaisseCompense.setDateDebutPeriode(JACalendar.format(getDateDebutPeriode(),
                JACalendar.FORMAT_YYYYMMDD));
        managerMontantEncaisseCompense.setDateFinPeriode(JACalendar.format(getDateFinPeriode(),
                JACalendar.FORMAT_YYYYMMDD));
        managerMontantEncaisseCompense.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerMontantEncaisseCompense.size(); i++) {
            CADecompteAnnuelFonds fondsEncaisseCompense = (CADecompteAnnuelFonds) managerMontantEncaisseCompense
                    .getEntity(i);
            idExterneEncaisseCompense.add(fondsEncaisseCompense.getIdExterne());
            sommeMontantEncaisseCompense.add(fondsEncaisseCompense.getSommeMontant());
            // calcul du total
            sommeMontantEncaisseCompenseTotal.add(fondsEncaisseCompense.getSommeMontant());
        }
    }

    /**
     * Création du manager pour le calcul du solde ouvert
     * 
     * @throws Exception
     */
    private void createCalculSoldeOuvert() throws Exception {
        CADecompteAnnuelFondsManager managerMontantSoldeOuvert = new CADecompteAnnuelFondsManager(3);
        managerMontantSoldeOuvert.setSession(getSession());
        managerMontantSoldeOuvert.setForIdCompteCourant(getForIdCompteCourant());
        managerMontantSoldeOuvert.setDateDebutPeriode(JACalendar.format(getDateDebutPeriode(),
                JACalendar.FORMAT_YYYYMMDD));
        managerMontantSoldeOuvert.setDateFinPeriode(JACalendar.format(getDateFinPeriode(), JACalendar.FORMAT_YYYYMMDD));
        managerMontantSoldeOuvert.find(BManager.SIZE_NOLIMIT);
        CADecompteAnnuelFonds decompteAnnuelFonds3 = (CADecompteAnnuelFonds) managerMontantSoldeOuvert.getFirstEntity();
        sommeMontantSoldeOuvert = new FWCurrency(decompteAnnuelFonds3.getSommeMontant());
    }

    /**
     * Création du manager pour le calcul du solde des poursuites et irrécouvrables
     * 
     * @throws Exception
     */
    private void createCalculSoldePoursuitresEtIrrec() throws Exception {
        CANominativeFsfpManager managerMontantPoursuiteIrrecouvrable = new CANominativeFsfpManager();
        managerMontantPoursuiteIrrecouvrable.setSession(getSession());
        managerMontantPoursuiteIrrecouvrable.setForIdCompteCourant(getForIdCompteCourant());
        managerMontantPoursuiteIrrecouvrable.setFromDateValeur(JACalendar.format(getDateFinPeriode(),
                JACalendar.FORMAT_YYYYMMDD));
        managerMontantPoursuiteIrrecouvrable.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerMontantPoursuiteIrrecouvrable.size(); i++) {
            isPoursuiteOuIrrecouvrable = false;
            CANominativeFsfp nominativeFsfp = (CANominativeFsfp) managerMontantPoursuiteIrrecouvrable.getEntity(i);
            // récupération du compteAnnexe
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setIdCompteAnnexe(nominativeFsfp.getIdCompteAnnexe());
            compteAnnexe.setSession(getSession());

            compteAnnexe.retrieve();

            if (compteAnnexe.isNew()) {
                throw new Exception(getSession().getLabel("5110"));
            }

            // récupération de la section
            CASection section = new CASection();
            section.setIdSection(nominativeFsfp.getIdSection());
            section.setSession(getSession());

            section.retrieve();

            if (section.isNew()) {
                throw new Exception(getSession().getLabel("5126"));
            }

            // vérifie si on est dans en présence d'un cas de poursuite ou
            // d'irrécouvrable
            if (compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)
                    || section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE)
                    || section.isSectionAuxPoursuites(true)) {
                isPoursuiteOuIrrecouvrable = true;
            }

            if (isPoursuiteOuIrrecouvrable) {
                sommeMontantPoursuiteIrrecouvrableTotal.add(nominativeFsfp.getSommeMontant());
            }
        }
    }

    /**
     * Création du document
     * 
     * @throws Exception
     * @throws IOException
     */
    private void createDocument() throws Exception, IOException {
        CAListDecompteAnnuelFonds excelDoc = new CAListDecompteAnnuelFonds(getSession(), getTransaction());
        excelDoc.setForIdCompteCourant(getForIdCompteCourant());
        excelDoc.setDateDebutPeriode(getDateDebutPeriode());
        excelDoc.setDateFinPeriode(getDateFinPeriode());
        excelDoc.setDocumentInfo(createDocumentInfo());
        excelDoc.populateSheet(idExterneFsfp, sommeMontantFsfp, idExterneEncaisseCompense,
                sommeMontantEncaisseCompense, sommeMontantFsfpTotal, sommeMontantEncaisseCompenseTotal,
                sommeMontantSoldeOuvert, sommeMontantPoursuiteIrrecouvrableTotal);

        this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("LIST_DECOMPTE_ANNUEL_FONDS_ERREUR");
        } else {
            return getSession().getLabel("LIST_DECOMPTE_ANNUEL_FONDS_OK");
        }
    }

    /**
     * @return Returns the forIdCompteCourant.
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * @param forIdCompteCourant
     *            The forIdCompteCourant to set.
     */
    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }
}
