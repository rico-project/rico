/*
 * Copyright 2018-2019 Karakun AG.
 * Copyright 2015-2018 Canoo Engineering AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.rico.internal.remoting.commands;

import dev.rico.internal.core.Assert;
import dev.rico.internal.remoting.legacy.communication.Command;
import dev.rico.internal.remoting.legacy.communication.CommandConstants;
import org.apiguardian.api.API;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apiguardian.api.API.Status.INTERNAL;

@API(since = "0.x", status = INTERNAL)
public final class CreateControllerCommand extends Command {

    private String parentControllerId;

    private String controllerName;

    private Map<String, Serializable> parameters;

    public CreateControllerCommand() {
        super(CommandConstants.CREATE_CONTROLLER_COMMAND_ID);
        parameters = new HashMap<>();
    }

    public String getParentControllerId() {
        return parentControllerId;
    }

    public void setParentControllerId(final String parentControllerId) {
        this.parentControllerId = parentControllerId;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(final String controllerName) {
        Assert.requireNonBlank(controllerName, "controllerName");
        this.controllerName = controllerName;
    }

    public Map<String, Serializable> getParameters() {
        return Optional.ofNullable(parameters).orElse(Collections.emptyMap());
    }

    public void setParameters(final Map<String, Serializable> parameters) {
        this.parameters = parameters;
    }
}
