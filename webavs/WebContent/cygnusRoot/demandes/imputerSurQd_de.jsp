<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.cygnus.vb.demandes.RFImputerSurQdViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.demandes.RFImputerSurQdViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
    //Les labels de cette page commence par le préfix "JSP_RF_DEM_IMPUTER_SUR_QD_"
    idEcran="";

	RFImputerSurQdViewBean viewBean = (RFImputerSurQdViewBean) session.getAttribute("viewBean");
    
    autoShowErrorPopup = true;
    
    bButtonDelete = false;
    bButtonUpdate = true;
    bButtonValidate = true;
    bButtonCancel = true;
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
              // document.forms[0].elements[i].name != 'csNationaliteAffiche' &&
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
    
    function upd() {
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_IMPUTER_SUR_QD%>.modifier";
		return true;
    }
    
    function init(){

        //document.forms[0].elements('_method').value = "add";
        
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
		
    	//document.getElementById("csNationaliteAffiche").value="";

    }

    function setFields(){
        
    	document.getElementById("idGestionnaire").disabled=true;
    	document.getElementById("idGestionnaire").readOnly=true;
	    
    }
    
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
            <%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_DEM_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
 <TR>
     <TD><ct:FWLabel key="JSP_RF_DEM_IMPUTER_SUR_QD_GESTIONNAIRE"/></TD>
     <TD colspan="5">
         <ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
         																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
     </TD>
 </TR>
 <TR><TD colspan="6">&nbsp;</TD></TR>
 <TR>
 	<TD>
 		<ct:FWLabel key="JSP_RF_DEM_IMPUTER_SUR_QD_ID_DEMANDE"/>
 	</TD>
 	<TD colspan="5">
 		<%=viewBean.getIdDemande()%>
 	</TD>
 </TR>
 <TR><TD colspan="6">&nbsp;</TD></TR>
 <TR>
     <TD ><ct:FWLabel key="JSP_RF_DEM_IMPUTER_SUR_QD_ID_QD"/></TD>
     <TD colspan="5">
         <INPUT type="text" name="idQdPrincipale" value="<%=viewBean.getIdQdPrincipale()%>"/>
     </TD>
 </TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
                <%-- tpl:put name="zoneButtons" --%>
                <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>