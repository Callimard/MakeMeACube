package org.callimard.makemeacube.common.aop;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public class AopTool {

    private AopTool() {
    }

    // Methods.

    /**
     * Only works on {@link JoinPoint} which is a method.
     *
     * @param jp              the join point (must be a method)
     * @param annotationClass the annotation that researched parameters are annotated
     * @param <T>             the type of the parameters.
     *
     * @return a list which contains all parameter of the join point which are annotated with the specified annotation class.
     *
     * @throws AopMethodSignatureException if the method does not have parameter annotated with the annotation
     */
    public static <T> List<T> searchParameters(JoinPoint jp, Class<? extends Annotation> annotationClass) {
        Object[] args = jp.getArgs();
        Parameter[] parameters = extractJoinPointMethodParameter(jp);
        if (parameters.length < 1) {
            throw new AopMethodSignatureException("Method does not have parameter");
        } else {
            List<Integer> indexes = searchParameters(parameters, annotationClass);
            if (!indexes.isEmpty()) {
                try {
                    List<T> foundParameters = Lists.newArrayList();
                    indexes.forEach(index -> foundParameters.add((T) args[index]));
                    return foundParameters;
                } catch (ClassCastException e) {
                    throw new AopMethodSignatureException(e);
                }
            } else {
                throw new AopMethodSignatureException("Method does not have parameter annotated with the annotation " + annotationClass);
            }
        }
    }

    private static Parameter[] extractJoinPointMethodParameter(JoinPoint jp) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        return method.getParameters();
    }

    private static List<Integer> searchParameters(Parameter[] parameters, Class<? extends Annotation> annotationClass) {
        int index = 0;
        List<Integer> found = Lists.newArrayList();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(annotationClass)) {
                found.add(index);
            }
            index++;
        }
        return found;
    }
}
