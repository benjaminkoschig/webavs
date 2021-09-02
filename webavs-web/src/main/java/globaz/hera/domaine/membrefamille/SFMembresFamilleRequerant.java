package globaz.hera.domaine.membrefamille;

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
public class SFMembresFamilleRequerant {

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
        return filterAndFindFirst(membre -> membre.getIdTiers().equals(idTiers))
                .orElseThrow(() -> new CommonTechnicalException("Membre famille not found with this id " + idTiers));
    }

    public ISFMembreFamilleRequerant filtreRequerant() {
        return filterAndFindFirst(membre -> Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_REQUERANT))
                .orElseThrow(() -> new CommonTechnicalException("Membre famille requerant not found"));
    }

    private List<ISFMembreFamilleRequerant> filter(Predicate<ISFMembreFamilleRequerant> predicate) {
        return membres.stream().filter(predicate).collect(Collectors.toList());
    }

    private Optional<ISFMembreFamilleRequerant> filterAndFindFirst(Predicate<ISFMembreFamilleRequerant> predicate) {
        return membres.stream().filter(predicate).findFirst();
    }
}



