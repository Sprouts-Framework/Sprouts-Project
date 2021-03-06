
<%--
 * extended-sign-up.jsp
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ include file="../template/libraries.jsp" %>

<div class="fieldset-btm-margin">
	<fieldset>
		<legend>
			<spring:message code="sign-up.legend.companyInfo" />
		</legend>
		<sprouts:textbox-input code="sign-up.companyName.field"
			path="companyName" />
		<sprouts:textbox-input code="sign-up.ticker.field" path="ticker" />
	</fieldset>
</div>