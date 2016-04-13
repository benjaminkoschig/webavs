package globaz.osiris.db.comptes;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISlave;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.bulletinneutre.CAComptabiliserBulletinNeutre;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperation;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperationManager;
import globaz.osiris.external.IntEcritureDouble;
import globaz.osiris.utils.CADateUtil;
import globaz.osiris.utils.CASursisPaiementEcheancier;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;
import java.math.BigDecimal;

/**
 * Date de création : (29.01.2002 18:29:44)
 * 
 * @author: Administrator
 * @revision SCO 22 janv. 2010
 */
public class CAEcriture extends CAOperation implements APIEcriture, APISlave {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DELAI_DECLENCHEMENT_DEFAUT = "0";
    private static final String LABEL_COMPTE_ANNEXE_AUXILIAIRE = "COMPTE_ANNEXE_AUXILIAIRE";
    private static final String LABEL_SECTION_AUX_INEXISTANTE = "SECTION_AUX_INEXISTANTE";

    public static void inverserCodeDebitCredit(CAEcriture ecriture) {
        // Inverser le signe
        if (ecriture.getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
            ecriture.setCodeDebitCredit(APIEcriture.EXTOURNE_CREDIT);
        } else if (ecriture.getCodeDebitCredit().equals(APIEcriture.DEBIT)) {
            ecriture.setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
        } else if (ecriture.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
            ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
        } else if (ecriture.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
        }
    }

    private String codeDebitCreditEcran = null;

    private CARubrique contrepartie;
    private Integer delaiDateValeur = null;
    private String idCompteEcran = new String();
    private String idExterneRubriqueEcran = new String();
    private boolean loadIdExterneRubriqueEcran = false;
    private String masseEcran = null;
    private String montantEcran = null;
    private Boolean rechercheRubriqueEcran = new Boolean(false);
    private CARubrique rubrique;

    private Boolean unsynchronizeSigneMasse = Boolean.FALSE;

