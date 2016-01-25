package globaz.draco.print.list;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.listes.DSSuiviDemManager;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.translation.CodeSystem;

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
public class DSSuiviDem_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_SUIVI_DEM = "0283CDS";
    protected final static String TEMPLATE_FILENAME = "DRACO_SUIVI_DEM";
    private String annee = "";
    private boolean start = true;
    private String typeDeclaration = "";

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public DSSuiviDem_Doc() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public DSSuiviDem_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, DSApplication.DEFAULT_APPLICATION_ROOT, "LISTESUIVIDEM");
        super.setDocumentTitle(getSession().getLabel("DRACO_SUIVI_TITRE"));
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
    public DSSuiviDem_Doc(BSession session) throws java.lang.Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "LISTESUIVIDEM");
        super.setDocumentTitle(getSession().getLabel("DRACO_SUIVI_TITRE"));
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
     * 
     * @throws Exception
     */
    protected void _letterBody() throws Exception {

        try {
            // On set le texte du haut du document
            String titre = getSession().getApplication().getLabel("DRACO_SUIVI_TITRE", getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.P_TITRE, titre + " " + getAnnee());
            String nbrDsEnvoyee = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_ENVOYEES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_ENVOYE, nbrDsEnvoyee);
            String nbrDsNonRecu = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_NONRECUES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_NONRECU, nbrDsNonRecu);
            String nbrDsRecu = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_RECUES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_RECU, nbrDsRecu);

            String decFinalDS = getSession().getApplication().getLabel("DRACO_SUIVI_DEC_FINAL_DS",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_DEC_FINAL_DS, decFinalDS);
            String decFinalReleve = getSession().getApplication().getLabel("DRACO_SUIVI_DEC_FINAL_RELEVE",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_DEC_FINAL_RELEVE, decFinalReleve);
            String boucleAcompteDS = getSession().getApplication().getLabel("DRACO_SUIVI_BOUCLE_ACOMPTE_DS",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_BOUCLE_ACOMPTE_DS, boucleAcompteDS);
            String boucleAcompteReleve = getSession().getApplication().getLabel("DRACO_SUIVI_BOUCLE_ACOMPTE_RELEVE",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_BOUCLE_ACOMPTE_RELEVE, boucleAcompteReleve);

            String nbrDsOuverte = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_OUVERTES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_OUVERTE, nbrDsOuverte);
            String nbrDsAFacturer = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_AFACTURER",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_AFACTURER, nbrDsAFacturer);
            String nbrDsCompta = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_DS_COMPTABILISES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_DS_COMPTA, nbrDsCompta);
            String nbrReleveOuvert = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_RELEVE_OUVERTES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_RELEVE_OUVERTE, nbrReleveOuvert);
            String nbrReleveAFacturer = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_RELEVE_AFACTURER",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_RELEVE_AFACTURER, nbrReleveAFacturer);
            String nbrReleveCompta = getSession().getApplication().getLabel("DRACO_SUIVI_NBR_RELEVE_COMPTABILISES",
                    getSession().getIdLangueISO());
            super.setParametres(DSSuiviDem_Param.L_NBR_RELEVE_COMPTA, nbrReleveCompta);
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getApplication().getLabel("DRACO_SUIVI_ERREUR_LABEL", getSession().getIdLangueISO())
                            + ": DSSuiviDem_Doc", FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        try {
            DSSuiviDemManager manaDSEnvoye = new DSSuiviDemManager();
            manaDSEnvoye.setSession(getSession());
            manaDSEnvoye.setForFromLeo(new Boolean(true));
            manaDSEnvoye.setForAnnee(getAnnee());
            manaDSEnvoye.setForTypeDeclaration(getTypeDeclaration());
            Integer paramDSEnvoye = new Integer(manaDSEnvoye.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_ENVOYE, paramDSEnvoye);

            DSSuiviDemManager manaDSNonRecu = new DSSuiviDemManager();
            manaDSNonRecu.setSession(getSession());
            manaDSNonRecu.setForFromLeo(new Boolean(true));
            manaDSNonRecu.setForAnnee(getAnnee());
            manaDSNonRecu.setForNombreNonRecu(new Boolean(true));
            manaDSNonRecu.setForTypeDeclaration(getTypeDeclaration());
            Integer paramDSNonRecu = new Integer(manaDSNonRecu.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_NONRECU, paramDSNonRecu);

            DSSuiviDemManager manaDSRecu = new DSSuiviDemManager();
            manaDSRecu.setSession(getSession());
            manaDSRecu.setForFromLeo(new Boolean(true));
            manaDSRecu.setForAnnee(getAnnee());
            manaDSRecu.setForNombreRecu(new Boolean(true));
            manaDSRecu.setForTypeDeclaration(getTypeDeclaration());
            manaDSRecu.find();
            Integer paramDSRecu = new Integer(manaDSRecu.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_RECU, paramDSRecu);

            DSSuiviDemManager manaDSOuverte13 = new DSSuiviDemManager();
            manaDSOuverte13.setSession(getSession());
            manaDSOuverte13.setForFromDS(new Boolean(true));
            manaDSOuverte13.setForAnnee(getAnnee());
            manaDSOuverte13.setForEtatDeclaration(DSDeclarationViewBean.CS_OUVERT);
            manaDSOuverte13.setForTypeDeclaration(getTypeDeclaration());
            manaDSOuverte13.setForGenreDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            Integer paramDSOuverte13 = new Integer(manaDSOuverte13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_OUVERTE_13, paramDSOuverte13);

            DSSuiviDemManager manaDSAFacturer13 = new DSSuiviDemManager();
            manaDSAFacturer13.setSession(getSession());
            manaDSAFacturer13.setForFromDS(new Boolean(true));
            manaDSAFacturer13.setForAnnee(getAnnee());
            manaDSAFacturer13.setForEtatDeclaration(DSDeclarationViewBean.CS_AFACTURER);
            manaDSAFacturer13.setForTypeDeclaration(getTypeDeclaration());
            manaDSAFacturer13.setForGenreDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            Integer paramDSAFacturer13 = new Integer(manaDSAFacturer13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_AFACTURER_13, paramDSAFacturer13);

            DSSuiviDemManager manaDSComptabilise13 = new DSSuiviDemManager();
            manaDSComptabilise13.setSession(getSession());
            manaDSComptabilise13.setForFromDS(new Boolean(true));
            manaDSComptabilise13.setForAnnee(getAnnee());
            manaDSComptabilise13.setForEtatDeclaration(DSDeclarationViewBean.CS_COMPTABILISE);
            manaDSComptabilise13.setForTypeDeclaration(getTypeDeclaration());
            manaDSComptabilise13.setForGenreDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            manaDSComptabilise13.find();
            Integer paramDSComptabilise13 = new Integer(manaDSComptabilise13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_COMPTA_13, paramDSComptabilise13);

            DSSuiviDemManager manaDSOuverte14 = new DSSuiviDemManager();
            manaDSOuverte14.setSession(getSession());
            manaDSOuverte14.setForFromDS(new Boolean(true));
            manaDSOuverte14.setForAnnee(getAnnee());
            manaDSOuverte14.setForEtatDeclaration(DSDeclarationViewBean.CS_OUVERT);
            manaDSOuverte14.setForTypeDeclaration(getTypeDeclaration());
            manaDSOuverte14.setForGenreDeclaration(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE);
            Integer paramDSOuverte14 = new Integer(manaDSOuverte14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_OUVERTE_14, paramDSOuverte14);

            DSSuiviDemManager manaDSAFacturer14 = new DSSuiviDemManager();
            manaDSAFacturer14.setSession(getSession());
            manaDSAFacturer14.setForFromDS(new Boolean(true));
            manaDSAFacturer14.setForAnnee(getAnnee());
            manaDSAFacturer14.setForEtatDeclaration(DSDeclarationViewBean.CS_AFACTURER);
            manaDSAFacturer14.setForTypeDeclaration(getTypeDeclaration());
            manaDSAFacturer14.setForGenreDeclaration(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE);
            Integer paramDSAFacturer14 = new Integer(manaDSAFacturer14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_AFACTURER_14, paramDSAFacturer14);

            DSSuiviDemManager manaDSComptabilise14 = new DSSuiviDemManager();
            manaDSComptabilise14.setSession(getSession());
            manaDSComptabilise14.setForFromDS(new Boolean(true));
            manaDSComptabilise14.setForAnnee(getAnnee());
            manaDSComptabilise14.setForEtatDeclaration(DSDeclarationViewBean.CS_COMPTABILISE);
            manaDSComptabilise14.setForTypeDeclaration(getTypeDeclaration());
            manaDSComptabilise14.setForGenreDeclaration(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE);
            Integer paramDSComptabilise14 = new Integer(manaDSComptabilise14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_DS_COMPTA_14, paramDSComptabilise14);

            DSSuiviDemManager manaReleveOuverte13 = new DSSuiviDemManager();
            manaReleveOuverte13.setSession(getSession());
            manaReleveOuverte13.setForFromReleve(new Boolean(true));
            manaReleveOuverte13.setForAnnee(getAnnee());
            manaReleveOuverte13.setForEtatReleve(CodeSystem.ETATS_RELEVE_SAISIE);
            manaReleveOuverte13.setForTypeDeclaration(getTypeDeclaration());
            manaReleveOuverte13.setForGenreReleve(CodeSystem.TYPE_RELEVE_DECOMP_FINAL);
            Integer paramReleveOuverte13 = new Integer(manaReleveOuverte13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_OUVERTE_13, paramReleveOuverte13);

            DSSuiviDemManager manaReleveAFacturer13 = new DSSuiviDemManager();
            manaReleveAFacturer13.setSession(getSession());
            manaReleveAFacturer13.setForFromReleve(new Boolean(true));
            manaReleveAFacturer13.setForAnnee(getAnnee());
            manaReleveAFacturer13.setForEtatReleve(CodeSystem.ETATS_RELEVE_FACTURER);
            manaReleveAFacturer13.setForTypeDeclaration(getTypeDeclaration());
            manaReleveAFacturer13.setForGenreReleve(CodeSystem.TYPE_RELEVE_DECOMP_FINAL);
            Integer paramReleveAFacturer13 = new Integer(manaReleveAFacturer13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_AFACTURER_13, paramReleveAFacturer13);

            DSSuiviDemManager manaReleveComptabilise13 = new DSSuiviDemManager();
            manaReleveComptabilise13.setSession(getSession());
            manaReleveComptabilise13.setForFromReleve(new Boolean(true));
            manaReleveComptabilise13.setForAnnee(getAnnee());
            manaReleveComptabilise13.setForEtatReleve(CodeSystem.ETATS_RELEVE_COMPTABILISER);
            manaReleveComptabilise13.setForTypeDeclaration(getTypeDeclaration());
            manaReleveComptabilise13.setForGenreReleve(CodeSystem.TYPE_RELEVE_DECOMP_FINAL);
            Integer paramReleveComptabilise13 = new Integer(manaReleveComptabilise13.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_COMPTA_13, paramReleveComptabilise13);

            DSSuiviDemManager manaReleveOuverte14 = new DSSuiviDemManager();
            manaReleveOuverte14.setSession(getSession());
            manaReleveOuverte14.setForFromReleve(new Boolean(true));
            manaReleveOuverte14.setForAnnee(getAnnee());
            manaReleveOuverte14.setForEtatReleve(CodeSystem.ETATS_RELEVE_SAISIE);
            manaReleveOuverte14.setForTypeDeclaration(getTypeDeclaration());
            manaReleveOuverte14.setForGenreReleve(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE);
            Integer paramReleveOuverte14 = new Integer(manaReleveOuverte14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_OUVERTE_14, paramReleveOuverte14);

            DSSuiviDemManager manaReleveAFacturer14 = new DSSuiviDemManager();
            manaReleveAFacturer14.setSession(getSession());
            manaReleveAFacturer14.setForFromReleve(new Boolean(true));
            manaReleveAFacturer14.setForAnnee(getAnnee());
            manaReleveAFacturer14.setForEtatReleve(CodeSystem.ETATS_RELEVE_FACTURER);
            manaReleveAFacturer14.setForTypeDeclaration(getTypeDeclaration());
            manaReleveAFacturer14.setForGenreReleve(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE);
            Integer paramReleveAFacturer14 = new Integer(manaReleveAFacturer14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_AFACTURER_14, paramReleveAFacturer14);

            DSSuiviDemManager manaReleveComptabilise14 = new DSSuiviDemManager();
            manaReleveComptabilise14.setSession(getSession());
            manaReleveComptabilise14.setForFromReleve(new Boolean(true));
            manaReleveComptabilise14.setForAnnee(getAnnee());
            manaReleveComptabilise14.setForEtatReleve(CodeSystem.ETATS_RELEVE_COMPTABILISER);
            manaReleveComptabilise14.setForTypeDeclaration(getTypeDeclaration());
            manaReleveComptabilise14.setForGenreReleve(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE);
            Integer paramReleveComptabilise14 = new Integer(manaReleveComptabilise14.getCount());
            super.setParametres(DSSuiviDem_Param.P_NBR_RELEVE_COMPTA_14, paramReleveComptabilise14);

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getApplication().getLabel("DRACO_SUIVI_ERREUR_NOMBRE", getSession().getIdLangueISO())
                            + ": DSSuiviDem_Doc", FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

    }

    /**
     * Retourne la décision ou null en cas d'exception Insérez la description de la méthode ici. Date de création :
     * (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // super.setSendMailOnError(true);
        super.setDocumentTitle(getSession().getLabel("DRACO_SUIVI_TITRE"));
        getDocumentInfo().setDocumentTypeNumber(DSSuiviDem_Doc.NUM_REF_INFOROM_SUIVI_DEM);

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
        super.setTemplateFile(DSSuiviDem_Doc.TEMPLATE_FILENAME);
        start = false;
        _letterBody();

    }

    public String getAnnee() {
        return annee;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
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

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }
}
