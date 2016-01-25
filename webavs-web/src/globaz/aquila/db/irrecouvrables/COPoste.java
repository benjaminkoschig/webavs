/*
 * Créé le 13 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.irrecouvrables;

import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <H1>Description</H1>
 * <p>
 * Une classe qui reprend les données de ventilation retournées par Osiris et les réorganisent de manière à faciliter
 * l'affichage dans l'écran de ventilation des irrécouvrables.
 * </p>
 * 
 * @author vre
 */
public class COPoste implements Serializable, Comparable<Object> {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ID_EXTERNE_BEGIN_WITH_2110_3300 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_3300
            .substring(0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_3300.substring(5, 9) + ")(.)*";
    private boolean cotisationsPersonnelles;
    private List<CODetailSection> detailsSections = new LinkedList<CODetailSection>();
    private String idRubriqueIrrecouvrable;
    private Map<String, CODetailSection> idUToDetailSection = new HashMap<String, CODetailSection>();// key=idSection_idRubrique
    private String libellePoste;
    private BigDecimal montantIrrecouvrable = new BigDecimal(0);
    private BigDecimal montantTotalDu = new BigDecimal(0);
    private BigDecimal montantTotalVerse = new BigDecimal(0);
    private int ordre;

    /**
     * @param rubriqueIrr
     * @param rubrique
     * @param ordre
     */
    public COPoste(CARubrique rubriqueIrr, CARubrique rubrique, String ordre) {
        idRubriqueIrrecouvrable = rubriqueIrr.getIdRubrique();
        libellePoste = rubriqueIrr.getDescription();
        this.ordre = Integer.parseInt(ordre);
        Pattern pattern = Pattern.compile(COPoste.ID_EXTERNE_BEGIN_WITH_2110_3300);
        Matcher matcher = pattern.matcher(rubriqueIrr.getIdExterne());
        cotisationsPersonnelles = matcher.matches();
    }

    /**
     * @param detailSection
     */
    public void addDetailSection(CODetailSection detailSection) {
        idUToDetailSection
                .put(detailSection.getIdSection() + "_" + detailSection.getIdRubrique() + "_"
                        + detailSection.getTypeOrdre(), detailSection);
        detailsSections.add(detailSection);
        montantTotalDu = montantTotalDu.add(detailSection.getMontantDu());
        montantTotalVerse = montantTotalVerse.add(detailSection.getMontantVerse());
        montantIrrecouvrable = montantIrrecouvrable.add(detailSection.getMontantIrrecouvrable());
    }

    @Override
    public int compareTo(Object arg0) {
        COPoste poste = (COPoste) arg0;

        if (ordre < poste.ordre) {
            return -1;
        } else if (ordre > poste.ordre) {
            return 1;
        } else {
            return 0;
        }
    }

    public List<CODetailSection> getDetailsSections() {
        return detailsSections;
    }

    public String getIdRubriqueIrrecouvrable() {
        return idRubriqueIrrecouvrable;
    }

    public String getLibellePoste() {
        return libellePoste;
    }

    public BigDecimal getMontantIrrecouvrable() {
        if (montantIrrecouvrable == null) {
            if ((montantTotalDu == null) || (montantTotalVerse == null)) {
                throw new IllegalStateException(
                        "Le montant dû ou le montant versé ne sont pas renseigné, impossible de calculer le montant irrécouvrable");
            }

            montantIrrecouvrable = montantTotalDu.subtract(montantTotalVerse);
        }

        return montantIrrecouvrable;
    }

    public String getMontantIrrecouvrableFormatte() {
        return JANumberFormatter.format(getMontantIrrecouvrable());
    }

    public BigDecimal getMontantTotalDu() {
        return montantTotalDu;
    }

    public String getMontantTotalDuFormatte() {
        return JANumberFormatter.format(getMontantTotalDu());
    }

    public BigDecimal getMontantTotalVerse() {
        return montantTotalVerse;
    }

    public String getMontantTotalVerseFormatte() {
        return JANumberFormatter.format(getMontantTotalVerse());
    }

    public int getOrdre() {
        return ordre;
    }

    public boolean isCotisationsPersonnelles() {
        return cotisationsPersonnelles;
    }

    /** recalcule les sommes des montants verses et irrecouvrables. */
    public void resetMontants() {
        montantIrrecouvrable = new BigDecimal(0);
        montantTotalVerse = new BigDecimal(0);

        for (Iterator<CODetailSection> detailsIterator = detailsSections.iterator(); detailsIterator.hasNext();) {
            CODetailSection detailSection = detailsIterator.next();

            detailSection.resetMontants();
            montantTotalVerse = montantTotalVerse.add(detailSection.getMontantVerse());
            montantIrrecouvrable = montantIrrecouvrable.add(detailSection.getMontantIrrecouvrable());
        }
    }

    /**
     * @param currency
     */
    public void setMontantIrrecouvrable(BigDecimal currency) {
        montantIrrecouvrable = currency;
    }

    /**
     * @param currency
     */
    public void setMontantTotalDu(BigDecimal currency) {
        montantTotalDu = currency;
    }

    /**
     * @param currency
     */
    public void setMontantTotalVerse(BigDecimal currency) {
        montantTotalVerse = currency;
    }

    /**
     * @param idSection
     * @param idRubrique
     * @param typeOrdre
     * @param montant
     */
    public void setMontantVerse(String idSection, String idRubrique, String typeOrdre, String montant) {
        CODetailSection detailSection = idUToDetailSection.get(idSection + "_" + idRubrique + "_" + typeOrdre);

        detailSection.setMontantVerse(new BigDecimal(montant));
    }

    /**
     * @param ordre
     */
    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