    /**
     * Constructor for CAEcriture.
     */
    public CAEcriture() {
        super();
        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAECRITURE);
    }

    public CAEcriture(CAOperation parent) {
        super(parent);

        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAECRITURE);

        // Mettre à jour l'indication de synchronisation du signe de la masse
        // avec la cotisation
        _updateUnsynchronizeSigneMasseOnLoad();
    }

    /**
     * Après l'activation, pour toutes opérations de types écritures (E%) et si l'id de la section auxiliaire est
     * renseignée il faut dupliquer l'écriture sur la section du compte annexe auxiliaire liée.
     */
    @Override
    protected void _afterActiver(BTransaction transaction) {
        super._afterActiver(transaction);

        if (!JadeStringUtil.isIntegerEmpty(getIdSectionAux())) {
            try {
                CASection sectionAux = new CASection();
                sectionAux.setSession(getSession());
                sectionAux.setIdSection(getIdSectionAux());

                sectionAux.retrieve(transaction);

                if (sectionAux.isNew()) {
                    _addError(transaction, getSession().getLabel(CAEcriture.LABEL_SECTION_AUX_INEXISTANTE));
                }

                CACompteAnnexe compteAnnexeAux = new CACompteAnnexe();
                compteAnnexeAux.setSession(getSession());
                compteAnnexeAux.setIdCompteAnnexe(sectionAux.getIdCompteAnnexe());

                compteAnnexeAux.retrieve(transaction);

                String idOperationAux = sectionAux.duplicateEcritureToAuxiliaire(transaction, compteAnnexeAux,
                        getJournal(), this);

                groupWithEcritureAux(transaction, idOperationAux);
            } catch (Exception e) {
                JadeLogger.error(this, e);
                _addError(transaction, "Problème lors de la copie décriture sur la section auxiliaire.");
            }
        }

        // Report automatiques de la date de déclenchement
        // Si le paiement provient de l'office des poursuites ou des faillites
        if (getProvenancePmt().equals(APIOperation.PROVPMT_ACOMPTEOP)
                || getProvenancePmt().equals(APIOperation.PROVPMT_ACOMPTEOF)) {
            // Si le codeMaster est Master ou Single
            if (getCodeMaster().equals(APIOperation.MASTER) || getCodeMaster().equals(APIOperation.SINGLE)) {
                reportDateDeclenchementContentieux(transaction);
            }
        }
    }

    /**
     * Effectue des traitements après un ajout dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après l'ajout de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        _synchroValUtili();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2002 18:39:22)
     */
    @Override
    protected void _afterRetrieveWithResultSet(globaz.globall.db.BStatement statement) throws Exception {
        // Laisser la supercalsse traiter l'événement
        super._afterRetrieveWithResultSet(statement);

        // Charger les zones pour l'écran si nécessaire
        loadIdExterneRubriqueEcran = true;

        // Mettre à jour l'indication de synchronisation du signe de la masse
        // avec la cotisation
        _updateUnsynchronizeSigneMasseOnLoad();
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        _synchroValUtili();
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_beforeActiver(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeActiver(BTransaction tr) {
        // Gestion de la caisse professionnelle
        if ((getCompte() != null) && getCompte().isUseCaissesProf()) {
            if (JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())
                    && !JadeStringUtil.isIntegerEmpty(getSection().getIdCaisseProfessionnelle())) {
                // Met à jour l'ecriture avec la Section
                setIdCaisseProfessionnelle(getSection().getIdCaisseProfessionnelle());
            }
        }

        // Mandat inforom112 : Solder les plans de paiements si la section est
        // unique et soldée ( <= 0 )
        if (getCodeMaster().equals(APIOperation.SINGLE)
                && !JadeStringUtil.isBlankOrZero(getSection().getIdPlanRecouvrement())) {

            FWCurrency currency = new FWCurrency(getSection().getSolde());
            currency.add(getMontantToCurrency());

            // Le solde est négatif ou égal à zero, la section est soldée
            if (!currency.isPositive()
                    && CAPlanRecouvrement.CS_ACTIF.equals(getSection().getPlanRecouvrement().getIdEtat())) {

                try {
                    CACouvertureSectionManager manager = new CACouvertureSectionManager();
                    manager.setSession(getSession());
                    manager.setForIdPlanRecouvrement(getSection().getIdPlanRecouvrement());
                    manager.find(tr);

                    if (manager.size() == 1) { // Il y a une seule section
                        // (condition pour solder le plan)

                        CAPlanRecouvrement plan = getSection().getPlanRecouvrement();
                        plan.setIdEtat(CAPlanRecouvrement.CS_SOLDE);
                        plan.setSession(getSession());
                        plan.update(tr); // On solde le plan
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    _addError(tr, "Problème lors du solde du plan de paiement.");
                }
            }

            // Inforom 313
            // Mettre à jour les échéances
            if (APIRubrique.COMPTE_FINANCIER.equals(getCompte().getNatureRubrique())
                    || APIRubrique.COMPTE_COMPENSATION.equals(getCompte().getNatureRubrique())) {
                if ((new FWCurrency(getMontant())).isNegative()) {
                    try {
                        if (CAPlanRecouvrement.CS_ACTIF
                                .equalsIgnoreCase(getSection().getPlanRecouvrement().getIdEtat())) {
                            CASursisPaiementEcheancier.ventilationPaiementEcheance(tr, getMontant(), getSection()
                                    .getIdPlanRecouvrement(), getDate());
                        }

                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                        _addError(tr, "Problème lors de la recherche des échéances - " + e.toString());
                    }
                }
            }
        }

        // ignorer les codes master
        if (getCodeMaster().equals(APIOperation.MASTER)) {
            return;
        }

        // Traitement des bulletins neutres
        if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection().getIdTypeSection())
                && APIEcriture.CREDIT.equals(getCodeDebitCredit()) && APIOperation.SINGLE.equals(getCodeMaster())
                && isInstanceOrSubClassOf(APIOperation.CAPAIEMENT)) {
            // Mandat D0009
            // Ventiler si mode globale est "inactif" ou si mode du compta annexe est "ventilation" ou si le mode par
            // défaut du compta annexe est "Par défaut" et que la mode globale est "Ventilation"
            // sinon l'écriture est portée au crédit de la version.
            if (getCompteAnnexe().getModeBulletinNeutre().equalsIgnoreCase(CACompteAnnexe.CS_BN_VENTILATION)
                    || CACompteAnnexe.CS_BN_INACTIF.equalsIgnoreCase(getModeTraitementBulletinNeutreParDefaut())
                    || (getCompteAnnexe().getModeBulletinNeutre().equalsIgnoreCase(CACompteAnnexe.CS_BN_DEFAUT) && getModeTraitementBulletinNeutreParDefaut()
                            .equalsIgnoreCase(CACompteAnnexe.CS_BN_VENTILATION))) {

                CAComptabiliserBulletinNeutre.activer(tr, this);
            }
        }

        // Vérifier si l'écriture doit être ventilée on la transforme en code
        // master
        if (getCompte().getEstVentilee().booleanValue() && (getCompteCourant() == null)
                && !APIOperation.SLAVE.equals(getCodeMaster())) {
            _ventiler(tr);
            if (getCodeMaster().equals(APIOperation.MASTER)) {
                return;
            }
        }

        // Déterminer le compte courant à utiliser - (priorité 1 = contrepartie
        // forcée)
        if ((getIdCompteCourant() == null) || JadeStringUtil.isIntegerEmpty(getIdCompteCourant())) {
            // En cas de contrepartie forcée
            if (getContrepartie() != null) {

                // S'il s'agit d'un compte courant
                if (getContrepartie().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                        || getContrepartie().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                    // On recherche le compte courant en question
                    CACompteCourant cc = new CACompteCourant();
                    cc.setSession(getSession());
                    cc.setIdRubrique(getIdContrepartie());
                    cc.setAlternateKey(APICompteCourant.AK_IDRUBRIQUE);
                    try {
                        cc.retrieve(tr);
                        // Si le compte courant est trouvé
                        if (!tr.hasErrors()) {
                            // On affecte le compte courant à l'écriture
                            setIdCompteCourant(cc.getIdCompteCourant());
                        }
                    } catch (Exception e) {
                        _addError(null, e.getMessage());
                    }
                }
            }

        }

        // S'il n'y a toujours pas de compte courant (priorité 2 = compte
        // courant forcé dans la rubrique)
        if ((getIdCompteCourant() == null) || JadeStringUtil.isIntegerEmpty(getIdCompteCourant())) {
            // Si on trouve une contrepartie forcée dans la rubrique, utiliser
            // celle-ci
            if ((getCompte().getIdContrepartie() != null)
                    && !JadeStringUtil.isIntegerEmpty(getCompte().getIdContrepartie())) {
                setIdCompteCourant(getCompte().getIdContrepartie());
            }
        }

        // S'il n'y a toujours pas de compte courant (priorité 3 = paramètre
        // selon le secteur et le type de section)
        if ((getIdCompteCourant() == null) || JadeStringUtil.isIntegerEmpty(getIdCompteCourant())) {

            // Si la rubrique n'est pas ventilée
            // if (!getCompte().getEstVentilee().booleanValue()) {

            // Récupérer le paramètre selon le type de section
            CASecteurTypeSectionManager mgrA = new CASecteurTypeSectionManager();
            mgrA.setSession(getSession());
            mgrA.setForIdSecteur(getCompte().getIdSecteur());
            mgrA.setForIdTypeSection(getSection().getIdTypeSection());
            try {
                mgrA.find(tr);
                // Si le compte courant est trouvé
                if (mgrA.size() > 0) {
                    // On affecte le compte courant à l'écriture
                    setIdCompteCourant(((CASecteurTypeSection) mgrA.getEntity(0)).getIdCompteCourant());
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }

        }

        // S'il n'y a toujours rien, ON N'EST PAS D'ACCORD, on le dit et on sort
        if ((getIdCompteCourant() == null) || JadeStringUtil.isIntegerEmpty(getIdCompteCourant())) {
            getMemoryLog().logMessage("5127", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // S'il n'y a pas d'erreur
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0) {

            // S'il ne s'agit pas d'une écriture master
            if (!getCodeMaster().equals(APIOperation.MASTER)) {
                // Vérifier s'il faut tenir un compteur à jour
                if (getCompte().getTenirCompteur().booleanValue()) {

                    // Instancier un nouveau manager
                    CACompteur compteur;
                    CACompteurManager mgr = new CACompteurManager();
                    mgr.setSession(getSession());

                    // Charger le compteur
                    mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
                    mgr.setForIdRubrique(getIdCompte());
                    mgr.setForAnnee(getAnneeCotisation());
                    try {
                        mgr.findWithoutErrors(tr);

                        if (mgr.size() == 0) {
                            compteur = new CACompteur();
                            compteur.setSession(getSession());
                            compteur.setIdCompteAnnexe(getIdCompteAnnexe());
                            compteur.setIdRubrique(getIdCompte());
                            compteur.setAnnee(getAnneeCotisation());
                        } else {
                            compteur = (CACompteur) mgr.getEntity(0);
                            compteur.setSession(getSession());
                        }

                        // Insérer l'opération
                        getMemoryLog().logMessage(compteur.addMasseAndMontant(this));

                        // S'il n'y a pas d'erreur
                        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0) {

                            // On met à jour l'enregistrement
                            if (compteur.isNew()) {
                                compteur.add(tr);
                            } else {
                                compteur.update(tr);
                            }

                            // Vérifier les erreurs
                            if (hasErrors()) {
                                _addError(null, getSession().getLabel("5022"));
                                return;
                            }
                        }
                    } catch (Exception e) {
                        _addError(null, e.getMessage());
                    }

                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'opération
        if ((getIdTypeOperation().equals(APIOperation.CAECRITURE) || getIdTypeOperation().equals(
                APIOperation.CAECRITURECOMPENSATION))
                && (getCompte() != null) && getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COMPENSATION)) {
            setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
        } else {
            setIdTypeOperation(APIOperation.CAECRITURE);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 10:55:44)
     */
    @Override
    protected void _beforeDesactiver(BTransaction tr) {
        // ignorer les codes master
        if (getCodeMaster().equals(APIOperation.MASTER)) {
            return;
        }

        // Vérifier s'il faut tenir un compteur à jour
        if (getCompte().getTenirCompteur().booleanValue()) {

            // Instancier un nouveau manager
            CACompteur _compteur;
            CACompteurManager _mgr = new CACompteurManager();
            _mgr.setSession(getSession());

            // Charger le compteur
            _mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
            _mgr.setForIdRubrique(getIdCompte());
            _mgr.setForAnnee(getAnneeCotisation());
            try {
                _mgr.findWithoutErrors(tr);

                if (tr.hasErrors() || (_mgr.size() == 0)) {
                    _addError(tr, getSession().getLabel("5022"));
                    return;
                } else {
                    _compteur = (CACompteur) _mgr.getEntity(0);
                    _compteur.setSession(getSession());
                }

                // Supprimer
                getMemoryLog().logMessage(_compteur.removeMasseAndMontant(this));

                // S'il n'y a pas d'erreur
                if (!getMemoryLog().hasErrors()) {

                    // Mettre a jour et vérifier les erreurs
                    _compteur.update(tr);
                    if (tr.hasErrors()) {
                        _addError(tr, getSession().getLabel("5022"));
                        return;
                    }
                }

            } catch (Exception e) {
                _addError(tr, e.getMessage());
                return;
            }
        }

        // Inforom 313
        if (!JadeStringUtil.isBlankOrZero(getSection().getIdPlanRecouvrement())) {
            if (APIRubrique.COMPTE_FINANCIER.equals(getCompte().getNatureRubrique())
                    || APIRubrique.COMPTE_COMPENSATION.equals(getCompte().getNatureRubrique())) {
                if ((new FWCurrency(getMontant())).isNegative()) {
                    try {

                        if (CAPlanRecouvrement.CS_ACTIF
                                .equalsIgnoreCase(getSection().getPlanRecouvrement().getIdEtat())
                                || CAPlanRecouvrement.CS_SOLDE.equalsIgnoreCase(getSection().getPlanRecouvrement()
                                        .getIdEtat())) {
                            CASursisPaiementEcheancier.annulerVentilationPmtEcheance(tr, getMontant(), Boolean.TRUE,
                                    getSection().getIdPlanRecouvrement(), getSection());
                        }

                    } catch (Exception e) {
                        _addError(tr, e.toString());
                    }
                }
            }
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CAEcriture extourne = new CAEcriture();
        extourne.dupliquer(this);
        // Libellé du texte si saisi
        if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
            extourne.setLibelle(text.substring(0, 40));
        } else {
            extourne.setLibelle(text);
        }
        // Inverser le signe
        CAEcriture.inverserCodeDebitCredit(extourne);
        // Inverser le signe de la masse si elle est désynchronisée
        if (extourne.getUnsynchronizeSigneMasse().booleanValue()) {
            FWCurrency cMasseExtourne = new FWCurrency(extourne.getMasse());
            cMasseExtourne.negate();
            extourne.setMasse(cMasseExtourne.toString());
        }
        // Retourner l'opération
        return extourne;
    }

    public void _setDateDelaiValeur(int dateDelaiValeur) {
        delaiDateValeur = new Integer(dateDelaiValeur);
    }

    /**
     * Chargement des valeurs par défaut par utilisateur
     */
    @Override
    public void _synchroChgValUtili() {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector<String>(15);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAEcriture", getNomEcran());
                // chargement des propriétés internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        getMapValeurUtilisateur().put("idExterneRoleEcran", valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        getMapValeurUtilisateur().put("idExterneSectionEcran", valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        getMapValeurUtilisateur().put("date", valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        getMapValeurUtilisateur().put("idExterneRubriqueEcran", valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        getMapValeurUtilisateur().put("anneeCotisation", valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        getMapValeurUtilisateur().put("masseEcran", valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        getMapValeurUtilisateur().put("taux", valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        setCodeDebitCredit(valeurUtilisateur.elementAt(9));
                    }
                    if (valeurUtilisateur.size() >= 11) {
                        getMapValeurUtilisateur().put("libelle", valeurUtilisateur.elementAt(10));
                    }
                    if (valeurUtilisateur.size() >= 12) {
                        getMapValeurUtilisateur().put("piece", valeurUtilisateur.elementAt(11));
                    }
                    if (valeurUtilisateur.size() >= 13) {
                        getMapValeurUtilisateur().put("idCaisseProfessionnelleEcran", valeurUtilisateur.elementAt(12));
                    }
                    if (valeurUtilisateur.size() >= 14) {
                        getMapValeurUtilisateur().put("montantEcran", valeurUtilisateur.elementAt(13));
                    }
                    if (valeurUtilisateur.size() >= 15) {
                        getMapValeurUtilisateur().put("idExterneCompteCourantEcran", valeurUtilisateur.elementAt(14));
                    }
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 13:45:40)
     */
    private void _synchroRubriqueFromEcran(globaz.globall.db.BTransaction transaction) {
        // Si l'id est null, on met à zéro le compte
        if (JadeStringUtil.isBlank(getIdExterneRubriqueEcran())) {
            setIdCompte("");
        } else if ((getCompte() == null) || !getCompte().getIdExterne().equals(getIdExterneRubriqueEcran())) {

            // Instancier un nouveau bean
            CARubrique _rub = new CARubrique();
            _rub.setSession(getSession());
            _rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            _rub.setIdExterne(getIdExterneRubriqueEcran());

            // Lecture
            try {
                _rub.retrieve(transaction);

                // En cas d'erreur, on remet à zéro
                if (_rub.isNew()) {
                    setIdCompte("");
                    _addError(transaction, getSession().getLabel("5115"));
                } else {
                    setIdCompte(_rub.getIdRubrique());
                    rubrique = _rub;
                }

            } catch (Exception e) {
                setIdCompte("");
                return;
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    @Override
    protected void _synchroValUtili() {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector<String>(15);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getDate());
            valeurUtilisateur.add(5, getIdExterneRubriqueEcran());
            valeurUtilisateur.add(6, getAnneeCotisation());
            valeurUtilisateur.add(7, JANumberFormatter.deQuote(getMasseEcran()));
            valeurUtilisateur.add(8, getTaux());
            valeurUtilisateur.add(9, getCodeDebitCredit());
            valeurUtilisateur.add(10, getLibelle());
            valeurUtilisateur.add(11, getPiece());
            valeurUtilisateur.add(12, getCaisseProfessionnelleNumero());
            valeurUtilisateur.add(13, getMontantEcran());
            valeurUtilisateur.add(14, getIdExterneCompteCourantEcran());
            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAEcriture", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Mettre à jour l'indicateur de synchronisation de la masse avec la cotisation lors du chargement
     */
    private void _updateUnsynchronizeSigneMasseOnLoad() {

        FWCurrency cMontant = new FWCurrency(montant);
        FWCurrency cMasse = new FWCurrency(masse);
        if ((cMasse.signum() != 0) && (cMasse.signum() != cMontant.signum())) {
            setUnsynchronizeSigneMasse(Boolean.TRUE);
        }
    }

    /**
     * Date de création : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            _addError(statement.getTransaction(), getSession().getLabel("5163"));
        }

        if ((getCompteAnnexe() != null) && getCompteAnnexe().isCompteAuxiliaire()) {
            _addError(statement.getTransaction(), getSession().getLabel(CAEcriture.LABEL_COMPTE_ANNEXE_AUXILIAIRE));
        }

        // Force la remise à vide de l'id section auxiliaire si
        // l'idExterneSectionAux saisi depuis l'écran est vide
        if (getSaisieEcran().booleanValue() && isUpdatable() && JadeStringUtil.isBlank(getIdExterneSectionAuxEcran())) {
            setIdSectionAux("");
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdSectionAux())) {
            CASection sectionAux = new CASection();
            sectionAux.setSession(getSession());
            sectionAux.setIdSection(getIdSectionAux());
            try {
                sectionAux.retrieve();

                if (sectionAux.isNew()) {
                    _addError(statement.getTransaction(),
                            getSession().getLabel(CAEcriture.LABEL_SECTION_AUX_INEXISTANTE));
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
                JadeLogger.error(this, e);
            }
        }
    }

    /**
     * Validation des données Date de création : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les données de la superclasse
        super._valider(transaction);

        // Récupérer la rubrique en provenance de l'écran
        if (getSaisieEcran().booleanValue()) {
            _synchroRubriqueFromEcran(transaction);
        }

        // Charger montant écran
        if (((montantEcran != null) && (montantEcran.trim().length() > 0))
                || ((masseEcran != null) && (masseEcran.trim().length() > 0))) {
            setMontant(JANumberFormatter.deQuote(montantEcran));

            // Charger masse écran
            if (masseEcran != null) {
                setMasse(JANumberFormatter.deQuote(masseEcran));
            }

            double _mont = 0.0;
            double _masse = 0.0;
            try {
                _mont = Double.parseDouble(JANumberFormatter.deQuote(montantEcran));
            } catch (Exception e) {
            }
            try {
                _masse = Double.parseDouble(JANumberFormatter.deQuote(masseEcran));
            } catch (Exception e) {
            }

            // Récupérer la masse si montant nul
            if (_mont == 0.0) {
                _mont = _masse;
            }

            if (_mont < 0.0) {
                if (codeDebitCreditEcran.equals("D")) {
                    setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
                } else {
                    setCodeDebitCredit(APIEcriture.EXTOURNE_CREDIT);
                }
            } else if (codeDebitCreditEcran.equals("D")) {
                setCodeDebitCredit(APIEcriture.DEBIT);
            } else {
                setCodeDebitCredit(APIEcriture.CREDIT);
            }

            // Remise à blanc
            codeDebitCreditEcran = null;
            masseEcran = null;
            montantEcran = null;
        }

        // Vérifier le compte
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            getMemoryLog().logMessage("5114", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            // Vérifier l'existence du compte
            if (getCompte() == null) {
                getMemoryLog().logMessage("5115", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
            } else {

                // Récupérer la masse, le taux et le montant en valeur double
                double fMasse = 0.0;
                double fMontant = 0.0;
                double fTaux = 0.0;
                try {
                    fMasse = Double.parseDouble(getMasse());
                } catch (Exception ex) {
                }
                try {
                    fMontant = Double.parseDouble(getMontant());
                } catch (Exception ex) {
                }
                try {
                    fTaux = Double.parseDouble(getTaux());
                } catch (Exception ex) {
                }

                // Vérifier le signe masse et montant (accepter si conversion
                // automatique déclenchée)
                if (((fMasse < 0.0) && (fMontant > 0.0)) || ((fMasse > 0.0) && (fMontant < 0.0))) {
                    if (!unsynchronizeSigneMasse.booleanValue()) {
                        getMemoryLog().logMessage("5175", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }
                // Les comptes courants ne sont pas autorisés
                if (getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)
                        || getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)) {
                    getMemoryLog().logMessage("5173", null, FWMessage.ERREUR, this.getClass().getName());
                }

                // Vérifier la masse pour compte avec masse
                if (getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE) && (fMasse == 0.0)
                        && ((fMontant < -1.0) || (fMontant > 1.0))) {
                    if ((getSection() == null)
                            || !(getSection().getCategorieSection().equals(
                                    APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL)
                                    || getSection().getCategorieSection().equals(
                                            APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE) || getSection()
                                    .getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_LTN))) {
                        getMemoryLog().logMessage("5118", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }

                // Vérifier la masse pour compte sans masse
                if (!getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE) && (fMasse != 0.0)) {
                    getMemoryLog().logMessage("5119", null, FWMessage.ERREUR, this.getClass().getName());
                }

                // Vérifier l'année de cotisation pour compte de cotisation
                if (getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                        || getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)) {
                    if (JadeStringUtil.isIntegerEmpty(getAnneeCotisation())) {
                        getMemoryLog().logMessage("5120", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }

                // Vérifier l'année de cotisation pour autres types de comptes
                if (!getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.AMORTISSEMENT)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.RECOUVREMENT)) {

                    if (!JadeStringUtil.isIntegerEmpty(getAnneeCotisation())) {
                        getMemoryLog().logMessage("5121", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }

                // Erreur si taux manque et compte avec masse
                if (getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE) && (fTaux == 0.0)) {
                    getMemoryLog().logMessage("5122", null, FWMessage.ERREUR, this.getClass().getName());
                }

                // Erreur si taux et autres types de comptes
                if (!getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE) && (fTaux != 0.0)) {
                    getMemoryLog().logMessage("5123", null, FWMessage.ERREUR, this.getClass().getName());
                }

                // Montant obligatoire si pas compte de cotisation
                if (!getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.AMORTISSEMENT)
                        && !getCompte().getNatureRubrique().equals(APIRubrique.RECOUVREMENT)) {
                    if (fMontant == 0.0) {
                        getMemoryLog().logMessage("5129", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }

                // Vérifier l'année de cotisation
                if (getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                        || getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)
                        || getCompte().getNatureRubrique().equals(APIRubrique.AMORTISSEMENT)
                        || getCompte().getNatureRubrique().equals(APIRubrique.RECOUVREMENT)) {
                    try {
                        BigDecimal _annee = new BigDecimal(getAnneeCotisation());
                        JAUtil.checkNumberPositiveOrZero(_annee);
                        if (_annee.intValue() < 1900) {
                            getMemoryLog().logMessage("5130", getAnneeCotisation(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }
                    } catch (Exception e) {
                        getMemoryLog().logMessage("5130", getAnneeCotisation(), FWMessage.ERREUR,
                                this.getClass().getName());
                    }
                }

                // Montant obligatoire s'il s'agit d'une rubrique de paiement /
                // compensation
                if (getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_FINANCIER)
                        || getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COMPENSATION)) {
                    if (fMontant == 0.0) {
                        getMemoryLog().logMessage("5129", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }

                // Vérifier que le compte courant forcé correspond à la rubrique
                // si cette
                // dernière a une contrepartie forcée. Test à faire uniquement
                // pour les débits ou crédits (ne pas faire lors d'extourne)
                if ((getCompteCourant() != null)
                        && (getCodeDebitCredit().equals(APIEcriture.DEBIT) || getCodeDebitCredit().equals(
                                APIEcriture.CREDIT))) {
                    if (!JadeStringUtil.isIntegerEmpty(getCompte().getIdContrepartie())
                            && !getCompte().getIdContrepartie().equals(getIdCompteCourant())
                            && !getCompte().getEstVentilee().booleanValue()) {
                        getMemoryLog().logMessage("5161", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }
            }
        }

        // Vérifier la contrepartie si saisie
        if (!JadeStringUtil.isIntegerEmpty(getIdContrepartie())) {
            if (getContrepartie() == null) {
                getMemoryLog().logMessage("5116", getIdContrepartie(), FWMessage.ERREUR, this.getClass().getName());
            } else {

                // Avertissement si la contrepartie n'est pas un compte courant
                if (!getContrepartie().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                        && !getContrepartie().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                    getMemoryLog().logMessage("5117", getIdContrepartie(), FWMessage.AVERTISSEMENT,
                            this.getClass().getName());
                }
            }
        }

        // Vérifier le code débit / crédit
        if (!getCodeDebitCredit().equals(APIEcriture.DEBIT) && !getCodeDebitCredit().equals(APIEcriture.CREDIT)
                && !getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)
                && !getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
            getMemoryLog().logMessage("5117", getCodeDebitCredit(), FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier la section
        if (!isNewSection()) {
            if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
                getMemoryLog().logMessage("5125", null, FWMessage.ERREUR, this.getClass().getName());
            } else
            // Vérifier l'existence de la section
            if (getSection() == null) {
                getMemoryLog().logMessage("5126", getIdSection(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // Vérifie la date
        try {
            int delai = (delaiDateValeur == null) ? CAParametres.getDelaiDateValeur(transaction) : delaiDateValeur
                    .intValue();

            JACalendar cal = new JACalendarGregorian();
            // Détermine la date limite : date du jour + 14j.
            JADate dateLimite = cal.addDays(JACalendar.today(), delai);
            JADate dateEcriture = new JADate(getDate());

            // SI dateEcriture > dateLimite ALORS erreur
            if (cal.compare(dateEcriture, dateLimite) == JACalendar.COMPARE_FIRSTUPPER) {
                _addError(transaction, getSession().getLabel("ERR_DELAI_DATE_VALEUR"));
            }
        } catch (JAException e) {
            _addError(transaction, getSession().getLabel("ERR_DELAI_DATE_VALEUR"));
        }

        // S'il n'y a pas d'erreur, on effectue certaines transformations
        if (!getMemoryLog().hasErrors()) {

            boolean isJournalTypeFacturation = false;
            if (getJournal() != null) {
                isJournalTypeFacturation = CAJournal.TYPE_FACTURATION.equalsIgnoreCase(getJournal().getTypeJournal());
            }

            // Calcul de la cotisation le cas échéant
            if (!isJournalTypeFacturation && JadeStringUtil.isDecimalEmpty(getMontant())
                    && getCompte().getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)) {

                // Calculer la cotisation
                double _cotisation = 0.0, _masse, _taux;
                try {
                    _masse = Double.parseDouble(getMasse());
                    _taux = Double.parseDouble(getTaux());
                    _cotisation = (_masse * _taux) / 100;
                } catch (NumberFormatException e) {
                }
                // Arrondir à 5 ct.
                setMontant(JANumberFormatter.formatNoQuote(_cotisation));
            }
        }

        // Gestion de la caisse professionnelle
        if (getCompte() != null) {
            if (getCompte().isUseCaissesProf()) {
                if (JadeStringUtil.isIntegerEmpty(getSection().getIdCaisseProfessionnelle())
                        && JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
                    _addError(transaction, getSession().getLabel("CAISSE_PROF_NON_RENSEIGNEE"));
                }
            } else {
                if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
                    _addError(transaction, getSession().getLabel("CAISSE_PROF_RENSEIGNEE"));
                }
            }
        }
    }

    /**
     * 
     * Ventilation d'une écriture Date de création : (30.01.2002 16:55:10)
     * 
     * <pre>
     * 1. Nous parcourons les comptes courants afin de connaitre le solde total ventilable 
     * 2. Nous parcourons à nouveau les mêmes comptes courants afin de :
     * 2.1. Créer des écritures dans les comptes courants selon le prorata de leurs soldes et le solde total
     * 2.2. Ajouter directement ces nouvelles écritures dans un groupement
     * </pre>
     * 
     * @param transaction
     *            La transaction en cours
     */
    protected void _ventiler(BTransaction transaction) {
        try {

            // Initialiser
            FWCurrency totalVentilable;
            FWCurrency totalVentilé;
            FWCurrency soldeCompteCourant;
            FWCurrency grandTotalVentilé = new FWCurrency();
            FWCurrency montantAVentiler = new FWCurrency();
            FWCurrency totalAVentiler = new FWCurrency(getMontant());
            FWCurrency soldeAVentiler = new FWCurrency(getMontant());
            String sPriorite = "0";
            String sProchainePriorite = "0";
            boolean hasMultiplePriority = false;
            CAGroupement grp = null;

            // Ne pas traiter les écritures slave
            if (APIOperation.SLAVE.equals(getCodeMaster())) {
                return;
            }
            // Transformer l'écriture actuelle en code MASTER
            setCodeMaster(APIOperation.MASTER);

            // Récupérer le solde par compte courant de la section concernée
            CAJoinCompteCourantOperationManager manager = new CAJoinCompteCourantOperationManager();
            manager.setSession(getSession());
            manager.setForIdSection(getSection().getIdSection());
            manager.find();

            // Si aucun secteur, la rubrique n'est pas ventilable
            if (manager.size() == 0) {
                setCodeMaster(APIOperation.SINGLE);
                return;
            }

            // Boucle de traitement par niveau de priorite (avec break de
            // sortie)
            while (true) {
                // Initialiser
                totalVentilable = new FWCurrency();
                totalVentilé = new FWCurrency();

                // Parcourir les comptes courants
                for (int i = 0; i < manager.size(); i++) {
                    CAJoinCompteCourantOperation joinEntity = (CAJoinCompteCourantOperation) manager.get(i);

                    // Si le compte courant peut être ventilé, on totalise
                    if (joinEntity.getVentilationAccepter().booleanValue()) {
                        soldeCompteCourant = new FWCurrency(joinEntity.getMontant());

                        if (joinEntity.getPriorite().equals(sPriorite)) {
                            if (soldeCompteCourant.signum() != soldeAVentiler.signum()) {
                                totalVentilable.add(soldeCompteCourant);
                            }
                        }

                        // Mémoriser prochaine priorité si supérieure
                        if (joinEntity.getPriorite().compareTo(sPriorite) > 0) {
                            if (sPriorite.equals(sProchainePriorite)
                                    || (joinEntity.getPriorite().compareTo(sProchainePriorite) <= 0)) {
                                sProchainePriorite = joinEntity.getPriorite();
                                hasMultiplePriority = true;
                            }
                        }
                    }
                }

                // S'il y a qqch à ventiler
                if (!totalVentilable.isZero()) {
                    // En cas de priorités multiples, vérifier si le solde à
                    // ventiler dépasse le total ventilable
                    boolean resteQqchAVentiler = false;
                    if (hasMultiplePriority && !sPriorite.equals(sProchainePriorite)) {
                        FWCurrency tmpSoldeAVentiler = new FWCurrency(soldeAVentiler.doubleValue());
                        tmpSoldeAVentiler.abs();
                        FWCurrency tmpTotalVentilable = new FWCurrency(totalVentilable.doubleValue());
                        tmpTotalVentilable.abs();
                        if (tmpSoldeAVentiler.compareTo(tmpTotalVentilable) > 0) {
                            resteQqchAVentiler = true;
                        }
                    }

                    // Parcourir les comptes courants
                    for (int i = 0; i < manager.size(); i++) {
                        CAJoinCompteCourantOperation joinEntity = (CAJoinCompteCourantOperation) manager.get(i);

                        // Si le compte courant peut être ventilé, on totalise
                        if (joinEntity.getVentilationAccepter().booleanValue()
                                && joinEntity.getPriorite().equals(sPriorite)) {
                            // Solde du compte courant
                            soldeCompteCourant = new FWCurrency(joinEntity.getMontant());

                            // S'il y a qqch à ventiler
                            if (!soldeCompteCourant.isZero()) {
                                // Vérifier si les montants sont de signe
                                // inversé
                                if (soldeCompteCourant.signum() != soldeAVentiler.signum()) {
                                    // Déterminer le montant à ventiler
                                    double fSoldeAVentiler = soldeAVentiler.doubleValue();
                                    // Plafonner si un plusieurs niveaux sont
                                    // détectés
                                    if (hasMultiplePriority) {
                                        if (Math.abs(fSoldeAVentiler) > Math.abs(totalVentilable.doubleValue())) {
                                            fSoldeAVentiler = Math.abs(totalVentilable.doubleValue());
                                            if (soldeAVentiler.isNegative()) {
                                                fSoldeAVentiler = -fSoldeAVentiler;
                                            }
                                        }
                                    }
                                    // Calculer le prorata à 5ct
                                    double fMontant = (fSoldeAVentiler * soldeCompteCourant.doubleValue())
                                            / totalVentilable.doubleValue();
                                    montantAVentiler = new FWCurrency(JANumberFormatter.formatNoQuote(fMontant));

                                    FWCurrency montantAControler = new FWCurrency(fMontant);
                                    montantAControler.abs();

                                    // Totaliser
                                    // BZ 6982 - Pour que les soldes en dessous de 3 centimes passent également dans le
                                    // traitement, vu que FORMATNOQUOTE arrondi une solde minime (0.01 -> 0.02) à 0.-
                                    if (!montantAVentiler.isZero() || (montantAControler.doubleValue() < 0.03)) {
                                        totalVentilé.add(montantAVentiler);
                                        grandTotalVentilé.add(montantAVentiler);

                                        // S'il reste +/- 20ct, on les attribue
                                        // à la dernière ventilation si dernière
                                        // priorité
                                        // TODO Bug 5410 : Revoir l'attribution des 20 centimes restants
                                        double fResteAVentiler = fSoldeAVentiler - totalVentilé.doubleValue();

                                        FWCurrency montantResteAventiler = new FWCurrency(fResteAVentiler);
                                        montantResteAventiler.abs();

                                        if ((montantResteAventiler.compareTo(new FWCurrency(0.20)) <= 0)
                                                && !resteQqchAVentiler) {
                                            montantAVentiler.add(fResteAVentiler);
                                            totalVentilé.add(fResteAVentiler);
                                            grandTotalVentilé.add(fResteAVentiler);
                                        }

                                        // Créer une écriture slave et y passer
                                        // les attributs

                                        if (!montantAVentiler.isZero()) {
                                            APISlave ecr = createSlave();
                                            ecr.setISession(getISession());
                                            ecr.setCodeMaster(APIOperation.SLAVE);
                                            ecr.setMontant(montantAVentiler.toString());
                                            ecr.setIdCompteCourant(joinEntity.getIdCompteCourant());
                                            ecr.setCodeDebitCredit(getCodeDebitCredit());
                                            ecr.setIdJournal(getIdJournal());
                                            ecr.setProvenancePmt(getProvenancePmt());
                                            ecr.activer(transaction);
                                            ecr.setIdSectionCompensation(getIdSectionCompensation());
                                            ecr.setSectionCompensationDeSur(getSectionCompensationDeSur());

                                            ecr.add(transaction, true);

                                            // Vérifier les erreurs
                                            if (transaction.hasErrors()) {
                                                _addError(transaction, getSession().getLabel("5033"));
                                                return;
                                            }

                                            // Création d'un groupement si
                                            // nécessaire
                                            if (grp == null) {
                                                if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection()
                                                        .getIdTypeSection())) {
                                                    grp = getGroupement(transaction);

                                                    if (grp == null) {
                                                        grp = createGroupement(transaction);
                                                    }
                                                } else {
                                                    grp = createGroupement(transaction);
                                                }
                                            }

                                            addEcritureToGroupement(transaction, grp, ecr);

                                            // Mise à jour du solde
                                            // CompteCourant/Section (uniquement
                                            // en mémoire...)
                                            soldeCompteCourant.add(montantAVentiler);
                                        }
                                    }
                                }
                            }

                        }

                        // Sortir s'il n'y plus rien à ventiler
                        if (grandTotalVentilé.equals(totalAVentiler)) {
                            break;
                        }
                    }

                    // Mettre a jour le solde à ventiler
                    soldeAVentiler.sub(totalVentilé);

                    // Sortir si plus rien a ventiler
                    if (soldeAVentiler.isZero()) {
                        break;
                    }

                    // Sortir si pas de prochain niveau de priorité
                    if (sProchainePriorite.equals(sPriorite)) {
                        break;
                    } else {
                        sPriorite = sProchainePriorite;
                    }

                } else {

                    // Sortir si pas de prochain niveau de priorité
                    if (sProchainePriorite.equals(sPriorite)) {
                        break;
                    } else {
                        sPriorite = sProchainePriorite;
                    }
                }
            }

            // Erreur si trop ventilé
            double fGrandTotalVentilé = grandTotalVentilé.doubleValue();
            double fTotalAVentiler = totalAVentiler.doubleValue();
            if (!grandTotalVentilé.isZero() && (Math.abs(fGrandTotalVentilé) > Math.abs(fTotalAVentiler))) {
                _addError(transaction, getSession().getLabel("5034"));
                return;
            }

            // S'il reste qqch à ventiler
            if (!grandTotalVentilé.equals(totalAVentiler)) {

                // Si on n'a rien ventilé, l'écriture redevient single sauf si
                // slave
                if (grandTotalVentilé.isZero() || soldeAVentiler.isZero()) {
                    if (!APIOperation.SLAVE.equals(getCodeMaster())) {
                        setCodeMaster(APIOperation.SINGLE);
                    }

                    // Sinon, on impute le solde sur le compte courant par
                    // défaut
                } else {

                    APISlave ecr = createSlave();
                    ecr.setISession(getISession());
                    ecr.setCodeMaster(APIOperation.SLAVE);
                    ecr.setMontant(soldeAVentiler.toString());
                    ecr.setIdCompteCourant(getCompte().getIdContrepartie());
                    ecr.setCodeDebitCredit(getCodeDebitCredit());
                    ecr.setIdJournal(getIdJournal());
                    ecr.setProvenancePmt(getProvenancePmt());
                    ecr.activer(transaction);
                    ecr.add(transaction, true);

                    // Vérifier les erreurs
                    if (transaction.hasErrors()) {
                        _addError(transaction, getSession().getLabel("5033"));
                        return;
                    }

                    addEcritureToGroupement(transaction, grp, ecr);
                }
            }

            // Traiement des erreurs
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.03.2002 09:29:02)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField(CAOperation.FIELD_ANNEECOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField(CAOperation.FIELD_CODEDEBITCREDIT,
                this._dbWriteString(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
        statement.writeField(CAOperation.FIELD_IDCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(CAOperation.FIELD_IDCONTREPARTIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdContrepartie(), "idContrepartie"));
        statement.writeField(CAOperation.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(CAOperation.FIELD_MASSE,
                this._dbWriteNumeric(statement.getTransaction(), getMasse(), "masse"));
        statement.writeField(CAOperation.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(CAOperation.FIELD_NOECRCOL,
                this._dbWriteNumeric(statement.getTransaction(), getNoEcritureCollective(), "noEcrCol"));
        statement.writeField(CAOperation.FIELD_PIECE,
                this._dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField(CAOperation.FIELD_TAUX,
                this._dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField(CAOperation.FIELD_IDECHEANCEPLAN,
                this._dbWriteNumeric(statement.getTransaction(), getIdEcheancePlan(), "idEcheancePlan"));
        statement.writeField(CAOperation.FIELD_IDSECTIONAUX,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionAux(), "idSectionAux"));
        statement.writeField(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE, this._dbWriteNumeric(
                statement.getTransaction(), getIdCaisseProfessionnelle(), "idCaisseProfessionnelle"));
        statement.writeField(CAOperation.FIELD_PROVENANCEPMT,
                this._dbWriteNumeric(statement.getTransaction(), getProvenancePmt(), "provenancePmt"));
    }

    /**
     * Attache une écriture générée au groupement.
     * 
     * @param transaction
     * @param grp
     * @param ecr
     * @throws Exception
     */
    public void addEcritureToGroupement(BTransaction transaction, CAGroupement grp, APISlave ecr) throws Exception {
        CAGroupementOperation grpOper = new CAGroupementOperation();
        grpOper.setSession(getSession());
        grpOper.setIdGroupement(grp.getIdGroupement());
        grpOper.setIdOperation(ecr.getIdOperation());
        grpOper.add(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(getSession().getLabel("5036"));
        }

        if (grpOper.isNew()) {
            throw new Exception(getSession().getLabel("5036"));
        }
    }

    /**
     * Créer un nouveau groupement pour l'écriture.
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    public CAGroupement createGroupement(BTransaction transaction) throws Exception {
        CAGroupement grp = new CAGroupement();
        grp.setSession(getSession());
        grp.setTypeGroupement(CAGroupement.MASTER);
        grp.setIdOperationMaster(getIdOperation());
        grp.add(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(getSession().getLabel("5035"));
        }

        if (grp.isNew()) {
            throw new Exception(getSession().getLabel("IMPOSSIBLE_CREER_GROUPEMENT"));
        }

        return grp;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:38:57)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntSlave
     */
    protected APISlave createSlave() {
        CAEcriture ecr = new CAEcriture();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:40:36)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAEcriture
     */
    public void dupliquer(CAEcriture oper) {

        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Copier les autres paramètres
        if (oper != null) {
            setAnneeCotisation(oper.getAnneeCotisation());
            setCodeDebitCredit(oper.getCodeDebitCredit());
            setIdCompte(oper.getIdCompte());
            setIdContrepartie(oper.getIdContrepartie());
            setLibelle(oper.getLibelle());
            setMasse(oper.getMasse());
            setMontant(oper.getMontant());
            setPiece(oper.getPiece());
            setTaux(oper.getTaux());
        }

        // Mettre à jour l'indication de synchronisation du signe de la masse
        // avec la cotisation
        _updateUnsynchronizeSigneMasseOnLoad();

    }

    @Override
    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    /**
     * Return le numéro de la caisse professionnelle.
     * 
     * @return Le numéro. Si id vide => return "".
     */
    public String getCaisseProfessionnelleNumero() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationNumero(pyxisSession, getIdCaisseProfessionnelle());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:02:32)
     * 
     * @return Boolean
     */
    public String getCodeDebitCreditEcran() {

        // Si pas encore chargé
        if (codeDebitCreditEcran == null) {
            if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                    || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                codeDebitCreditEcran = "D";
            } else {
                codeDebitCreditEcran = "C";
            }
        }

        return codeDebitCreditEcran;
    }

    /**
     * Retourne le code débit / crédit pour l'interface de comptabilité générale Date de création : (28.10.2002
     * 10:54:56)
     * 
     * @return String Le code débit / crédit au format de l'interface de comptabilité générale
     */
    public String getCodeDebitCreditPourCG() {
        String sDC = getCodeDebitCredit();
        if (sDC.equals(APIEcriture.DEBIT)) {
            return IntEcritureDouble.CODE_DEBIT;
        } else if (sDC.equals(APIEcriture.CREDIT)) {
            return IntEcritureDouble.CODE_CREDIT;
        } else if (sDC.equals(APIEcriture.EXTOURNE_DEBIT)) {
            return IntEcritureDouble.CODE_EXTOURNE_DEBIT;
        } else {
            return IntEcritureDouble.CODE_EXTOURNE_CREDIT;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 07:44:40)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    @Override
    public CARubrique getCompte() {
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            return null;
        }
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdCompte());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }

        return rubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 07:49:45)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public CARubrique getContrepartie() {
        if (JadeStringUtil.isIntegerEmpty(getIdContrepartie())) {
            return null;
        }
        if (contrepartie == null) {
            contrepartie = new CARubrique();
            contrepartie.setISession(getSession());
            contrepartie.setIdRubrique(getIdContrepartie());
            try {
                contrepartie.retrieve();
                if (contrepartie.isNew()) {
                    contrepartie = null;
                }

            } catch (Exception e) {
                contrepartie = null;
            }
        }

        return contrepartie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2002 13:00:19)
     * 
     * @return String
     * @param codeISOL
     *            String
     */
    @Override
    public String getDescription(String codeISOLangue) {
        String s = new String();
        if (getCompte() != null) {
            s = getCompte().getDescription(codeISOLangue);
        }
        return s;
    }

    /**
     * Return le groupement précédement créé. Utile pour un groupement commun à la ventilation et la création des
     * écritures bulletins neutres.
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    private CAGroupement getGroupement(BTransaction transaction) throws Exception {
        CAGroupementManager manager = new CAGroupementManager();
        manager.setSession(getSession());
        manager.setForIdOperationMaster(getIdOperation());
        manager.setForTypeGroupement(CAGroupement.MASTER);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (manager.isEmpty()) {
            return null;
        } else {
            return (CAGroupement) manager.getFirstEntity();
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2002 18:32:03)
     * 
     * @return String
     */
    @Override
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 15:55:06)
     * 
     * @return String
     */
    public String getIdCompteEcran() {
        return idCompteEcran;
    }

    @Override
    public String getIdContrepartie() {
        return idContrepartie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 13:44:59)
     * 
     * @return String
     */
    public String getIdExterneRubriqueEcran() {
        if (loadIdExterneRubriqueEcran) {
            loadIdExterneRubriqueEcran = false;
            if (getCompte() != null) {
                idExterneRubriqueEcran = getCompte().getIdExterne();
            }
        } else if (rechercheRubriqueEcran.booleanValue() && (getCompte() != null)) {
            idExterneRubriqueEcran = getCompte().getIdExterne();
        }

        return idExterneRubriqueEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:20:22)
     * 
     * @return String
     */
    public String getImAnneeCotisation() {
        return getAnneeCotisation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:19:22)
     * 
     * @return globaz.osiris.api.APICompteCourant
     */
    public APICompteCourant getImCompteCourant() {
        return getCompteCourant();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:20:04)
     * 
     * @return String
     */
    public String getImDate() {
        return getDate();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:19:45)
     * 
     * @return String
     */
    public String getImMontant() {
        return getMontant();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:20:55)
     * 
     * @return globaz.osiris.api.APIRubrique
     */
    public APIRubrique getImRubrique(BITransaction transaction) {
        return getCompte();
    }

    @Override
    public String getLibelle() {

        return libelle;
    }

    /**
     * Cette méthode retourne le libellé de l'écriture. S'il n'y a pas de libellé on récupère la description de la
     * rubrique et on ajoute l'année de cotisation s'il y en a une
     */
    public String getLibelleDescription() {
        String sDescription = new String();
        if (getLibelle().length() != 0) {
            sDescription = getLibelle();
        } else {
            sDescription = getCompte().getDescription();
        }
        return sDescription;
    }

    @Override
    public String getMasse() {
        // Bug 6486
        if (masse == null) {
            return null;
        }

        double _masse = 0.0;
        String _masseSignée = JANumberFormatter.deQuote(masse);

        // Si le code débit crédit est défini
        if ((getCodeDebitCredit() != null) && !unsynchronizeSigneMasse.booleanValue()) {
            try {
                _masse = Double.parseDouble(JANumberFormatter.deQuote(masse));
                // Signe négatif (crédit, extourne débit)
                if (getCodeDebitCredit().equals(APIEcriture.CREDIT)
                        || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                    _masse = -Math.abs(_masse);
                    // Signe positif (débit, extoure crédit)
                } else {
                    _masse = Math.abs(_masse);
                }
                _masseSignée = JANumberFormatter.formatNoRound(String.valueOf(_masse), 2);
            } catch (NumberFormatException e) {
            }
        }

        // Retourner le masse
        return JANumberFormatter.deQuote(_masseSignée);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:07:15)
     * 
     * @return String
     */
    public String getMasseEcran() {

        // Si masse pas encore chargée
        if (masseEcran == null) {

            double _masse = 0.0;
            masseEcran = JANumberFormatter.deQuote(masse);

            // Si le code débit crédit est défini
            if ((getCodeDebitCredit() != null) && !unsynchronizeSigneMasse.booleanValue()) {
                try {
                    _masse = Double.parseDouble(JANumberFormatter.deQuote(masse));
                    // Signe positif (DEBIT, CREDIT)
                    if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                            || getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
                        _masse = Math.abs(_masse);
                        // Signe négatif (extourne débit, extoure crédit)
                    } else {
                        _masse = -Math.abs(_masse);
                    }
                    masseEcran = JANumberFormatter.formatNoRound(String.valueOf(_masse), 2);
                } catch (NumberFormatException e) {
                }
            }
        }

        masseEcran = JANumberFormatter.deQuote(masseEcran);

        if (masseEcran.equals("0.00")) {
            masseEcran = "";
        } else {
            // Indiquer le signe en clair (+) si contrôle de masse désactive
            if (unsynchronizeSigneMasse.booleanValue() && !masseEcran.startsWith("-") && !masseEcran.startsWith("+")) {
                masseEcran = "+" + masseEcran;
            }
        }

        return masseEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 16:11:54)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMasseToCurrency() {
        return new FWCurrency(getMasse());
    }

    /**
     * Restitue le montant signé en fonction du code débit/crédit Date de création : (30.01.2002 07:36:20)
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        String montantSigne = JANumberFormatter.deQuote(montant);

        // Si le code débit crédit est défini
        if (getCodeDebitCredit() != null) {
            try {
                double montantTemp = Double.parseDouble(montantSigne);

                if (getCodeDebitCredit().equals(APIEcriture.CREDIT)
                        || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                    // Signe négatif (crédit, extourne débit)
                    montantTemp = -Math.abs(montantTemp);
                } else {
                    // Signe positif (débit, extoure crédit)
                    montantTemp = Math.abs(montantTemp);
                }

                montantSigne = JANumberFormatter.formatNoRound(String.valueOf(montantTemp), 2);
            } catch (NumberFormatException e) {
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantSigne);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:04:36)
     * 
     * @return String
     */
    public String getMontantEcran() {

        // Si montant écran pas encore chargé
        if (montantEcran == null) {
            double _montant = 0.0;
            montantEcran = montant;

            // Si le code débit crédit est défini
            if (getCodeDebitCredit() != null) {
                try {
                    _montant = Double.parseDouble(JANumberFormatter.deQuote(montant));
                    // Signe positif (DEBIT, CREDIT)
                    if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                            || getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
                        _montant = Math.abs(_montant);
                        // Signe négatif (extourne débit, extoure crédit)
                    } else {
                        _montant = -Math.abs(_montant);
                    }
                    montantEcran = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
                } catch (NumberFormatException e) {
                }
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantEcran);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 08:51:38)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantToCurrency() {
        return new FWCurrency(getMontant());
    }

    @Override
    public String getNoEcritureCollective() {
        return noEcritureCollective;
    }

    @Override
    public String getPiece() {

        return piece;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2002 16:24:25)
     * 
     * @return Boolean
     */
    public Boolean getRechercheRubriqueEcran() {
        return rechercheRubriqueEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.04.2003 10:57:55)
     * 
     * @return String
     */
    public String getSigneCodeDebitCredit() {
        if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
            if (getCodeDebitCreditEcran().equalsIgnoreCase("D")) {
                return "S";
            } else {
                return "H";
            }
        } else {
            if (getCodeDebitCreditEcran().equalsIgnoreCase("D")) {
                return "D";
            } else {
                return "C";
            }
        }
    }

    @Override
    public String getTaux() {

        return JANumberFormatter.deQuote(taux);
    }

    /**
     * Retourne l'état de la conversion du signe de la masse
     * 
     * @see globaz.osiris.api.APIEcriture#getSigneMasseInverseCotisation()
     */
    @Override
    public Boolean getUnsynchronizeSigneMasse() {
        return unsynchronizeSigneMasse;
    }

    private void groupWithEcritureAux(BTransaction transaction, String idOperationAux) throws Exception {
        CAGroupement grp = new CAGroupement();
        grp.setSession(getSession());
        grp.setTypeGroupement(CAGroupement.MASTER);
        grp.setIdOperationMaster(getIdOperation());
        grp.add(transaction);

        if (grp.hasErrors()) {
            throw new Exception("Problème lors de la création du groupement MASTER");
        }

        CAGroupementOperation grpOper = new CAGroupementOperation();
        grpOper.setSession(getSession());
        grpOper.setIdGroupement(grp.getIdGroupement());
        grpOper.setIdOperation(idOperationAux);
        grpOper.add(transaction);

        if (grpOper.hasErrors()) {
            throw new Exception("Problème lors de la création du groupement SLAVE");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 17:18:58)
     * 
     * @return boolean
     */
    public boolean isImForcerNonSoumis() {
        // Si non actif, pas soumis
        if (!getEstActive().booleanValue()) {
            return true;
            // Sinon, soumis
        } else {
            return false;
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#isOperationExtournable()
     */
    @Override
    public boolean isOperationExtournable() {
        CARubrique rub = getCompte();
        // On n'extourne pas les compensations
        if ((rub != null)
                && (rub.getNatureRubrique().equals(APIRubrique.COMPTE_COMPENSATION) || rub.getNatureRubrique().equals(
                        APIRubrique.COMPTE_FINANCIER))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Met à jour la date de déclenchement du dossier contentieux de la section. date du paiement + param DELAIREP
     * 
     * @param transaction
     */
    private void reportDateDeclenchementContentieux(BTransaction transaction) {
        // Recherche du contentieux correspondant à la section
        COContentieuxManager contentieuxManager = new COContentieuxManager();
        contentieuxManager.setSession(getSession());
        contentieuxManager.setForIdSection(getIdSection());
        try {
            contentieuxManager.find(transaction);
            if (contentieuxManager.size() == 0) {
                // Pas de contentieux pas de report de délai.
                // Le warning cause une boucle sans fin et ne sert à rien.
                // this._addWarning(transaction, "Le contentieux pour la section " + this.getIdSection()
                // + " est introuvable. La date de déclenchement n'a pas été mise à jour !");
            } else if (contentieuxManager.size() == 1) {
                COContentieux contentieux = (COContentieux) contentieuxManager.get(0);

                // Récupération du paramètre de report
                String reportParam;
                try {
                    reportParam = FWFindParameter.findParameter(transaction, "0", "DELAIREP", "0", "0", 0);
                } catch (Exception e) {
                    // Si le paramètre n'est pas définit on utilise la valeur par défaut
                    reportParam = CAEcriture.DELAI_DECLENCHEMENT_DEFAUT;
                }

                // Calcul de la nouvelle date de déclenchement
                Integer reportParamInt = Integer.parseInt(reportParam);

                if (reportParamInt > 0) {
                    JADate datePaiement = new JADate(getDate());
                    JADate dateDeclenchement = new JADate(contentieux.getProchaineDateDeclenchement());
                    JACalendar cal = getSession().getApplication().getCalendar();
                    JADate nouvelleDateDeclenchement;
                    nouvelleDateDeclenchement = cal.addDays(datePaiement, reportParamInt);
                    nouvelleDateDeclenchement = CADateUtil.getDateOuvrable(nouvelleDateDeclenchement);
                    // report si la nouvelle date > la date en vigueur
                    if (cal.compare(nouvelleDateDeclenchement, dateDeclenchement) == JACalendar.COMPARE_FIRSTUPPER) {
                        // Enregistrement en DB
                        contentieux.setSession(getSession());
                        contentieux.setProchaineDateDeclenchement(nouvelleDateDeclenchement.toStr("."));
                        contentieux.update(transaction);
                    }
                }

            } else {
                _addWarning(transaction,
                        "La date de déclenchement n'a pas été mise à jour ! Plusieurs dossiers pour cette section on été trouvés : "
                                + getIdSection());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "Erreur lors du calcul du report de la date de déclenchement pour la section : "
                    + getIdSection());
        }
    }

    @Override
    public void setAnneeCotisation(String newAnneeCotisation) {
        // if (isUpdatable())
        anneeCotisation = newAnneeCotisation;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    @Override
    public void setCodeDebitCredit(String newCodeDebitCredit) {
        // if (isUpdatable())
        codeDebitCredit = newCodeDebitCredit;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:09:42)
     * 
     * @param newCodeDebitCreditEcran
     *            Boolean
     */
    public void setCodeDebitCreditEcran(String newCodeDebitCreditEcran) {
        codeDebitCreditEcran = newCodeDebitCreditEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2002 18:34:23)
     * 
     * @param newIdCompte
     *            String
     */
    @Override
    public void setIdCompte(String newIdCompte) {
        // if (isUpdatable()) {
        idCompte = newIdCompte;
        rubrique = null;
        // } else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 15:55:06)
     * 
     * @param newIdCompteEcran
     *            String
     */
    public void setIdCompteEcran(String newIdCompteEcran) {
        idCompteEcran = newIdCompteEcran;
    }

    @Override
    public void setIdContrepartie(String newIdContrepartie) {
        // if (isUpdatable()) {
        idContrepartie = newIdContrepartie;
        contrepartie = null;
        // } else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 13:44:59)
     * 
     * @param newIdExterneRubriqueEcran
     *            String
     */
    public void setIdExterneRubriqueEcran(String newIdExterneRubriqueEcran) {
        idExterneRubriqueEcran = newIdExterneRubriqueEcran;
    }

    @Override
    public void setMasse(String newMasse) {
        // if (isUpdatable())
        masse = newMasse;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:16:38)
     * 
     * @return String
     */
    public void setMasseEcran(String newMasseEcran) {
        masseEcran = newMasseEcran;
        masse = newMasseEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 07:36:40)
     * 
     * @param newMontant
     *            String
     */
    @Override
    public void setMontant(String newMontant) {
        // if (isUpdatable())
        montant = newMontant;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 14:16:49)
     * 
     * @return String
     */
    public void setMontantEcran(String newMontantEcran) {
        montantEcran = newMontantEcran;
        montant = newMontantEcran;
    }

    @Override
    public void setNoEcritureCollective(String newNoEcritureCollective) {
        noEcritureCollective = newNoEcritureCollective;
    }

    @Override
    public void setPiece(String newPiece) {
        // if (isUpdatable())
        piece = newPiece;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2002 16:25:14)
     * 
     * @param newRechercheRubriqueEcran
     *            String
     */
    public void setRechercheRubriqueEcran(String newRechercheRubriqueEcran) {
        try {
            rechercheRubriqueEcran = Boolean.valueOf(newRechercheRubriqueEcran);
        } catch (Exception e) {
            rechercheRubriqueEcran = new Boolean(false);
        }
    }

    @Override
    public void setTaux(String newTaux) {
        // if (isUpdatable())
        taux = newTaux;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Active / Désactive la conversion du signe de la masse
     * 
     * @see globaz.osiris.api.APIEcriture#disableConversionSigneMasse()
     */
    @Override
    public void setUnsynchronizeSigneMasse(Boolean newDisableConversionSigneMasse) {
        unsynchronizeSigneMasse = newDisableConversionSigneMasse;
    }

}
