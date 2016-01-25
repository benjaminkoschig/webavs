/**
 * 
 */
package ch.globaz.vulpecula.domain.repositories.is;

import java.util.Collection;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.external.models.pyxis.Pays;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Pays}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface HistoriqueProcessusAfRepository extends Repository<HistoriqueProcessusAf> {
    public HistoriqueProcessusAf findByIdProcessus(String idProcessus);

    public Collection<String> findIdProcessusByType(String forBusinessProcessus);
}
