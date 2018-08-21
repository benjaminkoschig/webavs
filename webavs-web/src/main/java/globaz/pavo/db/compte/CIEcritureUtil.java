package globaz.pavo.db.compte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIMandatSplitting;
import globaz.pavo.util.CIUtil;

/**
 * @author user
 *
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureUtil {
    private CIEcriture ecriture;

    /**
     * Constructeur.
     *
     * @param ecr
     *            l'écriture concernée
     */
    public CIEcritureUtil(CIEcriture ecr) {
        super();
        ecriture = ecr;
    }

    /**
     * Test si l'affilié donné existe dans la période de l'écriture
     *
     * @param debutAff
     *            la date de début d'affiliation
     * @param finAff
     *            la date de fin d'affilation
     * @return true si l'écriture se trouve dans la période d'affiliation
     */
    public boolean checkPerdiodeAff(String debutAff, String finAff) throws Exception {
        if (ecriture == null) {
            return true;
        }
        if (JAUtil.isIntegerEmpty(ecriture.getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {

            String moisD = ecriture.getMoisDebut();
            if (isMoisSpeciaux()) {
                // pas d'info sur les mois, tester uniquement l'année
                int anneeInt = Integer.parseInt(ecriture.getAnnee());
                int anneeDebutAff = Integer.parseInt(debutAff.substring(6));
                if (anneeInt < anneeDebutAff) {
                    return false;
                }
                if (!JAUtil.isDateEmpty(finAff)) {
                    int anneeFinAff = Integer.parseInt(finAff.substring(6));
                    if (anneeInt > anneeFinAff) {
                        return false;
                    }
                }
                return true;
            } else {
                if (JAUtil.isStringEmpty(moisD)) {
                    moisD = "01";
                }
                boolean result = BSessionUtil.compareDateFirstLowerOrEqual(ecriture.getSession(), debutAff,
                        CIUtil.padDate("01." + moisD + "." + ecriture.getAnnee()));
                if (JAUtil.isDateEmpty(finAff)) {
                    return result;
                }
                String moisF = ecriture.getMoisFin();
                if (JAUtil.isStringEmpty(moisF)) {
                    moisF = "12";
                }
                return result & BSessionUtil.compareDateFirstGreaterOrEqual(ecriture.getSession(), finAff,
                        CIUtil.padDate("01." + moisF + "." + ecriture.getAnnee()));
            }
        } else {
            return true;
        }
    }

    /**
     * Teste est ajoute des écritures de splitting par rapport à cette écriture.
     *
     * @param transaction
     *            la transaction à utiliser
     * @return la liste des écritures splittées ou null si aucune écriture présente
     */
    public ArrayList checkPeriode(BTransaction transaction) throws Exception {
        if ((ecriture == null) || JAUtil.isStringEmpty(ecriture.getCompteIndividuelId())
                || (ecriture.getSession() == null)) {
            return null;
        }
        CIPeriodeSplittingManager spliMgr = new CIPeriodeSplittingManager();
        spliMgr.setSession(ecriture.getSession());
        spliMgr.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        spliMgr.find(transaction);
        ArrayList result = null;
        for (int i = 0; i < spliMgr.size(); i++) {
            CIPeriodeSplitting spl = (CIPeriodeSplitting) spliMgr.getEntity(i);
            if (!CIMandatSplitting.CS_PARTAGE_RAM.equals(spl.getParticulier())) {
                result = spl.checkPeriode(transaction, ecriture, true);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Permet de savoir si le total est négatif pour les ecritures spéciales
     *
     * @param typeJournal
     * @return
     */
    public boolean hasSommePositiveEcrituresSpeciales(String typeJournal) {

        return false;
    }

    /**
     * Test si le mois de fin et de début sont égal à 66,77 ou 99
     *
     * @return true si les mois de début et de fin sont spéciaux (66,77,99)
     */
    public boolean isMoisSpeciaux() {
        if (ecriture == null) {
            return false;
        }
        return "66".equals(ecriture.getMoisFin()) || "77".equals(ecriture.getMoisFin())
                || "99".equals(ecriture.getMoisFin());
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     *
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction) throws Exception {
        return this.rechercheCI(transaction, null, true, true);
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     *
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction, String id) throws Exception {
        return this.rechercheCI(transaction, id, true, true);
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     *
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction, String id, boolean wantValidate, boolean wantCreate)
            throws Exception {
        if (transaction.hasErrors()) {
            return false;
        }
        /*
         * if (JAUtil.isStringEmpty(getAvs()) && !CS_CIGENRE_6.equals(getGenreEcriture())) { _addError(transaction,
         * getSession().getLabel("MSG_ECRITURE_AVS")); return false; }
         */
        CICompteIndividuel ciTemp;
        CICompteIndividuelManager ciMng = new CICompteIndividuelManager();
        ciMng.setSession(ecriture.getSession());
        ciMng.orderByAvs(false);
        ciMng.setForNumeroAvs(ecriture.getAvs());
        if (CIEcriture.CS_CIGENRE_6.equals(ecriture.getGenreEcriture())) {
            ciMng.setForNomPrenom(ecriture.getNomPrenom());
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
            ciMng.find(transaction);
            // } else if (CS_CIGENRE_7.equals(getGenreEcriture()) &&
            // Integer.parseInt(getAnnee()) < 1997) {
            // ciMng.setForNomPrenom(getNomPrenom());
            // ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_7);
        } else {
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            if (!JAUtil.isStringEmpty(ecriture.getAvs())) {
                ciMng.find(transaction);
            }
        }
        // ciMng.find(transaction);
        if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
            // Si il existe un CI on s'accroche à celui-là
            // pour un ci au ra, tester si l'écriture n'est pas après une
            // clôture et que le ci est fermé
            CICompteIndividuel ciFound = (CICompteIndividuel) ciMng.getEntity(0);
            ecriture.ci = ciFound;
            ecriture.setCompteIndividuelId(ciFound.getCompteIndividuelId());
            return true;
        }
        if (CIEcriture.CS_CIGENRE_6.equals(ecriture.getGenreEcriture())) {
            // création d'un genre 6
            ciTemp = new CICompteIndividuel();
            if (!JAUtil.isStringEmpty(id)) {
                ciTemp.setCompteIndividuelId(id);
            }
            ciTemp.wantCallValidate(wantValidate);
            ciTemp.setSession(ecriture.getSession());
            ciTemp.setNomPrenom(ecriture.getNomPrenom());
            ciTemp.setSexe(ecriture.getSexe());
            ciTemp.setPaysOrigineId(ecriture.getPaysOrigine());
            ciTemp.setDateNaissance(ecriture.getDateDeNaissance());
            ciTemp.setNumeroAvs(ecriture.getAvs());
            ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
            if ("true".equalsIgnoreCase(ecriture.getAvsNNSS())) {
                ciTemp.setNnss(new Boolean(true));
            }
            ciTemp.setCiOuvert(new Boolean(true));
            if (wantCreate) {
                ciTemp.add(transaction);
            }
            if (!transaction.hasErrors()) {
                ecriture.ci = ciTemp;
                ecriture.setCompteIndividuelId(ciTemp.getCompteIndividuelId());
            } else {
                transaction.addErrors(ecriture.getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW") + ciTemp.getMsgType()
                        + " : " + ciTemp.getMessage());
                return false;
            }
        } else if (CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                && (Integer.parseInt(ecriture.getAnnee()) < 1997)) {
            // existe un genre 7 pour cet assuré
            ciMng.setForNomPrenom(ecriture.getNomPrenom());
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_7);
            ciMng.find(transaction);
            if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
                // Si il existe, on s'accroche à celui-là
                CICompteIndividuel ciFound = (CICompteIndividuel) ciMng.getEntity(0);
                ecriture.ci = ciFound;
                ecriture.setCompteIndividuelId(ciFound.getCompteIndividuelId());
            } else {
                // création d'un genre 7
                ciTemp = new CICompteIndividuel();
                if (!JAUtil.isStringEmpty(id)) {
                    ciTemp.setCompteIndividuelId(id);
                }
                ciTemp.wantCallValidate(wantValidate);
                ciTemp.setSession(ecriture.getSession());
                ciTemp.setNomPrenom(ecriture.getNomPrenom());
                ciTemp.setSexe(ecriture.getSexe());
                ciTemp.setPaysOrigineId(ecriture.getPaysOrigine());
                ciTemp.setDateNaissance(ecriture.getDateDeNaissance());
                ciTemp.setNumeroAvs(ecriture.getAvs());
                ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_7);
                if ("true".equalsIgnoreCase(ecriture.getAvsNNSS())) {
                    ciTemp.setNnss(new Boolean(true));
                }
                ciTemp.setCiOuvert(new Boolean(true));
                if (wantCreate) {
                    ciTemp.add(transaction);
                }
                if (!transaction.hasErrors()) {
                    ecriture.ci = ciTemp;
                    ecriture.setCompteIndividuelId(ciTemp.getCompteIndividuelId());
                } else {
                    transaction.addErrors(ecriture.getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW")
                            + ciTemp.getMsgType() + " : " + ciTemp.getMessage());
                    return false;
                }
            }
        } else {
            // sinon, recherche d'un éventuel CI provisoire
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            if (JAUtil.isStringEmpty(ecriture.getAvs())) {
                ciMng.setForNomPrenom(ecriture.getNomPrenom());
            }
            if (!JAUtil.isStringEmpty(ecriture.getAvs()) || !JAUtil.isStringEmpty(ecriture.getNomPrenom())) {
                ciMng.find(transaction);
            }
            if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
                // Si il existe un CI au registre provisoire correspondant au
                // numéro d'AVS on s'accroche à celui-là
                CICompteIndividuel ciFound = (CICompteIndividuel) ciMng.getEntity(0);
                ecriture.ci = ciFound;
                ecriture.setCompteIndividuelId(ciFound.getCompteIndividuelId());
            } else {
                // création d'un ci provisoire
                ciTemp = new CICompteIndividuel();
                if (!JAUtil.isStringEmpty(id)) {
                    ciTemp.setCompteIndividuelId(id);
                }
                ciTemp.wantCallValidate(wantValidate);
                ciTemp.setSession(ecriture.getSession());
                ciTemp.setNumeroAvs(ecriture.getAvs());
                if ("true".equalsIgnoreCase(ecriture.getAvsNNSS())) {
                    ciTemp.setNnss(new Boolean(true));
                }
                ciTemp.setNomPrenom(ecriture.getNomPrenom());
                ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciTemp.setCiOuvert(new Boolean(true));
                if (wantCreate) {
                    ciTemp.add(transaction);
                }
                if (!transaction.hasErrors()) {
                    ecriture.ci = ciTemp;
                    ecriture.setCompteIndividuelId(ciTemp.getCompteIndividuelId());
                } else {
                    transaction.addErrors(ecriture.getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW")
                            + ciTemp.getMsgType() + " : " + ciTemp.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Test si le total des inscription pour le même employeur et la même année d'un assuré n'est pas négatif. Date de
     * création : (14.11.2002 09:53:16)
     *
     * @param transaction
     *            la transaction a utiliser.
     * @return true si le montant total n'est pas négatif.
     */
    public boolean rechercheEcritureEmp(BTransaction transaction) throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CIEcritureSommeTemp somme = new CIEcritureSommeTemp();

        somme.setSession(ecriture.getSession());
        somme.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        somme.setForAnnee(ecriture.getAnnee());
        if (!ecriture.isNew()) {
            somme.setForNotId(ecriture.getEcritureId());
        }
        somme.setForEmployeur(ecriture.getEmployeur());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result == null) {
            result = new BigDecimal(0);
        }
        if (result.subtract(new BigDecimal(ecriture.getMontant())).signum() == -1) {
            return false;
        }

        return true;
    }

    public boolean rechercheEcritureEmpApg(BTransaction transaction, String TypeJournal, boolean chomage, boolean dt)
            throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CIEcrituresSommeApg somme = new CIEcrituresSommeApg();

        somme.setSession(ecriture.getSession());
        somme.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        if (!ecriture.isNew()) {
            somme.setForNotId(ecriture.getEcritureId());
        }
        somme.setForAnnee(ecriture.getAnnee());
        if (!chomage) {
            somme.setForTypeInscription(TypeJournal);
        } else {
            somme.setCaisseChomage(ecriture.getCaisseChomage());
        }
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result == null) {
            result = new BigDecimal(0);
        }
        if (result.subtract(new BigDecimal(ecriture.getMontant())).signum() == -1) {
            return false;
        }

        return true;
    }

    /**
     * ATTENTION : uniquement pour les affiliés de type Paritaire. Retourne le résultat sans l'ecriture en cours, pour
     * les DT, on ne peut pas le faire dans l'afteradd
     *
     * @param transaction
     * @param employeur
     * @return
     * @throws Exception
     */
    public BigDecimal rechercheEcritureEmpResult(BTransaction transaction, String employeur) throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return new BigDecimal("0");
        }
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
        AFAffiliation aff = new AFAffiliation();
        aff = application.getAffilieByNo(ecriture.getSession(), employeur, true, false, "1", "12", ecriture.getAnnee(),
                "", "");
        CIEcritureSommeTemp somme = new CIEcritureSommeTemp();
        somme.setSession(ecriture.getSession());
        somme.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        somme.setForAnnee(ecriture.getAnnee());
        somme.setForEmployeur(aff.getAffiliationId());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result == null) {
            result = new BigDecimal(0);
        }

        return result;
    }

    public boolean rechercheEcritureEmpTotAnnee(BTransaction transaction) throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CIEcritureSommeTemp somme = new CIEcritureSommeTemp();

        somme.setSession(ecriture.getSession());
        somme.setForNotGenre(CIEcriture.CS_CIGENRE_8);
        somme.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        somme.setForAnnee(ecriture.getAnnee());
        if (!ecriture.isNew()) {
            somme.setForNotId(ecriture.getEcritureId());
        }
        // somme.setForEmployeur(ecriture.getEmployeur());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result == null) {
            result = new BigDecimal(0);
        }

        if (result.subtract(new BigDecimal(ecriture.getMontant())).signum() == -1) {
            return false;
        }

        return true;
    }

    /**
     * Recherche si une écriture existe déjà pour le même employeur et la même année, même période et même montant. Date
     * de création : (14.11.2002 09:53:16)
     *
     * @param transaction
     *            la transaction a utiliser.
     * @return true si au moins une écriture a été trouvée.
     */
    public boolean rechercheEcritureIdentique(BTransaction transaction) throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CIEcritureManager ecrMgr = new CIEcritureManager();
        ecrMgr.setSession(ecriture.getSession());
        ecrMgr.setForAnnee(ecriture.getAnnee());
        ecrMgr.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        ecrMgr.setForEmployeur(ecriture.getEmployeur());
        ecrMgr.setForIdTypeCompteCompta("true");
        ecrMgr.find(transaction);
        if (ecrMgr.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Recherche si une écriture existe déjà pour le même employeur et la même année un assuré, différent de 99. Date de
     * création : (14.11.2002 09:53:16)
     *
     * @param transaction
     *            la transaction a utiliser.
     * @return true si au moins une écriture a été trouvée.
     */
    public boolean rechercheEcritureSemblable(BTransaction transaction) throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CIEcritureManager ecrMgr = new CIEcritureManager();
        ecrMgr.setSession(ecriture.getSession());
        ecrMgr.setForAnnee(ecriture.getAnnee());
        ecrMgr.setForCompteIndividuelId(ecriture.getCompteIndividuelId());
        ecrMgr.setForEmployeur(ecriture.getEmployeur());
        // Modif. 09.10.2006, regarder si une inscription identique existe au CI
        // seulement si on passe l'insc. en définitif
        if (CIEcriture.CS_CI.equals(ecriture.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS.equals(ecriture.getIdTypeCompte())) {
            ecrMgr.setForIdTypeCompteCompta("true");
        }
        ecrMgr.find(transaction, BManager.SIZE_NOLIMIT);
        if (ecrMgr.size() == 0) {
            return false;
        } else {
            for (int i = 0; i < ecrMgr.size(); i++) {
                CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                if (!"99".equals(ecr.getMoisDebut())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rechercheEcritureSemblablesDt(BTransaction transaction, String employeur, String numeroAvs)
            throws Exception {
        if ((ecriture == null) || (ecriture.getSession() == null)) {
            return true;
        }
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setSession(ecriture.getSession());
        ciMgr.setForNumeroAvs(numeroAvs);
        ciMgr.find(BManager.SIZE_USEDEFAULT);
        if (ciMgr.size() == 0) {
            return false;
        }
        CIEcritureManager ecrMgr = new CIEcritureManager();
        ecrMgr.setSession(ecriture.getSession());
        ecrMgr.setForAnnee(ecriture.getAnnee());
        ecrMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
        ecrMgr.setForAffilie(employeur);

        ecrMgr.find(transaction, BManager.SIZE_NOLIMIT);
        if (ecrMgr.size() == 0) {
            return false;
        } else {
            for (int i = 0; i < ecrMgr.size(); i++) {
                CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                if (!"99".equals(ecr.getMoisDebut())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Génère une réouverture de CI. Utilisée lors du traitement de clôture ou lors d'un ajout d'écriture si celle-ci
     * est après la dernière clôture
     *
     * @param transaction
     *            la transaction à utiliser
     */
    public void reouvreCI(BTransaction transaction) throws Exception {
        if ((ecriture == null) || JAUtil.isStringEmpty(ecriture.getAvs())) {
            return;
        }
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
        // test du genre
        String genreAnnonce = null;
        if (CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())) {
            // genre 7, envoi d'un 67
            genreAnnonce = "67";
        } else {
            // Tous les autres genres envoi d'un 65 --> Modification de l'ARC en 61 depuis 01.01.19
            genreAnnonce = "61";
        }
        if (genreAnnonce != null) {
            // annonce
            HashMap attributs = new HashMap();
            attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
            attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, genreAnnonce);
            // assuré
            attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, ecriture.getAvs());
            // envoi
            application.annonceARC(transaction, attributs, false);
        }
    }

}
