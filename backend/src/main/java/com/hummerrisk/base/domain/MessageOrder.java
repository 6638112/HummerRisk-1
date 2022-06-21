package com.hummerrisk.base.domain;

import java.io.Serializable;

public class MessageOrder implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.account_id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private String accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.account_name
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private String accountName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.status
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.create_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.send_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private Long sendTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message_order.scan_type
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private String scanType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table message_order
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.id
     *
     * @return the value of message_order.id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.id
     *
     * @param id the value for message_order.id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.account_id
     *
     * @return the value of message_order.account_id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.account_id
     *
     * @param accountId the value for message_order.account_id
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.account_name
     *
     * @return the value of message_order.account_name
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.account_name
     *
     * @param accountName the value for message_order.account_name
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.status
     *
     * @return the value of message_order.status
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.status
     *
     * @param status the value for message_order.status
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.create_time
     *
     * @return the value of message_order.create_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.create_time
     *
     * @param createTime the value for message_order.create_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.send_time
     *
     * @return the value of message_order.send_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public Long getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.send_time
     *
     * @param sendTime the value for message_order.send_time
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message_order.scan_type
     *
     * @return the value of message_order.scan_type
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public String getScanType() {
        return scanType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message_order.scan_type
     *
     * @param scanType the value for message_order.scan_type
     *
     * @mbg.generated Tue Jun 21 12:22:36 CST 2022
     */
    public void setScanType(String scanType) {
        this.scanType = scanType == null ? null : scanType.trim();
    }
}