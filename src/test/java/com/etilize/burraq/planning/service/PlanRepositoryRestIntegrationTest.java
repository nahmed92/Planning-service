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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.etilize.burraq.planning.service.test.AbstractRestIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Plan Repository Rest Integration Test
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
@DatabaseSetup(value = "plan.xml")
public class PlanRepositoryRestIntegrationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldFindAllPlan() throws Exception {
        mockMvc.perform(get("/plans")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.plans[*]", hasSize(1))) //
                .andExpect(jsonPath("$._embedded.plans[0].name", is("PlanA"))) //
                .andExpect(jsonPath("$._embedded.plans[0].products[*]", hasSize(1))) //
                .andExpect(jsonPath("$._embedded.plans[0].products[0].tasks[*]", //
                        hasSize(1))) //
                .andExpect(jsonPath("$._embedded.plans[0].products[0].tasks[0].type", //
                        is("EXTRACTED"))) //
                .andExpect(jsonPath("$._embedded.plans[0].products[0].tasks[0].status", //
                        is("IN_PROGRESS"))) //
                .andExpect(jsonPath("$._embedded.plans[0].products[0].tasks[0].assignee", //
                        is("abc"))) //
                .andExpect(jsonPath("$._embedded.plans[0].description",
                        is("PlanA description"))) //
                .andExpect(jsonPath("$._embedded.plans[0].status", is("INPROCESS"))) //
                .andExpect(jsonPath("$._links.self.href") //
                        .value(endsWith("/plans{?page,size,sort}"))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(1))) //
                .andExpect(jsonPath("$.page.totalPages", is(1))) //
                .andExpect(jsonPath("$.page.number", is(0)));

    }

    @Test
    public void shouldFindPlanById() throws Exception {
        mockMvc.perform(get("/plans/99")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.name", is("PlanA"))) //
                .andExpect(jsonPath("$.products[*]", hasSize(1))) //
                .andExpect(jsonPath("$.products[0].tasks[*]", //
                        hasSize(1))) //
                .andExpect(jsonPath("$.products[0].tasks[0].type", //
                        is("EXTRACTED"))) //
                .andExpect(jsonPath("$.products[0].tasks[0].status", //
                        is("IN_PROGRESS"))) //
                .andExpect(jsonPath("$.products[0].tasks[0].assignee", //
                        is("abc"))) //
                .andExpect(jsonPath("$.description", is("PlanA description"))) //
                .andExpect(jsonPath("$.status", is("INPROCESS"))) //
                .andExpect(jsonPath("$._links.self.href") //
                        .value(endsWith("/plans/99")));

    }

    @Test
    public void shouldCreateNewPlan() throws Exception {
        final PlanProductTask task1 = new PlanProductTask(Type.EXTRACTED, "roleId",
                TaskStatus.COMPLETE, "content-extractor@etilizepak.com");
        final PlanProductTask task2 = new PlanProductTask(Type.QC, "roleId2",
                TaskStatus.IN_PROGRESS, "content-qa@etilizepak.com");
        final List<PlanProductTask> tasks = Lists.newArrayList();
        tasks.add(task1);
        tasks.add(task2);
        final PlanProduct product = new PlanProduct(tasks);
        final List<PlanProduct> products = Lists.newArrayList();
        products.add(product);
        mockMvc.perform(post("/plans") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(new Plan("PlanD", products,
                        "PlanA description", null, PlanStatus.TODO)))) //
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldupdateNewPlan() throws Exception {
        final PlanProductTask task1 = new PlanProductTask(Type.EXTRACTED, "roleId",
                TaskStatus.COMPLETE, "content-extractor@etilizepak.com");
        final List<PlanProductTask> tasks = Lists.newArrayList();
        tasks.add(task1);
        final PlanProduct product = new PlanProduct(tasks);
        final List<PlanProduct> products = Lists.newArrayList();
        products.add(product);
        mockMvc.perform(put("/plans/99") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(new Plan("Updated_Plan", products,
                        "updated PlanA description", null, PlanStatus.TODO)))) //
                .andExpect(status().isNoContent());
    }

}
