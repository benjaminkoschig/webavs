package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.TypePeriode;

/**
 * <p>
 * Objet immuable contenant les donn�es pour l'affichage du d�tails du calcul dans l'�cran des rentes vers�e � tort
 * </p>
 * <p>
 * Chaque valeur d'entr�e est test� dans le constructeur afin qu'elle ne soit pas <code>null</code>
 * </p>
 * <p>
 * Cette objet est sp�cialis� pour la vue, et contient de ce fait quelques m�thodes sp�cifiques � cet effet
 * </p>
 */
public class RELigneDetailCalculRenteVerseeATort implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal montantMensuel;
    private Periode periode;

    /**
     * Construit cette objet en v�rifiant qu'aucun param�tre n'est <code>null</code> et v�rfie aussi que la p�riode
     * pass�e soit bien d�crite en mois-ann�e et pas jour-mois-ann�e, et la pr�sence d'une date de fin dans cette
     * p�riode
     * 
     * @param montantMensuel
     *            le montant pay� mensuellement durant la p�riode (le montant de la prestation due)
     * @param periode
     *            la p�riode durant laquelle cette prestation due a �t� vers�e, doit �tre compos�e de dates au format
     *            MM.AAAA et avoir une date de fin
     */
    public RELigneDetailCalculRenteVerseeATort(BigDecimal montantMensuel, Periode periode) {
        super();

        if (montantMensuel == null) {
            throw new IllegalArgumentException("[montantMensuel] can't be null");
        }
        if (montantMensuel.doubleValue() < 0.0) {
            throw new IllegalArgumentException("[montantMensul] must be positive");
        }
        if (periode == null) {
            throw new IllegalArgumentException("[periode] can't be null");
        }
        if (periode.getType() != TypePeriode.MOIS_ANNEE) {
            throw new IllegalArgumentException("[periode] must be a month/year periode, not a day/month/year");
        }
        if ("".equals(periode.getDateFin()) || "12.2999".equals(periode.getDateFin())) {
            throw new IllegalArgumentException("[periode] must have an end");
        }

        this.montantMensuel = montantMensuel;
        this.periode = periode;
    }

    /**
     * @return le mois de d�but du paiement de cette prestation due
     */
    public String getDateDebut() {
        return periode.getDateDebut();
    }

    /**
     * @return le mois de fin de paiement de cette prestation due
     */
    public String getDateFin() {
        return periode.getDateFin();
    }

    /**
     * @return le montant mensuel pay� (montant de la prestation due) mise au format pour l'�cran par la classe
     *         {@link FWCurrency}
     */
    public String getMontantMensuel() {
        return new FWCurrency(montantMensuel.doubleValue()).toStringFormat();
    }

    /**
     * @return le montant total pay� par cette prestation due durant la p�riode
     */
    public BigDecimal getMontantTotalPourLaPeriode() {
        int nombreDeMoisDansLaPeriode = getNombreMoisDansPeriode();

        return new BigDecimal(nombreDeMoisDansLaPeriode * montantMensuel.doubleValue()).setScale(2);
    }

    /**
     * @return le montant total pay� par cette prestation due durant la p�riode mise au format pour l'�cran par la
     *         classe {@link FWCurrency}
     */
    public String getMontantTotalPourLaPeriodeFormatte() {
        return new FWCurrency(getMontantTotalPourLaPeriode().doubleValue()).toStringFormat();
    }

    /**
     * @return le nombre de mois couvert par la p�riode de paiement de cette prestation due
     */
    public Integer getNombreMoisDansPeriode() {
        return JadeDateUtil.getNbMonthsBetween("01." + getDateDebut(), JadeDateUtil.getLastDateOfMonth(getDateFin()));
    }

    /**
     * @return la p�riode de paiement de la prestation due
     */
    public Periode getPeriode() {
        return periode;
    }
}
