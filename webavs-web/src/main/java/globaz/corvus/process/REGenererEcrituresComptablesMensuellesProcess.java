/*
 * Cr�� le 28 ao�t 07
 */
package globaz.corvus.process;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.tools.PRSession;
import java.util.Map;

/**
 * Process effectuant les �critures comptable. Il proc�de par �tapes successives :
 * 
 * <dl>
 * <dt>Etape 1</dt>
 * <dd>instanciation du processus de compta</dd>
 * 
 * <dt>Etape 2</dt>
 * <dd>Initialisation des rubriques une fois pour tout le process. On a les num�ros de rubriques mais on a besoin de
 * leur id pour g�n�rer les �critures</dd>
 * 
 * <dt>Etape 3</dt>
 * <dd>Regroupement des r�partitions (m�thode {@link #getMapRepartitions() getMapRepartitions()}). On obtient une map
 * contenant en clef une instance de {@link globaz.ij.process.Key Key} et contenant des listes de r�partitions.
 * Concr�tement, les r�partitions de paiement du lot que l'on est en train de traiter sont regroup�es par idTiers(du
 * b�n�ficiaire), idAffilie(du b�n�ficiaire), idTiersEmployeur non affili�. Dans le cas d'un b�n�ficiaire de type
 * employeur non affili�, l'id tiers de la cl� est remplac� par l'id tiers de l'assur� de base; car c'est sur le compte
 * annexe de l'assur� que doivent s'effectuer les �critures comptables, avec un ordre de versement sur l'adresse de
 * l'employeur non affili�.
 * 
 * Si un employeur non affili� � des dettes � compenser, elles ne seront pas effectu�es car l'idTiers de cet employeur
 * est remplac� par celui de l'assur�. Cela dit, un non affili� ne devrait jamais avoir de dettes � compenser.</dd>
 * 
 * <dt>Etape 4</dt>
 * <dd>Cette map de listes de r�partitions est ensuite parcourue pour ajouter a chaque r�partition la liste de
 * ventilations lui correspondant {@link #createVentilations(Map) createVentilations(Map)}</dd>
 * 
 * <dt>Etape 5</dt>
 * <dd>Cr�ation d'une autre map ayant les m�mes clefs que la map des r�partitions et en valeur des listes de
 * compensations ( {@link #createCompensations(Map) createCompensations(Map)}). On a donc � ce moment une map contenant
 * des listes de r�partitions avec leurs ventilations regroup�es par idTiers, idAffilie, etc. et une map contenant les
 * compensations correspondantes.</dd>
 * 
 * <dt>Etape 6</dt>
 * <dd>g�n�ration des �critures pour chaque regroupement de r�partitions. cette �tape est elle m�me compos�e de
 * plusieurs �tapes
 * 
 * <ul>
 * <li>Regroupement pour chaque groupement de r�partition des cotisations par ann�e de cotisation, et des montants
 * bruts. ces regroupements sont faits en diff�renciant les prestations normales des prestations de restitutions qui
 * doivent �tre �crits diff�remment en compta</li>
 * <li>Versement des ventilations de la r�partition qu'on est en train de traiter</li>
 * <li>Ecriture des montants bruts et des restitutions sur les rubriques concern�es</li>
 * <li>Ecriture des cotisations pour la r�partition en cours</li>
 * <li>Ecriture des compensations pour la r�partition en cours</li>
 * <li>Versement effectif de ce qui reste a verser apr�s cotisations, ventilations, compensations</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @author BSC
 */
public class REGenererEcrituresComptablesMensuellesProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateComptable = "";
    private APIRubrique RENTE_API_AI = null;
    private APIRubrique RENTE_API_AI_EXTOURNE = null;
    private APIRubrique RENTE_API_AI_RETROACTIF = null;
    private APIRubrique RENTE_API_AVS = null;
    private APIRubrique RENTE_API_AVS_EXTOURNE = null;
    private APIRubrique RENTE_API_AVS_RETROACTIF = null;
    private APIRubrique RENTE_IMPOT_SOURCE = null;
    private APIRubrique RENTE_PRESTATIONS_AI_RESTITUTION = null;
    private APIRubrique RENTE_PRESTATIONS_API_AI_RESTITUTION = null;
    private APIRubrique RENTE_PRESTATIONS_API_AVS_RESTITUTION = null;
    private APIRubrique RENTE_PRESTATIONS_AVS_RESTITUTION = null;
    private APIRubrique RENTE_RE_AI = null;
    private APIRubrique RENTE_RE_AI_EXTOURNE = null;
    private APIRubrique RENTE_RE_AI_RETROACTIF = null;
    private APIRubrique RENTE_RE_AVS = null;
    private APIRubrique RENTE_RE_AVS_EXTOURNE = null;
    private APIRubrique RENTE_RE_AVS_RETROACTIF = null;
    private APIRubrique RENTE_REGULARISATION_CCP = null;
    private APIRubrique RENTE_RO_AI = null;
    private APIRubrique RENTE_RO_AI_EXTOURNE = null;
    private APIRubrique RENTE_RO_AI_RETROACTIF = null;
    // les rubriques
    private APIRubrique RENTE_RO_AVS = null;
    private APIRubrique RENTE_RO_AVS_EXTOURNE = null;

    private APIRubrique RENTE_RO_AVS_RETROACTIF = null;

    // private RERegroupementOrdreVersementContainer rovContainer = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RETraiterLotDecisionsProcess.
     */
    public REGenererEcrituresComptablesMensuellesProcess() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe RETraiterLotDecisionsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public REGenererEcrituresComptablesMensuellesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Cr�e une nouvelle instance de la classe RETraiterLotDecisionsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public REGenererEcrituresComptablesMensuellesProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return
     */
    // public RERegroupementOrdreVersementContainer getRovContainer() {
    // return rovContainer;
    // }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        boolean noErrorBeforeClose = false;

        try {
            // instanciation du processus de compta
            BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");
            APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                    .getAPIFor(APIGestionComptabiliteExterne.class);
            compta.setDateValeur(dateComptable);
            compta.setEMailAddress(getEMailAddress());

            comptaMemoryLog.setSession((BSession) sessionOsiris);
            compta.setMessageLog(comptaMemoryLog);

            compta.setSendCompletionMail(false);
            compta.setTransaction(getTransaction());
            compta.setProcess(this);

            compta.createJournal();

            // initialisation des ids des rubrique une fois pour tout le
            // process.
            initIdsRubriques(sessionOsiris);

            // pour tous les regroupements du paiement mensuel
            // Iterator iter =
            // getRovContainer().getRegroupementOrdreVersementIterator();
            // while (iter.hasNext()) {

            // }

            FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
            // si pas d'erreurs avant le close, on sauvegarde les messages du
            // comptaMemoryLog
            // pour les restaurer si une erreure survient durant le close
            if (!comptaMemoryLog.hasErrors()) {
                noErrorBeforeClose = true;
                beforeCloseComptaMemoryLog.logMessage(comptaMemoryLog);
            }

            compta.comptabiliser();

            // si pas d'erreurs avant le close et en erreur apr�s le close, on
            // restaure l'ancien
            // memory log pour masquer l'exception.
            // Elle sera directement traitee dans la compta.
            if (noErrorBeforeClose && comptaMemoryLog.hasErrors()) {
                comptaMemoryLog = beforeCloseComptaMemoryLog;
            }

        } catch (Exception e) {
            // si l'exception survient durant le close -> noErrorBeforeClose ==
            // true, l'exception n'est pas remontee
            // Elle sera directement traitee dans la compta.
            if (!noErrorBeforeClose) {

                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
                return false;
            }
        } finally {
            if (comptaMemoryLog != null && comptaMemoryLog.size() > 0) {

                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: START LOG OSIRIS :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaMemoryLog.size(); i++) {

                    getMemoryLog()
                            .logMessage(null, comptaMemoryLog.getMessage(i).getComplement(),
                                    comptaMemoryLog.getMessage(i).getTypeMessage(),
                                    comptaMemoryLog.getMessage(i).getIdSource());
                }
                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: END LOG OSIRIS :::::::::::::::::::::::::::::");
            }
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getDateComptable())) {
            _addError("Pas de date comptable");
        }

        // if(getRovContainer()==null){
        // _addError("Pas de regroupement de rentes accord�es");
        // }

        super._validate();
    }

    /**
     * �crit une �criture en compta. Ne fait rien si le montant est nul.
     * 
     * @param compta
     *            une instance de APIProcessComptabilisation
     * @param montantSigne
     *            Le montant a �crire, sign�
     * @param rubrique
     *            l'id de la rubrique
     * @param idCompteAnnexe
     *            l'id du compta annexe
     * @param idSection
     *            l'id de la section
     * @param anneeCotisation
     *            l'ann�e de la cotisation, null s'il ne s'agit pas d'une cotisation
     */
    private void doEcriture(APIGestionComptabiliteExterne compta, String montantSigne, APIRubrique rubrique,
            String idCompteAnnexe, String idSection, String anneeCotisation) {
        if (!JadeStringUtil.isDecimalEmpty(montantSigne)) {
            FWCurrency montant = new FWCurrency(montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());

            getMemoryLog().logMessage(
                    java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ECRITURE"), new Object[] {
                            montantSigne.toString(), rubrique.getIdExterne() }), FWMessage.INFORMATION,
                    getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }

            if (anneeCotisation != null) {
                ecriture.setAnneeCotisation(anneeCotisation);
            }

            compta.addOperation(ecriture);
        }
    }

    /**
     * @param container
     */
    // public void setRovContainer(RERegroupementOrdreVersementContainer
    // container) {
    // rovContainer = container;
    // }

    /**
     * Effectue un ordre de versement, lance une Exception si le montant est n�gatif
     * 
     * @param compta
     *            DOCUMENT ME!
     * @param idCompteAnnexe
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     * @param montant
     *            DOCUMENT ME!
     * @param idAdressePaiement
     *            DOCUMENT ME!
     * @param nssRequerant
     *            String, NSS du requ�rant principal. Cette valeur peut �tre null.
     * 
     * @throws Exception
     *             Si le montant est n�gatif
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    private void doOrdreVersement(APIGestionComptabiliteExterne compta, String idCompteAnnexe, String idSection,
            String montant, String idAdressePaiement, String nomPrenomRequerant, String referenceInterne)
            throws Exception {

        if (new FWCurrency(montant).isNegative()) {
            throw new IllegalArgumentException("Montant n�gatif non accept� pour un ordre de versement");
        }

        APIOperationOrdreVersement ordreVersement = compta.createOperationOrdreVersement();
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(dateComptable);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setMontant(montant);
        ordreVersement.setCodeISOMonnaieBonification(getSession().getCode(
                IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setCodeISOMonnaieDepot(getSession()
                .getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        getMemoryLog().logMessage(
                java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ORDRE_VERSEMENT"), new Object[] {
                        montant, idAdressePaiement }), FWMessage.INFORMATION,
                getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
        // TODO
        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_IJAI);

        String motif = FWMessageFormat.format(getSession().getLabel("MOTIF_VERSEMENT_IJAI"), getDateComptable());

        if (nomPrenomRequerant != null) {
            motif += " (" + nomPrenomRequerant + ")";
        }

        if (referenceInterne != null && !JadeStringUtil.isEmpty(referenceInterne)) {
            motif += " " + getSession().getLabel("REFERENCE_INTERNE_VENTILLATION") + " : " + referenceInterne;
        }

        ordreVersement.setMotif(motif);
        compta.addOperation(ordreVersement);
    }

    /**
     * getter pour l'attribut date comptable
     * 
     * @return la valeur courante de l'attribut date comptable
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS");
    }

    /**
     * 
     * @param isRestitution
     *            s'il s'agit d'une restitution
     * 
     * @return la rubrique concern�e
     */
    private APIRubrique getRubriqueConcernee(boolean isRestitution) {
        APIRubrique rubrique = null;

        if (isRestitution) {
            rubrique = RENTE_API_AI;
        } else {

        }

        return rubrique;
    }

    /**
     * Donne la bonne section
     * 
     * @param isRestitution
     * @return
     * @throws Exception
     */
    private String getSection(boolean isRestitution) throws Exception {

        String section = null;

        // on retrouve l'annee
        JADate date = new JADate(getDateComptable());
        String AAAA = String.valueOf(date.getYear());

        String code = null;
        if (isRestitution) {
            code = "26";
        } else {
            // paiement principal
            code = "70";
        }

        section = AAAA + code + "000";

        return section;
    }

    /**
     * Initialise les Id des rubriques
     * 
     * @param sessionOsiris
     *            une instance de APIProcessComptabilisation
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void initIdsRubriques(BISession sessionOsiris) throws Exception {
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);
        // TODO
        RENTE_RO_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RO_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RO_AVS_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AVS_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AVS_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RO_AI_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AI_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AI_RETROACTIF = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RO_AVS_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AVS_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AVS_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RO_AI_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_RE_AI_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_API_AI_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_IMPOT_SOURCE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_REGULARISATION_CCP = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_PRESTATIONS_AVS_RESTITUTION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_PRESTATIONS_API_AVS_RESTITUTION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_PRESTATIONS_AI_RESTITUTION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        RENTE_PRESTATIONS_API_AI_RESTITUTION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut date comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateComptable(String string) {
        dateComptable = string;
    }

}
