package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.db.attestations.RFAttestationJointAssAttestationDossier;

public class VerificationAttestation {
    private String niveauAvertissement;
    private RFAttestationJointAssAttestationDossier attestation;

    public VerificationAttestation(String niveauAvertissement, RFAttestationJointAssAttestationDossier attestation) {
        super();
        this.niveauAvertissement = niveauAvertissement;
        this.attestation = attestation;
    }

    public VerificationAttestation(String niveauAvertissement) {
        super();
        this.niveauAvertissement = niveauAvertissement;
    }

    public VerificationAttestation() {
    }

    public String getNiveauAvertissement() {
        return niveauAvertissement;
    }

    public boolean hasAttestation() {
        return attestation != null;
    }

    public boolean hasNiveauAvertissement() {
        return niveauAvertissement != null;
    }

    public boolean isNiveauAvertissement() {
        return IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT.equals(niveauAvertissement);
    }

    public boolean isNiveauRefus() {
        return IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS.equals(niveauAvertissement);
    }

    public String getIdAttestation() {
        return attestation.getIdAttestation();
    }
}
