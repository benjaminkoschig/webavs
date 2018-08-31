package ch.globaz.vulpecula.documents.listesinternes;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

class TaxationOfficeContainer {
    static class Key {
        private final String code;
        private final String idExterne;
        private final Date date;

        public Key(String code, String idExterne, Date date) {
            this.code = code;
            this.idExterne = idExterne;
            this.date = date.getFirstDayOfMonth();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((code == null) ? 0 : code.hashCode());
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((idExterne == null) ? 0 : idExterne.hashCode());
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
            if (code == null) {
                if (other.code != null) {
                    return false;
                }
            } else if (!code.equals(other.code)) {
                return false;
            }
            if (date == null) {
                if (other.date != null) {
                    return false;
                }
            } else if (!date.equals(other.date)) {
                return false;
            }
            if (idExterne == null) {
                if (other.idExterne != null) {
                    return false;
                }
            } else if (!idExterne.equals(other.idExterne)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Key [code=" + code + ", idExterne=" + idExterne + ", date=" + date + "]";
        }
    }

    static class MontantTO {
        private final Montant montant;
        private final Montant masse;

        public MontantTO(Montant montant, Montant masse) {
            this.montant = montant;
            this.masse = masse;
        }

        @Override
        public String toString() {
            return "MontantTO [montant=" + montant + ", masse=" + masse + "]";
        }
    }

    private Map<Key, MontantTO> map = new HashMap<Key, MontantTO>();

    public TaxationOfficeContainer(List<TaxationOfficeDTO> tos) {
        for (TaxationOfficeDTO to : tos) {
            Key key = new Key(to.getCode(), to.getIdExterne(), new Date(to.getDate()));
            if (!map.containsKey(key)) {
                map.put(key, new MontantTO(Montant.ZERO, Montant.ZERO));
            }
            MontantTO montantTO = map.get(key);
            Montant nouvelleMasse = montantTO.masse.add(new Montant(to.getMasse()));
            Montant nouveauMontant = montantTO.montant.add(new Montant(to.getMontant()));
            map.put(key, new MontantTO(nouveauMontant, nouvelleMasse));
        }
    }

    public Montant getMasse(Convention convention, String idExterne, Date date) {
        return getMasse(convention.getCode(), idExterne, date);
    }

    Montant getMasse(String code, String idExterne, Date date) {
        MontantTO montantTO = map.get(new Key(code, idExterne, date));
        if (montantTO == null) {
            return Montant.ZERO;
        }
        return montantTO.masse;
    }

    Montant getMontant(String code, String idExterne, Date date) {
        MontantTO montantTO = map.get(new Key(code, idExterne, date));
        if (montantTO == null) {
            return Montant.ZERO;
        }
        return montantTO.montant;
    }

    public Montant getMontant(Convention key, String idExterne) {
        Montant result = Montant.ZERO;

        for (Map.Entry<Key, MontantTO> s : map.entrySet()) {
            if (idExterne.equals(s.getKey().idExterne) && key.getCode().equals(s.getKey().code)) {
                result = result.add(s.getValue().montant);
            }
        }

        return result;
    }

    public Montant getMontant(Collection<String> idsExterne) {
        Montant result = Montant.ZERO;

        for (Map.Entry<Key, MontantTO> s : map.entrySet()) {
            if (idsExterne.contains(s.getKey().idExterne)) {
                result = result.add(s.getValue().montant);
            }
        }

        return result;
    }

    public Montant getMasse(Convention key, String idExterne) {
        Montant result = Montant.ZERO;

        for (Map.Entry<Key, MontantTO> s : map.entrySet()) {
            if (idExterne.equals(s.getKey().idExterne) && key.getCode().equals(s.getKey().code)) {
                result = result.add(s.getValue().masse);
            }
        }

        return result;
    }

    public Montant getMasse(Collection<String> idsExternes) {
        Montant result = Montant.ZERO;

        for (Map.Entry<Key, MontantTO> s : map.entrySet()) {
            if (idsExternes.contains(s.getKey().idExterne)) {
                result = result.add(s.getValue().masse);
            }
        }

        return result;
    }
}
