/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.infrastructure.paymentgateway.paymentgateway.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.data.PaymentGatewayData;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.service.PaymentGatewayReadPlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/paymentgateway")
@Component
@Scope("singleton")
public class PaymentGatewayApiResource {

	private final Set<String> PAYMENT_GATEWAY_PARAMETERS = new HashSet<>(
			Arrays.asList("isActive", "gatewayEndpoint", "paymentType", "gatewayUserId"));

	private final String resourceNameForPermissions = "PAYMENTGATEWAY";
	private final PaymentGatewayReadPlatformService paymentGatewayReadPlatformService;
	private final PlatformSecurityContext securityContext;
	private final DefaultToApiJsonSerializer<PaymentGatewayData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

	@Autowired
	public PaymentGatewayApiResource(PlatformSecurityContext securityContext,
									 DefaultToApiJsonSerializer<PaymentGatewayData> toApiJsonSerializer,
									 ApiRequestParameterHelper apiRequestParameterHelper,
									 PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
									 PaymentGatewayReadPlatformService paymentGatewayReadPlatformService) {
		super();
		this.securityContext = securityContext;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		this.paymentGatewayReadPlatformService = paymentGatewayReadPlatformService;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveAllPaymentChannels(@Context final UriInfo uriInfo) {

		this.securityContext.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);

		final PaymentGatewayData paymentGatewayData = this.paymentGatewayReadPlatformService
				.retrievePaymentGatewayData();

		final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper
				.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, paymentGatewayData, this.PAYMENT_GATEWAY_PARAMETERS);
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String create(final String apiRequestBodyAsJson) {

		final CommandWrapper commandRequest = new CommandWrapperBuilder().createPaymentGateway().withJson(apiRequestBodyAsJson).build();

		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

		return this.toApiJsonSerializer.serialize(result);
	}

    @PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
    public String update(final String apiRequestBodyAsJson) {

        final CommandWrapper commandRequest = new CommandWrapperBuilder().updatePaymentGateway().withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }


}
