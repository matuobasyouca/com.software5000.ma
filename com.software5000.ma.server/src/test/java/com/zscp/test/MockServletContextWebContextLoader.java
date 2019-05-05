package com.zscp.test;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class MockServletContextWebContextLoader extends AbstractContextLoader {

    public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
        ConfigurableWebApplicationContext context = new XmlWebApplicationContext();
        context.setServletContext(new MockServletContext("/webapp", new FileSystemResourceLoader()));
        context.setConfigLocations(locations);
        context.refresh();
        AnnotationConfigUtils.registerAnnotationConfigProcessors((BeanDefinitionRegistry) context.getBeanFactory());
        context.registerShutdownHook();
        return context;
    }

    @Override
    protected String getResourceSuffix() {
        return "spring-*.xml";
    }

    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedContextConfiguration) throws Exception {
        ConfigurableWebApplicationContext context = new XmlWebApplicationContext();
        context.setServletContext(new MockServletContext("/webapp", new FileSystemResourceLoader()));
        context.setConfigLocations(mergedContextConfiguration.getLocations());
        context.refresh();
        AnnotationConfigUtils.registerAnnotationConfigProcessors((BeanDefinitionRegistry) context.getBeanFactory());
        context.registerShutdownHook();
        return context;
    }
}
