package ch.globaz.eform.businessimpl.services.sedex.sender;

public enum GFDaDossierContactInformationElementSender implements GFDaDossierElementSender{
    DEPARTEMENT("department"),
    NAME("name"),
    EMAIL("email"),
    PHONE("phone");
    private final String name;

    GFDaDossierContactInformationElementSender(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
