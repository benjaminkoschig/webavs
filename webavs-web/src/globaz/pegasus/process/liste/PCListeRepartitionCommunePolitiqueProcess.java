package globaz.pegasus.process.liste;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class PCListeRepartitionCommunePolitiqueProcess extends PCAbstractJob {

    private static final long serialVersionUID = 1L;

    private static final String TYPE_LISTE_PC_RENTE = "listePcRente";

    private String email = null;
    private String typeListe = "";

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    @Override
    public String getDescription() {
        return "Process pour générer les listes de prestations complémentaires par commune politique";
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return "PCListeRepartitionCommunePolitiqueProcess";
    }

    private String getSqlSelectBeneficiairePC(String dateAMJ) {

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montant ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.PCPCACC pcac on (prac.ZTIPRA = pcac.CUIPRA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (prac.ZTITBE = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_PC + " and pcac.CUTRBE = "
                + IPCDroits.CS_ROLE_FAMILLE_REQUERANT + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAMJ + ") ");

        return sql.toString();
    }

    private String getSqlSelectBeneficiaireRente(String dateAMJ) {

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montantPrestation ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.REREACC reac on (prac.ZTIPRA = reac.YLIRAC) ");
        sql.append(" inner join SCHEMA.REBACAL baca on (reac.YLIBAC = baca.YIIBCA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (baca.YIITBC = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_RENTES
                + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAMJ + ") ");

        return sql.toString();

    }

    private List<BeneficiairePCCommunePolitiquePojo> loadDataPC(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiairePC(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    private List<BeneficiairePCCommunePolitiquePojo> loadDataRente(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiaireRente(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    private String createPdfFile(List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        String filePath = Jade.getInstance().getPersistenceDir() + JadeUUIDGenerator.createStringUUID();

        SimpleOutputListBuilder<BeneficiairePCCommunePolitiquePojo> builder = new SimpleOutputListBuilder<BeneficiairePCCommunePolitiquePojo>();

        Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());
        File file = builder.local(locale).classValue(BeneficiairePCCommunePolitiquePojo.class)
                .title("Liste des bénéficiaires PC en cours par commune", Align.LEFT).asPdf().outputName(filePath)
                .addList(listBeneficiairePCCP).build();

        return file.getAbsolutePath();

    }

    private void addCommunePolitique(List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        Set<String> setIdTiers = new HashSet<String>();
        Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            setIdTiers.add(aBeneficiairePCCP.getIdTiers());
        }

        mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            String communePolitique = mapIdTiersCommunePolitique.get(aBeneficiairePCCP.getIdTiers());
            if (!JadeStringUtil.isEmpty(communePolitique)) {
                aBeneficiairePCCP.setCommunePolitique(communePolitique);
            }
        }

        Collections.sort(listBeneficiairePCCP);

    }

    @Override
    protected void process() throws Exception {
        try {
            String dateDuJourAMJ = (new JADate(JACalendar.todayJJsMMsAAAA())).toAMJ().toString();

            List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP = loadDataPC(dateDuJourAMJ);

            if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
                listBeneficiairePCCP.addAll(loadDataRente(dateDuJourAMJ));
            }

            addCommunePolitique(listBeneficiairePCCP);

            String fileAbsolutePath = createPdfFile(listBeneficiairePCCP);

            sendMailWithDoc(fileAbsolutePath);

        } catch (Exception e) {
            JadeLogger.error("An error occurred while generating prestation list by commune politique", e);
        }
        ;
    }

    private void sendMailWithDoc(String fileName) throws Exception {

        String[] tabFileName = { fileName };

        JadeSmtpClient.getInstance().sendMail(getEmail(), "todo", "todo", tabFileName);

    }

    public void setEmail(String email) {
        this.email = email;
    }

}
