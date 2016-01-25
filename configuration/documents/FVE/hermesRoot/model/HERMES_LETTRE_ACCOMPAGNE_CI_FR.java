/*
 * Generated by JasperReports - 06.09.13 10:57
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
public class HERMES_LETTRE_ACCOMPAGNE_CI_FR extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_P_SUBREPORT_HEADER = null;
    private JRFillParameter parameter_P_TITRE = null;
    private JRFillParameter parameter_P_ANNEXES = null;
    private JRFillParameter parameter_P_CORPS = null;
    private JRFillParameter parameter_P_SUBREPORT_SIGNATURE = null;
    private JRFillParameter parameter_P_DEFAULT_MODEL_PATH = null;
    private JRFillParameter parameter_P_POLITESSE = null;
    private JRFillParameter parameter_P_SIGN_LETTRE = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;


    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        ) throws JRException
    {
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_P_SUBREPORT_HEADER = (JRFillParameter)parsm.get("P_SUBREPORT_HEADER");
        parameter_P_TITRE = (JRFillParameter)parsm.get("P_TITRE");
        parameter_P_ANNEXES = (JRFillParameter)parsm.get("P_ANNEXES");
        parameter_P_CORPS = (JRFillParameter)parsm.get("P_CORPS");
        parameter_P_SUBREPORT_SIGNATURE = (JRFillParameter)parsm.get("P_SUBREPORT_SIGNATURE");
        parameter_P_DEFAULT_MODEL_PATH = (JRFillParameter)parsm.get("P_DEFAULT_MODEL_PATH");
        parameter_P_POLITESSE = (JRFillParameter)parsm.get("P_POLITESSE");
        parameter_P_SIGN_LETTRE = (JRFillParameter)parsm.get("P_SIGN_LETTRE");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)parsm.get("REPORT_SCRIPTLET");


        variable_PAGE_NUMBER = (JRFillVariable)varsm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)varsm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)varsm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)varsm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)varsm.get("COLUMN_COUNT");
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
            case 241 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SIGN_LETTRE.getValue()));
                break;
            }
            case 242 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ANNEXES.getValue()));
                break;
            }
            case 236 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 238 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_POLITESSE.getValue()));
                break;
            }
            case 227 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 243 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 234 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("Signature_CO2.jasper");
                break;
            }
            case 225 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 232 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/contentieux/workspace/webavs/WebContent/defaultModel");
                break;
            }
            case 229 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 235 : // parameterDefaultValue_P_ANNEXES
            {
                value = (java.lang.String)("");
                break;
            }
            case 224 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 226 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 223 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 230 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 228 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 233 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse.jasper");
                break;
            }
            case 239 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_CORPS.getValue()));
                break;
            }
            case 240 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TITRE.getValue()));
                break;
            }
            case 237 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 231 : // parameterDefaultValue_P_TITRE
            {
                value = (java.lang.String)("???");
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
            case 241 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SIGN_LETTRE.getValue()));
                break;
            }
            case 242 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ANNEXES.getValue()));
                break;
            }
            case 236 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 238 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_POLITESSE.getValue()));
                break;
            }
            case 227 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 243 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 234 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("Signature_CO2.jasper");
                break;
            }
            case 225 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 232 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/contentieux/workspace/webavs/WebContent/defaultModel");
                break;
            }
            case 229 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 235 : // parameterDefaultValue_P_ANNEXES
            {
                value = (java.lang.String)("");
                break;
            }
            case 224 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 226 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 223 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 230 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 228 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 233 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse.jasper");
                break;
            }
            case 239 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_CORPS.getValue()));
                break;
            }
            case 240 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TITRE.getValue()));
                break;
            }
            case 237 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 231 : // parameterDefaultValue_P_TITRE
            {
                value = (java.lang.String)("???");
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
            case 241 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SIGN_LETTRE.getValue()));
                break;
            }
            case 242 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ANNEXES.getValue()));
                break;
            }
            case 236 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 238 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_POLITESSE.getValue()));
                break;
            }
            case 227 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 243 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/AVS_AI.png");
                break;
            }
            case 234 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("Signature_CO2.jasper");
                break;
            }
            case 225 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 232 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/contentieux/workspace/webavs/WebContent/defaultModel");
                break;
            }
            case 229 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 235 : // parameterDefaultValue_P_ANNEXES
            {
                value = (java.lang.String)("");
                break;
            }
            case 224 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 226 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 223 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 230 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 228 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 233 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse.jasper");
                break;
            }
            case 239 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_CORPS.getValue()));
                break;
            }
            case 240 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TITRE.getValue()));
                break;
            }
            case 237 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 231 : // parameterDefaultValue_P_TITRE
            {
                value = (java.lang.String)("???");
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
