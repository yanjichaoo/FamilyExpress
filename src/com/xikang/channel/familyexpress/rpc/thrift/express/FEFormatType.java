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

public enum FEFormatType implements org.apache.thrift.TEnum {
  TEXT(0),
  SPX(1),
  OGG(2),
  MP3(3),
  PNG(4),
  JPG(5),
  GIF(6),
  _3GP(7);

  private final int value;

  private FEFormatType(int value) {
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
  public static FEFormatType findByValue(int value) { 
    switch (value) {
      case 0:
        return TEXT;
      case 1:
        return SPX;
      case 2:
        return OGG;
      case 3:
        return MP3;
      case 4:
        return PNG;
      case 5:
        return JPG;
      case 6:
        return GIF;
      case 7:
        return _3GP;
      default:
        return null;
    }
  }
}
