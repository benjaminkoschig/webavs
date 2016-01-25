<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.attestations.RFAttestationPiedDePageViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>
<%
	idEcran="PRF0039";
	RFAttestationPiedDePageViewBean viewBean = (RFAttestationPiedDePageViewBean) session
			.getAttribute("viewBean");

	
	autoShowErrorPopup = true;		

%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	servlet = "<%=(servletContext + mainServletPath)%>";

	function typeSousTypeDeSoinListInit(){

		document.getElementsByName("codeTypeDeSoinList")[0].options[parseInt("<%=viewBean.getCodeTypeDeSoinList()%>",10)].selected = true;
		document.getElementsByName("codeTypeDeSoin")[0].value="<%=viewBean.getCodeTypeDeSoinList()%>";
		document.getElementsByName("imgCodeTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;
		document.getElementsByName("codeSousTypeDeSoin")[0].value="<%=viewBean.getCodeSousTypeDeSoinList()%>";
		document.getElementsByName("imgCodeSousTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;


		currentSousTypeDeSoinTab=sousTypeDeSoinTab[parseInt(document.getElementsByName("codeTypeDeSoin")[0].value,10)-1];
		currentCodeSousTypeList=currentSousTypeDeSoinTab[0];
		currentLibelleSousTypeList=currentSousTypeDeSoinTab[1];
		codeSousTypeDeSoinList=document.getElementsByName("codeSousTypeDeSoinList")[0];
		codeSousTypeDeSoinList.options.length=0;
		codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option("","");
				
		for (i=0; i<currentCodeSousTypeList.length; i++){
			codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option(currentLibelleSousTypeList[i],currentCodeSousTypeList[i]);
		}

		codeSousTypeDeSoinList.options[parseInt("<%=viewBean.getCodeSousTypeDeSoinList()%>",10)].selected = true;

		//les listes ne sont plus modifiables
		codeSousTypeDeSoinList.disabled=true;
		document.getElementsByName("codeTypeDeSoinList")[0].disabled=true;
		document.getElementsByName("codeTypeDeSoin")[0].disabled=true;                                         		
		document.getElementsByName("codeSousTypeDeSoin")[0].disabled=true;
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='WARNING_RF_ATTESTATION_SUPPRESSION_OBJET'/>")){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE%>.supprimer";
	        document.forms[0].submit();
	    }
	}	
	
	function validate(){

	    state = validateFields();    	    

	    if (document.forms[0].elements('_method').value == "add"){
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE%>.ajouter";	    
	    }  
	    else{
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE%>.modifier";	    
	    }  	      
	    return state;
		
	}

	function init(){
   
	}

	function add(){
	}

	function upd(){		
		// si on vient de la recherche il faut pouvoir éditer les dates !		
	    document.getElementsByName("dateDebut")[0].disabled=false;
	    document.getElementsByName("dateFin")[0].disabled=false;

	    document.getElementsByName("codeSousTypeDeSoinList")[0].disabled=true;
		document.getElementsByName("codeTypeDeSoinList")[0].disabled=true;
		document.getElementsByName("codeTypeDeSoin")[0].disabled=true;                                         		
		document.getElementsByName("codeSousTypeDeSoin")[0].disabled=true;
		document.getElementsByName("idGestionnaire")[0].disabled=true;
		document.getElementsByName("typeAttes")[0].disabled=true;		
	}	

	function cancel(){
	}	

	function postInit(){	
		
		typeSousTypeDeSoinListInit();				

	    if("upd" == document.forms[0].elements('_method').value){
	    	document.getElementsByName("dateDebut")[0].disabled=false;
	    	document.getElementsByName("dateFin")[0].disabled=false;
	    }
		else{
	    	document.getElementsByName("dateDebut")[0].disabled=true;
	    	document.getElementsByName("dateFin")[0].disabled=true;		
		}
		
	    document.getElementsByName("idGestionnaire")[0].disabled=true;	
	    document.getElementsByName("typeAttes")[0].disabled=true;

	    
        <%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
        errorObj.text="<%=viewBean.getMessage()%>";
        showErrors()
        errorObj.text="";        
        <%}%>	
	    
	}


</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_SAISIE_ATTESTATION_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<!-- récupérer l'id dossier grace au menu request? -->
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_NUMERO_DOSSIER" /></TD>
	<TD colspan="5"><%=viewBean.getIdDossier() %></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_INFOS_ASSURE" /></TD>
	<TD colspan="5"><%=viewBean.getDetailAssure() %></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_GESTIONNAIRE" /></TD>
	<TD>
		<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getIdGestionnaire()%>"/>
	</TD>	
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_PERIODE" /></TD>
	<TD>Du &nbsp;<input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>&nbsp; au &nbsp;<input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf" %>
<TR><TD>
     	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
     	<INPUT type="hidden" name="isAttestation" value="true"/>
     	<INPUT type="hidden" name="isEditSoins" value="false"/>
     	<INPUT type="hidden" name="isSaisieQd" value="false"/>
     	<INPUT type="hidden" name="dateCreation" value="<%=viewBean.getDateCreation()%>"/>
</TD></TR>

<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TYPE_DOCUMENT" /></TD>
	
	<TD colspan="6">		
	<ct:FWListSelectTag data="<%=viewBean.getCsTypeAttestationData()%>" defaut="<%=viewBean.getCsTypeAttestation()%>" name="typeAttes" />
	<INPUT type="hidden" name="CsTypeAttestation" value="<%=viewBean.getCsTypeAttestation()%>" />
	</TD>	
</TR>
<TR><TD colspan="6"><HR></HR></TD></TR>

<!-- Arrivée sur le régime alimentaire -->
<TR>
	<TD>
		<TBODY id="fraisLivraison">
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_FL_CENTRE_LOCATION" /></TD>
				<TD><INPUT type="text" name="centreLocation" value="<%=viewBean.getCentreLocation()%>" /></TD>
			</TR>		
			<tr><td colspan="6"><br></td></tr>	
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_FL_DATE_FRAIS_LIVRAISON" /></TD>
				<TD><input data-g-calendar=" "  name="dateFraisLiv" value="<%=viewBean.getDateFraisLiv()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>	
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_FL_REMARQUE" /></TD>
				<TD><textarea rows="5" cols="100" name="remarque"><%=viewBean.getRemarque()%></textarea></TD>
			</TR>					 
			<tr><td colspan="6"><br></td></tr>	
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_FL_ATTESTATION_LIVRAISON" /></TD>
				<TD><input data-g-calendar=" "  name="attestationLivraison" value="<%=viewBean.getAttestationLivraison()%>"/></TD>
			</TR>		
		</TBODY>		
	</TD>
</TR>
<input type="hidden" name="fromAttestation" value="0"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>	
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>