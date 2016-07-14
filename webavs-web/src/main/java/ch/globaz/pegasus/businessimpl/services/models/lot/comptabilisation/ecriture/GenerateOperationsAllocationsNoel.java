package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class GenerateOperationsAllocationsNoel extends GenerateOperationBasic {

    List<Ecriture> ecritures = new ArrayList<Ecriture>();
    List<OrdreVersementCompta> ordreVersementCompta = new ArrayList<OrdreVersementCompta>();

    public void generateAllOperation(List<OrdreVersement> ovs, InfosTiers infosRequerant, InfosTiers infosConjoint)
            throws JadeApplicationException {

        for (OrdreVersement ov : ovs) {
            if (ov.isDom2R()) {
                BigDecimal montant = ov.getMontant().divide(new BigDecimal(2));
                generateOperations(infosRequerant, ov, montant);
                generateOperations(infosConjoint, ov, montant);
            } else {
                generateOperations(infosRequerant, ov, ov.getMontant());
            }
        }
    }

    private void generateOperations(InfosTiers infosTiers, OrdreVersement ov, BigDecimal montant)
            throws ComptabiliserLotException, JadeApplicationException {
        CompteAnnexeSimpleModel compteAnnexe = resolvedCompteAnnexe(ov);

        String csRoleFamille = resolveCsRoleFamille(infosTiers.getIdTiers(), ov);

        ecritures.add(generateEcritureCredit(SectionPegasus.DECISION_PC, montant, compteAnnexe.getIdCompteAnnexe(),
                TypeEcriture.ALLOCATION_NOEL, ov));

        String refPaiement = infosTiers.getNss() + " " + infosTiers.getNom() + " " + infosTiers.getPrenom() + " "
                + BSessionUtil.getSessionFromThreadContext().getCodeLibelle("64055001");

        ordreVersementCompta.add(new OrdreVersementCompta(compteAnnexe, infosTiers.getIdTiersAddressePaiement(),
                infosTiers.getIdDomaineApplication(), montant, SectionPegasus.DECISION_PC, infosTiers.getIdTiers(), ov
                        .getCsType(), csRoleFamille, refPaiement));
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
