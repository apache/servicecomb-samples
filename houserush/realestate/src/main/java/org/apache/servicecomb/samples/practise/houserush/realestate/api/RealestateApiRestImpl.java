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

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.*;
import org.apache.servicecomb.samples.practise.houserush.realestate.service.HouseTypeImageService;
import org.apache.servicecomb.samples.practise.houserush.realestate.service.RealestateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestSchema(schemaId = "realestateApiRest")
@RequestMapping("/")
public class RealestateApiRestImpl implements RealestateApi {

  @Autowired
  private RealestateService realestateService;

  @Autowired
  private HouseTypeImageService houseTypeImageService;

  @PostMapping("/realestates")
  public Realestate createRealestate(@RequestBody Realestate realestate) {
    return realestateService.createRealesate(realestate);
  }

  @GetMapping("/realestates/{id}")
  public Realestate findRealestate(@PathVariable("id") int id) {
    return realestateService.findRealestate(id);
  }

  @PutMapping("realestates/{id}")
  public Realestate updateRealestate(@PathVariable("id") int id, @RequestBody Realestate realestate) {
    realestate.setId(id);
    return realestateService.updateRealestate(realestate);
  }

  @DeleteMapping("realestates/{id}")
  public void removeRealestate(@PathVariable("id") int id) {
    realestateService.removeRealestate(id);
  }

  @GetMapping("realestates")
  public List<Realestate> indexRealestates() {
    return realestateService.indexRealestates();
  }

  @PostMapping("realestates/{realestateId}/buildings")
  public Building createBuilding(@PathVariable("realestateId") int realestateId, @RequestBody Building building) {
    return realestateService.createBuilding(realestateId, building);
  }

  @GetMapping("buildings/{id}")
  public Building findBuilding(@PathVariable("id") int id) {
    return realestateService.findBuilding(id);
  }

  @PutMapping("buildings/{id}")
  public Building updateBuilding(@PathVariable("id") int id, @RequestBody Building building) {
    building.setId(id);
    return realestateService.updateBuilding(building);
  }

  @DeleteMapping("buildings/{id}")
  public void removeBuilding(@PathVariable("id") int id) {
    realestateService.removeBuilding(id);
  }

  @GetMapping("realestates/{realestateId}/buildings")
  public List<Building> indexBuildings(@PathVariable("realestateId") int realestateId) {
    return realestateService.indexBuildings(realestateId);
  }

  @PostMapping("buildings/{buildingId}/houses")
  public House createHouse(@PathVariable("buildingId") int buildingId, @RequestBody House house) {
    return realestateService.createHouse(buildingId, house);
  }

  @GetMapping("houses/{id}")
  public House findHouse(@PathVariable("id") int id) {
    return realestateService.findHouse(id);
  }

  @PutMapping("houses/{id}")
  public House updateHouse(@PathVariable("id") int id, @RequestBody House house) {
    house.setId(id);
    return realestateService.updateHouse(house);
  }

  @DeleteMapping("houses/{id}")
  public void removeHouse(@PathVariable("id") int id) {
    realestateService.removeHouse(id);
  }

  @GetMapping("buildings/{buildingId}/houses")
  public List<House> indexHouses(@PathVariable("buildingId") int buildingId) {
    return realestateService.indexHouses(buildingId);
  }

  @PutMapping("houses/lock_houses_for_sale")
  public List<House> lockHousesForSale(@RequestBody List<Integer> ids) {
    return realestateService.lockHousesForSale(ids);
  }

  @PostMapping("housetype")
  public HouseType createHouseType(@RequestBody HouseType houseType){
    return realestateService.createHouseType(houseType);
  }

  @PutMapping("housetype/{id}")
  public HouseType updateHouseType(@PathVariable("id") int id, @RequestBody HouseType houseType){
    houseType.setId(id);

    //Delete original image if image has been changed.
    HouseType oldVersion = realestateService.findHouseType(id);
    if(oldVersion != null && oldVersion.getImageId() != houseType.getImageId()){
      houseTypeImageService.removeHouseTypeImage(oldVersion.getImageId());
    }

    return realestateService.updateHouseType(houseType);
  }

  @DeleteMapping("housetype/{id}")
  public void removeHouseType(@PathVariable("id") int id){
    //Delete house type image if it is exists.
    HouseType type = realestateService.findHouseType(id);
    if(type != null && type.getImageId() != 0){
      houseTypeImageService.removeHouseTypeImage(type.getImageId());
    }

    realestateService.removeHouseType(id);
  }

  @GetMapping("housetype/{id}")
  public HouseType findHouseType(@PathVariable("id") int id){
    return realestateService.findHouseType(id);
  }

  @GetMapping("housetype")
  public List<HouseType> indexHouseTypes(){
    return realestateService.indexHouseTypes();
  }

  @PostMapping(value = "housetype/image")
  public HouseTypeImage createHouseTypeImage(@RequestPart(name = "file") MultipartFile file) {
    return houseTypeImageService.createHouseTypeImage(file);
  }

  @DeleteMapping("housetype/image/{id}")
  public void removeHouseTypeImage(@PathVariable("id") int id) {
    houseTypeImageService.removeHouseTypeImage(id);
  }

  @GetMapping(value = "housetype/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] findHouseTypeImage(@PathVariable("id") int id) {
    return houseTypeImageService.findHouseTypeImage(id);
  }

}
