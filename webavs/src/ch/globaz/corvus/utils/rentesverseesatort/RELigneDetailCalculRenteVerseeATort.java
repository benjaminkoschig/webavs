package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.TypePeriode;

/**
 * <p>
 * Objet immuable contenant les données pour l'affichage du détails du calcul dans l'écran des rentes versée à tort
 * </p>
 * <p>
 * Chaque valeur d'entrée est testé dans le constructeur afin qu'elle ne soit pas <code>null</code>
 * </p>
 * <p>
 * Cette objet est spécialisé pour la vue, et contient de ce fait quelques méthodes spécifiques à cet effet
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
     * Construit cette objet en vérifiant qu'aucun paramètre n'est <code>null</code> et vérfie aussi que la période
     * passée soit bien décrite en mois-année et pas jour-mois-année, et la présence d'une date de fin dans cette
     * période
     * 
     * @param montantMensuel
     *            le montant payé mensuellement durant la période (le montant de la prestation due)
     * @param periode
     *            la période durant laquelle cette prestation due a été versée, doit être composée de dates au format
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
     * @return le mois de début du paiement de cette prestation due
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
     * @return le montant mensuel payé (montant de la prestation due) mise au format pour l'écran par la classe
     *         {@link FWCurrency}
     */
    public String getMontantMensuel() {
        return new FWCurrency(montantMensuel.doubleValue()).toStringFormat();
    }

    /**
     * @return le montant total payé par cette prestation due durant la période
     */
    public BigDecimal getMontantTotalPourLaPeriode() {
        int nombreDeMoisDansLaPeriode = getNombreMoisDansPeriode();

        return new BigDecimal(nombreDeMoisDansLaPeriode * montantMensuel.doubleValue()).setScale(2);
    }

    /**
     * @return le montant total payé par cette prestation due durant la période mise au format pour l'écran par la
     *         classe {@link FWCurrency}
     */
    public String getMontantTotalPourLaPeriodeFormatte() {
        return new FWCurrency(getMontantTotalPourLaPeriode().doubleValue()).toStringFormat();
    }

    /**
     * @return le nombre de mois couvert par la période de paiement de cette prestation due
     */
    public Integer getNombreMoisDansPeriode() {
        return JadeDateUtil.getNbMonthsBetween("01." + getDateDebut(), JadeDateUtil.getLastDateOfMonth(getDateFin()));
    }

    /**
     * @return la période de paiement de la prestation due
     */
    public Periode getPeriode() {
        return periode;
    }
}
