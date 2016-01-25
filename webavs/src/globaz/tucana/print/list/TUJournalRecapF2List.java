package globaz.tucana.print.list;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.statistique.access.TUTemporaireManager;
import globaz.tucana.statistiques.TUCategorie;
import globaz.tucana.statistiques.TUColonne;
import globaz.tucana.statistiques.TUEntry;
import globaz.tucana.statistiques.TUGroupeCategories;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.statistiques.TUJournalGenerator;
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
public class TUJournalRecapF2List extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String csAgence = "";
    private TUJournal journal = null;
    private List listCanton = null;
    private int nbrLignePage = 0;

    /**
     * @throws Exception
     */
    public TUJournalRecapF2List() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(TUApplication.DEFAULT_APPLICATION_TUCANA), TUApplication.APPLICATION_PREFIX, "ALFA",
                "Liste recap F2", new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @param session
     */
    public TUJournalRecapF2List(BSession session) {
        super(session, TUApplication.APPLICATION_PREFIX, "ALFA", session.getLabel("LIST_F2"),
                new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        getManager().setSession(getTransaction().getSession());

        try {
            journal = TUJournalGenerator.generate(getTransaction(), getAnnee(), "", getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F002, TUJournal.TYPE_CHARGEMENT_MOIS);
        } catch (Exception e) {
            // on ne fait rien, le journal sera vide
        }
        String title = "PAR MOIS / PAR TRIMESTRE / PAR SEMESTRE";
        String agence = JadeStringUtil.isBlankOrZero(getCsAgence()) ? "ALFA" : journal.getLibelleAgence();
        _addHeaderLine(null, agence, null, title, null, "Exercice : " + journal.getAnnee());

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

    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        str.append(getSession().getLabel("PRO_TIT_JOURNAL_RECAP_F2"));
        return str.toString();
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
            this._addColumnRight(element.getLibelle());
            listCanton.add(element.getLibelle());
        }
        this._addColumnRight("Total");
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #summary()
     */
    @Override
    protected void summary() throws FWIException {
        try {
            Iterator it = journal.getGroupeCategories().keySet().iterator();
            while (it.hasNext()) {
                TUGroupeCategories groupeCategories = (TUGroupeCategories) journal.getGroupeCategories().get(it.next());

                // if (nbrLignePage > 35) {
                // _addDataTableBreak();
                // nbrLignePage = 0;
                // }
                // ajoute les entêtes de groupes
                if (groupeCategories.getConfigStatistique().isAffiche()) {
                    _addCell(groupeCategories.getLibelle());
                } else {
                    _addCell("");
                }
                /*
                 * _addCell(""); _addCell(""); _addCell(""); _addCell(""); _addCell(""); _addCell(""); _addCell("");
                 * _addCell(""); _addCell(""); _addCell("");
                 */
                nbrLignePage++;
                try {
                    // System.out.println(newRow.size());
                    this._addDataTableGroupRow();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                // ajoute les colonnes
                Collections.sort(groupeCategories.getCategories());
                Iterator iter = groupeCategories.getCategories().iterator();
                while (iter.hasNext()) {
                    TUCategorie categorie = (TUCategorie) iter.next();
                    _addCell(categorie.getAbreviation());
                    boolean vok = false;
                    for (int i = 0; i < listCanton.size(); i++) {
                        vok = false;
                        Iterator iter2 = categorie.getEntries().keySet().iterator();
                        while (iter2.hasNext() && !vok) {
                            TUEntry entry = (TUEntry) categorie.getEntries().get(iter2.next());
                            if (entry.getColonne().getLibelle().equalsIgnoreCase((String) listCanton.get(i))
                                    && !JadeStringUtil.isBlank((String) listCanton.get(i))) {
                                _addCell(JANumberFormatter.fmt(entry.getTotal().toString(), true, true, true, 2));
                                vok = true;
                            }
                        }
                        if (!vok) {
                            _addCell("");
                        }
                    }
                    try {
                        if (categorie.getTotal() != null) {
                            _addCell(JANumberFormatter.fmt(categorie.getTotal().toString(), true, true, true, 2));
                        } else {
                            _addCell("0.00");
                        }
                        // System.out.println(newRow.size());
                        _addDataTableRow();
                        nbrLignePage++;

                    } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}