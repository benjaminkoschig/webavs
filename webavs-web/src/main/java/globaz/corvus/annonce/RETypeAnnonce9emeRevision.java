package globaz.corvus.annonce;

public enum RETypeAnnonce9emeRevision implements RECodeApplicationProvider {

    ANNONCE_AUGMENTATION(41),
    ANNONCE_DIMINUTION(42),
    ANNONCE_PONCTUELLE_SUBSEQUENTE(43);

    private int codeApplication;

    private RETypeAnnonce9emeRevision(int codeapplication) {
        codeApplication = codeapplication;
    }

    @Override
    public int getCodeApplication() {
        return codeApplication;
    }

}