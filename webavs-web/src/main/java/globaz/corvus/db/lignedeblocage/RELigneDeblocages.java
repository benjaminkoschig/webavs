/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import java.util.ArrayList;
import java.util.List;

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

    RELigneDeblocages getLigneDeblocageByType(RELigneDeblocageType byType) {

        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (l.getType().equals(byType)) {
                ld.add(l);
            }
        }

        return ld;
    }

    public List<RELigneDeblocage> toList() {
        List<RELigneDeblocage> list = new ArrayList<RELigneDeblocage>();
        for (RELigneDeblocage ligne : this) {
            list.add(ligne);
        }
        return list;
    }

    public List<RELigneDeblocageCreancier> toListCreancier() {
        return toListType();
    }

    public List<RELigneDeblocageVersement> toListVersement() {
        return toListType();
    }

    public List<RELigneDeblocageDette> toListDette() {
        return toListType();
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

    public RELigneDeblocages getLigneDeblocageVersementBeneficaire() {
        return getLigneDeblocageByType(RELigneDeblocageType.VERSEMENT_BENEFICIAIRE);
    }
}
