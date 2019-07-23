//package com.fh.config;
//
//
//import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import com.fh.realm.SecondRealm;
//import com.fh.realm.ShiroRealm;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
//import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
//
//import java.util.*;
//
///**
// * shiro配置类
// */
//@Configuration
//public class MyShiroConfig {
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }
//
//    @Bean
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        System.out.println("ShiroConfiguration.shirFilter()");
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        // 1.设置SecurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager());
//        // 2.设置拦截请求
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("/login", "anon");
//        map.put("/userLogin", "anon");
//        map.put("/user", "user,roles[user]");
//        map.put("/logout", "logout");
//        map.put("/admin", "roles[admin]");
//        map.put("/**", "authc");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        // 3.设置登录页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 4.设置为授权页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
//        return shiroFilterFactoryBean;
//    }
//
//    /**
//     * 凭证匹配器
//     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
//     * ）
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }
//
//    @Bean
//    public ShiroRealm myShiroRealm(){
//        ShiroRealm shiroRealm = new ShiroRealm();
//        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return shiroRealm;
//    }
//
//
//    @Bean
//    public SecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        return securityManager;
//    }
//
//    /**
//     *  开启shiro aop注解支持.
//     *  使用代理方式;所以需要开启代码支持;
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    @Bean(name="simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver
//    createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "/base/databaseError");//数据库异常处理
//        mappings.setProperty("UnauthorizedException","/base/error");
//        r.setExceptionMappings(mappings);  // None by default
//        //r.setDefaultErrorView("error");    // No default
//        r.setDefaultErrorView("/base/login");    // 访问错误，直接访问登录页面
//        r.setExceptionAttribute("ex");     // Default is "exception"
//        //r.setWarnLogCategory("example.MvcLogger");     // No default
//        return r;
//    }
//
//}
