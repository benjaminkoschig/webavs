package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.JadeLogger;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class GenerateOperationsAllocationsNoel extends GenerateOperationBasic {

    List<Ecriture> ecritures = new ArrayList<Ecriture>();
    List<OrdreVersementCompta> ordreVersementCompta = new ArrayList<OrdreVersementCompta>();

    public void generateAllOperation(List<OrdreVersement> ovs, InfosTiers infosRequerant, InfosTiers infosConjoint,
            String strPeriode, String strDecision) throws JadeApplicationException {

        for (OrdreVersement ov : ovs) {
            if (ov.isDom2R()) {
                BigDecimal montant = ov.getMontant().divide(new BigDecimal(2));
                generateOperations(infosRequerant, ov, montant, strPeriode, strDecision);
                generateOperations(infosConjoint, ov, montant, strPeriode, strDecision);
            } else {
                generateOperations(infosRequerant, ov, ov.getMontant(), strPeriode, strDecision);
            }
        }
    }

    private void generateOperations(InfosTiers infosTiers, OrdreVersement ov, BigDecimal montant, String strPeriode,
            String strDecision) throws ComptabiliserLotException, JadeApplicationException {
        CompteAnnexeSimpleModel compteAnnexe = resolvedCompteAnnexe(ov);

        String csRoleFamille = resolveCsRoleFamille(infosTiers.getIdTiers(), ov);

        ecritures.add(generateEcritureCredit(SectionPegasus.DECISION_PC, montant, compteAnnexe.getIdCompteAnnexe(),
                TypeEcriture.ALLOCATION_NOEL, ov));

        String motifVersement = formatMotifVersement(infosTiers, ov.getRefPaiement(), strPeriode, strDecision);

        ordreVersementCompta.add(new OrdreVersementCompta(compteAnnexe, infosTiers.getIdTiersAddressePaiement(),
                infosTiers.getIdDomaineApplication(), montant, SectionPegasus.DECISION_PC, infosTiers.getIdTiers(), ov
                        .getCsType(), csRoleFamille, motifVersement));
    }

    String formatMotifVersement(InfosTiers infosTiers, String refPaiement, String strPeriode, String strDecision) {
        String idTiersPrincipal = infosTiers.getIdTiersAddressePaiement();

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            idTiersPrincipal = infosTiers.getIdTiers();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(BSessionUtil.getSessionFromThreadContext(),
                idTiersPrincipal);

        String message = "";
        try {
            CodeSystem searchCodeSystemTraduction = CodeSystemUtils.searchCodeSystemTraduction("64055001",
                    BSessionUtil.getSessionFromThreadContext(), isoLangFromIdTiers);
            message = searchCodeSystemTraduction.getTraduction();
        } catch (Exception e) {
            JadeLogger.warn(e, e.getMessage());
            message = BSessionUtil.getSessionFromThreadContext().getCodeLibelle("64055001");
        }

        return MotifVersementUtil.formatDecision(infosTiers.getNss(),
                infosTiers.getNom() + " " + infosTiers.getPrenom(), refPaiement, message, strPeriode, strDecision);
    }

    public List<Ecriture> getEcritures() {
        return ecritures;
    }

    public List<OrdreVersementCompta> getOrdreVersementCompta() {
        return ordreVersementCompta;
    }

    private String resolveCsRoleFamille(String idTiersRequerant, OrdreVersement ov) {
        String csRoleFamille = null;
        if (idTiersRequerant.equals(ov.getIdTiers())) {
            csRoleFamille = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
        } else {
            csRoleFamille = IPCDroits.CS_ROLE_FAMILLE_CONJOINT;

        }
        return csRoleFamille;
    }

    private CompteAnnexeSimpleModel resolvedCompteAnnexe(OrdreVersement ov) throws ComptabiliserLotException {
        return CompteAnnexeResolver.resolveByIdTiers(ov.getIdTiers(), IntRole.ROLE_RENTIER);
    }

}
