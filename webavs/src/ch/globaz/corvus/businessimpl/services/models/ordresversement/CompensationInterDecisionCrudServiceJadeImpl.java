package ch.globaz.corvus.businessimpl.services.models.ordresversement;

import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.business.services.models.ordresversements.CompensationInterDecisionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.corvus.domaine.CompensationInterDecision;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.pyxis.business.services.PersonneAvsCrudService;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class CompensationInterDecisionCrudServiceJadeImpl extends CanevasCrudService<CompensationInterDecision>
        implements CompensationInterDecisionCrudService {

    private OrdresVersementCrudService ordresVersementCrudService;
    private PersonneAvsCrudService personneAvsCrudService;

    public CompensationInterDecisionCrudServiceJadeImpl(OrdresVersementCrudService ordresVersementCrudService,
            PersonneAvsCrudService personneAvsCrudService) {
        super();

        Checkers.checkNotNull(ordresVersementCrudService, "ordresVersementCrudService");
        Checkers.checkNotNull(personneAvsCrudService, "personneAvsCrudService");

        this.ordresVersementCrudService = ordresVersementCrudService;
        this.personneAvsCrudService = personneAvsCrudService;
    }

    @Override
    public CompensationInterDecision create(CompensationInterDecision objetDeDomaine) {

        try {
            RECompensationInterDecisions nouvelleCID = new RECompensationInterDecisions();
            nouvelleCID.setSession(BSessionUtil.getSessionFromThreadContext());
            nouvelleCID.setIdOrdreVersement(objetDeDomaine.getOrdreVersementDecisionCompensee().getId().toString());
            nouvelleCID.setIdOVCompensation(objetDeDomaine.getOrdreVersementDecisionPonctionnee().getId().toString());
            nouvelleCID.setIdTiers(objetDeDomaine.getBeneficiaireCompensationInterDecision().getId().toString());
            nouvelleCID.setMontant(objetDeDomaine.getMontantCompense().toString());
            nouvelleCID.add();

            objetDeDomaine.setId(Long.parseLong(nouvelleCID.getId()));
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public boolean delete(Long id) {

        CompensationInterDecision objetDeDomaine = this.read(id);

        BTransaction transaction = null;
        try {
            transaction = new BTransaction(BSessionUtil.getSessionFromThreadContext());
            transaction.openTransaction();
            try {

                RECompensationInterDecisions cid = new RECompensationInterDecisions();
                cid.setSession(BSessionUtil.getSessionFromThreadContext());
                cid.setIdCompensationInterDecision(objetDeDomaine.getId().toString());
                cid.retrieve(transaction);

                REOrdresVersements ovSpecifiqueCID = new REOrdresVersements();
                ovSpecifiqueCID.setSession(BSessionUtil.getSessionFromThreadContext());
                ovSpecifiqueCID.setIdOrdreVersement(objetDeDomaine.getOrdreVersementDecisionPonctionnee().getId()
                        .toString());
                ovSpecifiqueCID.retrieve(transaction);

                ovSpecifiqueCID.delete(transaction);
                cid.delete(transaction);

                transaction.commit();
            } catch (Exception ex) {
                transaction.rollback();
                throw new RETechnicalException(ex);
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            }
        }

        return true;
    }

    @Override
    public CompensationInterDecision read(Long id) {

        CompensationInterDecision objetDeDomaine = new CompensationInterDecision();
        objetDeDomaine.setId(id);
        try {
            RECompensationInterDecisions cid = new RECompensationInterDecisions();
            cid.setSession(BSessionUtil.getSessionFromThreadContext());
            cid.setIdCompensationInterDecision(objetDeDomaine.getId().toString());
            cid.retrieve();

            PersonneAVS beneficiaireCompensationInterDecision = new PersonneAVS();
            beneficiaireCompensationInterDecision.setId(Long.parseLong(cid.getIdTiers()));
            beneficiaireCompensationInterDecision = personneAvsCrudService.read(beneficiaireCompensationInterDecision);
            objetDeDomaine.setBeneficiaireCompensationInterDecision(beneficiaireCompensationInterDecision);

            objetDeDomaine.setMontantCompense(new BigDecimal(cid.getMontant()));

            OrdreVersement ordreVersementDecisionCompensee = new OrdreVersement();
            ordreVersementDecisionCompensee.setId(Long.parseLong(cid.getIdOrdreVersement()));
            ordreVersementDecisionCompensee = ordresVersementCrudService.read(ordreVersementDecisionCompensee);
            objetDeDomaine.setOrdreVersementDecisionCompensee(ordreVersementDecisionCompensee);

            OrdreVersement ordreVersementDecisionPonctionnee = new OrdreVersement();
            ordreVersementDecisionPonctionnee.setId(Long.parseLong(cid.getIdOVCompensation()));
            ordreVersementDecisionPonctionnee = ordresVersementCrudService.read(ordreVersementDecisionPonctionnee);
            objetDeDomaine.setOrdreVersementDecisionPonctionnee(ordreVersementDecisionPonctionnee);
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public CompensationInterDecision update(CompensationInterDecision objetDeDomaine) {

        BITransaction transaction;
        try {
            transaction = new BTransaction(BSessionUtil.getSessionFromThreadContext());
            transaction.openTransaction();

            try {

                RECompensationInterDecisions cid = new RECompensationInterDecisions();
                cid.setSession(BSessionUtil.getSessionFromThreadContext());
                cid.setIdCompensationInterDecision(objetDeDomaine.getId().toString());
                cid.retrieve(transaction);

                REOrdresVersements ovSpecifiqueCID = new REOrdresVersements();
                ovSpecifiqueCID.setSession(BSessionUtil.getSessionFromThreadContext());
                ovSpecifiqueCID.setIdOrdreVersement(objetDeDomaine.getOrdreVersementDecisionPonctionnee().getId()
                        .toString());
                ovSpecifiqueCID.retrieve(transaction);

                // il n'est possible que de modifier le montant compensé
                cid.setMontant(objetDeDomaine.getMontantCompense().toString());
                ovSpecifiqueCID.setMontant(objetDeDomaine.getMontantCompense().toString());

                cid.update(transaction);
                ovSpecifiqueCID.update(transaction);
            } catch (Exception ex) {
                transaction.addErrors(ex.toString());
                throw new RETechnicalException(ex);
            } finally {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
                transaction.closeTransaction();
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

}
