/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xikang.channel.common.rpc.thrift.message;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigestAuthenticationReq implements org.apache.thrift.TBase<DigestAuthenticationReq, DigestAuthenticationReq._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DigestAuthenticationReq");

  private static final org.apache.thrift.protocol.TField CLIENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("clientId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CLIENT_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("clientCount", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField CLIENT_RANDOM_FIELD_DESC = new org.apache.thrift.protocol.TField("clientRandom", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField ACCESS_TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("accessToken", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DigestAuthenticationReqStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DigestAuthenticationReqTupleSchemeFactory());
  }

  public String clientId; // required
  public long clientCount; // required
  public String clientRandom; // required
  public String accessToken; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CLIENT_ID((short)1, "clientId"),
    CLIENT_COUNT((short)2, "clientCount"),
    CLIENT_RANDOM((short)3, "clientRandom"),
    ACCESS_TOKEN((short)4, "accessToken");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // CLIENT_ID
          return CLIENT_ID;
        case 2: // CLIENT_COUNT
          return CLIENT_COUNT;
        case 3: // CLIENT_RANDOM
          return CLIENT_RANDOM;
        case 4: // ACCESS_TOKEN
          return ACCESS_TOKEN;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __CLIENTCOUNT_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CLIENT_ID, new org.apache.thrift.meta_data.FieldMetaData("clientId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CLIENT_COUNT, new org.apache.thrift.meta_data.FieldMetaData("clientCount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CLIENT_RANDOM, new org.apache.thrift.meta_data.FieldMetaData("clientRandom", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ACCESS_TOKEN, new org.apache.thrift.meta_data.FieldMetaData("accessToken", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DigestAuthenticationReq.class, metaDataMap);
  }

  public DigestAuthenticationReq() {
  }

  public DigestAuthenticationReq(
    String clientId,
    long clientCount,
    String clientRandom,
    String accessToken)
  {
    this();
    this.clientId = clientId;
    this.clientCount = clientCount;
    setClientCountIsSet(true);
    this.clientRandom = clientRandom;
    this.accessToken = accessToken;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DigestAuthenticationReq(DigestAuthenticationReq other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetClientId()) {
      this.clientId = other.clientId;
    }
    this.clientCount = other.clientCount;
    if (other.isSetClientRandom()) {
      this.clientRandom = other.clientRandom;
    }
    if (other.isSetAccessToken()) {
      this.accessToken = other.accessToken;
    }
  }

  public DigestAuthenticationReq deepCopy() {
    return new DigestAuthenticationReq(this);
  }

  @Override
  public void clear() {
    this.clientId = null;
    setClientCountIsSet(false);
    this.clientCount = 0;
    this.clientRandom = null;
    this.accessToken = null;
  }

  public String getClientId() {
    return this.clientId;
  }

  public DigestAuthenticationReq setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public void unsetClientId() {
    this.clientId = null;
  }

  /** Returns true if field clientId is set (has been assigned a value) and false otherwise */
  public boolean isSetClientId() {
    return this.clientId != null;
  }

  public void setClientIdIsSet(boolean value) {
    if (!value) {
      this.clientId = null;
    }
  }

  public long getClientCount() {
    return this.clientCount;
  }

  public DigestAuthenticationReq setClientCount(long clientCount) {
    this.clientCount = clientCount;
    setClientCountIsSet(true);
    return this;
  }

  public void unsetClientCount() {
    __isset_bit_vector.clear(__CLIENTCOUNT_ISSET_ID);
  }

  /** Returns true if field clientCount is set (has been assigned a value) and false otherwise */
  public boolean isSetClientCount() {
    return __isset_bit_vector.get(__CLIENTCOUNT_ISSET_ID);
  }

  public void setClientCountIsSet(boolean value) {
    __isset_bit_vector.set(__CLIENTCOUNT_ISSET_ID, value);
  }

  public String getClientRandom() {
    return this.clientRandom;
  }

  public DigestAuthenticationReq setClientRandom(String clientRandom) {
    this.clientRandom = clientRandom;
    return this;
  }

  public void unsetClientRandom() {
    this.clientRandom = null;
  }

  /** Returns true if field clientRandom is set (has been assigned a value) and false otherwise */
  public boolean isSetClientRandom() {
    return this.clientRandom != null;
  }

  public void setClientRandomIsSet(boolean value) {
    if (!value) {
      this.clientRandom = null;
    }
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public DigestAuthenticationReq setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public void unsetAccessToken() {
    this.accessToken = null;
  }

  /** Returns true if field accessToken is set (has been assigned a value) and false otherwise */
  public boolean isSetAccessToken() {
    return this.accessToken != null;
  }

  public void setAccessTokenIsSet(boolean value) {
    if (!value) {
      this.accessToken = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CLIENT_ID:
      if (value == null) {
        unsetClientId();
      } else {
        setClientId((String)value);
      }
      break;

    case CLIENT_COUNT:
      if (value == null) {
        unsetClientCount();
      } else {
        setClientCount((Long)value);
      }
      break;

    case CLIENT_RANDOM:
      if (value == null) {
        unsetClientRandom();
      } else {
        setClientRandom((String)value);
      }
      break;

    case ACCESS_TOKEN:
      if (value == null) {
        unsetAccessToken();
      } else {
        setAccessToken((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CLIENT_ID:
      return getClientId();

    case CLIENT_COUNT:
      return Long.valueOf(getClientCount());

    case CLIENT_RANDOM:
      return getClientRandom();

    case ACCESS_TOKEN:
      return getAccessToken();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CLIENT_ID:
      return isSetClientId();
    case CLIENT_COUNT:
      return isSetClientCount();
    case CLIENT_RANDOM:
      return isSetClientRandom();
    case ACCESS_TOKEN:
      return isSetAccessToken();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DigestAuthenticationReq)
      return this.equals((DigestAuthenticationReq)that);
    return false;
  }

  public boolean equals(DigestAuthenticationReq that) {
    if (that == null)
      return false;

    boolean this_present_clientId = true && this.isSetClientId();
    boolean that_present_clientId = true && that.isSetClientId();
    if (this_present_clientId || that_present_clientId) {
      if (!(this_present_clientId && that_present_clientId))
        return false;
      if (!this.clientId.equals(that.clientId))
        return false;
    }

    boolean this_present_clientCount = true;
    boolean that_present_clientCount = true;
    if (this_present_clientCount || that_present_clientCount) {
      if (!(this_present_clientCount && that_present_clientCount))
        return false;
      if (this.clientCount != that.clientCount)
        return false;
    }

    boolean this_present_clientRandom = true && this.isSetClientRandom();
    boolean that_present_clientRandom = true && that.isSetClientRandom();
    if (this_present_clientRandom || that_present_clientRandom) {
      if (!(this_present_clientRandom && that_present_clientRandom))
        return false;
      if (!this.clientRandom.equals(that.clientRandom))
        return false;
    }

    boolean this_present_accessToken = true && this.isSetAccessToken();
    boolean that_present_accessToken = true && that.isSetAccessToken();
    if (this_present_accessToken || that_present_accessToken) {
      if (!(this_present_accessToken && that_present_accessToken))
        return false;
      if (!this.accessToken.equals(that.accessToken))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(DigestAuthenticationReq other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    DigestAuthenticationReq typedOther = (DigestAuthenticationReq)other;

    lastComparison = Boolean.valueOf(isSetClientId()).compareTo(typedOther.isSetClientId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClientId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.clientId, typedOther.clientId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetClientCount()).compareTo(typedOther.isSetClientCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClientCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.clientCount, typedOther.clientCount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetClientRandom()).compareTo(typedOther.isSetClientRandom());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClientRandom()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.clientRandom, typedOther.clientRandom);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAccessToken()).compareTo(typedOther.isSetAccessToken());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAccessToken()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.accessToken, typedOther.accessToken);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DigestAuthenticationReq(");
    boolean first = true;

    sb.append("clientId:");
    if (this.clientId == null) {
      sb.append("null");
    } else {
      sb.append(this.clientId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("clientCount:");
    sb.append(this.clientCount);
    first = false;
    if (!first) sb.append(", ");
    sb.append("clientRandom:");
    if (this.clientRandom == null) {
      sb.append("null");
    } else {
      sb.append(this.clientRandom);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("accessToken:");
    if (this.accessToken == null) {
      sb.append("null");
    } else {
      sb.append(this.accessToken);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DigestAuthenticationReqStandardSchemeFactory implements SchemeFactory {
    public DigestAuthenticationReqStandardScheme getScheme() {
      return new DigestAuthenticationReqStandardScheme();
    }
  }

  private static class DigestAuthenticationReqStandardScheme extends StandardScheme<DigestAuthenticationReq> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DigestAuthenticationReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CLIENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.clientId = iprot.readString();
              struct.setClientIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CLIENT_COUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.clientCount = iprot.readI64();
              struct.setClientCountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CLIENT_RANDOM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.clientRandom = iprot.readString();
              struct.setClientRandomIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ACCESS_TOKEN
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.accessToken = iprot.readString();
              struct.setAccessTokenIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DigestAuthenticationReq struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.clientId != null) {
        oprot.writeFieldBegin(CLIENT_ID_FIELD_DESC);
        oprot.writeString(struct.clientId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(CLIENT_COUNT_FIELD_DESC);
      oprot.writeI64(struct.clientCount);
      oprot.writeFieldEnd();
      if (struct.clientRandom != null) {
        oprot.writeFieldBegin(CLIENT_RANDOM_FIELD_DESC);
        oprot.writeString(struct.clientRandom);
        oprot.writeFieldEnd();
      }
      if (struct.accessToken != null) {
        oprot.writeFieldBegin(ACCESS_TOKEN_FIELD_DESC);
        oprot.writeString(struct.accessToken);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DigestAuthenticationReqTupleSchemeFactory implements SchemeFactory {
    public DigestAuthenticationReqTupleScheme getScheme() {
      return new DigestAuthenticationReqTupleScheme();
    }
  }

  private static class DigestAuthenticationReqTupleScheme extends TupleScheme<DigestAuthenticationReq> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DigestAuthenticationReq struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetClientId()) {
        optionals.set(0);
      }
      if (struct.isSetClientCount()) {
        optionals.set(1);
      }
      if (struct.isSetClientRandom()) {
        optionals.set(2);
      }
      if (struct.isSetAccessToken()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetClientId()) {
        oprot.writeString(struct.clientId);
      }
      if (struct.isSetClientCount()) {
        oprot.writeI64(struct.clientCount);
      }
      if (struct.isSetClientRandom()) {
        oprot.writeString(struct.clientRandom);
      }
      if (struct.isSetAccessToken()) {
        oprot.writeString(struct.accessToken);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DigestAuthenticationReq struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.clientId = iprot.readString();
        struct.setClientIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.clientCount = iprot.readI64();
        struct.setClientCountIsSet(true);
      }
      if (incoming.get(2)) {
        struct.clientRandom = iprot.readString();
        struct.setClientRandomIsSet(true);
      }
      if (incoming.get(3)) {
        struct.accessToken = iprot.readString();
        struct.setAccessTokenIsSet(true);
      }
    }
  }

}
