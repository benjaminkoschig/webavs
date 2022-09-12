package ch.globaz.eform.businessimpl.services.sedex.sender;

public enum GFDaDossierInsuredPersonElementSender implements GFDaDossierElementSender{
    OFFICIAL_NAME("officialName"),
    FIRST_NAME("firstName"),
    DATE_OF_BIRT("dateOfBirt"),
    VN("Vn");
    private final String name;

    GFDaDossierInsuredPersonElementSender(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
