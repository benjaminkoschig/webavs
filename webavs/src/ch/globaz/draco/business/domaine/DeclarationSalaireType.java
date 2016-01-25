package ch.globaz.draco.business.domaine;

/**
 * Différent type d'une déclaration de salaire
 * 
 * @author sco
 */
public enum DeclarationSalaireType {

    PRINCIPALE("122001"),
    COMPLEMENTAIRE("122002"),
    BOUCLEMENT_ACOMPTE("122003"),
    CONTROLE_EMPLOYEUR("122004"),
    LTN("122005"),
    LTN_COMPLEMENTAIRE("122006"),
    SALAIRE_DIFFERES("122007"),
    ICI("122008");

    private String codeSystem;

    private DeclarationSalaireType(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public boolean isPrincipale() {
        return PRINCIPALE.equals(this);
    }

    public boolean isComplementaire() {
        return COMPLEMENTAIRE.equals(this);
    }

    public boolean isBouclementAcompte() {
        return BOUCLEMENT_ACOMPTE.equals(this);
    }

    public boolean isControleEmployeur() {
        return CONTROLE_EMPLOYEUR.equals(this);
    }

    public boolean isLTN() {
        return LTN.equals(this);
    }

    public boolean isLTNComplementaire() {
        return LTN_COMPLEMENTAIRE.equals(this);
    }

    public boolean isSalaireDifferes() {
        return SALAIRE_DIFFERES.equals(this);
    }

    public boolean isIci() {
        return ICI.equals(this);
    }

    public static DeclarationSalaireType fromCodeSystem(String codeSystem) {

        if (PRINCIPALE.getCodeSystem().equals(codeSystem)) {
            return PRINCIPALE;
        } else if (COMPLEMENTAIRE.getCodeSystem().equals(codeSystem)) {
            return COMPLEMENTAIRE;
        } else if (BOUCLEMENT_ACOMPTE.getCodeSystem().equals(codeSystem)) {
            return BOUCLEMENT_ACOMPTE;
        } else if (CONTROLE_EMPLOYEUR.getCodeSystem().equals(codeSystem)) {
            return CONTROLE_EMPLOYEUR;
        } else if (LTN.getCodeSystem().equals(codeSystem)) {
            return LTN;
        } else if (LTN_COMPLEMENTAIRE.getCodeSystem().equals(codeSystem)) {
            return LTN_COMPLEMENTAIRE;
        } else if (SALAIRE_DIFFERES.getCodeSystem().equals(codeSystem)) {
            return SALAIRE_DIFFERES;
        } else if (ICI.getCodeSystem().equals(codeSystem)) {
            return ICI;
        }

        throw new IllegalArgumentException("This value: " + codeSystem + " is not definded for this enum: "
                + DeclarationSalaireType.class.getName());
    }
}
