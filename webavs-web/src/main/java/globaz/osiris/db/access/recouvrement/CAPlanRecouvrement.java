package globaz.osiris.db.access.recouvrement;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAPaiementBVR;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.utils.CASursisPaiement;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Représente une entité de type PlanRecouvrement.
 * 
 * @author Arnaud Dostes, 29-mar-2005
 */
public class CAPlanRecouvrement extends CABEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // OSIPLRETA : Plan de recouvrement état
    public final static String CS_ACTIF = "226001";
    public final static String CS_ANNULE = "226004";
    public static final String CS_AVANCE_APG = "223006";
    public static final String CS_AVANCE_IJAI = "223008";
    public static final String CS_AVANCE_RENTES = "223007";
    public static final String CS_AVANCE_PTRA = "223009";
    // OSIPLRMOD : Plan de recouvrement mode
    public final static String CS_BVR = "223001";
    public final static String CS_COMPENSATION = "223004";
    public final static String CS_DIRECT = "223002";
    public final static String CS_ECHEANCE_2_MOIS_SUR_3 = "225011";

    public final static String CS_ECHEANCE_ANNUELLE = "225007";

    public final static String CS_ECHEANCE_BIMESTRIELLE = "225010";
    public final static String CS_ECHEANCE_FACTURATION_EXTERNE = "225003";
    public final static String CS_ECHEANCE_FACTURATION_INTERNE = "225004";
    public final static String CS_ECHEANCE_FACTURATION_PERIODIQUE = "225002";
    public final static String CS_ECHEANCE_HEBDOMADAIRE = "225008";
    // OSIPLRECH : Plan de recouvrement échéance
    public final static String CS_ECHEANCE_MANUELLE = "225001";
    public final static String CS_ECHEANCE_MENSUELLE = "225005";
    public final static String CS_ECHEANCE_QUINZAINE = "225009";
    public final static String CS_ECHEANCE_SEMESTRIELLE = "225012";
    public final static String CS_ECHEANCE_TRIMESTRIELLE = "225006";
    public final static String CS_INACTIF = "226002";
    public final static String CS_RECOUVREMENT = "223005";
    public final static String CS_SOLDE = "226003";
    // OSIPLRVEN : Plan de recouvrement mode de ventilation
    public final static String CS_VEN_VANC = "224001";
    public final static String CS_VEN_VPRIO = "224004";
    public final static String CS_VEN_VPRO = "224003";
    public final static String CS_VEN_VREC = "224002";

    public final static String CS_VERSEMENT = "223003";
    public static final String FIELD_ACOMPTE = "ACOMPTE";
    public static final String FIELD_COLLABORATEUR = "COLLABORATEUR";
    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_DATEECHEANCE = "DATEECHEANCE";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDETAT = "IDETAT";
    public static final String FIELD_IDMODERECOUVREMENT = "IDMODERECOUVREMENT";
    public static final String FIELD_IDMODEVENTILATION = "IDMODEVENTILATION";
    public static final String FIELD_IDPLANRECOUVREMENT = "IDPLANRECOUVREMENT";
    // public static final String FIELD_IDADRESSEPAIEMENT = "IDADRESSEPAIEMENT";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDTYPEECHEANCE = "IDTYPEECHEANCE";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_PARTPENALE = "PARTPENALE";
    public static final String FIELD_PLAFOND = "PLAFOND";
    public static final String FIELD_POURCENTAGE = "POURCENTAGE";

    public static final String FIELD_PREMIERACOMPTE = "PREMIERACOMPTE";
    protected static final String LABEL_ACOMPTE_NEGATIF = "ACOMPTE_NEGATIF";
    protected static final String LABEL_COMPTEANNEXE_OBLIGATOIRE = "COMPTEANNEXE_OBLIGATOIRE";
    protected static final String LABEL_DATE_ECHEANCE_INCORRECT = "DATE_ECHEANCE_INCORRECT";
    protected static final String LABEL_DATE_ECHEANCE_MUST_BE_EMPTY = "DATE_ECHEANCE_MUST_BE_EMPTY";
    protected static final String LABEL_DATE_OCTROI_INCORRECT = "DATE_OCTROI_INCORRECT";
    protected static final String LABEL_MISSING_ACOMPTE = "MISSING_ACOMPTE";
    protected static final String LABEL_MISSING_DATE_ECHEANCE = "MISSING_DATE_ECHEANCE";
    protected static final String LABEL_MISSING_DATE_OCTROI = "MISSING_DATE_OCTROI";
    protected static final String LABEL_MISSING_LIBELLE = "MISSING_LIBELLE";
    public static final String TABLE_CAPLARP = "CAPLARP";

    /**
     * @param session
     * @param memoryLog
     * @param plan
     * @param paiement
     * @return
     */
    private static CAPaiementBVR checkActif(BSession session, FWMemoryLog memoryLog, CAPlanRecouvrement plan,
            CAPaiementBVR paiement) {
        if (CAPlanRecouvrement.CS_INACTIF.equals(plan.getIdEtat())) {
            // le montant du paiement est différent du montant de l'échéance
            paiement.setEtat(APIOperation.ETAT_ERREUR);
            paiement.setMemoryLog(memoryLog);
            paiement.getMemoryLog().logMessage(session.getLabel("PLAN_INACTIF"), FWMessage.ERREUR,
                    plan.getClass().getName());
        }
        return paiement;
    }

    /**
     * Cloture une écheance pour cette date
     * 
     * @param idPlanRecouvrement
     *            (l'id du plan)
     * @param dateExigibilite
     *            la date d'exigibilite (DD.MM.YYYY)
     */
    public static void serviceClotureEcheance(BSession session, String idPlanRecouvrement, String dateExigibilite)
            throws Exception {
        CAEcheancePlanManager echeances = new CAEcheancePlanManager();
        echeances.setSession(session);
        echeances.setForIdPlanRecouvrement(idPlanRecouvrement);
        echeances.setForDateExigibilite(dateExigibilite);
        echeances.find();
        if (echeances.size() != 1) {
            // pas trouvée
            throw new Exception(session.getLabel("ECHEANCE_NOT_FOUND"));
        } else {
            CAEcheancePlan echeance = (CAEcheancePlan) echeances.getFirstEntity();
            if (!JadeStringUtil.isEmpty(echeance.getDateEffective())) {
                // déjà cloturée
                throw new Exception(session.getLabel("ECHEANCE_CLOSED"));
            } else {
                // on cloture
                echeance.setDateEffective(JACalendar.todayJJsMMsAAAA());
                echeance.update();
            }
        }
    }

    /**
     * @param session
     * @param idPlanRecouvrement
     * @param montant
     * @return
     * @throws Exception
     */
    public static CAPaiementBVR[] serviceComputePaiements(BSession session, String idPlanRecouvrement,
            BigDecimal montantPaiementTotal, FWMemoryLog memoryLog) throws Exception {
        // TODO : déduire les sections à couvrir, instancier un array de
        // CAPAiementBVR contenant l'idSection, l'IDCompteAnnexe,
        // l'idEcheancePlan et le montant pour cette section à attribuer
        //
        CAPlanRecouvrement plan = new CAPlanRecouvrement();
        plan.setSession(session);
        plan.setIdPlanRecouvrement(idPlanRecouvrement);
        plan.retrieve();
        if (plan.isNew()) { // aucun plan trouvé
            throw new Exception(FWMessageFormat.format(session.getLabel("PLAN_NULL"), idPlanRecouvrement));
        }

        // Récupération de la première échéance à couvrir
        CASection[] sections = null;
        CAEcheancePlan echeance = null;
        try {
            echeance = plan.fetchEcheance();
            sections = CAPlanRecouvrement.serviceSectionsCouvrir(session, idPlanRecouvrement);
        } catch (Exception e) {
            // Ignorer l'erreur. Si aucune echéance active n'existe alors créer
            // un paiement qui contient le montant complet payé par l'affilié.
            // On ignore l'erreur car l'on ne veut pas interrompre le
            // traitement. De ce fait l'utilisateur devra quittancer le
            // paiement.
        }

        CAPaiementBVR[] paiements = null;

        if ((sections == null) || (sections.length == 0)) {
            // => 1 paiement comprenant le montant total sans id section

            paiements = new CAPaiementBVR[1];
            paiements[0] = new CAPaiementBVR();
            paiements[0].setSession(session);
            paiements[0].setMemoryLog(memoryLog);
            paiements[0].setIdCompteAnnexe(plan.getIdCompteAnnexe());
            if (echeance != null) {
                paiements[0].setIdEcheancePlan(echeance.getIdEcheancePlan());
            } else {
                paiements[0].getMemoryLog().logMessage(
                        session.getLabel("PLAN_SOLDE") + " Plan N° " + plan.getIdPlanRecouvrement(), FWMessage.ERREUR,
                        plan.getClass().getName());
            }
            paiements[0].setMontant(montantPaiementTotal.toString());
            paiements[0] = CAPlanRecouvrement.checkActif(session, memoryLog, plan, paiements[0]);
            paiements[0].setEtat(APIOperation.ETAT_ERREUR);
            paiements[0].getMemoryLog().logMessage(session.getLabel("5125"), FWMessage.ERREUR,
                    plan.getClass().getName());
            return paiements;
        }

        // ///////////// pro rata
        if (CAPlanRecouvrement.CS_VEN_VPRO.equals(plan.getIdModeVentilation())) {
            // autant de paiements que de sections
            paiements = new CAPaiementBVR[sections.length];
            // CAPaiementBVR bvr
            BigDecimal montantProRata = montantPaiementTotal.divide(new BigDecimal(paiements.length),
                    BigDecimal.ROUND_HALF_EVEN);
            // on répartit sur les sections
            for (int i = 0; i < sections.length; i++) {
                paiements[i] = new CAPaiementBVR();
                paiements[i].setSession(session);
                paiements[i].setIdSection(sections[i].getIdSection());
                paiements[i].setIdCompteAnnexe(plan.getIdCompteAnnexe());
                paiements[i].setIdEcheancePlan(echeance.getIdEcheancePlan());
                paiements[i].setMontant("" + montantProRata);
                paiements[i] = CAPlanRecouvrement.checkActif(session, memoryLog, plan, paiements[i]);
                if (montantPaiementTotal.compareTo(new BigDecimal(echeance.getMontant())) != 0) {
                    // le montant du paiement est différent du montant de
                    // l'échéance
                    // paiements[i].setEtat(APIOperation.ETAT_ERREUR);
                    paiements[i].setMemoryLog(memoryLog);
                    paiements[i].getMemoryLog().logMessage(session.getLabel("BVR_SOLDE_ECHEANCE"),
                            FWMessage.INFORMATION, plan.getClass().getName());
                }
            }
        } else { // ////////// PRIORITE,ANCIENNE OU RECENTE
            ArrayList tmpPaiements = new ArrayList();
            // anciennes d'abord
            // montant restant à ventiler
            BigDecimal montantRestant = montantPaiementTotal;
            // on balaye les sections non soldées qui sont déjà triées
            for (int i = 0; (i < sections.length) && (montantRestant.compareTo(new BigDecimal("0")) > 0); i++) {
                FWMemoryLog memoryLog2 = new FWMemoryLog();
                memoryLog2.setSession(session);
                memoryLog2.logMessage(memoryLog);
                CAPaiementBVR paiement = new CAPaiementBVR();
                paiement.setSession(session);
                paiement.setIdSection(sections[i].getIdSection());
                paiement.setIdCompteAnnexe(plan.getIdCompteAnnexe());
                paiement.setIdEcheancePlan(echeance.getIdEcheancePlan());
                // paiement.setMontant(echeance.getMontant());
                // paiement.setMontant("" + montantRestant);
                BigDecimal montantSection = new BigDecimal(sections[i].getSolde());
                if (montantRestant == montantSection) {
                    paiement.setMontant(String.valueOf(montantRestant));
                    montantRestant = montantRestant.subtract(montantSection);
                } else if (montantRestant.compareTo(montantSection) < 0) {
                    paiement.setMontant("" + montantRestant);
                    montantRestant = new BigDecimal("0");
                } else { // montantRestant > montantSection
                    // dda (source Hotline Numéro 9IL50514) : Le montant payé
                    // par l'affilié est égal à son plan de paiement mais le
                    // solde de la section est inférieur à ce montant
                    if ((montantRestant.compareTo(montantSection) > 0)
                            && ((i == sections.length - 1) || (montantSection.compareTo(new BigDecimal("0")) <= 0))) {
                        paiement.setMontant(String.valueOf(montantRestant));
                        montantRestant = new BigDecimal("0");
                    } else {
                        paiement.setMontant("" + montantSection);
                        montantRestant = montantRestant.subtract(montantSection);
                    }
                }
                paiement = CAPlanRecouvrement.checkActif(session, memoryLog2, plan, paiement);
                tmpPaiements.add(i, paiement);
                if ((montantPaiementTotal.compareTo(new BigDecimal(echeance.getMontant())) != 0)
                        || ((montantRestant.compareTo(new BigDecimal("0.0")) != 0) && (i == sections.length - 1))) {
                    // le montant du paiement est différent du montant de
                    // l'échéance
                    // paiement.setEtat(APIOperation.ETAT_ERREUR);
                    paiement.setMemoryLog(memoryLog2);
                    paiement.getMemoryLog().logMessage(session.getLabel("BVR_SOLDE_ECHEANCE"), FWMessage.INFORMATION,
                            plan.getClass().getName());
                }
            }
            paiements = new CAPaiementBVR[tmpPaiements.size()];
            tmpPaiements.toArray(paiements);
        }
        return paiements;
    }

    /**
     * Réactive une échéance
     * 
     * @param session
     * @param idPlanRecouvrement
     * @param dateExigibilite
     *            DD.MM.YYYY
     * @throws Exception
     */
    public static void serviceReactiverEcheance(BSession session, String idPlanRecouvrement, String dateExigibilite)
            throws Exception {
        CAEcheancePlanManager echeances = new CAEcheancePlanManager();
        echeances.setSession(session);
        echeances.setForIdPlanRecouvrement(idPlanRecouvrement);
        echeances.setForDateExigibilite(dateExigibilite);
        echeances.setFromDateEffective("01.01.1900");
        echeances.find();
        if (echeances.size() == 1) {
            CAEcheancePlan echeance = (CAEcheancePlan) echeances.getFirstEntity();
            echeance.setDateEffective("0");
            echeance.update();
        }
    }

    /**
     * Renvoit les sections ouvertes du plan à couvrir en fonction du mode de ventilation et du montant
     * <ul>
     * <li>Mode de ventilation ANCIEN : les sections sont triées par date d'ancienneté
     * <li>Mode de ventilation PRIORITAIRE : les sections sont triées par ordre de priorité
     * <li>Mode de ventilation PRORATA : les sections sont toutes renvoyées, le montant sera distribuée au prorata
     * <li>Mode de ventilation RECENT : les factures sont triées par ordre récent
     * </ul>
     * 
     * @param montant
     * @return un ArrayList des sections à recouvrir dans l'ordre
     */
    public static CASection[] serviceSectionsCouvrir(BSession session, String idPlanRecouvrement) throws Exception {
        CAPlanRecouvrement plan = new CAPlanRecouvrement();
        plan.setSession(session);
        plan.setIdPlanRecouvrement(idPlanRecouvrement);
        plan.retrieve();
        // les couvertures
        CACouvertureSectionManager couvertures = plan.fetchSectionsCouvertes();
        if (couvertures.size() < 1) {
            throw new Exception(session.getLabel("PLAN_VIDE"));
        }
        // les sections
        // CASection sections[] = new CASection[couvertures.size()];
        ArrayList sections = new ArrayList();
        for (int i = 0; i < couvertures.size(); i++) {
            //
            CACouvertureSection couverture = (CACouvertureSection) couvertures.getEntity(i);
            //
            CASection tmpSection = new CASection();
            tmpSection.setSession(session);
            tmpSection.setIdSection(couverture.getIdSection());
            tmpSection.retrieve();
            // à condition que la section soit pas soldée
            if (Float.parseFloat(tmpSection.getSolde()) != 0f) {
                sections.add(tmpSection);
            }
        }

        if (CAPlanRecouvrement.CS_VEN_VANC.equals(plan.getIdModeVentilation())) {
            // les sections les plus anciennes d'abord = BUBBLE SORT !
            sections = CAPlanRecouvrement.triSectionPourVentilationAncien(session, sections);
        } else if (CAPlanRecouvrement.CS_VEN_VPRIO.equals(plan.getIdModeVentilation())) {
            // sections prioritaire
            // les sections sont déja par numéro d'ordre
            CASection sectionArray[] = new CASection[sections.size()];
            sections.toArray(sectionArray);
            return sectionArray;
        } else if (CAPlanRecouvrement.CS_VEN_VPRO.equals(plan.getIdModeVentilation())) {
            // sections prorata
            // on renvoit toutes les sections
            CASection sectionArray[] = new CASection[sections.size()];
            sections.toArray(sectionArray);
            return sectionArray;
        } else if (CAPlanRecouvrement.CS_VEN_VREC.equals(plan.getIdModeVentilation())) {
            // les sections les plus récentes d'abord = BUBBLE SORT !
            sections = CAPlanRecouvrement.triSectionPourVentilationRecent(session, sections);
        }
        CASection sectionArray[] = new CASection[sections.size()];
        sections.toArray(sectionArray);
        return sectionArray;
    }

    /**
     * @param session
     * @param sections
     * @return
     * @throws Exception
     */
    private static ArrayList triSectionPourVentilationAncien(BSession session, ArrayList sections) throws Exception {
        ArrayList sections2 = new ArrayList();
        ArrayList dateTraite = new ArrayList();
        Map orderer = new TreeMap();
        Map liaison = new HashMap();
        for (int i = 0; i < sections.size(); i++) {
            CASection sectionA = (CASection) sections.get(i);
            if (JadeStringUtil.isBlankOrZero(sectionA.getDateEcheance())) {
                throw new Exception(session.getLabel("ERREUR_SECTION_DATE_ECHEANCE") + " (" + sectionA.getIdSection()
                        + ") " + sectionA.getIdExterne());
            }

            int dateSectionA = Integer.parseInt(JACalendar.format(sectionA.getDateEcheance(),
                    JACalendar.FORMAT_YYYYMMDD));
            String idSection = sectionA.getIdSection();

            if (dateTraite.isEmpty() || !dateTraite.contains(String.valueOf(dateSectionA) + sectionA.getIdExterne())) {
                orderer.put(String.valueOf(dateSectionA) + sectionA.getIdExterne(), idSection);
                liaison.put(idSection, String.valueOf(i));
                dateTraite.add(String.valueOf(dateSectionA) + sectionA.getIdExterne());
            } else {
                orderer.put(String.valueOf(dateSectionA + 1) + sectionA.getIdExterne(), idSection);
                liaison.put(idSection, String.valueOf(i));
                dateTraite.add(String.valueOf(dateSectionA + 1));
            }

        }
        for (Iterator it = orderer.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            String idSec = (String) orderer.get(key);

            String i = (String) liaison.get(idSec);
            CASection sec = (CASection) sections.get(Integer.parseInt(i));

            sections2.add(sec);
        }
        return sections2;
    }

    /**
     * @param session
     * @param sections
     * @return
     * @throws Exception
     */
    private static ArrayList triSectionPourVentilationRecent(BSession session, ArrayList sections) throws Exception {
        ArrayList sections2 = CAPlanRecouvrement.triSectionPourVentilationAncien(session, sections);
        ArrayList sections3 = new ArrayList();
        for (int i = sections2.size() - 1; i >= 0; i--) {
            sections3.add(sections2.get(i));
        }
        return sections3;
    }

    private String acompte = "";
    private String collaborateur = "";
    private CACompteAnnexe compteAnnexe = null;
    private String date = JACalendar.today().toStr(".");
    private String dateEcheance = "";
    private String idCompteAnnexe = "";
    private String idEtat = CAPlanRecouvrement.CS_ACTIF;

    private String idModeRecouvrement = CAPlanRecouvrement.CS_BVR;
    private String idModeVentilation = CAPlanRecouvrement.CS_VEN_VANC;
    private String idPlanRecouvrement = "";
    // private String idAdressePaiement = "";
    private String idRubrique = "";
    private String idSection = "";

    private String idTypeEcheance = CAPlanRecouvrement.CS_ECHEANCE_MENSUELLE;

    private String libelle = "";

    private Boolean partPenale = new Boolean(false);

    private String plafond = "";

    private String pourcentage = "";

    private String premierAcompte = "";

    private Vector tempEcheances = new Vector();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (CAPlanRecouvrement.CS_SOLDE.equals(getIdEtat())) {
            CACouvertureSectionManager mgrCouverture = new CACouvertureSectionManager();
            mgrCouverture.setSession(getSession());
            mgrCouverture.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            mgrCouverture.find();
            Iterator it = mgrCouverture.iterator();
            while (it.hasNext()) {
                CACouvertureSection couverture = (CACouvertureSection) it.next();
                CASursisPaiement.terminerBlocageSectionContentieux(transaction, couverture.getSection());
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        // Cette entité n'hérite pas d'une autre entité
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPlanRecouvrement(this._incCounter(transaction, getIdPlanRecouvrement(), _getTableName()));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        CACouvertureSectionManager mgrCouverture = new CACouvertureSectionManager();
        mgrCouverture.setSession(getSession());
        mgrCouverture.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        try {
            mgrCouverture.find(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }
        if (mgrCouverture.size() > 0) {
            _addError(transaction, getSession().getLabel("SUPPR_PLAN_IMPOSSIBLE"));
            return;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAPlanRecouvrement.TABLE_CAPLARP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPlanRecouvrement = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDPLANRECOUVREMENT);
        date = statement.dbReadDateAMJ(CAPlanRecouvrement.FIELD_DATE);
        libelle = statement.dbReadString(CAPlanRecouvrement.FIELD_LIBELLE);
        idModeRecouvrement = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT);
        idModeVentilation = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDMODEVENTILATION);
        idTypeEcheance = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDTYPEECHEANCE);
        dateEcheance = statement.dbReadDateAMJ(CAPlanRecouvrement.FIELD_DATEECHEANCE);
        acompte = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_ACOMPTE, 2);
        premierAcompte = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_PREMIERACOMPTE, 2);
        plafond = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_PLAFOND, 2);
        pourcentage = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_POURCENTAGE, 5);
        idEtat = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDETAT);
        collaborateur = statement.dbReadString(CAPlanRecouvrement.FIELD_COLLABORATEUR);
        idCompteAnnexe = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDCOMPTEANNEXE);
        idSection = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDSECTION);
        // idAdressePaiement = statement.dbReadNumeric(FIELD_IDADRESSEPAIEMENT);
        idRubrique = statement.dbReadNumeric(CAPlanRecouvrement.FIELD_IDRUBRIQUE);
        partPenale = statement.dbReadBoolean(CAPlanRecouvrement.FIELD_PARTPENALE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // date d'octroi obligatoire et valide
        _propertyMandatory(statement.getTransaction(), getDate(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_DATE_OCTROI));
        _checkDate(statement.getTransaction(), getDate(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_OCTROI_INCORRECT));
        // mode de recouvrement
        _propertyMandatory(statement.getTransaction(), getIdModeRecouvrement(),
                getSession().getLabel("MISSING_MODE_RECOUVREMENT"));
        // mode de ventilation
        _propertyMandatory(statement.getTransaction(), getIdModeVentilation(),
                getSession().getLabel("MISSING_MODE_VENTILATION"));
        // collaborateur
        _propertyMandatory(statement.getTransaction(), getCollaborateur(),
                getSession().getLabel("MISSING_COLLABORATEUR"));
        // Acompte
        BigDecimal bdAcompte = JadeStringUtil.isBlank(getAcompte()) ? new BigDecimal(0) : new BigDecimal(
                JANumberFormatter.deQuote(getAcompte()));
        if (CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getIdTypeEcheance())) {
            if (bdAcompte.compareTo(new BigDecimal(0)) != 0) {
                _addError(statement.getTransaction(), getSession().getLabel("BAD_ACOMPTE"));
            }
        } else {
            _propertyMandatory(statement.getTransaction(), getAcompte(),
                    getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_ACOMPTE));
            if (bdAcompte.compareTo(new BigDecimal(0)) <= 0) {
                _addError(statement.getTransaction(), getSession().getLabel(CAPlanRecouvrement.LABEL_ACOMPTE_NEGATIF));
            }
        }
        // 1er acompte
        BigDecimal bdPremierAcompte = JadeStringUtil.isBlank(getPremierAcompte()) ? new BigDecimal(0) : new BigDecimal(
                JANumberFormatter.deQuote(getPremierAcompte()));
        if (CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getIdTypeEcheance())) {
            if (bdPremierAcompte.compareTo(new BigDecimal(0)) != 0) {
                _addError(statement.getTransaction(), getSession().getLabel("BAD_PREMIER_ACOMPTE"));
            }
        } else {
            if (bdPremierAcompte.compareTo(new BigDecimal(0)) < 0) {
                _addError(statement.getTransaction(), getSession().getLabel("PREMIER_ACOMPTE_NEGATIF"));
            }
        }
        // Montant max
        BigDecimal bdMontantMax = JadeStringUtil.isBlank(getPlafond()) ? new BigDecimal(0) : new BigDecimal(
                JANumberFormatter.deQuote(getPlafond()));
        if (CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getIdTypeEcheance())) {
            if (bdMontantMax.compareTo(new BigDecimal(0)) != 0) {
                _addError(statement.getTransaction(), getSession().getLabel("BAD_PLAFOND"));
            }
            // } else {
            // if (bdMontantMax.compareTo(new
            // BigDecimal(JANumberFormatter.deQuote(getCumulSoldeSections()))) >
            // 0) {
            // _addError(statement.getTransaction(),
            // getSession().getLabel("PLAFOND_SUP_SOLDE"));
            // }
        }
        if (JadeStringUtil.isBlank(getPlafond())) {
            setPlafond(getCumulSoldeSections());
        }
        // Echéance
        /*
         * if (isEcheanceFacturation(getIdTypeEcheance()) && !CS_COMPENSATION.equals(getIdModeRecouvrement())) {
         * _addError(statement.getTransaction(), getSession().getLabel("BAD_TYPE_ECHEANCE")); }
         */
        // Date échéance
        if (hasEcheanceAuto()) {
            _propertyMandatory(statement.getTransaction(), getDateEcheance(),
                    getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_DATE_ECHEANCE));
            _checkDate(statement.getTransaction(), getDateEcheance(),
                    getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_ECHEANCE_INCORRECT));
        } else {
            if (!JadeStringUtil.isBlank(getDateEcheance())) {
                _addError(statement.getTransaction(),
                        getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_ECHEANCE_MUST_BE_EMPTY));
            }
        }
        // Compte annexe
        _propertyMandatory(statement.getTransaction(), getIdCompteAnnexe(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_COMPTEANNEXE_OBLIGATOIRE));
        // Adresse de paiement
        // if (CS_VERSEMENT.equals(idModeRecouvrement) ||
        // CS_RECOUVREMENT.equals(idModeRecouvrement)) {
        // _propertyMandatory(statement.getTransaction(),
        // getIdAdressePaiement(),
        // getSession().getLabel("MISSING_ADRESSE_PAIEMENT"));
        // } else {
        // if (!JAUtil.isPropertyEmpty(getIdAdressePaiement())) {
        // _addError(statement.getTransaction(),
        // getSession().getLabel("BAD_ADRESSE_PAIEMENT"));
        // }
        // }
        // vérification que le compte annexe existe
        // vérification que la section existe
        /*
         * if (CS_COMPENSATION.equals(idModeRecouvrement)) { CACompteAnnexe compteAnnexe = new CACompteAnnexe();
         * CASection section = new CASection(); if (!JadeStringUtil.isBlank(getIdSection())) {
         * section.setSession(getSession()); section.setIdSection(getIdSection()); try {
         * section.retrieve(statement.getTransaction()); } catch (Exception e) { _addError(statement.getTransaction(),
         * e.getMessage()); } if (section.isNew()) { _addError(statement.getTransaction(), "section inexistante"); } }
         * if (!JadeStringUtil.isBlank(getIdCompteAnnexe())) { compteAnnexe.setSession(getSession());
         * compteAnnexe.setIdCompteAnnexe(getIdSection()); try { compteAnnexe.retrieve(statement.getTransaction()); }
         * catch (Exception e) { _addError(statement.getTransaction(), e.getMessage()); } if (compteAnnexe.isNew()) {
         * _addError(statement.getTransaction(), "Compte annexe inexistant"); } else { // liaison compte annexe /
         * section if (!section.getIdCompteAnnexe().equals(compteAnnexe.getIdCompteAnnexe ())) {
         * _addError(statement.getTransaction(), "Cette section n'appartient pas à ce compte annexe"); } } } }
         */
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDPLANRECOUVREMENT",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanRecouvrement(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAPlanRecouvrement.FIELD_IDPLANRECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanRecouvrement(), "idPlanRecouvrement"));
        statement.writeField(CAPlanRecouvrement.FIELD_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(CAPlanRecouvrement.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeRecouvrement(), "idModeRecouvrement"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDMODEVENTILATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeVentilation(), "idModeVentilation"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDTYPEECHEANCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeEcheance(), "idTypeEcheance"));
        statement.writeField(CAPlanRecouvrement.FIELD_DATEECHEANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField(CAPlanRecouvrement.FIELD_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getAcompte(), "acompte"));
        statement.writeField(CAPlanRecouvrement.FIELD_PREMIERACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getPremierAcompte(), "premierAcompte"));
        statement.writeField(CAPlanRecouvrement.FIELD_PLAFOND,
                this._dbWriteNumeric(statement.getTransaction(), getPlafond(), "plafond"));
        statement.writeField(CAPlanRecouvrement.FIELD_POURCENTAGE,
                this._dbWriteNumeric(statement.getTransaction(), getPourcentage(), "pourcentage"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDETAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
        statement.writeField(CAPlanRecouvrement.FIELD_COLLABORATEUR,
                this._dbWriteString(statement.getTransaction(), getCollaborateur(), "collaborateur"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        // statement.writeField(FIELD_IDADRESSEPAIEMENT,
        // _dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(),
        // "idAdressePaiement"));
        statement.writeField(CAPlanRecouvrement.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CAPlanRecouvrement.FIELD_PARTPENALE, this._dbWriteBoolean(statement.getTransaction(),
                getPartPenale(), BConstants.DB_TYPE_BOOLEAN_CHAR, "partPenale"));
    }

    /**
     * Renvoit les couvertures triées par numéro d'ordre
     * 
     * @param session
     * @return
     */
    public CAEcheancePlan fetchEcheance() throws Exception {
        CAEcheancePlanManager echeances = new CAEcheancePlanManager();
        echeances.setSession(getSession());
        echeances.setOrder("DATEEXIGIBILITE"); // dans cet ordre
        echeances.setForIdPlanRecouvrement(getIdPlanRecouvrement()); // pour ce
        // plan
        echeances.setForDateEffectiveIsNull(); // non soldée
        echeances.find();
        if (echeances.size() < 1) {
            throw new Exception(getSession().getLabel("AUCUNE_ECHEANCE") + " - " + getIdPlanRecouvrement());
        }
        return (CAEcheancePlan) echeances.getFirstEntity();
    }

    /**
     * Renvoit les couvertures triées par numéro d'ordre
     * 
     * @param session
     * @return
     */
    public CACouvertureSectionManager fetchSectionsCouvertes() throws Exception {
        CACouvertureSectionManager couvertures = new CACouvertureSectionManager();
        couvertures.setSession(getSession());
        couvertures.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        couvertures.setOrder("NUMEROORDRE");
        couvertures.find();
        return couvertures;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getAcompte() {
        return acompte;
    }

    /**
     * @return La valeur courante de la propriété, formatée
     */
    public String getAcompteFormate() {
        return JANumberFormatter.formatNoRound(getAcompte(), 2);
    }

    /**
     * @return Le texte de l'adresse de paiement
     */
    public String getAdressePaiementTiers() {
        String adresse = "";
        try {
            if (getCompteAnnexe() == null) {
                // appel page neuve
                return "";
            }
            adresse = getCompteAnnexe().getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_SURSIS_PAIEMENT, getCompteAnnexe().getIdExterneRole(),
                    JACalendar.today().toStr("."), false);
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = getCompteAnnexe().getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_SURSIS_PAIEMENT, "", JACalendar.today().toStr("."), false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = getCompteAnnexe().getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.today().toStr("."),
                        false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = getCompteAnnexe().getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_FACTURATION, getCompteAnnexe().getIdExterneRole(),
                        JACalendar.today().toStr("."));
            }
            return adresse;
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return "";
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getCollaborateur() {
        return collaborateur;
    }

    /**
     * @return Le compte annexe lié
     */
    public CACompteAnnexe getCompteAnnexe() {
        compteAnnexe = (CACompteAnnexe) retrieveEntityByIdIfNeeded(null, getIdCompteAnnexe(), CACompteAnnexe.class,
                compteAnnexe);
        return compteAnnexe;
    }

    /**
     * @return Le cumul des soldes des sections liées
     */
    public String getCumulSoldeSections() {
        if ("".equals(getIdPlanRecouvrement())) {
            return "";
        }
        BigDecimal solde = new BigDecimal(0);
        if (JadeStringUtil.isDecimalEmpty(getPlafond())) {
            CACouvertureSectionManager manager = new CACouvertureSectionManager();
            manager.setSession(getSession());
            manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            try {
                manager.find();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            for (int i = 0; i < manager.size(); i++) {
                CACouvertureSection couvertureSection = (CACouvertureSection) manager.getEntity(i);
                solde = solde.add(new BigDecimal(JANumberFormatter.deQuote(couvertureSection.getSection().getSolde())));
            }
        } else {
            solde = solde.add(new BigDecimal(JANumberFormatter.deQuote(getPlafond())));
        }
        return solde.toString();
    }

    /**
     * @return Le cumul des soldes des sections liées, formaté
     */
    public String getCumulSoldeSectionsFormate() {
        return JANumberFormatter.formatNoRound(getCumulSoldeSections(), 2);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDate() {
        return date;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * @param codeISOLangue
     *            Le code ISO de la langue demandée
     * @return La description du type d'échéance dans la langue demandée
     */
    public String getDescriptionTypeEcheance(String codeISOLangue) {
        String s = "";
        try {
            // Si le type est fourni, récupérer le code système
            if (!JadeStringUtil.isIntegerEmpty(getIdTypeEcheance())) {
                globaz.globall.parameters.FWParametersSystemCode code = new FWParametersSystemCode();
                code.setISession(getSession());
                // Déterminer la langue
                code.setIdLangue("F");
                if (codeISOLangue.equalsIgnoreCase("de")) {
                    code.setIdLangue("D");
                } else if (codeISOLangue.equalsIgnoreCase("it")) {
                    code.setIdLangue("I");
                }
                code.getCode(getIdTypeEcheance());
                // Le code système existe
                if (!code.isNew()) {
                    s = code.getCodeUtilisateur(code.getIdLangue()).getLibelle();
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // Retourne la valeur
        return s;
    }

    /**
     * @return La valeur courante de la propriété
     */
    // public String getIdAdressePaiement() {
    // return idAdressePaiement;
    // }
    /**
     * @return La valeur courante de la propriété
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEtat() {
        return idEtat;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdModeRecouvrement() {
        return idModeRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdModeVentilation() {
        return idModeVentilation;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdTypeEcheance() {
        return idTypeEcheance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Cette méthode indique si le sursis au paiement concerne une part pénale
     * 
     * @return Boolean partPenale
     */
    public Boolean getPartPenale() {
        return partPenale;
    }

    /**
     * Retourne "on" si la partPenale est à true
     * 
     * @return "on" si la partPenale est à true
     */
    public String getPartPenaleJsp() {
        return partPenale.booleanValue() ? "on" : "";
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getPlafond() {
        return plafond;
    }

    /**
     * @return La valeur courante de la propriété, formatée
     */
    public String getPlafondFormate() {
        return JANumberFormatter.formatNoRound(getPlafond(), 2);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getPourcentage() {
        return pourcentage;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getPremierAcompte() {
        return premierAcompte;
    }

    /**
     * @return La valeur courante de la propriété, formatée
     */
    public String getPremierAcompteFormate() {
        return JANumberFormatter.formatNoRound(getPremierAcompte(), 2);
    }

    /**
     * @return Le solde residuel du plan
     */
    public String getSoldeResiduelPlan() {
        if ("".equals(getIdPlanRecouvrement())) {
            return "";
        }
        BigDecimal solde = new BigDecimal(0);
        if (JadeStringUtil.isDecimalEmpty(getPlafond())) {
            CAEcheancePlanManager manager = new CAEcheancePlanManager();
            manager.setSession(getSession());
            manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            try {
                solde = solde.add(manager.getSum("MONTANT"));
                solde = solde.add(manager.getSum(CAEcheancePlan.FIELD_MONTANTPAYE));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        } else {
            solde = solde.add(new BigDecimal(JANumberFormatter.deQuote(getPlafond())));

            CAEcheancePlanManager manager = new CAEcheancePlanManager();
            manager.setSession(getSession());
            manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            try {
                solde = solde.add(manager.getSum(CAEcheancePlan.FIELD_MONTANTPAYE));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return solde.toString();
    }

    /**
     * @return Le cumul des soldes des sections liées, formaté
     */
    public String getSoldeResiduelPlanFormate() {
        return JANumberFormatter.formatNoRound(getSoldeResiduelPlan(), 2);
    }

    /**
     * @return
     */
    public Vector getTempEcheances() {
        return tempEcheances;
    }

    /**
     * indique si l'échéance est de type annuelle, mensuelle ou trimestrielle
     * 
     * @return true si mensuelle,annuelle ou trim.
     */
    public boolean hasEcheanceAuto() {
        return !CAPlanRecouvrement.CS_ECHEANCE_MANUELLE.equals(getIdTypeEcheance());
    }

    /**
     * @return
     */
    public boolean hasEcheances() {
        CAEcheancePlanManager echeances = new CAEcheancePlanManager();
        echeances.setSession(getSession());
        echeances.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        try {
            echeances.find();
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return true;
        }
        return echeances.size() > 0;
    }

    /**
     * @return
     */
    public boolean isActif() {
        if (CAPlanRecouvrement.CS_INACTIF.equals(getIdEtat())) {
            return false;
        }
        return true;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setAcompte(String string) {
        acompte = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setCollaborateur(String string) {
        collaborateur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateEcheance(String string) {
        dateEcheance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    // public void setIdAdressePaiement(String string) {
    // idAdressePaiement = string;
    // }
    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEtat(String string) {
        idEtat = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdModeRecouvrement(String string) {
        idModeRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdModeVentilation(String string) {
        idModeVentilation = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdTypeEcheance(String string) {
        idTypeEcheance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    /**
     * Cette méthode permet de setter la part pénale
     * 
     * @param newPartPenale
     */
    public void setPartPenale(Boolean newPartPenale) {
        partPenale = newPartPenale;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setPlafond(String string) {
        plafond = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setPourcentage(String string) {
        pourcentage = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setPremierAcompte(String acompte) {
        String unformatted = JANumberFormatter.deQuote(acompte);
        premierAcompte = unformatted;
    }

    /**
     * @param vector
     */
    public void setTempEcheances(Vector vector) {
        tempEcheances = vector;
    }
}
