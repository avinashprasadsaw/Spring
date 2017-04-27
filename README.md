# README #
If anybody need any kind of help.Then please contact me on Avinash Prasad Saw<avinashprasadsaw@gmail.com> or Mobile no-7059422377.
I am always available to help you.

In this project we use Spring Annotation based configuration for Servlet 3.0 containers 
[thus no web.xml] and also shows corresponding XML based Spring-MVC and Spring-Security configuration 
for side-by-side comparison where applicable. Let’s get started.

# Detials of Spring Security integration with Spring MVC  using annotation #
===============================================================================================
# Implementation Detail #
	#step-1
	-------
		First thing to notice here is the maven-war-plugin declaration. As we are using full
		annotation configuration, we don’t even use web.xml, so we will need to configure this
		plugin in order to avoid maven failure to build war package.
		Along with that, we have also included JSP/Servlet/Jstl dependencies which we will be 
		needing as we are going to use servlet api’s and jstl view in our code. In general, 
		containers might already contains these libraries, so we can set the scope as ‘provided’
		for them in pom.xml.

	#Step-2
	-------
		Create AppInitilizer.java.This class work as a web.xml.
		As we can see on code that it extends AbstractAnnotationConfigDispatcherServletInitializer.
		So please have a look on this first
# AbstractAnnotationConfigDispatcherServletInitializer #

	Understanding Spring Web Initialization 

Few years ago majority of us were used to write XML config files everywhere, to setup even simple Java EE application. Today using Java or Groovy to configure projects is becoming preferred way - you just need to take a look at Gradle or functionalities introduced in further versions of the Spring Framework to gen up on this.

Now I'll deal with configuring Spring contexts for web application.

Java EE provides ServletContainerInitializer interface, which allows libraries to be notified of a web application startup. Since Spring 3.1 we have SpringServletContainerInitializer class which handles WebApplicationInitializer by instantiating all found classes implementing this interface, sorting them basing on @Order annotation (non-annotated classes gets the highest possible order, so they are processed at the end) and invoking onStartup() method.


Spring since version 3.2 provides us a few classes implementing WebApplicationInitializer interface, from which first is AbstractContextLoaderInitializer. This class included in spring-web module uses abstract createRootApplicationContext() method to create application context, delegates it to ContextLoaderListener which then is being registered in the ServletContext instance. Creating application context using this class looks as follows:
public class SpringAnnotationWebInitializer
  extends AbstractContextLoaderInitializer {

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    AnnotationConfigWebApplicationContext applicationContext =
      new AnnotationConfigWebApplicationContext();
    applicationContext.register(SpringAnnotationConfig.class);
    return applicationContext;
  }

}

