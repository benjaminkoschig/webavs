package ch.globaz.orion.business.models.pucs;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PucsFileMergeTest {

    @Test
    public void testGetIdsPucsFileSeparteByCommaOneValue() throws Exception {
        List<PucsFile> files = new ArrayList<PucsFile>();
        PucsFile pucsFile1 = new PucsFile();
        pucsFile1.setFilename("testOne");
        files.add(pucsFile1);

        PucsFileMerge merger = new PucsFileMerge(files, "");
        assertEquals("testOne", merger.getIdsPucsFileSeparteByComma());
    }

    @Test
    public void testGetIdsPucsFileSeparteByComma() throws Exception {
        List<PucsFile> files = new ArrayList<PucsFile>();
        PucsFile pucsFile1 = new PucsFile();
        pucsFile1.setFilename("testOne");
        PucsFile pucsFile2 = new PucsFile();
        pucsFile2.setFilename("testTwo");
        files.add(pucsFile1);
        files.add(pucsFile2);
        files.add(null);

        PucsFileMerge merger = new PucsFileMerge(files, "");
        assertEquals("testOne;testTwo", merger.getIdsPucsFileSeparteByComma());
    }

}
