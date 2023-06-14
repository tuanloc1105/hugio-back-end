package vn.com.hugio.common.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_NATIVE_LOCKMODE;

@SuppressWarnings("unchecked")
@Component
@ConditionalOnBean({EntityManager.class})
public class EntityManagerUtil {
    private final EntityManager entityManager;

    @Autowired
    public EntityManagerUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public List<Object[]> createNativeQuery(String sqlString, Map<String, Object> parameter) {
        Query query = this.entityManager.createNativeQuery(sqlString);
        if (Optional.ofNullable(parameter).isPresent()) {
            for (String key : parameter.keySet()) {
                query.setParameter(key, parameter.get(key));
            }
        }
        // this can be use in repository interface class with annotation @Lock
        //query.setLockMode(LockModeType.PESSIMISTIC_READ);
        query.setHint(HINT_NATIVE_LOCKMODE, LockModeType.PESSIMISTIC_READ);
        return query.getResultList();
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public <T> List<T> createNativeQuery(String sqlString, Map<String, Object> parameter, Class<T> clz) {
        Query query = this.entityManager.createNativeQuery(sqlString, clz);
        if (Optional.ofNullable(parameter).isPresent()) {
            for (String key : parameter.keySet()) {
                query.setParameter(key, parameter.get(key));
            }
        }
        // this can be use in repository interface class with annotation @Lock
        //query.setLockMode(LockModeType.PESSIMISTIC_READ);
        query.setHint(HINT_NATIVE_LOCKMODE, LockModeType.PESSIMISTIC_READ);
        return query.getResultList();
    }


}
