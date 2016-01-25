package globaz.osiris.db.irrecouvrable;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.api.APIVPPoste;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CAMaxAnneeCotisationEcritureForSection;
import globaz.osiris.db.comptes.CAMaxAnneeCotisationEcritureForSectionManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreManager;
import globaz.osiris.exceptions.CABusinessException;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitaire pour le calcul de la mise en charge des irrécouvrable
 * 
 * @author bjo
 * 
 */
public class CAIrrecouvrableUtils {

    private static BigDecimal calculerMontant(BigDecimal montant, BigDecimal taux, BigDecimal tauxTotal,
            char methodeArrondi) {
        montant = montant.setScale(5);
        taux = taux.setScale(5);
        return JANumberFormatter.round(montant.multiply(taux.divide(tauxTotal, 5)), 0.05, 2, methodeArrondi);
    }

    /**
     * Calcul la part de l'employeur
     * 
     * @param montantEcriture
     * @param tauxEmployeur
     * @param tauxTotal
     * @param montantSalarie
     * @return
     */
    public static BigDecimal calculerPartEmployeur(BigDecimal montantEcriture, BigDecimal tauxEmployeur,
            BigDecimal tauxTotal, BigDecimal montantSalarie) {
        BigDecimal partEmployeur = CAIrrecouvrableUtils.calculerMontant(montantEcriture, tauxEmployeur, tauxTotal,
                JANumberFormatter.NEAR);
        BigDecimal diffMontantEcriturePartSalarie = montantEcriture.subtract(montantSalarie);

        if (diffMontantEcriturePartSalarie.compareTo(partEmployeur) != 0) {
            return diffMontantEcriturePartSalarie;
        }
        return partEmployeur;
    }

    /**
     * Calcul la part du salarie
     * 
     * @param montantEcriture
     * @param tauxSalarie
     * @param tauxTotal
     * @return
     */
    public static BigDecimal calculerPartSalarie(BigDecimal montantEcriture, BigDecimal tauxSalarie,
            BigDecimal tauxTotal) {
        BigDecimal partSalarie = CAIrrecouvrableUtils.calculerMontant(montantEcriture, tauxSalarie, tauxTotal,
                JANumberFormatter.INF);
        return partSalarie;
    }

    /**
     * Calcul le taux total en aditionnant le tauxSalarie et le tauxEmployeur
     * 
     * @param tauxSalarie
     * @param tauxEmployeur
     * @return
     */
    public static BigDecimal calculerTauxTotal(BigDecimal tauxSalarie, BigDecimal tauxEmployeur) {
        BigDecimal tauxTotal = new BigDecimal("0.00000");
        tauxTotal = tauxTotal.setScale(5);
        // on additionne les 2 taux pour trouver la part de chacun (par ex 50/50 si 5.05 chacun)
        tauxTotal = tauxTotal.add(tauxSalarie);
        tauxTotal = tauxTotal.add(tauxEmployeur);
        return tauxTotal;
    }

