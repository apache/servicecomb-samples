/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.practise.houserush.realestate.service;

import org.apache.http.HttpStatus;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.HouseType;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Realestate;
import org.apache.servicecomb.samples.practise.houserush.realestate.dao.*;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RealestateServiceImpl implements RealestateService {
  @Autowired
  private RealestateDao realestateDao;

  @Autowired
  private BuildingDao buildingDao;

  @Autowired
  private HouseDao houseDao;

  @Autowired
  private HouseTypeDao houseTypeDao;

  @Autowired
  private TreeDao treeDao;

  @Override
  public Realestate createRealesate(Realestate realestate) {
    return realestateDao.save(realestate);
  }

  @Override
  public Realestate findRealestate(Integer id) {
    return realestateDao.findOne(id);
  }

  @Override
  @Transactional
  public Realestate updateRealestate(Realestate realestate) {
    int id = realestate.getId();
    if (realestateDao.exists(id)) {
      return realestateDao.save(realestate);
    } else {
      throw new DataRetrievalFailureException("cannot update the non-existed realestate");
    }
  }

  @Override
  public void removeRealestate(Integer id) {
    realestateDao.delete(id);
  }

  @Override
  public List<Realestate> indexRealestates() {
    return realestateDao.findAll();
  }

  @Override
  public Building createBuilding(Integer realestateId, Building building) {
    Realestate realestate = realestateDao.findOne(realestateId);
    if (null == realestate) {
      throw new DataRetrievalFailureException("cannot create buildings for the not-existed realestate");
    } else {
      building.setRealestate(realestate);
      return buildingDao.save(building);
    }
  }

  @Override
  public Building findBuilding(Integer id) {
    return buildingDao.findOne(id);
  }

  @Override
  @Transactional
  public Building updateBuilding(Building building) {
    int id = building.getId();
    if (buildingDao.exists(id)) {
      return buildingDao.save(building);
    } else {
      throw new DataRetrievalFailureException("cannot update the non-existed building");
    }
  }

  @Override
  @Transactional
  public void removeBuilding(Integer id) {
    buildingDao.delete(id);
  }

  @Override
  public List<Building> indexBuildings(Integer realestateId) {
    Realestate realestate = realestateDao.findOne(realestateId);
    return realestate.getBuildings();
  }

  @Override
  public House createHouse(Integer buildingId, House house) {
    Building building = buildingDao.findOne(buildingId);
    if (building != null) {
      house.setBuilding(building);
      return houseDao.save(house);
    } else {
      throw new DataRetrievalFailureException("cannot create house for the non-existed building");
    }
  }

  @Override
  public House findHouse(Integer id) {
    return houseDao.findOne(id);
  }

  @Override
  public House updateHouse(House house) {
    int id = house.getId();
    if (houseDao.exists(id)) {
      return houseDao.save(house);
    } else {
      throw new DataRetrievalFailureException("cannot update the non-existed house");
    }
  }

  @Override
  public void removeHouse(Integer id) {
    houseDao.delete(id);
  }

  @Override
  public List<House> indexHouses(Integer buildingId) {
    Building building = buildingDao.findOne(buildingId);
    if (building != null) {
      return building.getHouses();
    } else {
      throw new DataRetrievalFailureException("cannot index the houses for the non-existed building");
    }
  }

  @Override
  @Transactional
  public List<House> lockHousesForSale(List<Integer> houseIds) {
    List<House> houses = houseDao.findAllByIdInForUpdate(houseIds);
    houses.forEach(house -> {
      if (!"in_stock".equals(house.getState())) {
        throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "house " + house.getId() + " is not in_stock.");
      }
    });
    houseDao.updateLockingStatesForHouses(houseIds);
    return houses;
  }

  @Override
  public HouseType createHouseType(HouseType houseType) {
    return houseTypeDao.save(houseType);
  }

  @Override
  public void removeHouseType(Integer id) {
    houseTypeDao.delete(id);
  }

  @Override
  public HouseType updateHouseType(HouseType houseType) {
    int id = houseType.getId();
    if(houseTypeDao.exists(id)){
      return houseTypeDao.save(houseType);
    }else {
      throw new DataRetrievalFailureException("cannot update the non-existed house type.");
    }
  }

  @Override
  public HouseType findHouseType(Integer id) {
    return houseTypeDao.findOne(id);
  }

  @Override
  public List<HouseType> indexHouseTypes() {
    return houseTypeDao.findAll();
  }

  @Override
  @Transactional
  public org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.tree.Realestate findTreeRealestate(Integer id) {
    return treeDao.findOne(id);
  }
  @Transactional
  public List<Building> findByRealestateId(Integer realestateId){
    return buildingDao.findByRealestateId(realestateId);
  }

}
