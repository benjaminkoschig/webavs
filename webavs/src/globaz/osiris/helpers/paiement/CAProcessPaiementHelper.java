package globaz.osiris.helpers.paiement;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAPaiementEtranger;
import globaz.osiris.db.comptes.CAPaiementEtrangerViewBean;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.external.IntRole;
import globaz.osiris.file.paiement.exception.LabelNameException;

/**
 * Classe : type_conteneur Description : Date de création: 4 mars 04
 * 
 * @author scr
 */
public class CAProcessPaiementHelper {

    private BSession session = null;
    private BTransaction transaction = null;

    /**
     * Constructor for ProcessPaiementHelper.
     */
    public CAProcessPaiementHelper(BSession session, BTransaction transaction) throws Exception {
        this.session = session;
        if (transaction == null) {
            this.transaction = (BTransaction) session.newTransaction();
        } else {
            this.transaction = transaction;
        }
    }

    /**
     * Method compenserSectionManuelle. Compense les sections d'un compte à partir d'un montant donnée, en partant de la
     * plus ancienne à la plus récente (tant que le solde du montant n'est pas nulle)
     * 
     * @param compteAnnexe
     *            Compte annexe des sections à compenser
     * @param libelle
     *            Libelle du paiement
     * @param idOrganeExecution
     *            Référence l'organe d'exécution du compte débité
     * @param montantCompensatoir
     *            Le montant à compenser
     * @param jdate
     *            Date du paiement
     * @param idJournal
     *            Référence le journal ou seront inscrits les paiements
     * @throws LabelNameException
     *             Exception contenant un label pour internationalisation du message à afficher
     * @throws Exception
     */
    // public void compenserSectionManuelle(CACompteAnnexe compteAnnexe,
    // String libelle,
    // String idOrganeExecution,
    // FWCurrency montantCompensatoir,
    // String date,
    // String idJournal) throws LabelNameException, Exception {
    //
    // FWCurrency montantPourProposition = new
    // FWCurrency(montantCompensatoir.toString());
    // montantPourProposition.negate();
    //
    // String[] args = new String[1];
    // args[0] = new String(montantPourProposition.toString());
    // //If we get here, all data are well formatted and have relevant value.
    // Collection sectionsACompenser =
    // compteAnnexe.propositionCompensation(CACompteAnnexe.PC_TYPE_MONTANT,
    // CACompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
    // args);
    //
    // if (sectionsACompenser==null || sectionsACompenser.size()==0)
    // throw new LabelNameException("PMT_ETRANGER_NO_SECTION_A_COMPENSER", "");
    //
    // CAOrganeExecution organeExec = new CAOrganeExecution();
    // organeExec.setSession(session);
    // organeExec.setIdOrganeExecution(idOrganeExecution);
    // organeExec.retrieve(transaction);
    // if (organeExec==null || organeExec.isNew())
    // throw new LabelNameException("PMT_ETRANGER_ORGANE_EXEC_ERROR", "");
    //
    // String idRubrique = organeExec.getIdRubrique();
    //
    // for (Iterator iter = sectionsACompenser.iterator(); iter.hasNext(); ) {
    //
    // CASection section = (CASection) iter.next();
    //
    //
    // FWCurrency solde = section.getSoldeToCurrency();
    //
    // //montantCompensatoir > solde
    // if (montantCompensatoir.compareTo(solde)==1) {
    //
    // boolean isLastSection = !iter.hasNext();
    //
    // CAPaiementEtranger paiement = new CAPaiementEtranger();
    // paiement.setIdJournal(idJournal);
    // paiement.setDate(date);
    // paiement.setLibelle(libelle);
    // paiement.setIdCompte(idRubrique);
    // paiement.setCodeDebitCredit(CAEcriture.CREDIT);
    // paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
    // paiement.setIdSection(section.getIdSection());
    // paiement.setIdTypeOperation(CAEcriture.CAPAIEMENTETRANGER);
    //
    //
    // //Derniere section a compenser -> on compense avec la totalite du montant
    // //(générera un paiement négatif)
    // if (isLastSection) {
    // paiement.setMontant(montantCompensatoir.toString());
    // }
    // //Sinon, compenser avec le solde de la section
    // else {
    // paiement.setMontant(solde.toString());
    // }
    // paiement.add(transaction);
    // montantCompensatoir.sub(solde);
    // }
    //
    // else {
    // //Compenser la facture avec pour valeur : montantCompensatoir
    // CAPaiementEtranger paiement = new CAPaiementEtranger();
    // paiement.setIdJournal(idJournal);
    // paiement.setMontant(montantCompensatoir.toString());
    // paiement.setDate(date);
    // paiement.setLibelle(libelle);
    // paiement.setIdCompte(idRubrique);
    // paiement.setCodeDebitCredit(CAEcriture.CREDIT);
    // paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
    // paiement.setIdSection(section.getIdSection());
    // paiement.setIdTypeOperation(CAEcriture.CAPAIEMENTETRANGER);
    // paiement.add(transaction);
    // break;
    // }
    // }
    // if (session.hasErrors())
    // throw new Exception(session.getErrors().toString());
    // }
    public void addPaiementEtranger(CACompteAnnexe compteAnnexe, String noAvs, CAPaiementEtrangerViewBean viewBean)
            throws Exception {

        String idRubrique = null;

        if (viewBean.getIdCompte() == null) {
            CAOrganeExecution organeExec = new CAOrganeExecution();
            organeExec.setSession(session);
            organeExec.setIdOrganeExecution(viewBean.getIdOrganeExecution());

            organeExec.retrieve(transaction);
            if ((organeExec == null) || organeExec.isNew()) {
                throw new LabelNameException("PMT_ETRANGER_ORGANE_EXEC_ERROR", "");
            }
            idRubrique = organeExec.getIdRubrique();
        } else {
            idRubrique = viewBean.getIdCompte();
        }

        boolean hasError = false;
        if (viewBean.getDate() == null) {
            hasError = true;
            viewBean.getMemoryLog().logMessage(session.getLabel("PMT_ETRANGER_DATE_ERROR"), FWMessage.ERREUR,
                    this.getClass().getName());
        }

        if (compteAnnexe == null) {
            hasError = true;
            viewBean.getMemoryLog().logMessage(session.getLabel("PMT_ETRANGER_NO_AVS_ERROR") + " " + noAvs,
                    FWMessage.ERREUR, this.getClass().getName());
        }

        viewBean.setIdCompte(idRubrique);

        if (compteAnnexe != null) {
            viewBean.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        }

        viewBean.setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        if (hasError) {
            viewBean.setEtat(APIOperation.ETAT_ERREUR);
        }
        viewBean.add(transaction);
    }