    /**
     * Verifie qu'il y a un bien un parametre de type salarie et l'autre de type employeur
     * 
     * @param param1
     * @param param2
     * @return
     */
    public static boolean checkParametreSalarieEmployeur(CAVPTypeDeProcedureOrdre param1,
            CAVPTypeDeProcedureOrdre param2) {
        if (param1.getTypeOrdre().equals(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)
                && param2.getTypeOrdre().equals(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)) {
            return true;
        } else if (param1.getTypeOrdre().equals(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)
                && param2.getTypeOrdre().equals(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne une session PAVO
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public static BSession createSessionPavo(BSession session) throws Exception {
        BSession sessionPavo;
        sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession();
        session.connectSession(sessionPavo);
        return sessionPavo;
    }

    /**
     * Cette méthode retourne l'année de cotisation la plus élevée des écritures de la section passée en paramètre.
     * 
     * @param section
     * @param session
     * @return Integer année de cotisation Max
     * @throws CATechnicalException
     *             , Exception
     */
    public static Integer findAnneeCotisationEcritureMaxForSection(CASection section, BSession session)
            throws CABusinessException, Exception {
        if (section == null) {
            throw new CABusinessException("Parameter section is empty");
        }
        if (session == null) {
            throw new CABusinessException("Parameter session is empty");
        }
        CAMaxAnneeCotisationEcritureForSectionManager maxAnneeCotisationecritureForSectionManager = new CAMaxAnneeCotisationEcritureForSectionManager();
        maxAnneeCotisationecritureForSectionManager.setSession(session);
        maxAnneeCotisationecritureForSectionManager.setForIdSection(section.getIdSection());
        maxAnneeCotisationecritureForSectionManager.find();
        if (!maxAnneeCotisationecritureForSectionManager.isEmpty()) {
            CAMaxAnneeCotisationEcritureForSection anneeCotisationMax = (CAMaxAnneeCotisationEcritureForSection) maxAnneeCotisationecritureForSectionManager
                    .getFirstEntity();
            return new Integer(anneeCotisationMax.getAnneeCotisationMax());
        } else {
            throw new CABusinessException("No max year found for this section");
        }
    }

    /**
     * Trouve toutes les écritures appartenant à une section donnée et étant à l'état "comptablisée", et retourne le
     * manager chargé
     * 
     * @param section
     * @param session
     * @return
     * @throws Exception
     */
    public static CAEcritureManager findEcritureForSection(CASection section, BSession session) throws Exception {
        CAEcritureManager ecritureManager = new CAEcritureManager();
        ecritureManager.setSession(session);
        ecritureManager.setForIdSection(section.getIdSection());
        ecritureManager.setVueOperationCaSe(true);
        ecritureManager.setForEtat(APIOperation.ETAT_COMPTABILISE);
        ecritureManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        ecritureManager.find();
        return ecritureManager;
    }

    /**
     * Trouve le 1er compte individuel répondant au nss passé en paramètre (retourne null si aucun résultat)
     * 
     * @param nss
     * @param session
     * @return
     * @throws Exception
     */
    public static CICompteIndividuel findFirstCompteIndividuel(String nss, BSession session) throws Exception {
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(session);
        ciManager.setForNumeroAvs(NSUtil.unFormatAVS(nss));
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.find();
        if (ciManager.size() > 0) {
            return (CICompteIndividuel) ciManager.getFirstEntity();
        } else {
            return null;
        }

    }

    /**
     * Trouve le parametrage correspondant à "idRubrique" et au type de procédure "autre", et retourne le manager chargé
     * 
     * @param idRubrique
     * @param session
     * @return
     * @throws Exception
     */
    public static CAVPTypeDeProcedureOrdreManager findParametrageForRubrique(String idRubrique, BSession session)
            throws Exception {
        CAVPTypeDeProcedureOrdreManager parametrageTypeProcedureManager = new CAVPTypeDeProcedureOrdreManager();
        parametrageTypeProcedureManager.setSession(session);
        parametrageTypeProcedureManager.setForIdRubrique(idRubrique);
        parametrageTypeProcedureManager.setForTypeProcedure(APIVPPoste.CS_PROCEDURE_AUTRE_PROCEDURE);
        parametrageTypeProcedureManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        parametrageTypeProcedureManager.find();
        return parametrageTypeProcedureManager;
    }

    /**
     * Trouve le tauxRubrique pour une écriture donnée
     * 
     * @param ecriture
     * @return
     */
    public static CATauxRubriques findTauxRubrique(CAEcriture ecriture) {
        CARubrique rubrique = ecriture.getCompte();
        CATauxRubriques taux = new CATauxRubriques();
        if (!JadeStringUtil.isIntegerEmpty(ecriture.getAnneeCotisation())) {
            taux = rubrique.getTauxRubriques("31.12." + ecriture.getAnneeCotisation(), ecriture.getSection()
                    .getIdCaisseProfessionnelle(), CATauxRubriquesManager.ORDER_BY_CAISSE_PROF_ASC_DATE_DESC);
        } else {
            taux = null;
        }
        return taux;
    }

    /**
     * Retourne true si la nature de la rubrique est compte de compensation
     * 
     * @param natureRubrique
     * @return
     */
    public static boolean isCompensation(String natureRubrique) {
        if (APIRubrique.COMPTE_COMPENSATION.equals(natureRubrique)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne true si beforeDate<=afterDate
     * 
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static boolean isDateBeforOrEqual(String beforeDate, String afterDate) {
        return beforeDate.equals(afterDate) || JadeDateUtil.isDateBefore(beforeDate, afterDate);
    }

    /**
     * Retourne true si la section est valide (non vide et !=-1)
     * 
     * @param idSection
     * @return
     */
    public static boolean isIdSectionValide(String idSection) {
        return !JadeStringUtil.isBlank(idSection) && !"-1".equals(idSection);
    }

    /**
     * Retourne true si le montant passé est négatif
     * 
     * @param montant
     * @return
     */
    public static boolean isMontantNegatif(BigDecimal montant) {
        if (montant.signum() == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne true si la nature de la rubrique est COMPTE_COURANT_DEBITEUR, COMPTE_COURANT_CREANCIER ou
     * COMPTE_FINANCIER
     * 
     * @param natureRubrique
     * @return
     */
    public static boolean isPaiement(String natureRubrique) {
        if (APIRubrique.COMPTE_COURANT_DEBITEUR.equals(natureRubrique)) {
            return true;
        } else if (APIRubrique.COMPTE_COURANT_CREANCIER.equals(natureRubrique)) {
            return true;
        } else if (APIRubrique.COMPTE_FINANCIER.equals(natureRubrique)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne true si le numéro de rubrique irrécouvrable correspond au pattern "211([0-9])(.)(3300)(.)*"
     * 
     * @param numeroRubriqueIrrecouvrable
     * @return
     */
    public static boolean isRubriqueCotPers(String numeroRubriqueIrrecouvrable) {
        String reg = "211([0-9])(.)(3300)(.)*";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(numeroRubriqueIrrecouvrable);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne le compteAnnexe pour l'id donné
     * 
     * @param idCompteAnnexe
     * @param session
     * @return
     * @throws Exception
     */
    public static CACompteAnnexe retrieveCompteAnnexe(String idCompteAnnexe, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idCompteAnnexe)) {
            throw new IllegalArgumentException("unable to load compteAnnexe, the idCompteAnnexe is blank");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.retrieve();

        return compteAnnexe;
    }

    /**
     * Retourne la rubrique pour l'id donné
     * 
     * @param idRubrique
     * @param session
     * @return
     * @throws Exception
     */
    public static CARubrique retrieveRubrique(String idRubrique, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idRubrique)) {
            throw new IllegalArgumentException("unable to retrieve rubrique, the idRurbique is blank" + idRubrique);
        }

        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);
        rubrique.setIdRubrique(idRubrique);
        rubrique.retrieve();

        if ((rubrique == null) || rubrique.isNew()) {
            throw new Exception("Rubrique not retrieved because not exist !!! " + idRubrique);
        }

        return rubrique;
    }

    /**
     * Retourne la section pour l'id donné
     * 
     * @param idSection
     * @param session
     * @return
     * @throws Exception
     */
    public static CASection retrieveSection(String idSection, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idSection)) {
            throw new IllegalArgumentException("unable to loadSection, the idSection is blank");
        }

        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();

        return section;
    }

    /**
     * Retourne le tiers pour l'id donnée
     * 
     * @param idTiers
     * @param session
     * @return
     * @throws Exception
     */
    public static TITiersViewBean retrieveTiers(String idTiers, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new IllegalArgumentException("unable to retrieve tiers, the idSection is blank");
        }

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        tiers.retrieve();

        return tiers;
    }
}
