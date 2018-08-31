package ch.globaz.vulpecula.documents.listesinternes;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.domain.models.common.Annee;

class TaxationOfficeContainerService {
    /**
     * Si on souhaite omettre les taxations d'office, on va toutes les rechercher par rapport à l'année courante.
     * 
     * @param annee Année à laquelle rechercher les T.O
     * @return TaxationOfficeContainer utilisé pour les calculs
     */
    public TaxationOfficeContainer getTOContainer(Annee annee, boolean omitTO) {
        List<TaxationOfficeDTO> tos = new ArrayList<TaxationOfficeDTO>();
        if (omitTO) {
            tos = retrieveTO(annee);
        }
        return new TaxationOfficeContainer(tos);
    }

    private List<TaxationOfficeDTO> retrieveTO(Annee annee) {
        String query = "SELECT rub.IDEXTERNE AS ID_EXTERNE,lito.MASSE,lito.TAUX,lito.MONTANT,adm.HBCADM AS CODE,hdec.date AS DATE FROM schema.PT_TAXATIONS_OFFICE to "
                + "JOIN schema.PT_LIGNES_TAXATIONS lito ON lito.ID_PT_TAXATIONS_OFFICE=to.ID "
                + "JOIN schema.PT_DECOMPTES dec ON to.ID_PT_DECOMPTES=dec.ID "
                + "JOIN schema.PT_HISTORIQUE_DECOMPTES hdec ON hdec.ID_PT_DECOMPTES=dec.ID "
                + "inner join schema.Afaffip aff on aff.MAIAFF=dec.ID_PT_EMPLOYEURS "
                + "JOIN schema.TIADMIP adm ON adm.HTITIE=aff.MACONV "
                + "inner join schema.afcotip cot on cot.MEICOT=lito.ID_AFCOTIP "
                + "inner join schema.AFASSUP ass on cot.MBIASS=ass.MBIASS "
                + "inner join schema.CARUBRP rub on ass.MBIRUB=rub.IDRUBRIQUE "
                + "WHERE to.CS_ETAT=68019004 AND hdec.CS_ETAT=68012009 "
                + "AND hdec.DATE>="
                + annee.getFirstDayOfYear().getValue() + " AND hdec.DATE<=" + annee.getLastDayOfYear().getValue();
        return SCM.newInstance(TaxationOfficeDTO.class).query(query).execute();
    }
}
