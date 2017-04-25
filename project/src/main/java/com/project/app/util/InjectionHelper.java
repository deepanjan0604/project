package com.project.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.project.app.configurations.DefaultAppConfig;

/**
 * Helper class which is able to autowire a specified class. It holds a static
 * reference to the {@link org .springframework.context.ApplicationContext}.
 */
public final class InjectionHelper implements ApplicationContextAware {

	private static final InjectionHelper INSTANCE = new InjectionHelper();
	public static ApplicationContext applicationContext;

	@Autowired
	DefaultAppConfig defaultAppConfig;

	public InjectionHelper() {
	}

	/**
	 * Tries to autowire the specified instance of the class if one of the
	 * specified beans which need to be autowired are null.
	 *
	 * @param classToAutowire
	 *            the instance of the class which holds @Autowire annotations
	 * @param beansToAutowireInClass
	 *            the beans which have the @Autowire annotation in the specified
	 *            {#classToAutowire}
	 */
	public static void autowire(Object classToAutowire,
			Object... beansToAutowireInClass) {
		for (Object bean : beansToAutowireInClass) {
			if (bean == null) {
				applicationContext.getAutowireCapableBeanFactory()
						.autowireBean(classToAutowire);
			}
		}
	}

	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		InjectionHelper.applicationContext = applicationContext;
	}

	/**
	 * @return the singleton instance.
	 */
	public static InjectionHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns the bean of mentioned type
	 * 
	 * @param classToGet
	 * @return
	 */
	public static <T extends Object> T getBean(Class<T> classToGet) {
		return classToGet.cast(applicationContext.getBean(classToGet));
	}

	/**
	 * Returns the bean of the mentioned name
	 * 
	 * @param classToGet
	 * @return
	 */
	public static Object getBean(String classToGet) {
		return applicationContext.getBean(classToGet);
	}
}
