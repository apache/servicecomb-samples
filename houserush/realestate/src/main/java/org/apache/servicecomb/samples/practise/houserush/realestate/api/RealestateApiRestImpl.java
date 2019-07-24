package org.apache.servicecomb.samples.practise.houserush.realestate.api;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Building;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.House;
import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.Realestate;
import org.apache.servicecomb.samples.practise.houserush.realestate.service.RealestateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestSchema(schemaId = "realestateApiRest")
@RequestMapping("/")
public class RealestateApiRestImpl implements RealestateApi {

    @Autowired
    private RealestateService realestateService;


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
    public Building createBuilding(@PathVariable("realestateId") int realestateId, Building building) {
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
    public House createHouse(@PathVariable("buildingId") int buildingId, House house) {
        return realestateService.createHouse(buildingId, house);
    }

    @GetMapping("houses/{id}")
    public House findHouse(@PathVariable("id") int id) {
        return realestateService.findHouse(id);
    }

    @PutMapping("houses/{id}")
    public House updateHouse(@PathVariable("id") int id, House house) {
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
}
