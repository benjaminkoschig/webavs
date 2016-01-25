package globaz.tucana.print.list;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.statistique.access.TUTemporaireManager;
import globaz.tucana.statistiques.TUCategorie;
import globaz.tucana.statistiques.TUColonne;
import globaz.tucana.statistiques.TUGroupeCategories;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.statistiques.TUJournalGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.lowagie.text.DocumentException;

/**
 * Liste F1V2
 * 
 * @author fgo
 * @version : version 1.0
 */
public class TUJournalRecapF1V1List extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NEGATIF = "negatif";
    private static final String POSITIF = "positif";
    private String annee = "";
    private String csAgence = "";
    private TUJournal journal = null;
    private List listCanton = null;
    private String mois = "";

    /**
     * @throws Exception
     */
    public TUJournalRecapF1V1List() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(TUApplication.DEFAULT_APPLICATION_TUCANA), TUApplication.APPLICATION_PREFIX, "ALFA",
                "Liste recap F1V1", new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @param session
     */
    public TUJournalRecapF1V1List(BSession session) {
        super(session, TUApplication.APPLICATION_PREFIX, "ALFA", session.getLabel("LIST_F1V1"),
                new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        getManager().setSession(getTransaction().getSession());

        try {
            journal = TUJournalGenerator.generate(getTransaction(), getAnnee(), getMois(), getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F001_1, TUJournal.TYPE_CHARGEMENT_AUCUN);
        } catch (Exception e) {
            // on ne fait rien, le journal sera vide
        }

        // _setDocumentTitle(getSession().getLabel("LIST_F1V2"));
        _setDocumentTitle("Journal récapitulatif pour le mois : ");
        String company = journal.getLibelleAgence();
        // try {
        // company =
        // getSession().getCodeLibelle(globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(TUApplication.APPLICATION_NAME).getProperty(TUApplication.CS_AGENCE));
        // } catch (RemoteException e) {
        // // en cas de prblème company sera vide
        // } catch (Exception e) {
        // // en cas de prblème company sera vide
        // }
        // _setCompanyName("Signature : ");

        _addHeaderLine("Signature :", company, JACalendar.todayJJsMMsAAAA());
        String title = "Journal récapitulatif pour le mois : " + journal.getMoisAlpha() + " " + journal.getAnnee();
        _addHeaderLine(null, null, null, title, null, null);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void bindPageHeader() throws Exception {

    }

    /**
     * Récupération de l'année de sélection
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    public String getCsAgence() {
        return csAgence;
    }

    public String getMois() {
        return mois;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        listCanton = new ArrayList();
        this._addColumnLeft("");
        Iterator iter = journal.getColonnes().keySet().iterator();
        while (iter.hasNext()) {
            TUColonne element = (TUColonne) journal.getColonnes().get(iter.next());
            listCanton.add(element.getAbreviation());
        }
        this._addColumnRight("En faveur du siège");
        this._addColumnRight("En faveur du l'agence");
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Modification de l'année de sélection
     * 
     * @param string
     */
    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    public void setMois(String newMois) {
        mois = newMois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #summary()
     */
    @Override
    protected void summary() throws FWIException {

        Iterator it = journal.getGroupeCategories().keySet().iterator();
        while (it.hasNext()) {
            TUGroupeCategories groupeCategories = (TUGroupeCategories) journal.getGroupeCategories().get(it.next());
            // ajoute les entêtes de groupes
            if (groupeCategories.getConfigStatistique().isAffiche()) {
                _addCell(groupeCategories.getLibelle());
            } else {
                _addCell("");
            }
            // ajoute les colonnes
            Collections.sort(groupeCategories.getCategories());
            Iterator iter = groupeCategories.getCategories().iterator();
            while (iter.hasNext()) {
                TUCategorie categorie = (TUCategorie) iter.next();
                // _addCell(categorie.getAbreviation());
                String total = categorie.getTotal().compareTo(new BigDecimal("0.00")) < 0 ? categorie.getTotal()
                        .negate().toString() : categorie.getTotal().toString();
                if (groupeCategories.getConfigStatistique().getSigne().equalsIgnoreCase(TUJournalRecapF1V1List.POSITIF)
                        && categorie.getTotal().compareTo(new BigDecimal("0.00")) > 0) {
                    _addCell(JANumberFormatter.fmt(total, true, true, true, 2));
                    _addCell("");
                } else if (groupeCategories.getConfigStatistique().getSigne()
                        .equalsIgnoreCase(TUJournalRecapF1V1List.NEGATIF)
                        && categorie.getTotal().compareTo(new BigDecimal("0.00")) < 0) {
                    _addCell(JANumberFormatter.fmt(total, true, true, true, 2));
                    _addCell("");
                } else {
                    _addCell("");
                    _addCell(JANumberFormatter.fmt(total, true, true, true, 2));
                }
                try {
                    _addDataTableRow();
                } catch (DocumentException e) {
                    // ne fait rien, ne génère pas la ligne.
                }
            }
        }
    }
}