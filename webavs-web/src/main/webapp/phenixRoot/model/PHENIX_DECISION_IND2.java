/*
 * Generated by JasperReports - 16.07.18 14:30
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
public class PHENIX_DECISION_IND2 extends JRCalculator
{


    /**
     *
     */
    private JRFillParameter parameter_P_DUPLICATA_FR = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_P_TYPEETPERIODE = null;
    private JRFillParameter parameter_P_REMARQUE = null;
    private JRFillParameter parameter_L_FRANC_COL1 = null;
    private JRFillParameter parameter_L_FRANC_TOT2 = null;
    private JRFillParameter parameter_L_FRANC_TOT1 = null;
    private JRFillParameter parameter_L_PERIODE = null;
    private JRFillParameter parameter_L_FRANC_COL2 = null;
    private JRFillParameter parameter_P_SUBREPORT_SIGNATURE = null;
    private JRFillParameter parameter_L_TOTAL = null;
    private JRFillParameter parameter_P_DEFAULT_MODEL_PATH = null;
    private JRFillParameter parameter_L_MONTANT = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_L_COTISATION = null;
    private JRFillParameter parameter_L_FRANC_DIFFERENCE = null;
    private JRFillParameter parameter_P_SUBREPORT_HEADER = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_P_PARA1 = null;
    private JRFillParameter parameter_MODEL_PATH = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_L_DIFFERENCE = null;
    private JRFillParameter parameter_P_DUPLICATA_DE = null;
    private JRFillParameter parameter_P_SOURCE = null;

    private JRFillField field_COTI_FACTUREE = null;
    private JRFillField field_COTI_ANNUELLE = null;
    private JRFillField field_LIBELLE_COTI = null;

    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;
    private JRFillVariable variable_IMAGE_LOGO = null;
    private JRFillVariable variable_TOTAL_ANNUEL = null;
    private JRFillVariable variable_TOTAL_FACTURE = null;
    private JRFillVariable variable_DIFFERENCE = null;
    private JRFillVariable variable_GRP_1_COUNT = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        ) throws JRException
    {
        parameter_P_DUPLICATA_FR = (JRFillParameter)parsm.get("P_DUPLICATA_FR");
        parameter_REPORT_CONNECTION = (JRFillParameter)parsm.get("REPORT_CONNECTION");
        parameter_P_TYPEETPERIODE = (JRFillParameter)parsm.get("P_TYPEETPERIODE");
        parameter_P_REMARQUE = (JRFillParameter)parsm.get("P_REMARQUE");
        parameter_L_FRANC_COL1 = (JRFillParameter)parsm.get("L_FRANC_COL1");
        parameter_L_FRANC_TOT2 = (JRFillParameter)parsm.get("L_FRANC_TOT2");
        parameter_L_FRANC_TOT1 = (JRFillParameter)parsm.get("L_FRANC_TOT1");
        parameter_L_PERIODE = (JRFillParameter)parsm.get("L_PERIODE");
        parameter_L_FRANC_COL2 = (JRFillParameter)parsm.get("L_FRANC_COL2");
        parameter_P_SUBREPORT_SIGNATURE = (JRFillParameter)parsm.get("P_SUBREPORT_SIGNATURE");
        parameter_L_TOTAL = (JRFillParameter)parsm.get("L_TOTAL");
        parameter_P_DEFAULT_MODEL_PATH = (JRFillParameter)parsm.get("P_DEFAULT_MODEL_PATH");
        parameter_L_MONTANT = (JRFillParameter)parsm.get("L_MONTANT");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)parsm.get("REPORT_SCRIPTLET");
        parameter_L_COTISATION = (JRFillParameter)parsm.get("L_COTISATION");
        parameter_L_FRANC_DIFFERENCE = (JRFillParameter)parsm.get("L_FRANC_DIFFERENCE");
        parameter_P_SUBREPORT_HEADER = (JRFillParameter)parsm.get("P_SUBREPORT_HEADER");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)parsm.get("REPORT_PARAMETERS_MAP");
        parameter_P_PARA1 = (JRFillParameter)parsm.get("P_PARA1");
        parameter_MODEL_PATH = (JRFillParameter)parsm.get("MODEL_PATH");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)parsm.get("REPORT_DATA_SOURCE");
        parameter_L_DIFFERENCE = (JRFillParameter)parsm.get("L_DIFFERENCE");
        parameter_P_DUPLICATA_DE = (JRFillParameter)parsm.get("P_DUPLICATA_DE");
        parameter_P_SOURCE = (JRFillParameter)parsm.get("P_SOURCE");

        field_COTI_FACTUREE = (JRFillField)fldsm.get("COTI_FACTUREE");
        field_COTI_ANNUELLE = (JRFillField)fldsm.get("COTI_ANNUELLE");
        field_LIBELLE_COTI = (JRFillField)fldsm.get("LIBELLE_COTI");

        variable_PAGE_NUMBER = (JRFillVariable)varsm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)varsm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)varsm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)varsm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)varsm.get("COLUMN_COUNT");
        variable_IMAGE_LOGO = (JRFillVariable)varsm.get("IMAGE_LOGO");
        variable_TOTAL_ANNUEL = (JRFillVariable)varsm.get("TOTAL_ANNUEL");
        variable_TOTAL_FACTURE = (JRFillVariable)varsm.get("TOTAL_FACTURE");
        variable_DIFFERENCE = (JRFillVariable)varsm.get("DIFFERENCE");
        variable_GRP_1_COUNT = (JRFillVariable)varsm.get("GRP_1_COUNT");
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
            case 78 : // parameterDefaultValue_L_DIFFERENCE
            {
                value = (java.lang.String)("Diff�rence");
                break;
            }
            case 109 : // textField_15
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL1.getValue()));
                break;
            }
            case 73 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("CP_Signature_1.jasper");
                break;
            }
            case 103 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 72 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse_Federale_Compensation.jasper");
                break;
            }
            case 85 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_MONTANT.getValue()));
                break;
            }
            case 96 : // variable_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 66 : // parameterDefaultValue_P_TYPEETPERIODE
            {
                value = (java.lang.String)("P_TYPEETPERIODE");
                break;
            }
            case 84 : // group_GRP_1
            {
                value = (java.lang.Object)("ff");
                break;
            }
            case 99 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicata.jpg");
                break;
            }
            case 111 : // textField_17
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getValue()));
                break;
            }
            case 80 : // variable_TOTAL_ANNUEL
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getValue()));
                break;
            }
            case 98 : // printWhen_1
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_FR.getValue()));
                break;
            }
            case 90 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_DIFFERENCE.getValue()));
                break;
            }
            case 93 : // textField_9
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_FACTURE.getValue()));
                break;
            }
            case 112 : // textField_18
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getValue()));
                break;
            }
            case 74 : // parameterDefaultValue_L_MONTANT
            {
                value = (java.lang.String)("Montant d�j� factur�");
                break;
            }
            case 106 : // textField_14
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SOURCE.getValue()));
                break;
            }
            case 110 : // textField_16
            {
                value = (java.lang.String)(((java.lang.String)field_LIBELLE_COTI.getValue()));
                break;
            }
            case 95 : // textField_11
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT2.getValue()));
                break;
            }
            case 107 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 68 : // parameterDefaultValue_P_REMARQUE
            {
                value = (java.lang.String)("P_REMARQUE");
                break;
            }
            case 70 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("");
                break;
            }
            case 104 : // textField_12
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TYPEETPERIODE.getValue()));
                break;
            }
            case 63 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 113 : // textField_19
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL2.getValue()));
                break;
            }
            case 101 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicataDE.jpg");
                break;
            }
            case 82 : // variable_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)variable_TOTAL_ANNUEL.getValue()).doubleValue() - ((java.lang.Double)variable_TOTAL_FACTURE.getValue()).doubleValue()));
                break;
            }
            case 67 : // parameterDefaultValue_P_PARA1
            {
                value = (java.lang.String)("P_PARA1");
                break;
            }
            case 108 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"PHENIX_DETAIL_CALCULIND.jasper");
                break;
            }
            case 71 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/iText/Default_workspace/iText model/defaultModel");
                break;
            }
            case 97 : // variableInitialValue_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 77 : // parameterDefaultValue_L_TOTAL
            {
                value = (java.lang.String)("Total");
                break;
            }
            case 105 : // textField_13
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_PARA1.getValue()));
                break;
            }
            case 87 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_COTISATION.getValue()));
                break;
            }
            case 88 : // textField_4
            {
                value = (java.lang.Double)(((java.lang.Double)variable_DIFFERENCE.getValue()));
                break;
            }
            case 91 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT1.getValue()));
                break;
            }
            case 61 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 86 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_PERIODE.getValue()));
                break;
            }
            case 114 : // parametersMap_3
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 75 : // parameterDefaultValue_L_PERIODE
            {
                value = (java.lang.String)("Pour la p�riode");
                break;
            }
            case 115 : // subreport_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 79 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 76 : // parameterDefaultValue_L_COTISATION
            {
                value = (java.lang.String)("COTISATIONS DUES");
                break;
            }
            case 69 : // parameterDefaultValue_P_SOURCE
            {
                value = (java.lang.String)("SOURCE");
                break;
            }
            case 65 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 64 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 92 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_TOTAL.getValue()));
                break;
            }
            case 94 : // textField_10
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_ANNUEL.getValue()));
                break;
            }
            case 83 : // variableInitialValue_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(0.00));
                break;
            }
            case 89 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_DIFFERENCE.getValue()));
                break;
            }
            case 60 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 100 : // printWhen_2
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_DE.getValue()));
                break;
            }
            case 81 : // variable_TOTAL_FACTURE
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getValue()));
                break;
            }
            case 58 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 62 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 102 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
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
            case 78 : // parameterDefaultValue_L_DIFFERENCE
            {
                value = (java.lang.String)("Diff�rence");
                break;
            }
            case 109 : // textField_15
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL1.getValue()));
                break;
            }
            case 73 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("CP_Signature_1.jasper");
                break;
            }
            case 103 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 72 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse_Federale_Compensation.jasper");
                break;
            }
            case 85 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_MONTANT.getValue()));
                break;
            }
            case 96 : // variable_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 66 : // parameterDefaultValue_P_TYPEETPERIODE
            {
                value = (java.lang.String)("P_TYPEETPERIODE");
                break;
            }
            case 84 : // group_GRP_1
            {
                value = (java.lang.Object)("ff");
                break;
            }
            case 99 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicata.jpg");
                break;
            }
            case 111 : // textField_17
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getOldValue()));
                break;
            }
            case 80 : // variable_TOTAL_ANNUEL
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getOldValue()));
                break;
            }
            case 98 : // printWhen_1
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_FR.getValue()));
                break;
            }
            case 90 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_DIFFERENCE.getValue()));
                break;
            }
            case 93 : // textField_9
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_FACTURE.getOldValue()));
                break;
            }
            case 112 : // textField_18
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getOldValue()));
                break;
            }
            case 74 : // parameterDefaultValue_L_MONTANT
            {
                value = (java.lang.String)("Montant d�j� factur�");
                break;
            }
            case 106 : // textField_14
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SOURCE.getValue()));
                break;
            }
            case 110 : // textField_16
            {
                value = (java.lang.String)(((java.lang.String)field_LIBELLE_COTI.getOldValue()));
                break;
            }
            case 95 : // textField_11
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT2.getValue()));
                break;
            }
            case 107 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 68 : // parameterDefaultValue_P_REMARQUE
            {
                value = (java.lang.String)("P_REMARQUE");
                break;
            }
            case 70 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("");
                break;
            }
            case 104 : // textField_12
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TYPEETPERIODE.getValue()));
                break;
            }
            case 63 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 113 : // textField_19
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL2.getValue()));
                break;
            }
            case 101 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicataDE.jpg");
                break;
            }
            case 82 : // variable_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)variable_TOTAL_ANNUEL.getOldValue()).doubleValue() - ((java.lang.Double)variable_TOTAL_FACTURE.getOldValue()).doubleValue()));
                break;
            }
            case 67 : // parameterDefaultValue_P_PARA1
            {
                value = (java.lang.String)("P_PARA1");
                break;
            }
            case 108 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"PHENIX_DETAIL_CALCULIND.jasper");
                break;
            }
            case 71 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/iText/Default_workspace/iText model/defaultModel");
                break;
            }
            case 97 : // variableInitialValue_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 77 : // parameterDefaultValue_L_TOTAL
            {
                value = (java.lang.String)("Total");
                break;
            }
            case 105 : // textField_13
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_PARA1.getValue()));
                break;
            }
            case 87 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_COTISATION.getValue()));
                break;
            }
            case 88 : // textField_4
            {
                value = (java.lang.Double)(((java.lang.Double)variable_DIFFERENCE.getOldValue()));
                break;
            }
            case 91 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT1.getValue()));
                break;
            }
            case 61 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 86 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_PERIODE.getValue()));
                break;
            }
            case 114 : // parametersMap_3
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 75 : // parameterDefaultValue_L_PERIODE
            {
                value = (java.lang.String)("Pour la p�riode");
                break;
            }
            case 115 : // subreport_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 79 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 76 : // parameterDefaultValue_L_COTISATION
            {
                value = (java.lang.String)("COTISATIONS DUES");
                break;
            }
            case 69 : // parameterDefaultValue_P_SOURCE
            {
                value = (java.lang.String)("SOURCE");
                break;
            }
            case 65 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 64 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 92 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_TOTAL.getValue()));
                break;
            }
            case 94 : // textField_10
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_ANNUEL.getOldValue()));
                break;
            }
            case 83 : // variableInitialValue_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(0.00));
                break;
            }
            case 89 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_DIFFERENCE.getValue()));
                break;
            }
            case 60 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 100 : // printWhen_2
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_DE.getValue()));
                break;
            }
            case 81 : // variable_TOTAL_FACTURE
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getOldValue()));
                break;
            }
            case 58 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 62 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 102 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
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
            case 78 : // parameterDefaultValue_L_DIFFERENCE
            {
                value = (java.lang.String)("Diff�rence");
                break;
            }
            case 109 : // textField_15
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL1.getValue()));
                break;
            }
            case 73 : // parameterDefaultValue_P_SUBREPORT_SIGNATURE
            {
                value = (java.lang.String)("CP_Signature_1.jasper");
                break;
            }
            case 103 : // subreport_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_HEADER.getValue()));
                break;
            }
            case 72 : // parameterDefaultValue_P_SUBREPORT_HEADER
            {
                value = (java.lang.String)("Header_Caisse_Federale_Compensation.jasper");
                break;
            }
            case 85 : // textField_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_MONTANT.getValue()));
                break;
            }
            case 96 : // variable_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 66 : // parameterDefaultValue_P_TYPEETPERIODE
            {
                value = (java.lang.String)("P_TYPEETPERIODE");
                break;
            }
            case 84 : // group_GRP_1
            {
                value = (java.lang.Object)("ff");
                break;
            }
            case 99 : // image_1
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicata.jpg");
                break;
            }
            case 111 : // textField_17
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getValue()));
                break;
            }
            case 80 : // variable_TOTAL_ANNUEL
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getValue()));
                break;
            }
            case 98 : // printWhen_1
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_FR.getValue()));
                break;
            }
            case 90 : // textField_6
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_DIFFERENCE.getValue()));
                break;
            }
            case 93 : // textField_9
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_FACTURE.getEstimatedValue()));
                break;
            }
            case 112 : // textField_18
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_ANNUELLE.getValue()));
                break;
            }
            case 74 : // parameterDefaultValue_L_MONTANT
            {
                value = (java.lang.String)("Montant d�j� factur�");
                break;
            }
            case 106 : // textField_14
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SOURCE.getValue()));
                break;
            }
            case 110 : // textField_16
            {
                value = (java.lang.String)(((java.lang.String)field_LIBELLE_COTI.getValue()));
                break;
            }
            case 95 : // textField_11
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT2.getValue()));
                break;
            }
            case 107 : // parametersMap_2
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 68 : // parameterDefaultValue_P_REMARQUE
            {
                value = (java.lang.String)("P_REMARQUE");
                break;
            }
            case 70 : // parameterDefaultValue_MODEL_PATH
            {
                value = (java.lang.String)("");
                break;
            }
            case 104 : // textField_12
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_TYPEETPERIODE.getValue()));
                break;
            }
            case 63 : // variableInitialValue_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 113 : // textField_19
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_COL2.getValue()));
                break;
            }
            case 101 : // image_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_DEFAULT_MODEL_PATH.getValue())+"/duplicataDE.jpg");
                break;
            }
            case 82 : // variable_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)variable_TOTAL_ANNUEL.getEstimatedValue()).doubleValue() - ((java.lang.Double)variable_TOTAL_FACTURE.getEstimatedValue()).doubleValue()));
                break;
            }
            case 67 : // parameterDefaultValue_P_PARA1
            {
                value = (java.lang.String)("P_PARA1");
                break;
            }
            case 108 : // subreport_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"PHENIX_DETAIL_CALCULIND.jasper");
                break;
            }
            case 71 : // parameterDefaultValue_P_DEFAULT_MODEL_PATH
            {
                value = (java.lang.String)("C:/Documents and Settings/Administrator/My Documents/consultran/projet/globaz/iText/Default_workspace/iText model/defaultModel");
                break;
            }
            case 97 : // variableInitialValue_GRP_1_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 77 : // parameterDefaultValue_L_TOTAL
            {
                value = (java.lang.String)("Total");
                break;
            }
            case 105 : // textField_13
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_PARA1.getValue()));
                break;
            }
            case 87 : // textField_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_COTISATION.getValue()));
                break;
            }
            case 88 : // textField_4
            {
                value = (java.lang.Double)(((java.lang.Double)variable_DIFFERENCE.getEstimatedValue()));
                break;
            }
            case 91 : // textField_7
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_FRANC_TOT1.getValue()));
                break;
            }
            case 61 : // variableInitialValue_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 86 : // textField_2
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_PERIODE.getValue()));
                break;
            }
            case 114 : // parametersMap_3
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
            case 75 : // parameterDefaultValue_L_PERIODE
            {
                value = (java.lang.String)("Pour la p�riode");
                break;
            }
            case 115 : // subreport_3
            {
                value = (java.lang.String)(((java.lang.String)parameter_P_SUBREPORT_SIGNATURE.getValue()));
                break;
            }
            case 79 : // variable_IMAGE_LOGO
            {
                value = (java.lang.String)(((java.lang.String)parameter_MODEL_PATH.getValue())+"cfc_logo.gif");
                break;
            }
            case 76 : // parameterDefaultValue_L_COTISATION
            {
                value = (java.lang.String)("COTISATIONS DUES");
                break;
            }
            case 69 : // parameterDefaultValue_P_SOURCE
            {
                value = (java.lang.String)("SOURCE");
                break;
            }
            case 65 : // variableInitialValue_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(0));
                break;
            }
            case 64 : // variable_COLUMN_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 92 : // textField_8
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_TOTAL.getValue()));
                break;
            }
            case 94 : // textField_10
            {
                value = (java.lang.Double)(((java.lang.Double)variable_TOTAL_ANNUEL.getEstimatedValue()));
                break;
            }
            case 83 : // variableInitialValue_DIFFERENCE
            {
                value = (java.lang.Double)(new Double(0.00));
                break;
            }
            case 89 : // textField_5
            {
                value = (java.lang.String)(((java.lang.String)parameter_L_DIFFERENCE.getValue()));
                break;
            }
            case 60 : // variable_REPORT_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 59 : // variableInitialValue_COLUMN_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 100 : // printWhen_2
            {
                value = (java.lang.Boolean)(((java.lang.Boolean)parameter_P_DUPLICATA_DE.getValue()));
                break;
            }
            case 81 : // variable_TOTAL_FACTURE
            {
                value = (java.lang.Double)(((java.lang.Double)field_COTI_FACTUREE.getValue()));
                break;
            }
            case 58 : // variableInitialValue_PAGE_NUMBER
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 62 : // variable_PAGE_COUNT
            {
                value = (java.lang.Integer)(new Integer(1));
                break;
            }
            case 102 : // parametersMap_1
            {
                value = (java.util.Map)(new java.util.HashMap(((java.util.Map)parameter_REPORT_PARAMETERS_MAP.getValue())));
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
