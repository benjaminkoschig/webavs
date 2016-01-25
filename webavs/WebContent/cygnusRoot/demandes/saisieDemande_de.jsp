<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean"%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
    //Les labels de cette page commence par le préfix "JSP_RF_DOS_D"
    idEcran="PRF0004";

	RFSaisieDemandeViewBean viewBean = (RFSaisieDemandeViewBean) session.getAttribute("viewBean");
    
    autoShowErrorPopup = true;
    
    bButtonDelete = false;
    bButtonUpdate = false;
    bButtonValidate = false;
    bButtonCancel = false;
    bButtonNew = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" />
<ct:menuChange displayId="options" menuId="cygnus-optionsdemandes" showTab="options"/>

<script language="JavaScript">

    function readOnly(flag) {
    	
    	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
      	for(i=0; i < document.forms[0].length; i++) {
          if (!document.forms[0].elements[i].readOnly &&
        	   document.forms[0].elements[i].name != 'nom' &&
        	   //document.forms[0].elements[i].name != 'csNationaliteAffiche' &&
               //document.forms[0].elements[i].name != 'csSexeAffiche' &&
               //document.forms[0].elements[i].name != 'csCantonAffiche' &&
               document.forms[0].elements[i].type != 'hidden') {
              
              document.forms[0].elements[i].disabled = flag;
          }
      	}
    }
    
    function cancel() {
    	
    	if (document.forms[0].elements('_method').value == "add"){
            document.forms[0].elements('userAction').value="back";
        }else{
            document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_DEMANDE%>.rechercher";
        }
    }  
    
    function validate() {
        return true;
    }    
    
    function del() {
    }

    function add() {
    }
    
    function init(){
    	
    	document.forms[0].elements('_method').value = "add";
        
        <%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
        errorObj.text="<%=viewBean.getMessage()%>";
        showErrors();
        errorObj.text="";
        setFieldsError();
        <%if (viewBean.isPreparerDecisonDemarre()){%>
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS%>.chercher";
        action(COMMIT);
        <%}}%>
        
    }
    
    function postInit(){
    	    	
    	setFields();
    }

	function setFieldsError(){
		
		document.getElementById("idGestionnaire").value="";
		document.getElementById("nom").value="";
 		<%--document.getElementById("csNationaliteAffiche").value="";
    		document.getElementById("nomAffiche").value="";
    		document.getElementById("prenomAffiche").value="";
    		document.getElementById("csCantonAffiche").value="";
    		document.getElementById("csSexeAffiche").value="";
    		document.getElementById("dateNaissanceAffiche").value="";
    		document.getElementById("dateDecesAffiche").value="";
    		document.getElementById("likeNSS").value="";
    		document.getElementById("codeTypeDeSoin").value="";
        	document.getElementById("codeSousTypeDeSoin").value="";
 		--%>   
 	}

    function setFields(){
    	<%if(!RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE.equals(viewBean.getTypeValidation())){%>
			
    		//alert("entrée dans setFields() : nouvelle saisie");
    	
    		document.getElementById("nom").disabled=false;
    		document.getElementById("nom").readOnly=false;
    		document.getElementById("idGestionnaire").disabled=false;
			document.getElementById("idGestionnaire").readOnly=false;
		 <%}else{%>
	
			//alert("entrée dans setFields() : nouvelle saisie, même assuré");		
	
			document.getElementById("idGestionnaire").disabled=false;
			//document.getElementById("idGestionnaire").readOnly=false;
			document.getElementById("nom").disabled=true;
    		//document.getElementById("nom").readOnly=true;
	    <%}%>
        document.getElementById("codeTypeDeSoinList").disabled=false;
        document.getElementById("codeSousTypeDeSoinList").disabled=false;
        document.getElementById("codeTypeDeSoin").disabled=false;
        document.getElementById("codeSousTypeDeSoin").disabled=false;
    }
    
	function nssFailure() {
        document.getElementById("idTiers").value=null;
        document.getElementById("nss").value=null;        
    }
   
    function nssUpdateHiddenFields() {
    	
    	document.getElementById("nom").value=document.getElementById("nom").value;
 		<%--document.getElementById("nom").value=document.getElementById("nomAffiche").value;
    	   	document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
           	document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;
       	   	document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;
           	document.getElementById("dateDeces").value=document.getElementById("dateDecesAffiche").value;
           	document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;
           	document.getElementById("csCanton").value=document.getElementById("csCantonAffiche").value;
           	document.getElementById("noAVS").value=document.getElementById("likeNSS").value;
  		--%>  
  	}
    
