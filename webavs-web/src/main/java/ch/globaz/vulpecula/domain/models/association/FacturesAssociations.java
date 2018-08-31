package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.domain.models.common.Annee;

public class FacturesAssociations implements Iterable<FactureAssociation> {
    static final class Key implements Comparable<Key> {
        private final String idAssociationParent;
        private final Annee annee;
        private final String idEmployeur;

        public Key(String idAssociationParent, Annee annee, String idEmployeur) {
            this.idAssociationParent = idAssociationParent;
            this.annee = annee;
            this.idEmployeur = idEmployeur;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((annee == null) ? 0 : annee.hashCode());
            result = prime * result + ((idAssociationParent == null) ? 0 : idAssociationParent.hashCode());
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
            if (annee == null) {
                if (other.annee != null) {
                    return false;
                }
            } else if (!annee.equals(other.annee)) {
                return false;
            }
            if (idAssociationParent == null) {
                if (other.idAssociationParent != null) {
                    return false;
                }
            } else if (!idAssociationParent.equals(other.idAssociationParent)) {
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

        @Override
        public int compareTo(Key key) {
            int comparedAnnee = annee.compareTo(key.annee);
            if (comparedAnnee != 0) {
                return -comparedAnnee;
            }

            int comparedAssociation = idAssociationParent.compareTo(key.idAssociationParent);
            if (comparedAssociation != 0) {
                return -comparedAssociation;
            }

            int comparedEmployeur = idEmployeur.compareTo(key.idEmployeur);
            if (comparedEmployeur != 0) {
                return -comparedEmployeur;
            }

            return comparedEmployeur;
        }
    }

    private Map<Key, FactureAssociation> factures = new TreeMap<Key, FactureAssociation>();

    public void addLignes(List<LigneFactureAssociation> lignes) {
        for (LigneFactureAssociation ligne : lignes) {
            addLigne(ligne);
        }
    }

    public void addLigne(LigneFactureAssociation ligne) {
        FactureAssociation facture = null;
        Key key = new Key(ligne.getIdAssociationProfessionnelleParent(), ligne.getAnnee(), ligne.getEnteteFacture()
                .getIdEmployeur());
        if (factures.containsKey(key)) {
            FactureAssociation factureTemp = factures.get(key);
            // Si c'est pas en erreur, on ajoute l'entête (facture)

            if (!factureTemp.getEtat().equals(EtatFactureAP.EN_ERREUR)) {
                facture = factureTemp;
            }

        } else {
            if (!ligne.getEtat().equals(EtatFactureAP.EN_ERREUR)) {
                facture = new FactureAssociation();
                facture.setId(ligne.getIdEnteteFacture());
                facture.setAnnee(ligne.getAnnee());
                facture.setAssociationParent(ligne.getAssociationProfessionnelleParent());
                facture.setModele(ligne.getModeleEntete());
                facture.setPassageFacturation(ligne.getPassageFacturation());
                facture.setEtat(ligne.getEtat());
                facture.setEmployeur(ligne.getEnteteFacture().getEmployeur());
                factures.put(key, facture);
            }
        }
        if (facture != null) {
            facture.addLigne(ligne);
        }
    }

    FactureAssociation getAt(Key key) {
        return factures.get(key);
    }

    public List<FactureAssociation> getFactures() {
        return new ArrayList<FactureAssociation>(factures.values());
    }

    @Override
    public Iterator<FactureAssociation> iterator() {
        return factures.values().iterator();
    }

    public boolean containsFactures() {
        return !factures.isEmpty();
    }
}
