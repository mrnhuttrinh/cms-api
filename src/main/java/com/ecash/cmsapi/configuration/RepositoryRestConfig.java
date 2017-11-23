package com.ecash.cmsapi.configuration;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

  /**
   * reference:
   * https://jira.spring.io/browse/DATAREST-161?focusedCommentId=153328&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-153328
   */
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

    final Set<BeanDefinition> beans = provider.findCandidateComponents("com.ecash.ecashcore.model");

    for (BeanDefinition bean : beans) {
      Class<?> idExposedClasses = null;
      try {
        idExposedClasses = Class.forName(bean.getBeanClassName());
        config.exposeIdsFor(Class.forName(idExposedClasses.getName()));
      } catch (ClassNotFoundException e) {
        // Exception cause by some class in this model is not an entity (just ignore)
      }
    }
  }
}