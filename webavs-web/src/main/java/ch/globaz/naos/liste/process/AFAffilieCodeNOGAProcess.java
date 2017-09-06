/**
 * 
 */
package ch.globaz.naos.liste.process;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.prestation.interfaces.util.nss.PRUtil;
import java.io.File;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.liste.process.ALIndeRevenuMinNonAtteintProcess;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.naos.liste.container.AFAffiliesCodeNOGAContainer;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

/**
 * Process qui envoi la liste des affiliés lié à un code NOGA (ou ayant un code NOGA)
 * 
 * @author est
 */
public class AFAffilieCodeNOGAProcess extends BProcess {

    private static final long serialVersionUID = -3582914692974436954L;
    private static final String NUM_INFOROM = "0326CAF";

    private static final Logger logger = LoggerFactory.getLogger(ALIndeRevenuMinNonAtteintProcess.class);

    private List<String> mailsList = new ArrayList<String>();

    private Boolean isOnlyAffiliesActifs;

    private String idCScodeNoga;

    @Override
    protected void _executeCleanUp() {
        //
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        mailsList.add(getEMailAddress());

        String sqlAffiliesActifs = getSqlRequestActifs();

        List<AFAffiliesCodeNOGAContainer> listAffiliesWithCodeNoga = SCM.newInstance(AFAffiliesCodeNOGAContainer.class)
                .session(getSession()).query(sqlAffiliesActifs).execute();

        // Permet de récupérer aussi les non actifs
        if (!isOnlyAffiliesActifs) {
            listAffiliesWithCodeNoga.addAll(SCM.newInstance(AFAffiliesCodeNOGAContainer.class).session(getSession())
                    .query(getSqlRequestNonActifs()).execute());
        }

        // On enlève les doublons
        Set<AFAffiliesCodeNOGAContainer> hs = new HashSet<AFAffiliesCodeNOGAContainer>();
        hs.addAll(listAffiliesWithCodeNoga);
        listAffiliesWithCodeNoga.clear();
        listAffiliesWithCodeNoga.addAll(hs);

        // On tire par codeNoga
        Collections.sort(listAffiliesWithCodeNoga, new Comparator<AFAffiliesCodeNOGAContainer>() {

            @Override
            public int compare(AFAffiliesCodeNOGAContainer o1, AFAffiliesCodeNOGAContainer o2) {
                int codeNogaCmp = o1.getCodeNoga().compareTo(o2.getCodeNoga());
                if (codeNogaCmp != 0) {
                    return codeNogaCmp;
                }
                return o1.getRaisonSociale().compareTo(o2.getRaisonSociale());
            }
        });

        File file = genererFileExcel(listAffiliesWithCodeNoga);
        // Envoi du mail
        sendMail(mailsList, file.getAbsolutePath());

        return true;
    }

