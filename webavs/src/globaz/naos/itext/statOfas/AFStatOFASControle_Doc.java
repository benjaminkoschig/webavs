package globaz.naos.itext.statOfas;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.application.AFApplication;
import globaz.naos.db.statOfas.AFStatOFASControleDSManager;
import globaz.naos.db.statOfas.AFStatOFASControleManager;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Le document imprime les zones de facture selon les paramètres suivants: _modeRecouvrement : aucun, bvr,
 * remboursement, recouvrement direct _critereDecompte : interne, positif, note de credit, decompte zéro Date de
 * création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class AFStatOFASControle_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_STAT_OFAS_CONTROLE = "0210CAF";
    protected final static String TEMPLATE_FILENAME = "NAOS_STAT_OFAS_CONTROLE";
    public final static String TYPE_CONTROLE_AUTRE_MESURE = "811010";
    public final static String TYPE_REVISEUR_EXTERNE_CNA = "852003";
    public final static String TYPE_REVISEUR_EXTERNE_SANS_CNA = "852002";
    public final static String TYPE_REVISEUR_INTERNE = "852001";
    private String annee = "";
    private boolean start = true;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFStatOFASControle_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFStatOFASControle_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STATCONT_TITRE"));
        setParentWithCopy(parent);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFStatOFASControle_Doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STATCONT_TITRE"));
    }

    /**
     * Insert the method's description here. Creation date: (05.06.2003 08:55:49)
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _letterBody() {

        try {
            // On set le texte du haut du document
            String titre = getSession().getApplication().getLabel("STATCONT_TITRE", getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.P_TITRE, titre);
            String bureauInt = getSession().getApplication().getLabel("STATCONT_BUREAU_INTERNE",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_BUREAU_INTERNE, bureauInt);
            String bureauExt = getSession().getApplication().getLabel("STATCONT_BUREAU_EXTERNE_SCNA",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_BUREAU_EXTERNE, bureauExt);
            String cna = getSession().getApplication().getLabel("STATCONT_BUREAU_EXTERNE_CNA",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_CNA, cna);
            String rapportErreur = getSession().getApplication().getLabel("STATCONT_RAPPORT_AVEC_ERREUR",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_RAPPORT_ERREUR, rapportErreur);
            String controleSurplace = getSession().getApplication().getLabel("STATCONT_CONTROLE_SURPLACE",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_SUR_PLACE, controleSurplace);
            String controleAutre = getSession().getApplication().getLabel("STATCONT_CONTROLE_AUTRE",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_AUTRE_MESURE, controleAutre);
            String paiement = getSession().getApplication().getLabel("STATCONT_PAIEMENT_COTI",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_PAIEMENT, paiement);
            String remboursement = getSession().getApplication().getLabel("STATCONT_REMBOURS_COTI",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_REMBOURSEMENT, remboursement);
            String nombre = getSession().getApplication().getLabel("STATCONT_NOMBRE", getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_NOMBRE, nombre);
            String total = getSession().getApplication().getLabel("STATCONT_TOTAL", getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_TOTAL, total);
            String francs = getSession().getApplication().getLabel("STATCONT_FRANCS", getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_FRANCS, francs);
            String pasLier = getSession().getApplication().getLabel("STATCONT_ID_RAPPORT",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_ID_CONTROLE, pasLier);
            String lier = getSession().getApplication().getLabel("STATCONT_SANS_ID_RAPPORT",
                    getSession().getIdLangueISO());
            super.setParametres(AFStatOFASControle_Param.L_SANS_ID_CONTROLE, lier);
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un libellé pour les statistique OFAS " + ": AFStatOFASControle_Doc",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        try {
            AFStatOFASControleManager mana049 = new AFStatOFASControleManager();
            mana049.setSession(getSession());
            mana049.setForAnnee(getAnnee());
            mana049.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana049.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_INTERNE);
            Integer param049 = new Integer(mana049.getCount());
            super.setParametres(AFStatOFASControle_Param.P_049, param049);

            AFStatOFASControleManager mana050 = new AFStatOFASControleManager();
            mana050.setSession(getSession());
            mana050.setForAnnee(getAnnee());
            mana050.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana050.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_SANS_CNA);
            Integer param050 = new Integer(mana050.getCount());
            super.setParametres(AFStatOFASControle_Param.P_050, param050);

            AFStatOFASControleManager mana160 = new AFStatOFASControleManager();
            mana160.setSession(getSession());
            mana160.setForAnnee(getAnnee());
            mana160.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana160.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_CNA);
            Integer param160 = new Integer(mana160.getCount());
            super.setParametres(AFStatOFASControle_Param.P_160, param160);

            Integer param051 = new Integer(param049.intValue() + param050.intValue() + param160.intValue());
            super.setParametres(AFStatOFASControle_Param.P_051, param051);

            AFStatOFASControleManager mana164 = new AFStatOFASControleManager();
            mana164.setSession(getSession());
            mana164.setForAnnee(getAnnee());
            mana164.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana164.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_INTERNE);
            Integer param164 = new Integer(mana164.getCount());
            super.setParametres(AFStatOFASControle_Param.P_164, param164);

            AFStatOFASControleManager mana165 = new AFStatOFASControleManager();
            mana165.setSession(getSession());
            mana165.setForAnnee(getAnnee());
            mana165.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana165.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_SANS_CNA);
            Integer param165 = new Integer(mana165.getCount());
            super.setParametres(AFStatOFASControle_Param.P_165, param165);

            AFStatOFASControleManager mana166 = new AFStatOFASControleManager();
            mana166.setSession(getSession());
            mana166.setForAnnee(getAnnee());
            mana166.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana166.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_CNA);
            Integer param166 = new Integer(mana166.getCount());
            super.setParametres(AFStatOFASControle_Param.P_166, param166);

            Integer param167 = new Integer(param164.intValue() + param165.intValue() + param166.intValue());
            super.setParametres(AFStatOFASControle_Param.P_167, param167);

            AFStatOFASControleManager mana161 = new AFStatOFASControleManager();
            mana161.setSession(getSession());
            mana161.setForAnnee(getAnnee());
            mana161.setForIsRapportDifference(new Boolean(true));
            mana161.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana161.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_INTERNE);
            Integer param161 = new Integer(mana161.getCount());
            super.setParametres(AFStatOFASControle_Param.P_161, param161);

            AFStatOFASControleManager mana162 = new AFStatOFASControleManager();
            mana162.setSession(getSession());
            mana162.setForAnnee(getAnnee());
            mana162.setForIsRapportDifference(new Boolean(true));
            mana162.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana162.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_SANS_CNA);
            Integer param162 = new Integer(mana162.getCount());
            super.setParametres(AFStatOFASControle_Param.P_162, param162);

            AFStatOFASControleManager mana163 = new AFStatOFASControleManager();
            mana163.setSession(getSession());
            mana163.setForAnnee(getAnnee());
            mana163.setForIsRapportDifference(new Boolean(true));
            mana163.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana163.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_CNA);
            Integer param163 = new Integer(mana163.getCount());
            super.setParametres(AFStatOFASControle_Param.P_163, param163);

            Integer param052 = new Integer(param161.intValue() + param162.intValue() + param163.intValue());
            super.setParametres(AFStatOFASControle_Param.P_052, param052);

            AFStatOFASControleManager mana168 = new AFStatOFASControleManager();
            mana168.setSession(getSession());
            mana168.setForAnnee(getAnnee());
            mana168.setForIsRapportDifference(new Boolean(true));
            mana168.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana168.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_INTERNE);
            Integer param168 = new Integer(mana168.getCount());
            super.setParametres(AFStatOFASControle_Param.P_168, param168);

            AFStatOFASControleManager mana169 = new AFStatOFASControleManager();
            mana169.setSession(getSession());
            mana169.setForAnnee(getAnnee());
            mana169.setForIsRapportDifference(new Boolean(true));
            mana169.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana169.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_SANS_CNA);
            Integer param169 = new Integer(mana169.getCount());
            super.setParametres(AFStatOFASControle_Param.P_169, param169);

            AFStatOFASControleManager mana170 = new AFStatOFASControleManager();
            mana170.setSession(getSession());
            mana170.setForAnnee(getAnnee());
            mana170.setForIsRapportDifference(new Boolean(true));
            mana170.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana170.setForTypeReviseur(AFStatOFASControle_Doc.TYPE_REVISEUR_EXTERNE_CNA);
            Integer param170 = new Integer(mana170.getCount());
            super.setParametres(AFStatOFASControle_Param.P_170, param170);

            Integer param171 = new Integer(param168.intValue() + param169.intValue() + param170.intValue());
            super.setParametres(AFStatOFASControle_Param.P_171, param171);

            AFStatOFASControleDSManager mana053 = new AFStatOFASControleDSManager();
            mana053.setSession(getSession());
            mana053.setForAnneeControle(getAnnee());
            mana053.setForFromContEmpl(new Boolean(true));
            mana053.setForNotTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana053.setForIsIdRapportPasVide(new Boolean(true));
            mana053.find(BManager.SIZE_NOLIMIT);
            FWCurrency param053 = new FWCurrency(0);
            FWCurrency param054 = new FWCurrency(0);
            FWCurrency declMontant5354 = new FWCurrency(0);
            if (mana053.size() > 0) {
                for (int i = 0; i < mana053.size(); i++) {
                    declMontant5354 = ((DSDeclarationViewBean) mana053.getEntity(i)).getMontantFacture();
                    if (declMontant5354.isPositive()) {
                        param053.add(declMontant5354);
                    } else {
                        param054.add(declMontant5354);
                    }
                }
            }
            super.setParametres(AFStatOFASControle_Param.P_053, new Double(param053.doubleValue()));
            super.setParametres(AFStatOFASControle_Param.P_054, new Double(param054.doubleValue()));

            AFStatOFASControleDSManager mana172 = new AFStatOFASControleDSManager();
            mana172.setSession(getSession());
            mana172.setForAnneeControle(getAnnee());
            mana172.setForFromContEmpl(new Boolean(true));
            mana172.setForTypeControle(AFStatOFASControle_Doc.TYPE_CONTROLE_AUTRE_MESURE);
            mana172.setForIsIdRapportPasVide(new Boolean(true));
            mana172.find(BManager.SIZE_NOLIMIT);
            FWCurrency param172 = new FWCurrency(0);
            FWCurrency param173 = new FWCurrency(0);
            FWCurrency declMontant7273 = new FWCurrency(0);
            if (mana172.size() > 0) {
                for (int i = 0; i < mana172.size(); i++) {
                    declMontant7273 = ((DSDeclarationViewBean) mana172.getEntity(i)).getMontantFacture();
                    if (declMontant7273.isPositive()) {
                        param172.add(declMontant7273);
                    } else {
                        param173.add(declMontant7273);
                    }
                }
            }
            super.setParametres(AFStatOFASControle_Param.P_172, new Double(param172.doubleValue()));
            super.setParametres(AFStatOFASControle_Param.P_173, new Double(param173.doubleValue()));

            AFStatOFASControleDSManager manaPaiement = new AFStatOFASControleDSManager();
            manaPaiement.setSession(getSession());
            manaPaiement.setForAnneeControle(getAnnee());
            manaPaiement.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
            manaPaiement.setForIsIdRapportVide(new Boolean(true));
            manaPaiement.find(BManager.SIZE_NOLIMIT);
            FWCurrency paramPaiement = new FWCurrency(0);
            FWCurrency paramRemboursement = new FWCurrency(0);
            FWCurrency declMontant = new FWCurrency(0);
            if (manaPaiement.size() > 0) {
                for (int i = 0; i < manaPaiement.size(); i++) {
                    declMontant = ((DSDeclarationViewBean) manaPaiement.getEntity(i)).getMontantFacture();
                    if (declMontant.isPositive()) {
                        paramPaiement.add(declMontant);
                    } else {
                        paramRemboursement.add(declMontant);
                    }
                }
            }

            // DSDeclarationListViewBean manaPaiement = new
            // DSDeclarationListViewBean();
            // manaPaiement.setSession(getSession());
            // manaPaiement.setForAnnee(getAnnee());
            // manaPaiement.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
            // manaPaiement.setForIsIdRapportVide(new Boolean(true));
            // manaPaiement.find(BManager.SIZE_NOLIMIT);
            // FWCurrency paramPaiement = new FWCurrency(0);
            // FWCurrency paramRemboursement = new FWCurrency(0);
            // FWCurrency declMontant = new FWCurrency(0);
            // if(manaPaiement.size()>0){
            // for(int i=0;i<manaPaiement.size();i++){
            // declMontant =
            // ((DSDeclarationViewBean)manaPaiement.getEntity(i)).getMontantFacture();
            // if(declMontant.isPositive()){
            // paramPaiement.add(declMontant);
            // }else{
            // paramRemboursement.add(declMontant);
            // }
            // }
            // }
            super.setParametres(AFStatOFASControle_Param.P_PAIEMENT, new Double(paramPaiement.doubleValue()));
            super.setParametres(AFStatOFASControle_Param.P_REMBOURSEMENT, new Double(paramRemboursement.doubleValue()));

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un chiffre pour les statistique OFAS " + ": AFStatOFASControle_Doc",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

    }

    /**
     * Retourne la décision ou null en cas d'exception Insérez la description de la méthode ici. Date de création :
     * (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // super.setSendMailOnError(true);
        super.setDocumentTitle(getSession().getLabel("STATCONT_TITRE"));
        getDocumentInfo().setDocumentTypeNumber(AFStatOFASControle_Doc.NUM_REF_INFOROM_STAT_OFAS_CONTROLE);

    }

    @Override
    public final void beforeExecuteReport() {

        try {

        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
            }
        } finally {
        }

        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        super.setTemplateFile(AFStatOFASControle_Doc.TEMPLATE_FILENAME);
        // app = (FAApplication) getSession().getApplication();
        start = false;
        _letterBody();

    }

    public String getAnnee() {
        return annee;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        return start;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
