package globaz.helios.translation;

import globaz.fweb.taglib.FWTree;
import javax.servlet.jsp.JspWriter;

/**
 * Insérez la description du type ici. Date de création : (15.01.2003 15:38:49)
 * 
 * @author: Administrator
 */
public class CGTreeDisplayTag extends FWTree {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean displayRoot = true;
    private java.lang.String expandLevel = "";
    private boolean exportValues = false;
    private java.lang.String exportValuesInputName = "";
    private java.lang.String rootLabel = "All";
    private java.lang.String rootValue = "All";
    private boolean useSubtreeSelection = true;

    /**
     * Commentaire relatif au constructeur FWTreeCheckboxTag.
     */
    public CGTreeDisplayTag() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 11:05:22)
     */
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

        out.write("displayMainTree(tree, 0, " + getDefaut() + ");\n");
        out.write("</SCRIPT>\n");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:23:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getExpandLevel() {
        return expandLevel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getExportValuesInputName() {
        return exportValuesInputName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRootLabel() {
        return rootLabel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRootValue() {
        return rootValue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return boolean
     */
    public boolean isDisplayRoot() {
        return displayRoot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return boolean
     */
    public boolean isExportValues() {
        return exportValues;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @return boolean
     */
    public boolean isUseSubtreeSelection() {
        return useSubtreeSelection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 08:53:37)
     * 
     * @param tree
     *            globaz.globall.util.JATreeNode
     * @param out
     *            javax.servlet.jsp.JspWriter
     */
    protected void recurseDrawTreeTag(String suffix, String lastSuffix, globaz.globall.util.JATreeNode tree,
            JspWriter out) throws java.io.IOException {
        if (tree.isLeaf()) {
            out.write("tree" + lastSuffix + ".addItem(\"" + ((String[]) tree.getValue())[1] + "\", \""
                    + ((String[]) tree.getValue())[0] + "\");\n");
        } else {
            out.write("var tree" + suffix + " = new TreeItem(\"" + ((String[]) tree.getValue())[1] + "\",\""
                    + ((String[]) tree.getValue())[0] + "\");\n");
            for (int i = 0; i < tree.getChildrenCount(); i++) {
                recurseDrawTreeTag(suffix + "_" + i, suffix, tree.getChildAt(i), out);
            }
            if (lastSuffix != null) {
                out.write("tree" + lastSuffix + ".addItem(tree" + suffix + ");\n");
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newDisplayRoot
     *            boolean
     */
    public void setDisplayRoot(boolean newDisplayRoot) {
        displayRoot = newDisplayRoot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:23:04)
     * 
     * @param newExpandLevel
     *            java.lang.String
     */
    public void setExpandLevel(java.lang.String newExpandLevel) {
        expandLevel = newExpandLevel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newExportValues
     *            boolean
     */
    public void setExportValues(boolean newExportValues) {
        exportValues = newExportValues;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newExportValuesInputName
     *            java.lang.String
     */
    public void setExportValuesInputName(java.lang.String newExportValuesInputName) {
        exportValuesInputName = newExportValuesInputName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newRootLabel
     *            java.lang.String
     */
    public void setRootLabel(java.lang.String newRootLabel) {
        rootLabel = newRootLabel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newRootValue
     *            java.lang.String
     */
    public void setRootValue(java.lang.String newRootValue) {
        rootValue = newRootValue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.01.2003 12:40:53)
     * 
     * @param newUseSubtreeSelection
     *            boolean
     */
    public void setUseSubtreeSelection(boolean newUseSubtreeSelection) {
        useSubtreeSelection = newUseSubtreeSelection;
    }

}
