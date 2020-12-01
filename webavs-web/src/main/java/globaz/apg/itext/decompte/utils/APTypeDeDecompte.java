package globaz.apg.itext.decompte.utils;

import java.util.ArrayList;
import java.util.List;
import globaz.apg.enums.APTypeDePrestation;

public enum APTypeDeDecompte {

    ACM_GE(2, "acm", "AP_DECOMPTE_DETAIL.jasper",
            new APTypeDePrestation[] { APTypeDePrestation.ACM_ALFA, APTypeDePrestation.ACM2_ALFA }),
    AMAT_GE(3, "LAMat", "AP_DECOMPTE_DETAIL.jasper", new APTypeDePrestation[] { APTypeDePrestation.LAMAT }),
    NORMAL(1, "normal", "AP_DECOMPTE_DETAIL.jasper", new APTypeDePrestation[] { APTypeDePrestation.STANDARD }),
    NORMAL_PANDEMIE(8, "Decompte_Pandemie", "AP_DECOMPTE_DETAIL.jasper", new APTypeDePrestation[] { APTypeDePrestation.PANDEMIE }),
    NORMAL_ACM_NE(5, "normal_acmne", "AP_DECOMPTE_DETAIL_ACMNE.jasper",
            new APTypeDePrestation[] { APTypeDePrestation.STANDARD, APTypeDePrestation.ACM_NE }),
    COMPCIAB(6, "normal", "AP_DECOMPTE_DETAIL_CIAB.jasper", new APTypeDePrestation[] { APTypeDePrestation.COMPCIAB }),
    // TODO SCO : MATCIAB1 est regroupé avec le normal et le MATCIAB2 est seul sur un document.
    MATCIAB1(6, "normal", "AP_DECOMPTE_DETAIL_CIAB.jasper", new APTypeDePrestation[] { APTypeDePrestation.MATCIAB1 }),
    MATCIAB2(6, "matciab2", "AP_DECOMPTE_DETAIL_CIAB.jasper", new APTypeDePrestation[] { APTypeDePrestation.MATCIAB2 }),
    JOUR_ISOLE(7, "normal", "AP_DECOMPTE_DETAIL_CIAB.jasper", new APTypeDePrestation[] { APTypeDePrestation.JOUR_ISOLE });

    /**
     * Le but de cette méthode est de déterminer le type de décompte en fonction du(des) type(s) de prestations qu'il
     * contient
     *
     * @param typesDePrestationDecompte
     * @return Le type de décompte en fonction des prestations qu'il contient
     */
    public static APTypeDeDecompte determinerTypeDeDecompte(final List<APTypeDePrestation> typesDePrestationDecompte) {

        // En premier lieu on filtre la liste reçu pour ne pas avoir de doublon dans les types de prestations
        List<APTypeDePrestation> listeFiltrees = new ArrayList<APTypeDePrestation>();
        for (final APTypeDePrestation type : typesDePrestationDecompte) {
            if (!listeFiltrees.contains(type)) {
                listeFiltrees.add(type);
            }
        }
        final APTypeDePrestation[] vals = new APTypeDePrestation[listeFiltrees.size()];
        listeFiltrees.toArray(vals);

        return APTypeDeDecompte.determinerTypeDuDecompte(vals);
    }

