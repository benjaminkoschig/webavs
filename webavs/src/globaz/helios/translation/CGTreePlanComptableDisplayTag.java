package globaz.helios.translation;

import javax.servlet.jsp.JspWriter;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 6 oct. 04
 * 
 * @author scr
 * 
 */
public class CGTreePlanComptableDisplayTag extends CGTreeDisplayTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CGTreePlanComptableDisplayTag.
     */
    public CGTreePlanComptableDisplayTag() {
        super();
    }

    @Override
    public void drawTag() throws java.io.IOException {

        JspWriter out = pageContext.getOut();
        out.write("<SCRIPT language=\"JavaScript\" src=\"../heliosRoot/treeSelection.js\"></SCRIPT>\n");
        out.write("<SCRIPT language=\"JavaScript\">\n");
        out.write("checkboxBaseId = \"" + getName() + "\";\n");
        String[] temp = new String[2];
        temp[0] = getRootValue();
        temp[1] = getRootLabel();
        getData().setValue(temp);
        if (isExportValues()) {
            out.write("exportValues = true;\n");
        }
        if (!getExportValuesInputName().equals("")) {
            out.write("exportValuesHTMLElement = " + getExportValuesInputName() + ";\n");
        }
        if (!getExpandLevel().equals("")) {
            out.write("expandLevel = " + getExpandLevel() + ";\n");
        }
        recurseDrawTreeTag("", null, getData(), out);

        out.write("displayMainTreePlanComptable(tree, 0, " + getDefaut() + ");\n");
        out.write("</SCRIPT>\n");
    }

}
