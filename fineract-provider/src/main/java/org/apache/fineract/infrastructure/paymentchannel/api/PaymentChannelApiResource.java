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

package org.apache.fineract.infrastructure.paymentchannel.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentchannel.service.PaymentChannelReadPlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/paymentchannel")
@Component
@Scope("singleton")
public class PaymentChannelApiResource {
	
	 private final Set<String> PAYMENT_DATA_PARAMETERS = new HashSet<>(Arrays.asList("id", "channelName", "channelEndpointTag", 
			 "channelType", "isActive", "phoneNumberDefaultRegion", "dateCreated", "lastModified"));
	
	private final String resourceNameForPermissions = "PAYMENT_CHANNEL";
	private final PaymentChannelReadPlatformService paymentChannelReadPlatformService;
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<PaymentChannelData> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Autowired
	public PaymentChannelApiResource(PlatformSecurityContext context,
			DefaultToApiJsonSerializer<PaymentChannelData> toApiJsonSerializer,
			ApiRequestParameterHelper apiRequestParameterHelper,
			PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
			PaymentChannelReadPlatformService paymentChannelReadPlatformService) {
		super();
		this.context = context;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		this.paymentChannelReadPlatformService = paymentChannelReadPlatformService;
	}

	@GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllPaymentChannels(@Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);

        final Collection<PaymentChannelData> paymentChannelData = this.paymentChannelReadPlatformService.retrieveAllPaymentChannelData();

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, paymentChannelData, this.PAYMENT_DATA_PARAMETERS);
    }
	
	@GET
    @Path("{paymentChannelId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveCharge(@PathParam("paymentChannelId") final Long paymentChannelId, @Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        PaymentChannelData paymentChannelData = this.paymentChannelReadPlatformService.retrievePaymentChannelDataById(paymentChannelId);
        
        return this.toApiJsonSerializer.serialize(settings, paymentChannelData, this.PAYMENT_DATA_PARAMETERS);
    }
}
