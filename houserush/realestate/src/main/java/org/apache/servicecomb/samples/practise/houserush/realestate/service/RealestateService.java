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

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.HouseType;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Realestate;

import java.util.List;

public interface RealestateService {
  Realestate createRealesate(Realestate realestate);

  Realestate findRealestate(Integer id);

  Realestate updateRealestate(Realestate realestate);

  void removeRealestate(Integer id);

  List<Realestate> indexRealestates();

  Building createBuilding(Integer realestateId, Building building);

  Building findBuilding(Integer id);

  Building updateBuilding(Building building);

  void removeBuilding(Integer id);

  List<Building> indexBuildings(Integer realestateId);

  House createHouse(Integer buildingId, House house);

  House findHouse(Integer id);

  List<House> findHouses(List<Integer> ids);

  House updateHouse(House house);

  void removeHouse(Integer id);

  List<House> indexHouses(Integer buildingId);

  List<House> lockHousesForSale(List<Integer> houseIds);

  HouseType createHouseType(HouseType houseType);

  void removeHouseType(Integer id);

  HouseType updateHouseType(HouseType houseType);

  HouseType findHouseType(Integer id);

  List<HouseType> indexHouseTypes();

  org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.view.Realestate findTreeRealestate(Integer id);
}
