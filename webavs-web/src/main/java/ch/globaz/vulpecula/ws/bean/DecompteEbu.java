package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlElement;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.models.decompte.TableauContributions;

/**
 * 
 * @since eBMS 1.0
 */
public abstract class DecompteEbu {
    @XmlElement(nillable = false, required = true)
    protected String idDecompte;
    @XmlElement(nillable = false, required = true)
    protected String dateDecompte;
    @XmlElement(nillable = false, required = true)
    protected String periodBegin;
    @XmlElement(nillable = false, required = true)
    protected String periodEnd;
    @XmlElement()
    protected EtatDecompteEbu status;
    @XmlElement()
    protected TypeDecompte typeDecompte;

    @XmlElement(name = "synchronize_id")
    protected String synchronize_id;
    // protected String totalSalairePortail;
    // protected String totalSalaireWebMetier;

    @XmlElement()
    protected TableauContributionsEbu tableContribution;

    // private List<LigneDecomptePeriodiqueEbu> decompteLines;
    // private List<LigneDecompteComplementaireEbu> decompteLinesComplementaire;

    public DecompteEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public DecompteEbu(Decompte decompte, String synchronize_id2) {
        idDecompte = decompte.getId();
        dateDecompte = decompte.getDateEtablissementAsSwissValue();
        periodBegin = decompte.getPeriodeDebutFormate();
        periodEnd = decompte.getPeriodeFinFormate();
        status = EtatDecompteEbu.fromEtatDecompte(decompte.getEtat());
        // totalSalairePortail = "0";
        // totalSalaireWebMetier = "0";
        typeDecompte = decompte.getType();
        tableContribution = new TableauContributionsEbu(new TableauContributions(decompte));
        synchronize_id = synchronize_id2;
    }

    /**
     * @return the id
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(EtatDecompteEbu status) {
        this.status = status;
    }

    public boolean isEnCours() {
        return EtatDecompteEbu.EN_COURS.equals(status);
    }

    @Override
    public String toString() {
        return "Decompte  : " + idDecompte + ", " + dateDecompte + ", " + periodBegin + ", " + periodEnd + ", "
                + status + ", " + typeDecompte;
    }
}
