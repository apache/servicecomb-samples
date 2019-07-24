package org.apache.servicecomb.samples.practise.houserush.realestate.dao;

import java.util.List;

public interface HouseDaoMore {
    int updateLockingStatesForHouses(List<Integer> ids);
}
