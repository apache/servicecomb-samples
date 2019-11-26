package org.apache.servicecomb.samples.practise.houserush.realestate.dao;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.view.Realestate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeDao  extends JpaRepository<Realestate, Integer> {
}
