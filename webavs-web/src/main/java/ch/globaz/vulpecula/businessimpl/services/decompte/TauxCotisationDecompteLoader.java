package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.cotisation.AFCotisation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.services.CotisationService;

/**
 * Classe effectuant la mise en cache des taux d'assurances afin de ne pas surcharger la base de données.
 * Implémenté sous forme de Map, la clé correspond au triplet "idEmployeur", "idAssurance", "date".
 * L'employeur entre ici en compte car certains taux peuvent être forcés sur certaines cotisations.
 * 
 */
public class TauxCotisationDecompteLoader {
    private CotisationService cotisationService = VulpeculaServiceLocator.getCotisationService();

    private static final class Key {
        private final String idEmployeur;
        private final String idAssurance;
        private final Date date;

        public Key(String idEmployeur, String idAssurance, Date date) {
            this.idEmployeur = idEmployeur;
            this.idAssurance = idAssurance;
            this.date = date;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((idAssurance == null) ? 0 : idAssurance.hashCode());
            result = prime * result + ((idEmployeur == null) ? 0 : idEmployeur.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (date == null) {
                if (other.date != null) {
                    return false;
                }
            } else if (!date.equals(other.date)) {
                return false;
            }
            if (idAssurance == null) {
                if (other.idAssurance != null) {
                    return false;
                }
            } else if (!idAssurance.equals(other.idAssurance)) {
                return false;
            }
            if (idEmployeur == null) {
                if (other.idEmployeur != null) {
                    return false;
                }
            } else if (!idEmployeur.equals(other.idEmployeur)) {
                return false;
            }
            return true;
        }
    }

    private Map<Key, Taux> cacheTaux = new ConcurrentHashMap<Key, Taux>();

    // Ne gère pas la synchronisation entre les différents thread. Il se peut que la cotisation soit recherchée
    // plusieurs fois.
    // Mais cela ne devrait pas poser de problèmes
    public Taux getOrLoadTauxAssurance(DecompteSalaire decompteSalaire, AdhesionCotisationPosteTravail cotisation) {
        Date date = decompteSalaire.getDateCalculTaux();
        return getOrLoadTauxAssurance(decompteSalaire.getIdEmployeur(), cotisation.getCotisation(), date);
    }

    public Taux getOrLoadTauxAssurance(String idEmployeur, Cotisation cotisation, Date date) {
        Key key = new Key(idEmployeur, cotisation.getAssuranceId(), date);
        if (!cacheTaux.containsKey(key)) {
            AFCotisation afCotisation = cotisationService.findAFCotisation(cotisation.getId(), date);
            String taux = afCotisation.getTaux(JadeDateUtil.getDMYDate(new java.util.Date(date.getTime())), null);
            if (!JadeStringUtil.isEmpty(taux)) {
                cacheTaux.put(key, new Taux(taux));
            }
        }
        return cacheTaux.get(key);
    }
}
