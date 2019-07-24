package org.apache.servicecomb.samples.practise.houserush.realestate.dao;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Realestate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealestateDao extends JpaRepository<Realestate, Integer> {
}