    /**
     * @param vals
     */
    private static APTypeDeDecompte determinerTypeDuDecompte(final APTypeDePrestation[] vals) {
        APTypeDeDecompte typeDuDecompte = null;

        if (vals.length != 0) {
            if (vals.length == 1) { // 1 type de prestation trouvé
                switch (vals[0]) {
                    case STANDARD:
                        typeDuDecompte = APTypeDeDecompte.NORMAL;
                        break;
                    case COMPCIAB:
                        typeDuDecompte = APTypeDeDecompte.COMPCIAB;
                        break;
                    case MATCIAB1:
                        typeDuDecompte = APTypeDeDecompte.NORMAL;
                        break;
                    case MATCIAB2:
                        typeDuDecompte = APTypeDeDecompte.MATCIAB2;
                        break;                    case JOUR_ISOLE:
                        typeDuDecompte = APTypeDeDecompte.JOUR_ISOLE;
                        break;
                    case LAMAT:
                        typeDuDecompte = APTypeDeDecompte.AMAT_GE;
                        break;
                    case ACM_ALFA:
                    case ACM2_ALFA: // ACM 2 se comporte de la même manière qu'un décompte ACM 1
                        typeDuDecompte = APTypeDeDecompte.ACM_GE;
                        break;
                    case ACM_NE:
                        typeDuDecompte = APTypeDeDecompte.NORMAL_ACM_NE;
                        break;
                    case PANDEMIE:
                        typeDuDecompte = APTypeDeDecompte.NORMAL_PANDEMIE;
                        break;
                }
            } else if (vals.length == 2) { // 2 types de prestations trouvés. (Due aux regroupement dans
                // APDonneeRegroupementDecompte)
                boolean standard = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.STANDARD, vals);
                boolean acm_ne = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.ACM_NE, vals);
                boolean acm1 = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.ACM_ALFA, vals);
                boolean acm2 = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.ACM2_ALFA, vals);
                boolean jour_isole = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.JOUR_ISOLE, vals);
                boolean complement = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.COMPCIAB, vals);
                boolean matciab1 = APTypeDeDecompte.searchTypeInArray(APTypeDePrestation.MATCIAB1, vals);

                if(matciab1) {
                    typeDuDecompte = APTypeDeDecompte.NORMAL;
                }

                if (standard && acm_ne) {
                    typeDuDecompte = APTypeDeDecompte.NORMAL_ACM_NE;
                }

                if (acm1 && acm2) {
                    typeDuDecompte = APTypeDeDecompte.ACM_GE;
                }

                if (standard && complement) {
                    typeDuDecompte = APTypeDeDecompte.COMPCIAB;
                }

                if (jour_isole && complement) {
                    typeDuDecompte = APTypeDeDecompte.JOUR_ISOLE;
                }
            } else { // Trop de type de prestation, aucun décompte n'en contient plus que 2
                throw new IllegalArgumentException(
                        "Too many APTypeDePrestation founded to resole the APTypeDuDecompte");
            }
        } else { // Aucune type de prestation trouvés, impossible de résoudre le type de décompte
            throw new IllegalArgumentException("Any APTypeDePrestation founded to resole the APTypeDuDecompte");
        }

        if (typeDuDecompte == null) {
            throw new IllegalArgumentException("Unable to resole the APTypeDuDecompte");
        }

        return typeDuDecompte;
    }

    /**
     * Recherche le {@link APTypeDePrestation} 'typeRecherche' dans le tableau
     *
     * @param typeRecherche
     *                          Le type de prestation à rechercher
     * @param t2
     *                          Un tableau contenant des types de prestations
     * @return <code>true</code> si le type recherche est trouvé dans le tableau
     */
    private static boolean searchTypeInArray(final APTypeDePrestation typeRecherche, final APTypeDePrestation[] tab) {
        if (typeRecherche == null) {
            throw new IllegalArgumentException(
                    "APTypeDeDecompte.searchTypeInArray(APTypeDePrestation typeRecherche, final APTypeDePrestation[] tab) : typeRecherche is null");
        }

        for (int index = 0; index < tab.length; index++) {
            if (typeRecherche.equals(tab[index])) {
                return true;
            }
        }
        return false;
    }

    private static boolean typesMatches(final APTypeDePrestation[] t1, final APTypeDePrestation[] t2) {
        // On entre même pas en matière si les tableau sont de taille différentes
        if (t1.length != t2.length) {
            return false;
        }
        // Si t1.length == 0, t2.length sera aussi égal à 0
        if (t1.length == 0) {
            return false;
        }
        // t2.length aussi egal à 0, on test si le type de prestation est le même
        if (t1.length == 1) {
            return t1[0].equals(t2[0]);
        }
        // La taille des 2 tableaux sont > 1
        else {
            // Pour chacun des type de prest de t1 on recherche l'équivalenet dans t2
            for (int index = 0; index < t1.length; index++) {
                final APTypeDePrestation type1 = t1[index];
                // Un type de prestation n'a pas été trouvéa dans le tableau, on sort
                if (!APTypeDeDecompte.searchTypeInArray(type1, t2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Un index pour l'ordonnencement des type de décomptes
     */
    private int index;

    /**
     * Le nom du décompte
     */
    private String nomDocument;

    /**
     * Le nom du modèle à utiliser pour l'impression du détails des décomptes
     */
    private String nomModeleDetailDecompte;

    /**
     * Le(s) type(s) de prestations supportées pas ce type de décomptes
     */
    private APTypeDePrestation[] typesDePrestation;

    private APTypeDeDecompte(final int index, final String nomDocument, String nomModeleDetailDecompte,
                             final APTypeDePrestation[] typesDePrestation) {
        this.index = index;
        this.nomDocument = nomDocument;
        this.nomModeleDetailDecompte = nomModeleDetailDecompte;
        this.typesDePrestation = typesDePrestation;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Retourne le nom du document
     *
     * @return le nom du document
     */
    public String getNomDocument() {
        return nomDocument;
    }

    /**
     * Retourne le nom du modèle à utiliser pour l'impression du détails des décomptes
     *
     * @return le nom du modèle à utiliser pour l'impression du détails des décomptes
     */
    public String getNomModeleDetailDecompte() {
        return nomModeleDetailDecompte;
    }

    /**
     * Retourne le(s) type(s) de prestations supportées pas ce type de décomptes
     *
     * @return le(s) type(s) de prestations supportées pas ce type de décomptes
     */
    public APTypeDePrestation[] getTypesDePrestation() {
        return typesDePrestation;
    }
}
