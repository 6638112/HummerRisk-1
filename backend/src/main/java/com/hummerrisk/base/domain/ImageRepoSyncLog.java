package com.hummerrisk.base.domain;

import java.io.Serializable;

public class ImageRepoSyncLog implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.repo_id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private String repoId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.create_time
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.operator
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private String operator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.sum
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private Long sum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image_repo_sync_log.result
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private Boolean result;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table image_repo_sync_log
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.id
     *
     * @return the value of image_repo_sync_log.id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.id
     *
     * @param id the value for image_repo_sync_log.id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.repo_id
     *
     * @return the value of image_repo_sync_log.repo_id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public String getRepoId() {
        return repoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.repo_id
     *
     * @param repoId the value for image_repo_sync_log.repo_id
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setRepoId(String repoId) {
        this.repoId = repoId == null ? null : repoId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.create_time
     *
     * @return the value of image_repo_sync_log.create_time
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.create_time
     *
     * @param createTime the value for image_repo_sync_log.create_time
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.operator
     *
     * @return the value of image_repo_sync_log.operator
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.operator
     *
     * @param operator the value for image_repo_sync_log.operator
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.sum
     *
     * @return the value of image_repo_sync_log.sum
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public Long getSum() {
        return sum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.sum
     *
     * @param sum the value for image_repo_sync_log.sum
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setSum(Long sum) {
        this.sum = sum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image_repo_sync_log.result
     *
     * @return the value of image_repo_sync_log.result
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image_repo_sync_log.result
     *
     * @param result the value for image_repo_sync_log.result
     *
     * @mbg.generated Sun Jan 22 22:55:01 CST 2023
     */
    public void setResult(Boolean result) {
        this.result = result;
    }
}