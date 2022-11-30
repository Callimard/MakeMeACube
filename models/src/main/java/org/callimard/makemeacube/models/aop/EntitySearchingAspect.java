package org.callimard.makemeacube.models.aop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.callimard.makemeacube.common.aop.AopTool;
import org.callimard.makemeacube.models.sql.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Aspect
public class EntitySearchingAspect {

    // Variables.

    private final Map<Class<?>, JpaRepository<?, Integer>> repositoriesMap = Maps.newHashMap();
    private final Map<Class<?>, ThreadLocal<Object[]>> threadLocalsMap = Maps.newHashMap();

    // Constructors.

    public EntitySearchingAspect(@NonNull UserRepository userRepository, @NotNull UserAddressRepository userAddressRepository,
                                 @NotNull Printer3DRepository printer3DRepository, @NotNull MaterialRepository materialRepository) {
        addEntityManagement(User.class, userRepository);
        addEntityManagement(UserAddress.class, userAddressRepository);
        addEntityManagement(Printer3D.class, printer3DRepository);
        addEntityManagement(Material.class, materialRepository);
    }

    private void addEntityManagement(Class<?> entityClass, JpaRepository<?, Integer> entityRepository) {
        this.repositoriesMap.put(entityClass, entityRepository);
        this.threadLocalsMap.put(entityClass, new ThreadLocal<>());
    }

    // Advices.

    @Around("@annotation(org.callimard.makemeacube.models.aop.SearchUsers)")
    public Object searchUsers(ProceedingJoinPoint joinPoint) throws Throwable {
        return entitySearchingAdvice(joinPoint, UserId.class, User.class);
    }

    @Around("@annotation(org.callimard.makemeacube.models.aop.SearchUserAddresses)")
    public Object searchUserAddresses(ProceedingJoinPoint joinPoint) throws Throwable {
        return entitySearchingAdvice(joinPoint, UserAddressId.class, UserAddress.class);
    }

    @Around("@annotation(org.callimard.makemeacube.models.aop.SearchPrinter3Ds)")
    public Object searchPrinter3Ds(ProceedingJoinPoint joinPoint) throws Throwable {
        return entitySearchingAdvice(joinPoint, Printer3DId.class, Printer3D.class);
    }

    @Around("@annotation(org.callimard.makemeacube.models.aop.SearchMaterials)")
    public Object searchMaterials(ProceedingJoinPoint joinPoint) throws Throwable {
        return entitySearchingAdvice(joinPoint, MaterialId.class, Material.class);
    }

    // Methods.

    public <T> T entityOf(Class<T> entityClass) {
        return entitiesOf(entityClass).get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> entitiesOf(Class<T> entityClass) {
        return (List<T>) Arrays.stream(threadLocalsMap.get(entityClass).get()).toList();
    }

    private <T> Object entitySearchingAdvice(ProceedingJoinPoint joinPoint, Class<? extends Annotation> annotation, Class<T> entityClass)
            throws Throwable {
        var entities = searchEntities(joinPoint, annotation, entityClass);
        updateThreadLocal(entityClass, entities);
        var returnedValue = joinPoint.proceed();
        freeThreadLocal(entityClass);

        return returnedValue;
    }

    private <T> List<T> searchEntities(JoinPoint joinPoint, Class<? extends Annotation> annotation, Class<T> entityClass) {
        List<Integer> entityIds = AopTool.searchParameters(joinPoint, annotation);
        return retrieveEntities(entityIds, entityClass);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> retrieveEntities(List<Integer> entityIds, Class<T> entityClass) {
        List<T> entities = Lists.newArrayList();
        entityIds.forEach(id -> {
            Optional<T> entity = (Optional<T>) repositoriesMap.get(entityClass).findById(id);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("The entity " + entityClass + " has not been found with the id " + id);
            }
            entities.add(entity.get());
        });
        return entities;
    }

    private <T> void updateThreadLocal(Class<T> entityClass, List<T> entities) {
        var threadLocal = threadLocalsMap.get(entityClass);
        threadLocal.set(entities.toArray());
    }

    private <T> void freeThreadLocal(Class<T> entityClass) {
        threadLocalsMap.get(entityClass).remove();
    }

}
