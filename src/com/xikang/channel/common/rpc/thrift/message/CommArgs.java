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

public class CommArgs implements org.apache.thrift.TBase<CommArgs, CommArgs._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("CommArgs");

  private static final org.apache.thrift.protocol.TField TERMINAL_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("terminalInfo", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField APP_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("appInfo", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField I18N_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("i18nInfo", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField AUTH_MODE_FIELD_DESC = new org.apache.thrift.protocol.TField("authMode", org.apache.thrift.protocol.TType.I32, (short)5);
  private static final org.apache.thrift.protocol.TField DIGEST_AUTHENTICATION_REQ_FIELD_DESC = new org.apache.thrift.protocol.TField("digestAuthenticationReq", org.apache.thrift.protocol.TType.STRUCT, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CommArgsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CommArgsTupleSchemeFactory());
  }

  public TerminalInfo terminalInfo; // required
  public AppInfo appInfo; // required
  public String userId; // required
  public I18nInfo i18nInfo; // required
  /**
   * 
   * @see AuthMode
   */
  public AuthMode authMode; // required
  public DigestAuthenticationReq digestAuthenticationReq; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TERMINAL_INFO((short)1, "terminalInfo"),
    APP_INFO((short)2, "appInfo"),
    USER_ID((short)3, "userId"),
    I18N_INFO((short)4, "i18nInfo"),
    /**
     * 
     * @see AuthMode
     */
    AUTH_MODE((short)5, "authMode"),
    DIGEST_AUTHENTICATION_REQ((short)6, "digestAuthenticationReq");

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
        case 1: // TERMINAL_INFO
          return TERMINAL_INFO;
        case 2: // APP_INFO
          return APP_INFO;
        case 3: // USER_ID
          return USER_ID;
        case 4: // I18N_INFO
          return I18N_INFO;
        case 5: // AUTH_MODE
          return AUTH_MODE;
        case 6: // DIGEST_AUTHENTICATION_REQ
          return DIGEST_AUTHENTICATION_REQ;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TERMINAL_INFO, new org.apache.thrift.meta_data.FieldMetaData("terminalInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TerminalInfo.class)));
    tmpMap.put(_Fields.APP_INFO, new org.apache.thrift.meta_data.FieldMetaData("appInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, AppInfo.class)));
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.I18N_INFO, new org.apache.thrift.meta_data.FieldMetaData("i18nInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, I18nInfo.class)));
    tmpMap.put(_Fields.AUTH_MODE, new org.apache.thrift.meta_data.FieldMetaData("authMode", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, AuthMode.class)));
    tmpMap.put(_Fields.DIGEST_AUTHENTICATION_REQ, new org.apache.thrift.meta_data.FieldMetaData("digestAuthenticationReq", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DigestAuthenticationReq.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(CommArgs.class, metaDataMap);
  }

  public CommArgs() {
  }

  public CommArgs(
    TerminalInfo terminalInfo,
    AppInfo appInfo,
    String userId,
    I18nInfo i18nInfo,
    AuthMode authMode,
    DigestAuthenticationReq digestAuthenticationReq)
  {
    this();
    this.terminalInfo = terminalInfo;
    this.appInfo = appInfo;
    this.userId = userId;
    this.i18nInfo = i18nInfo;
    this.authMode = authMode;
    this.digestAuthenticationReq = digestAuthenticationReq;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CommArgs(CommArgs other) {
    if (other.isSetTerminalInfo()) {
      this.terminalInfo = new TerminalInfo(other.terminalInfo);
    }
    if (other.isSetAppInfo()) {
      this.appInfo = new AppInfo(other.appInfo);
    }
    if (other.isSetUserId()) {
      this.userId = other.userId;
    }
    if (other.isSetI18nInfo()) {
      this.i18nInfo = new I18nInfo(other.i18nInfo);
    }
    if (other.isSetAuthMode()) {
      this.authMode = other.authMode;
    }
    if (other.isSetDigestAuthenticationReq()) {
      this.digestAuthenticationReq = new DigestAuthenticationReq(other.digestAuthenticationReq);
    }
  }

  public CommArgs deepCopy() {
    return new CommArgs(this);
  }

  @Override
  public void clear() {
    this.terminalInfo = null;
    this.appInfo = null;
    this.userId = null;
    this.i18nInfo = null;
    this.authMode = null;
    this.digestAuthenticationReq = null;
  }

  public TerminalInfo getTerminalInfo() {
    return this.terminalInfo;
  }

  public CommArgs setTerminalInfo(TerminalInfo terminalInfo) {
    this.terminalInfo = terminalInfo;
    return this;
  }

  public void unsetTerminalInfo() {
    this.terminalInfo = null;
  }

  /** Returns true if field terminalInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetTerminalInfo() {
    return this.terminalInfo != null;
  }

  public void setTerminalInfoIsSet(boolean value) {
    if (!value) {
      this.terminalInfo = null;
    }
  }

  public AppInfo getAppInfo() {
    return this.appInfo;
  }

  public CommArgs setAppInfo(AppInfo appInfo) {
    this.appInfo = appInfo;
    return this;
  }

  public void unsetAppInfo() {
    this.appInfo = null;
  }

  /** Returns true if field appInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetAppInfo() {
    return this.appInfo != null;
  }

  public void setAppInfoIsSet(boolean value) {
    if (!value) {
      this.appInfo = null;
    }
  }

  public String getUserId() {
    return this.userId;
  }

  public CommArgs setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public void unsetUserId() {
    this.userId = null;
  }

  /** Returns true if field userId is set (has been assigned a value) and false otherwise */
  public boolean isSetUserId() {
    return this.userId != null;
  }

  public void setUserIdIsSet(boolean value) {
    if (!value) {
      this.userId = null;
    }
  }

  public I18nInfo getI18nInfo() {
    return this.i18nInfo;
  }

  public CommArgs setI18nInfo(I18nInfo i18nInfo) {
    this.i18nInfo = i18nInfo;
    return this;
  }

  public void unsetI18nInfo() {
    this.i18nInfo = null;
  }

  /** Returns true if field i18nInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetI18nInfo() {
    return this.i18nInfo != null;
  }

  public void setI18nInfoIsSet(boolean value) {
    if (!value) {
      this.i18nInfo = null;
    }
  }

  /**
   * 
   * @see AuthMode
   */
  public AuthMode getAuthMode() {
    return this.authMode;
  }

  /**
   * 
   * @see AuthMode
   */
  public CommArgs setAuthMode(AuthMode authMode) {
    this.authMode = authMode;
    return this;
  }

  public void unsetAuthMode() {
    this.authMode = null;
  }

  /** Returns true if field authMode is set (has been assigned a value) and false otherwise */
  public boolean isSetAuthMode() {
    return this.authMode != null;
  }

  public void setAuthModeIsSet(boolean value) {
    if (!value) {
      this.authMode = null;
    }
  }

  public DigestAuthenticationReq getDigestAuthenticationReq() {
    return this.digestAuthenticationReq;
  }

  public CommArgs setDigestAuthenticationReq(DigestAuthenticationReq digestAuthenticationReq) {
    this.digestAuthenticationReq = digestAuthenticationReq;
    return this;
  }

  public void unsetDigestAuthenticationReq() {
    this.digestAuthenticationReq = null;
  }

  /** Returns true if field digestAuthenticationReq is set (has been assigned a value) and false otherwise */
  public boolean isSetDigestAuthenticationReq() {
    return this.digestAuthenticationReq != null;
  }

  public void setDigestAuthenticationReqIsSet(boolean value) {
    if (!value) {
      this.digestAuthenticationReq = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TERMINAL_INFO:
      if (value == null) {
        unsetTerminalInfo();
      } else {
        setTerminalInfo((TerminalInfo)value);
      }
      break;

    case APP_INFO:
      if (value == null) {
        unsetAppInfo();
      } else {
        setAppInfo((AppInfo)value);
      }
      break;

    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((String)value);
      }
      break;

    case I18N_INFO:
      if (value == null) {
        unsetI18nInfo();
      } else {
        setI18nInfo((I18nInfo)value);
      }
      break;

    case AUTH_MODE:
      if (value == null) {
        unsetAuthMode();
      } else {
        setAuthMode((AuthMode)value);
      }
      break;

    case DIGEST_AUTHENTICATION_REQ:
      if (value == null) {
        unsetDigestAuthenticationReq();
      } else {
        setDigestAuthenticationReq((DigestAuthenticationReq)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TERMINAL_INFO:
      return getTerminalInfo();

    case APP_INFO:
      return getAppInfo();

    case USER_ID:
      return getUserId();

    case I18N_INFO:
      return getI18nInfo();

    case AUTH_MODE:
      return getAuthMode();

    case DIGEST_AUTHENTICATION_REQ:
      return getDigestAuthenticationReq();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TERMINAL_INFO:
      return isSetTerminalInfo();
    case APP_INFO:
      return isSetAppInfo();
    case USER_ID:
      return isSetUserId();
    case I18N_INFO:
      return isSetI18nInfo();
    case AUTH_MODE:
      return isSetAuthMode();
    case DIGEST_AUTHENTICATION_REQ:
      return isSetDigestAuthenticationReq();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CommArgs)
      return this.equals((CommArgs)that);
    return false;
  }

  public boolean equals(CommArgs that) {
    if (that == null)
      return false;

    boolean this_present_terminalInfo = true && this.isSetTerminalInfo();
    boolean that_present_terminalInfo = true && that.isSetTerminalInfo();
    if (this_present_terminalInfo || that_present_terminalInfo) {
      if (!(this_present_terminalInfo && that_present_terminalInfo))
        return false;
      if (!this.terminalInfo.equals(that.terminalInfo))
        return false;
    }

    boolean this_present_appInfo = true && this.isSetAppInfo();
    boolean that_present_appInfo = true && that.isSetAppInfo();
    if (this_present_appInfo || that_present_appInfo) {
      if (!(this_present_appInfo && that_present_appInfo))
        return false;
      if (!this.appInfo.equals(that.appInfo))
        return false;
    }

    boolean this_present_userId = true && this.isSetUserId();
    boolean that_present_userId = true && that.isSetUserId();
    if (this_present_userId || that_present_userId) {
      if (!(this_present_userId && that_present_userId))
        return false;
      if (!this.userId.equals(that.userId))
        return false;
    }

    boolean this_present_i18nInfo = true && this.isSetI18nInfo();
    boolean that_present_i18nInfo = true && that.isSetI18nInfo();
    if (this_present_i18nInfo || that_present_i18nInfo) {
      if (!(this_present_i18nInfo && that_present_i18nInfo))
        return false;
      if (!this.i18nInfo.equals(that.i18nInfo))
        return false;
    }

    boolean this_present_authMode = true && this.isSetAuthMode();
    boolean that_present_authMode = true && that.isSetAuthMode();
    if (this_present_authMode || that_present_authMode) {
      if (!(this_present_authMode && that_present_authMode))
        return false;
      if (!this.authMode.equals(that.authMode))
        return false;
    }

    boolean this_present_digestAuthenticationReq = true && this.isSetDigestAuthenticationReq();
    boolean that_present_digestAuthenticationReq = true && that.isSetDigestAuthenticationReq();
    if (this_present_digestAuthenticationReq || that_present_digestAuthenticationReq) {
      if (!(this_present_digestAuthenticationReq && that_present_digestAuthenticationReq))
        return false;
      if (!this.digestAuthenticationReq.equals(that.digestAuthenticationReq))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(CommArgs other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    CommArgs typedOther = (CommArgs)other;

    lastComparison = Boolean.valueOf(isSetTerminalInfo()).compareTo(typedOther.isSetTerminalInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTerminalInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.terminalInfo, typedOther.terminalInfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAppInfo()).compareTo(typedOther.isSetAppInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAppInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.appInfo, typedOther.appInfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUserId()).compareTo(typedOther.isSetUserId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userId, typedOther.userId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetI18nInfo()).compareTo(typedOther.isSetI18nInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetI18nInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.i18nInfo, typedOther.i18nInfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAuthMode()).compareTo(typedOther.isSetAuthMode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAuthMode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.authMode, typedOther.authMode);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDigestAuthenticationReq()).compareTo(typedOther.isSetDigestAuthenticationReq());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDigestAuthenticationReq()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.digestAuthenticationReq, typedOther.digestAuthenticationReq);
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
    StringBuilder sb = new StringBuilder("CommArgs(");
    boolean first = true;

    sb.append("terminalInfo:");
    if (this.terminalInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.terminalInfo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("appInfo:");
    if (this.appInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.appInfo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("userId:");
    if (this.userId == null) {
      sb.append("null");
    } else {
      sb.append(this.userId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("i18nInfo:");
    if (this.i18nInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.i18nInfo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("authMode:");
    if (this.authMode == null) {
      sb.append("null");
    } else {
      sb.append(this.authMode);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("digestAuthenticationReq:");
    if (this.digestAuthenticationReq == null) {
      sb.append("null");
    } else {
      sb.append(this.digestAuthenticationReq);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class CommArgsStandardSchemeFactory implements SchemeFactory {
    public CommArgsStandardScheme getScheme() {
      return new CommArgsStandardScheme();
    }
  }

  private static class CommArgsStandardScheme extends StandardScheme<CommArgs> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, CommArgs struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TERMINAL_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.terminalInfo = new TerminalInfo();
              struct.terminalInfo.read(iprot);
              struct.setTerminalInfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // APP_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.appInfo = new AppInfo();
              struct.appInfo.read(iprot);
              struct.setAppInfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userId = iprot.readString();
              struct.setUserIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // I18N_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.i18nInfo = new I18nInfo();
              struct.i18nInfo.read(iprot);
              struct.setI18nInfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // AUTH_MODE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.authMode = AuthMode.findByValue(iprot.readI32());
              struct.setAuthModeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // DIGEST_AUTHENTICATION_REQ
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.digestAuthenticationReq = new DigestAuthenticationReq();
              struct.digestAuthenticationReq.read(iprot);
              struct.setDigestAuthenticationReqIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, CommArgs struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.terminalInfo != null) {
        oprot.writeFieldBegin(TERMINAL_INFO_FIELD_DESC);
        struct.terminalInfo.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.appInfo != null) {
        oprot.writeFieldBegin(APP_INFO_FIELD_DESC);
        struct.appInfo.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.userId != null) {
        oprot.writeFieldBegin(USER_ID_FIELD_DESC);
        oprot.writeString(struct.userId);
        oprot.writeFieldEnd();
      }
      if (struct.i18nInfo != null) {
        oprot.writeFieldBegin(I18N_INFO_FIELD_DESC);
        struct.i18nInfo.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.authMode != null) {
        oprot.writeFieldBegin(AUTH_MODE_FIELD_DESC);
        oprot.writeI32(struct.authMode.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.digestAuthenticationReq != null) {
        oprot.writeFieldBegin(DIGEST_AUTHENTICATION_REQ_FIELD_DESC);
        struct.digestAuthenticationReq.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CommArgsTupleSchemeFactory implements SchemeFactory {
    public CommArgsTupleScheme getScheme() {
      return new CommArgsTupleScheme();
    }
  }

  private static class CommArgsTupleScheme extends TupleScheme<CommArgs> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, CommArgs struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTerminalInfo()) {
        optionals.set(0);
      }
      if (struct.isSetAppInfo()) {
        optionals.set(1);
      }
      if (struct.isSetUserId()) {
        optionals.set(2);
      }
      if (struct.isSetI18nInfo()) {
        optionals.set(3);
      }
      if (struct.isSetAuthMode()) {
        optionals.set(4);
      }
      if (struct.isSetDigestAuthenticationReq()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetTerminalInfo()) {
        struct.terminalInfo.write(oprot);
      }
      if (struct.isSetAppInfo()) {
        struct.appInfo.write(oprot);
      }
      if (struct.isSetUserId()) {
        oprot.writeString(struct.userId);
      }
      if (struct.isSetI18nInfo()) {
        struct.i18nInfo.write(oprot);
      }
      if (struct.isSetAuthMode()) {
        oprot.writeI32(struct.authMode.getValue());
      }
      if (struct.isSetDigestAuthenticationReq()) {
        struct.digestAuthenticationReq.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, CommArgs struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.terminalInfo = new TerminalInfo();
        struct.terminalInfo.read(iprot);
        struct.setTerminalInfoIsSet(true);
      }
      if (incoming.get(1)) {
        struct.appInfo = new AppInfo();
        struct.appInfo.read(iprot);
        struct.setAppInfoIsSet(true);
      }
      if (incoming.get(2)) {
        struct.userId = iprot.readString();
        struct.setUserIdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.i18nInfo = new I18nInfo();
        struct.i18nInfo.read(iprot);
        struct.setI18nInfoIsSet(true);
      }
      if (incoming.get(4)) {
        struct.authMode = AuthMode.findByValue(iprot.readI32());
        struct.setAuthModeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.digestAuthenticationReq = new DigestAuthenticationReq();
        struct.digestAuthenticationReq.read(iprot);
        struct.setDigestAuthenticationReqIsSet(true);
      }
    }
  }

}

