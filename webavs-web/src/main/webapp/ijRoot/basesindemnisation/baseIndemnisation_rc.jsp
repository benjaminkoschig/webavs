<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersViewBean"%>
<%@page import="globaz.ij.vb.basesindemnisation.IJBaseIndemnisationViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0024";
	
	rememberSearchCriterias = true;
	
//action new redéfini car le userAction dans la requête n'est pas forcément le bon	
	IJBaseIndemnisationJoinTiersViewBean viewBean = (IJBaseIndemnisationJoinTiersViewBean) request.getAttribute("viewBean");

	String idPrononce = "".equals(viewBean.getIdPrononce())?request.getParameter("idPrononce"):viewBean.getIdPrononce();
	
	if ("".equals(viewBean.getIdPrononce())){
		viewBean.setIdPrononce(idPrononce);
	}
	
	String csTypeIJ = "".equals(viewBean.getCsTypeIJ())?request.getParameter("csTypeIJ"):viewBean.getCsTypeIJ();
	
	if(IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)||
			IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)||
			IIJPrononce.CS_FPI.equals(viewBean.getCsTypeIJ())){
		//actionNew =  servletContext + mainServletPath + "?userAction=ij.basesindemnisation.baseIndemnisation.afficher&_method=add&idPrononce=" + idPrononce + "&csTypeIJ=" +csTypeIJ;
		//actionNew =  servletContext + mainServletPath + "?userAction=ij.controleAbsences.dossierControleAbsencesAjax.ajouter&idPrononce=" + idPrononce + "&csTypeIJ=" +csTypeIJ + "&idTiers=" + viewBean.getIdTiers();
		//actionNew =  servletContext + mainServletPath + "?userAction=ij.controleAbsences.dossierControleAbsencesAjax.ajouter&idPrononce=" + idPrononce + "&csTypeIJ=" +csTypeIJ; //r&idTiers=" + viewBean.getIdTiers() + "&idBaseIndemnisation=" + viewBean.getIdBaseIndemisation();
		actionNew =  servletContext + mainServletPath + "?userAction=ij.controleAbsences.dossierControleAbsencesAjax.afficher&idTiers=" + viewBean.getIdTiers() + "&idPrononce=" + idPrononce + "&csTypeIJ=" +csTypeIJ;// + "&idBaseIndemnisation=" + viewBean.getIdBaseIndemisation();
	}else{
		actionNew =  servletContext + mainServletPath + "?userAction=ij.basesindemnisation.baseIndemnisationAitAa.afficher&_method=add&idPrononce=" + idPrononce + "&csTypeIJ=" +csTypeIJ;
	}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "ij.basesindemnisation.baseIndemnisation.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_BASES_INDEMNISATION"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD><TABLE width="100%">
						<TR>
							<TD><LABEL for="fromDateDebut"><ct:FWLabel key="JSP_BASE_IND_A_PARTIR_DE_DATE_DEBUT"/></LABEL></TD>
							<TD><ct:FWCalendarTag name="fromDateDebut" value=""/></TD>

							<TD valign="top"><LABEL for="forIdPrononce"><ct:FWLabel key="JSP_BASE_IND_NO_PRONONCE"/></LABEL></TD>
							<TD valign="top"><span>							
										<%="".equals(viewBean.getFullDescriptionPrononce()[0])?request.getParameter("descr0"):viewBean.getFullDescriptionPrononce()[0]%><br>
									  	<%=viewBean.getDetailRequerant()%></span>
								<INPUT type="hidden" name="forIdPrononce" value="<%=idPrononce%>">
								<INPUT type="hidden" name="csTypeIJ" value="<%=csTypeIJ%>">	
								<INPUT type="hidden" name="hasPostitField" value="<%=true%>">			
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ETAT"/>&nbsp;</TD>
							<TD>
									<SELECT name="forCsEtat">
										<OPTION value=""><ct:FWLabel key=""/></OPTION>
										<OPTION value="<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_OUVERT%>"><ct:FWLabel key="JSP_OUVERT"/></OPTION>
										<OPTION value="<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_VALIDE%>"><ct:FWLabel key="JSP_VALIDE"/></OPTION>								
										<OPTION value="<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_COMMUNIQUE%>"><ct:FWLabel key="JSP_COMMUNIQUE"/></OPTION>
										<OPTION value="<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_ANNULE%>"><ct:FWLabel key="JSP_ANNULE"/></OPTION>	
									</SELECT>
							</TD>
						<TR>
						</TR>
						<tr>	
							<TD><LABEL><ct:FWLabel key="JSP_BASE_IND_TRIER_PAR"/></LABEL></TD>
							<TD colspan="4">

								<SELECT name="orderBy"> 
									<OPTION value="<%=globaz.ij.db.basesindemnisation.IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE%>" SELECTED><ct:FWLabel key="JSP_BASE_IND_DATE_DEBUT"/></OPTION>
									<OPTION value="<%=globaz.ij.db.basesindemnisation.IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION%>"><ct:FWLabel key="JSP_BASE_IND_NO"/></OPTION>																											
								</SELECT>							
							</TD>
						</TR>
						
						
					
						</TABLE></TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>