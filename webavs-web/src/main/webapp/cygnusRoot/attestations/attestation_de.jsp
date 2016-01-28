<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
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
	idEcran="PRF0036";
	RFAttestationViewBean viewBean = (RFAttestationViewBean) session
			.getAttribute("viewBean");

	
	autoShowErrorPopup = true;		
	
	bButtonDelete = false;	
	bButtonUpdate = false;
	bButtonNew = false;
	bButtonValidate = true;
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
	}
	
	function init(){
        document.forms[0].elements('_method').value = "add";	
	}
	
	function validate(){	
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE%>.afficher";	
		document.forms[0].submit();
	}

	function displayAndChoose()
	{			
		// si type et sous type de soin sont non vide alors on filtre la liste type de document			
		if($('[name=codeTypeDeSoinList]').val()=="02" && $('[name=codeSousTypeDeSoinList]').val()=="01")
			$('[name=csTypeAttestation]')[0].selectedIndex=1;
		else if(($('[name=codeTypeDeSoinList]').val()=="03" && 
						($('[name=codeSousTypeDeSoinList]').val()=="01" ||
						$('[name=codeSousTypeDeSoinList]').val()=="02" || 
						$('[name=codeSousTypeDeSoinList]').val()=="03" || 
						$('[name=codeSousTypeDeSoinList]').val()=="04" || 
						$('[name=codeSousTypeDeSoinList]').val()=="05" || 
						$('[name=codeSousTypeDeSoinList]').val()=="06"  ))
				|| ($('[name=codeTypeDeSoinList]').val()=="02")
				|| ($('[name=codeTypeDeSoinList]').val()=="08")
				|| ($('[name=codeTypeDeSoinList]').val()=="09" && 
						($('[name=codeSousTypeDeSoinList]').val()=="01" ||
						$('[name=codeSousTypeDeSoinList]').val()=="02" || 
						$('[name=codeSousTypeDeSoinList]').val()=="03" ))					
				|| ($('[name=codeTypeDeSoinList]').val()=="13" && 
						($('[name=codeSousTypeDeSoinList]').val()=="02" ||
						$('[name=codeSousTypeDeSoinList]').val()=="05" ))
				|| ($('[name=codeTypeDeSoinList]').val()=="16" && 
						($('[name=codeSousTypeDeSoinList]').val()=="01" ||
						$('[name=codeSousTypeDeSoinList]').val()=="02" || 
						$('[name=codeSousTypeDeSoinList]').val()=="03" || 
						$('[name=codeSousTypeDeSoinList]').val()=="04" ))
				)
			$('[name=csTypeAttestation]')[0].selectedIndex=3;
		else if($('[name=codeTypeDeSoinList]').val()=="05")
			$('[name=csTypeAttestation]')[0].selectedIndex=4;
		else if($('[name=codeTypeDeSoinList]').val()=="09" ||
				($('[name=codeSousTypeDeSoinList]').val()=="10" && $('[name=codeSousTypeDeSoinList]').val()=="01"))
			$('[name=csTypeAttestation]')[0].selectedIndex=5;
		else if($('[name=codeTypeDeSoinList]').val()=="11")
			$('[name=csTypeAttestation]')[0].selectedIndex=6;
		else if($('[name=codeTypeDeSoinList]').val()=="13")
			$('[name=csTypeAttestation]')[0].selectedIndex=2;
		else if($('[name=codeTypeDeSoinList]').val()=="13" && 
				$('[name=codeSousTypeDeSoinList]').val()=="02")
			$('[name=csTypeAttestation]')[0].selectedIndex=7;

	}

	function add(){
	}

	function upd(){
	}	

	function cancel(){
	}

	function postInit(){	   	
		action('add');
		displayAndChoose();

	    typeSousTypeDeSoinListInit();
	    
        <%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
        errorObj.text="<%=viewBean.getMessage()%>";
        showErrors();
        errorObj.text="";
        <%}%>	
	}

	$(function(){
		$('[name=codeSousTypeDeSoinList]').change(function() {
			displayAndChoose();
		});	

		$('[name=codeSousTypeDeSoin]').change(function() {
			displayAndChoose();
		});
		
		/*$('[name=csTypeAttestation]').change(function() {
			displayAndChoose();
		});*/
			
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
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_INFOS_ASSURE" /></TD>
	<TD colspan="5"><%=viewBean.getDetailAssure() %></TD>
	<INPUT type="hidden" name="nss" value="<%=viewBean.getNss() %>"/>
	<INPUT type="hidden" name="nom" value="<%=viewBean.getNom() %>"/>
	<INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom() %>"/>
	<INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance() %>"/>
	<INPUT type="hidden" name="libelleCourtSexe" value="<%=viewBean.getLibelleCourtSexe() %>"/>
	<INPUT type="hidden" name="libellePays" value="<%=viewBean.getLibellePays() %>"/>
	<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe() %>"/>
	<INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite() %>"/>
	<INPUT type="hidden" name="IdGestionnaire" value="<%=viewBean.getIdGestionnaire() %>"/>
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
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_PERIODE" />
	</TD>
	<TD>
		<ct:FWLabel key="JSP_RF_ATTESTATION_FROM" />&nbsp;
		<input data-g-calendar=" " name="dateDebut" value="<%=viewBean.getDateDebut()%>" />
		&nbsp;
		<ct:FWLabel key="JSP_RF_ATTESTATION_TILL" />
		&nbsp;
		<input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/>
	</TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf" %>
   	<TD colspan="6">
     	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
     	<INPUT type="hidden" name="isEditSoins" value="false"/>
     	<INPUT type="hidden" name="isSaisieQd" value="false"/>			                		                	
   	</TD>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TYPE_DOCUMENT" /></TD>
	
	<TD><ct:FWListSelectTag data="<%=viewBean.getCsTypeAttestationData()%>" defaut="" name="csTypeAttestation" /></TD>
</TR>
<TR><TD colspan="6"><HR></HR></TD></TR>
<tr><td><input type="hidden" value="1" name="fromAttestation" /></td></tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>		
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>