package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author BSC
 * 
 *         Container pour l'ordonnancement des Annonces 50 en vue de l'impression des Listes A et B.
 * 
 *         On veut imprimer les éléments selon l'ordre suivant :
 * 
 *         Liste A Augmentation | +----> 09.2008--> API AI (puis nom prenom) | API AVS (puis nom prenom) | REO AI (puis
 *         nom prenom) | RO AI (puis nom prenom) | REO AVS (puis nom prenom) | RO AVS (puis nom prenom) +---->
 *         08.2008--> ... ...
 * 
 *         Liste A diminution | +----> 09.2008--> API AI (puis nom prenom) | API AVS (puis nom prenom) | REO AI (puis
 *         nom prenom) | RO AI (puis nom prenom) | REO AVS (puis nom prenom) | RO AVS (puis nom prenom) +---->
 *         08.2008--> ... ...
 * 
 *         Liste B Augmentation | +----> 09.2008--> API AI (puis nom prenom) | API AVS (puis nom prenom) | REO AI (puis
 *         nom prenom) | RO AI (puis nom prenom) | REO AVS (puis nom prenom) | RO AVS (puis nom prenom) +---->
 *         08.2008--> ... ...
 * 
 *         Liste B Diminution | +----> 09.2008--> API AI (puis nom prenom) | API AVS (puis nom prenom) | REO AI (puis
 *         nom prenom) | RO AI (puis nom prenom) | REO AVS (puis nom prenom) | RO AVS (puis nom prenom) +---->
 *         08.2008--> ... ...
 * 
 */
public class REAnnonces50Container {

    // /////////////////////////////////////////////////////////////////////////////////////////////
    // Pour la gestion du level et des nom,prenom
    // /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Key sur les level, nom, prenom
     */
    public class KeyLevelNomPrenom {

        private String genrePrestations = "";
        private String nom = "";
        private String nss = "";
        private String prenom = "";

