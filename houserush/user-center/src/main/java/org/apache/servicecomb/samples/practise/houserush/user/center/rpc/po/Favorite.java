package org.apache.servicecomb.samples.practise.houserush.user.center.rpc.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Favorite {

  private Integer id;

  private int houseOrderId;

  private Integer customerId;

  private Date CreatedAt;

  private Date UpdatedAt;

  private String houseName;

  private BigDecimal price;

  private String builDingName;

  private String realestateName;

  private String state;
}