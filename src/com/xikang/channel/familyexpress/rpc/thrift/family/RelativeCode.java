/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xikang.channel.familyexpress.rpc.thrift.family;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum RelativeCode implements org.apache.thrift.TEnum {
  ELDER(1),
  FELLOW(2),
  YOUNGER(3);

  private final int value;

  private RelativeCode(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static RelativeCode findByValue(int value) { 
    switch (value) {
      case 1:
        return ELDER;
      case 2:
        return FELLOW;
      case 3:
        return YOUNGER;
      default:
        return null;
    }
  }
}
