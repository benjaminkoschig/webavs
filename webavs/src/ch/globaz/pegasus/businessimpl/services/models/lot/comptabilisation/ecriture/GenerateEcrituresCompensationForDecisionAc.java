package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIReferenceRubrique;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class GenerateEcrituresCompensationForDecisionAc extends GenerateOperationBasic {
    private List<Ecriture> ecritures = new ArrayList<Ecriture>();
    private Map<Integer, List<Ecriture>> ecrituresBasique = null;
    private List<EcritureRequerantConjointPeriode> ecrituresPeriodes;
    private String idCompteAnnexRequerant;
    private CompensationInfoRestiution infosCompensationConjoint;
    private CompensationInfoRestiution infosCompensationRequerant;

    public GenerateEcrituresCompensationForDecisionAc(Map<Integer, List<Ecriture>> ecrituresBasique,
            String idCompteAnnexRequerant, String idCompteAnnexConjoint) {
        this.ecrituresBasique = ecrituresBasique;
        this.idCompteAnnexRequerant = idCompteAnnexRequerant;
        infosCompensationRequerant = new CompensationInfoRestiution();
        infosCompensationConjoint = new CompensationInfoRestiution();
        infosCompensationRequerant.setIdCompteAnnexe(idCompteAnnexRequerant);
        infosCompensationConjoint.setIdCompteAnnexe(idCompteAnnexConjoint);
    }

    private void computeMontantBeneficiaire() throws ComptabiliserLotException {
        for (EcritureRequerantConjointPeriode periode : ecrituresPeriodes) {
            if (periode.hasBeneficiaireRequerant()) {
                infosCompensationRequerant.addMontantDispo(periode.getRequerant(), periode.isBeneficiaireDom2R());
            }
            if (periode.hasBeneficiaireConjoint()) {
                infosCompensationConjoint.addMontantDispo(periode.getConjoint(), periode.isBeneficiaireDom2R());
            }
        }
    }

    private void generateCompensation(BigDecimal montantRestitutionMin, String idCompteAnnexeDecision,
            String idCompteAnnexeRestitution, TypeEcriture typeEcriture) throws ComptabiliserLotException {
        // si il n'y pas d'argent on ne créer pas d'ecriture car il n'y rien a compenser
        if (montantRestitutionMin.signum() != 0) {
            ecritures.add(this.generateEcriture(SectionPegasus.DECISION_PC, APIEcriture.DEBIT, montantRestitutionMin,
                    idCompteAnnexeDecision, APIReferenceRubrique.COMPENSATION_RENTES, typeEcriture));

            ecritures.add(this.generateEcriture(SectionPegasus.RESTIUTION, APIEcriture.CREDIT, montantRestitutionMin,
                    idCompteAnnexeRestitution, APIReferenceRubrique.COMPENSATION_RENTES, typeEcriture));
        }
    }

    private void generateCompensation(EricturePeriode ecitures, BigDecimal montantRestitutionMin,
            TypeEcriture typeEcriture) throws ComptabiliserLotException {
        this.generateCompensation(montantRestitutionMin, ecitures.getBeneficiaire().getIdCompteAnnexe(), ecitures
                .getRestitution().getIdCompteAnnexe(), typeEcriture);
    }

    private BigDecimal generateCompensationAnComputeMontantNonRembourse(CompensationInfoRestiution infosResitution,
            BigDecimal montantNonRembourse, String idCompteAnnexDecision, String idCompteAnnexRestitution,
            TypeEcriture typeEcriture) throws ComptabiliserLotException {

        BigDecimal montantRestitutionMin = null;

        if (infosResitution.hasMontantAdispositionDom2R()) {
            montantRestitutionMin = infosResitution.getMontantAdispositionDom2R().min(montantNonRembourse);
            montantNonRembourse = montantNonRembourse.subtract(montantRestitutionMin);
            infosResitution.substractMontantAdispositionDom2R(montantRestitutionMin);
            this.generateCompensation(montantRestitutionMin, idCompteAnnexRequerant, idCompteAnnexRestitution,
                    typeEcriture);
        }
        if ((infosResitution.hasMontantAdisposition()) && (montantNonRembourse.signum() == 1)) {
            montantRestitutionMin = infosResitution.getMontantAdisposition().min(montantNonRembourse);
            montantNonRembourse = montantNonRembourse.subtract(montantRestitutionMin);
            infosResitution.substractMontantAdisposition(montantRestitutionMin);
            this.generateCompensation(montantRestitutionMin, idCompteAnnexDecision, idCompteAnnexRestitution,
                    typeEcriture);
        }
        return montantNonRembourse;

    }

    public List<Ecriture> generateEcritures() throws ComptabiliserLotException {
        ecrituresPeriodes = generateListEcriturePeriode();
        sortEcriturePeriode();
        computeMontantBeneficiaire();
        this.generateInterPeriode();
        this.generateEntrePeriode();
        generateEntreConjointRequerant();
        return ecritures;
    }

    private void generateEntreConjointRequerant() throws ComptabiliserLotException {
        generateEntrePersonne(infosCompensationRequerant, infosCompensationConjoint);
        generateEntrePersonne(infosCompensationConjoint, infosCompensationRequerant);
    }

    private void generateEntrePeriode() throws ComptabiliserLotException {
        this.generateEntrePeriode(infosCompensationRequerant);
        this.generateEntrePeriode(infosCompensationConjoint);
    }

    private void generateEntrePeriode(CompensationInfoRestiution infosResitution) throws ComptabiliserLotException {

        Map<EricturePeriode, BigDecimal> map = new HashMap<EricturePeriode, BigDecimal>();
        for (Entry<EricturePeriode, BigDecimal> entry : infosResitution.getMapRestiutionNonRemboursee().entrySet()) {
            if (infosResitution.hasMonney() /* && (entry.getKey().getBeneficiaire() != null) */) {
                BigDecimal montantNonRembourse = generateCompensationAnComputeMontantNonRembourse(infosResitution,
                        entry.getValue(), infosResitution.getIdCompteAnnexe(), entry.getKey().getRestitution()
                                .getIdCompteAnnexe(), TypeEcriture.COMPENSATION_ENTRE_PERIODE);
                if (montantNonRembourse.signum() != 0) {
                    map.put(entry.getKey(), montantNonRembourse);
                }
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        infosResitution.setMapRestiutionNonRemboursee(map);
    }

    private void generateEntrePersonne(CompensationInfoRestiution infosResitutionRequ,
            CompensationInfoRestiution infosResitutionConj) throws ComptabiliserLotException {
        Map<EricturePeriode, BigDecimal> map = new HashMap<EricturePeriode, BigDecimal>();

        for (Entry<EricturePeriode, BigDecimal> entry : infosResitutionRequ.getMapRestiutionNonRemboursee().entrySet()) {
            if (infosResitutionConj.hasMonney()) {
                BigDecimal montantNonRembourse = generateCompensationAnComputeMontantNonRembourse(infosResitutionConj,
                        entry.getValue(), infosResitutionConj.getIdCompteAnnexe(),
                        /* infosResitutionRequ.getIdCompteAnnexe() */entry.getKey().getRestitution()
                                .getIdCompteAnnexe(), TypeEcriture.COMPENSATION_ENTRE_PERSONNE);
                if (montantNonRembourse.signum() != 0) {
                    map.put(entry.getKey(), montantNonRembourse);
                }
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        infosResitutionRequ.setMapRestiutionNonRemboursee(map);
    }

    private void generateInterPeriode() throws ComptabiliserLotException {
        for (EcritureRequerantConjointPeriode periode : ecrituresPeriodes) {

            this.generateInterPeriode(periode.getRequerant(), infosCompensationRequerant);
            this.generateInterPeriode(periode.getConjoint(), infosCompensationConjoint);
        }
    }

    private void generateInterPeriode(EricturePeriode ecituresPeriode, CompensationInfoRestiution infosResitution)
            throws ComptabiliserLotException {
        if ((ecituresPeriode != null)
                && (((ecituresPeriode.getRestitution()) != null) && ((ecituresPeriode.getBeneficiaire()) != null))) {

            BigDecimal montantRestitutionMin = ecituresPeriode.resolveMontantMin();

            BigDecimal montantNonRembourse = ecituresPeriode.getMontantRestitution().subtract(montantRestitutionMin);
            if (montantNonRembourse.signum() != 0) {
                infosResitution.getMapRestiutionNonRemboursee().put(ecituresPeriode, montantNonRembourse);
            }

            if (ecituresPeriode.isBenficaireDom2R()) {
                infosResitution.substractMontantAdispositionDom2R(montantRestitutionMin);
            } else {
                infosResitution.substractMontantAdisposition(montantRestitutionMin);
            }

            this.generateCompensation(ecituresPeriode, montantRestitutionMin, TypeEcriture.COMPENSATION_INTER_PERIODE);
        } else if (ecituresPeriode.getRestitution() != null) {
            infosResitution.getMapRestiutionNonRemboursee().put(ecituresPeriode,
                    ecituresPeriode.getRestitution().getMontant());
        }
    }

    private List<EcritureRequerantConjointPeriode> generateListEcriturePeriode() throws ComptabiliserLotException {
        List<EcritureRequerantConjointPeriode> list = new ArrayList<EcritureRequerantConjointPeriode>();
        for (Entry<Integer, List<Ecriture>> entry : ecrituresBasique.entrySet()) {
            EcritureRequerantConjointPeriode ecriturePeriode = new EcritureRequerantConjointPeriode();
            ecriturePeriode.setNoPeriode(entry.getKey());
            for (Ecriture ecriture : entry.getValue()) {
                ecriturePeriode.addEcriture(ecriture);
            }
            list.add(ecriturePeriode);
        }
        return list;
    }

    public BigDecimal getMontantADispositionConjoint() {
        return infosCompensationConjoint.getMontantAdisposition().add(
                infosCompensationConjoint.getMontantAdispositionDom2R());
    }

    public BigDecimal getMontantADispositionRequerant() {
        return infosCompensationRequerant.getMontantAdisposition().add(
                infosCompensationRequerant.getMontantAdispositionDom2R());
    }

    public MontantDispo getMontantsDisponible() {
        MontantDispo montantDispo = new MontantDispo(infosCompensationRequerant.getMontantAdispositionDom2R(),
                infosCompensationConjoint.getMontantAdispositionDom2R(),
                infosCompensationRequerant.getMontantAdisposition(), infosCompensationConjoint.getMontantAdisposition());
        return montantDispo;

    }

    protected Map<EricturePeriode, BigDecimal> getRestitutionNonRembourserConjoint() {
        return infosCompensationConjoint.getMapRestiutionNonRemboursee();
    }

    protected Map<EricturePeriode, BigDecimal> getRestitutionNonRembourserRequerant() {
        return infosCompensationRequerant.getMapRestiutionNonRemboursee();
    }

    private void sortEcriturePeriode() {
        Collections.sort(ecrituresPeriodes, new Comparator<EcritureRequerantConjointPeriode>() {
            @Override
            public int compare(EcritureRequerantConjointPeriode o1, EcritureRequerantConjointPeriode o2) {
                return o1.getNoPeriode().compareTo(o2.getNoPeriode());
            }
        });
    }
}
