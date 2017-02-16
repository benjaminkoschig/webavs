package globaz.corvus.process.paiement.mensuel;

import globaz.jade.client.util.JadeStringUtil;

public final class Key implements Comparable<Key> {

    // FIXME public de merde
    public String idCompteAnnexe;
    private String idTiersAdressePaiement;
    private String refPmt;

    public Key(Key k) {
        idCompteAnnexe = k.idCompteAnnexe;
        idTiersAdressePaiement = k.idTiersAdressePaiement;
        refPmt = k.refPmt;
    }

    public Key(String idCompteAnnexe, String idTiersAdressePaiement, String refPmt) {
        this.idCompteAnnexe = idCompteAnnexe;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.refPmt = refPmt;
    }

    @Override
    public int compareTo(Key key) {
        if (idCompteAnnexe.compareTo(key.idCompteAnnexe) != 0) {
            return idCompteAnnexe.compareTo(key.idCompteAnnexe);
        } else if (idTiersAdressePaiement.compareTo(key.idTiersAdressePaiement) != 0) {
            return idTiersAdressePaiement.compareTo(key.idTiersAdressePaiement);
        } else if (refPmt.compareTo(key.refPmt) != 0) {
            return refPmt.compareTo(key.refPmt);
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCompteAnnexe == null) ? 0 : idCompteAnnexe.hashCode());
        result = prime * result + ((idTiersAdressePaiement == null) ? 0 : idTiersAdressePaiement.hashCode());
        result = prime * result + ((refPmt == null) ? 0 : refPmt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Key other = (Key) obj;
        if (idCompteAnnexe == null) {
            if (other.idCompteAnnexe != null) {
                return false;
            }
        } else if (!idCompteAnnexe.equals(other.idCompteAnnexe)) {
            return false;
        }
        if (idTiersAdressePaiement == null) {
            if (other.idTiersAdressePaiement != null) {
                return false;
            }
        } else if (!idTiersAdressePaiement.equals(other.idTiersAdressePaiement)) {
            return false;
        }
        if (refPmt == null) {
            if (other.refPmt != null) {
                return false;
            }
        } else if (!refPmt.equals(other.refPmt)) {
            return false;
        }
        return true;
    }

    public boolean isReferenceDePaiementIdentique(Key key) {
        return areStringEquals(refPmt, key == null ? null : key.refPmt);
    }

    public boolean isAdresseDePaiementIdentique(Key key) {
        return areStringEquals(idTiersAdressePaiement, key == null ? null : key.idTiersAdressePaiement);
    }

    public boolean isCompteAnnexeIdentique(Key key) {
        return areStringEquals(idCompteAnnexe, key == null ? null : key.idCompteAnnexe);
    }

    public boolean isKeyIdentique(Key key) {
        return isCompteAnnexeIdentique(key) && isAdresseDePaiementIdentique(key) && isReferenceDePaiementIdentique(key);
    }

    private boolean areStringEquals(String a, String b) {
        // les 2 valeurs sont nulles
        boolean result = JadeStringUtil.isBlankOrZero(a) && JadeStringUtil.isBlankOrZero(b);
        if (result) {
            return false;
        } else {
            // Si un des 2 est null -> ils sont différents
            if (JadeStringUtil.isBlankOrZero(a) || JadeStringUtil.isBlankOrZero(b)) {
                return false;
            }
        }
        // Aucun des 2 n'est null, on compare les valeurs
        return a.equals(b);
    }

}
