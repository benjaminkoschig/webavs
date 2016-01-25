package ch.globaz.corvus.businessimpl.services.models.ordresversement;

import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSessionUtil;
import java.math.BigDecimal;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.corvus.domaine.Prestation;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Service de persistance pour les ordres de versement des rentes implémenté selon la persistance du framework Jade
 */
public class OrdresVersementCrudServiceJadeImpl extends CanevasCrudService<OrdreVersement> implements
        OrdresVersementCrudService {

    @Override
    public OrdreVersement create(OrdreVersement objetDeDomaine) {

        try {
            REOrdresVersements nouvelOV = new REOrdresVersements();
            nouvelOV.setCsType(objetDeDomaine.getType().getCodeSysteme().toString());
            nouvelOV.setIdPrestation(objetDeDomaine.getPrestation().getId().toString());
            nouvelOV.setIdTiers(objetDeDomaine.getTitulaire().getId().toString());
            nouvelOV.setIsCompense(objetDeDomaine.isCompense());
            nouvelOV.setIsCompensationInterDecision(objetDeDomaine.isCompensationInterDecision());
            nouvelOV.setMontantDette(objetDeDomaine.getMontantDette().toString());
            nouvelOV.setMontant(objetDeDomaine.getMontantCompense().toString());

            nouvelOV.setSession(BSessionUtil.getSessionFromThreadContext());
            nouvelOV.add();

            objetDeDomaine.setId(Long.parseLong(nouvelOV.getIdOrdreVersement()));
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public OrdreVersement read(Long id) {

        OrdreVersement objetDeDomaine = new OrdreVersement();
        objetDeDomaine.setId(id);

        try {
            REOrdresVersements ov = new REOrdresVersements();
            ov.setSession(BSessionUtil.getSessionFromThreadContext());
            ov.setIdOrdreVersement(objetDeDomaine.getId().toString());
            ov.retrieve();

            if (ov.isNew()) {
                throw new RETechnicalException("Entity not found");
            }

            objetDeDomaine.setCompensationInterDecision(ov.getIsCompensationInterDecision());
            objetDeDomaine.setCompense(ov.getIsCompense());
            objetDeDomaine.setMontantCompense(new BigDecimal(ov.getMontant()));
            objetDeDomaine.setMontantDette(new BigDecimal(ov.getMontantDette()));

            Prestation prestation = new Prestation();
            prestation.setId(Long.parseLong(ov.getIdPrestation()));
            objetDeDomaine.setPrestation(prestation);

            PersonneAVS titulaire = new PersonneAVS();
            titulaire.setId(Long.parseLong(ov.getIdTiers()));
            objetDeDomaine.setTitulaire(titulaire);

            objetDeDomaine.setType(ov.getCsTypeOrdreVersement());
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public OrdreVersement update(OrdreVersement objetDeDomaine) {

        try {
            REOrdresVersements ovAModifier = new REOrdresVersements();
            ovAModifier.setIdOrdreVersement(objetDeDomaine.getId().toString());

            ovAModifier.setSession(BSessionUtil.getSessionFromThreadContext());
            ovAModifier.retrieve();

            ovAModifier.setCsType(objetDeDomaine.getType().getCodeSysteme().toString());
            ovAModifier.setIdPrestation(objetDeDomaine.getPrestation().getId().toString());
            ovAModifier.setIsCompense(objetDeDomaine.isCompense());
            ovAModifier.setIsCompensationInterDecision(objetDeDomaine.isCompensationInterDecision());
            ovAModifier.setMontantDette(objetDeDomaine.getMontantDette().toString());
            ovAModifier.setMontant(objetDeDomaine.getMontantCompense().toString());

            ovAModifier.update();

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

}
