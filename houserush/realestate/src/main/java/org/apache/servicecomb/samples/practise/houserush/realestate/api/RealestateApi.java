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

package org.apache.servicecomb.samples.practise.houserush.realestate.api;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RealestateApi {
  Realestate createRealestate(Realestate realestate);

  Realestate findRealestate(int id);

  Realestate updateRealestate(int id, Realestate realestate);

  void removeRealestate(int id);

  List<Realestate> indexRealestates();

  Building createBuilding(int realestateId, Building building);

  Building findBuilding(int id);

  Building updateBuilding(int id, Building building);

  void removeBuilding(int id);

  List<Building> indexBuildings(int realestateId);

  House createHouse(int buidingId, House house);

  House findHouse(int id);

  House updateHouse(int id, House house);

  void removeHouse(int id);

  List<House> indexHouses(int buildingId);

  List<House> lockHousesForSale(List<Integer> ids);

  HouseType createHouseType(HouseType houseType);

  void removeHouseType(int id);

  HouseType updateHouseType(int id, HouseType houseType);

  HouseType findHouseType(int id);

  List<HouseType> indexHouseTypes();

  HouseTypeImage createHouseTypeImage(MultipartFile file);

  void removeHouseTypeImage(int id);

  byte[] findHouseTypeImage(int id);

  public Map<String,Object>  findByRealestateId(Integer realestateId);
}