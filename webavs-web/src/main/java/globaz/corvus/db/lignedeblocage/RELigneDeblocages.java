/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Montant;

/**
 * Structure représentant une liste de ligne de déblocage
 * 
 * @author sco
 */
public class RELigneDeblocages extends ArrayList<RELigneDeblocage> {

    private static final long serialVersionUID = 1L;

    RELigneDeblocages getLigneDeblocageByEtat(RELigneDeblocageEtat byEtat) {

        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (l.getEtat().equals(byEtat)) {
                ld.add(l);
            }
        }

        return ld;
    }

    public RELigneDeblocages filtreComptabilises() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.COMPTABILISE);
    }

    public RELigneDeblocages filtreEnregistres() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.ENREGISTRE);
    }

    public RELigneDeblocages filtreEnregistresAndNone() {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        deblocages.addAll(getLigneDeblocageByEtat(RELigneDeblocageEtat.NONE));
        deblocages.addAll(getLigneDeblocageByEtat(RELigneDeblocageEtat.ENREGISTRE));
        return deblocages;
    }

    public RELigneDeblocages filtreEnregistresAndValides() {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        deblocages.addAll(getLigneDeblocageByEtat(RELigneDeblocageEtat.VALIDE));
        deblocages.addAll(getLigneDeblocageByEtat(RELigneDeblocageEtat.ENREGISTRE));
        return deblocages;
    }

    public RELigneDeblocages filtreValides() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.VALIDE);
    }

    public RELigneDeblocages filtreValidesAndComptabilises() {
        RELigneDeblocages valides = filtreValides();
        RELigneDeblocages comptabilise = filtreComptabilises();
        RELigneDeblocages lignes = new RELigneDeblocages();
        lignes.addAll(valides.toList());
        lignes.addAll(comptabilise.toList());
        return lignes;
    }

    public List<RELigneDeblocageCreancier> toListCreancier() {
        return toListType();
    }

    public List<RELigneDeblocageVersement> toListVersement() {
        return toListType();
    }

    public List<RELigneDeblocageDette> toListDette() {
        List<RELigneDeblocageDette> dettes = toListType();
        Collections.sort(dettes, new Comparator<RELigneDeblocageDette>() {
            @Override
            public int compare(RELigneDeblocageDette o1, RELigneDeblocageDette o2) {
                return o1.getIdExterne().compareTo(o2.getIdExterne());
            }
        });
        return dettes;
    }

    public List<RELigneDeblocage> toList() {
        List<RELigneDeblocage> list = new ArrayList<RELigneDeblocage>();
        for (RELigneDeblocage ligne : this) {
            list.add(ligne);
        }
        return list;
    }

    private <T extends RELigneDeblocage> List<T> toListType() {
        List<T> list = new ArrayList<T>();
        for (RELigneDeblocage ligne : this) {
            list.add((T) ligne);
        }
        return list;
    }

    public RELigneDeblocages getLigneDeblocageCreancier() {
        return getLigneDeblocageByType(RELigneDeblocageType.CREANCIER);
    }

    public RELigneDeblocages getLigneDeblocageDetteEnCompta() {
        return getLigneDeblocageByType(RELigneDeblocageType.DETTE_EN_COMPTA);
    }

    public RELigneDeblocages getLigneDeblocageImpotsSource() {
        return getLigneDeblocageByType(RELigneDeblocageType.IMPOTS_SOURCE);
    }

    public RELigneDeblocages getLigneDeblocageVersementBeneficiaire() {
        return getLigneDeblocageByType(RELigneDeblocageType.VERSEMENT_BENEFICIAIRE);
    }

    public RELigneDeblocages changeEtatToValide() {
        return changeEtat(RELigneDeblocageEtat.VALIDE);
    }

    public RELigneDeblocages changeEtatToComptabilise() {
        return changeEtat(RELigneDeblocageEtat.COMPTABILISE);
    }

    public RELigneDeblocages changeEtatToEnregistre() {
        return changeEtat(RELigneDeblocageEtat.ENREGISTRE);
    }

    public RELigneDeblocages changeIdLot(Long idLot) {
        for (RELigneDeblocage ligne : this) {
            ligne.setIdLot(idLot);
        }
        return this;
    }

    public RELigneDeblocages changeSection(Long idSection) {
        for (RELigneDeblocage ligne : this) {
            ligne.setIdSectionCompensee(idSection);
        }
        return this;
    }

    public RELigneDeblocages changeIdTiersAdressePaiementAndApplication(Long idTiers, Long idApplication,
            String idCompteAnnexe) {
        for (RELigneDeblocage ligne : this) {
            ligne.setIdTiersAdressePaiement(idTiers);
            ligne.setIdApplicationAdressePaiement(idApplication);
            ligne.setIdCompteAnnexe(idCompteAnnexe);
        }
        return this;
    }

    public Montant sumMontants() {
        Montant sum = Montant.ZERO;
        for (RELigneDeblocage ligne : this) {
            sum = sum.add(ligne.getMontant());
        }
        return sum;
    }

    public RELigneDeblocages filtreByIdLot(Long idLot) {
        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (idLot.equals(l.getIdLot())) {
                ld.add(l);
            }
        }
        return ld;
    }

    public Set<Long> getIdsLigne() {
        Set<Long> ids = new HashSet<Long>();
        for (RELigneDeblocage ligne : this) {
            ids.add(Long.valueOf(ligne.getIdEntity()));
        }
        return ids;
    }

    RELigneDeblocages getLigneDeblocageByType(RELigneDeblocageType byType) {
        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (l.getType().equals(byType)) {
                ld.add(l);
            }
        }
        return ld;
    }

    private RELigneDeblocages changeEtat(RELigneDeblocageEtat etat) {
        for (RELigneDeblocage ligne : this) {
            ligne.setEtat(etat);
        }
        return this;
    }

    public Boolean isLiberable() {
        for (RELigneDeblocage ligne : this) {
            if (ligne.getEtat().isValide()) {
                return false;
            }
        }
        return true;
    }

    public Boolean isDevalidable() {
        return !isLiberable();
    }

    public RELigneDeblocages distinct() {
        Map<String, RELigneDeblocage> mapDeblocages = new HashMap<String, RELigneDeblocage>();
        for (RELigneDeblocage deblocage : this) {
            mapDeblocages.put(deblocage.getId(), deblocage);
        }
        RELigneDeblocages deblocages = new RELigneDeblocages();
        deblocages.addAll(mapDeblocages.values());
        return deblocages;
    }

}
