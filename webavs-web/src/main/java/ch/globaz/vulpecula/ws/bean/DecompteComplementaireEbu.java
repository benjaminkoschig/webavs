/**
 * 
 */
package ch.globaz.vulpecula.ws.bean;

import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.models.decompte.TableauContributions;

/**
 * @author sel
 * 
 */
@XmlType(name = "decompteComplementaire")
public class DecompteComplementaireEbu extends DecompteEbu {

    private boolean hasAvs;
    private List<LigneDecompteComplementaireEbu> decompteLines;

    public DecompteComplementaireEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public DecompteComplementaireEbu(Decompte decompte, String syncroId) {
        List<Cotisation> listeCoti = VulpeculaServiceLocator.getCotisationService().findByIdAffilie(
                decompte.getEmployeur().getId());
        Employeur emp = decompte.getEmployeur();
        emp.setCotisations(listeCoti);
        if (decompte.getDateEtablissement() != null
                || !JadeStringUtil.isBlankOrZero(decompte.getDateEtablissement().getSwissValue())) {
            hasAvs = emp.isSoumisAVS(decompte.getDateEtablissement());
        } else {
            hasAvs = emp.isSoumisAVS();
        }
        idDecompte = decompte.getId();
        dateDecompte = decompte.getDateEtablissementAsSwissValue();
        periodBegin = decompte.getPeriodeDebutFormate();
        periodEnd = decompte.getPeriodeFinFormate();
        status = EtatDecompteEbu.fromEtatDecompte(decompte.getEtat());
        // totalSalairePortail = "0";
        // totalSalaireWebMetier = "0";
        typeDecompte = decompte.getType();
        tableContribution = new TableauContributionsEbu(new TableauContributions(decompte));
        synchronize_id = syncroId;
    }

    /**
     * @return the listDecompte
     */
    public List<LigneDecompteComplementaireEbu> getDecompteLines() {
        return decompteLines;
    }

    /**
     * @param listDecompte the listDecompte to set
     */
    public void setDecompteLines(List<LigneDecompteComplementaireEbu> listLigneDecompte) {
        decompteLines = listLigneDecompte;
    }

    public boolean isHasAvs() {
        return hasAvs;
    }

    public void setHasAvs(boolean hasAvs) {
        this.hasAvs = hasAvs;
    }
}
