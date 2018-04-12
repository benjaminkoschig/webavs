package globaz.orion.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.orion.process.adi.ADIControlesEnum;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.toolbox.CPToolBox;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;
import ch.globaz.orion.db.EBDemandeModifAcompteMessageEntity;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.xmlns.eb.adi.DecisionAcompteIndEntity;

/**
 * Cette class implémente les différents contrôles sur les décisions. Les méthodes retourne l'instance courante
 * de EBControleADI, ce qui permet de les chaîner. Pour finir, un appel à la méthode "persistErreurs()" permet d'insérer
 * ces erreurs dans la table EBDEM_MODIF_ACO_MESSAGE.
 * 
 * @author cbu
 * 
 */
public class EBControlesADI {
    private static final String CONST_1ER_JANVIER = "01.01.";
    // private final static String CONTROLE_DIFFERENCE_25_POURCENT = "ADI_CONTROLE_DIFFERENCE_25_POURCENT";
    // private final static String CONTROLE_ACTIVITE_ACCESSOIRE = "ADI_CONTROLE_ACTIVITE_ACCESSOIRE";
    // private final static String CONTROLE_CONTENTIEUX_EXISTANT = "ADI_CONTROLE_CONTENTIEUX_EXISTANT";
    // private final static String CONTROLE_IRRECOUVRABLE = "ADI_CONTROLE_IRRECOUVRABLE";
    // private final static String CONTROLE_PERTE_COMMERCIALE = "ADI_CONTROLE_PERTE_COMMERCIALE";
    // private final static String CONTROLE_TAXATION_EN_COURS = "ADI_CONTROLE_TAXATION_EN_COURS";
    // private final static String CONTROLE_COMMUNICATION_FISCALE_RECUE = "ADI_CONTROLE_COMMUNICATION_FISCALE_RECUE";
    // private final static String CONTROLE_PAS_DE_COTISATION_AVS = "ADI_CONTROLE_PAS_DE_COTISATION_AVS";
    // private final static String CONTROLE_DIN_1181 = "ADI_CONTROLE_DIN_1181";
    // private final static String CONTROLE_REDUCTION = "ADI_CONTROLE_REDUCTION";
    // private final static String CONTROLE_REMISE = "ADI_CONTROLE_REMISE";
    // private final static String CONTROLE_PLUSIEURS_PERIODES_DANS_ANNEE =
    // "ADI_CONTROLE_PLUSIEURS_PERIODES_DANS_ANNEE";
    // private final static String CONTROLE_REMARQUE_TRANSMISE = "ADI_CONTROLE_REMARQUE_TRANSMISE";
    // private final static String CONTROLE_ERREUR_INCONNUE = "ADI_CONTROLE_ERREUR_INCONNUE";

    private BSession session = null;
    private Annee anneeDecision = null;
    private AFAffiliation affiliation = null;
    private DecisionAcompteIndEntity demande = null;
    List<ADIControlesEnum> listeErreurs = null;

    public EBControlesADI(BSession session, DecisionAcompteIndEntity demande, AFAffiliation affiliation) {
        if (session == null) {
            throw new IllegalArgumentException("Session can't be null!");
        }

        if (demande == null) {
            throw new IllegalArgumentException("Demande can't be null!");
        }

        if (affiliation == null) {
            throw new IllegalArgumentException("Affiliation can't be null!");
        }

        this.session = session;
        this.demande = demande;
        this.affiliation = affiliation;
        anneeDecision = new Annee(demande.getAnnee());
        listeErreurs = new ArrayList<ADIControlesEnum>();
    }

    private BSession getSession() {
        return session;
    }

    private void addMessageDemande(ADIControlesEnum erreur) {
        listeErreurs.add(erreur);
    }

