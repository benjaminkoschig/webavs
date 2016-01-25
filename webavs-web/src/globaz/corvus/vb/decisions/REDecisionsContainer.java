package globaz.corvus.vb.decisions;

import globaz.corvus.api.decisions.IREPreparationDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe servant de container pour regouper les infos du décompte par adr. de pmt.
 * </p>
 * 
 * <p>
 * Structure de la map 'decisions' :
 * 
 * key (adr. pmt) | |---> REDecisionInfoContainer |---> ... |---> REDecisionInfoContainer
 * 
 * 
 * 
 * 
 * Structure de la map : decisions --------------------------------------------------------------
 * REDecisionInfoContainer ¦ key (KeyAdrPmt) ¦ REDecisionInfoContainer ¦-->--------------------------------------
 * -------------------------------------------------------------- ¦ key (keyGrpRA) ¦RenteAccordInfoVo ¦ ¦ ¦ ¦
 * ¦------------------------------------- ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦ ¦
 * -------------------------------------- ¦ ¦ ¦ --------------------------------------------------------------
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * </p>
 * 
 * @author SCR
 */

public class REDecisionsContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * <H1>Description</H1>
     * 
     * <p>
     * Classe servant de clef pour regrouper les décision par adresse de pmt, genre de prestations et réf. pmt.
     * </p>
     */

    class KeyAdrPmt implements Comparable<KeyAdrPmt> {

        // ~ Instance fields
        // ------------------------------------------------------------------------------------------------

        // private String typeMontant = "";

        private String cleRegroupement = "";

        /**
	     */
        private String idDomaineApp = "";

        /**
	     */
        private String idTiersAdrPmt = "";

        /**
	     */
        private String refInterne = "";

        /**
	     */
        // private String genrePrestation = "";

        // ~ Constructors
        // ---------------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe KeyPmtDecision.
         * 
         * @param genrePrestation
         *            DOCUMENT ME!
         * @param idTiersAdrPmt
         *            DOCUMENT ME!
         * @param idDomaineApp
         *            DOCUMENT ME!
         * @param refInterne
         *            DOCUMENT ME!
         * 
         */
        public KeyAdrPmt(String idTiersAdressePmt, String idDomaineApp, String refInterne, String cleRegroupement) {
            // this.typeMontant = typeMontant;
            // this.genrePrestation = genrePrestation;
            idTiersAdrPmt = idTiersAdressePmt;
            this.idDomaineApp = idDomaineApp;
            this.refInterne = refInterne;
            this.cleRegroupement = cleRegroupement;
        }

        // ~ Methods
        // --------------------------------------------------------------------------------------------------------

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         * 
         * @param o
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public int compareTo(KeyAdrPmt keyAdrPmt) {

            if (idTiersAdrPmt.compareTo(keyAdrPmt.idTiersAdrPmt) != 0) {
                return idTiersAdrPmt.compareTo(keyAdrPmt.idTiersAdrPmt);
            } else if (idDomaineApp.compareTo(keyAdrPmt.idDomaineApp) != 0) {
                return idDomaineApp.compareTo(keyAdrPmt.idDomaineApp);
            } else if (refInterne.compareTo(keyAdrPmt.refInterne) != 0) {
                return refInterne.compareTo(keyAdrPmt.refInterne);
            } else if (cleRegroupement.compareTo(keyAdrPmt.cleRegroupement) != 0) {
                return cleRegroupement.compareTo(keyAdrPmt.cleRegroupement);
            } else {
                return 0;
            }
        }

        /**
         * @param obj
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof KeyAdrPmt)) {
                return false;
            }

            KeyAdrPmt keyAdrPmt = (KeyAdrPmt) obj;

            return ((keyAdrPmt.idTiersAdrPmt.equals(idTiersAdrPmt)) && (keyAdrPmt.idDomaineApp.equals(idDomaineApp))
                    && (keyAdrPmt.refInterne.equals(refInterne)) && (keyAdrPmt.cleRegroupement.equals(cleRegroupement)));
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public int hashCode() {
            return (idTiersAdrPmt + idDomaineApp + refInterne).hashCode();
        }

    }

    private Map<KeyAdrPmt, REDecisionInfoContainer> decisions = null;
    private int idDecisionCourante = 0;
    private String idDemandeRente = "";
    private List<KeyAdrPmt> keys = new ArrayList<KeyAdrPmt>();

    public void addElement(BSession session, RERenteAccordeeJoinInfoComptaJoinPrstDues elem, String csTypeDecision,
            String decisionDu, String idBC) throws Exception {

        // On ajoute pas les prestations dues ayant une date de début postérieur
        // à la date de fin de la rente Accordée
        //
        // Exemple : RA [ ]
        // PD [ pd1 ][ pd2 ][ pd3 ]
        //
        // La prestation due #3 ne sera pas prise en compte.
        // La date de fin de la rente accordé peu être la date de fin d'une
        // période AI/API, ou la date de décès du bénéficiaire de la
        // prestation.

        if (!JadeStringUtil.isBlankOrZero(elem.getDateFinDroit())) {
            if (BSessionUtil.compareDateFirstGreater(session, elem.getDebutPaiement(), elem.getDateFinDroit())) {
                return;
            }
        }

        // Si la période de prestation due n'est pas du courant, et que l'on
        // valide le courant, on la saute.
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(csTypeDecision) && !isCourant(elem, decisionDu)) {
            return;
        }

        KeyAdrPmt key = new KeyAdrPmt(elem.getIdTiersAdressePmt(), elem.getIdDomaineApplication(),
                elem.getReferencePmt(), elem.getCleRegroupementDecision());

        if (decisions == null) {
            decisions = new HashMap<REDecisionsContainer.KeyAdrPmt, REDecisionInfoContainer>();
        }

        if (decisions.containsKey(key)) {
            REDecisionInfoContainer decision = decisions.get(key);

            REBeneficiaireInfoVO vo = new REBeneficiaireInfoVO(session, elem.getCsTypeMontant(), Long.parseLong(elem
                    .getIdPrestationAccordee()), elem.getDebutPaiement(), elem.getFinPaiement());

            vo.setFraction(elem.getFractionRente());
            vo.setGenrePrestation(elem.getCodePrestation());
            vo.setIdTiersBeneficiaire(Long.parseLong(elem.getIdTiersBeneficiaire()));

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, elem.getIdTiersBeneficiaire());

            if (tiers == null) {
                throw new Exception("Tiers not found ! idRA/idTiers = " + elem.getIdPrestationAccordee() + "/"
                        + elem.getIdTiersBeneficiaire());
            }

            vo.setDescriptionBeneficiare(tiers.getDescription(session));
            vo.setDateNaissanceBeneficiaire(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
            vo.setMontant(elem.getMontantPrestDue());
            vo.setIdPrestationDue(Long.parseLong(elem.getIdPrestationDue()));

            decision.addPrstDuesParRenteAccordee(null, vo);
            decision.addIdBC(idBC);
            decisions.put(key, decision);

        } else {

            REDecisionInfoContainer decision = new REDecisionInfoContainer();
            // decision.concatGenrePrestation(elem.getCodePrestation());
            decision.setCsTypeDecision(csTypeDecision);
            decision.setDecisionDu(decisionDu);

            REBeneficiaireInfoVO vo = new REBeneficiaireInfoVO(session, elem.getCsTypeMontant(), Long.parseLong(elem
                    .getIdPrestationAccordee()), elem.getDebutPaiement(), elem.getFinPaiement());

            vo.setFraction(elem.getFractionRente());
            vo.setGenrePrestation(elem.getCodePrestation());
            vo.setIdTiersBeneficiaire(Long.parseLong(elem.getIdTiersBeneficiaire()));
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, elem.getIdTiersBeneficiaire());

            if (tiers == null) {
                throw new Exception("Tiers not found ! idRA/idTiers = " + elem.getIdPrestationAccordee() + "/"
                        + elem.getIdTiersBeneficiaire());
            }

            vo.setDescriptionBeneficiare(tiers.getDescription(session));
            vo.setMontant(elem.getMontantPrestDue());
            vo.setIdPrestationDue(Long.parseLong(elem.getIdPrestationDue()));

            decision.addPrstDuesParRenteAccordee(null, vo);
            decision.addIdBC(idBC);
            decisions.put(key, decision);

            keys.add(key);
        }
    }

    /** passe a la décision precedante */
    public void decisionPrecedente() {
        if (hasDecisionPrecedente()) {
            idDecisionCourante--;
        }
    }

    /** passe a la décision suivante */
    public void decisionSuivante() {
        if (hasDecisionSuivante()) {
            idDecisionCourante++;
        }
    }

    public REDecisionInfoContainer getDecisionIC() {

        if ((keys == null) || (decisions == null)) {
            return null;
        }

        else if (idDecisionCourante >= keys.size()) {
            return null;
        }

        else if (idDecisionCourante < 0) {
            return null;
        }

        KeyAdrPmt key = keys.get(idDecisionCourante);
        return decisions.get(key);

    }

    /**
     * @return the idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * retourne vrai si il y a plusieurs décisions et que la décision courante est après la premiere de la liste.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasDecisionPrecedente() {
        return (keys != null) && (idDecisionCourante > 0);
    }

    /**
     * retourne vrai si il y a plusieurs décisins et que la décision courante est avant la derniere de la liste.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasDecisionSuivante() {
        return (keys != null) && (idDecisionCourante < (keys.size() - 1));
    }

    public void init() {
        idDecisionCourante = 0;
    }

    private boolean isCourant(RERenteAccordeeJoinInfoComptaJoinPrstDues elem, String decisionDu) throws Exception {

        if (JadeStringUtil.isBlankOrZero(elem.getFinPaiement())) {
            return true;
        }

        JADate dateDebutPmt = new JADate(elem.getDebutPaiement());
        JADate dateFinPmt = new JADate(elem.getFinPaiement());
        JADate dateDecisionDu = new JADate(decisionDu);

        JACalendar cal = new JACalendarGregorian();
        if ((JACalendar.COMPARE_FIRSTUPPER == cal.compare(dateDecisionDu, dateDebutPmt))
                && (JACalendar.COMPARE_FIRSTLOWER == cal.compare(dateDecisionDu, dateFinPmt))) {

            return true;
        }
        return false;
    }

    /**
     * 
     * Charge la décision 'info' dans le container
     * 
     * @param session
     * @param decision
     * @throws Exception
     */
    public void loadDecision(BSession session, REDecisionEntity decision) throws Exception {

        if ((decision == null) || decision.isNew()) {
            return;
        }

        if (decisions == null) {
            decisions = new HashMap();
        }
        JACalendar cal = new JACalendarGregorian();
        JADate dfRetroPure = null;
        // Si décision de type Retro Pure, on recherche la date de fin du RETRO
        if (IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO.equals(decision.getCsTypeDecision())) {
            dfRetroPure = new JADate(decision.getDateFinRetro());
        }

        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setForIdDecision(decision.getIdDecision());

        mgr.setSession(session);
        mgr.find(BManager.SIZE_NOLIMIT);

        // Déclaration unique d'une clé constante.
        // Une seule décision sera chargée.

        // Fake permet de charger plusieurs décisions en gardant le même
        // principe, lors de la création des décision
        // via la methode addElement.
        KeyAdrPmt key = new KeyAdrPmt("CONST", "CONST", "REF-CONST", "KEY-CONST");

        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions elm = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr
                    .get(i);

            // En cas de RETRO pure, la fin de paiement du $p peut être erroné si entre la prise de décision du
            // courant
            // et la prise de décision du RETRO, il y a eu l'adaptation bis-anuelle.
            // Dans ce cas, la date finPaiement vaudra par exemple. 12.2011 alors que si la décision courante a été
            // validée
            // en 10.2010, cette date de fin de paiement doit valoir 09.2010 au lieu de 12.2011.
            if (dfRetroPure != null) {
                JADate dfPmt = new JADate(elm.getFinPaiement());
                if (cal.compare(dfRetroPure, dfPmt) == JACalendar.COMPARE_FIRSTLOWER) {
                    elm.setFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dfRetroPure.toStrAMJ()));
                }
            }

            if (decisions.containsKey(key)) {
                REDecisionInfoContainer decIC = decisions.get(key);

                REBeneficiaireInfoVO vo = new REBeneficiaireInfoVO(session, elm.getCsTypeMontant(), Long.parseLong(elm
                        .getIdPrestationAccordee()), elm.getDebutPaiement(), elm.getFinPaiement());

                vo.setFraction(elm.getFractionRente());
                vo.setGenrePrestation(elm.getCodePrestation());

                vo.setIdTiersBeneficiaire(Long.parseLong(elm.getIdTiersBeneficiaire()));

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, elm.getIdTiersBeneficiaire());

                if (tiers == null) {
                    throw new Exception("Tiers not found ! idRA/idTiers = " + elm.getIdPrestationAccordee() + "/"
                            + elm.getIdTiersBeneficiaire());
                }

                vo.setDescriptionBeneficiare(tiers.getDescription(session));
                vo.setDateNaissanceBeneficiaire(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                vo.setMontant(elm.getMontantPrestDue());
                vo.setIdPrestationDue(Long.parseLong(elm.getIdPrestationDue()));
                decIC.addPrstDuesParRenteAccordee(decision, vo);
                decisions.put(key, decIC);

            }

            else {
                // Ne devrait jamais arriver, sinon on est mal !!!
                /*
                 * if (i>0) { //throw newException( "Load Decision Error, incohérance dans la clé de référencement !!!"
                 * ); System.out.println("ouillllouuuiiiiiiiillllllleeeeee"); }
                 */

                REDecisionInfoContainer decIC = new REDecisionInfoContainer();
                // decIC.concatGenrePrestation(elm.getCodePrestation());
                decIC.setCsTypeDecision(decision.getCsTypeDecision());
                decIC.setDecisionDu(decision.getDecisionDepuis());
                decIC.setIdDecision(decision.getIdDecision());

                // Il se peut que la date de fin de la PD soient vide alors que
                // la RA en ait une.
                // Dans ce cas, on saisira la date de fin de la RA.
                // Ce cas arrive en cas de diminution d'une rente par exemple.

                String df = elm.getFinPaiement();
                if (JadeStringUtil.isBlankOrZero(df)) {
                    df = elm.getDateFinDroit();
                }

                REBeneficiaireInfoVO vo = new REBeneficiaireInfoVO(session, elm.getCsTypeMontant(), Long.parseLong(elm
                        .getIdPrestationAccordee()), elm.getDebutPaiement(), df);

                vo.setFraction(elm.getFractionRente());
                vo.setGenrePrestation(elm.getCodePrestation());
                vo.setIdTiersBeneficiaire(Long.parseLong(elm.getIdTiersBeneficiaire()));
                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, elm.getIdTiersBeneficiaire());

                if (tiers == null) {
                    throw new Exception("Tiers not found ! idRA/idTiers = " + elm.getIdPrestationAccordee() + "/"
                            + elm.getIdTiersBeneficiaire());
                }

                vo.setDescriptionBeneficiare(tiers.getDescription(session));
                vo.setMontant(elm.getMontantPrestDue());
                vo.setIdPrestationDue(Long.parseLong(elm.getIdPrestationDue()));
                decIC.addPrstDuesParRenteAccordee(decision, vo);
                decisions.put(key, decIC);
                keys.add(key);
            }
        }
    }

    public void parcourDecisionsIC(BSession session) throws Exception {

        init();
        REDecisionInfoContainer dic = getDecisionIC();

        if (dic != null) {
            dic.initMapBenef(session, getDecisionIC().getIdDecision());
            dic.initAnnexesCopiesRemarqueDecision(session, getDecisionIC().getIdDecision());
        }

        while (hasDecisionSuivante()) {
            decisionSuivante();
            dic = getDecisionIC();

            if (dic != null) {
                dic.initMapBenef(session, getDecisionIC().getIdDecision());
                dic.initAnnexesCopiesRemarqueDecision(session, getDecisionIC().getIdDecision());
            }
        }
    }

    /**
     * @param idDemandeRente
     *            the idDemandeRente to set
     */
    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

}
