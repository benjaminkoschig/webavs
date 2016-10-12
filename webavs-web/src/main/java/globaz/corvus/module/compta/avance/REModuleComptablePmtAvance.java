package globaz.corvus.module.compta.avance;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APISection;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.HashMap;

/**
 * Description Génère les OV des pmts en avances
 * 
 * @author scr
 * 
 */
public class REModuleComptablePmtAvance extends AREModuleComptable {

    // Class statique, référence par cette instance
    private static REModuleComptablePmtAvance instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized REModuleComptablePmtAvance getInstance(BSession session) throws Exception {
        if (REModuleComptablePmtAvance.instance == null) {
            REModuleComptablePmtAvance.instance = new REModuleComptablePmtAvance(session, true);
        }
        return REModuleComptablePmtAvance.instance;
    }

    // Map idSection --> csDomaineApplicatofAvance
    private HashMap<String, String> mappingIdSectionDomainAvance = new HashMap();

    private BSession sessionOsiris = null;

    private REModuleComptablePmtAvance(BSession sessionOsiris, boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
        this.sessionOsiris = sessionOsiris;
        instantiateMappingIDSectionDomaineAvance();
    }

    private APICompteAnnexe getCompteAnnexe(BSession session, BTransaction transaction,
            APIGestionComptabiliteExterne compta, String idTiers, String idExterneRole) throws Exception {

        // récupération du compte annexe Rentier
        APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(idTiers, IntRole.ROLE_RENTIER, idExterneRole);

        if (compteAnnexe == null) {

            APICompteAnnexe cptAnnexe = (APICompteAnnexe) sessionOsiris.getAPIFor(APICompteAnnexe.class);
            compteAnnexe = cptAnnexe.createCompteAnnexe(sessionOsiris, transaction, idTiers, IntRole.ROLE_RENTIER,
                    idExterneRole);
        }
        return compteAnnexe;
    }

    /**
     * Instancaiation du mapping des id des sections comptables avec les domaine applicatifs
     */
    private void instantiateMappingIDSectionDomaineAvance() {
        mappingIdSectionDomainAvance.put(IREAvances.CS_DOMAINE_AVANCE_RENTE, APISection.ID_TYPE_SECTION_AVANCES);
        mappingIdSectionDomainAvance.put(IREAvances.CS_DOMAINE_AVANCE_PC, APISection.ID_TYPE_SECTION_PC);
        mappingIdSectionDomainAvance.put(IREAvances.CS_DOMAINE_AVANCE_RFM, APISection.ID_TYPE_SECTION_AVANCE_RFM);

    }

    public FWMemoryLog payerAvance(BProcess process, BISession session, BTransaction transaction,
            APIGestionComptabiliteExterne compta, String idTiersBeneficiaire, String idTiersAdrPmt, String csDomaine,
            FWCurrency montant, String libelle, String date, String csDomaineApplicatifAvance, String idOrganeExecution)
            throws Exception {

        // creation de l'idExterneRole (le numéro AVS)
        String idExterneRole = null;
        idExterneRole = PRTiersHelper.getTiersParId(session, idTiersBeneficiaire).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        FWMemoryLog memoryLog = new FWMemoryLog();
        memoryLog.logMessage(((BSession) session).getLabel("PMT_AVANCE") + " " + idExterneRole, FWMessage.INFORMATION,
                this.getClass().getName());

        if ((montant == null) || montant.isNegative() || montant.isZero()) {
            memoryLog.logMessage(((BSession) session).getLabel("PMT_AVANCE_MONTANT_EMPTY"), FWMessage.INFORMATION, this
                    .getClass().getName());
            return memoryLog;
        }

        // récupération du compte annexe RENTIER du bénéficiaire(création si non
        // existant).
        APICompteAnnexe compteAnnexe = getCompteAnnexe((BSession) session, transaction, compta, idTiersBeneficiaire,
                idExterneRole);

        /*
         * 
         * Initialisation de la section
         */

        // Gestion de l'id de section en fonction du domaine d'avance
        String idTypeSection = mappingIdSectionDomainAvance.get(csDomaineApplicatifAvance);

        // on créé un numero de facture unique qui servira a creer la section
        String noFacture = CAUtil.creerNumeroSectionUnique(sessionOsiris, transaction, IntRole.ROLE_RENTIER,
                idExterneRole, idTypeSection, String.valueOf(new JADate(date).getYear()),
                APISection.ID_CATEGORIE_SECTION_AVANCE);

        APISection section = compta.getSectionByIdExterne(compteAnnexe.getIdCompteAnnexe(), idTypeSection, noFacture,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

        memoryLog.logMessage(doOrdreVersement((BSession) session, compta, compteAnnexe.getIdCompteAnnexe(), section
                .getIdSection(), montant.toString(),
                loadAdressePaiement((BSession) session, transaction, date, idTiersAdrPmt, csDomaine)
                        .getIdAvoirPaiementUnique(), libelle, date, true, idOrganeExecution));
        return memoryLog;
    }

}
