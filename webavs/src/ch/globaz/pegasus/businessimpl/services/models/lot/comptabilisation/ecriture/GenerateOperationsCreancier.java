package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

class GenerateOperationsCreancier extends GenerateEcrituresDispatcher {

    private GenerateOvComptaAndGroupe generateOvCompta;

    public GenerateOperationsCreancier(PrestationOvDecompte decompte, MontantDispo montantDispo,
            GenerateOvComptaAndGroupe generateOvComptaAndGroupe) throws JadeApplicationException {
        super(decompte, montantDispo, decompte.getCreanciers());
        generateOvCompta = generateOvComptaAndGroupe;
        generateAllOperations();
    }

    @Override
    protected void addOperation(OrdreVersement ov, BigDecimal montant, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException {
        //
        // this.ecritures.add(this.generateEcriture(SectionPegasus.DECISION_PC, APIEcriture.CREDIT,
        // APIReferenceRubrique.COMPENSATION_RENTES, montant, compteAnnexe.getIdCompteAnnexe(),
        // TypeEcriture.CREANCIER, ov));

        generateOvCompta.addOvCompta(compteAnnexe, ov.getIdTiersAdressePaiement(), ov.getIdDomaineApplication(),
                montant, SectionPegasus.DECISION_PC, ov.getIdTiers(), ov.getCsType(), null);
    }

    public List<OrdreVersementCompta> getOrdresVersementCompta() {
        return generateOvCompta.getOvsCompta();
    }

    @Override
    protected BigDecimal resolveMontantOv(SimpleOrdreVersement ov) {
        return new BigDecimal(ov.getMontant());
    }

}
