package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.CloudEvent;
import com.hummerrisk.base.domain.CloudEventExample;
import com.hummerrisk.base.domain.CloudEventWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CloudEventMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    long countByExample(CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int deleteByExample(CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int insert(CloudEventWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int insertSelective(CloudEventWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    List<CloudEventWithBLOBs> selectByExampleWithBLOBs(CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    List<CloudEvent> selectByExample(CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    CloudEventWithBLOBs selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByExampleSelective(@Param("record") CloudEventWithBLOBs record, @Param("example") CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") CloudEventWithBLOBs record, @Param("example") CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByExample(@Param("record") CloudEvent record, @Param("example") CloudEventExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByPrimaryKeySelective(CloudEventWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(CloudEventWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_event
     *
     * @mbg.generated Sun Nov 20 15:31:15 CST 2022
     */
    int updateByPrimaryKey(CloudEvent record);
}
