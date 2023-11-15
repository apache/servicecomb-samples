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
package org.apache.servicecomb.samples;

public class ChildDataModel {
  private int numInt;

  private long numLong;

  private double numDouble;

  private float numFloat;

  private String data;

  public int getNumInt() {
    return numInt;
  }

  public void setNumInt(int numInt) {
    this.numInt = numInt;
  }

  public long getNumLong() {
    return numLong;
  }

  public void setNumLong(long numLong) {
    this.numLong = numLong;
  }

  public double getNumDouble() {
    return numDouble;
  }

  public void setNumDouble(double numDouble) {
    this.numDouble = numDouble;
  }

  public float getNumFloat() {
    return numFloat;
  }

  public void setNumFloat(float numFloat) {
    this.numFloat = numFloat;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
