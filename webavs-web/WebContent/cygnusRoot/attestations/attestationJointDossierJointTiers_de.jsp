<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.attestations.RFAttestationViewBean"%>
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
	idEcran="PRF0034";
	RFAttestationViewBean viewBean = (RFAttestationViewBean) session
			.getAttribute("viewBean");

	
	autoShowErrorPopup = true;		
	
	bButtonDelete = false;	
	bButtonNew = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	servlet = "<%=(servletContext + mainServletPath)%>";

	function add() {
	    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.ajouterRegimeAlimentaire"
	}
	
	// prépare pour enregistement 
	function validate() {				
	    state = validateFields();    	    

	    if (document.forms[0].elements('_method').value == "add"){
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.ajouterRegimeAlimentaire";	    
	    }  
	    else{
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.modifier";	    
	    }  	      
	    return state;
	
	}	

	function displayAndChoose()
	{		
		if($('#dateDebut').val()!="" &&
				$('#dateFin').val()!="" &&
				$('[name=codeTypeDeSoin]').val()!="" &&
				$('[name=codeSousTypeDeSoin]').val()!="" ){			
			// Attention !!!!
			//cas ou le code type de soin vaut 02 et le codeSousTypeDeSoin vaut 01
			if($('[name=codeSousTypeDeSoin]').val()=="01" && $('[name=codeTypeDeSoin]').val()=="02"){
				$('#regimeAlimentaire').show();
				$('[name=forCsTypeAttestation]')[0].selectedIndex=1;
			}
		}
		else{
			$('#regimeAlimentaire').hide();
		}		
	}

	function upd(){
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.modifier";
	}

	function init(){}

	$(function(){				
		$('#regimeAlimentaire').hide();

		/*$('#anchor_dateDebut').click(function() {
			displayAndChoose();	
								
		});	
		$('#anchor_dateFin').click(function() {
			displayAndChoose();
		});*/	
		$('[name=codeTypeDeSoinList]').click(function() {
			displayAndChoose();
		});	
		$('[name=codeSousTypeDeSoinList]').click(function() {
			displayAndChoose();
		});	


		$('[name=csTypeAttestation]').click(function() {			
			
		});	

	
	});

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
<TR>	
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_INFOS_ASSURE" /></TD>
	<TD colspan="4"><%=viewBean.getDetailAssure() %></TD>
	<TD><input type="hidden" name="forDetailAssure"></input></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_GESTIONNAIRE" /></TD>
	<TD>
		<%if(viewBean.isNew()){ %>
		<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getSession().getUserId()%>"/>
		<%}else {%>
		<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getIdGestionnaire()%>"/>
		<%}%>
	</TD>	
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_PERIODE" /></TD>
	<TD>Du &nbsp;<input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>&nbsp; au &nbsp;<input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf" %>
<TR><TD><INPUT type="hidden" name="isSaisieDemande" value="false"/></TD></TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_NIVEAU_AVERTISSEMENT" /></TD>
	
	<TD><ct:FWListSelectTag data="<%=viewBean.getCsNiveauAvertissementData()%>" defaut="" name="forCsNiveauAvertissement" /></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TYPE_DOCUMENT" /></TD>
	
	<TD><ct:FWListSelectTag data="<%=viewBean.getCsTypeAttestationData()%>" defaut="" name="forCsTypeAttestation" /></TD>
</TR>
<TR><TD colspan="6"><HR></HR></TD></TR>

<!-- Arrivé sur le régime alimentaire -->
<TR>
	<TD>
		<TBODY id="regimeAlimentaire">
			<TR>
				<TD><B><ct:FWLabel key="JSP_RF_ATTESTATION_RL_MOTIF_INTERVENTION" /></B></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_TROUBLE_AGE" /></TD>
				<TD><INPUT type="checkbox" name="aTroubleAge" <%=viewBean.getaTroubleAge().booleanValue()==true?"CHECKED":""%> /></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_MALADIE" /></TD>
				<TD><INPUT type="checkbox" name="aMaladie" <%=viewBean.getaMaladie().booleanValue()==true?"CHECKED":""%>/></TD>			
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_ACCIDENT" /></TD>
				<TD><INPUT type="checkbox" name="aAccident" <%=viewBean.getaAccident().booleanValue()==true?"CHECKED":""%>/></TD>			
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_INVALIDITE" /></TD>
				<TD><INPUT type="checkbox" name="aInvalidite" <%=viewBean.getaInvalidite().booleanValue()==true?"CHECKED":""%>/></TD>
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><B><ct:FWLabel key="JSP_RF_ATTESTATION_RL_SUPPLEMENT" /></B></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_INTOLERANCE_GLUTEN" /></TD>
				<TD><INPUT type="checkbox" name="aIntoleranceGluten" <%=viewBean.getaIntoleranceGluten().booleanValue()==true?"CHECKED":""%>/></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_INTOLERANCE_LACTOSE" /></TD>
				<TD><INPUT type="checkbox" name="aIntoleranceLactose" <%=viewBean.getaIntoleranceLactose().booleanValue()==true?"CHECKED":""%>/></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DENUTRITION" /></TD>
				<TD><INPUT type="checkbox" name="aDenutrition" <%=viewBean.getaDenutrition().booleanValue()==true?"CHECKED":""%>/></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DENUTRITION" /></TD>
				<TD><INPUT type="checkbox" name="aDiabete" <%=viewBean.getaDiabete().booleanValue()==true?"CHECKED":""%>/></TD>
			</TR>			
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_AUTRE" /></TD>
				<TD><INPUT type="text" name="autre" value="<%=viewBean.getAutre()%>" /></TD>
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><B><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DUREE_AIDE" /></B></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_AIDE_DUREE_DETERMINEE" /></TD>
				<TD><input data-g-calendar=" "  name="aideDureeDeterminee" value="<%=viewBean.getAideDureeDeterminee()%>"/></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_AIDE_DUREE_INDETERMINEE" /></TD>
				<TD><input data-g-calendar=" "  name="aideDureeIndeterminee" value="<%=viewBean.getAideDureeIndeterminee()%>"/></TD>
			</TR>	
		</TBODY>		
	</TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>		
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>