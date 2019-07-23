package com.fh.realm;

import com.fh.pojo.User;
import com.fh.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/*只实现认证的realm*/
public class SecondRealm extends AuthorizingRealm {
    @Autowired
    private IUserService userService;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo
            (AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("SecondRealm................");
        logger.info("doGetAuthenticationInfo:" + authenticationToken);
        logger.info("doGetAuthenticationInfo:" + authenticationToken);
        // 1.把AuthenticationToken对象装换为UsernamePasswordToken对象
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 2.从UsernamePasswordToken中获取username
        String username = token.getUsername();
        // 3.调用数据库的方法，从数据库中查询username对应的用户记录
        User user = userService.getUserByUsername(username);
        // 由于数据库中没有MD5加密，因此在此处进行模拟
//        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 模拟盐值加密 加密方式 密码 盐值 加密次数
        SimpleHash simpleHash =
                new SimpleHash("SHA1",user.getPassword(),user.getUsername(),1);
        user.setPassword(simpleHash.toString());
        // 4.若用户不存在，则可以抛出UnknownAccountException异常
        if (null == user) {
            throw new UnknownAccountException("用户不存在!");
        }
        // 5.根据用户信息的情况，决定是否需要抛出其他的AuthenticationException异常
        // 6.根据用户的情况，来构建AuthenticationInfo，通常使用的实现类为SimpleAuthenticationInfo
        // principal:认证的实体信息，可以是username，也可以是数据库对应的用户实体类
        // credentials:密码,从数据库获取的， shiro会自行将这个密码和前台传过来的密码即authenticationToken的密码进行比对
        // realmName，当前realm对象的name，调用父类的getName()
//        SimpleAuthenticationInfo simpleAuthenticationInfo =
//                new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        // 如果采用了盐值加密 则需要额外传入一个盐值参数,改参数为ByteSource类型
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUsername());
        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, getName());
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 1.从 PrincipalCollection中获取登录用户的信息
        User user = (User) principalCollection.getPrimaryPrincipal();
        // 2.利用登录的用户信息来获取当前用户的角色或权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>();
        roles.add("user");
        if("zenglei2".equals(user.getUsername())){
            roles.add("admin");
        }
        // 3.创建SimpleAuthorizationInfo，并设置roles属性
        SimpleAuthorizationInfo simpleAuthorizationInfo =
                new SimpleAuthorizationInfo(roles);
        // 4.返回SimpleAuthorizationInfo对象
        return simpleAuthorizationInfo;
    }
}
