package com.doer.moodle.common.config;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringValueResolver;

import com.doer.moodle.common.config.zk.ZkClient;
import com.doer.moodle.common.config.zk.ZkUtil;
import com.doer.moodle.common.contants.ConfigConstant;
import com.doer.moodle.common.exceptions.PlatformException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ConfigPlaceholderConfigurer extends PlaceholderConfigurerSupport {
	private static final Logger log = Logger.getLogger(PlaceholderConfigurerSupport.class);

	/** 从不检查系统属性 */
	public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;

	/**
	 * 如果在指定配置properties文件中没有解析到属性，就去检查系统属性，默认
	 */
	public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;

	/**
	 * 在见指定属性之前，先检查系统属性，系统属性会覆盖指定属性
	 */
	public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;

	private boolean searchSystemEnvironment = !SpringProperties
			.getFlag(AbstractEnvironment.IGNORE_GETENV_PROPERTY_NAME);

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		List<String> paths = ZkUtil
				.getChildren(ConfigConstant.CONFIG_INFO_PATH);
		if (CollectionUtils.isEmpty(paths)) {
			throw new PlatformException("0", "配置中心没有配置信息！");
		}
		props = new Properties();
		for (String path : paths) {
			path = ConfigConstant.UNIX_SEPERATE
					+ path.replace(ConfigConstant.DOT,
							ConfigConstant.UNIX_SEPERATE);
			String config = ZkUtil.getConfig(path);
			if (StringUtils.isBlank(config)) {
				continue;
			}
			Map<String, String> configMap = new Gson().fromJson(config,
					new TypeToken<Map<String, String>>() {
					}.getType());
			for (Map.Entry<String, String> entry : configMap.entrySet()) {
				props.setProperty(entry.getKey(), entry.getValue());
			}
		}
		StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(
				props);
		this.doProcessProperties(beanFactoryToProcess, valueResolver);
		log.info("属性设置完成");
	}

	private class PlaceholderResolvingStringValueResolver implements
			StringValueResolver {

		private final PropertyPlaceholderHelper helper;

		private final PlaceholderResolver resolver;

		public PlaceholderResolvingStringValueResolver(Properties props) {
			this.helper = new PropertyPlaceholderHelper(placeholderPrefix,
					placeholderSuffix, valueSeparator,
					ignoreUnresolvablePlaceholders);
			this.resolver = new PropertyPlaceholderConfigurerResolver(props);
		}

		@Override
		public String resolveStringValue(String strVal) throws BeansException {
			String value = this.helper.replacePlaceholders(strVal,
					this.resolver);
			return (value.equals(nullValue) ? null : value);
		}
	}

	private class PropertyPlaceholderConfigurerResolver implements
			PlaceholderResolver {

		private final Properties props;

		private PropertyPlaceholderConfigurerResolver(Properties props) {
			this.props = props;
		}

		@Override
		public String resolvePlaceholder(String placeholderName) {
			return ConfigPlaceholderConfigurer.this.resolvePlaceholder(
					placeholderName, props, SYSTEM_PROPERTIES_MODE_FALLBACK);
		}
	}

	protected String resolvePlaceholder(String placeholder, Properties props,
			int systemPropertiesMode) {
		String propVal = null;
		if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
			propVal = resolveSystemProperty(placeholder);
		}
		if (propVal == null) {
			propVal = resolvePlaceholder(placeholder, props);
		}
		if (propVal == null
				&& systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
			propVal = resolveSystemProperty(placeholder);
		}
		return propVal;
	}

	protected String resolvePlaceholder(String placeholder, Properties props) {
		return props.getProperty(placeholder);
	}

	protected String resolveSystemProperty(String key) {
		try {
			String value = System.getProperty(key);
			if (value == null && this.searchSystemEnvironment) {
				value = System.getenv(key);
			}
			return value;
		} catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not access system property '" + key + "': "
						+ ex);
			}
			return null;
		}
	}

	public static void main(String[] args) {
		String json = "{\"jdbc.url\":\"jdbc:mysql://127.0.0.1:3306/doer?characterEncoding=utf8\",\"jdbc.username\":\"root\",\"jdbc.password\":\"bNVOqb7WKLX5Bjnw+LMv92taj25KOxDimXxILPQjw42wgv+1lHzOH8kr97xDwWdhpY67QuYCS7sWN4W46YbkFA==\",\"jdbc.maxActive\":\"60\",\"jdbc.minIdle\":\"1\",\"jdbc.initialSize\":\"3\",\"jdbc.maxWait\":\"50000\",\"jdbc.minEvictableIdleTimeMillis\":\"30000\",\"jdbc.timeBetweenEvictionRunsMillis\":\"60000\"}";
		Map<String, String> configMap = new Gson().fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		for (Map.Entry<String, String> entry : configMap.entrySet()) {
			System.out.println(entry.getKey() + "..." + entry.getValue());
		}
	}

}
