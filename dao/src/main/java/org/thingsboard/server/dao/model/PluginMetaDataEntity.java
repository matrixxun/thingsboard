/**
 * Copyright © 2016 The Thingsboard Authors
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
package org.thingsboard.server.dao.model;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.databind.JsonNode;
import org.thingsboard.server.common.data.id.PluginId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.plugin.ComponentLifecycleState;
import org.thingsboard.server.common.data.plugin.PluginMetaData;
import org.thingsboard.server.dao.model.type.ComponentLifecycleStateCodec;
import org.thingsboard.server.dao.model.type.JsonCodec;

import java.util.Objects;
import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.ADDITIONAL_INFO_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.RULE_ACTION;

@Table(name = ModelConstants.PLUGIN_COLUMN_FAMILY_NAME)
public class PluginMetaDataEntity implements SearchTextEntity<PluginMetaData> {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(name = ModelConstants.ID_PROPERTY)
    private UUID id;

    @Column(name = ModelConstants.PLUGIN_API_TOKEN_PROPERTY)
    private String apiToken;

    @ClusteringColumn
    @Column(name = ModelConstants.PLUGIN_TENANT_ID_PROPERTY)
    private UUID tenantId;

    @Column(name = ModelConstants.PLUGIN_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.PLUGIN_CLASS_PROPERTY)
    private String clazz;

    @Column(name = ModelConstants.PLUGIN_ACCESS_PROPERTY)
    private boolean publicAccess;

    @Column(name = ModelConstants.PLUGIN_STATE_PROPERTY, codec = ComponentLifecycleStateCodec.class)
    private ComponentLifecycleState state;

    @Column(name = ModelConstants.PLUGIN_CONFIGURATION_PROPERTY, codec = JsonCodec.class)
    private JsonNode configuration;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Column(name = ADDITIONAL_INFO_PROPERTY, codec = JsonCodec.class)
    private JsonNode additionalInfo;

    public PluginMetaDataEntity() {
    }

    public PluginMetaDataEntity(PluginMetaData pluginMetaData) {
        if (pluginMetaData.getId() != null) {
            this.id = pluginMetaData.getId().getId();
        }
        this.tenantId = pluginMetaData.getTenantId().getId();
        this.apiToken = pluginMetaData.getApiToken();
        this.clazz = pluginMetaData.getClazz();
        this.name = pluginMetaData.getName();
        this.publicAccess = pluginMetaData.isPublicAccess();
        this.state = pluginMetaData.getState();
        this.configuration = pluginMetaData.getConfiguration();
        this.searchText = pluginMetaData.getName();
        this.additionalInfo = pluginMetaData.getAdditionalInfo();
    }

    @Override
    public String getSearchTextSource() {
        return searchText;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public JsonNode getConfiguration() {
        return configuration;
    }

    public void setConfiguration(JsonNode configuration) {
        this.configuration = configuration;
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public ComponentLifecycleState getState() {
        return state;
    }

    public void setState(ComponentLifecycleState state) {
        this.state = state;
    }

    public String getSearchText() {
        return searchText;
    }

    public JsonNode getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public PluginMetaData toData() {
        PluginMetaData data = new PluginMetaData(new PluginId(id));
        data.setTenantId(new TenantId(tenantId));
        data.setCreatedTime(UUIDs.unixTimestamp(id));
        data.setName(name);
        data.setConfiguration(configuration);
        data.setClazz(clazz);
        data.setPublicAccess(publicAccess);
        data.setState(state);
        data.setApiToken(apiToken);
        data.setAdditionalInfo(additionalInfo);
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PluginMetaDataEntity entity = (PluginMetaDataEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(apiToken, entity.apiToken) && Objects.equals(tenantId, entity.tenantId)
                && Objects.equals(name, entity.name) && Objects.equals(clazz, entity.clazz) && Objects.equals(state, entity.state)
                && Objects.equals(configuration, entity.configuration)
                && Objects.equals(searchText, entity.searchText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, apiToken, tenantId, name, clazz, state, configuration, searchText);
    }

    @Override
    public String toString() {
        return "PluginMetaDataEntity{" + "id=" + id + ", apiToken='" + apiToken + '\'' + ", tenantId=" + tenantId + ", name='" + name + '\'' + ", clazz='"
                + clazz + '\'' + ", state=" + state + ", configuration=" + configuration + ", searchText='" + searchText + '\'' + '}';
    }
}
