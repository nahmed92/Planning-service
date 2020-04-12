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

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.planning.service.test.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Plan Repository Integration Test
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
@DatabaseSetup(value = "plan.xml")
public class PlanRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PlanRepository planRepository;

    @Test
    public void ShouldFindPlanById() throws Exception {
        final Optional<Plan> plan = planRepository.findById(99);
        assertThat(plan.get().getId(), is(99));
        assertThat(plan.get().getName(), is("PlanA"));
        assertThat(plan.get().getStatus(), is(PlanStatus.INPROCESS));

    }

    @Test
    public void ShouldUpdatePlan() throws Exception {
        final Optional<Plan> plan = planRepository.findById(99);
        plan.get().setName("PlanD");
        plan.get().setStatus(PlanStatus.COMPLETE);
        plan.get().setDescription("updated description");
        final Plan updated_plan = planRepository.save(plan.get());
        assertThat(updated_plan.getName(), is("PlanD"));
        assertThat(updated_plan.getProducts().size(), is(1));
        assertThat(updated_plan.getDescription(), is("updated description"));
        assertThat(updated_plan.getStatus(), is(PlanStatus.COMPLETE));
        assertThat(updated_plan.getParentPlanId(), is(nullValue()));

    }

    @Test
    public void ShouldDeletePlan() throws Exception {
        planRepository.deleteById(99);
        final Optional<Plan> plan = planRepository.findById(1);
        assertThat(plan.isPresent(), is(false));

    }

    @Test
    public void ShouldSavePlan() throws Exception {
        final PlanProductTask task1 = new PlanProductTask(Type.EXTRACTED, "roleId",
                TaskStatus.COMPLETE, "abc");
        final PlanProductTask task2 = new PlanProductTask(Type.EXTRACTED, "roleId2",
                TaskStatus.IN_PROGRESS, "abc");
        final List<PlanProductTask> tasks = Lists.newArrayList();
        tasks.add(task1);
        tasks.add(task2);
        final PlanProduct product = new PlanProduct(tasks);
        final List<PlanProduct> products = Lists.newArrayList();
        products.add(product);

        final Plan plan = planRepository.save(
                new Plan("PlanD", products, "PlanA description", null, PlanStatus.TODO));
        assertThat(plan.getName(), is("PlanD"));
        assertThat(plan.getProducts().get(0).getTasks().get(0).getRoleId(), is("roleId"));
        assertThat(plan.getProducts().get(0).getTasks().get(0).getStatus(),
                is(TaskStatus.COMPLETE));
        assertThat(plan.getProducts().get(0).getTasks().get(1).getRoleId(),
                is("roleId2"));
        assertThat(plan.getProducts().get(0).getTasks().get(1).getStatus(),
                is(TaskStatus.IN_PROGRESS));
        assertThat(plan.getDescription(), is("PlanA description"));
        assertThat(plan.getStatus(), is(PlanStatus.TODO));
        assertThat(plan.getParentPlanId(), is(nullValue()));
    }

}
