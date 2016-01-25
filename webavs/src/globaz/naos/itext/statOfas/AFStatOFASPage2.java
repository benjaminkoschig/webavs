package globaz.naos.itext.statOfas;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.statOfas.AFStatistiquesOfas;
import globaz.naos.db.statOfas.AFStatistiquesOfasManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

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
public class AFStatOFASPage2 extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_STAT_OFAS_AFFILIE = "0118CAF";
    protected final static String TEMPLATE_FILENAME = "NAOS_STAT_OFAS_PAGE2";
    public final static Boolean TRUE = new Boolean(true);
    private String annee = "";
    private String anneeMoinUn = "";
    private String idTypeAdresse = "";
    private String montantMinime = "";
    private boolean start = true;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFStatOFASPage2() throws Exception {
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
    public AFStatOFASPage2(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
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
    public AFStatOFASPage2(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
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

            super.setParametres(AFStatOFAS_Param.L_REPARTITION_CANTON,
                    getSession().getApplication().getLabel("STAT_REPARTITION_CANTON", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NOMBRE_TOTAL,
                    getSession().getApplication().getLabel("STAT_NOMBRE_TOTAL", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_LTN,
                    getSession().getApplication().getLabel("STAT_LTN", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NBR_EMPLOYEUR,
                    getSession().getApplication().getLabel("STAT_NBR_EMPLOYEUR", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NBR_EMPLOYE,
                    getSession().getApplication().getLabel("STAT_NBR_EMPLOYE", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_DECOMPTE,
                    getSession().getApplication().getLabel("STAT_COTI_DECOMPTE", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_EXCLU_LTN,
                    getSession().getApplication().getLabel("STAT_EXCLU_LTN", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_LPP,
                    getSession().getApplication().getLabel("STAT_LPP", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_CAS1,
                    getSession().getApplication().getLabel("STAT_NBR_EMPLOYEUR_LPP", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_CAS2,
                    getSession().getApplication().getLabel("STAT_NBR_EMPLOYEUR_DS", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_CAS3,
                    getSession().getApplication().getLabel("STAT_NBR_CONTROLE", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_AUTRE,
                    getSession().getApplication().getLabel("STAT_AUTRE", getSession().getIdLangueISO()));
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un libellé pour les statistique OFAS " + ": AFStatOFASPage2",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        try {
            String totalCanton = "0";
            AFAffiliationManager manaCanton = new AFAffiliationManager();
            AFAffiliation affilie = new AFAffiliation();
            Map<String, String> mapCanton = new TreeMap<String, String>();
            String canton = "";
            String nombreAffilie = "";
            TIAdresseDataSource data = new TIAdresseDataSource();
            TITiers tiers = new TITiers();

            mapCanton.put(TILocalite.CS_ZURICH, "0");
            mapCanton.put(TILocalite.CS_BERNE, "0");
            mapCanton.put(TILocalite.CS_LUCERNE, "0");
            mapCanton.put(TILocalite.CS_URI, "0");
            mapCanton.put(TILocalite.CS_SCHWYZ, "0");
            mapCanton.put(TILocalite.CS_OBWALD, "0");
            mapCanton.put(TILocalite.CS_NIDWALD, "0");
            mapCanton.put(TILocalite.CS_GLARIS, "0");
            mapCanton.put(TILocalite.CS_ZOUG, "0");
            mapCanton.put(TILocalite.CS_FRIBOURG, "0");
            mapCanton.put(TILocalite.CS_SOLEURE, "0");
            mapCanton.put(TILocalite.CS_BALE_VILLE, "0");
            mapCanton.put(TILocalite.CS_BALE_CAMPAGNE, "0");
            mapCanton.put(TILocalite.CS_SCHAFFOUSE, "0");
            mapCanton.put(TILocalite.CS_APPENZELL_AR, "0");
            mapCanton.put(TILocalite.CS_APPENZELL_AI, "0");
            mapCanton.put(TILocalite.CS_SAINT_GALL, "0");
            mapCanton.put(TILocalite.CS_GRISONS, "0");
            mapCanton.put(TILocalite.CS_ARGOVIE, "0");
            mapCanton.put(TILocalite.CS_THURGOVIE, "0");
            mapCanton.put(TILocalite.CS_TESSIN, "0");
            mapCanton.put(TILocalite.CS_VAUD, "0");
            mapCanton.put(TILocalite.CS_VALAIS, "0");
            mapCanton.put(TILocalite.CS_NEUCHATEL, "0");
            mapCanton.put(TILocalite.CS_GENEVE, "0");
            mapCanton.put(TILocalite.CS_JURA, "0");
            mapCanton.put(TILocalite.CS_ETRANGER, "0");

            manaCanton.setSession(getSession());
            manaCanton.setFromDateFin("3112" + getAnnee());
            manaCanton.setForDateDebutAffLowerOrEqualTo("3112" + getAnnee());
            manaCanton.setForNotTypeAffiliation(CodeSystem.TYPE_AFFILI_FICHIER_CENT);
            manaCanton.find(BManager.SIZE_NOLIMIT);
            if (manaCanton.size() > 0) {
                for (int i = 0; i < manaCanton.size(); i++) {
                    affilie = (AFAffiliation) manaCanton.getEntity(i);
                    tiers = affilie.getTiers();
                    if (getIdTypeAdresse().equals("1")) {
                        data = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                IConstantes.CS_APPLICATION_DEFAUT, affilie.getAffilieNumero(), "3112" + getAnnee(),
                                true, tiers.getLangueIso());
                    } else {
                        data = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                ICommonConstantes.CS_APPLICATION_COTISATION, affilie.getAffilieNumero(), "3112"
                                        + getAnnee(), true, tiers.getLangueIso());
                    }

                    if (data == null) {
                        canton = TILocalite.CS_ETRANGER;
                        // throw new
                        // Exception("Pas d'adresse de courrier pour le tiers :"
                        // + affilie.getAffilieNumero());
                    } else {
                        Hashtable table = data.getData();
                        canton = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_ID);
                    }
                    if (JadeStringUtil.isEmpty(canton)) {
                        canton = TILocalite.CS_ETRANGER;
                        if (mapCanton.containsKey(canton)) {
                            nombreAffilie = mapCanton.get(canton);
                            nombreAffilie = "" + (JadeStringUtil.toInt(nombreAffilie) + 1);
                            mapCanton.put(canton, nombreAffilie);
                            totalCanton = "" + (JadeStringUtil.toInt(totalCanton) + 1);
                        } else {
                            mapCanton.put(canton, "1");
                            totalCanton = "" + (JadeStringUtil.toInt(totalCanton) + 1);
                        }
                        // throw new Exception("Pas de canton pour le tiers :" +
                        // affilie.getAffilieNumero());
                    } else {
                        if (mapCanton.containsKey(canton)) {
                            nombreAffilie = mapCanton.get(canton);
                            nombreAffilie = "" + (JadeStringUtil.toInt(nombreAffilie) + 1);
                            mapCanton.put(canton, nombreAffilie);
                            totalCanton = "" + (JadeStringUtil.toInt(totalCanton) + 1);
                        } else {
                            mapCanton.put(canton, "1");
                            totalCanton = "" + (JadeStringUtil.toInt(totalCanton) + 1);
                        }
                    }
                }
                super.setParametres(AFStatOFAS_Param.P_102, mapCanton.get(TILocalite.CS_ZURICH));
                super.setParametres(AFStatOFAS_Param.P_103, mapCanton.get(TILocalite.CS_BERNE));
                super.setParametres(AFStatOFAS_Param.P_104, mapCanton.get(TILocalite.CS_LUCERNE));
                super.setParametres(AFStatOFAS_Param.P_105, mapCanton.get(TILocalite.CS_URI));
                super.setParametres(AFStatOFAS_Param.P_106, mapCanton.get(TILocalite.CS_SCHWYZ));
                super.setParametres(AFStatOFAS_Param.P_107, mapCanton.get(TILocalite.CS_OBWALD));
                super.setParametres(AFStatOFAS_Param.P_108, mapCanton.get(TILocalite.CS_NIDWALD));
                super.setParametres(AFStatOFAS_Param.P_109, mapCanton.get(TILocalite.CS_GLARIS));
                super.setParametres(AFStatOFAS_Param.P_110, mapCanton.get(TILocalite.CS_ZOUG));
                super.setParametres(AFStatOFAS_Param.P_111, mapCanton.get(TILocalite.CS_FRIBOURG));
                super.setParametres(AFStatOFAS_Param.P_112, mapCanton.get(TILocalite.CS_SOLEURE));
                super.setParametres(AFStatOFAS_Param.P_113, mapCanton.get(TILocalite.CS_BALE_VILLE));
                super.setParametres(AFStatOFAS_Param.P_114, mapCanton.get(TILocalite.CS_BALE_CAMPAGNE));
                super.setParametres(AFStatOFAS_Param.P_115, mapCanton.get(TILocalite.CS_SCHAFFOUSE));
                super.setParametres(AFStatOFAS_Param.P_116, mapCanton.get(TILocalite.CS_APPENZELL_AR));
                super.setParametres(AFStatOFAS_Param.P_117, mapCanton.get(TILocalite.CS_APPENZELL_AI));
                super.setParametres(AFStatOFAS_Param.P_118, mapCanton.get(TILocalite.CS_SAINT_GALL));
                super.setParametres(AFStatOFAS_Param.P_119, mapCanton.get(TILocalite.CS_GRISONS));
                super.setParametres(AFStatOFAS_Param.P_120, mapCanton.get(TILocalite.CS_ARGOVIE));
                super.setParametres(AFStatOFAS_Param.P_121, mapCanton.get(TILocalite.CS_THURGOVIE));
                super.setParametres(AFStatOFAS_Param.P_122, mapCanton.get(TILocalite.CS_TESSIN));
                super.setParametres(AFStatOFAS_Param.P_123, mapCanton.get(TILocalite.CS_VAUD));
                super.setParametres(AFStatOFAS_Param.P_124, mapCanton.get(TILocalite.CS_VALAIS));
                super.setParametres(AFStatOFAS_Param.P_125, mapCanton.get(TILocalite.CS_NEUCHATEL));
                super.setParametres(AFStatOFAS_Param.P_126, mapCanton.get(TILocalite.CS_GENEVE));
                super.setParametres(AFStatOFAS_Param.P_127, mapCanton.get(TILocalite.CS_JURA));
                super.setParametres(AFStatOFAS_Param.P_128, totalCanton);
                super.setParametres(AFStatOFAS_Param.P_129, mapCanton.get(TILocalite.CS_ETRANGER));

            }
            DSDeclarationListViewBean manaLTN1 = new DSDeclarationListViewBean();
            manaLTN1.setSession(getSession());
            manaLTN1.setForAnnee(getAnneeMoinUn());
            manaLTN1.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN);
            manaLTN1.setForEtat(DSDeclarationViewBean.CS_COMPTABILISE);
            manaLTN1.find(BManager.SIZE_NOLIMIT);
            super.setParametres(AFStatOFAS_Param.P_707, "" + manaLTN1.size());

            AFStatistiquesOfasManager manaLTN2 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas nbrEmploye = new AFStatistiquesOfas();
            manaLTN2.setSession(getSession());
            manaLTN2.setForFields2(AFStatOFASPage2.TRUE);
            manaLTN2.setForFromLTN2(AFStatOFASPage2.TRUE);
            manaLTN2.setForNbrEmployes(AFStatOFASPage2.TRUE);
            manaLTN2.setAnnee(getAnneeMoinUn());
            manaLTN2.find(BManager.SIZE_NOLIMIT);
            if (manaLTN2.size() > 0) {
                nbrEmploye = (AFStatistiquesOfas) manaLTN2.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_708, "" + nbrEmploye.getNombre());
            }

            AFStatistiquesOfasManager manaLTN3 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas montantLTN = new AFStatistiquesOfas();
            manaLTN3.setSession(getSession());
            manaLTN3.setForFieldsLTN3(AFStatOFASPage2.TRUE);
            manaLTN3.setForFromLTN3(AFStatOFASPage2.TRUE);
            manaLTN3.setForMontantCoti(AFStatOFASPage2.TRUE);
            manaLTN3.setAnnee(getAnneeMoinUn());
            manaLTN3.find(BManager.SIZE_NOLIMIT);
            if (manaLTN3.size() > 0) {
                montantLTN = (AFStatistiquesOfas) manaLTN3.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_709, new Double(montantLTN.getMontant()));
            }

            AFStatistiquesOfasManager manaLTN4 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas nbrExclu = new AFStatistiquesOfas();
            manaLTN4.setSession(getSession());
            manaLTN4.setForFieldsLTN4(AFStatOFASPage2.TRUE);
            manaLTN4.setForFrom5(AFStatOFASPage2.TRUE);
            manaLTN4.setForExcluLTN(AFStatOFASPage2.TRUE);
            manaLTN4.setAnnee(getAnneeMoinUn());
            manaLTN4.find(BManager.SIZE_NOLIMIT);
            if (manaLTN4.size() > 0) {
                nbrExclu = (AFStatistiquesOfas) manaLTN4.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_712, "" + nbrExclu.getNombre());
            }

            AFStatistiquesOfasManager manaLPP1 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas nbrEmployeurLPP = new AFStatistiquesOfas();
            manaLPP1.setSession(getSession());
            manaLPP1.setForFields2(AFStatOFASPage2.TRUE);
            manaLPP1.setForFromLPP1(AFStatOFASPage2.TRUE);
            manaLPP1.setForNbrEmployeurLPP(AFStatOFASPage2.TRUE);
            manaLPP1.setAnnee(getAnnee());
            manaLPP1.find(BManager.SIZE_NOLIMIT);
            if (manaLPP1.size() > 0) {
                nbrEmployeurLPP = (AFStatistiquesOfas) manaLPP1.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_CAS1, "" + nbrEmployeurLPP.getNombre());
            }

            AFStatistiquesOfasManager manaLPP2 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas nbrEmployeurDS = new AFStatistiquesOfas();
            manaLPP2.setSession(getSession());
            manaLPP2.setForFields2(AFStatOFASPage2.TRUE);
            manaLPP2.setForFromLPP2(AFStatOFASPage2.TRUE);
            manaLPP2.setForNbrEmployeurDS(AFStatOFASPage2.TRUE);
            manaLPP2.setAnnee(getAnnee());
            manaLPP2.find(BManager.SIZE_NOLIMIT);
            if (manaLPP2.size() > 0) {
                nbrEmployeurDS = (AFStatistiquesOfas) manaLPP2.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_CAS2, "" + nbrEmployeurDS.getNombre());
            }

            AFStatistiquesOfasManager manaLPP3 = new AFStatistiquesOfasManager();
            AFStatistiquesOfas nbrControle = new AFStatistiquesOfas();
            manaLPP3.setSession(getSession());
            manaLPP3.setForFields2(AFStatOFASPage2.TRUE);
            manaLPP3.setForFromLPP3(AFStatOFASPage2.TRUE);
            manaLPP3.setForNbrControleEmpl(AFStatOFASPage2.TRUE);
            manaLPP3.setAnnee(getAnnee());
            manaLPP3.find(BManager.SIZE_NOLIMIT);
            if (manaLPP3.size() > 0) {
                nbrControle = (AFStatistiquesOfas) manaLPP3.getFirstEntity();
                super.setParametres(AFStatOFAS_Param.P_CAS3, "" + nbrControle.getNombre());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un chiffre pour les statistique OFAS " + ": AFStatOFASPage2",
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
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
        getDocumentInfo().setDocumentTypeNumber(AFStatOFASPage2.NUM_REF_INFOROM_STAT_OFAS_AFFILIE);

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
        super.setTemplateFile(AFStatOFASPage2.TEMPLATE_FILENAME);
        // app = (FAApplication) getSession().getApplication();
        start = false;
        _letterBody();

    }

    public String getAnnee() {
        return annee;
    }

    public String getAnneeMoinUn() {
        if (JadeStringUtil.isBlankOrZero(anneeMoinUn) && !JadeStringUtil.isBlank(getAnnee())) {
            setAnneeMoinUn("" + (JadeStringUtil.toInt(annee) - 1));
        }
        return anneeMoinUn;
    }

    public String getIdTypeAdresse() {
        return idTypeAdresse;
    }

    public String getMontantMinime() {
        return montantMinime;
    }

    public void initMontantMinime() {
        if (JadeStringUtil.isBlankOrZero(montantMinime) && !JadeStringUtil.isBlank(getAnnee())) {
            try {
                setMontantMinime(FWFindParameter.findParameter(getTransaction(), "10500070", "COTMININDE", "0101"
                        + getAnnee(), "0", 2));
            } catch (Exception e) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            }
        }
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

    public void setAnneeMoinUn(String anneeMoinUn) {
        this.anneeMoinUn = anneeMoinUn;
    }

    public void setIdTypeAdresse(String idTypeAdresse) {
        this.idTypeAdresse = idTypeAdresse;
    }

    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }
}
