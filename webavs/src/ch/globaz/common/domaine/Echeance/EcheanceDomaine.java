package ch.globaz.common.domaine.Echeance;

public enum EcheanceDomaine implements CodeSysteme<EcheanceDomaine> {

    PEGASUS("52206001");

    private String id;

    EcheanceDomaine(String id) {
        this.id = id;
    }

    @Override
    public String getCodeSysteme() {
        return id;
    }

}
