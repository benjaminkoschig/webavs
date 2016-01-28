<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.commons.nss.*"%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="GDO0000";
globaz.cepheus.vb.demande.DODemandePrestationsViewBean viewBean = (globaz.cepheus.vb.demande.DODemandePrestationsViewBean) request.getAttribute("viewBean");

bButtonNew = false;

IFrameListHeight = "200";
IFrameDetailHeight ="250";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_DEMANDE_PRESTATIONS%>.lister";
	detailLink = "<%=servletContext + mainServletPath%>?userAction=<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_DEMANDE_PRESTATIONS%>.afficher&idLot=<%=viewBean.getIdTiers()%>&choisir=<%=request.getParameter("choisir")%>";	
	
	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			// document.forms[0].submit(); appelle depuis l'ecran DE
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}
	
	function clearFields () {
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("likeNumeroAVS")[0].value="";
		document.getElementsByName("forTypeDemande")[0].value="";
		document.getElementsByName("forEtatDemande")[0].value="";
		document.getElementsByName("orderBy")[0].value="<%=globaz.cepheus.db.demande.DODemandePrestations.FIELDNAME_NUM_AVS%>";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("forCsSexe")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_DEMANDE_PRESTATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						    <TD></TD>
							<TD width="100%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="80%">
									<TR>
										<TD><ct:FWLabel key="JSP_NSS_ABREGE"/></TD>
										<TD><ct1:nssPopup avsMinNbrDigit="99" 
													  nssMinNbrDigit="99" 
													  newnss="<%=viewBean.isNNSS()%>" 
													  name="likeNumeroAVS"/></TD>
										<TD><ct:FWLabel key="JSP_NOM"/></TD>
										<TD><INPUT type="text" name="likeNom" value="<%=viewBean.getNom()%>"></TD>
										<TD><ct:FWLabel key="JSP_PRENOM"/></TD>
										<TD><INPUT type="text" name="likePrenom" value="<%=viewBean.getPrenom()%>"></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TD>
										<TD>
											<ct:FWCalendarTag name="forDateNaissance" value=""/>
										</TD>
										<TD><ct:FWLabel key="JSP_SEXE"/></TD>
										<TD>
											<ct:FWCodeSelectTag name="forCsSexe" wantBlank="<%=true%>" codeType="PYSEXE" defaut=""/>
										</TD>
									</TR>
									<TR>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_TYPE_PRRESTATION"/></TD>
										<TD>
											<ct:select name="forTypeDemande" defaultValue="<%=viewBean.getTypeDemande()%>" wantBlank="true">
												<ct:optionsCodesSystems csFamille="<%=globaz.prestation.api.IPRDemande.CS_GROUPE_TYPE_DEMANDE%>"/>
											</ct:select>
										</TD>
										<TD><ct:FWLabel key="JSP_ETAT"/></TD>
										<TD>
											<ct:select name="forEtatDemande" defaultValue="<%=viewBean.getEtatDemande()%>" wantBlank="true">
												<ct:optionsCodesSystems csFamille="<%=globaz.prestation.api.IPRDemande.CS_GROUPE_ETAT_DEMANDE%>"/>
											</ct:select>
										</TD>
									</TR>
									<TR><TD colspan="4" height="30">&nbsp;</TD></TR>
									<TR>
										<TD><ct:FWLabel key="JSP_TRIER_PAR"/></TD>										
										<TD><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="orderBy"/></TD>
										<TD colspan="2">&nbsp;</TD>
									</TR>
									<TR><TD colspan="4">&nbsp;</TD></TR>
									<TR>
										<TD><input type="button" onclick="clearFields()" accesskey="C" value="Clear">  [ALT+C]</TD>
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