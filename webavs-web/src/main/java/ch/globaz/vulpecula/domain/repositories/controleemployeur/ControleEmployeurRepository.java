package ch.globaz.vulpecula.domain.repositories.controleemployeur;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface ControleEmployeurRepository extends Repository<ControleEmployeur> {
    List<ControleEmployeur> findByIdEmployeur(String idEmployeur);

    ControleEmployeur findDernierControleByIdEmployeur(String idEmployeur);

    /**
     * Récupère les controles d'employeur dans la période passée. Utilisé dans la liste des controles d'employeur pour
     * une année
     * 
     * @param periode
     * @return
     */
    List<ControleEmployeur> findControleEmployeurInPeriode(Periode periode);

    List<Employeur> findEmployeurAControlerParRapportADateControleAu(Date dateControleAu)
            throws JadePersistenceException;

    List<Employeur> findEmployeurAControlerTous(Date dateControleAu) throws JadePersistenceException;

}
