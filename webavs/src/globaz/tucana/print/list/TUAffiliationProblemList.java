package globaz.tucana.print.list;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.tucana.application.TUApplication;
import globaz.tucana.db.list.TUAffiliationProblem;
import globaz.tucana.db.list.TUAffiliationProblemManager;
import java.util.HashMap;
import java.util.Iterator;
import com.lowagie.text.DocumentException;

/**
 * Utilitaire Bouclement ALFA Comptabilité Auxiliaire<br/>
 * Cette liste permet de retrouver les affiliés qu'il faudrait ajouter ou supprimer "à la main" dans le bouclement ALFA.
 * 
 * @author DDA
 * 
 */
public class TUAffiliationProblemList extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private HashMap affilieOsiris = new HashMap();

    private String forDatePeriodeBegin;

    private String forDatePeriodeEnd;

    private String forGenreAssu;

    private String forIdExterneRubrique;

    /**
     * @throws Exception
     */
    public TUAffiliationProblemList() throws Exception {
        super(new BSession(TUApplication.DEFAULT_APPLICATION_TUCANA), TUApplication.APPLICATION_PREFIX, "ALFA",
                "Liste affiliation spécial", new TUAffiliationProblemManager(),
                TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    /**
     * @param session
     */
    public TUAffiliationProblemList(BSession session) {
        super(session, TUApplication.APPLICATION_PREFIX, "ALFA", "Liste affiliation spécial",
                new TUAffiliationProblemManager(), TUApplication.DEFAULT_APPLICATION_TUCANA);
    }

    @Override
    public void _beforeExecuteReport() {
        TUAffiliationProblemManager manager = (TUAffiliationProblemManager) _getManager();
        manager.setSession(getSession());

        manager.setForIdExterneRubrique(getForIdExterneRubrique());
        manager.setForDatePeriodeBegin(getForDatePeriodeBegin());
        manager.setForDatePeriodeEnd(getForDatePeriodeEnd());
        manager.setForGenreAssu(getForGenreAssu());
        manager.setSearchOsirisOnly(false);

        _setDocumentTitle(getSession().getLabel("LIST_AFFILIATION_SPECIALE"));

        String dateMois = getForDatePeriodeBegin().substring(4, 6) + "/" + getForDatePeriodeBegin().substring(0, 4);

        _addHeaderLine(null, "Rubrique " + getForIdExterneRubrique(), null, "Type d'assurance : "
                + getTypeAssurance(getForGenreAssu()), null, dateMois);
        getDocumentInfo().setTemplateName("");
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                "nom.caisse." + getSession().getIdLangueISO().toUpperCase()));

        findOsirisCases();
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
     * Ajout d'une ligne si l'entité n'est pas contenu dans la liste des entités d'osiris.
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        TUAffiliationProblem problem = (TUAffiliationProblem) entity;
        if (affilieOsiris.containsKey(problem.getHashKey())) {
            affilieOsiris.remove(problem.getHashKey());
        } else {
            _addCell(problem.getIdExterneRole());
            _addCell(JANumberFormatter.format(problem.getMontant()));
        }
    }

    /**
     * Retrouve les cas de comptabilité auxiliaire.
     * 
     */
    private void findOsirisCases() {
        TUAffiliationProblemManager managerOsiris = new TUAffiliationProblemManager();
        managerOsiris.setSession(getSession());

        managerOsiris.setForIdExterneRubrique(getForIdExterneRubrique());
        managerOsiris.setForDatePeriodeBegin(getForDatePeriodeBegin());
        managerOsiris.setForDatePeriodeEnd(getForDatePeriodeEnd());
        managerOsiris.setForGenreAssu(getForGenreAssu());
        managerOsiris.setSearchOsirisOnly(true);

        try {
            managerOsiris.find(BManager.SIZE_NOLIMIT);

            affilieOsiris = new HashMap();
            for (int i = 0; i < managerOsiris.size(); i++) {
                TUAffiliationProblem prob = (TUAffiliationProblem) managerOsiris.get(i);
                affilieOsiris.put(prob.getHashKey(), prob);
            }
        } catch (Exception e) {
            _addError(e.getMessage());
        }
    }

    @Override
    protected String getEMailObject() {
        return "L'impression du document 'Liste affiliation spécial (" + getTypeAssurance(getForGenreAssu())
                + ")' s'est terminée avec succès";
    }

    public String getForDatePeriodeBegin() {
        return forDatePeriodeBegin;
    }

    public String getForDatePeriodeEnd() {
        return forDatePeriodeEnd;
    }

    public String getForGenreAssu() {
        return forGenreAssu;
    }

    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    private String getTypeAssurance(String forGenreAssu) {
        String title = "";
        if ("812002".equals(getForGenreAssu())) {
            title = "Cotisation";
        } else if ("812009".equals(getForGenreAssu())) {
            title = "FFPP";
        }

        return title;
    }

    @Override
    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("AFFILIE"));
        _addColumnLeft(getSession().getLabel("MONTANT"));
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForDatePeriodeBegin(String forDatePeriodeBegin) {
        this.forDatePeriodeBegin = forDatePeriodeBegin;
    }

    public void setForDatePeriodeEnd(String forDatePeriodeEnd) {
        this.forDatePeriodeEnd = forDatePeriodeEnd;
    }

    public void setForGenreAssu(String forGenreAssu) {
        this.forGenreAssu = forGenreAssu;
    }

    public void setForIdExterneRubrique(String forIdExterneRubrique) {
        this.forIdExterneRubrique = forIdExterneRubrique;
    }

    /**
     * Liste les cas qui se trouvent uniquement dans la liste Osiris.
     */
    @Override
    protected void summary() throws FWIException {
        if (!affilieOsiris.isEmpty()) {
            Iterator it = affilieOsiris.keySet().iterator();
            while (it.hasNext()) {
                String key = "" + it.next();
                TUAffiliationProblem problem = (TUAffiliationProblem) affilieOsiris.get(key);

                _addCell(problem.getIdExterneRole());
                _addCell(JANumberFormatter.format(problem.getMontant()));
                try {
                    _addDataTableRow();
                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
