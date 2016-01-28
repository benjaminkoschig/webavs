package globaz.corvus.annonce;

public enum RETypeAnnonce10emeRevision implements RECodeApplicationProvider {

    ANNONCE_AUGMENTATION(44),
    ANNONCE_DIMINUTION(45),
    ANNONCE_PONCTUELLE_SUBSEQUENTE(46);

    private int codeApplication;

    private RETypeAnnonce10emeRevision(int codeapplication) {
        codeApplication = codeapplication;
    }

    @Override
    public int getCodeApplication() {
        return codeApplication;
    }

}