        public KeyLevelNomPrenom(final String genrePrestation, final String nss) {
            super();
            genrePrestations = genrePrestation;
            this.nss = nss;

            // via l'historique des no avs
            try {
                TIHistoriqueAvsManager hAvs = new TIHistoriqueAvsManager();
                hAvs.setSession((BSession) session);
                hAvs.setForNumAvs(NSUtil.formatAVSUnknown(nss));
                hAvs.find();
                PRTiersWrapper ayantDroit1 = null;
                if (hAvs.getFirstEntity() != null) {
                    ayantDroit1 = PRTiersHelper.getTiersParId(session,
                            ((TIHistoriqueAvs) hAvs.getFirstEntity()).getIdTiers());
                    nom = JadeStringUtil.fillWithSpaces(ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_NOM), 40);
                    prenom = JadeStringUtil.fillWithSpaces(ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_PRENOM), 40);
                }
            } catch (Exception e) {
                nom = "";
                prenom = "";
            }
        }

        public String getGenrePrestations() {
            return genrePrestations;
        }

        public String getNom() {
            return nom;
        }

        public String getNss() {
            return nss;
        }

        public String getPrenom() {
            return prenom;
        }
    }

    /**
     * Pour la comparaison sur les mois de rapport
     * 
     * @author BSC
     * 
     */
    public class KeyLevelNomPrenomComparator implements Comparator {

        @Override
        public int compare(final Object o1, final Object o2) {

            if ((o1 instanceof KeyLevelNomPrenom) && (o2 instanceof KeyLevelNomPrenom)) {

                // premier critere le level du genre de prestation
                if (getLevelRente((KeyLevelNomPrenom) o1) == getLevelRente((KeyLevelNomPrenom) o2)) {

                    // deuxieme critere : le nom et le prenom
                    try {
                        String nomPrenom1 = ((KeyLevelNomPrenom) o1).getNom() + ((KeyLevelNomPrenom) o1).getPrenom();
                        String nomPrenom2 = ((KeyLevelNomPrenom) o2).getNom() + ((KeyLevelNomPrenom) o2).getPrenom();

                        return nomPrenom1.compareTo(nomPrenom2);

                    } catch (Exception e) {
                        return -1;
                    }
                } else {
                    return getLevelRente((KeyLevelNomPrenom) o1) - getLevelRente((KeyLevelNomPrenom) o2);
                }

            } else {
                return -1;
            }

        }

        @Override
        public boolean equals(final Object obj) {
            return 0 == compare(this, obj);
        }

        private int getLevelRente(final KeyLevelNomPrenom a) {

            // RO AVS
            if ("10".equals(a.getGenrePrestations()) || "13".equals(a.getGenrePrestations())
                    || "14".equals(a.getGenrePrestations()) || "15".equals(a.getGenrePrestations())
                    || "16".equals(a.getGenrePrestations()) || "33".equals(a.getGenrePrestations())
                    || "34".equals(a.getGenrePrestations()) || "35".equals(a.getGenrePrestations())
                    || "36".equals(a.getGenrePrestations())) {

                return 6;

                // REO AVS
            } else if ("20".equals(a.getGenrePrestations()) || "23".equals(a.getGenrePrestations())
                    || "24".equals(a.getGenrePrestations()) || "25".equals(a.getGenrePrestations())
                    || "26".equals(a.getGenrePrestations()) || "45".equals(a.getGenrePrestations())
                    || "46".equals(a.getGenrePrestations())) {

                return 5;

                // API AVS
            } else if ("85".equals(a.getGenrePrestations()) || "86".equals(a.getGenrePrestations())
                    || "87".equals(a.getGenrePrestations()) || "89".equals(a.getGenrePrestations())
                    || "94".equals(a.getGenrePrestations()) || "95".equals(a.getGenrePrestations())
                    || "96".equals(a.getGenrePrestations()) || "97".equals(a.getGenrePrestations())) {

                return 2;

                // RO AI
            } else if ("50".equals(a.getGenrePrestations()) || "53".equals(a.getGenrePrestations())
                    || "54".equals(a.getGenrePrestations()) || "55".equals(a.getGenrePrestations())
                    || "56".equals(a.getGenrePrestations())) {

                return 4;

                // REO AI
            } else if ("70".equals(a.getGenrePrestations()) || "72".equals(a.getGenrePrestations())
                    || "73".equals(a.getGenrePrestations()) || "74".equals(a.getGenrePrestations())
                    || "75".equals(a.getGenrePrestations()) || "76".equals(a.getGenrePrestations())) {

                return 3;

                // API AI
            } else {
                return 1;
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////
    // Pour la gestion du mois de rapport
    // /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Key sur le mois de rapport
     */
    public class KeyMoisRapport {

        private String moisRapport = "";

        public KeyMoisRapport(final String moisRapport) {
            super();
            this.moisRapport = moisRapport;
        }

        @Override
        public boolean equals(final Object obj) {

            if (obj instanceof KeyMoisRapport) {
                return moisRapport.equals(((KeyMoisRapport) obj).moisRapport);
            } else {
                return false;
            }
        }

        public String getMoisRapport() {
            return moisRapport;
        }
    }

    /**
     * Pour la comparaison des key de mois de rapport
     * 
     * @author BSC
     * 
     */
    public class KeyMoisRapportComparator implements Comparator {

        @Override
        public int compare(final Object o1, final Object o2) {

            if ((o1 instanceof KeyMoisRapport) && (o2 instanceof KeyMoisRapport)) {

                return Integer.parseInt(PRDateFormater.convertDate_MMAA_to_AAAAMM(((KeyMoisRapport) o1)
                        .getMoisRapport()))
                        - Integer.parseInt(PRDateFormater.convertDate_MMAA_to_AAAAMM(((KeyMoisRapport) o2)
                                .getMoisRapport()));
            } else {
                return -1;
            }
        }

        @Override
        public boolean equals(final Object obj) {
            return 0 == compare(this, obj);
        }
    }

    // pour trier les annnonces par level et nom/prenom
    private final Comparator keyLevelNomPrenomComparator = new KeyLevelNomPrenomComparator();
    // pour trier les annonces par mois de rapport
    private final Comparator keyMoisRapportComparator = new KeyMoisRapportComparator();

    private final Map listeAAugmentation = new TreeMap(keyMoisRapportComparator);

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private final Map listeADiminution = new TreeMap(keyMoisRapportComparator);

    private final Map listeBAugmentation = new TreeMap(keyMoisRapportComparator);

    private final Map listeBDiminution = new TreeMap(keyMoisRapportComparator);

    private BISession session = null;

    public REAnnonces50Container(final BISession session) {
        super();
        this.session = session;
    }

    private void addIntoListeAAugmentation(final REAnnonce50VO annonce) {
        KeyMoisRapport keyMoisRappport = new KeyMoisRapport(annonce.getMoisRapport());
        // le mois de rapport n'exite pas
        if (!listeAAugmentation.containsKey(keyMoisRappport)) {
            Map newListeMoisRapport = new TreeMap(keyLevelNomPrenomComparator);
            listeAAugmentation.put(keyMoisRappport, newListeMoisRapport);
        }

        // on ajoute l'annonce dans le mois de rapport
        Map listeMoisRapport = (Map) listeAAugmentation.get(keyMoisRappport);

        KeyLevelNomPrenom keyLevelNomPrenom = new KeyLevelNomPrenom(annonce.getGenrePrestations(),
                annonce.getNssAyantDroit());
        // le level nom prenom n'existe pas
        if (!listeMoisRapport.containsKey(keyLevelNomPrenom)) {
            List l = new ArrayList();
            l.add(annonce);
            listeMoisRapport.put(keyLevelNomPrenom, l);
        } else {
            ((List) listeMoisRapport.get(keyLevelNomPrenom)).add(annonce);
        }
    }

    private void addIntoListeADiminution(final REAnnonce50VO annonce) {
        KeyMoisRapport keyMoisRappport = new KeyMoisRapport(annonce.getMoisRapport());
        // le mois de rapport n'exite pas
        if (!listeADiminution.containsKey(keyMoisRappport)) {
            Map newListeMoisRapport = new TreeMap(keyLevelNomPrenomComparator);
            listeADiminution.put(keyMoisRappport, newListeMoisRapport);
        }

        // on ajoute l'annonce dans le mois de rapport
        Map listeMoisRapport = (Map) listeADiminution.get(keyMoisRappport);

        KeyLevelNomPrenom keyLevelNomPrenom = new KeyLevelNomPrenom(annonce.getGenrePrestations(),
                annonce.getNssAyantDroit());
        // le level nom prenom n'existe pas
        if (!listeMoisRapport.containsKey(keyLevelNomPrenom)) {
            List l = new ArrayList();
            l.add(annonce);
            listeMoisRapport.put(keyLevelNomPrenom, l);
        } else {
            ((List) listeMoisRapport.get(keyLevelNomPrenom)).add(annonce);
        }
    }

    private void addIntoListeBAugmentation(final REAnnonce50VO annonce) {
        KeyMoisRapport keyMoisRappport = new KeyMoisRapport(annonce.getMoisRapport());
        // le mois de rapport n'exite pas
        if (!listeBAugmentation.containsKey(keyMoisRappport)) {
            Map newListeMoisRapport = new TreeMap(keyLevelNomPrenomComparator);
            listeBAugmentation.put(keyMoisRappport, newListeMoisRapport);
        }

        // on ajoute l'annonce dans le mois de rapport
        Map listeMoisRapport = (Map) listeBAugmentation.get(keyMoisRappport);

        KeyLevelNomPrenom keyLevelNomPrenom = new KeyLevelNomPrenom(annonce.getGenrePrestations(),
                annonce.getNssAyantDroit());
        // le level nom prenom n'existe pas
        if (!listeMoisRapport.containsKey(keyLevelNomPrenom)) {
            List l = new ArrayList();
            l.add(annonce);
            listeMoisRapport.put(keyLevelNomPrenom, l);
        } else {
            ((List) listeMoisRapport.get(keyLevelNomPrenom)).add(annonce);
        }
    }

    private void addIntoListeBDiminution(final REAnnonce50VO annonce) {
        KeyMoisRapport keyMoisRappport = new KeyMoisRapport(annonce.getMoisRapport());
        // le mois de rapport n'exite pas
        if (!listeBDiminution.containsKey(keyMoisRappport)) {
            Map newListeMoisRapport = new TreeMap(keyLevelNomPrenomComparator);
            listeBDiminution.put(keyMoisRappport, newListeMoisRapport);
        }

        // on ajoute l'annonce dans le mois de rapport
        Map listeMoisRapport = (Map) listeBDiminution.get(keyMoisRappport);
        KeyLevelNomPrenom keyLevelNomPrenom = new KeyLevelNomPrenom(annonce.getGenrePrestations(),
                annonce.getNssAyantDroit());
        // le level nom prenom n'existe pas
        if (!listeMoisRapport.containsKey(keyLevelNomPrenom)) {
            List l = new ArrayList();
            l.add(annonce);
            listeMoisRapport.put(keyLevelNomPrenom, l);
        } else {
            ((List) listeMoisRapport.get(keyLevelNomPrenom)).add(annonce);
        }
    }

    public Map getListeAAugmentation() {
        return listeAAugmentation;
    }

    public Map getListeADiminution() {
        return listeADiminution;
    }

    public Map getListeBAugmentation() {
        return listeBAugmentation;
    }

    public Map getListeBDiminution() {
        return listeBDiminution;
    }

    /**
     * Insertion des annonces dans la container
     * 
     * @param annonce
     */
    public void insertAnnonce(final REAnnonce50VO annonce) {

        if (annonce.getCodeTraitement().startsWith("0")) {
            // Liste A
            if (isAugmentation(annonce)) {
                addIntoListeAAugmentation(annonce);
            } else {
                addIntoListeADiminution(annonce);
            }
        } else {
            // Liste B
            if (isAugmentation(annonce)) {
                addIntoListeBAugmentation(annonce);
            } else {
                addIntoListeBDiminution(annonce);
            }
        }
    }

    /**
     * True si l'annonce est une annonce d'augmentation False si diminution
     * 
     * @param annonce
     * @return
     */
    private boolean isAugmentation(final REAnnonce50VO annonce) {
        if (JadeStringUtil.isBlank(annonce.getFinDroit())) {
            // Augmentation
            return true;
        } else {
            // Diminution
            return false;
        }
    }
}
