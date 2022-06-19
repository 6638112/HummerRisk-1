package com.hummerrisk.base.domain;

import java.io.Serializable;

public class ResourceWithBLOBs extends Resource implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.custodian_run_log
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private String custodianRunLog;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.metadata
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private String metadata;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.resources
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private String resources;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.resource_command
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private String resourceCommand;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.resource_command_action
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private String resourceCommandAction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table resource
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.custodian_run_log
     *
     * @return the value of resource.custodian_run_log
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public String getCustodianRunLog() {
        return custodianRunLog;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.custodian_run_log
     *
     * @param custodianRunLog the value for resource.custodian_run_log
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public void setCustodianRunLog(String custodianRunLog) {
        this.custodianRunLog = custodianRunLog == null ? null : custodianRunLog.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.metadata
     *
     * @return the value of resource.metadata
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.metadata
     *
     * @param metadata the value for resource.metadata
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata == null ? null : metadata.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.resources
     *
     * @return the value of resource.resources
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public String getResources() {
        return resources;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.resources
     *
     * @param resources the value for resource.resources
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public void setResources(String resources) {
        this.resources = resources == null ? null : resources.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.resource_command
     *
     * @return the value of resource.resource_command
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public String getResourceCommand() {
        return resourceCommand;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.resource_command
     *
     * @param resourceCommand the value for resource.resource_command
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public void setResourceCommand(String resourceCommand) {
        this.resourceCommand = resourceCommand == null ? null : resourceCommand.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.resource_command_action
     *
     * @return the value of resource.resource_command_action
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public String getResourceCommandAction() {
        return resourceCommandAction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.resource_command_action
     *
     * @param resourceCommandAction the value for resource.resource_command_action
     *
     * @mbg.generated Mon Jun 20 01:10:06 CST 2022
     */
    public void setResourceCommandAction(String resourceCommandAction) {
        this.resourceCommandAction = resourceCommandAction == null ? null : resourceCommandAction.trim();
    }
}