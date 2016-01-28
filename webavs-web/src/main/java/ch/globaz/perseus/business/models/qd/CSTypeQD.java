package ch.globaz.perseus.business.models.qd;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSVariableMetier;

/**
 * Enumération des type de QD. Contient pour chaque type, le code système et le variableMetier maximal alouable
 * correspondant.
 * 
 * @author JSI
 * 
 */
public enum CSTypeQD {

    FRAIS_GARDE("55007001", CSVariableMetier.QD_MAX_FRAIS_GARDE, null),
    FRAIS_MALADIE("55007002", CSVariableMetier.QD_MAX_FRAIS_MALADIE, null),
    SQD_ASS_SOC_FRANCHISES_ASSURANCE_MALADIE("55007003", CSVariableMetier.QD_MAX_FRAIS_FRANCHISE_MALADIE, FRAIS_MALADIE),
    SQD_FRAIS_ADMINISTRATION("55007032", null, // CSVariableMetier.QD_MAX_TRANSPORT_AMBULANCE,
            FRAIS_MALADIE),
    SQD_FRAIS_DENTAIRES("55007008", null, FRAIS_MALADIE),
    SQD_FRAIS_DENTAIRES_ASSURANCE_DENTAIRE("55007030", null, FRAIS_MALADIE),
    SQD_FRAIS_DENTAIRES_MEDICAMENTS("55007010", null, FRAIS_MALADIE),
    SQD_FRAIS_DENTAIRES_TRAITEMENT_ORTHODONTIQUE("55007009", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_APPAREIL_ACOUSTIQUE("55007016", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_ATTESTATION("55007026", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_CHAISE_PERCEE("55007017", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_CHAUSSURES_ORTHOPEDIQUES("55007018", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_DIVERS("55007029", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_FAUTEUIL_ROULANT("55007020", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_LIT_ELECTRIQUE("55007021", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_LUNETTES_LENTILLES("55007022", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_LUNETTES_LOUPES("55007023", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_MOYENS_CONTRACEPTIFS("55007024", null, FRAIS_MALADIE),
    SQD_MOYENS_AUX_PERRUQUE("55007025", CSVariableMetier.QD_MAX_PERRUQUES, FRAIS_MALADIE),
    SQD_PREST_DOM_AIDE_MENAGE("55007011", CSVariableMetier.QD_MAX_AIDE_MENAGE, FRAIS_MALADIE),
    SQD_PREST_DOM_ASSISTANCE_FAMILLE("55007014", CSVariableMetier.QD_MAX_ASSISTANCE_FAMILLE, FRAIS_MALADIE),
    SQD_PREST_DOM_ASSISTANCE_ORGANISATION("55007013", CSVariableMetier.QD_MAX_ASSISTANCE_ORGANISATIONS, FRAIS_MALADIE),
    SQD_PREST_DOM_ASSISTANCE_PRIVE("55007012", CSVariableMetier.QD_MAX_ASSISTANCE_PRIVE, FRAIS_MALADIE),
    SQD_PREST_DOM_COTISATIONS_PARITAIRES("55007015", null, FRAIS_MALADIE),
    SQD_REGIMES_ALIMENTAIRES("55007027", null, FRAIS_MALADIE),
    SQD_SEJOURS_CONVALESCENCE("55007031", null, FRAIS_MALADIE),
    SQD_SEJOURS_CURE_THERMALE("55007004", null, FRAIS_MALADIE),
    SQD_TRANSPORT_AU_LIEU("55007006", null, FRAIS_MALADIE),
    SQD_TRANSPORT_PRIVE("55007007", null, FRAIS_MALADIE),
    SQD_TRANSPORT_TAXI("55007028", null, // CSVariableMetier.QD_MAX_TRANSPORT_TAXI,
            FRAIS_MALADIE),
    SQD_TRANSPORTS_AMBULANCE("55007005", null, // CSVariableMetier.QD_MAX_TRANSPORT_AMBULANCE,
            FRAIS_MALADIE);

    /**
     * Pour un code système donné, retourne l'énum CSTypeQD correspondant.
     * 
     * @param cs
     * @return CSTypeQD
     */
    public static CSTypeQD getType(String cs) {
        CSTypeQD type = null;
        for (int i = 0; (i < CSTypeQD.values().length) && (type == null); i++) {
            if (CSTypeQD.values()[i].codeSystem.equals(cs)) {
                type = CSTypeQD.values()[i];
            }
        }
        return type;
    }

    private String codeSystem;

    private CSTypeQD qdParente;

    private CSVariableMetier variableMetier;

    /**
     * Constructeur pour un type de QD.
     * 
     * @param codeSystem
     * @param variableMetier
     */
    private CSTypeQD(String codeSystem, CSVariableMetier variableMetier, CSTypeQD qdParente) {
        this.codeSystem = codeSystem;
        this.variableMetier = variableMetier;
        this.qdParente = qdParente;
    }

    /**
     * Donne le code système lié à un type de QD
     * 
     * @return code système
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    public List<CSTypeQD> getListChild() {
        ArrayList<CSTypeQD> al = new ArrayList<CSTypeQD>();

        for (int i = 0; i < CSTypeQD.values().length; i++) {
            if (equals(CSTypeQD.values()[i].qdParente)) {
                al.add(CSTypeQD.values()[i]);
            }
        }

        return al;
    }

    /**
     * @return the qdParente
     */
    public CSTypeQD getQdParente() {
        return qdParente;
    }

    /**
     * Retourne le variableMetier maximal lié à un type de QD
     * 
     * @return variableMetier
     */
    public CSVariableMetier getVariableMetier() {
        return variableMetier;
    }

}
