package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.decision;

import ch.globaz.common.converter.ConvertValueEnum;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;

public class DecisionConverter {
    ConvertValueEnum<String, MotifDecision> motifConverter = new ConvertValueEnum<String, MotifDecision>();

    public DecisionConverter() {
        motifConverter.put("64045001", MotifDecision.JUSTIFICATIFS_PAS_FOURNIS);
        motifConverter.put("64045002", MotifDecision.PARTIS_SANS_ADRESSE);
        motifConverter.put("64045003", MotifDecision.INTERETS_ETRANGERS);
        motifConverter.put("64045004", MotifDecision.SEJOUR_ETRANGERS);
        motifConverter.put("64045005", MotifDecision.DEPART_DEFINITIF);
        motifConverter.put("64045006", MotifDecision.RENCONTRE_IMPOSSIBLE);
        motifConverter.put("64045007", MotifDecision.SUPPRESSION_RENTE);
        motifConverter.put("64045008", MotifDecision.NOTIFIE_ENFANT);
        motifConverter.put("64045009", MotifDecision.PARENT_ETRANGER);
        // motifConverter.put("64045010", MotifDecision.VEUVAGE);// à voir ce code n'est pas utilisé mais présent dans
        // la
        // // liste
        motifConverter.put("64045011", MotifDecision.SEPARATION);
        motifConverter.put("64045012", MotifDecision.DEMANDE_BENEFICIAIRE);
        motifConverter.put("64045013", MotifDecision.INCARCERATION);
        motifConverter.put("64045014", MotifDecision.TRANSFERT_DOSSIER_AUTRE_ORGANE);
        motifConverter.put("64045015", MotifDecision.DECES);
        motifConverter.put("64045016", MotifDecision.VEUVAGE);
        motifConverter.put("64045017", MotifDecision.MARIAGE);
        motifConverter.put("64045018", MotifDecision.DIVORCE);
        motifConverter.put("64045019", MotifDecision.AUTRE);

        motifConverter.put("64043001", MotifDecision.JUSTIFICATIFS_DEMANDES_INITIAL);
        motifConverter.put("64043002", MotifDecision.PARTIS_SANS_ADRESSE_INITIAL);
        motifConverter.put("64043003", MotifDecision.DOMICILLE_PAR_RECONNU);
        motifConverter.put("64043004", MotifDecision.AUCUN_DROIT_DOMICILLE);
        motifConverter.put("64043005", MotifDecision.AUCUN_DROIT_EMS);
        motifConverter.put("64043006", MotifDecision.AUCUN_DROIT_EMS2);
        motifConverter.put("64043007", MotifDecision.RENCONTRE_IMPOSSIBLE_INITIAL);
        motifConverter.put("64043008", MotifDecision.DROIT_ENTRETIEN);
        motifConverter.put("64043009", MotifDecision.DROIT_INDEMNITE_AI);
        motifConverter.put("64043011", MotifDecision.ENFANT_PARENT_RENTIER);
        motifConverter.put("64043012", MotifDecision.DELAI_CARENCE);
        motifConverter.put("64043013", MotifDecision.RENONCIATION);
    }

    public Decision convertToDomain(SimpleDecisionHeader simpleDecisionHeader, String csMotif) {

        Decision decision = new Decision();
        decision.setId(simpleDecisionHeader.getId());
        decision.setDateDebut(new Date(simpleDecisionHeader.getDateDebutDecision()));
        if (simpleDecisionHeader.getDateFinDecision() != null && !simpleDecisionHeader.getDateFinDecision().isEmpty()) {
            decision.setDateFin(new Date(simpleDecisionHeader.getDateFinDecision()));
        }
        decision.setDatePreparation(new Date(simpleDecisionHeader.getDatePreparation()));
        decision.setDateDecision(new Date(simpleDecisionHeader.getDateDecision()));
        if (simpleDecisionHeader.getDateValidation() != null && !simpleDecisionHeader.getDateValidation().isEmpty()) {
            decision.setDateValidation(new Date(simpleDecisionHeader.getDateValidation()));
        }
        decision.setValidationPar(simpleDecisionHeader.getValidationPar());
        decision.setPreparationPar(simpleDecisionHeader.getPreparationPar());
        decision.setNumero(simpleDecisionHeader.getNoDecision());
        decision.setEtat(simpleDecisionHeader.getEtat());
        decision.setType(simpleDecisionHeader.getType());
        if (csMotif != null && !csMotif.isEmpty()) {
            decision.setMotif(motifConverter.convert(csMotif));
        } else {
            decision.setMotif(MotifDecision.INDEFINIT);
        }
        return decision;
    }

}
