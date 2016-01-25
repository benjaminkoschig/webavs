package ch.globaz.pyxis.domaine;

import ch.globaz.common.domaine.Checkers;

/**
 * Objet de domaine représentant une personne physique à qui un NSS a été donné
 */
public class PersonneAVS extends Personne {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private NumeroSecuriteSociale nss;

    public PersonneAVS() {
        super();

        nss = null;
    }

    /**
     * @return le NSS de cette personne
     * @throws IllegalStateException
     *             si aucun NSS défini pour cette personne
     */
    public NumeroSecuriteSociale getNss() {
        if (nss == null) {
            throw new IllegalStateException("No social security number found for this person");
        }
        return nss;
    }

    /**
     * (re-)défini le NSS de cette personne
     * 
     * @param nss
     *            un NSS valide (voir {@link NumeroSecuriteSociale} pour en savoir plus sur la validité d'un NSS)
     * @throws NullPointerException
     *             si le NSS passé en paramètre est null
     */
    public void setNss(final NumeroSecuriteSociale nss) {
        Checkers.checkNotNull(nss, "nss");
        this.nss = nss;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PersonneAVS(");

        stringBuilder.append("id:").append(getId()).append(", ");
        stringBuilder.append("nss:").append(getNss()).append(", ");
        stringBuilder.append("last name:").append(getNom()).append(", ");
        stringBuilder.append("first name:").append(getPrenom());

        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