That was the simplest way to start up Spring web context. But if we want to experience benefits provided by Spring MVC and don't want to manually register DispatcherServlet it'll be better to use another class: AbstractDispatcherServletInitializer. It extends previous class and adds two abstract methods: createServletApplicationContext() and getServletMappings().  First method returns WebApplicationContext that will be passed to DispatcherServlet, which will be automatically added into container ServletContext. Please notice that this context will be established as a child of the context returned by createRootApplicationContext() method. Second method - as you have probably already deduced - returns mappings that are used during servlet registration. You can also override getServletFilters() method if you need any custom filters, because default implementation returns just empty array. Exemplary implementation using this class could be:
public class SpringWebMvcInitializer
  extends AbstractDispatcherServletInitializer {

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    AnnotationConfigWebApplicationContext applicationContext =
      new AnnotationConfigWebApplicationContext();
    applicationContext.register(SpringRootConfig.class);
    return applicationContext;
  }

  @Override
  protected WebApplicationContext createServletApplicationContext() {
    AnnotationConfigWebApplicationContext applicationContext =
      new AnnotationConfigWebApplicationContext();
    applicationContext.register(SpringMvcConfig.class);
    return applicationContext;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/*"};
  }

}

And now last but definitely not least class: AbstractAnnotationConfigDispatcherServletInitializer. Here we can see further step in simplifying Spring initialization - we don't need to manually create contexts but just set appropriate config classes in getRootConfigClasses() and getServletConfigClasses() methods. I hope you are already familiar with those names, because they works exactly like in the former case. Of course due to this class extends AbstractDispatcherServletInitializer we can still override getServletFilters(). Finally we can implement our configuration in the following way:
public class SpringWebMvcSimpleInitializer
  extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {SpringRootConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {SpringMvcConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/*"};
  }

}

# Migrate Spring MVC servlet.xml to Java Config #
In this project AppConfig.java work as servlet.xml

Since Spring 3, Java configuration (@Configuration) has been moved into spring-core and has caught my attention. This is a quick sample of how to convert an existing servlet.xml file into a java config file extending WebMvcConfigurerAdapter.

Beginning xml
--------------
sample-servlet.xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd">
 
    <!-- Scan for spring annotated components -->
    <context:component-scan base-package="com.luckyryan.sample"/>
 
    <!-- Process annotations on registered beans like @Autowired... -->
    <context:annotation-config/>
 
    <!-- This tag registers the DefaultAnnotationHandlerMapping and
         AnnotationMethodHandlerAdapter beans that are required for Spring MVC  -->
    <mvc:annotation-driven/>
 
    <!-- Exception Resolver that resolves exceptions through @ExceptionHandler methods -->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver"/>
 
    <!-- View Resolver for JSPs -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
 
    <!-- This tag allows for mapping the DispatcherServlet to "/" -->
    <mvc:default-servlet-handler/>
 
    <!-- resources exclusions from servlet mapping -->
    <mvc:resources mapping="/assets/**" location="classpath:/META-INF/resources/webjars/"/>
    <mvc:resources mapping="/css/**" location="/css/"/>
    <mvc:resources mapping="/img/**" location="/img/"/>
    <mvc:resources mapping="/js/**" location="/js/"/>
 
</beans>
1. Create the configuration class, I like to create a “config” package with a class named appConfig.java
2. Add @Configuration, this will let spring know this contains bean definitions.

appConfig.java
--------------

package com.luckyryan.sample.config;
 
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class appConfig {
 
}

3. Add @EnableWebMVC, this is the same as <mvc:annotation-driven/>


appConfig.java
--------------
@EnableWebMvc
@Configuration
public class appConfig {
 
}

4. Add @ComponentScan(basePackages = {“com.luckyryan.sample”}), this is the same as <context:component-scan base-package=”com.luckyryan.sample”/>

appConfig.java
---------------

@EnableWebMvc
@ComponentScan(basePackages = {"com.luckyryan.sample"})
@Configuration
public class appConfig {
 
}

5. Extend the class to use WebMvcConfigurerAdapter. This adds stub implementations from the WebMvcConfigurer interface which is used by @EnableWebMVC. It also gives us a chance to override resources and the default handler.

appConfig.java
--------------
@EnableWebMvc
@ComponentScan(basePackages = {"com.luckyryan.sample"})
@Configuration
public class appConfig extends WebMvcConfigurerAdapter {
 
}

6. Declare our static resources. I added cache to the java config but it’s not required.

appConfig.java
--------------

@EnableWebMvc
@ComponentScan(basePackages = {"com.luckyryan.sample"})
@Configuration
public class appConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
 
}

7. Set default servlet handler, this is the same as <mvc:default-servlet-handler/>

appConfig.java
--------------
@EnableWebMvc
@ComponentScan(basePackages = {"com.luckyryan.sample"})
@Configuration
public class appConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}

8. Add bean for InternalResourceViewResolver

appConfig.java
--------------
package com.luckyryan.sample.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
 
@EnableWebMvc
@ComponentScan(basePackages = {"com.luckyryan.sample"})
@Configuration
public class appConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}

9. Update the servlet declaration in web.xml for the new config class and annotation conf. Note: this replaces the need for 

<context:annotation-config/> which was not featured in the servlet.xml example.

web.xml
-------
<servlet>
    <servlet-name>sample</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </init-param>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            com.luckyryan.sample.config.appConfig
        </param-value>
    </init-param>
</servlet>
 
<servlet-mapping>
    <servlet-name>sample</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>


#All spring MVC configuration have done now we are going to look spring security configuration#

SecurityConfig.java is the the security configuration file which extends WebSecurityConfigurerAdapter
So please have a look on  WebSecurityConfigurerAdapter below.

WebSecurityConfigurerAdapter
----------------------------
The @EnableWebSecurity annotation and WebSecurityConfigurerAdapter work together to provide web based security. By extending WebSecurityConfigurerAdapter and only a few lines of code we are able to do the following:

Require the user to be authenticated prior to accessing any URL within our application
Create a user with the username “user”, password “password”, and role of “ROLE_USER”
Enables HTTP Basic and Form based authentication
Spring Security will automatically render a login page and logout success page for you

@Configuration
@EnableWebSecurity
public class HelloWebSecurityConfiguration
   extends WebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth
      .inMemoryAuthentication()
        .withUser("user").password("password").roles("USER");
  }
}

For your reference, this is similar to the following XML configuration with a few exceptions:

Spring Security will render the login, authentication failure url, and logout success URLs
The login-processing-url will only be processed for HTTP POST
The login-page will only be processed for HTTP GET


----------------------------

<http use-expressions="true">
  <intercept-url pattern="/**" access="authenticated"/>
  <logout
    logout-success-url="/login?logout"
    logout-url="/logout"
  />
  <form-login
    authentication-failure-url="/login?error"
    login-page="/login"
    login-processing-url="/login"
    password-parameter="password"
    username-parameter="username"
  />
</http>
<authentication-manager>
  <authentication-provider>
    <user-service>
      <user name="user" 
          password="password" 
          authorities="ROLE_USER"/>
    </user-service>
  </authentication-provider>
</authentication-manager>


The last step is we need to map the springSecurityFilterChain. We can easily do this by extending AbstractSecurityWebApplicationInitializer and optionally overriding methods to customize the mapping.
In SecurityWebApplicationInitializer.java we have done which extends AbstractSecurity WebApplicationInitializer.

AbstractSecurity WebApplicationInitializer
------------------------------------------


The most basic example below accepts the default mapping and adds springSecurityFilterChain with the following characteristics:

springSecurityFilterChain is mapped to “/*”
springSecurityFilterChain uses the dispatch types of ERROR and REQUEST
The springSecurityFilterChain mapping is inserted before any servlet Filter mappings that have already been configured

public class SecurityWebApplicationInitializer 
   extends AbstractSecurityWebApplicationInitializer {
}
The above code is the equivalent of the following lines within the web.xml:


<filter>
  <filter-name>springSecurityFilterChain</filter-name>
  <filter-class>
    org.springframework.web.filter.DelegatingFilterProxy
  </filter-class>
</filter>

<filter-mapping>
  <filter-name>springSecurityFilterChain</filter-name>
  <url-pattern>/*</url-pattern>
  <dispatcher>ERROR</dispatcher>
  <dispatcher>REQUEST</dispatcher>
</filter-mapping>