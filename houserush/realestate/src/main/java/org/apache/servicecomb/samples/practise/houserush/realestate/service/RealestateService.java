package org.apache.servicecomb.samples.practise.houserush.realestate.service;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
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

    House updateHouse(House house);

    void removeHouse(Integer id);

    List<House> indexHouses(Integer buildingId);

    List<House> lockHousesForSale(List<Integer> houseIds);

}
