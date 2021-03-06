<%@ include file="../template/libraries.jsp" %>


<!-- TODO: Menu must be changed, here's an example -->

<!-- Links that are shown to everybody -->

<ul class="nav navbar-nav">
	<security:authorize access="permitAll()">
	</security:authorize>
</ul>

<!-- Links that are shown to Customers -->

<ul class="nav navbar-nav">
	<security:authorize access="hasRole('Customer')">
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown"> <spring:message code="master.customer" />
				<span class="caret"></span>
		</a>
			<ul class="dropdown-menu">
			</ul></li>
	</security:authorize>
</ul>

<!-- Links that are shown to Anonymous users -->

<security:authorize access="isAnonymous()">
	
	<ul class="nav navbar-nav navbar-right">
		<li><a href="home/sign-in.do"><spring:message
					code="master.sign-in" /></a></li>
	</ul>
	<ul class="nav navbar-nav navbar-right">
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown"> <spring:message code="master.sign-up" />
				<span class="caret"></span>
		</a>
			<ul class="dropdown-menu">
				<li><a href="home/customer/create.do"><spring:message
							code="master.sign-up.as-customer" /></a></li>
			</ul></li>
	</ul>
</security:authorize>

<!-- Links that are shown to Authenticated users (PROFILE SECTION) -->

<security:authorize access="isAuthenticated()">
	<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
			<a href="#" class="dropdown-toggle"	data-toggle="dropdown"> <spring:message	code="master.profile" /> <span class="caret"></span></a>
				<security:authorize access="hasRole('Customer')">
				<ul class="dropdown-menu">
				</ul>
				</security:authorize>
			</li>
		<li><a href="home/sign-out.do"> <spring:message
					code="master.sign-out" /> (<security:authentication
					property="principal.username" />)
					
		</a></li>
		<security:authorize access="hasRole('Customer')">
			<li>
				<security:authentication property="principal.actors" var="actors"/>
				<jstl:forEach items="${actors}" var="actor">
						<jstl:if test="${actor.socialIdentity != null }">
	        				<a href="${actor.socialIdentity.homePage}" target="_blank"><img alt="brand" src="${actor.socialIdentity.picture}" width="32px" height="32px" class="img-rounded"></a>
						</jstl:if>
				</jstl:forEach>
			</li>
		</security:authorize>
	</ul>
</security:authorize>