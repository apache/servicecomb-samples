package org.apache.servicecomb.samples.practise.houserush.sale.aggregate.view;

import java.util.Date;


import lombok.Data;

@Data
public class SaleSummary {
  private Integer id;

  private String state;

  private Integer realestateId;

  private String realestateName;

  private Date beginAt;

  private Date endAt;
}
