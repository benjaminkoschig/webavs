package ch.globaz.amal.business.constantes;

public interface IAMPublipostage {
    public enum AMPublipostageAdresse {
        CASEPOSTALE("CasePostale"),
        DESIGNATION1("Designation1"),
        DESIGNATION2("Designation2"),
        DESIGNATION3("Designation3"),
        DESIGNATION4("Designation4"),
        LOCALITE("Localite"),
        NOM("Nom"),
        NPA("NPA"),
        NUMERO("Numero"),
        PRENOM("Prenom"),
        RUE("Rue"),
        TITRE("Titre");

        // Value storage
        private final String value;

        // Default private constructor
        private AMPublipostageAdresse(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    public enum AMPublipostagePyxis {
        NNSS("NNSS"),
        NUMCONTRIBUABLE("NumContribuable");

        // Value storage
        private final String value;

        // Default private constructor
        private AMPublipostagePyxis(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    public enum AMPublipostageCarteCulture {

        CARTECULTURE("CarteCulture");

        // Value storage
        private final String value;

        // Default private constructor
        private AMPublipostageCarteCulture(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    public enum AMPublipostageSimpleDetailFamille {
        ASSUREUR("Assureur"),
        CODE_ACTIF("Code actif"),
        CODETRAITEMENTDOSSIER("CodeTraitementDossier"),
        DEBUTDROIT("DebutDroit"),
        DOCUMENT("Document"),
        FINDROIT("FinDroit"),
        MONTANTCONTRIBUTION("MontantContribution"),
        MONTANTCONTRIBUTIONSUPPLEMENT("MontantContributionSupplement"),
        MONTANTCONTRIBUTIONSUPPLEMENTPCFAMILLE("MontantContributionSupplementPCFamille"),
        TYPEDEMANDE("TypeDemande");

        // Value storage
        private final String value;

        // Default private constructor
        private AMPublipostageSimpleDetailFamille(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    public enum AMPublipostageSimpleFamille {
        CODEFIN("CodeFin"),
        DATEFINDEFINITIVE("DateFinDefinitive"),
        DATENAISSANCE("DateNaissance"),
        DATENAISSANCE_YYYYMMDD("DateNaissanceYYYYMMDD"),
        IDCONTRIBUABLE("idContribuable"),
        ISCONTRIBUABLEPRINCIPAL("isContribuablePrincipal"),
        ISCARTECULTURE("isCarteCulture"),
        NOMPRENOM("NomPrenom"),
        NOPERSONNE("NoPersonne"),
        PEREMEREENFANT("PereMereEnfant"),
        SEXE("Sexe");

        // Value storage
        private final String value;

        // Default private constructor
        private AMPublipostageSimpleFamille(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }
}
