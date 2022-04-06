package com.gd.gd_service.config;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tangxl
 * @Date:2021年12月21日20:51:27
 * @Description: TODO 全局事务配置
 */
@Configuration
@Aspect
public class TransactionManagerConfig {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManagerConfig.class);
    /**
     * 配置方法过期时间，默认-1,永不超时，单位秒
     */
    @Value("${spring.application.transactiontimeout}")
    private int AOP_TIME_OUT;
    /**
     * 配置切入点表达式 : 指定哪些包中的类使用事务
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.gd.gd_service.service.impl.*.*(..)))";
    //事务管理器
    @Autowired
    private TransactionManager transactionManager;
    /**
     * 全局事务配置
     * REQUIRED ：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
     * SUPPORTS ：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
     * MANDATORY ：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
     * REQUIRES_NEW ：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
     * NOT_SUPPORTED ：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
     * NEVER ：以非事务方式运行，如果当前存在事务，则抛出异常。
     * NESTED ：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 REQUIRED 。
     * 指定方法：通过使用 propagation 属性设置，例如：@Transactional(propagation = Propagation.REQUIRED)
     */
    @Bean
    public TransactionInterceptor txAdvice(){
        /**
         * 配置事务管理规则
         * 这里配置只读事务
         * 查询方法， 只读事务，不做更新操作
         */
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        /**
         * 增、删、改 需要的事务
         * 必须带事务
         * 当前存在事务就使用当前事务，当前不存在事务,就开启一个新的事务
         */
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        // 设置回滚规则：什么异常都需要回滚
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 设置超时时间，超时则抛出异常回滚
        requiredTx.setTimeout(AOP_TIME_OUT);
        System.out.println("=====>>事务超时时间："+requiredTx.getTimeout());
        /**
         * 无事务地执行，挂起任何存在的事务
         */
        RuleBasedTransactionAttribute noTx = new RuleBasedTransactionAttribute();
        noTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        /**
         * 设置方法对应的事务
         */
        Map<String, TransactionAttribute> methodMap = new HashMap<>();
        // 可以提及事务或回滚事务的方法
        //只读事务
        methodMap.put("get*", readOnlyTx);
        methodMap.put("query*", readOnlyTx);
        methodMap.put("find*", readOnlyTx);
        methodMap.put("list*", readOnlyTx);
        methodMap.put("count*", readOnlyTx);
        methodMap.put("exist*", readOnlyTx);
        methodMap.put("search*", readOnlyTx);
        methodMap.put("fetch*", readOnlyTx);
        //写事务
        methodMap.put("add*", requiredTx);
        methodMap.put("save*", requiredTx);
        methodMap.put("insert*", requiredTx);
        methodMap.put("update*", requiredTx);
        methodMap.put("modify*", requiredTx);
        methodMap.put("delete*", requiredTx);
        methodMap.put("creat*", requiredTx);
        methodMap.put("edit*", requiredTx);
        methodMap.put("remove*", requiredTx);
        methodMap.put("repair*", requiredTx);
        methodMap.put("binding*", requiredTx);
        methodMap.put("clean*", requiredTx);
        methodMap.put("upload*",requiredTx);
        //无事务
        methodMap.put("noTx*", noTx);
        // 其他方法无事务，只读
        methodMap.put("*", readOnlyTx);

        //声明一个通过方法名字配置事务属性的对象
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(methodMap);

        //返回事务拦截器
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean(name = "txAdviceAdvisor")
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
        logger.info("=====>>创建txAdviceAdvisor");
        //配置事务切入点表达式
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        //增强事务，关联切入点和事务属性
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
