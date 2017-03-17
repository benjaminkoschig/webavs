<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.conventions.RFConventionViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>
<%
	idEcran="PRF0022";

	RFConventionViewBean viewBean = (RFConventionViewBean) session.getAttribute("viewBean");		
	
 	rememberSearchCriterias = true;
 	bButtonNew = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>

<SCRIPT language="JavaScript">
	
	bFind = false;
	
	usrAction = "<%=IRFActions.ACTION_CONVENTION%>.lister";

	actionNew = "<%=IRFActions.ACTION_CONVENTION%>.afficher";

	function clearFields() {			
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		
		document.getElementsByName("codeTypeDeSoin")[0].value="";
		document.getElementsByName("codeSousTypeDeSoin")[0].value="";
		document.getElementsByName("codeTypeDeSoinList")[0].value="";
		document.getElementsByName("codeSousTypeDeSoinList")[0].value="";
		document.getElementsByName("forOrderBy")[0].value="";
		document.forms[0].elements('partiallikeNumeroAVS').focus();
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
		document.getElementsByName("forLibelle")[0].value="";
		document.getElementsByName("forCsSexe")[0].value="";		
		document.getElementsByName("forFournisseur")[0].value="";
	}

	// on ne set pas les champs depuis la request dans cet écran
	function initRc( ) {	
		try {	
			//clearFields();			
		} catch (ex) {}
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_CONV_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
<TD>
		<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
			<TR>
				<TD><ct:FWLabel key="JSP_RF_CONV_GESTIONNAIRE" /></TD>			
				<TD>				
					<ct:FWListSelectTag data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getSession().getUserId()%>" name="idGestionnaire"/>
				</TD>													
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_CONV_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
				<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
			  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RFNSSDTO) {%>										
				<TD>
					<ct1:nssPopup avsMinNbrDigit="99"
							  nssMinNbrDigit="99"
							  newnss="<%=viewBean.isNNSS(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>"
							  name="likeNumeroAVS"
							  onChange=""
							  value="<%=NSUtil.formatWithoutPrefixe(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>"/>
				</TD>
				<%}else{ %>
				<TD>
					<ct1:nssPopup avsMinNbrDigit="99"
							  nssMinNbrDigit="99"
							  name="likeNumeroAVS"
							  value=""/>
				</TD>
				<%} %>

				<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_CONV_NOM"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
				<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_CONV_PRENOM"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="text" name="likePrenom" onchange="likePrenomChange();" value=""></TD>
			</TR>		
			<TR>
				<TD colspan="2">&nbsp;</TD>
				<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_CONV_NAISSANCE"/></LABEL>&nbsp;</TD>
				<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>
				<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_CONV_SEXE"/></LABEL>&nbsp;</TD>	
				<TD><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>			
			<TR>
				<TD><LABEL for="forLibelle"><ct:FWLabel key="JSP_RF_CONV_LIBELLE"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="text" name="forLibelle" value="" ></TD>
				<TD><LABEL for="forFournisseur"><ct:FWLabel key="JSP_RF_CONV_FOURNISSEUR"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="text" name="forFournisseur" value=""></TD>
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
			<TR><TD><INPUT type="hidden" name="isSaisieDemande" value="false"/>
			<INPUT type="hidden" name="isEditSoins" value="false"/>
			<INPUT type="hidden" name="isSaisieQd" value="false"/></TD></TR>			
			<TR><TD colspan="6">&nbsp;</TD></TR>			
			<TR>				
				<TD><LABEL for="forActif"><ct:FWLabel key="JSP_RF_CONV_ACTIF"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="checkbox" name="forActif" CHECKED ></TD>
				<TD><LABEL for="forParConvention"><ct:FWLabel key="JSP_RF_CONV_PAR_CONVENTION"/></LABEL>&nbsp;</TD>
				<TD><INPUT type="checkbox" name="forParConvention" <%=viewBean.getForParConvention().booleanValue()==true?"CHECKED":""%> ></TD>
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_TRI"/></TD>
				<TD colspan="5"><ct:FWListSelectTag data="<%=viewBean.getConvOrderByData()%>" defaut="" name="forOrderBy" /></TD>
			</TR>	
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>			
				<TD>
					<input 	type="button" 
							onclick="clearFields()" 
							accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
							value="<ct:FWLabel key="JSP_EFFACER"/>">
					<label>
						[ALT+<ct:FWLabel key="AK_EFFACER"/>]
					</label>
				</TD>
			</TR>
		</TABLE>
	</TD>	
</TR>		
	<%-- /tpl:put --%>
						
<%@ include file="/theme/find/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>			
	<%-- /tpl:put --%>				
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>