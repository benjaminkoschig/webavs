<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.typeDeSoins.RFParametrageTypeSoinsRecherchePeriodeViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<script language="JavaScript" src="<%=servletContext%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js">	
</script>
<%
	idEcran="PRF0027";

	RFParametrageTypeSoinsRecherchePeriodeViewBean viewBean = (RFParametrageTypeSoinsRecherchePeriodeViewBean) request.getAttribute("viewBean");
	
	IFrameListHeight = "220";
	IFrameDetailHeight = "1000";
	
	bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>

<SCRIPT language="JavaScript">
	
	bFind = true;
	
	<%
		actionNew = actionNew + "&idSousTypeSoin="+viewBean.getIdSousTypeSoin();
	%>

	detailLink = "<%=actionNew%>";
	usrAction = "<%=IRFActions.ACTION_PARAMETRAGE_SOINS_RECHERCHE_PERIODE%>.lister";
	
	function loadFrames() {

		typeSousTypeDeSoinListInit();		
			
		if(bFind) {
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}

	
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


	

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_RECHERCHE_PERIODE_SOIN_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD  width="100%">
		<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
			<TR><TD>&nbsp;</TD></TR>					
              	<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
              	<TR>
              		<TD colspan="6">
	                	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
	                	<INPUT type="hidden" name="isEditSoins" value="true"/>
	                	<INPUT type="hidden" name="idSousTypeSoin" value="<%=viewBean.getIdSousTypeSoin()%>"/>
	                	<INPUT type="hidden" name="forIdSousTypeSoin" value="<%=viewBean.getForIdSousTypeSoin()%>"/>	
              		</TD>
              	</TR>
			<TR><TD>&nbsp;</TD></TR>		
			<TR>			
				<TD><ct:FWLabel key="JSP_RF_RECHERCHE_PERIODE_DATE_DEBUT"/>&nbsp;</TD>
				<TD><input data-g-calendar="type:month"  name="forDateDebut" value="" /></TD>
				<TD><ct:FWLabel key="JSP_RF_RECHERCHE_PERIODE_DATE_FIN"/>&nbsp;</TD>
				<TD><input data-g-calendar="type:month"  name="forDateFin" value="" /></TD>																		
			</TR>			
		</TABLE>
	</TD>
</TR>			 
	<%-- /tpl:put --%>			
<%@ include file="/theme/capage/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>				
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>