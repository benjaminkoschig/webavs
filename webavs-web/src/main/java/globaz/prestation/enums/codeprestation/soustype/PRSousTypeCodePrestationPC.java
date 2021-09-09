package globaz.prestation.enums.codeprestation.soustype;

import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import java.util.ArrayList;
import java.util.List;

public enum PRSousTypeCodePrestationPC {

    // Sous types PC AI
    PC_AI_106("106", 106, PRDomainDePrestation.AI),
    PC_AI_107("107", 107, PRDomainDePrestation.AI),
    PC_AI_108("108", 108, PRDomainDePrestation.AI),
    PC_AI_109("109", 109, PRDomainDePrestation.AI),
    PC_AI_110("110", 110, PRDomainDePrestation.AI),
    PC_AI_112("112", 112, PRDomainDePrestation.AI),
    PC_AI_117("117", 117, PRDomainDePrestation.AI),
    PC_AI_118("118", 118, PRDomainDePrestation.AI),
    // TODO Definir code prestation pour SPEN
    PC_AI_119("119", 119, PRDomainDePrestation.AI),
//    PC_AI_119("119", 119, PRDomainDePrestation.AI),
//    PC_AI_120("120", 120, PRDomainDePrestation.AI),

    // Sous types PC AVS
    PC_AVS_100("100", 100, PRDomainDePrestation.AVS),
    PC_AVS_101("101", 101, PRDomainDePrestation.AVS),
    PC_AVS_102("102", 102, PRDomainDePrestation.AVS),
    PC_AVS_103("103", 103, PRDomainDePrestation.AVS),
    PC_AVS_104("104", 104, PRDomainDePrestation.AVS),
    PC_AVS_114("114", 114, PRDomainDePrestation.AVS),
    PC_AVS_115("115", 115, PRDomainDePrestation.AVS),
    PC_AVS_116("116", 116, PRDomainDePrestation.AVS);
    // TODO A traiter lorsque les références rubriques auront été définies
//    PC_AVS_121("121", 121, PRDomainDePrestation.AVS),
//    PC_AVS_122("122", 122, PRDomainDePrestation.AVS);

    private static PRSousTypeCodePrestationPC[] convert(List<PRSousTypeCodePrestationPC> list) {
        PRSousTypeCodePrestationPC[] array = new PRSousTypeCodePrestationPC[list.size()];
        for (int ctr = 0; ctr < list.size(); ctr++) {
            array[ctr] = list.get(ctr);
        }
        return array;
    }

    /**
     * Retourne les sous-type genre de prestation PC AI
     * 
     * @return Retourne les sous-type genre de prestation PC AI
     */
    public static PRSousTypeCodePrestationPC[] getSousTypeGenrePrestationPCAI() {
        return PRSousTypeCodePrestationPC.getSousTypeGenrePrestationPCParType(PRDomainDePrestation.AI);
    }

    /**
     * Retourne les sous-type genre de prestation PC AVS
     * 
     * @return Retourne les sous-type genre de prestation PC AVS
     */
    public static PRSousTypeCodePrestationPC[] getSousTypeGenrePrestationPCAVS() {
        return PRSousTypeCodePrestationPC.getSousTypeGenrePrestationPCParType(PRDomainDePrestation.AVS);
    }

    /**
     * Retourne les sous-type genre de prestation PC du type voulut
     * 
     * @return Retourne les sous-type genre de prestation du type voulut
     */
    private static PRSousTypeCodePrestationPC[] getSousTypeGenrePrestationPCParType(
            PRDomainDePrestation typeDePrestation) {
        List<PRSousTypeCodePrestationPC> result = new ArrayList<PRSousTypeCodePrestationPC>();
        for (PRSousTypeCodePrestationPC sousType : PRSousTypeCodePrestationPC.values()) {
            if (sousType.getTypeDePrestation().equals(typeDePrestation)) {
                result.add(sousType);
            }
        }
        return PRSousTypeCodePrestationPC.convert(result);
    }

    /**
     * Test si le sous-type code prestation fournis en paramètres fait partie des sous-types RFM
     * 
     * @param sousTypeCodePrestation
     *            Le code prestation à testé sous forme de valeur entière
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isValidSousTypeCodePrestation(int sousTypeCodePrestation) {
        for (PRSousTypeCodePrestationPC code : PRSousTypeCodePrestationPC.values()) {
            if (code.getSousTypeCodePrestation() == sousTypeCodePrestation) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test si le sous-type code prestation fournis en paramètres fait partie des sous-types RFM Cette méthode est
     * 'safe', c'est à dire qu'elle ne va générer aucune exception dans le cas ou la valeur fournie en paramètres est
     * ''pourrie''. Si c'est le cas, l'exception sera 'catched' et la méthode renverra <code>false</code>
     * 
     * @param sousTypeCodePrestation
     * @return <code>true</code> Dans le cas ou la chaîne de caractère à pu être convertie en valeur entière ET que
     *         cette valeur entière figure dans cette enum
     */
    public static final boolean isValidSousTypeCodePrestation(String sousTypeCodePrestation) {
        Integer value = null;
        try {
            value = Integer.valueOf(sousTypeCodePrestation);
            if (value != null) {
                return PRSousTypeCodePrestationPC.isValidSousTypeCodePrestation(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int sousTypeCodePrestation;

    private String sousTypeCodePrestationAsString;

    private PRDomainDePrestation typeDePrestation;

    private PRSousTypeCodePrestationPC(String sousTypeCodePrestString, int sousTypeCodePrestInteger,
            PRDomainDePrestation typeDePrestation) {
        sousTypeCodePrestationAsString = sousTypeCodePrestString;
        sousTypeCodePrestation = sousTypeCodePrestInteger;
        this.typeDePrestation = typeDePrestation;
    }

    /**
     * @return the sousTypeCodePrestation
     */
    public final int getSousTypeCodePrestation() {
        return sousTypeCodePrestation;
    }

    /**
     * @return the sousTypeCodePrestationAsString
     */
    public final String getSousTypeCodePrestationAsString() {
        return sousTypeCodePrestationAsString;
    }

    /**
     * @return the typeDePrestation
     */
    public final PRDomainDePrestation getTypeDePrestation() {
        return typeDePrestation;
    }

    /**
     * @param sousTypeCodePrestation
     *            the sousTypeCodePrestation to set
     */
    public final void setSousTypeCodePrestation(int sousTypeCodePrestation) {
        this.sousTypeCodePrestation = sousTypeCodePrestation;
    }

    /**
     * @param sousTypeCodePrestationAsString
     *            the sousTypeCodePrestationAsString to set
     */
    public final void setSousTypeCodePrestationAsString(String sousTypeCodePrestationAsString) {
        this.sousTypeCodePrestationAsString = sousTypeCodePrestationAsString;
    }
}
