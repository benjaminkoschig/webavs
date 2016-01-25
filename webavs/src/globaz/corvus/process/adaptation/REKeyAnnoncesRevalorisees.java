package globaz.corvus.process.adaptation;

import globaz.corvus.process.REListeErreursProcess.KeyRAAnnoncesAdaptation;

public class REKeyAnnoncesRevalorisees implements Comparable<REKeyAnnoncesRevalorisees> {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public String cleRAAnnoncesAdaptation = "";
    public String codePrestation = "";
    public String nss = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REKeyAnnoncesRevalorisees(String nss, String codePrestation, String cleRAAnnoncesAdaptation) {
        this.nss = nss;
        this.codePrestation = codePrestation;
        this.cleRAAnnoncesAdaptation = cleRAAnnoncesAdaptation;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public int compareTo(REKeyAnnoncesRevalorisees keyAnnRev) {
        if (nss.compareTo(keyAnnRev.nss) != 0) {
            return nss.compareTo(keyAnnRev.nss);
        } else if (codePrestation.compareTo(keyAnnRev.codePrestation) != 0) {
            return codePrestation.compareTo(keyAnnRev.codePrestation);
        } else if (cleRAAnnoncesAdaptation.compareTo(keyAnnRev.cleRAAnnoncesAdaptation) != 0) {
            return cleRAAnnoncesAdaptation.compareTo(keyAnnRev.cleRAAnnoncesAdaptation);
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyRAAnnoncesAdaptation)) {
            return false;
        }

        REKeyAnnoncesRevalorisees keyRev = (REKeyAnnoncesRevalorisees) obj;

        return ((keyRev.nss.equals(nss)) && (keyRev.codePrestation.equals(codePrestation)) && (keyRev.cleRAAnnoncesAdaptation
                .equals(cleRAAnnoncesAdaptation)));
    }

    @Override
    public int hashCode() {
        return (nss + codePrestation + cleRAAnnoncesAdaptation).hashCode();
    }

}
