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
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteCompteAnnexe;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.helper.FASectionHelper;
import globaz.musca.process.helper.FASectionsACompenserHelper;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CAUtil;
import java.util.Collection;
import java.util.Iterator;

/**
 * Compensation de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageCompenserProcess extends FAGenericProcess {

    private static final long serialVersionUID = 5425917681075062607L;
    private globaz.musca.application.FAApplication app = null;
    private String dernierIdExterne = "";
    private String dernierNumAff = "";
    private String idExterne = "";
    private String idModuleFacturation = "";
    private String numAffilie = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageCompenserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageCompenserProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageCompenserProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Method _calculerIdExterneRubrique. Retourne la rubrique de compensation ou la rubrique pour montant minime
     * (ex.:+/- 2.-)
     * 
     * @param entFacture
     * @return String
     */
    public String _calculerIdExterneRubrique(APISection section) {
        // Bug 5438
        if (section.getIdModeCompensation().equals(APISection.MODE_REPORT)
                && (!CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes()))) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_SOLDE);
        } else if (CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes())) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_TAXE);
        } else {
            return getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
        }
    }

    public boolean _compenserDecompte(FAEnteteFactureManager entManager, int indexMntPositifMin) {
        int compt = 0;
        for (int i = 0; i < indexMntPositifMin; i++) {
            // if (!this.isAborted()) {
            // décomte negatifs
            FAEnteteFacture entFactureNeg = (FAEnteteFacture) entManager.getEntity(i);
            compt++;
            setProgressDescription(entFactureNeg.getIdExterneRole() + " <br>" + compt + "/" + entManager.size()
                    + "<br>");
            if (isAborted()) {
                setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entFactureNeg.getIdExterneRole()
                        + " <br>" + compt + "/" + entManager.size() + "<br>");
                if ((getParent() != null) && getParent().isAborted()) {
                    getParent().setProcessDescription(
                            "Traitement interrompu<br> sur l'affilié : " + entFactureNeg.getIdExterneRole() + " <br>"
                                    + compt + "/" + entManager.size() + "<br>");
                }
                break;
            } else {
                for (int j = indexMntPositifMin; j < entManager.size(); j++) {

                    FAEnteteFacture entFacturePos = (FAEnteteFacture) entManager.getEntity(j);

                    if (entFactureNeg.getIdExterneRole().equalsIgnoreCase(entFacturePos.getIdExterneRole())) {
                        FWCurrency montantNeg = new FWCurrency(entFactureNeg.getTotalFacture());
                        FWCurrency montantPos = new FWCurrency(entFacturePos.getTotalFacture());
                        if (montantNeg.isNegative() && montantPos.isPositive() && !montantNeg.isZero()
                                && !montantPos.isZero()) {
                            // Compenser le décompte négatif avec le positif
                            _doCompenserDecompte(entFactureNeg, entFacturePos);
                        }

                    }

                    if (new FWCurrency(entFactureNeg.getTotalFacture()).isZero()) {
                        // prendre le prochain décompte négatifs si remis à zéro
                        break;
                    }

                }
            }
        }

        return !getTransaction().hasErrors();
    }

    public boolean _creerAfactCompensationAnnexe(FAEnteteFacture entFacture, APISection sec, String montantCompFacture,
            boolean aQuittancer, String remarque) {

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
        afactDeCompensation.setIdExterneRubrique(_calculerIdExterneRubrique(sec));
        afactDeCompensation.setMontantFacture(montantCompFacture);

        // Informations provenant de la section Osiris
        afactDeCompensation.setIdExterneFactureCompensation(sec.getIdExterne());
        afactDeCompensation.setIdTypeFactureCompensation(sec.getIdTypeSection());
        afactDeCompensation.setIdExterneDebiteurCompensation(sec.getCompteAnnexe().getIdExterneRole());
        afactDeCompensation.setIdTiersDebiteurCompensation(sec.getCompteAnnexe().getIdTiers());
        afactDeCompensation.setIdRoleDebiteurCompensation(sec.getCompteAnnexe().getIdRole());

        afactDeCompensation.setAQuittancer(new Boolean(aQuittancer));
        afactDeCompensation.setRemarque(remarque);
        try {
            afactDeCompensation.add(getTransaction());
            // Mise à jour de la section, on ajoute le numéro de passage
            // pour indiquer qu'elle a déjà été prise en compte
            if (sec.getIdModeCompensation().equals(APISection.MODE_REPORT)) {
                sec.updateInfoCompensation(sec.getIdModeCompensation(), entFacture.getIdPassage());
            }
        } catch (Exception e) {
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        }

        return !getTransaction().hasErrors();
    }

    public boolean _doCompenserAnnexe(Collection<?> sections, FAEnteteCompteAnnexe entCompteAnnexe,
            CACompteAnnexe compteAnnexe) {

        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        entManager.setISession(getSession());
        entManager.setForIdPassage(getIdPassage());
        entManager.setForIdRole(entCompteAnnexe.getIdRoles());
        entManager.setForIdExterneRole(entCompteAnnexe.getIdExterneRole());
        BStatement statement = null;
        FAEnteteFacture entFacture = null;

        FWCurrency totalFacture = new FWCurrency(entCompteAnnexe.getTotalDecomptes());

        if (totalFacture.isNegative()) {
            entManager.setOrderBy(" TOTALFACTURE ASC ");
        } else if (totalFacture.isPositive()) {
            entManager.setOrderBy(" TOTALFACTURE DESC ");
        }
        try {
            FWCurrency posMontantRestantFacture = null;
            boolean isCompensationTotal, aQuittancer;
            String remarque;

            FASectionsACompenserHelper sectHelper = new FASectionsACompenserHelper();
            sectHelper.initSectionsACompenser(sections);
            statement = entManager.cursorOpen(getTransaction());
            boolean passerAuSuivant = false;
            // prendre les entêtes de la facturation une à une et compenser
            while (((entFacture = (FAEnteteFacture) entManager.cursorReadNext(statement)) != null)
                    && !entFacture.isNew()) {
                if (passerAuSuivant) {
                    sectHelper.initSectionsACompenser(sections);
                }

                // Compenser la section et mise à jours dans le helper !!!
                FWCurrency montantFacture = new FWCurrency(entFacture.getTotalFacture());
                posMontantRestantFacture = new FWCurrency(montantFacture.toString());

                // On travaille en positif
                posMontantRestantFacture.abs();

                FASectionHelper sectionACompenser = null;
                // Tant qu'il y a des sections à compenser....
                while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                    if (posMontantRestantFacture.isNegative() || posMontantRestantFacture.isZero()) {
                        break;
                    }
                    CASection section = new CASection();
                    section = (CASection) sectionACompenser.getSection();
                    if (APISection.MODE_REPORT.equals(section.getIdModeCompensation())
                            || APISection.MODE_COMP_COMPLEMENTAIRE.equals(section.getIdModeCompensation())
                            || APISection.MODE_COMP_CONT_EMPLOYEUR.equals(section.getIdModeCompensation())
                            || APISection.MODE_COMP_COT_PERS.equals(section.getIdModeCompensation())
                            || APISection.MODE_COMP_DEC_FINAL.equals(section.getIdModeCompensation())) {
                        isCompensationTotal = true;
                        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                        sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        sectHelper.updateSection(sectionACompenser);
                        continue;
                    }
                    int typeSection = JadeStringUtil.parseInt(section.getIdTypeSection(), 0);
                    if ((typeSection != 16) && (typeSection != 21) && (typeSection != 28) && (typeSection != 29)
                            && (typeSection != 15)) {
                        isCompensationTotal = false;
                        aQuittancer = false;
                        remarque = "";
                        FWCurrency montantSectionACompenser = new FWCurrency(sectionACompenser.getMontantACompenser()
                                .toString());

                        FWCurrency soldeMontantACompenser = new FWCurrency(montantSectionACompenser.toString());
                        // Variable de travail
                        FWCurrency posMontantSection = new FWCurrency(montantSectionACompenser.toString());
                        // On travaille en positif
                        posMontantSection.abs();

                        FWCurrency montantACompenser = new FWCurrency(0);

                        // Compensation totale
                        if (posMontantRestantFacture.isPositive()
                                && (posMontantRestantFacture.compareTo(posMontantSection) >= 0)) {
                            montantACompenser = new FWCurrency(posMontantSection.toString());
                            isCompensationTotal = true;
                        }
                        // Compensation partielle
                        else if (posMontantRestantFacture.isPositive()
                                && (posMontantRestantFacture.compareTo(posMontantSection) < 0)) {
                            montantACompenser = new FWCurrency(posMontantRestantFacture.toString());
                            isCompensationTotal = false;
                        }

                        // CAS montant facture > 0 --> montant sections < 0
                        // On va compenser avec des montant négatif
                        if (montantFacture.isPositive()) {
                            montantACompenser.negate();
                        }

                        // Si le compte est a un solde positif avec un motif de
                        // contentieux bloqué et actif
                        // mettre une remarque pour information
                        if (montantSectionACompenser.isPositive()
                                && compteAnnexe.isCompteBloqueEtActif(passage.getDateFacturation()))// bloqué
                        {
                            remarque = getSession().getLabel("COMPTEANNEXE_CONTENTIEUX_MOTIF")
                                    + CACodeSystem.getLibelle(getSession(), compteAnnexe.getIdContMotifBloque());
                        }
                        // Si le compte est a un solde positif avec une section
                        // avec contentieux
                        // mettre une remarque pour information
                        else if (montantSectionACompenser.isPositive()) {
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
                        // Si c'est une facture pour les étudiants
                        if (entFacture.getIdSousType()
                                .equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT)) {
                            aQuittancer = false;
                        } else {
                            // Si le compte annexe sous surveillance
                            if (compteAnnexe.isASurveiller().booleanValue() || compteAnnexe.isVerrouille()) {
                                aQuittancer = true;
                            } else {
                                if (montantSectionACompenser.isPositive()) {
                                    aQuittancer = true;
                                } else {
                                    aQuittancer = false;

                                }
                            }
                        }
                        _creerAfactCompensationAnnexe(entFacture, sectionACompenser.getSection(),
                                montantACompenser.toString(), aQuittancer, remarque);

                        soldeMontantACompenser.sub(montantACompenser);

                        montantACompenser.abs();
                        posMontantRestantFacture.sub(montantACompenser);

                        // Update de la section
                        if (isCompensationTotal) {
                            sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                            sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        } else {
                            sectionACompenser.setStatus(FASectionHelper.PARTIELLEMENT_COMPENSEE);
                            sectionACompenser.setMontantACompenser(soldeMontantACompenser);

                        }
                        sectHelper.updateSection(sectionACompenser);
                    } else {
                        isCompensationTotal = true;
                        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                        sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        sectHelper.updateSection(sectionACompenser);
                        continue;
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

    public boolean _doCompenserDecompte(FAEnteteFacture entFactureNeg, FAEnteteFacture entFacturePos) {

        FAAfact afactDeCompensation = new FAAfact();
        afactDeCompensation.setSession(getSession());

        // mettre à jour les attributs pour l'afactDeCompensation
        // ---------------------------------------------------------

        // afact non quittancé pour passage interne
        if (FAPassage.CS_TYPE_INTERNE.equalsIgnoreCase(passage.getIdTypeFacturation())) {
            afactDeCompensation.setAQuittancer(new Boolean(false));
        }

        afactDeCompensation.setIdPassage(passage.getIdPassage());
        afactDeCompensation.setIdEnteteFacture(entFactureNeg.getIdEntete());
        afactDeCompensation.setIdModuleFacturation(globaz.musca.external.ServicesFacturation.getIdModFacturationByType(
                getSession(), getTransaction(), FAModuleFacturation.CS_MODULE_COMPENSATION));

        afactDeCompensation.setIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION_INTERNE);

        // prendre le premier afact et l'utiliser pour copier certaines valeurs
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(getSession());
        afactManager.setForIdPassage(getIdPassage());
        afactManager.setForIdEnteteFacture(entFactureNeg.getIdEntete());
        try {
            afactManager.find(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de chercer les afacts: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    afactManager.getClass().getName());

        }

        FAAfact afact = (FAAfact) afactManager.getEntity(0);

        if ((afact != null) && !afact.isNew()) {
            afactDeCompensation.setIdExterneRubrique(getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE));
            FWCurrency montantNeg = new FWCurrency(entFactureNeg.getTotalFacture());
            montantNeg.abs();
            FWCurrency montantPos = new FWCurrency(entFacturePos.getTotalFacture());

            int delta = montantPos.compareTo(montantNeg);

            FWCurrency montantDeCompensation = null;
            if (delta > 0) {
                // compenser à hauteur du totalFacture du décompte negatif
                montantDeCompensation = montantNeg;
            } else if (delta < 0) {
                // compenser à hauteur du totalFacture du décompte negatif
                montantDeCompensation = montantPos;
            } else {
                montantDeCompensation = montantNeg;
            }
            if (montantDeCompensation != null) {
                afactDeCompensation.setMontantFacture(montantDeCompensation.toString());
                FWCurrency nouveauMontantPourEntete = new FWCurrency(entFactureNeg.getTotalFacture());

                nouveauMontantPourEntete.add(montantDeCompensation);
                entFactureNeg.setTotalFacture(nouveauMontantPourEntete.toString());
            }
            afactDeCompensation.setIdExterneFactureCompensation(entFacturePos.getIdExterneFacture());
            try {
                // ajouter l'afact positif au décompte négatif
                afactDeCompensation.add(getTransaction());
                // s'il n'y a pas d'erreur, décompenser le décompte positif
                if (!getTransaction().hasErrors()) {
                    FAAfact inverseCompensation = (FAAfact) afactDeCompensation.clone();
                    inverseCompensation.setIdPassage(passage.getIdPassage());
                    inverseCompensation.setIdEnteteFacture(entFacturePos.getIdEntete());
                    inverseCompensation.setIdExterneFactureCompensation(entFactureNeg.getIdExterneFacture());
                    // compenser négativement à hauteur du montantDeCompensation
                    // calculé ci-dessus
                    if (montantDeCompensation != null) {
                        inverseCompensation.setMontantFacture("-" + montantDeCompensation);
                        FWCurrency nouveauMontantPourInverse = new FWCurrency(entFacturePos.getTotalFacture());

                        nouveauMontantPourInverse.sub(montantDeCompensation);
                        entFacturePos.setTotalFacture(nouveauMontantPourInverse.toString());
                    }

                    // rajouter l'afact négatif
                    inverseCompensation.add(getTransaction());
                }
            } catch (Exception ee) {
                getMemoryLog().logMessage("Impossible d'ajouter des afacts: " + ee.getMessage(),
                        FWViewBeanInterface.ERROR, afactManager.getClass().getName());
            }
        }

        return !getTransaction().hasErrors();
    }

    private boolean _doReporterMontant(FAEnteteCompteAnnexe enteteCompte, CACompteAnnexe cpt) {
        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        entManager.setISession(getSession());
        entManager.setForIdPassage(getIdPassage());
        entManager.setForIdExterneRole(enteteCompte.getIdExterneRole());
        BStatement statement = null;
        FAEnteteFacture entFacture = null;

        FWCurrency totalFacture = new FWCurrency(enteteCompte.getTotalDecomptes());

        if (totalFacture.isNegative()) {
            entManager.setOrderBy(" TOTALFACTURE ASC ");
        } else if (totalFacture.isPositive()) {
            entManager.setOrderBy(" TOTALFACTURE DESC ");
        }
        try {

            boolean aQuittancer;
            String remarque;

            statement = entManager.cursorOpen(getTransaction());
            // prendre les entêtes de la facturation une à une et compenser
            while (((entFacture = (FAEnteteFacture) entManager.cursorReadNext(statement)) != null)
                    && !entFacture.isNew()) {

                CASectionManager sectionMana = new CASectionManager();
                sectionMana.setSession(getSession());
                sectionMana.setForIdCompteAnnexe(cpt.getIdCompteAnnexe());
                sectionMana.setForModeCompensationIsReport(true);
                sectionMana.find();
                if (sectionMana.size() > 0) {
                    for (int i = 0; i < sectionMana.size(); i++) {
                        CASection section = (CASection) sectionMana.getEntity(i);
                        numAffilie = entFacture.getIdExterneRole();
                        // typeSection = section.getIdTypeSection();
                        idExterne = entFacture.getIdExterneFacture();
                        if (!entFacture.getIdExterneRole().equals(dernierNumAff)
                                || !entFacture.getIdExterneFacture().equals(dernierIdExterne)) {
                            dernierNumAff = entFacture.getIdExterneRole();
                            // dernierTypeSec = section.getIdTypeSection();
                            dernierIdExterne = entFacture.getIdExterneFacture();
                        }
                        String sousType = entFacture.getIdSousType();
                        sousType = sousType.substring(4, 6);
                        int type = JadeStringUtil.parseInt(sousType, 0);
                        if (section.getIdModeCompensation().equals(APISection.MODE_COMP_DEC_FINAL)
                                && !((type == 13) || (type == 14))) {

                            continue;
                        } else if (section.getIdModeCompensation().equals(APISection.MODE_COMP_COT_PERS)
                                && !((type == 20) || (type == 22))) {

                            continue;
                        } else if (section.getIdModeCompensation().equals(APISection.MODE_COMP_CONT_EMPLOYEUR)
                                && !(type == 17)) {

                            continue;
                        } else if (section.getIdModeCompensation().equals(APISection.MODE_COMP_COMPLEMENTAIRE)
                                && !(type == 18)) {

                            continue;
                        }

                        if (numAffilie.equals(dernierNumAff)
                        // && typeSection.equals(dernierTypeSec)
                                && idExterne.equals(dernierIdExterne)) {
                            int typeSection = JadeStringUtil.parseInt(section.getIdTypeSection(), 0);
                            if ((typeSection != 16) && (typeSection != 21) && (typeSection != 28)
                                    && (typeSection != 29)) {
                                aQuittancer = false;
                                remarque = "";

                                dernierNumAff = entFacture.getIdExterneRole();
                                // dernierTypeSec = section.getIdTypeSection();
                                dernierIdExterne = entFacture.getIdExterneFacture();

                                // Si le numéro de passage est renseigné dans la
                                // section en mode report
                                // c'est qu'elle a déjà été utilisé, donc on
                                // crée pas la compensation
                                if (JadeStringUtil.isBlankOrZero(section.getIdPassageComp())
                                        && !JadeStringUtil.isBlankOrZero(section.getSolde())) {
                                    _creerAfactCompensationAnnexe(entFacture, section, section.getSolde(), aQuittancer,
                                            remarque);
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("Ne trouve pas d'entete de facture pour le tiers:" + enteteCompte.getIdTiers(),
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
        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        BStatement statement = null;
        try {
            entManager.setUseManagerForCompensationAnnexe(true);
            entManager.setSession(getSession());
            entManager.setForIdPassage(passage.getIdPassage());
            entManager.setForTotalFactureNotBetween(true);
            entManager._setGroupBy(" IDTIERS, IDEXTERNEROLE, NONIMPRIMABLE, IDROLE, MODIMP ");
            entManager.setOrderBy(" IDEXTERNEROLE ASC, TOTALDECOMPTES DESC");

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

                    /*
                     * un fake entity qui calcule le somme des décomptes qui ont un montant de signe opposé dans la
                     * compta annexe
                     */
                    if (!compensationAnnexeTempEntity.isNew()
                            && (!(new FWCurrency(compensationAnnexeTempEntity.getTotalDecomptes())).isZero())) {
                        // utiliser la comptabilité annexe
                        try {

                            APICompteAnnexe cpt = null;
                            // Compte annexe
                            CACompteAnnexeManager compteAnnexe = new CACompteAnnexeManager();
                            compteAnnexe.setISession(app.getSessionOsiris(getSession()));
                            compteAnnexe.setForIdTiers(compensationAnnexeTempEntity.getIdTiers());
                            compteAnnexe.setForIdRole(compensationAnnexeTempEntity.getIdRoles());
                            try {
                                compteAnnexe.find(getTransaction());
                                if (compteAnnexe.size() > 1) {
                                    String idCompteAnnexe = "";
                                    for (int j = 0; j < compteAnnexe.size(); j++) {
                                        cpt = (CACompteAnnexe) compteAnnexe.getEntity(j);
                                        if (cpt != null) {
                                            if (compensationAnnexeTempEntity.getIdExterneRole().equals(
                                                    cpt.getIdExterneRole())) {
                                                FWCurrency totalFacture = new FWCurrency(
                                                        compensationAnnexeTempEntity.getTotalDecomptes());
                                                FWCurrency totalCompteAnnexe = new FWCurrency(cpt.getSolde());
                                                idCompteAnnexe = cpt.getIdCompteAnnexe();

                                                if ((totalFacture.isNegative() && totalCompteAnnexe.isPositive())
                                                        || (totalFacture.isPositive() && totalCompteAnnexe.isNegative())) {

                                                    // la collection de
                                                    // proposition de
                                                    // compensation
                                                    Collection<?> sectionCol = cpt.propositionCompensation(
                                                            APICompteAnnexe.PC_TYPE_FACTURATION,
                                                            APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                                            compensationAnnexeTempEntity.getTotalDecomptes());

                                                    _doCompenserAnnexe(sectionCol, compensationAnnexeTempEntity,
                                                            (CACompteAnnexe) cpt);
                                                }
                                            }
                                        }
                                    }
                                    for (int i = 0; i < compteAnnexe.size(); i++) {
                                        cpt = (CACompteAnnexe) compteAnnexe.getEntity(i);
                                        if (cpt != null) {
                                            if (!cpt.getIdCompteAnnexe().equals(idCompteAnnexe)) {
                                                FWCurrency totalFacture = new FWCurrency(
                                                        compensationAnnexeTempEntity.getTotalDecomptes());
                                                FWCurrency totalCompteAnnexe = new FWCurrency(cpt.getSolde());

                                                if ((totalFacture.isNegative() && totalCompteAnnexe.isPositive())
                                                        || (totalFacture.isPositive() && totalCompteAnnexe.isNegative())) {

                                                    // la collection de
                                                    // proposition de
                                                    // compensation
                                                    Collection<?> sectionCol = cpt.propositionCompensation(
                                                            APICompteAnnexe.PC_TYPE_FACTURATION,
                                                            APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                                            compensationAnnexeTempEntity.getTotalDecomptes());

                                                    _doCompenserAnnexe(sectionCol, compensationAnnexeTempEntity,
                                                            (CACompteAnnexe) cpt);
                                                }
                                            }
                                        }
                                    }
                                } else if (compteAnnexe.size() == 1) {
                                    for (int i = 0; i < compteAnnexe.size(); i++) {
                                        cpt = (CACompteAnnexe) compteAnnexe.getEntity(i);
                                        if (cpt != null) {
                                            FWCurrency totalFacture = new FWCurrency(
                                                    compensationAnnexeTempEntity.getTotalDecomptes());
                                            FWCurrency totalCompteAnnexe = new FWCurrency(cpt.getSolde());

                                            if ((totalFacture.isNegative() && totalCompteAnnexe.isPositive())
                                                    || (totalFacture.isPositive() && totalCompteAnnexe.isNegative())) {

                                                // la collection de proposition
                                                // de compensation
                                                Collection<?> sectionCol = cpt.propositionCompensation(
                                                        APICompteAnnexe.PC_TYPE_FACTURATION,
                                                        APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                                        compensationAnnexeTempEntity.getTotalDecomptes());

                                                _doCompenserAnnexe(sectionCol, compensationAnnexeTempEntity,
                                                        (CACompteAnnexe) cpt);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                this._addError(getTransaction(), this.getClass().getName()
                                        + ": Erreur dans le compte annexe");
                            }
                        } catch (Exception e) {
                            getMemoryLog().logMessage(e.getMessage(), globaz.framework.util.FWMessage.FATAL,
                                    this.getClass().getName());
                            return false;
                        }
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
        boolean successful = false;
        try {
            this.setPassage((FAPassage) passage);
            FAEnteteFactureManager entManager = new FAEnteteFactureManager();
            // garantir que le manager rapatrie toute les données dans son
            // container
            entManager.changeManagerSize(0);
            entManager.setUseManagerForCompensation(true);
            entManager.setSession(getSession());
            entManager.setForIdPassage(passage.getIdPassage());
            entManager.setForTotalFactureNotBetween(true);
            entManager.setOrderBy(" TOTALFACTURE ASC");

            entManager.find(getTransaction());
            int indexMntPositifMin = _getIndexMontantPositifMin(entManager);

            // appeler la méthode de report des montants minimes
            successful = _reporterMontantMinime(passage);
            // logger information dans l'email
            this.logInfo4Process(successful, "OBJEMAIL_FA_REPORT");

            // appeler la méthode de compensation interne
            successful = _compenserDecompte(entManager, indexMntPositifMin);

            // logger information dans l'email
            this.logInfo4Process(successful, "OBJEMAIL_FA_COMPENSATIONINTERNE");

            // compenser en compte Annexe Osiris
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

    public int _getIndexMontantPositifMin(FAEnteteFactureManager entManager) {
        // index dans le manager à calculer se référant au montant positif
        // minimum
        int indexMntPositifMin = 0;
        FWCurrency currentMontant;

        FAEnteteFacture entFacture = new FAEnteteFacture();
        for (int i = 0; i < entManager.size(); i++) {
            entFacture = (FAEnteteFacture) entManager.getEntity(i);
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

    private boolean _reporterMontantMinime(IFAPassage passage) {
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
         * Report des montants minimes Prendre tous les décomptes du passage, les grouper par tiers (plus petit au plus
         * grand) et par la somme des décomptes pour ce tiers (décomptes du plus grand au plus petit)
         */

        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        BStatement statement = null;
        try {
            // PO 6394 On reporte sur tous les factures peu importe le montant.
            entManager.setUseManagerForCompensationAnnexe(true);
            entManager.setSession(getSession());
            entManager.setForIdPassage(passage.getIdPassage());
            // entManager.setForTotalFactureNotBetween(true);
            entManager._setGroupBy(" IDTIERS, IDEXTERNEROLE, NONIMPRIMABLE, IDROLE, MODIMP ");
            entManager.setOrderBy(" IDEXTERNEROLE ASC, TOTALDECOMPTES DESC");

            statement = entManager.cursorOpen(getTransaction());

            // initialiser le module de la comptabilité d'Osiris
            app = (FAApplication) getSession().getApplication();

            FAEnteteCompteAnnexe enteteCompte = null;
            int compt = 0;
            while ((!getTransaction().hasErrors())
                    && ((enteteCompte = (FAEnteteCompteAnnexe) entManager.cursorReadNext(statement)) != null)) {
                compt++;
                setProgressDescription(enteteCompte.getIdExterneRole() + " <br>" + compt + "/" + entManager.size()
                        + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                            + enteteCompte.getIdExterneRole() + " <br>" + compt + "/" + entManager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + enteteCompte.getIdExterneRole()
                                        + " <br>" + compt + "/" + entManager.size() + "<br>");
                    }
                    break;
                } else {
                    /*
                     * un fake entity qui calcule le somme des décomptes qui ont un montant de signe opposé dans la
                     * compta annexe
                     */
                    // if (!enteteCompte.isNew() && (!(new FWCurrency(enteteCompte.getTotalDecomptes())).isZero())) {
                    if (!enteteCompte.isNew()) {
                        // utiliser la comptabilité annexe
                        try {

                            APICompteAnnexe cpt = null;

                            // Compte annexe
                            cpt = this.getCompteAnnexe(enteteCompte);

                            if ((cpt != null) && !cpt.isNew()) {

                                _doReporterMontant(enteteCompte, (CACompteAnnexe) cpt);
                            }

                        } catch (Exception e) {
                            getMemoryLog().logMessage(e.getMessage(), globaz.framework.util.FWMessage.FATAL,
                                    this.getClass().getName());
                            return false;
                        }
                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de reporter les montant minimes: " + e.getMessage(),
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
        try {
            rubrique = reference.getRubriqueByCodeReference(idRubrique).getIdExterne();
        } catch (Exception e) {
            this._addError("Erreur lors de la recherche de la rubrique (refRubrique:" + idRubrique + ")");
        }
        if (JadeStringUtil.isEmpty(rubrique)) {
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
