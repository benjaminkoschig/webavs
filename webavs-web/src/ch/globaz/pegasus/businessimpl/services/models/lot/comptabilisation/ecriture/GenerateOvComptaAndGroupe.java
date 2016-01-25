package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;

public class GenerateOvComptaAndGroupe {
    class Key {
        String idCompteAnnexe;
        String idTiersAdressePaiement;

        public Key(String idTiersAdressePaiement, String idCompteAnnexe) {
            super();
            this.idTiersAdressePaiement = idTiersAdressePaiement;
            this.idCompteAnnexe = idCompteAnnexe;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (idCompteAnnexe == null) {
                if (other.idCompteAnnexe != null) {
                    return false;
                }
            } else if (!idCompteAnnexe.equals(other.idCompteAnnexe)) {
                return false;
            }
            if (idTiersAdressePaiement == null) {
                if (other.idTiersAdressePaiement != null) {
                    return false;
                }
            } else if (!idTiersAdressePaiement.equals(other.idTiersAdressePaiement)) {
                return false;
            }
            return true;
        }

        private GenerateOvComptaAndGroupe getOuterType() {
            return GenerateOvComptaAndGroupe.this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + getOuterType().hashCode();
            result = (prime * result) + ((idCompteAnnexe == null) ? 0 : idCompteAnnexe.hashCode());
            result = (prime * result) + ((idTiersAdressePaiement == null) ? 0 : idTiersAdressePaiement.hashCode());
            return result;
        }

    }

    private Map<Key, OrdreVersementCompta> ordresVersementCompta = new LinkedHashMap<Key, OrdreVersementCompta>();

    public void addOvCompta(CompteAnnexeSimpleModel compteAnnexe, String idAdressePaiement,
            String idDomaineApplication, BigDecimal montant, SectionPegasus section, String idTiers, String csTypeOv,
            String csRoleFamille) {
        // On regroupe le ov par compte annexe et adresse de paiement.
        if (!ordresVersementCompta.containsKey(generateKeyForOv(idAdressePaiement, compteAnnexe))) {
            OrdreVersementCompta ovCompta = new OrdreVersementCompta(compteAnnexe, idAdressePaiement,
                    idDomaineApplication, montant, section, idTiers, csTypeOv, csRoleFamille);
            ordresVersementCompta.put(generateKeyForOv(idAdressePaiement, compteAnnexe), ovCompta);
        } else {
            OrdreVersementCompta ovCompta = ordresVersementCompta
                    .get(generateKeyForOv(idAdressePaiement, compteAnnexe));
            BigDecimal montanNew = ovCompta.getMontant().add(montant);
            ovCompta.setMontant(montanNew);
        }
    }

    private Key generateKeyForOv(String idAdressePaiement, CompteAnnexeSimpleModel compteAnnexe) {
        return new Key(idAdressePaiement, compteAnnexe.getIdCompteAnnexe());
    }

    public List<OrdreVersementCompta> getOvsCompta() {
        List<OrdreVersementCompta> ovs = new ArrayList<OrdreVersementCompta>();
        for (OrdreVersementCompta ov : ordresVersementCompta.values()) {
            ovs.add(ov);
        }
        return ovs;
    }
}
