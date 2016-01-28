package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteCompteAnnexe;
import globaz.musca.db.facturation.FAEnteteFactureAPG;
import globaz.musca.db.facturation.FAEnteteFactureAPGManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.helper.FASectionsACompenserHelper;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.translation.CACodeSystem;
import java.util.Collection;
import java.util.Iterator;

/**
 * Compensation de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageCompenserAPGProcess extends FAGenericProcess {

    private static final long serialVersionUID = 1171086649533191037L;
    private globaz.musca.application.FAApplication app = null;
    private String idModuleFacturation = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageCompenserAPGProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageCompenserAPGProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageCompenserAPGProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Method _calculerIdExterneRubrique. Retourne la rubrique de compensation ou la rubrique pour montant minime
     * (ex.:+/- 2.-)
     * 
     * @param entFacture
     * @return String
     */
    public String _calculerIdExterneRubrique(FAEnteteFactureAPG entFacture) {
        if (entFacture.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_APG)
                || entFacture.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES)) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_APG_MAT);
        } else if (entFacture.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_IJAI)) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_IJAI);
        } else if (entFacture.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_AF)) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_ALFA);
        } else {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_RENTES);
        }
    }

    /**
     * Insert the method's description here. Creation date: (29.07.2003 14:18:12)
     * 
     * @return boolean
     * @param entFacture
     *            globaz.musca.db.facturation.FAEnteteFacture
     * @param sec
     *            globaz.osiris.api.APISection
     * @param montantFacture
     *            String
     */
    public boolean _creerAfactCompensationAnnexe(FAEnteteFactureAPG entFacture, String remarque) {

        // compenser à hauteur de la section
        FAAfact afactDeCompensation = new FAAfact();
        afactDeCompensation.setSession(getSession());

        // mettre à jour les attributs pour l'afactDeCompensation
        // ---------------------------------------------------------

        // afact non quittancé pour passage interne
        if (FAPassage.CS_TYPE_INTERNE.equalsIgnoreCase(passage.getIdTypeFacturation())) {
            afactDeCompensation.setAQuittancer(new Boolean(false));
        }

        afactDeCompensation.setIdPassage(passage.getIdPassage());
        afactDeCompensation.setIdEnteteFacture(entFacture.getIdEntete());
        afactDeCompensation.setIdModuleFacturation(getIdModuleFacturation());
        afactDeCompensation.setIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
        afactDeCompensation.setIdExterneRubrique(_calculerIdExterneRubrique(entFacture));
        afactDeCompensation.setMontantFacture(entFacture.getSoldeSection());

        // Informations provenant de la section Osiris
        afactDeCompensation.setIdExterneFactureCompensation(entFacture.getIdExterne());
        afactDeCompensation.setIdTypeFactureCompensation(entFacture.getIdTypeSection());
        afactDeCompensation.setIdSection(entFacture.getIdSection());

        afactDeCompensation.setAQuittancer(new Boolean(false));
        afactDeCompensation.setRemarque(remarque);
        try {
            afactDeCompensation.add(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        }

        return !getTransaction().hasErrors();
    }

    public boolean _doCompenserAnnexe(Collection<?> sections, FAEnteteCompteAnnexe entCompteAnnexe,
            CACompteAnnexe compteAnnexe) {

        FAEnteteFactureAPGManager entManager = new FAEnteteFactureAPGManager();
        entManager.setISession(getSession());
        entManager.setForIdPassage(getIdPassage());
        entManager.setForIdExterneRole(entCompteAnnexe.getIdExterneRole());
        // entManager.setForIdRole("517002");
        BStatement statement = null;
        FAEnteteFactureAPG entFacture = new FAEnteteFactureAPG();

        try {
            FWCurrency posMontantRestantFacture = null;
            String remarque;

            String lastIdEnteteFacture = null;
            String lastIdExterneRole = null;

            FASectionsACompenserHelper sectHelper = new FASectionsACompenserHelper();
            sectHelper.initSectionsACompenser(sections);
            statement = entManager.cursorOpen(getTransaction());
            // prendre les entêtes de la facturation une à une et compenser
            while ((entFacture = (FAEnteteFactureAPG) entManager.cursorReadNext(statement)) != null) {
                if (!entFacture.getIdExterneRole().equals(lastIdExterneRole)
                        || entFacture.getIdEntete().equals(lastIdEnteteFacture)) {
                    // Compenser la section et mise à jours dans le helper !!!
                    FWCurrency montantFacture = new FWCurrency(entFacture.getTotalFacture());
                    posMontantRestantFacture = new FWCurrency(montantFacture.toString());

                    // On travaille en positif
                    posMontantRestantFacture.abs();

                    int idSousType = JadeStringUtil.parseInt(entFacture.getIdSousType().substring(4, 6), 0);
                    if (((idSousType >= 1) && (idSousType <= 12)) || ((idSousType >= 40) && (idSousType <= 46))
                            || ((idSousType > 60) && (idSousType < 63))) {

                        remarque = "";
                        // Si le compte est a un solde positif avec un motif de
                        // contentieux bloqué et actif
                        // mettre une remarque pour information
                        if (compteAnnexe.isCompteBloqueEtActif(passage.getDateFacturation()))// bloqué
                        {
                            remarque = getSession().getLabel("COMPTEANNEXE_CONTENTIEUX_MOTIF")
                                    + CACodeSystem.getLibelle(getSession(), compteAnnexe.getIdContMotifBloque());
                        }
                        // Si le compte est a un solde positif avec une section
                        // avec contentieux
                        // mettre une remarque pour information
                        else {
                            Collection<?> collec = compteAnnexe.propositionCompensation(
                                    APICompteAnnexe.PC_TYPE_MONTANT, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                    entFacture.getTotalFacture());
                            Iterator<?> it = collec.iterator();
                            while (it.hasNext()) {
                                APISection sec = (APISection) it.next();
                                if (sec.getContentieuxEstSuspendu().booleanValue()) {
                                    remarque = getSession().getLabel("SECTION_CONTENTIEUX");
                                    break;
                                }
                            }

                        }

                        _creerAfactCompensationAnnexe(entFacture, remarque);

                        lastIdEnteteFacture = entFacture.getIdEntete();
                        lastIdExterneRole = entFacture.getIdExterneRole();

                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Ne trouve pas d'entete de facture pour le tiers:" + entCompteAnnexe.getIdTiers(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
            // ****************ROLLBACK!!
        } finally {
            try {
                // fermer le curseur sur les entetes de facture à la fin du
                // traitement
                entManager.cursorClose(statement);
                statement = null;
            } catch (Exception ee) {
                getMemoryLog().logMessage(ee.getMessage(), FWMessage.AVERTISSEMENT, this.getClass().getName());
            } finally {
                return (!getTransaction().hasErrors());
            }
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    public boolean _executeCompenserByAnnexeProcess(IFAPassage passage) {
        // test du passage
        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                passage.retrieve(getTransaction());
                this.setPassage((FAPassage) passage);
            } catch (Exception e) {
            }
            ;
        }

        /*
         * Proposition de compensation de la facturation en compte annexe Prendre tous les décomptes du passage, le
         * grouper par tiers (plus petit au plus grand) et par la somme des décomptes pour ce tiers (décomptes du plus
         * grand au plus petit)
         */

        FAEnteteFactureAPGManager entManager = new FAEnteteFactureAPGManager();
        entManager._setUseManagerForCompensationAnnexe(true);
        entManager.setSession(getSession());
        entManager.setForIdPassage(passage.getIdPassage());
        String numAffilie = "";
        String dernierNumAff = "";

        BStatement statement = null;

        try {
            statement = entManager.cursorOpen(getTransaction());

            // initialiser le module de la comptabilité d'Osiris
            app = (FAApplication) getSession().getApplication();

            FAEnteteCompteAnnexe compensationAnnexeTempEntity = null;
            int compt = 0;
            while ((!getTransaction().hasErrors())
                    && ((compensationAnnexeTempEntity = (FAEnteteCompteAnnexe) entManager.cursorReadNext(statement)) != null)) {
                compt++;
                setProgressDescription(compensationAnnexeTempEntity.getIdExterneRole() + " <br>" + compt + "/"
                        + entManager.size() + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                            + compensationAnnexeTempEntity.getIdExterneRole() + " <br>" + compt + "/"
                            + entManager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : "
                                        + compensationAnnexeTempEntity.getIdExterneRole() + " <br>" + compt + "/"
                                        + entManager.size() + "<br>");
                    }
                    break;
                } else {
                    // utiliser la comptabilité annexe
                    try {

                        APICompteAnnexe cpt = null;

                        // Compte annexe
                        cpt = this.getCompteAnnexe(compensationAnnexeTempEntity);
                        numAffilie = compensationAnnexeTempEntity.getIdExterneRole();

                        if (cpt != null) {
                            // la collection de proposition de compensation
                            Collection<?> sectionCol = cpt.propositionCompensation(APICompteAnnexe.PC_TYPE_MONTANT,
                                    APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                    compensationAnnexeTempEntity.getTotalDecomptes());

                            if (!numAffilie.equals(dernierNumAff)) {
                                _doCompenserAnnexe(sectionCol, compensationAnnexeTempEntity, (CACompteAnnexe) cpt);
                                dernierNumAff = compensationAnnexeTempEntity.getIdExterneRole();
                            }
                        }

                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), globaz.framework.util.FWMessage.FATAL,
                                this.getClass().getName());
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de compenser les afacts par le compte annexe: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        } finally {
            if (!getTransaction().hasErrors()) {
                try { // committer
                    getTransaction().commit();
                } catch (Exception e) {
                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        this._addError(getTransaction(), FWMessage.FATAL);
                    }
                } finally {
                    try {
                        entManager.cursorClose(statement);
                        statement = null;
                    } catch (Exception ee) {
                        getMemoryLog().logMessage("Impossible de fermer le curseur: " + ee.getMessage(),
                                FWViewBeanInterface.ERROR, this.getClass().getName());
                    }
                }
                return true;
            } else {
                getMemoryLog().logMessage(getTransaction().getErrors().toString(),
                        globaz.framework.util.FWMessage.FATAL, this.getClass().getName());
                try {
                    getTransaction().rollback();
                } catch (Exception ee) {
                    this._addError(getTransaction(), FWMessage.FATAL);
                } finally {
                    try {
                        entManager.cursorClose(statement);
                        statement = null;
                    } catch (Exception ee) {
                        getMemoryLog().logMessage("Impossible de fermer le curseur: " + ee.getMessage(),
                                FWViewBeanInterface.ERROR, this.getClass().getName());
                    }
                }
                return false;
            }
        }

    }

    public boolean _executeCompenserProcess(IFAPassage passage) {
        // test du passage
        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                passage.retrieve(getTransaction());
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de fermer le curseur: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, this.getClass().getName());
            }
            ;
        }
        this.setPassage((FAPassage) passage);
        FAEnteteFactureAPGManager entManager = new FAEnteteFactureAPGManager();
        // garantir que le manager rapatrie toute les données dans son container
        entManager.changeManagerSize(0);
        entManager._setUseManagerForCompensation(true);
        entManager.setSession(getSession());
        entManager.setForIdPassage(passage.getIdPassage());
        boolean successful = false;
        try {
            entManager.find(getTransaction());
            successful = _executeCompenserByAnnexeProcess(passage);

            this.logInfo4Process(successful, "OBJEMAIL_FA_COMPENSATIONEXTERNE");

        } catch (Exception e) {
            return false;
        } finally {
            if (!getTransaction().hasErrors() && successful) {
                try {
                    // committer la compensation interne
                    getTransaction().commit();
                } catch (Exception e) {
                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        this._addError(getTransaction(), FWMessage.FATAL);
                    }
                }
            } else {
                try {
                    getTransaction().rollback();
                } catch (Exception ee) {
                    this._addError(getTransaction(), FWMessage.FATAL);
                }
            }
            return successful;
        }

    }

    @Override
    protected boolean _executeProcess() {

        // prendre le passage en cours;
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());
        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
        }
        ;

        // Vérifier si le passage a les critères de validité pour un traitement
        if (!_passageIsValid(passage)) {
            abort();
            return false;
        }

        // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }

        boolean estCompense = false;

        // compenser en interne
        estCompense = _executeCompenserProcess(passage);

        // finaliser le passage (le verrouiller)
        if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_TRAITEMENT)) {
            abort();
            return false;
        }
        // return estCompense;
        return estCompense;

    }

    public int _getIndexMontantPositifMin(FAEnteteFactureAPGManager entManager) {
        // index dans le manager à calculer se référant au montant positif
        // minimum
        int indexMntPositifMin = 0;
        FWCurrency currentMontant;

        FAEnteteFactureAPG entFacture = new FAEnteteFactureAPG();
        for (int i = 0; i < entManager.size(); i++) {
            entFacture = (FAEnteteFactureAPG) entManager.getEntity(i);
            currentMontant = new FWCurrency(entFacture.getTotalFacture());

            /*
             * prendre le premier montant positif minimum et commencer la compensation et qu'il existe aussi des
             * montants négatifs
             */
            if (currentMontant.isPositive() && (i != 0)) {
                indexMntPositifMin = i;
                break;
            }
        }

        return indexMntPositifMin;
    }

    public CACompteAnnexe getCompteAnnexe(FAEnteteCompteAnnexe compensationAnnexeEntity) {

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setISession(app.getSessionOsiris(getSession()));
        compteAnnexe.setIdTiers(compensationAnnexeEntity.getIdTiers());
        compteAnnexe.setIdRole(compensationAnnexeEntity.getIdRoles());
        compteAnnexe.setIdExterneRole(compensationAnnexeEntity.getIdExterneRole());

        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);

        try {
            compteAnnexe.retrieve(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(), this.getClass().getName() + ": Erreur dans le compte annexe");
        } finally {
            if (!getTransaction().hasErrors()) {
                return compteAnnexe;
            } else {
                return null;
            }
        }
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * @param idRubrique
     *            Code système de la référence rubrique. Voir APIReferenceRubrique.
     * @return le numéro de la rubrique.
     */
    public String getRubriqueCode(String idRubrique) {
        String rubrique = "";
        CAReferenceRubrique reference = new CAReferenceRubrique();
        reference.setSession(getSession());
        rubrique = reference.getRubriqueByCodeReference(idRubrique).getIdExterne();
        if (rubrique.equals("") || rubrique.equals(null)) {
            this._addError("La reference rubrique n'est pas définie !!! (refRubrique:" + idRubrique + ")");
        }
        return rubrique;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }
}
