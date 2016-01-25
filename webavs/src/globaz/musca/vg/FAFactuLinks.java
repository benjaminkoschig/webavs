package globaz.musca.vg;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/*
 * !!!!!!! [oca] Ce module n'est a utilisé que pour des tests uniquement, ne pas utiliser en prod
 */

public class FAFactuLinks implements ITISummarizable {
    private String element = "";
    private String icon = "";
    private int maxHorizontalItems = 0;
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private String style = "";
    private String title = "";

    private String urlTitle = "";

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession session) throws Exception {
        TISummaryInfo info = new TISummaryInfo();
        StringBuffer content = new StringBuffer();
        TITiers t = new TITiers();
        t.setSession(session);
        t.setIdTiers(idTiers);

        content.append("<h2>" + t.getNomEtNumero() + "</h2>");

        final Set<String> allNumero = new TreeSet<String>();

        // RELEVES
        List<Map<String, String>> resRel = TISQL
                .query(session,
                        "MMIREL MMIREL," + "trim(r.MALNAF) MALNAF," + "trim(IDEXTERNEFACTURE) IDEXTERNEFACTURE",

                        " from " + TIToolBox.getCollection() + "AFREVEP r," + TIToolBox.getCollection() + "FAENTFP e "
                                + "where MMEBID=IDENTETEFACTURE and HTITIE=" + idTiers + " "
                                + " order by MALNAF,IDEXTERNEFACTURE desc");

        Map<String, List<Map<String, String>>> mapRel = JadeListUtil.groupBy(resRel, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                String key = m.get("IDEXTERNEFACTURE") + " " + m.get("MALNAF");
                allNumero.add(key);
                return key;
            }
        });
        // DS
        List<Map<String, String>> resDS = TISQL.query(session, "TAIDDE TAIDDE, " + "trim(MALNAF) MALNAF, "
                + "TANDEC TANDEC," + "TAANNE TAANNE ",

        " from " + TIToolBox.getCollection() + "DSDECLP d,  " + TIToolBox.getCollection() + "AFAFFIP aff"
                + " where aff.HTITIE=" + idTiers + " and  d.MAIAFF = aff.MAIAFF" + " order by aff.MALNAF,TANDEC");

        Map<String, List<Map<String, String>>> mapDS = JadeListUtil.groupBy(resDS, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                // si num dec vide ou 0, on prend l'année
                String key = (JadeStringUtil.isIntegerEmpty((String) m.get("TANDEC")) ? m.get("TAANNE") : m
                        .get("TANDEC")) + " " + m.get("MALNAF");
                allNumero.add(key);
                return key;
            }
        });

        // FACTU
        List<Map<String, String>> resFactu = TISQL.query(session, "IDPASSAGE IDPASSAGE,"
                + "IDENTETEFACTURE IDENTETEFACTURE," + "trim(IDEXTERNEROLE) IDEXTERNEROLE,"
                + "trim(IDEXTERNEFACTURE) IDEXTERNEFACTURE," + "TOTALFACTURE TOTALFACTURE",

        " from " + TIToolBox.getCollection() + "FAENTFP " + "where IDTIERS=" + idTiers + " "
                + " order by IDEXTERNEROLE,IDEXTERNEFACTURE desc");

        Map<String, List<Map<String, String>>> mapFactu = JadeListUtil.groupBy(resFactu, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                String key = m.get("IDEXTERNEFACTURE") + " " + m.get("IDEXTERNEROLE");
                allNumero.add(key);
                return key;
            }
        });

        // COMPTA
        List<Map<String, String>> resCompta = TISQL.query(session, "s.IDSECTION IDSECTION,"
                + "c.IDCOMPTEANNEXE IDCOMPTEANNEXE," + "trim(IDEXTERNEROLE) IDEXTERNEROLE,"
                + "trim(IDEXTERNE) IDEXTERNE," + "s.BASE BASE," + "s.SOLDE SOLDE",

        " from " + TIToolBox.getCollection() + "CASECTP s," + TIToolBox.getCollection() + "CACPTAP c "
                + "where c.IDCOMPTEANNEXE = s.IDCOMPTEANNEXE and c.IDTIERS=" + idTiers + " "
                + " order by IDEXTERNEROLE,IDEXTERNE desc");

        Map<String, List<Map<String, String>>> mapCompta = JadeListUtil.groupBy(resCompta, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                String key = m.get("IDEXTERNE") + " " + m.get("IDEXTERNEROLE");
                allNumero.add(key);
                return key;
            }
        });

        // IM
        List<Map<String, String>> resIM = TISQL.query(session, "IDINTERETMORATOIRE IDINTERETMORATOIRE,"
                + "trim(IDEXTERNEROLE) IDEXTERNEROLE," + "trim(IDEXTERNE) IDEXTERNE",

                " from " + TIToolBox.getCollection() + "CAIMDCP im," + TIToolBox.getCollection() + "CASECTP s,"
                        + TIToolBox.getCollection() + "CACPTAP c "
                        + "where im.idsection = s.idsection and c.IDCOMPTEANNEXE = s.IDCOMPTEANNEXE and c.IDTIERS="
                        + idTiers + " and im.idsection <> im.idsectionfacture"
                        + " order by IDEXTERNEROLE,IDEXTERNE desc");

        Map<String, List<Map<String, String>>> mapIM = JadeListUtil.groupBy(resIM, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                String key = m.get("IDEXTERNE") + " " + m.get("IDEXTERNEROLE");
                allNumero.add(key);
                return key;
            }
        });

        /*
         * ------------------------------------------------------ Compteurs
         */

        List<Map<String, String>> resCompteur = TISQL
                .query(session,
                        "CUMULCOTISATION CUMULCOTISATION," + "CUMULMASSE CUMULMASSE," + "IDEXTERNE IDEXTERNE,"
                                + "ANNEE ANNEE," + "IDEXTERNEROLE IDEXTERNEROLE",

                        " from "
                                + TIToolBox.getCollection()
                                + "CACPTRP cp, "
                                + TIToolBox.getCollection()
                                + "CACPTAP ca, "
                                + TIToolBox.getCollection()
                                + "CARUBRP r "
                                + " where cp.IDCOMPTEANNEXE = CA.IDCOMPTEANNEXE and r.IDRUBRIQUE = cp.IDRUBRIQUE and ca.IDTIERS ="
                                + idTiers);

        Map<String, List<Map<String, String>>> mapCompteur = JadeListUtil.groupBy(resCompteur, new Key() {
            @Override
            public String exec(Object e) {
                Map m = (Map) e;
                String key = m.get("ANNEE") + "";

                return key;
            }
        });

        /*
         * ------------------------------------------------------ build content
         */

        content.append("<table border=1>");
        content.append("<tr>" + "<th></th>" + "<th>DS</th>" + "<th>REL</th>" + "<th>FACTU</th>" + "<th>SECTION</th>"
                + "<th>IM</th>" +

                "</tr>");

        String lastYear = "";
        for (Iterator<String> it = allNumero.iterator(); it.hasNext();) {

            String key = it.next();
            String year = key.substring(0, 4);

            if (!year.equals(lastYear)) {
                content.append("<tr><td colspan=6><b>" + year + "</b></td></tr>");

                /*
                 * Affiche les compteurs pour cette annee
                 */
                List<Map<String, String>> list = mapCompteur.get(year);

                content.append("<tr><td colspan=6>");
                if (list == null) {
                    content.append("<li>Pas de compteur pour l'année : " + year);
                } else {
                    for (Map<String, String> row : list) {
                        content.append("<li>" + row.get("IDEXTERNEROLE") + " " + row.get("IDEXTERNE") + " "
                                + row.get("CUMULCOTISATION") + " " + row.get("CUMULMASSE") + " ");
                    }

                }
                content.append("</td></tr>");

            }
            lastYear = year;

            content.append("<tr>");
            content.append("<td>" + key + "</td>");

            // DS --------------------------------------------------
            content.append("<td>");
            if (mapDS.containsKey(key)) {
                List<Map<String, String>> list = mapDS.get(key);
                for (Map<String, String> row : list) {
                    String idDec = row.get("TAIDDE");
                    String lienDeclAprecuFacture = "<li><a href='#' onclick=\"callLocalUrl('/draco?userAction=draco.declaration.ligneDeclaration.chercher&selectedId="
                            + idDec + "')\">X</a>";
                    content.append(lienDeclAprecuFacture + " ");
                }
            }
            content.append("&nbsp;</td>");

            // REL --------------------------------------------------
            content.append("<td>");
            if (mapRel.containsKey(key)) {
                List<Map<String, String>> list = mapRel.get(key);
                for (Map<String, String> row : list) {
                    String idReleve = row.get("MMIREL");

                    String lienRel = "<li><a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.releve.apercuReleve.afficher&selectedId="
                            + idReleve + "')\">X</a>";
                    content.append(lienRel + " ");
                }
            }
            content.append("&nbsp;</td>");

            // FACTU --------------------------------------------------
            content.append("<td>");
            if (mapFactu.containsKey(key)) {
                List<Map<String, String>> list = mapFactu.get(key);
                for (Map<String, String> row : list) {
                    String idPassage = row.get("IDPASSAGE");
                    String idEnteteFacture = row.get("IDENTETEFACTURE");
                    String total = row.get("TOTALFACTURE");
                    String lienEntete = "<li><a href='#' onclick=\"callLocalUrl('/musca?userAction=musca.facturation.afact.chercher&idEnteteFacture="
                            + idEnteteFacture + "&idPassage=" + idPassage + "')\">" + total + "</a>";
                    content.append(lienEntete + " ");
                }
            }
            content.append("&nbsp;</td>");

            // COMPTA --------------------------------------------------
            content.append("<td>");
            if (mapCompta.containsKey(key)) {
                List<Map<String, String>> list = mapCompta.get(key);
                for (Map<String, String> row : list) {
                    String idSection = row.get("IDSECTION");
                    String base = row.get("BASE");
                    String solde = row.get("SOLDE");
                    if (!"0.00".equals(solde)) {
                        base += " / " + solde;
                    }
                    String lienSection = "<li><a href='#' onclick=\"callLocalUrl('/osiris?userAction=osiris.comptes.apercuSectionDetaille.chercher&id="
                            + idSection + "')\">" + base + "</a>";
                    content.append(lienSection + " ");
                }
            }
            content.append("&nbsp;</td>");

            // IM -----------------------------------------------------
            content.append("<td>");
            if (mapIM.containsKey(key)) {
                List<Map<String, String>> list = mapIM.get(key);
                for (Map<String, String> row : list) {
                    String idIM = row.get("IDINTERETMORATOIRE");
                    String lienIM = "<li><a href='#' onclick=\"callLocalUrl('/osiris?userAction=osiris.interets.detailInteretMoratoire.chercher&domaine=CA&id="
                            + idIM + "')\">X</a>";
                    content.append(lienIM + " ");
                }
            }
            content.append("&nbsp;</td>");

            content.append("</tr>");
        }
        content.append("</table>");

        info.setText(content.toString());
        return new TISummaryInfo[] { info };

    }

    @Override
    public int getMaxHorizontalItems() {
        return maxHorizontalItems;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    @Override
    public String getStyle() {
        return style;
    }

    /*
     * Getter et Setter
     */
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setMaxHorizontalItems(int maxHorizontalItems) {
        this.maxHorizontalItems = maxHorizontalItems;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
