package globaz.pavo.db.acl;

public class CITreatAclEnCoursResult {
    private String arcStatut = ""; // En cours, Terminé ou Problème
    private String attestationAssuranceLocation = "";

    public String getArcStatut() {
        return arcStatut;
    }

    public String getAttestationAssuranceLocation() {
        return attestationAssuranceLocation;
    }

    public void setArcStatut(String arcStatut) {
        this.arcStatut = arcStatut;
    }

    public void setAttestationAssuranceLocation(String attestationAssuranceLocation) {
        this.attestationAssuranceLocation = attestationAssuranceLocation;
    }
}
