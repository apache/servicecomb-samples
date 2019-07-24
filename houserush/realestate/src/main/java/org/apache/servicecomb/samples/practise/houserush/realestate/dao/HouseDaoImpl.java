package org.apache.servicecomb.samples.practise.houserush.realestate.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
public class HouseDaoImpl implements HouseDaoMore {
    @PersistenceContext
    private EntityManager em;

    @Override
    public int updateLockingStatesForHouses(List<Integer> ids) {
        return em.createQuery("UPDATE House h set h.state = 'locking' where h.id in (?1)").setParameter(1, ids).executeUpdate();
    }
}
