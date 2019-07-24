package org.apache.servicecomb.samples.practise.houserush.realestate.dao;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingDao extends JpaRepository<Building, Integer> {
}
