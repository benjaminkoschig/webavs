package globaz.ij.acor2020.service;

import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@AllArgsConstructor
public class PRMembresFamilleRequerant {

    private final List<ISFMembreFamilleRequerant> membres;

    public List<ISFMembreFamilleRequerant> filtreFamille() {
        return this.filter(membre -> !Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_REQUERANT));
    }

    public List<ISFMembreFamilleRequerant> filtreConjoints() {
        return this.filter(membre -> Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_CONJOINT));
    }

    public List<ISFMembreFamilleRequerant> filtreTousSaufEnfants() {
        return this.filter(membre -> !Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_ENFANT));
    }

    public List<ISFMembreFamilleRequerant> filtreEnfants() {
        return this.filter(membre -> Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_ENFANT));
    }

    public ISFMembreFamilleRequerant filtreByIdTiers(final String idTiers) {
        return filterAndFindFrist(membre -> membre.getIdTiers().equals(idTiers))
                .orElseThrow(() -> new CommonTechnicalException("Membre famille not found with this id " + idTiers));
    }

    public ISFMembreFamilleRequerant filtreRequerant() {
        return filterAndFindFrist(membre -> Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_REQUERANT))
                .orElseThrow(() -> new CommonTechnicalException("Membre famille requerant not found"));
    }

    private List<ISFMembreFamilleRequerant> filter(Predicate<ISFMembreFamilleRequerant> predicate) {
        return membres.stream().filter(predicate).collect(Collectors.toList());
    }

    private Optional<ISFMembreFamilleRequerant> filterAndFindFrist(Predicate<ISFMembreFamilleRequerant> predicate) {
        return membres.stream().filter(predicate).findFirst();
    }
}



