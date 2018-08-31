/**
 * 
 */
package ch.globaz.vulpecula.ws.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.models.decompte.TableauContributions;

/**
 * @author sel
 * 
 */
@XmlType(name = "decomptePeriodique")
public class DecomptePeriodiqueEbu extends DecompteEbu {

    private List<LigneDecomptePeriodiqueEbu> decompteLines;

    public DecomptePeriodiqueEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public DecomptePeriodiqueEbu(Decompte decompte, String synchronize_id2) {
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
     * @return the listDecompte
     */
    public List<LigneDecomptePeriodiqueEbu> getDecompteLines() {
        return decompteLines;
    }

    /**
     * @param listDecompte the listDecompte to set
     */
    public void setDecompteLines(List<LigneDecomptePeriodiqueEbu> listLigneDecompte) {
        decompteLines = listLigneDecompte;
    }
}
