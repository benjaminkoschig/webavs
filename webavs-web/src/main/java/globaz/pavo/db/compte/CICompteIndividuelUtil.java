package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.util.CIAffilie;
import globaz.pavo.util.CIAffilieManager;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Vérification du total du CI d'une personne pour une année pour les cot. pers. Date de création : (20.03.2003
 * 07:58:08)
 * 
 * @author: David Girardin
 */
public class CICompteIndividuelUtil extends CIEcritureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Mode d'inscription compensation */
    public static final int MODE_COMPENSATION = 1;
    /** Mode d'inscription direct */
    public static final int MODE_DIRECT = 0;

    /** l'id du journal */
    private String idJournal;

    /** flag provisoire de l'écriture. Par défault à true */
    private boolean provisoire = true;

    public static boolean isNNSSLength(String nss) {
        if (NSUtil.unFormatAVS(nss.trim()).length() == 13) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Classe permettant de faire le contrôle de facturation et, si nécessaire, l'ajustement des écritures.
     */
    public CICompteIndividuelUtil() {
        super();
    }

    /**
     * Classe permettant de faire le contrôle de facturation et, si nécessaire, l'ajustement des écritures.
     * 
     * @param idJournal
     *            l'id du journal à utiliser.
     */
    public CICompteIndividuelUtil(String idJournal) {
        super();
        this.idJournal = idJournal;
    }

    /**
     * Ajoute l'inscription.<br>
     * Date de création : (20.03.2003 08:00:55)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @param avs
     *            le numéro avs de l'assuré. Format avec ou sans points de séparation.
     * @param moisDebut
     *            le mois de début
     * @param moisFin
     *            le mois de fin
     * @param montant
     *            le montant au format Strings
     * @param genreEcriture
     *            le code système du genre de l'écriture.
     * @param ci
     *            le ci de l'assuré du type <code>CICompteIndividuel</code>.
     * @exception EXception
     *                si une erreur survient dans le traitement.
     */
    private boolean addInscription(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, CICompteIndividuel ci, String code,
            boolean simpleAdd) throws Exception {
        return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                ci, code, simpleAdd, "");
    }

    private boolean addInscription(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, CICompteIndividuel ci, String code,
            boolean simpleAdd, String codeSpecial) throws Exception {
        CIEcriture ecriture = new CIEcriture();
        ecriture.setSession(getSession());
        ecriture.setForAffiliePersonnel(true);
        if (ci == null) {
            ecriture.setAvs(avs);
            if (CIUtil.isNNSSlengthOrNegate(avs)) {
                ecriture.setNumeroavsNNSS("true");
                ecriture.setAvsNNSS("true");
            }
            ecriture.getWrapperUtil().rechercheCI(transaction);
        } else {
            ecriture.setCompteIndividuelId(ci.getCompteIndividuelId());
        }
        if (montant.length() > 0) {
            if ("-".equals(montant.trim().substring(0, 1))) {
                ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                montant = montant.trim().substring(1);
            }

        }
        if (!JAUtil.isStringEmpty(code)) {
            ecriture.setCode(code);
        }
        CIAffilie affilie = null;
        try {

            affilie = new CIAffilie();
            CIAffilieManager aff = new CIAffilieManager();
            aff.setForAffiliationId(idAffiliation);
            aff.setSession(getSession());
            aff.find(transaction);
            affilie = (CIAffilie) aff.getFirstEntity();
            if (!JAUtil.isStringEmpty(affilie.getBrancheEconomique())) {
                ecriture.setBrancheEconomique(affilie.getBrancheEconomique());
            }

        } catch (Exception e) {
        }
        ecriture.setEmployeur(idAffiliation);
        ecriture.setGenreEcriture(genreEcriture);
        ecriture.setMontant(montant);
        ecriture.setMoisDebut(moisDebut);
        ecriture.setMoisFin(moisFin);
        ecriture.setAnnee(annee);
        ecriture.setIdJournal(getIdJournal());
        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        ecriture.setCodeSpecial(codeSpecial);
        ecriture.setNoSumNeeded(true);
        if (avs.length() == 13) {
            ecriture.setNumeroavsNNSS("true");
        } else {
            ecriture.setNumeroavsNNSS("false");
        }

        // ecriture.simpleAdd(transaction);
        // ajouter les contôle nécessaire avec un add normal
        if (simpleAdd) {
            if (CIEcriture.CS_CIGENRE_7.equals(genreEcriture) && JadeStringUtil.isBlankOrZero(codeSpecial)) {
                ecriture.setCodeSpecial(determineCodeSpecial(affilie, avs));
            }
            ecriture.simpleAdd(transaction);
        } else {
            ecriture.add(transaction);
        }

        return true;
    }

    public void annuleDispense(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee) {
        avs = JAStringFormatter.deformatAvs(avs);
        CIEcritureManager mgr = new CIEcritureManager();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        int pos = 0;
        int neg = 0;
        try {
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(avs);
            ciMgr.setSession(getSession());
            ciMgr.find(transaction);
            if (ciMgr.size() > 0) {
                CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                CIEcriture ecr = new CIEcriture();
                mgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                // mgr.setForEmployeur(idAffiliation);
                // mgr.setForMoisDebut(moisDebut);
                // mgr.setForMoisFin(moisFin);
                mgr.setForCode(CIEcriture.CS_CODE_EXEMPTION);
                mgr.setForAnnee(annee);
                mgr.setSession(getSession());
                mgr.find(transaction);
                if (mgr.size() > 0) {
                    for (int i = 0; i < mgr.size(); i++) {

                        ecr = (CIEcriture) mgr.getEntity(i);
                        if (JAUtil.isIntegerEmpty(ecr.getExtourne())) {
                            pos++;
                        } else {
                            neg++;
                        }
                    }
                }
                int difference = pos - neg;
                if (difference <= 0) {
                    return;
                }
                for (int ind = 0; ind < difference; ind++) {
                    ecr.extourne(false);
                    ecr.setExtourne(CIEcriture.CS_EXTOURNE_1);
                    ecr.setModeAjout(CIEcriture.MODE_EXTOURNE);
                    ecr.setIdJournal(getIdJournal());
                    ecr.setEspionSaisie(new BSpy(getSession()).toString());
                    ecr.setId("");
                    ecr.setEcritureId("");
                    ecr.add(transaction);
                }

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String determineCodeSpecial(CIAffilie aff, String avs) {
        // Att: les variables statiques du type d'affiliation n'existent pas
        // a mettre à jour
        if (aff != null) {
            String genreAff = aff.getTypeAffiliation();
            if ("804001".equals(genreAff) || "804008".equals(genreAff)) {
                // indépendant
                return CIEcriture.CS_NONFORMATTEUR_INDEPENDANT;
            } else if ("804002".equals(genreAff)) {
                // employeur
                return CIEcriture.CS_NONFORMATTEUR_SALARIE;
            } else if ("804005".equals(genreAff)) {
                // employeur/indépendant
                // test du no avs
                if (CIUtil.unFormatAVS(avs).equals(CIUtil.unFormatAVS(aff.getTiers().getNumAvsActuel()))) {
                    // indépendant
                    return CIEcriture.CS_NONFORMATTEUR_INDEPENDANT;
                } else {
                    return CIEcriture.CS_NONFORMATTEUR_SALARIE;
                }
            }
        }
        return "";
    }

    /**
     * On peut passer plusieurs genre séparés par des virgules (in)
     * 
     * @param transaction
     * @param avs
     * @param annee
     * @param genre
     * @return
     */
    public boolean existeCIAnneeGenre(BTransaction transaction, String avs, String annee, String genre) {
        avs = JAStringFormatter.deformatAvs(avs);
        CIEcritureManager mgr = new CIEcritureManager();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        try {
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(avs);
            ciMgr.setSession(getSession());
            ciMgr.find(transaction);
            if (ciMgr.size() > 0) {
                CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                mgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                mgr.setForGenreIn(genre);
                mgr.setForAnnee(annee);

                mgr.setSession(getSession());
                if (mgr.getCount() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeDispense(BTransaction transaction, String avs, String annee) {
        avs = JAStringFormatter.deformatAvs(avs);
        CIEcritureManager mgr = new CIEcritureManager();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        int pos = 0;
        int neg = 0;
        try {
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(avs);
            ciMgr.setSession(getSession());
            ciMgr.find(transaction);
            if (ciMgr.size() > 0) {
                CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                CIEcriture ecr = new CIEcriture();
                mgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                // mgr.setForEmployeur(idAffiliation);
                // mgr.setForMoisDebut(moisDebut);
                // mgr.setForMoisFin(moisFin);
                mgr.setForCode(CIEcriture.CS_CODE_EXEMPTION);
                mgr.setForAnnee(annee);
                mgr.setSession(getSession());
                mgr.find(transaction);
                if (mgr.size() > 0) {
                    for (int i = 0; i < mgr.size(); i++) {

                        ecr = (CIEcriture) mgr.getEntity(i);
                        if (JAUtil.isIntegerEmpty(ecr.getExtourne())) {
                            pos++;
                        } else {
                            neg++;
                        }
                    }
                }
                int difference = pos - neg;
                if (difference <= 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Renvoie l'id du journal.
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Retourne la valeur de l'extourne pour un affilié et un mois de fin donnée par rapport aux écritures déjà
     * inscrites au CI.
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param idAffiliation
     *            l'id affiliation
     * @param moisFin
     *            le nouveau mois de fin pour le calcul de l'extourne
     * @param annee
     *            l'année concernée
     * @param genreEcriture
     *            le code système du genre de l'écriture à prendre en compte
     * @return la valeur de l'extourne calculée
     * @throws Exception
     *             si une erreur survient
     */
    public String getMontantExtourne(BTransaction transaction, String idTiers, String moisFin, String annee,
            String genreEcriture) throws Exception {
        int moisFinInt = Integer.parseInt(moisFin);
        if ((moisFinInt == 12) || (moisFinInt == 1)) { // extourne l'année
            // complète
            moisFinInt = 0;
        }
        int moisFinMax = 1;
        int moisDebutMin = 12;
        FWCurrency extourne = new FWCurrency("0.00");
        setForAnnee(annee);
        // setForIdTypeCompteCompta("0"); //BTC: commenter pour rechercher aussi
        // les temporaires
        setForIdTiers(idTiers); // 10.03.2005:BTC:recherche des écritures en
        // passant par le tiers au lieu de l'affiliation
        setOrderBy("KBNMOD");
        // recherche toutes les écritures de cet affilié
        this.find(transaction);
        for (int i = 0; i < size(); i++) {
            CIEcriture ecriture = (CIEcriture) getEntity(i);
            if (genreEcriture.equals(ecriture.getGenreEcriture())) {
                // lecture mois fin
                int moisFinEcr;
                if (JAUtil.isStringEmpty(ecriture.getMoisFin())) {
                    moisFinEcr = 12;
                } else if (ecriture.isMoisSpeciaux()) {
                    moisFinEcr = moisFinMax;
                } else {
                    moisFinEcr = Integer.parseInt(ecriture.getMoisFin());
                }

                if (moisFinEcr >= moisFinInt) {
                    // calcul de l'extourne
                    // lecture mois début
                    int moisDebutEcr;
                    if (JAUtil.isStringEmpty(ecriture.getMoisDebut())) {
                        moisDebutEcr = 1;
                    } else if (ecriture.isMoisSpeciaux()) {
                        moisDebutEcr = moisDebutMin;
                    } else {
                        moisDebutEcr = Integer.parseInt(ecriture.getMoisDebut());
                    }
                    // calcul de la fraction
                    BigDecimal mnt = new BigDecimal(ecriture.getMontant());
                    BigDecimal ex = new BigDecimal(moisFinEcr - moisFinInt);
                    BigDecimal tot = new BigDecimal((moisFinEcr - moisDebutEcr) + 1);
                    mnt = mnt.multiply(ex);
                    mnt = mnt.divide(tot, BigDecimal.ROUND_DOWN);
                    // ajout ou soustraction au total
                    if (CIEcriture.CS_EXTOURNE_1.equals(ecriture.getExtourne())) {
                        extourne.sub(mnt.toString());
                    } else {
                        extourne.add(mnt.toString());
                    }
                    // sauve le plus grand mois de fin pour les éventuels cas 99
                    if (moisFinEcr > moisFinMax) {
                        moisFinMax = moisFinEcr;
                    }
                    // sauve le plus petit mois de début pour les éventuels cas
                    // 99
                    if (moisDebutEcr < moisDebutMin) {
                        moisDebutMin = moisDebutEcr;
                    }
                }
            }
        }
        return extourne.toString();
    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnnee(String numeroAvs, String idAffiliation, String genre, String annee,
            String notCode) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenre(genre);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            sumMgr.setForEmployeur(idAffiliation);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForNotCodeAmortissement(notCode);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * getSommeParAnneeCode
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeCodeAmortissement(String numeroAvs, String genre, String annee, String code) {
        // Méthode qui retourne la somme des inscriptions CIs pour une année, un
        // genre d'écriture et un code spécial
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setForCodeAmortissement(code);
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenre(genre);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeCodeAmortissement(String numeroAvs, String numAffilie, String genre,
            String annee, String code) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenre(genre);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            sumMgr.setForNumeroAffilie(numAffilie);
            sumMgr.setForCodeAmortissement(code);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * getSommeParAnneeCodeSpecial
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeCodeSpecial(String numeroAvs, String genre, String annee, String codeSpecial) {
        // Méthode qui retourne la somme des inscriptions CIs pour une année, un
        // genre d'écriture et un code spécial
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setForCodeSpecial(codeSpecial);
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenre(genre);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeCotPers(String numeroAvs, String annee, boolean wantIrrecouvrable) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            if (!wantIrrecouvrable) {
                sumMgr.setForNotCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT);
            }
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeGenre(String numeroAvs, String annee, boolean wantIrrecouvrable, String genre,
            Boolean nonActif) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setNonActif(nonActif);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            if (!wantIrrecouvrable) {
                sumMgr.setForNotCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT + ", "
                        + CIEcriture.CS_CODE_MIS_EN_COMTE);
            }
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeIdAffilie(String idAffilie, String annee) {
        BigDecimal result = new BigDecimal("0");
        try {
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForAllEcritures("all");
            sumMgr.setForIdAffilie(idAffilie);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForNotCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeNoAffilie(String numAffilie, String annee) {
        BigDecimal result = new BigDecimal("0");
        try {
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForAllEcritures("all");
            sumMgr.setForNumeroAffilie(numAffilie);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForNotType(new Boolean(true));
            sumMgr.setForNotCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeNoAffilie(String numeroAvs, String forNumeroAffilie, String genre, String annee,
            String notCode) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForGenre(genre);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            sumMgr.setForNumeroAffilie(forNumeroAffilie);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForNotCodeAmortissement(notCode);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Method getSommeParAnnee.
     * 
     * @param numeroAvs
     * @param genre
     * @param annee
     * @param notCode
     * @return BigDecimal
     */
    public BigDecimal getSommeParAnneeNoAffilieNoAVS(String numeroAvs, String numAffilie, String annee) {
        BigDecimal result = new BigDecimal("0");
        try {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
            ciMgr.find();
            if (ciMgr.size() < 1) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
            if (ciMgr.size() < 1) {
                return new BigDecimal("0");
            }
            CIEcrituresSomme sumMgr = new CIEcrituresSomme();
            sumMgr.setSession(getSession());
            sumMgr.setForAnnee(annee);
            sumMgr.setForCompteIndividuelId(((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId());
            sumMgr.setForAllEcritures("all");
            sumMgr.setForNumeroAffilie(numAffilie);
            sumMgr.setForGenreCotPers("true");
            sumMgr.setForNotCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT);
            result = sumMgr.getSum("KBMMON");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Indique s'il y a des irrécouvrable pour une année, si année pas saisie retourne le si'il y a des irréc pour le CI
     * 
     * @param nss
     * @param annee
     * @return
     */
    public boolean hasIrrecouvrable(String nss, String annee, BTransaction transaction) {
        try {
            CICompteIndividuel ci = CICompteIndividuel.loadCITemporaire(nss, transaction);
            if ((ci == null) || ci.isNew()) {
                return false;
            }
            CIEcrituresSomme somme = new CIEcrituresSomme();
            somme.setSession(getSession());
            somme.setForCodeAmortissement(CIEcriture.CS_CODE_AMORTISSEMENT);
            somme.setForAnnee(annee);
            somme.setForCompteIndividuelId(ci.getCompteIndividuelId());
            if (somme.getSum("KBMMON").compareTo(new BigDecimal("0")) != 0) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;

    }

    boolean hasMontantAutreCI(String noAvs, String annee, BTransaction transaction) {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(noAvs);
        try {
            ciMgr.find();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (ciMgr.size() > 0) {
            CICompteIndividuel ciBase = (CICompteIndividuel) ciMgr.getFirstEntity();
            if (ciBase.hasCILies()) {
                try {
                    ArrayList ciLies = ciBase.getIdCILies(transaction);
                    for (int i = 0; i < ciLies.size(); i++) {
                        String idCi = (String) ciLies.get(i);
                        if (JadeStringUtil.isBlankOrZero(idCi) || idCi.equals(ciBase.getCompteIndividuelId())) {
                            continue;
                        }
                        CIEcrituresSomme sumMgr = new CIEcrituresSomme();
                        sumMgr.setSession(getSession());
                        sumMgr.setForAnnee(annee);
                        sumMgr.setForGenreCotPers("true");
                        sumMgr.setForCompteIndividuelId(idCi);
                        sumMgr.setForAllEcritures("all");
                        BigDecimal result = sumMgr.getSum("KBMMON");
                        if (result.compareTo(new BigDecimal("0")) != 0) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // pas d'autre montant trouvé = > return false
                return false;
            } else {
                // Pas de ci lié, pas d'autre montant
                return false;
            }

        } else {
            // Pas de ci trouvé, registre provi => aucun autre montant n'existe
            return false;
        }

    }

    /**
     * Spécifie l'id du journal à utiliser.
     * 
     * @param idJournal
     *            l'id du journal
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * Vérifie le total d'un assuré pour l'année spécifiée et, si nécessaire, l'ajuste (écritures temporaires
     * comprises).<br>
     * 
     * @return true si le test a été effectué avec succès.
     * @param transaction
     *            la transaction à utiliser.
     * @param idAffiliation
     *            l'ID affiliation de l'écriture
     * @param avs
     *            le numéro avs de l'assuré. Format avec ou sans points de séparation.
     * @param moisDebut
     *            le mois de début
     * @param moisFin
     *            le mois de fin
     * @param montant
     *            le montant
     * @param genreEcriture
     *            le code système du genre de l'écriture.
     * @exception Exception
     *                si une erreur survient dans le traitement.
     */
    public boolean verifieAssure(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture) throws Exception {
        avs = JAStringFormatter.deformatAvs(avs);
        float mont = 0;
        CICompteIndividuel ci = CICompteIndividuel.loadCITemporaire(avs, transaction);

        if (JAUtil.isStringEmpty(avs)) {
            transaction.addErrors(getSession().getLabel("MSG_CI_VAL_AVS"));
            return false;
        }
        if (ci == null) {
            // pas de CI trouvé
            return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                    genreEcriture, null, null, false);
        }
        setForCompteIndividuelId(ci.getCompteIndividuelId());
        if (annee == null) {
            transaction.addErrors(getSession().getLabel("MSG_REVENU_VAL_ANNEE"));
            return false;
        }
        setForAnnee(annee);
        setForEmployeur(idAffiliation);
        setOrderBy("KBNMOD");
        this.find(transaction);
        if (size() == 0) {
            // aucune écriture
            return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                    genreEcriture, ci, null, false);
        }
        boolean moisDebutEcritureMatch = false;
        boolean moisFinEcritureMatch = false;
        double montantTotal = 0;
        double montantMatch = 0;
        int ecrCnt = 0;
        CIEcriture ecritureUnique = null;
        for (int i = 0; i < size(); i++) {
            // recherche des correspondances
            CIEcriture ecriture = (CIEcriture) getEntity(i);
            if (!CIEcriture.CS_CIGENRE_2.equals(ecriture.getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_3.equals(ecriture.getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_4.equals(ecriture.getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_9.equals(ecriture.getGenreEcriture())) {
                // écriture pas concernée
                continue;
            }
            if (CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())) {
                if (CIEcriture.CS_COTISATION_MINIMALE.equals(ecriture.getCodeSpecial())
                        || CIEcriture.CS_NONFORMATTEUR_SALARIE.equals(ecriture.getCodeSpecial())) {
                    // écriture pas concernée
                    continue;
                }
            }
            ecrCnt++;
            // sauve l'écriture si celle-ci est unique
            ecritureUnique = ecriture;
            mont = Float.parseFloat(JANumberFormatter.deQuote(ecriture.getMontant()));
            if (JAUtil.isIntegerEmpty(ecriture.getExtourne())
                    || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                    || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                    || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
                montantTotal += mont;
            } else {
                montantTotal -= mont;
            }
        }
        if (ecrCnt == 0) {
            // aucune écritures concernées
            return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                    genreEcriture, ci, null, false);
        }
        if (ecrCnt > 1) {
            // inscrire la différence
            FWCurrency montantFormat = new FWCurrency(montant);
            double montantDouble = Double.parseDouble(JANumberFormatter.deQuote(montant));
            if (montantTotal != montantDouble) {
                montantFormat.sub(montantTotal);
                return this.addInscription(transaction, idAffiliation, avs, "99", "99", annee,
                        montantFormat.toStringFormat(), genreEcriture, ci, null, true);
            } else {
                return true;
            }
        } else {
            // modifier l'écriture existante
            FWCurrency montantFormat = new FWCurrency(montant);
            if (montantFormat.isNegative()) {
                ecritureUnique.setExtourne(CIEcriture.CS_EXTOURNE_1);
                montantFormat.abs();
            }
            ecritureUnique.setMoisDebut(moisDebut);
            ecritureUnique.setMoisFin(moisFin);
            ecritureUnique.setGenreEcriture(genreEcriture);
            ecritureUnique.setMontant(montantFormat.toStringFormat());
            ecritureUnique.setForAffiliePersonnel(true);
            ecritureUnique.update(transaction);
            return transaction.hasErrors();
        }

    }

    public boolean verifieCI(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, int modeInscription, boolean nonCompense)
            throws Exception {
        if (nonCompense) {
            return this.verifieCI(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                    modeInscription, CIEcriture.CS_CODE_MIS_EN_COMTE);
        } else {
            return this.verifieCI(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                    modeInscription, null);
        }

    }

    public boolean verifieCI(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, int modeInscription,
            boolean nonCompense, String codeSpecial, String noAffilie) throws Exception {
        if (nonCompense) {
            return this.verifieCI(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                    modeInscription, CIEcriture.CS_CODE_MIS_EN_COMTE, codeSpecial, noAffilie);
        } else {
            return this.verifieCI(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                    modeInscription, null, codeSpecial, noAffilie);
        }

    }

    public boolean verifieCI(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, int modeInscription, String code)
            throws Exception {
        return this.verifieCI(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant, genreEcriture,
                modeInscription, code);
    }

    /**
     * Vérifie le total du CI pour l'année spécifiée et, si nécessaire, l'ajuste.<br>
     * En cas d'ajustement, les écritures existantes sont permièrement extournées si leur période est comprise dans la
     * période spécifiée.<br>
     * Si la caisse ne souhaite pas d'extourne, la solution d'utilisation de '99' pour la période peut être aussi
     * utilisée (fichier config). Date de création : (20.03.2003 08:00:55)
     * 
     * @return true si le test a été effectué avec succès.
     * @param transaction
     *            la transaction à utiliser.
     * @param avs
     *            le numéro avs de l'assuré. Format avec ou sans points de séparation.
     * @param moisDebut
     *            le mois de début
     * @param moisFin
     *            le mois de fin
     * @param montant
     *            le montant
     * @param genreEcriture
     *            le code système du genre de l'écriture.
     * @param nonCompense
     *            mettre à true si les écritures déjà mises en compte doivent être ignorées dans l'opération
     * @exception Exception
     *                si une erreur survient dans le traitement.
     */
    public boolean verifieCI(BTransaction transaction, String idAffiliation, String avs, String moisDebut,
            String moisFin, String annee, String montant, String genreEcriture, int modeInscription, String code,
            String codeSpecial, String noAffilie) throws Exception {
        avs = JAStringFormatter.deformatAvs(avs);
        float mont = 0;
        CICompteIndividuel ci = CICompteIndividuel.loadCITemporaire(avs, transaction);

        if (hasMontantAutreCI(avs, annee, transaction) && (avs.trim().length() != 13)) {
            transaction.addErrors(getSession().getLabel("AUTRE_CI_LIE"));
            return false;
        }
        if (modeInscription == CICompteIndividuelUtil.MODE_COMPENSATION) {
            if (JAUtil.isStringEmpty(avs)) {
                transaction.addErrors(getSession().getLabel("MSG_CI_VAL_AVS"));
                return false;
            }

            if (ci == null) {
                // pas de CI trouvé
                if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(montant))) {
                    return true;
                }
                return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                        genreEcriture, null, code, false, codeSpecial);
            }
            // pour tous les ci liés
            ArrayList listCI = ci.getIdCILies(transaction);
            if (listCI.size() != 0) {
                setForCompteIndividuelIdList(listCI.toArray());
            } else {
                setForCompteIndividuelId(ci.getCompteIndividuelId());
            }
            if (annee == null) {
                transaction.addErrors(getSession().getLabel("MSG_REVENU_VAL_ANNEE"));
                return false;
            }
            setForAnnee(annee);
            // setForEmployeur(idAffiliation);
            // setForIdTypeCompteCompta("0");
            setForNotCompense(CIEcriture.CS_CODE_MIS_EN_COMTE + "," + CIEcriture.CS_CODE_AMORTISSEMENT);
            setOrderBy("KBNMOD");
            this.find(transaction);
            if (size() == 0) {
                if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(montant))) {
                    return true;
                }
                // aucune écriture
                return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                        genreEcriture, ci, code, false, codeSpecial);
            }
            boolean moisEcritureMatch = true;
            boolean genreEcritureMatch = true;
            double montantTotal = 0;
            double montantMatch = 0;
            CICompteIndividuel lastCompte = ci;
            int totalEcr = 0;
            CIEcriture ecriture = null;
            for (int i = 0; i < size(); i++) {
                AFAffiliation affilie = new AFAffiliation();
                affilie.setSession(getSession());
                affilie.setAffiliationId(idAffiliation);
                affilie.retrieve();
                if (affilie.isNew()) {
                    _addError(transaction, "L'affilie n'existe pas : " + idAffiliation);
                }
                // recherche des correspondances
                ecriture = (CIEcriture) getEntity(i);
                // On ignore les correction
                if (!JadeStringUtil.isBlankOrZero(noAffilie) && (!noAffilie.equals(ecriture.getIdAffilie()))) {
                    continue;
                }
                if (CIEcriture.CS_CORRECTION.equals(ecriture.getIdTypeCompte())) {
                    continue;
                }
                if (!CIEcriture.CS_CIGENRE_2.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_3.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_4.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_9.equals(ecriture.getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_0.equals(ecriture.getGenreEcriture())) {
                    // écriture pas concernée
                    continue;
                }
                if (CIEcriture.CS_CIGENRE_7.equals(ecriture.getGenreEcriture())) {
                    if (!JAUtil.isIntegerEmpty(ecriture.getCodeSpecial())
                            && CIEcriture.CS_NONFORMATTEUR_SALARIE.equals(ecriture.getCodeSpecial())) {
                        // écriture pas concernée
                        continue;
                    }
                }
                // PO - Tenir compte des affiliés qui sont indépendant et non
                // actif en même temps
                // => extourner seulement les écritures en rapport avec le genre
                // passé
                // Si genre 4 => prendre les écritures de genre 4 et 7 formateur
                // de rente
                // Si genre 3 ou 9 => prendre genre 3 ou 9 ou 7
                // Si genre 7 => pb ?
                if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(affilie.getTypeAffiliation())) {
                    if (!CIEcriture.CS_CIGENRE_4.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(ecriture.getGenreEcriture())) {
                        continue;
                    }
                    if (CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !JadeStringUtil.isBlankOrZero(ecriture.getCodeSpecial())
                            && !CIEcriture.CS_NONFORMATTEUR_NONACTIF.equalsIgnoreCase(ecriture.getCodeSpecial())
                            && !CIEcriture.CS_COTISATION_MINIMALE.equals(ecriture.getCodeSpecial())) {
                        continue;
                    }
                }
                if (CodeSystem.TYPE_AFFILI_INDEP.equals(affilie.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affilie.getTypeAffiliation())) {

                    if (!CIEcriture.CS_CIGENRE_3.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_CIGENRE_9.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(ecriture.getGenreEcriture())) {
                        continue;
                    }
                    if (CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_NONFORMATTEUR_INDEPENDANT.equals(ecriture.getCodeSpecial())) {
                        continue;
                    }

                }
                if (CodeSystem.TYPE_AFFILI_TSE.equals(affilie.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(affilie.getTypeAffiliation())) {
                    if (!CIEcriture.CS_CIGENRE_2.equalsIgnoreCase(ecriture.getGenreEcriture())
                            && !CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(ecriture.getGenreEcriture())) {
                        continue;
                    }
                }

                // Modif jmc 4.11
                // On ne recherche plus le CI lié, si au pire le total est
                // négatif => le processus de compta pete
                // moidif. BTC
                // si différents comptes, sauvegarde du CI de l'écriture
                /*
                 * if(avs.trim().length() != 13 && !ecriture.getCompteIndividuelId
                 * ().equals(ci.getCompteIndividuelId())) { lastCompte = ecriture.getCI(transaction,false); }
                 */
                totalEcr++;
                mont = Float.parseFloat(JANumberFormatter.deQuote(ecriture.getMontant()));
                if (JAUtil.isIntegerEmpty(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
                    montantTotal += mont;
                } else {
                    montantTotal -= mont;
                }
                if (!ecriture.getMoisDebut().equals(moisDebut) || !ecriture.getMoisFin().equals(moisFin)) {
                    // périodes différentes
                    moisEcritureMatch = false;
                }
                if (!ecriture.getGenreEcriture().equals(genreEcriture)) {
                    // genres différents
                    genreEcritureMatch = false;
                }
            }
            if (montantTotal == 0) {
                if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(montant))) {
                    return true;
                }
                // aucune écritures concernées
                return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                        genreEcriture, ci, code, false, codeSpecial);
            }
            double montantDouble = Double.parseDouble(JANumberFormatter.deQuote(montant));
            if (montantTotal == montantDouble) {
                // même montant -> ne rien faire;
                return true;
            }
            if (moisEcritureMatch && genreEcritureMatch) {
                // inscrire la différence, car mois et genre concordent
                FWCurrency montantFormat = new FWCurrency(montant);
                montantFormat.sub(montantTotal);
                return this.addInscription(transaction, idAffiliation, lastCompte.getNumeroAvs(), moisDebut, moisFin,
                        annee, montantFormat.toStringFormat(), genreEcriture, lastCompte, code, true, codeSpecial);
            } else if (genreEcritureMatch && !moisEcritureMatch) {
                // inscrire la différence avec 99-99 car les mois sont
                // différents
                FWCurrency montantFormat = new FWCurrency(montant);
                montantFormat.sub(montantTotal);
                /*************************************
                 * *********************************** Extourner avec période 1-12 1-5-5
                 * *********************************** ***********************************
                 */
                if (totalEcr == 1) {
                    FWCurrency montantFormatNeg = new FWCurrency(montantTotal);
                    montantFormatNeg.negate();
                    this.addInscription(transaction, idAffiliation, lastCompte.getNumeroAvs(), ecriture.getMoisDebut(),
                            ecriture.getMoisFin(), annee, montantFormatNeg.toStringFormat(), genreEcriture, lastCompte,
                            code, true, codeSpecial);
                    // nouvelle écriture
                    if (!JadeStringUtil.isBlankOrZero(montant)) {
                        return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                                genreEcriture, ci, code, false, codeSpecial);
                    } else {
                        return true;
                    }
                } else {
                    return this.addInscription(transaction, idAffiliation, lastCompte.getNumeroAvs(), "99", "99",
                            annee, montantFormat.toStringFormat(), genreEcriture, lastCompte, code, true, codeSpecial);
                }
            } else if (!genreEcritureMatch && moisEcritureMatch) {
                // Annulation et nouvelle écriture avec les mois qu'on connait
                FWCurrency montantFormat = new FWCurrency(montantTotal);
                montantFormat.negate();
                this.addInscription(transaction, idAffiliation, lastCompte.getNumeroAvs(), moisDebut, moisFin, annee,
                        montantFormat.toStringFormat(), genreEcriture, lastCompte, code, true, codeSpecial);
                // nouvelle écriture
                if (!JadeStringUtil.isBlankOrZero(montant)) {
                    return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                            genreEcriture, ci, code, false, codeSpecial);
                } else {
                    return true;
                }
            } else {
                // Annulatio avec 99-99
                FWCurrency montantFormat = new FWCurrency(montantTotal);
                montantFormat.negate();

                this.addInscription(transaction, idAffiliation, lastCompte.getNumeroAvs(), "99", "99", annee,
                        montantFormat.toStringFormat(), genreEcriture, lastCompte, code, true, codeSpecial);
                // nouvelle écriture
                if (!JadeStringUtil.isBlankOrZero(montant)) {
                    return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                            genreEcriture, ci, code, false, codeSpecial);
                } else {
                    return true;
                }
            }
        } else {
            // mettre le falg MIS EN COMTPE en mode direct
            return this.addInscription(transaction, idAffiliation, avs, moisDebut, moisFin, annee, montant,
                    genreEcriture, ci, code, false, codeSpecial);
        }
    }

}
