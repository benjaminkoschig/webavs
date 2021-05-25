/*
 * Créé le 15 juin 05
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APEnvoyerAnnoncesViewBean extends PRAbstractViewBeanSupport {

    private boolean dejaEnvoye = false;
    private String eMailAddress = "";
    private String forDateEnvoi = "";
    private String forMoisAnneeComptable = "";
    private boolean okPourReenvoi = false;
    private String forGenreServices = "";

    /**
     * getter pour l'attribut EMail address
     *
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * getter pour l'attribut for date envoi
     *
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     *
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    /**
     * getter pour l'attribut deja envoye
     *
     * @return la valeur courante de l'attribut deja envoye
     */
    public boolean isDejaEnvoye() {
        return dejaEnvoye;
    }

    /**
     * getter pour l'attribut ok pour reenvoi
     *
     * @return la valeur courante de l'attribut ok pour reenvoi
     */
    public boolean isOkPourReenvoi() {
        return okPourReenvoi;
    }

    /**
     * setter pour l'attribut deja envoye
     *
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setDejaEnvoye(boolean b) {
        dejaEnvoye = b;
    }

    /**
     * setter pour l'attribut EMail address
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * setter pour l'attribut for date envoi
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * setter pour l'attribut ok pour reenvoi
     *
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setOkPourReenvoi(boolean b) {
        okPourReenvoi = b;
    }

    /**
     * (non-Javadoc)
     *
     * @return DOCUMENT ME!
     * @see globaz.apg.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        // TODO
        return true;
    }

    public String getForGenreServices() {
        return forGenreServices;
    }

    public void setForGenreServices(final String genreServices) {
        this.forGenreServices = genreServices;
    }
}
