package pvs.app.common;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class PVSRepository<IdType, EntityType, DataAccessor extends CrudRepository<EntityType, IdType>> {
    protected DataAccessor dataAccessor;

    public PVSRepository() {
    }

    public PVSRepository(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public EntityType get(IdType id) {
        Optional<EntityType> p = dataAccessor.findById(id);
        return p.orElse(null);
    }

    public EntityType put(IdType id, EntityType entity) {
        if (dataAccessor.existsById(id)) dataAccessor.deleteById(id);
        return dataAccessor.save(entity);
    }

    public void putAll(Iterable<EntityType> entities) {
        dataAccessor.saveAll(entities);
    }

    public EntityType remove(IdType id) {
        EntityType removedEntity;
        Optional<EntityType> optional = dataAccessor.findById(id);
        if (optional.isPresent()) {
            removedEntity = optional.get();
            dataAccessor.delete(removedEntity);
        } else {
            removedEntity = null;
        }
        return removedEntity;
    }

    public void removeAll(Iterable<EntityType> entities) {
        dataAccessor.deleteAll(entities);
    }

    public void clear() {
        dataAccessor.deleteAll();
    }

    public boolean contains(IdType id) {
        return dataAccessor.existsById(id);
    }

    public long count() {
        return dataAccessor.count();
    }
}
