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
	idEcran="PRF0040";
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

	function setRadioButtonVar(){
		if($('#vpBenevole:checked').val())
			$('[name=veillesPresence]').val(1);
		else
			$('[name=veillesPresence]').val(2);	

		if($('#nrBenef:checked').val())
			$('[name=nettoyageRangement]').val(1);
		else
			if($('#nrBenevole:checked').val())
				$('[name=nettoyageRangement]').val(2);
			else
				$('[name=nettoyageRangement]').val(3);
		
		if($('#vBenef:checked').val())
			$('[name=vaisselle]').val(1);
		else
			if($('#vBenevole:checked').val())
				$('[name=vaisselle]').val(2);
			else
				$('[name=vaisselle]').val(3);

		if($('#lBenef:checked').val())
			$('[name=lits]').val(1);
		else
			if($('#lBenevole:checked').val())
				$('[name=lits]').val(2);
			else
				$('[name=lits]').val(3);

		if($('#lesBenef:checked').val())
			$('[name=lessive]').val(1);
		else
			if($('#lesBenevole:checked').val())
				$('[name=lessive]').val(2);
			else
				$('[name=lessive]').val(3);				

		if($('#rrBenef:checked').val())
			$('[name=repassage]').val(1);
		else
			if($('#rrBenevole:checked').val())
				$('[name=repassage]').val(2);
			else
				$('[name=repassage]').val(3);					
	}
	
	function validate(){

	    state = validateFields();    	    
		//il faut récupérer les valeurs des boutons radio pour setter les var. correspondantes		
		setRadioButtonVar();
		
				
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
	<INPUT type="hidden" name="csTypeAttestation" value="<%=viewBean.getCsTypeAttestation()%>" />
	</TD>	
</TR>
<TR><TD colspan="6"><HR></HR></TD></TR>

<!-- Arrivée sur le maintien à domicile -->
	
		<TR>
			<TD colspan="6"><B><U><ct:FWLabel key="JSP_RF_ATTESTATION_MD_ETAT_SANTE" /></U></B></TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD colspan="6"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_MOTIF_INTERVENTION" /></TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD colspan="6">	
				<div class="dataTable">															
					<TABLE  style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
						<TR style="border=1px solid black; " >
							<TH colspan="4"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_BENEFICIAIRE" /></TH>
							<TH colspan="4"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_PERSONNE" /></TH>
						</TR>
						<TR>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_TROUBLE_AGE" />
							<INPUT type="checkbox" name="aTroubleAge" <%=viewBean.getaTroubleAge().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_MALADIE" />
							<INPUT type="checkbox" name="aMaladie" <%=viewBean.getaMaladie().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_ACCIDENT" />
							<INPUT type="checkbox" name="aAccident" <%=viewBean.getaAccident().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_INVALIDITE" />
							<INPUT type="checkbox" name="aInvalidite" <%=viewBean.getaInvalidite().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_TROUBLE_AGE" />
							<INPUT type="checkbox" name="autrePersonneATroubleAge" <%=viewBean.getAutrePersonneATroubleAge().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_MALADIE" />
							<INPUT type="checkbox" name="autrePersonneAMaladie" <%=viewBean.getAutrePersonneAMaladie().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_ACCIDENT" />
							<INPUT type="checkbox" name="autrePersonneAAccident" <%=viewBean.getAutrePersonneAAccident().booleanValue()==true?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_INVALIDITE" />
							<INPUT type="checkbox" name="autrePersonneAInvalidite" <%=viewBean.getAutrePersonneAInvalidite().booleanValue()==true?"CHECKED":""%> /></TD>
						</TR>												
					</TABLE>		
				</div>	
			</TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_MOTIF" /></TD>
			<TD colspan="3"><INPUT size="80" type="text" name="descriptionMotif" value="<%=viewBean.getDescriptionMotif()%>" /></TD>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AUTRE_MOTIF" /></TD>
			<TD><INPUT type="text" name="autrePersonneDescriptionMotif" value="<%=viewBean.getAutrePersonneDescriptionMotif()%>" /></TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>	
		<TR>
			<TD colspan="6"><B><U><ct:FWLabel key="JSP_RF_ATTESTATION_MD_INDICATION_LOGEMENT" /></U></B></TD>
		</TR>					
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_TOTAL_PIECE" /></TD>
			<TD><INPUT type="text" name="nombreTotalPiece" value="<%=viewBean.getNombreTotalPiece()%>" /></TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_PIECE_UTILISE" /></TD>
			<TD><INPUT type="text" name="nombrePieceUtilise" value="<%=viewBean.getNombrePieceUtilise()%>" /></TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_PERSONNE_LOGEMENT" /></TD>
			<TD><INPUT type="text" name="nombrePersonneLogement" value="<%=viewBean.getNombrePersonneLogement()%>" /></TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>	
		<TR>
			<TD colspan="6"><B><U><ct:FWLabel key="JSP_RF_ATTESTATION_MD_EVALUATION_BESOINS" /></U></B></TD>
		</TR>		
		<TR><TD colspan="6">&nbsp;</TD></TR>					
		<!-- radio buttons! -->
		<TR>
			<TD colspan="4">
				<div class="dataTable">															
					<TABLE style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
						<TR style="border=1px solid black; " >
							<TH><B><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_DOMICILE" /></B></TH>
							<TH><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_BENEVOLE" /></TH>
							<TH><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_REMUNERER" /></TH>
						</TR>
						<TR>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_VEILLES_PRESENCE" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="vpBenevole" name="_veillesPresence" <%=(JadeStringUtil.isEmpty(viewBean.getVeillesPresence()) || "1".equals(viewBean.getVeillesPresence()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="vpRemunere" name="_veillesPresence" <%=(!JadeStringUtil.isEmpty(viewBean.getVeillesPresence()) && "2".equals(viewBean.getVeillesPresence()))?"CHECKED":""%> />
							<INPUT type="hidden" name="veillesPresence" /></TD>				
						</TR>
					</TABLE>
				</div>
			</TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>			
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_DUREE_AIDE_REMUNERE" /></TD>
			<TD><INPUT type="text" name="dureeAideRemunere" value="<%=viewBean.getDureeAideRemunere()%>" /></TD>
		</TR>	
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD colspan="4">			
				<div class="dataTable">															
					<TABLE style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
						<TR style="border=1px solid black; " >
							<TH><B><ct:FWLabel key="JSP_RF_ATTESTATION_MD_TENUE_MENAGE" /></B></TH>
							<TH><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_BENEFICIAIRE" /></TH>
							<TH><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_BENEVOLE" /></TH>
							<TH><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_REMUNERER" /></TH>				
						</TR>		
						<TR>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_NETTOYAGE_RANGEMENT" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="nrBenef" name="_nettoyageRangement" <%=(JadeStringUtil.isEmpty(viewBean.getNettoyageRangement()) || "1".equals(viewBean.getNettoyageRangement()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="nrBenevole" name="_nettoyageRangement" <%=(!JadeStringUtil.isEmpty(viewBean.getNettoyageRangement()) && "2".equals(viewBean.getNettoyageRangement()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="nrRemunere" name="_nettoyageRangement" <%=(!JadeStringUtil.isEmpty(viewBean.getNettoyageRangement()) && "3".equals(viewBean.getNettoyageRangement()))?"CHECKED":""%> />
							<INPUT type="hidden" name="nettoyageRangement" /></TD>										
						</TR>		
						<TR style="background-color=#E8EEF4;">
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_VAISSELLE" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="vBenef" name="_vaisselle" <%=(JadeStringUtil.isEmpty(viewBean.getVaisselle()) || "1".equals(viewBean.getVaisselle()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="vBenevole" name="_vaisselle" <%=(!JadeStringUtil.isEmpty(viewBean.getVaisselle()) && "2".equals(viewBean.getVaisselle()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="vRemunere" name="_vaisselle" <%=(!JadeStringUtil.isEmpty(viewBean.getVaisselle()) && "3".equals(viewBean.getVaisselle()))?"CHECKED":""%> />
							<INPUT type="hidden" name="vaisselle" /></TD>
						</TR>
						<TR>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_LITS" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lBenef" name="_lits" <%=(JadeStringUtil.isEmpty(viewBean.getLits()) || "1".equals(viewBean.getLits()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lBenevole" name="_lits" <%=(!JadeStringUtil.isEmpty(viewBean.getLits()) && "2".equals(viewBean.getLits()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lRemunere" name="_lits" <%=(!JadeStringUtil.isEmpty(viewBean.getLits()) && "3".equals(viewBean.getLits()))?"CHECKED":""%> />
							<INPUT type="hidden" name="lits" /></TD>
						</TR>			
						<TR style="background-color=#E8EEF4;">
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_LESSIVE" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lesBenef" name="_lessive" <%=(JadeStringUtil.isEmpty(viewBean.getLessive()) || "1".equals(viewBean.getLessive()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lesBenevole" name="_lessive" <%=(!JadeStringUtil.isEmpty(viewBean.getLessive()) && "2".equals(viewBean.getLessive()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="lesRemunere" name="_lessive" <%=(!JadeStringUtil.isEmpty(viewBean.getLessive()) && "3".equals(viewBean.getLessive()))?"CHECKED":""%> />
							<INPUT type="hidden" name="lessive" /></TD>
						</TR>			
						<TR>
							<TD style="border-right=1px solid #C0C0C0"><ct:FWLabel key="JSP_RF_ATTESTATION_MD_REPASSAGE" /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="rrBenef" name="_repassage" <%=(JadeStringUtil.isEmpty(viewBean.getRepassage()) || "1".equals(viewBean.getRepassage()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="rrBenevole" name="_repassage" <%=(!JadeStringUtil.isEmpty(viewBean.getRepassage()) && "2".equals(viewBean.getRepassage()))?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align: center"><INPUT type="radio" id="rrRemunere" name="_repassage" <%=(!JadeStringUtil.isEmpty(viewBean.getRepassage()) && "3".equals(viewBean.getRepassage()))?"CHECKED":""%> />
							<INPUT type="hidden" name="repassage" /></TD>
						</TR>
					</TABLE>
				</div>
			</TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<!-- fin radio buttons! -->
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_DUREE_AIDE_REMUNERE" /></TD>
			<TD><INPUT type="text" name="dureeAideRemunereTenueMenage" value="<%=viewBean.getDureeAideRemunereTenueMenage()%>" /></TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD colspan="6"><B><ct:FWLabel key="JSP_RF_ATTESTATION_MD_PREPARATION_REPAS" /></B></TD>
		</TR>				
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_REPAS_DOMICILE" /></TD>
			<TD><INPUT type="checkbox" name="repasDomicileCMS" <%=viewBean.getRepasDomicileCMS().booleanValue()==true?"CHECKED":""%> /></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_REPAS_CMS" /></TD>
			<TD><INPUT type="checkbox" name="recoitRepasCMS" <%=viewBean.getRecoitRepasCMS().booleanValue()==true?"CHECKED":""%> /></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_PAS_REPAS" /></TD>
			<TD><INPUT type="text" name="raisonPasRepas" value="<%=viewBean.getRaisonPasRepas()%>" /></TD>
		</TR>			
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_REMUNEREE" /></TD>
			<TD><INPUT type="checkbox" name="aideRemunereNecessaire" <%=viewBean.getAideRemunereNecessaire().booleanValue()==true?"CHECKED":""%> /></TD>
		</TR>	
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_HEURES_REMUNERE" /></TD>
			<TD><INPUT type="text" name="heuresMoisRemunere" value="<%=viewBean.getHeuresMoisRemunere()%>" /></TD>
		</TR>											
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD colspan="6"><B><U><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_REEVALUATION" /></U></B></TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>				
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_DD" /></TD>
			<TD colspan="6"><input data-g-calendar=" "  name="aideDureeDeterminee" value="<%=viewBean.getAideDureeDeterminee()%>"/></TD>
		</TR>			 
		<TR>
			<TD><ct:FWLabel key="JSP_RF_ATTESTATION_MD_AIDE_DI" /></TD>
			<TD colspan="6"><input data-g-calendar=" "  name="aideDureeIndeterminee" value="<%=viewBean.getAideDureeIndeterminee()%>"/></TD>
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