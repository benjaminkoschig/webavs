/*
 * Créé le 15 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.JOComplementJournal;
import globaz.journalisation.db.journalisation.access.JOComplementJournalManager;
import globaz.journalisation.db.journalisation.access.JOInitFormattage;
import globaz.journalisation.db.journalisation.access.JOValeurFormattage;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.util.LEUtil;
import globaz.lupus.db.handler.LUJournalDefaulthandler;
import globaz.lupus.db.journalisation.LUGroupeJournalViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEnvoiViewBean extends LUJournalViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String PARAM_RACINE_TYPE = "typeProv";
    public static String PARAM_RACINE_TYPE_INTER = "typeInterProv";
    public static String PARAM_RACINE_VAL_INTER = "valInterProv";
    public static String PARAM_RACINE_VALEUR = "valProv";

    public static String SRC_GO_BACK = "goBack";
    public static String SRC_typeInterProv1 = LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER + "1";
    public static String SRC_typeInterProv2 = LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER + "2";
    public static String SRC_typeInterProv3 = LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER + "3";

    public static String SRC_typeInterProv4 = LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER + "4";
    public static String SRC_typeProv1 = LEEnvoiViewBean.PARAM_RACINE_TYPE + "1";
    public static String SRC_typeProv2 = LEEnvoiViewBean.PARAM_RACINE_TYPE + "2";
    public static String SRC_typeProv3 = LEEnvoiViewBean.PARAM_RACINE_TYPE + "3";

    public static String SRC_typeProv4 = LEEnvoiViewBean.PARAM_RACINE_TYPE + "4";
    public static String SRC_valInterProv1 = LEEnvoiViewBean.PARAM_RACINE_VAL_INTER + "1";
    public static String SRC_valInterProv2 = LEEnvoiViewBean.PARAM_RACINE_VAL_INTER + "2";
    public static String SRC_valInterProv3 = LEEnvoiViewBean.PARAM_RACINE_VAL_INTER + "3";

    public static String SRC_valInterProv4 = LEEnvoiViewBean.PARAM_RACINE_VAL_INTER + "4";
    public static String SRC_valProv1 = LEEnvoiViewBean.PARAM_RACINE_VALEUR + "1";
    public static String SRC_valProv2 = LEEnvoiViewBean.PARAM_RACINE_VALEUR + "2";
    public static String SRC_valProv3 = LEEnvoiViewBean.PARAM_RACINE_VALEUR + "3";

    public static String SRC_valProv4 = LEEnvoiViewBean.PARAM_RACINE_VALEUR + "4";

    public static final HashSet getExceptFormule() {
        HashSet h = new HashSet();
        h.add(ILEConstantes.CS_DEF_FORMULE_DENONCIATION_LAA);
        h.add(ILEConstantes.CS_DEF_FORMULE_RAPPEL_LAA);
        h.add(ILEConstantes.CS_DEF_FORMULE_RECEPTION);
        h.add(ILEConstantes.CS_DEF_FORMULE_SOMMATION_LAA);
        return h;
    }

    private TITiers _tiers;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        LUJournalDefaulthandler jHandler = new LUJournalDefaulthandler();
        if (isJournalInitial()) {
            // si c'est le journal initial, on doit supprimer le groupe journal
            LUGroupeJournalViewBean groupeJ = jHandler
                    .getGroupeJournal(getIdGroupeJournal(), getSession(), transaction);
            groupeJ.delete(transaction);
            // et tous les journaux dans ce groupe
            LEEnvoiListViewBean listeJ = new LEEnvoiListViewBean();
            listeJ.setSession(getSession());
            listeJ.setForIdGroupeJournal(getIdGroupeJournal());
            listeJ.wantComplementJournal(false);
            listeJ.find(transaction);
            for (int i = 0; i < listeJ.size(); i++) {
                LEEnvoiViewBean envoi = (LEEnvoiViewBean) listeJ.getEntity(i);
                envoi.wantCallMethodAfter(false);
                envoi.retrieve(transaction);
                envoi.delete(transaction);
            }
        } else {
            // Mettre à jour les liens du journal précédent et du premier
            // journal et mettre é jour la date de rappel
            // journal précédent
            LUJournalViewBean jPrec = jHandler.getJournal(getIdPrecedent(), getSession(), transaction);
            // on a de toute façon pas affaire au journal initial -->
            // getIdInital > 0
            jPrec.wantCallMethodAfter(false);
            jPrec.setIdSuivant("0");
            jPrec.update(transaction);
            // mise à jour de la date de rappel
            jHandler.updateGroupeJournal(getDateRappel(), "", getSession(), transaction, jPrec);
            // et on supprime tous les journaux suivants
            LEEnvoiListViewBean listeEnvoi = new LEEnvoiListViewBean();
            listeEnvoi.wantComplementJournal(false);
            listeEnvoi.wantGroupeJournal(false);
            listeEnvoi.setSession(getSession());
            listeEnvoi.setForIdGroupeJournal(getIdGroupeJournal());
            listeEnvoi.setFromIdJournal(getIdJournalisation());
            listeEnvoi.setOrderby(IJOCommonJournalisationDefTable.IDJOURNALISATION);
            listeEnvoi.find(transaction);
            for (int i = 0; i < listeEnvoi.size(); i++) {
                LEEnvoiViewBean envoi = (LEEnvoiViewBean) listeEnvoi.getEntity(i);
                envoi.retrieve(transaction);
                envoi.wantCallMethodAfter(false);
                envoi.delete(transaction);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        super._beforeDelete(transaction);
        // on efface les champs utilisateurs où sont stockés les params de la
        // formule
        LEChampUtilisateurListViewBean manager = new LEChampUtilisateurListViewBean();
        manager.setSession(getSession());
        manager.setForIdJournalisation(getIdJournalisation());
        manager.find(transaction);
        for (int ind = 0; ind < manager.getSize(); ind++) {
            LEChampUtilisateurViewBean champUt = (LEChampUtilisateurViewBean) manager.get(ind);
            champUt.delete(transaction);
        }
    }

    public String getAdresseTiers() throws Exception {
        try {
            if (getTiers() != null) {

                if (getTiers().getRue() != null) {
                    return getTiers().getRue() == null ? "" : getTiers().getRue();
                }

                else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            return "";
        }
    }

    @Override
    public String getDate() {
        return super.getDate();
    }

    public String getIdEnvoi() {
        return getIdJournalisation();
    }

    private String getInfoProvenance(String csProvenance) throws Exception {
        if (getRefProvList() == null) {
            // load la liste des provenance
            LUReferenceProvenanceListViewBean refProvList = new LUReferenceProvenanceListViewBean();
            refProvList.setSession(getSession());
            refProvList.setForIdJournalisation(getIdJournalisation());
            refProvList.find();
            setRefProvList(refProvList);
        }
        for (int i = 0; i < getRefProvList().size(); i++) {
            LUReferenceProvenanceViewBean crt = (LUReferenceProvenanceViewBean) getRefProvList().getEntity(i);
            if (csProvenance.equals(crt.getTypeReferenceProvenance())) {
                return crt.getIdCleReferenceProvenance();
            }
        }
        // aucune provenance trouvée pour ce journal donc exception
        return "";
    }

    @Override
    public String getLibelleAffichage() {
        StringBuffer libelleLeo = new StringBuffer(super.getLibelleAffichage());
        // si il s'agit d'une journalisation initiale, peut-être ajouter (en fct
        // du param) la provenance
        // parser la liste de provenance et si match ajout dans le libelle
        if (isJournalInitial() && isLoadedFromManager()) {
            try {
                LEUtil util = new LEUtil();
                ArrayList listeCodeProv = util.getCsCodeProvListe(getSession());

                // On parse tous les codes provenances et on regarde s'il existe
                // une correspondace dans le paramétrage
                Iterator listeFormat = getListeFormat();

                while (listeFormat.hasNext()) {
                    JOValeurFormattage crt = (JOValeurFormattage) listeFormat.next();
                    if (listeCodeProv.contains(crt.getCsPere())) {
                        String val = getInfoProvenance(crt.getCsPere());
                        if (!JadeStringUtil.isEmpty(val)) {
                            libelleLeo.append(val);
                        }
                        if (!JadeStringUtil.isEmpty(crt.getAlpha())) {
                            libelleLeo.append(crt.getAlpha());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return libelleLeo.toString();
    }

    private Iterator getListeFormat() throws Exception {
        JOComplementJournalManager complementJournalListe = new JOComplementJournalManager();
        complementJournalListe.setSession(getSession());
        complementJournalListe.setForIdJournalisation(getIdJournalisation());
        complementJournalListe.setForCsTypeCodeSysteme(JOConstantes.GR_CS_JO_FMT_ID);
        complementJournalListe.find();
        String valeurFmtListe = new String();
        if (complementJournalListe.size() > 0) {
            valeurFmtListe = ((JOComplementJournal) complementJournalListe.getFirstEntity()).getValeurCodeSysteme();
            return JOInitFormattage.getInstance(getSession()).getValues(valeurFmtListe);
        } else {
            return null;
        }

    }

    public String getLocaliteTiers() throws Exception {
        return getTiers().getLocalite() == null ? "" : getTiers().getLocalite();
    }

    public String getNomTiers() throws Exception {
        return getTiers().getNomPrenom() == null ? "" : getTiers().getNomPrenom();
    }

    public TITiers getTiers() throws Exception {
        if (_tiers == null) {
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(getIdDestinataire());
            _tiers.retrieve();
        }
        return _tiers;
    }

    public void setIdEnvoi(String idEnvoi) {
        setIdJournalisation(idEnvoi);
    }

}
