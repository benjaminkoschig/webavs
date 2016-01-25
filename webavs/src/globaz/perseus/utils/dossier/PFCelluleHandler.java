/**
 * 
 */
package globaz.perseus.utils.dossier;

import globaz.framework.util.FWCurrency;

/**
 * @author DDE
 * 
 */
public class PFCelluleHandler {

    private Boolean colored;
    private Boolean erreur;
    private Boolean moisCourant;
    private String montantMensuel;
    private String montantRetro;
    private Integer order;
    private Boolean retroActif;
    private String texte;
    private Boolean titre;
    private Boolean total;

    public PFCelluleHandler(Integer order) {
        colored = false;
        retroActif = false;
        texte = "&nbsp;";
        titre = false;
        total = false;
        this.order = order;
        moisCourant = false;
        erreur = false;
    }

    /**
     * @return the colored
     */
    public Boolean getColored() {
        return colored;
    }

    /**
     * @return the erreur
     */
    public Boolean getErreur() {
        return erreur;
    }

    /**
     * @return the moisCourant
     */
    public Boolean getMoisCourant() {
        return moisCourant;
    }

    /**
     * @return the montantMensuel
     */
    public String getMontantMensuel() {
        return montantMensuel;
    }

    /**
     * @return the montantRetro
     */
    public String getMontantRetro() {
        return montantRetro;
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @return the retroActif
     */
    public Boolean getRetroActif() {
        return retroActif;
    }

    /**
     * @return the texte
     */
    public String getTexte() {
        return texte;
    }

    /**
     * @return the titre
     */
    public Boolean getTitre() {
        return titre;
    }

    /**
     * @return the total
     */
    public Boolean getTotal() {
        return total;
    }

    /**
     * @param colored
     *            the colored to set
     */
    public void setColored(Boolean colored) {
        this.colored = colored;
    }

    /**
     * @param erreur
     *            the erreur to set
     */
    public void setErreur(Boolean erreur) {
        this.erreur = erreur;
    }

    /**
     * @param moisCourant
     *            the moisCourant to set
     */
    public void setMoisCourant(Boolean moisCourant) {
        this.moisCourant = moisCourant;
    }

    /**
     * @param montantMensuel
     *            the montantMensuel to set
     */
    public void setMontantMensuel(String montantMensuel) {
        FWCurrency currency = new FWCurrency(montantMensuel);
        this.montantMensuel = currency.toStringFormat();
    }

    /**
     * @param montantRetro
     *            the montantRetro to set
     */
    public void setMontantRetro(String montantRetro) {
        FWCurrency currency = new FWCurrency(montantRetro);
        this.montantRetro = currency.toStringFormat();
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @param retroActif
     *            the retroActif to set
     */
    public void setRetroActif(Boolean retroActif) {
        this.retroActif = retroActif;
    }

    /**
     * @param texte
     *            the texte to set
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     * @param titre
     *            the titre to set
     */
    public void setTitre(Boolean titre) {
        this.titre = titre;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(Boolean total) {
        this.total = total;
    }

}
