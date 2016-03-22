package com.doer.moodle.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringValueResolver;

public class ZkConfigPlaceholderConfigurer extends PlaceholderConfigurerSupport {

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
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		String[] beanDefinitionNames = beanFactoryToProcess.getBeanDefinitionNames();
		System.out.println("总bean数:"+beanDefinitionNames.length);
		for(String s :beanDefinitionNames){
			System.out.println("当前bean："+s);
		}
		props = new Properties();
		props.setProperty("jdbc.url", "jdbc:mysql://127.0.0.1:3306/doer?characterEncoding=utf8");
		props.setProperty("jdbc.username", "root");
		props.setProperty("jdbc.password", "bNVOqb7WKLX5Bjnw+LMv92taj25KOxDimXxILPQjw42wgv+1lHzOH8kr97xDwWdhpY67QuYCS7sWN4W46YbkFA==");
		props.setProperty("moodle.provider.appname", "moodle_dubbo_service");
		props.setProperty("moodle.provider.registry.protocol", "zookeeper");
		props.setProperty("moodle.provider.registry.address", "127.0.0.1:2181");
		props.setProperty("moodle.provider.registry.file", "./dubbo-registry.dat");
		props.setProperty("moodle.provider.protocol.port", "20880");
		props.setProperty("moodle.provider.protocol", "dubbo");
		props.setProperty("moodle.provider.timeout", "20000");
		System.out.println("属性设置完成");
		StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
		System.out.println("解析完成");
		this.doProcessProperties(beanFactoryToProcess, valueResolver);
		System.out.println("do完成");
	}

	private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

		private final PropertyPlaceholderHelper helper;

		private final PlaceholderResolver resolver;

		public PlaceholderResolvingStringValueResolver(Properties props) {
			this.helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator,
					ignoreUnresolvablePlaceholders);
			this.resolver = new PropertyPlaceholderConfigurerResolver(props);
		}

		@Override
		public String resolveStringValue(String strVal) throws BeansException {
			String value = this.helper.replacePlaceholders(strVal, this.resolver);
			return (value.equals(nullValue) ? null : value);
		}
	}

	private class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver {

		private final Properties props;

		private PropertyPlaceholderConfigurerResolver(Properties props) {
			this.props = props;
		}

		@Override
		public String resolvePlaceholder(String placeholderName) {
			return ZkConfigPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, props, SYSTEM_PROPERTIES_MODE_FALLBACK);
		}
	}

	protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
		String propVal = null;
		if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
			propVal = resolveSystemProperty(placeholder);
		}
		if (propVal == null) {
			propVal = resolvePlaceholder(placeholder, props);
		}
		if (propVal == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
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
				logger.debug("Could not access system property '" + key + "': " + ex);
			}
			return null;
		}
	}

}
