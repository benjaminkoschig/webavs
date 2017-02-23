package globaz.osiris.db.yellowreportfile;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CAYellowReportFileServiceTest {

    final CAYellowReportFileService service = new CAYellowReportFileService(null);
    CAYellowReportFile currentReportFile = null;

    final String path = "path/054bvr.xml";
    final CAYellowReportFileType type = CAYellowReportFileType.CAMT054_BVR;

    @Before
    public void setUp() {
        currentReportFile = service.createWithoutDb(path, type);
    }

    @Test
    public void testCreateWithoutDB() throws Exception {

        assertTrue("Given path is no more the same one !", path.equals(currentReportFile.getFileName()));
        assertTrue("Given type is no more the same one !", type.equals(currentReportFile.getType()));

        final CAYellowReportFileState state = CAYellowReportFileState.IDENTIFIED;
        assertTrue("The state of a new report file must be IDENTIFIED", state.equals(currentReportFile.getState()));

        assertNull(currentReportFile.getRemark());
        assertNotNull(currentReportFile.getDateCreated());
    }

    @Test
    public void testChangeStateWithoutDB() {

        CAYellowReportFileState state = CAYellowReportFileState.EXECUTED;

        service.changeStateWithoutDB(currentReportFile, state, null);
        assertTrue("Must be the same state", state.equals(currentReportFile.getState()));

        state = CAYellowReportFileState.IN_TREATMENT;
        service.changeStateWithoutDB(currentReportFile, state, null);
        assertTrue("Must be the same state", state.equals(currentReportFile.getState()));

        state = CAYellowReportFileState.FAILED;
        String message = "Problem during marshalling...";
        service.changeStateWithoutDB(currentReportFile, state, message);
        assertTrue("Must be the same state", state.equals(currentReportFile.getState()));
        assertTrue("Must be the same message", message.equals(currentReportFile.getRemark()));
    }
}
