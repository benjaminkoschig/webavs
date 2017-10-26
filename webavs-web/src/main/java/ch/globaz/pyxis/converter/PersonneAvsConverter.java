package ch.globaz.pyxis.converter;

import globaz.jade.exception.JadePersistenceException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.hera.domaine.relationconjoint.RelationConjoint;
import ch.globaz.hera.domaine.relationconjoint.RelationsConjoints;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.PaysList;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Sexe;

public class PersonneAvsConverter {
    private static final Logger LOG = LoggerFactory.getLogger(PersonneAvsConverter.class);

    private final PaysList paysList;
    private final JadeCodeSystemeService codeSystemeService;
    private RelationsConjoints relationsConjoints;
    private final Map<String, Map<Langues, String>> mapTitre = new ConcurrentHashMap<String, Map<Langues, String>>();

    public PersonneAvsConverter(PaysList paysList, JadeCodeSystemeService codeSystemeService,
            RelationsConjoints relationsConjoints) {
        this.paysList = paysList;
        this.codeSystemeService = codeSystemeService;
        this.relationsConjoints = relationsConjoints;
    }

    public PersonneAvsConverter(PaysList paysList, JadeCodeSystemeService codeSystemeService) {
        this.paysList = paysList;
        this.codeSystemeService = codeSystemeService;
        relationsConjoints = null;
    }

    public void setRelationsConjoints(RelationsConjoints relationsConjoints) {
        this.relationsConjoints = relationsConjoints;
    }

    public PersonneAVS convertToDomain(PersonneEtendueComplexModel personneEtendue) {
        return convertToDomain(personneEtendue.getTiers(), personneEtendue.getPersonne(), personneEtendue
                .getPersonneEtendue().getNumAvsActuel(), null);
    }

    public PersonneAVS convertToDomain(TiersSimpleModel simpleTiers, PersonneSimpleModel simplePersonne, String nss,
            Date date) {
        PersonneAVS personne = new PersonneAVS();

        personne.setDateDeces(simplePersonne.getDateDeces());
        personne.setDateNaissance(simplePersonne.getDateNaissance());
        personne.setSexe(Sexe.parseAllowEmpyOrZeroValue(simplePersonne.getSexe()));

        personne.setId(Long.valueOf(simpleTiers.getIdTiers()));
        personne.setNom(simpleTiers.getDesignation1());
        personne.setPrenom(simpleTiers.getDesignation2());
        personne.setPays(paysList.resolveById(simpleTiers.getIdPays()));
        personne.setTitreParLangue(convertTitre(simpleTiers.getTitreTiers()));

        if (relationsConjoints != null) {
            RelationsConjoints relations = relationsConjoints.filtreForRequerant(simpleTiers.getIdTiers());
            if (!relations.isEmpty()) {
                RelationConjoint relation = relations.filtreByPeriode(date).resolveMostRecent();
                if (relation.isEmpty()) {
                    LOG.error("Unable to resolve the civil statuts: {}", personne);
                } else {
                    EtatCivil etatCivil = relation.resolveEtatCivils(date);
                    personne.setEtatCivil(etatCivil);
                }
            }
        }

        try {
            if (nss != null && !nss.trim().isEmpty()) {
                personne.setNss(new NumeroSecuriteSociale(nss));
            } else {
                personne.setNss(new NumeroSecuriteSociale());
            }
        } catch (IllegalArgumentException e) {
            LOG.warn(e.getMessage());
            personne.setNss(new NumeroSecuriteSociale());
        }
        return personne;
    }

    public void loadTitre(Set<String> csTitres) {
        for (String csTitre : csTitres) {
            convertTitre(csTitre);
        }
    }

    private Map<Langues, String> convertTitre(String csTitre) {
        if (mapTitre.containsKey(csTitre)) {
            return mapTitre.get(csTitre);
        }

        try {
            Map<Langues, String> titreTiers = new EnumMap<Langues, String>(Langues.class);
            if (csTitre != null && !"0".equals(csTitre) && !csTitre.trim().isEmpty()) {
                JadeCodeSysteme csTitreTiers = codeSystemeService.getCodeSysteme(csTitre);
                if (csTitreTiers != null) {
                    for (Langues uneLangue : Langues.values()) {
                        if (csTitreTiers.getTraduction(uneLangue) != null) {
                            titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                        }
                    }
                }
                mapTitre.put(csTitre, titreTiers);
            }
            return titreTiers;
        } catch (JadePersistenceException e) {
            throw new RuntimeException("Impossible de convertir le titre(" + csTitre + ") ");
        }
    }
}
