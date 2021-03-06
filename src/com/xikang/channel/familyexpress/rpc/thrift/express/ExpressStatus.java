/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xikang.channel.familyexpress.rpc.thrift.express;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum ExpressStatus implements org.apache.thrift.TEnum {
  SENT(0),
  FORWARDED(1),
  UNREAD(2),
  READ(3);

  private final int value;

  private ExpressStatus(int value) {
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
  public static ExpressStatus findByValue(int value) { 
    switch (value) {
      case 0:
        return SENT;
      case 1:
        return FORWARDED;
      case 2:
        return UNREAD;
      case 3:
        return READ;
      default:
        return null;
    }
  }
}
