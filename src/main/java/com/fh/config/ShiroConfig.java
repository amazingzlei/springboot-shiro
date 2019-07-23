package com.fh.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.fh.realm.SecondRealm;
import com.fh.realm.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig {
    //3.配置ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 1.设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(getDefaultSecurityManager());
        // 2.设置拦截请求
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/login", "anon");
        map.put("/userLogin", "anon");
        map.put("/user", "user,roles[user]");
        map.put("/logout", "logout");
        map.put("/admin", "roles[admin]");
        map.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        // 3.设置登录页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 4.设置为授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        return shiroFilterFactoryBean;
    }

    //2.配置securityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 设置认证策略
        defaultWebSecurityManager.setAuthenticator(getModularRealmAuthenticator());
        // 设置多realm
//        List list = new ArrayList(2);
//        list.add(getShiroRealm());
//        list.add(getSecondRealm());
//        defaultWebSecurityManager.setRealms(list);
        // 设置单realm
        defaultWebSecurityManager.setRealm(getShiroRealm());
        // 设置shiro缓存
        defaultWebSecurityManager.setCacheManager(getEhCacheManager());
        //注入记住我管理器;
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }

    //1. 配置realm
    @Bean
    public ShiroRealm getShiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public SecondRealm getSecondRealm() {
        SecondRealm shiroRealm = new SecondRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    // 如果设置了多realm，则需要设置认证器,而且必须写在realm之前
    @Bean
    public ModularRealmAuthenticator getModularRealmAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    // 开启thymeleaf整合shiro的标签
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    // 下面开启shiro的注解

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    // 配置shiro缓存
    @Bean
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:cache/ehcache.xml");
        return ehCacheManager;
    }

    // 配置remeberme
    /**
     * cookie对象;
     * @return
     * */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

}
