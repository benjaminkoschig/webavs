/*
 * Generated by JasperReports - 11.07.11 16:23
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
public class NAOS_ATTESTATION_PERSONNELLE extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_P_SUBREPORT_HEADER = null;
    private JRFillParameter parameter_P_TEXTE6 = null;
    private JRFillParameter parameter_P_TEXTE3 = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_P_SIGNATURE = null;
    private JRFillParameter parameter_P_TEXTE2 = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_P_TEXTE1 = null;
    private JRFillParameter parameter_P_TEXTE5 = null;
    private JRFillParameter parameter_P_SUBREPORT_SIGNATURE = null;
    private JRFillParameter parameter_MODEL_PATH = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_P_TEXTE4 = null;


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
        parameter_P_SUBREPORT_HEADER = (JRFillParameter)parsm.get("P_SUBREPORT_HEADER");
        parameter_P_TEXTE6 = (JRFillParameter)parsm.get("P_TEXTE6");
        parameter_P_TEXTE3 = (JRFillParameter)parsm.get("P_TEXTE3");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_P_SIGNATURE = (JRFillParameter)parsm.get("P_SIGNATURE");
        parameter_P_TEXTE2 = (JRFillParameter)parsm.get("P_TEXTE2");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_P_TEXTE1 = (JRFillParameter)parsm.get("P_TEXTE1");
        parameter_P_TEXTE5 = (JRFillParameter)parsm.get("P_TEXTE5");
        parameter_P_SUBREPORT_SIGNATURE = (JRFillParameter)parsm.get("P_SUBREPORT_SIGNATURE");
        parameter_MODEL_PATH = (JRFillParameter)parsm.get("MODEL_PATH");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)parsm.get("REPORT_SCRIPTLET");
        parameter_P_TEXTE4 = (JRFillParameter)parsm.get("P_TEXTE4");


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
            case 37 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 44 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 39 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 40 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 25 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 26 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 36 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 38 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 35 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 46 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE6.getValue()));
                break;
            }
            case 29 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 24 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 41 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 32 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 30 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 34 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE5.getValue()));
                break;
            }
            case 33 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 47 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getValue()).intValue() > 1));
                break;
            }
            case 45 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 28 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 27 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
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
            case 37 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 44 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 39 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 40 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 25 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 26 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 36 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 38 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 35 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 46 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE6.getValue()));
                break;
            }
            case 29 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 24 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 41 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 32 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 30 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 34 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE5.getValue()));
                break;
            }
            case 33 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 47 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()).intValue() > 1));
                break;
            }
            case 45 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 28 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 27 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
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
            case 37 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE2.getValue()));
                break;
            }
            case 44 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE4.getValue()));
                break;
            }
            case 31 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 39 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 40 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 25 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 26 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 36 : // variableInitialValue_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 38 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 35 : // variable_grp1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 46 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE6.getValue()));
                break;
            }
            case 29 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 24 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 41 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 32 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("U:\\Musca\\muscaRoot\\model\\");
                break;
            }
            case 30 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 34 : // group_grp1
            {
                value = (java.lang.Object)(null);
                break;
            }
            case 42 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE5.getValue()));
                break;
            }
            case 33 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 47 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()).intValue() > 1));
                break;
            }
            case 45 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE3.getValue()));
                break;
            }
            case 43 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TEXTE1.getValue()));
                break;
            }
            case 28 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 27 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
