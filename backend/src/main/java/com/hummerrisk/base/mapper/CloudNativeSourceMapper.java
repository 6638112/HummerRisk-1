package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.CloudNativeSource;
import com.hummerrisk.base.domain.CloudNativeSourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CloudNativeSourceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    long countByExample(CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int deleteByExample(CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int insert(CloudNativeSource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int insertSelective(CloudNativeSource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    List<CloudNativeSource> selectByExampleWithBLOBs(CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    List<CloudNativeSource> selectByExample(CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    CloudNativeSource selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByExampleSelective(@Param("record") CloudNativeSource record, @Param("example") CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") CloudNativeSource record, @Param("example") CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByExample(@Param("record") CloudNativeSource record, @Param("example") CloudNativeSourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByPrimaryKeySelective(CloudNativeSource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(CloudNativeSource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_source
     *
     * @mbg.generated Mon Aug 15 21:20:10 CST 2022
     */
    int updateByPrimaryKey(CloudNativeSource record);
}