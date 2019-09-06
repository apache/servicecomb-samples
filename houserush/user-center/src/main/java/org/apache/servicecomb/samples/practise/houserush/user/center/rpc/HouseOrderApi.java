package org.apache.servicecomb.samples.practise.houserush.user.center.rpc;

import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.po.Favorite;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.po.HouseOrder;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.po.Sale;

import java.util.List;

public interface HouseOrderApi {
  List<Favorite> findMyFavorite(int customerId);

  Sale findSaleByRealestateId(int realestateId);

  HouseOrder findOne(int houseOrderId);

  List<Sale> indexSales();
}
