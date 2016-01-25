package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import java.math.BigDecimal;

public class OrdreversmentDetteDisplay extends OrdreVersementDisplay {

    private String idOv = null;
    private String idSection = null;

    public OrdreversmentDetteDisplay(String idSection, BigDecimal montant, String csTypeOv) {
        super(csTypeOv, montant);
        this.idSection = idSection;
    }

    public OrdreversmentDetteDisplay(String idSection, BigDecimal montant, String csTypeOv, String idOV,
            String noPeriode, boolean isRequerant) {
        super(csTypeOv, montant, noPeriode, isRequerant);
        this.idSection = idSection;
        idOv = idOV;
    }

    @Override
    public String getDescriptionOv() {
        if (JadeStringUtil.isBlankOrZero(idSection)) {
            throw new IllegalArgumentException("Unable to read the section, the idSection passed is empty");
        }

        CASectionManager mgr = new CASectionManager();
        mgr.setForIdSection(idSection);

        try {
            mgr.find();
            if (mgr.hasErrors()) {
                new RuntimeException("Technical exception, Error in research of the section");
            }
        } catch (Exception e) {
            new RuntimeException("Technical exception, Error in research of the section", e);
        }
        if (mgr.size() == 1) {
            CASection section = (CASection) mgr.getFirstEntity();
            return section.getDescription();
        } else {
            return null;
        }
    }

    @Override
    public String getId() {
        return idSection + "_id:" + idOv;
    }

}
