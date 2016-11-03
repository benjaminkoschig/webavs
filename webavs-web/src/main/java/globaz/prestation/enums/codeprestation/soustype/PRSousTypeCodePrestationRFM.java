package globaz.prestation.enums.codeprestation.soustype;

import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import java.util.ArrayList;
import java.util.List;

public enum PRSousTypeCodePrestationRFM {

    // stadards
    RFM_AI_215("215", 215, PRDomainDePrestation.AI),
    RFM_AI_227("227", 227, PRDomainDePrestation.AI),
    RFM_AVS_201("201", 201, PRDomainDePrestation.AVS),
    RFM_AVS_213("213", 213, PRDomainDePrestation.AVS),

    RFM_AI_DOMICILE_206("206", 206, PRDomainDePrestation.AI),
    RFM_AVS_DOMICILE_200("200", 200, PRDomainDePrestation.AVS),
    RFM_AI_HOME_212("212", 212, PRDomainDePrestation.AI),
    RFM_AVS_HOME_214("214", 214, PRDomainDePrestation.AVS);

    private static PRSousTypeCodePrestationRFM[] convert(List<PRSousTypeCodePrestationRFM> list) {
        PRSousTypeCodePrestationRFM[] array = new PRSousTypeCodePrestationRFM[list.size()];
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
    public static PRSousTypeCodePrestationRFM[] getSousTypeGenrePrestationRFMAI() {
        return PRSousTypeCodePrestationRFM.getSousTypeGenrePrestationRFMParType(PRDomainDePrestation.AI);
    }

    /**
     * Retourne les sous-type genre de prestation PC AVS
     * 
     * @return Retourne les sous-type genre de prestation PC AVS
     */
    public static PRSousTypeCodePrestationRFM[] getSousTypeGenrePrestationRFMAVS() {
        return PRSousTypeCodePrestationRFM.getSousTypeGenrePrestationRFMParType(PRDomainDePrestation.AVS);
    }

    /**
     * Retourne les sous-type genre de prestation PC du type voulut
     * 
     * @return Retourne les sous-type genre de prestation du type voulut
     */
    private static PRSousTypeCodePrestationRFM[] getSousTypeGenrePrestationRFMParType(
            PRDomainDePrestation typeDePrestation) {
        List<PRSousTypeCodePrestationRFM> result = new ArrayList<PRSousTypeCodePrestationRFM>();
        for (PRSousTypeCodePrestationRFM sousType : PRSousTypeCodePrestationRFM.values()) {
            if (sousType.getTypeDePrestation().equals(typeDePrestation)) {
                result.add(sousType);
            }
        }
        return PRSousTypeCodePrestationRFM.convert(result);
    }

    /**
     * Test si le sous-type code prestation fournis en paramètres fait partie des sous-types RFM
     * 
     * @param sousTypeCodePrestation
     *            Le code prestation à testé sous forme de valeur entière
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isValidSousTypeCodePrestation(int sousTypeCodePrestation) {
        for (PRSousTypeCodePrestationRFM code : PRSousTypeCodePrestationRFM.values()) {
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
                return PRSousTypeCodePrestationRFM.isValidSousTypeCodePrestation(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int sousTypeCodePrestation;

    private String sousTypeCodePrestationAsString;

    private PRDomainDePrestation typeDePrestation;

    private PRSousTypeCodePrestationRFM(String sousTypeCodePrestationAsString, int sousTypeCodePrestation,
            PRDomainDePrestation typeDePrestation) {
        this.sousTypeCodePrestationAsString = sousTypeCodePrestationAsString;
        this.sousTypeCodePrestation = sousTypeCodePrestation;
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
