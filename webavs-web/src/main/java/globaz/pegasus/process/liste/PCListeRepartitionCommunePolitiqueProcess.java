package globaz.pegasus.process.liste;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.CommunePolitiqueBean;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuiliderJade;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.business.domaine.pca.PcaEtat;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

/**
 * Permet l'impression de la liste des bénéficiaires PC (ou PC/Rente) en cours par commune politique
 * 
 * @author sco
 * 
 */
public class PCListeRepartitionCommunePolitiqueProcess extends PCAbstractJob {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_LISTE_PC_RENTE = "listePcRente";
    public static final String TYPE_LISTE_PC = "listePc";
    private static final String NUMERO_INFOROM_LISTE_PC_RENTE = "6512PPC";
    private static final String NUMERO_INFOROM_LISTE_PC = "6511PPC";

    private String email = null;
    private String typeListe = "";

    private String labelNomDocument = "";
    private String labelGenereLe = "";
    private String labelSubjectMailOk = "";
    private String labelSubjectMailko = "";
    private String labelBodyMailOk = "";
    private String labelBodyMailError = "";
    private String labelDescription = "";
    private String labelUser = "";
    private String labelCommunePolitique = "";

    @Override
    protected void process() throws Exception {
        try {

            findAllLabel();

            String dateDuJourAM = new Date().getValueMonth();

            List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP = loadDataPC(dateDuJourAM);

            if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
                listBeneficiairePCCP.addAll(loadDataRente(dateDuJourAM));
            }

            addCommunePolitique(listBeneficiairePCCP);

            sendMailWithDoc(createExcelFile(regroupByCommunePolitique(listBeneficiairePCCP)));

        } catch (Exception e) {
            JadeLogger.error("An error occurred while generating prestation list by commune politique", e);
            sendMailError(e);
        }
    }

    private void sendMailError(Exception e) {

        labelBodyMailError += "\n\n\n***** INFORMATIONS POUR GLOBAZ *****\n";
        labelBodyMailError += stack2string(e);

        try {
            JadeSmtpClient.getInstance().sendMail(getEmail(), labelSubjectMailko, labelBodyMailError, null);
        } catch (Exception e1) {
            JadeLogger.error("Unabled to send mail to " + getEmail(), e);
        }
    }

    private void findAllLabel() {
        if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
            labelNomDocument = NUMERO_INFOROM_LISTE_PC_RENTE + "_"
                    + getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_TITRE_BENEFICIARE_PC_RENTE");
            labelSubjectMailOk = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_RENTE_OK");
            labelSubjectMailko = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_RENTE_ERROR");
            labelBodyMailOk = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_RENTE_OK");
            labelBodyMailError = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_RENTE_KO");
            labelDescription = getSession().getLabel("PEGASUS_LISTE_BENEFICIARE_PC_RENTE_DESCRIPTION");
        } else {
            labelNomDocument = NUMERO_INFOROM_LISTE_PC + "_"
                    + getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_TITRE_BENEFICIARE_PC");
            labelSubjectMailOk = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_OK");
            labelSubjectMailko = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_ERROR");
            labelBodyMailOk = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_OK");
            labelBodyMailError = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_KO");
            labelDescription = getSession().getLabel("PEGASUS_LISTE_BENEFICIARE_PC_DESCRIPTION");
        }

        labelGenereLe = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_GENERE_LE");
        labelUser = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey());
        labelCommunePolitique = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_COMMUNE_NOM");
    }

    private Map<String, List<BeneficiairePCCommunePolitiquePojo>> regroupByCommunePolitique(
            List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        Map<String, List<BeneficiairePCCommunePolitiquePojo>> mapByCommunPolitique = new TreeMap<String, List<BeneficiairePCCommunePolitiquePojo>>();

        for (BeneficiairePCCommunePolitiquePojo pojo : listBeneficiairePCCP) {

            List<BeneficiairePCCommunePolitiquePojo> sousListe = mapByCommunPolitique.get(pojo
                    .getCodeCommunePolitique());
            if (sousListe == null) {
                sousListe = new ArrayList<BeneficiairePCCommunePolitiquePojo>();
            }
            sousListe.add(pojo);
            mapByCommunPolitique.put(pojo.getCodeCommunePolitique(), sousListe);
        }

        return mapByCommunPolitique;
    }

    /**
     * Récupération des données des PC
     * 
     * @param dateAMJ
     * @return
     */
    private List<BeneficiairePCCommunePolitiquePojo> loadDataPC(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiairePC(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    /**
     * Récupération des données des rentes
     * 
     * @param dateAMJ
     * @return
     */
    private List<BeneficiairePCCommunePolitiquePojo> loadDataRente(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiaireRente(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    private String createExcelFile(Map<String, List<BeneficiairePCCommunePolitiquePojo>> mapByCommunPolitique) {

        String fileName = NUMERO_INFOROM_LISTE_PC;
        if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
            fileName = NUMERO_INFOROM_LISTE_PC_RENTE;
        }

        SimpleOutputListBuilder simpleList = SimpleOutputListBuiliderJade.newInstance().outputNameAndAddPath(fileName);

        Iterator<List<BeneficiairePCCommunePolitiquePojo>> ite = mapByCommunPolitique.values().iterator();
        while (ite.hasNext()) {

            List<BeneficiairePCCommunePolitiquePojo> sousListe = ite.next();
            String sheetName = sousListe.get(0).getCodeCommunePolitique();
            String nomCommune = sousListe.get(0).getNomCommunePolitique();

            Details paramsData = new Details();
            paramsData.add(labelGenereLe, JACalendar.todayJJsMMsAAAA());
            paramsData.newLigne();
            paramsData.add(labelUser, getSession().getUserId());
            paramsData.newLigne();
            paramsData.add(labelCommunePolitique, nomCommune);

            simpleList.addList(sousListe).classElementList(BeneficiairePCCommunePolitiquePojo.class)
                    .addHeaderDetails(paramsData);
            simpleList.addTitle(labelNomDocument, Align.LEFT);

            if (!JadeStringUtil.isEmpty(sheetName)) {
                simpleList.addSubTitle(sheetName.replaceAll("\\*", "all"));
            }

            if (ite.hasNext()) {
                simpleList.jump();
            }
        }

        File file = simpleList.asXls().build();

        return file.getAbsolutePath();

    }

    /**
     * Ajout la commune politque a toutes les occurences de la liste
     * 
     * @param listBeneficiairePCCP
     */
    private void addCommunePolitique(List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        Set<String> setIdTiers = new HashSet<String>();
        Map<String, CommunePolitiqueBean> mapIdTiersCommunePolitique = new HashMap<String, CommunePolitiqueBean>();

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            setIdTiers.add(aBeneficiairePCCP.getIdTiers());
        }

        mapIdTiersCommunePolitique = PRTiersHelper.findCommunePolitique(setIdTiers, new Date().getDate(), getSession());

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            CommunePolitiqueBean communePolitique = mapIdTiersCommunePolitique.get(aBeneficiairePCCP.getIdTiers());
            if (communePolitique != null) {
                aBeneficiairePCCP.setCommunePolitique(communePolitique);
            }
        }

        Collections.sort(listBeneficiairePCCP);

    }

    /**
     * Envoi du mail avec le document
     */
    private void sendMailWithDoc(String fileName) throws Exception {

        String[] tabFileName = { fileName };

        JadeSmtpClient.getInstance().sendMail(getEmail(), labelSubjectMailOk, labelBodyMailOk, tabFileName);
    }

    private String getSqlSelectBeneficiairePC(String dateAM) {

        StringBuilder sql = new StringBuilder("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montant ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.PCPCACC pcac on (prac.ZTIPRA = pcac.CUIPRA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (prac.ZTITBE = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_PC
                + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAM + ") and pcac.CUTETA = "
                + PcaEtat.VALIDE.getValue());

        return sql.toString();
    }

    private String getSqlSelectBeneficiaireRente(String dateAM) {

        StringBuilder sql = new StringBuilder("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montant ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.REREACC reac on (prac.ZTIPRA = reac.YLIRAC) ");
        sql.append(" inner join SCHEMA.REBACAL baca on (reac.YLIBAC = baca.YIIBCA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (baca.YIITBC = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE ");
        sql.append(" prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_RENTES);
        sql.append(" and ((prac.ZTTETA in (" + IREPrestationAccordee.CS_ETAT_VALIDE + ","
                + IREPrestationAccordee.CS_ETAT_PARTIEL + ") and prac.ZTDFDR = 0 ) ");
        sql.append(" OR (prac.ZTTETA = " + IREPrestationAccordee.CS_ETAT_DIMINUE
                + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAM + ")))");

        return sql.toString();
    }

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    @Override
    public String getDescription() {
        return labelDescription;
    }

    @Override
    public String getName() {
        return "PCListeRepartitionCommunePolitiqueProcess";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }
}
