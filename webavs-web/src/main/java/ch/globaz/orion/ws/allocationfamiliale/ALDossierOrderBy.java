package ch.globaz.orion.ws.allocationfamiliale;

public enum ALDossierOrderBy {
    ORDER_BY_NO_DOSSIER("numDossier"),
    ORDER_BY_NSS("nss"),
    ORDER_BY_NOM_PRENOM_ALLOCATAIRE("nomPrenomAllocataire"),
    ORDER_BY_ETAT("etatDossier"),
    ORDER_BY_FIN_VALIDITE("finValidite");

    private String text;

    private ALDossierOrderBy(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
