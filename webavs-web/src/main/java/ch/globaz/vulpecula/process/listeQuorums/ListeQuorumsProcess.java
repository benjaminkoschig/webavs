package ch.globaz.vulpecula.process.listeQuorums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.BProcessWithContext;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

/**
 * Process permettant de générer un fichier Excel contenant les quorums ainsi que les détails
 *
 * @author Sébastien Quiquerez
 *
 */
public class ListeQuorumsProcess extends BProcessWithContext {
    private static final long serialVersionUID = 377241909415609987L;
    private Periode periode;
    private String codeConvention;
    private Boolean detail;
    private ListeQuorumsExcel excelDoc;

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTE_QUORUMS_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        init();

        return true;
    }

    private void init() throws Exception {
        // Initialisatin des maps
        Map<String, List<LigneQuorums>> mapQuorumsParConventionMembres = new HashMap<String, List<LigneQuorums>>();
        Map<String, List<LigneQuorums>> mapQuorumsParConventionNonMembres = new HashMap<String, List<LigneQuorums>>();
        Map<String, List<LigneSansPersonnel>> mapQuorumsParConventionEntrepriseMembresSansPersonnel = new HashMap<String, List<LigneSansPersonnel>>();
        Map<String, List<LigneSansPersonnel>> mapQuorumsParConventionEntrepriseNonMembresSansPersonnel = new HashMap<String, List<LigneSansPersonnel>>();

        // Génération des listes et reprise des données de la DB
        List<LigneQuorums> lignesMembers = QueryExecutor.execute(sqlTravailleurs("in"), LigneQuorums.class,
                getSession());
        List<LigneQuorums> lignesNoMembers = QueryExecutor.execute(sqlTravailleurs("not in"), LigneQuorums.class,
                getSession());
        List<LigneSansPersonnel> lignesNoPersonnelMembres = QueryExecutor.execute(sqlSansPersonnel("in"),
                LigneSansPersonnel.class, getSession());
        List<LigneSansPersonnel> lignesNoPersonnelNonMembres = QueryExecutor.execute(sqlSansPersonnel("not in"),
                LigneSansPersonnel.class, getSession());
        List<LigneQuorums> generalForSyndiques = new ArrayList<LigneQuorums>();
        // Fusion des travailleurs membres et non membres (Pour les syndiqués)
        generalForSyndiques.addAll(lignesMembers);
        generalForSyndiques.addAll(lignesNoMembers);

        // Transformation des listes en map groupée par code de convention
        putInMapByConvention(mapQuorumsParConventionMembres, lignesMembers);
        putInMapByConvention(mapQuorumsParConventionNonMembres, lignesNoMembers);
        putInMapByConventionSansPersonnel(mapQuorumsParConventionEntrepriseMembresSansPersonnel,
                lignesNoPersonnelMembres);
        putInMapByConventionSansPersonnel(mapQuorumsParConventionEntrepriseNonMembresSansPersonnel,
                lignesNoPersonnelNonMembres);

        // Séparation de la liste en une liste de syndiqués et une liste de non syndiqués.
        Map<String, Map<String, List<LigneQuorums>>> mapRetour = listeParConventionSyndique(generalForSyndiques);
        Map<String, List<LigneQuorums>> mapSyndiques = mapRetour.get("syndiques");
        Map<String, List<LigneQuorums>> mapNonSyndiques = mapRetour.get("nonSyndiques");

        Map<String, int[][]> travailleurs = new HashMap<String, int[][]>();
        Map<String, int[][]> entreprises = new HashMap<String, int[][]>();
        Map<String, int[][]> entreprisesSansPersonnel = new HashMap<String, int[][]>();
        Map<String, int[][]> syndiques = new HashMap<String, int[][]>();
        Map<String, Map<String, int[][]>> mapToPrint = new HashMap<String, Map<String, int[][]>>();
        mapToPrint.put("entreprises", entreprises);
        mapToPrint.put("entreprisesSansPersonnel", entreprisesSansPersonnel);
        mapToPrint.put("travailleurs", travailleurs);
        mapToPrint.put("syndiques", syndiques);

        /*
         * Pour chaque convention, consolidation (dans des tableaux à deux dimensions se trouvant dans des maps) des
         * données extraites des listes ou des map, en trois parties
         * (Entreprises, Travailleurs et Syndiqués)
         */

        for (String convention : mapQuorumsParConventionMembres.keySet()) {
            // Tri des map pour ressortir les listes des traveilleurs membres et non membres pour toutes les conventions
            Collection<LigneQuorums> frM = listeParLangue("FR", convention, mapQuorumsParConventionMembres);
            Collection<LigneQuorums> deM = listeParLangue("DE", convention, mapQuorumsParConventionMembres);
            Collection<LigneQuorums> frNM = listeParLangue("FR", convention, mapQuorumsParConventionNonMembres);
            Collection<LigneQuorums> deNM = listeParLangue("DE", convention, mapQuorumsParConventionNonMembres);
            int spfrM = nombreParLangueSansPersonnel("FR", convention,
                    mapQuorumsParConventionEntrepriseMembresSansPersonnel);
            int spfrNM = nombreParLangueSansPersonnel("FR", convention,
                    mapQuorumsParConventionEntrepriseNonMembresSansPersonnel);
            int spdeM = nombreParLangueSansPersonnel("DE", convention,
                    mapQuorumsParConventionEntrepriseMembresSansPersonnel);
            int spdeNM = nombreParLangueSansPersonnel("DE", convention,
                    mapQuorumsParConventionEntrepriseNonMembresSansPersonnel);

            // map regroupant les listes de travailleurs M et NM en Fr et De groupées par convention
            int tabTravailleurs[][] = { { frM.size(), frNM.size() }, { deM.size(), deNM.size() } };

            travailleurs.put(convention, tabTravailleurs);

            // map regroupant les listes d'entreprises M et NM en Fr et De groupées par convention (calcul des nombres
            // pour les entreprises)
            int tabEntreprises[][] = { { listeParLangueEtAffilie(frM).size(), listeParLangueEtAffilie(frNM).size() },
                    { listeParLangueEtAffilie(deM).size(), listeParLangueEtAffilie(deNM).size() } };

            entreprises.put(convention, tabEntreprises);

            // map regroupant les listes d'entreprises SANS PERSONNEL M et NM en Fr et De groupées par convention
            // (calcul des nombres
            // pour les entreprises)
            int tabEntrepriseSansPersonnel[][] = { { spfrM, spfrNM }, { spdeM, spdeNM } };

            entreprisesSansPersonnel.put(convention, tabEntrepriseSansPersonnel);

            // map regroupant les listes de syndiqués ou non groupé par convention
            int tabSyndiques[][] = { { mapSyndiques.get(convention).size() },
                    { mapNonSyndiques.get(convention).size() } };

            syndiques.put(convention, tabSyndiques);
        }

        Map<String, List<LigneQuorums>> mapExel = new HashMap<String, List<LigneQuorums>>();
        mapExel.put("Membres", lignesMembers);
        mapExel.put("Non membres", lignesNoMembers);

        writeFile(mapExel, mapToPrint);
        print();
    }

    private String sqlTravailleurs(String membre) {
        StringBuilder sql = new StringBuilder();

        sql.append(
                "select malnaf as noAffilie, avsp.hxnavs as NSS, hbcadm as codeConvention, fw.pcouid as qualification, fw2.pcouid as langue, tiers.HTLDE1 as syndicat ");
        sql.append("from SCHEMA.afaffip af ");
        sql.append(
                "join SCHEMA.PT_POSTES_TRAVAILS poste on poste.ID_AFAFFIP =af.maiaff and poste.DATE_FIN_ACTIVITE = 0 ");
        sql.append("join SCHEMA.PT_TRAVAILLEURS tra on tra.ID = poste.ID_PT_TRAVAILLEURS ");
        sql.append("join SCHEMA.tipavsp avsp on avsp.htitie = tra.ID_TITIERP ");
        sql.append("join SCHEMA.PT_ASSOCIATION_COTISATION ass on ass.ID_PT_EMPLOYEURS = af.maiaff ");
        sql.append("join SCHEMA.titierp ti on ti.htitie = af.htitie ");
        sql.append("join SCHEMA.fwcoup fw on fw.pcosid = poste.CS_QUALIFICATION and fw.PLAIDE = 'F' ");
        sql.append(
                "join SCHEMA.PT_CONV_QUALIFICATIONS qual on af.MACONV = qual.ID_TIADMIP_CONVENTION and poste.CS_QUALIFICATION = qual.CS_QUALIFICATION ");
        sql.append("join SCHEMA.fwcoup fw2 on fw2.pcosid = HTTLAN and fw2.PLAIDE = 'F' ");
        sql.append("join SCHEMA.tiadmip adm on adm.htitie = af.maconv ");
        sql.append("LEFT outer join SCHEMA.PT_SYNDICATS syn on (tra.ID = syn.ID_PT_TRAVAILLEURS and syn.DATE_DEBUT<=");
        sql.append(periode.getDateFin().getValue());
        sql.append(" and (syn.DATE_FIN>=");
        sql.append(periode.getDateDebut().getValue());
        sql.append(" or syn.DATE_FIN=0)) ");
        sql.append("left outer join SCHEMA.TITIERP tiers on (syn.ID_TIADMIP=tiers.HTITIE) ");
        sql.append("where (maddeb <> madfin) and  (maddeb<= ");
        sql.append(periode.getDateFin().getValue());
        sql.append(" and (madfin>=");
        sql.append(periode.getDateDebut().getValue());
        sql.append(" or madfin=0)) ");
        if (!JadeStringUtil.isBlank(codeConvention)) {
            sql.append("and hbcadm = ");
            sql.append(codeConvention);
        }
        sql.append(" and qual.CS_OUVRIER_EMPLOYEUR <> 68010002");
        sql.append(" and maiaff ");
        sql.append(membre);
        sql.append(" (select maiaff from SCHEMA.afaffip af ");
        sql.append("join SCHEMA.PT_ASSOCIATION_COTISATION ass on ass.ID_PT_EMPLOYEURS = af.maiaff ");
        sql.append(
                "join SCHEMA.PT_COTISATIONS_AP cot on cot.ID =ass.ID_PT_COTISATIONS_AP and UPPER(cot.LIBELLE_UPPER) like '%CANTONAL%' ");
        sql.append(")");
        sql.append("group by malnaf, avsp.hxnavs, hbcadm, fw.pcouid, fw2.pcouid, tiers.HTLDE1 ");
        sql.append("ORDER by hbcadm, malnaf ");

        return sql.toString();
    }

    private String sqlSansPersonnel(String membre) {
        StringBuilder sql = new StringBuilder();

        sql.append(
                "select adm.HBCADM as convention, langue.PCOUID as langue, count(*) as somme from SCHEMA.afaffip aff ");
        sql.append("inner join SCHEMA.tiadmip adm on aff.MACONV=adm.htitie ");
        sql.append("inner join SCHEMA.titierp tiers on tiers.htitie=aff.htitie ");
        sql.append("inner join SCHEMA.AFPARTP part on aff.maiaff=part.maiaff and part.MFTPAR=818011 ");
        sql.append("left join SCHEMA.fwcoup langue on langue.pcosid = tiers.HTTLAN and plaide='F' ");
        sql.append("where (maddeb<>madfin) and aff.maddeb<=");
        sql.append(periode.getDateFin().getValue());
        sql.append(" and (aff.madfin=0 or aff.madfin>=");
        sql.append(periode.getDateDebut().getValue());
        sql.append(") and (part.mfdfin=0 and part.mfddeb <= ");
        sql.append(periode.getDateFin().getValue());
        sql.append(") ");
        if (!JadeStringUtil.isBlank(codeConvention)) {
            sql.append("and hbcadm = ");
            sql.append(codeConvention);
        }
        sql.append(" and aff.maiaff ");
        sql.append(membre);
        sql.append(" (select maiaff from SCHEMA.afaffip af ");
        sql.append("join SCHEMA.PT_ASSOCIATION_COTISATION ass on ass.ID_PT_EMPLOYEURS = af.maiaff ");
        sql.append(
                "join SCHEMA.PT_COTISATIONS_AP cot on cot.ID =ass.ID_PT_COTISATIONS_AP and UPPER(cot.LIBELLE_UPPER) like '%CANTONAL%' ");
        sql.append(")");
        sql.append("group by adm.HBCADM, langue.PCOUID ");
        sql.append("order by adm.HBCADM, langue.PCOUID ");

        return sql.toString();
    }

    /**
     * Appel de la méthode permettant de créer le fichier excel des détails.
     *
     * @param mapExel Liste des détails des quorums
     */
    private void writeFile(Map<String, List<LigneQuorums>> mapExel, Map<String, Map<String, int[][]>> mapTableau) {
        excelDoc = new ListeQuorumsExcel(getSession(), DocumentConstants.LISTE_QUORUMS_DOC_NAME,
                DocumentConstants.LISTE_QUORUMS_NAME, mapExel, mapTableau, detail, periode);
        excelDoc.create();
    }

    private void print() throws Exception {
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelDoc.getOutputFile());
    }

    /**
     * Passage d'une liste à une map groupée par convention
     *
     * @param map Map de retour
     * @param quorums Liste à passer dans la map
     */
    private void putInMapByConvention(Map<String, List<LigneQuorums>> map, List<LigneQuorums> quorums) {

        for (LigneQuorums quorum : quorums) {
            List<LigneQuorums> quorumsTemp;
            String key = quorum.getCodeConvention();
            if (map.containsKey(key)) {
                quorumsTemp = map.get(key);
            } else {
                quorumsTemp = new ArrayList<LigneQuorums>();
            }
            quorumsTemp.add(quorum);
            map.put(key, quorumsTemp);
        }
    }

    /**
     * Passage d'une liste entreprises sans personnel à une map groupée par convention
     *
     * @param map Map de retour
     * @param quorums Liste à passer dans la map
     */
    private void putInMapByConventionSansPersonnel(Map<String, List<LigneSansPersonnel>> map,
            List<LigneSansPersonnel> quorums) {

        for (LigneSansPersonnel quorum : quorums) {
            List<LigneSansPersonnel> quorumsTemp;
            String key = quorum.getConvention();
            if (map.containsKey(key)) {
                quorumsTemp = map.get(key);
            } else {
                quorumsTemp = new ArrayList<LigneSansPersonnel>();
            }
            quorumsTemp.add(quorum);
            map.put(key, quorumsTemp);
        }

    }

    /**
     * Permet de filtrer une liste en fonction de la langue passée en paramètre
     *
     * @param langue Langue à friltrer
     * @param convention Permet de reprendre la bonne liste de quorums
     * @param map Contient les listes de quorums groupées par convention
     * @return Collection de quorums filtrée
     */
    private Collection<LigneQuorums> listeParLangue(final String langue, String convention,
            Map<String, List<LigneQuorums>> map) {
        Predicate<? super LigneQuorums> predicate = new Predicate<LigneQuorums>() {
            @Override
            public boolean apply(LigneQuorums input) {
                return input.getLangue().equals(langue);
            }
        };
        return Collections2.filter(map.get(convention), predicate);
    }

    private int nombreParLangueSansPersonnel(String langue, String convention,
            Map<String, List<LigneSansPersonnel>> map) {

        for (LigneSansPersonnel ligne : map.get(convention)) {
            if (ligne.getLangue().equals(langue)) {
                return Integer.parseInt(ligne.getSomme());
            }
        }
        return -1;
    }

    private List<String> listeParLangueEtAffilie(Collection<LigneQuorums> frM) {
        List<String> listeFiltree = new ArrayList<String>();
        for (LigneQuorums ligne : frM) {
            if (!listeFiltree.contains(ligne.getNoAffilie())) {
                listeFiltree.add(ligne.getNoAffilie());
            }
        }
        return listeFiltree;
    }

    /**
     * Permet de passer une liste dans une map groupée par convention et syndiqués ou non
     *
     * @param listeGeneral Liste à passer dans la map
     * @return Map de maps contenant des listes de quorums (groupement par convention et par syndiqués)
     */
    private Map<String, Map<String, List<LigneQuorums>>> listeParConventionSyndique(List<LigneQuorums> listeGeneral) {

        // Séparation de la liste entre syndiqués et non syndiqués
        List<LigneQuorums> syndiques = new ArrayList<LigneQuorums>();
        List<LigneQuorums> nonSyndiques = new ArrayList<LigneQuorums>();
        for (LigneQuorums ligne : listeGeneral) {
            if (ligne.getSyndicat() == null) {
                nonSyndiques.add(ligne);
            } else {
                syndiques.add(ligne);
            }
        }

        // Groupement par convention
        Map<String, List<LigneQuorums>> mapSyndiques = new HashMap<String, List<LigneQuorums>>();
        Map<String, List<LigneQuorums>> mapNonSyndiques = new HashMap<String, List<LigneQuorums>>();

        putInMapByConvention(mapSyndiques, syndiques);
        putInMapByConvention(mapNonSyndiques, nonSyndiques);

        Map<String, Map<String, List<LigneQuorums>>> returnMap = new HashMap<String, Map<String, List<LigneQuorums>>>();
        returnMap.put("syndiques", mapSyndiques);
        returnMap.put("nonSyndiques", mapNonSyndiques);

        return returnMap;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Periode getPeriode() {
        return periode;
    }

    public String getCodeConvention() {
        return codeConvention;
    }

    public void setCodeConvention(String idConvention) {
        codeConvention = idConvention;
    }

    public Boolean getDetail() {
        return detail;
    }

    public void setDetail(Boolean detail) {
        this.detail = detail;
    }

}
