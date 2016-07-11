package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FABeanCompensationDecomptePeriodique;
import globaz.musca.db.facturation.FABeanCompensationDecomptePeriodiqueFacture;
import globaz.musca.db.facturation.FABeanCompensationDecomptePeriodiqueSection;
import globaz.musca.db.facturation.FACompensationDecomptePeriodique;
import globaz.musca.db.facturation.FACompensationDecomptePeriodiqueManager;
import globaz.musca.db.facturation.FAEnteteCompteAnnexe;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.helper.FASectionHelper;
import globaz.musca.process.helper.FASectionsACompenserHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CAUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Compensation de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageCompenserNewProcess extends FAGenericProcess {

    private static final long serialVersionUID = -951874324700550589L;

    protected enum Result {
        AQUITTANCERNON,
        AQUITTANCEROUI,
        PASCOMPENSATION
    }

    private globaz.musca.application.FAApplication app = null;
    private String dateFinAffiliation = "";
    private String dernierIdExterne = "";
    private String dernierNumAff = "";
    private String idExterne = "";
    private String idModuleFacturation = "";
    private String numAffilie = "";

    /**
     * Constructeur par défaut.
     */
    public FAPassageCompenserNewProcess() {
        super();
    }

    /**
     * Constructeur avec Bprocess.
     */
    public FAPassageCompenserNewProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur avec BSession.
     */
    public FAPassageCompenserNewProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Method _calculerIdExterneRubrique. Retourne la rubrique de compensation ou la rubrique pour montant minime
     * (ex.:+/- 2.-)
     * 
     * @param section La section.
     * @return La rubrique de compensation ou montant minime.
     */
    public String _calculerIdExterneRubrique(APISection section) {
        if (section.getIdModeCompensation().equals(APISection.MODE_REPORT)
                && (!CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes()))) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_SOLDE);
        } else if (CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes())) {
            return getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_TAXE);
        } else {
            return getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
        }
    }

    /**
     * Compensation des décomptes.
     * 
     * @param entManager Manager.
     * @param indexMntPositifMin Index du montant positif minime.
     * @return True si aucune erreurs.
     */
    public boolean _compenserDecompte(FAEnteteFactureManager entManager, int indexMntPositifMin) {

        for (int i = 0; i < indexMntPositifMin; i++) {
            if (!isAborted()) {
                // décomte negatifs
                FAEnteteFacture entFactureNeg = (FAEnteteFacture) entManager.getEntity(i);
                int compt = 0;
                for (int j = indexMntPositifMin; j < entManager.size(); j++) {
                    compt++;
                    setProgressDescription(entFactureNeg.getIdExterneRole() + " <br>" + compt + "/" + entManager.size()
                            + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                                + entFactureNeg.getIdExterneRole() + " <br>" + compt + "/" + entManager.size() + "<br>");
                        if ((getParent() != null) && getParent().isAborted()) {
                            getParent().setProcessDescription(
                                    "Traitement interrompu<br> sur l'affilié : " + entFactureNeg.getIdExterneRole()
                                            + " <br>" + compt + "/" + entManager.size() + "<br>");
                        }
                        break;
                    } else {
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
                            // prendre le prochain décompte négatifs si remis à
                            // zéro
                            break;
                        }
                    }
                }
            }
        }

        return !getTransaction().hasErrors();
    }

    /**
     * Creation d'une facture pour la compensation d'annexe.
     * 
     * @param entFacture L entete de facture.
     * @param sec La section.
     * @param montantCompFacture Le montant a compenser.
     * @param aQuittancer A quittancer ou non.
     * @param remarque Une remarque.
     * @return True si aucune erreurs.
     */
    public boolean _creerAfactCompensationAnnexe(FAEnteteFacture entFacture, APISection sec, String montantCompFacture,
            boolean aQuittancer, String remarque) {

        // compenser à hauteur de la section
        FAAfact afactDeCompensation = new FAAfact();
        afactDeCompensation.setSession(getSession());

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
        afactDeCompensation.setIdSection(sec.getIdSection());

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

    /**
     * Compensation du compte annexe.
     * 
     * @param sections Les sections disponibles.
     * @param enteteFacture L'entete de facture.
     * @param compteAnnexe Le compte annexe.
     * @return True si aucune erreurs.
     */
    protected boolean _doCompenserAnnexe(Collection sections, FAEnteteCompteAnnexe enteteFacture,
            CACompteAnnexe compteAnnexe) {

        try {
            FAEnteteFactureManager entManager = new FAEnteteFactureManager();
            entManager.setISession(getSession());
            entManager.setForIdPassage(getIdPassage());
            entManager.setForIdExterneRole(enteteFacture.getIdExterneRole());
            entManager.setForIdRole(enteteFacture.getIdRoles()); // PO 9130

            FWCurrency totalFacture = new FWCurrency(enteteFacture.getTotalDecomptes());
            if (totalFacture.isNegative()) {
                entManager.setOrderBy(" TOTALFACTURE ASC ");
            } else if (totalFacture.isPositive()) {
                entManager.setOrderBy(" TOTALFACTURE DESC ");
            }

            FASectionsACompenserHelper sectHelper = new FASectionsACompenserHelper();
            sectHelper.initSectionsACompenser(sections);

            FWCurrency posMontantRestantFacture;
            boolean isSectionCompenserTotalement;
            boolean aQuittancer;
            String remarque;
            FAEnteteFacture entFacture;

            boolean passerAuSuivant = false;

            // prendre les entêtes de la facturation une à une et compenser
            BStatement statement = entManager.cursorOpen(getTransaction());
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

                FASectionHelper sectionACompenser;

                // Tant qu'il y a des sections à compenser....
                while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                    if (posMontantRestantFacture.isNegative() || posMontantRestantFacture.isZero()) {
                        break;
                    }

                    CASection section = (CASection) sectionACompenser.getSection();
                    int typeSection = JadeStringUtil.parseInt(section.getIdTypeSection(), 0);

                    if (isBonTypedeSection(typeSection)) {
                        isSectionCompenserTotalement = false;

                        aQuittancer = false;

                        // Si la section est en mode report on ne la prend pas.
                        if (isModeCompensation(section)) {
                            isSectionCompenserTotalement = true;
                            compenserTotalementSection(sectHelper, sectionACompenser);
                            continue;
                        }

                        Result r = appliquerRegleCompensation(entFacture, montantFacture, section, compteAnnexe);

                        if (r == Result.AQUITTANCERNON) {
                            aQuittancer = false;
                        }

                        if (r == Result.AQUITTANCEROUI) {
                            aQuittancer = true;
                        }

                        if (r == Result.PASCOMPENSATION) {
                            // Comme on ne veut pas utiliser cette section pour
                            // compenser la facturation
                            // On la met dans le même état que si elle avait été
                            // compensé totalement.
                            isSectionCompenserTotalement = true;
                            compenserTotalementSection(sectHelper, sectionACompenser);
                            continue;
                        }

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
                            isSectionCompenserTotalement = true;
                        }
                        // Compensation partielle
                        else if (posMontantRestantFacture.isPositive()
                                && (posMontantRestantFacture.compareTo(posMontantSection) < 0)) {

                            montantACompenser = new FWCurrency(posMontantRestantFacture.toString());
                            isSectionCompenserTotalement = false;
                        }

                        // CAS montant facture > 0 --> montant sections < 0
                        // On va compenser avec des montant négatif
                        if (montantFacture.isPositive()) {
                            montantACompenser.negate();
                        }

                        // Si le compte est a un solde positif avec un motif de contentieux bloqué et actif mettre une
                        // remarque pour information
                        if (montantSectionACompenser.isPositive()
                                && compteAnnexe.isCompteBloqueEtActif(passage.getDateFacturation())) {

                            remarque = getSession().getLabel("COMPTEANNEXE_CONTENTIEUX_MOTIF")
                                    + CACodeSystem.getLibelle(getSession(), compteAnnexe.getIdContMotifBloque());
                        }
                        // Si le compte est a un solde positif avec une section avec contentieux mettre une remarque
                        // pour information
                        else if (montantSectionACompenser.isPositive()) {
                            Collection<CASection> collec = compteAnnexe.propositionCompensation(
                                    APICompteAnnexe.PC_TYPE_MONTANT, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                    entFacture.getTotalFacture(), false);

                            Iterator<CASection> it = collec.iterator();
                            while (it.hasNext()) {
                                APISection sec = it.next();
                                if (sec.getContentieuxEstSuspendu().booleanValue()) {
                                    remarque = getSession().getLabel("SECTION_CONTENTIEUX");
                                    break;
                                }
                            }
                        }

                        _creerAfactCompensationAnnexe(entFacture, sectionACompenser.getSection(),
                                montantACompenser.toString(), aQuittancer, remarque);

                        soldeMontantACompenser.sub(montantACompenser);

                        montantACompenser.abs();
                        posMontantRestantFacture.sub(montantACompenser);

                        // Update de la section
                        if (isSectionCompenserTotalement) {
                            sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                            sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        } else {
                            sectionACompenser.setStatus(FASectionHelper.PARTIELLEMENT_COMPENSEE);
                            sectionACompenser.setMontantACompenser(soldeMontantACompenser);
                        }

                        sectHelper.updateSection(sectionACompenser);
                    } else {
                        isSectionCompenserTotalement = true;
                        compenserTotalementSection(sectHelper, sectionACompenser);
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Ne trouve pas d'entete de facture pour le tiers:" + enteteFacture.getIdTiers() + e,
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        return !getTransaction().hasErrors();
    }

    protected void compenserTotalementSection(FASectionsACompenserHelper sectHelper, FASectionHelper sectionACompenser) {
        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
        sectionACompenser.setMontantACompenser(new FWCurrency(0));
        sectHelper.updateSection(sectionACompenser);
    }

    protected boolean isBonTypedeSection(int typeSection) {
        boolean isGoodType = typeSection != 16;
        isGoodType &= typeSection != 21;
        isGoodType &= typeSection != 28;
        isGoodType &= typeSection != 29;
        isGoodType &= typeSection != 15;
        return isGoodType;
    }

    protected boolean isModeCompensation(CASection section) {
        boolean isModeReport = APISection.MODE_REPORT.equals(section.getIdModeCompensation());
        isModeReport |= APISection.MODE_COMP_COMPLEMENTAIRE.equals(section.getIdModeCompensation());
        isModeReport |= APISection.MODE_COMP_CONT_EMPLOYEUR.equals(section.getIdModeCompensation());
        isModeReport |= APISection.MODE_COMP_COT_PERS.equals(section.getIdModeCompensation());
        isModeReport |= APISection.MODE_COMP_DEC_FINAL.equals(section.getIdModeCompensation());

        return isModeReport;
    }

    /**
     * Compensation des décomptes.
     * 
     * @param entFactureNeg Entete de facturation négatif.
     * @param entFacturePos Entete de facturation positif.
     * @return True si aucune erreurs.
     */
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
        entManager.setForIdRole(enteteCompte.getIdRoles()); // PO 9130
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
                        idExterne = entFacture.getIdExterneFacture();
                        if (!entFacture.getIdExterneRole().equals(dernierNumAff)
                                || !entFacture.getIdExterneFacture().equals(dernierIdExterne)) {
                            dernierNumAff = entFacture.getIdExterneRole();
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

                        if (numAffilie.equals(dernierNumAff) && idExterne.equals(dernierIdExterne)) {
                            int typeSection = JadeStringUtil.parseInt(section.getIdTypeSection(), 0);
                            if ((typeSection != 16) && (typeSection != 21) && (typeSection != 28)
                                    && (typeSection != 29)) {
                                aQuittancer = false;
                                remarque = "";

                                dernierNumAff = entFacture.getIdExterneRole();
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
        /*
         * Proposition de compensation de la facturation en compte annexe Prendre tous les décomptes du passage, le
         * grouper par tiers (plus petit au plus grand) et par la somme des décomptes pour ce tiers (décomptes du plus
         * grand au plus petit)
         */
        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        BStatement statement = null;
        entManager.setUseManagerForCompensationAnnexe(true);
        entManager.setSession(getSession());
        entManager.setForIdPassage(passage.getIdPassage());
        entManager.setForTotalFactureNotBetween(true);
        entManager._setGroupBy(" IDTIERS, IDEXTERNEROLE, NONIMPRIMABLE, IDROLE, MODIMP ");
        entManager.setOrderBy(" IDEXTERNEROLE ASC, TOTALDECOMPTES DESC");

        try {
            statement = entManager.cursorOpen(getTransaction());

            // initialiser le module de la comptabilité d'Osiris
            app = (FAApplication) getSession().getApplication();

            FAEnteteCompteAnnexe entete = null;
            int compt = 0;
            while ((!getTransaction().hasErrors())
                    && ((entete = (FAEnteteCompteAnnexe) entManager.cursorReadNext(statement)) != null)) {
                compt++;
                setProgressDescription(entete.getIdExterneRole() + " <br>" + compt + "/" + entManager.size() + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entete.getIdExterneRole()
                            + " <br>" + compt + "/" + entManager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + entete.getIdExterneRole() + " <br>"
                                        + compt + "/" + entManager.size() + "<br>");
                    }
                    break;
                } else {
                    /*
                     * un fake entity qui calcule le somme des décomptes qui ont un montant de signe opposé dans la
                     * compta annexe
                     */
                    if (!entete.isNew() && (!(new FWCurrency(entete.getTotalDecomptes())).isZero())) {
                        // utiliser la comptabilité annexe
                        try {
                            new FWCurrency(entete.getTotalDecomptes());
                            CACompteAnnexe cpt = null;

                            // Recher du compte annexe
                            CACompteAnnexeManager compteAnnexe = new CACompteAnnexeManager();
                            compteAnnexe.setISession(app.getSessionOsiris(getSession()));
                            compteAnnexe.setForIdTiers(entete.getIdTiers());
                            compteAnnexe.setForIdExterneRole(entete.getIdExterneRole());
                            compteAnnexe.setForIdRole(entete.getIdRoles());
                            try {
                                compteAnnexe.find(getTransaction());
                                if (compteAnnexe.size() == 1) {
                                    cpt = (CACompteAnnexe) compteAnnexe.getFirstEntity();
                                    if ((cpt != null)
                                            && isCompensationPossible(new FWCurrency(entete.getTotalDecomptes()),
                                                    new FWCurrency(cpt.getSolde()))) {

                                        // la collection de proposition de
                                        // compensation
                                        Collection sectionCol = cpt
                                                .propositionCompensation(APICompteAnnexe.PC_TYPE_FACTURATION,
                                                        APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                                        entete.getTotalDecomptes(), false);

                                        _doCompenserAnnexe(sectionCol, entete, cpt);
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

    /**
     * Lancer le processus de compensation.
     * 
     * @param passage LE passage de facturation.
     * @return True si aucune erreurs.
     */
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

            if (successful) {
                // appeler la méthode de compensation interne
                successful = _compenserDecompte(entManager, indexMntPositifMin);
                // logger information dans l'email
                this.logInfo4Process(successful, "OBJEMAIL_FA_COMPENSATIONINTERNE");
            }

            if (successful) {
                successful = compenserDecomptePeriodique(passage.getIdPassage());
                this.logInfo4Process(successful, "INFOROM384_OBJEMAIL_COMPENSATION_DECOMPTE_PERIODIQUE");
            }

            if (successful) {
                // compenser en compte Annexe Osiris
                successful = _executeCompenserByAnnexeProcess(passage);
                this.logInfo4Process(successful, "OBJEMAIL_FA_COMPENSATIONEXTERNE");
            }

        } catch (Exception e) {
            successful = false;
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
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());

        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
        }

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
        return estCompense;

    }

    /**
     * Obtient l index du montant positif minimum.
     * 
     * @param entManager manager.
     * @return l'index.
     */
    public int _getIndexMontantPositifMin(FAEnteteFactureManager entManager) {
        // index dans le manager à calculer se référant au montant positif
        // minimum
        int indexMntPositifMin = 0;
        FWCurrency currentMontant;

        for (int i = 0; i < entManager.size(); i++) {
            FAEnteteFacture entFacture = (FAEnteteFacture) entManager.getEntity(i);
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

    /**
     * 
     * Report des montants minimes Prendre tous les décomptes du passage, les grouper par tiers (plus petit au plus
     * grand) et par la somme des décomptes pour ce tiers (décomptes du plus grand au plus petit)
     * 
     * @param passage Le passage.
     * @return True si aucune erreurs.
     */
    private boolean _reporterMontantMinime(IFAPassage passage) {

        FAEnteteFactureManager entManager = new FAEnteteFactureManager();
        BStatement statement = null;
        try {
            entManager.setUseManagerForCompensationAnnexe(true);
            entManager.setSession(getSession());
            entManager.setForIdPassage(passage.getIdPassage());
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

    protected Result appliquerRegleCompensation(FAEnteteFacture entFacture, FWCurrency montantFacture,
            CASection section, CACompteAnnexe compteAnnexe) throws Exception {
        // Si c'est une facture pour les étudiants, les compensations ne seront
        // jamais à valider
        if (APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT.equals(entFacture.getIdSousType())) {
            return Result.AQUITTANCERNON;
        } else {
            if (montantFacture.isNegative()) {
                // Si c'est une note de crédit et et que l'affilié est radié
                // on verifie s'il a du contentieux
                if (isAffiliationRadie(entFacture)) {
                    return hasContentieux(section, compteAnnexe) ? Result.AQUITTANCEROUI : Result.AQUITTANCERNON;
                } else {
                    if (isSectionEchue(section, passage)) {
                        return hasContentieux(section, compteAnnexe) ? Result.AQUITTANCEROUI : Result.AQUITTANCERNON;
                    } else {
                        return Result.PASCOMPENSATION;
                    }
                }
            } else {
                return hasContentieux(section, compteAnnexe) ? Result.AQUITTANCEROUI : Result.AQUITTANCERNON;
            }
        }

    }

    private boolean compenserDecomptePeriodique(String idPassage) throws Exception {

        try {
            FACompensationDecomptePeriodiqueManager mgrCompensationDecomptePeriodique = new FACompensationDecomptePeriodiqueManager();
            mgrCompensationDecomptePeriodique.setSession(getSession());
            mgrCompensationDecomptePeriodique.setForIdPassage(idPassage);
            mgrCompensationDecomptePeriodique.find(BManager.SIZE_NOLIMIT);

            Map<String, FABeanCompensationDecomptePeriodique> mapBeanCompensationDecomptePeriodique = new HashMap<String, FABeanCompensationDecomptePeriodique>();
            Map<String, FABeanCompensationDecomptePeriodiqueFacture> mapBeanCompensationDecomptePeriodiqueFacture = null;
            Map<String, FABeanCompensationDecomptePeriodiqueSection> mapBeanCompensationDecomptePeriodiqueSection = null;

            for (int i = 0; i < mgrCompensationDecomptePeriodique.getSize(); i++) {
                FACompensationDecomptePeriodique entityCompensationDecomptePeriodique = (FACompensationDecomptePeriodique) mgrCompensationDecomptePeriodique
                        .getEntity(i);

                String theKey = entityCompensationDecomptePeriodique.getNumeroAffilie() + "_"
                        + entityCompensationDecomptePeriodique.getRole();
                String theKeyFacture = entityCompensationDecomptePeriodique.getIdFacture();
                String theKeySection = entityCompensationDecomptePeriodique.getIdSection();

                if (!mapBeanCompensationDecomptePeriodique.containsKey(theKey)) {
                    FABeanCompensationDecomptePeriodique beanCompensationDecomptePeriodique = new FABeanCompensationDecomptePeriodique();
                    beanCompensationDecomptePeriodique.setIdTiers(entityCompensationDecomptePeriodique.getIdTiers());
                    beanCompensationDecomptePeriodique.setNumeroAffilie(entityCompensationDecomptePeriodique
                            .getNumeroAffilie());
                    beanCompensationDecomptePeriodique.setRole(entityCompensationDecomptePeriodique.getRole());
                    beanCompensationDecomptePeriodique.setCompteAnnexeASurveiller(entityCompensationDecomptePeriodique
                            .getCompteAnnexeASurveiller());
                    beanCompensationDecomptePeriodique.setIdPassage(idPassage);
                    mapBeanCompensationDecomptePeriodique.put(theKey, beanCompensationDecomptePeriodique);
                }

                mapBeanCompensationDecomptePeriodiqueFacture = mapBeanCompensationDecomptePeriodique.get(theKey)
                        .getMapFacture();

                if (!mapBeanCompensationDecomptePeriodiqueFacture.containsKey(theKeyFacture)) {
                    FABeanCompensationDecomptePeriodiqueFacture beanCompensationDecomptePeriodiqueFacture = new FABeanCompensationDecomptePeriodiqueFacture();
                    beanCompensationDecomptePeriodiqueFacture.setIdFacture(entityCompensationDecomptePeriodique
                            .getIdFacture());
                    beanCompensationDecomptePeriodiqueFacture.setMontant(entityCompensationDecomptePeriodique
                            .getMontantFacture());

                    mapBeanCompensationDecomptePeriodiqueFacture.put(theKeyFacture,
                            beanCompensationDecomptePeriodiqueFacture);
                }

                mapBeanCompensationDecomptePeriodiqueSection = mapBeanCompensationDecomptePeriodique.get(theKey)
                        .getMapSection();
                if (!mapBeanCompensationDecomptePeriodiqueSection.containsKey(theKeySection)) {
                    FABeanCompensationDecomptePeriodiqueSection beanCompensationDecomptePeriodiqueSection = new FABeanCompensationDecomptePeriodiqueSection();
                    beanCompensationDecomptePeriodiqueSection.setIdSection(entityCompensationDecomptePeriodique
                            .getIdSection());
                    beanCompensationDecomptePeriodiqueSection.setMontant(entityCompensationDecomptePeriodique
                            .getMontantSection());
                    beanCompensationDecomptePeriodiqueSection
                            .setIdExterneFactureCompensation(entityCompensationDecomptePeriodique
                                    .getIdExterneFactureCompensation());
                    beanCompensationDecomptePeriodiqueSection
                            .setIdTypeFactureCompensation(entityCompensationDecomptePeriodique
                                    .getIdTypeFactureCompensation());

                    mapBeanCompensationDecomptePeriodiqueSection.put(theKeySection,
                            beanCompensationDecomptePeriodiqueSection);
                }

            }

            for (Map.Entry<String, FABeanCompensationDecomptePeriodique> aBeanCompensationDecomptePeriodique : mapBeanCompensationDecomptePeriodique
                    .entrySet()) {
                for (Map.Entry<String, FABeanCompensationDecomptePeriodiqueFacture> aBeanCompensationDecomptePeriodiqueFacture : aBeanCompensationDecomptePeriodique
                        .getValue().getMapFacture().entrySet()) {
                    for (Map.Entry<String, FABeanCompensationDecomptePeriodiqueSection> aBeanCompensationDecomptePeriodiqueSection : aBeanCompensationDecomptePeriodique
                            .getValue().getMapSection().entrySet()) {

                        FWCurrency montantFacture = new FWCurrency(aBeanCompensationDecomptePeriodiqueFacture
                                .getValue().getMontant());
                        FWCurrency montantSection = new FWCurrency(aBeanCompensationDecomptePeriodiqueSection
                                .getValue().getMontant());

                        if (montantFacture.doubleValue() == 0) {
                            break;
                        }

                        if (montantSection.doubleValue() == 0) {
                            continue;
                        }

                        FWCurrency montantCompensation = null;
                        if (montantFacture.doubleValue() <= Math.abs(montantSection.doubleValue())) {
                            montantCompensation = new FWCurrency(montantFacture.toString());
                            montantCompensation.negate();
                        } else {
                            montantCompensation = new FWCurrency(montantSection.toString());
                        }

                        boolean aQuittancer = false;
                        String remarque = "";
                        if (aBeanCompensationDecomptePeriodique.getValue().getCompteAnnexeASurveiller()) {
                            aQuittancer = true;
                            remarque = getSession().getLabel("INFOROM384_AFACT_A_QUITTANCER_CA_A_SURVEILLER");
                        }

                        FAAfact afactDeCompensation = new FAAfact();
                        afactDeCompensation.setSession(getSession());
                        afactDeCompensation.setIdPassage(idPassage);
                        afactDeCompensation.setIdEnteteFacture(aBeanCompensationDecomptePeriodiqueFacture.getValue()
                                .getIdFacture());
                        afactDeCompensation.setIdModuleFacturation(globaz.musca.external.ServicesFacturation
                                .getIdModFacturationByType(getSession(), getTransaction(),
                                        FAModuleFacturation.CS_MODULE_COMPENSATION_NEW));
                        afactDeCompensation.setIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
                        afactDeCompensation
                                .setIdExterneRubrique(getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE));
                        afactDeCompensation.setMontantFacture(montantCompensation.toString());

                        afactDeCompensation.setIdExterneFactureCompensation(aBeanCompensationDecomptePeriodiqueSection
                                .getValue().getIdExterneFactureCompensation());
                        afactDeCompensation.setIdTypeFactureCompensation(aBeanCompensationDecomptePeriodiqueSection
                                .getValue().getIdTypeFactureCompensation());
                        afactDeCompensation.setIdExterneDebiteurCompensation(aBeanCompensationDecomptePeriodique
                                .getValue().getNumeroAffilie());
                        afactDeCompensation.setIdTiersDebiteurCompensation(aBeanCompensationDecomptePeriodique
                                .getValue().getIdTiers());
                        afactDeCompensation.setIdRoleDebiteurCompensation(aBeanCompensationDecomptePeriodique
                                .getValue().getRole());
                        afactDeCompensation.setIdSection(aBeanCompensationDecomptePeriodiqueSection.getValue()
                                .getIdSection());

                        afactDeCompensation.setAQuittancer(new Boolean(aQuittancer));
                        afactDeCompensation.setRemarque(remarque);

                        afactDeCompensation.add(getTransaction());

                        montantFacture.add(montantCompensation);
                        montantSection.sub(montantCompensation);
                        aBeanCompensationDecomptePeriodiqueFacture.getValue().setMontant(montantFacture.toString());
                        aBeanCompensationDecomptePeriodiqueSection.getValue().setMontant(montantSection.toString());
                    }

                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("INFOROM384_EXCEPTION_COMPENSATION_DECOMPTE_PERIODIQUE") + " : "
                            + e.toString(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        }

        return true;

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

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
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

    protected boolean hasContentieux(CASection section, CACompteAnnexe compteAnnexe) throws CATechnicalException {

        if (isCAContentieux(compteAnnexe)) {
            return true;
        } else {
            if (isSectionContentieux(section)) {
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean isAffiliationRadie(FAEnteteFacture entFacture) throws Exception {
        if (JadeStringUtil.isEmpty(entFacture.getIdTiers()) || JadeStringUtil.isEmpty(entFacture.getIdExterneRole())
                || JadeStringUtil.isEmpty(entFacture.getIdExterneFacture())) {
            return false;
        }
        AFAffiliation affToUse = AFAffiliationUtil.loadAffiliation(getSession(), entFacture.getIdTiers(),
                entFacture.getIdExterneRole(), entFacture.getIdExterneFacture(), entFacture.getIdRole());
        if (affToUse == null) {
            return false;
        } else {
            setDateFinAffiliation(affToUse.getDateFin());
            return !JadeStringUtil.isEmpty(affToUse.getDateFin());
        }
    }

    private boolean isCAContentieux(CACompteAnnexe compteAnnexe) {
        if (compteAnnexe.isASurveiller().booleanValue() || compteAnnexe.isMotifExistant(CACodeSystem.CS_RENTIER)
                || compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)) {
            return true;
        }
        return false;
    }

    protected boolean isCompensationPossible(FWCurrency totalFacture, FWCurrency totalCompteAnnexe) {
        return (totalFacture.isNegative() && totalCompteAnnexe.isPositive())
                || (totalFacture.isPositive() && totalCompteAnnexe.isNegative());
    }

    private boolean isSectionContentieux(CASection section) throws CATechnicalException {
        if (section.hasMotifContentieux(CACodeSystem.CS_RENTIER)
                || section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE) || section.isSectionAuxPoursuites(true)
                || !JadeStringUtil.isBlankOrZero(section.getIdPlanRecouvrement())) {
            return true;
        }
        return false;
    }

    protected boolean isSectionEchue(CASection section, FAPassage passage) throws Exception {

        JADate dateEcheanceSection = new JADate(section.getDateEcheance());
        JADate dateFactu = new JADate(passage.getDateFacturation());
        JACalendar cal = getSession().getApplication().getCalendar(); // new

        if (cal.compare(dateFactu, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
            String dateExecutionEtape = section._getDateExecutionSommation();
            if (JadeStringUtil.isBlank(dateExecutionEtape)) {
                return false;
            } else {
                JADate dateSommation = new JADate(dateExecutionEtape);
                dateSommation = cal.addDays(dateSommation, 15);

                return cal.compare(dateFactu, dateSommation) == JACalendar.COMPARE_FIRSTUPPER;
            }
        }
        return false;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }
}
