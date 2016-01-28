package globaz.prestation.acor;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 */
public class PRAcorFileContent {

    private List contents = null;
    private String fileName = null;

    public void addLine(String elm) {
        if (contents == null) {
            contents = new LinkedList();
        }
        contents.add(elm);
    }

    public List getContents() {
        return contents;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContents(List contents) {
        this.contents = contents;
    }
}
