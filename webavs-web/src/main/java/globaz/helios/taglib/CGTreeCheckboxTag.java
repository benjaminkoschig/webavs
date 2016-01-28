package globaz.helios.taglib;

import globaz.fweb.taglib.FWTreeCheckboxTag;
import globaz.globall.util.JATreeNode;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.jsp.JspWriter;

public class CGTreeCheckboxTag extends FWTreeCheckboxTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int countId = 0;

    private void displayTree(JATreeNode tree, int level, JspWriter out, String suffix, String lastSuffix)
            throws Exception {
        String treeJsName = "";
        if (tree.isLeaf()) {
            treeJsName = "tree" + lastSuffix;
        } else {
            treeJsName = "tree" + suffix;
        }

        if (level == 0) {
            out.write("<SCRIPT language=\"JavaScript\">maxTreeDepth = getLevelDepthTree(" + treeJsName
                    + ");</SCRIPT>\n");
            out.write("<TABLE frames=\"void\" rules=\"rows\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">");
        }

        String id = "treeItem" + countId;
        countId++;
        String libelle = ((String[]) tree.getValue())[1];

        if ((getExpandLevelAsInt() != 0) && (level > getExpandLevelAsInt())) {
            out.write("<SCRIPT language=\"JavaScript\">\n" + treeJsName + ".expanded = false;\n");

            if (level != 0) {
                out.write(treeJsName + ".parent.expanded = false;\n");
            }

            out.write("</SCRIPT>\n");
        }

        out.write("<tr id=\"" + id + "\"");

        if (tree.getChildrenCount() == 0) {
            out.write(" class=\"TreeChild\"");
        } else {
            out.write(" class=\"TreeNode" + level + "\"");
        }

        if ((getExpandLevelAsInt() != 0) && (level > getExpandLevelAsInt())) {
            out.write(" style=\"display:none\"");
        }

        out.write(">\n");
        int i = 0;
        out.write("<td>");

        for (i = 0; i < level; i++) {
            out.write("&nbsp;&nbsp;&nbsp;");
        }

        if (tree.getChildrenCount() > 0) {
            out.write("+&nbsp;");
        } else {
            out.write("&nbsp;&nbsp;&nbsp;");
        }

        out.write("<a");
        if (tree.getChildrenCount() > 0) {
            out.write(" class=\"TreeNode\" href=\"javascript:processTree(" + treeJsName + ",'" + id + "');\"");
        } else {
            out.write(" class=\"TreeChild\"");
        }

        out.write(">" + libelle + "</a></td>\n");
        out.write("<td align=\"right\"><input type=\"checkbox\" id=\"" + getName() + id + "\" name=\"" + getName() + id
                + "\" value=\"" + ((String[]) tree.getValue())[0] + "\"");
        if (isExportValues()) {
            if (tree.getChildrenCount() > 0) {
                out.write(" onClick=\"selectTree(" + treeJsName + ",'" + id
                        + "');exportValuesTree(tree);return true;\"");
            } else {
                out.write(" onClick=\"exportValuesTree(tree);return true;\"");
            }
        } else if (tree.getChildrenCount() > 0) {
            out.write(" onClick=\"selectTree(" + treeJsName + ",'" + id + "');return true;\"");
        }

        out.write(" CHECKED");
        out.write("></td>\n");
        out.write("</tr>\n");

        if (tree.getChildrenCount() > 0) {
            for (i = 0; i < tree.getChildrenCount(); i++) {
                displayTree(tree.getChildAt(i), level + 1, out, suffix + "_" + i, suffix);
            }
        }

        if (level == 0) {
            out.write("</table>\n");

            if (isExportValues()) {
                out.write("<SCRIPT language=\"JavaScript\">exportValuesTree(tree);</SCRIPT>\n");
            }
        }
    }

    @Override
    public void drawTag() throws java.io.IOException {
        String[] temp = new String[2];
        temp[0] = getRootValue();
        temp[1] = getRootLabel();
        getData().setValue(temp);

        countId = 0;

        JspWriter out = pageContext.getOut();
        out.write("<SCRIPT language=\"JavaScript\" src=\"../scripts/tree.js\"></SCRIPT>\n");
        out.write("<SCRIPT language=\"JavaScript\">\n");

        out.write("checkboxBaseId = \"" + getName() + "\";\n");

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

        out.write("</SCRIPT>\n");

        try {
            displayTree(getData(), 0, out, "", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getExpandLevelAsInt() {
        if (!JadeStringUtil.isBlank(getExpandLevel())) {
            return Integer.parseInt(getExpandLevel());
        } else {
            return 0;
        }
    }

    private void recurseDrawTreeTag(String suffix, String lastSuffix, globaz.globall.util.JATreeNode tree, JspWriter out)
            throws java.io.IOException {
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

}
