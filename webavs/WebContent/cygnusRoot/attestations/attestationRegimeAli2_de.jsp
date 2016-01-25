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
	idEcran="PRF0037";
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
		document.getElementsByName("codeTypeDeSoinList")[0].disabled=true;
		document.getElementsByName("codeTypeDeSoin")[0].disabled=true;                                         		
		document.getElementsByName("codeSousTypeDeSoin")[0].disabled=true;
		document.getElementsByName("codeSousTypeDeSoinList")[0].disabled=true;
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
	<TD colspan="4">Du &nbsp;<input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>&nbsp; au &nbsp;<input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
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
	<INPUT type="hidden" name="csTypeAttestation" value="<%=viewBean.getCsTypeAttestation()%>" />
	</TD>	
</TR>
<TR><TD colspan="6"><HR></HR></TD></TR>

<!-- Arrivée sur le régime alimentaire -->
<TR>
	<TD>
		<TBODY id="regimeAlimentaire">
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_RECEPTION" /></TD>
				<TD><input data-g-calendar=" "  name="dateReception" value="<%=viewBean.getDateReception()%>"/></TD>
			</TR>		
			<tr><td colspan="6"><br></td></tr>	 
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_DECISION" /></TD>
				<TD><input data-g-calendar=" "  name="dateDecision" value="<%=viewBean.getDateDecision()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_DECISION_REFUS" /></TD>
				<TD><input data-g-calendar=" "  name="dateDecisionRefus" value="<%=viewBean.getDateDecisionRefus()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_ENVOI_INFOS_MEDECIN" /></TD>
				<TD><input data-g-calendar=" "  name="dateEnvoiInfosMedecin" value="<%=viewBean.getDateEnvoiInfosMedecin()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_RETOUR_INFOS_MEDECIN" /></TD>
				<TD><input data-g-calendar=" "  name="dateRetourInfosMedecin" value="<%=viewBean.getDateRetourInfosMedecin()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_ENVOI_EVALUATION_CMS" /></TD>
				<TD><input data-g-calendar=" "  name="dateEnvoiEvaluationCMS" value="<%=viewBean.getDateEnvoiEvaluationCMS()%>"/></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD width="250px"><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_RETOUR_EVALUATION_CMS" /></TD>
				<TD><input data-g-calendar=" "  name="dateRetourEvaluationCMS" value="<%=viewBean.getDateRetourEvaluationCMS()%>"/></TD>
			</TR>								
			<tr><td colspan="6"><br></td></tr>								
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_TYPE_REGIME" /></TD>				
				<TD colspan="6"><textarea name="typeRegime" rows="5" cols="80" ><%=viewBean.getTypeRegime()%></textarea></TD>
			</TR>				
			<tr><td colspan="6"><br></td></tr>				
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_REGIME_ACCEPTE" /></TD>
				<TD><INPUT type="checkbox" name="isRegimeAccepte" <%=viewBean.getIsRegimeAccepte().booleanValue()==true?"CHECKED":""%> /></TD>
			</TR>
			<tr><td colspan="6"><br></td></tr>			
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_MONTANT_MENSUEL" /></TD>
				<TD><INPUT type="text" name="montantMensuelAccepte" value="<%=new FWCurrency(viewBean.getMontantMensuelAccepte()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" /></TD>
			</TR>
			<tr><td colspan="6"><br></td></tr>			
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_AIDE_ECHEANCE_REVISION" /></TD>
				<TD><input data-g-calendar=" "  name="echeanceRevision" value="<%=viewBean.getEcheanceRevision()%>"/></TD>
			</TR>
			<tr><td colspan="6"><br></td></tr>	
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_COMMENTAIRE" /></TD>				
				<TD colspan="6"><textarea name="commentaire" rows="5" cols="80" ><%=viewBean.getCommentaire()%></textarea></TD>
			</TR>	
			<tr><td colspan="6"><br></td></tr>	
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_AUTRES_FRAIS" /></TD>				
				<TD colspan="6"><textarea name="autresFraisPourRegime" rows="5" cols="80" ><%=viewBean.getAutresFraisPourRegime()%></textarea></TD>
			</TR>		
			<tr><td colspan="6"><br></td></tr>
			<TR>
				<TD><b><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_DETERMINEE" /></b></TD>
			</TR>			
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_DETERMINEE_DES_LE" /></TD>
				<TD width="200px"><input data-g-calendar=" "  name="dateAideDureeDetermineeDesLe" value="<%=viewBean.getDateAideDureeDetermineeDesLe()%>"/></TD>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_DETERMINEE_JUSQUA" /></TD>
				<TD width="500px"><input data-g-calendar=" "  name="dateAideDureeDetermineeJusqua" value="<%=viewBean.getDateAideDureeDetermineeJusqua()%>"/></TD>
			</TR>
			<tr><td colspan="6"><br></td></tr>			
			<TR>
				<TD><b><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_INDETERMINEE" /></b></TD>
			</TR>						
			<TR>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_INDETERMINEE_DES_LE" /></TD>
				<TD width="200px"><input data-g-calendar=" "  name="dateAideDureeIndetermineeDesLe" value="<%=viewBean.getDateAideDureeIndetermineeDesLe()%>"/></TD>
				<TD><ct:FWLabel key="JSP_RF_ATTESTATION_RL_DATE_AIDE_INDETERMINEE_REEVALUATION" />&nbsp;</TD>
				<TD width="500px"><input data-g-calendar=" "  name="dateAideDureeIndetermineeReevaluation" value="<%=viewBean.getDateAideDureeIndetermineeReevaluation()%>"/></TD>				
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