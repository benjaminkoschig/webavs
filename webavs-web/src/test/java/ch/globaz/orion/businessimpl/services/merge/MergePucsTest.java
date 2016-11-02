package ch.globaz.orion.businessimpl.services.merge;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;

public class MergePucsTest {

    @Ignore
    @Test
    public void testMerge() throws Exception {
        String workDir = "testsresources\\ch\\globaz\\orion\\businessimpl\\services\\merge\\";
        MergePucs mergePucs = new MergePucs(workDir);

        List<PucsFile> files = new ArrayList<PucsFile>();
        PucsFile pucsFile1 = new PucsFile();
        pucsFile1.setFilename("testOne");
        pucsFile1.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        PucsFile pucsFile2 = new PucsFile();
        pucsFile2.setFilename("testTwo");
        pucsFile2.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        files.add(pucsFile1);
        files.add(pucsFile2);

        Document document = mergePucs.mergeForAffilie(files, null).getDocument();
        String filePath = mergePucs.out("1078865-10", document);

        ElementsDomParser parser = new ElementsDomParser(filePath);
        assertEquals(3, parser.find("Person").getResult().get(0).getLength());
        assertEquals("3", parser.find("NumberOf-AHV-AVS-Salary-Tags").getFirstValue());
    }

    @Ignore
    @Test
    public void testMergeAfSeul() throws Exception {
        String workDir = "testsresources\\ch\\globaz\\orion\\businessimpl\\services\\merge\\";
        MergePucs mergePucs = new MergePucs(workDir);

        List<PucsFile> files = new ArrayList<PucsFile>();
        PucsFile pucsFile1 = new PucsFile();
        pucsFile1.setFilename("testOneAfSeul");
        pucsFile1.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        PucsFile pucsFile2 = new PucsFile();
        pucsFile2.setFilename("testOneTwoAfSeul");
        pucsFile2.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        files.add(pucsFile1);
        files.add(pucsFile2);

        Document document = mergePucs.mergeForAffilie(files, null).getDocument();
        String filePath = mergePucs.out("1078865-10-Af_Seul", document);
        ElementsDomParser parser = new ElementsDomParser(filePath);
        assertEquals(2, parser.find("Person").getResult().get(0).getLength());
        assertEquals("65000.00", parser.find("Total-FAK-CAF-ContributorySalary").getFirstValue());
        assertEquals("2", parser.find("NumberOf-FAK-CAF-Salary-Tags").getFirstValue());
        assertEquals("3820.00", parser.find("Total-FAK-CAF-FamilyIncomeSupplement").getFirstValue());
    }

    // @Test
    // public void testMergeAfSeul2() throws Exception {
    // String workDir = "testsresources\\ch\\globaz\\orion\\businessimpl\\services\\merge\\";
    // MergePucs mergePucs = new MergePucs(workDir);
    //
    // List<PucsFile> files = new ArrayList<PucsFile>();
    // PucsFile pucsFile1 = new PucsFile();
    // pucsFile1.setId("AF_SEUL_1");
    // pucsFile1.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
    // PucsFile pucsFile2 = new PucsFile();
    // pucsFile2.setId("AF_SEUL_2");
    // pucsFile2.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
    // files.add(pucsFile1);
    // files.add(pucsFile2);
    //
    // Document document = mergePucs.mergeForAffilie(files);
    // String filePath = mergePucs.out("1078865-10-Af_Seul", document);
    //
    // }
    @Ignore
    @Test
    public void testIsSameProvenance() throws Exception {
        List<PucsFile> files = new ArrayList<PucsFile>();
        PucsFile pucsFile1 = new PucsFile();
        pucsFile1.setFilename("testOne");
        pucsFile1.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        PucsFile pucsFile2 = new PucsFile();
        pucsFile2.setFilename("testTwo");
        pucsFile2.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        files.add(pucsFile1);
        files.add(pucsFile2);

        assertTrue(MergePucs.isSameProvenance(files));

        PucsFile pucsFile3 = new PucsFile();
        pucsFile3.setFilename("testTwo");
        pucsFile3.setProvenance(DeclarationSalaireProvenance.DAN);
        files.add(pucsFile3);

        assertFalse(MergePucs.isSameProvenance(files));
    }

}
