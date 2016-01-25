package globaz.libra.vb.formules;

import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormule;
import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.formules.LIComplementFormuleAction;
import globaz.libra.db.formules.LIComplementFormuleActionManager;
import globaz.libra.db.formules.LIFormulePDF;
import globaz.libra.vb.LIAbstractPersistentObjectViewBean;
import java.util.Iterator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIFormulesDetailViewBean extends LIAbstractPersistentObjectViewBean {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Object[] METHODES_SEL_FORMULE = new Object[] { new String[] { "csDocumentCS", "csDocument" } };
    private String className = new String();
    private LIComplementFormuleAction complementFormuleAction = null;

    private String csDocument = new String();

    private String csDocumentCS = new String();
    private String csDomaine = new String();
    private String csValeur = new String();
    private ENDefinitionFormule definitionFormule = null;

    private LIFormulePDF formulePDF = null;
    boolean isRetourSelCS = false;

    // ~ Constructors
    // ----------------------------------------------------------------------------------------------------

    public LIFormulesDetailViewBean() {
        super();
        definitionFormule = new ENDefinitionFormule();
        formulePDF = new LIFormulePDF();
        complementFormuleAction = new LIComplementFormuleAction();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void add() throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();
            processAdd(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("Unable to delete an object of this type (LIFormulesDetailViewBean");
    }

    public String getClassName() {
        return className;
    }

    public LIComplementFormuleAction getComplementFormuleAction() {
        return complementFormuleAction;
    }

    public String getCsDocument() {
        return csDocument;
    }

    public String getCsDocumentCS() {
        return csDocumentCS;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsValeur() {
        return csValeur;
    }

    public ENDefinitionFormule getDefinitionFormule() {
        return definitionFormule;
    }

    /**
     * Return les domaines existants
     */
    public String[] getDomaines() throws Exception {

        LIDomainesManager domMgr = new LIDomainesManager();
        domMgr.setSession((BSession) getISession());
        domMgr.find();

        int i = 0;
        String[] domainesTab = null;
        domainesTab = new String[(domMgr.size() * 2)];

        for (Iterator iterator = domMgr.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();

            if (getISession().hasRight(domaine.getNomApplication(), globaz.framework.secure.FWSecureConstants.READ)) {
                domainesTab[i] = domaine.getIdDomaine();
                domainesTab[i + 1] = getISession().getCodeLibelle(domaine.getCsDomaine());
                i += 2;
            }

        }

        return domainesTab;
    }

    public LIFormulePDF getFormulePDF() {
        return formulePDF;
    }

    @Override
    public String getId() {
        return formulePDF.getIdFormule();
    }

    /**
     * getter pour l'attribut methodes selection formule
     * 
     * @return la valeur courante de l'attribut methodes selection formule
     */
    public Object[] getMethodesSelectionFormule() {
        return METHODES_SEL_FORMULE;
    }

    @Override
    public BSpy getSpy() {
        return formulePDF.getSpy();
    }

    public boolean isRetourSelCS() {
        return isRetourSelCS;
    }

    private void processAdd(BITransaction transaction) throws Exception {

        // ajout de la definition formule
        definitionFormule.setCsDocument(getCsDocument());
        definitionFormule.add(transaction);

        // ajout de la formule pdf
        formulePDF.setDomaine(getCsDomaine());
        formulePDF.setClasseName(getClassName());
        formulePDF.setIdDefinitionFormule(definitionFormule.getIdDefinitionFormule());
        formulePDF.setCsType(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
        formulePDF.add(transaction);

        // ajout du complement formule
        complementFormuleAction.setIdFormule(formulePDF.getIdFormule());
        complementFormuleAction.setCsTypeFormule(ILIConstantesExternes.CS_ACTION_FORMULE_GRP);
        complementFormuleAction.setCsValeur(getCsValeur());
        complementFormuleAction.add(transaction);

        // Ajout du rappel
        ENRappel rappel = new ENRappel();
        rappel.setSession((BSession) getISession());
        rappel.setIdFormule(formulePDF.getIdFormule());
        rappel.add(transaction);

    }

    private void processUpd(BITransaction transaction) throws Exception {

        // update de la formule pdf
        formulePDF.setDomaine(getCsDomaine());
        formulePDF.setCsType(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
        formulePDF.setClasseName(getClassName());
        formulePDF.update(transaction);

        // update de la definition formule
        definitionFormule.setCsDocument(getCsDocument());
        definitionFormule.update(transaction);

        // update du complement formule
        complementFormuleAction.setCsTypeFormule(ILIConstantesExternes.CS_ACTION_FORMULE_GRP);
        complementFormuleAction.setCsValeur(getCsValeur());
        complementFormuleAction.update(transaction);

    }

    @Override
    public void retrieve() throws Exception {

        formulePDF.retrieve();

        definitionFormule.setIdDefinitionFormule(formulePDF.getIdDefinitionFormule());
        definitionFormule.retrieve();

        LIComplementFormuleActionManager compMgr = new LIComplementFormuleActionManager();
        compMgr.setSession((BSession) getISession());
        compMgr.setForIdFormule(formulePDF.getIdFormule());
        compMgr.setForCsTypeFormule(ILIConstantesExternes.CS_ACTION_FORMULE_GRP);
        compMgr.find();

        complementFormuleAction = (LIComplementFormuleAction) compMgr.getFirstEntity();

        if (null == complementFormuleAction) {
            complementFormuleAction = new LIComplementFormuleAction();
        }

    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setComplementFormule(LIComplementFormuleAction complementFormuleAction) {
        this.complementFormuleAction = complementFormuleAction;
    }

    public void setCsDocument(String csDocument) {
        this.csDocument = csDocument;
    }

    public void setCsDocumentCS(String csDocumentCS) {
        setCsDocument(csDocumentCS);
        getDefinitionFormule().setCsDocument(csDocumentCS);
        setRetourSelCS(true);
        this.csDocumentCS = csDocumentCS;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsValeur(String csValeur) {
        this.csValeur = csValeur;
    }

    public void setDefinitionFormule(ENDefinitionFormule definitionFormule) {
        this.definitionFormule = definitionFormule;
    }

    public void setFormulePDF(LIFormulePDF formulePDF) {
        this.formulePDF = formulePDF;
    }

    @Override
    public void setId(String newId) {
        formulePDF.setId(newId);
    }

    @Override
    public void setISession(BISession newSession) {
        super.setISession(newSession);
        if (newSession instanceof BSession) {
            formulePDF.setSession((BSession) newSession);
            definitionFormule.setSession((BSession) newSession);
            formulePDF.setSession((BSession) newSession);
        }
    }

    public void setRetourSelCS(boolean isRetourSelCS) {
        this.isRetourSelCS = isRetourSelCS;
    }

    @Override
    public void update() throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();
            processUpd(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

}