    /***
     * Défini l'objet du mail quand le process est terminé quand on imprime la liste seul (depuis le viewbean)
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_CODE_NOGA_PROCESS_TERMINE");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private String getSqlRequestActifs() throws ParseException {
        StringBuilder requeteSQL = new StringBuilder();
        requeteSQL
                .append("SELECT afi.MALFED as NUM_IDE, afi.MALNAF as NUM_AFFILIE, afi.MADESL as RAISON_SOCIALE, cat.PCOUID  as CODE_CATEGORIE, cat.PCOLUT as TXT_CATEGORIE, val.PCOUID as CODE_NOGA, val.PCOLUT as TXT_NOGA, coupGenreAfi.PCOLUT as GENRE_AFFI ,temp.MASSE_SALARIALE, temp.NB_ASSURES ");
        requeteSQL.append(" FROM SCHEMA.FWCOSP code ");
        requeteSQL.append(" INNER JOIN SCHEMA.fwcoup val on val.pcosid = code.pcosid and val.plaide = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL.append(" INNER JOIN SCHEMA.fwcoup cat on cat.pcosid = code.pcoise and cat.plaide = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL.append(" INNER JOIN SCHEMA.AFAFFIP afi on afi.MATCDN = code.PCOSID");
        requeteSQL.append(" INNER JOIN SCHEMA.AFADHEP adh on adh.MAIAFF = afi.MAIAFF");
        requeteSQL.append(" INNER JOIN SCHEMA.AFCOTIP cot on cot.MRIADH = adh.MRIADH");

        requeteSQL
                .append(" LEFT JOIN ( select count(kaiind) as NB_ASSURES, malnaf, sum(SUMMONTANT) as MASSE_SALARIALE from ( SELECT kaiind, malnaf, SUM(kbmmon) as SUMMONTANT from ("
                        + " SELECT ecr.KAIIND, case when kbtext <> 0 then -kbmmon else kbmmon end as KBMMON, malnaf FROM SCHEMA.CIECRIP ecr"
                        + " inner join SCHEMA.afaffip aff on aff.maiaff = ecr.KBITIE inner join SCHEMA.ciindip ind on ind.KAIIND = ecr.KAIIND"
                        + " inner join ("
                        + " SELECT SCHEMA.CIECRIP.KBITIE, MAX(SCHEMA.CIECRIP.KBNANN) AS maxAnnee"
                        + " FROM SCHEMA.CIECRIP"
                        + " WHERE SCHEMA.CIECRIP.KBNANN <> "
                        + Date.now().getAnnee()
                        + " GROUP BY KBITIE) sousSelect on ecr.KBNANN = sousSelect.maxAnnee and aff.maiaff = sousSelect.KBITIE AND (aff.MADFIN = 0 OR aff.MADFIN > "
                        + retrieveDateJourAsInt()
                        + " ) and KBTGEN in (310001,310006))"
                        + " group by kaiind, malnaf having sum(kbmmon)> 0) group by malnaf) as temp on temp.malnaf = afi.malnaf");

        requeteSQL
                .append(" LEFT JOIN SCHEMA.FWCOUP coupGenreAfi ON afi.MATTAF = coupGenreAfi.PCOSID AND coupGenreAfi.PLAIDE = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL
                .append(" INNER JOIN SCHEMA.AFASSUP ass on (ass.MBIASS = cot.MBIASS AND MBTGEN = 801001 AND MBTTYP=812001)");
        requeteSQL.append(" WHERE cot.MEDFIN = 0");
        requeteSQL.append(" AND (afi.MADFIN = 0 OR afi.MADFIN > " + retrieveDateJourAsInt() + ")");
        if (idCScodeNoga != null && !idCScodeNoga.isEmpty() && !"0".equals(idCScodeNoga)) {
            requeteSQL.append(" AND code.PCOSID = " + idCScodeNoga);
        }

        requeteSQL.append(" ORDER BY val.PCOUID ASC, afi.MADESL ASC");

        return requeteSQL.toString();
    }

    /***
     * Requête qui récupère les affiliés avec code Noga.
     * La masse salariale et le nombre d'assuré viennent des CI, pour la dernière année inscrite au CI sauf l'année en
     * cours.
     * Seuls les écritures salariés sont comptés (genre 1 et 6 (extourne)).
     * 
     * @return
     * @throws ParseException
     */
    private String getSqlRequestNonActifs() throws ParseException {
        StringBuilder requeteSQL = new StringBuilder();
        requeteSQL
                .append("SELECT afi.MALFED as NUM_IDE, afi.MALNAF as NUM_AFFILIE, afi.MADESL as RAISON_SOCIALE, cat.PCOUID  as CODE_CATEGORIE, cat.PCOLUT as TXT_CATEGORIE, val.PCOUID as CODE_NOGA, val.PCOLUT as TXT_NOGA, coupGenreAfi.PCOLUT as GENRE_AFFI ,temp.MASSE_SALARIALE, temp.NB_ASSURES ");
        requeteSQL.append(" FROM SCHEMA.FWCOSP code ");
        requeteSQL.append(" INNER JOIN SCHEMA.fwcoup val on val.pcosid = code.pcosid and val.plaide = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL.append(" INNER JOIN SCHEMA.fwcoup cat on cat.pcosid = code.pcoise and cat.plaide = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL.append(" INNER JOIN SCHEMA.AFAFFIP afi on afi.MATCDN = code.PCOSID");
        requeteSQL
                .append(" INNER JOIN SCHEMA.AFADHEP adh on adh.MAIAFF = afi.MAIAFF and (adh.mrdfin <> 0 and adh.mrdfin = (SELECT max(adhtemp.mrdfin) FROM SCHEMA.AFADHEP adhtemp where adhtemp.MAIAFF =  adh.MAIAFF)) ");
        requeteSQL.append(" INNER JOIN SCHEMA.AFCOTIP cot on cot.MRIADH = adh.MRIADH");

        requeteSQL
                .append(" LEFT JOIN ( select count(kaiind) as NB_ASSURES, malnaf, sum(SUMMONTANT) as MASSE_SALARIALE from ( SELECT kaiind, malnaf, SUM(kbmmon) as SUMMONTANT from ("
                        + " SELECT ecr.KAIIND, case when kbtext <> 0 then -kbmmon else kbmmon end as KBMMON, malnaf FROM SCHEMA.CIECRIP ecr"
                        + " inner join SCHEMA.afaffip aff on aff.maiaff = ecr.KBITIE inner join SCHEMA.ciindip ind on ind.KAIIND = ecr.KAIIND"
                        + " inner join ("
                        + " SELECT SCHEMA.CIECRIP.KBITIE, MAX(SCHEMA.CIECRIP.KBNANN) AS maxAnnee"
                        + " FROM SCHEMA.CIECRIP"
                        + " WHERE SCHEMA.CIECRIP.KBNANN <> "
                        + Date.now().getAnnee()
                        + " GROUP BY KBITIE ) sousSelect on ecr.KBNANN = sousSelect.maxAnnee and aff.maiaff = sousSelect.KBITIE AND (aff.MADFIN <> 0 AND aff.MADFIN < "
                        + retrieveDateJourAsInt()
                        + " )  and KBTGEN in (310001,310006))"
                        + " group by kaiind, malnaf having sum(kbmmon)> 0) group by malnaf) as temp on temp.malnaf = afi.malnaf");

        requeteSQL
                .append(" LEFT JOIN SCHEMA.FWCOUP coupGenreAfi ON afi.MATTAF = coupGenreAfi.PCOSID AND coupGenreAfi.PLAIDE = '");
        requeteSQL.append(retrieveLettreLangueUser());
        requeteSQL.append("'");
        requeteSQL
                .append(" INNER JOIN SCHEMA.AFASSUP ass on (ass.MBIASS = cot.MBIASS AND MBTGEN = 801001 AND MBTTYP=812001)");

        // On contrôle que l'affiliation à une date de fin et plus petite que la date du jour
        requeteSQL.append(" WHERE (afi.MADFIN <> 0 AND afi.MADFIN < " + retrieveDateJourAsInt() + ")");

        if (idCScodeNoga != null && !idCScodeNoga.isEmpty() && !"0".equals(idCScodeNoga)) {
            requeteSQL.append(" AND code.PCOSID = " + idCScodeNoga);
        }

        requeteSQL.append(" ORDER BY val.PCOUID ASC, afi.MADESL ASC");

        return requeteSQL.toString();
    }

    private File genererFileExcel(List<AFAffiliesCodeNOGAContainer> listAffiliesWithCodeNoga) {
        Details detail = new Details();
        detail.add(getSession().getLabel("LISTE_CODE_NOGA_NUM_INFOROM"), NUM_INFOROM);
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_CODE_NOGA_NUM_CAISSE"), getNoCaisse());
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_CODE_NOGA_TRAITEMENT_DU"), Date.now().toString());

        if (isOnlyAffiliesActifs) {
            detail.newLigne();
            detail.newLigne();
            detail.add(getSession().getLabel("NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_AFFI_ACTIVE"), "");
        }

        SimpleOutputListBuilderJade simpleOutputListBuilderJade = SimpleOutputListBuilderJade.newInstance();

        File file = simpleOutputListBuilderJade
                .session(getSession())
                .outputNameAndAddPath(NUM_INFOROM + "_" + getSession().getLabel("LISTE_CODE_NOGA_EXCEL_NAME"))
                .globazTheme()
                .headerLeftTop(NUM_INFOROM)
                .addTranslater()
                .addList(listAffiliesWithCodeNoga)
                .classElementList(AFAffiliesCodeNOGAContainer.class)
                .addTitle(getSession().getLabel("LISTE_CODE_NOGA_TITRE") + " " + retrieveCodeNOGAFromCS(idCScodeNoga),
                        Align.CENTER).addHeaderDetails(detail).asXls().build();
        simpleOutputListBuilderJade.close();
        return file;
    }

    private void sendMail(List<String> mailsList, String joinFilePath) {
        // ajout de la pièce jointe
        List<String> joinsFilesPathsList = new ArrayList<String>();

        String subject;
        String body;

        subject = MessageFormat.format(getSession().getLabel("LISTE_CODE_NOGA_TITRE"), "");

        joinsFilesPathsList.add(joinFilePath);

        if (idCScodeNoga != null && !idCScodeNoga.isEmpty()) {

            body = MessageFormat.format(
                    FWMessageFormat.prepareQuotes(getSession().getLabel("LISTE_CODE_NOGA_MAIL_BODY_WITH_CODE"), false),
                    retrieveCodeNOGAFromCS(idCScodeNoga));
        } else {
            body = MessageFormat.format(getSession().getLabel("LISTE_CODE_NOGA_MAIL_BODY"), "");
        }

        try {
            ProcessMailUtils.sendMail(mailsList, subject, body, joinsFilesPathsList);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du mail", e);
        }
    }

    private String getNoCaisse() {
        try {
            ALApplication app = (ALApplication) GlobazServer.getCurrentSystem().getApplication(
                    ALApplication.DEFAULT_APPLICATION_WEBAF);

            return app.getNoCaisse();
        } catch (Exception e) {
            logger.error("Unabled to retrieve the propertie 'common.noCaisseFormate' " + e);
        }

        return "";
    }

    private String retrieveCodeNOGAFromCS(String idCScodeNoga) {
        return getSession().getCode(idCScodeNoga);
    }

    /***
     * Méthode qui permet de récupérer la lettre en fonction de l'utilisateur de l'app
     * F pour français, D pour allemand et I pour Italien
     * 
     * @return
     */
    private String retrieveLettreLangueUser() {
        String langue = PRUtil.getISOLangueTiers(getSession().getIdLangueISO());

        return langue.substring(0, 1);
    }

    /***
     * Retourne la date sous format YYYMMDD en int
     * 
     * @return
     * @throws ParseException
     */
    private String retrieveDateJourAsInt() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date d = sdf.parse(new Date().toString());
        sdf.applyPattern("yyyyMMdd");
        return sdf.format(d);
    }

    public Boolean getIsOnlyAffiliesActifs() {
        return isOnlyAffiliesActifs;
    }

    public void setIsOnlyAffiliesActifs(Boolean isOnlyAffiliesActifs) {
        this.isOnlyAffiliesActifs = isOnlyAffiliesActifs;
    }

    public String getIdCScodeNoga() {
        return idCScodeNoga;
    }

    public void setIdCScodeNoga(String idCScodeNoga) {
        this.idCScodeNoga = idCScodeNoga;
    }

}