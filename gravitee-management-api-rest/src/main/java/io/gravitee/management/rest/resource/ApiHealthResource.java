/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.management.rest.resource;

import io.gravitee.management.model.ApiEntity;
import io.gravitee.management.rest.resource.param.HealthParam;
import io.gravitee.management.service.AnalyticsService;
import io.gravitee.management.service.ApiService;
import io.gravitee.management.service.PermissionService;
import io.gravitee.management.service.PermissionType;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class ApiHealthResource extends AbstractResource {

    @PathParam("api")
    private String api;

    @Inject
    private ApiService apiService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private AnalyticsService analyticsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hits(@BeanParam HealthParam healthParam) {
        ApiEntity api = apiService.findById(this.api);

        permissionService.hasPermission(getAuthenticatedUser(), this.api, PermissionType.EDIT_API);

        healthParam.validate();

        return Response.ok(analyticsService.health(
                api.getId(),
                healthParam.getFrom(),
                healthParam.getTo(),
                healthParam.getInterval())).build();
    }
}
