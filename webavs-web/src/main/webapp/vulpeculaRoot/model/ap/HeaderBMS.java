/*
 * Generated by JasperReports - 26.09.17 11:33
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
public class HeaderBMS extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_P_HEADER_TEL_COLLABORATEUR = null;
    private JRFillParameter parameter_P_HEADER_CONFIDENTIEL = null;
    private JRFillParameter parameter_P_HEADER_DATE = null;
    private JRFillParameter parameter_P_ASSOCIATION = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_P_HEADER_ADRESSE = null;
    private JRFillParameter parameter_P_HEADER_RECOMMANDEE = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_P_HEADER_NOM_COLLABORATEUR = null;
    private JRFillParameter parameter_P_SIGNATURE_SERVICE = null;
    private JRFillParameter parameter_P_HEADER_LIBELLES = null;
    private JRFillParameter parameter_P_DEFAULT_MODEL_PATH = null;
    private JRFillParameter parameter_P_HEADER_EMAIL_COLLABORATEUR = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_P_HEADER_DONNEES = null;


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
        parameter_P_HEADER_TEL_COLLABORATEUR = (JRFillParameter)parsm.get("P_HEADER_TEL_COLLABORATEUR");
        parameter_P_HEADER_CONFIDENTIEL = (JRFillParameter)parsm.get("P_HEADER_CONFIDENTIEL");
        parameter_P_HEADER_DATE = (JRFillParameter)parsm.get("P_HEADER_DATE");
        parameter_P_ASSOCIATION = (JRFillParameter)parsm.get("P_ASSOCIATION");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_P_HEADER_ADRESSE = (JRFillParameter)parsm.get("P_HEADER_ADRESSE");
        parameter_P_HEADER_RECOMMANDEE = (JRFillParameter)parsm.get("P_HEADER_RECOMMANDEE");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_P_HEADER_NOM_COLLABORATEUR = (JRFillParameter)parsm.get("P_HEADER_NOM_COLLABORATEUR");
        parameter_P_SIGNATURE_SERVICE = (JRFillParameter)parsm.get("P_SIGNATURE_SERVICE");
        parameter_P_HEADER_LIBELLES = (JRFillParameter)parsm.get("P_HEADER_LIBELLES");
        parameter_P_DEFAULT_MODEL_PATH = (JRFillParameter)parsm.get("P_DEFAULT_MODEL_PATH");
        parameter_P_HEADER_EMAIL_COLLABORATEUR = (JRFillParameter)parsm.get("P_HEADER_EMAIL_COLLABORATEUR");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)parsm.get("REPORT_SCRIPTLET");
        parameter_P_HEADER_DONNEES = (JRFillParameter)parsm.get("P_HEADER_DONNEES");


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
            case 128 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 131 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 132 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 127 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 133 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 130 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 139 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ASSOCIATION.getValue()));
                break;
            }
            case 137 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC?te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 129 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 138 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/bms_aven_logo.jpg");
                break;
            }
            case 134 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 136 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 126 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 135 : // parameterDefaultValue_P_HEADER_DATE
            {
                value = (java.lang.String)("Le Noirmont, le 10.02.2005");
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
            case 128 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 131 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 132 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 127 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 133 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 130 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 139 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ASSOCIATION.getValue()));
                break;
            }
            case 137 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC?te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 129 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 138 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/bms_aven_logo.jpg");
                break;
            }
            case 134 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 136 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 126 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 135 : // parameterDefaultValue_P_HEADER_DATE
            {
                value = (java.lang.String)("Le Noirmont, le 10.02.2005");
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
            case 128 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 131 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 132 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 127 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 133 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 130 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 139 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_ASSOCIATION.getValue()));
                break;
            }
            case 137 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC?te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 129 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 138 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/bms_aven_logo.jpg");
                break;
            }
            case 134 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 136 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 126 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 135 : // parameterDefaultValue_P_HEADER_DATE
            {
                value = (java.lang.String)("Le Noirmont, le 10.02.2005");
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
