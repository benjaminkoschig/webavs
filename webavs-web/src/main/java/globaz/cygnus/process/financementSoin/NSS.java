package globaz.cygnus.process.financementSoin;

import globaz.cygnus.utils.RFUtils;

public class NSS {

    private String nss = null;

    public NSS(String nss) {
        this.nss = nss;

        if ((nss == null) || (nss.length() == 0)) {
            throw new IllegalArgumentException("NSS cannot be null or empty");
        }
    }

    public boolean isValid() {
        return RFUtils.isNNS(nss);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NSS other = (NSS) obj;
        if (nss == null) {
            if (other.nss != null) {
                return false;
            }
        } else if (!nss.equals(other.nss)) {
            return false;
        }
        return true;
    }

    public String getNss() {
        return nss;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nss == null) ? 0 : nss.hashCode());
        return result;
    }

}
