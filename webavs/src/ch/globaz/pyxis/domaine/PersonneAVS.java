package ch.globaz.pyxis.domaine;

import ch.globaz.common.domaine.Checkers;

/**
 * Objet de domaine repr�sentant une personne physique � qui un NSS a �t� donn�
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
     *             si aucun NSS d�fini pour cette personne
     */
    public NumeroSecuriteSociale getNss() {
        if (nss == null) {
            throw new IllegalStateException("No social security number found for this person");
        }
        return nss;
    }

    /**
     * (re-)d�fini le NSS de cette personne
     * 
     * @param nss
     *            un NSS valide (voir {@link NumeroSecuriteSociale} pour en savoir plus sur la validit� d'un NSS)
     * @throws NullPointerException
     *             si le NSS pass� en param�tre est null
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
