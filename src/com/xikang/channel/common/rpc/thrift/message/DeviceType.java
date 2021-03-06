/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xikang.channel.common.rpc.thrift.message;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum DeviceType implements org.apache.thrift.TEnum {
  WEB(1),
  XK_WATCH(101),
  XK_TERMINAL(201),
  ANDROID(1001),
  IOS(1002);

  private final int value;

  private DeviceType(int value) {
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
  public static DeviceType findByValue(int value) { 
    switch (value) {
      case 1:
        return WEB;
      case 101:
        return XK_WATCH;
      case 201:
        return XK_TERMINAL;
      case 1001:
        return ANDROID;
      case 1002:
        return IOS;
      default:
        return null;
    }
  }
}
