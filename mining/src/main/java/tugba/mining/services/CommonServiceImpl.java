package tugba.mining.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tugba.mining.domain.BaseEntity;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	EntityManager entityManager;

	@Override
	public <T extends BaseEntity> List<T> listAll(Class<T> clazz) {
		return entityManager.createQuery("select e from Event e", clazz).getResultList();
	}

	@Override
	public <T extends BaseEntity> T getById(T clazz, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(BaseEntity entity) {
		entityManager.persist(entity);
	}

	@Override
	public <T extends BaseEntity> void delete(T clazz, Long id) {
		// TODO Auto-generated method stub
		
	}
}
