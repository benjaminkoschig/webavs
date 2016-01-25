package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIReferenceRubrique;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

class GenerateEcrituresDette extends GenerateEcrituresDispatcher {

    private Map<String, SectionSimpleModel> sections;

    public GenerateEcrituresDette(PrestationOvDecompte decompte, MontantDispo montantDispo,
            List<SectionSimpleModel> sections) throws JadeApplicationException {
        super(decompte, montantDispo, decompte.getDettes());
        this.sections = toMap(sections);
        generateAllOperations();
    }

    @Override
    public void addOperation(OrdreVersement ov, BigDecimal montant, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException {
        SectionSimpleModel section = resolveSection(ov);

        ecritures.add(this.generateEcriture(SectionPegasus.DECISION_PC, APIEcriture.DEBIT,
                APIReferenceRubrique.COMPENSATION_RENTES, montant, null, compteAnnexe.getIdCompteAnnexe(),
                TypeEcriture.DETTE, ov));

        ecritures.add(this.generateEcriture(null, APIEcriture.CREDIT, APIReferenceRubrique.COMPENSATION_RENTES,
                montant, section, section.getIdCompteAnnexe(), TypeEcriture.DETTE, ov));
    }

    @Override
    public BigDecimal resolveMontantOv(SimpleOrdreVersement ov) {
        BigDecimal montant = null;
        if (JadeStringUtil.isBlankOrZero((ov.getMontantDetteModifier()))) {
            montant = new BigDecimal(ov.getMontant());
        } else {
            montant = new BigDecimal(ov.getMontantDetteModifier());
        }
        return montant;
    }

    private SectionSimpleModel resolveSection(OrdreVersement ov) throws ComptabiliserLotException {
        SectionSimpleModel section = sections.get(ov.getIdSectionDetteEnCompta());
        if (section == null) {
            throw new ComptabiliserLotException("Unable to resolve the section with this id section: "
                    + ov.getIdSectionDetteEnCompta() + ", for this id ov: " + ov.getId());
        }
        return section;
    }

    private Map<String, SectionSimpleModel> toMap(List<SectionSimpleModel> sections) {
        Map<String, SectionSimpleModel> map = new HashMap<String, SectionSimpleModel>();
        for (SectionSimpleModel section : sections) {
            map.put(section.getIdSection(), section);
        }
        return map;
    }
}
