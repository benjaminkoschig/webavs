<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.cygnus.vb.qds.RFSaisieQdViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_QD_S"
	idEcran="PRF0016";

	RFSaisieQdViewBean viewBean = (RFSaisieQdViewBean) session.getAttribute("viewBean");
	
	autoShowErrorPopup = true;
	
	bButtonDelete 	= false;
	bButtonUpdate 	= false;
	bButtonValidate = false;
	bButtonCancel 	= false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsqds">
</ct:menuChange>

<script language="JavaScript">

function cancel() {
		
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE%>.chercher";
		
    	return true;
}

function validate() {
	
    state = true;
    document.forms[0].elements('_method').value = "add";
    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_CHOIX_GENRE%>.afficher";
	
    return state;
}

function del() {
}

function add() {
}

function init(){
	
	 document.forms[0].elements('_method').value = "add";

	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	setFieldsError();
	<%}%>
	
}

function postInit(){
	setFields();
}

function setFieldsError(){
		
    	document.getElementById("csNationaliteAffiche").value="";
    	document.getElementById("nomAffiche").value="";
    	document.getElementById("prenomAffiche").value="";
    	document.getElementById("csCantonAffiche").value="";
    	document.getElementById("csSexeAffiche").value="";
    	document.getElementById("dateNaissanceAffiche").value="";
    	document.getElementById("dateDecesAffiche").value="";
    	document.getElementById("likeNSS").value="";
}

function setFields(){
        
	    document.getElementById("csNationaliteAffiche").value="";
	    document.getElementById("csNationaliteAffiche").readOnly=true;
	    document.getElementById("csNationaliteAffiche").disabled=true;

	    document.getElementById("csSexeAffiche").value="";
	    document.getElementById("csSexeAffiche").readOnly=true;
    	document.getElementById("csSexeAffiche").disabled=true;
    	
    	document.getElementById("csCantonAffiche").value="";
	    document.getElementById("csCantonAffiche").readOnly=true;
    	document.getElementById("csCantonAffiche").disabled=true;

	    document.getElementById("partiallikeNSS").value="";
	    document.getElementById("partiallikeNSS").disabled=false;
	    
	    document.getElementById("idGestionnaire").disabled=false;
}

function nssFailure() {
	document.getElementById("idTiers").value=null;
	document.getElementById("nss").value=null;
}

function nssUpdateHiddenFields() {
	document.getElementById("nom").value=document.getElementById("nomAffiche").value;
	document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
	document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;
	document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;
	document.getElementById("dateDeces").value=document.getElementById("dateDecesAffiche").value;
	document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;
	document.getElementById("csCanton").value=document.getElementById("csCantonAffiche").value;
	document.getElementById("noAVS").value=document.getElementById("likeNSS").value;
}

function nssChange(tag) {

	if(tag.select==null) {

	}else {
		var element = tag.select.options[tag.select.selectedIndex];

		if (element.nss!=null){
			document.getElementById("nss").value=element.nss;
		}

		if (element.nom!=null) {
			document.getElementById("nom").value=element.nom;
			document.getElementById("nomAffiche").value=element.nom;
		}

		if (element.prenom!=null){
			document.getElementById("prenom").value=element.prenom;
			document.getElementById("prenomAffiche").value=element.prenom;
		}

		if (element.provenance!=null){
			document.getElementById("provenance").value=element.provenance;
		}

		if (element.codeSexe!=null) {
			for (var i=0; i < document.getElementById("csSexeAffiche").length ; i++) {
				if (element.codeSexe==document.getElementById("csSexeAffiche").options[i].value) {
					document.getElementById("csSexeAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csSexe").value=element.codeSexe;
		}
		
		if (element.id!=null){
			document.getElementById("idTiers").value=element.idAssure;
		}

		if (element.dateNaissance!=null){
			document.getElementById("dateNaissance").value=element.dateNaissance;
			document.getElementById("dateNaissanceAffiche").value=element.dateNaissance;
		}

		if (element.dateDeces!=null){
			document.getElementById("dateDeces").value=element.dateDeces;
			document.getElementById("dateDecesAffiche").value=element.dateDeces;
		}

		if (element.codePays!=null) {
			for (var i=0; i < document.getElementById("csNationaliteAffiche").length ; i++) {
				if (element.codePays==document.getElementById("csNationaliteAffiche").options[i].value) {
					document.getElementById("csNationaliteAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csNationalite").value=element.codePays;
		}

		if (element.codeCantonDomicile!=null) {
			for (var i=0; i < document.getElementById("csCantonAffiche").length ; i++) {
				if (element.codeCantonDomicile==document.getElementById("csCantonAffiche").options[i].value) {
					document.getElementById("csCantonAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csCanton").value=element.codeCantonDomicile;
		}

		if ('<%=globaz.prestation.interfaces.util.nss.PRUtil.PROVENANCE_TIERS%>'==element.provenance) {
			document.getElementById("nomAffiche").disabled=true;
			document.getElementById("prenomAffiche").disabled=true;
			document.getElementById("csSexeAffiche").disabled=true;
			document.getElementById("dateNaissanceAffiche").disabled=true;
			document.getElementById("dateDecesAffiche").disabled=true;
			document.getElementById("csNationaliteAffiche").disabled=true;
			document.getElementById("csCantonAffiche").disabled=true;
		}
	}
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_QD_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_GESTIONNAIRE"/></TD>
					<TD>
						<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getSession().getUserId()%>"/>
					</TD>
				</TR>
				<TR><TD>&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD>&nbsp;</TD></TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_NSS"/></TD>
					<TD>
						<%
							String params = "&provenance1=TIERS";
							String jspLocation = servletContext + "/cygnusRoot/numeroSecuriteSocialeSF_select.jsp";
						%>
	
							<ct1:nssPopup name="likeNSS" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>"
											  value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss=""
											  jspName="<%=jspLocation%>" 
											  avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"/>
	
							<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>"/>
							<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>
							<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
					</TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_NOM"/></TD>
					<TD>
						<INPUT type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
						<INPUT type="text" name="nomAffiche" value="<%=viewBean.getNom()%>" disabled="true" readonly/>
					</TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_PRENOM"/></TD>
					<TD>
					    <INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
						<INPUT type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>" disabled="true" readonly/>
					</TD>
				</TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_NAISSANCE"/></TD>
					<TD>
						<INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
						<INPUT type="text" name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>" disabled="true" readonly/>
	
					</TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_SEXE"/></TD>
					<TD>
						 <ct:FWCodeSelectTag name="csSexeAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYSEXE"
									      	defaut="<%=viewBean.getCsSexe()%>"/>
                        <INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
					</TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_NATIONALITE"/></TD>
					 <TD>
                        <ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite())?TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
                        <INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>"/>
                    </TD>
				</TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_CANTON_DOMICILE"/></TD>
					<TD>
						<ct:select name="csCantonAffiche" defaultValue="<%=viewBean.getCsCanton()%>" wantBlank="true" disabled="disabled" >
							<ct:optionsCodesSystems csFamille="PYCANTON">
							</ct:optionsCodesSystems>
						</ct:select>
						<INPUT type="hidden" name="csCanton" value="<%=viewBean.getCsCanton()%>"/>
					</TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_DECES"/></TD>
					<TD>
						<INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>"/>
						<INPUT type="text" name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>" disabled="true" readonly/>
					</TD>
					<TD colspan="2">&nbsp;</TD>
				</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ANNULER"/>" onclick="if(cancel()) action(COMMIT);">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/>" onclick="if(validate()) action(COMMIT);">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>