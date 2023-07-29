package vn.com.hugio.common.service;

import org.springframework.data.domain.Pageable;
import vn.com.hugio.common.entity.BaseEntity;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.common.log.LOG;

import java.util.Collection;
import java.util.List;

public abstract class BaseService<E extends BaseEntity, R extends BaseRepository<E>> {

    protected R repository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    // Persist

    public E save(E entity) {
        LOG.info("%s SAVE NEW ENTITY", this.repository.getClass().getSimpleName());
        return this.repository.save(entity);
    }

    public Long count() {
        return this.repository.count();
    }

    @SuppressWarnings("UnusedReturnValue")
    public List<E> saveAll(Collection<E> entities) {
        LOG.info("%s SAVE %s NEW ENTITY(S)", this.repository.getClass().getSimpleName(), entities.size());
        return this.repository.saveAll(entities);
    }

    public void deleteAll(Iterable<E> entities) {
        LOG.info("%s DELETE ALL ENTITY", this.repository.getClass().getSimpleName());
        this.repository.deleteAll(entities);
    }

    public void delete(E entity) {
        LOG.info("%s DELETE ENTITY", this.repository.getClass().getSimpleName());
        this.repository.delete(entity);
    }

    public void delete(Long id) {
        LOG.info("%s DELETE ENTITY [%s]", this.repository.getClass().getSimpleName(), id);
        this.repository.deleteById(id);
    }

    // Query

    public E findById(Long id) {
        LOG.info("%s FIND ENTITY [%s]", this.repository.getClass().getSimpleName(), id);
        return this.repository.findById(id).orElse(null);
    }

    public List<E> findAll() {
        LOG.info("%s FIND ALL ENTITIES", this.repository.getClass().getSimpleName());
        return this.repository.findAll();
    }

    public List<E> findAllWithPageable(Pageable pageable) {
        LOG.info("%s FIND ALL ENTITIES WITH PAGEABLE", this.repository.getClass().getSimpleName());
        return this.repository.findAll(pageable).getContent();
    }

}