    /**
     * Cette méthode va insérer les erreurs rencontrées dans EBDEM_MODIF_ACO_MESSAGE puis retourne la liste
     * 
     * @param demandeCree
     * 
     * @return la liste des erreurs rencontrées
     * @throws Exception
     */
    public List<ADIControlesEnum> persistMessages(EBDemandeModifAcompteEntity demandeCree) throws Exception {
        for (ADIControlesEnum erreur : listeErreurs) {
            EBDemandeModifAcompteMessageEntity demandeModifAcompteMessageEntity = new EBDemandeModifAcompteMessageEntity();
            demandeModifAcompteMessageEntity.setSession(getSession());
            demandeModifAcompteMessageEntity.setIdDemande(demandeCree.getId());
            demandeModifAcompteMessageEntity.setMessageErreur(erreur.getLabel());
            demandeModifAcompteMessageEntity.add();
        }

        return listeErreurs;
    }

    public List<ADIControlesEnum> getListeErreurs() {
        return listeErreurs;
    }

    /**
     * Recherche pour l'année concernée la décision active. Indique l'erreur "Différence de 25%" si le revenu communiqué
     * par l'indépendant est 25% supérieur ou inférieur à celui de la décision trouvée.
     * 
     * @return this
     */
    public EBControlesADI isDifference25Pourcent() {
        CPDecision decision = null;
        // Rechercher le montant minimum pour l'année concernée

        // Retrouver la dernière décision active
        CPDecisionManager decisionManager = new CPDecisionManager();
        Montant revenuDecisionTrouvee = null;
        Montant revenuDecisionPortail = null;

        try {
            // Faire la comparaison de 25% uniquement pour les revenu > au revenu minimum
            float revenuMin = CPTableIndependant.getRevenuMin(getSession().getCurrentThreadTransaction(), "01.01."
                    + demande.getAnnee());
            if (demande.getResultatNet().floatValue() > revenuMin) {

                decisionManager.setSession(session);
                decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
                decisionManager.setForIsActive(true);
                decisionManager.setForAnneeDecision(anneeDecision.toString());
                decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
                decisionManager.find(BManager.SIZE_USEDEFAULT);

                if (!decisionManager.isEmpty()) {
                    decision = (CPDecision) decisionManager.getFirstEntity();

                    CPDonneesBase donneesBase = new CPDonneesBase();
                    donneesBase.setSession(session);
                    donneesBase.setIdDecision(decision.getIdDecision());
                    donneesBase.retrieve();

                    if (!donneesBase.isNew()) {
                        if (donneesBase.getRevenu1() != null && !donneesBase.getRevenu1().isEmpty()) {
                            revenuDecisionTrouvee = new Montant(donneesBase.getRevenu1());
                            Taux taux25 = new Taux(25);
                            Montant revenuDecisionTrouvee25Percent = revenuDecisionTrouvee.multiply(taux25);
                            Montant revenuDecisionTrouvee25PerscentSup = revenuDecisionTrouvee
                                    .add(revenuDecisionTrouvee25Percent);
                            Montant revenuDecisionTrouvee25PercentInf = revenuDecisionTrouvee
                                    .substract(revenuDecisionTrouvee25Percent);

                            revenuDecisionPortail = new Montant(demande.getResultatNet());

                            if (revenuDecisionPortail.greater(revenuDecisionTrouvee25PerscentSup)
                                    || revenuDecisionPortail.less(revenuDecisionTrouvee25PercentInf)) {
                                addMessageDemande(ADIControlesEnum.CONTROLE_DIFFERENCE_25_POURCENT);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
        }

        return this;
    }

    /**
     * Contrôle si l'indépendant à la particularité d'affiliation "Activité accessoire"
     * 
     * @return this
     * @throws Exception
     */
    public EBControlesADI isActiviteAccessoire() throws Exception {
        if (AFParticulariteAffiliation.existeParticularite(getSession().getCurrentThreadTransaction(),
                affiliation.getId(), AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, CONST_1ER_JANVIER
                        + anneeDecision)) {
            // log erreur
            addMessageDemande(ADIControlesEnum.CONTROLE_ACTIVITE_ACCESSOIRE);
        }
        return this;
    }

    /**
     * Contrôle si l'indépendant à une section ouverte au moins à l'état "poursuite"
     * 
     * @return this
     */
    public EBControlesADI isContentieux() {
        try {
            if (!JadeStringUtil.isEmpty(affiliation.getId())) {
                // Extraction du compte annexe
                String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        getSession().getApplication());
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(affiliation.getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve();
                if ((compte != null) && !compte.isNew()) {
                    if (compte.isCompteAnnexeAvecSectionsPoursuite(true, "")) {
                        // log erreur
                        addMessageDemande(ADIControlesEnum.CONTROLE_CONTENTIEUX_EXISTANT);
                    }
                }
            }
        } catch (Exception ex) {
            addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
        }

        return this;
    }

    /**
     * Contrôle si l'indépendant à une écriture CI de type "Irrécouvrable"
     * 
     * @return this
     * @throws Exception
     */
    public EBControlesADI isCIIrrecouvrable() throws Exception {
        CICompteIndividuelUtil ciUtil = new CICompteIndividuelUtil();
        ciUtil.setSession(session);
        if (ciUtil.hasIrrecouvrable(NSUtil.unFormatAVS(affiliation.getTiers().getNumAvsActuel()),
                anneeDecision.toString(), session.getCurrentThreadTransaction())) {
            // log erreur
            addMessageDemande(ADIControlesEnum.CONTROLE_IRRECOUVRABLE);
        }

        return this;
    }

    /**
     * Contrôle si l'indépendant possède une décision définitive ou rectificative pour l'année communiquée - 1 an et si
     * le revenu de l'indépendant est < 0 pour la décision trouvée.
     * 
     * @return this
     */
    public EBControlesADI isPerteCommerciale() {
        try {
            if (!JadeStringUtil.isBlankOrZero(affiliation.getId())
                    && !JadeStringUtil.isBlankOrZero(anneeDecision.toString())) {
                // recherche si décision n - 1 avait un revenu négatif
                int annee = Integer.parseInt(anneeDecision.toString()) - 1;
                CPDecisionManager decMng = new CPDecisionManager();
                decMng.setSession(getSession());
                decMng.setForAnneeDecision(Integer.toString(annee));
                decMng.setForIdAffiliation(affiliation.getId());
                decMng.setInTypeDecision(CPDecision.CS_DEFINITIVE + ", " + CPDecision.CS_RECTIFICATION);
                decMng.setForIsActive(Boolean.TRUE);
                decMng.find(BManager.SIZE_NOLIMIT);
                if (decMng.getSize() > 0) {
                    CPDecision dec = (CPDecision) decMng.getFirstEntity();
                    // recherche du revenu
                    CPDonneesBase base = new CPDonneesBase();
                    base.setSession(getSession());
                    base.setIdDecision(dec.getIdDecision());
                    base.retrieve();
                    if (!base.isNew() && !JadeStringUtil.isBlankOrZero(base.getRevenu1())) {
                        float revenu = Float.parseFloat(JANumberFormatter.deQuote(base.getRevenu1()));
                        if (revenu < 0) {
                            // log erreur
                            addMessageDemande(ADIControlesEnum.CONTROLE_PERTE_COMMERCIALE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
        }
        return this;
    }

    /**
     * Contrôle si l'indépendant a une décision en cours de travail, quel que soit l'année.
     * 
     * @return this
     */
    public EBControlesADI isTaxationEnCours() {
        try {
            if (!JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
                CPDecisionManager decManager = new CPDecisionManager();
                decManager.setSession(getSession());
                decManager.setForIdAffiliation(affiliation.getId());
                decManager.setForIsActive(Boolean.FALSE);
                // decManager.setForAnneeDecision(anneeDecision.toString());
                decManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
                decManager.setInEtat(CPDecision.CS_VALIDATION + ", " + CPDecision.CS_CREATION + ", "
                        + CPDecision.CS_CALCUL);
                decManager.find(BManager.SIZE_NOLIMIT);
                if (decManager.size() > 0) {
                    // Log erreur
                    addMessageDemande(ADIControlesEnum.CONTROLE_TAXATION_EN_COURS);
                }
            }
        } catch (Exception ex) {
            addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
        }
        return this;
    }

    /**
     * Contrôle si l'indépendant possède une communication fiscale non traitée, quelque soit l'année.
     * 
     * @return this
     */
    public EBControlesADI isCommunicationFiscaleRecue() {
        CPCommunicationFiscaleRetourManager communicationFiscaleRetourManager = new CPCommunicationFiscaleRetourManager();
        communicationFiscaleRetourManager.setSession(getSession());
        communicationFiscaleRetourManager.setForIdAffiliation(affiliation.getId());

        String inStatus = CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE + ","
                + CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + ","
                + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT + ","
                + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE + ","
                + CPCommunicationFiscaleRetourViewBean.CS_ERREUR + ","
                + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE + ","
                + CPCommunicationFiscaleRetourViewBean.CS_VALIDE;
        communicationFiscaleRetourManager.setInStatus(inStatus);

        try {
            if (communicationFiscaleRetourManager.getCount() > 0) {
                // log erreur
                addMessageDemande(ADIControlesEnum.CONTROLE_COMMUNICATION_FISCALE_RECUE);
            }
        } catch (Exception ex) {
            addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
        }

        return this;
    }

    /**
     * Contrôle si l'indépendant n'a pas de cotisation active de genre "AVS personnel" pour l'année concernée.
     * 
     * @return this
     */
    public EBControlesADI isCotisationPersonnelle() {
        if (affiliation != null) {
            try {
                if (!AFAffiliationUtil.hasCotPersActif(affiliation, CONST_1ER_JANVIER + anneeDecision, "31.12."
                        + anneeDecision)) {
                    // log erreur
                    addMessageDemande(ADIControlesEnum.CONTROLE_PAS_DE_COTISATION_AVS);
                }
            } catch (Exception ex) {
                addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
            }
        }

        return this;
    }

    /**
     * Contrôle si l'indépendant à la case DIN 1181 d'active sur la décision.
     * 
     * @return this
     */
    public EBControlesADI isDIN1181() {
        if (!JadeStringUtil.isBlankOrZero(affiliation.getId())
                && !JadeStringUtil.isBlankOrZero(anneeDecision.toString())) {
            // recherche décision en cours

            try {
                CPDecisionManager decMng = new CPDecisionManager();
                decMng.setSession(getSession());
                decMng.setForIdAffiliation(affiliation.getId());
                decMng.setForAnneeDecision(anneeDecision.toString());
                decMng.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
                decMng.setForIsActive(Boolean.TRUE);
                decMng.find(BManager.SIZE_NOLIMIT);
                if (decMng.getSize() > 0) {
                    CPDecision dec = (CPDecision) decMng.getFirstEntity();
                    // Controle si Cotisation paye en tant que salsairé (DIN 1181)
                    if (!dec.isNew() && Boolean.TRUE.equals(dec.getCotiMinimumPayeEnSalarie())) {
                        // log erreur
                        addMessageDemande(ADIControlesEnum.CONTROLE_DIN_1181);
                    }
                }
            } catch (Exception ex) {
                addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
            }
        }

        return this;
    }

    /**
     * Contrôle si, pour l'année concernée, l'indépenant a une écriture en comptabilité sur la rubrique de réduction.
     * 
     * @return this
     */
    public EBControlesADI isReductionCotisation() {
        if (affiliation != null) {
            try {
                if (!JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                    AFAssuranceManager manager = new AFAssuranceManager();
                    manager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                    manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                    manager.setSession(getSession());
                    manager.find(BManager.SIZE_NOLIMIT);
                    if (manager.size() > 0) {
                        getCompteAnnexe(manager, CodeSystem.GEN_PARAM_ASS_REDUCTION,
                                ADIControlesEnum.CONTROLE_REDUCTION);
                    }
                }
            } catch (Exception ex) {
                addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
            }
        }

        return this;
    }

    /**
     * Contrôle si, pour l'année concernée, l'indépenant a une écriture en comptabilité sur la rubrique de remise.
     * 
     * @return this
     */
    public EBControlesADI isRemiseCotisation() {
        if (affiliation != null) {
            try {
                if (!JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                    AFAssuranceManager manager = new AFAssuranceManager();
                    manager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                    manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                    manager.setSession(getSession());
                    manager.find(BManager.SIZE_NOLIMIT);
                    if (manager.size() > 0) {
                        getCompteAnnexe(manager, CodeSystem.GEN_PARAM_ASS_REMISE, ADIControlesEnum.CONTROLE_REMISE);
                    }
                }
            } catch (Exception ex) {
                addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
            }
        }

        return this;
    }

    private void getCompteAnnexe(AFAssuranceManager manager, String genParamAssReduction, ADIControlesEnum typeMessage)
            throws Exception {
        AFAssurance assurance = (AFAssurance) manager.getFirstEntity();
        String rubExterne = assurance.getParametreAssuranceValeur(genParamAssReduction, CONST_1ER_JANVIER
                + anneeDecision, "");
        if (!JadeStringUtil.isNull(rubExterne) && !JadeStringUtil.isEmpty(rubExterne)) {
            // recherche id Rubrique
            CARubrique rub = new CARubrique();
            rub.setSession(getSession());
            rub.setIdExterne(rubExterne);
            rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            rub.retrieve();
            if (!rub.isNew()) {
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(1);
                compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        getSession().getApplication()));
                compte.setIdExterneRole(affiliation.getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve(getSession().getCurrentThreadTransaction());
                if (!compte.isNew()
                        && !JadeStringUtil.isIntegerEmpty(CPToolBox.rechMontantFacture(getSession(), getSession()
                                .getCurrentThreadTransaction(), compte.getIdCompteAnnexe(), rub.getIdRubrique(),
                                anneeDecision.toString()))) {
                    // log erreur
                    addMessageDemande(typeMessage);
                }
            }
        }
    }

    /**
     * Contrôle si l'indépedant a plusieurs décisions actives pour l'année concernée.
     * 
     * @return this
     */
    public EBControlesADI isPlusieursDecisionsPourAnnee() {
        if (!JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            try {
                CPDecisionManager decManager = new CPDecisionManager();
                decManager.setSession(getSession());
                decManager.setForIdTiers(affiliation.getIdTiers());
                decManager.setForIsActive(Boolean.TRUE);
                decManager.setForAnneeDecision(anneeDecision.toString());
                decManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
                decManager.find(BManager.SIZE_NOLIMIT);
                if (decManager.size() > 1) {
                    // Log erreur
                    addMessageDemande(ADIControlesEnum.CONTROLE_PLUSIEURS_PERIODES_DANS_ANNEE);
                }
            } catch (Exception ex) {
                addMessageDemande(ADIControlesEnum.CONTROLE_ERREUR_INCONNUE);
            }
        }
        return this;
    }

    /**
     * Contrôle si une remarque a été transmise
     * 
     * @return this
     */
    public EBControlesADI isRemarquePortail() {
        // Log "Remarque Transmise" si remarque transmise depuis le portail
        if (demande.getRemarquePartner() != null && !demande.getRemarquePartner().isEmpty()) {
            addMessageDemande(ADIControlesEnum.CONTROLE_REMARQUE_TRANSMISE);
        }
        return this;
    }
}
