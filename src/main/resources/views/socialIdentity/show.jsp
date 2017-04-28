<%--
 * show.jsp
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ include file="../template/libraries.jsp" %>

<tiles:importAttribute name="readOnly" toName="readOnly" />
<tiles:importAttribute name="action" toName="action" />

<sprouts:form modelAttribute="modelObject" readOnly="${readOnly}">

	<sprouts:hidden-field path="id" />
	<sprouts:hidden-field path="version" />

	<sprouts:protected path="id" />
	<sprouts:protected path="version" />

	<div class="fieldset-btm-margin">
			<sprouts:textbox-input code="customer.socialIdentity.nick" path="nick" />
			<sprouts:textbox-input code="customer.socialIdentity.socialNetwork" path="socialNetwork" />
			<sprouts:textbox-input code="customer.socialIdentity.homePage" path="homePage" />
			<sprouts:textbox-input code="customer.socialIdentity.picture" path="picture" />
	</div>

	<jstl:if test="${crudAction != 'showing'}">
		<sprouts:submit-button code="${action}" name="${action}" />
	</jstl:if>
	<sprouts:cancel-button code="return.button" url="profile/customer/show.do" />
 
</sprouts:form>

