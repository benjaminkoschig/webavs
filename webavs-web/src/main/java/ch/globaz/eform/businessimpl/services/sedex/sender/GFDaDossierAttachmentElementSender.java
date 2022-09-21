package ch.globaz.eform.businessimpl.services.sedex.sender;

public enum GFDaDossierAttachmentElementSender implements GFDaDossierElementSender{
    TITLE("title"),
    DOCUMENT_TYPE("documentType");
    private final String name;

    GFDaDossierAttachmentElementSender(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
