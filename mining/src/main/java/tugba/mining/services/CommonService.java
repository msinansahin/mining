package tugba.mining.services;

import java.util.List;

import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Event;

public interface CommonService {

    List<Event> listEvent();

    <T> T getById(Class <T> clazz, Long id);

    void saveOrUpdate(BaseEntity entity);

    <T> void delete(T clazz, Long id);

}
