/*
 * Generated by JasperReports - 26.02.15 13:41
 */
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.*;

import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import java.net.*;


/**
 *
 */
public class DRACO_LETTRE_RECLAMATION extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_P_COL3 = null;
    private JRFillParameter parameter_P_SUBREPORT_HEADER = null;
    private JRFillParameter parameter_P_TEXTE3 = null;
    private JRFillParameter parameter_P_COL1 = null;
    private JRFillParameter parameter_P_COL2 = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_P_SIGNATURE = null;
    private JRFillParameter parameter_P_TEXTE2 = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_P_TEXTE1 = null;
    private JRFillParameter parameter_P_COL4 = null;
    private JRFillParameter parameter_P_DEFAULT_MODEL_PATH = null;
    private JRFillParameter parameter_P_SUBREPORT_SIGNATURE = null;
    private JRFillParameter parameter_SHOWDETAIL = null;
    private JRFillParameter parameter_MODEL_PATH = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_P_TEXTE4 = null;

    private JRFillField field_COL2 = null;
    private JRFillField field_COL1 = null;
    private JRFillField field_COL3 = null;
    private JRFillField field_COL4 = null;

    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;
    private JRFillVariable variable_IMAGE_LOGO = null;
    private JRFillVariable variable_grp1_COUNT = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        ) throws JRException
    {
        parameter_P_COL3 = (JRFillParameter)parsm.get("P_COL3");
        parameter_P_SUBREPORT_HEADER = (JRFillParameter)parsm.get("P_SUBREPORT_HEADER");
        parameter_P_TEXTE3 = (JRFillParameter)parsm.get("P_TEXTE3");
        parameter_P_COL1 = (JRFillParameter)parsm.get("P_COL1");
        parameter_P_COL2 = (JRFillParameter)parsm.get("P_COL2");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_P_SIGNATURE = (JRFillParameter)parsm.get("P_SIGNATURE");
        parameter_P_TEXTE2 = (JRFillParameter)parsm.get("P_TEXTE2");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_P_TEXTE1 = (JRFillParameter)parsm.get("P_TEXTE1");
        parameter_P_COL4 = (JRFillParameter)parsm.get("P_COL4");
        parameter_P_DEFAULT_MODEL_PATH = (JRFillParameter)parsm.get("P_DEFAULT_MODEL_PATH");
        parameter_P_SUBREPORT_SIGNATURE = (JRFillParameter)parsm.get("P_SUBREPORT_SIGNATURE");
        parameter_SHOWDETAIL = (JRFillParameter)parsm.get("SHOWDETAIL");
        parameter_MODEL_PATH = (JRFillParameter)parsm.get("MODEL_PATH");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)parsm.get("REPORT_SCRIPTLET");
        parameter_P_TEXTE4 = (JRFillParameter)parsm.get("P_TEXTE4");

        field_COL2 = (JRFillField)fldsm.get("COL2");
        field_COL1 = (JRFillField)fldsm.get("COL1");
        field_COL3 = (JRFillField)fldsm.get("COL3");
        field_COL4 = (JRFillField)fldsm.get("COL4");

        variable_PAGE_NUMBER = (JRFillVariable)varsm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)varsm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)varsm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)varsm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)varsm.get("COLUMN_COUNT");
        variable_IMAGE_LOGO = (JRFillVariable)varsm.get("IMAGE_LOGO");
        variable_grp1_COUNT = (JRFillVariable)varsm.get("grp1_COUNT");
    }


    /**
     * Test method
     */
    public static void helloJasper()
    {
        System.out.println("------------------------------");
        System.out.println(" Hello, Jasper!...");
        System.out.println("------------------------------");
    }


    /**
     *
     */
    public Object evaluate(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 38 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 45 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 37 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 46 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 39 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 34 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 35 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 47 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 32 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 55 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)field_COL1.getValue()));
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 49 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 48 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 56 : // textField_9
            {
                value = (java.lang.String)(((java.lang.String)field_COL2.getValue()));
                break;
            }
            case 54 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL3.getValue()));
                break;
            }
            case 40 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 57 : // textField_10
            {
                value = (java.lang.String)(((java.lang.String)field_COL3.getValue()));
                break;
            }
            case 36 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 53 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL2.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 41 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 44 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 30 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 51 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue() > 1));
                break;
            }
            case 33 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 52 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL1.getValue()));
                break;
            }
            case 50 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 58 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateOld(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 38 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 45 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 37 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 46 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 39 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 34 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 35 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 47 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 32 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 55 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)field_COL1.getOldValue()));
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 49 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 48 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 56 : // textField_9
            {
                value = (java.lang.String)(((java.lang.String)field_COL2.getOldValue()));
                break;
            }
            case 54 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL3.getValue()));
                break;
            }
            case 40 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 57 : // textField_10
            {
                value = (java.lang.String)(((java.lang.String)field_COL3.getOldValue()));
                break;
            }
            case 36 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 53 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL2.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 41 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 44 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 30 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 51 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue() > 1));
                break;
            }
            case 33 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 52 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL1.getValue()));
                break;
            }
            case 50 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 58 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateEstimated(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 38 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 45 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 37 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 46 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 39 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 34 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 35 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 47 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 32 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 55 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)field_COL1.getValue()));
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 49 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 48 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 56 : // textField_9
            {
                value = (java.lang.String)(((java.lang.String)field_COL2.getValue()));
                break;
            }
            case 54 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL3.getValue()));
                break;
            }
            case 40 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 57 : // textField_10
            {
                value = (java.lang.String)(((java.lang.String)field_COL3.getValue()));
                break;
            }
            case 36 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 53 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL2.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 41 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 44 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 30 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 51 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue() > 1));
                break;
            }
            case 33 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 52 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_COL1.getValue()));
                break;
            }
            case 50 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 58 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
