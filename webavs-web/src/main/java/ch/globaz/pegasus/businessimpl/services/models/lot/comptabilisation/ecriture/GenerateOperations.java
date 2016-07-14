package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import java.util.List;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;

/**
 * Interface définissant un point d'entrée publique pour la génération des opérations de compatabilisation pour le
 * traitement des lots PC
 * 
 * @author sce
 * @version 1.12
 * 
 */
public interface GenerateOperations {
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance) throws JadeApplicationException;
}
