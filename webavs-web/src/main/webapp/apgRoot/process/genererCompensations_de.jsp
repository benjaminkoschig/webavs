<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="java.util.Vector"%>
<%@ page import="globaz.apg.menu.MenuPrestation" %>
<%@ page import="globaz.apg.db.lots.APLotManager" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3005";

	globaz.apg.vb.process.APGenererCompensationsViewBean viewBean = (globaz.apg.vb.process.APGenererCompensationsViewBean)(session.getAttribute("viewBean"));
	
	//On redirige vers le _rc des lots si on n'a rien à faire ici si le lot n'est pas ouvert
	
	boolean isOuvertOuVideOuCompense = globaz.apg.api.lots.IAPLot.CS_OUVERT.equals(viewBean.getEtatLot()) || globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getEtatLot()) || globaz.apg.api.lots.IAPLot.CS_COMPENSE.equals(viewBean.getEtatLot());
	if (!isOuvertOuVideOuCompense){
		userActionValue="apg.lots.lot.chercher";
	} else {
	 	userActionValue="apg.process.genererCompensations.executer";
	}
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	MenuPrestation menuPrestation = MenuPrestation.of(session);
	Vector<?> v = APLotManager.getIdsDescriptionsLotsOuvertsOuCompenses(viewBean.getSession(), menuPrestation.getCsTypePrestation());
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="<%=menuPrestation.getMenuIdPrincipal()%>" showTab="menu"/>
<ct:menuChange displayId="options" menuId="<%=menuPrestation.getMenuIdOptionsEmpty()%>"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_COMPENSATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" <%=(isOuvertOuVideOuCompense)?"":"class=\"disabled\" readonly disabled"%>></TD>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						
						<TR>
							<TD><ct:FWLabel key="JSP_LOTS_NON_VALIDES"/></TD>
							<TD>
								<%if (v!=null && v.size()>0){%>
									<%if (!isOuvertOuVideOuCompense) {%>

										<input type="text" name ="lot" value="<%=viewBean.getDescriptionLot()%>">
										<input type="hidden" name ="forIdLot" value="<%=viewBean.getForIdLot()%>">
									<%} else {%>
											<ct:FWListSelectTag data="<%=v%>" 
													defaut="<%=viewBean.getForIdLot()%>" 
													name="forIdLot"/>								
								
        <%
								}
        } else {
        %>
									<ct:FWLabel key="JSP_AUCUN_LOT_NON_VALIDE"/>
								<%}%>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOIS_PROCHAINE_FACTURATION"/></TD>
							<TD>
							<ct:FWLabel key="JSP_MOIS"/>	
							<ct:FWListSelectTag data="<%=viewBean.getMonths()%>" defaut="<%=viewBean.getMoisPeriodeFacturation()%>" name="MoisPeriodeFacturation"/>
    <TD><INPUT type="hidden" name="typePrestation"
               value="<%=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)%>">
    </TD>
							</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>