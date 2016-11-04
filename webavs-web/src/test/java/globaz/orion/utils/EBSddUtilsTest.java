package globaz.orion.utils;

import static org.junit.Assert.*;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.orion.ws.cotisation.DecompteMensuel;
import ch.globaz.orion.ws.cotisation.DecompteMensuelBuilder;
import ch.globaz.orion.ws.cotisation.DecompteMensuelLine;
import ch.globaz.orion.ws.cotisation.DecompteMensuelLineBuilder;
import ch.globaz.xmlns.eb.sdd.DecompteAndLignes;
import ch.globaz.xmlns.eb.sdd.DecompteEntity;
import ch.globaz.xmlns.eb.sdd.DecompteStatutEnum;
import ch.globaz.xmlns.eb.sdd.DecompteTypeEnum;
import ch.globaz.xmlns.eb.sdd.LigneDeDecompteEntity;

public class EBSddUtilsTest {

    @Test
    public void testPrepareDataForEbusiness() throws Exception {

        DecompteMensuel decompte = createDecompte();

        decompte = EBSddUtils.prepareDataForEbusiness(decompte);

        for (DecompteMensuelLine line : decompte.getLinesDecompte()) {

            assertTrue("Comparaison du type de coti avec", !line.getTypeCotisation().equals("812027"));

        }

    }

    @Test
    public void testComputeDataForReleve() throws Exception {

        DecompteAndLignes decompte = createDecompteAndLignes();

        List<LigneDeDecompteEntity> lignes = new ArrayList<LigneDeDecompteEntity>();
        addLignesDeDecomptesTest(lignes);

        AFApercuReleve releve = new AFApercuReleve();

        AFApercuReleveLineFacturation cotiAVS = new AFApercuReleveLineFacturation();
        cotiAVS.setCotisationId("1");
        cotiAVS.setAssuranceId("1");
        cotiAVS.setIdPlan("1");
        cotiAVS.setTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        releve.addCotisation(cotiAVS);

        AFApercuReleveLineFacturation cotiAF = new AFApercuReleveLineFacturation();
        cotiAF.setCotisationId("2");
        cotiAF.setAssuranceId("2");
        cotiAF.setIdPlan("2");
        cotiAF.setTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
        releve.addCotisation(cotiAF);

        AFApercuReleveLineFacturation cotiPCF = new AFApercuReleveLineFacturation();
        cotiPCF.setCotisationId("25");
        cotiPCF.setAssuranceId("25");
        cotiPCF.setIdPlan("25");
        cotiPCF.setTypeAssurance(CodeSystem.TYPE_ASS_PC_FAMILLE);
        releve.addCotisation(cotiPCF);

        AFApercuReleveLineFacturation cotiPREVHOR = new AFApercuReleveLineFacturation();
        cotiPREVHOR.setCotisationId("26");
        cotiPREVHOR.setAssuranceId("26");
        cotiPREVHOR.setIdPlan("26");
        cotiPREVHOR.setTypeAssurance(CodeSystem.TYPE_ASS_PREVHOR);
        releve.addCotisation(cotiPREVHOR);

        EBSddUtils.computeDataForReleve(lignes, releve);
    }

    private DecompteMensuel createDecompte() {

        List<DecompteMensuelLine> lines = new ArrayList<DecompteMensuelLine>();

        lines.add(createLigneDecompteAVS());
        lines.add(createLigneDecompteAF());
        lines.add(createLigneDecompteAC());
        lines.add(createLigneDecompteFFPP());

        return new DecompteMensuelBuilder().withIdAffilie("1231").withAnneeDecompte("2016").withMoisDecompte("10")
                .withNumeroAffilie("401.1004").withLinesDecompte(lines).build();
    }

    private DecompteMensuelLine createLigneDecompteAVS() {
        return new DecompteMensuelLineBuilder().withIdCotisation("1").withIdRubrique(101).withLibelleFr("AVS")
                .withMasse(BigDecimal.ZERO).withTypeCotisation("812001").build();
    }

    private DecompteMensuelLine createLigneDecompteAF() {
        return new DecompteMensuelLineBuilder().withIdCotisation("2").withIdRubrique(102).withLibelleFr("AF")
                .withMasse(BigDecimal.ZERO).withTypeCotisation("812002").build();
    }

    private DecompteMensuelLine createLigneDecompteAC() {
        return new DecompteMensuelLineBuilder().withIdCotisation("3").withIdRubrique(103).withLibelleFr("AC")
                .withMasse(BigDecimal.ZERO).withTypeCotisation("812006").build();
    }

    private DecompteMensuelLine createLigneDecompteFFPP() {
        return new DecompteMensuelLineBuilder().withIdCotisation("4").withIdRubrique(104).withLibelleFr("FFPP")
                .withMasse(BigDecimal.ZERO).withTypeCotisation("812027").build();
    }

    private DecompteAndLignes createDecompteAndLignes() {

        DecompteEntity d = new DecompteEntity();
        d.setCreationDate(createDate("20161031"));
        d.setDateDeSaisie(createDate("20161031"));
        d.setIdAffiliationWebavs(52561); // 000.6789
        d.setIdPartner(0);
        d.setIdUser(0);

        d.setLastModificationDate(createDate("20161031"));
        d.setMoisDecompte(createDate("20161015"));
        d.setStatut(DecompteStatutEnum.SAISIE);
        d.setType(DecompteTypeEnum.PERIODIQUE);
        d.setVersion(1);

        DecompteAndLignes dl = new DecompteAndLignes();
        dl.setDecompte(d);

        return dl;
    }

    private void addLignesDeDecomptesTest(List<LigneDeDecompteEntity> lignes) {
        LigneDeDecompteEntity lineAVS = new LigneDeDecompteEntity();
        lineAVS.setIdCotisationWebavs(1);
        lineAVS.setTypeCotisationWebavs(812001);
        lineAVS.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(lineAVS);

        LigneDeDecompteEntity lineAF = new LigneDeDecompteEntity();
        lineAF.setIdCotisationWebavs(2);
        lineAF.setTypeCotisationWebavs(812002);
        lineAF.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(lineAF);

        LigneDeDecompteEntity lineAC = new LigneDeDecompteEntity();
        lineAC.setIdCotisationWebavs(3);
        lineAC.setTypeCotisationWebavs(812006);
        lineAC.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(lineAC);

        LigneDeDecompteEntity lineAC2 = new LigneDeDecompteEntity();
        lineAC2.setIdCotisationWebavs(4);
        lineAC2.setTypeCotisationWebavs(812007);
        lineAC2.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(lineAC2);
    }

    private XMLGregorianCalendar createDate(String date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(date).getDate());

        XMLGregorianCalendar xmlDate = null;

        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return xmlDate;
    }

}
