package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.osiris.business.model.SectionSimpleModel;

public class SectionFactory {

    public static SectionSimpleModel generateSection(String idCompteAnnexe, String idSection) {
        SectionSimpleModel sectionSimpleModel = new SectionSimpleModel();
        sectionSimpleModel.setIdCompteAnnexe(idCompteAnnexe);
        sectionSimpleModel.setIdSection(idSection);
        return sectionSimpleModel;
    }

}
