<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.jade.process.business.conf.JadeProcessDisplayValue"%>
<%@page import="ch.globaz.jade.process.business.models.entity.EntiteStep"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessStateEnum"%>
<%@page import="globaz.fx.vb.process.FXJadeStepAjaxViewBean"%>

<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
FXJadeStepAjaxViewBean viewBean=(FXJadeStepAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);

String userActionUpdate = "fx.process.jadeStepAjax.modifierAJAX";
%>
	<liste> 
		<%
		String idGroup=null; 

		for(JadeAbstractModel model: viewBean.getSearchModel().getSearchResults()){
			EntiteStep line=((EntiteStep)model);
			Boolean sameStep = viewBean.getSimpleExecutionProcess().getIdCurrentStep().equals(line.getSimpleEntite().getIdCurrentStep());
			String disabled = (sameStep || JadeProcessStateEnum.POPULATION_CREATED.equals(viewBean.getSimpleExecutionProcess().getCsEtat()))?"":"disabled='disabled'";
			String checked = (line.getSimpleEntite().getIsManual())?"checked='checked'":"";
			
			String disabledExecute =  (/*!line.getIsManual() && sameStep*/ line.getSimpleEntite().getIsManual() || JadeProcessStateEnum.FINISHED.equals(viewBean.getSimpleExecutionProcess().getCsEtat())) ?"disabled='disabled'":"";
			String color = "";
			if(JadeProcessEntityStateEnum.ERROR.equals(line.getSimpleEntite().getCsEtat())) {
				color = "class='entiteError'";
			}else if(JadeProcessEntityStateEnum.WARNING.equals(line.getSimpleEntite().getCsEtat())){
				color = "class='entiteWarning'";
			}
		%>
			<tr idEntity="<%=line.getId()%>" <%=color%>>
				<td style="text-align: left"> <%=JadeStringUtil.escapeXML(line.getSimpleEntite().getDescription())%></td>
				<td><%=line.getSimpleStep().getOrdre()%> <input type="hidden" class="simpleEntity.value1" value='<%=line.getSimpleEntite().getValue1()%>' /></td>
				<td><%= line.getSimpleEntite().getCsEtat().toLabel()%></td>
				<td> 
					<ct:ifhasright element="<%=userActionUpdate%>" crud="u">
						<input type="checkbox" name="manual" class="manual" <%=checked%> <%=disabled%> />  
					</ct:ifhasright>
				</td>
				<td> 
					<ct:ifhasright element="<%=userActionUpdate%>" crud="u">
						<input type="checkbox" name="execute" class="execute" <%=disabledExecute%> />
					</ct:ifhasright> 
				</td>
				<td> <%=line.getSimpleEntite().getExecuteStepTime()%> </td>
				<td> <%=line.getSimpleEntite().getId()%> </td>
				<%=viewBean.displayValue(line)%>
				
				<% if(viewBean.getIsCurrentSetInjectable()) {%>
				<td> 
					<button type="button" name="inject" class="inject" <%= (!viewBean.isEntityInjectable(line))?"disabled='disabled'":"" %> > Réintégré </button>  
				</td>
				<%} %>
			</tr>
		<%}%>
		</liste>
	<ct:serializeObject objectName="viewBean.simpleExecutionProcess" destination="xml"/>