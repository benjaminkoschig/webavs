package ch.globaz.vulpecula.ws.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.models.decompte.TableauContributions;
import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;
import ch.globaz.vulpecula.models.decompte.TableauContributions.TypeContribution;

/**
 * @since WebBMS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tableauContributions")
public class TableauContributionsEbu {
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EntreeContributionEbu implements Comparable<EntreeContributionEbu> {
        private final String taux;
        private final String masse;

        public EntreeContributionEbu() {
            taux = "0.0";
            masse = "0.0";
        }

        public EntreeContributionEbu(Taux taux, Montant masse) {
            this.taux = taux.getValue();
            this.masse = masse.getValue();
        }

        public EntreeContributionEbu(EntreeContribution entree) {
            this(entree.getTaux(), entree.getMasse());
        }

        public String getTaux() {
            return taux;
        }

        public String getMasse() {
            return masse;
        }

        public String getMontant() {
            return Montant.valueOf(masse).multiply(new Taux(taux)).normalize().getValue();
        }

        @Override
        public int compareTo(EntreeContributionEbu o) {
            return o.taux.compareTo(taux);
        }

    }

    private List<EntreeContributionEbu> caisseSociale = null;
    private List<EntreeContributionEbu> avs = null;
    private List<EntreeContributionEbu> ac = null;
    private List<EntreeContributionEbu> ac2 = null;
    private List<EntreeContributionEbu> af = null;

    public TableauContributionsEbu() {
        caisseSociale = new ArrayList<TableauContributionsEbu.EntreeContributionEbu>();
        avs = new ArrayList<TableauContributionsEbu.EntreeContributionEbu>();
        ac = new ArrayList<TableauContributionsEbu.EntreeContributionEbu>();
        ac2 = new ArrayList<TableauContributionsEbu.EntreeContributionEbu>();
        af = new ArrayList<TableauContributionsEbu.EntreeContributionEbu>();
    }

    public TableauContributionsEbu(TableauContributions tableau) {
        this();
        for (Map.Entry<TypeContribution, Collection<EntreeContribution>> entry : tableau.entrySet()) {
            if (entry.getKey().isCaissesSociales()) {
                for (EntreeContribution entree : entry.getValue()) {
                    caisseSociale.add(new EntreeContributionEbu(entree));
                }
            }
            if (entry.getKey().isAvs()) {
                for (EntreeContribution entree : entry.getValue()) {
                    avs.add(new EntreeContributionEbu(entree));
                }
            }
            if (entry.getKey().isAc()) {
                for (EntreeContribution entree : entry.getValue()) {
                    ac.add(new EntreeContributionEbu(entree));
                }
            }
            if (entry.getKey().isAc2()) {
                for (EntreeContribution entree : entry.getValue()) {
                    ac2.add(new EntreeContributionEbu(entree));
                }
            }
            if (entry.getKey().isAf()) {
                for (EntreeContribution entree : entry.getValue()) {
                    af.add(new EntreeContributionEbu(entree));
                }
            }
        }
    }

}
