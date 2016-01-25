/*
 * Generated by JasperReports - 13.10.15 13:08
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
public class HeaderCICICAM extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_P_HEADER_TEL_COLLABORATEUR = null;
    private JRFillParameter parameter_P_HEADER_CONFIDENTIEL = null;
    private JRFillParameter parameter_P_HEADER_DATE = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_P_HEADER_ADRESSE = null;
    private JRFillParameter parameter_P_HEADER_RECOMMANDEE = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_P_HEADER_NOM_COLLABORATEUR = null;
    private JRFillParameter parameter_P_HEADER_LIBELLES = null;
    private JRFillParameter parameter_P_HEADER_ADRESSE_CAISSE = null;
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
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_P_HEADER_ADRESSE = (JRFillParameter)parsm.get("P_HEADER_ADRESSE");
        parameter_P_HEADER_RECOMMANDEE = (JRFillParameter)parsm.get("P_HEADER_RECOMMANDEE");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_P_HEADER_NOM_COLLABORATEUR = (JRFillParameter)parsm.get("P_HEADER_NOM_COLLABORATEUR");
        parameter_P_HEADER_LIBELLES = (JRFillParameter)parsm.get("P_HEADER_LIBELLES");
        parameter_P_HEADER_ADRESSE_CAISSE = (JRFillParameter)parsm.get("P_HEADER_ADRESSE_CAISSE");
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
            case 2118 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_NOM_COLLABORATEUR.getValue()));
                break;
            }
            case 2119 : // printWhen_4
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2106 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2105 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2116 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue()));
                break;
            }
            case 2103 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2109 : // parameterDefaultValue_P_HEADER_ADRESSE_CAISSE
            {
                value = (java.lang.String)("Holzikofenweg 36, CH-3003 Bern, Telefon 031-322 64 25, Fax 031-322 88 71 E-mail: ak@26zas.admin.ch, Postkonto 30-478-6");
                break;
            }
            case 2113 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2100 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2120 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_TEL_COLLABORATEUR.getValue()));
                break;
            }
            case 2114 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DATE.getValue()));
                break;
            }
            case 2115 : // printWhen_2
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue())!=null));
                break;
            }
            case 2107 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2117 : // printWhen_3
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2111 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 2101 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2125 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_LIBELLES.getValue()));
                break;
            }
            case 2124 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DONNEES.getValue()));
                break;
            }
            case 2121 : // printWhen_5
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue())!=null));
                break;
            }
            case 2108 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 2122 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue()));
                break;
            }
            case 2104 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2102 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2112 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC�te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 2123 : // textField_6
            {
                value = (java.lang.String)(new String("Confidentiel"));
                break;
            }
            case 2110 : // parameterDefaultValue_P_HEADER_DATE
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
            case 2118 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_NOM_COLLABORATEUR.getValue()));
                break;
            }
            case 2119 : // printWhen_4
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2106 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2105 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2116 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue()));
                break;
            }
            case 2103 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2109 : // parameterDefaultValue_P_HEADER_ADRESSE_CAISSE
            {
                value = (java.lang.String)("Holzikofenweg 36, CH-3003 Bern, Telefon 031-322 64 25, Fax 031-322 88 71 E-mail: ak@26zas.admin.ch, Postkonto 30-478-6");
                break;
            }
            case 2113 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2100 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2120 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_TEL_COLLABORATEUR.getValue()));
                break;
            }
            case 2114 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DATE.getValue()));
                break;
            }
            case 2115 : // printWhen_2
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue())!=null));
                break;
            }
            case 2107 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2117 : // printWhen_3
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2111 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 2101 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2125 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_LIBELLES.getValue()));
                break;
            }
            case 2124 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DONNEES.getValue()));
                break;
            }
            case 2121 : // printWhen_5
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue())!=null));
                break;
            }
            case 2108 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 2122 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue()));
                break;
            }
            case 2104 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2102 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2112 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC�te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 2123 : // textField_6
            {
                value = (java.lang.String)(new String("Confidentiel"));
                break;
            }
            case 2110 : // parameterDefaultValue_P_HEADER_DATE
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
            case 2118 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_NOM_COLLABORATEUR.getValue()));
                break;
            }
            case 2119 : // printWhen_4
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2106 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2105 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2116 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue()));
                break;
            }
            case 2103 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2109 : // parameterDefaultValue_P_HEADER_ADRESSE_CAISSE
            {
                value = (java.lang.String)("Holzikofenweg 36, CH-3003 Bern, Telefon 031-322 64 25, Fax 031-322 88 71 E-mail: ak@26zas.admin.ch, Postkonto 30-478-6");
                break;
            }
            case 2113 : // printWhen_1
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2100 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2120 : // textField_4
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_TEL_COLLABORATEUR.getValue()));
                break;
            }
            case 2114 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DATE.getValue()));
                break;
            }
            case 2115 : // printWhen_2
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_ADRESSE.getValue())!=null));
                break;
            }
            case 2107 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 2117 : // printWhen_3
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_DATE.getValue())!=null));
                break;
            }
            case 2111 : // parameterDefaultValue_P_HEADER_NOM_COLLABORATEUR
            {
                value = (java.lang.String)("");
                break;
            }
            case 2101 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2125 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_LIBELLES.getValue()));
                break;
            }
            case 2124 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_DONNEES.getValue()));
                break;
            }
            case 2121 : // printWhen_5
            {
                value = (java.lang.Boolean)(new Boolean(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue())!=null));
                break;
            }
            case 2108 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("..//defaultModel");
                break;
            }
            case 2122 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_HEADER_RECOMMANDEE.getValue()));
                break;
            }
            case 2104 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2102 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 2112 : // parameterDefaultValue_P_HEADER_ADRESSE
            {
                value = (java.lang.String)("Herr Globaz\nC�te 1 a\n2340 Le Noirmont\nSwitzerland");
                break;
            }
            case 2123 : // textField_6
            {
                value = (java.lang.String)(new String("Confidentiel"));
                break;
            }
            case 2110 : // parameterDefaultValue_P_HEADER_DATE
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
