package ch.globaz.al.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ALEntityFieldChangeAnalyserTest {

    @Test
    public void hasDateChanged() {
        // Contr�le sur des valeurs identiques
        assertFalse("", ALEntityFieldChangeAnalyser.hasValueChanged(null, null));
        assertFalse("", ALEntityFieldChangeAnalyser.hasValueChanged("", ""));
        assertFalse("", ALEntityFieldChangeAnalyser.hasValueChanged("12.03.2016", "12.03.2016"));
        assertFalse("", ALEntityFieldChangeAnalyser.hasValueChanged("20160514", "20160514"));

        // Contr�le sur des valeurs diff�rentes
        assertTrue("", ALEntityFieldChangeAnalyser.hasValueChanged(null, ""));
        assertTrue("", ALEntityFieldChangeAnalyser.hasValueChanged("", null));
        assertTrue("", ALEntityFieldChangeAnalyser.hasValueChanged("12.03.2016", "13.03.2016"));
        assertTrue("", ALEntityFieldChangeAnalyser.hasValueChanged("20160514", "20160513"));
    }
}
