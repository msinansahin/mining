package tugba.mining.services;

import java.util.List;

import tugba.mining.domain.BaseEntity;

public interface CommonService {

    <T extends BaseEntity> List<T> listAll(Class<T> clazz);

    <T extends BaseEntity> T getById(T clazz, Long id);

    void saveOrUpdate(BaseEntity entity);

    <T extends BaseEntity> void delete(T clazz, Long id);

}
