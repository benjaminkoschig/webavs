package globaz.tucana.print.list;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.tucana.application.TUApplication;
import globaz.tucana.db.statistique.access.TUTemporaire;
import globaz.tucana.db.statistique.access.TUTemporaireManager;
import java.rmi.RemoteException;
import com.lowagie.text.DocumentException;

/**
 * Liste annuelle pour l'agence F002
 * 
 * @author fgo date de création : 11 juil. 06
 * @version : version 1.0
 * 
 */
public class TUListeAnnuelleAgence extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private int nbrLignePage = 0;
    private boolean printFirstPageInfos = true;
    private int sizeMgr = 1;

    /**
     * @throws Exception
     */
    public TUListeAnnuelleAgence() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(TUApplication.DEFAULT_APPLICATION_TUCANA), TUApplication.APPLICATION_PREFIX, "ALFA",
                "Liste annuelle par agence", new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @param session
     */
    public TUListeAnnuelleAgence(BSession session) {
        super(session, TUApplication.APPLICATION_PREFIX, "ALFA", session.getLabel("LIST_TIT_ANNUELLE_AGENCE"),
                new TUTemporaireManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        TUTemporaireManager manager = (TUTemporaireManager) _getManager();
        manager.setSession(getSession());
        manager.setForAnnee(getForAnnee());
        manager.setForOrder(TUTemporaireManager.ORDER_AGENCE_ANNUELLE);

        _setDocumentTitle(getSession().getLabel("LIST_TIT_ANNUELLE_AGENCE"));
        String company = "";
        try {
            company = getSession().getCodeLibelle(
                    globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(TUApplication.APPLICATION_NAME)
                            .getProperty(TUApplication.CS_AGENCE));
        } catch (RemoteException e) {
            // en cas de prblème company sera vide
        } catch (Exception e) {
            // en cas de prblème company sera vide
        }
        _setCompanyName(company);
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

    /**
     * Ajoute les informations de header sur la première page.
     * 
     * @throws FWIException
     */
    private void addFirstPageInfos() throws FWIException {
        if (!_getReport().isOpen()) {
            _getReport().open();
        }

        _addLine(getFontCell(), getSession().getLabel("LIST_MOIS_ANNEE"), null, null, null, null);
        _addLine(getFontCell(), "", null, null, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #addRow(globaz.globall.db.BEntity)
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        if (printFirstPageInfos) {
            addFirstPageInfos();
            printFirstPageInfos = false;
        }
        TUTemporaire temp = (TUTemporaire) entity;
        _addCell("");
        _addCell(temp.getCategorie());
        for (int i = 0; i < 12; i++) {
            if (i + 1 == Integer.parseInt(temp.getMoisNume())) {
                _addCell(JANumberFormatter.format(temp.getTotal()));
            } else {
                _addCell("");
            }
        }
        sizeMgr++;
        nbrLignePage++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #beginGroup(int,
     * globaz.globall.db.BEntity, globaz.globall.db.BEntity)
     */
    @Override
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
        // Test le saut de page if (lastEntity != null) {
        if (nbrLignePage > 20) {
            _addDataTableBreak();
            nbrLignePage = 0;
        }
        switch (level) {
            case 0: {
                try {
                    // insert le groupe
                    _addCell(((TUTemporaire) nextEntity).getGroupe());
                    _addDataTableGroupRow();
                    _addDataTableGroupRow();
                } catch (FWIException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                break;
            }
            default: {
                break;
            }
        }

    }

    /**
     * Récupération de l'année de sélection
     * 
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        _hideRowBackground();
        _addColumnLeft("", 10);
        _addColumnLeft("", 50);
        _addColumnCenter("1");
        _addColumnCenter("2");
        _addColumnCenter("3");
        _addColumnCenter("4");
        _addColumnCenter("5");
        _addColumnCenter("6");
        _addColumnCenter("7");
        _addColumnCenter("8");
        _addColumnCenter("9");
        _addColumnCenter("10");
        _addColumnCenter("11");
        _addColumnCenter("12");

        // définition des méthodes de groupage
        _groupOnMethod("getGroupe");
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
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #summary()
     */
    @Override
    protected void summary() throws FWIException {
        if (sizeMgr >= _getManager().size()) {
            try {
                _hideRowBackground();
                _addCell("");
                _addDataTableGroupRow();
                _addCell("");
                _addDataTableGroupRow();
                _addCell("");
                _addDataTableGroupRow();
                _hideRowBackground();
                _addCell(globaz.globall.db.GlobazServer.getCurrentSystem()
                        .getApplication(TUApplication.APPLICATION_NAME)
                        .getProperty(TUApplication.APPLICATION_LOCALITE_CORRESPONDANCE)
                        + " " + JACalendar.todayJJsMMsAAAA());
                _addCell("");
                _addCell("");
                _addCell(getSession().getLabel("LIST_TIMBRE"));
                _addDataTableGroupRow();

            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FWIException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}