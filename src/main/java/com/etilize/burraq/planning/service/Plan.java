/*
 * #region
 * planning-service
 * %%
 * Copyright (C) 2020 Etilize
 * %%
 * NOTICE: All information contained herein is, and remains the property of ETILIZE.
 * The intellectual and technical concepts contained herein are proprietary to
 * ETILIZE and may be covered by U.S. and Foreign Patents, patents in process, and
 * are protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from ETILIZE. Access to the source code contained herein
 * is hereby forbidden to anyone except current ETILIZE employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements
 * explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication
 * or disclosure of this source code, which includes information that is confidential
 * and/or proprietary, and is a trade secret, of ETILIZE. ANY REPRODUCTION, MODIFICATION,
 * DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS
 * SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF ETILIZE IS STRICTLY PROHIBITED,
 * AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT
 * OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR
 * IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * #endregion
 */

package com.etilize.burraq.planning.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Plan that hold related products and  plan related information
 *
 * @author Nasir Ahmed
 *
 * @since 1.0
 *
 */
@Entity
@Table(name = "plan")
public class Plan implements Serializable {

    private static final long serialVersionUID = 6355991843182082976L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    @NotBlank(message = "name is required.")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private List<PlanProduct> products;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "parent_plan_id")
    private Integer parentPlanId;

    @Column(name = "status")
    private PlanStatus status;

    /**
     * default constructor
     */
    public Plan() {
    }

    /**
     * Constructor {@link Plan}
     * @param name plan name
     * @param products {@link List<Product>}
     * @param description plan description
     * @param parentPlanId plan parentPlanId
     * @param status plan status
     */
    public Plan(final String name, final List<PlanProduct> products,
            final String description, final Integer parentPlanId,
            final PlanStatus status) {
        this.name = name;
        this.products = products;
        this.description = description;
        this.parentPlanId = parentPlanId;
        this.status = status;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the products
     */
    public List<PlanProduct> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(final List<PlanProduct> products) {
        this.products = products;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the parentPlanId
     */
    public Integer getParentPlanId() {
        return parentPlanId;
    }

    /**
     * @param parentPlanId the parentPlanId to set
     */
    public void setParentPlanId(final Integer parentPlanId) {
        this.parentPlanId = parentPlanId;
    }

    /**
     * @return the status
     */
    public PlanStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final PlanStatus status) {
        this.status = status;
    }

    @Override
    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Plan)) {
            return false;
        }
        final Plan plan = (Plan) object;
        return new EqualsBuilder() //
                .append(id, plan.getId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getId()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Id", getId()) //
                .append("Name", getName()) //
                .append("products", getProducts()) //
                .append("Description", getDescription()) //
                .append("ParentPlanId", getParentPlanId()) //
                .append("Status", getStatus()) //
                .toString();
    }
}
