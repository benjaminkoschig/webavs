package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.prestation.tools.PRSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.SectionPegasus;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.TypeEcriture;

class SectionRubriqueResolver {

    private String annee;
    private Map<String, SectionSimpleModel> cacheSection = new ConcurrentHashMap<String, SectionSimpleModel>();
    private JournalSimpleModel journal;
    private Map<String, APIRubrique> rubriques = new ConcurrentHashMap<String, APIRubrique>();

    public SectionRubriqueResolver(JournalSimpleModel journal, String anne) {
        if (anne == null) {
            throw new IllegalArgumentException("Unable to instancie the resolver, the anne is null!");
        }
        if (journal == null) {
            throw new IllegalArgumentException("Unable to instancie the resolver, the journal is null!");
        }
        this.journal = journal;
        annee = anne;
    }

    protected SectionSimpleModel createSection(CompteAnnexeSimpleModel compteAnnexe, String typeSection,
            String categorieSection) throws ComptabiliserLotException {
        SectionSimpleModel sectionRestitution = null;
        String idExterne;

        if (compteAnnexe == null) {
            throw new IllegalArgumentException("Unable to createSection, the compteAnnexe is null!");
        }

        if (JadeStringUtil.isBlankOrZero(typeSection)) {
            throw new IllegalArgumentException("Unable to createSection, the typeSection is null or empty!");
        }

        if (JadeStringUtil.isBlankOrZero(categorieSection)) {
            throw new IllegalArgumentException("Unable to createSection, the categorieSection is null or empty!");
        }

        try {
            idExterne = CABusinessServiceLocator
                    .getSectionService()
                    .creerNumeroSectionUnique(compteAnnexe.getIdRole(), compteAnnexe.getIdExterneRole(), typeSection,
                            annee, categorieSection).getIdExterne();

            sectionRestitution = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                    compteAnnexe.getIdCompteAnnexe(), typeSection, idExterne, journal);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ComptabiliserLotException("Unable to find the section ", e);
        } catch (JadePersistenceException e) {
            throw new ComptabiliserLotException("Unable to find the section ", e);
        } catch (JadeApplicationException e) {
            throw new ComptabiliserLotException("Unable to find the section ", e);
        }

        return sectionRestitution;
    }

    protected APIRubrique findRubrique(String idCodeReferenceRubrique) throws ComptabiliserLotException {
        APIRubrique rubrique;
        APIReferenceRubrique referenceRubrique;
        try {
            referenceRubrique = (APIReferenceRubrique) PRSession.connectSession(
                    BSessionUtil.getSessionFromThreadContext(), "OSIRIS").getAPIFor(APIReferenceRubrique.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Technical exception, error to retrieve the reference rubrique", e);
        }

        rubrique = referenceRubrique.getRubriqueByCodeReference(idCodeReferenceRubrique);
        if (rubrique == null) {
            throw new ComptabiliserLotException("No rubrique was found with this reférenceRubrique: "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(idCodeReferenceRubrique) + " "
                    + idCodeReferenceRubrique);
        }
        return rubrique;
    }

    public APIRubrique resloveRubrique(String idCodeReferenceRubrique) throws ComptabiliserLotException {
        if (JadeStringUtil.isIntegerEmpty(idCodeReferenceRubrique)) {
            throw new IllegalArgumentException("Unable to findRubrique the  idCodeReferenceRubrique is empty!");
        }
        if (!rubriques.containsKey(idCodeReferenceRubrique)) {
            rubriques.put(idCodeReferenceRubrique, findRubrique(idCodeReferenceRubrique));
        }
        return rubriques.get(idCodeReferenceRubrique);

    }

    public SectionSimpleModel resolveSection(Ecriture ecriture) throws ComptabiliserLotException {
        if (ecriture == null) {
            throw new IllegalArgumentException("Unable to resolveSection, the ecriture is null!");
        }
        if (TypeEcriture.DETTE.equals(ecriture.getTypeEcriture())) {
            if ((ecriture.getSectionSimple() == null) && APIEcriture.CREDIT.equals(ecriture.getCodeDebitCredit())) {
                throw new ComptabiliserLotException("The ecriture of type dette must have a specified section, "
                        + ecriture.toString());
            } else if (ecriture.getSectionSimple() != null) {
                return ecriture.getSectionSimple();
            } else {
                return this.resolveSection(ecriture.getSection(), ecriture.getCompteAnnexe());
            }
        } else {
            return this.resolveSection(ecriture.getSection(), ecriture.getCompteAnnexe());
        }
    }

    public SectionSimpleModel resolveSection(SectionPegasus section, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException {

        if (compteAnnexe == null) {
            throw new IllegalArgumentException("Unable to resolveSection section, the  compteAnnexe is null!");
        }

        if (section == null) {
            throw new IllegalArgumentException("Unable to resolveSection section, the section is null!");
        }

        String key = compteAnnexe.getIdCompteAnnexe() + "_" + section.toString();

        if (!cacheSection.containsKey(key)) {
            cacheSection.put(key, createSection(compteAnnexe, section.getType(), section.getCategorie()));
        }
        return cacheSection.get(key);
    }
}
