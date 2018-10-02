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

package org.apache.fineract.infrastructure.paymentgateway.paymentchannel.api;

import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.core.serialization.ExcludeNothingWithPrettyPrintingOffJsonSerializerGoogleGson;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.data.PaymentGatewayData;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.service.PaymentGatewayReadPlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/paymentchannels")
@Component
@Scope("singleton")
public class PaymentChannelApiResource {

	private final static Logger logger = LoggerFactory.getLogger(PaymentChannelApiResource.class);

	private final Set<String> PAYMENT_CHANNEL_DATA_PARAMETERS = new HashSet<>(
			Arrays.asList("id", "channelName", "channelType", "isActive"));

	private final String resourceNameForPermissions = "PAYMENTCHANNEL";
	private final PaymentGatewayReadPlatformService paymentGatewayReadPlatformService;
	private final PlatformSecurityContext securityContext;
	private final DefaultToApiJsonSerializer<PaymentGatewayData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

	@Autowired
	public PaymentChannelApiResource(PlatformSecurityContext securityContext,
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

		// Check to see if payment gateway is enabled. If so, read the gateway URL
		boolean isActive = this.paymentGatewayReadPlatformService.isPaymentGatewayActive();
		if (isActive) {
			// Call the payment gateway API

			this.securityContext.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);

			final PaymentGatewayData paymentGatewayData = this.paymentGatewayReadPlatformService
					.retrievePaymentGatewayData();

			String gatewayUrl = paymentGatewayData.getGatewayUrl();

			try {
				URL url = new URL(gatewayUrl+"/active-vendors");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Content-Type", "application/json");
				int status = con.getResponseCode();

				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();
				con.disconnect();

				return content.toString();
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}

		return "Inactive";

	}

}
