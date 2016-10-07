package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.GeneratePerstationPeriodeDecompte;

/**
 * 
 * Le rôle de cette class est de générer toutes les opérations (écritures et ordre versement en comptabilité) liées aux
 * décision après calcule
 * 
 * @author dma
 * 
 */
class GenerateOperationsApresCalcul implements GenerateOperations {
    private Operations operations = new Operations();

    private void computControlAmount(BigDecimal amountPrestation) {
        operations.setControlAmount(operations.getControlAmount().add(amountPrestation));
    }

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance) throws JadeApplicationException {
        return generateAllOperations(ovs, sections, dateForOv, dateEcheance, null);
    }

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance, PrestationOvDecompte decompteIni) throws JadeApplicationException {
        operations = new Operations();
        MontantDispo montantDispo = null;

        PrestationOvDecompte decompte = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteIni);

        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = gernerateEcrituresStandard(decompte);

        montantDispo = generateEcrituresCompensation(ac, decompte);

        montantDispo = generateEcrituresDettes(sections, decompte, montantDispo);

        montantDispo = generateOperationsCreanciers(decompte, montantDispo, dateForOv);

        generateOvs(montantDispo, decompte);

        generateOperationAllocationNoel(dateForOv, decompte);

        computControlAmount(decompte.getPrestationAmount());

        return operations;
    }

    private MontantDispo generateEcrituresCompensation(GenerateEcrituresResitutionBeneficiareForDecisionAc ac,
            PrestationOvDecompte decompte) throws ComptabiliserLotException {

        GenerateEcrituresCompensationForDecisionAc generateCompensation = new GenerateEcrituresCompensationForDecisionAc(
                ac.getMapEcritures(), decompte.getIdCompteAnnexeRequerant(), decompte.getIdCompteAnnexeConjoint());

        operations.addAllEcritures(generateCompensation.generateEcritures());
        return generateCompensation.getMontantsDisponible();
    }

    private MontantDispo generateEcrituresDettes(List<SectionSimpleModel> sections, PrestationOvDecompte decompte,
            MontantDispo montantDispo) throws JadeApplicationException {
        if (decompte.getDettes().size() > 0) {
            GenerateEcrituresDette generateEcrituresDette = new GenerateEcrituresDette(decompte, montantDispo, sections);
            operations.addAllEcritures(generateEcrituresDette.getEcritures());
            // this.operations.setControlAmount(this.operations.getControlAmount().add(this.sumOv(decompte.getDettes())));
            return generateEcrituresDette.getMontantsDisponible();
        } else {
            return montantDispo;
        }
    }

    private void generateOperationAllocationNoel(String dateForOv, PrestationOvDecompte decompte)
            throws JadeApplicationException {
        if (decompte.getAllocationsNoel().size() > 0) {
            // Périodes
            final String dateFin = JadeStringUtil.isBlankOrZero(decompte.getDateFin()) ? REPmtMensuel
                    .getDateDernierPmt(BSessionUtil.getSessionFromThreadContext()) : decompte.getDateFin();
            final String periode = decompte.getDateDebut() + " - " + dateFin;

            String strDecision = MotifVersementUtil.getTranslatedLabelFromTiers(
                    decompte.getIdTiersAddressePaiementRequerant(), decompte.getIdTiersRequerant(),
                    "PEGASUS_COMPTABILISATION_DECISION_DU", BSessionUtil.getSessionFromThreadContext());
            strDecision += " " + decompte.getDateDecision();

            GenerateOperationsAllocationsNoel allocationsNoel = new GenerateOperationsAllocationsNoel();
            allocationsNoel.generateAllOperation(decompte.getAllocationsNoel(), decompte.getInfosRequerant(),
                    decompte.getInfosConjoint(), periode, strDecision);
            operations.addAllEcritures(allocationsNoel.getEcritures());
            operations.addAllOVs(allocationsNoel.getOrdreVersementCompta());
        }
    }

    private MontantDispo generateOperationsCreanciers(PrestationOvDecompte decompte, MontantDispo montantDispo,
            String dateForOv) throws JadeApplicationException {
        if (decompte.getCreanciers().size() > 0) {
            GenerateOperationsCreancier generateEcrituresCreancier = new GenerateOperationsCreancier(decompte,
                    montantDispo, new GenerateOvComptaAndGroupe());
            operations.addAllEcritures(generateEcrituresCreancier.getEcritures());
            operations.addAllOVs(generateEcrituresCreancier.getOrdresVersementCompta());
            return generateEcrituresCreancier.getMontantsDisponible();
        } else {
            return montantDispo;
        }
    }

    private void generateOvs(final MontantDispo montantDispo, final PrestationOvDecompte decompte) {
        GenerateOvBeneficiaire generateOvBeneficiaire = new GenerateOvBeneficiaire(new GenerateOvComptaAndGroupe());
        operations.addAllOVs(generateOvBeneficiaire.generateOvComptaBeneficiare(montantDispo, decompte));
    }

    private GenerateEcrituresResitutionBeneficiareForDecisionAc gernerateEcrituresStandard(PrestationOvDecompte decompte)
            throws JadeApplicationException {

        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = newEcritureBasic();
        operations.addAllEcritures(ac.generateEcritures(decompte.getPrestationsPeriodes()));
        return ac;
    }

    protected GenerateEcrituresResitutionBeneficiareForDecisionAc newEcritureBasic() {
        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        return ac;
    }
}
