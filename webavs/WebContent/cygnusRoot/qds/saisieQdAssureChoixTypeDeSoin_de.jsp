<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.qds.RFQdAbstractViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.cygnus.vb.qds.RFSaisieQdViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_QD_S"
	idEcran="PRF0018";

	RFSaisieQdViewBean viewBean = (RFSaisieQdViewBean) session.getAttribute("viewBean");
	
	//autoShowErrorPopup = true;
	
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

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsqds">
</ct:menuChange>

<script language="JavaScript">

<%@ include file="../utils/rfUtilsQd.js" %>

function readOnly(flag) {
	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
  for(i=0; i < document.forms[0].length; i++) {
      if (!document.forms[0].elements[i].readOnly &&
	       document.forms[0].elements[i].type != 'hidden') {
          
          document.forms[0].elements[i].disabled = flag;
      }
  }
}

function cancel() {
    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE%>.chercher";
}  

function onChangeGenreQd() {
}

function validate() {
}

function upd(){
}



function init(){
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	<%}%>
}

function postInit(){
	action('add');

	document.getElementById("csGenreQdBis").disabled=true;
	document.getElementById("csGenreQdBis").readOnly=true;
	
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_QD_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
					<TD colspan="5">
						<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getSession().getUserId()%>"/>
						<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>"/>
						<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>
						<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
						<INPUT type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
						<INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
						<INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
						<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
						<INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>"/>
						<INPUT type="hidden" name="csCanton" value="<%=viewBean.getCsCanton()%>"/>
						<INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>"/>
						<INPUT type="hidden" name="idDossier" value="<%=viewBean.getIdDossier()%>"/>
						<INPUT type="hidden" name="isPlafonnee" value="true"/>
						<INPUT type="hidden" name="idQd" value="<%=viewBean.getIdQdAssure()%>"/>
						<INPUT type="hidden" name="csGenreQd" value="<%=viewBean.getCsGenreQd()%>"/>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_DEM_S_TIERS"/></TD>
					<TD colspan="5">
						<%=viewBean.getDetailAssure()%>
					</TD>
				</TR>
				<TR><TD>&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD>&nbsp;</TD></TR>
                
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_GENRE_QD"/></TD>
	                <TD colspan="5">
	                		<SELECT name="csGenreQdBis" onchange="onChangeTypeQd()">
	                        <%Vector genreQdData = viewBean.getGenresQdData();
	                          for (int i=0;i<genreQdData.size();i++){
	                          	 if(((String[]) genreQdData.get(i))[0].equals(viewBean.getCsGenreQd())){%>
	                        		<OPTION value="<%=((String[]) genreQdData.get(i))[0]%>" selected><%=((String[]) genreQdData.get(i))[1]%></OPTION>
	                        	<%}else{%>
									<OPTION value="<%=((String[]) genreQdData.get(i))[0]%>"><%=((String[]) genreQdData.get(i))[1]%></OPTION>
	                        	<%}%>
	                        <%}%>
	                     </SELECT>
	                </TD>
                </TR>
                
                
               	<TR><TD colspan="6">&nbsp;</TD></TR>
               	<TR><TD colspan="6"><HR></HR></TD></TR>
               	<TR><TD colspan="6">&nbsp;</TD></TR>
               	
	            <%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
                <TR><TD><INPUT type="hidden" name="isSaisieDemande" value="false"/></TD></TR>
                <TR><TD><INPUT type="hidden" name="isEditSoins" value="false"/></TD></TR>
                <TR><TD><INPUT type="hidden" name="isSaisieQd" value="true"/></TD></TR>
                
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>