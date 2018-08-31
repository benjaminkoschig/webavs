package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.models.decompte.TableauContributions;

@XmlType(name = "decompteSpecial")
public class DecompteSpecialEbu extends DecompteEbu {

    public DecompteSpecialEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public DecompteSpecialEbu(Decompte decompte, String synchronize_id2) {
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

}
