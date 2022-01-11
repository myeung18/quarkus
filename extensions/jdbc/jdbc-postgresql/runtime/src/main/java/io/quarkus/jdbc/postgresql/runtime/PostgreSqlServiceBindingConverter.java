package io.quarkus.jdbc.postgresql.runtime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.quarkus.kubernetes.service.binding.runtime.JdbcDatasourceUtil;
import io.quarkus.kubernetes.service.binding.runtime.ServiceBinding;
import io.quarkus.kubernetes.service.binding.runtime.ServiceBindingConfigSource;
import io.quarkus.kubernetes.service.binding.runtime.ServiceBindingConverter;

public class PostgreSqlServiceBindingConverter implements ServiceBindingConverter {

    public static final String QUARKUS_DATASOURCE_JDBC_URL = "quarkus.datasource.jdbc.url";
    public static final String BINDING_TYPE = "postgresql";

    @Override
    public Optional<ServiceBindingConfigSource> convert(List<ServiceBinding> serviceBindings) {
        Optional<ServiceBindingConfigSource> configSource = JdbcDatasourceUtil.convert(serviceBindings, BINDING_TYPE);

        if (!configSource.isPresent()) {
            return configSource;
        }
        Optional<ServiceBinding> matchingByType = ServiceBinding.singleMatchingByType(BINDING_TYPE, serviceBindings);
        if (matchingByType.isEmpty()) {
            return Optional.empty();
        }
        String options = matchingByType.get().getProperties().get("options");
        if (options == null) {
            return configSource;
        }
        Map<String, String> properties = configSource.get().getProperties();
        properties.put(QUARKUS_DATASOURCE_JDBC_URL, properties.get(QUARKUS_DATASOURCE_JDBC_URL) + "?" + options);
        return configSource;
    }
}
