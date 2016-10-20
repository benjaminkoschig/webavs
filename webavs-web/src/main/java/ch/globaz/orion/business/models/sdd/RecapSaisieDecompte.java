package ch.globaz.orion.business.models.sdd;

public final class RecapSaisieDecompte {

    private String numeroAffilie;
    private String nomPrenom;
    private String localite;
    private String periode;
    private String type;
    private String dateSaisie;
    private String erreur;

    private RecapSaisieDecompte(RecapSaisieDecompteBuilder builder) {
        numeroAffilie = builder.numeroAffilie;
        nomPrenom = builder.nomPrenom;
        localite = builder.localite;
        periode = builder.periode;
        type = builder.type;
        dateSaisie = builder.dateSaisie;
        erreur = builder.erreur;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getLocalite() {
        return localite;
    }

    public String getPeriode() {
        return periode;
    }

    public String getType() {
        return type;
    }

    public String getDateSaisie() {
        return dateSaisie;
    }

    public String getErreur() {
        return erreur;
    }

    public static final class RecapSaisieDecompteBuilder {

        private String numeroAffilie;
        private String nomPrenom;
        private String localite;
        private String periode;
        private String type;
        private String dateSaisie;
        private String erreur;

        public RecapSaisieDecompteBuilder() {
            super();
        }

        public RecapSaisieDecompteBuilder numeroAffilie(String numeroAffilie) {
            this.numeroAffilie = numeroAffilie;
            return this;
        }

        public RecapSaisieDecompteBuilder nomPrenom(String nomPrenom) {
            this.nomPrenom = nomPrenom;
            return this;
        }

        public RecapSaisieDecompteBuilder localite(String localite) {
            this.localite = localite;
            return this;
        }

        public RecapSaisieDecompteBuilder periode(String periode) {
            this.periode = periode;
            return this;
        }

        public RecapSaisieDecompteBuilder type(String type) {
            this.type = type;
            return this;
        }

        public RecapSaisieDecompteBuilder dateSaisie(String dateSaisie) {
            this.dateSaisie = dateSaisie;
            return this;
        }

        public RecapSaisieDecompteBuilder erreur(String erreur) {
            this.erreur = erreur;
            return this;
        }

        public RecapSaisieDecompte build() {
            return new RecapSaisieDecompte(this);
        }
    }
}
