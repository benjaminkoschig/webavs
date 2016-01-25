package globaz.osiris.db.contentieux;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.globall.db.BAccessBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jdbc.JadeJdbcProfiler;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author user
 * @revision dda 13.01.2005
 */
public class CAExtraitCompteListViewBean extends BAccessBean implements FWListViewBeanInterface, BIContainer,
        BIPersistentObjectList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String SOLDE_INITIAL = "SOLDE_INITIAL";

    private String forIdSection = new String();
    private String forIdTypeOperation = new String();
    private String forSelectionSections = new String();
    private String forSelectionTri = new String();
    private String fromPositionnement = new String();
    private String idCompteAnnexe = new String();
    private String idSection = new String();

    private Vector ligneExtraitCompte = new Vector();

    private String message = new String();
    private boolean modeScreen = true;
    private String msgType = new String();
    private String printLanguage;

    private String untilDate = new String();

    private String untilSection = new String();

    /**
     * Commentaire relatif au constructeur CAExtraitCompte.
     */
    public CAExtraitCompteListViewBean() {
        super();
    }

    /**
     * Method canDoNext. Cette méthode retourne false, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la
     * page JSP à cause des templates de pagination
     * 
     * @return boolean
     */
    public boolean canDoNext() {
        return false;
    }

    /**
     * Method canDoPrev. Cette méthode retourne false, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la
     * page JSP à cause des templates de pagination
     * 
     * @return boolean
     */
    public boolean canDoPrev() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#clear()
     */
    @Override
    public void clear() {
        ligneExtraitCompte.clear();
    }

    /**
     * Affichage du solde initial.
     * 
     * @param compte
     * @throws Exception
     */
    private void displaySoldeInitial(BTransaction transaction, CAExtraitCompteManager manager, CACompteAnnexe compte)
            throws Exception {
        if (!JadeStringUtil.isBlank(getFromPositionnement())
                && !compte.getSoldeInitialAt(getFromPositionnement(), getForSelectionSections()).equalsIgnoreCase(
                        "0.00")
                && (getForSelectionTri().equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE) || getForSelectionTri()
                        .equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR))) {
            getSoldeInitialOrderByDate(transaction, manager, getPrintLanguage(), compte);
        } else if (!JadeStringUtil.isBlank(getFromPositionnement())
                && getForSelectionTri().equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_IDSECTION)
                && !getFromPositionnement().equals(getUntilSection())) {
            getSoldeInitialOrderBySection(transaction, manager, getPrintLanguage());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 08:41:58)
     */
    @Override
    public void find() {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            this.find(transaction, new CAExtraitCompteManager());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:55:39)
     */
    public void find(BTransaction transaction, CAExtraitCompteManager manager) {
        JadeLogger.trace(this, "find(" + transaction + ")");
        JadeJdbcProfiler.startCollect(this, "find()");

        try {
            CACompteAnnexe compte = readCompteAnnexe();

            if ((!compte.hasErrors()) && (!compte.isNew())) {
                setLanguageToProcess(compte);

                ligneExtraitCompte.clear();

                initManager(manager, transaction);
                manager.executeQuery(transaction);

                displaySoldeInitial(transaction, manager, compte);

                if ((!manager.hasErrors()) && (!manager.isEmpty())) {
                    processFoundedExtraitCompte(transaction, manager);
                } else {
                    throw new Exception("Erreur de lecture, CAExtraitCompteManager");
                }
            }
        } catch (Exception e) {
            // CAST OMMIT
        } finally {
            JadeJdbcProfiler.stopCollect(this, "find()");
            if (transaction != null) {
                if (transaction.hasErrors()) {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(transaction.getErrors().toString());
                } else if (transaction.hasWarnings()) {
                    setMsgType(FWViewBeanInterface.WARNING);
                    setMessage(transaction.getWarnings().toString());
                } else {
                    setMsgType(FWViewBeanInterface.OK);
                    setMessage(transaction.getErrors().toString());
                }
            } else {
                if (hasErrors()) {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getErrors().toString());
                } else if (hasWarnings()) {
                    setMsgType(FWViewBeanInterface.WARNING);
                    setMessage(getWarnings().toString());
                } else {
                    setMsgType(FWViewBeanInterface.OK);
                    setMessage(getErrors().toString());
                }
            }
        }

    }

    @Override
    public void findNext() throws Exception {
        // Not used here
    }

    @Override
    public void findPrev() throws Exception {
        // Not used here
    }

    @Override
    public BIPersistentObject get(int idx) {
        return (BIPersistentObject) getLigneExtraitCompte().get(idx);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#getCount()
     */
    @Override
    public int getCount() throws Exception {
        return ligneExtraitCompte.size();
    }

    /**
     * Retourne la date a affichée pour le solde initial lors d'un tri par section, soit la date de la première ligne de
     * l'extrait de compte.
     * 
     * @param manager
     * @return
     * @throws Exception
     */
    public String getDateSoldeInitialModeTriParSection(CAExtraitCompteManager manager) throws Exception {
        for (int i = 0; i < manager.size(); i++) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) manager.getEntity(i);

            if (extraitCompte.getSum() != 0) {
                return extraitCompte.getDate();
            }
        }

        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#getEntity(int)
     */
    @Override
    public BIEntity getEntity(int idx) {
        return (BIEntity) ligneExtraitCompte.get(idx);
    }

    /**
     * @return
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:12:02)
     * 
     * @return String
     */
    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:09:44)
     * 
     * @return String
     */
    public String getForSelectionSections() {
        return forSelectionSections;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:10:57)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:13:16)
     * 
     * @return String
     */
    public String getFromPositionnement() {
        return fromPositionnement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 12:11:48)
     * 
     * @return String
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 12:12:09)
     * 
     * @return String
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:55:01)
     * 
     * @return Vector
     */
    public Vector getLigneExtraitCompte() {
        return ligneExtraitCompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * Method getOffset. Cette méthode retourne 0, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la page
     * JSP à cause des templates de pagination
     * 
     * @return int
     */
    public int getOffset() {
        return 0;
    }

    /**
     * @return
     */
    public String getPrintLanguage() {
        return printLanguage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWListViewBeanInterface#getSize()
     */
    @Override
    public int getSize() {
        return size();
    }

    /**
     * Return la description du solde initial grâce à la session.
     * 
     * @param codeIsoLangue
     * @return
     * @throws Exception
     */
    private String getSoldeInitialDescriptionFromSession(String codeIsoLangue) throws Exception {
        return getSession().getApplication().getLabel(CAExtraitCompteListViewBean.SOLDE_INITIAL, codeIsoLangue);
    }

    /**
     * Affiche le solde initial lors d'un tri par date.
     * 
     * @param transaction
     * @param manager
     * @param codeIsoLangue
     * @param compte
     * @throws Exception
     */
    private void getSoldeInitialOrderByDate(BTransaction transaction, CAExtraitCompteManager manager,
            String codeIsoLangue, CACompteAnnexe compte) throws Exception {
        FWCurrency soldeInitial = new FWCurrency(compte.getSoldeInitialAt(getFromPositionnement(),
                getForSelectionSections()));

        if (!soldeInitial.isZero()) {
            CALigneExtraitCompte ligne = new CALigneExtraitCompte();
            ligne.setDate(JACalendar.format(new JADate(getFromPositionnement())));
            ligne.setDescription(getSoldeInitialDescriptionFromSession(codeIsoLangue));

            ligne.setTotal(soldeInitial.toString());
            ligneExtraitCompte.add(ligne);
        }
    }

    /**
     * Affiche le solde initial lors d'un tri par section.
     * 
     * @param transaction
     * @param manager
     * @param codeIsoLangue
     * @throws Exception
     */
    private void getSoldeInitialOrderBySection(BTransaction transaction, CAExtraitCompteManager manager,
            String codeIsoLangue) throws Exception {
        CASectionManager secMan = new CASectionManager();
        secMan.setForIdCompteAnnexe(getIdCompteAnnexe());

        secMan.setUntilIdExterne(getFromPositionnement());

        if (getForSelectionSections().equals(CAExtraitCompteManager.SOLDE_OPEN)) {
            secMan.setForSelectionSections(CASectionManager.SOLDE_OPEN);
        } else if (getForSelectionSections().equals(CAExtraitCompteManager.SOLDE_CLOSED)) {
            secMan.setForSelectionSections(CASectionManager.SOLDE_CLOSED);
        } else {
            secMan.setForSelectionSections(CASectionManager.SOLDE_ALL);
        }

        secMan.setSession(getSession());

        secMan.find();

        FWCurrency soldeSec = new FWCurrency();
        if (!secMan.hasErrors()) {
            for (int i = 0; i < secMan.size(); i++) {
                CASection sec = (CASection) secMan.getEntity(i);
                soldeSec.add(sec.getSolde());
            }

            if (!soldeSec.isZero()) {
                CALigneExtraitCompte ligne = new CALigneExtraitCompte();

                ligne.setDate(getDateSoldeInitialModeTriParSection(manager));
                ligne.setDescription(getSoldeInitialDescriptionFromSession(codeIsoLangue));
                ligne.setTotal(soldeSec.toString());

                ligneExtraitCompte.add(ligne);
            }
        } else {
            throw new Exception("Erreur reprise CASection");
        }
    }

    /**
     * @return
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * @return
     */
    public String getUntilSection() {
        return untilSection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#hasRightRead()
     */
    @Override
    public boolean hasRightRead() {
        return super.hasRight(globaz.framework.secure.FWSecureConstants.READ);
    }

    /**
     * Initialisation du manager avec les critères de recherche.
     * 
     * @param manager
     */
    private void initManager(CAExtraitCompteManager manager, BTransaction transaction) throws SQLException, JAException {
        manager.setIdCompteAnnexe(getIdCompteAnnexe());

        manager.setOrder(getForSelectionTri());
        manager.setSolde(getForSelectionSections());

        if (getForSelectionTri().equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE)
                || getForSelectionTri().equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR)) {
            if (!JadeStringUtil.isBlank(getFromPositionnement())) {
                manager.setFromDate(getFromPositionnement());
            }

            if (!JadeStringUtil.isBlank(getUntilDate())) {
                manager.setUntilDate(getUntilDate());
            }
        }

        if (getForSelectionTri().equalsIgnoreCase(CAExtraitCompteManager.ORDER_BY_IDSECTION)) {
            if (!JadeStringUtil.isBlank(getFromPositionnement())) {
                manager.setFromIdExterne(getFromPositionnement());
            }

            if (!JadeStringUtil.isBlank(getUntilSection())) {
                manager.setUntilIdExterne(getUntilSection());
            }
        }

        if (!getForIdTypeOperation().equalsIgnoreCase(CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION)) {
            manager.setLikeIdTypeOperation(getForIdTypeOperation());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            manager.setForIdSection(getForIdSection());
        }

        if (manager.getSession() == null) {
            manager.setSession(getSession());
        }

        manager.init(transaction);
        manager.fillSelectVariables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return ligneExtraitCompte.isEmpty();
    }

    /**
     * @return
     */
    public boolean isModeScreen() {
        return modeScreen;
    }

    @Override
    public Iterator iterator() {
        List list = new ArrayList(getLigneExtraitCompte());
        return list.iterator();
    }

    /**
     * Ajoute tous les "Extrait de compte" trouvées au vecteur "ligneExtraitCompte".
     * 
     * @param transaction
     * @param manager
     */
    private void processFoundedExtraitCompte(BTransaction transaction, CAExtraitCompteManager manager) throws Exception {
        for (int i = 0; i < manager.size(); i++) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) manager.getEntity(i);

            if (extraitCompte.getSum() != 0) {
                CALigneExtraitCompte ligne = new CALigneExtraitCompte();
                ligne.setDescription(manager.getDescription(extraitCompte, transaction, getPrintLanguage(),
                        isModeScreen()));

                if ((extraitCompte.getIdTypeOperation().startsWith(APIOperation.CAECRITURE))
                        || (extraitCompte.getIdTypeOperation().startsWith(APIOperation.CAAUXILIAIRE))) {
                    ligne.setTotal("" + extraitCompte.getSum());
                } else {
                    ligne.setHorsCompteAnnexe("" + extraitCompte.getSum());
                }

                ligne.setDate(extraitCompte.getDate());
                ligne.setIdExterne(extraitCompte.getIdExterne());
                ligne.setDateJournal(extraitCompte.getDateJournal());
                ligne.setProvenancePmt(getSession().getCodeLibelle(extraitCompte.getProvenancePmt()));
                ligne.setCodeProvenancePmt(extraitCompte.getProvenancePmt());
                ligne.setIdSectionCompensation(extraitCompte.getIdSectionCompensation());
                ligne.setSectionCompensationDeSur(extraitCompte.getSectionCompensationDeSur());
                ligneExtraitCompte.add(ligne);
            }
        }
    }

    /**
     * Lecture du compte annexe Nécessaire pour obtenir les informations telles n° d'affilié, nom, prénom etc...
     * 
     * @return
     * @throws Exception
     */
    private CACompteAnnexe readCompteAnnexe() throws Exception {
        CACompteAnnexe compte = new CACompteAnnexe();
        compte.setIdCompteAnnexe(getIdCompteAnnexe());
        compte.setISession(getSession());
        compte.retrieve();
        return compte;
    }

    /**
     * @param string
     */
    public void setForIdSection(String string) {
        forIdSection = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:12:02)
     * 
     * @param newForIdTypeOperation
     *            String
     */
    public void setForIdTypeOperation(String newForIdTypeOperation) {
        forIdTypeOperation = newForIdTypeOperation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:09:44)
     * 
     * @param newForSelectionSections
     *            String
     */
    public void setForSelectionSections(String newForSelectionSections) {
        forSelectionSections = newForSelectionSections;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:10:57)
     * 
     * @param newForSelectionTri
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 09:13:16)
     * 
     * @param newFromPositionnement
     *            String
     */
    public void setFromPositionnement(String newFromPositionnement) {
        fromPositionnement = newFromPositionnement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 12:11:48)
     * 
     * @param newIdCompteAnnexe
     *            String
     */
    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 12:12:09)
     * 
     * @param newIdSection
     *            String
     */
    public void setIdSection(String newIdSection) {
        idSection = newIdSection;
    }

    /**
     * Ou les résultats doivent-ils être imprimés (écran ou pdf) ? Si la langue d'impression n'est pas encore définie,
     * la langue par default sera utilisé.
     * 
     * @param compte
     */
    private void setLanguageToProcess(CACompteAnnexe compte) {
        if (JadeStringUtil.isBlank(getPrintLanguage())) {
            setPrintLanguage(getSession().getIdLangueISO());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:55:01)
     * 
     * @param newLigneExtraitCompte
     *            Vector
     */
    public void setLigneExtraitCompte(Vector newLigneExtraitCompte) {
        ligneExtraitCompte = newLigneExtraitCompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param b
     */
    public void setModeScreen(boolean b) {
        modeScreen = b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @param b
     */
    public void setPrintLanguage(String s) {
        printLanguage = s;
    }

    /**
     * @param string
     */
    public void setUntilDate(String s) {
        untilDate = s;
    }

    /**
     * @param string
     */
    public void setUntilSection(String string) {
        untilSection = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 14:45:14)
     * 
     * @return int
     */
    @Override
    public int size() {
        return ligneExtraitCompte.size();
    }

}
