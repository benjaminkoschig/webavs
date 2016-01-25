package globaz.corvus.vb.recap;

import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.vb.exception.REDetailRecapInstanciationException;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author FGo Représentation graphique du détail d'une récap mensuelle
 * @version 1.0 Created on Fri Nov 30 11:34:32 CET 2007
 */
public class REDetailRecapMensuelleViewBean extends RERecapMensuelle implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateRapport = "";
    // private Map mapTree = null;
    private RERecapElement elem500001 = new RERecapElement("500001");
    private RERecapElement elem500002 = new RERecapElement("500002");
    private RERecapElement elem500003 = new RERecapElement("500003");
    private RERecapElement elem500004 = new RERecapElement("500004");
    private RERecapElement elem500005 = new RERecapElement("500005");
    private RERecapElement elem500006 = new RERecapElement("500006");
    private RERecapElement elem500007 = new RERecapElement("500007");
    private RERecapElement elem500099 = new RERecapElement("500099");
    private RERecapElement elem501001 = new RERecapElement("501001");
    private RERecapElement elem501002 = new RERecapElement("501002");
    private RERecapElement elem501003 = new RERecapElement("501003");
    private RERecapElement elem501004 = new RERecapElement("501004");
    private RERecapElement elem501005 = new RERecapElement("501005");
    private RERecapElement elem501006 = new RERecapElement("501006");
    private RERecapElement elem501007 = new RERecapElement("501007");
    private RERecapElement elem501099 = new RERecapElement("501099");
    private RERecapElement elem503001 = new RERecapElement("503001");
    private RERecapElement elem503002 = new RERecapElement("503002");
    private RERecapElement elem503003 = new RERecapElement("503003");
    private RERecapElement elem503004 = new RERecapElement("503004");
    private RERecapElement elem503005 = new RERecapElement("503005");
    private RERecapElement elem503006 = new RERecapElement("503006");
    private RERecapElement elem503007 = new RERecapElement("503007");
    private RERecapElement elem503099 = new RERecapElement("503099");
    private RERecapElement elem510001 = new RERecapElement("510001");
    private RERecapElement elem510002 = new RERecapElement("510002");
    private RERecapElement elem510003 = new RERecapElement("510003");
    private RERecapElement elem510004 = new RERecapElement("510004");
    private RERecapElement elem510005 = new RERecapElement("510005");
    private RERecapElement elem510006 = new RERecapElement("510006");
    private RERecapElement elem510007 = new RERecapElement("510007");
    private RERecapElement elem510099 = new RERecapElement("510099");
    private RERecapElement elem511001 = new RERecapElement("511001");
    private RERecapElement elem511002 = new RERecapElement("511002");
    private RERecapElement elem511003 = new RERecapElement("511003");
    private RERecapElement elem511004 = new RERecapElement("511004");
    private RERecapElement elem511005 = new RERecapElement("511005");
    private RERecapElement elem511006 = new RERecapElement("511006");
    private RERecapElement elem511007 = new RERecapElement("511007");
    private RERecapElement elem511099 = new RERecapElement("511099");
    private RERecapElement elem513001 = new RERecapElement("513001");
    private RERecapElement elem513002 = new RERecapElement("513002");
    private RERecapElement elem513003 = new RERecapElement("513003");
    private RERecapElement elem513004 = new RERecapElement("513004");
    private RERecapElement elem513005 = new RERecapElement("513005");
    private RERecapElement elem513006 = new RERecapElement("513006");
    private RERecapElement elem513007 = new RERecapElement("513007");
    private RERecapElement elem513099 = new RERecapElement("513099");

    private String eMailAdress = "";
    private String to2_500 = "";
    private String to2_501 = "";
    private String to2_503 = "";
    private String to2_510 = "";
    private String to2_511 = "";

    private String to2_513 = "";

    /**
     * Constructeur de la classe RERecapMensuelleViewBean
     */
    public REDetailRecapMensuelleViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        // Chargement des éléments
        loadElements();
        eMailAdress = getSession().getUserEMail();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);
        if (!hasErrors()) {
            updateElement(transaction);
        }
    }

    /**
     * Récupère la date de rapport
     * 
     * @return the dateRapport
     */
    public String getDateRapport() {
        return dateRapport;
    }

    /**
     * @return the elem500001
     */
    public RERecapElement getElem500001() {
        return elem500001;
    }

    /**
     * @return the elem500002
     */
    public RERecapElement getElem500002() {
        return elem500002;
    }

    /**
     * @return the elem500003
     */
    public RERecapElement getElem500003() {
        return elem500003;
    }

    /**
     * @return the elem500004
     */
    public RERecapElement getElem500004() {
        return elem500004;
    }

    /**
     * @return the elem500005
     */
    public RERecapElement getElem500005() {
        return elem500005;
    }

    /**
     * @return the elem500006
     */
    public RERecapElement getElem500006() {
        return elem500006;
    }

    /**
     * @return the elem500007
     */
    public RERecapElement getElem500007() {
        return elem500007;
    }

    /**
     * @return the elem500099
     */
    public RERecapElement getElem500099() {
        return elem500099;
    }

    /**
     * @return the elem501001
     */
    public RERecapElement getElem501001() {
        return elem501001;
    }

    /**
     * @return the elem501002
     */
    public RERecapElement getElem501002() {
        return elem501002;
    }

    /**
     * @return the elem501003
     */
    public RERecapElement getElem501003() {
        return elem501003;
    }

    /**
     * @return the elem501004
     */
    public RERecapElement getElem501004() {
        return elem501004;
    }

    /**
     * @return the elem501005
     */
    public RERecapElement getElem501005() {
        return elem501005;
    }

    /**
     * @return the elem501006
     */
    public RERecapElement getElem501006() {
        return elem501006;
    }

    /**
     * @return the elem501007
     */
    public RERecapElement getElem501007() {
        return elem501007;
    }

    /**
     * @return the elem501099
     */
    public RERecapElement getElem501099() {
        return elem501099;
    }

    /**
     * @return the elem503001
     */
    public RERecapElement getElem503001() {
        return elem503001;
    }

    /**
     * @return the elem503002
     */
    public RERecapElement getElem503002() {
        return elem503002;
    }

    /**
     * @return the elem503003
     */
    public RERecapElement getElem503003() {
        return elem503003;
    }

    /**
     * @return the elem503004
     */
    public RERecapElement getElem503004() {
        return elem503004;
    }

    /**
     * @return the elem503005
     */
    public RERecapElement getElem503005() {
        return elem503005;
    }

    /**
     * @return the elem503006
     */
    public RERecapElement getElem503006() {
        return elem503006;
    }

    /**
     * @return the elem503007
     */
    public RERecapElement getElem503007() {
        return elem503007;
    }

    /**
     * @return the elem503099
     */
    public RERecapElement getElem503099() {
        return elem503099;
    }

    /**
     * @return the elem510001
     */
    public RERecapElement getElem510001() {
        return elem510001;
    }

    /**
     * @return the elem510002
     */
    public RERecapElement getElem510002() {
        return elem510002;
    }

    /**
     * @return the elem510003
     */
    public RERecapElement getElem510003() {
        return elem510003;
    }

    /**
     * @return the elem510004
     */
    public RERecapElement getElem510004() {
        return elem510004;
    }

    /**
     * @return the elem510005
     */
    public RERecapElement getElem510005() {
        return elem510005;
    }

    /**
     * @return the elem510006
     */
    public RERecapElement getElem510006() {
        return elem510006;
    }

    /**
     * @return the elem510007
     */
    public RERecapElement getElem510007() {
        return elem510007;
    }

    /**
     * @return the elem510099
     */
    public RERecapElement getElem510099() {
        return elem510099;
    }

    /**
     * @return the elem511001
     */
    public RERecapElement getElem511001() {
        return elem511001;
    }

    /**
     * @return the elem511002
     */
    public RERecapElement getElem511002() {
        return elem511002;
    }

    /**
     * @return the elem511003
     */
    public RERecapElement getElem511003() {
        return elem511003;
    }

    /**
     * @return the elem511004
     */
    public RERecapElement getElem511004() {
        return elem511004;
    }

    /**
     * @return the elem511005
     */
    public RERecapElement getElem511005() {
        return elem511005;
    }

    /**
     * @return the elem511006
     */
    public RERecapElement getElem511006() {
        return elem511006;
    }

    /**
     * @return the elem511007
     */
    public RERecapElement getElem511007() {
        return elem511007;
    }

    /**
     * @return the elem511099
     */
    public RERecapElement getElem511099() {
        return elem511099;
    }

    /**
     * @return the elem513001
     */
    public RERecapElement getElem513001() {
        return elem513001;
    }

    /**
     * @return the elem513002
     */
    public RERecapElement getElem513002() {
        return elem513002;
    }

    /**
     * @return the elem513003
     */
    public RERecapElement getElem513003() {
        return elem513003;
    }

    /**
     * @return the elem513004
     */
    public RERecapElement getElem513004() {
        return elem513004;
    }

    /**
     * @return the elem513005
     */
    public RERecapElement getElem513005() {
        return elem513005;
    }

    /**
     * @return the elem513006
     */
    public RERecapElement getElem513006() {
        return elem513006;
    }

    /**
     * @return the elem513007
     */
    public RERecapElement getElem513007() {
        return elem513007;
    }

    /**
     * @return the elem513099
     */
    public RERecapElement getElem513099() {
        return elem513099;
    }

    /**
     * @return the eMailAdress
     */
    public String getEMailAdress() {
        return eMailAdress;
    }

    public String getTo2_500() {
        return to2_500;
    }

    public String getTo2_501() {
        return to2_501;
    }

    public String getTo2_503() {
        return to2_503;
    }

    public String getTo2_510() {
        return to2_510;
    }

    public String getTo2_511() {
        return to2_511;
    }

    public String getTo2_513() {
        return to2_513;
    }

    /**
     * Charge le manager d'éléments de la récap mensuelle
     * 
     * @return
     */
    private RERecapElementManager loadElementManager() {
        RERecapElementManager mgr = new RERecapElementManager();
        mgr.setSession(getSession());
        mgr.setForIdRecapMensuelle(getIdRecapMensuelle());
        try {
            mgr.find();
        } catch (Exception e) {
            mgr.clear();
        }
        return mgr;
    }

    /**
     * Chargement les éléments
     * 
     * @throws REDetailRecapInstanciationException
     */
    private void loadElements() throws REDetailRecapInstanciationException {
        // Recherche les éléments de la récap mensuelle
        RERecapElementManager elementMgr = loadElementManager();
        // Ajoute les éléments dans la collection
        for (int i = 0; i < elementMgr.size(); i++) {
            RERecapElement entity = (RERecapElement) elementMgr.getEntity(i);
            String methodName = "setElem".concat(entity.getCodeRecap());
            try {
                Method methode = getClass().getMethod(methodName, new Class[] { RERecapElement.class });
                methode.invoke(this, new Object[] { entity });
            } catch (SecurityException e) {
                throw new REDetailRecapInstanciationException(getClass().getName().concat("loadElement()"), e);
            } catch (IllegalArgumentException e) {
                throw new REDetailRecapInstanciationException(getClass().getName().concat("loadElement()"), e);
            } catch (NoSuchMethodException e) {
                throw new REDetailRecapInstanciationException(getClass().getName().concat("loadElement()"), e);
            } catch (IllegalAccessException e) {
                throw new REDetailRecapInstanciationException(getClass().getName().concat("loadElement()"), e);
            } catch (InvocationTargetException e) {
                throw new REDetailRecapInstanciationException(getClass().getName().concat("loadElement()"), e);
            }
        }
    }

    /**
     * Modifie la date de rapport
     * 
     * @param dateRapport
     *            the newDateRapport to set
     */
    public void setDateRapport(String newDateRapport) {
        dateRapport = newDateRapport;
    }

    /**
     * @param newElem500001
     *            the elem500001 to set
     */
    public void setElem500001(RERecapElement newElem500001) {
        elem500001 = newElem500001;
    }

    /**
     * @param newElem500002
     *            the elem500002 to set
     */
    public void setElem500002(RERecapElement newElem500002) {
        elem500002 = newElem500002;
    }

    /**
     * @param newElem500003
     *            the elem500003 to set
     */
    public void setElem500003(RERecapElement newElem500003) {
        elem500003 = newElem500003;
    }

    /**
     * @param newElem500004
     *            the elem500004 to set
     */
    public void setElem500004(RERecapElement newElem500004) {
        elem500004 = newElem500004;
    }

    /**
     * @param newElem500005
     *            the elem500005 to set
     */
    public void setElem500005(RERecapElement newElem500005) {
        elem500005 = newElem500005;
    }

    /**
     * @param newElem500006
     *            the elem500006 to set
     */
    public void setElem500006(RERecapElement newElem500006) {
        elem500006 = newElem500006;
    }

    /**
     * @param newElem500007
     *            the elem500007 to set
     */
    public void setElem500007(RERecapElement newElem500007) {
        elem500007 = newElem500007;
    }

    /**
     * @param newElem500099
     *            the elem500099 to set
     */
    public void setElem500099(RERecapElement newElem500099) {
        elem500099 = newElem500099;
    }

    /**
     * @param newElem501001
     *            the elem501001 to set
     */
    public void setElem501001(RERecapElement newElem501001) {
        elem501001 = newElem501001;
    }

    /**
     * @param newElem501002
     *            the elem501002 to set
     */
    public void setElem501002(RERecapElement newElem501002) {
        elem501002 = newElem501002;
    }

    /**
     * @param newElem501003
     *            the elem501003 to set
     */
    public void setElem501003(RERecapElement newElem501003) {
        elem501003 = newElem501003;
    }

    /**
     * @param newElem501004
     *            the elem501004 to set
     */
    public void setElem501004(RERecapElement newElem501004) {
        elem501004 = newElem501004;
    }

    /**
     * @param newElem501005
     *            the elem501005 to set
     */
    public void setElem501005(RERecapElement newElem501005) {
        elem501005 = newElem501005;
    }

    /**
     * @param newElem501006
     *            the elem501006 to set
     */
    public void setElem501006(RERecapElement newElem501006) {
        elem501006 = newElem501006;
    }

    /**
     * @param newElem501007
     *            the elem501007 to set
     */
    public void setElem501007(RERecapElement newElem501007) {
        elem501007 = newElem501007;
    }

    /**
     * @param newElem501099
     *            the elem501099 to set
     */
    public void setElem501099(RERecapElement newElem501099) {
        elem501099 = newElem501099;
    }

    /**
     * @param newElem503001
     *            the elem503001 to set
     */
    public void setElem503001(RERecapElement newElem503001) {
        elem503001 = newElem503001;
    }

    /**
     * @param newElem503002
     *            the elem503002 to set
     */
    public void setElem503002(RERecapElement newElem503002) {
        elem503002 = newElem503002;
    }

    /**
     * @param newElem503003
     *            the elem503003 to set
     */
    public void setElem503003(RERecapElement newElem503003) {
        elem503003 = newElem503003;
    }

    /**
     * @param newElem503004
     *            the elem503004 to set
     */
    public void setElem503004(RERecapElement newElem503004) {
        elem503004 = newElem503004;
    }

    /**
     * @param newElem503005
     *            the elem503005 to set
     */
    public void setElem503005(RERecapElement newElem503005) {
        elem503005 = newElem503005;
    }

    /**
     * @param newElem503006
     *            the elem503006 to set
     */
    public void setElem503006(RERecapElement newElem503006) {
        elem503006 = newElem503006;
    }

    /**
     * @param newElem503007
     *            the elem503007 to set
     */
    public void setElem503007(RERecapElement newElem503007) {
        elem503007 = newElem503007;
    }

    /**
     * @param newElem503099
     *            the elem503099 to set
     */
    public void setElem503099(RERecapElement newElem503099) {
        elem503099 = newElem503099;
    }

    /**
     * @param newElem510001
     *            the elem510001 to set
     */
    public void setElem510001(RERecapElement newElem510001) {
        elem510001 = newElem510001;
    }

    /**
     * @param newElem510002
     *            the elem510002 to set
     */
    public void setElem510002(RERecapElement newElem510002) {
        elem510002 = newElem510002;
    }

    /**
     * @param newElem510003
     *            the elem510003 to set
     */
    public void setElem510003(RERecapElement newElem510003) {
        elem510003 = newElem510003;
    }

    /**
     * @param newElem510004
     *            the elem510004 to set
     */
    public void setElem510004(RERecapElement newElem510004) {
        elem510004 = newElem510004;
    }

    /**
     * @param newElem510005
     *            the elem510005 to set
     */
    public void setElem510005(RERecapElement newElem510005) {
        elem510005 = newElem510005;
    }

    /**
     * @param newElem510006
     *            the elem510006 to set
     */
    public void setElem510006(RERecapElement newElem510006) {
        elem510006 = newElem510006;
    }

    /**
     * @param newElem510007
     *            the elem510007 to set
     */
    public void setElem510007(RERecapElement newElem510007) {
        elem510007 = newElem510007;
    }

    /**
     * @param newElem510099
     *            the elem510099 to set
     */
    public void setElem510099(RERecapElement newElem510099) {
        elem510099 = newElem510099;
    }

    /**
     * @param newElem511001
     *            the elem511001 to set
     */
    public void setElem511001(RERecapElement newElem511001) {
        elem511001 = newElem511001;
    }

    /**
     * @param newElem511002
     *            the elem511002 to set
     */
    public void setElem511002(RERecapElement newElem511002) {
        elem511002 = newElem511002;
    }

    /**
     * @param newElem511003
     *            the elem511003 to set
     */
    public void setElem511003(RERecapElement newElem511003) {
        elem511003 = newElem511003;
    }

    /**
     * @param newElem511004
     *            the elem511004 to set
     */
    public void setElem511004(RERecapElement newElem511004) {
        elem511004 = newElem511004;
    }

    /**
     * @param newElem511005
     *            the elem511005 to set
     */
    public void setElem511005(RERecapElement newElem511005) {
        elem511005 = newElem511005;
    }

    /**
     * @param newElem511006
     *            the elem511006 to set
     */
    public void setElem511006(RERecapElement newElem511006) {
        elem511006 = newElem511006;
    }

    /**
     * @param newElem511007
     *            the elem511007 to set
     */
    public void setElem511007(RERecapElement newElem511007) {
        elem511007 = newElem511007;
    }

    /**
     * @param newElem511099
     *            the elem511099 to set
     */
    public void setElem511099(RERecapElement newElem511099) {
        elem511099 = newElem511099;
    }

    /**
     * @param newElem513001
     *            the elem513001 to set
     */
    public void setElem513001(RERecapElement newElem513001) {
        elem513001 = newElem513001;
    }

    /**
     * @param newElem513002
     *            the elem513002 to set
     */
    public void setElem513002(RERecapElement newElem513002) {
        elem513002 = newElem513002;
    }

    /**
     * @param newElem513003
     *            the elem513003 to set
     */
    public void setElem513003(RERecapElement newElem513003) {
        elem513003 = newElem513003;
    }

    /**
     * @param newElem513004
     *            the elem513004 to set
     */
    public void setElem513004(RERecapElement newElem513004) {
        elem513004 = newElem513004;
    }

    /**
     * @param newElem513005
     *            the elem513005 to set
     */
    public void setElem513005(RERecapElement newElem513005) {
        elem513005 = newElem513005;
    }

    /**
     * @param newElem513006
     *            the elem513006 to set
     */
    public void setElem513006(RERecapElement newElem513006) {
        elem513006 = newElem513006;
    }

    /**
     * @param newElem513007
     *            the elem513007 to set
     */
    public void setElem513007(RERecapElement newElem513007) {
        elem513007 = newElem513007;
    }

    /**
     * @param newElem513099
     *            the elem513099 to set
     */
    public void setElem513099(RERecapElement newElem513099) {
        elem513099 = newElem513099;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#setSession(globaz.globall.db.BSession)
     */
    @Override
    public void setSession(BSession newSession) {
        // le code ci-dessous permet de "setter" la session dans tous les objets
        // RERecapElement référencés comme attributs de la classe
        Method methode[] = getClass().getDeclaredMethods();
        super.setSession(newSession);
        try {
            for (int j = 0; j < methode.length; j++) {
                if ((methode[j]).getName().startsWith("getElem")) {
                    ((BEntity) methode[j].invoke(this, new Object[0])).setSession(getSession());
                }
            }
        } catch (IllegalArgumentException e) {
            setMsgType(FWMessage.ERREUR);
            setMessage(getClass().getName().concat("setSession() : erreur lors de l'assignement des sessions - ")
                    .concat(e.toString()));
        } catch (IllegalAccessException e) {
            setMsgType(FWMessage.ERREUR);
            setMessage(getClass().getName().concat("setSession() : erreur lors de l'assignement des sessions - ")
                    .concat(e.toString()));
        } catch (InvocationTargetException e) {
            setMsgType(FWMessage.ERREUR);
            setMessage(getClass().getName().concat("setSession() : erreur lors de l'assignement des sessions - ")
                    .concat(e.toString()));
        }
    }

    public void setTo2_500(String to2_500) {
        this.to2_500 = to2_500;
    }

    public void setTo2_501(String to2_501) {
        this.to2_501 = to2_501;
    }

    public void setTo2_503(String to2_503) {
        this.to2_503 = to2_503;
    }

    public void setTo2_510(String to2_510) {
        this.to2_510 = to2_510;
    }

    public void setTo2_511(String to2_511) {
        this.to2_511 = to2_511;
    }

    public void setTo2_513(String to2_513) {
        this.to2_513 = to2_513;
    }

    /**
     * Effectue un update sur tout les attributs de type RERecapElement
     * 
     * @param transaction
     * @throws Exception
     */
    private void updateElement(BTransaction transaction) throws Exception {
        Method methode[] = getClass().getDeclaredMethods();
        for (int j = 0; j < methode.length; j++) {
            if ((methode[j]).getName().startsWith("getElem")) {
                if (((BEntity) methode[j].invoke(this, new Object[0])).isNew()) {
                    ((BEntity) methode[j].invoke(this, new Object[0])).add(transaction);
                } else {
                    ((BEntity) methode[j].invoke(this, new Object[0])).update(transaction);
                }
            }
        }
    }

    /*
     * public String getCas02(){
     * 
     * int total=0;
     * 
     * total = new Integer(getElem500002().getCas()).intValue() + new Integer(getElem501002().getCas()).intValue() + new
     * Integer(getElem503002().getCas()).intValue() + new Integer(getElem510002().getCas()).intValue() + new
     * Integer(getElem511002().getCas()).intValue() + new Integer(getElem513002().getCas()).intValue();
     * 
     * return String.valueOf(total); }
     * 
     * public String getCas03(){
     * 
     * int total=0;
     * 
     * total = new Integer(getElem500003().getCas()).intValue() + new Integer(getElem501003().getCas()).intValue() + new
     * Integer(getElem503003().getCas()).intValue() + new Integer(getElem510003().getCas()).intValue() + new
     * Integer(getElem511003().getCas()).intValue() + new Integer(getElem513003().getCas()).intValue();
     * 
     * return String.valueOf(total); }
     * 
     * public String getCas04(){
     * 
     * int total=0;
     * 
     * total = new Integer(getElem500004().getCas()).intValue() + new Integer(getElem501004().getCas()).intValue() + new
     * Integer(getElem503004().getCas()).intValue() + new Integer(getElem510004().getCas()).intValue() + new
     * Integer(getElem511004().getCas()).intValue() + new Integer(getElem513004().getCas()).intValue();
     * 
     * return String.valueOf(total); }
     * 
     * public String getCas05(){
     * 
     * int total=0;
     * 
     * total = new Integer(getElem500005().getCas()).intValue() + new Integer(getElem501005().getCas()).intValue() + new
     * Integer(getElem503005().getCas()).intValue() + new Integer(getElem510005().getCas()).intValue() + new
     * Integer(getElem511005().getCas()).intValue() + new Integer(getElem513005().getCas()).intValue();
     * 
     * return String.valueOf(total); }
     * 
     * public String getCasEnCours(){
     * 
     * int total=0;
     * 
     * total = new Integer(getCas02()).intValue() + new Integer(getCas03()).intValue() + new
     * Integer(getCas04()).intValue();
     * 
     * return String.valueOf(total);
     * 
     * }
     * 
     * public String getCasMensuelleSousTotal(){
     * 
     * int total=0;
     * 
     * total = new Integer(getCasEnCours()).intValue() + new Integer(getCas05()).intValue();
     * 
     * return String.valueOf(total);
     * 
     * }
     */

}