    public void addPaiementEtranger(String idJournal, CACompteAnnexe compteAnnexe, String noAvs, String montantCHF,
            String montantME, String cours, String codeIsoMonnaie, String codeDebitCredit, String date,
            String idOrganeExecution, String dumpRecordFile) throws LabelNameException, Exception {
        String idRubrique = null;

        CAOrganeExecution organeExec = new CAOrganeExecution();
        organeExec.setSession(session);
        organeExec.setIdOrganeExecution(idOrganeExecution);
        organeExec.retrieve(transaction);
        if ((organeExec == null) || organeExec.isNew()) {
            throw new LabelNameException("PMT_ETRANGER_ORGANE_EXEC_ERROR", "");
        }

        idRubrique = organeExec.getIdRubrique();

        CAPaiementEtranger paiement = new CAPaiementEtranger();
        boolean hasError = false;
        if (date == null) {
            hasError = true;
            paiement.getMemoryLog().logMessage(session.getLabel("PMT_ETRANGER_DATE_ERROR"), FWMessage.ERREUR,
                    this.getClass().getName());
        }

        // if (montant==null) {
        // hasError = true;
        // paiement.getMemoryLog().logMessage(
        // session.getLabel("PMT_ETRANGER_MONTANT_ERROR"),
        // FWMessage.ERREUR,
        // getClass().getName());
        // }

        if (compteAnnexe == null) {
            hasError = true;
            paiement.getMemoryLog().logMessage(session.getLabel("PMT_ETRANGER_NO_AVS_ERROR") + " " + noAvs,
                    FWMessage.ERREUR, this.getClass().getName());
        }

        try {
            validateCompteAnnexeIsASurveillerEstVerrouille(compteAnnexe);
        } catch (Exception e) {
            hasError = true;
            paiement.getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        paiement.setIdJournal(idJournal);
        paiement.setMontant(montantCHF);
        paiement.setMontantME(montantME);
        paiement.setCodeIsoME(codeIsoMonnaie);
        paiement.setCoursME(cours);
        paiement.setDate(date);
        paiement.setIdCompte(idRubrique);
        paiement.setCodeDebitCredit(codeDebitCredit);

        if (compteAnnexe != null) {
            paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        }

        paiement.setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        if (hasError) {
            paiement.setEtat(APIOperation.ETAT_ERREUR);
            paiement.getMemoryLog().logMessage("File record = " + dumpRecordFile, FWMessage.ERREUR,
                    this.getClass().getName());

        }
        paiement.add(transaction);
    }

    public String formatNoAvs(String noAvs) {
        if ((noAvs == null) || (noAvs.length() < 11)) {
            return null;
        }

        StringBuffer formattedNoAvs = new StringBuffer();
        formattedNoAvs.append(noAvs.substring(0, 3)).append(".");
        formattedNoAvs.append(noAvs.substring(3, 5)).append(".");
        formattedNoAvs.append(noAvs.substring(5, 8)).append(".");
        formattedNoAvs.append(noAvs.substring(8, 11));

        return formattedNoAvs.toString();
    }

    public void updatePaiementEtranger(CAPaiementEtranger paiement, CACompteAnnexe compteAnnexe,
            String idOrganeExecution) throws LabelNameException, Exception {

        paiement.setMemoryLog(null);
        String idRubrique = null;

        if (paiement.getIdCompte() == null) {
            CAOrganeExecution organeExec = new CAOrganeExecution();
            organeExec.setSession(session);
            organeExec.setIdOrganeExecution(idOrganeExecution);
            organeExec.retrieve(transaction);
            if ((organeExec == null) || organeExec.isNew()) {
                throw new LabelNameException("PMT_ETRANGER_ORGANE_EXEC_ERROR", "");
            }
            idRubrique = organeExec.getIdRubrique();
        } else {
            idRubrique = paiement.getIdCompte();
        }

        paiement.setIdCompte(idRubrique);

        if (compteAnnexe == null) {
            paiement.getMemoryLog().logMessage(session.getLabel("PMT_ETRANGER_NO_AVS_ERROR"), FWMessage.ERREUR,
                    this.getClass().getName());
        }

        else {
            paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        }

        paiement.setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        paiement.setEtat(APIOperation.ETAT_OUVERT);
        paiement.update(transaction);
    }

    public void validateCompteAnnexeIsASurveillerEstVerrouille(CACompteAnnexe compteAnnexe) throws Exception {
        if ((compteAnnexe != null) && (compteAnnexe.isASurveiller().booleanValue())) {
            throw new Exception(session.getLabel("SOUS_SURVEILLANCE") + " (" + compteAnnexe.getIdExterneRole() + ")");
        }

        if ((compteAnnexe != null) && (compteAnnexe.isVerrouille())) {
            throw new Exception(session.getLabel("VERROUILLE") + " (" + compteAnnexe.getIdExterneRole() + ")");
        }
    }

    public void validateData(String montant, String noAvs, String date, boolean quittancer) throws LabelNameException,
            Exception {
        validateDate(date);
        validateMontant(montant);
        validateNoAvs(noAvs, false, quittancer);
    }

    // /**
    // * Method compenserSectionsAutomatique.
    // *
    // * Compense les sections d'un compte à partir d'un montant donnée, en
    // partant de la
    // * plus ancienne à la plus récente (tant que le solde du montant n'est pas
    // nulle)
    // *
    // * @param compteAnnexe Compte annexe des sections à compenser
    // * @param journal journal contenant le paiement
    // * @param idOrganeExecution Référence l'organe d'exécution du compte
    // débité
    // * @param montantCompensatoir Le montant à compenser
    // * @param jdate Date du paiement
    // * @throws LabelNameException Exception contenant un label pour
    // internationalisation du message à afficher
    // * @throws Exception
    // */
    // public void compenserSectionsAutomatique(CACompteAnnexe compteAnnexe,
    // String idJournal,
    // String idOrganeExecution,
    // FWCurrency montantCompensatoir,
    // String date) throws LabelNameException, Exception {
    //
    //
    // FWCurrency montantPourProposition = new
    // FWCurrency(montantCompensatoir.toString());
    // montantPourProposition.negate();
    //
    // String[] args = new String[1];
    // args[0] = new String(montantPourProposition.toString());
    // Collection sectionsACompenser =
    // compteAnnexe.propositionCompensation(CACompteAnnexe.PC_TYPE_MONTANT,
    // CACompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
    // args);
    //
    // if (sectionsACompenser==null || sectionsACompenser.size()==0)
    // throw new LabelNameException("PMT_ETRANGER_NO_SECTION_A_COMPENSER", "");
    //
    //
    // CAOrganeExecution organeExec = new CAOrganeExecution();
    // organeExec.setSession(session);
    // organeExec.setIdOrganeExecution(idOrganeExecution);
    // organeExec.retrieve(transaction);
    // if (organeExec==null || organeExec.isNew())
    // throw new LabelNameException("PMT_ETRANGER_ORGANE_EXEC_ERROR", "");
    //
    // String idRubrique = organeExec.getIdRubrique();
    //
    //
    // for (Iterator iter = sectionsACompenser.iterator(); iter.hasNext(); ) {
    // CASection section = (CASection) iter.next();
    //
    // FWCurrency solde = section.getSoldeToCurrency();
    //
    // if (montantCompensatoir.compareTo(solde)==1) {
    //
    // boolean isLastSection = !iter.hasNext();
    //
    // CAPaiementEtranger paiement = new CAPaiementEtranger();
    // paiement.setIdJournal(idJournal);
    // paiement.setDate(date);
    // paiement.setIdCompte(idRubrique);
    // paiement.setCodeDebitCredit(CAEcriture.CREDIT);
    // paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
    // paiement.setIdSection(section.getIdSection());
    // paiement.setIdTypeOperation(CAEcriture.CAPAIEMENTETRANGER);
    //
    //
    // //Derniere section a compenser -> on compense avec la totalite du montant
    // //(générera un paiement négatif)
    // if (isLastSection) {
    // paiement.setMontant(montantCompensatoir.toString());
    // }
    // //Sinon, compenser avec le solde de la section
    // else {
    // paiement.setMontant(solde.toString());
    // }
    // paiement.add(transaction);
    // montantCompensatoir.sub(solde);
    // }
    //
    // else {
    // //Compenser la facture avec pour valeur : montantCompensatoir
    // CAPaiementEtranger paiement = new CAPaiementEtranger();
    // paiement.setIdJournal(idJournal);
    // paiement.setMontant(montantCompensatoir.toString());
    // paiement.setDate(date);
    // paiement.setIdCompte(idRubrique);
    // paiement.setCodeDebitCredit(CAEcriture.CREDIT);
    // paiement.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
    // paiement.setIdSection(section.getIdSection());
    // paiement.setIdTypeOperation(CAEcriture.CAPAIEMENTETRANGER);
    // paiement.add(transaction);
    // break;
    // }
    // }
    // }

    public JADate validateDate(String date) throws Exception, LabelNameException {
        // Contrôle validation de la date
        JACalendar cal = new JACalendarGregorian();
        JADate jdate = new JADate(date);
        // La date n'est pas valide
        if (!cal.isValid(jdate)) {
            throw new LabelNameException("PMT_ETRANGER_DATE_ERROR", date);
        }
        return jdate;
    }

    public FWCurrency validateMontant(String montant) throws LabelNameException {
        FWCurrency cmontant = null;
        try {
            cmontant = new FWCurrency(montant);
            if (JadeStringUtil.isDecimalEmpty(cmontant.toString())) {
                throw new LabelNameException("PMT_ETRANGER_MONTANT_ERROR", montant);
            }
        } catch (RuntimeException e) {
            throw new LabelNameException("PMT_ETRANGER_MONTANT_ERROR", montant);
        }

        return cmontant;
    }

    public CACompteAnnexe validateNoAvs(String noAvs, boolean isFormatNumeroAvs, boolean quittancer)
            throws LabelNameException, Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);

        compteAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(session.getApplication()));

        String formattedNoAvs = noAvs;

        if (isFormatNumeroAvs) {
            formattedNoAvs = formatNoAvs(noAvs);
        }

        try {
            compteAnnexe.setIdExterneRole(formattedNoAvs);
            JAUtil.checkAvs(formattedNoAvs);
            compteAnnexe.retrieve(transaction);

            if (compteAnnexe.isNew() && quittancer) {
                CACompteAnnexe newCompteAnnexe = new CACompteAnnexe();
                newCompteAnnexe.setSession(session);

                newCompteAnnexe.setIdExterneRole(formattedNoAvs);
                newCompteAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        session.getApplication()));

                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                IntRole role = (IntRole) globaz.globall.db.GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(session, IntRole.class);
                role.retrieve(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(session.getApplication()),
                        formattedNoAvs);

                if (role.isNew()) {
                    throw new LabelNameException("PMT_ETRANGER_COMPTE_ANNEXE_ERROR", noAvs);
                }
                newCompteAnnexe.setIdTiers(role.getIdTiers());

                newCompteAnnexe.add(transaction);

                if (newCompteAnnexe.hasErrors()) {
                    throw new LabelNameException("PMT_ETRANGER_COMPTE_ANNEXE_ERROR", noAvs);
                }

                compteAnnexe = newCompteAnnexe;
            } else if (compteAnnexe.isNew()) {
                throw new LabelNameException("PMT_ETRANGER_COMPTE_ANNEXE_ERROR", noAvs);
            }
        } catch (LabelNameException lne) {
            throw lne;
        } catch (Exception e) {
            throw new LabelNameException("PMT_ETRANGER_NO_AVS_ERROR", e.getMessage());
        }

        return compteAnnexe;
    }

}