<%--function nssChange(tag) {
    
        if(tag.select==null) {
    
        }else {
            var element = tag.select.options[tag.select.selectedIndex];
    
            if (element.idTiers!=null){
                document.getElementById("idTiers").value=element.idTiers;
            }
          
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
--%>  

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
            <%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_DEM_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
                <TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
                    <TD>
                        <ct:FWListSelectTag 
                        	name="idGestionnaire" 
                        	data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
                        	defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
                        	<INPUT type="hidden" id="idGestionnaire" name="idGestionnaire" value="<%=viewBean.getGestionnaire()%>" />	
                    </TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_DEM_S_TIERS"/></TD>
                	<TD>
                		<div data-g-multiwidgets="languages:¦'T'¦,mandatory:true" class="multiWidgets" >
                       	<input id="nom" <%--id="widget5-multiwidget"--%>
                       			class="widgetTiers" 
                       			name="nom"
                       			value="<%=viewBean.getNom()%>"
                    			data-g-autocomplete="manager:¦globaz.cygnus.db.decisions.RFTiersManager¦,
                                                method:¦find¦,
                                                criterias:¦{
                                                    likeNom: 'Nom',
                                                    likePrenom: 'Prénom',
                                                    likeNumeroNSS: 'Numéro AVS',
                                                    forDateNaissance: 'Date de naissance',
                                                    likeAlias: 'Alias'
                                                }¦,
                                                lineFormatter:¦<b>#{nom} #{prenom}</b> #{datenaissance} #{numero_nss} #{alias}¦,
                                                modelReturnVariables:¦nom,prenom,numero_nss,idTiers¦,
                                                functionReturn:¦
                                                    function(element){
                                                    $('#idTiers').val($(element).attr('idTiers'));
                                                        this.value=$(element).attr('nom')+' '+$(element).attr('prenom');
                                                    }
                                                ¦" type="text">
                    	</div>
                    	<INPUT type="hidden" id="idTiers" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
                    	<INPUT type="hidden" id="nom" name="nom" value="<%=viewBean.getNom()%>" />
                    	<INPUT type="hidden" id="prenom" name="prenom" value="<%=viewBean.getPrenom()%>" />
           			</TD>
            	</TR>
                
 <%------------------------------------------------------------------------------------- --%>
<%-- 
                <TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_NSS"/></TD>
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
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_NOM"/></TD>
                    <TD>
                        <INPUT type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
                        <INPUT type="text" name="nomAffiche" value="<%=viewBean.getNom()%>" disabled="true" readonly/>
                    </TD>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_PRENOM"/></TD>
                    <TD>
                        <INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
                        <INPUT type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>" disabled="true" readonly/>
                    </TD>
                </TR>
                <TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_NAISSANCE"/></TD>
                    <TD>
                        <INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
                        <INPUT type="text" name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>" disabled="true" readonly/>
    
                    </TD>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_SEXE"/></TD>
                    <TD>
                            <ct:FWCodeSelectTag name="csSexeAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYSEXE"
									      	defaut="<%=viewBean.getCsSexe()%>"/>
                        <INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
                    </TD>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_NATIONALITE"/></TD>
                    <TD>
                        <ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite())?TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
                        <INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>"/>
                    </TD>
                </TR>
                <TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_CANTON_DOMICILE"/></TD>
                    <TD>
                        <ct:select name="csCantonAffiche" defaultValue="<%=viewBean.getCsCanton()%>" wantBlank="true" disabled="disabled" >
                            <ct:optionsCodesSystems csFamille="PYCANTON">
                            </ct:optionsCodesSystems>
                        </ct:select>
                        <INPUT type="hidden" name="csCanton" value="<%=viewBean.getCsCanton()%>"/>
                    </TD>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_DECES"/></TD>
                    <TD>
                        <INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>"/>
                        <INPUT type="text" name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>" disabled="true" readonly/>
                    </TD>
                    <TD colspan="2">&nbsp;</TD>
                </TR>
--%>                
                <TR><TD>&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD>&nbsp;</TD></TR>
                
                <%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
                <TR><TD><INPUT type="hidden" name="isSaisieDemande" value="true"/></TD></TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
                <%-- tpl:put name="zoneButtons" --%>
                <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>