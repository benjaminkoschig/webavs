<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.conventions.RFConventionViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>
<%
	idEcran="PRF0021";
	RFConventionViewBean viewBean = (RFConventionViewBean) session
			.getAttribute("viewBean");
	
	autoShowErrorPopup = true;
	
	bButtonValidate = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	function cancel(){
		//si on Cancel il faut revenir avec le viewBean RFSaisieConvention
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_CONVENTION%>.chercher";   	
	}	

	function del() {
	    if (window.confirm("<ct:FWLabel key='WARNING_RF_CONV_SUPPRESSION_CONVENTION'/>")){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_CONVENTION%>.supprimer";
	        document.forms[0].submit();
	    }
	}	

	// prépare pour enregistement 
	function validate() {				
	    state = validateFields();   
		parent.isNouveau = false;
		parent.isModification = false; 	    

		//dans tous les cas il faut remplacer les caractères " et ' dans le libellé				
		var str = $('[name=libelle]').val();				
		
		while(str.indexOf('"')>-1){str=str.replace('"','');}
		while(str.indexOf("'")>-1){str=str.replace("'","");}		
		
		$('[name=libelle]').val(str);
		
	    if (document.forms[0].elements('_method').value == "add"){		   
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_CONVENTION%>.ajouter";	    
	    }  
	    else{
		    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_CONVENTION%>.modifier";	    
	    }  	      
	    return state;
	
	}

	function init(){
		parent.isNouveau = true;
		<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
		errorObj.text="<%=viewBean.getMessage()%>";
		showErrors();
		errorObj.text="";
		<%}%>		
	}
	
	function postInit(){
	    <%if (((RFConventionViewBean)viewBean).getIsAjout()){%>		
			action('add');
		<%}%>	    
	}

	function add() {
	    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_CONVENTION%>.ajouter"
	}

	function upd(){
		 parent.isModification = true;
	}


</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_SAISIE_CONVENTION_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_GESTIONNAIRE" /></TD>
	<TD>	
        <ct:FWListSelectTag name="idGestionnaire" 
        data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
        defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
        	viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
	</TD>	
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_LIBELLE" /></TD>
	<TD>
		<INPUT style="width: 400px;" type="text" name="libelle" value="<%=viewBean.getLibelle()%>" /> 
	</TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_DATE_CREATION" /></TD>
	<TD>
		<input data-g-calendar=" "  name="forDateCreation" value="<%=viewBean.getDateCreation()%>"/>
	</TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_ACTIF" /></TD>
	<TD>
		<INPUT type="checkbox" name="forActif" <%="add".equals(request.getParameter("_method")) || viewBean.getIsActif().booleanValue()==true?"CHECKED":""%>/>
	</TD>	
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>	
	<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_SUIVANT"/> (alt+<ct:FWLabel key="AK_REQ_SUIVANT"/>)" onclick="if(validate()) action(COMMIT);" accesskey="<ct:FWLabel key="AK_REQ_SUIVANT"/>">
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>