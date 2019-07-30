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

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Realestate;

import java.util.List;

public interface RealestateApi {
  /**
   * 新增楼盘
   *
   * @param realestate 楼盘信息
   * @return Realestate 添加成功后的楼盘信息
   */
  Realestate createRealestate(Realestate realestate);

  /**
   * 查询楼盘
   *
   * @param id 楼盘id
   * @return Realestate 楼盘信息
   */
  Realestate findRealestate(int id);

  /**
   * 修改楼盘信息
   *
   * @param id         楼盘id
   * @param realestate 楼盘信息
   * @return Realestate 修改成功后的楼盘信息
   */
  Realestate updateRealestate(int id, Realestate realestate);

  /**
   * 删除楼盘
   *
   * @param id 楼盘id
   */
  void removeRealestate(int id);

  /**
   * 查询所有楼盘
   *
   * @return List<Realestate> 所有楼盘列表
   */
  List<Realestate> indexRealestates();

  /**
   * 新增建筑楼
   *
   * @param realestateId 楼盘id
   * @param building     建筑楼信息
   * @return Building 添加成功后的建筑楼信息
   */
  Building createBuilding(int realestateId, Building building);

  /**
   * 查询建筑楼
   *
   * @param id 建筑楼id
   * @return Building 建筑楼信息
   */
  Building findBuilding(int id);

  /**
   * 更改建筑楼信息
   *
   * @param id       建筑楼id
   * @param building 建筑楼信息
   * @return Building 更改成功后的建筑楼信息
   */
  Building updateBuilding(int id, Building building);

  /**
   * 删除建筑楼
   *
   * @param id 建筑楼id
   */
  void removeBuilding(int id);

  /**
   * 查询某一楼盘下的所有建筑楼
   *
   * @param realestateId 楼盘id
   * @return List<Building> 建筑楼列表
   */
  List<Building> indexBuildings(int realestateId);

  /**
   * 新增房源信息
   *
   * @param buidingId 建筑楼id
   * @param house     房源信息
   * @return House 添加成功后的房源信息
   */
  House createHouse(int buidingId, House house);

  /**
   * 查询房源信息
   *
   * @param id 房源id
   * @return House 房源信息
   */
  House findHouse(int id);

  /**
   * 更改房源信息
   *
   * @param id    房源id
   * @param house 房源信息
   * @return House 更改成功后的房源信息
   */
  House updateHouse(int id, House house);

  /**
   * 删除房源信息
   *
   * @param id 房源id
   */
  void removeHouse(int id);

  /**
   * 查询某一建筑楼下的所有房源
   *
   * @param buildingId 建筑楼id
   * @return List<House> 所有房源列表
   */
  List<House> indexHouses(int buildingId);

  /**
   * 锁定已售房源
   *
   * @param ids 已售房源id列表
   * @return List<House> 锁定的房源列表
   */
  List<House> lockHousesForSale(List<Integer> ids);
}